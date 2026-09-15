"""One self-contained HTML page with every security-context insert of every round.

For each round that supplied context (researcher-written rounds with `acquisitions`
in their plan, agent rounds with `contextInsertFile` on their conditions) the page
shows the insert text with statement headers and failure-behaviour lines
highlighted, the acquisition record behind it (instruction version, model, command
count) when the local record exists, and the round's counts for the arms that
received it (functional of N; issue checks failed / unresolved / passed of 10N).
No model calls; reads `research/iterations/*/{plan.json,contexts,qualified-analysis.json}`.
"""
from __future__ import annotations

import argparse
import html
import json
from pathlib import Path
import re
import subprocess

from research.import_evidence import ROOT

ITERATIONS = ROOT / 'research/iterations'
RECORDS = ROOT / '.local/context-generation'
RULE_V9 = ("6. Fail safe without losing the change's effect. Rejection belongs at the boundary where untrusted data enters (parser, decoder, store). "
           "At the integration point where the change produces its primary effect, a missing or invalid value is substituted by a stated default or normalized "
           "so that the effect still occurs; never propose skipping or suppressing the primary effect as failure behavior.")
LABELS = {'overview': 'Overview', 'task': 'Task-focused', 'flows': 'Data-flow', 'requirements': 'Requirements', 'boundaries': 'Trust boundaries',
          'operations': 'Operations', 'task_only': 'Task only (one sentence)', 'catalog': 'CWE Top 25 list'}


def natural_key(name: str):
    match = re.match(r'i(\d+)([a-z]?)-', name)
    return (int(match.group(1)), match.group(2)) if match else (999, name)


def stats(text: str) -> dict:
    lines = text.split('\n')
    return {'characters': len(text), 'statements': sum(1 for l in lines if l.startswith('[')), 'failureLines': sum(1 for l in lines if l.startswith('Failure behavior:'))}


def record_meta(record_id: str | None) -> dict | None:
    if not record_id: return None
    path = RECORDS / record_id / 'record.json'
    if not path.exists(): return {'id': record_id}
    r = json.loads(path.read_text())
    version = re.search(r'-v(\d+)-', r.get('protocol', '')); commands = r.get('commandsExecuted')
    return {'id': record_id, 'version': f"v{version.group(1)}" if version else r.get('protocol'), 'model': r.get('model'), 'angle': r.get('angle'),
            'commands': len(commands) if isinstance(commands, list) else commands, 'items': len((r.get('output') or {}).get('items', [])), 'status': r.get('status')}


def arm_counts(iteration: Path) -> dict[str, dict]:
    path = iteration / 'qualified-analysis.json'
    if not path.exists(): return {}
    out = {}
    for c in json.loads(path.read_text())['conditions']:
        issues = c['issues']
        out[c['condition']] = {'n': c['n'], 'functional': c['withinBudgetFull'], 'failed': issues['failed'], 'unresolved': issues['unresolved'], 'passed': issues['evaluated'] - issues['failed'], 'planned': issues['plannedChecks']}
    return out


def round_inserts(iteration: Path) -> list[dict]:
    plan = json.loads((iteration / 'plan.json').read_text()); counts = arm_counts(iteration); inserts = []
    if plan.get('acquisitions'):
        for item in plan['acquisitions']:
            path = iteration / 'contexts' / f"{item['method'].lower()}-{item['strategy']}.txt"
            if not path.exists(): continue
            # Researcher-round plans: condition['strategy'] is the method, 'securityStrategy' the insert kind, 'contextAcquisitionId' the acquisition.
            conditions = plan.get('conditions', [])
            arms = [c['id'] for c in conditions if c.get('contextAcquisitionId') == item['id'] or (c.get('securityStrategy') == item['strategy'] and c.get('strategy') == item['method'])]
            controls = [c['id'] for c in conditions if c.get('securityStrategy') == 'none' and c.get('strategy') == item['method']]
            inserts.append({'title': f"{item['method']}: {LABELS.get(item['strategy'], item['strategy'])}", 'source': 'researcher-written' if item.get('harness') is None else item.get('harness'),
                            'file': str(path.relative_to(ROOT)), 'text': path.read_text(), 'record': record_meta(item.get('id')) if str(item.get('id', '')).startswith('agent-') else None,
                            'arms': [{'id': a, **counts.get(a, {})} for a in arms], 'controls': [{'id': c, **counts.get(c, {})} for c in controls]})
    seen = {}
    for c in plan.get('conditions', []):
        file = c.get('contextInsertFile')
        if not file: continue
        seen.setdefault(file, []).append(c)
    for file, conds in seen.items():
        path = ROOT / file
        if not path.exists(): continue
        record_id = None; rid = path.with_name(path.name.replace('-compact', '').replace('-nofb', '').rsplit('.', 1)[0] + '.record-id.txt')
        if rid.exists(): record_id = rid.read_text().strip()
        method = conds[0].get('repository') or conds[0].get('method')
        cell = conds[0].get('parentCondition') or conds[0]['id'].split('__')[0]
        inserts.append({'title': f"{method}: {path.stem.replace('-', ' ')}", 'source': 'agent-acquired', 'file': file, 'text': path.read_text(), 'record': record_meta(record_id),
                        'arms': [{'id': c['id'], **counts.get(c['id'], {})} for c in conds],
                        'controls': [{'id': c['id'], **counts.get(c['id'], {})} for c in plan.get('conditions', []) if c.get('sidecar') == 'none' and (c.get('parentCondition') or c['id'].split('__')[0]) == cell]})
    return inserts


def gather() -> list[dict]:
    rounds = []
    for directory in sorted((p for p in ITERATIONS.iterdir() if (p / 'plan.json').exists()), key=lambda p: natural_key(p.name)):
        inserts = round_inserts(directory)
        if not inserts: continue
        readme = directory / 'README.md'; title = readme.read_text().split('\n', 1)[0].lstrip('# ').strip() if readme.exists() else directory.name
        rounds.append({'id': directory.name, 'title': title, 'inserts': inserts, 'findings': (directory / 'findings.md').exists()})
    return rounds


def render_text(text: str) -> str:
    out = []
    for line in text.split('\n'):
        escaped = html.escape(line)
        if line.startswith('['): out.append(f'<div class="stmt">{escaped}</div>')
        elif line.startswith('Failure behavior:'): out.append(f'<div class="fail">{escaped}</div>')
        elif line.startswith(('Enforcement point:', 'Inspected source:', 'Suggested verification:', 'Relations:', 'Task relevance:', 'Basis:', 'Uncited unknown')): out.append(f'<div class="meta">{escaped}</div>')
        else: out.append(f'<div>{escaped or "&nbsp;"}</div>')
    return '\n'.join(out)


def counts_cell(c: dict) -> str:
    if 'n' not in c: return html.escape(c['id']) + ': no counts'
    return f"{html.escape(c['id'])}: functional {c['functional']} of {c['n']}; failed / unresolved / passed {c['failed']} / {c['unresolved']} / {c['passed']} of {c['planned']}"


def render(rounds: list[dict], rule_v10: str) -> str:
    nav = '\n'.join(f'<li><a href="#{r["id"]}">{html.escape(r["id"])}</a> <span class="dim">{len(r["inserts"])}</span></li>' for r in rounds)
    sections = []
    for r in rounds:
        cards = []
        for i, ins in enumerate(r['inserts']):
            s = stats(ins['text']); rec = ins.get('record') or {}
            meta = ' · '.join(x for x in [ins['source'], rec.get('version') and f"instructions {rec['version']}", rec.get('angle') and f"angle {rec['angle']}", rec.get('model'),
                                          rec.get('commands') is not None and f"{rec['commands']} commands", rec.get('items') is not None and f"{rec['items']} items kept"] if x)
            arms = ''.join(f'<li>{counts_cell(a)}</li>' for a in ins['arms']); controls = ''.join(f'<li class="dim">{counts_cell(c)}</li>' for c in ins['controls'])
            cards.append(f'''<details class="insert" id="{r["id"]}-{i}"><summary><b>{html.escape(ins["title"])}</b> <span class="dim">{s["statements"]} statements · {s["failureLines"]} failure lines · {s["characters"]:,} characters</span></summary>
<p class="meta-line">{html.escape(meta)}{' · record ' + html.escape(rec['id']) if rec.get('id') else ''} · <code>{html.escape(ins["file"])}</code></p>
<div class="counts"><b>Arms that received it</b><ul>{arms}</ul><b>Fresh control</b><ul>{controls}</ul></div>
<pre class="text">{render_text(ins["text"])}</pre></details>''')
        link = f' · <a href="../research/iterations/{r["id"]}/findings.md">findings</a>' if r['findings'] else ''
        sections.append(f'<section id="{r["id"]}"><h2>{html.escape(r["id"])} <span class="dim">{html.escape(r["title"])}</span>{link}</h2>{"".join(cards)}</section>')
    return f'''<!doctype html><html lang="en"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Security context inserts</title>
<style>
:root{{--bg:#fafaf8;--fg:#1c1c1c;--dim:#6b6b6b;--card:#fff;--line:#e3e1dc;--stmt:#0b3d91;--fail:#fff3cd;--failfg:#6a4a00;--meta:#8a8a8a}}
@media (prefers-color-scheme:dark){{:root{{--bg:#121212;--fg:#e8e8e8;--dim:#9a9a9a;--card:#1c1c1c;--line:#2e2e2e;--stmt:#8fb3ff;--fail:#3a3010;--failfg:#f0d080;--meta:#7a7a7a}}}}
body{{margin:0;background:var(--bg);color:var(--fg);font:14px/1.45 -apple-system,BlinkMacSystemFont,"Segoe UI",Helvetica,Arial,sans-serif}}
.wrap{{display:flex;gap:24px;padding:16px;max-width:1500px;margin:0 auto}} nav{{position:sticky;top:16px;align-self:flex-start;min-width:180px;max-height:90vh;overflow:auto}}
nav ul{{list-style:none;padding:0;margin:0}} nav li{{margin:2px 0}} nav a{{color:var(--fg);text-decoration:none}} nav a:hover{{text-decoration:underline}}
main{{flex:1;min-width:0}} h1{{font-size:20px;margin:0 0 8px}} h2{{font-size:16px;margin:28px 0 8px;border-bottom:1px solid var(--line);padding-bottom:4px}}
.dim{{color:var(--dim);font-weight:normal;font-size:12px}} .intro{{max-width:900px}} .rule{{background:var(--card);border:1px solid var(--line);border-radius:6px;padding:8px 12px;margin:6px 0}}
details.insert{{background:var(--card);border:1px solid var(--line);border-radius:6px;padding:6px 12px;margin:8px 0}} summary{{cursor:pointer}}
.meta-line{{color:var(--dim);font-size:12px;margin:6px 0}} .counts{{font-size:12px;margin:6px 0}} .counts ul{{margin:2px 0 6px 18px;padding:0}}
pre.text{{white-space:pre-wrap;word-break:break-word;font:12px/1.5 ui-monospace,SFMono-Regular,Menlo,monospace;margin:8px 0 4px}}
.stmt{{color:var(--stmt);font-weight:600;margin-top:10px}} .fail{{background:var(--fail);color:var(--failfg);padding:1px 4px;border-radius:3px}} .meta{{color:var(--meta)}}
input#q{{width:100%;box-sizing:border-box;padding:6px 8px;border:1px solid var(--line);border-radius:6px;background:var(--card);color:var(--fg);margin:8px 0 12px}}
</style></head><body><div class="wrap"><nav><b>Rounds</b><ul>{nav}</ul></nav><main>
<h1>Security context inserts, every round</h1>
<div class="intro"><p class="dim">Each insert is shown exactly as appended after the task. Blue lines are statement headers ([id; kind; basis; threat; catalogue ids]); highlighted lines are the failure-behaviour clauses; grey lines are anchors and verification hints. Counts beside each insert are the round's own: functional artifacts of N, and issue checks failed / unresolved / passed of 10N, for the arms that received the insert and for the round's fresh control.</p>
<p><b>Agent instruction versions.</b> v7 and v8 (rounds I10 to I18): no rule about failure behaviour. v9 (I19) adds rule 6:</p><div class="rule">{html.escape(RULE_V9)}</div><p>v10 (I20, I21a, I21b) replaces it with:</p><div class="rule">{html.escape(rule_v10)}</div>
<input id="q" type="search" placeholder="Filter inserts by text (for example: storeRun, skip, Player, retain)"></div>
{"".join(sections)}
</main></div>
<script>
const q=document.getElementById('q');q.addEventListener('input',()=>{{const v=q.value.toLowerCase();document.querySelectorAll('details.insert').forEach(d=>{{const hit=!v||d.textContent.toLowerCase().includes(v);d.style.display=hit?'':'none';if(v&&hit)d.open=true;}});}});
</script></body></html>'''


def main():
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--output', type=Path, default=ROOT / 'docs/contexts.html'); args = parser.parse_args()
    rule = next((l.strip() for l in (ROOT / 'research/security/agent/common.md').read_text().split('\n') if l.startswith('6.')), '')
    rounds = gather(); args.output.parent.mkdir(parents=True, exist_ok=True); args.output.write_text(render(rounds, rule))
    print(f'{len(rounds)} rounds, {sum(len(r["inserts"]) for r in rounds)} inserts -> {args.output}')


if __name__ == '__main__':
    main()
