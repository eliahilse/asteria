import json
from pathlib import Path
import tempfile
import unittest

from research import context_graph, i10_sidecar
from research.import_evidence import canonical
from research.test_context_graph import DOCUMENT, MODEL, FILE, STORE


class I10SidecarTests(unittest.TestCase):
    def test_normalize_paths(self):
        self.assertEqual(i10_sidecar.normalize('ApoMario/ApoMario.jar!/apoMario/level/ApoMarioLevel.java'), 'ApoMario/apoMario/level/ApoMarioLevel.java')
        self.assertEqual(i10_sidecar.normalize('ApoMario/src/apoMario/level/ApoMarioLevel.java'), 'ApoMario/apoMario/level/ApoMarioLevel.java')
        self.assertEqual(i10_sidecar.normalize('ApoIcarus/apoJump/X.java'), 'ApoIcarus/apoJump/X.java')

    def test_static_matches_frozen_render_and_adaptive_slices_by_method(self):
        with tempfile.TemporaryDirectory() as temporary:
            directory = Path(temporary); graph = context_graph.build(DOCUMENT, MODEL)
            (directory / 'generation-dataflow.graph.json').write_bytes(canonical(graph))
            sidecar = i10_sidecar.MethodSidecar(directory)
            self.assertEqual(sidecar.initial({'strategy': 'Generation'}), context_graph.render(graph))
            with self.assertRaises(ValueError): sidecar.initial({'strategy': 'Reuse'})
            snapshot_path = 'ApoMario/ApoMario.jar!/apoMario/Store.java'
            text, ids = sidecar.update({'files': {snapshot_path}, 'symbols': set(), 'queries': [], 'stage': 'after_read', 'method': 'Generation'}, set())
            self.assertEqual(set(ids), {'item:X1', 'item:R1', 'item:C1', 'item:R2'}); self.assertIn('SECURITY CONTEXT FOR THE CODE YOU ARE TOUCHING', text)
            text, ids = sidecar.update({'files': {snapshot_path}, 'method': 'Generation'}, set(ids))
            self.assertIsNone(text); self.assertEqual(ids, [])
            text, ids = sidecar.update({'files': {'ApoMario/ApoMario.jar!/apoMario/Other.java'}, 'method': 'Generation'}, set())
            self.assertIsNone(text)


if __name__ == '__main__':
    unittest.main()
