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

    def test_plans_and_extracted_facts_are_not_model_observations(self):
        extraction = self.data['contextExtraction']
        self.assertEqual(extraction['coverage']['parsedFiles'], 168)
        self.assertEqual(len(extraction['facts']), 145)
        plan = self.data['experimentPlans'][0]
        self.assertEqual(len(plan['schedule']), 140)
        self.assertTrue(all(c['prompt']['sha256'] == c['promptSha256'] for c in plan['conditions']))
        self.assertFalse(any(r['model'] == plan['model'] for r in self.data['runs']))

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

    def test_new_execution_preserves_historical_errors(self):
        if not self.data['securityProtocols']:
            self.skipTest('No new security evaluation imported')
        observed = [r for r in self.fresh if r['securityEvaluation']]
        self.assertEqual(len(observed), 4)
        self.assertTrue(all(len(r['securityEvaluation']['checks']) == 11 for r in observed))
        self.assertTrue(all(t['status'] == 'infrastructure_error' for r in observed for t in r['tests'] if t['suite'] == 'security'))
        constrained = next(r for r in observed if r['securityContext'])
        self.assertEqual(constrained['assessment'], 'no_targeted_findings')
        self.assertEqual(constrained['securityEvaluation']['status'], 'fail')
        self.assertEqual([t['name'] for t in constrained['securityEvaluation']['checks'] if t['status'] == 'fail'], ['oversizedPhysicalLine'])


if __name__ == '__main__':
    unittest.main()
