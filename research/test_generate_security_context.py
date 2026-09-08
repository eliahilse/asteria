import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from research import generate_security_context as generator
from research.import_evidence import canonical, digest


def item(kind='recommendation', basis='reasoned', refs=None):
    return {'id': 'storage', 'kind': kind, 'basis': basis, 'topic': 'persistence',
            'statement': 'Validate stored data before using it.', 'task_relevance': 'The task persists scores.',
            'cwes': [], 'evidence_ids': refs or [], 'suggested_check': 'Exercise malformed stored input.'}


class ContextV2Tests(unittest.TestCase):
    def test_prospective_recommendation_survives_without_fabricating_repository_evidence(self):
        output, checks = generator.validate_output({'summary': 'Task requirements', 'items': [item()], 'limitations': []}, {})
        self.assertEqual(output['items'][0]['evidence'], [])
        self.assertEqual(checks['itemsByKind']['recommendation'], 1)
        with self.assertRaisesRegex(ValueError, 'Observed claims'):
            generator.validate_output({'summary': 'Observed claim', 'items': [item('security_property', 'observed')], 'limitations': []}, {})

    def test_invented_reference_is_rejected(self):
        with self.assertRaisesRegex(ValueError, 'Unknown evidence ID'):
            generator.validate_output({'summary': 'Claim', 'items': [item(refs=['E9999'])], 'limitations': []}, {})

    def test_search_evidence_is_resolved_from_the_snapshot_and_full_exchange_is_retained(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary); repo = root / 'repo'; repo.mkdir()
            (repo / 'Score.java').write_text('class Score {\n  String name;\n}\n')
            record, snap, directory = generator.prepare(repo, 'Persist scores', 'requirements', root / 'records', max_turns=2)
            requests = []
            def invoke(command, request, timeout):
                requests.append(request)
                if len(requests) == 1: action = {'action': 'search', 'query': 'String name', 'paths': None}
                else:
                    observed = item('security_property', 'observed', ['E0001'])
                    observed['statement'] = 'The score class declares a string name.'
                    proposed = item(); proposed['id'] = 'requirement'
                    action = {'action': 'finish', 'summary': 'Name storage', 'items': [observed, proposed], 'limitations': ['No runtime execution.']}
                return {'request_id': request['request_id'], 'model': request['model'], 'settings': None, 'finish_reason': 'stop', 'output_text': json.dumps(action)}
            with patch.object(generator, 'invoke', side_effect=invoke): result = generator.execute(record, snap, directory, ['fixture'])
            self.assertEqual(result['status'], 'settings_unverified')
            ref = result['output']['items'][0]['evidence'][0]
            self.assertEqual((ref['path'], ref['start_line'], ref['end_line'], ref['quote']), ('Score.java', 2, 2, '  String name;'))
            self.assertIn('E0001', requests[1]['messages'][-1]['content'])
            self.assertIn('Score.java:2-2', result['promptInsert'])
            self.assertIn('Validate stored data', result['promptInsert'])
            for turn in result['turns']:
                self.assertEqual(turn['requestSha256'], digest(canonical(turn['request'])))
                self.assertEqual(turn['responseSha256'], digest(canonical(turn['response'])))
            self.assertEqual(json.loads((directory / 'record.json').read_text())['output'], result['output'])
            with self.assertRaises(FileExistsError): generator.execute(record, snap, directory, ['fixture'])


if __name__ == '__main__': unittest.main()
