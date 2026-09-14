"""Hand spot-check sheet for the mechanism review (research.issue_handling).

Samples classified rows, two per issue check by default with a fixed seed, and
inlines the cited source lines from the review packets so a person can confirm
or reject each category without opening the artifacts. The sheet has an empty
verdict column; the filled sheet is the human check the review otherwise lacks.
"""
from __future__ import annotations

import argparse
import csv
import io
import json
from pathlib import Path
import random
import re

from research.import_evidence import ROOT

CHECKS = ['rejectsNegativeScore', 'rejectsNegativeTime', 'rejectsNullName', 'rejectsBlankName', 'rejectsExcessiveName', 'boundsRetainedEntries',
          'malformedStoreDoesNotCrash', 'oversizedPhysicalLine', 'nativeDeserializationCanary', 'largePersistedRecordSet']
EVIDENCE = re.compile(r'^(?P<path>.+?):L(?P<start>\d+)(?:-L(?P<end>\d+))?$')


def sample(rows: list[dict], per_check: int, seed: int) -> list[dict]:
    """Stratified: `per_check` classified (non-unresolved) rows per check, deterministic for a seed."""
    rng = random.Random(seed); chosen = []
    for check in CHECKS:
        pool = sorted((r for r in rows if r['check'] == check and r['category'] != 'unresolved'), key=lambda r: (r['condition'], int(r['repetition'])))
        chosen += rng.sample(pool, min(per_check, len(pool)))
    return chosen


def cited_text(evidence: str, sources: dict[str, list[str]]) -> list[dict]:
    out = []
    for item in [e.strip() for e in evidence.split(';') if e.strip()]:
        match = EVIDENCE.match(item)
        if not match: out.append({'ref': item, 'lines': None, 'text': 'unparsable reference'}); continue
        path, start = match['path'], int(match['start']); end = int(match['end'] or start)
        lines = sources.get(path)
        if lines is None: out.append({'ref': item, 'lines': None, 'text': 'file not in packet'}); continue
        text = '\n'.join(f'{n}: {lines[n - 1]}' for n in range(start, min(end, len(lines)) + 1))
        out.append({'ref': item, 'lines': [start, end], 'text': text})
    return out


def build(iteration: str, per_check: int = 2, seed: int = 7, root: Path = ROOT) -> list[dict]:
    public = root / 'research/iterations' / iteration; packets_dir = root / '.local/issue-handling' / iteration / 'packets'
    rows = list(csv.DictReader((public / 'issue-handling.csv').open()))
    entries = []
    for row in sample(rows, per_check, seed):
        run_id = f"{iteration}__{row['condition']}__r{row['repetition']}"
        packet = json.loads((packets_dir / f'{run_id}.json').read_text())
        sources = {s['path']: s['text'].splitlines() for s in packet['sources']}
        entries.append({'runId': run_id, 'condition': row['condition'], 'repetition': row['repetition'], 'check': row['check'], 'qualifiedStatus': row['qualifiedStatus'],
                        'category': row['category'], 'note': row['note'], 'reviewer': row['reviewer'], 'sourceSha256': row['sourceSha256'], 'citations': cited_text(row['evidence'], sources)})
    return entries


def render(entries: list[dict], iteration: str, seed: int) -> str:
    lines = [f'# Mechanism review spot-check: {iteration}', '',
             f'{len(entries)} classified rows, two per issue check, drawn with seed {seed} from `issue-handling.csv`. For each row, read the cited lines and fill the verdict: '
             '**agree** (the category describes the mechanism in the cited code), **disagree** (state the category you would assign), or **unsure**. '
             'The evaluator outcome is given for orientation only; the question is whether the mechanism label is right.', '',
             '| # | Trajectory | Check | Evaluator | Category | Human verdict |', '| ---: | --- | --- | --- | --- | --- |']
    for i, e in enumerate(entries, 1):
        lines.append(f"| {i} | {e['condition']} r{e['repetition']} | {e['check']} | {e['qualifiedStatus']} | {e['category']} |  |")
    lines += ['', '## Rows', '']
    for i, e in enumerate(entries, 1):
        lines += [f"### {i}. {e['condition']} r{e['repetition']} · {e['check']}", '', f"Evaluator: **{e['qualifiedStatus']}** · Reviewer category: **{e['category']}** · Reviewer: {e['reviewer']}", '',
                  f"Reviewer note: {e['note']}", '']
        for c in e['citations']:
            lines += [f"Cited `{c['ref']}`:", '', '```java', c['text'], '```', '']
        lines += ['Human verdict: ', '']
    return '\n'.join(lines)


def main():
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration', required=True)
    parser.add_argument('--per-check', type=int, default=2); parser.add_argument('--seed', type=int, default=7); args = parser.parse_args()
    entries = build(args.iteration, args.per_check, args.seed)
    public = ROOT / 'research/iterations' / args.iteration
    (public / 'issue-handling-spotcheck.md').write_text(render(entries, args.iteration, args.seed))
    buffer = io.StringIO(); writer = csv.writer(buffer)
    writer.writerow(['index', 'runId', 'check', 'qualifiedStatus', 'category', 'evidence', 'reviewer', 'sourceSha256', 'humanVerdict', 'humanCategory', 'humanNote'])
    for i, e in enumerate(entries, 1): writer.writerow([i, e['runId'], e['check'], e['qualifiedStatus'], e['category'], '; '.join(c['ref'] for c in e['citations']), e['reviewer'], e['sourceSha256'], '', '', ''])
    (public / 'issue-handling-spotcheck.csv').write_text(buffer.getvalue())
    print(f'{len(entries)} rows written to {public / "issue-handling-spotcheck.md"} and .csv')


if __name__ == '__main__':
    main()
