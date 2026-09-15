"""AST autocontext sidecar: the code graph of what enters the generator's viewport, injected after every read.

A frozen *code graph* per method (`<method>-code-graph.json` under an
iteration's contexts/ folder) is the compact form of the tree-sitter code
model of the repository snapshot (research.code_model): one entry per class,
constructor and method with its file and line range, and the resolved call
edges between them. After each read or search (and before a submission, for the code
the generator edited) the sidecar takes the symbols whose line ranges overlap
what was shown, and hands back, for each one not shown before, its callers and
callees with the file path and line range the generator can read next. No
security statements, no model calls; the same lines are never injected
twice. `Composite` joins this sidecar with a guard judge on one arm.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path
import re

from research.graph_sidecar import GUARD_SYSTEM_V2, GuardSidecar, normalize
from research.import_evidence import ROOT, digest

GRAPH_KINDS = ('class', 'interface', 'constructor', 'method')
HEADER = '--- CODE GRAPH (automatic): callers and callees of the code just read, from the parsed repository snapshot ---'
FOOTER = '--- END CODE GRAPH ---'


def compact_graph(model: dict) -> dict:
    """The code graph kept from a research.code_model build: classes, constructors and methods with ranges, their unresolved call names, and the resolved edges."""
    keep = {s['id'] for s in model['symbols'] if s['kind'] in GRAPH_KINDS}
    symbols = [{'id': s['id'], 'kind': s['kind'], 'file': s['file'], 'start': s['start'], 'end': s['end'], 'owner': s.get('owner'),
                'params': [p.get('type') for p in s.get('params') or []], 'returns': s.get('returns'), 'calls': list(s.get('calls') or [])}
               for s in model['symbols'] if s['id'] in keep]
    edges = sorted({(e['from'], e['to']) for e in model['edges'] if e['from'] in keep and e['to'] in keep})
    return {'schemaVersion': 1, 'source': {'fingerprint': model['fingerprint'], 'generator': model.get('generator'), 'root': model.get('root')},
            'files': [{'path': f['path'], 'lines': f['lines'], 'sha256': f['sha256']} for f in model['files']],
            'symbols': symbols, 'edges': [list(e) for e in edges]}


def jar_path(path: str) -> str:
    """Fallback map from a source-tree path to the snapshot's archive path (ApoMario/src/x.java -> ApoMario/ApoMario.jar!/x.java)."""
    return re.sub(r'^([^/]+)/src/', r'\1/\1.jar!/', path)


class AutoContextSidecar:
    def __init__(self, directory: Path, path_maps: dict[str, dict[str, str]] | None = None, max_symbols: int = 6, max_neighbors: int = 8, max_chars: int = 4000):
        """path_maps: per method, normalized path -> the path the generator reads (from the repository snapshot); jar_path is the fallback."""
        self.directory, self.max_symbols, self.max_neighbors, self.max_chars = Path(directory), max_symbols, max_neighbors, max_chars
        self.graphs, self.graph_hashes, self.path_maps = {}, {}, path_maps or {}
        for method in ('Generation', 'Reuse'):
            path = self.directory / f'{method.lower()}-code-graph.json'
            if path.exists(): raw = path.read_bytes(); self.graphs[method] = json.loads(raw); self.graph_hashes[method] = digest(raw)
        self.index = {}
        for method, graph in self.graphs.items():
            symbols = {s['id']: s for s in graph['symbols']}
            callees, callers = {}, {}
            for source, target in graph['edges']: callees.setdefault(source, []).append(target); callers.setdefault(target, []).append(source)
            by_file = {}
            for s in graph['symbols']: by_file.setdefault(normalize(s['file']), []).append(s)
            self.index[method] = {'symbols': symbols, 'callees': callees, 'callers': callers, 'byFile': by_file}

    def describe(self) -> dict:
        return {'module': 'research.autocontext_sidecar', 'moduleSha256': digest(Path(__file__).read_bytes()), 'kind': 'ast', 'graphs': dict(self.graph_hashes),
                'maxSymbols': self.max_symbols, 'maxNeighbors': self.max_neighbors, 'maxChars': self.max_chars, 'pathMaps': {m: len(p) for m, p in self.path_maps.items()}}

    def initial(self, condition): return None

    def graph_for(self, method: str) -> dict:
        if method not in self.graphs: raise ValueError(f'No frozen {method} code graph in {self.directory}')
        return self.index[method]

    def read_path(self, method: str, file: str) -> str:
        return self.path_maps.get(method, {}).get(normalize(file)) or jar_path(file)

    def label(self, method: str, symbol_id: str) -> str:
        s = self.index[method]['symbols'][symbol_id]
        return f"{symbol_id} ({self.read_path(method, s['file'])}:{s['start']}-{s['end']})"

    def block(self, method: str, symbol: dict) -> list[str]:
        index = self.graph_for(method); sid = symbol['id']
        callees = list(dict.fromkeys(index['callees'].get(sid, []))); callers = list(dict.fromkeys(index['callers'].get(sid, [])))
        unresolved = len(symbol.get('calls') or []) - len(callees)
        lines = [f"{sid} [{symbol['kind']}] {self.read_path(method, symbol['file'])}:{symbol['start']}-{symbol['end']}"]
        if callees:
            shown = callees[:self.max_neighbors]; more = len(callees) - len(shown)
            lines.append('  calls: ' + '; '.join(self.label(method, c) for c in shown) + (f'; +{more} more' if more > 0 else '') + (f'; {unresolved} calls outside the snapshot' if unresolved > 0 else ''))
        elif unresolved > 0: lines.append(f'  calls: {unresolved} calls outside the snapshot only')
        if callers:
            shown = callers[:self.max_neighbors]; more = len(callers) - len(shown)
            lines.append('  called by: ' + '; '.join(self.label(method, c) for c in shown) + (f'; +{more} more' if more > 0 else ''))
        else: lines.append('  called by: nothing in the snapshot')
        return lines

    def update(self, touched: dict, shown_ids: set[str]):
        """(text, ids) for the symbols overlapping the touched ranges that were not shown before; (None, []) when nothing new overlaps."""
        method = touched.get('method'); index = self.graph_for(method)
        ranges = {}
        for source in ('ranges', 'searchRanges'):  # reads and edits, and the excerpts returned by searches: everything that entered the viewport
            for path, spans in (touched.get(source) or {}).items(): ranges.setdefault(normalize(path), []).extend(tuple(r) for r in spans)
        candidates = []
        for file, spans in ranges.items():
            for s in index['byFile'].get(file, []):
                if s['kind'] not in ('constructor', 'method') or f'ast:{s["id"]}' in shown_ids: continue
                if any(s['start'] <= end and start <= s['end'] for start, end in spans):
                    degree = len(index['callees'].get(s['id'], [])) + len(index['callers'].get(s['id'], []))
                    candidates.append((-degree, s['file'], s['start'], s))
        candidates.sort(key=lambda c: (c[0], c[1], c[2], c[3]['id']))
        blocks, ids = [], []
        for _, _, _, s in candidates[:self.max_symbols]:
            block = self.block(method, s)
            if sum(len(l) + 1 for l in [HEADER, *blocks, *block, FOOTER]) > self.max_chars and blocks: break
            blocks += block; ids.append(f'ast:{s["id"]}')
        if not blocks: return None, []
        return '\n'.join([HEADER, *blocks, FOOTER]), ids


class Composite:
    """One sidecar object for combined kinds: `update` from the autocontext sidecar, `judge` from the guard, `initial` None (the harness checks the frozen insert itself)."""

    def __init__(self, autocontext: AutoContextSidecar, guard: GuardSidecar):
        self.autocontext, self.guard = autocontext, guard
        self.judge_turns, self.max_interventions = guard.judge_turns, guard.max_interventions

    def describe(self) -> dict:
        return {'module': 'research.autocontext_sidecar', 'moduleSha256': digest(Path(__file__).read_bytes()), 'kind': 'composite',
                'ast': self.autocontext.describe(), 'guard': self.guard.describe(), 'judgeTurns': self.guard.judge_turns, 'maxInterventions': self.guard.max_interventions}

    def initial(self, condition): return None

    def update(self, touched, shown_ids): return self.autocontext.update(touched, shown_ids)

    def judge(self, view): return self.guard.judge(view)


def snapshot_path_maps(parent: str = 'i07-operational-replication') -> dict[str, dict[str, str]]:
    """normalized path -> snapshot path, per method, from the frozen repository inputs of the parent iteration."""
    from research.context_repository import snapshot
    from research.security_followup import repository_input
    maps = {}
    for method in ('Generation', 'Reuse'):
        snap = snapshot(repository_input(method, ROOT / '.local/iterations' / parent))
        maps[method] = {normalize(s['path']): s['path'] for s in snap['sources']}
    return maps


def I26():
    """I26: the S2 data-flow graph of I24b as the guard's insert, the code graph of the same acquisition workspace for autocontext; three judge reads, three cancellations per trajectory."""
    contexts = ROOT / 'research/iterations/i26-graph/contexts'
    return Composite(AutoContextSidecar(contexts, snapshot_path_maps()), GuardSidecar(contexts, kinds=None))


def I27():
    """I27: the I26 contexts with guard v2: submissions only, at most two requirement/control citations, a verbatim quote required, prompt v2; three judge reads, three cancellations per trajectory."""
    contexts = ROOT / 'research/iterations/i26-graph/contexts'
    guard = GuardSidecar(contexts, kinds=None, consult_on=('submit_feature_changes',), require_quote=True, max_cited=2, normative_only=True, system=GUARD_SYSTEM_V2, once_per_statement=True)
    return Composite(AutoContextSidecar(contexts, snapshot_path_maps()), guard)


def main():
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--freeze', nargs=2, metavar=('MODEL_JSON', 'OUT_JSON'), help='write the compact code graph of a code-model.json')
    args = parser.parse_args()
    if args.freeze:
        model = json.loads(Path(args.freeze[0]).read_text()); graph = compact_graph(model)
        Path(args.freeze[1]).write_text(json.dumps(graph, ensure_ascii=False, separators=(',', ':'), sort_keys=True) + '\n')
        print(f"{len(graph['symbols'])} symbols, {len(graph['edges'])} edges, {len(graph['files'])} files -> {args.freeze[1]}")


if __name__ == '__main__':
    main()
