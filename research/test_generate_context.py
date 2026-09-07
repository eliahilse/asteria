import json
import tempfile
import unittest
import zipfile
from pathlib import Path
from unittest.mock import patch

from research.context_repository import check_evidence, operate, snapshot
from research.generate_context import execute, prepare


class GenerationTests(unittest.TestCase):
    def repo(self, root):
        repo = root / 'game'
        repo.mkdir()
        (repo / 'Score.java').write_text('class Score {\n  String input;\n}\n')
        (repo / '.env').write_text('SECRET=must-never-enter-prompt')
        (repo / '.local').mkdir()
        (repo / '.local/audit.json').write_text('PREVIOUS CONTEXT MUST NOT ENTER')
        (root / 'audit.json').write_text('OUTSIDE REPOSITORY')
        (repo / 'outside.java').symlink_to(root / 'audit.json')
        return repo

    def test_fresh_input_boundary_and_immutable_reads(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            repo = self.repo(root)
            record, snap, _ = prepare(repo, 'Add highscore', 'task', root / 'runs')
            self.assertNotIn('must-never-enter-prompt', json.dumps(snap))
            self.assertNotIn('PREVIOUS CONTEXT', json.dumps(snap))
            self.assertNotIn('OUTSIDE REPOSITORY', json.dumps(snap))
            self.assertNotIn('String input', record['initialPrompt'])
            (repo / 'Score.java').write_text('changed later')
            result = operate(snap, {'action': 'read', 'files': [{'path': 'Score.java', 'start_line': 1, 'end_line': 3}]})
            self.assertIn('String input', result['excerpts'][0]['text'])
            with self.assertRaises(ValueError):
                operate(snap, {'action': 'read', 'files': [{'path': '../audit.json', 'start_line': 1, 'end_line': 1}]})
            evidence = {'path': 'Score.java', 'start_line': 2, 'end_line': 2, 'quote': 'String input;'}
            self.assertFalse(check_evidence(evidence, snap, [])['inspected'])
            self.assertTrue(check_evidence(evidence, snap, result['excerpts'])['inspected'])
            self.assertFalse(check_evidence({**evidence, 'quote': 'invented'}, snap, result['excerpts'])['sourceMatch'])

    def test_embedded_source_and_deterministic_snapshot(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            repo = self.repo(root)
            with zipfile.ZipFile(repo / 'game.jar', 'w') as archive:
                archive.writestr('a/Other.java', 'class Other {}')
                archive.writestr('a/Other.class', b'\x00binary')
            snap = snapshot(repo)
            self.assertEqual(snap['fingerprint'], snapshot(repo)['fingerprint'])
            self.assertIn('game.jar!/a/Other.java', [f['path'] for f in snap['files']])
            self.assertNotIn('game.jar!/a/Other.class', [f['path'] for f in snap['files']])

    def test_model_acquires_then_finishes_with_citation_checks(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            record, snap, out = prepare(self.repo(root), 'Add highscore', 'task', root / 'runs')
            actions = [
                {'action': 'search', 'query': 'input'},
                {'action': 'finish', 'summary': 'TEST FIXTURE ONLY', 'limitations': ['Not executed'], 'items': [
                    {'id': 'a', 'kind': 'security_property', 'topic': 'input', 'statement': 'Input field exists',
                     'task_relevance': 'Test fixture', 'cwes': [], 'suggested_check': 'Inspect callers',
                     'evidence': [{'path': 'Score.java', 'start_line': 2, 'end_line': 2, 'quote': 'String input;'}]}]},
            ]
            def model(command, request, timeout):
                durable = json.loads((out / 'record.json').read_text())
                self.assertEqual(durable['turns'][-1]['status'], 'started')
                return {'model': request['model'], 'request_id': request['request_id'], 'settings': request['settings'],
                        'finish_reason': 'stop', 'output_text': json.dumps(actions.pop(0)), 'usage': {}}
            with patch('research.generate_context.invoke', side_effect=model):
                done = execute(record, snap, out, ['test-only'])
                self.assertEqual(done['status'], 'completed')
                self.assertEqual(done['citationChecks']['matched'], 1)
                self.assertEqual(done['inspectedLines'], 1)
                self.assertEqual(len(done['turns']), 2)
                with self.assertRaises(FileExistsError): execute(record, snap, out, ['test-only'])

    def test_identity_mismatch_stops_and_never_reuses_prior_output(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            repo = self.repo(root)
            record, snap, out = prepare(repo, 'Add highscore', 'overview', root / 'runs')
            with patch('research.generate_context.invoke', return_value={'model': 'other', 'request_id': 'wrong', 'output_text': 'PRIVATE PRIOR OUTPUT'}):
                self.assertEqual(execute(record, snap, out, ['test-only'])['status'], 'identity_mismatch')
            fresh, _, _ = prepare(repo, 'Add highscore', 'overview', root / 'runs')
            self.assertNotIn('PRIVATE PRIOR OUTPUT', fresh['initialPrompt'])
            self.assertEqual(fresh['turns'], [])

    def test_budget_exhaustion_preserves_trace_without_inventing_output(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            record, snap, out = prepare(self.repo(root), 'Add highscore', 'flows', root / 'runs', max_turns=2)
            def model(command, request, timeout):
                return {'model': request['model'], 'request_id': request['request_id'], 'settings': None,
                        'finish_reason': 'stop', 'output_text': '{"action":"search","query":"absent"}'}
            with patch('research.generate_context.invoke', side_effect=model):
                done = execute(record, snap, out, ['test-only'])
            self.assertEqual(done['status'], 'budget_exhausted')
            self.assertIsNone(done['output'])
            self.assertFalse(done['settingsVerified'])


if __name__ == '__main__': unittest.main()
