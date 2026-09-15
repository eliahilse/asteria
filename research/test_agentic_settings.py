import unittest

from research.agentic_delivery import EFFORTS, resolve_settings
from research.feature_delivery import SETTINGS


class SettingsTests(unittest.TestCase):
    def test_default_keeps_the_shared_settings(self):
        self.assertEqual(resolve_settings(), SETTINGS); self.assertIsNot(resolve_settings(), SETTINGS)

    def test_override_changes_only_the_reasoning_effort(self):
        for effort in EFFORTS:
            resolved = resolve_settings(effort)
            self.assertEqual(resolved['reasoning_effort'], effort)
            self.assertEqual({k: v for k, v in resolved.items() if k != 'reasoning_effort'}, {k: v for k, v in SETTINGS.items() if k != 'reasoning_effort'})
        with self.assertRaises(ValueError): resolve_settings('maximum')


if __name__ == '__main__':
    unittest.main()
