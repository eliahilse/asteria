import os
import unittest
from unittest.mock import patch

from research import feature_delivery, tasks


class TaskRegistryTests(unittest.TestCase):
    def test_default_task_is_highscore_and_achievements_is_registered(self):
        with patch.dict(os.environ, {}, clear=False):
            os.environ.pop('ASTERIA_TASK', None)
            self.assertEqual(tasks.current().key, 'highscore')
        with patch.dict(os.environ, {'ASTERIA_TASK': 'achievements'}):
            task = tasks.current()
            self.assertEqual((task.name, task.feature_class, task.positive_check, task.large_check), ('Achievements', 'apoMario.game.panels.ApoMarioStateAchievements', 'validUnlockRoundTrip', 'largeStoreFile'))
            self.assertEqual(len(task.issue_checks), 10); self.assertEqual(sum(len(v) for v in task.test_names.values()), 28)
            self.assertTrue(set(task.required_edits) <= set(task.targets))
        with patch.dict(os.environ, {'ASTERIA_TASK': 'options'}):
            with self.assertRaises(ValueError): tasks.current()
        self.assertEqual(tasks.by_name('Highscore').key, 'highscore'); self.assertEqual(tasks.by_name('achievements').name, 'Achievements')

    def test_highscore_protocol_text_and_targets_are_unchanged(self):
        with patch.dict(os.environ, {'ASTERIA_TASK': 'highscore'}):
            self.assertEqual(feature_delivery.protocol_text(), feature_delivery.PROTOCOL); self.assertEqual(feature_delivery.targets(), feature_delivery.TARGETS)
        with patch.dict(os.environ, {'ASTERIA_TASK': 'achievements'}):
            text = feature_delivery.protocol_text()
            self.assertIn('Achievements feature', text); self.assertIn('addTimeSurvived', text); self.assertNotIn('storeRun', text)
            self.assertEqual(len(feature_delivery.targets()), 5)

    def test_every_task_has_the_probe_and_control_sources(self):
        from research.import_evidence import ROOT
        for task in tasks.TASKS.values():
            self.assertTrue((ROOT / 'research/security' / task.probe_source).exists(), task.key)
            for control in task.controls: self.assertTrue((ROOT / 'research/security' / f'{control}.java').exists(), control)
            self.assertEqual(task.checks[0], task.positive_check); self.assertIn(task.large_check, task.checks)


if __name__ == '__main__':
    unittest.main()
