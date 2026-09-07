"""Freeze the complete original Highscore matrix before collecting replay outcomes."""
from __future__ import annotations

import argparse
import csv
import json
import random
import zipfile
from pathlib import Path

from research.import_evidence import ROOT, canonical, digest

STUDY_ID = 'highscore-paper-luna-v1'
DEFAULT = ROOT / 'research/studies' / STUDY_ID
CONTEXTS = {'S': 'Structural', 'F': 'Functional', 'B': 'Behavioral'}
# Exact suffix assembled by the authors' driver, including its contradiction
# with the task's code-only instruction. The replay retains it in every arm.
OUTPUT_SUFFIX = '\n'.join([
    '\n\n--- OUTPUT FORMAT ---',
    'First, think step-by-step through the class architecture, dependencies, ',
    'and potential logic edge cases. Write down your brief internal reasoning.',
    'Then, output the final Java source code. For each class, wrap it cleanly in this block:',
    '```java filename=ClassName.java', '// your code here', '```',
])


def source_attachment(ref: str) -> tuple[bytes, dict]:
    game, cls = ref.split('.')[0], ref.split('.')[-1]
    directory = ROOT / 'apogames/Java' / game
    if game == 'ApoMario':
        path = directory / 'ApoMario.jar'
        with zipfile.ZipFile(path) as archive:
            matches = [n for n in archive.namelist() if Path(n).name == cls + '.java']
            if len(matches) != 1: raise ValueError(f'Expected one source attachment: {ref}')
            raw = archive.read(matches[0])
        return raw, {'path': str(path.relative_to(ROOT)), 'member': matches[0]}
    matches = sorted(directory.rglob(cls + '.java'))
    if len(matches) != 1: raise ValueError(f'Expected one source attachment: {ref}')
    return matches[0].read_bytes(), {'path': str(matches[0].relative_to(ROOT)), 'member': None}


def prepare(output: Path = DEFAULT, repetitions=5) -> dict:
    if type(repetitions) is not int or repetitions < 1: raise ValueError('Positive repetitions required')
    with (ROOT / 'vamos-artifact/Pipeline/Prompts.csv').open(encoding='utf-8-sig') as source:
        rows = list(csv.DictReader(source))
    conditions, prompts = [], {}
    inputs = {Path(__file__).resolve(), ROOT / 'vamos-artifact/Pipeline/Prompts.csv', ROOT / 'vamos-artifact/Pipeline/run.py'}
    for paper_id, row in enumerate(rows):
        if row['Task'] != 'Highscore': continue
        types = [key for key, name in CONTEXTS.items() if row[name].strip()]
        parts = [row['Prompt'].strip() + '\n' + OUTPUT_SUFFIX]
        attachments = []
        def attach(raw, name, kind, origin):
            text = raw.decode('utf-8', errors='replace')
            attachments.append({**origin, 'name': name, 'kind': kind, 'sha256': digest(raw), 'renderedSha256': digest(text.encode())})
            inputs.add(ROOT / origin['path'])
            parts.append(f'\n\n--- BEGIN ATTACHED {kind.upper()}: {name} ---\n{text}\n--- END ATTACHED {kind.upper()}: {name} ---')
        for key in types:
            path = ROOT / 'vamos-artifact/Contexts' / f"{row[CONTEXTS[key]].strip()}_{CONTEXTS[key]}.json"
            attach(path.read_bytes(), path.name, 'context', {'path': str(path.relative_to(ROOT)), 'member': None})
        for column, kind in [('Reuse Files', 'reuse source'), ('Source Code', 'target source')]:
            for ref in [v.strip() for v in row[column].split(';') if v.strip()]:
                raw, origin = source_attachment(ref)
                attach(raw, ref.split('.')[-1] + '.java', kind, origin)
        prompt = ''.join(parts)
        sha = digest(prompt.encode())
        condition_id = row['Method'].lower() + '_' + (''.join(types).lower() or 'none')
        conditions.append({'id': condition_id, 'stage': 'baseline', 'strategy': row['Method'], 'paperPromptId': paper_id,
                           'baseContext': '+'.join(types) or 'None', 'contextTypes': types, 'securityStrategy': 'none',
                           'sourceGame': row['Source'], 'targetGame': row['Target'], 'attachments': attachments,
                           'repetitions': repetitions, 'promptFile': f'prompts/{sha}.txt', 'promptSha256': sha,
                           'promptCharacters': len(prompt), 'promptBytes': len(prompt.encode()), 'promptTokens': None})
        prompts[sha] = prompt
    if len(conditions) != 16: raise ValueError('Expected two methods × eight Highscore context combinations')
    rng, schedule = random.Random(20260908), []
    for repetition in range(1, repetitions + 1):
        block = [{'runId': f'paper_luna__{c["id"]}__r{repetition}', 'condition': c['id'], 'stage': 'baseline', 'repetition': repetition} for c in conditions]
        rng.shuffle(block)
        schedule.extend(block)
    plan = {'schemaVersion': 2, 'id': STUDY_ID, 'phase': 'screening', 'task': 'Highscore', 'model': 'gpt-5.6-luna',
            'reasoning': 'medium', 'temperature': None, 'maxOutputTokens': 65536, 'scheduleSeed': 20260908,
            'allowUnverifiedSettings': True, 'conditions': conditions, 'schedule': schedule,
            'axes': {'method': ['Generation', 'Reuse'], 'paperContext': ['None', 'S', 'F', 'B', 'S+F', 'S+B', 'F+B', 'S+F+B'],
                     'securityContext': ['none', 'overview', 'task', 'flows']},
            'selection': {'perMethod': 2, 'order': ['full functional successes / all attempts', 'functional checks passed / (16 × all attempts)',
                                                  'compilations / all attempts', 'ascending original prompt ID'],
                          'requiresCompleteScreening': True, 'securityOutcomesUsed': False},
            'followup': {'repetitions': 5, 'freshControls': True, 'securityStrategies': ['overview', 'task', 'flows'],
                         'contextAcquisition': 'Fresh target repository + original feature task for generation; target and donor repositories + reuse task for reuse. One independently acquired context per method × security strategy, frozen across selected paper-context conditions.',
                         'comparison': 'Each selected method/context × no security, overview, task-focused, data-flow. New code responses in every cell; no model-seed pairing is claimed.'},
            'deviations': ['Luna with requested medium reasoning replaces the original model/temperature.',
                           'Original task text and output suffix retained. Attachment bytes are inlined with boundaries in original attachment order instead of using a provider Files API; UTF-8 replacement decoding is recorded.',
                           'Five exploratory repetitions per cell, randomized dispatch order. Repetition IDs are blocks, not identical model seeds.',
                           'Original author integration plus documented deterministic package/import repairs. Sixteen functional and eleven separate security checks.',
                           'Requested settings and served model are recorded. Missing effective-settings attestations remain unverified and are disclosed in results.',
                           'Selection uses functionality over all attempts. Security outcomes do not choose winners. Selected controls are rerun in the follow-up to limit selection bias.',
                           'Security treatments also add repository-derived information and prompt length. This design does not isolate security wording from those effects.'],
            'sourceHashes': {str(p.relative_to(ROOT)): digest(p.read_bytes()) for p in sorted(inputs)}}
    plan['fingerprint'] = digest(canonical(plan))
    output.mkdir(parents=True, exist_ok=True)
    path = output / 'manifest.json'
    if path.exists() and path.read_bytes() != canonical(plan): raise FileExistsError('Choose a new study version; this manifest is frozen')
    (output / 'prompts').mkdir(exist_ok=True)
    for sha, prompt in prompts.items(): (output / 'prompts' / f'{sha}.txt').write_text(prompt)
    path.write_bytes(canonical(plan))
    return plan


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, default=DEFAULT)
    args = parser.parse_args()
    plan = prepare(args.output)
    print(f"{len(plan['conditions'])} conditions; {len(plan['schedule'])} attempts; {plan['fingerprint']}")
