"""Repeat the predeclared I06/I07 artifacts without model calls or source changes."""
from __future__ import annotations

import argparse
from concurrent.futures import ThreadPoolExecutor
import fcntl
import json
import os
from pathlib import Path
import subprocess
import sys

from research.amplification_audit import compile_probe, probe
from research.evaluate_integrated import evaluate_response
from research.evaluate_security import find_jdk, JAR
from research.import_evidence import ROOT, canonical, digest
from research.iteration_runner import freeze, validate
from research.iteration_results import read_study
from research.qualification import apply_available, NAME
from research.run_experiment import timestamp, write_atomic
from research.scientific_summary import csv_bytes
from research.study_results import TESTS

IDENTIFIER = 'i08-evaluator-repeatability'
PARENTS = ('i06-operational-guards', 'i07-operational-replication')


def checks_by_name(checks):
    return {t['suite'] + '.' + t['name']: next((c['status'] for c in checks if c['suite'] == t['suite'] and c['name'] == t['name']), 'not_run') for t in TESTS}


def measures(report, precondition):
    raw = checks_by_name(report.get('checks', []) + (report.get('security') or {}).get('checks', [])); qualified = dict(raw)
    key = 'security_v1.' + NAME
    if qualified[key] in ('pass', 'fail') and precondition.get('status') != 'matched': qualified[key] = 'unknown'
    return {'compilation': report.get('mainCompilation'), 'functionalSuccess': report.get('functionalSuccess'), 'rawChecks': raw, 'qualifiedChecks': qualified}


def prepare():
    directory = ROOT / '.local/iterations' / IDENTIFIER
    if (directory / 'manifest.json').exists(): raise ValueError('Repeatability plan already frozen')
    selected, sources, parent_fingerprints = [], {}, {}
    for identifier in PARENTS:
        runtime = ROOT / '.local/iterations' / identifier; original = validate(runtime / 'manifest.json')
        if original.get('evaluationProtocol') != 'highscore-response-v3-integrated-security': raise ValueError('Unexpected source evaluator')
        study = read_study(runtime)
        if not study['summary']['complete']: raise ValueError('Complete both source rounds before preparing repeatability')
        study, qualification = apply_available(study)
        parent_fingerprints[identifier] = original['fingerprint']
        for name, sha in original['sourceHashes'].items():
            if name in sources and sources[name] != sha: raise ValueError('Source rounds used inconsistent inputs')
            sources[name] = sha
        for artifact in (runtime / 'manifest.json', ROOT / 'research/iterations' / identifier / 'amplification-audit.json'):
            sources[str(artifact.relative_to(ROOT))] = digest(artifact.read_bytes())
        for condition in original['conditions']:
            row = next(r for r in original['schedule'] if r['condition'] == condition['id'] and r['repetition'] == 1)
            path = runtime / 'runs' / row['runId'] / 'record.json'; record = json.loads(path.read_text())
            sources[str(path.relative_to(ROOT))] = digest(path.read_bytes())
            item = {'sourceIteration': identifier, 'runId': row['runId'], 'condition': row['condition'], 'source': None,
                    'sourceSha256': None, 'originalReport': None, 'expected': None, 'unavailableReason': None}
            if record.get('finalEvaluation'):
                report_path = path.parent / record['finalEvaluation']; report = json.loads(report_path.read_text())
                source = report_path.parent.parent / 'complete-files.txt'
                if digest(source.read_bytes()) != report['responseSha256']: raise ValueError('Original delivered source differs')
                for artifact in (source, report_path): sources[str(artifact.relative_to(ROOT))] = digest(artifact.read_bytes())
                run = next(r for r in study['runs'] if r['runId'] == row['runId'])
                raw = report['checks'] + (report.get('security') or {}).get('checks', [])
                item.update(source=str(source.relative_to(ROOT)), sourceSha256=report['responseSha256'], originalReport=str(report_path.relative_to(ROOT)),
                    expected={'compilation': report.get('mainCompilation'), 'functionalSuccess': run['functionalSuccess'],
                              'rawChecks': checks_by_name(raw), 'qualifiedChecks': checks_by_name(run['checks'])},
                    sanitizedHashes=report['sanitizedHashes'], compiledClasses=((report.get('security') or {}).get('linkage') or {}).get('compiledClasses'))
            else: item['unavailableReason'] = 'Selected trajectory has no final evaluated source; no replacement selected.'
            selected.append(item)
    if len(selected) != 32: raise ValueError('Expected the 32 predeclared artifact slots')
    directory.mkdir(parents=True, exist_ok=True); preparation = directory / 'precondition-tools'; preparation.mkdir()
    classes = compile_probe(find_jdk(), preparation)
    for source in [Path(__file__), ROOT / 'research/amplification_audit.py', ROOT / 'research/qualification.py', *classes.rglob('*.class')]:
        sources[str(source.relative_to(ROOT))] = digest(source.read_bytes())
    plan = {'id': IDENTIFIER, 'protocol': 'fixed-artifact-repeatability-v1', 'createdAt': timestamp(), 'modelCalls': 0,
        'parentFingerprints': parent_fingerprints, 'selected': selected, 'workers': 3, 'probeClasses': str(classes.relative_to(ROOT)),
        'schedule': [{'id': f'{item["runId"]}__repeat-{number}', 'runId': item['runId'], 'repeat': number} for item in selected for number in (1, 2)],
        'sourceHashes': sources, 'meaning': 'Original measurement plus two repeats of each predeclared source artifact. No new model sample and no outcome replacement. Missing artifacts and measurements stay explicit.'}
    plan['fingerprint'] = digest(canonical(plan)); freeze(directory / 'manifest.json', plan)
    freeze(ROOT / 'research/iterations' / IDENTIFIER / 'plan.json', plan)
    print(f'{len(selected)} selected artifact slots; {len(plan["schedule"])} planned repeat slots; zero model calls')
    return plan


def one(repeat_id):
    directory = ROOT / '.local/iterations' / IDENTIFIER; plan = validate(directory / 'manifest.json')
    row = next(r for r in plan['schedule'] if r['id'] == repeat_id); item = next(r for r in plan['selected'] if r['runId'] == row['runId'])
    output = directory / 'repeats' / repeat_id; output.mkdir(parents=True, exist_ok=False)
    result = {**row, 'planFingerprint': plan['fingerprint'], 'status': 'started', 'startedAt': timestamp()}
    write_atomic(output / 'record.json', result)
    try:
        if item['source'] is None:
            result.update(status='unavailable', reason=item['unavailableReason']); return
        raw = (ROOT / item['source']).read_bytes()
        if digest(raw) != item['sourceSha256']: raise ValueError('Selected source changed')
        report = evaluate_response(raw.decode(), output / 'evaluation', {'iteration': IDENTIFIER, 'sourceRunId': item['runId'], 'repeat': row['repeat']})
        result['reportSha256'] = digest((output / 'evaluation/report.json').read_bytes())
        if report['responseSha256'] != item['sourceSha256']: raise ValueError('Repeated input changed')
        compiled = ((report.get('security') or {}).get('linkage') or {}).get('compiledClasses')
        precondition = {'status': 'unknown', 'reason': 'No compiled artifact available for the precondition probe.'}
        if compiled:
            for name, sha in compiled.items():
                if digest((output / 'evaluation/security-project-classes' / name).read_bytes()) != sha: raise ValueError('Compiled artifact changed before precondition audit')
            classpath = os.pathsep.join([str(ROOT / plan['probeClasses']), str(output / 'evaluation/security-project-classes'), str(JAR)])
            precondition = probe(find_jdk(), classpath, 'apoMario.game.panels.ApoMarioHighscore', output / 'precondition')
        result.update(status='evaluated', sanitizedSourceMatches=report['sanitizedHashes'] == item['sanitizedHashes'],
            compiledClassesMatch=compiled == item['compiledClasses'], precondition=precondition, measures=measures(report, precondition))
    except BaseException as error:
        result.update(status='evaluation_error', errorType=type(error).__name__, reason=str(error)); raise
    finally:
        result['finishedAt'] = timestamp(); write_atomic(output / 'record.json', result)
        print(repeat_id + ': ' + result['status'], flush=True)


def execute():
    directory = ROOT / '.local/iterations' / IDENTIFIER; plan = validate(directory / 'manifest.json')
    with (directory / '.repeatability.lock').open('a') as lock:
        fcntl.flock(lock, fcntl.LOCK_EX | fcntl.LOCK_NB)
        pending = [r for r in plan['schedule'] if not (directory / 'repeats' / r['id']).exists()]
        def run(row):
            result = subprocess.run([sys.executable, '-u', '-m', 'research.repeatability', '--repeat-id', row['id']], cwd=ROOT, capture_output=True, text=True)
            (directory / (row['id'] + '.log')).write_text(result.stdout + result.stderr)
            print(result.stdout.strip() or row['id'] + ': exit ' + str(result.returncode), flush=True)
        with ThreadPoolExecutor(max_workers=plan['workers']) as pool: list(pool.map(run, pending))


def summarize():
    directory = ROOT / '.local/iterations' / IDENTIFIER; plan = validate(directory / 'manifest.json'); repeats, transitions = [], []
    for row in plan['schedule']:
        path = directory / 'repeats' / row['id'] / 'record.json'
        if not path.exists(): raise ValueError('Complete every repeat slot before final reporting')
        record = json.loads(path.read_text())
        if record['status'] == 'started' or record['planFingerprint'] != plan['fingerprint']: raise ValueError('Incomplete or mismatched repeat')
        if record['status'] == 'evaluated':
            if digest((path.parent / 'evaluation/report.json').read_bytes()) != record['reportSha256']: raise ValueError('Repeated report changed')
            report = json.loads((path.parent / 'evaluation/report.json').read_text())
            if record['measures'] != measures(report, record['precondition']): raise ValueError('Repeated measures differ from source report')
            precondition_path = path.parent / 'precondition/report.json'
            if precondition_path.exists() and canonical(json.loads(precondition_path.read_text())) != canonical(record['precondition']): raise ValueError('Repeated precondition differs')
            item = next(s for s in plan['selected'] if s['runId'] == row['runId'])
            for measurement in ('rawChecks', 'qualifiedChecks'):
                for test, before in item['expected'][measurement].items():
                    after = record['measures'][measurement][test]
                    if before != after: transitions.append({'repeatId': row['id'], 'runId': row['runId'], 'measurement': measurement, 'test': test, 'before': before, 'after': after})
            for key in ('compilation', 'functionalSuccess'):
                if item['expected'][key] != record['measures'][key]: transitions.append({'repeatId': row['id'], 'runId': row['runId'], 'measurement': key, 'test': None, 'before': item['expected'][key], 'after': record['measures'][key]})
        repeats.append(record)
    stability = []
    for test in TESTS:
        name = test['suite'] + '.' + test['name']; complete, stable, unresolved = 0, 0, 0
        for item in plan['selected']:
            rows = [r for r in repeats if r['runId'] == item['runId']]
            if not item['expected'] or any(r['status'] != 'evaluated' or not r['sanitizedSourceMatches'] or not r['compiledClassesMatch'] for r in rows): unresolved += 1; continue
            values = [item['expected']['qualifiedChecks'][name], *[r['measures']['qualifiedChecks'][name] for r in rows]]
            if any(v not in ('pass', 'fail') for v in values): unresolved += 1; continue
            complete += 1; stable += len(set(values)) == 1
        stability.append({'test': name, 'selectedArtifacts': len(plan['selected']), 'completePassFailTriplets': complete, 'stableTriplets': stable, 'unresolvedOrUnavailableArtifacts': unresolved})
    result = {'id': IDENTIFIER, 'planFingerprint': plan['fingerprint'], 'modelCalls': 0, 'repeats': repeats, 'transitions': transitions, 'stability': stability,
        'sourceMismatches': [r['id'] for r in repeats if r['status'] == 'evaluated' and (not r['sanitizedSourceMatches'] or not r['compiledClassesMatch'])]}
    public = ROOT / 'research/iterations' / IDENTIFIER
    write_atomic(directory / 'results.json', result); write_atomic(public / 'results.json', result)
    (public / 'stability.csv').write_bytes(csv_bytes(stability)); (public / 'transitions.csv').write_bytes(csv_bytes(transitions))
    print(f'{len(repeats)} repeat slots; {len(transitions)} measurement transitions; {len(result["sourceMismatches"])} source/class mismatches')
    return result


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--prepare', action='store_true'); parser.add_argument('--execute', action='store_true'); parser.add_argument('--summarize', action='store_true'); parser.add_argument('--repeat-id')
    args = parser.parse_args()
    if args.prepare: prepare()
    if args.repeat_id: one(args.repeat_id)
    if args.execute: execute()
    if args.summarize: summarize()
