"""Full hits per arm: artifacts that pass all sixteen functional tests and all eleven security checks. No model calls.

Reads `research/iterations/<round>/qualified-results.json` (the qualified
outcomes the paper reports; results.json when no qualification was saved)
and, when present, the trajectory records under
`<root>/.local/iterations/<round>/runs` for the agentic counters (tool turns,
reads, searches, injections, guard consultations / positive verdicts /
interventions, turn of the first compiling submission). Writes
`research/iterations/<round>/full-hits.md` and `.csv`; --all lists every
round's arms sorted by full hits.
"""
from __future__ import annotations

import argparse
import csv
import io
import json
from pathlib import Path
from statistics import median

from research.import_evidence import ROOT

SECURITY = ('validRecordRoundTrip', 'rejectsNegativeScore', 'rejectsNegativeTime', 'rejectsNullName', 'rejectsBlankName', 'rejectsExcessiveName',
            'boundsRetainedEntries', 'malformedStoreDoesNotCrash', 'oversizedPhysicalLine', 'nativeDeserializationCanary', 'largePersistedRecordSet')
INPUT_POLICY = SECURITY[1:6]


def outcomes(run: dict) -> dict[str, str]:
    return {c['name']: c['status'] for c in run.get('checks') or [] if c.get('suite') == 'security_v1'}


def is_full_hit(run: dict) -> bool:
    checks = outcomes(run)
    return bool(run.get('functionalSuccess')) and all(checks.get(name) == 'pass' for name in SECURITY)


def compiling_turn(record: dict) -> int | None:
    """Tool turn of the first submission whose compilation succeeded, from a trajectory record; None when none compiled."""
    for submission in record.get('submissions') or []:
        compilation = submission.get('compilation')
        if compilation is None: compilation = (submission.get('feedback') or {}).get('compilation')
        ok = compilation.get('success') if isinstance(compilation, dict) else (compilation == 'pass' if isinstance(compilation, str) else bool(compilation))
        if ok: return submission.get('turn') or submission.get('number')
    return None


def arm_rows(iteration: str, root: Path = ROOT) -> list[dict]:
    source = next((root / 'research/iterations' / iteration / name for name in ('qualified-results.json', 'results.json') if (root / 'research/iterations' / iteration / name).exists()), None)
    if source is None: raise FileNotFoundError(f'no results for {iteration}')
    results = json.loads(source.read_text())
    study = next(s for s in results['studies'] if s['plan']['id'] == iteration)
    records = {}
    for path in (root / '.local/iterations' / iteration / 'runs').glob('*/record.json'):
        try: r = json.loads(path.read_text()); records[r['runId']] = r
        except (ValueError, KeyError): continue
    rows = []
    for condition in sorted({r['condition'] for r in study['runs']}, key=lambda c: [x['id'] for x in study['plan']['conditions']].index(c) if c in [x['id'] for x in study['plan']['conditions']] else c):
        runs = [r for r in study['runs'] if r['condition'] == condition]; recs = [records[r['runId']] for r in runs if r['runId'] in records]
        checks = [outcomes(r) for r in runs]
        row = {'round': iteration, 'arm': condition, 'n': len(runs), 'source': source.name,
               'compiled': sum(1 for r in runs if r.get('mainCompilation')), 'functionalFirst': sum(1 for r in runs if r.get('firstFunctionalSuccess')), 'functional': sum(1 for r in runs if r.get('functionalSuccess')),
               'inputPolicyClean': sum(1 for c in checks if all(c.get(n) == 'pass' for n in INPUT_POLICY)), 'securityClean': sum(1 for c in checks if all(c.get(n) == 'pass' for n in SECURITY)),
               'fullHits': sum(1 for r in runs if is_full_hit(r)),
               'failed': sum(v == 'fail' for c in checks for v in c.values()), 'passed': sum(v == 'pass' for c in checks for v in c.values())}
        row['unresolved'] = 11 * len(runs) - row['failed'] - row['passed']  # fixed denominator: a check absent from a rejected or non-compiling artifact is unresolved
        if recs:
            guard = [e for r in recs for e in r.get('sidecarEvents') or [] if e.get('stage') == 'guard']
            turns = [r.get('toolTurns') or len(r.get('turns') or []) for r in recs]; first = [t for t in (compiling_turn(r) for r in recs) if t is not None]
            row.update({'toolTurnsMedian': median(turns) if turns else None, 'reads': sum(sum(t.get('action') == 'read' for t in r.get('turns') or []) for r in recs),
                        'searches': sum(sum(t.get('action') == 'search' for t in r.get('turns') or []) for r in recs), 'submissions': sum(len(r.get('submissions') or []) for r in recs),
                        'injections': sum(e.get('injected', False) for r in recs for e in r.get('sidecarEvents') or [] if e.get('stage') != 'guard'),
                        'guardConsultations': sum(bool(e.get('consulted')) for e in guard), 'guardPositive': sum(bool(e.get('wouldIntervene')) for e in guard), 'guardInterventions': sum(bool(e.get('intervene')) for e in guard),
                        'firstCompilingTurnMedian': median(first) if first else None, 'compiledEver': len(first)})
        rows.append(row)
    return rows


def render(rows: list[dict], iteration: str) -> str:
    agentic = any('toolTurnsMedian' in r for r in rows)
    lines = [f'# {iteration}: full hits per arm', '', 'A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). '
             'Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from ' + (rows[0]['source'] if rows else 'results') + '.', '',
             '| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p |' + (' Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |' if agentic else ''),
             '| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- |' + (' ---: | ---: | ---: | ---: | ---: | --- | --- |' if agentic else '')]
    for r in rows:
        line = f"| {r['arm']} | {r['n']} | {r['compiled']} | {r['functionalFirst']} / {r['functional']} | {r['inputPolicyClean']} | {r['securityClean']} | **{r['fullHits']}** | {r['failed']} / {r['unresolved']} / {r['passed']} |"
        if agentic and 'toolTurnsMedian' in r:
            line += f" {r['toolTurnsMedian']} | {r['reads']} | {r['searches']} | {r['submissions']} | {r['injections']} | {r['guardConsultations']} / {r['guardPositive']} / {r['guardInterventions']} | {r['firstCompilingTurnMedian']}; {r['compiledEver']} |"
        elif agentic: line += ' | | | | | | |'
        lines.append(line)
    return '\n'.join(lines) + '\n'


def csv_text(rows: list[dict]) -> str:
    if not rows: return ''
    keys = list(dict.fromkeys(k for r in rows for k in r)); buffer = io.StringIO(); writer = csv.DictWriter(buffer, fieldnames=keys); writer.writeheader(); writer.writerows(rows); return buffer.getvalue()


def main():
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration'); parser.add_argument('--all', action='store_true'); parser.add_argument('--root', type=Path, default=ROOT)
    args = parser.parse_args()
    if args.iteration:
        rows = arm_rows(args.iteration, args.root); target = args.root / 'research/iterations' / args.iteration
        (target / 'full-hits.md').write_text(render(rows, args.iteration)); (target / 'full-hits.csv').write_text(csv_text(rows)); print(render(rows, args.iteration)); return
    if args.all:
        rows = []
        for directory in sorted((args.root / 'research/iterations').iterdir()):
            if (directory / 'results.json').exists():
                try: rows += arm_rows(directory.name, args.root)
                except (StopIteration, ValueError, KeyError): continue
        rows.sort(key=lambda r: (-r['fullHits'] / max(r['n'], 1), -r['fullHits'], r['round'], r['arm']))
        print('| Round | Arm | N | Functional | Security clean | Full hits |'); print('| --- | --- | ---: | ---: | ---: | ---: |')
        for r in rows[:25]: print(f"| {r['round']} | {r['arm']} | {r['n']} | {r['functional']} | {r['securityClean']} | {r['fullHits']} |")


if __name__ == '__main__':
    main()
