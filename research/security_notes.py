"""Security notes: per-symbol security context surfaced for the code an agent reads, with call edges autoloaded.

A note ties statements of the acquired security context to one class, constructor or method of
the repository snapshot, like a docstring written for security. Notes are derived deterministically
from the acquisition's data-flow graph (research.context_graph): every requirement, control,
observation or risk anchored at, or enforced at, a symbol becomes that symbol's note, keyed by the
code graph's symbol ids (research.autocontext_sidecar.compact_graph). `<method>-notes.json` is
frozen next to `<method>-code-graph.json` in a round's contexts/ folder with the hashes of both
sources.

The NotesSidecar is an injection sidecar (kind `notes`): after each read or search, and before a
submission for the code the generator edited, it hands back the notes of the symbols whose line
ranges overlap what entered the viewport, then the notes of their callers and callees one call
edge out, each with the path and line range the generator can read next. Nothing is shown twice,
no model is called at run time and nothing is vetoed: the sidecar contributes context, the
compiler and the tests judge the result.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path
import re
import shutil

from research.autocontext_sidecar import jar_path, snapshot_path_maps
from research.graph_sidecar import normalize
from research.import_evidence import ROOT, digest

NOTE_KINDS = ('requirement', 'control', 'observation', 'existing_risk', 'change_risk')  # statement kinds that become notes; verification and unknown items stay in the document
RELATIONS = ('enforced_at', 'anchored_at')  # graph edge types tying a statement to code; enforced_at wins when both exist
SYMBOL_KINDS = ('class', 'interface', 'constructor', 'method')
HEADER = '--- SECURITY NOTES (automatic): security context tied to the code just read and to its callers and callees; [ids] are statements of the security context ---'
FOOTER = '--- END SECURITY NOTES ---'
METHODS = ('Generation', 'Reuse')


def statement_order(item_id: str) -> tuple[str, int]:
    match = re.fullmatch(r'([A-Za-z]+)(\d+)', item_id)
    return (match.group(1), int(match.group(2))) if match else (item_id, 0)


def derive_notes(dataflow: dict, code_graph: dict, kinds: tuple[str, ...] = NOTE_KINDS) -> dict:
    """Notes per code-graph symbol from the statements anchored at or enforced at it. Statements anchored at symbols outside the code graph are counted, not kept."""
    nodes = {n['id']: n for n in dataflow['nodes']}
    symbols = {s['id']: s for s in code_graph['symbols'] if s['kind'] in SYMBOL_KINDS}
    notes: dict[str, dict] = {}; skipped: set[str] = set(); used: set[str] = set()
    for edge in dataflow['edges']:
        if edge.get('type') not in RELATIONS or not str(edge.get('to', '')).startswith('symbol:'): continue
        statement = nodes.get(edge['from'])
        if statement is None or not str(statement['id']).startswith('item:') or statement.get('kind') not in kinds: continue
        symbol_id = edge['to'][len('symbol:'):]
        if symbol_id not in symbols: skipped.add(symbol_id); continue
        symbol = symbols[symbol_id]
        note = notes.setdefault(symbol_id, {'kind': symbol['kind'], 'file': symbol['file'], 'start': symbol['start'], 'end': symbol['end'], 'items': []})
        short = statement['id'][len('item:'):]
        existing = next((i for i in note['items'] if i['id'] == short), None)
        item = {'id': short, 'kind': statement['kind'], 'relation': edge['type'], 'text': statement.get('label') or '', 'failureBehavior': statement.get('failure_behavior') or ''}
        if existing is None: note['items'].append(item)
        elif existing['relation'] == 'anchored_at' and edge['type'] == 'enforced_at': existing['relation'] = 'enforced_at'
        used.add(statement['id'])
    for note in notes.values(): note['items'].sort(key=lambda i: statement_order(i['id']))
    return {'schemaVersion': 1, 'kinds': list(kinds), 'notes': dict(sorted(notes.items())),
            'statements': len(used), 'symbols': len(notes), 'skippedSymbols': sorted(skipped)}


def render_note(symbol_id: str, note: dict, path: str, indent: str = '') -> list[str]:
    lines = [f"{indent}{symbol_id} [{note['kind']}] {path}:{note['start']}-{note['end']}"]
    for item in note['items']:
        where = 'enforced here' if item['relation'] == 'enforced_at' else 'concerns this code'
        lines.append(f"{indent}  [{item['id']}; {item['kind']}; {where}] {item['text']}")
        if item['failureBehavior']: lines.append(f"{indent}    on failure: {item['failureBehavior']}")
    return lines


def render_all(notes: dict) -> str:
    return '\n'.join(line for symbol_id, note in notes['notes'].items() for line in [*render_note(symbol_id, note, note['file']), ''])


def freeze(out_dir: Path, dataflow_dir: Path, code_graph_dir: Path, methods: tuple[str, ...] = METHODS) -> dict:
    """Write `<method>-notes.json` (with source hashes) and `<method>-notes.txt` into out_dir and copy the code graphs next to them."""
    out_dir.mkdir(parents=True, exist_ok=True); written = {}
    for method in methods:
        dataflow_path = dataflow_dir / f'{method.lower()}-dataflow.graph.json'; code_path = code_graph_dir / f'{method.lower()}-code-graph.json'
        if not dataflow_path.exists() or not code_path.exists(): raise FileNotFoundError(f'{method}: need {dataflow_path} and {code_path}')
        dataflow_raw, code_raw = dataflow_path.read_bytes(), code_path.read_bytes()
        notes = derive_notes(json.loads(dataflow_raw), json.loads(code_raw))
        notes['method'] = method
        notes['source'] = {'dataflowGraph': str(dataflow_path.relative_to(ROOT)) if dataflow_path.is_relative_to(ROOT) else str(dataflow_path), 'dataflowSha256': digest(dataflow_raw),
                           'codeGraph': str(code_path.relative_to(ROOT)) if code_path.is_relative_to(ROOT) else str(code_path), 'codeGraphSha256': digest(code_raw)}
        target = out_dir / f'{method.lower()}-code-graph.json'
        if target.resolve() != code_path.resolve(): shutil.copyfile(code_path, target)
        (out_dir / f'{method.lower()}-notes.json').write_text(json.dumps(notes, indent=1, sort_keys=True) + '\n')
        (out_dir / f'{method.lower()}-notes.txt').write_text(render_all(notes))
        written[method] = {'symbols': notes['symbols'], 'statements': notes['statements'], 'skippedSymbols': len(notes['skippedSymbols'])}
    return written


class NotesSidecar:
    """Injection sidecar (kind `notes`): the frozen notes of what entered the viewport and of its call neighbours, each once."""

    def __init__(self, directory: Path, path_maps: dict[str, dict[str, str]] | None = None, max_symbols: int = 6, max_neighbors: int = 6, max_chars: int = 4000):
        self.directory, self.max_symbols, self.max_neighbors, self.max_chars = Path(directory), max_symbols, max_neighbors, max_chars
        self.path_maps = path_maps or {}
        self.notes, self.note_hashes, self.graph_hashes, self.index = {}, {}, {}, {}
        for method in METHODS:
            notes_path, graph_path = self.directory / f'{method.lower()}-notes.json', self.directory / f'{method.lower()}-code-graph.json'
            if not notes_path.exists(): continue
            if not graph_path.exists(): raise FileNotFoundError(f'{method}: notes without a code graph in {self.directory}')
            notes_raw, graph_raw = notes_path.read_bytes(), graph_path.read_bytes()
            notes, graph = json.loads(notes_raw), json.loads(graph_raw)
            if notes.get('source', {}).get('codeGraphSha256') not in (None, digest(graph_raw)): raise ValueError(f'{method}: notes were derived from another code graph')
            self.notes[method], self.note_hashes[method], self.graph_hashes[method] = notes['notes'], digest(notes_raw), digest(graph_raw)
            symbols = {s['id']: s for s in graph['symbols']}
            callees, callers, by_file = {}, {}, {}
            for source, target in graph['edges']: callees.setdefault(source, []).append(target); callers.setdefault(target, []).append(source)
            for s in graph['symbols']: by_file.setdefault(normalize(s['file']), []).append(s)
            self.index[method] = {'symbols': symbols, 'callees': callees, 'callers': callers, 'byFile': by_file}

    def describe(self) -> dict:
        return {'module': 'research.security_notes', 'moduleSha256': digest(Path(__file__).read_bytes()), 'kind': 'notes', 'notes': dict(self.note_hashes), 'graphs': dict(self.graph_hashes),
                'maxSymbols': self.max_symbols, 'maxNeighbors': self.max_neighbors, 'maxChars': self.max_chars, 'pathMaps': {m: len(p) for m, p in self.path_maps.items()}}

    def initial(self, condition): return None

    def notes_for(self, method: str) -> dict:
        if method not in self.notes: raise ValueError(f'No frozen {method} notes in {self.directory}')
        return self.notes[method]

    def read_path(self, method: str, file: str) -> str:
        return self.path_maps.get(method, {}).get(normalize(file)) or jar_path(file)

    def neighbours(self, method: str, symbol_id: str) -> list[str]:
        index, notes = self.index[method], self.notes[method]
        linked = [*index['callees'].get(symbol_id, []), *index['callers'].get(symbol_id, [])]
        return [n for n in dict.fromkeys(linked) if n in notes and n != symbol_id]

    def update(self, touched: dict, shown_ids: set[str]):
        """(text, ids) for the noted symbols overlapping the touched ranges, with their noted call neighbours; (None, []) when nothing new overlaps."""
        method = touched.get('method'); notes = self.notes_for(method); index = self.index[method]
        ranges: dict[str, list[tuple[int, int]]] = {}
        for source in ('ranges', 'searchRanges'):  # reads and edits, and the excerpts returned by searches
            for path, spans in (touched.get(source) or {}).items(): ranges.setdefault(normalize(path), []).extend(tuple(r) for r in spans)
        candidates = []
        for file, spans in ranges.items():
            for s in index['byFile'].get(file, []):
                if s['id'] not in notes or f'note:{s["id"]}' in shown_ids: continue
                if any(s['start'] <= end and start <= s['end'] for start, end in spans):
                    degree = len(index['callees'].get(s['id'], [])) + len(index['callers'].get(s['id'], []))
                    candidates.append((SYMBOL_KINDS.index(s['kind']) if s['kind'] in SYMBOL_KINDS else len(SYMBOL_KINDS), -degree, s['file'], s['start'], s['id']))
        candidates.sort()
        blocks, ids = [], []
        for _, _, _, _, symbol_id in candidates[:self.max_symbols]:
            note = notes[symbol_id]; block = render_note(symbol_id, note, self.read_path(method, note['file']))
            block_ids = [f'note:{symbol_id}']
            for neighbour in self.neighbours(method, symbol_id):
                if f'note:{neighbour}' in shown_ids or f'note:{neighbour}' in ids or f'note:{neighbour}' in block_ids: continue
                if len(block_ids) > self.max_neighbors: break
                block += ['  via a call edge:', *render_note(neighbour, notes[neighbour], self.read_path(method, notes[neighbour]['file']), indent='    ')]
                block_ids.append(f'note:{neighbour}')
            if sum(len(l) + 1 for l in [HEADER, *blocks, *block, FOOTER]) > self.max_chars and blocks: break
            blocks += block; ids += block_ids
        if not blocks: return None, []
        return '\n'.join([HEADER, *blocks, FOOTER]), ids


def I34():
    """I34: security notes derived from the v13 Highscore data-flow graphs (I31) over the I28 code graphs of the same snapshot; no document up front unless the arm adds `static`."""
    return NotesSidecar(ROOT / 'research/iterations/i34-notes/contexts', snapshot_path_maps())


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--freeze', metavar='OUT_DIR', help='write <method>-notes.json/.txt and copy the code graphs into this contexts folder')
    parser.add_argument('--dataflow-dir', help='folder with <method>-dataflow.graph.json (an acquisition round\'s contexts)')
    parser.add_argument('--code-graph-dir', help='folder with <method>-code-graph.json (an autocontext round\'s contexts)')
    parser.add_argument('--render', metavar='NOTES_JSON', help='print the notes of a frozen notes file')
    args = parser.parse_args()
    if args.freeze:
        if not args.dataflow_dir or not args.code_graph_dir: parser.error('--freeze needs --dataflow-dir and --code-graph-dir')
        print(json.dumps(freeze(Path(args.freeze), Path(args.dataflow_dir), Path(args.code_graph_dir)), indent=1))
    elif args.render: print(render_all(json.loads(Path(args.render).read_text())))
    else: parser.print_help()


if __name__ == '__main__': main()
