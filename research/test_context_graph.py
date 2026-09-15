import unittest

from research import context_graph

FILE = 'ApoMario/src/apoMario/Store.java'
STORE = 'apoMario.Store#store(int,String)'
LOAD = 'apoMario.Store#load()'
MENU = 'apoMario.Menu#show()'


def anchor(symbol, start, end, file=FILE):
    return {'symbol': symbol, 'file': file, 'start_line': start, 'end_line': end, 'resolvedFile': file, 'resolvedStart': start, 'resolvedEnd': end, 'symbols': [symbol], 'textSha256': 'x'}


def item(id, kind, basis='task', anchors=(), related=(), **kw):
    return {'id': id, 'kind': kind, 'basis': basis, 'statement': f'{id} statement', 'task_relevance': 'r', 'threat': None, 'cwe': [], 'capec': [], 'asvs': [], 'cert': [],
            'enforcement_point': None, 'failure_behavior': None, 'anchors': list(anchors), 'verification': None, 'related': list(related), **kw}


DOCUMENT = {'angle': 'dataflow', 'summary': 'S', 'limitations': ['L'],
            'assets': [{'name': 'records', 'property': 'integrity', 'anchors': [anchor(STORE, 10, 20)]}],
            'boundaries': [{'name': 'store file', 'untrusted_input': 'bytes', 'source': 'disk', 'sink': 'load', 'entry_point': True, 'anchors': [anchor(LOAD, 30, 50)]}],
            'items': [item('X1', 'existing_risk', 'observed', [anchor(LOAD, 31, 40)], threat='denial_of_service', cwe=['CWE-400']),
                      item('R1', 'requirement', 'task', [], ['X1'], enforcement_point=anchor(LOAD, 30, 50)),
                      item('C1', 'control', 'reasoned', [], ['R1'], enforcement_point=anchor(LOAD, 30, 50), failure_behavior='stop reading'),
                      item('R2', 'requirement', 'task', [], [], enforcement_point=anchor(STORE, 10, 20)),
                      item('U1', 'unknown', 'reasoned')]}
MODEL = {'symbols': [{'id': STORE, 'file': FILE, 'start': 10, 'end': 20, 'sinks': []}, {'id': LOAD, 'file': FILE, 'start': 30, 'end': 50, 'sinks': ['file_read']},
                     {'id': MENU, 'file': 'ApoMario/src/apoMario/Menu.java', 'start': 5, 'end': 9, 'sinks': []}],
         'edges': [{'from': MENU, 'to': LOAD}]}


class ContextGraphTests(unittest.TestCase):
    def test_build_types_nodes_and_edges(self):
        graph = context_graph.build(DOCUMENT, MODEL)
        kinds = graph['metrics']['nodesByKind']
        self.assertEqual(kinds['asset'], 1); self.assertEqual(kinds['boundary'], 1); self.assertEqual(kinds['symbol'], 3); self.assertEqual(kinds['file'], 1)
        types = graph['metrics']['edgesByType']
        self.assertEqual(types['located_at'], 1); self.assertEqual(types['crosses'], 1); self.assertEqual(types['anchored_at'], 1)
        self.assertEqual(types['enforced_at'], 3); self.assertEqual(types['mitigates'], 1); self.assertEqual(types['satisfies'], 1); self.assertEqual(types['calls'], 1)
        self.assertEqual(graph['metrics']['controls'], 1); self.assertEqual(graph['metrics']['controlsWithEnforcementPoint'], 1)
        self.assertEqual(graph['metrics']['sinkSymbols'], 1); self.assertEqual(graph['metrics']['sinkSymbolsAnchored'], 1)
        self.assertEqual(graph['metrics']['anchoredItems'], 4); self.assertEqual(graph['metrics']['entryPoints'], 1)
        self.assertEqual(graph['sha256'], context_graph.build(DOCUMENT, MODEL)['sha256'])
        self.assertEqual([n['kind'] for n in graph['nodes']][:3], ['asset', 'boundary', 'existing_risk'])

    def test_render_matches_insert_conventions(self):
        text = context_graph.render(context_graph.build(DOCUMENT, MODEL))
        self.assertIn('--- BEGIN REPOSITORY-DERIVED SECURITY CONTEXT (angle: dataflow) ---', text)
        self.assertIn(f'- records [integrity] @ {FILE}:10-20 ({STORE})', text)
        self.assertIn('[X1; existing_risk; observed; denial_of_service; CWE-400] X1 statement', text); self.assertIn('- store file [entry point]: bytes from disk to load', text)
        self.assertIn(f'Enforcement point: {FILE}:30-50 ({LOAD})', text); self.assertIn('Failure behavior: stop reading', text)
        self.assertIn('Relations: satisfies R1', text); self.assertIn('Uncited unknown; absence was not established.', text)
        self.assertTrue(text.endswith('--- END REPOSITORY-DERIVED SECURITY CONTEXT ---\n'))
        ablated = context_graph.render(context_graph.build(DOCUMENT, MODEL), failure_behavior=False)
        self.assertNotIn('Failure behavior:', ablated); self.assertIn('C1 statement', ablated); self.assertIn(f'Enforcement point: {FILE}:30-50 ({LOAD})', ablated)
        self.assertEqual([l for l in text.split('\n') if not l.startswith('Failure behavior:')], ablated.split('\n'))  # the only difference is the dropped lines

    def test_slice_follows_touched_code_and_call_edges(self):
        graph = context_graph.build(DOCUMENT, MODEL)
        text, ids = context_graph.slice_for(graph, set(), {STORE}, set())
        self.assertEqual(ids, ['item:R2']); self.assertIn('R2 statement', text); self.assertNotIn('X1 statement', text)
        text, ids = context_graph.slice_for(graph, set(), {MENU}, set())
        self.assertEqual(set(ids), {'item:X1', 'item:R1', 'item:C1'})
        text, ids = context_graph.slice_for(graph, {FILE}, set(), {'item:X1', 'item:R1', 'item:C1', 'item:R2'})
        self.assertIsNone(text); self.assertEqual(ids, [])
        sidecar = context_graph.Sidecar(graph, 'adaptive')
        self.assertIsNone(sidecar.initial()); first, shown = sidecar.update({'files': {FILE}, 'symbols': set()}, set())
        self.assertEqual(set(shown), {'item:X1', 'item:R1', 'item:C1', 'item:R2'}); self.assertIn('SECURITY CONTEXT FOR THE CODE YOU ARE TOUCHING', first)
        self.assertEqual(context_graph.Sidecar(graph, 'static').update({'files': {FILE}}, set()), (None, []))
        self.assertTrue(context_graph.Sidecar(graph, 'static').initial().startswith('--- BEGIN'))

    def test_dot_export(self):
        dot = context_graph.to_dot(context_graph.build(DOCUMENT, MODEL))
        self.assertTrue(dot.startswith('digraph context {')); self.assertIn('"item:C1" -> "item:R1" [label="satisfies"]', dot)


if __name__ == '__main__':
    unittest.main()
