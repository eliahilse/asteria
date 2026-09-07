import unittest
from research.import_evidence import Importer, ROOT, canonical, digest


class EvidenceTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.data = Importer().build()
        cls.fresh = [r for r in cls.data['runs'] if r['cohort'] == 'fresh_pilot']

    def test_preserves_attempt_denominators(self):
        self.assertEqual(len(self.fresh), 16)
        self.assertEqual(sum(r['compileStatus'] == 'pass' for r in self.fresh), 4)
        self.assertEqual(sum(r['functionalSuccess'] for r in self.fresh), 1)
        self.assertEqual(sum(len(r['findings']) for r in self.fresh), 3)

    def test_missing_java_is_not_a_security_failure_or_pass(self):
        checks = [t for r in self.fresh for t in r['tests'] if t['suite'] == 'security']
        self.assertEqual(sum(t['status'] == 'infrastructure_error' for t in checks), 24)
        self.assertFalse(any(t['status'] in ('pass', 'fail') for t in checks))

    def test_no_invented_observations_for_selected_published_runs(self):
        published = [r for r in self.data['runs'] if r['cohort'] == 'published_selected']
        self.assertEqual(len(published), 6)
        self.assertTrue(all(not r['tests'] and r['prompt'] is None for r in published))

    def test_every_artifact_matches_original_bytes(self):
        for artifact in self.data['artifacts']:
            self.assertEqual(digest((ROOT / artifact['path']).read_bytes()), artifact['sha256'])

    def test_fingerprint_is_reproducible(self):
        self.assertEqual(self.data, Importer().build())
        data = dict(self.data)
        fingerprint = data.pop('fingerprint')
        self.assertEqual(fingerprint, digest(canonical(data)))

    def test_rejects_artifacts_outside_repository(self):
        with self.assertRaises(ValueError):
            Importer().artifact('../outside.txt')


if __name__ == '__main__':
    unittest.main()
