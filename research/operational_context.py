"""Repository/task-only context with explicit operation-level guard fields.

Wrap the frozen v3 acquisition machinery in a dedicated process; no original
generator or experiment inputs are edited. This is a new exploratory strategy.
"""
import argparse
from contextlib import contextmanager
from copy import deepcopy
import json
from pathlib import Path
from unittest.mock import patch

from research import generate_security_context as base
from research.import_evidence import ROOT, digest
from research.iteration_runner import freeze
from research.model_adapter import command_from_env
from research.run_experiment import write_atomic
from research.security_followup import acquisition_task, repository_input

FIELDS = ('operation', 'untrusted_input', 'invariant', 'enforcement_point', 'failure_behavior')
STRATEGY = ('Operational guards', '''Derive security obligations for the concrete operations needed by this feature.
Inspect relevant source before proposing guards. Trace input acquisition, run completion, storage writing,
loading, decoding, sorting and rendering where applicable. For every proposed guard state the operation,
untrusted input, invariant, exact enforcement point relative to allocation or side effects, and failure behavior.
Include resource units and the order of checks: a bound checked after an unbounded allocation does not bound it.
Use domain evidence to distinguish valid gameplay values from unsafe inputs; numeric thresholds without a
repository/task basis are explicit policy choices, not established facts. Address consistency and lifecycle
behavior alongside storage robustness. Cover other security mechanisms only when source/task evidence makes
them relevant. Mark unverified edges and policy choices explicitly. Do not write feature implementation code.''')
EXTRA_PROTOCOL = '''
OPERATION REPRESENTATION
Each item also has operation, untrusted_input, invariant, enforcement_point and failure_behavior.
For recommendations and change risks all five fields must be nonempty and concrete. For other kinds use
null where a field is not established or applicable. State prospective safeguards as proposals, including
their policy assumptions. These fields are prompt context; they are not verified implementation facts.
Aim for 6–10 distinct items; omit repetitive generic advice. Keep the original evidence and basis rules.
'''
VALIDATE = base.validate_output
TOOL = deepcopy(base.TOOL)
ITEM = TOOL['function']['parameters']['properties']['items']['items']
for name in FIELDS:
    ITEM['properties'][name] = {'type': ['string', 'null']}
    ITEM['required'].append(name)


def validate_output(action, evidence):
    output, checks = VALIDATE(action, evidence)
    for item in output['items']:
        for field in FIELDS:
            value = item.get(field)
            if value is not None and (not isinstance(value, str) or not value.strip()): raise ValueError(f'Invalid {field} in {item["id"]}')
            if item['kind'] in ('recommendation', 'change_risk') and value is None: raise ValueError(f'Proposed guard {item["id"]} requires {field}')
    return output, checks


def prompt_insert(record):
    output = record['output']; lines = ['--- BEGIN REPOSITORY-DERIVED SECURITY CONTEXT ---', output['summary']]
    for item in output['items']:
        lines += ['', f"[{item['id']}; {item['kind']}; {item['basis']}; {item['topic']}] {item['statement']}"]
        for field in FIELDS:
            if item.get(field): lines.append(field.replace('_', ' ').capitalize() + ': ' + item[field])
        lines.append('Task relevance: ' + item['task_relevance'])
        for ref in item['evidence']:
            lines.append(f"Inspected source {ref['evidence_id']}: {ref['path']}:{ref['start_line']}-{ref['end_line']}")
        if not item['evidence']:
            lines.append('Uncited unknown; absence was not established.' if item['kind'] == 'unknown' else 'Basis: prospective task requirement or reasoning; not an implemented safeguard.')
        lines.append('Suggested verification: ' + item['suggested_check'])
    lines += ['', 'Uncertainty and limits:', *['- ' + value for value in output['limitations']], '--- END REPOSITORY-DERIVED SECURITY CONTEXT ---', '']
    return '\n'.join(lines)


@contextmanager
def protocol():
    with patch.dict(base.STRATEGIES, {'operations': STRATEGY}), patch.object(base, 'PROTOCOL', base.PROTOCOL + EXTRA_PROTOCOL), \
         patch.object(base, 'TOOL', TOOL), patch.object(base, 'validate_output', validate_output), patch.object(base, 'prompt_insert', prompt_insert):
        yield


def prepare_pair(directory):
    index = directory / 'acquisitions-operations.json'
    if index.exists(): raise ValueError('Operational acquisition pair already exists')
    directory.mkdir(parents=True, exist_ok=True); acquisitions = []
    for method in ('Generation', 'Reuse'):
        repo = repository_input(method, directory)
        task = acquisition_task(method) + '\nTime API contract: survivalTime values are milliseconds; display them as mm:ss.'
        with protocol(): record, _, target = base.prepare(repo, task, 'operations', ROOT / '.local/context-generation')
        record.update(protocol='repository-security-context-v4-operational-fields', schemaVersion=4, method=method, iteration=directory.name)
        record['generatorHashes']['research/operational_context.py'] = digest(Path(__file__).read_bytes())
        write_atomic(target / 'record.json', record)
        acquisitions.append({'method': method, 'strategy': 'operations', 'id': record['id']})
    freeze(index, acquisitions)
    print('Prepared two independent repository/task acquisitions; no model calls made')


def execute_one(directory, method):
    item = next(a for a in json.loads((directory / 'acquisitions-operations.json').read_text()) if a['method'] == method)
    target = ROOT / '.local/context-generation' / item['id']; record = json.loads((target / 'record.json').read_text())
    for path, sha in record['generatorHashes'].items():
        if digest((ROOT / path).read_bytes()) != sha: raise ValueError('Frozen operational generator changed')
    with protocol(): result = base.execute(record, json.loads((target / 'snapshot.json').read_text()), target, command_from_env())
    if result.get('promptInsert'):
        output = directory / 'contexts' / (method.lower() + '-operations.txt'); output.parent.mkdir(exist_ok=True)
        if output.exists(): raise ValueError('Context insert already exists')
        output.write_text(result['promptInsert'])
    print(method + '/operations: ' + result['status'])


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--directory', type=Path, required=True)
    parser.add_argument('--prepare', action='store_true'); parser.add_argument('--method', choices=['Generation', 'Reuse'])
    args = parser.parse_args()
    if args.prepare: prepare_pair(args.directory.resolve())
    elif args.method: execute_one(args.directory.resolve(), args.method)
    else: parser.error('--prepare or --method is required')
