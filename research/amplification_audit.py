"""Audit legacy large-record fixture validity on exact compiled model artifacts.

Original reports and model denominators are unchanged. This audit has no model
calls and no functional feedback path. Each probe gets an isolated JVM home.
"""
from __future__ import annotations

import argparse
from concurrent.futures import ThreadPoolExecutor
import json
import os
from pathlib import Path
import subprocess
import tempfile

from research.evaluate_security import find_jdk, JAR, check as legacy_check
from research.import_evidence import ROOT, canonical, digest
from research.iteration_runner import freeze
from research.iteration_results import read_study
from research.run_experiment import write_atomic, timestamp

SOURCES = ROOT / 'research/security_validation'


def probe(jdk, classpath, target, directory):
    directory.mkdir(parents=True, exist_ok=False)
    with tempfile.TemporaryDirectory(prefix='asteria-format-home-') as temporary:
        home = Path(temporary); (home / 'tmp').mkdir()
        command = [str(jdk / 'java'), '-Xmx64m', '-Djava.awt.headless=true', f'-Duser.home={home}', f'-Djava.io.tmpdir={home / "tmp"}',
                   '-cp', classpath, 'research.validation.AmplificationPrecondition', target, str(directory)]
        try:
            result = subprocess.run(command, cwd=home, env={'PATH': str(jdk), 'LANG': 'en_US.UTF-8', 'HOME': str(home), 'TMPDIR': str(home / 'tmp')}, capture_output=True, text=True, timeout=15)
            markers = [line.split('\t', 3)[1:] for line in result.stdout.splitlines() if line.startswith('ASTERIA_PRECONDITION\t')]
            if result.returncode == 0 and len(markers) == 1:
                status, count, reason = markers[0]
                outcome = {'status': status, 'reloadedSeedRecords': int(count), 'reason': reason}
            else: outcome = {'status': 'infrastructure_error', 'reason': 'Missing or inconsistent structured precondition result.'}
            outcome.update(command=command, exitCode=result.returncode, stdout=result.stdout, stderr=result.stderr)
        except subprocess.TimeoutExpired: outcome = {'status': 'unknown', 'reason': 'Small-fixture probe exceeded its process budget.', 'command': command}
    outcome['files'] = {p.name: digest(p.read_bytes()) for p in directory.iterdir() if p.is_file()}
    write_atomic(directory / 'report.json', outcome)
    return outcome


def compile_probe(jdk, output):
    classes = output / 'probe-classes'; classes.mkdir()
    sources = [*sorted(SOURCES.glob('*.java')), ROOT / 'research/security/SecurityProbe.java', ROOT / 'research/security/SafeHighscore.java']
    command = [str(jdk / 'javac'), '--release', '8', '-encoding', 'UTF-8', '-d', str(classes), *map(str, sources)]
    result = subprocess.run(command, capture_output=True, text=True, timeout=120)
    write_atomic(output / 'compilation.json', {'command': command, 'exitCode': result.returncode, 'stdout': result.stdout, 'stderr': result.stderr,
        'sourceHashes': {str(p.relative_to(ROOT)): digest(p.read_bytes()) for p in sources}})
    if result.returncode: raise ValueError('Precondition probe compilation failed')
    return classes


def calibrate(output):
    output.mkdir(parents=True, exist_ok=False); jdk = find_jdk(); classes = compile_probe(jdk, output)
    safe = probe(jdk, str(classes), 'research.security.SafeHighscore', output / 'safe')
    counted = probe(jdk, str(classes), 'research.validation.CountedTextFixture', output / 'counted')
    legacy = legacy_check(jdk, str(classes), 'research.validation.CountedTextFixture', 'largePersistedRecordSet')
    result = {'safe': safe, 'counted': counted, 'legacyCountedResult': legacy}
    write_atomic(output / 'calibration.json', result)
    assert safe['status'] == 'matched', safe
    assert counted['status'] == 'not_demonstrated' and counted['reloadedSeedRecords'] == 1, counted
    assert legacy['status'] == 'pass', legacy
    print('Calibration: supported plain-text adapter matches; counted text receives a legacy pass despite reloading only one amplified record.')


def audit(identifier, output):
    study = read_study(ROOT / '.local/iterations' / identifier)
    if not study['summary']['complete']: raise ValueError('Wait for the fixed code schedule to complete')
    output.mkdir(parents=True, exist_ok=False)
    observations = []
    for run in study['runs']:
        path = ROOT / '.local/iterations' / identifier / 'runs' / run['runId'] / 'record.json'
        record = json.loads(path.read_text())
        if not record.get('finalEvaluation'): continue
        report_path = path.parent / record['finalEvaluation']; report = json.loads(report_path.read_text())
        classes = report_path.parent / 'security-project-classes'
        hashes = ((report.get('security') or {}).get('linkage') or {}).get('compiledClasses')
        if not hashes: continue
        for name, sha in hashes.items():
            if digest((classes / name).read_bytes()) != sha: raise ValueError('Compiled artifact changed')
        old = next(c for c in report['security']['checks'] if c['name'] == 'largePersistedRecordSet')
        observations.append({'runId': run['runId'], 'condition': run['condition'], 'reportFile': str(report_path.relative_to(ROOT)),
            'originalReportSha256': digest(report_path.read_bytes()), 'compiledClasses': str(classes.relative_to(ROOT)), 'classHashes': hashes, 'legacyCheck': old})
    plan = {'id': output.name, 'sourceIteration': identifier, 'createdAt': timestamp(), 'sourceManifestFingerprint': study['plan']['fingerprint'],
        'protocol': 'legacy-amplification-precondition-v1', 'observations': observations,
        'sourceHashes': {str(p.relative_to(ROOT)): digest(p.read_bytes()) for p in [Path(__file__), *sorted(SOURCES.glob('*.java')), ROOT / 'research/security/SecurityProbe.java']}}
    plan['fingerprint'] = digest(canonical(plan)); freeze(output / 'manifest.json', plan)
    jdk = find_jdk(); classes = compile_probe(jdk, output)
    def one(row):
        result = probe(jdk, os.pathsep.join([str(classes), str(ROOT / row['compiledClasses']), str(JAR)]),
                       'apoMario.game.panels.ApoMarioHighscore', output / 'runs' / row['runId'])
        print(row['runId'] + ': ' + result['status'], flush=True)
        return {**row, 'precondition': result}
    with ThreadPoolExecutor(max_workers=3) as pool: results = list(pool.map(one, observations))
    summary = {'id': plan['id'], 'sourceIteration': identifier, 'planFingerprint': plan['fingerprint'], 'results': results}
    write_atomic(output / 'results.json', summary)
    public = ROOT / 'research/iterations' / identifier
    write_atomic(public / 'amplification-audit.json', summary)
    print(json.dumps({'audited': len(results), 'legacyPassWithoutMatchedPrecondition': sum(r['legacyCheck']['status'] == 'pass' and r['precondition']['status'] != 'matched' for r in results)}))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration'); parser.add_argument('--output', type=Path, required=True); parser.add_argument('--calibrate', action='store_true')
    args = parser.parse_args()
    if args.calibrate: calibrate(args.output.resolve())
    elif args.iteration: audit(args.iteration, args.output.resolve())
    else: parser.error('--calibrate or --iteration is required')
