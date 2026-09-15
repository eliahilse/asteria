from pathlib import Path
import tempfile
import unittest

from research import context_agent as ca

try:
    import tree_sitter_java  # noqa: F401
    HAVE_TREE_SITTER = True
except ImportError:
    HAVE_TREE_SITTER = False


class AngleTests(unittest.TestCase):
    def test_generic_prompt_has_no_repository_and_high_level_keeps_it(self):
        generic = ca.build_prompt('Implement a Highscore feature in ApoMario.', 'Generation', 'generic')
        self.assertIn('WORKSPACE\nNone. No repository is available', generic); self.assertNotIn('outline.md', generic); self.assertNotIn('Repositories: ApoMario', generic)
        self.assertIn('GROUND RULES', generic); self.assertIn('ANGLE: GENERIC CONTEXT', generic); self.assertIn('"angle": "generic"', generic)
        high = ca.build_prompt('Implement a Highscore feature in ApoMario.', 'Reuse', 'highlevel')
        self.assertIn('Repositories: ApoMario, ApoIcarus', high); self.assertIn('ANGLE: HIGH-LEVEL GUIDANCE', high); self.assertIn('leave every `anchors` list empty', high)
        for angle in ('highlevel', 'generic'):
            self.assertIn(f'research/security/agent/{angle}.md', ca.generator_hashes(angle))

    @unittest.skipUnless(HAVE_TREE_SITTER, 'tree-sitter not installed')
    def test_workspace_without_games_has_vocabulary_and_empty_code_model(self):
        with tempfile.TemporaryDirectory() as temporary:
            info = ca.build_workspace('Generation', Path(temporary), games=[])
            workspace = Path(temporary) / 'workspace'
            self.assertEqual((info['games'], info['files'], info['sourceFiles'], info['symbols']), ([], {}, 0, 0))
            self.assertTrue((workspace / 'schema.json').exists()); self.assertTrue((workspace / 'ONTOLOGY.md').exists()); self.assertTrue((workspace / 'code-model.json').exists())
            self.assertFalse((workspace / 'ApoMario').exists())


if __name__ == '__main__':
    unittest.main()
