import unittest

from research.feature_delivery import apply_changes


class LenientDeliveryTests(unittest.TestCase):
    def test_strict_rejects_and_lenient_replaces_an_existing_file(self):
        files = {'ApoMarioLevel.java': 'class ApoMarioLevel {}\n', 'ApoMarioMenu.java': 'class ApoMarioMenu {}\n'}
        action = {'new_files': [{'filename': 'ApoMarioLevel.java', 'content': 'class ApoMarioLevel { int score; }\n'}], 'edits': []}
        with self.assertRaises(ValueError): apply_changes(files, action)
        result = apply_changes(files, action, lenient=True)
        self.assertEqual(result['ApoMarioLevel.java'], 'class ApoMarioLevel { int score; }\n'); self.assertEqual(result['ApoMarioMenu.java'], files['ApoMarioMenu.java'])
        self.assertEqual(files['ApoMarioLevel.java'], 'class ApoMarioLevel {}\n')  # input untouched

    def test_lenient_keeps_edits_exact(self):
        files = {'ApoMarioLevel.java': 'class ApoMarioLevel {}\n'}
        action = {'new_files': [], 'edits': [{'filename': 'ApoMarioLevel.java', 'old_text': 'missing', 'new_text': 'x'}]}
        with self.assertRaises(ValueError): apply_changes(files, action, lenient=True)


if __name__ == '__main__':
    unittest.main()
