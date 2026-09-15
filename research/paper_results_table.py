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
SECURITY = ('rejectsNegativeScore', 'rejectsNegativeTime', 'rejectsNullName', 'rejectsBlankName', 'rejectsExcessiveName',
            'boundsRetainedEntries', 'malformedStoreDoesNotCrash', 'oversizedPhysicalLine', 'nativeDeserializationCanary', 'largePersistedRecordSet')
ends = lambda suffix: (lambda condition: condition.endswith('__' + suffix))
# (group, [(row label, [(round, condition matcher)])]); every round uses the version-13 documents and the cells Generation S and Reuse S+B
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
    data = json.loads((ROOT / 'research/iterations' / iteration / 'qualified-results.json').read_text())
    return next(s for s in data['studies'] if s['plan']['id'] == iteration)['runs']


def costs() -> dict:
    return {(r['round'], r['arm']): r for r in csv.DictReader((ROOT / 'research/iterations/cost-summary.csv').open())}


def measures(run: dict) -> dict:
    compiled = run.get('mainCompilation') == 'pass'
    functional = [c for c in run['checks'] if c['suite'] in FUNCTIONAL_SUITES]
    security = {c['name']: c['status'] for c in run['checks'] if c['suite'] == 'security_v1'}
    tests = sum(c['status'] == 'pass' for c in functional) / 16 if compiled else None
    checks = sum(security.get(n) == 'pass' for n in SECURITY) / 10 if compiled else None
    full = bool(run.get('functionalSuccess')) and all(security.get(n) == 'pass' for n in SECURITY + ('validRecordRoundTrip',))
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


def rows() -> list[tuple[str, list[dict]]]:
    return [(group, [r for r in (row(label, sources) for label, sources in items) if r is not None]) for group, items in GROUPS]


def pct(value) -> str:
    return '--' if value is None else f'{value:.0f}\\%'


def latex_row(r: dict) -> str:
    return f"{r['label']} & {r['n']} & {pct(r['compiled'])} & {pct(r['tests'])} & {pct(r['checks'])} & {pct(r['full'])} & {r['input']:.1f} & {r['minutes']:.1f} \\\\"


def body() -> str:
    lines = []
    for group, items in rows():
        if not items: continue
        lines.append(f"\\multicolumn{{8}}{{@{{}}l}}{{\\emph{{{group}}}}} \\\\")
        lines += [latex_row(r) for r in items]
    return '\n'.join(lines)


if __name__ == '__main__':
    print(body())
