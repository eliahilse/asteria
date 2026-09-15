"""The paper's results table: one row per context strategy, pooled over the cells of its round(s). No model calls.

Columns per row: N trajectories; functional (all sixteen tests pass within the budget); failed security checks of the resolved ones (ten issue checks);
artifacts passing all five input-policy checks; full hits (all sixteen tests and all eleven checks); generator input tokens per trajectory (millions),
output plus reasoning tokens per trajectory (thousands), model-call minutes per trajectory. Judge calls are included in the guard arms.
Sources: research/iterations/<round>/qualified-analysis.json, full-hits.csv, and research/iterations/cost-summary.csv.
"""
from __future__ import annotations

import csv
import json

from research.import_evidence import ROOT

ends = lambda suffix: (lambda condition: condition.endswith('__' + suffix))
GROUPS = [
    ('One response, no feedback (five cells)', [
        ('no context', [('i24a-high', ends('none')), ('i24b-full', ends('none')), ('i24c-generic', ends('none'))]),
        ('S1 high-level', [('i24a-high', ends('static'))]),
        ('S2 full document', [('i24b-full', ends('static'))]),
        ('S3 generic (no repository)', [('i24c-generic', ends('static'))]),
    ]),
    ('Up to five submissions with feedback (two to four cells)', [
        ('no context', [('i07-operational-replication', ends('none')), ('i20-v10', ends('none'))]),
        ('researcher-written', [('i07-operational-replication', lambda c: c.split('__')[-1] in ('operations', 'requirements', 'boundaries'))]),
        ('agent document v10, compact', [('i20-v10', ends('static'))]),
    ]),
    ('Agentic: repository tools and feedback (two cells)', [
        ('no context', [('i28-graph', ends('none')), ('i29-v11', ends('none')), ('i30-v12', ends('none')), ('i31-v13', ends('none'))]),
        ('S2 document (v10)', [('i28-graph', ends('static'))]),
        ('S2 + code graph', [('i28-graph', ends('static-ast'))]),
        ('S2 + graph + guard judge', [('i28-graph', ends('static-ast-guard'))]),
        ('S2 + graph + advisory judge', [('i28-graph', ends('static-ast-advise'))]),
        ('document v11 (bounds, domains)', [('i29-v11', ends('static'))]),
        ('document v11 + guard judge', [('i29-v11', ends('static-guard'))]),
        ('document v12 (text domains)', [('i30-v12', ends('static'))]),
        ('document v12 + guard judge', [('i30-v12', ends('static-guard'))]),
        ('document v13 (bounded reads)', [('i31-v13', ends('static'))]),
        ('document v13 + guard judge', [('i31-v13', ends('static-guard'))]),
    ]),
]


def load(iteration: str):
    analysis = {c['condition']: c for c in json.loads((ROOT / 'research/iterations' / iteration / 'qualified-analysis.json').read_text())['conditions']}
    hits = {r['arm']: r for r in csv.DictReader((ROOT / 'research/iterations' / iteration / 'full-hits.csv').open())}
    return analysis, hits


def costs() -> dict:
    return {(r['round'], r['arm']): r for r in csv.DictReader((ROOT / 'research/iterations/cost-summary.csv').open())}


def row(label: str, sources: list) -> dict:
    cost = costs(); total = {'n': 0, 'functional': 0, 'failed': 0, 'resolved': 0, 'clean': 0, 'full': 0, 'input': 0, 'output': 0, 'minutes': 0.0}
    for iteration, match in sources:
        analysis, hits = load(iteration)
        for condition, c in analysis.items():
            if not match(condition): continue
            h = hits[condition]; k = cost[(iteration, condition)]
            total['n'] += c['n']; total['functional'] += c['withinBudgetFull']; total['failed'] += c['issues']['failed']; total['resolved'] += c['issues']['evaluated']
            total['clean'] += int(h['inputPolicyClean']); total['full'] += int(h['fullHits'])
            total['input'] += int(float(k['inputTokens'])); total['output'] += int(float(k['outputTokens'])) + int(float(k['reasoningTokens'])); total['minutes'] += float(k['callSeconds']) / 60
    return {'label': label, **total}


def rows() -> list[tuple[str, list[dict]]]:
    return [(group, [row(label, sources) for label, sources in items]) for group, items in GROUPS]


def latex_row(r: dict) -> str:
    n = r['n']
    return (f"{r['label']} & {n} & {r['functional']} & {r['failed']} / {r['resolved']} & {r['clean']} & {r['full']} & "
            f"{r['input'] / n / 1e6:.1f} & {r['output'] / n / 1e3:.0f} & {r['minutes'] / n:.1f} \\\\")


def body() -> str:
    lines = []
    for group, items in rows():
        lines.append(f"\\multicolumn{{9}}{{@{{}}l}}{{\\emph{{{group}}}}} \\\\")
        lines += [latex_row(r) for r in items]
    return '\n'.join(lines)


if __name__ == '__main__':
    print(body())
