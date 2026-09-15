import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from research import catalog_experiment
from research.import_evidence import ROOT, digest

LOCAL = ROOT / '.local'  # the code under test requires paths under the repository's local directory; absent on CI
LOCAL.mkdir(exist_ok=True)


def condition(cell, strategy, prompt_file, sha):
    return {'id': f'{cell}__{strategy}', 'parentCondition': cell, 'securityStrategy': strategy, 'strategy': 'Generation' if cell.startswith('generation') else 'Reuse',
            'promptFile': prompt_file, 'promptSha256': sha, 'repetitions': 5, 'contextAcquisitionId': None if strategy == 'none' else f'ctx-{strategy}'}


class CatalogExperimentTests(unittest.TestCase):
    def test_conditions_reuse_parent_bytes_and_append_new_inserts(self):
        with tempfile.TemporaryDirectory(dir=LOCAL) as temporary:
            root = Path(temporary); parent_dir = root / 'parent'; directory = root / 'child'
            (parent_dir / 'prompts').mkdir(parents=True); (directory / 'contexts').mkdir(parents=True)
            conditions, prompts = [], {}
            for cell in catalog_experiment.CELLS:
                for strategy in ('none', 'requirements', 'operations'):
                    raw = f'{cell} {strategy} prompt'.encode(); sha = digest(raw)
                    (parent_dir / 'prompts' / f'{sha}.txt').write_bytes(raw); prompts[(cell, strategy)] = raw
                    conditions.append(condition(cell, strategy, f'prompts/{sha}.txt', sha))
            parent = {'conditions': conditions}
            additions = []
            for method in ('Generation', 'Reuse'):
                for strategy in ('task_only', 'catalog'):
                    insert = f'--- {method} {strategy} insert ---'.encode()
                    (directory / 'contexts' / f'{method.lower()}-{strategy}.txt').write_bytes(insert)
                    additions.append({'method': method, 'strategy': strategy, 'id': f'ctx-{method}-{strategy}', 'promptInsertSha256': digest(insert)})
            sources = {}
            built = catalog_experiment.build_conditions(parent, parent_dir, directory, additions, sources)
            self.assertEqual([c['id'] for c in built], [f'{cell}__{arm}' for cell in catalog_experiment.CELLS for arm in catalog_experiment.ARMS])
            for c in built:
                raw = (directory / c['promptFile']).read_bytes()
                self.assertEqual(digest(raw), c['promptSha256']); self.assertEqual(c['repetitions'], 5)
                cell = c['parentCondition']
                if c['securityStrategy'] in ('none', 'requirements'):
                    self.assertEqual(raw, prompts[(cell, c['securityStrategy'])])
                else:
                    method = 'Generation' if cell == 'generation_s' else 'Reuse'
                    self.assertEqual(raw, prompts[(cell, 'none')] + b'\n\n' + f'--- {method} {c["securityStrategy"]} insert ---'.encode())
                    self.assertEqual(c['contextAcquisitionId'], f'ctx-{method}-{c["securityStrategy"]}')
            self.assertEqual(len(sources), 4)
            self.assertNotIn('generation_s__operations', [c['id'] for c in built])
            rows = catalog_experiment.schedule('i09-test', built)
            self.assertEqual(len(rows), 40)
            self.assertEqual(sorted(r['condition'] for r in rows), sorted(c['id'] for c in built for _ in range(5)))
            self.assertEqual(rows, catalog_experiment.schedule('i09-test', built))

    def test_tampered_insert_is_rejected(self):
        with tempfile.TemporaryDirectory(dir=LOCAL) as temporary:
            root = Path(temporary); parent_dir = root / 'parent'; directory = root / 'child'
            (parent_dir / 'prompts').mkdir(parents=True); (directory / 'contexts').mkdir(parents=True)
            conditions = []
            for cell in catalog_experiment.CELLS:
                for strategy in ('none', 'requirements'):
                    raw = f'{cell} {strategy}'.encode(); sha = digest(raw)
                    (parent_dir / 'prompts' / f'{sha}.txt').write_bytes(raw); conditions.append(condition(cell, strategy, f'prompts/{sha}.txt', sha))
            additions = []
            for method in ('Generation', 'Reuse'):
                for strategy in ('task_only', 'catalog'):
                    (directory / 'contexts' / f'{method.lower()}-{strategy}.txt').write_bytes(b'insert')
                    additions.append({'method': method, 'strategy': strategy, 'id': 'x', 'promptInsertSha256': digest(b'insert')})
            additions[0]['promptInsertSha256'] = 'tampered'
            with self.assertRaisesRegex(ValueError, 'insert differs'):
                catalog_experiment.build_conditions({'conditions': conditions}, parent_dir, directory, additions, {})

    def test_acquisition_preparation_uses_both_generic_modules_without_model_calls(self):
        with tempfile.TemporaryDirectory(dir=LOCAL) as temporary:
            directory = Path(temporary) / 'i09-test'
            prepared = []

            def fake_prepare(module):
                def prepare(repo, task, output, max_turns=16):
                    target = directory / 'records' / f'{module}-{len(prepared)}'; target.mkdir(parents=True)
                    record = {'id': target.name, 'strategy': module, 'task': task}
                    (target / 'record.json').write_text(json.dumps(record)); prepared.append((module, task)); return record, {}, target
                return prepare

            with patch.object(catalog_experiment.task_context, 'prepare', side_effect=fake_prepare('task_only')), \
                 patch.object(catalog_experiment.catalog_context, 'prepare', side_effect=fake_prepare('catalog')), \
                 patch.object(catalog_experiment, 'repository_input', side_effect=lambda method, output: directory / method), \
                 patch.object(catalog_experiment, 'write_atomic', side_effect=lambda path, value: path.write_text(json.dumps(value))):
                acquisitions = catalog_experiment.prepare_acquisitions(directory)
            self.assertEqual([(a['method'], a['strategy']) for a in acquisitions],
                             [('Generation', 'task_only'), ('Generation', 'catalog'), ('Reuse', 'task_only'), ('Reuse', 'catalog')])
            self.assertTrue(all(task.endswith('display them as mm:ss.') for _, task in prepared))
            self.assertIn('Donor repository: ApoIcarus', prepared[2][1]); self.assertNotIn('Donor', prepared[0][1])
            self.assertEqual(json.loads((directory / 'acquisitions-generic.json').read_text()), acquisitions)
            with self.assertRaisesRegex(ValueError, 'already exists'):
                catalog_experiment.prepare_acquisitions(directory)


if __name__ == '__main__':
    unittest.main()
