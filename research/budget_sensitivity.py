"""Freeze fresh trajectories with an increased repair budget and unchanged inserts."""
from __future__ import annotations

import argparse
from copy import deepcopy
import json
import random
import re

from research.import_evidence import ROOT, canonical, digest
from research.iteration_runner import freeze, validate
from research.iteration_results import read_study
from research.run_experiment import timestamp


def prepare(identifier, parent_id, budget=5):
    if not all(re.fullmatch(r'[a-z0-9][a-z0-9_-]+', value) for value in (identifier, parent_id)):
        raise ValueError('Invalid iteration ID')
    if budget != 5: raise ValueError('This revision declares five submissions')
    parent_dir = ROOT / '.local/iterations' / parent_id
    parent = validate(parent_dir / 'manifest.json')
    if parent['maxSubmissions'] != 3 or not read_study(parent_dir)['summary']['complete']:
        raise ValueError('A complete three-submission parent is required')
    plan = deepcopy(parent); plan.pop('fingerprint')
    directory = ROOT / '.local/iterations' / identifier
    if directory.exists(): raise ValueError('Use a fresh iteration directory')
    directory.mkdir(parents=True)
    original = 'up to three submissions'
    if plan['system'].count(original) != 1: raise ValueError('Unexpected delivery system text')
    plan.update(id=identifier, createdAt=timestamp(), maxSubmissions=budget,
                system=plan['system'].replace(original, 'up to five submissions'))
    plan['systemSha256'] = digest(plan['system'].encode())
    plan['parentIteration'] = {'id': parent_id, 'fingerprint': parent['fingerprint'],
        'purpose': 'Fresh budget-sensitivity run; parent contexts reused exactly, parent responses never supplied.'}
    for condition in plan['conditions']:
        raw = (parent_dir / condition['promptFile']).read_bytes()
        if digest(raw) != condition['promptSha256']: raise ValueError('Parent prompt changed')
        target = directory / condition['promptFile']; target.parent.mkdir(parents=True, exist_ok=True)
        if not target.exists(): target.write_bytes(raw)
    for source in sorted((parent_dir / 'contexts').glob('*.txt')):
        target = directory / 'contexts' / source.name; target.parent.mkdir(exist_ok=True); target.write_bytes(source.read_bytes())
    freeze(directory / 'acquisitions.json', plan['acquisitions'])
    plan['sourceHashes'].update({str(path.relative_to(ROOT)): digest(path.read_bytes()) for path in
        [ROOT / 'research/budget_sensitivity.py', parent_dir / 'manifest.json', ROOT / 'research/amplification_audit.py',
         ROOT / 'research/qualification.py', *sorted((ROOT / 'research/security_validation').glob('*.java'))]})
    schedule, rng = [], random.Random(identifier)
    repetitions = {condition['repetitions'] for condition in plan['conditions']}
    if len(repetitions) != 1: raise ValueError('Balanced repetitions required')
    for repetition in range(1, repetitions.pop() + 1):
        block = [{'runId': f'{identifier}__{c["id"]}__r{repetition}', 'condition': c['id'], 'repetition': repetition} for c in plan['conditions']]
        rng.shuffle(block); schedule.extend(block)
    plan['schedule'] = schedule
    plan['analysis'].update(
        primary='Full functional success within five submissions / all scheduled trajectories. Report first-submission and within-three success separately, retaining the submitted source and every failed edit.',
        comparison='Fresh no-security controls in all four paper-context cells; same five-submission budget, model request settings and evaluation in every arm. All original four security arms retained.',
        budget='Exploratory budget sensitivity following I04: identical task, attachments and security insert bytes; only the declared system budget and remaining-submissions feedback change from three to five. New independent code conversations; no continuation or selective retry of I04. Cross-round differences also include model sampling and collection time.',
        qualification='After the full schedule, apply the frozen amplification-precondition audit to every final compiled artifact, then the frozen qualifier. Preserve raw reports and publish qualified results separately. Unsupported large-record observations are unknown.',
        contextReplication='No new context acquisitions: this round deliberately holds I04 inserts fixed and provides no independent replication of context generation. Comparisons at submission three are descriptive: the five-submission allowance is announced at the start.')
    plan['fingerprint'] = digest(canonical(plan)); freeze(directory / 'manifest.json', plan)
    public = ROOT / 'research/iterations' / identifier; public.mkdir(parents=True, exist_ok=True)
    freeze(public / 'plan.json', plan)
    for source in (directory / 'contexts').glob('*.txt'):
        target = public / 'contexts' / source.name; target.parent.mkdir(exist_ok=True); target.write_bytes(source.read_bytes())
    validate(directory / 'manifest.json')
    print(f'{identifier}: {len(schedule)} fresh trajectories, at most {budget * len(schedule)} submissions; exact parent prompt bytes reused')
    return plan


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--id', required=True); parser.add_argument('--parent', required=True)
    args = parser.parse_args(); prepare(args.id, args.parent)
