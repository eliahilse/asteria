"""Run-end hook audit: how each delivered artifact treats a live player without a name.

The autonomous coupling tests end a run on the driver's human player, whose
`teamName` is null because it has no AI (`ApoMarioPlayer.java:197`); two of
them (`recordedSurvivalTimeIsTheRealElapsedTime`,
`secondRunAlsoRecordedAndBoardSortedDescending`) never set a name. An
artifact that declines to record a run without a name therefore fails exactly
those tests. This audit reads every trajectory's final evaluated submission,
lists the failing functional checks, extracts the *new* lines (absent from the
unmodified target files) that read `getTeamName`, and classifies the hook:

- `fallback`: a new line substitutes a literal name when the live name is null or blank;
- `skip_on_null`: a new line returns or filters the player when the name is null, with no fallback;
- `passthrough`: the live name is handed on unchanged;
- `none`: no new line reads the live name (feature not wired or not compiled).

Joined with the final artifact's `rejectsNullName` outcome (pass = the store
rejects a null name), the hook class gives the fate of a run without a name:
`skipped_at_hook`, `rejected_at_store` (passed through into a rejecting store),
`recorded` (fallback, or passed through into an accepting store) or `none`.
The classes are regular-expression judgments over cited lines; every cited
line is printed so the class can be checked against the source.
"""
from __future__ import annotations

import argparse
import csv
import io
import json
from pathlib import Path
import re

from research.feature_delivery import original_sources
from research.import_evidence import ROOT

NULL_NAME_TESTS = ('recordedSurvivalTimeIsTheRealElapsedTime', 'secondRunAlsoRecordedAndBoardSortedDescending')
FENCE = re.compile(r'^```java filename=(?P<name>[A-Za-z0-9_.]+)$')
FALLBACK = re.compile(r'(?i)\w*name\s*==\s*null[^;]*"[^"]+"')  # a name variable tested against null and a literal name in the same statement
SKIP = re.compile(r'getTeamName\(\)\s*==\s*null[^;]*return|getTeamName\(\)\s*!=\s*null\s*&&')
CLASSES = ('fallback', 'skip_on_null', 'passthrough', 'none')
MECHANISMS = ('recorded', 'skipped_at_hook', 'rejected_at_store', 'none')


def parse_complete_files(text: str) -> dict[str, list[str]]:
    files, current = {}, None
    for line in text.split('\n'):
        match = FENCE.match(line)
        if match: current = match['name']; files[current] = []; continue
        if line == '```': current = None; continue
        if current is not None: files[current].append(line)
    return files


def new_lines(files: dict[str, list[str]], originals: dict[str, str]) -> list[tuple[str, int, str]]:
    out = []
    for name, lines in files.items():
        known = {l.strip() for l in originals.get(name, '').split('\n')}
        out += [(name, i, l) for i, l in enumerate(lines, 1) if l.strip() and l.strip() not in known]
    return out


def classify(cited: list[tuple[str, int, str]]) -> str:
    if not cited: return 'none'
    if any(FALLBACK.search(l) for _, _, l in cited): return 'fallback'
    if any(SKIP.search(l) for _, _, l in cited): return 'skip_on_null'
    return 'passthrough'


def store_rejects_null(run_dir: Path, record: dict) -> str | None:
    """Status of the final artifact's `rejectsNullName` security check (pass = the store rejects a null name), or None when unavailable."""
    path = record.get('finalEvaluation')
    if not path or not (run_dir / path).exists(): return None
    checks = ((json.loads((run_dir / path).read_text()).get('security') or {}).get('checks')) or []
    return next((c.get('status') for c in checks if c.get('name') == 'rejectsNullName'), None)


def mechanism(hook_class: str, rejects_null: str | None) -> str:
    if hook_class == 'skip_on_null': return 'skipped_at_hook'
    if hook_class == 'passthrough': return 'rejected_at_store' if rejects_null == 'pass' else 'recorded'
    if hook_class == 'fallback': return 'recorded'
    return 'none'


def audit_run(run_dir: Path, originals: dict[str, str]) -> dict:
    record = json.loads((run_dir / 'record.json').read_text())
    evaluated = [s for s in record.get('submissions', []) if s.get('status') == 'evaluated']
    row = {'runId': record['runId'], 'condition': record['condition'], 'repetition': record['repetition'], 'status': record.get('status'),
           'functionalSuccess': bool(record.get('functionalSuccess')), 'finalSubmission': None, 'compilation': None, 'failingChecks': [], 'nullNameTestsFailed': 0,
           'cited': [], 'class': 'none', 'rejectsNullName': None, 'mechanism': 'none'}
    if not evaluated: return row
    final = evaluated[-1]; row['finalSubmission'] = final['number']; row['compilation'] = final.get('compilation')
    checks = (final.get('feedback') or {}).get('functionalChecks') or []
    row['failingChecks'] = [c['name'] for c in checks if c.get('status') != 'pass']
    row['nullNameTestsFailed'] = sum(1 for name in NULL_NAME_TESTS if name in row['failingChecks'])
    complete = run_dir / f"submission-{final['number']}" / 'complete-files.txt'
    if complete.exists():
        cited = [(n, i, l) for n, i, l in new_lines(parse_complete_files(complete.read_text()), originals) if 'getTeamName' in l or FALLBACK.search(l) and re.search(r'name', l, re.I)]
        row['cited'] = [{'file': n, 'line': i, 'text': l.strip()} for n, i, l in cited]
        row['class'] = classify(cited)
    row['rejectsNullName'] = store_rejects_null(run_dir, record)
    row['mechanism'] = mechanism(row['class'], row['rejectsNullName'])
    return row


def audit(iteration: str, root: Path = ROOT, originals: dict[str, str] | None = None) -> list[dict]:
    if originals is None: originals, _ = original_sources()
    runs = sorted(p for p in (root / '.local/iterations' / iteration / 'runs').iterdir() if (p / 'record.json').exists())
    return [audit_run(p, originals) for p in runs]


def summarize(rows: list[dict]) -> list[dict]:
    out = []
    for condition in sorted({r['condition'] for r in rows}):
        group = [r for r in rows if r['condition'] == condition]
        not_recorded = [r for r in group if r['mechanism'] in ('skipped_at_hook', 'rejected_at_store')]
        out.append({'condition': condition, 'n': len(group), 'functional': sum(r['functionalSuccess'] for r in group),
                    **{c: sum(r['class'] == c for r in group) for c in CLASSES},
                    **{m: sum(r['mechanism'] == m for r in group) for m in MECHANISMS},
                    'nullNameFailures': sum(r['nullNameTestsFailed'] > 0 for r in group),
                    'nullNameOnlyFailures': sum(null_name_only(r) for r in group),
                    'notRecorded': len(not_recorded), 'notRecordedAndFail': sum(r['nullNameTestsFailed'] > 0 for r in not_recorded),
                    'discordant': sum(discordant(r) for r in group)})
    return out


def null_name_only(row: dict) -> bool:
    """The final artifact fails functional checks, and every failing check is one of the two null-name coupling tests."""
    return bool(row['failingChecks']) and set(row['failingChecks']) <= set(NULL_NAME_TESTS)


def discordant(row: dict) -> bool:
    """Mechanism and test outcome disagree: a hook classed as not recording passes the null-name tests (a fallback through a constant or helper is not detected), or a hook classed as recording fails them (another cause)."""
    not_recorded = row['mechanism'] in ('skipped_at_hook', 'rejected_at_store')
    return (not_recorded and row['nullNameTestsFailed'] == 0 and row['compilation'] == 'pass') or (row['mechanism'] == 'recorded' and row['nullNameTestsFailed'] > 0)


def render(rows: list[dict], iteration: str) -> str:
    summary = summarize(rows)
    lines = [f'# Run-end hook audit: {iteration}', '',
             'For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. '
             f'The two null-name coupling tests are {NULL_NAME_TESTS[0]} and {NULL_NAME_TESTS[1]}; the driver player has no AI, so its team name is null. '
             'Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; '
             'skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. '
             'Fate of a run without a name = hook class joined with the final artifact\'s rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.', '',
             'Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.', '',
             '| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |',
             '| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |']
    for s in summary:
        lines.append(f"| {s['condition']} | {s['n']} | {s['functional']} | {s['fallback']} | {s['skip_on_null']} | {s['passthrough']} | {s['none']} | {s['skipped_at_hook']} | {s['rejected_at_store']} | {s['recorded']} | {s['notRecorded']} | {s['notRecordedAndFail']} | {s['nullNameFailures']} | {s['nullNameOnlyFailures']} | {s['discordant']} |")
    review = [r for r in rows if discordant(r)]
    lines += ['', '## Rows for hand review', '']
    lines += [f"- {r['condition']} r{r['repetition']}: {r['class']}, {r['mechanism'].replace('_', ' ')}, rejectsNullName {r['rejectsNullName']}, null-name tests failed {r['nullNameTestsFailed']}, {'functional' if r['functionalSuccess'] else 'not functional'}" for r in review] or ['- none']
    lines += ['', '## Trajectories', '']
    for r in rows:
        head = f"### {r['condition']} r{r['repetition']}: {r['class']}, {r['mechanism'].replace('_', ' ')}, {'functional' if r['functionalSuccess'] else 'not functional'}"
        lines += [head, '', f"Final evaluated submission: {r['finalSubmission']}; compilation: {r['compilation']}; rejectsNullName: {r['rejectsNullName']}; failing functional checks: {', '.join(r['failingChecks']) or 'none'}.", '']
        for c in r['cited']: lines.append(f"- `{c['file']}:{c['line']}` `{c['text']}`")
        if not r['cited']: lines.append('- no new line reads the live player name')
        lines.append('')
    return '\n'.join(lines)


def csv_text(rows: list[dict]) -> str:
    buffer = io.StringIO(); writer = csv.writer(buffer)
    writer.writerow(['runId', 'condition', 'repetition', 'status', 'functionalSuccess', 'finalSubmission', 'compilation', 'class', 'rejectsNullName', 'mechanism', 'nullNameTestsFailed', 'failingChecks', 'citedLines'])
    for r in rows:
        writer.writerow([r['runId'], r['condition'], r['repetition'], r['status'], r['functionalSuccess'], r['finalSubmission'], r['compilation'], r['class'], r['rejectsNullName'], r['mechanism'],
                         r['nullNameTestsFailed'], ' '.join(r['failingChecks']), ' | '.join(f"{c['file']}:{c['line']}: {c['text']}" for c in r['cited'])])
    return buffer.getvalue()


def main():
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration', required=True); args = parser.parse_args()
    rows = audit(args.iteration); public = ROOT / 'research/iterations' / args.iteration
    (public / 'hook-audit.md').write_text(render(rows, args.iteration)); (public / 'hook-audit.csv').write_text(csv_text(rows))
    for s in summarize(rows):
        print(f"{s['condition']}: N={s['n']} functional={s['functional']} | hook fallback={s['fallback']} skip={s['skip_on_null']} passthrough={s['passthrough']} none={s['none']} "
              f"| skippedAtHook={s['skipped_at_hook']} rejectedAtStore={s['rejected_at_store']} recorded={s['recorded']} | notRecorded={s['notRecorded']} (fail null-name test: {s['notRecordedAndFail']}) nullNameFail={s['nullNameFailures']}")


if __name__ == '__main__':
    main()
