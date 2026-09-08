"""Expose functional assertion, bootstrap and runtime failures in saved evidence."""
import argparse
import json
from pathlib import Path
import re

from research.import_evidence import ROOT, canonical, digest
from research.iteration_results import read_study
from research.scientific_summary import csv_bytes

SUITES = {'ApoMarioHighscoreTest': 'unit', 'ApoMarioHighscoreCouplingTest': 'invoked', 'ApoMarioHighscoreWiringTest': 'autonomous'}


def classify(call):
    output = (call.get('stdout') or '') + (call.get('stderr') or '')
    match = re.search(r'Tests run:\s*(\d+)', output)
    executed = int(match.group(1)) if match else None
    if call['exitCode'] is None: category = 'process_timeout'
    elif executed == 0 and call['exitCode'] != 0: category = 'suite_bootstrap_failure'
    elif 'java.lang.AssertionError' in output: category = 'functional_assertion_failure'
    elif re.search(r'(?:Exception|Error)(?::|\n)', output): category = 'runtime_exception'
    else: category = 'incomplete_or_inconsistent_junit_output'
    return category, executed


def save(identifier):
    runtime = ROOT / '.local/iterations' / identifier; study = read_study(runtime)
    if not study['summary']['complete']: raise ValueError('Complete the fixed schedule before final diagnostics')
    rows = []
    for run in study['runs']:
        if run['functionalSuccess']: continue
        record_path = runtime / 'runs' / run['runId'] / 'record.json'; record = json.loads(record_path.read_text())
        if not record.get('finalEvaluation'): continue
        report_path = record_path.parent / record['finalEvaluation']; report = json.loads(report_path.read_text())
        for call in report.get('processes', []):
            command = call['command']
            if 'org.junit.runner.JUnitCore' not in command: continue
            suite = SUITES.get(command[-1].split('.')[-1])
            checks = [c for c in report['checks'] if c['suite'] == suite]
            if not checks or all(c['status'] == 'pass' for c in checks): continue
            category, executed = classify(call)
            rows.append({'runId': run['runId'], 'condition': run['condition'], 'submission': report['submission'],
                'suite': suite, 'category': category, 'exitCode': call['exitCode'], 'junitReportedTestsRun': executed,
                'passedChecks': sum(c['status'] == 'pass' for c in checks), 'failedChecks': sum(c['status'] == 'fail' for c in checks),
                'unresolvedChecks': sum(c['status'] not in ('pass', 'fail') for c in checks),
                'report': str(report_path.relative_to(ROOT)), 'reportSha256': digest(report_path.read_bytes()),
                'stdout': call['stdout'], 'stderr': call['stderr']})
    result = {'iteration': identifier, 'manifestFingerprint': study['plan']['fingerprint'],
        'analysisSha256': digest(Path(__file__).read_bytes()), 'failures': rows,
        'meaning': 'Diagnostics of the original final functional JUnit processes; no outcome or feedback is modified. A bootstrap failure may leave all named tests unexecuted rather than failed. Compilations and missing delivered artifacts remain represented by the full result table. These diagnostic categories describe process evidence, not security vulnerabilities.'}
    directory = ROOT / 'research/iterations' / identifier
    (directory / 'functional-diagnostics.json').write_bytes(canonical(result))
    (directory / 'functional-diagnostics.csv').write_bytes(csv_bytes(rows))
    print(f'{identifier}: {len(rows)} unsuccessful functional suite processes retained with exact stdout/stderr and report hashes')
    return result


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration', required=True)
    save(parser.parse_args().iteration)
