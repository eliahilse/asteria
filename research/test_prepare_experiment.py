import tempfile
import unittest
from pathlib import Path
from research.prepare_experiment import prepare
from research.import_evidence import ROOT, digest


class ExperimentTests(unittest.TestCase):
    def test_frozen_schedule_and_exact_bridge_prompts(self):
        with tempfile.TemporaryDirectory() as directory:
            out=Path(directory);manifest=prepare(out)
            self.assertEqual(len(manifest['conditions']),16)
            self.assertEqual(len(manifest['schedule']),140)
            self.assertEqual(sum(r['stage']=='bridge' for r in manifest['schedule']),20)
            self.assertEqual(len({r['runId'] for r in manifest['schedule']}),140)
            self.assertEqual(manifest,prepare(out))
            for condition in manifest['conditions']:
                content=(out/condition['promptFile']).read_bytes()
                self.assertEqual(digest(content),condition['promptSha256'])
                if condition['stage']=='bridge':self.assertEqual(content,(ROOT/condition['source']).read_bytes())
                if condition['stage']=='ablation':
                    self.assertNotIn(b'BEGIN ATTACHED CONTEXT',content)
                    self.assertNotIn(b'Write down your brief internal reasoning',content)
                    # The original game legitimately uses 1000000 for time conversion.
                    # Check concrete test-harness identifiers, not incidental numbers.
                    self.assertFalse(b'SecurityProbe' in content)
                    self.assertFalse(b'nativeDeserializationCanary' in content)
                    self.assertIn(b'BEGIN ATTACHED TARGET SOURCE',content)
            with self.assertRaises(FileExistsError):prepare(out,bridge_repetitions=1)

if __name__=='__main__':unittest.main()
