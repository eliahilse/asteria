import contextlib
import io
import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch
from research import feature_delivery as delivery
from research.feature_delivery import apply_changes, original_sources


class DeliveryTests(unittest.TestCase):
    def test_edits_preserve_unrelated_members_and_new_files(self):
        files = {'Game.java': 'class Game {\n int score;\n void end() {}\n}\n'}
        changed = apply_changes(files, {'new_files': [{'filename': 'Scores.java', 'content': 'class Scores {}'}], 'edits': [{'filename': 'Game.java', 'old_text': 'void end() {}', 'new_text': 'void end() { save(); }'}]})
        self.assertEqual(changed['Game.java'], 'class Game {\n int score;\n void end() { save(); }\n}\n')
        self.assertEqual(files['Game.java'], 'class Game {\n int score;\n void end() {}\n}\n')
        self.assertIn('Scores.java', changed)

    def test_failed_batch_is_transactional_and_ambiguous_anchors_are_rejected(self):
        files = {'Game.java': 'int a; int a;'}
        action = {'new_files': [{'filename': 'Scores.java', 'content': 'class Scores {}'}], 'edits': [{'filename': 'Game.java', 'old_text': 'int a;', 'new_text': 'int b;'}]}
        with self.assertRaisesRegex(ValueError, 'matches 2'): apply_changes(files, action)
        self.assertEqual(files, {'Game.java': 'int a; int a;'})
        action['new_files'][0]['filename'] = '../Outside.java'
        with self.assertRaisesRegex(ValueError, 'basename'): apply_changes(files, action)
        with self.assertRaisesRegex(ValueError, 'exists'): apply_changes(files, {'new_files': [{'filename': 'Game.java', 'content': 'partial'}], 'edits': []})

    def test_original_source_membership_and_line_endings_are_explicit(self):
        files, origins = original_sources()
        self.assertEqual(set(files), {'ApoMarioLevel.java', 'ApoMarioMenu.java', 'ApoMarioPanel.java'})
        self.assertTrue(all('\r\n' not in value for value in files.values()))
        self.assertTrue(all(len(value['sha256']) == 64 and len(value['normalizedSha256']) == 64 for value in origins.values()))

    def test_feedback_is_bounded_functional_only_and_invalid_changes_do_not_accumulate(self):
        files = {name: 'class ' + name.removesuffix('.java') + ' { int preserved; }' for name in delivery.TARGETS}
        valid = {'new_files': [{'filename': 'Scores.java', 'content': 'class Scores { int value; }'}], 'edits': [{'filename': 'ApoMarioLevel.java', 'old_text': 'int preserved;', 'new_text': 'int preserved; int added;'}]}
        invalid = {**valid, 'edits': [{'filename': 'ApoMarioLevel.java', 'old_text': 'missing', 'new_text': 'changed'}]}
        correction = {'new_files': [], 'edits': [{'filename': 'Scores.java', 'old_text': 'int value;', 'new_text': 'int corrected;'}]}
        actions = iter([invalid, valid, correction])
        requests = []
        def respond(command, request, timeout):
            requests.append(request)
            return {'model': delivery.MODEL, 'request_id': request['request_id'], 'settings': None, 'finish_reason': 'stop', 'output_text': json.dumps(next(actions))}
        reports = [{'status': 'evaluated', 'mainCompilation': 'pass', 'functionalSuccess': success,
                    'checks': [{'suite': 'invoked', 'name': 'functional-fixture', 'status': 'pass' if success else 'fail'}],
                    'security': {'checks': [{'detail': 'PRIVATE_SECURITY_FEEDBACK'}]}} for success in (False, True)]
        with tempfile.TemporaryDirectory() as temporary, contextlib.redirect_stdout(io.StringIO()), \
                patch.object(delivery, 'original_sources', return_value=(files, {})), \
                patch.object(delivery, 'invoke', side_effect=respond), \
                patch.object(delivery, 'evaluate_response', side_effect=reports) as evaluate:
            record = delivery.run(Path(temporary), ['fixture-adapter'])
            directory = Path(temporary) / record['id']
            self.assertEqual(record['status'], 'completed')
            self.assertEqual([s['status'] for s in record['submissions']], ['invalid_changes', 'evaluated', 'evaluated'])
            self.assertEqual(len(requests), 3)
            self.assertEqual(evaluate.call_count, 2)
            self.assertNotIn('PRIVATE_SECURITY_FEEDBACK', json.dumps(requests))
            self.assertIn('functional-fixture', requests[-1]['messages'][-1]['content'])
            self.assertFalse(record['settingsVerified'])
            self.assertEqual((directory / 'submission-3/source/Scores.java').read_text(), 'class Scores { int corrected; }')
            self.assertIn('int preserved;', (directory / 'submission-3/source/ApoMarioLevel.java').read_text())
            self.assertEqual(delivery.digest((directory / 'protocol.py').read_bytes()), record['protocolSha256'])

    def test_model_substitution_stops_without_evaluation_or_retry(self):
        def substitute(command, request, timeout):
            return {'model': 'different-model', 'request_id': request['request_id'], 'settings': None, 'output_text': '{}', 'finish_reason': 'stop'}
        with tempfile.TemporaryDirectory() as temporary, contextlib.redirect_stdout(io.StringIO()), \
                patch.object(delivery, 'invoke', side_effect=substitute) as invoke, \
                patch.object(delivery, 'evaluate_response') as evaluate:
            record = delivery.run(Path(temporary), ['fixture-adapter'])
            self.assertEqual(record['status'], 'identity_mismatch')
            self.assertIsNone(record['settingsVerified'])
            self.assertEqual(invoke.call_count, 1)
            evaluate.assert_not_called()

    def test_evaluation_error_is_not_misreported_as_transactional_delivery_rejection(self):
        action = {'new_files': [], 'edits': [{'filename': 'ApoMarioLevel.java', 'old_text': 'public class ApoMarioLevel {', 'new_text': 'public class ApoMarioLevel { /* fixture */'}]}
        def respond(command, request, timeout):
            return {'model': delivery.MODEL, 'request_id': request['request_id'], 'settings': delivery.SETTINGS, 'output_text': json.dumps(action), 'finish_reason': 'stop'}
        with tempfile.TemporaryDirectory() as temporary, contextlib.redirect_stdout(io.StringIO()), \
                patch.object(delivery, 'invoke', side_effect=respond) as invoke, \
                patch.object(delivery, 'evaluate_response', side_effect=ValueError('fixture infrastructure error')):
            with self.assertRaisesRegex(ValueError, 'infrastructure'):
                delivery.run(Path(temporary), ['fixture-adapter'])
            record = json.loads(next(Path(temporary).glob('*/record.json')).read_text())
            self.assertEqual(record['status'], 'evaluation_error')
            self.assertEqual(record['submissions'][0]['status'], 'evaluation_error')
            self.assertEqual(invoke.call_count, 1)


if __name__ == '__main__': unittest.main()
