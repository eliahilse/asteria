from copy import deepcopy
import unittest

from research.qualification import NAME, qualify
from research.study_results import STATUSES


def fixture(status='pass', precondition='not_demonstrated'):
    check = {'suite': 'security_v1', 'name': NAME, 'kind': 'security', 'status': status, 'detail': 'Original observation'}
    counts = {key: int(key == status) for key in STATUSES}
    summary = {'id': 'arm', 'fullFunctional': 1, 'checks': [{**check, **counts, 'executed': 1}]}
    study = {'plan': {'label': 'I04', 'deviations': []}, 'summary': {'conditions': [summary]},
             'runs': [{'runId': 'run', 'condition': 'arm', 'functionalSuccess': True, 'checks': [check]}]}
    audit = {'id': 'audit', 'planFingerprint': 'frozen', 'results': [{'runId': 'run', 'legacyCheck': deepcopy(check),
             'precondition': {'status': precondition, 'reason': 'Reload produced one entry instead of two'}}]}
    return study, audit


class QualificationTests(unittest.TestCase):
    def test_both_unsupported_pass_and_failure_become_unknown_without_changing_originals(self):
        for status in ('pass', 'fail'):
            with self.subTest(status=status):
                study, audit = fixture(status); original = deepcopy(study)
                result, metadata = qualify(study, audit)
                self.assertEqual(study, original)
                self.assertEqual(result['runs'][0]['checks'][0]['status'], 'unknown')
                self.assertEqual(result['runs'][0]['checks'][0]['originalStatus'], status)
                self.assertTrue(result['runs'][0]['functionalSuccess'])
                summary = result['summary']['conditions'][0]
                self.assertEqual(summary['checks'][0]['unknown'], 1)
                self.assertEqual(summary['securityChecksExecuted'], 0)
                self.assertIsNone(summary['checks'][0]['passRate'])
                self.assertEqual(metadata['changes'][0]['from'], status)

    def test_demonstrated_format_keeps_observed_pass_and_failure(self):
        for status in ('pass', 'fail'):
            study, audit = fixture(status, 'matched')
            result, metadata = qualify(study, audit)
            self.assertEqual(result['runs'], study['runs'])
            self.assertEqual(metadata['changes'], [])

    def test_missing_or_mismatched_audit_cannot_silently_qualify(self):
        study, audit = fixture(); audit['results'] = []
        with self.assertRaisesRegex(ValueError, 'does not match'): qualify(study, audit)
        study, audit = fixture(); audit['results'][0]['legacyCheck']['status'] = 'fail'
        with self.assertRaisesRegex(ValueError, 'does not match'): qualify(study, audit)


if __name__ == '__main__': unittest.main()
