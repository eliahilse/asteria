import unittest
from research.functional_diagnostics import classify


class FunctionalDiagnosticsTests(unittest.TestCase):
    def test_bootstrap_failure_is_not_counted_as_a_failed_executed_test(self):
        call = {'exitCode': 1, 'stdout': 'java.lang.NullPointerException: before setup\nTests run: 0, Failures: 1\n', 'stderr': ''}
        self.assertEqual(classify(call), ('suite_bootstrap_failure', 0))

    def test_assertion_and_process_timeout_have_distinct_diagnostics(self):
        self.assertEqual(classify({'exitCode': 1, 'stdout': 'java.lang.AssertionError: missing score\nTests run: 5, Failures: 1\n', 'stderr': ''}), ('functional_assertion_failure', 5))
        self.assertEqual(classify({'exitCode': None, 'stdout': '', 'stderr': 'Process timeout'}), ('process_timeout', None))


if __name__ == '__main__': unittest.main()
