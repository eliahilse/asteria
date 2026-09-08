"""Evaluator v3: isolated JVM homes and security probes on the compiled feature/game.

The original functional compiler, integration repairs and all test contracts stay
unchanged. Security probes use the exact successful main compilation, including
modified game classes. A failed game compilation cannot yield security passes.
"""
from __future__ import annotations

import argparse
import json
import os
from pathlib import Path
import shutil
import subprocess
from unittest.mock import patch

from research import evaluate_isolated as isolated
from research import evaluate_security as security
from research.import_evidence import ROOT, digest
from research.run_experiment import write_atomic

PROTOCOL = 'highscore-response-v3-integrated-security'


def evaluate_response(text: str, output: Path, identity=None):
    wrapper_source = Path(__file__).read_bytes()
    output = output.resolve()
    process_run, compile_sources = subprocess.run, security.compile_sources
    state = {'mainCompilationSeen': False, 'compiledClasses': None, 'probeCompilation': None}
    classes = output / 'security-project-classes'

    def capture(command, *args, **kwargs):
        result = process_run(command, *args, **kwargs)
        # Only the original author's first whole-project compilation, before
        # JUnit or supplementary suites add anything to its output directory.
        if not isinstance(command, (str, bytes)) and Path(command[0]).name == 'javac' and '-nowarn' in command and '-d' in command and not state['mainCompilationSeen']:
            state['mainCompilationSeen'] = True
            state['mainCompilationCommand'] = list(command)
            state['mainCompilationExitCode'] = result.returncode
            if result.returncode == 0:
                source = Path(command[command.index('-d') + 1])
                shutil.copytree(source, classes)
                state['compiledClasses'] = {str(p.relative_to(classes)): digest(p.read_bytes()) for p in sorted(classes.rglob('*.class'))}
        return result

    def compile_probe(jdk, sources, destination):
        if not any(Path(p).is_relative_to(output / 'author-evidence') for p in sources):
            return compile_sources(jdk, sources, destination)
        if not state['compiledClasses']:
            return subprocess.CompletedProcess([], 1, '', 'Whole game compilation did not succeed; no security observations inferred from a partial feature compilation.')
        destination.mkdir(parents=True, exist_ok=True)
        shutil.copytree(classes, destination, dirs_exist_ok=True)
        empty = destination.parent / 'empty-source'; empty.mkdir(exist_ok=True)
        command = [str(jdk / 'javac'), '--release', '8', '-encoding', 'UTF-8', '-sourcepath', str(empty),
                   '-cp', str(classes) + os.pathsep + str(security.JAR), '-d', str(destination), str(security.SOURCES / 'SecurityProbe.java')]
        compiled = subprocess.run(command, capture_output=True, text=True, timeout=120)
        state['probeCompilation'] = {'command': command, 'exitCode': compiled.returncode, 'stdout': compiled.stdout, 'stderr': compiled.stderr}
        return compiled

    with patch.object(subprocess, 'run', side_effect=capture), patch.object(security, 'compile_sources', side_effect=compile_probe):
        report = isolated.evaluate_response(text, output, identity)
    report['protocol'] = PROTOCOL
    report['inputHashes']['research/evaluate_integrated.py'] = digest(wrapper_source)
    (output / 'integrated-evaluator-wrapper.py').write_bytes(wrapper_source)
    report['environment']['securityClassPath'] = 'SecurityProbe plus the exact successful whole-game compilation, before test classes are added; original game jar remains the final classpath fallback.'
    if report.get('security'):
        report['security']['environmentRevision'] = 'isolated-home-integrated-game-v3'
        report['security']['linkage'] = state
    write_atomic(output / 'report.json', report)
    return report


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--response', required=True, type=Path)
    parser.add_argument('--output', required=True, type=Path)
    args = parser.parse_args(); raw = args.response.read_bytes()
    report = evaluate_response(raw.decode(), args.output, {'inputKind': 'saved-source-reevaluation', 'inputFileSha256': digest(raw)})
    checks = (report.get('security') or {}).get('checks', [])
    print(json.dumps({'status': report['status'], 'functionalSuccess': report.get('functionalSuccess'),
        'functionalPasses': sum(c['status'] == 'pass' for c in report['checks']), 'securityFailures': sum(c['status'] == 'fail' for c in checks),
        'securityEvaluated': sum(c['status'] in ('pass', 'fail') for c in checks), 'isolatedJvms': len(report['isolatedJvms'])}))
    if report['status'] != 'evaluated': raise SystemExit(1)
