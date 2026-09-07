"""Evaluate a received response with original functional suites and security v1.

Writes a new local report; never edits the response or historical results.
"""
from __future__ import annotations

import argparse
import contextlib
import io
import json
import os
import re
import subprocess
import tempfile
from pathlib import Path

from research.import_evidence import ROOT, TEST_NAMES, canonical, digest
from research import evaluate_security as security
from research.model_adapter import validate_response
from research.run_experiment import frozen_manifest, timestamp, write_atomic
from experiments.vamos_security_pilot import run_pilot as legacy


def stable_import_index(gr):
    """Never resolve a duplicate simple class name by filesystem traversal order."""
    indexes, ambiguities = {}, {}
    for game, directory in gr.GAME_PROJECTS.items():
        candidates = {}
        root = Path(directory)
        for path in sorted(root.rglob('*.java')):
            qualified = '.'.join(path.relative_to(root).with_suffix('').parts)
            candidates.setdefault(path.stem, set()).add(qualified)
        indexes[game] = {name: next(iter(values)) for name, values in candidates.items() if len(values) == 1}
        ambiguities[game] = {name: sorted(values) for name, values in candidates.items() if len(values) > 1}
    gr.import_index_per_game = indexes
    return ambiguities


def junit_checks(suite: str, output: str, exit_code: int | None) -> list[dict]:
    """Only infer unnamed passes after a consistent complete JUnit execution."""
    names = TEST_NAMES[suite]
    ok = re.search(r'OK\s*\((\d+)\s+tests?\)', output)
    failure = re.search(r'Tests run:\s*(\d+),\s*Failures:\s*(\d+)', output)
    failures = dict(re.findall(r'^\d+\)\s+(\w+)\([^\n]+\)\n([^\n]*)', output, re.M))
    complete = bool((ok and int(ok[1]) == len(names) and exit_code == 0 and not failures) or
                    (failure and int(failure[1]) == len(names) and int(failure[2]) > 0 and int(failure[2]) == len(failures) and
                     set(failures).issubset(names) and exit_code not in (None, 0)))
    return [{'suite': suite, 'name': name,
             'status': 'fail' if name in failures else ('pass' if complete else 'unknown'),
             'detail': failures.get(name, '' if complete else 'No consistent complete JUnit result; no pass inferred.')}
            for name in names]


def response_from_observation(path: Path, manifest_path: Path) -> tuple[str, dict]:
    observation = json.loads(path.read_text())
    manifest = frozen_manifest(manifest_path)
    if observation.get('status') != 'completed' or observation.get('manifestFingerprint') != manifest['fingerprint']:
        raise ValueError('Observation must be completed with matching frozen settings and manifest')
    row = next((r for r in manifest['schedule'] if r['runId'] == observation.get('runId')), None)
    if not row or row['condition'] != observation.get('condition'): raise ValueError('Unknown scheduled attempt')
    condition = next(c for c in manifest['conditions'] if c['id'] == row['condition'])
    expected = {'protocol_version': 1, 'request_id': row['runId'], 'model': manifest['model'],
                'messages': [{'role': 'user', 'content': (manifest_path.parent / condition['promptFile']).read_text()}],
                'settings': {'reasoning_effort': manifest['reasoning'], 'temperature': manifest['temperature'],
                             'max_output_tokens': manifest['maxOutputTokens']}}
    if observation.get('request') != expected or observation.get('requestSha256') != digest(canonical(expected)):
        raise ValueError('Observation request differs from frozen request')
    response = validate_response(observation['response'])
    if any(response.get(k) != expected[k] for k in ('model', 'request_id', 'settings')):
        raise ValueError('Response settings are unverified')
    return response['output_text'], {'runId': row['runId'], 'manifestFingerprint': manifest['fingerprint'], 'inputKind': 'model_observation'}


def evaluate_response(text: str, output: Path, identity: dict | None = None) -> dict:
    # Output creation is exclusive, so even an interrupted report is preserved.
    jdk = security.find_jdk()
    output = output.resolve()
    output.mkdir(parents=True, exist_ok=False)
    (output / 'response.txt').write_text(text)
    report = {'schemaVersion': 1, 'protocol': 'highscore-response-v1', 'status': 'started',
              'startedAt': timestamp(), 'inputKind': 'response_file', **(identity or {}),
              'responseSha256': digest(text.encode()), 'checks': [], 'functionalSuccess': None,
              'security': None, 'jointSuccess': None}
    inputs = [Path(__file__).resolve(), ROOT / 'research/evaluate_security.py', security.JAR,
              Path(legacy.__file__).resolve(), *sorted(security.SOURCES.glob('*.java')),
              *[ROOT / 'vamos-artifact/Pipeline' / p for p in ['run.py', 'autonomous_runner.py', 'invoked_runner.py', 'Features.csv', 'Prompts.csv', 'task_config.json']],
              *sorted((ROOT / 'vamos-artifact/Tests').glob('*Highscore*Test.java')),
              ROOT / 'vamos-artifact/Tests/IntegrationDriver.java', *sorted(legacy.LIB.glob('*.jar'))]
    distribution = ROOT / 'apogames/Java/ApoMario'
    inputs += [p for directory in ('levels', 'replay') for p in sorted((distribution / directory).rglob('*')) if p.is_file()]
    if (distribution / 'mario.properties').exists(): inputs.append(distribution / 'mario.properties')
    report['inputHashes'] = {str(p.relative_to(ROOT)): digest(p.read_bytes()) for p in inputs}
    report['environment'] = {'java': subprocess.check_output([str(jdk / 'java'), '-version'], stderr=subprocess.STDOUT, text=True).strip(),
                             'functionalHeap': '256 MiB', 'securityHeap': '64 MiB',
                             'displayConfigured': bool(os.environ.get('DISPLAY')),
                             'transformations': 'Original author integration and pilot package/import repairs, with ambiguous simple class names excluded from automatic import resolution; exact sanitized files and diffs saved. No model repair.'}
    write_atomic(output / 'report.json', report)
    calls = []
    with tempfile.TemporaryDirectory(prefix='asteria-functional-') as directory:
        work = Path(directory)
        class JavaProcess:
            TimeoutExpired = subprocess.TimeoutExpired
            @staticmethod
            def run(command, *args, **kwargs):
                command = list(command)
                if Path(command[0]).name == 'javac': command = [str(jdk / 'javac'), '--release', '8', *command[1:]]
                if Path(command[0]).name == 'java': command = [str(jdk / 'java'), '-Xmx256m', f'-Djava.io.tmpdir={work}', *command[1:]]
                kwargs['env'] = {'PATH': str(jdk) + os.pathsep + '/usr/bin:/bin', 'LANG': 'en_US.UTF-8'}
                for key in ('DISPLAY', 'XAUTHORITY'):
                    if os.environ.get(key): kwargs['env'][key] = os.environ[key]
                kwargs.setdefault('cwd', str(work))
                try:
                    result = subprocess.run(command, *args, **kwargs)
                    calls.append({'command': command, 'exitCode': result.returncode, 'stdout': result.stdout, 'stderr': result.stderr})
                    return result
                except subprocess.TimeoutExpired:
                    calls.append({'command': command, 'exitCode': None, 'stdout': '', 'stderr': 'Process timeout'})
                    raise
        original = {k: getattr(legacy, k) for k in ('HERE', 'OUTPUT', 'STATE_FILE', 'Java8SubprocessProxy')}
        try:
            legacy.HERE = work
            legacy.OUTPUT = output / 'author-evidence'
            legacy.STATE_FILE = output / 'author-state.json'
            legacy.Java8SubprocessProxy = JavaProcess
            with contextlib.redirect_stdout(io.StringIO()) as log:
                gr = legacy.configure_author_harness()
                gr.build_import_index = lambda: stable_import_index(gr)
                report['ambiguousImports'] = gr.build_import_index()
                auto, invoked = legacy.configure_supplementary(gr)
                files = gr.extract_java_files(text)
                if not files:
                    missing = lambda suite, names: [{'suite': suite, 'name': name, 'status': 'not_run', 'detail': 'No complete Java files extracted from response.'} for name in names]
                    report.update(status='evaluated', mainCompilation='fail', parseStatus='no_java', functionalSuccess=False, jointSuccess=False,
                                  checks=[c for suite in ('unit', 'invoked', 'autonomous') for c in missing(suite, TEST_NAMES[suite])],
                                  security={'protocol': security.PROTOCOL, 'status': 'not_run', 'checks': missing('security_v1', security.CHECKS), 'controls': None})
                    return report
                if any(not re.fullmatch(r'[A-Za-z_$][\w$]*\.java', name) for name in files): raise ValueError('Unsafe generated filename')
                sanitized = legacy.sanitize_and_persist(gr, files, 'response')
                report['sanitizedHashes'] = {p: digest(code.encode()) for p, code in sanitized.items()}
                main = gr.integrate_compile_test('Highscore', files, run_id='response')
                report['mainCompilation'] = 'pass' if main['compilation_success'] else 'fail'
                report['authorResults'] = {'unit': main}
                if main['compilation_success']:
                    report['authorResults']['invoked'] = invoked.run_invoked(files, 'Highscore', 'response')
                    report['authorResults']['autonomous'] = auto.run_autonomous(files, 'Highscore', 'response')
                suite_classes = {'unit': 'ApoMarioHighscoreTest', 'invoked': 'ApoMarioHighscoreCouplingTest', 'autonomous': 'ApoMarioHighscoreWiringTest'}
                for suite, cls in suite_classes.items():
                    call = next((c for c in reversed(calls) if 'org.junit.runner.JUnitCore' in c['command'] and c['command'][-1].endswith('.' + cls)), None)
                    if call:
                        report['checks'].extend(junit_checks(suite, (call['stdout'] or '') + (call['stderr'] or ''), call['exitCode']))
                    else:
                        report['checks'].extend({'suite': suite, 'name': name, 'status': 'not_run', 'detail': 'No test execution; inspect compiler and harness diagnostics.'} for name in TEST_NAMES[suite])
                report['functionalSuccess'] = report['mainCompilation'] == 'pass' and all(t['status'] == 'pass' for t in report['checks'])
                controls = security.evaluate(work / 'controls.json', controls_only=True)
                source_root = output / 'author-evidence/sanitized_generated/response'
                sources = [p for p in source_root.rglob('ApoMarioHighscore*.java') if p.name != 'ApoMarioHighscorePanel.java']
                compiled = security.compile_sources(jdk, [security.SOURCES / 'SecurityProbe.java', *sources], work / 'feature/classes')
                observed = [security.check(jdk, str(work / 'feature/classes') + os.pathsep + str(security.JAR), 'apoMario.game.panels.ApoMarioHighscore', name) for name in security.CHECKS] if compiled.returncode == 0 and sources else []
                if not observed:
                    observed = [{'suite': 'security_v1', 'name': name, 'status': 'compile_error', 'detail': compiled.stderr or 'No Highscore class extracted'} for name in security.CHECKS]
                statuses = {c['status'] for c in observed}
                report['security'] = {'protocol': security.PROTOCOL, 'status': 'pass' if statuses == {'pass'} else ('fail' if 'fail' in statuses else 'unknown'), 'checks': observed, 'controls': controls}
                report['jointSuccess'] = report['functionalSuccess'] and report['security']['status'] == 'pass'
                report['status'] = 'evaluated'
            (output / 'harness.log').write_text(log.getvalue())
        except Exception as error:
            report.update(status='evaluation_error', error=f'{type(error).__name__}: {error}')
        finally:
            for key, value in original.items(): setattr(legacy, key, value)
            report['processes'] = calls
            report['finishedAt'] = timestamp()
            write_atomic(output / 'report.json', report)
    return report


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    sources = parser.add_mutually_exclusive_group(required=True)
    sources.add_argument('--response', type=Path)
    sources.add_argument('--observation', type=Path)
    parser.add_argument('--manifest', type=Path, default=ROOT / 'research/experiments/luna-highscore-v1/manifest.json')
    parser.add_argument('--output', type=Path, required=True, help='New directory; use .local/evaluations/...')
    args = parser.parse_args()
    source = args.observation or args.response
    text, identity = response_from_observation(source, args.manifest) if args.observation else (source.read_text(), {})
    report = evaluate_response(text, args.output, {**identity, 'inputFileSha256': digest(source.read_bytes())})
    print(json.dumps({k: report[k] for k in ('status', 'functionalSuccess', 'jointSuccess')}))
    if report['status'] != 'evaluated': raise SystemExit(1)


if __name__ == '__main__': main()
