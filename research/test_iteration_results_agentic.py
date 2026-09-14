"""The results reader accepts agentic-delivery iterations and labels arms by mode and sidecar."""
import contextlib
import io
import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from research import agentic_delivery as agentic
from research.import_evidence import canonical, digest
from research.iteration_results import read_study
from research.test_agentic_delivery import Fixture, ORIGINS, SOURCES, act, valid_changes


def evaluation(complete: str, output, meta):
    report = {'status': 'evaluated', 'mainCompilation': 'pass', 'functionalSuccess': True, 'processes': [], 'inputHashes': {},
            'responseSha256': digest(complete.encode()), 'checks': [{'suite': 'unit', 'name': 'emptyBoardInitially', 'status': 'pass'}],
            'security': {'checks': [{'suite': 'security_v1', 'name': 'rejectsNegativeScore', 'status': 'fail'}], 'controls': {'controls': [{'validated': True}]}}}
    Path(output).mkdir(parents=True, exist_ok=True); (Path(output) / 'report.json').write_bytes(canonical(report))
    return report


class AgenticReaderTests(unittest.TestCase):
    def test_reader_verifies_agentic_records_and_labels_arms(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary); fixture = Fixture(root)
            plan = fixture.plan; plan['analysis'] = {'primary': 'fixture'}
            for condition in plan['conditions']: condition['baseContext'] = 'S'; condition['repetitions'] = 1
            plan.pop('fingerprint'); plan['fingerprint'] = digest(canonical(plan)); (root / 'manifest.json').write_bytes(canonical(plan))
            runs = {}
            for mode, sidecar, actions in (('single_shot', 'none', [valid_changes()]),
                                           ('agentic', 'none', [act('search', query='name'), act('read', files=[{'path': 'Score.java', 'start_line': 1, 'end_line': 4}]), act('submit_feature_changes', **valid_changes())])):
                run_id = f'fixture__generation_s__{mode}__{sidecar}__r1'; actions = iter(actions)

                def invoke(command, request, timeout):
                    return {'protocol_version': 1, 'request_id': request['request_id'], 'model': request['model'], 'settings': None, 'finish_reason': 'stop',
                            'output_text': json.dumps(next(actions)), 'usage': {}, 'cost_usd': None}
                with patch.object(agentic, 'validate', return_value=plan), patch.object(agentic, 'repository_snapshot', return_value=fixture.snap), \
                     patch.object(agentic, 'original_sources', return_value=(dict(SOURCES), ORIGINS)), patch('research.agentic_delivery.invoke', side_effect=invoke), \
                     patch.object(agentic, 'evaluate_response', side_effect=evaluation), contextlib.redirect_stdout(io.StringIO()):
                    runs[mode] = agentic.trajectory(root / 'manifest.json', run_id, None, ['fixture-adapter'])
            self.assertTrue(all(r['functionalSuccess'] for r in runs.values()))
            study = read_study(root)
            self.assertEqual(study['plan']['axes']['securityContext'], ['none', 'single_shot+static', 'single_shot+adaptive', 'agentic+none', 'agentic+static', 'agentic+adaptive'])
            labels = {c['id']: c['securityStrategy'] for c in study['plan']['conditions']}
            self.assertEqual(labels['generation_s__single_shot__none'], 'none'); self.assertEqual(labels['generation_s__agentic__adaptive'], 'agentic+adaptive')
            observed = {c['id']: c for c in study['summary']['conditions'] if c['attempts']}
            self.assertEqual(set(observed), {'generation_s__single_shot__none', 'generation_s__agentic__none'})
            for condition in observed.values():
                self.assertEqual((condition['fullFunctional'], condition['firstFullFunctional'], condition['securityChecksExecuted']), (1, 1, 1))
            self.assertEqual(study['plan']['acquisitions'], [])
            # Tampering with a frozen prompt is still detected for agentic runs.
            record_path = root / 'runs/fixture__generation_s__agentic__none__r1/record.json'; record = json.loads(record_path.read_text())
            record['submissions'][0]['request']['messages'][1]['content'] = 'changed'
            record['submissions'][0]['requestSha256'] = digest(canonical(record['submissions'][0]['request']))
            record_path.write_text(json.dumps(record))
            with self.assertRaisesRegex(ValueError, 'frozen task'): read_study(root)

    def test_agentic_none_is_the_reference_when_no_single_shot_arm_exists(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary); fixture = Fixture(root); plan = fixture.plan
            plan['conditions'] = [c for c in plan['conditions'] if c['mode'] == 'agentic']; plan['schedule'] = [r for r in plan['schedule'] if '__agentic__' in r['condition']]
            plan['analysis'] = {'primary': 'fixture'}
            for condition in plan['conditions']: condition['baseContext'] = 'S'; condition['repetitions'] = 1
            plan.pop('fingerprint'); plan['fingerprint'] = digest(canonical(plan)); (root / 'manifest.json').write_bytes(canonical(plan))
            study = read_study(root)
            labels = {c['id']: c['securityStrategy'] for c in study['plan']['conditions']}
            self.assertEqual(labels['generation_s__agentic__none'], 'none'); self.assertEqual(labels['generation_s__agentic__static'], 'agentic+static')
            self.assertEqual(study['plan']['axes']['securityContext'][0], 'none')


if __name__ == '__main__':
    unittest.main()
