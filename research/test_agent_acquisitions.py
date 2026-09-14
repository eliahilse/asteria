import json
from pathlib import Path
import tempfile
import unittest

from research import agent_acquisitions


class AgentAcquisitionTests(unittest.TestCase):
    def test_summary_counts_and_table(self):
        with tempfile.TemporaryDirectory() as temporary:
            directory = Path(temporary) / 'agent-1'; directory.mkdir()
            record = {'protocol': 'repository-security-context-v7-agent', 'id': 'agent-1', 'method': 'Reuse', 'angle': 'catalog', 'status': 'settings_unverified',
                      'model': 'm', 'modelReported': [], 'submittedAt': '2026-09-14T20:00:00+00:00', 'finishedAt': '2026-09-14T20:06:30+00:00', 'commandsExecuted': 12,
                      'exitCode': 0, 'rawOutput': {'items': [1, 2, 3]}, 'output': {'items': [1, 2], 'assets': [1], 'boundaries': []}, 'promptInsert': 'x' * 100,
                      'citationChecks': {'validator': 'anchor-validation-v2', 'total': 5, 'matched': 4, 'symbolCorrected': 1, 'rejectedAnchors': [{}], 'droppedItems': [{}],
                                         'uncitedItems': 0, 'itemsByKind': {'control': 2}}}
            (directory / 'record.json').write_text(json.dumps(record))
            (Path(temporary) / 'context-old').mkdir(); (Path(temporary) / 'context-old' / 'record.json').write_text(json.dumps({'protocol': 'other'}))
            row = agent_acquisitions.summarize(directory)
            self.assertEqual((row['elapsedMinutes'], row['rawItems'], row['items'], row['droppedItems'], row['anchorsMatched'], row['anchorsCorrected'], row['anchorsRejected']), (6.5, 3, 2, 1, 4, 1, 1))
            self.assertIsNone(agent_acquisitions.summarize(Path(temporary) / 'context-old'))
            text = agent_acquisitions.table([row])
            self.assertIn('| Reuse | catalog | settings_unverified | 6.5 | 12 | 3→2 | 4/5 (1) | 1 | 0 | 100 | control 2 |', text)


if __name__ == '__main__':
    unittest.main()
