"""Versioned multi-submission experiments; functional feedback only, all outcomes retained."""
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

from research.feature_delivery import MODEL, SETTINGS, TARGETS, TOOL, PROTOCOL, apply_changes, original_sources
from research.import_evidence import ROOT, canonical, digest
from research.model_adapter import AdapterFailure, command_from_env, invoke
from research.run_experiment import timestamp, write_atomic
from research.security_followup import acquisition_task
from research.evaluate_response import evaluate_response

SYSTEM = PROTOCOL.replace('No security context or security-test feedback is supplied in this delivery calibration.',
    'Security context may be appended to the task. Security-test feedback is never supplied.') + '''
Use the existing game framework and rendering/input conventions. Implement menu navigation as well as recording.
Include actual edits to ALL THREE existing target files: ApoMarioLevel.java, ApoMarioPanel.java, ApoMarioMenu.java.
The menu must call or render the highscore view; an unused new view/getter is insufficient.
Complete modified files are reconstructed by the harness. Deliver edits via the tool, not code fences or prose.
'''
BASE = ROOT / 'research/studies/highscore-paper-luna-v2'


def freeze(path: Path, value):
    raw = canonical(value)
    if path.exists():
        if path.read_bytes() != raw: raise ValueError(f'Frozen artifact differs: {path.name}')
    else:
        path.parent.mkdir(parents=True, exist_ok=True)
        with path.open('xb') as handle: handle.write(raw)


def prepare(identifier: str, cells: list[str], repetitions: int, context_directory: Path | None = None) -> dict:
    if not re.fullmatch(r'[a-z0-9][a-z0-9_-]+', identifier) or not cells or not 1 <= repetitions <= 30: raise ValueError('Invalid iteration plan')
    directory = ROOT / '.local/iterations' / identifier
    directory.mkdir(parents=True, exist_ok=True)
    context_directory = context_directory or directory
    baseline = json.loads((BASE / 'manifest.json').read_text())
    acquisitions = json.loads((context_directory / 'acquisitions.json').read_text())
    sources = {str(p.relative_to(ROOT)): digest(p.read_bytes()) for p in [Path(__file__), ROOT / 'research/feature_delivery.py', ROOT / 'research/model_adapter.py',
               ROOT / 'research/evaluate_response.py', ROOT / 'research/evaluate_security.py', ROOT / 'research/iteration_contexts.py', BASE / 'manifest.json']}
    conditions = []
    for cell in cells:
        parent = next(c for c in baseline['conditions'] if c['id'] == cell)
        original = (BASE / parent['promptFile']).read_bytes()
        sources[str((BASE / parent['promptFile']).relative_to(ROOT))] = digest(original)
        attachments = original.decode().split('\n\n--- BEGIN ATTACHED', 1)[1]
        prompt = acquisition_task(parent['strategy']) + '\n\n--- BEGIN ATTACHED' + attachments
        prompt = prompt.replace('\r\n', '\n')
        for strategy in ['none', 'requirements', 'boundaries']:
            text, acquisition = prompt, None
            if strategy != 'none':
                acquisition = next(a for a in acquisitions if a['method'] == parent['strategy'] and a['strategy'] == strategy)
                path = context_directory / 'contexts' / f"{parent['strategy'].lower()}-{strategy}.txt"
                insert = path.read_bytes(); text += '\n\n' + insert.decode()
                sources[str(path.relative_to(ROOT))] = digest(insert)
                for artifact in ('record.json', 'snapshot.json'):
                    path = ROOT / '.local/context-generation' / acquisition['id'] / artifact
                    sources[str(path.relative_to(ROOT))] = digest(path.read_bytes())
            sha = digest(text.encode())
            path = directory / 'prompts' / f'{sha}.txt'; path.parent.mkdir(exist_ok=True)
            if path.exists() and path.read_bytes() != text.encode(): raise ValueError('Prompt changed')
            if not path.exists(): path.write_text(text)
            conditions.append({**parent, 'id': f'{cell}__{strategy}', 'parentCondition': cell, 'securityStrategy': strategy, 'repetitions': repetitions,
                'contextAcquisitionId': acquisition['id'] if acquisition else None, 'promptFile': str(path.relative_to(directory)),
                'promptSha256': sha, 'promptBytes': len(text.encode()), 'promptCharacters': len(text)})
    schedule, rng = [], random.Random(identifier)
    for repetition in range(1, repetitions + 1):
        block = [{'runId': f'{identifier}__{c["id"]}__r{repetition}', 'condition': c['id'], 'repetition': repetition} for c in conditions]
        rng.shuffle(block); schedule.extend(block)
    plan = {'id': identifier, 'phase': 'security_followup', 'protocol': 'structured-delivery-functional-feedback-v1', 'createdAt': timestamp(),
            'model': MODEL, 'settings': SETTINGS, 'maxSubmissions': 3, 'system': SYSTEM, 'systemSha256': digest(SYSTEM.encode()),
            'conditions': conditions, 'schedule': schedule, 'acquisitions': acquisitions, 'sourceHashes': sources,
            'analysis': {'primary': 'Full functional success within three submissions / all trajectories; failed security checks and evaluation coverage per trajectory.',
                         'firstSubmission': 'Report first-submission success separately; repairs never replace it.',
                         'comparison': 'Fresh control within the same method/paper-context, with identical delivery and feedback budget.',
                         'stopping': 'Complete the fixed schedule, including all failures. No outcome-based early stopping or hidden retries.',
                         'limits': 'Exploratory development informed by previous failures; no security-test feedback, test code or fixture thresholds sent to either model. One acquired context per method/strategy is shared within this iteration. Menu edits are required but runtime menu rendering is not covered by the original 16 checks.'}}
    if (directory / 'manifest.json').exists(): plan['createdAt'] = json.loads((directory / 'manifest.json').read_text())['createdAt']
    plan['fingerprint'] = digest(canonical(plan)); freeze(directory / 'manifest.json', plan)
    return plan


def validate(path: Path):
    plan = json.loads(path.read_text())
    if digest(canonical({k: v for k, v in plan.items() if k != 'fingerprint'})) != plan['fingerprint']: raise ValueError('Plan fingerprint mismatch')
    for name, sha in plan['sourceHashes'].items():
        if digest((ROOT / name).read_bytes()) != sha: raise ValueError(f'Frozen input changed: {name}')
    return plan


def trajectory(manifest: Path, run_id: str, command: list[str]):
    plan = validate(manifest)
    row = next(r for r in plan['schedule'] if r['runId'] == run_id)
    condition = next(c for c in plan['conditions'] if c['id'] == row['condition'])
    directory = manifest.parent / 'runs' / run_id
    directory.mkdir(parents=True, exist_ok=False)
    original, origins = original_sources(); files = dict(original)
    prompt = (manifest.parent / condition['promptFile']).read_bytes().decode()
    if digest(prompt.encode()) != condition['promptSha256']: raise ValueError('Prompt fingerprint mismatch')
    messages = [{'role': 'system', 'content': plan['system']}, {'role': 'user', 'content': prompt}]
    record = {**row, 'status': 'started', 'startedAt': timestamp(), 'manifestFingerprint': plan['fingerprint'], 'sourceOrigins': origins,
              'submissions': [], 'functionalSuccess': False, 'settingsVerified': None}
    path = directory / 'record.json'; write_atomic(path, record)
    try:
        for number in range(1, plan['maxSubmissions'] + 1):
            request = {'protocol_version': 1, 'request_id': f'{run_id}-s{number}', 'model': plan['model'], 'settings': plan['settings'],
                       'messages': list(messages), 'tools': [TOOL], 'tool_choice': {'type': 'function', 'function': {'name': 'submit_feature_changes'}}, 'parallel_tool_calls': False}
            submission = {'number': number, 'status': 'started', 'request': request, 'requestSha256': digest(canonical(request)), 'startedAt': timestamp()}
            record['submissions'].append(submission); write_atomic(path, record)
            response = invoke(command, request, 600)
            submission.update(response=response, responseSha256=digest(canonical(response)), receivedAt=timestamp())
            if response['model'] != plan['model'] or response['request_id'] != request['request_id']:
                record['status'] = submission['status'] = 'identity_mismatch'; break
            if response['settings'] is not None and response['settings'] != plan['settings']:
                record['status'] = submission['status'] = 'settings_mismatch'; break
            record['settingsVerified'] = record['settingsVerified'] is not False and response['settings'] == plan['settings']
            if response['finish_reason'] != 'stop': record['status'] = submission['status'] = 'incomplete_response'; break
            messages.append({'role': 'assistant', 'content': response['output_text']})
            try:
                candidate = apply_changes(files, json.loads(response['output_text']))
                missing = [name for name in TARGETS if candidate[name] == original[name]]
                if missing: raise ValueError('Missing required game integration edits: ' + ', '.join(missing))
            except (ValueError, KeyError, TypeError) as error:
                submission['status'] = 'invalid_changes'; feedback = {'deliveryError': str(error), 'applied': False}
            else:
                files = candidate
                changed = {name: content for name, content in files.items() if content != original.get(name)}
                stage = directory / f'submission-{number}'; stage.mkdir()
                complete = '\n\n'.join(f'```java filename={name}\n{content}\n```' for name, content in changed.items())
                (stage / 'complete-files.txt').write_text(complete)
                submission.update(status='evaluating', deliveredFiles=list(changed), completeResponseSha256=digest(complete.encode()))
                write_atomic(path, record)
                report = evaluate_response(complete, stage / 'evaluation', {'iteration': plan['id'], 'runId': run_id, 'submission': number})
                submission.update(status='evaluated', compilation=report.get('mainCompilation'), functionalSuccess=report.get('functionalSuccess'),
                                  evaluationFile=str((stage / 'evaluation/report.json').relative_to(directory)), evaluationCanonicalSha256=digest(canonical(report)))
                record['finalEvaluation'] = submission['evaluationFile']
                feedback = {'compilation': report.get('mainCompilation'), 'functionalSuccess': report.get('functionalSuccess'),
                            'functionalChecks': [c for c in report['checks'] if c['suite'] in ('unit', 'invoked', 'autonomous')],
                            'compilerErrors': [(p.get('stderr') or '')[-18000:] for p in report.get('processes', []) if p.get('exitCode') and 'javac' in Path(p['command'][0]).name]}
                if report['status'] != 'evaluated': record['status'] = 'evaluation_error'; break
                if report['functionalSuccess']:
                    record.update(status='completed', functionalSuccess=True); break
            submission['feedback'] = feedback
            messages.append({'role': 'user', 'content': json.dumps(feedback) + f'\n{plan["maxSubmissions"] - number} submissions remain. Correct the current source using exact edits.'})
            write_atomic(path, record)
        else: record['status'] = 'budget_exhausted'
    except AdapterFailure as error:
        record.update(status='adapter_error', errorCategory=error.category); submission['status'] = 'adapter_error'
    except BaseException:
        record['status'] = 'evaluation_error' if submission.get('status') == 'evaluating' else 'interrupted'; raise
    finally:
        record['finishedAt'] = timestamp(); write_atomic(path, record)
    print(f'{run_id}: {record["status"]}; submissions={len(record["submissions"])}; functional={record["functionalSuccess"]}', flush=True)


def run(manifest: Path, workers=3):
    if not 1 <= workers <= 4: raise ValueError('Use 1–4 workers')
    plan = validate(manifest)
    with (manifest.parent / '.iteration.lock').open('a') as lock:
        fcntl.flock(lock, fcntl.LOCK_EX | fcntl.LOCK_NB)
        pending = [r for r in plan['schedule'] if not (manifest.parent / 'runs' / r['runId']).exists()]
        def one(row):
            process = subprocess.run([sys.executable, '-u', '-m', 'research.iteration_runner', '--manifest', str(manifest), '--run-id', row['runId'], '--execute'], cwd=ROOT, capture_output=True, text=True)
            (manifest.parent / f'{row["runId"]}.log').write_text(process.stdout + process.stderr)
            print(process.stdout.strip() or f'{row["runId"]}: process exited {process.returncode}', flush=True)
        with ThreadPoolExecutor(max_workers=workers) as pool: list(pool.map(one, pending))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--manifest', type=Path, required=True)
    parser.add_argument('--run-id')
    parser.add_argument('--workers', type=int, default=3)
    parser.add_argument('--execute', action='store_true')
    args = parser.parse_args()
    if args.execute and args.run_id: trajectory(args.manifest, args.run_id, command_from_env())
    elif args.execute: run(args.manifest, args.workers)
    else:
        plan = validate(args.manifest)
        print(f"{plan['id']}: {len(plan['schedule'])} trajectories; at most {3 * len(plan['schedule'])} requests")
