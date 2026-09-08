"""Freeze the operation-context comparison after the budget study is preserved."""
from copy import deepcopy
import argparse
import json
from pathlib import Path
import random
import re

from research.import_evidence import ROOT, canonical, digest
from research.iteration_runner import freeze, validate
from research.iteration_results import read_study
from research.run_experiment import timestamp


def prepare(identifier, parent_id):
    if not all(re.fullmatch(r'[a-z0-9][a-z0-9_-]+', s) for s in (identifier, parent_id)): raise ValueError('Invalid iteration ID')
    parent_dir = ROOT / '.local/iterations' / parent_id; directory = ROOT / '.local/iterations' / identifier
    parent = validate(parent_dir / 'manifest.json')
    if parent['maxSubmissions'] != 5 or not read_study(parent_dir)['summary']['complete']:
        raise ValueError('Complete the five-submission parent before freezing this comparison')
    if (directory / 'manifest.json').exists(): raise ValueError('Experiment already frozen')
    additions = json.loads((directory / 'acquisitions-operations.json').read_text())
    plan = deepcopy(parent); plan.pop('fingerprint'); plan.update(id=identifier, createdAt=timestamp())
    plan['parentIteration'] = {'id': parent_id, 'fingerprint': parent['fingerprint'],
        'purpose': 'New operation-context candidate compared with fixed requirements and boundaries; fresh responses in every arm.'}
    sources = plan['sourceHashes']
    for path in [Path(__file__), ROOT / 'research/operational_context.py', parent_dir / 'manifest.json']:
        sources[str(path.relative_to(ROOT))] = digest(path.read_bytes())
    for item in additions:
        path = ROOT / '.local/context-generation' / item['id'] / 'record.json'; record = json.loads(path.read_text())
        if record['status'] not in ('completed', 'settings_unverified') or record['model'] != plan['model'] or not record.get('promptInsert'):
            raise ValueError('Both new operation acquisitions must finish with the expected model')
        for name, sha in record['generatorHashes'].items():
            if digest((ROOT / name).read_bytes()) != sha: raise ValueError('Acquisition source changed')
            sources[name] = sha
        item.update(task=record['task'], promptInsertSha256=record['promptInsertSha256'], recordSha256=digest(path.read_bytes()))
        for source in (path, path.parent / 'snapshot.json'): sources[str(source.relative_to(ROOT))] = digest(source.read_bytes())
    plan['acquisitions'] = [a for a in parent['acquisitions'] if a['strategy'] != 'overview'] + additions
    conditions = []
    for old in parent['conditions']:
        condition = deepcopy(old)
        if old['securityStrategy'] == 'overview':
            item = next(a for a in additions if a['method'] == old['strategy'])
            control = next(c for c in parent['conditions'] if c['parentCondition'] == old['parentCondition'] and c['securityStrategy'] == 'none')
            path = directory / 'contexts' / (old['strategy'].lower() + '-operations.txt'); insert = path.read_bytes()
            if digest(insert) != item['promptInsertSha256']: raise ValueError('Operation insert differs')
            raw = (parent_dir / control['promptFile']).read_bytes() + b'\n\n' + insert
            condition.update(id=old['parentCondition'] + '__operations', securityStrategy='operations', contextAcquisitionId=item['id'])
            sources[str(path.relative_to(ROOT))] = digest(insert)
        else: raw = (parent_dir / old['promptFile']).read_bytes()
        sha = digest(raw); condition.update(promptFile=f'prompts/{sha}.txt', promptSha256=sha, promptBytes=len(raw), promptCharacters=len(raw.decode()))
        target = directory / condition['promptFile']; target.parent.mkdir(exist_ok=True)
        if not target.exists(): target.write_bytes(raw)
        conditions.append(condition)
    plan['conditions'] = conditions
    for source in (parent_dir / 'contexts').glob('*.txt'):
        if 'overview' in source.name: continue
        target = directory / 'contexts' / source.name
        if not target.exists(): target.write_bytes(source.read_bytes())
    freeze(directory / 'acquisitions.json', plan['acquisitions'])
    schedule, rng = [], random.Random(identifier)
    for repetition in range(1, 6):
        block = [{'runId': f'{identifier}__{c["id"]}__r{repetition}', 'condition': c['id'], 'repetition': repetition} for c in conditions]
        rng.shuffle(block); schedule.extend(block)
    plan['schedule'] = schedule
    plan['analysis'] = {
        'primary': 'Full functional success within five submissions / every scheduled trajectory; report first submission and budget curves separately.',
        'security': 'Separate input policies, retention policy, parser robustness, resource stress and deserialization dispatch. Primary security diagnosis is resource-stress outcomes per check and trajectories affected; retain the original ten-check total as a descriptive secondary measure.',
        'comparison': 'All four paper-context cells, with fresh no-security controls, requirements, boundaries and operational guards; five new code trajectories per combination. The first three arms reuse I04/I05 prompt bytes. Operational guards replace overview prospectively, based on I03/I04 findings.',
        'stopping': 'Complete all 80 scheduled trajectories, including failures and null or adverse effects. Maximum five submissions each; never replace an unsuccessful run.',
        'context': 'Two new independent operational acquisitions see only repository/task, with explicit operation/input/invariant/enforcement-point/failure-behavior fields. Requirements and boundary inserts are held fixed from I04. Acquisitions receive no previous audits, code outputs, feedback, tests or fixture thresholds.',
        'qualification': parent['analysis']['qualification'],
        'limits': 'Exploratory strategy development informed by previous outcomes. New representation, instructions, content and length vary together, so this does not isolate formatting. Only one acquired operational insert per method; no generalization across repositories. No security feedback enters code generation. Model identity is checked; effective provider settings remain unverified when unattested.'}
    plan['fingerprint'] = digest(canonical(plan)); freeze(directory / 'manifest.json', plan)
    public = ROOT / 'research/iterations' / identifier; public.mkdir(parents=True, exist_ok=True); freeze(public / 'plan.json', plan)
    for source in (directory / 'contexts').glob('*.txt'):
        target = public / 'contexts' / source.name; target.parent.mkdir(exist_ok=True); target.write_bytes(source.read_bytes())
    validate(directory / 'manifest.json')
    print(f'{identifier}: {len(schedule)} trajectories frozen; two new operation contexts, four fixed comparison inserts')
    return plan


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--id', required=True); parser.add_argument('--parent', required=True)
    args = parser.parse_args(); prepare(args.id, args.parent)
