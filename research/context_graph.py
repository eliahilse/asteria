"""Graph-shaped security context over a validated context document and the code model.

Nodes are assets, trust boundaries, typed context items, and the code symbols and
files they are anchored to. Edges are typed from the item kinds and anchors.
The graph renders as the static prompt insert, exports to DOT, and yields
adaptive slices: the statements anchored at, or one call-edge away from, the
code a developer agent has touched so far.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path

from research.import_evidence import canonical, digest

ITEM_KINDS = ('security_property', 'existing_risk', 'change_risk', 'requirement', 'control', 'verification', 'unknown')
RISKS = ('existing_risk', 'change_risk')
RELATION = {('control', 'requirement'): 'satisfies', ('requirement', 'existing_risk'): 'mitigates', ('requirement', 'change_risk'): 'mitigates',
            ('verification', 'requirement'): 'verifies', ('verification', 'control'): 'verifies', ('existing_risk', 'security_property'): 'weakens',
            ('change_risk', 'security_property'): 'weakens', ('control', 'existing_risk'): 'mitigates', ('control', 'change_risk'): 'mitigates'}
ORDER = ('asset', 'boundary', 'security_property', 'existing_risk', 'change_risk', 'requirement', 'control', 'verification', 'unknown')


def _anchor_edges(source: str, anchors: list[dict], edge_type: str, nodes: dict, edges: list):
    for anchor in anchors:
        file = anchor.get('resolvedFile') or anchor.get('file'); start, end = anchor.get('resolvedStart') or anchor.get('start_line'), anchor.get('resolvedEnd') or anchor.get('end_line')
        symbols = anchor.get('symbols') or ([anchor['symbol']] if anchor.get('symbol') else [])
        if file: nodes.setdefault(f'file:{file}', {'id': f'file:{file}', 'kind': 'file', 'label': file})
        for symbol in symbols:
            nodes.setdefault(f'symbol:{symbol}', {'id': f'symbol:{symbol}', 'kind': 'symbol', 'label': symbol, 'file': file})
            edges.append({'from': source, 'to': f'symbol:{symbol}', 'type': edge_type, 'file': file, 'start': start, 'end': end})
        if not symbols and file:
            edges.append({'from': source, 'to': f'file:{file}', 'type': edge_type, 'file': file, 'start': start, 'end': end})


def build(document: dict, model: dict | None = None) -> dict:
    """Typed graph from a validated context document (research.context_agent.validate_output) and an optional code model."""
    nodes, edges = {}, []
    for index, asset in enumerate(document.get('assets', []), 1):
        node_id = f'asset:A{index}'; nodes[node_id] = {'id': node_id, 'kind': 'asset', 'label': asset['name'], 'property': asset['property']}
        _anchor_edges(node_id, asset.get('anchors', []), 'located_at', nodes, edges)
    for index, boundary in enumerate(document.get('boundaries', []), 1):
        node_id = f'boundary:B{index}'
        nodes[node_id] = {'id': node_id, 'kind': 'boundary', 'label': boundary['name'], 'untrusted_input': boundary['untrusted_input'], 'source': boundary['source'], 'sink': boundary['sink']}
        _anchor_edges(node_id, boundary.get('anchors', []), 'crosses', nodes, edges)
    items = {item['id']: item for item in document.get('items', [])}
    for item in items.values():
        node_id = f"item:{item['id']}"
        nodes[node_id] = {'id': node_id, 'kind': item['kind'], 'label': item['statement'], 'basis': item['basis'], 'threat': item.get('threat'),
                          'cwe': item.get('cwe', []), 'capec': item.get('capec', []), 'asvs': item.get('asvs', []), 'cert': item.get('cert', []),
                          'failure_behavior': item.get('failure_behavior'), 'verification': item.get('verification'), 'task_relevance': item.get('task_relevance')}
        _anchor_edges(node_id, item.get('anchors', []), 'anchored_at', nodes, edges)
        if item.get('enforcement_point'): _anchor_edges(node_id, [item['enforcement_point']], 'enforced_at', nodes, edges)
        for target in item.get('related', []):
            if target in items:
                edges.append({'from': node_id, 'to': f'item:{target}', 'type': RELATION.get((item['kind'], items[target]['kind']), 'related')})
    if model:
        symbols = {node['label'] for node in nodes.values() if node['kind'] == 'symbol'}
        by_id = {s['id']: s for s in model.get('symbols', [])}
        for symbol in symbols:
            record = by_id.get(symbol)
            if record: nodes[f'symbol:{symbol}'].update(sinks=record.get('sinks', []), start=record.get('start'), end=record.get('end'))
        for edge in model.get('edges', []):
            if edge['from'] in symbols or edge['to'] in symbols:
                for end in (edge['from'], edge['to']): nodes.setdefault(f'symbol:{end}', {'id': f'symbol:{end}', 'kind': 'symbol', 'label': end, 'file': by_id.get(end, {}).get('file')})
                edges.append({'from': f"symbol:{edge['from']}", 'to': f"symbol:{edge['to']}", 'type': 'calls'})
    graph = {'schemaVersion': 1, 'angle': document.get('angle'), 'summary': document.get('summary'), 'limitations': document.get('limitations', []),
             'nodes': sorted(nodes.values(), key=lambda n: (ORDER.index(n['kind']) if n['kind'] in ORDER else len(ORDER), n['id'])), 'edges': edges}
    graph['metrics'] = metrics(graph, model); graph['sha256'] = digest(canonical({k: v for k, v in graph.items() if k != 'sha256'}))
    return graph


def metrics(graph: dict, model: dict | None = None) -> dict:
    nodes = {n['id']: n for n in graph['nodes']}; by_kind, by_type = {}, {}
    for node in graph['nodes']: by_kind[node['kind']] = by_kind.get(node['kind'], 0) + 1
    for edge in graph['edges']: by_type[edge['type']] = by_type.get(edge['type'], 0) + 1
    items = [n for n in graph['nodes'] if n['kind'] in ITEM_KINDS]
    anchored = {e['from'] for e in graph['edges'] if e['type'] in ('anchored_at', 'enforced_at')}
    controls = [n for n in items if n['kind'] == 'control']
    enforced = {e['from'] for e in graph['edges'] if e['type'] == 'enforced_at'}
    linked = {e['from'] for e in graph['edges'] if e['type'] in ('satisfies', 'mitigates')} | {e['to'] for e in graph['edges'] if e['type'] in ('satisfies', 'mitigates')}
    result = {'nodesByKind': by_kind, 'edgesByType': by_type, 'items': len(items), 'anchoredItems': sum(1 for n in items if n['id'] in anchored),
              'controls': len(controls), 'controlsWithEnforcementPoint': sum(1 for n in controls if n['id'] in enforced),
              'itemsLinkedToRequirementOrRisk': sum(1 for n in items if n['id'] in linked and n['kind'] in ('control', 'requirement'))}
    if model:
        sink_symbols = {s['id'] for s in model.get('symbols', []) if s.get('sinks')}
        covered = {nodes[e['to']]['label'] for e in graph['edges'] if e['type'] in ('anchored_at', 'enforced_at', 'crosses') and e['to'] in nodes and nodes[e['to']]['kind'] == 'symbol'}
        result['sinkSymbols'] = len(sink_symbols); result['sinkSymbolsAnchored'] = len(sink_symbols & covered)
    return result


def _location(edge: dict) -> str:
    symbol = edge['to'].split(':', 1)[1] if edge['to'].startswith('symbol:') else None
    place = f"{edge.get('file')}:{edge.get('start')}-{edge.get('end')}" if edge.get('file') and edge.get('start') else (edge.get('file') or '')
    return f'{place} ({symbol})' if symbol and place else (symbol or place)


def render_items(graph: dict, ids: list[str] | None = None) -> list[str]:
    nodes = {n['id']: n for n in graph['nodes']}; out = {}
    for edge in graph['edges']: out.setdefault(edge['from'], []).append(edge)
    lines = []
    for node in graph['nodes']:
        if node['kind'] not in ITEM_KINDS or (ids is not None and node['id'] not in ids): continue
        tags = [node['id'].split(':', 1)[1], node['kind'], node['basis']] + ([node['threat']] if node.get('threat') else []) + node['cwe'] + node['capec'] + node['asvs'] + node['cert']
        lines += ['', f"[{'; '.join(tags)}] {node['label']}", 'Task relevance: ' + (node.get('task_relevance') or '')]
        for edge in out.get(node['id'], []):
            if edge['type'] == 'enforced_at': lines.append('Enforcement point: ' + _location(edge))
        if node.get('failure_behavior'): lines.append('Failure behavior: ' + node['failure_behavior'])
        cited = [e for e in out.get(node['id'], []) if e['type'] == 'anchored_at']
        for edge in cited: lines.append('Inspected source: ' + _location(edge))
        if not cited: lines.append('Uncited unknown; absence was not established.' if node['kind'] == 'unknown' else 'Basis: prospective task requirement or reasoning; not an implemented safeguard.')
        if node.get('verification'): lines.append('Suggested verification: ' + node['verification'])
        relations = [f"{e['type']} {nodes[e['to']]['id'].split(':', 1)[1]}" for e in out.get(node['id'], []) if e['type'] in RELATION.values() or e['type'] == 'related']
        if relations: lines.append('Relations: ' + ', '.join(relations))
    return lines


def render(graph: dict) -> str:
    """Static insert in graph order: assets, boundaries, then items by kind."""
    nodes = {n['id']: n for n in graph['nodes']}; out = {}
    for edge in graph['edges']: out.setdefault(edge['from'], []).append(edge)
    lines = [f"--- BEGIN REPOSITORY-DERIVED SECURITY CONTEXT (angle: {graph.get('angle')}) ---", graph.get('summary') or '']
    assets = [n for n in graph['nodes'] if n['kind'] == 'asset']; boundaries = [n for n in graph['nodes'] if n['kind'] == 'boundary']
    if assets: lines += ['', 'Assets:'] + [f"- {a['label']} [{a['property']}]" + (' @ ' + '; '.join(_location(e) for e in out.get(a['id'], [])) if out.get(a['id']) else '') for a in assets]
    if boundaries: lines += ['', 'Trust boundaries:'] + [f"- {b['label']}: {b['untrusted_input']} from {b['source']} to {b['sink']}" + (' @ ' + '; '.join(_location(e) for e in out.get(b['id'], [])) if out.get(b['id']) else '') for b in boundaries]
    lines += render_items(graph)
    lines += ['', 'Uncertainty and limits:', *['- ' + value for value in graph.get('limitations', [])], '--- END REPOSITORY-DERIVED SECURITY CONTEXT ---', '']
    return '\n'.join(lines)


def slice_for(graph: dict, touched_files: set[str], touched_symbols: set[str], shown: set[str], hops: int = 1) -> tuple[str | None, list[str]]:
    """Statements anchored at touched code or within `hops` call edges of it, excluding ids already shown."""
    nodes = {n['id']: n for n in graph['nodes']}
    relevant = {n['id'] for n in graph['nodes'] if n['kind'] == 'symbol' and (n['label'] in touched_symbols or (n.get('file') in touched_files))}
    relevant |= {n['id'] for n in graph['nodes'] if n['kind'] == 'file' and n['label'] in touched_files}
    frontier = set(relevant)
    for _ in range(hops):
        step = set()
        for edge in graph['edges']:
            if edge['type'] == 'calls' and (edge['from'] in frontier or edge['to'] in frontier): step |= {edge['from'], edge['to']}
        frontier = step - relevant; relevant |= step
    ids = []
    for edge in graph['edges']:
        if edge['type'] in ('anchored_at', 'enforced_at') and edge['to'] in relevant and edge['from'] not in shown and edge['from'] not in ids: ids.append(edge['from'])
    for edge in list(graph['edges']):  # one hop along typed item relations, both directions
        if edge['type'] in RELATION.values():
            if edge['from'] in ids and edge['to'] not in shown and edge['to'] not in ids: ids.append(edge['to'])
            if edge['to'] in ids and edge['from'] not in shown and edge['from'] not in ids: ids.append(edge['from'])
    ids = [i for i in ids if nodes[i]['kind'] in ITEM_KINDS]
    if not ids: return None, []
    ordered = [n['id'] for n in graph['nodes'] if n['id'] in ids]
    text = '\n'.join(['--- SECURITY CONTEXT FOR THE CODE YOU ARE TOUCHING ---', *render_items(graph, ordered), '--- END ---'])
    return text, ordered


class Sidecar:
    """Adapter for research.agentic_delivery: static insert or adaptive slices from one graph."""

    def __init__(self, graph: dict, mode: str = 'adaptive'):
        if mode not in ('none', 'static', 'adaptive'): raise ValueError('Unknown sidecar mode')
        self.graph, self.mode = graph, mode

    def initial(self, condition=None):
        return render(self.graph) if self.mode == 'static' else None

    def update(self, touched: dict, shown_ids: set[str]):
        if self.mode != 'adaptive': return None, []
        return slice_for(self.graph, set(touched.get('files') or ()), set(touched.get('symbols') or ()), set(shown_ids))


def to_dot(graph: dict) -> str:
    shapes = {'asset': 'ellipse', 'boundary': 'hexagon', 'symbol': 'box', 'file': 'note', 'control': 'octagon', 'requirement': 'diamond'}
    lines = ['digraph context {', '  rankdir=LR; node [fontsize=9];']
    for node in graph['nodes']:
        label = node['label'].replace('"', "'"); label = label[:60] + ('…' if len(label) > 60 else '')
        lines.append(f'  "{node["id"]}" [label="{node["kind"]}\\n{label}" shape={shapes.get(node["kind"], "box")}];')
    for edge in graph['edges']: lines.append(f'  "{edge["from"]}" -> "{edge["to"]}" [label="{edge["type"]}"];')
    return '\n'.join(lines + ['}', ''])


def main():
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--record', type=Path, required=True, help='context_agent record.json')
    parser.add_argument('--output', type=Path); parser.add_argument('--dot', type=Path); parser.add_argument('--render', action='store_true')
    args = parser.parse_args(); record = json.loads(args.record.read_text())
    if not record.get('output'): raise SystemExit('Record has no validated output')
    model_path = args.record.parent / 'workspace' / 'code-model.json'; model = json.loads(model_path.read_text()) if model_path.exists() else None
    graph = build(record['output'], model)
    if args.output: args.output.write_bytes(canonical(graph))
    if args.dot: args.dot.write_text(to_dot(graph))
    print(render(graph) if args.render else json.dumps(graph['metrics'], indent=1))


if __name__ == '__main__':
    main()
