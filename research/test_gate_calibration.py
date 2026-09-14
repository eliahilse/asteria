import json
from pathlib import Path
import tempfile
import unittest

from research import gate_calibration

GRAPH = {'nodes': [{'id': 'item:R1', 'kind': 'requirement', 'cwe': ['CWE-20']}, {'id': 'item:C1', 'kind': 'control', 'cwe': ['CWE-400', 'CWE-770']}, {'id': 'symbol:x', 'kind': 'symbol'}], 'edges': []}
MAPPING = {'checks': {'rejectsNegativeScore': {'cwes': ['CWE-20', 'CWE-1284']}, 'oversizedPhysicalLine': {'cwes': ['CWE-400', 'CWE-770', 'CWE-789']}, 'boundsRetainedEntries': {'cwes': ['CWE-770']},
                      'nativeDeserializationCanary': {'cwes': ['CWE-502']}, 'validRecordRoundTrip': {'cwes': []}}}


def report(failed):
    checks = [{'name': n, 'status': 'fail' if n in failed else 'pass'} for n in gate_calibration.ISSUE_CHECKS]
    return {'mainCompilation': 'pass', 'functionalSuccess': False, 'security': {'checks': checks}}


class GateCalibrationTests(unittest.TestCase):
    def test_join_and_counts(self):
        with tempfile.TemporaryDirectory() as temporary:
            run = Path(temporary) / 'fixture__generation_s__agentic__gate__r1'; (run / 'submission-1/evaluation').mkdir(parents=True); (run / 'submission-2/evaluation').mkdir(parents=True)
            (run / 'submission-1/evaluation/report.json').write_text(json.dumps(report({'rejectsNegativeScore', 'oversizedPhysicalLine'})))
            (run / 'submission-2/evaluation/report.json').write_text(json.dumps(report(set())))
            record = {'runId': run.name, 'condition': 'generation_s__agentic__gate',
                      'submissions': [{'number': 1, 'status': 'evaluated', 'evaluationFile': 'submission-1/evaluation/report.json'}, {'number': 2, 'status': 'evaluated', 'evaluationFile': 'submission-2/evaluation/report.json'}, {'number': 3, 'status': 'invalid_changes'}],
                      'sidecarEvents': [{'stage': 'gate', 'submission': 1, 'consulted': True, 'wouldIntervene': True, 'verdictIntervene': True, 'citedIds': ['item:R1'], 'quoted': ['x'], 'unquoted': 0, 'reason': 'a'},
                                        {'stage': 'gate', 'submission': 2, 'consulted': True, 'wouldIntervene': False, 'verdictIntervene': True, 'citedIds': ['item:C1'], 'quoted': [], 'unquoted': 1, 'reason': 'b'},
                                        {'stage': 'gate', 'submission': 3, 'consulted': True, 'wouldIntervene': True, 'verdictIntervene': True, 'citedIds': ['item:C1'], 'quoted': ['y'], 'unquoted': 0, 'reason': 'c'},
                                        {'stage': 'after_read', 'injected': False}]}
            (run / 'record.json').write_text(json.dumps(record))
            rows = gate_calibration.join(run, GRAPH, MAPPING)
            self.assertEqual(len(rows), 3)
            self.assertEqual(rows[0]['predictedChecks'], ['rejectsNegativeScore']); self.assertEqual(rows[0]['predictedFailed'], ['rejectsNegativeScore']); self.assertEqual(rows[0]['failedChecks'], ['oversizedPhysicalLine', 'rejectsNegativeScore'])
            self.assertEqual(rows[1]['predictedChecks'], ['boundsRetainedEntries', 'oversizedPhysicalLine']); self.assertEqual(rows[1]['predictedFailed'], []); self.assertFalse(rows[1]['wouldIntervene'])
            self.assertEqual(rows[2]['evaluatedChecks'], 0)
            summary = gate_calibration.summarize(rows)
            self.assertEqual((summary['judgedSubmissions'], summary['evaluatedJudgedSubmissions'], summary['notEvaluated']), (3, 2, 1))
            self.assertEqual((summary['wouldIntervene'], summary['wouldIntervene_withAnyFailedCheck'], summary['wouldIntervene_withPredictedFailedCheck']), (1, 1, 1))
            self.assertEqual((summary['silent'], summary['silent_withAnyFailedCheck'], summary['verdictInterveneWithoutQuote']), (1, 0, 1))
            text = gate_calibration.render(rows, summary, 'fixture')
            self.assertIn('| 1 | yes | R1 | 1 | oversizedPhysicalLine, rejectsNegativeScore | rejectsNegativeScore | rejectsNegativeScore |', text)
            self.assertIn('Would intervene: 1 of 2 evaluated', text)


if __name__ == '__main__':
    unittest.main()
