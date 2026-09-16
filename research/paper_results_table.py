"""The paper's results table: one row per context strategy, all with the same document version, pooled over the same cells. No model calls.

Every row: N artifacts; Compiled (% of N); Tests passed (mean % of the sixteen functional tests per compiled artifact); Checks passed (mean % of the
ten security checks per compiled artifact; an unresolved check counts as not passed); All pass (% of N passing all sixteen tests and all eleven
checks); Input (mean generator, and judge, input tokens per artifact, millions); Minutes (mean model-call minutes per artifact).
Sources: research/iterations/<round>/qualified-results.json and research/iterations/cost-summary.csv.
"""
from __future__ import annotations

import csv
import json

from research.import_evidence import ROOT

FUNCTIONAL_SUITES = ('unit', 'invoked', 'autonomous')
ends = lambda suffix: (lambda condition: condition.endswith('__' + suffix))
# (group, [(row label, [(round, condition matcher)])]); every round uses the version-13 documents and the cells Generation S and Reuse S+B
ACHIEVEMENTS_GROUPS = [
    ('One response, no feedback', [
        ('no security context', [('a02b-s2', ends('none'))]),
        ('S1 high-level guidance', [('a02a-s1', ends('static'))]),
        ('S2 full document', [('a02b-s2', ends('static'))]),
        ('S3 generic, no repository', [('a02c-s3', ends('static'))]),
    ]),
    ('Agentic: repository tools, compiler and test feedback', [
        ('no security context', [('a03a-none', ends('none'))]),
        ('S1 high-level guidance', [('a03b-s1', ends('static'))]),
        ('S2 full document', [('a03c-s2', ends('static'))]),
        ('S3 generic, no repository', [('a03d-s3', ends('static'))]),
        ('S2 full document + judge', [('a03e-guard', ends('static-guard'))]),
    ]),
]
GROUPS = [
    ('One response, no feedback', [
        ('no security context', [('i32b-s2', ends('none'))]),
        ('S1 high-level guidance', [('i32a-s1', ends('static'))]),
        ('S2 full document', [('i32b-s2', ends('static'))]),
        ('S3 generic, no repository', [('i32c-s3', ends('static'))]),
    ]),
    ('Agentic: repository tools, compiler and test feedback', [
        ('no security context', [('i31-v13', ends('none'))]),
        ('S1 high-level guidance', [('i33a-s1', ends('static'))]),
        ('S2 full document', [('i31-v13', ends('static'))]),
        ('S3 generic, no repository', [('i33b-s3', ends('static'))]),
        ('S2 full document + judge', [('i31-v13', ends('static-guard'))]),
    ]),
]


def runs(iteration: str) -> list[dict]:
    from research import tasks
    data = json.loads((ROOT / 'research/iterations' / iteration / 'qualified-results.json').read_text())
    study = next(s for s in data['studies'] if s['plan']['id'] == iteration); tasks.of_plan(study['plan'])
    return study['runs']


def costs() -> dict:
    return {(r['round'], r['arm']): r for r in csv.DictReader((ROOT / 'research/iterations/cost-summary.csv').open())}


def measures(run: dict, task=None) -> dict:
    from research import tasks
    task = task or tasks.current(); issue_checks = task.issue_checks; total_tests = sum(len(v) for v in task.test_names.values())
    compiled = run.get('mainCompilation') == 'pass'
    functional = [c for c in run['checks'] if c['suite'] in FUNCTIONAL_SUITES]
    security = {c['name']: c['status'] for c in run['checks'] if c['suite'] == 'security_v1'}
    tests = sum(c['status'] == 'pass' for c in functional) / total_tests if compiled else None
    checks = sum(security.get(n) == 'pass' for n in issue_checks) / len(issue_checks) if compiled else None
    full = bool(run.get('functionalSuccess')) and all(security.get(n) == 'pass' for n in task.checks)
    return {'compiled': compiled, 'tests': tests, 'checks': checks, 'full': full}


def row(label: str, sources: list) -> dict | None:
    cost = costs(); items = []; tokens = 0; minutes = 0.0
    for iteration, match in sources:
        try: pool = runs(iteration)
        except FileNotFoundError: return None
        for run in pool:
            if not match(run['condition']): continue
            items.append(measures(run))
        for (rnd, arm), k in cost.items():
            if rnd == iteration and match(arm): tokens += int(float(k['inputTokens'])); minutes += float(k['callSeconds']) / 60
    if not items: return None
    n = len(items); compiled = [m for m in items if m['compiled']]
    return {'label': label, 'n': n, 'compiled': 100 * len(compiled) / n,
            'tests': 100 * sum(m['tests'] for m in compiled) / len(compiled) if compiled else None,
            'checks': 100 * sum(m['checks'] for m in compiled) / len(compiled) if compiled else None,
            'full': 100 * sum(m['full'] for m in items) / n, 'input': tokens / n / 1e6, 'minutes': minutes / n}


def rows(groups=None) -> list[tuple[str, list[dict]]]:
    return [(group, [r for r in (row(label, sources) for label, sources in items) if r is not None]) for group, items in (groups or GROUPS)]


def pct(value) -> str:
    return '--' if value is None else f'{value:.0f}\\%'


def latex_row(r: dict) -> str:
    return f"{r['label']} & {r['n']} & {pct(r['compiled'])} & {pct(r['tests'])} & {pct(r['checks'])} & {pct(r['full'])} & {r['input']:.1f} & {r['minutes']:.1f} \\\\"


def body(groups=None) -> str:
    lines = []
    for group, items in rows(groups):
        if not items: continue
        lines.append(f"\\multicolumn{{8}}{{@{{}}l}}{{\\emph{{{group}}}}} \\\\")
        lines += [latex_row(r) for r in items]
    return '\n'.join(lines)


def write(path=ROOT / 'paper/sections/results.tex') -> None:
    """Replace the bodies of tab:results (Highscore) and, when present, tab:results-ach (Achievements) in the paper with the generated ones."""
    source = path.read_text()
    for label, groups in (('tab:results', GROUPS), ('tab:results-ach', ACHIEVEMENTS_GROUPS)):
        if f'\\label{{{label}}}' not in source: continue
        a = source.index(f'\\label{{{label}}}'); start = source.index('\\midrule\n', a) + len('\\midrule\n'); end = source.index('\\bottomrule', start)
        source = source[:start] + body(groups) + '\n' + source[end:]
    path.write_text(source)


if __name__ == '__main__':
    import sys
    if '--write' in sys.argv: write(); print('tab:results written')
    elif '--achievements' in sys.argv: print(body(ACHIEVEMENTS_GROUPS))
    else: print(body())
