from pathlib import Path
import tempfile
import unittest

from research.saved_iteration import resolve


class SavedIterationTests(unittest.TestCase):
    def test_clean_checkout_uses_committed_results_without_restoring_runtime(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary); pointer = root / 'research/iterations/current.json'
            pointer.parent.mkdir(parents=True); pointer.write_text('{"id":"i05-budget-sensitivity"}')
            self.assertEqual(resolve(root), (True, 'i05-budget-sensitivity'))
            self.assertEqual(resolve(root, iteration='i03-security-perspectives'), (True, 'i03-security-perspectives'))

    def test_live_iteration_takes_precedence_but_public_build_keeps_completed_snapshot(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary); current = root / 'research/iterations/current.json'
            current.parent.mkdir(parents=True); current.write_text('{"id":"i05-budget-sensitivity"}')
            active = root / '.local/iterations/active.json'; active.parent.mkdir(parents=True); active.write_text('{"id":"i06-operational-guards"}')
            manifest = active.parent / 'i06-operational-guards/manifest.json'; manifest.parent.mkdir(); manifest.write_text('{}')
            self.assertEqual(resolve(root), (False, 'i06-operational-guards'))
            self.assertEqual(resolve(root, public=True), (True, 'i05-budget-sensitivity'))

    def test_explicit_original_and_invalid_paths_are_not_silently_replaced(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary)
            self.assertEqual(resolve(root, iteration='original'), (False, 'original'))
            with self.assertRaisesRegex(ValueError, 'Invalid iteration'): resolve(root, iteration='../../elsewhere')


if __name__ == '__main__': unittest.main()
