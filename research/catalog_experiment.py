"""Generic acquisition comparison: task-only and CWE-catalog inserts against fresh controls and the fixed requirements insert.

Acquisitions start from the Generation/Reuse repository snapshots and the Highscore
task only. The code schedule is frozen from the completed five-submission parent:
its no-security and requirements prompt bytes are reused unchanged, and the two
new inserts are appended to the no-security prompt of each selected cell.
"""
from __future__ import annotations

import argparse
from copy import deepcopy
import json
from pathlib import Path
import random
import re

from research import catalog_context, task_context
from research.import_evidence import ROOT, canonical, digest
from research.iteration_results import read_study
from research.iteration_runner import freeze, validate
from research.model_adapter import command_from_env
from research.run_experiment import timestamp, write_atomic
from research.security_followup import acquisition_task, repository_input

CELLS = ('generation_s', 'reuse_sb')
ARMS = ('none', 'requirements', 'task_only', 'catalog')
MODULES = {'task_only': task_context, 'catalog': catalog_context}
REPETITIONS = 5
TIME_CONTRACT = '\nTime API contract: survivalTime values are milliseconds; display them as mm:ss.'


def prepare_acquisitions(directory: Path):
    index = directory / 'acquisitions-generic.json'
    if index.exists(): raise ValueError('Generic acquisition set already exists')
    directory.mkdir(parents=True, exist_ok=True); acquisitions = []
    for method in ('Generation', 'Reuse'):
        repo = repository_input(method, directory)
        task = acquisition_task(method) + TIME_CONTRACT
        for strategy, module in MODULES.items():
            record, _, target = module.prepare(repo, task, ROOT / '.local/context-generation')
            record.update(method=method, iteration=directory.name)
            write_atomic(target / 'record.json', record)
            acquisitions.append({'method': method, 'strategy': strategy, 'id': record['id']})
    freeze(index, acquisitions)
    print(f'Prepared {len(acquisitions)} independent repository/task acquisitions; no model calls made')
    return acquisitions


def execute_one(directory: Path, method: str, strategy: str):
    item = next(a for a in json.loads((directory / 'acquisitions-generic.json').read_text()) if a['method'] == method and a['strategy'] == strategy)
    target = ROOT / '.local/context-generation' / item['id']; record = json.loads((target / 'record.json').read_text())
    for path, sha in record['generatorHashes'].items():
        if digest((ROOT / path).read_bytes()) != sha: raise ValueError('Frozen acquisition generator changed')
    result = MODULES[strategy].execute(record, json.loads((target / 'snapshot.json').read_text()), target, command_from_env())
    if result.get('promptInsert'):
        output = directory / 'contexts' / f'{method.lower()}-{strategy}.txt'; output.parent.mkdir(exist_ok=True)
        if output.exists(): raise ValueError('Context insert already exists')
        output.write_text(result['promptInsert'])
    print(f"{method}/{strategy}: {result['status']}; items={len((result.get('output') or {}).get('items', []))}")
    return result


def build_conditions(parent: dict, parent_dir: Path, directory: Path, additions: list[dict], sources: dict) -> list[dict]:
    conditions = []
    for cell in CELLS:
        control = next(c for c in parent['conditions'] if c['parentCondition'] == cell and c['securityStrategy'] == 'none')
        control_bytes = (parent_dir / control['promptFile']).read_bytes()
        for arm in ARMS:
            if arm in ('none', 'requirements'):
                source = next(c for c in parent['conditions'] if c['parentCondition'] == cell and c['securityStrategy'] == arm)
                condition = deepcopy(source); raw = (parent_dir / source['promptFile']).read_bytes()
                if digest(raw) != source['promptSha256']: raise ValueError('Parent prompt bytes differ from their fingerprint')
            else:
                item = next(a for a in additions if a['method'] == control['strategy'] and a['strategy'] == arm)
                path = directory / 'contexts' / f"{control['strategy'].lower()}-{arm}.txt"; insert = path.read_bytes()
                if digest(insert) != item['promptInsertSha256']: raise ValueError(f'{arm} insert differs from its acquisition record')
                raw = control_bytes + b'\n\n' + insert
                condition = deepcopy(control); condition.update(id=f'{cell}__{arm}', securityStrategy=arm, contextAcquisitionId=item['id'])
                sources[str(path.relative_to(ROOT))] = digest(insert)
            sha = digest(raw)
            condition.update(promptFile=f'prompts/{sha}.txt', promptSha256=sha, promptBytes=len(raw), promptCharacters=len(raw.decode()), repetitions=REPETITIONS)
            target = directory / condition['promptFile']; target.parent.mkdir(parents=True, exist_ok=True)
            if target.exists() and target.read_bytes() != raw: raise ValueError('Prompt changed')
            if not target.exists(): target.write_bytes(raw)
            conditions.append(condition)
    return conditions


def schedule(identifier: str, conditions: list[dict]) -> list[dict]:
    rows, rng = [], random.Random(identifier)
    for repetition in range(1, REPETITIONS + 1):
        block = [{'runId': f'{identifier}__{c["id"]}__r{repetition}', 'condition': c['id'], 'repetition': repetition} for c in conditions]
        rng.shuffle(block); rows.extend(block)
    return rows


ANALYSIS = {
    'primary': 'Full functional success within five submissions / every scheduled trajectory; report first submission and repair curves separately.',
    'security': 'Report every issue check with a fixed denominator: failed, unresolved and passed out of N per check and 10×N per condition. The positive valid-record round trip is separate. Unresolved outcomes are not passes and do not leave the denominator. Keep the five evaluation categories and the ten-check total as descriptive measures.',
    'comparison': 'Two paper-context cells (Generation S, Reuse S+B) × fresh no-security control, fixed I04 requirements insert, task-only insert and CWE-catalog insert × five fresh trajectories. Control and requirements prompt bytes are the parent’s; the two generic arms append a newly acquired insert to the control prompt. Requirements is a within-round bridge to earlier rounds, not a new acquisition.',
    'stopping': 'Complete all 40 scheduled trajectories, including failures and null or adverse effects. Maximum five submissions each; never replace an unsuccessful run.',
    'context': 'Four new acquisitions (two methods × task-only and CWE catalog) see only repository and task. The task-only instruction is one sentence. The catalog instruction adds the 2025 CWE Top 25 identifiers, names and MITRE descriptions with no feature-, repository- or check-specific guidance. Acquisitions receive no previous audits, code outputs, feedback, tests or fixture thresholds.',
    'qualification': 'After the full schedule, apply the frozen amplification-precondition audit to every final compiled artifact, then the frozen qualifier. Preserve raw reports and publish qualified results separately.',
    'limits': 'Exploratory strategy development informed by earlier rounds. One acquired insert per method and strategy is shared by five code trajectories; this is not five independent context replications. Instruction, generated content and insert length vary together. The catalog is a general published list, so its relevant entries overlap the declared checks by construction of the checks, not by prompt authoring. No security feedback enters code generation. Model identity is checked; effective provider settings remain unverified when unattested.',
}


def prepare(identifier: str, parent_id: str):
    if not all(re.fullmatch(r'[a-z0-9][a-z0-9_-]+', s) for s in (identifier, parent_id)): raise ValueError('Invalid iteration ID')
    parent_dir = ROOT / '.local/iterations' / parent_id; directory = ROOT / '.local/iterations' / identifier
    parent = validate(parent_dir / 'manifest.json')
    if parent['maxSubmissions'] != 5 or not read_study(parent_dir)['summary']['complete']:
        raise ValueError('Complete the five-submission parent before freezing this comparison')
    if (directory / 'manifest.json').exists(): raise ValueError('Experiment already frozen')
    additions = json.loads((directory / 'acquisitions-generic.json').read_text())
    plan = deepcopy(parent); plan.pop('fingerprint'); plan.update(id=identifier, createdAt=timestamp())
    plan['parentIteration'] = {'id': parent_id, 'fingerprint': parent['fingerprint'],
        'purpose': 'Generic acquisition instructions (task only, CWE catalog) compared with fresh controls and the fixed requirements insert on two paper-context cells; fresh responses in every arm.'}
    sources = plan['sourceHashes']
    for path in [Path(__file__), ROOT / 'research/task_context.py', ROOT / 'research/catalog_context.py', catalog_context.CATALOG_FILE, parent_dir / 'manifest.json']:
        sources[str(path.relative_to(ROOT))] = digest(path.read_bytes())
    for item in additions:
        path = ROOT / '.local/context-generation' / item['id'] / 'record.json'; record = json.loads(path.read_text())
        if record['status'] not in ('completed', 'settings_unverified') or record['model'] != plan['model'] or not record.get('promptInsert'):
            raise ValueError('All four generic acquisitions must finish with the expected model')
        if record['strategy'] != item['strategy'] or record['protocol'] != MODULES[item['strategy']].PROTOCOL: raise ValueError('Acquisition protocol mismatch')
        for name, sha in record['generatorHashes'].items():
            if digest((ROOT / name).read_bytes()) != sha: raise ValueError('Acquisition source changed')
            sources[name] = sha
        item.update(task=record['task'], promptInsertSha256=record['promptInsertSha256'], recordSha256=digest(path.read_bytes()))
        for source in (path, path.parent / 'snapshot.json'): sources[str(source.relative_to(ROOT))] = digest(source.read_bytes())
    plan['acquisitions'] = [a for a in parent['acquisitions'] if a['strategy'] == 'requirements'] + additions
    for source in (parent_dir / 'contexts').glob('*-requirements.txt'):
        target = directory / 'contexts' / source.name
        if not target.exists(): target.write_bytes(source.read_bytes())
    plan['conditions'] = build_conditions(parent, parent_dir, directory, additions, sources)
    freeze(directory / 'acquisitions.json', plan['acquisitions'])
    plan['schedule'] = schedule(identifier, plan['conditions'])
    plan['analysis'] = dict(ANALYSIS)
    plan['fingerprint'] = digest(canonical(plan)); freeze(directory / 'manifest.json', plan)
    public = ROOT / 'research/iterations' / identifier; public.mkdir(parents=True, exist_ok=True); freeze(public / 'plan.json', plan)
    for source in (directory / 'contexts').glob('*.txt'):
        target = public / 'contexts' / source.name; target.parent.mkdir(exist_ok=True); target.write_bytes(source.read_bytes())
    validate(directory / 'manifest.json')
    print(f'{identifier}: {len(plan["schedule"])} trajectories frozen; four new generic inserts, two fixed requirements inserts, two fresh controls')
    return plan


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--id', required=True)
    parser.add_argument('--prepare-acquisitions', action='store_true')
    parser.add_argument('--acquire', nargs=2, metavar=('METHOD', 'STRATEGY'))
    parser.add_argument('--freeze', metavar='PARENT_ID')
    args = parser.parse_args()
    directory = ROOT / '.local/iterations' / args.id
    if args.prepare_acquisitions: prepare_acquisitions(directory)
    elif args.acquire: execute_one(directory, *args.acquire)
    elif args.freeze: prepare(args.id, args.freeze)
    else: parser.error('--prepare-acquisitions, --acquire or --freeze is required')
