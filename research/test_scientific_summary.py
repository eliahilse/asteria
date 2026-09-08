import unittest
from research.scientific_summary import ISSUES, summarize, wilson


def study_fixture():
    conditions = [{'id': arm, 'strategy': 'Generation', 'baseContext': 'SFB', 'parentCondition': 'generation_sfb', 'securityStrategy': arm} for arm in ('none', 'requirements')]
    rows, runs = [], []
    for arm in ('none', 'requirements'):
        checks = [{'suite': 'security_v1', 'name': name, 'pass': 0, 'fail': 2 if arm == 'none' else 0,
                   'executed': 2 if arm == 'none' else 0} for name in ISSUES]
        rows.append({'id': arm, 'attempts': 2, 'compiled': 2 if arm == 'none' else 0, 'fullFunctional': 2 if arm == 'none' else 0,
            'firstFullFunctional': 2 if arm == 'none' else 0, 'modelSubmissions': 2, 'unverifiedSettings': 2, 'transportErrors': 0, 'checks': checks})
        for repetition in range(2): runs.append({'condition': arm, 'functionalSuccess': arm == 'none', 'checks': []})
    return {'plan': {'id': 'fixture', 'fingerprint': 'fixture', 'conditions': conditions, 'acquisitions': []},
            'summary': {'complete': True, 'conditions': rows}, 'runs': runs}


class ScientificSummaryTests(unittest.TestCase):
    def test_small_sample_intervals_do_not_claim_certainty_at_zero_or_perfect_success(self):
        self.assertIsNone(wilson(0, 0))
        self.assertAlmostEqual(wilson(0, 2)[1], .6576197724933469)
        self.assertAlmostEqual(wilson(5, 5)[0], .5655175352168251)
        self.assertEqual(wilson(5, 5)[1], 1.)

    def test_noncompiling_treatment_does_not_look_like_a_security_improvement(self):
        report = summarize(study_fixture()); comparison = report['comparisons'][0]
        self.assertEqual(comparison['perCheckDirections']['unresolved'], 10)
        self.assertEqual(comparison['failureCountDeltaIdentificationBoundsPerTrajectory'], [-10., 0.])
        self.assertEqual(comparison['descriptiveDirectionRegardlessOfMissing'], 'unresolved_or_equal')
        self.assertEqual(report['conditions'][1]['issues']['unresolved'], 20)
        self.assertEqual(report['conditions'][1]['functionalAndAllDeclaredSecurityPass'], 0)

    def test_a_complete_observed_improvement_has_negative_bounds_without_using_checks_as_sample_size(self):
        study = study_fixture(); row = study['summary']['conditions'][1]
        row['compiled'] = row['fullFunctional'] = row['firstFullFunctional'] = 2
        for check in row['checks']: check['pass'] = check['executed'] = 2
        report = summarize(study); comparison = report['comparisons'][0]
        self.assertEqual(comparison['failureCountDeltaIdentificationBoundsPerTrajectory'], [-10., -10.])
        self.assertEqual(comparison['perCheckDirections']['decreased'], 10)
        self.assertEqual(report['conditions'][1]['withinBudgetFullWilson95'], wilson(2, 2))

    def test_partial_schedules_do_not_get_final_intervals(self):
        study = study_fixture(); study['summary']['complete'] = False
        with self.assertRaisesRegex(ValueError, 'Complete the frozen schedule'): summarize(study)


if __name__ == '__main__': unittest.main()
