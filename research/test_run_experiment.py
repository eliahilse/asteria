import json
import sys
import tempfile
import unittest
from pathlib import Path
from unittest.mock import patch

from research.import_evidence import ROOT, canonical, digest
from research.model_adapter import AdapterFailure, invoke, validate_response
from research.run_experiment import execute, frozen_manifest


MANIFEST = ROOT / 'research/experiments/luna-highscore-v1/manifest.json'
FAKE = '''import json,sys
r=json.load(sys.stdin)
json.dump(dict(protocol_version=1,request_id=r['request_id'],model=r['model'],
settings=r['settings'],output_text='TEST FIXTURE ONLY',finish_reason='stop',usage={'input_tokens':7}),sys.stdout)
'''


class RunnerTests(unittest.TestCase):
    def test_resume_exact_payload_and_preserve_unfinished_attempt(self):
        with tempfile.TemporaryDirectory() as directory:
            out = Path(directory)
            first = execute(MANIFEST, out, [sys.executable, '-c', FAKE], limit=1)[0]
            self.assertEqual(first['status'], 'completed')
            self.assertIsNone(first['response']['cost_usd'])
            self.assertEqual(first['requestSha256'], digest(canonical(first['request'])))
            self.assertEqual(first['promptSha256'], digest(first['request']['messages'][0]['content'].encode()))
            manifest = frozen_manifest(MANIFEST)
            unfinished = out / f"{manifest['schedule'][1]['runId']}.json"
            unfinished.write_text('{"status":"started"}')
            second = execute(MANIFEST, out, [sys.executable, '-c', FAKE], limit=1)[0]
            self.assertEqual(second['runId'], manifest['schedule'][2]['runId'])
            self.assertEqual(json.loads(unfinished.read_text()), {'status': 'started'})
            self.assertIsNone(second['evaluation'])

    def test_failure_record_is_not_retried_and_does_not_expose_stderr(self):
        with tempfile.TemporaryDirectory() as directory:
            out = Path(directory)
            row = execute(MANIFEST, out, [sys.executable, '-c', "import sys;sys.stderr.write('secret-token');sys.exit(1)"])[0]
            self.assertEqual(row['status'], 'adapter_error')
            self.assertNotIn('secret-token', (out / f"{row['runId']}.json").read_text())
            next_row = execute(MANIFEST, out, [sys.executable, '-c', FAKE])[0]
            self.assertNotEqual(row['runId'], next_row['runId'])

    def test_model_substitution_is_flagged(self):
        with tempfile.TemporaryDirectory() as directory:
            row = execute(MANIFEST, Path(directory), [sys.executable, '-c', FAKE.replace("model=r['model']", "model='different-model'")])[0]
            self.assertEqual(row['status'], 'settings_unverified')
            self.assertEqual(row['mismatches'], ['served_model'])

    def test_timeout_is_uncertain_and_invalid_numbers_are_rejected(self):
        with self.assertRaises(AdapterFailure) as caught:
            invoke([sys.executable, '-c', 'import time;time.sleep(5)'], {}, 0.05)
        self.assertEqual(caught.exception.category, 'transport_outcome_unknown')
        for cost in (-1, float('nan'), float('inf'), True):
            with self.assertRaises(AdapterFailure):
                validate_response(dict(protocol_version=1, model='test', output_text='x', finish_reason='stop', cost_usd=cost))

    def test_started_record_survives_interruption(self):
        with tempfile.TemporaryDirectory() as directory:
            with patch('research.run_experiment.invoke', side_effect=KeyboardInterrupt):
                with self.assertRaises(KeyboardInterrupt): execute(MANIFEST, Path(directory), ['unused'])
            records = list(Path(directory).glob('*.json'))
            self.assertEqual(len(records), 1)
            self.assertEqual(json.loads(records[0].read_text())['status'], 'started')


if __name__ == '__main__': unittest.main()
