import unittest
from research.submission_analysis import summarize


class SubmissionAnalysisTests(unittest.TestCase):
    def fixture(self):
        plan = {'maxSubmissions': 5, 'conditions': [{'id': 'arm', 'repetitions': 3}], 'schedule': [{}, {}, {}]}
        records = [
            {'runId': 'first', 'condition': 'arm', 'status': 'completed', 'submissions': [{'number': 1, 'status': 'evaluated', 'functionalSuccess': True}]},
            {'runId': 'late', 'condition': 'arm', 'status': 'completed', 'submissions': [{'number': 1, 'status': 'invalid_changes'}, {'number': 2, 'status': 'evaluated', 'compilation': 'fail'}, {'number': 3, 'status': 'evaluated', 'functionalSuccess': False}, {'number': 4, 'status': 'evaluated', 'functionalSuccess': True}]},
            {'runId': 'error', 'condition': 'arm', 'status': 'adapter_error', 'submissions': [{'number': 1, 'status': 'adapter_error'}]}]
        return plan, records

    def test_early_stopping_and_errors_remain_in_every_denominator(self):
        plan, records = self.fixture(); result = summarize(plan, records)
        self.assertEqual([r['full'] for r in result['curves']], [1, 1, 1, 2, 2])
        self.assertEqual([r['n'] for r in result['curves']], [3] * 5)
        self.assertEqual([r['cumulativeCalls'] for r in result['curves']], [3, 4, 5, 6, 6])
        self.assertEqual(result['curves'][-1]['cumulativeInvalidEdits'], 1)
        self.assertEqual(result['curves'][-1]['cumulativeFailedCompilations'], 1)
        self.assertIsNone(result['trajectories'][-1]['firstFullSubmission'])

    def test_incomplete_collection_cannot_produce_final_budget_rates(self):
        plan, records = self.fixture(); records[0]['status'] = 'started'
        with self.assertRaisesRegex(ValueError, 'complete fixed schedule'): summarize(plan, records)


if __name__ == '__main__': unittest.main()
