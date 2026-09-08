from copy import deepcopy
import unittest
from research.repeatability import checks_by_name, measures


class RepeatabilityTests(unittest.TestCase):
    def test_raw_observation_is_retained_when_precondition_requires_unknown(self):
        report = {'mainCompilation': 'pass', 'functionalSuccess': True, 'checks': [],
            'security': {'checks': [{'suite': 'security_v1', 'name': 'largePersistedRecordSet', 'status': 'pass'}]}}
        original = deepcopy(report)
        observed = measures(report, {'status': 'not_demonstrated'})
        self.assertEqual(report, original)
        self.assertEqual(observed['rawChecks']['security_v1.largePersistedRecordSet'], 'pass')
        self.assertEqual(observed['qualifiedChecks']['security_v1.largePersistedRecordSet'], 'unknown')
        self.assertTrue(observed['functionalSuccess'])

    def test_matched_precondition_retains_an_observed_failure(self):
        report = {'checks': [], 'security': {'checks': [{'suite': 'security_v1', 'name': 'largePersistedRecordSet', 'status': 'fail'}]}}
        observed = measures(report, {'status': 'matched'})
        self.assertEqual(observed['qualifiedChecks']['security_v1.largePersistedRecordSet'], 'fail')

    def test_missing_checks_remain_unexecuted(self):
        self.assertEqual(len(checks_by_name([])), 27)
        self.assertEqual(set(checks_by_name([]).values()), {'not_run'})


if __name__ == '__main__': unittest.main()
