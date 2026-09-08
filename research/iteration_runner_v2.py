"""Fixed perspective schedules with corrected evaluation and repository/task-only context.

The I01 edit protocol and trajectory implementation are reused unchanged. A fresh
interpreter installs the v3 evaluator before executing each new trajectory.
"""
from __future__ import annotations

import argparse
from concurrent.futures import ThreadPoolExecutor
import fcntl
import json
from pathlib import Path
import random
import re
import subprocess
import sys
from unittest.mock import patch

from research import iteration_runner as delivery
from research.evaluate_integrated import evaluate_response
from research.generate_security_context import STRATEGIES
from research.import_evidence import ROOT, canonical, digest
from research.model_adapter import command_from_env
from research.run_experiment import timestamp
from research.security_followup import acquisition_task


def prepare(identifier: str, cells: list[str], repetitions: int, context_directory: Path | None = None):
    if not re.fullmatch(r'[a-z0-9][a-z0-9_-]+', identifier) or not cells or not 1 <= repetitions <= 30: raise ValueError('Invalid iteration plan')
    directory = ROOT / '.local/iterations' / identifier; directory.mkdir(parents=True, exist_ok=True)
    context_directory = context_directory or directory
    baseline = json.loads((delivery.BASE / 'manifest.json').read_text())
    acquisitions = json.loads((context_directory / 'acquisitions.json').read_text())
    for item in acquisitions:
        path = ROOT / '.local/context-generation' / item['id'] / 'record.json'
        record = json.loads(path.read_text())
        item.update(task=record['task'], promptInsertSha256=record.get('promptInsertSha256'), recordSha256=digest(path.read_bytes()))
    sources = {str(p.relative_to(ROOT)): digest(p.read_bytes()) for p in [Path(__file__),
        *[ROOT / 'research' / name for name in ('iteration_runner.py', 'feature_delivery.py', 'model_adapter.py',
          'evaluate_response.py', 'evaluate_security.py', 'evaluate_isolated.py', 'evaluate_integrated.py',
          'generate_context.py', 'generate_security_context.py', 'context_repository.py', 'iteration_contexts_v2.py',
          'security_followup.py', 'paper_matrix.py')], delivery.BASE / 'manifest.json']}
    calibration_path = ROOT / '.local/calibration/integrated-security-reference/report.json'
    calibration = json.loads(calibration_path.read_text())
    if calibration['protocol'] != 'highscore-response-v3-integrated-security' or not calibration['functionalSuccess']:
        raise ValueError('The corrected evaluator must reproduce its reference before collection')
    sources.update(calibration['inputHashes'])
    for name, sha in sources.items():
        if digest((ROOT / name).read_bytes()) != sha: raise ValueError(f'Calibrated input changed: {name}')
    conditions = []
    for cell in cells:
        parent = next(c for c in baseline['conditions'] if c['id'] == cell)
        original = (delivery.BASE / parent['promptFile']).read_bytes()
        sources[str((delivery.BASE / parent['promptFile']).relative_to(ROOT))] = digest(original)
        attachments = original.decode().split('\n\n--- BEGIN ATTACHED', 1)[1]
        prompt = (acquisition_task(parent['strategy']) + '\n\n--- BEGIN ATTACHED' + attachments).replace('\r\n', '\n')
        for strategy in ['none', *STRATEGIES]:
            text, acquisition = prompt, None
            if strategy != 'none':
                acquisition = next(a for a in acquisitions if a['method'] == parent['strategy'] and a['strategy'] == strategy)
                record_path = ROOT / '.local/context-generation' / acquisition['id'] / 'record.json'
                record = json.loads(record_path.read_text())
                if record['status'] not in ('completed', 'settings_unverified') or not record.get('output'): raise ValueError('A complete fresh context is required before freezing the code schedule')
                if record['model'] != delivery.MODEL: raise ValueError('Context model mismatch')
                path = context_directory / 'contexts' / f"{parent['strategy'].lower()}-{strategy}.txt"
                insert = path.read_bytes()
                if digest(insert) != record['promptInsertSha256']: raise ValueError('Context insert lineage mismatch')
                text += '\n\n' + insert.decode()
                sources[str(path.relative_to(ROOT))] = digest(insert)
                for artifact in (record_path, record_path.parent / 'snapshot.json'): sources[str(artifact.relative_to(ROOT))] = digest(artifact.read_bytes())
            sha = digest(text.encode()); path = directory / 'prompts' / f'{sha}.txt'; path.parent.mkdir(exist_ok=True)
            if path.exists() and path.read_bytes() != text.encode(): raise ValueError('Prompt changed')
            if not path.exists(): path.write_text(text)
            conditions.append({**parent, 'id': f'{cell}__{strategy}', 'parentCondition': cell, 'securityStrategy': strategy, 'repetitions': repetitions,
                'contextAcquisitionId': acquisition['id'] if acquisition else None, 'promptFile': str(path.relative_to(directory)),
                'promptSha256': sha, 'promptBytes': len(text.encode()), 'promptCharacters': len(text)})
    schedule, rng = [], random.Random(identifier)
    for repetition in range(1, repetitions + 1):
        block = [{'runId': f'{identifier}__{c["id"]}__r{repetition}', 'condition': c['id'], 'repetition': repetition} for c in conditions]
        rng.shuffle(block); schedule.extend(block)
    plan = {'id': identifier, 'phase': 'security_followup', 'protocol': 'structured-delivery-functional-feedback-v2',
        'evaluationProtocol': 'highscore-response-v3-integrated-security', 'createdAt': timestamp(), 'model': delivery.MODEL,
        'settings': delivery.SETTINGS, 'maxSubmissions': 3, 'system': delivery.SYSTEM, 'systemSha256': digest(delivery.SYSTEM.encode()),
        'conditions': conditions, 'schedule': schedule, 'acquisitions': acquisitions, 'sourceHashes': sources,
        'calibrationReportSha256': digest(calibration_path.read_bytes()),
        'analysis': {'primary': 'Full functional success within three submissions / all planned trajectories. Report first-submission success separately.',
            'security': 'Report every issue check’s failures, evaluated count and unresolved count. Positive valid-record round trip is separate. Missing coverage cannot establish improvement.',
            'comparison': 'Fresh no-security control within each method/paper-context, identical delivery, feedback, evaluation and submission budget. Only context insert varies between arms.',
            'stopping': 'Complete this fixed schedule regardless of positive, negative or null outcomes; no replacing failures or outcome-based early stopping.',
            'limits': 'Exploratory revision informed by I01. Fresh repository/task acquisitions receive no prior code outputs, audits, evaluation feedback, test code or fixture thresholds. Context strategies encode general security perspectives chosen by the researcher. One acquired context per method/strategy is shared within this iteration, so trajectories are not independent context replications. Runtime menu rendering is not covered by the original 16 checks. Returned model identity is checked; missing provider attestation of effective settings remains explicit.'}}
    path = directory / 'manifest.json'
    if path.exists(): plan['createdAt'] = json.loads(path.read_text())['createdAt']
    plan['fingerprint'] = digest(canonical(plan)); delivery.freeze(path, plan)
    return plan


def trajectory(manifest: Path, run_id: str):
    with patch.object(delivery, 'evaluate_response', side_effect=evaluate_response):
        delivery.trajectory(manifest, run_id, command_from_env())


def run(manifest: Path, workers: int):
    plan = delivery.validate(manifest)
    with (manifest.parent / '.iteration.lock').open('a') as lock:
        fcntl.flock(lock, fcntl.LOCK_EX | fcntl.LOCK_NB)
        pending = [row for row in plan['schedule'] if not (manifest.parent / 'runs' / row['runId']).exists()]
        def one(row):
            result = subprocess.run([sys.executable, '-u', '-m', 'research.iteration_runner_v2', '--manifest', str(manifest), '--run-id', row['runId'], '--execute'], cwd=ROOT, capture_output=True, text=True)
            (manifest.parent / f"{row['runId']}.log").write_text(result.stdout + result.stderr)
            print(result.stdout.strip() or f"{row['runId']}: process exit {result.returncode}", flush=True)
        with ThreadPoolExecutor(max_workers=workers) as pool: list(pool.map(one, pending))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--manifest', type=Path, required=True)
    parser.add_argument('--run-id')
    parser.add_argument('--workers', type=int, choices=range(1, 5), default=3)
    parser.add_argument('--execute', action='store_true')
    args = parser.parse_args()
    if args.execute and args.run_id: trajectory(args.manifest, args.run_id)
    elif args.execute: run(args.manifest, args.workers)
    else:
        plan = delivery.validate(args.manifest)
        print(f"{plan['id']}: {len(plan['schedule'])} trajectories; at most {plan['maxSubmissions'] * len(plan['schedule'])} model submissions")
