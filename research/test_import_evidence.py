import json
import tempfile
import unittest
from pathlib import Path
from research.import_evidence import Importer, ROOT, canonical, digest


class EvidenceTests(unittest.TestCase):
    def test_current_study_has_no_historical_runs_or_result_payloads(self):
        data = Importer().build()
        self.assertEqual(data['schemaVersion'], 2)
        self.assertEqual(data['runs'], [])
        self.assertEqual(len(data['tests']), 27)
        self.assertEqual(sum(t['kind'] == 'security' for t in data['tests']), 11)
        self.assertFalse(any('/results/' in a['path'] and ('pilot' in a['path'] or 'meeting' in a['path']) for a in data['artifacts']))
        self.assertEqual(len(data['experimentPlans'][0]['schedule']), 140)

    def test_source_bytes_and_fingerprint_are_reproducible(self):
        data = Importer().build()
        for artifact in data['artifacts']:
            self.assertEqual(digest((ROOT / artifact['path']).read_bytes()), artifact['sha256'])
        self.assertEqual(data, Importer().build())
        self.assertEqual(data.pop('fingerprint'), digest(canonical(data)))

    def test_deleted_payloads_are_pruned_from_generated_assets(self):
        with tempfile.TemporaryDirectory() as directory:
            out = Path(directory)
            (out / 'evidence').mkdir()
            stale = out / 'evidence' / ('0' * 64 + '.txt')
            stale.write_text('old result')
            Importer().write(out)
            self.assertFalse(stale.exists())

    def test_rejects_artifacts_outside_repository(self):
        with self.assertRaises(ValueError): Importer().artifact('../outside.txt')

    def test_new_attempts_require_explicit_import_and_missing_checks_stay_unrun(self):
        with tempfile.TemporaryDirectory(dir=ROOT) as directory:
            out = Path(directory)
            data = Importer().build(); plan = data['experimentPlans'][0]; row = plan['schedule'][0]
            condition = next(c for c in plan['conditions'] if c['id'] == row['condition'])
            settings = dict(reasoning_effort=plan['reasoning'], temperature=plan['temperature'], max_output_tokens=plan['maxOutputTokens'])
            request = dict(protocol_version=1, request_id=row['runId'], model=plan['model'], settings=settings,
                           messages=[dict(role='user', content=(ROOT / condition['prompt']['path']).read_text())])
            record = dict(schemaVersion=1, **row, manifestFingerprint=plan['fingerprint'], request=request, requestSha256=digest(canonical(request)))
            record.update(status='adapter_error', errorCategory='adapter_start_failed')
            (out / 'attempt.json').write_bytes(canonical(record))
            self.assertEqual(Importer().build()['runs'], [])
            runs = Importer(runs_dir=out).build()['runs']
            self.assertEqual(len(runs), 1)
            self.assertTrue(all(c['status'] == 'not_run' for c in runs[0]['checks']))
            self.assertEqual(runs[0]['compileStatus'], 'not_run')
            record['request']['messages'][0]['content'] = 'tampered'
            (out / 'attempt.json').write_bytes(canonical(record))
            with self.assertRaisesRegex(ValueError, 'frozen prompt'): Importer(runs_dir=out).build()


if __name__ == '__main__': unittest.main()
