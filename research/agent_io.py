"""One page per acquisition: what went into the context agent and what came out.

For every local acquisition record with a validated output
(`.local/context-generation/agent-*/record.json`), the page shows side by side
the exact initial prompt the agent received (task, workspace, ground rules,
angle, output instructions) with its model and settings, and the validated
document it produced (summary, assets, boundaries, every statement with its
failure behaviour, enforcement point, anchors and verification), plus the
compact insert as later delivered to the generator when a round used it.
Writes a self-contained HTML file. No model calls.
"""
from __future__ import annotations

import argparse
import html
import json
from pathlib import Path
import re

from research.import_evidence import ROOT

RECORDS = ROOT / '.local/context-generation'
ITERATIONS = ROOT / 'research/iterations'


def natural_key(name: str):
    match = re.match(r'i(\d+)([a-z]?)-', name)
    return (int(match.group(1)), match.group(2)) if match else (999, name)


def usage_index() -> dict[str, list[dict]]:
    """record id -> rounds and compact insert files that used it."""
    out: dict[str, list[dict]] = {}
    for rid_file in sorted(ITERATIONS.glob('*/contexts/*.record-id.txt'), key=lambda p: natural_key(p.parent.parent.name)):
        record_id = rid_file.read_text().strip(); stem = rid_file.name.replace('.record-id.txt', '')
        compact = next((p for p in rid_file.parent.glob(f'{stem}-compact*.txt')), None)
        out.setdefault(record_id, []).append({'round': rid_file.parent.parent.name, 'compact': str(compact.relative_to(ROOT)) if compact else None})
    return out


def gather() -> list[dict]:
    usage = usage_index(); records = []
    for directory in RECORDS.glob('agent-*'):
        path = directory / 'record.json'
        if not path.exists(): continue
        r = json.loads(path.read_text())
        if not r.get('output') or not str(r.get('protocol', '')).startswith('repository-security-context'): continue
        version = re.search(r'-v(\d+)-', r['protocol']); commands = r.get('commandsExecuted')
        records.append({'id': r['id'], 'method': r['method'], 'angle': r['angle'], 'version': f"v{version.group(1)}" if version else r['protocol'], 'model': r.get('model'),
                        'settings': r.get('settings', {}), 'status': r.get('status'), 'started': r.get('startedAt'), 'commands': len(commands) if isinstance(commands, list) else commands,
                        'turns': len(r.get('turns') or []), 'prompt': r.get('initialPrompt') or '', 'output': r['output'], 'used': usage.get(r['id'], [])})
    version_key = lambda v: int(v[1:]) if re.fullmatch(r'v\d+', v) else 0
    records.sort(key=lambda x: (version_key(x['version']), x['method'], x['angle'], x['started'] or ''))
    return records


def esc(value) -> str:
    return html.escape('' if value is None else str(value))


def render_prompt(text: str) -> str:
    out = []
    for line in text.split('\n'):
        if re.fullmatch(r'[A-Z][A-Z /-]{2,}', line.strip()): out.append(f'<div class="hdr">{esc(line)}</div>')
        else: out.append(f'<div>{esc(line) or "&nbsp;"}</div>')
    return '\n'.join(out)


def render_anchor(a: dict) -> str:
    parts = [a.get('symbol'), a.get('file')]
    lines = a.get('lines') or a.get('range')
    if isinstance(lines, (list, tuple)) and len(lines) == 2: parts.append(f'{lines[0]}-{lines[1]}')
    elif a.get('start') is not None: parts.append(f"{a.get('start')}-{a.get('end')}")
    return esc(' '.join(str(p) for p in parts if p))


def render_output(output: dict) -> str:
    parts = [f'<p class="summary">{esc(output.get("summary"))}</p>']
    if output.get('assets'):
        parts.append('<h4>Assets</h4><ul>' + ''.join(f'<li><b>{esc(a.get("name"))}</b> [{esc(a.get("property"))}] {esc(a.get("description") or "")}</li>' for a in output['assets']) + '</ul>')
    if output.get('boundaries'):
        parts.append('<h4>Trust boundaries</h4><ul>' + ''.join(f'<li><b>{esc(b.get("name") or b.get("label"))}</b>{" [entry point]" if b.get("entry_point") else ""}: {esc(b.get("untrusted_input"))} from {esc(b.get("source"))} to {esc(b.get("sink"))}</li>' for b in output['boundaries']) + '</ul>')
    parts.append('<h4>Statements</h4>')
    for it in output.get('items', []):
        tags = [it.get('id'), it.get('kind'), it.get('basis'), it.get('threat')] + list(it.get('cwe') or []) + list(it.get('capec') or [])
        card = [f'<div class="item"><div class="stmt">[{esc("; ".join(str(t) for t in tags if t))}] {esc(it.get("statement"))}</div>']
        if it.get('task_relevance'): card.append(f'<div class="meta">Task relevance: {esc(it["task_relevance"])}</div>')
        ep = it.get('enforcement_point')
        if ep: card.append(f'<div class="meta">Enforcement point: {render_anchor(ep) if isinstance(ep, dict) else esc(ep)}</div>')
        if it.get('failure_behavior'): card.append(f'<div class="fail">Failure behavior: {esc(it["failure_behavior"])}</div>')
        anchors = it.get('anchors') or []
        if anchors: card.append('<div class="meta">Anchors: ' + '; '.join(render_anchor(a) if isinstance(a, dict) else esc(a) for a in anchors) + '</div>')
        if it.get('verification'): card.append(f'<div class="meta">Verification: {esc(it["verification"])}</div>')
        card.append('</div>'); parts.append(''.join(card))
    if output.get('limitations'): parts.append('<h4>Limitations the agent stated</h4><ul>' + ''.join(f'<li>{esc(l)}</li>' for l in output['limitations']) + '</ul>')
    return '\n'.join(parts)


def render(records: list[dict]) -> str:
    nav, panels = [], []
    for i, r in enumerate(records):
        used = ', '.join(u['round'] for u in r['used']) or 'not used in a delivery round'
        nav.append(f'<li><a href="#" data-i="{i}"><b>{esc(r["version"])} {esc(r["method"])} {esc(r["angle"])}</b><br><span class="dim">{esc(used)}</span></a></li>')
        compact = next((u['compact'] for u in r['used'] if u['compact']), None)
        delivered = (ROOT / compact).read_text() if compact else None
        items = r['output'].get('items', [])
        settings = ', '.join(f'{k} {v}' for k, v in (r['settings'] or {}).items())
        head = (f'<h2>{esc(r["version"])} · {esc(r["method"])} · {esc(r["angle"])} <span class="dim">{esc(r["id"])}</span></h2>'
                f'<p class="dim">{esc(r["model"])} · {esc(settings)} · {esc(r["commands"])} shell commands · {r["turns"]} turns · status {esc(r["status"])} · used in: {esc(used)}</p>')
        inp = f'<section class="col"><h3>In: the prompt the agent received <span class="dim">{len(r["prompt"]):,} characters</span></h3><pre>{render_prompt(r["prompt"])}</pre></section>'
        outp = (f'<section class="col"><h3>Out: the validated document <span class="dim">{len(items)} statements</span></h3>{render_output(r["output"])}'
                + (f'<h3>As delivered to the generator <span class="dim">compact insert, {len(delivered):,} characters, <code>{esc(compact)}</code></span></h3><pre>{render_prompt(delivered)}</pre>' if delivered else '') + '</section>')
        panels.append(f'<article class="panel" data-i="{i}" hidden>{head}<div class="cols">{inp}{outp}</div></article>')
    return f'''<!doctype html><html lang="en"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Context agent, in and out</title>
<style>
:root{{--bg:#fafaf8;--fg:#1c1c1c;--dim:#6b6b6b;--card:#fff;--line:#e3e1dc;--stmt:#0b3d91;--fail:#fff3cd;--failfg:#6a4a00;--meta:#8a8a8a;--hdr:#333}}
@media (prefers-color-scheme:dark){{:root{{--bg:#121212;--fg:#e8e8e8;--dim:#9a9a9a;--card:#1c1c1c;--line:#2e2e2e;--stmt:#8fb3ff;--fail:#3a3010;--failfg:#f0d080;--meta:#7a7a7a;--hdr:#ddd}}}}
body{{margin:0;background:var(--bg);color:var(--fg);font:14px/1.45 -apple-system,BlinkMacSystemFont,"Segoe UI",Helvetica,Arial,sans-serif}}
.wrap{{display:flex;gap:20px;padding:16px;max-width:1700px;margin:0 auto}} nav{{position:sticky;top:16px;align-self:flex-start;min-width:230px;max-height:92vh;overflow:auto}}
nav ul{{list-style:none;padding:0;margin:0}} nav li{{margin:4px 0}} nav a{{display:block;color:var(--fg);text-decoration:none;padding:4px 6px;border-radius:4px}} nav a:hover,nav a.on{{background:var(--card);outline:1px solid var(--line)}}
main{{flex:1;min-width:0}} h1{{font-size:20px;margin:0 0 4px}} h2{{font-size:16px;margin:0 0 4px}} h3{{font-size:14px;margin:0 0 8px}} h4{{font-size:13px;margin:12px 0 4px}}
.dim{{color:var(--dim);font-weight:normal;font-size:12px}} .cols{{display:flex;gap:16px;align-items:flex-start}} .col{{flex:1;min-width:0;background:var(--card);border:1px solid var(--line);border-radius:6px;padding:10px 12px}}
@media (max-width:1100px){{.cols{{flex-direction:column}}}}
pre{{white-space:pre-wrap;word-break:break-word;font:12px/1.5 ui-monospace,SFMono-Regular,Menlo,monospace;margin:0}} .hdr{{font-weight:700;color:var(--hdr);margin-top:8px}}
.item{{border-top:1px solid var(--line);padding:8px 0}} .stmt{{color:var(--stmt);font-weight:600}} .fail{{background:var(--fail);color:var(--failfg);padding:2px 4px;border-radius:3px;margin:3px 0}} .meta{{color:var(--meta);font-size:12px}} .summary{{margin:0 0 6px}}
ul{{margin:2px 0 6px 18px;padding:0}} code{{font-size:11px}}
</style></head><body><div class="wrap"><nav><h1>Context agent</h1><p class="dim">In: the prompt. Out: the validated document and the insert the generator got. {len(records)} acquisitions, by instruction version.</p><ul>{"".join(nav)}</ul></nav><main>{"".join(panels)}</main></div>
<script>
const links=[...document.querySelectorAll('nav a')],panels=[...document.querySelectorAll('.panel')];
function show(i){{panels.forEach(p=>p.hidden=p.dataset.i!==String(i));links.forEach(l=>l.classList.toggle('on',l.dataset.i===String(i)));location.hash='a'+i;}}
links.forEach(l=>l.addEventListener('click',e=>{{e.preventDefault();show(l.dataset.i);}}));
const start=parseInt((location.hash||'#a0').slice(2))||0;show(isNaN(start)?0:start);
</script></body></html>'''


def main():
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--output', type=Path, default=ROOT / 'workbench/public/agent-io.html'); args = parser.parse_args()
    records = gather(); args.output.parent.mkdir(parents=True, exist_ok=True); args.output.write_text(render(records))
    print(f'{len(records)} acquisitions -> {args.output}')


if __name__ == '__main__':
    main()
