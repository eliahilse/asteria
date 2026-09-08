"""Compare I06/I07 without pooling rounds, contexts or issue checks as samples."""
import argparse
import json
from pathlib import Path

from research.import_evidence import ROOT, canonical, digest
from research.iteration_results import read_study
from research.iteration_runner import validate
from research.qualification import apply_available
from research.scientific_summary import csv_bytes, summarize

ROUNDS = ('i06-operational-guards', 'i07-operational-replication')


def verify_design(first, second):
    for key in ('model', 'settings', 'system', 'maxSubmissions', 'evaluationProtocol'):
        if first[key] != second[key]: raise ValueError('Replication differs in ' + key)
    earlier = {c['id']: c for c in first['conditions']}
    if set(earlier) != {c['id'] for c in second['conditions']}: raise ValueError('Different condition sets')
    for condition in second['conditions']:
        previous = earlier[condition['id']]
        for key in ('strategy', 'baseContext', 'parentCondition', 'securityStrategy', 'repetitions'):
            if previous[key] != condition[key]: raise ValueError('Condition definition differs: ' + key)
        if condition['securityStrategy'] == 'operations':
            if previous['contextAcquisitionId'] == condition['contextAcquisitionId']: raise ValueError('Operational acquisition was reused')
        elif previous['promptSha256'] != condition['promptSha256'] or previous['contextAcquisitionId'] != condition['contextAcquisitionId']:
            raise ValueError('Fixed comparison prompt or acquisition changed')
    ids = [{r['runId'] for r in p['schedule']} for p in (first, second)]
    if ids[0] & ids[1] or any(len(s) != len(p['schedule']) for s, p in zip(ids, (first, second))): raise ValueError('Code trajectory IDs are not fresh and unique')
    positions = [sorted((r['condition'], r['repetition']) for r in p['schedule']) for p in (first, second)]
    if positions[0] != positions[1]: raise ValueError('Replication schedule positions differ')


def design():
    plans = [validate(ROOT / '.local/iterations' / name / 'manifest.json') for name in ROUNDS]
    verify_design(*plans)
    context_pairs = []
    for method in ('Generation', 'Reuse'):
        items = [next(a for a in p['acquisitions'] if a['method'] == method and a['strategy'] == 'operations') for p in plans]
        records = [json.loads((ROOT / '.local/context-generation' / a['id'] / 'record.json').read_text()) for a in items]
        for key in ('task', 'snapshotFingerprint', 'generatorHashes', 'model'):
            if records[0][key] != records[1][key]: raise ValueError('Acquisition input/protocol differs: ' + key)
        context_pairs.append({'method': method, 'acquisitionIds': [a['id'] for a in items],
            'snapshotFingerprint': records[0]['snapshotFingerprint'], 'taskSha256': digest(records[0]['task'].encode()),
            'insertSha256': [r['promptInsertSha256'] for r in records]})
    return plans, context_pairs


def save():
    plans, contexts = design(); analyses, provenance = [], []
    for name in ROUNDS:
        study = read_study(ROOT / '.local/iterations' / name)
        if not study['summary']['complete']: raise ValueError('Complete both frozen schedules before comparing outcomes')
        study, qualification = apply_available(study); analysis = summarize(study)
        analysis['measurementQualification'] = qualification
        path = ROOT / 'research/iterations' / name / 'qualified-analysis.json'
        if canonical(analysis) != canonical(json.loads(path.read_text())): raise ValueError('Published qualified analysis differs from raw evidence')
        analyses.append(analysis); provenance.append({'iteration': name, 'planFingerprint': study['plan']['fingerprint'],
            'qualifiedAnalysisSha256': digest(path.read_bytes()), 'qualification': qualification})
    rows, effects = [], []
    for analysis in analyses:
        for condition in analysis['conditions']:
            row = {'iteration': analysis['iteration'], 'condition': condition['condition'], 'n': condition['n'],
                'compiled': condition['compiled'], 'firstFull': condition['firstFull'], 'withinBudgetFull': condition['withinBudgetFull'],
                'codeCalls': condition['modelSubmissions'], 'jointPass': condition['functionalAndAllDeclaredSecurityPass'],
                **{'issues' + key.title(): value for key, value in condition['issues'].items()}}
            for name in ('oversizedPhysicalLine', 'largePersistedRecordSet'):
                check = next(t for t in analysis['perTest'] if t['condition'] == condition['condition'] and t['test'] == name)
                row.update({name + '_' + key: check[key] for key in ('passed', 'failed', 'unresolved')})
            rows.append(row)
    for first in analyses[0]['comparisons']:
        second = next(c for c in analyses[1]['comparisons'] if c['treatment'] == first['treatment'])
        row = {'condition': first['treatment'], 'securityStrategy': first['securityStrategy']}
        for label, value in zip(('i06', 'i07'), (first, second)):
            lower, upper = value['failureCountDeltaIdentificationBoundsPerTrajectory']
            line = next(t for t in value['tests'] if t['test'] == 'oversizedPhysicalLine')
            row.update({label + 'FunctionalRateDelta': value['functionalRateDelta'], label + 'IssueDeltaLower': lower,
                label + 'IssueDeltaUpper': upper, label + 'OversizedLineFailureRateDelta': line['failureRateDelta'],
                label + 'OversizedLineDirection': line['direction']})
        row['fewerTotalFailuresInBothDespiteMissing'] = all(c['failureCountDeltaIdentificationBoundsPerTrajectory'][1] < 0 for c in (first, second))
        row['fewerOversizedLineFailuresInBoth'] = all(row[label + 'OversizedLineDirection'] == 'decreased' for label in ('i06', 'i07'))
        effects.append(row)
    result = {'rounds': list(ROUNDS), 'sourceProvenance': provenance, 'operationalAcquisitions': contexts,
        'analysisSha256': digest(Path(__file__).read_bytes()), 'conditions': rows, 'effects': effects,
        'interpretation': 'Rounds are separate. Two operational acquisitions per method; requirements/boundaries share earlier inserts. Bounds address missingness, not sampling uncertainty. No significance claim or pooled independent-check model.'}
    public = ROOT / 'research/iterations' / ROUNDS[1]
    (public / 'replication-analysis.json').write_bytes(canonical(result))
    (public / 'replication-conditions.csv').write_bytes(csv_bytes(rows)); (public / 'replication-effects.csv').write_bytes(csv_bytes(effects))
    lines = ['# I06/I07 replication comparison', '', result['interpretation'], '',
        'Both qualified summaries were recomputed from raw evidence and compared with their published snapshots. Model settings, delivery, evaluator, matrix and fixed comparison prompts match. Operational acquisitions have distinct identities and matching repository/task/protocol inputs.', '',
        '## Every security strategy against its fresh controls', '',
        'Functional differences are percentage points. Issue-count differences are per trajectory; negative favors the security context. The ranges cover every assignment of missing outcomes and are not confidence intervals.', '',
        '| Condition | I06 full Δ, pp | I07 full Δ, pp | I06 issue Δ bounds | I07 issue Δ bounds | Fewer total failures in both | Oversized-line direction, I06 / I07 |',
        '| --- | ---: | ---: | --- | --- | --- | --- |']
    for row in effects:
        lines.append(f"| {row['condition']} | {100*row['i06FunctionalRateDelta']:+.0f} | {100*row['i07FunctionalRateDelta']:+.0f} | [{row['i06IssueDeltaLower']:+.1f}, {row['i06IssueDeltaUpper']:+.1f}] | [{row['i07IssueDeltaLower']:+.1f}, {row['i07IssueDeltaUpper']:+.1f}] | {row['fewerTotalFailuresInBothDespiteMissing']} | {row['i06OversizedLineDirection']} / {row['i07OversizedLineDirection']} |")
    lines += ['', 'The condition CSV retains both rounds\' full counts, first-submission success, calls, joint passes and each resource check\'s pass/fail/unresolved counts. The JSON includes input hashes and acquisition identities. All conditions and null or adverse effects remain included.', '',
        'These comparisons do not isolate schema formatting from content, length or acquisition instructions. A tendency across two acquired inserts does not establish generalization across repositories. Policy failures, resource fixtures and unique vulnerabilities remain distinct concepts.', '']
    (public / 'replication-analysis.md').write_text('\n'.join(lines))
    print(f'{len(rows)} separate round/condition rows; {len(effects)} all-strategy comparisons; raw qualified analyses reproduced exactly')
    return result


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--check-design', action='store_true'); args = parser.parse_args()
    if args.check_design:
        design(); print('Matched frozen designs and repository/task inputs; operational acquisition identities are distinct')
    else: save()
