"""Summarize agent-based context acquisitions (research.context_agent records) in counts."""
from __future__ import annotations

import argparse
import csv
import datetime as dt
import io
import json
from pathlib import Path

from research.import_evidence import ROOT

FIELDS = ['id', 'method', 'angle', 'status', 'model', 'modelReported', 'elapsedMinutes', 'commands', 'exitCode', 'rawItems', 'items', 'droppedItems',
          'anchorsTotal', 'anchorsMatched', 'anchorsCorrected', 'anchorsRejected', 'uncitedItems', 'assets', 'boundaries', 'insertCharacters', 'validator', 'failure']


def summarize(directory: Path) -> dict | None:
    path = directory / 'record.json'
    if not path.exists(): return None
    record = json.loads(path.read_text())
    if record.get('protocol') != 'repository-security-context-v7-agent': return None
    checks = record.get('citationChecks') or {}; output = record.get('output') or {}; kinds = checks.get('itemsByKind') or {}
    elapsed = None
    if record.get('submittedAt') and record.get('finishedAt'):
        elapsed = round((dt.datetime.fromisoformat(record['finishedAt']) - dt.datetime.fromisoformat(record['submittedAt'])).total_seconds() / 60, 1)
    return {'id': record['id'], 'method': record['method'], 'angle': record['angle'], 'status': record['status'], 'model': record['model'],
            'modelReported': ','.join(record.get('modelReported') or []) or None, 'elapsedMinutes': elapsed, 'commands': record.get('commandsExecuted'),
            'exitCode': record.get('exitCode'), 'rawItems': len((record.get('rawOutput') or {}).get('items', [])), 'items': len(output.get('items', [])),
            'droppedItems': len(checks.get('droppedItems') or []), 'anchorsTotal': checks.get('total'), 'anchorsMatched': checks.get('matched'),
            'anchorsCorrected': checks.get('symbolCorrected', 0), 'anchorsRejected': len(checks.get('rejectedAnchors') or []), 'uncitedItems': checks.get('uncitedItems'),
            'assets': len(output.get('assets', [])), 'boundaries': len(output.get('boundaries', [])), 'insertCharacters': len(record.get('promptInsert') or ''),
            'validator': checks.get('validator'), 'failure': record.get('failure'), 'itemsByKind': kinds}


def table(rows: list[dict]) -> str:
    head = ['Method', 'Angle', 'Status', 'Minutes', 'Commands', 'Items (raw→kept)', 'Anchors matched/total (corrected)', 'Assets', 'Boundaries', 'Insert chars', 'Kinds']
    lines = ['| ' + ' | '.join(head) + ' |', '| ' + ' | '.join('---' for _ in head) + ' |']
    for r in sorted(rows, key=lambda r: (r['method'], r['angle'], r['id'])):
        kinds = ', '.join(f'{k} {v}' for k, v in sorted((r.get('itemsByKind') or {}).items()))
        lines.append(f"| {r['method']} | {r['angle']} | {r['status']} | {r['elapsedMinutes']} | {r['commands']} | {r['rawItems']}→{r['items']} | "
                     f"{r['anchorsMatched']}/{r['anchorsTotal']} ({r['anchorsCorrected']}) | {r['assets']} | {r['boundaries']} | {r['insertCharacters']} | {kinds} |")
    return '\n'.join(lines)


def main():
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--directory', type=Path, default=ROOT / '.local/context-generation')
    parser.add_argument('--csv', type=Path); parser.add_argument('--markdown', type=Path); args = parser.parse_args()
    rows = [r for r in (summarize(d) for d in sorted(args.directory.iterdir()) if d.is_dir()) if r]
    if args.csv:
        buffer = io.StringIO(); writer = csv.DictWriter(buffer, fieldnames=FIELDS, extrasaction='ignore'); writer.writeheader(); writer.writerows(rows)
        args.csv.write_text(buffer.getvalue())
    text = table(rows)
    if args.markdown: args.markdown.write_text('# Agent acquisitions\n\nEvery record under the acquisition directory; counts only, no percentages.\n\n' + text + '\n')
    print(text)


if __name__ == '__main__':
    main()
