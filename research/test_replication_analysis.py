from copy import deepcopy
import json
import unittest

from research.import_evidence import ROOT
from research.replication_analysis import ROUNDS, verify_design


class ReplicationDesignTests(unittest.TestCase):
    def plans(self):
        return [json.loads((ROOT / 'research/iterations' / name / 'plan.json').read_text()) for name in ROUNDS]

    def test_predeclared_rounds_have_matched_designs(self):
        verify_design(*self.plans())

    def test_changed_control_or_reused_trajectory_cannot_be_called_replication(self):
        first, second = self.plans()
        changed = deepcopy(second)
        next(c for c in changed['conditions'] if c['securityStrategy'] == 'none')['promptSha256'] = 'changed'
        with self.assertRaisesRegex(ValueError, 'Fixed comparison'): verify_design(first, changed)
        changed = deepcopy(second); changed['schedule'][0]['runId'] = first['schedule'][0]['runId']
        with self.assertRaisesRegex(ValueError, 'fresh and unique'): verify_design(first, changed)


if __name__ == '__main__': unittest.main()
