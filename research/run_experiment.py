"""Execute frozen requests through a locally configured adapter; never retry implicitly."""
from __future__ import annotations

import argparse
import fcntl
import json
import math
import os
import re
import time
from datetime import datetime, timezone
from pathlib import Path

from research.import_evidence import ROOT, canonical, digest
from research.model_adapter import AdapterFailure, command_from_env, invoke


def timestamp() -> str:
    return datetime.now(timezone.utc).isoformat()


def frozen_manifest(path: Path) -> dict:
    manifest = json.loads(path.read_text())
    check = dict(manifest)
    fingerprint = check.pop('fingerprint')
    if digest(canonical(check)) != fingerprint: raise ValueError('Manifest fingerprint mismatch')
    for name, sha in manifest['sourceHashes'].items():
        source = (ROOT / name).resolve()
        if not source.is_relative_to(ROOT) or digest(source.read_bytes()) != sha:
            raise ValueError('Frozen source provenance mismatch')
    conditions = {c['id']: c for c in manifest['conditions']}
    if len(conditions) != len(manifest['conditions']): raise ValueError('Duplicate condition')
    seen = set()
    for row in manifest['schedule']:
        if not re.fullmatch(r'[a-zA-Z0-9_-]+', row['runId']) or row['runId'] in seen:
            raise ValueError('Unsafe or duplicate run ID')
        seen.add(row['runId'])
        if row['condition'] not in conditions: raise ValueError('Unknown condition')
    for condition in conditions.values():
        prompt = (path.parent / condition['promptFile']).resolve()
        if not prompt.is_relative_to(path.parent.resolve()) or digest(prompt.read_bytes()) != condition['promptSha256']:
            raise ValueError('Frozen prompt hash mismatch')
    return manifest


def write_atomic(path: Path, value: dict):
    temporary = path.with_suffix('.tmp')
    with temporary.open('wb') as handle:
        handle.write(canonical(value))
        handle.flush()
        os.fsync(handle.fileno())
    temporary.replace(path)


def execute(manifest_path: Path, output: Path, command: list[str], *, limit=1, stage=None, timeout=600.0) -> list[dict]:
    if limit < 1 or not math.isfinite(timeout) or timeout <= 0: raise ValueError('Positive limit and timeout required')
    manifest = frozen_manifest(manifest_path)
    output.mkdir(parents=True, exist_ok=True)
    completed = []
    with (output / '.runner.lock').open('a') as lock:
        try: fcntl.flock(lock, fcntl.LOCK_EX | fcntl.LOCK_NB)
        except BlockingIOError: raise ValueError('Another runner owns this output directory') from None
        identity = output / 'manifest-fingerprint.txt'
        if identity.exists() and identity.read_text().strip() != manifest['fingerprint']:
            raise ValueError('Output directory belongs to a different manifest')
        identity.write_text(manifest['fingerprint'] + '\n')
        conditions = {c['id']: c for c in manifest['conditions']}
        for row in manifest['schedule']:
            if stage and row['stage'] != stage: continue
            path = output / f"{row['runId']}.json"
            # Any durable record, even 'started' or failed, prevents a duplicate call.
            if path.exists(): continue
            condition = conditions[row['condition']]
            request = {'protocol_version': 1, 'request_id': row['runId'], 'model': manifest['model'],
                       'messages': [{'role': 'user', 'content': (manifest_path.parent / condition['promptFile']).read_text()}],
                       'settings': {'reasoning_effort': manifest['reasoning'], 'temperature': manifest['temperature'],
                                    'max_output_tokens': manifest['maxOutputTokens']}}
            observation = {'schemaVersion': 1, **row, 'status': 'started', 'startedAt': timestamp(),
                           'manifestFingerprint': manifest['fingerprint'], 'promptSha256': condition['promptSha256'],
                           'request': request, 'requestSha256': digest(canonical(request)),
                           'runnerHashes': {str(p.relative_to(ROOT)): digest(p.read_bytes()) for p in
                                            (Path(__file__).resolve(), ROOT / 'research/model_adapter.py')},
                           'evaluation': None}
            write_atomic(path, observation)
            start = time.monotonic()
            try:
                response = invoke(command, request, timeout)
                mismatches = []
                if response['model'] != request['model']: mismatches.append('served_model')
                if response['request_id'] != request['request_id']: mismatches.append('request_id')
                if response['settings'] != request['settings']: mismatches.append('effective_settings')
                observation.update(status='completed' if not mismatches else 'settings_unverified',
                                   response=response, mismatches=mismatches)
            except AdapterFailure as error:
                observation.update(status='adapter_error', errorCategory=error.category)
            observation.update(finishedAt=timestamp(), elapsedSeconds=time.monotonic() - start)
            write_atomic(path, observation)
            completed.append(observation)
            print(f"{row['runId']}: {observation['status']}", flush=True)
            if len(completed) >= limit: break
    return completed


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--manifest', type=Path, default=ROOT / 'research/experiments/luna-highscore-v1/manifest.json')
    parser.add_argument('--output', type=Path, default=ROOT / '.local/runs/luna-highscore-v1')
    parser.add_argument('--execute', action='store_true', help='Submit requests; otherwise validate and show the plan')
    parser.add_argument('--limit', type=int, default=1, help='Maximum new requests for this invocation')
    parser.add_argument('--stage', choices=['bridge', 'ablation'])
    parser.add_argument('--timeout', type=float, default=600)
    args = parser.parse_args()
    if args.execute:
        execute(args.manifest, args.output, command_from_env(), limit=args.limit, stage=args.stage, timeout=args.timeout)
    else:
        manifest = frozen_manifest(args.manifest)
        schedule = [r for r in manifest['schedule'] if not args.stage or r['stage'] == args.stage]
        recorded = sum((args.output / f"{r['runId']}.json").exists() for r in schedule)
        print(f"Validated {manifest['id']}: {len(schedule)} scheduled, {recorded} recorded, {len(schedule)-recorded} remaining. No requests submitted.")


if __name__ == '__main__': main()
