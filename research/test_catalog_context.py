import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from research import catalog_context
from research import generate_security_context as base
from research import task_context
from research.import_evidence import ROOT, digest
from research.test_generate_security_context import item


class CatalogContextTests(unittest.TestCase):
    def test_catalog_text_lists_all_25_entries_and_no_check_specific_guidance(self):
        text = catalog_context.INSTRUCTION
        self.assertTrue(text.startswith(task_context.INSTRUCTION))
        for entry in catalog_context.CATALOG['entries']:
            self.assertIn(f"{entry['rank']}. {entry['id']} {entry['name']}: {entry['description']}", text)
        self.assertEqual(text.count('\n1. CWE-'), 1)
        for guidance in base.STRATEGIES.values():
            self.assertNotIn(guidance[1], text)
        for hint in ('highscore', 'negative', 'readline', '64 mib', 'retention', 'canary', 'apomario', 'storeRun'.lower()):
            self.assertNotIn(hint, text.lower())

    def test_requests_carry_the_catalog_and_records_freeze_it(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary); repo = root / 'repo'; repo.mkdir()
            (repo / 'Score.java').write_text('class Score {\n  String name;\n}\n')
            original = (dict(base.STRATEGIES), base.PROTOCOL, base.TOOL, task_context.INSTRUCTION, task_context.PROTOCOL, task_context.STRATEGY)
            record, snap, directory = catalog_context.prepare(repo, 'Add Highscore to ApoMario.', root / 'records', 2)
            self.assertEqual(record['protocol'], catalog_context.PROTOCOL)
            self.assertEqual(record['strategy'], 'catalog')
            self.assertEqual(record['acquisitionInstruction'], catalog_context.INSTRUCTION)
            self.assertEqual(record['catalog']['sha256'], digest(catalog_context.CATALOG_FILE.read_bytes()))
            self.assertIn('research/security/cwe-top25-2025.json', record['generatorHashes'])
            self.assertIn('research/catalog_context.py', record['generatorHashes'])
            self.assertEqual(json.loads((directory / 'record.json').read_text())['catalog'], record['catalog'])
            requests = []

            def invoke(command, request, timeout):
                requests.append(request)
                prompt = request['messages'][0]['content']
                self.assertIn('TASK\nAdd Highscore to ApoMario.', prompt)
                self.assertIn('WEAKNESS CATALOG', prompt)
                self.assertIn('15. CWE-502 Deserialization of Untrusted Data', prompt)
                self.assertNotIn('STRATEGY\n', prompt)
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
                result = catalog_context.execute(record, snap, directory, ['fixture'])
            self.assertEqual(result['status'], 'settings_unverified')
            self.assertEqual(result['strategy'], 'catalog')
            self.assertIn('Score.java:2-2', result['promptInsert'])
            self.assertEqual(len(result['turns']), 2)
            self.assertEqual((base.STRATEGIES, base.PROTOCOL, base.TOOL, task_context.INSTRUCTION, task_context.PROTOCOL, task_context.STRATEGY), original)

    def test_task_only_records_and_changed_catalogs_are_rejected(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary); repo = root / 'repo'; repo.mkdir()
            (repo / 'Score.java').write_text('class Score {}\n')
            plain, snap, directory = task_context.prepare(repo, 'Add Highscore.', root / 'records')
            with patch.object(base, 'invoke') as invoke, self.assertRaisesRegex(ValueError, 'catalog acquisition'):
                catalog_context.execute(plain, snap, directory, ['fixture'])
            invoke.assert_not_called()
            record, snap, directory = catalog_context.prepare(repo, 'Add Highscore.', root / 'records')
            record['catalog']['sha256'] = 'changed'
            with patch.object(base, 'invoke') as invoke, self.assertRaisesRegex(ValueError, 'frozen weakness catalog'):
                catalog_context.execute(record, snap, directory, ['fixture'])
            invoke.assert_not_called()

    def test_catalog_file_is_the_ranked_2025_list(self):
        catalog = json.loads((ROOT / 'research/security/cwe-top25-2025.json').read_text())
        self.assertEqual([e['id'] for e in catalog['entries']][:3], ['CWE-79', 'CWE-89', 'CWE-352'])
        self.assertEqual(catalog['entries'][-1]['id'], 'CWE-770')
        self.assertEqual(len({e['id'] for e in catalog['entries']}), 25)
        bad = dict(catalog, entries=catalog['entries'][:24])
        path = Path(tempfile.mkdtemp()) / 'bad.json'; path.write_text(json.dumps(bad))
        with self.assertRaisesRegex(ValueError, '25 ranked entries'):
            catalog_context.load_catalog(path)


if __name__ == '__main__':
    unittest.main()
