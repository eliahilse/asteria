import contextlib
import io
import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch
from research import iteration_runner as runner


class IterationTests(unittest.TestCase):
    def test_trajectory_retains_first_failure_and_never_returns_security_feedback(self):
        sources, _ = runner.original_sources()
        action = {'new_files': [{'filename': 'Scores.java', 'content': 'class Scores {}'}], 'edits': [
            {'filename': name, 'old_text': source.splitlines()[0], 'new_text': source.splitlines()[0] + '\n// fixture edit'} for name, source in sources.items()]}
        actions = iter([action, {'new_files': [], 'edits': [{'filename': 'Scores.java', 'old_text': 'class Scores {}', 'new_text': 'class Scores { int corrected; }'}]}])
        requests = []
        def response(command, request, timeout):
            requests.append(request)
            return {'model': runner.MODEL, 'request_id': request['request_id'], 'settings': None, 'finish_reason': 'stop', 'output_text': json.dumps(next(actions))}
        reports = [{'status': 'evaluated', 'mainCompilation': 'pass', 'functionalSuccess': success,
                    'checks': [{'suite': 'invoked', 'name': 'functional-fixture', 'status': 'pass' if success else 'fail'}],
                    'security': {'checks': [{'detail': 'SECURITY_FEEDBACK_MUST_STAY_PRIVATE'}]}} for success in (False, True)]
        with tempfile.TemporaryDirectory() as temporary, contextlib.redirect_stdout(io.StringIO()):
            root = Path(temporary); (root / 'prompt.txt').write_text('Fixture task')
            plan = {'id': 'test-iteration', 'fingerprint': 'fixture', 'model': runner.MODEL, 'settings': runner.SETTINGS, 'system': runner.SYSTEM, 'maxSubmissions': 3,
                    'schedule': [{'runId': 'fixture-run', 'condition': 'fixture', 'repetition': 1}],
                    'conditions': [{'id': 'fixture', 'promptFile': 'prompt.txt', 'promptSha256': runner.digest(b'Fixture task')}]}
            with patch.object(runner, 'validate', return_value=plan), patch.object(runner, 'invoke', side_effect=response), patch.object(runner, 'evaluate_response', side_effect=reports):
                runner.trajectory(root / 'manifest.json', 'fixture-run', ['fixture-adapter'])
            record = json.loads((root / 'runs/fixture-run/record.json').read_text())
            self.assertEqual(record['status'], 'completed')
            self.assertEqual([s['functionalSuccess'] for s in record['submissions']], [False, True])
            self.assertEqual(len(requests), 2)
            self.assertNotIn('SECURITY_FEEDBACK_MUST_STAY_PRIVATE', json.dumps(requests))
            self.assertIn('functional-fixture', requests[1]['messages'][-1]['content'])
            self.assertFalse(record['settingsVerified'])

if __name__ == '__main__': unittest.main()
