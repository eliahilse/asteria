"""Acquire security context from the task plus a published weakness catalog.

The instruction is the task-only sentence followed by the 2025 CWE Top 25 list
(identifier, name and MITRE description). No repository-, feature- or check-
specific security guidance is added. The v3 repository tools, output schema,
evidence checks, budget and model settings are reused through task_context.
"""
from __future__ import annotations

import argparse
from contextlib import contextmanager
import json
from pathlib import Path
from unittest.mock import patch

from research import task_context
from research.import_evidence import ROOT, digest
from research.model_adapter import command_from_env
from research.run_experiment import write_atomic

PROTOCOL = 'repository-security-context-v6-cwe-catalog'
STRATEGY = 'catalog'
CATALOG_FILE = ROOT / 'research/security/cwe-top25-2025.json'


def load_catalog(path: Path = CATALOG_FILE) -> dict:
    catalog = json.loads(path.read_text())
    entries = catalog['entries']
    if len(entries) != 25 or [e['rank'] for e in entries] != list(range(1, 26)):
        raise ValueError('The weakness catalog must list the 25 ranked entries')
    return catalog


def catalog_text(catalog: dict) -> str:
    lines = ['WEAKNESS CATALOG',
             f"Vocabulary: {catalog['name']} ({catalog['publisher']}; {catalog['listSource']}).",
             'Where an item corresponds to a catalog entry, cite its identifier in cwes and use the catalog name.',
             'Consider an entry only where the repository and task make it applicable; entries that do not apply need no item.']
    lines += [f"{e['rank']}. {e['id']} {e['name']}: {e['description']}" for e in catalog['entries']]
    return '\n'.join(lines)


CATALOG = load_catalog()
INSTRUCTION = task_context.INSTRUCTION + '\n\n' + catalog_text(CATALOG)


@contextmanager
def protocol():
    with patch.object(task_context, 'INSTRUCTION', INSTRUCTION), patch.object(task_context, 'PROTOCOL', PROTOCOL), \
         patch.object(task_context, 'STRATEGY', STRATEGY):
        yield


def prepare(repo: Path, task: str, output: Path, max_turns=16):
    with protocol():
        record, snap, directory = task_context.prepare(repo, task, output, max_turns)
    record['schemaVersion'] = 6
    record['catalog'] = {'name': CATALOG['name'], 'listSource': CATALOG['listSource'], 'retrievedAt': CATALOG['retrievedAt'],
                         'file': str(CATALOG_FILE.relative_to(ROOT)), 'sha256': digest(CATALOG_FILE.read_bytes()), 'entries': len(CATALOG['entries'])}
    for path in (Path(__file__), CATALOG_FILE):
        record['generatorHashes'][str(path.relative_to(ROOT))] = digest(path.read_bytes())
    write_atomic(directory / 'record.json', record)
    return record, snap, directory


def execute(record, snap, directory, command, timeout=600):
    if record['protocol'] != PROTOCOL or record['strategy'] != STRATEGY or record.get('catalog', {}).get('sha256') != digest(CATALOG_FILE.read_bytes()):
        raise ValueError('Expected a catalog acquisition with the frozen weakness catalog')
    with protocol():
        return task_context.execute(record, snap, directory, command, timeout)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--repo', type=Path, required=True)
    parser.add_argument('--task', required=True)
    parser.add_argument('--output', type=Path, default=ROOT / '.local/context-generation')
    parser.add_argument('--max-turns', type=int, default=16)
    parser.add_argument('--execute', action='store_true')
    args = parser.parse_args()
    command = command_from_env() if args.execute else None
    record, snap, directory = prepare(args.repo.resolve(), args.task, args.output, args.max_turns)
    print(json.dumps({'id': record['id']}), flush=True)
    if command:
        execute(record, snap, directory, command)


if __name__ == '__main__':
    main()
