"""Functional success by submission allowance, retaining unsuccessful trajectories."""
from __future__ import annotations

import argparse
import json
from pathlib import Path

from research.import_evidence import ROOT, canonical, digest
from research.iteration_results import read_study
from research.scientific_summary import csv_bytes, wilson


def summarize(plan, records):
    if len(records) != len(plan['schedule']) or any(r['status'] == 'started' for r in records):
        raise ValueError('A complete fixed schedule is required')
    curves, trajectories, errors = [], [], []
    for record in records:
        submissions = record['submissions']
        first = next((s['number'] for s in submissions if s.get('functionalSuccess') is True), None)
        trajectories.append({'runId': record['runId'], 'condition': record['condition'], 'status': record['status'],
            'firstFullSubmission': first, 'calls': len(submissions), 'invalidEdits': sum(s['status'] == 'invalid_changes' for s in submissions),
            'failedCompilations': sum(s.get('compilation') == 'fail' for s in submissions)})
        for s in submissions:
            if s.get('feedback', {}).get('deliveryError'):
                errors.append({'runId': record['runId'], 'condition': record['condition'], 'submission': s['number'],
                    'status': s['status'], 'detail': s['feedback']['deliveryError'], 'requestSha256': s['requestSha256']})
    for condition in plan['conditions']:
        selected = [r for r in records if r['condition'] == condition['id']]
        if len(selected) != condition['repetitions']: raise ValueError('Condition denominator differs from schedule')
        results = [r for r in trajectories if r['condition'] == condition['id']]; n = len(results)
        for budget in range(1, plan['maxSubmissions'] + 1):
            full = sum(r['firstFullSubmission'] is not None and r['firstFullSubmission'] <= budget for r in results)
            subs = [s for r in selected for s in r['submissions'] if s['number'] <= budget]
            lo, hi = wilson(full, n)
            curves.append({'condition': condition['id'], 'submissionAllowance': budget, 'n': n,
                'full': full, 'rate': full / n, 'wilson95Low': lo, 'wilson95High': hi,
                'newlySuccessful': sum(r['firstFullSubmission'] == budget for r in results),
                'cumulativeCalls': len(subs), 'cumulativeInvalidEdits': sum(s['status'] == 'invalid_changes' for s in subs),
                'cumulativeFailedCompilations': sum(s.get('compilation') == 'fail' for s in subs)})
    return {'curves': curves, 'trajectories': trajectories, 'deliveryErrors': errors}


def save(identifier):
    runtime = ROOT / '.local/iterations' / identifier; study = read_study(runtime)
    if not study['summary']['complete']: raise ValueError('A complete fixed schedule is required')
    records, source_hashes = [], {}
    for row in study['plan']['schedule']:
        path = runtime / 'runs' / row['runId'] / 'record.json'; record = json.loads(path.read_text())
        source_hashes[str(path.relative_to(ROOT))] = digest(path.read_bytes())
        for submission in record['submissions']:
            if not submission.get('evaluationFile'): continue
            report_path = (path.parent / submission['evaluationFile']).resolve()
            if not report_path.is_relative_to(path.parent.resolve()): raise ValueError('Invalid intermediate report path')
            report = json.loads(report_path.read_text())
            if digest(canonical(report)) != submission['evaluationCanonicalSha256'] or report['responseSha256'] != submission['completeResponseSha256']:
                raise ValueError('Intermediate evaluation lineage mismatch')
            if submission['functionalSuccess'] != report['functionalSuccess'] or submission['compilation'] != report['mainCompilation']:
                raise ValueError('Intermediate result differs from report')
            source_hashes[str(report_path.relative_to(ROOT))] = digest(report_path.read_bytes())
        records.append(record)
    result = {'iteration': identifier, 'manifestFingerprint': study['plan']['fingerprint'],
        'analysisSha256': digest(Path(__file__).read_bytes()), 'sourceHashes': source_hashes,
        'meaning': 'Cumulative full functional success within the actual announced allowance, using every scheduled trajectory. Earlier budgets are descriptive slices of the same conversations, not independent samples or counterfactual protocols. Security outcomes are reported separately on final artifacts.',
        **summarize(study['plan'], records)}
    directory = ROOT / 'research/iterations' / identifier
    (directory / 'submission-analysis.json').write_bytes(canonical(result))
    (directory / 'submission-curves.csv').write_bytes(csv_bytes(result['curves']))
    (directory / 'delivery-errors.csv').write_bytes(csv_bytes(result['deliveryErrors']))
    print(f'{identifier}: {len(records)} complete trajectories; {len(result["curves"])} budget rows; intermediate report hashes verified')
    return result


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration', required=True)
    save(parser.parse_args().iteration)
