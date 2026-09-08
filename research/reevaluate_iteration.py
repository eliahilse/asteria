"""Re-measure every saved I01 submission after changing only JVM home isolation.

No model calls, source repair, retries, or replacement of original observations.
Each evaluator gets its own interpreter because the legacy harness has globals.
"""
from __future__ import annotations

import argparse
from concurrent.futures import ThreadPoolExecutor
import fcntl
import json
from pathlib import Path
import re
import subprocess
import sys

from research.import_evidence import ROOT, canonical, digest
from research.iteration_runner import freeze, validate
from research.run_experiment import timestamp, write_atomic


def prepare(source: Path, identifier: str):
    if not re.fullmatch(r'[a-z0-9][a-z0-9_-]+', identifier): raise ValueError('Invalid iteration identifier')
    original = validate(source / 'manifest.json')
    directory = ROOT / '.local/iterations' / identifier
    observations = []
    hashes = {str(p.relative_to(ROOT)): digest(p.read_bytes()) for p in
              [Path(__file__), ROOT / 'research/evaluate_isolated.py', ROOT / 'research/evaluate_response.py',
               ROOT / 'research/evaluate_security.py', source / 'manifest.json']}
    for row in original['schedule']:
        run = source / 'runs' / row['runId']
        record = json.loads((run / 'record.json').read_text())
        for submission in record['submissions']:
            if not submission.get('evaluationFile'): continue
            report = run / submission['evaluationFile']
            parsed = json.loads(report.read_text())
            if digest(canonical(parsed)) != submission['evaluationCanonicalSha256']: raise ValueError('Original evaluation changed')
            response = report.parent.parent / 'complete-files.txt'
            if digest(response.read_bytes()) != submission['completeResponseSha256']: raise ValueError('Original source changed')
            for path in (run / 'record.json', report, response): hashes[str(path.relative_to(ROOT))] = digest(path.read_bytes())
            observations.append({**row, 'submission': submission['number'], 'final': submission['evaluationFile'] == record.get('finalEvaluation'),
                                 'source': str(response.relative_to(ROOT)), 'originalReport': str(report.relative_to(ROOT)),
                                 'output': f"evaluations/{row['runId']}/submission-{submission['number']}"})
    plan = {'id': identifier, 'sourceIteration': original['id'], 'sourceManifestFingerprint': original['fingerprint'],
            'createdAt': timestamp(), 'protocol': 'saved-code-home-isolation-comparison-v1',
            'change': 'Fresh user.home, HOME, java.io.tmpdir, TMPDIR, XDG_CACHE_HOME per JVM. Original source, tests, security compilation and budgets unchanged.',
            'analysis': 'Paired changes on every previously evaluated submission and on each trajectory’s last evaluated artifact. Original feedback paths and stopping decisions are retained; this cannot reconstruct trajectories under isolated feedback.',
            'observations': observations, 'sourceHashes': hashes}
    if (directory / 'manifest.json').exists(): plan['createdAt'] = json.loads((directory / 'manifest.json').read_text())['createdAt']
    plan['fingerprint'] = digest(canonical(plan)); freeze(directory / 'manifest.json', plan)
    return plan


def run(directory: Path, workers: int):
    plan = validate(directory / 'manifest.json')
    with (directory / '.reevaluation.lock').open('a') as lock:
        fcntl.flock(lock, fcntl.LOCK_EX | fcntl.LOCK_NB)
        def one(row):
            output = directory / row['output']
            if output.exists(): return
            process = subprocess.run([sys.executable, '-m', 'research.evaluate_isolated', '--response', str(ROOT / row['source']),
                                      '--output', str(output)], cwd=ROOT, capture_output=True, text=True)
            (directory / f"{row['runId']}-s{row['submission']}.log").write_text(process.stdout + process.stderr)
            print(f"{row['runId']} s{row['submission']}: {process.stdout.strip() or 'exit ' + str(process.returncode)}", flush=True)
        with ThreadPoolExecutor(max_workers=workers) as pool: list(pool.map(one, plan['observations']))


def summarize(directory: Path):
    plan = validate(directory / 'manifest.json'); pairs = []
    for row in plan['observations']:
        path = directory / row['output'] / 'report.json'
        if not path.exists(): continue
        before, after = json.loads((ROOT / row['originalReport']).read_text()), json.loads(path.read_text())
        if after['responseSha256'] != before['responseSha256']: raise ValueError('Paired sources differ')
        def measures(report):
            return {'status': report['status'], 'compiled': report.get('mainCompilation') == 'pass',
                    'fullFunctional': report.get('functionalSuccess'), 'functionalPasses': sum(c['status'] == 'pass' for c in report['checks']),
                    'checks': report['checks'] + (report.get('security') or {}).get('checks', [])}
        pairs.append({**row, 'before': measures(before), 'after': measures(after), 'afterReportSha256': digest(path.read_bytes())})
    result = {'id': plan['id'], 'planFingerprint': plan['fingerprint'], 'planned': len(plan['observations']), 'completed': len(pairs), 'pairs': pairs}
    public = ROOT / 'research/iterations' / plan['id']; public.mkdir(parents=True, exist_ok=True)
    freeze(public / 'plan.json', plan)
    write_atomic(directory / 'comparison.json', result); write_atomic(public / 'comparison.json', result)
    lines = [f"# {plan['id']}: same-code environment correction", '', plan['change'], '', plan['analysis'], '',
             f"Completed {len(pairs)}/{len(plan['observations'])} saved submissions. No new model calls.", '',
             '| Saved artifact | N | Original full pass | Isolated full pass | Fail → pass | Pass → fail |',
             '| --- | ---: | ---: | ---: | ---: | ---: |']
    for label, rows in [('Every evaluated submission', pairs), ('Last evaluated artifact per trajectory', [r for r in pairs if r['final']])]:
        a = sum(r['before']['fullFunctional'] is True for r in rows); b = sum(r['after']['fullFunctional'] is True for r in rows)
        improved = sum(r['before']['fullFunctional'] is not True and r['after']['fullFunctional'] is True for r in rows)
        regressed = sum(r['before']['fullFunctional'] is True and r['after']['fullFunctional'] is not True for r in rows)
        lines.append(f'| {label} | {len(rows)} | {a} | {b} | {improved} | {regressed} |')
    lines += ['', 'See comparison.json for every functional and security check before and after. Unknown checks remain unknown. These repeated measurements do not increase the number of independent model trajectories.', '']
    (public / 'README.md').write_text('\n'.join(lines))
    print('\n'.join(lines))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--id', default='i01-home-isolated')
    parser.add_argument('--source', type=Path, default=ROOT / '.local/iterations/i01-actionable-context')
    parser.add_argument('--prepare', action='store_true')
    parser.add_argument('--execute', action='store_true')
    parser.add_argument('--summarize', action='store_true')
    parser.add_argument('--workers', type=int, choices=range(1, 5), default=3)
    args = parser.parse_args(); directory = ROOT / '.local/iterations' / args.id
    if args.prepare: print(json.dumps({'observations': len(prepare(args.source, args.id)['observations'])}))
    if args.execute: run(directory, args.workers)
    if args.summarize: summarize(directory)
