import json
from pathlib import Path
import tempfile
import unittest

from research import agentic_delivery as agentic
from research import autocontext_sidecar as auto
from research import security_notes as notes_module
from research.import_evidence import digest

MODEL = {'fingerprint': 'f' * 64, 'generator': {'tool': 'test'}, 'root': '/x',
         'files': [{'path': 'Game/src/g/Level.java', 'lines': 60, 'sha256': 'a' * 64, 'encoding': 'utf-8'}, {'path': 'Game/src/g/Panel.java', 'lines': 40, 'sha256': 'b' * 64, 'encoding': 'utf-8'}],
         'symbols': [{'id': 'g.Level', 'kind': 'class', 'file': 'Game/src/g/Level.java', 'start': 1, 'end': 60, 'owner': None, 'params': [], 'returns': None, 'calls': []},
                     {'id': 'g.Level#score', 'kind': 'field', 'file': 'Game/src/g/Level.java', 'start': 3, 'end': 3, 'owner': 'g.Level', 'params': [], 'returns': 'int', 'calls': []},
                     {'id': 'g.Level#finish()', 'kind': 'method', 'file': 'Game/src/g/Level.java', 'start': 10, 'end': 20, 'owner': 'g.Level', 'params': [], 'returns': 'void', 'calls': ['save']},
                     {'id': 'g.Level#save(int)', 'kind': 'method', 'file': 'Game/src/g/Level.java', 'start': 22, 'end': 30, 'owner': 'g.Level', 'params': [{'type': 'int', 'name': 'p'}], 'returns': 'void', 'calls': []},
                     {'id': 'g.Panel#Panel()', 'kind': 'constructor', 'file': 'Game/src/g/Panel.java', 'start': 5, 'end': 9, 'owner': 'g.Panel', 'params': [], 'returns': None, 'calls': ['finish']}],
         'edges': [{'from': 'g.Level#finish()', 'to': 'g.Level#save(int)'}, {'from': 'g.Panel#Panel()', 'to': 'g.Level#finish()'}]}

DATAFLOW = {'nodes': [
    {'id': 'item:R1', 'kind': 'requirement', 'label': 'Reject a negative score at save.', 'failure_behavior': 'Throw IllegalArgumentException; nothing is written.'},
    {'id': 'item:C1', 'kind': 'control', 'label': 'The store holds at most ten records.', 'failure_behavior': ''},
    {'id': 'item:C2', 'kind': 'control', 'label': 'finish passes the actual score.', 'failure_behavior': ''},
    {'id': 'item:X1', 'kind': 'observation', 'label': 'The panel constructs the level once.', 'failure_behavior': ''},
    {'id': 'item:V1', 'kind': 'verification', 'label': 'A test drives save with -1.', 'failure_behavior': ''},
    {'id': 'symbol:g.Level#save(int)', 'kind': 'symbol', 'label': 'g.Level#save(int)', 'file': 'Game/src/g/Level.java'},
    {'id': 'symbol:g.Level', 'kind': 'symbol', 'label': 'g.Level', 'file': 'Game/src/g/Level.java'},
    {'id': 'symbol:g.Level#finish()', 'kind': 'symbol', 'label': 'g.Level#finish()', 'file': 'Game/src/g/Level.java'},
    {'id': 'symbol:g.Panel#Panel()', 'kind': 'symbol', 'label': 'g.Panel#Panel()', 'file': 'Game/src/g/Panel.java'},
    {'id': 'symbol:g.Gone#lost()', 'kind': 'symbol', 'label': 'g.Gone#lost()', 'file': 'Game/src/g/Gone.java'}],
    'edges': [
    {'from': 'item:R1', 'to': 'symbol:g.Level#save(int)', 'type': 'anchored_at'},
    {'from': 'item:R1', 'to': 'symbol:g.Level#save(int)', 'type': 'enforced_at'},
    {'from': 'item:C1', 'to': 'symbol:g.Level', 'type': 'enforced_at'},
    {'from': 'item:C2', 'to': 'symbol:g.Level#finish()', 'type': 'anchored_at'},
    {'from': 'item:X1', 'to': 'symbol:g.Panel#Panel()', 'type': 'anchored_at'},
    {'from': 'item:V1', 'to': 'symbol:g.Level#save(int)', 'type': 'anchored_at'},
    {'from': 'item:R1', 'to': 'symbol:g.Gone#lost()', 'type': 'anchored_at'},
    {'from': 'item:R1', 'to': 'item:C1', 'type': 'mitigates'}]}


def touched(ranges, stage='after_read'):
    return {'method': 'Generation', 'ranges': ranges, 'searchRanges': {}, 'files': set(ranges), 'symbols': set(), 'queries': [], 'stage': stage}


class DeriveTests(unittest.TestCase):
    def test_notes_follow_anchors_and_enforcement_points_of_note_kinds_only(self):
        notes = notes_module.derive_notes(DATAFLOW, auto.compact_graph(MODEL))
        self.assertEqual(sorted(notes['notes']), ['g.Level', 'g.Level#finish()', 'g.Level#save(int)', 'g.Panel#Panel()'])
        save = notes['notes']['g.Level#save(int)']
        self.assertEqual([i['id'] for i in save['items']], ['R1'])  # the verification item V1 is not a note
        self.assertEqual(save['items'][0]['relation'], 'enforced_at')  # enforced_at wins over anchored_at for the same statement
        self.assertEqual((save['kind'], save['start'], save['end']), ('method', 22, 30))
        self.assertEqual(notes['notes']['g.Level']['items'][0]['id'], 'C1')
        self.assertEqual((notes['statements'], notes['symbols'], notes['skippedSymbols']), (4, 4, ['g.Gone#lost()']))

    def test_render_shows_ids_relation_text_and_failure_behavior(self):
        notes = notes_module.derive_notes(DATAFLOW, auto.compact_graph(MODEL))
        lines = notes_module.render_note('g.Level#save(int)', notes['notes']['g.Level#save(int)'], 'Game/Game.jar!/g/Level.java')
        self.assertEqual(lines[0], 'g.Level#save(int) [method] Game/Game.jar!/g/Level.java:22-30')
        self.assertEqual(lines[1], '  [R1; requirement; enforced here] Reject a negative score at save.')
        self.assertEqual(lines[2], '    on failure: Throw IllegalArgumentException; nothing is written.')


class SidecarTests(unittest.TestCase):
    def setUp(self):
        self.temporary = tempfile.TemporaryDirectory(); root = Path(self.temporary.name)
        self.dataflow_dir, self.code_dir, self.out = root / 'dataflow', root / 'code', root / 'contexts'
        self.dataflow_dir.mkdir(); self.code_dir.mkdir()
        (self.dataflow_dir / 'generation-dataflow.graph.json').write_text(json.dumps(DATAFLOW))
        (self.code_dir / 'generation-code-graph.json').write_text(json.dumps(auto.compact_graph(MODEL)))
        self.written = notes_module.freeze(self.out, self.dataflow_dir, self.code_dir, methods=('Generation',))
        self.sidecar = notes_module.NotesSidecar(self.out, {'Generation': {'Game/g/Level.java': 'Game/Game.jar!/g/Level.java'}})

    def tearDown(self): self.temporary.cleanup()

    def test_freeze_writes_notes_with_source_hashes_and_the_code_graph_beside_them(self):
        self.assertEqual(self.written, {'Generation': {'symbols': 4, 'statements': 4, 'skippedSymbols': 1}})
        notes = json.loads((self.out / 'generation-notes.json').read_text())
        self.assertEqual(notes['source']['codeGraphSha256'], digest((self.code_dir / 'generation-code-graph.json').read_bytes()))
        self.assertEqual(notes['source']['dataflowSha256'], digest((self.dataflow_dir / 'generation-dataflow.graph.json').read_bytes()))
        self.assertTrue((self.out / 'generation-code-graph.json').exists()); self.assertIn('[R1; requirement; enforced here]', (self.out / 'generation-notes.txt').read_text())
        described = self.sidecar.describe()
        self.assertEqual((described['kind'], sorted(described['notes']), sorted(described['graphs'])), ('notes', ['Generation'], ['Generation']))
        self.assertIsNone(self.sidecar.initial({'strategy': 'Generation'}))

    def test_reading_a_method_surfaces_its_note_and_the_class_note_with_readable_paths(self):
        text, ids = self.sidecar.update(touched({'Game/src/g/Level.java': [[24, 26]]}), set())
        self.assertEqual(ids, ['note:g.Level', 'note:g.Level#save(int)', 'note:g.Level#finish()'])  # the class first, then the method read, then its noted caller via the call edge
        self.assertTrue(text.startswith(notes_module.HEADER) and text.endswith(notes_module.FOOTER))
        self.assertIn('g.Level#save(int) [method] Game/Game.jar!/g/Level.java:22-30', text)
        self.assertIn('  via a call edge:\n    g.Level#finish() [method] Game/Game.jar!/g/Level.java:10-20\n      [C2; control; concerns this code] finish passes the actual score.', text)
        self.assertNotIn('X1', text)

    def test_nothing_is_shown_twice_and_search_excerpts_count_as_viewport(self):
        first_text, first = self.sidecar.update(touched({'Game/src/g/Level.java': [[24, 26]]}), set())
        text, ids = self.sidecar.update(touched({'Game/src/g/Level.java': [[24, 26]]}), set(first))
        self.assertEqual((text, ids), (None, []))
        view = touched({}); view['searchRanges'] = {'Game/src/g/Panel.java': [[6, 6]]}
        text, ids = self.sidecar.update(view, set(first))
        self.assertEqual(ids, ['note:g.Panel#Panel()'])  # its callee finish() was shown already
        self.assertIn('[X1; observation; concerns this code] The panel constructs the level once.', text)

    def test_bounds_limit_symbols_neighbours_and_characters(self):
        one = notes_module.NotesSidecar(self.out, max_symbols=1, max_neighbors=0)
        text, ids = one.update(touched({'Game/src/g/Level.java': [[1, 60]]}), set())
        self.assertEqual(ids, ['note:g.Level'])
        tight = notes_module.NotesSidecar(self.out, max_chars=len(notes_module.HEADER) + len(notes_module.FOOTER) + 200)
        text, ids = tight.update(touched({'Game/src/g/Level.java': [[1, 60]]}), set())
        self.assertEqual(ids, ['note:g.Level']); self.assertLessEqual(len(text), tight.max_chars + 400)

    def test_edited_ranges_before_a_submission_surface_the_notes_of_the_edited_code(self):
        text, ids = self.sidecar.update(touched({'Game/src/g/Level.java': [[12, 12]]}, stage='before_submit'), {'note:g.Level'})
        self.assertEqual(ids, ['note:g.Level#finish()', 'note:g.Level#save(int)', 'note:g.Panel#Panel()'])  # the edited method, then its noted callee and caller

    def test_notes_from_another_code_graph_are_refused(self):
        (self.out / 'generation-code-graph.json').write_text(json.dumps({**auto.compact_graph(MODEL), 'edges': []}))
        with self.assertRaisesRegex(ValueError, 'another code graph'): notes_module.NotesSidecar(self.out)


class KindTests(unittest.TestCase):
    def test_notes_is_an_injection_kind_that_combines_with_static_but_not_with_ast(self):
        self.assertIn('notes', agentic.INJECT_KINDS); self.assertIn('notes', agentic.SIDECARS)
        self.assertEqual(agentic.kind_parts('static-notes'), ('static', 'notes'))
        self.assertEqual(agentic.kind_parts('notes-guard'), ('notes', 'guard'))
        with self.assertRaisesRegex(ValueError, 'At most one injection kind'): agentic.kind_parts('ast-notes')
        self.assertNotIn({'mode': 'agentic', 'sidecar': 'notes'}, agentic.DEFAULT_ARMS)


if __name__ == '__main__': unittest.main()
