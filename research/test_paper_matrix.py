import json
import tempfile
import unittest
from pathlib import Path
from unittest.mock import patch

from research.paper_matrix import prepare
from research.run_experiment import frozen_manifest
from research.study_execution import submit
from research.study_results import summarize
from research.evaluate_response import response_from_observation
from research.import_evidence import canonical, digest


class MatrixTests(unittest.TestCase):
    def test_complete_factorial_and_exact_attachment_membership(self):
        with tempfile.TemporaryDirectory() as d:
            root = Path(d)
            plan = prepare(root)
            self.assertEqual(len(plan['conditions']), 16)
            self.assertEqual(len(plan['schedule']), 80)
            self.assertEqual(len({r['runId'] for r in plan['schedule']}), 80)
            self.assertEqual(frozen_manifest(root / 'manifest.json')['fingerprint'], plan['fingerprint'])
            for c in plan['conditions']:
                self.assertEqual(sum(a['kind'] == 'target source' for a in c['attachments']), 3)
                self.assertEqual(sum(a['kind'] == 'reuse source' for a in c['attachments']), 2 if c['strategy'] == 'Reuse' else 0)
                self.assertEqual(sum(a['kind'] == 'context' for a in c['attachments']), len(c['contextTypes']))
                text = (root / c['promptFile']).read_text()
                self.assertNotIn('BEGIN SECURITY', text)
                for t in ('Structural', 'Functional', 'Behavioral'):
                    self.assertEqual(f'BEGIN ATTACHED CONTEXT: ApoMario_{t}.json' in text, t[0] in c['contextTypes'])
            self.assertEqual(prepare(root)['fingerprint'], plan['fingerprint'])

    def test_unattested_settings_are_retained_without_being_falsely_verified(self):
        with tempfile.TemporaryDirectory() as d:
            root = Path(d); plan = prepare(root / 'plan', repetitions=1)
            runs = root / 'runs'; runs.mkdir()
            def model(command, request, timeout):
                return {'model': request['model'], 'request_id': request['request_id'], 'settings': None,
                        'protocol_version': 1, 'finish_reason': 'stop', 'output_text': 'TEST FIXTURE ONLY'}
            with patch('research.study_execution.invoke', side_effect=model):
                record = submit(plan, root / 'plan/manifest.json', plan['schedule'][0], runs, ['test-only'])
                submit(plan, root / 'plan/manifest.json', plan['schedule'][0], runs, ['must-not-be-called'])
            path = runs / (record['runId'] + '.json')
            text, identity = response_from_observation(path, root / 'plan/manifest.json')
            self.assertEqual(record['status'], 'settings_unverified')
            self.assertFalse(identity['settingsVerified'])
            self.assertEqual(text, 'TEST FIXTURE ONLY')
            plan['allowUnverifiedSettings'] = False
            plan['fingerprint'] = digest(canonical({k: v for k, v in plan.items() if k != 'fingerprint'}))
            (root / 'plan/manifest.json').write_bytes(canonical(plan))
            record['manifestFingerprint'] = plan['fingerprint']; path.write_bytes(canonical(record))
            with self.assertRaises(ValueError): response_from_observation(path, root / 'plan/manifest.json')

    def test_selection_waits_for_all_cells_and_does_not_use_security_scores(self):
        plan = {'phase': 'screening', 'conditions': [
            {'id': 'a', 'strategy': 'Generation', 'paperPromptId': 8, 'repetitions': 1},
            {'id': 'b', 'strategy': 'Generation', 'paperPromptId': 9, 'repetitions': 1},
            {'id': 'c', 'strategy': 'Reuse', 'paperPromptId': 0, 'repetitions': 1}], 'selection': {'perMethod': 1}}
        observations = [{'runId': x, 'condition': x, 'status': 'completed'} for x in 'abc']
        reports = {x: {'status': 'evaluated', 'mainCompilation': 'pass', 'functionalSuccess': x != 'a', 'checks': [],
                       'security': {'checks': [{'suite': 'security_v1', 'name': 'validRecordRoundTrip', 'status': 'pass' if x == 'a' else 'fail'}]}} for x in 'abc'}
        partial = summarize(plan, observations[:2], reports)
        self.assertFalse(partial['complete']); self.assertEqual(partial['selected'], [])
        complete = summarize(plan, observations, reports)
        self.assertTrue(complete['complete']); self.assertEqual(complete['selected'], ['b', 'c'])
        row = complete['conditions'][0]
        self.assertTrue(all(c['not_run'] == 1 and c['passRate'] is None for c in row['checks'] if c['kind'] == 'functional'))


if __name__ == '__main__': unittest.main()
