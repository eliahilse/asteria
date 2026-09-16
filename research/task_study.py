"""Base study for a task: the prior study's prompt per cell (task text, context files, attached sources), frozen as a tracked manifest.

Mirrors research.paper_matrix for any task in vamos-artifact/Pipeline/Prompts.csv and a chosen subset of cells; the manifest is the
`base` the agentic rounds read their cell prompts from (conditions keyed by cell id: generation_s, reuse_sb, ...).
"""
from __future__ import annotations

import argparse
import csv
import json
from pathlib import Path

from research import tasks
from research.import_evidence import ROOT, canonical, digest
from research.paper_matrix import CONTEXTS, OUTPUT_SUFFIX, source_attachment


def prepare(task: tasks.Task, cells: list[str], output: Path, repetitions: int = 5) -> dict:
    with (ROOT / 'vamos-artifact/Pipeline/Prompts.csv').open(encoding='utf-8-sig') as source:
        rows = list(csv.DictReader(source))
    conditions, prompts, inputs = [], {}, {Path(__file__).resolve(), ROOT / 'research/paper_matrix.py', ROOT / 'vamos-artifact/Pipeline/Prompts.csv'}
    for paper_id, row in enumerate(rows):
        if row['Task'] != task.name: continue
        types = [key for key, name in CONTEXTS.items() if row[name].strip()]
        cell = row['Method'].lower() + '_' + (''.join(types).lower() or 'none')
        if cell not in cells: continue
        parts = [row['Prompt'].strip() + '\n' + OUTPUT_SUFFIX]; attachments = []
        def attach(raw, name, kind, origin):
            text = raw.decode('utf-8', errors='replace')
            attachments.append({**origin, 'name': name, 'kind': kind, 'sha256': digest(raw), 'renderedSha256': digest(text.encode())}); inputs.add(ROOT / origin['path'])
            parts.append(f'\n\n--- BEGIN ATTACHED {kind.upper()}: {name} ---\n{text}\n--- END ATTACHED {kind.upper()}: {name} ---')
        for key in types:
            path = ROOT / 'vamos-artifact/Contexts' / f"{row[CONTEXTS[key]].strip()}_{CONTEXTS[key]}.json"
            attach(path.read_bytes(), path.name, 'context', {'path': str(path.relative_to(ROOT)), 'member': None})
        for column, kind in [('Reuse Files', 'reuse source'), ('Source Code', 'target source')]:
            for ref in [v.strip() for v in row[column].split(';') if v.strip()]:
                raw, origin = source_attachment(ref); attach(raw, ref.split('.')[-1] + '.java', kind, origin)
        prompt = ''.join(parts); sha = digest(prompt.encode()); prompts[sha] = prompt
        conditions.append({'id': cell, 'stage': 'baseline', 'strategy': row['Method'], 'paperPromptId': paper_id, 'baseContext': '+'.join(types) or 'None', 'contextTypes': types,
                           'securityStrategy': 'none', 'sourceGame': row['Source'], 'targetGame': row['Target'], 'attachments': attachments, 'repetitions': repetitions,
                           'promptFile': f'prompts/{sha}.txt', 'promptSha256': sha, 'promptCharacters': len(prompt), 'promptBytes': len(prompt.encode()), 'promptTokens': None})
    if {c['id'] for c in conditions} != set(cells): raise ValueError(f'Cells not found for {task.name}: {set(cells) - {c["id"] for c in conditions}}')
    manifest = {'schemaVersion': 1, 'id': output.name, 'task': task.key, 'phase': 'baseline', 'model': 'gpt-5.6-luna', 'reasoning': 'high', 'temperature': None, 'maxOutputTokens': 65536,
                'conditions': conditions, 'schedule': [], 'sourceHashes': {str(p.relative_to(ROOT)): digest(p.read_bytes()) for p in sorted(inputs)},
                'purpose': 'Base prompts per cell for the agentic and one-response rounds of this task; no trajectories are collected from this manifest itself.'}
    manifest['fingerprint'] = digest(canonical(manifest))
    output.mkdir(parents=True, exist_ok=True); (output / 'prompts').mkdir(exist_ok=True)
    path = output / 'manifest.json'
    if path.exists() and path.read_bytes() != canonical(manifest): raise FileExistsError('Frozen study differs; choose a new output directory.')
    for sha, text in prompts.items(): (output / 'prompts' / f'{sha}.txt').write_text(text)
    path.write_bytes(canonical(manifest)); return manifest


def main():
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--task', default='achievements'); parser.add_argument('--cells', default='generation_s,reuse_sb')
    parser.add_argument('--output', type=Path); args = parser.parse_args()
    task = tasks.by_name(args.task); output = args.output or ROOT / task.study_dir
    manifest = prepare(task, args.cells.split(','), output)
    print(f"{task.name}: {len(manifest['conditions'])} cells -> {output}; {manifest['fingerprint'][:12]}")
    for c in manifest['conditions']: print(f"  {c['id']}: {c['promptCharacters']:,} characters, {len(c['attachments'])} attachments")


if __name__ == '__main__':
    main()
