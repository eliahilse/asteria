"""Run a frozen study with bounded concurrency and separate, durable evaluations."""
from __future__ import annotations

import argparse
import fcntl
import json
import subprocess
import sys
import time
from concurrent.futures import ThreadPoolExecutor, as_completed
from pathlib import Path

from research.import_evidence import ROOT, canonical, digest
from research.model_adapter import AdapterFailure, command_from_env, invoke
from research.run_experiment import frozen_manifest, timestamp, write_atomic


def evaluable(observation: dict, plan: dict) -> bool:
    response = observation.get('response') or {}
    expected = observation.get('request') or {}
    return (observation.get('status') in ('completed', 'settings_unverified') and
            all(response.get(k) == expected.get(k) for k in ('model', 'request_id')) and
            (response.get('settings') == expected.get('settings') or
             (plan.get('allowUnverifiedSettings') and response.get('settings') is None)))


def submit(plan: dict, manifest: Path, row: dict, directory: Path, command: list[str], timeout=600) -> dict:
    path = directory / f"{row['runId']}.json"
    if path.exists(): return json.loads(path.read_text())
    condition = next(c for c in plan['conditions'] if c['id'] == row['condition'])
    request = {'protocol_version': 1, 'request_id': row['runId'], 'model': plan['model'],
               'messages': [{'role': 'user', 'content': (manifest.parent / condition['promptFile']).read_text()}],
               'settings': {'reasoning_effort': plan['reasoning'], 'temperature': plan['temperature'], 'max_output_tokens': plan['maxOutputTokens']}}
    record = {'schemaVersion': 1, **row, 'status': 'started', 'startedAt': timestamp(),
              'manifestFingerprint': plan['fingerprint'], 'promptSha256': condition['promptSha256'],
              'request': request, 'requestSha256': digest(canonical(request)),
              'runnerHashes': {str(p.relative_to(ROOT)): digest(p.read_bytes()) for p in [Path(__file__).resolve(), ROOT / 'research/model_adapter.py']}}
    write_atomic(path, record)
    start = time.monotonic()
    try:
        response = invoke(command, request, timeout)
        mismatches = [k for k in ('model', 'request_id', 'settings') if response.get(k) != request[k]]
        status = 'identity_mismatch' if any(k != 'settings' for k in mismatches) else ('settings_unverified' if mismatches else 'completed')
        record.update(status=status, response=response, mismatches=mismatches)
    except AdapterFailure as error:
        record.update(status='adapter_error', errorCategory=error.category)
    except BaseException:
        record.update(status='interrupted', errorCategory='transport_outcome_unknown')
        raise
    finally:
        record.update(finishedAt=timestamp(), elapsedSeconds=time.monotonic() - start)
        write_atomic(path, record)
    return record


def run(manifest: Path, output: Path, command: list[str], *, workers=4, limit=None):
    plan = frozen_manifest(manifest)
    if not 1 <= workers <= 8: raise ValueError('Use 1–8 workers')
    if limit is not None and limit < 1: raise ValueError('Positive submission limit required')
    output.mkdir(parents=True, exist_ok=True)
    runs, evaluations = output / 'runs', output / 'evaluations'
    runs.mkdir(exist_ok=True); evaluations.mkdir(exist_ok=True)
    with (output / '.study.lock').open('a') as lock:
        try: fcntl.flock(lock, fcntl.LOCK_EX | fcntl.LOCK_NB)
        except BlockingIOError: raise ValueError('Study is already running') from None
        identity = output / 'manifest-fingerprint.txt'
        if identity.exists() and identity.read_text().strip() != plan['fingerprint']: raise ValueError('Output belongs to another study')
        identity.write_text(plan['fingerprint'] + '\n')
        jobs, new = [], 0
        for row in plan['schedule']:
            existing = runs / f"{row['runId']}.json"
            if existing.exists():
                if evaluable(json.loads(existing.read_text()), plan) and not (evaluations / row['runId']).exists(): jobs.append(row)
            elif limit is None or new < limit:
                jobs.append(row); new += 1
        def one(row):
            record = submit(plan, manifest, row, runs, command)
            if evaluable(record, plan) and not (evaluations / row['runId']).exists():
                # Separate interpreter: the original integration harness has mutable module globals.
                result = subprocess.run([sys.executable, '-m', 'research.evaluate_response', '--observation', str(runs / f"{row['runId']}.json"),
                                         '--manifest', str(manifest), '--output', str(evaluations / row['runId'])],
                                        cwd=ROOT, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
                (output / f"{row['runId']}-evaluation.log").write_text(result.stdout + result.stderr)
            report_path = evaluations / row['runId'] / 'report.json'
            report = json.loads(report_path.read_text()) if report_path.exists() else {}
            print(f"{row['runId']}: {record['status']}; functional={report.get('functionalSuccess')}; security="
                  f"{sum(c['status']=='pass' for c in (report.get('security') or {}).get('checks',[]))}/11", flush=True)
        with ThreadPoolExecutor(max_workers=workers) as pool:
            futures = [pool.submit(one, row) for row in jobs]
            for future in as_completed(futures): future.result()


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--manifest', type=Path, required=True)
    parser.add_argument('--output', type=Path)
    parser.add_argument('--workers', type=int, default=4)
    parser.add_argument('--limit', type=int)
    parser.add_argument('--execute', action='store_true')
    args = parser.parse_args()
    plan = frozen_manifest(args.manifest)
    output = args.output or ROOT / '.local/experiments' / plan['id']
    if args.execute: run(args.manifest.resolve(), output.resolve(), command_from_env(), workers=args.workers, limit=args.limit)
    else: print(f"Validated {plan['id']}: {len(plan['conditions'])} conditions, {len(plan['schedule'])} attempts. No submission.")
