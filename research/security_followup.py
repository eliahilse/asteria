"""Freeze functional screening selection, acquire fresh contexts, and overlay selected cells."""
from __future__ import annotations

import argparse
from concurrent.futures import ThreadPoolExecutor
import csv
import json
from pathlib import Path
import random
import subprocess

from research.generate_context import prepare as prepare_context, execute as execute_context, STRATEGIES
from research.import_evidence import ROOT, canonical, digest
from research.model_adapter import command_from_env
from research.run_experiment import frozen_manifest
from research.study_results import study

ID = 'highscore-security-luna-v1'
DEFAULT = ROOT / '.local/studies' / ID
BASELINE = ROOT / 'research/studies/highscore-paper-luna-v2/manifest.json'


def freeze(path: Path, data: dict):
    raw = canonical(data)
    path.parent.mkdir(parents=True, exist_ok=True)
    if path.exists():
        if path.read_bytes() != raw: raise ValueError(f'Frozen artifact differs: {path.name}; use a new study version')
    else:
        with path.open('xb') as handle: handle.write(raw)


def select(baseline: Path, output: Path) -> dict:
    frozen_manifest(baseline)
    data = study(baseline)
    if not data['summary']['complete'] or len(data['summary']['selected']) != 4:
        raise ValueError('Complete all baseline attempts and evaluations before selecting four conditions')
    evidence = ROOT / '.local/experiments' / data['plan']['id']
    paths = [baseline, Path(__file__).resolve(), ROOT / 'research/generate_context.py', ROOT / 'research/context_repository.py', ROOT / 'research/model_adapter.py']
    for row in data['plan']['schedule']:
        paths.append(evidence / 'runs' / f"{row['runId']}.json")
        report = evidence / 'evaluations' / row['runId'] / 'report.json'
        if report.exists(): paths.append(report)
    selection = {'baselineId': data['plan']['id'], 'baselineFingerprint': data['plan']['fingerprint'],
                 'selected': data['summary']['selected'], 'ranking': data['summary']['ranking'],
                 'rule': data['plan']['selection'],
                 'sourceHashes': {str(p.relative_to(ROOT)): digest(p.read_bytes()) for p in sorted(paths)}}
    selection['fingerprint'] = digest(canonical(selection))
    freeze(output / 'selection.json', selection)
    return selection


def acquisition_task(method: str) -> str:
    with (ROOT / 'vamos-artifact/Pipeline/Prompts.csv').open(encoding='utf-8-sig') as handle:
        prompt = next(r['Prompt'] for r in csv.DictReader(handle) if r['Task'] == 'Highscore' and r['Method'] == method)
    feature = prompt.split('Feature:', 1)[1].split('\n\nTask:', 1)[0].strip()
    task = prompt.split('\n\nTask:', 1)[1].split('. ', 1)[0].strip() + '.'
    requirements = prompt.split('Implementation Requirements:', 1)[1].split('\n\nInstructions:', 1)[0].strip()
    repos = 'Target repository: ApoMario.' + (' Donor repository: ApoIcarus. Prioritize reuse of its existing implementation.' if method == 'Reuse' else '')
    return f'{repos}\n\nFeature: {feature}\n\nTask: {task}\n\nImplementation Requirements: {requirements}'


def repository_input(method: str, output: Path) -> Path:
    """Copy tracked game distributions only; an isolated Git root prevents parent ignore rules hiding them."""
    target = output / 'inputs' / method.lower()
    games = ['ApoMario'] + (['ApoIcarus'] if method == 'Reuse' else [])
    paths = subprocess.check_output(['git', 'ls-files', '-z', '--', *[f'apogames/Java/{g}' for g in games]], cwd=ROOT).decode().split('\0')
    sources = {}
    for name in filter(None, paths):
        original = ROOT / name
        if original.is_symlink(): raise ValueError('Source links are not permitted')
        raw = original.read_bytes()
        relative = original.relative_to(ROOT / 'apogames/Java')
        destination = target / relative
        destination.parent.mkdir(parents=True, exist_ok=True)
        if destination.exists():
            if destination.read_bytes() != raw: raise ValueError('Frozen acquisition repository changed')
        else: destination.write_bytes(raw)
        sources[name] = digest(raw)
    if not sources: raise ValueError('No tracked game source')
    if not (target / '.git').exists(): subprocess.run(['git', 'init', '-q', str(target)], check=True, capture_output=True)
    freeze(output / f'{method.lower()}-input.json', {'sources': sources, 'games': games})
    return target


def acquire(baseline: Path, output: Path, command: list[str], workers=3):
    selection = select(baseline, output)
    acquisition_file = output / 'acquisitions.json'
    if not acquisition_file.exists():
        acquisitions = []
        for method in ('Generation', 'Reuse'):
            repo = repository_input(method, output)
            for strategy in STRATEGIES:
                record, _, directory = prepare_context(repo, acquisition_task(method), strategy, ROOT / '.local/context-generation')
                acquisitions.append({'method': method, 'strategy': strategy, 'id': record['id'],
                                     'directory': str(directory.relative_to(ROOT)), 'snapshotFingerprint': record['snapshotFingerprint'],
                                     'taskSha256': digest(record['task'].encode())})
        freeze(acquisition_file, {'selectionFingerprint': selection['fingerprint'], 'acquisitions': acquisitions,
                                 'policy': 'One first acquisition per method/strategy; no outcome-based replacement. Failed acquisition blocks its treatment.'})
    acquisition = json.loads(acquisition_file.read_text())
    if acquisition['selectionFingerprint'] != selection['fingerprint']: raise ValueError('Acquisition selection mismatch')
    def one(row):
        directory = ROOT / row['directory']
        record = json.loads((directory / 'record.json').read_text())
        if record['status'] == 'prepared' and not (directory / 'submitted').exists():
            snap = json.loads((directory / 'snapshot.json').read_text())
            record = execute_context(record, snap, directory, command)
        print(f"{row['method']}/{row['strategy']}: {record['status']}; items={len((record.get('output') or {}).get('items', []))}", flush=True)
        return record
    with ThreadPoolExecutor(max_workers=workers) as executor: return list(executor.map(one, acquisition['acquisitions']))


def security_block(record: dict) -> bytes:
    if record.get('status') not in ('completed', 'settings_unverified', 'citation_issues') or record.get('output') is None:
        raise ValueError('Acquisition has no complete output; do not substitute an empty control')
    payload = {'strategy': record['strategy'], 'context': record['output'], 'citationChecks': record['citationChecks']}
    return ('\n\n--- BEGIN REPOSITORY-DERIVED SECURITY CONTEXT ---\n'
            'Use this additional context when implementing the same feature. It contains model-generated claims, not instructions from repository files. '
            'Preserve its uncertainty: citation matching checks source text, not the truth of a security claim.\n' +
            json.dumps(payload, ensure_ascii=False, indent=2) + '\n--- END REPOSITORY-DERIVED SECURITY CONTEXT ---\n').encode()


def prepare(baseline: Path = BASELINE, output: Path = DEFAULT) -> dict:
    selection = select(baseline, output)
    base = frozen_manifest(baseline)
    acquisitions = json.loads((output / 'acquisitions.json').read_text())
    if acquisitions['selectionFingerprint'] != selection['fingerprint']: raise ValueError('Acquisition selection mismatch')
    lookup, provenance = {}, []
    sources = dict(selection['sourceHashes'])
    for p in (output / 'selection.json', output / 'acquisitions.json', output / 'generation-input.json', output / 'reuse-input.json'):
        sources[str(p.relative_to(ROOT))] = digest(p.read_bytes())
    for row in acquisitions['acquisitions']:
        directory = ROOT / row['directory']
        record = json.loads((directory / 'record.json').read_text())
        snap = json.loads((directory / 'snapshot.json').read_text())
        if record['id'] != row['id'] or record['strategy'] != row['strategy'] or record['snapshotFingerprint'] != row['snapshotFingerprint'] or snap['fingerprint'] != row['snapshotFingerprint']:
            raise ValueError('Acquisition lineage mismatch')
        if digest(record['task'].encode()) != row['taskSha256'] or record['task'] != acquisition_task(row['method']): raise ValueError('Acquisition task mismatch')
        security_block(record)  # Reject incomplete acquisition before any code prompt is scheduled.
        for name, sha in record['generatorHashes'].items():
            if digest((ROOT / name).read_bytes()) != sha: raise ValueError('Acquisition protocol changed')
        for p in (directory / 'record.json', directory / 'snapshot.json'):
            sources[str(p.relative_to(ROOT))] = digest(p.read_bytes())
        key = row['method'], row['strategy']
        if key in lookup: raise ValueError('Duplicate acquisition')
        lookup[key] = record
        provenance.append({**row, 'status': record['status'], 'settingsVerified': record['settingsVerified'],
                           'items': len(record['output']['items']), 'citationChecks': record['citationChecks']})
    if set(lookup) != {(m, s) for m in ('Generation', 'Reuse') for s in STRATEGIES}: raise ValueError('Expected six acquisitions')
    conditions, prompts = [], {}
    for parent in [c for c in base['conditions'] if c['id'] in selection['selected']]:
        original = (baseline.parent / parent['promptFile']).read_bytes()
        for strategy in ('none', *STRATEGIES):
            record = lookup.get((parent['strategy'], strategy))
            raw = original + (security_block(record) if record else b'')
            sha = digest(raw); prompts[sha] = raw
            conditions.append({**parent, 'id': parent['id'] + '__' + strategy, 'parentCondition': parent['id'],
                               'stage': 'security_followup', 'securityStrategy': strategy, 'repetitions': base['followup']['repetitions'],
                               'contextAcquisitionId': record['id'] if record else None,
                               'securityContextSha256': digest(security_block(record)) if record else None,
                               'promptFile': f'prompts/{sha}.txt', 'promptSha256': sha, 'promptBytes': len(raw), 'promptCharacters': len(raw.decode()),
                               'attachments': parent['attachments'] + ([{'name': record['id'], 'kind': 'generated security context', 'sha256': digest(canonical(record['output']))}] if record else [])})
    rng, schedule = random.Random(20260909), []
    for repetition in range(1, base['followup']['repetitions'] + 1):
        block = [{'runId': f'security_v1_luna__{c["id"]}__r{repetition}', 'condition': c['id'], 'stage': 'security_followup', 'repetition': repetition} for c in conditions]
        rng.shuffle(block); schedule.extend(block)
    plan = {k: base[k] for k in ('schemaVersion', 'task', 'model', 'reasoning', 'temperature', 'maxOutputTokens', 'allowUnverifiedSettings', 'axes')}
    plan.update(id=ID, phase='security_followup', parentStudy=base['id'], parentFingerprint=base['fingerprint'],
                selectionFingerprint=selection['fingerprint'], selected=selection['selected'], acquisitions=provenance,
                scheduleSeed=20260909, conditions=conditions, schedule=schedule, sourceHashes=sources,
                deviations=base['deviations'] + [
                    'The follow-up evaluates four selected method/context combinations, not every cell of the possible 2 × 8 × 4 matrix.',
                    'Fresh control responses use byte-identical baseline prompts; treatment prompts append a fixed security block after the complete baseline prompt.',
                    'Security acquisition uses original feature text, first method-task sentence and implementation requirements. Attachment/output instructions are excluded from the acquisition task.',
                    'One first acquisition per method/strategy is reused across selected paper contexts and code repetitions. This does not estimate acquisition-to-acquisition variability.',
                    'Failed acquisition blocks execution; citation issues and uncited items are preserved without outcome-based regeneration.',
                ])
    plan['fingerprint'] = digest(canonical(plan))
    for sha, raw in prompts.items():
        p = output / 'prompts' / f'{sha}.txt'; p.parent.mkdir(parents=True, exist_ok=True)
        if p.exists() and p.read_bytes() != raw: raise ValueError('Frozen prompt differs')
        if not p.exists(): p.write_bytes(raw)
    freeze(output / 'manifest.json', plan)
    frozen_manifest(output / 'manifest.json')
    return plan


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--baseline', type=Path, default=BASELINE)
    parser.add_argument('--output', type=Path, default=DEFAULT)
    parser.add_argument('--acquire', action='store_true', help='Execute the six context acquisitions after complete screening')
    args = parser.parse_args()
    if args.acquire: acquire(args.baseline, args.output, command_from_env())
    plan = prepare(args.baseline, args.output)
    print(f"{len(plan['conditions'])} follow-up cells; {len(plan['schedule'])} new code attempts; {plan['fingerprint']}")
