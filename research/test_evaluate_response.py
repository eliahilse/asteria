import unittest
from research.evaluate_response import junit_checks
from research.import_evidence import TEST_NAMES


class FunctionalParserTests(unittest.TestCase):
    def test_pass_requires_complete_count_and_zero_exit(self):
        self.assertTrue(all(c['status'] == 'pass' for c in junit_checks('unit', 'OK (7 tests)', 0)))
        for output, code in [('OK (1 test)', 0), ('OK (7 tests)', 1), ('OK (7 tests)', None), ('JUnit version 4.13.2', -6), ('Tests run: 7, Failures: 0', 1)]:
            self.assertTrue(all(c['status'] == 'unknown' for c in junit_checks('unit', output, code)))

    def test_failed_check_retained_without_inventing_other_passes(self):
        name = TEST_NAMES['unit'][0]
        partial = f'1) {name}(example.Test)\njava.lang.AssertionError: expected empty\n'
        checks = junit_checks('unit', partial, 1)
        self.assertEqual(checks[0]['status'], 'fail')
        self.assertTrue(all(c['status'] == 'unknown' for c in checks[1:]))
        complete = junit_checks('unit', partial + 'Tests run: 7,  Failures: 1\n', 1)
        self.assertTrue(all(c['status'] == 'pass' for c in complete[1:]))

    def test_initialization_failure_does_not_backfill_test_passes(self):
        output = '1) initializationError(example.Test)\njava.lang.Error: missing runtime\nTests run: 7,  Failures: 1\n'
        self.assertTrue(all(c['status'] == 'unknown' for c in junit_checks('unit', output, 1)))


if __name__ == '__main__': unittest.main()
