"""Fresh repository/task context acquisitions and exact readable prompt inserts."""
from __future__ import annotations

import argparse
from concurrent.futures import ThreadPoolExecutor
import json
from pathlib import Path

from research.generate_context import prepare, execute
from research.import_evidence import ROOT, canonical, digest
from research.model_adapter import command_from_env
from research.run_experiment import write_atomic
from research.security_followup import acquisition_task, repository_input

VARIANTS = {
    'requirements': ('task', '''Derive prospective security requirements for this feature from the task and inspected repository.
Do not stop at a description of existing classes. State concrete implementation obligations for input validity,
safe persistence, resource bounds and handling untrusted data. Explain why each matters to this task.
Proposed limits and guards are design recommendations, not facts about existing code; label them accordingly.
Include the code boundary at which a proposed guard belongs. Do not claim findings without source evidence.'''),
    'boundaries': ('flows', '''Trace how attacker-controlled values or stored bytes can reach the new feature.
For each relevant trust boundary, state the source, transformation, sensitive operation, possible failure,
and a concrete guard to apply before that operation. Cover both new records and loading persisted records.
Consider resource use while reading/decoding, not only the size of the final displayed board.
Distinguish evidence about current behavior from prospective implementation recommendations.'''),
}


def prompt_insert(record: dict) -> str:
    output = record.get('output')
    if not output or record['status'] not in ('completed', 'settings_unverified', 'citation_issues'):
        raise ValueError('No completed context output')
    lines = ['--- BEGIN REPOSITORY-DERIVED SECURITY CONTEXT ---',
             'Apply this task-specific context while implementing the feature. Observations and recommendations are model-generated; preserve stated uncertainty.',
             '', output['summary']]
    for item in output['items']:
        lines += ['', f"[{item['kind']}: {item['topic']}] {item['statement']}", f"Task relevance: {item['task_relevance']}"]
        for evidence in item['evidence']:
            lines += [f"Source: {evidence['path']}:{evidence['start_line']}-{evidence['end_line']}", evidence['quote']]
        lines += [f"Suggested verification: {item['suggested_check']}"]
    if output['limitations']: lines += ['', 'Uncertainty and limits:', *['- ' + value for value in output['limitations']]]
    lines += ['--- END REPOSITORY-DERIVED SECURITY CONTEXT ---', '']
    return '\n'.join(lines)


def acquire(directory: Path, command: list[str], workers=2):
    directory.mkdir(parents=True, exist_ok=True)
    index_path = directory / 'acquisitions.json'
    if index_path.exists():
        acquisitions = json.loads(index_path.read_text())
    else:
        acquisitions = []
        for method in ('Generation', 'Reuse'):
            repo = repository_input(method, directory)
            for variant, (strategy, instruction) in VARIANTS.items():
                task = acquisition_task(method) + '\nTime API contract: survivalTime values are milliseconds; display them as mm:ss.'
                record, _, target = prepare(repo, task, strategy, ROOT / '.local/context-generation', max_turns=16)
                record.update(variant=variant, method=method, iteration=directory.name)
                record['generatorHashes']['research/iteration_contexts.py'] = digest(Path(__file__).read_bytes())
                record['initialPrompt'] += '\nACQUISITION EMPHASIS\n' + instruction
                write_atomic(target / 'record.json', record)
                acquisitions.append({'method': method, 'strategy': variant, 'id': record['id']})
        write_atomic(index_path, acquisitions)
    def one(item):
        target = ROOT / '.local/context-generation' / item['id']
        record = json.loads((target / 'record.json').read_text())
        if record['status'] == 'prepared' and not (target / 'submitted').exists():
            record = execute(record, json.loads((target / 'snapshot.json').read_text()), target, command)
        if record.get('output'):
            text = prompt_insert(record)
            insert = directory / 'contexts' / f"{item['method'].lower()}-{item['strategy']}.txt"
            insert.parent.mkdir(exist_ok=True)
            if insert.exists() and insert.read_text() != text: raise ValueError('Existing prompt insert differs')
            if not insert.exists(): insert.write_text(text)
            record['promptInsert'] = text; record['promptInsertSha256'] = digest(text.encode())
            write_atomic(target / 'record.json', record)
        print(f"{item['method']}/{item['strategy']}: {record['status']}; items={len((record.get('output') or {}).get('items', []))}", flush=True)
    with ThreadPoolExecutor(max_workers=workers) as pool:
        list(pool.map(one, acquisitions))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--iteration', required=True)
    parser.add_argument('--execute', action='store_true')
    args = parser.parse_args()
    if not args.iteration.replace('-', '').replace('_', '').isalnum(): parser.error('Invalid iteration ID')
    if args.execute: acquire(ROOT / '.local/iterations' / args.iteration, command_from_env())
    else: print('Two fresh strategies × Generation/Reuse; each receives only repository, task and acquisition instructions.')
