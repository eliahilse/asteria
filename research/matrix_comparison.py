"""Side by side: the prior study's Highscore matrix and our one-response rerun of it.

Prior study: `research/prior-study/highscore-runs.json` (80 runs, Gemini 3.1
Flash Lite, one response; compilation and the seven unit tests per run).
Ours: the run records of a one-response round (default `i23-matrix`), per
cell: delivered (accepted by the delivery protocol), compiled, functional
(all sixteen tests), unit tests passed of 7N (the comparable number), invoked
and autonomous tests passed, and, when the round is published, issue checks
failed / unresolved / passed of 10N. Writes `comparison.md` and
`comparison.csv` into the round directory. No model calls.
"""
from __future__ import annotations

import argparse
import csv
import io
import json
from pathlib import Path

from research.import_evidence import ROOT

PRIOR = ROOT / 'research/prior-study/highscore-runs.json'
CONTEXTS = ['None', 'S', 'F', 'B', 'S+F', 'S+B', 'F+B', 'S+F+B']
CELL = {('Generation', c): f"generation_{c.lower().replace('+', '') if c != 'None' else 'none'}" for c in CONTEXTS}
CELL.update({('Reuse', c): f"reuse_{c.lower().replace('+', '') if c != 'None' else 'none'}" for c in CONTEXTS})


def prior_cells(path: Path = PRIOR) -> dict[tuple[str, str], dict]:
    out: dict[tuple[str, str], dict] = {}
    for r in json.loads(path.read_text())['runs']:
        a = out.setdefault((r['method'], r['context_label']), {'n': 0, 'compiled': 0, 'unitPassed': 0, 'unitRun': 0})
        a['n'] += 1; a['compiled'] += bool(r['compilation_success'])
        if r['compilation_success'] and r.get('tests_run'): a['unitPassed'] += r['tests_passed']; a['unitRun'] += r['tests_run']
    return out


def our_cells(iteration: str, root: Path = ROOT) -> dict[str, dict]:
    """Per cell: n, delivered, compiled, functional and per-suite pass counts from the local run records; without them (a clean checkout) the published qualified analysis supplies n, compiled and functional, the rest is None."""
    out: dict[str, dict] = {}
    runs = root / '.local/iterations' / iteration / 'runs'
    for record_path in sorted(runs.glob('*/record.json')):
        r = json.loads(record_path.read_text()); cell = r['condition'].split('__')[0]
        if r.get('status') in (None, 'started'): continue  # still collecting
        a = out.setdefault(cell, {'n': 0, 'delivered': 0, 'compiled': 0, 'functional': 0, 'unit': 0, 'invoked': 0, 'autonomous': 0})
        a['n'] += 1; a['functional'] += bool(r.get('functionalSuccess'))
        subs = [s for s in r.get('submissions', []) if s.get('status') == 'evaluated']
        if not subs: continue
        a['delivered'] += 1; s = subs[-1]; a['compiled'] += s.get('compilation') == 'pass'
        checks = (s.get('feedback') or {}).get('functionalChecks') or []
        if not checks and r.get('finalEvaluation') and (record_path.parent / r['finalEvaluation']).exists():
            checks = json.loads((record_path.parent / r['finalEvaluation']).read_text()).get('checks') or []
        for c in checks:
            if c.get('status') == 'pass' and c.get('suite') in a: a[c['suite']] += 1
    analysis = root / 'research/iterations' / iteration / 'qualified-analysis.json'
    if analysis.exists():
        for c in json.loads(analysis.read_text())['conditions']:
            cell = c['condition'].split('__')[0]
            if cell not in out:  # no local run records (a clean checkout, CI): the published analysis carries n, compiled and functional; the per-suite counts need the records
                out[cell] = {'n': c['n'], 'delivered': None, 'compiled': c['compiled'], 'functional': c['withinBudgetFull'], 'unit': None, 'invoked': None, 'autonomous': None, 'fromAnalysis': True}
            out[cell]['issues'] = (c['issues']['failed'], c['issues']['unresolved'], c['issues']['evaluated'] - c['issues']['failed'], c['issues']['plannedChecks'])
    return out


def rows(iteration: str, root: Path = ROOT, prior_path: Path = PRIOR) -> list[dict]:
    prior, ours = prior_cells(prior_path), our_cells(iteration, root); out = []
    for method in ('Generation', 'Reuse'):
        for ctx in CONTEXTS:
            p = prior.get((method, ctx), {}); o = ours.get(CELL[(method, ctx)], {})
            out.append({'method': method, 'context': ctx, 'cell': CELL[(method, ctx)],
                        'priorN': p.get('n', 0), 'priorCompiled': p.get('compiled', 0), 'priorUnitPassed': p.get('unitPassed', 0), 'priorUnitRun': p.get('unitRun', 0),
                        'n': o.get('n', 0), 'delivered': o.get('delivered', 0), 'compiled': o.get('compiled', 0), 'functional': o.get('functional', 0),
                        'unit': o.get('unit', 0), 'invoked': o.get('invoked', 0), 'autonomous': o.get('autonomous', 0), 'issues': o.get('issues')})
    return out


def render(table: list[dict], iteration: str) -> str:
    lines = [f'# {iteration}: the prior study\'s Highscore matrix and our one-response rerun', '',
             'Prior study: Gemini 3.1 Flash Lite, one response per prompt, its pipeline repairs imports and placement before compiling; unit tests are the seven `ApoMarioHighscoreTest` checks, counted over compiled runs only. '
             'Ours: Luna, one response, exact-edit delivery (a rejected delivery counts as not compiled), reasoning effort as declared in the round; unit / invoked / autonomous are the 7, 4 and 5 functional tests per trajectory, counted over all N; functional means all sixteen pass; issue checks are failed / unresolved / passed of 10N.', '',
             '| Method | Context | Prior compiled | Prior unit tests (compiled runs) | Ours delivered | Ours compiled | Ours functional | Unit of 7N | Invoked of 4N | Autonomous of 5N | Issue checks f / u / p |',
             '| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | --- |']
    for r in table:
        issues = f"{r['issues'][0]} / {r['issues'][1]} / {r['issues'][2]} of {r['issues'][3]}" if r['issues'] else 'not published'
        n = r['n'] or 0
        lines.append(f"| {r['method']} | {r['context']} | {r['priorCompiled']} of {r['priorN']} | {r['priorUnitPassed']} of {r['priorUnitRun']} | {r['delivered']} of {n} | {r['compiled']} of {n} | {r['functional']} of {n} | {r['unit']} of {7*n} | {r['invoked']} of {4*n} | {r['autonomous']} of {5*n} | {issues} |")
    for method in ('Generation', 'Reuse'):
        sub = [r for r in table if r['method'] == method]
        lines.append(f"| **{method} total** | | {sum(r['priorCompiled'] for r in sub)} of {sum(r['priorN'] for r in sub)} | {sum(r['priorUnitPassed'] for r in sub)} of {sum(r['priorUnitRun'] for r in sub)} | {sum(r['delivered'] for r in sub)} of {sum(r['n'] for r in sub)} | {sum(r['compiled'] for r in sub)} of {sum(r['n'] for r in sub)} | {sum(r['functional'] for r in sub)} of {sum(r['n'] for r in sub)} | {sum(r['unit'] for r in sub)} of {7*sum(r['n'] for r in sub)} | {sum(r['invoked'] for r in sub)} of {4*sum(r['n'] for r in sub)} | {sum(r['autonomous'] for r in sub)} of {5*sum(r['n'] for r in sub)} | |")
    return '\n'.join(lines) + '\n'


def csv_text(table: list[dict]) -> str:
    buffer = io.StringIO(); writer = csv.writer(buffer)
    writer.writerow(['method', 'context', 'cell', 'priorN', 'priorCompiled', 'priorUnitPassed', 'priorUnitRun', 'n', 'delivered', 'compiled', 'functional', 'unitPassed', 'invokedPassed', 'autonomousPassed', 'issuesFailed', 'issuesUnresolved', 'issuesPassed', 'issuesPlanned'])
    for r in table:
        issues = r['issues'] or ('', '', '', '')
        writer.writerow([r['method'], r['context'], r['cell'], r['priorN'], r['priorCompiled'], r['priorUnitPassed'], r['priorUnitRun'], r['n'], r['delivered'], r['compiled'], r['functional'], r['unit'], r['invoked'], r['autonomous'], *issues])
    return buffer.getvalue()


def main():
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration', default='i23-matrix'); args = parser.parse_args()
    table = rows(args.iteration); public = ROOT / 'research/iterations' / args.iteration
    (public / 'comparison.md').write_text(render(table, args.iteration)); (public / 'comparison.csv').write_text(csv_text(table))
    print(render(table, args.iteration))


if __name__ == '__main__':
    main()
