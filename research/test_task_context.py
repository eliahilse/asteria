import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from research import generate_security_context as base
from research import task_context
from research.test_generate_security_context import item


class TaskContextTests(unittest.TestCase):
    def test_actual_requests_have_no_perspective_guidance_and_preserve_evidence(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary)
            repo = root / 'repo'
            repo.mkdir()
            (repo / 'Score.java').write_text('class Score {\n  String name;\n}\n')
            original = (dict(base.STRATEGIES), base.PROTOCOL, base.TOOL)
            record, snap, directory = task_context.prepare(repo, 'Add Highscore to ApoMario.', root / 'records', 2)
            requests = []

            def invoke(command, request, timeout):
                requests.append(request)
                prompt = request['messages'][0]['content']
                self.assertIn('TASK\nAdd Highscore to ApoMario.', prompt)
                self.assertIn(task_context.INSTRUCTION, prompt)
                for guidance in base.STRATEGIES.values():
                    self.assertNotIn(guidance[1], prompt)
                for hint in ('STRATEGY\n', 'resource', 'persistence', 'cryptography', 'trust boundar',
                             'guard', 'allocation', 'negative', 'OPERATION REPRESENTATION'):
                    self.assertNotIn(hint.lower(), prompt.lower())
                self.assertEqual(request['tools'], [base.TOOL])
                if len(requests) == 1:
                    action = {'action': 'search', 'query': 'String name', 'paths': None}
                else:
                    observed = item('security_property', 'observed', ['E0001'])
                    observed['statement'] = 'The class declares a string name.'
                    action = {'action': 'finish', 'summary': 'Name field', 'items': [observed], 'limitations': []}
                return {'request_id': request['request_id'], 'model': request['model'], 'settings': None,
                        'finish_reason': 'stop', 'output_text': json.dumps(action)}

            with patch.object(base, 'invoke', side_effect=invoke):
                result = task_context.execute(record, snap, directory, ['fixture'])
            self.assertEqual(result['protocol'], task_context.PROTOCOL)
            self.assertEqual(result['strategy'], 'task_only')
            self.assertEqual(result['status'], 'settings_unverified')
            self.assertIn('Score.java:2-2', result['promptInsert'])
            self.assertEqual(len(result['turns']), 2)
            self.assertEqual(json.loads((directory / 'record.json').read_text())['output'], result['output'])
            self.assertEqual((base.STRATEGIES, base.PROTOCOL, base.TOOL), original)
            with self.assertRaises(FileExistsError):
                task_context.execute(record, snap, directory, ['fixture'])

    def test_changed_prompt_is_rejected_before_any_model_call(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary)
            repo = root / 'repo'
            repo.mkdir()
            (repo / 'Score.java').write_text('class Score {}\n')
            record, snap, directory = task_context.prepare(repo, 'Add Highscore.', root / 'records')
            record['initialPrompt'] += '\nExtra security instructions'
            with patch.object(base, 'invoke') as invoke, self.assertRaisesRegex(ValueError, 'prompt changed'):
                task_context.execute(record, snap, directory, ['fixture'])
            invoke.assert_not_called()


if __name__ == '__main__':
    unittest.main()
