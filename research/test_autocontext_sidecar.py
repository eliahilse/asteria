import json
from pathlib import Path
import tempfile
import unittest

from research import agentic_delivery as agentic
from research import autocontext_sidecar as auto

MODEL = {'fingerprint': 'f' * 64, 'generator': {'tool': 'test'}, 'root': '/x',
         'files': [{'path': 'Game/src/g/Level.java', 'lines': 60, 'sha256': 'a' * 64, 'encoding': 'utf-8'}, {'path': 'Game/src/g/Panel.java', 'lines': 40, 'sha256': 'b' * 64, 'encoding': 'utf-8'}],
         'symbols': [{'id': 'g.Level', 'kind': 'class', 'file': 'Game/src/g/Level.java', 'start': 1, 'end': 60, 'owner': None, 'params': [], 'returns': None, 'calls': []},
                     {'id': 'g.Level#score', 'kind': 'field', 'file': 'Game/src/g/Level.java', 'start': 3, 'end': 3, 'owner': 'g.Level', 'params': [], 'returns': 'int', 'calls': []},
                     {'id': 'g.Level#finish()', 'kind': 'method', 'file': 'Game/src/g/Level.java', 'start': 10, 'end': 20, 'owner': 'g.Level', 'params': [], 'returns': 'void', 'calls': ['save', 'System.nanoTime']},
                     {'id': 'g.Level#save(int)', 'kind': 'method', 'file': 'Game/src/g/Level.java', 'start': 22, 'end': 30, 'owner': 'g.Level', 'params': [{'type': 'int', 'name': 'p'}], 'returns': 'void', 'calls': []},
                     {'id': 'g.Panel#Panel()', 'kind': 'constructor', 'file': 'Game/src/g/Panel.java', 'start': 5, 'end': 9, 'owner': 'g.Panel', 'params': [], 'returns': None, 'calls': ['finish']}],
         'edges': [{'from': 'g.Level#finish()', 'to': 'g.Level#save(int)'}, {'from': 'g.Panel#Panel()', 'to': 'g.Level#finish()'}, {'from': 'g.Level#finish()', 'to': 'g.Level#save(int)'}]}


def touched(ranges):
    return {'method': 'Generation', 'ranges': ranges, 'files': set(ranges), 'symbols': set(), 'queries': [], 'stage': 'after_read'}


class AutoContextTests(unittest.TestCase):
    def setUp(self):
        self.temporary = tempfile.TemporaryDirectory(); root = Path(self.temporary.name)
        graph = auto.compact_graph(MODEL); (root / 'generation-code-graph.json').write_text(json.dumps(graph))
        self.graph, self.root = graph, root
        self.sidecar = auto.AutoContextSidecar(root, {'Generation': {'Game/g/Level.java': 'Game/Game.jar!/g/Level.java'}})

    def tearDown(self): self.temporary.cleanup()

    def test_compact_graph_keeps_classes_constructors_methods_and_deduplicated_edges(self):
        self.assertEqual([s['id'] for s in self.graph['symbols']], ['g.Level', 'g.Level#finish()', 'g.Level#save(int)', 'g.Panel#Panel()'])
        self.assertEqual(self.graph['edges'], [['g.Level#finish()', 'g.Level#save(int)'], ['g.Panel#Panel()', 'g.Level#finish()']])
        self.assertEqual(self.graph['symbols'][2]['params'], ['int']); self.assertEqual(self.graph['source']['fingerprint'], 'f' * 64)

    def test_update_renders_callers_and_callees_of_the_symbols_read_with_readable_paths(self):
        text, ids = self.sidecar.update(touched({'Game/Game.jar!/g/Level.java': [[12, 15]]}), set())
        self.assertEqual(ids, ['ast:g.Level#finish()'])
        self.assertIn(auto.HEADER, text); self.assertIn(auto.FOOTER, text)
        self.assertIn('g.Level#finish() [method] Game/Game.jar!/g/Level.java:10-20', text)
        self.assertIn('calls: g.Level#save(int) (Game/Game.jar!/g/Level.java:22-30); 1 calls outside the snapshot', text)
        self.assertIn('called by: g.Panel#Panel() (Game/Game.jar!/g/Panel.java:5-9)', text)  # no path map entry: the jar fallback

    def test_update_never_repeats_shown_symbols_and_skips_fields_and_classes(self):
        text, ids = self.sidecar.update(touched({'Game/Game.jar!/g/Level.java': [[1, 60]]}), set())
        self.assertEqual(sorted(ids), ['ast:g.Level#finish()', 'ast:g.Level#save(int)']); self.assertEqual(ids[0], 'ast:g.Level#finish()')  # highest degree first
        again = self.sidecar.update(touched({'Game/Game.jar!/g/Level.java': [[1, 60]]}), set(ids))
        self.assertEqual(again, (None, []))
        self.assertEqual(self.sidecar.update(touched({'Game/Game.jar!/g/Other.java': [[1, 5]]}), set()), (None, []))

    def test_bounds_limit_symbols_and_characters(self):
        small = auto.AutoContextSidecar(self.root, max_symbols=1); text, ids = small.update(touched({'Game/Game.jar!/g/Level.java': [[1, 60]]}), set())
        self.assertEqual(ids, ['ast:g.Level#finish()'])
        tight = auto.AutoContextSidecar(self.root, max_chars=10); text, ids = tight.update(touched({'Game/Game.jar!/g/Level.java': [[1, 60]]}), set())
        self.assertEqual(ids, ['ast:g.Level#finish()'])  # at least one block is always kept

    def test_describe_and_composite_delegation(self):
        described = self.sidecar.describe()
        self.assertEqual((described['kind'], described['maxSymbols'], list(described['graphs'])), ('ast', 6, ['Generation']))
        self.assertEqual(len(described['graphs']['Generation']), 64)

        class Guard:
            judge_turns, max_interventions = 2, 1
            def describe(self): return {'kind': 'guard'}
            def judge(self, view): return {'consulted': True, 'intervene': False, 'view': view}
        composite = auto.Composite(self.sidecar, Guard())
        self.assertEqual((composite.judge_turns, composite.max_interventions, composite.describe()['judgeTurns'], composite.describe()['guard']), (2, 1, 2, {'kind': 'guard'}))
        self.assertIsNone(composite.initial({})); self.assertEqual(composite.judge({'x': 1})['view'], {'x': 1})
        self.assertEqual(composite.update(touched({'Game/Game.jar!/g/Level.java': [[12, 15]]}), set())[1], ['ast:g.Level#finish()'])

    def test_kind_parts_accepts_ordered_combinations_only(self):
        self.assertEqual(agentic.kind_parts('static-ast-guard'), ('static', 'ast', 'guard'))
        self.assertEqual(agentic.kind_parts('ast'), ('ast',)); self.assertEqual(agentic.kind_parts('static-guard_shadow'), ('static', 'guard_shadow'))
        for bad in ('guard-static', 'none-static', 'static-static', 'adaptive-ast', 'guard-gate', 'static-', '', None, 'rag'):
            with self.assertRaises(ValueError): agentic.kind_parts(bad)
        with self.assertRaisesRegex(ValueError, 'agentic mode'): agentic.normalize_arms(['single_shot:ast'])
        self.assertEqual(agentic.normalize_arms(['agentic:static-ast-guard']), [{'mode': 'agentic', 'sidecar': 'static-ast-guard'}])


if __name__ == '__main__':
    unittest.main()
