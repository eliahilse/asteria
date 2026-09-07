import unittest
from research.report_matrix import wilson, comparisons


class ReportTests(unittest.TestCase):
    def test_small_sample_zero_success_is_not_a_zero_probability_claim(self):
        self.assertIsNone(wilson(0, 0))
        lower, upper = wilson(0, 5)
        self.assertAlmostEqual(lower, 0)
        self.assertAlmostEqual(upper, 0.4344824648)
        self.assertAlmostEqual(wilson(5, 5)[0], 1 - upper)
        with self.assertRaises(ValueError): wilson(6, 5)

    def test_comparisons_preserve_execution_vs_attempt_denominators(self):
        control = {'id': 'check', 'kind': 'security', 'passRate': 1, 'allAttemptRate': 0.5}
        treatment = {**control, 'passRate': 0.5}
        study = {'plan': {'conditions': [
            {'id': 'a', 'parentCondition': 'reuse_s', 'securityStrategy': 'none'},
            {'id': 'b', 'parentCondition': 'reuse_s', 'securityStrategy': 'flows'},
        ]}, 'summary': {'conditions': [{'id': 'a', 'checks': [control]}, {'id': 'b', 'checks': [treatment]}]}}
        row = next(comparisons(study))
        self.assertEqual(row['testedDeltaPp'], -50)
        self.assertEqual(row['allAttemptDeltaPp'], 0)
        control['passRate'] = None
        self.assertIsNone(next(comparisons(study))['testedDeltaPp'])


if __name__ == '__main__': unittest.main()
