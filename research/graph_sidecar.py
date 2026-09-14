"""Configurable graph sidecar for delivery rounds after I10.

One context graph per method (frozen under an iteration's contexts/ folder as
<method>-<angle>.graph.json). The static insert is context_graph.render with the
configured statement kinds; adaptive slices anchor on the line ranges the
generator edited or read (symbol granularity) or on whole files (file
granularity). The configuration is reported by describe() and recorded in every
trajectory by the harness.
"""
from __future__ import annotations

import json
from pathlib import Path
import re

from research import context_graph
from research.import_evidence import ROOT, digest

COMPACT = ('requirement', 'control')


def normalize(path: str) -> str:
    path = re.sub(r'/[^/]+\.jar!/', '/', path)
    return re.sub(r'^([^/]+)/src/', r'\1/', path)


class GraphSidecar:
    def __init__(self, directory: Path, angle: str = 'dataflow', granularity: str = 'symbol', hops: int = 0, kinds: tuple[str, ...] | None = COMPACT, header: bool = False):
        if granularity not in ('symbol', 'file'): raise ValueError('granularity must be symbol or file')
        self.directory, self.angle, self.granularity, self.hops, self.kinds, self.header = Path(directory), angle, granularity, hops, kinds, header
        self.graphs, self.graph_hashes = {}, {}
        for method in ('Generation', 'Reuse'):
            path = self.directory / f'{method.lower()}-{angle}.graph.json'
            if path.exists():
                raw = path.read_bytes(); self.graphs[method] = json.loads(raw); self.graph_hashes[method] = digest(raw)
        self.file_index = {m: {normalize(n['label']): n['label'] for n in g['nodes'] if n['kind'] == 'file'} for m, g in self.graphs.items()}
        self.symbol_files = {m: {n['label']: normalize(n.get('file') or '') for n in g['nodes'] if n['kind'] == 'symbol'} for m, g in self.graphs.items()}

    def describe(self) -> dict:
        return {'module': 'research.graph_sidecar', 'moduleSha256': digest(Path(__file__).read_bytes()), 'angle': self.angle, 'granularity': self.granularity,
                'hops': self.hops, 'kinds': list(self.kinds) if self.kinds else None, 'header': self.header, 'graphs': dict(self.graph_hashes)}

    def graph_for(self, method: str) -> dict:
        if method not in self.graphs: raise ValueError(f'No frozen {method} context graph in {self.directory}')
        return self.graphs[method]

    def initial(self, condition) -> str | None:
        return context_graph.render(self.graph_for(condition['strategy']), kinds=self.kinds, header=self.header)

    def update(self, touched: dict, shown_ids: set[str]):
        method = touched.get('method'); graph = self.graph_for(method)
        if self.granularity == 'symbol':
            ranges = {normalize(path): [tuple(r) for r in spans] for path, spans in (touched.get('ranges') or {}).items()}
            symbols = context_graph.symbols_in_ranges(graph, ranges, normalize) | set(touched.get('symbols') or ())
            return context_graph.slice_for(graph, set(), symbols, set(shown_ids), hops=self.hops, kinds=self.kinds)
        wanted = {normalize(f) for f in touched.get('files') or ()}
        files = {label for norm, label in self.file_index[method].items() if norm in wanted}
        symbols = {symbol for symbol, file in self.symbol_files[method].items() if file in wanted} | set(touched.get('symbols') or ())
        return context_graph.slice_for(graph, files, symbols, set(shown_ids), hops=self.hops, kinds=self.kinds)


def SIDECAR():
    """Default for I11: symbol granularity, no call hops, requirement and control statements only."""
    return GraphSidecar(ROOT / 'research/iterations/i11-symbol-sidecar/contexts')
