import unittest
from research.category_analysis import category_status


class CategoryAnalysisTests(unittest.TestCase):
    def check(self, name, status): return {'suite': 'security_v1', 'name': name, 'status': status}

    def test_observed_failure_establishes_affected_even_with_other_missing_checks(self):
        self.assertEqual(category_status([self.check('a', 'fail')], ['a', 'b']), 'affected')

    def test_only_complete_passes_establish_unaffected(self):
        self.assertEqual(category_status([self.check('a', 'pass')], ['a', 'b']), 'unresolved')
        self.assertEqual(category_status([self.check('a', 'pass'), self.check('b', 'unknown')], ['a', 'b']), 'unresolved')
        self.assertEqual(category_status([self.check('a', 'pass'), self.check('b', 'pass')], ['a', 'b']), 'unaffected')


if __name__ == '__main__': unittest.main()
