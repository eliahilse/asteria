import contextlib
import io
import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from research import agentic_delivery as agentic
from research import context_graph, graph_sidecar
from research.import_evidence import canonical
from research.test_agentic_delivery import CORRECTION, Fixture, LEVEL_PATH, SCORE_PATH, act, report, valid_changes
from research.test_context_graph import DOCUMENT, MODEL, FILE, STORE, LOAD, MENU


class GraphSidecarTests(unittest.TestCase):
    def setUp(self):
        self.temporary = tempfile.TemporaryDirectory(); self.directory = Path(self.temporary.name)
        self.graph = context_graph.build(DOCUMENT, MODEL)
        (self.directory / 'generation-dataflow.graph.json').write_bytes(canonical(self.graph))

    def tearDown(self): self.temporary.cleanup()

    def test_compact_static_insert_and_description(self):
        sidecar = graph_sidecar.GraphSidecar(self.directory)
        text = sidecar.initial({'strategy': 'Generation'})
        self.assertIn('[R1; requirement', text); self.assertIn('[C1; control', text)
        self.assertNotIn('[X1; existing_risk', text); self.assertNotIn('Assets:', text); self.assertNotIn('[U1; unknown', text)
        self.assertEqual(text, context_graph.render(self.graph, kinds=graph_sidecar.COMPACT, header=False))
        described = sidecar.describe()
        self.assertEqual((described['granularity'], described['hops'], described['kinds'], described['header']), ('symbol', 0, ['requirement', 'control'], False))
        self.assertIn('Generation', described['graphs']); self.assertEqual(len(described['moduleSha256']), 64)

    def test_symbol_granularity_uses_edit_ranges_not_files(self):
        sidecar = graph_sidecar.GraphSidecar(self.directory)
        jar_path = 'ApoMario/ApoMario.jar!/apoMario/Store.java'
        # An edit inside store(int,String) (L10-L20) touches only R2; the whole-file touch would also bring X1/R1/C1 via load().
        text, ids = sidecar.update({'method': 'Generation', 'files': {jar_path}, 'ranges': {jar_path: [[12, 14]]}}, set())
        self.assertEqual(ids, ['item:R2']); self.assertIn('R2 statement', text)
        text, ids = sidecar.update({'method': 'Generation', 'files': {jar_path}, 'ranges': {jar_path: [[35, 36]]}}, {'item:R2'})
        self.assertEqual(set(ids), {'item:R1', 'item:C1'}); self.assertNotIn('item:X1', ids)  # compact kinds exclude the risk
        text, ids = sidecar.update({'method': 'Generation', 'files': {jar_path}, 'ranges': {}}, set())
        self.assertIsNone(text); self.assertEqual(ids, [])
        wide = graph_sidecar.GraphSidecar(self.directory, granularity='file', kinds=None, hops=1)
        text, ids = wide.update({'method': 'Generation', 'files': {jar_path}, 'ranges': {}}, set())
        self.assertEqual(set(ids), {'item:X1', 'item:R1', 'item:C1', 'item:R2'})

    def test_harness_reports_edit_and_read_ranges_and_sidecar_config(self):
        with tempfile.TemporaryDirectory() as temporary:
            fixture = Fixture(Path(temporary)); seen = []

            class Probe:
                def initial(self, condition): return None
                def describe(self): return {'module': 'probe', 'granularity': 'symbol'}
                def update(self, touched, shown_ids):
                    seen.append({'stage': touched['stage'], 'ranges': {k: list(v) for k, v in (touched.get('ranges') or {}).items()}, 'files': sorted(touched['files'])})
                    return None, []
            record, _ = fixture.run('agentic', 'adaptive', [act('read', files=[{'path': SCORE_PATH, 'start_line': 2, 'end_line': 3}]), act('submit_feature_changes', **valid_changes()), act('submit_feature_changes', **CORRECTION)], [report(False), report(True)], Probe())
            self.assertEqual(record['sidecarConfig'], {'module': 'probe', 'granularity': 'symbol'})
            after_read = next(s for s in seen if s['stage'] == 'after_read'); self.assertEqual(after_read['ranges'], {SCORE_PATH: [[2, 3]]})
            before_submit = next(s for s in seen if s['stage'] == 'before_submit')
            self.assertEqual(before_submit['ranges'].get(LEVEL_PATH), [[2, 2]]); self.assertIn(LEVEL_PATH, before_submit['files'])
            self.assertEqual(record['touchedRanges'].get(LEVEL_PATH), [[2, 2]]); self.assertEqual(record['touchedRanges'].get(SCORE_PATH), [[2, 3]])


if __name__ == '__main__':
    unittest.main()
