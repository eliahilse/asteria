"""Read the exact security prompt inserts for the active study, without model calls."""
from __future__ import annotations

import argparse
import json
from pathlib import Path
import re

from research.import_evidence import ROOT, canonical, digest
from research.security_followup import security_block
from research.saved_iteration import resolve

LABELS = {'overview': 'Overview', 'task': 'Task-focused', 'flows': 'Data-flow', 'requirements': 'Requirements', 'boundaries': 'Trust boundaries', 'operations': 'Operational guards', 'task_only': 'Task only'}


def index(public=False, iteration=None):
    public, iteration = resolve(ROOT, public, iteration)
    if iteration and iteration != 'original':
        directory = ROOT / ('research/iterations' if public else '.local/iterations') / iteration
        plan = json.loads((directory / ('plan.json' if public else 'manifest.json')).read_text())
    elif not public:
        directory = ROOT / '.local/studies/highscore-security-luna-v1'
        if not (directory / 'manifest.json').exists(): return {'local': True, 'iteration': None, 'groups': []}
        plan = json.loads((directory / 'manifest.json').read_text())
    else: return {'local': False, 'iteration': None, 'groups': []}
    groups = {}
    for item in plan.get('acquisitions', []):
        if public:
            text = (directory / 'contexts' / f"{item['method'].lower()}-{item['strategy']}.txt").read_text()
            # The exact task is already frozen into each corresponding prompt.
            from research.security_followup import acquisition_task
            task = item.get('task') or acquisition_task(item['method']) + '\nTime API contract: survivalTime values are milliseconds; display them as mm:ss.'
            if item.get('promptInsertSha256') and digest(text.encode()) != item['promptInsertSha256']: raise ValueError('Frozen public context insert differs')
        else:
            record = json.loads((ROOT / '.local/context-generation' / item['id'] / 'record.json').read_text())
            if not record.get('output'): continue
            text = record.get('promptInsert') or security_block(record).decode()
            task = record['task']
        key = (item['method'], task)
        group = groups.setdefault(key, {'method': item['method'], 'task': task, 'inserts': []})
        group['inserts'].append({'id': item['id'], 'strategy': item['strategy'], 'label': LABELS.get(item['strategy'], item['strategy']), 'text': text, 'sha256': digest(text.encode())})
    return {'local': not public, 'iteration': plan['id'], 'groups': list(groups.values())}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--public', action='store_true')
    parser.add_argument('--iteration')
    parser.add_argument('--write-public', action='store_true')
    args = parser.parse_args()
    data = index(args.public or args.write_public, args.iteration)
    if args.write_public:
        target = ROOT / 'workbench/public/data/context-inserts.json'
        target.parent.mkdir(parents=True, exist_ok=True); target.write_bytes(canonical(data))
    else: print(json.dumps(data))
