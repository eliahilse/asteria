"""Time and tokens per arm and per acquisition, from the saved records. No model calls.

Delivery rounds (`.local/iterations/<round>/runs/*/record.json`): per arm the
number of trajectories, model calls, input / output / reasoning tokens (from
the provider usage of every response), the wall time of the calls and of the
trajectories. Acquisitions (`.local/context-generation/agent-*/record.json`):
per record the instruction version, angle, method, shell commands, elapsed
minutes and the Codex usage of the run (input, cached input, output,
reasoning tokens). Writes `research/iterations/cost-summary.md` and `.csv`
for every round, or `cost.md/.csv` into one round directory with --iteration.
"""
from __future__ import annotations

import argparse
import csv
from datetime import datetime
import io
import json
from pathlib import Path
import re

from research.import_evidence import ROOT

ITERATIONS = ROOT / '.local/iterations'
RECORDS = ROOT / '.local/context-generation'


def natural_key(name: str):
    match = re.match(r'i(\d+)([a-z]?)-', name)
    return (int(match.group(1)), match.group(2)) if match else (999, name)


def seconds(start: str | None, end: str | None) -> float:
    if not start or not end: return 0.0
    return (datetime.fromisoformat(end) - datetime.fromisoformat(start)).total_seconds()


def arm_costs(iteration: str, root: Path = ROOT) -> list[dict]:
    arms: dict[str, dict] = {}
    for record_path in sorted((root / '.local/iterations' / iteration / 'runs').glob('*/record.json')):
        r = json.loads(record_path.read_text())
        if r.get('status') in (None, 'started'): continue
        a = arms.setdefault(r['condition'], {'round': iteration, 'arm': r['condition'], 'trajectories': 0, 'calls': 0, 'inputTokens': 0, 'cachedInputTokens': 0, 'outputTokens': 0, 'reasoningTokens': 0, 'callSeconds': 0.0, 'trajectorySeconds': 0.0})
        a['trajectories'] += 1; a['trajectorySeconds'] += seconds(r.get('startedAt'), r.get('finishedAt'))
        calls = r.get('turns') or r.get('submissions') or []
        for c in calls:
            resp = c.get('response') or {}; usage = resp.get('usage') or {}
            if not resp and not usage: continue
            a['calls'] += 1; a['inputTokens'] += usage.get('input_tokens') or 0; a['cachedInputTokens'] += usage.get('cached_input_tokens') or 0
            a['outputTokens'] += usage.get('output_tokens') or 0; a['reasoningTokens'] += usage.get('reasoning_tokens') or 0
            a['callSeconds'] += seconds(c.get('startedAt'), c.get('receivedAt'))
        for event in r.get('sidecarEvents') or []:  # judge calls of gate/coach/rewind sidecars carry their own usage when recorded
            for j in event.get('judgeCalls') or event.get('transcript') or []:
                usage = (j.get('response') or {}).get('usage') if isinstance(j, dict) else None
                if usage:
                    a['calls'] += 1; a['inputTokens'] += usage.get('input_tokens') or 0; a['outputTokens'] += usage.get('output_tokens') or 0; a['reasoningTokens'] += usage.get('reasoning_tokens') or 0
    return list(arms.values())


def acquisition_costs(root: Path = ROOT) -> list[dict]:
    out = []
    for path in sorted((root / '.local/context-generation').glob('agent-*/record.json')):
        r = json.loads(path.read_text())
        if not str(r.get('protocol', '')).startswith('repository-security-context'): continue
        usage = {}
        for t in r.get('turns') or []:
            if t.get('type') == 'turn.completed' and t.get('usage'): usage = t['usage']
        version = re.search(r'-v(\d+)-', r.get('protocol', '')); commands = r.get('commandsExecuted')
        out.append({'record': r['id'], 'version': f"v{version.group(1)}" if version else r.get('protocol'), 'angle': r.get('angle'), 'method': r.get('method'), 'status': r.get('status'),
                    'commands': len(commands) if isinstance(commands, list) else (commands or 0), 'minutes': round(seconds(r.get('startedAt'), r.get('finishedAt')) / 60, 1),
                    'inputTokens': usage.get('input_tokens', 0), 'cachedInputTokens': usage.get('cached_input_tokens', 0), 'outputTokens': usage.get('output_tokens', 0),
                    'reasoningTokens': usage.get('reasoning_output_tokens', usage.get('reasoning_tokens', 0)), 'items': len((r.get('output') or {}).get('items', []))})
    out.sort(key=lambda x: (int(x['version'][1:]) if re.fullmatch(r'v\d+', str(x['version'])) else 0, x['angle'] or '', x['method'] or '', x['record']))
    return out


def render(arms: list[dict], acquisitions: list[dict]) -> str:
    lines = ['# Time and tokens', '',
             'Delivery arms: model calls of the generator (and of a judge where a sidecar recorded one), provider usage per call summed, wall time of the calls and of whole trajectories (including evaluation). Acquisitions: one Codex run each; usage as reported by Codex for the run.', '',
             '## Delivery rounds', '', '| Round | Arm | Trajectories | Calls | Input tokens | of which cached | Output tokens | Reasoning tokens | Call time (min) | Trajectory time (min) |', '| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |']
    for a in arms:
        lines.append(f"| {a['round']} | {a['arm']} | {a['trajectories']} | {a['calls']} | {a['inputTokens']:,} | {a['cachedInputTokens']:,} | {a['outputTokens']:,} | {a['reasoningTokens']:,} | {a['callSeconds']/60:.1f} | {a['trajectorySeconds']/60:.1f} |")
    lines += ['', '## Acquisitions (context agent)', '', '| Record | Instructions | Angle | Method | Commands | Minutes | Input tokens | of which cached | Output tokens | Reasoning tokens | Items |', '| --- | --- | --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |']
    for q in acquisitions:
        lines.append(f"| {q['record']} | {q['version']} | {q['angle']} | {q['method']} | {q['commands']} | {q['minutes']} | {q['inputTokens']:,} | {q['cachedInputTokens']:,} | {q['outputTokens']:,} | {q['reasoningTokens']:,} | {q['items']} |")
    return '\n'.join(lines) + '\n'


def csv_text(rows: list[dict]) -> str:
    if not rows: return ''
    buffer = io.StringIO(); writer = csv.DictWriter(buffer, fieldnames=list(rows[0].keys())); writer.writeheader(); writer.writerows(rows); return buffer.getvalue()


def main():
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration'); args = parser.parse_args()
    if args.iteration:
        arms = arm_costs(args.iteration); target = ROOT / 'research/iterations' / args.iteration
        (target / 'cost.md').write_text(render(arms, [])); (target / 'cost.csv').write_text(csv_text(arms)); print(render(arms, []))
        return
    arms = []
    for directory in sorted((p for p in ITERATIONS.iterdir() if (p / 'runs').exists()), key=lambda p: natural_key(p.name)): arms += arm_costs(directory.name)
    acquisitions = acquisition_costs()
    (ROOT / 'research/iterations/cost-summary.md').write_text(render(arms, acquisitions))
    (ROOT / 'research/iterations/cost-summary.csv').write_text(csv_text(arms)); (ROOT / 'research/iterations/cost-acquisitions.csv').write_text(csv_text(acquisitions))
    print(f'{len(arms)} arms, {len(acquisitions)} acquisitions -> research/iterations/cost-summary.md')


if __name__ == '__main__':
    main()
