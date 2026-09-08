"""Fresh I02 acquisitions with explicit prospective recommendations and stable references."""
from concurrent.futures import ThreadPoolExecutor
import json
from pathlib import Path

from research.generate_security_context import STRATEGIES, prepare, execute
from research.import_evidence import ROOT, digest
from research.run_experiment import write_atomic
from research.security_followup import acquisition_task, repository_input


def acquire(directory: Path, command: list[str], workers=2):
    directory.mkdir(parents=True, exist_ok=True)
    index = directory / 'acquisitions.json'
    if index.exists(): acquisitions = json.loads(index.read_text())
    else:
        acquisitions = []
        for method in ('Generation', 'Reuse'):
            repo = repository_input(method, directory)
            for strategy in STRATEGIES:
                task = acquisition_task(method) + '\nTime API contract: survivalTime values are milliseconds; display them as mm:ss.'
                record, _, target = prepare(repo, task, strategy, ROOT / '.local/context-generation')
                record.update(method=method, iteration=directory.name)
                record['generatorHashes']['research/iteration_contexts_v2.py'] = digest(Path(__file__).read_bytes())
                write_atomic(target / 'record.json', record)
                acquisitions.append({'method': method, 'strategy': strategy, 'id': record['id']})
        write_atomic(index, acquisitions)
    def one(item):
        target = ROOT / '.local/context-generation' / item['id']; record = json.loads((target / 'record.json').read_text())
        if record['status'] == 'prepared' and not (target / 'submitted').exists(): record = execute(record, json.loads((target / 'snapshot.json').read_text()), target, command)
        if record.get('promptInsert'):
            path = directory / 'contexts' / f"{item['method'].lower()}-{item['strategy']}.txt"; path.parent.mkdir(exist_ok=True)
            if path.exists() and path.read_text() != record['promptInsert']: raise ValueError('Frozen insert changed')
            if not path.exists(): path.write_text(record['promptInsert'])
        print(f"{item['method']}/{item['strategy']}: {record['status']}; items={len((record.get('output') or {}).get('items', []))}", flush=True)
    with ThreadPoolExecutor(max_workers=workers) as pool: list(pool.map(one, acquisitions))
