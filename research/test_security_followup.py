import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from research.import_evidence import ROOT, canonical, digest
from research.paper_matrix import prepare as baseline_plan
from research.security_followup import acquisition_task, prepare, repository_input, select, security_block
from research.context_repository import snapshot
from research.run_experiment import frozen_manifest


class FollowupTests(unittest.TestCase):
    def test_incomplete_screening_cannot_select_or_acquire(self):
        with tempfile.TemporaryDirectory() as directory:
            out = Path(directory)
            with patch('research.security_followup.frozen_manifest'), patch('research.security_followup.study', return_value={'summary': {'complete': False}}):
                with self.assertRaisesRegex(ValueError, 'Complete all baseline'): select(out / 'missing.json', out)
            self.assertEqual(list(out.iterdir()), [])

    def test_acquisition_task_preserves_contract_and_only_reuse_has_donor(self):
        generation, reuse = acquisition_task('Generation'), acquisition_task('Reuse')
        self.assertNotIn('ApoIcarus', generation)
        self.assertIn('ApoIcarus', reuse)
        for task in (generation, reuse):
            self.assertIn('real score, name, and survival time (mm:ss)', task)
            self.assertIn('recordRunEnd(apoMario.level.ApoMarioLevel level)', task)
            self.assertNotIn('Instructions:', task)
            self.assertNotIn('Context: Use the attached json', task)
            self.assertNotIn('test_forged', task)
        for status in ('prepared', 'budget_exhausted', 'adapter_error'):
            with self.assertRaisesRegex(ValueError, 'no complete output'): security_block({'status': status})

    def test_combined_input_contains_both_games_without_thesis_contexts(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            repo = repository_input('Reuse', root)
            snap = snapshot(repo)
            self.assertTrue(any(f['path'].startswith('ApoMario/') for f in snap['files']))
            self.assertTrue(any(f['path'].startswith('ApoIcarus/') for f in snap['files']))
            self.assertTrue(all(f['path'].split('/')[0] in ('ApoMario', 'ApoIcarus') for f in snap['files']))
            self.assertEqual(snap['fingerprint'], snapshot(repository_input('Reuse', root))['fingerprint'])

    def test_followup_freezes_80_fresh_requests_and_byte_identical_controls(self):
        # Local temporary fixtures never become active study observations.
        local = ROOT / '.local'; local.mkdir(exist_ok=True)
        with tempfile.TemporaryDirectory(prefix='followup-test-', dir=local) as directory:
            root = Path(directory); base_dir = root / 'baseline'; base = baseline_plan(base_dir)
            selected = ['generation_none', 'generation_s', 'reuse_none', 'reuse_s']
            selection = {'fingerprint': 'fixture-selection', 'selected': selected, 'sourceHashes': {}}
            out = root / 'followup'; out.mkdir()
            for name in ('selection', 'generation-input', 'reuse-input'):
                (out / f'{name}.json').write_bytes(canonical(selection if name == 'selection' else {}))
            acquisitions = []
            for method in ('Generation', 'Reuse'):
                for strategy in ('overview', 'task', 'flows'):
                    rid = method.lower() + '-' + strategy
                    path = root / rid; path.mkdir()
                    record = {'id': rid, 'strategy': strategy, 'snapshotFingerprint': 'fixture-snapshot',
                              'task': acquisition_task(method), 'generatorHashes': {}, 'settingsVerified': False,
                              'status': 'settings_unverified', 'output': {'summary': 'TEST FIXTURE ONLY', 'items': [], 'limitations': ['test only']},
                              'citationChecks': {'matched': 0, 'total': 0, 'uncitedItems': 0}}
                    (path / 'record.json').write_bytes(canonical(record))
                    (path / 'snapshot.json').write_bytes(canonical({'fingerprint': record['snapshotFingerprint']}))
                    acquisitions.append({'method': method, 'strategy': strategy, 'id': rid, 'directory': str(path.relative_to(ROOT)),
                                         'snapshotFingerprint': record['snapshotFingerprint'], 'taskSha256': digest(record['task'].encode())})
            (out / 'acquisitions.json').write_bytes(canonical({'selectionFingerprint': selection['fingerprint'], 'acquisitions': acquisitions}))
            with patch('research.security_followup.select', return_value=selection):
                plan = prepare(base_dir / 'manifest.json', out)
                self.assertEqual(prepare(base_dir / 'manifest.json', out)['fingerprint'], plan['fingerprint'])
            self.assertEqual(len(plan['conditions']), 16)
            self.assertEqual(len(plan['schedule']), 80)
            self.assertFalse({r['runId'] for r in base['schedule']} & {r['runId'] for r in plan['schedule']})
            self.assertEqual(frozen_manifest(out / 'manifest.json')['fingerprint'], plan['fingerprint'])
            originals = {c['id']: c for c in base['conditions']}
            for c in plan['conditions']:
                raw = (out / c['promptFile']).read_bytes()
                original = (base_dir / originals[c['parentCondition']]['promptFile']).read_bytes()
                if c['securityStrategy'] == 'none': self.assertEqual(raw, original)
                else:
                    self.assertTrue(raw.startswith(original))
                    self.assertIn(b'BEGIN REPOSITORY-DERIVED SECURITY CONTEXT', raw[len(original):])
                    self.assertIn(b'TEST FIXTURE ONLY', raw[len(original):])
            broken = out / plan['conditions'][0]['promptFile']; broken.write_bytes(b'tampered')
            with self.assertRaisesRegex(ValueError, 'prompt hash'): frozen_manifest(out / 'manifest.json')


if __name__ == '__main__': unittest.main()
