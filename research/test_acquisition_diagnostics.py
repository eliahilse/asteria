import unittest
from research.acquisition_diagnostics import diagnose


class DiagnosticTests(unittest.TestCase):
    def test_revision_counts_preserve_candidates_and_distinguish_id_churn(self):
        first = {'items': [{'id': 'a'}, {'id': 'b'}]}
        record = {'id': 'fixture', 'strategy': 'task', 'sourceFiles': 4, 'sourceLines': 40,
                  'turns': [{'toolResult': {'error': 'range too long'}}, {'candidateOutput': first, 'toolResult': {'citationErrors': ['fixture']}},
                            {'candidateOutput': {'items': [{'id': 'a-fixed'}]}}], 'output': {'items': [{'id': 'a-fixed'}]}}
        result = diagnose(record)
        self.assertEqual((result['toolErrorTurns'], result['citationFeedbackTurns']), (1, 1))
        self.assertEqual((result['firstCandidateItems'], result['finalItems']), (2, 1))
        self.assertEqual(result['firstCandidateIdsAbsentFromFinal'], ['a', 'b'])
        self.assertEqual(result['newFinalIds'], ['a-fixed'])
        self.assertEqual(len(first['items']), 2)


if __name__ == '__main__': unittest.main()
