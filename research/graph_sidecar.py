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
from research.import_evidence import ROOT, canonical, digest
from research.model_adapter import command_from_env, invoke

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


JUDGE_SYSTEM = '''You review one code submission against the security controls that apply at the operations it edits.
Intervene only when the submission clearly violates a listed control at its enforcement point, for example it stores a record without the required validation or reads a file without the required bound.
Do not intervene for style, incompleteness, missing tests, or controls whose enforcement point the submission does not touch. When unsure, do not intervene.
Return exactly one judge_submission call.'''
JUDGE_TOOL = {'type': 'function', 'function': {'name': 'judge_submission', 'strict': True,
    'description': 'Decide whether the submission clearly violates one of the listed controls at its enforcement point.',
    'parameters': {'type': 'object', 'additionalProperties': False, 'required': ['intervene', 'statement_ids', 'reason'],
                   'properties': {'intervene': {'type': 'boolean'}, 'statement_ids': {'type': 'array', 'items': {'type': 'string'}},
                                  'reason': {'type': 'string'}}}}}
EDIT_CHARS, FILE_CHARS = 4000, 6000


def judge_prompt(statements: str, changes: dict) -> str:
    parts = ['SECURITY CONTROLS AT THE EDITED OPERATIONS', statements, '', 'SUBMISSION']
    for edit in (changes.get('edits') or []):
        parts += [f"--- edit {edit.get('filename')} ---", 'OLD:', (edit.get('old_text') or '')[:EDIT_CHARS], 'NEW:', (edit.get('new_text') or '')[:EDIT_CHARS]]
    for new in (changes.get('new_files') or []):
        parts += [f"--- new file {new.get('filename')} ---", (new.get('content') or '')[:FILE_CHARS]]
    parts += ['', 'Decide: intervene only on a clear violation of a listed control at its enforcement point; list the ids of the violated statements.']
    return '\n'.join(parts)


class GateSidecar(GraphSidecar):
    """Judges each submission that touches an enforcement point; rejects only on a judged violation, otherwise silent."""

    def __init__(self, directory: Path, angle: str = 'dataflow', kinds: tuple[str, ...] | None = COMPACT, command=None, timeout: int = 600):
        super().__init__(directory, angle, granularity='symbol', hops=0, kinds=kinds, header=False)
        self.command, self.timeout = command, timeout

    def describe(self) -> dict:
        return {**super().describe(), 'mode': 'gate', 'judgeSystemSha256': digest(JUDGE_SYSTEM.encode()), 'judgeTool': JUDGE_TOOL['function']['name'], 'editChars': EDIT_CHARS, 'fileChars': FILE_CHARS}

    def initial(self, condition): return None

    def update(self, touched: dict, shown_ids: set[str]): return None, []

    def judge(self, view: dict) -> dict:
        graph = self.graph_for(view['method'])
        ranges = {normalize(path): [tuple(r) for r in spans] for path, spans in (view.get('ranges') or {}).items()}
        symbols = context_graph.symbols_in_ranges(graph, ranges, normalize)
        statements, ids = context_graph.slice_for(graph, set(), symbols, set(), hops=0, kinds=self.kinds)
        if not ids: return {'consulted': False, 'intervene': False, 'ids': [], 'reason': 'no enforcement point touched', 'text': None, 'transcript': None}
        request = {'protocol_version': 1, 'request_id': view['requestId'], 'model': view['model'], 'settings': view['settings'],
                   'messages': [{'role': 'system', 'content': JUDGE_SYSTEM}, {'role': 'user', 'content': judge_prompt(statements, view.get('changes') or {})}],
                   'tools': [JUDGE_TOOL], 'tool_choice': {'type': 'function', 'function': {'name': 'judge_submission'}}, 'parallel_tool_calls': False}
        response = invoke(self.command or command_from_env(), request, self.timeout)
        transcript = {'request': request, 'requestSha256': digest(canonical(request)), 'response': response, 'responseSha256': digest(canonical(response))}
        if response.get('model') != view['model'] or response.get('request_id') != view['requestId']:
            return {'consulted': True, 'intervene': False, 'ids': ids, 'reason': 'identity_mismatch', 'text': None, 'transcript': transcript}
        try: verdict = json.loads(response['output_text'])
        except ValueError: return {'consulted': True, 'intervene': False, 'ids': ids, 'reason': 'invalid_verdict', 'text': None, 'transcript': transcript}
        short = {i.split(':', 1)[1]: i for i in ids}
        cited = [short[s] for s in (verdict.get('statement_ids') or []) if isinstance(s, str) and s in short]
        intervene = bool(verdict.get('intervene')) and bool(cited)
        text = None
        if intervene:
            text = '\n'.join(['--- SUBMISSION REJECTED BY THE SECURITY SIDECAR ---', 'Reason: ' + str(verdict.get('reason') or ''), *context_graph.render_items(graph, cited), '--- END ---'])
        return {'consulted': True, 'intervene': intervene, 'ids': cited if intervene else ids, 'reason': str(verdict.get('reason') or ''), 'text': text, 'transcript': transcript}


def GATE():
    """Default for I12: gate sidecar over the I11 Generation data-flow graph, requirement and control statements."""
    return GateSidecar(ROOT / 'research/iterations/i12-gate-sidecar/contexts')
