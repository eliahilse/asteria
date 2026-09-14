"""I10 sidecar: one context graph per method, static insert or adaptive slices.

Graphs are frozen under research/iterations/i10-agentic-delivery/contexts/ as
<method>-dataflow.graph.json; the static insert files beside them are
context_graph.render(graph) byte for byte. Snapshot paths (jar members as
Game/Game.jar!/pkg/Type.java, loose sources as Game/pkg/Type.java) and workspace
paths (Game/src/pkg/Type.java) are normalized to Game/pkg/Type.java.
"""
from __future__ import annotations

import json
from pathlib import Path
import re

from research import context_graph
from research.import_evidence import ROOT

CONTEXTS = ROOT / 'research/iterations/i10-agentic-delivery/contexts'


def normalize(path: str) -> str:
    path = re.sub(r'/[^/]+\.jar!/', '/', path)
    return re.sub(r'^([^/]+)/src/', r'\1/', path)


class MethodSidecar:
    def __init__(self, directory: Path = CONTEXTS, angle: str = 'dataflow'):
        self.graphs = {}
        for method in ('Generation', 'Reuse'):
            path = directory / f'{method.lower()}-{angle}.graph.json'
            if path.exists():
                graph = json.loads(path.read_text())
                self.graphs[method] = graph
                self.files = {}
        self.file_index = {method: {normalize(n['label']): n['label'] for n in g['nodes'] if n['kind'] == 'file'} for method, g in self.graphs.items()}
        self.symbol_files = {method: {n['label']: normalize(n.get('file') or '') for n in g['nodes'] if n['kind'] == 'symbol'} for method, g in self.graphs.items()}

    def graph_for(self, method: str) -> dict:
        if method not in self.graphs: raise ValueError(f'No frozen {method} context graph')
        return self.graphs[method]

    def initial(self, condition) -> str | None:
        return context_graph.render(self.graph_for(condition['strategy']))

    def update(self, touched: dict, shown_ids: set[str]):
        method = touched.get('method')
        graph = self.graph_for(method)
        wanted = {normalize(f) for f in touched.get('files') or ()}
        files = {label for norm, label in self.file_index[method].items() if norm in wanted}
        symbols = {symbol for symbol, file in self.symbol_files[method].items() if file in wanted} | set(touched.get('symbols') or ())
        return context_graph.slice_for(graph, files, symbols, set(shown_ids))


SIDECAR = MethodSidecar
