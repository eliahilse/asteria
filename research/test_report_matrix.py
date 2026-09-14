import unittest
from research.report_matrix import PERCENT_MIN_N, comparisons, count_delta, rate, render, wilson


class ReportTests(unittest.TestCase):
    def test_overview_reports_unit_and_autonomous_tiers_without_an_invoked_column(self):
        check = {'id': 'invoked.recordsRealScore', 'suite': 'invoked', 'pass': 1, 'fail': 0, 'not_run': 0, 'unknown': 0, 'compile_error': 0, 'infrastructure_error': 0, 'attempts': 1, 'executed': 1}
        study = {'plan': {'id': 'synthetic', 'phase': 'other', 'model': 'm', 'reasoning': 'none', 'fingerprint': 'f', 'schedule': [1], 'deviations': [], 'conditions': [{'id': 'a'}]},
                 'summary': {'complete': True, 'conditions': [{'id': 'a', 'attempts': 1, 'pending': 0, 'unverifiedSettings': 0, 'compiled': 1, 'fullFunctional': 1, 'checks': [check]}]}}
        text = render({'studies': [study]}, 'now')
        header = next(line for line in text.splitlines() if line.startswith('| Condition |'))
        self.assertIn('Unit passes / 7n', header)
        self.assertIn('Autonomous passes / 5n', header)
        self.assertNotIn('Invoked', header)
        # The invoked check itself stays in the per-test table.
        self.assertIn('| invoked.recordsRealScore |', text)
        # Counts first: a one-attempt cell is 1/1, never 100.0%.
        self.assertIn('| a | 1/1 | 1/1 |', text)
        self.assertNotIn('1/1 (100.0%)', text)
        self.assertIn('Unit: attempt; n = attempts recorded per condition', text)

    def test_rates_are_counts_and_percentages_need_twenty_observations(self):
        self.assertEqual(PERCENT_MIN_N, 20)
        self.assertEqual(rate(4, 5), '4/5')
        self.assertEqual(rate(16, 20), '16/20 (80.0%)')
        self.assertEqual(rate(0, 0), '— (0 attempts)')
        self.assertEqual(count_delta(4, 5, 5, 5), '−1 (4−5)')
        self.assertEqual(count_delta(5, 5, 5, 5), '0 (5−5)')
        self.assertEqual(count_delta(5, 5, 4, 5), '+1 (5−4)')
        self.assertEqual(count_delta(20, 25, 15, 25), '+5 (20−15) = +20.0 pp')
        self.assertEqual(count_delta(4, 4, 5, 5), '— (N 4 vs 5)')
        self.assertEqual(count_delta(20, 40, 5, 20), '+25.0 pp')
        self.assertEqual(count_delta(0, 0, 5, 5), '—')

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
