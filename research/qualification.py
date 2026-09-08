"""Separate measurement qualification; original reports and result files are immutable."""
from __future__ import annotations

import argparse
from copy import deepcopy
import json
from pathlib import Path

from research.import_evidence import ROOT, canonical, digest
from research.run_experiment import write_atomic
from research.study_results import STATUSES

NAME = 'largePersistedRecordSet'


def qualify(study, audit):
    revised = deepcopy(study); by_run = {r['runId']: r for r in audit['results']}; changes = []
    for run in revised['runs']:
        check = next((c for c in run['checks'] if c['suite'] == 'security_v1' and c['name'] == NAME), None)
        if not check or check['status'] not in ('pass', 'fail'): continue
        result = by_run.get(run['runId'])
        if not result or result['legacyCheck'] != check: raise ValueError('Qualification does not match the original check')
        if result['precondition']['status'] == 'matched': continue
        old = check['status']
        check.update(status='unknown', originalStatus=old, qualification='legacy-amplification-precondition-v1',
            detail='Valid-record amplification was not established: ' + result['precondition']['reason'] + ' Original observation: ' + check.get('detail', ''))
        changes.append({'runId': run['runId'], 'condition': run['condition'], 'test': NAME, 'from': old, 'to': 'unknown'})
    for condition in revised['summary']['conditions']:
        selected = [r for r in revised['runs'] if r['condition'] == condition['id']]
        test = next(c for c in condition['checks'] if c['suite'] == 'security_v1' and c['name'] == NAME)
        counts = {status: 0 for status in STATUSES}
        for run in selected:
            check = next((c for c in run['checks'] if c['suite'] == 'security_v1' and c['name'] == NAME), None)
            counts[check['status'] if check else 'not_run'] += 1
        n = counts['pass'] + counts['fail']
        test.update(**counts, executed=n, passRate=counts['pass'] / n if n else None,
                    allAttemptRate=counts['pass'] / len(selected) if selected else None)
        condition['securityChecksPassed'] = sum(c['pass'] for c in condition['checks'] if c['kind'] == 'security')
        condition['securityChecksExecuted'] = sum(c['executed'] for c in condition['checks'] if c['kind'] == 'security')
    metadata = {'protocol': 'large-record-precondition-qualification-v1', 'auditId': audit['id'], 'auditPlanFingerprint': audit['planFingerprint'],
        'auditSha256': digest(canonical(audit)), 'qualifierSha256': digest(Path(__file__).read_bytes()), 'changes': changes,
        'meaning': 'Pass/fail large-record observations whose necessary two-record format precondition was not demonstrated become unknown. Original reports remain unchanged. Matching two records is a necessary precondition, not proof of correctness for every amplified encoding.'}
    revised['plan']['label'] = revised['plan'].get('label', '') + 'q'
    revised['plan']['evaluationNote'] = 'Qualified measurement: large-record outcomes without the required format precondition are unknown. Original reports remain archived.'
    revised['plan']['deviations'].append(metadata['meaning'])
    revised['measurementQualification'] = metadata
    return revised, metadata


def apply_available(study):
    path = ROOT / 'research/iterations' / study['plan']['id'] / 'amplification-audit.json'
    audit = json.loads(path.read_text())
    directory = ROOT / '.local/iterations' / audit['id']
    manifest = json.loads((directory / 'manifest.json').read_text())
    if digest(canonical({k: v for k, v in manifest.items() if k != 'fingerprint'})) != audit['planFingerprint']: raise ValueError('Audit plan fingerprint mismatch')
    if manifest['sourceManifestFingerprint'] != study['plan']['fingerprint']: raise ValueError('Audit references a different experiment')
    if canonical(audit) != canonical(json.loads((directory / 'results.json').read_text())): raise ValueError('Published audit differs from raw audit')
    for row in audit['results']:
        if digest((ROOT / row['reportFile']).read_bytes()) != row['originalReportSha256']: raise ValueError('Original audited report changed')
        if canonical(row['precondition']) != canonical(json.loads((directory / 'runs' / row['runId'] / 'report.json').read_text())): raise ValueError('Precondition report differs')
    return qualify(study, audit)


def save(identifier):
    from research.iteration_results import index
    data = index(iteration=identifier)
    data['studies'][0], metadata = apply_available(data['studies'][0])
    data['measurementQualification'] = metadata
    write_atomic(ROOT / 'research/iterations' / identifier / 'qualified-results.json', data)
    print(f"{identifier}: {len(metadata['changes'])} measurements qualified as unknown; all original outcomes retained")
    return data


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration', required=True)
    save(parser.parse_args().iteration)
