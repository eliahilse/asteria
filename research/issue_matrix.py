"""Fixed-denominator issue matrix for one saved iteration.

Every issue check is reported as failed / unresolved / passed out of N per check
(10 × N for the ten-check total). Unresolved outcomes are not passes and never
leave the denominator. Run from the repository root:

    python3 -m research.issue_matrix --iteration i07-operational-replication

Reads research/iterations/ID/qualified-results.json and writes issue-matrix.md
and issue-matrix.csv beside it. No model calls or reevaluation.
"""
from __future__ import annotations

import argparse
import csv
import json
from pathlib import Path

from research import tasks
from research.import_evidence import ROOT

# Protocol order from research/security/PROTOCOL.md; the positive check is reported separately.
class _Checks:
    """The current task's checks, resolved when read so one module serves both tasks."""
    def __iter__(self): return iter(tasks.current().issue_checks)
    def __len__(self): return len(tasks.current().issue_checks)
    def __getitem__(self, i): return tasks.current().issue_checks[i]
    def __contains__(self, x): return x in tasks.current().issue_checks
ISSUE_CHECKS = _Checks()
class _Positive(str):
    def __new__(cls): return str.__new__(cls, '')
    def __eq__(self, other): return other == tasks.current().positive_check
    def __hash__(self): return hash(tasks.current().positive_check)
    def __str__(self): return tasks.current().positive_check
    def __format__(self, spec): return format(tasks.current().positive_check, spec)
POSITIVE_CHECK = _Positive()
def POSITIVE_LABEL_(): return f'{tasks.current().positive_check} (positive persistence check, not an issue)'
def TOTAL_LABEL_(): return f'Total ({len(tasks.current().issue_checks)} issue checks)'
REASONS = {
    'not_run': 'check not executed',
    'unknown': 'precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown',
    'compile_error': 'final artifact failed the security-suite compilation',
    'infrastructure_error': 'harness failure',
}
INTERPRETATION = (
    'Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition '
    'and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, '
    'infrastructure error) are not passes and are never removed from the denominator, so cells are comparable '
    'across conditions without per-cell denominators. Failed counts are repeated contract failures across '
    'trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. '
    'The positive persistence check is excluded from the total.'
)


def cwe_mapping(path: Path = ROOT / 'research/security/cwe-mapping.json') -> dict[str, list[str]]:
    """Check name -> CWE ids; accepts {check: [ids]} or {checks: {check: {cwes: [ids]}}}. Empty without the file."""
    if not path.exists(): return {}
    raw = json.loads(path.read_text())
    entries = raw.get('checks', raw) if isinstance(raw, dict) else {}
    mapping = {name: list(entry['cwes'] if isinstance(entry, dict) else entry) for name, entry in entries.items()}
    if any(not isinstance(cwe, str) for ids in mapping.values() for cwe in ids): raise ValueError('CWE mapping must list CWE ids as strings')
    return {**mapping, **{k: list(v) for k, v in tasks.current().cwe.items()}}


def counts(summary: dict, name: str) -> dict | None:
    return next((t for t in summary['checks'] if t['suite'] == 'security_v1' and t['name'] == name), None)


def cell(summary: dict, name: str) -> tuple[int, int, int] | None:
    """(failed, unresolved, passed) out of N = attempts; None when the check is absent or nothing was attempted."""
    t = counts(summary, name)
    if t is None or not summary['attempts']: return None
    return t['fail'], summary['attempts'] - t['executed'], t['pass']


def matrix(study: dict, cwes: dict[str, list[str]] | None = None) -> dict:
    cwes = cwes or {}
    by_id = {r['id']: r for r in study['summary']['conditions']}
    conditions = [c['id'] for c in study['plan']['conditions']]
    n = {c: by_id[c]['attempts'] for c in conditions}
    rows = [{'test': 'N per check', 'cwe': '', 'kind': 'denominator', 'cells': {c: (n[c], n[c], n[c]) if n[c] else None for c in conditions}}]
    for name in ISSUE_CHECKS:
        rows.append({'test': name, 'cwe': ', '.join(cwes.get(name, [])), 'kind': 'issue', 'cells': {c: cell(by_id[c], name) for c in conditions}})
    total = {}
    for c in conditions:
        parts = [cell(by_id[c], name) for name in ISSUE_CHECKS]
        total[c] = None if any(p is None for p in parts) else tuple(sum(p[i] for p in parts) for i in range(3))
    rows.append({'test': TOTAL_LABEL_(), 'cwe': '', 'kind': 'total', 'cells': total})
    rows.append({'test': POSITIVE_LABEL_(), 'cwe': ', '.join(cwes.get(POSITIVE_CHECK, [])), 'kind': 'positive', 'cells': {c: cell(by_id[c], POSITIVE_CHECK) for c in conditions}})
    unresolved = []
    for c in conditions:
        for name in [*ISSUE_CHECKS, POSITIVE_CHECK]:
            t = counts(by_id[c], name)
            if t is None or n[c] - t['executed'] <= 0: continue
            reasons = '; '.join(f"{reason} ({t[reason]}): {REASONS[reason]}" for reason in REASONS if t[reason] > 0)
            unresolved.append({'condition': c, 'test': name, 'unresolved': n[c] - t['executed'], **{reason: t[reason] for reason in REASONS},
                               'meaning': f'{reasons}. Unresolved outcomes are not passes; they remain in the fixed denominator N = {n[c]}.'})
    return {'study': study['plan']['id'], 'conditions': conditions, 'n': n, 'rows': rows, 'unresolved': unresolved}


def table(headers: list, rows: list[list], align: str = '---') -> str:
    def text(value): return str(value).replace('|', '\\|').replace('\n', ' ')
    return '\n'.join('| ' + ' | '.join(map(text, row)) + ' |' for row in [headers, ['---', '---', *[align] * (len(headers) - 2)], *rows])


def fmt(value: tuple[int, int, int] | None) -> str:
    return '—' if value is None else ' / '.join(map(str, value))


def render(data: dict, iteration: str, cwes: dict[str, list[str]] | None = None) -> str:
    cwes = cwe_mapping() if cwes is None else cwes
    lines = [f'# Issue matrix: {iteration}', '', INTERPRETATION, '']
    for study in data['studies']:
        m = matrix(study, cwes)
        ns = sorted(set(m['n'].values()))
        n_text = f"N = {ns[0]} per condition" if len(ns) == 1 else 'N per condition is given in the first row'
        lines += [f"## {m['study']}", '', f"Conditions in plan order. {n_text}; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of {len(ISSUE_CHECKS)} × N.", '',
                  table(['Test', 'CWE', *m['conditions']], [
                      [f"**{r['test']}**" if r['kind'] == 'total' else f"*{r['test']}*" if r['kind'] == 'positive' else r['test'], r['cwe'],
                       *[('' if r['cells'][c] is None else str(r['cells'][c][0])) if r['kind'] == 'denominator' else fmt(r['cells'][c]) for c in m['conditions']]] for r in m['rows']], '---:'), '',
                  '### Unresolved reasons', '']
        if m['unresolved']:
            lines += [table(['Condition', 'Test', 'Unresolved', 'not_run', 'unknown', 'compile_error', 'infrastructure_error', 'Meaning'],
                            [[u['condition'], u['test'], u['unresolved'], *[u[r] for r in REASONS], u['meaning']] for u in m['unresolved']]), '']
        else: lines += ['Every planned check was executed; no unresolved cells.', '']
    lines += ['Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.', '']
    return '\n'.join(lines)


def csv_rows(data: dict, cwes: dict[str, list[str]] | None = None) -> list[list]:
    cwes = cwe_mapping() if cwes is None else cwes
    out = []
    for study in data['studies']:
        m = matrix(study, cwes)
        out.append(['study', 'test', 'cwe', *[f'{c} {k}' for c in m['conditions'] for k in ('failed', 'unresolved', 'passed')]])
        for r in m['rows']:
            out.append([m['study'], r['test'], r['cwe'], *[v for c in m['conditions'] for v in (r['cells'][c] or ('', '', ''))]])
    return out


def write(iteration: str, root: Path = ROOT) -> tuple[Path, Path]:
    directory = root / 'research/iterations' / iteration
    data = json.loads((directory / 'qualified-results.json').read_text()); tasks.of_plan(data['studies'][0]['plan'])
    md, out = directory / 'issue-matrix.md', directory / 'issue-matrix.csv'
    md.write_text(render(data, iteration))
    with out.open('w', newline='') as handle: csv.writer(handle).writerows(csv_rows(data))
    return md, out


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument('--iteration', required=True, help='Saved iteration id under research/iterations/')
    for path in write(parser.parse_args().iteration): print(f'Wrote {path.relative_to(ROOT)}')


def __getattr__(name):  # PEP 562: the labels as module attributes, resolved for the current task when read
    if name == 'POSITIVE_LABEL': return POSITIVE_LABEL_()
    if name == 'TOTAL_LABEL': return TOTAL_LABEL_()
    raise AttributeError(name)
