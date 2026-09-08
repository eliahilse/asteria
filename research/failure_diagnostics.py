"""Classify observed failures without redefining frozen test outcomes.

Source excerpts are syntactic inspection candidates, not automatic proofs of
boundedness or exploitability. Original code and reports remain unchanged.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path
import re

from research.import_evidence import ROOT, canonical, digest
from research.iteration_results import read_study
from research.scientific_summary import ISSUES, csv_bytes


def category(name, detail):
    if 'OutOfMemoryError' in detail: return 'observed_heap_exhaustion'
    if detail.startswith("Exceeded the protocol's"): return 'process_time_budget'
    if 'More than 100 entries retained.' in detail or 'Reload retained more than 100 records.' in detail: return 'retention_threshold'
    if ISSUES[name][0] == 'Input policy': return 'input_rejection_policy'
    if name == 'nativeDeserializationCanary' and 'hook executed' in detail: return 'test_canary_dispatch'
    return 'other_fixture_failure'


def save(identifier):
    runtime = ROOT / '.local/iterations' / identifier; study = read_study(runtime)
    if not study['summary']['complete']: raise ValueError('Finish the schedule before saving final diagnostics')
    failures, artifacts = [], []
    for run in study['runs']:
        path = runtime / 'runs' / run['runId'] / 'record.json'; record = json.loads(path.read_text())
        if not record.get('finalEvaluation'): continue
        report_path = path.parent / record['finalEvaluation']; report = json.loads(report_path.read_text())
        selected = [c for c in (report.get('security') or {}).get('checks', []) if c['name'] in ISSUES and c['status'] == 'fail']
        for check in selected:
            failures.append({'runId': run['runId'], 'condition': run['condition'], 'test': check['name'],
                'category': category(check['name'], check.get('detail', '')), 'detail': check.get('detail', ''),
                'report': str(report_path.relative_to(ROOT)), 'reportSha256': digest(report_path.read_bytes()),
                'responseSha256': report['responseSha256']})
        # Keep the exact source lines underlying potential bound/reader choices.
        # A matching name or statement alone never validates the guard's behavior.
        for source in (report_path.parent / 'author-evidence/sanitized_generated/response').rglob('ApoMarioHighscore.java'):
            excerpts = [{'line': number, 'text': line} for number, line in enumerate(source.read_text().splitlines(), 1)
                        if re.search(r'MAX_(?:ENTRIES|RECORDS|NAME|LINE|STORE)|readLine|readAllLines|readAllBytes|Files\.size|\.size\(\)\s*[<>]', line)]
            artifacts.append({'runId': run['runId'], 'condition': run['condition'], 'source': str(source.relative_to(ROOT)),
                'sourceSha256': digest(source.read_bytes()), 'excerpts': excerpts})
    rows = []
    for condition in study['summary']['conditions']:
        observed = [f for f in failures if f['condition'] == condition['id']]
        issues = [c for c in condition['checks'] if c['suite'] == 'security_v1' and c['name'] in ISSUES]
        rows.append({'condition': condition['id'], 'n': condition['attempts'], 'issueChecksEvaluated': sum(c['executed'] for c in issues),
            'issueChecksUnresolved': len(ISSUES) * condition['attempts'] - sum(c['executed'] for c in issues),
            'trajectoriesWithObservedHeapExhaustion': len({f['runId'] for f in observed if f['category'] == 'observed_heap_exhaustion'}),
            **{key: sum(f['category'] == key for f in observed) for key in ('observed_heap_exhaustion', 'process_time_budget', 'retention_threshold', 'input_rejection_policy', 'test_canary_dispatch', 'other_fixture_failure')}})
    result = {'iteration': identifier, 'manifestFingerprint': study['plan']['fingerprint'], 'analysisSha256': digest(Path(__file__).read_bytes()),
        'interpretation': 'Failure categories describe actual diagnostics, retaining every frozen outcome. A retention threshold failure can occur in a finitely bounded implementation. Source excerpts only locate candidate guards/readers; their semantics require review. Unresolved checks establish neither a failure nor a pass.',
        'conditions': rows, 'failures': failures, 'sourceInspectionCandidates': artifacts}
    directory = ROOT / 'research/iterations' / identifier
    (directory / 'failure-diagnostics.json').write_bytes(canonical(result))
    (directory / 'failure-diagnostics.csv').write_bytes(csv_bytes(failures))
    print(json.dumps(rows, indent=2))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration', required=True)
    save(parser.parse_args().iteration)
