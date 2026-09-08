"""Versioned evaluator wrapper: a fresh home/temp directory for every generated-code JVM.

The original evaluator and test contracts remain unchanged. This wrapper runs in
one interpreter per evaluation; do not call it concurrently from Python threads.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path
import subprocess
import tempfile
from unittest.mock import patch

from research import evaluate_response as original
from research.import_evidence import ROOT, canonical, digest
from research.run_experiment import write_atomic

PROTOCOL = 'highscore-response-v2-isolated-home'


def evaluate_response(text: str, output: Path, identity=None):
    wrapper_source = Path(__file__).read_bytes()
    executed = []
    run = subprocess.run
    def isolated(command, *args, **kwargs):
        if isinstance(command, (str, bytes)) or Path(command[0]).name != 'java': return run(command, *args, **kwargs)
        with tempfile.TemporaryDirectory(prefix='asteria-jvm-') as directory:
            home, temporary = Path(directory) / 'home', Path(directory) / 'tmp'
            home.mkdir(); temporary.mkdir()
            filtered = [arg for arg in command[1:] if not str(arg).startswith(('-Duser.home=', '-Djava.io.tmpdir='))]
            actual = [command[0], f'-Duser.home={home}', f'-Djava.io.tmpdir={temporary}', *filtered]
            # The original evaluator supplies an allowlisted environment; retain it.
            kwargs['env'] = {**kwargs.get('env', {}), 'HOME': str(home), 'TMPDIR': str(temporary), 'XDG_CACHE_HOME': str(home / '.cache')}
            kwargs.setdefault('cwd', directory)
            executed.append({'originalCommand': list(command), 'command': actual, 'home': str(home), 'temporary': str(temporary)})
            return run(actual, *args, **kwargs)
    with patch.object(subprocess, 'run', side_effect=isolated):
        report = original.evaluate_response(text, output, identity)
    report['protocol'] = PROTOCOL
    report['inputHashes']['research/evaluate_isolated.py'] = digest(wrapper_source)
    (output / 'evaluator-wrapper.py').write_bytes(wrapper_source)
    report['environment']['jvmFilesystemIsolation'] = 'Fresh user.home, HOME and java.io.tmpdir for every functional and security-probe JVM; original working directories and test contracts retained.'
    report['isolatedJvms'] = executed
    remaining = list(executed)
    for process in report.get('processes', []):
        matching = next((item for item in remaining if item['originalCommand'] == process['command']), None)
        if matching:
            process['command'] = matching['command']
            remaining.remove(matching)
    if report.get('security'):
        report['security']['environmentRevision'] = 'isolated-home-v2'
    write_atomic(output / 'report.json', report)
    return report


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--response', required=True, type=Path)
    parser.add_argument('--output', required=True, type=Path)
    args = parser.parse_args()
    raw = args.response.read_bytes()
    report = evaluate_response(raw.decode(), args.output, {'inputKind': 'saved-source-reevaluation', 'inputFileSha256': digest(raw)})
    print(json.dumps({'status': report['status'], 'functionalSuccess': report['functionalSuccess'], 'functionalPasses': sum(c['status'] == 'pass' for c in report['checks']),
                      'securityFailures': sum(c['status'] == 'fail' for c in (report.get('security') or {}).get('checks', [])), 'isolatedJvms': len(report['isolatedJvms'])}))
    if report['status'] != 'evaluated': raise SystemExit(1)
