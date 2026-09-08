import json
from pathlib import Path
import subprocess
import tempfile
import unittest
from unittest.mock import patch
from research import evaluate_isolated as evaluator


class IsolationTests(unittest.TestCase):
    def test_each_jvm_gets_a_clean_home_and_preserves_the_allowlisted_environment(self):
        homes = []
        def process(command, **kwargs):
            home = Path(next(arg.split('=', 1)[1] for arg in command if arg.startswith('-Duser.home=')))
            temporary = Path(next(arg.split('=', 1)[1] for arg in command if arg.startswith('-Djava.io.tmpdir=')))
            self.assertTrue(home.is_dir()); self.assertTrue(temporary.is_dir())
            self.assertEqual(kwargs['env']['HOME'], str(home))
            self.assertEqual(kwargs['env']['LANG'], 'fixture')
            self.assertNotIn('-Duser.home=/shared', command)
            self.assertFalse((home / 'scores.dat').exists())
            (home / 'scores.dat').write_text('must not leak to the next JVM')
            homes.append(home)
            return subprocess.CompletedProcess(command, 0)
        def evaluation(text, output, identity):
            output.mkdir()
            for name in ('FirstSuite', 'SecondSuite'):
                subprocess.run(['java', '-Duser.home=/shared', name], env={'LANG': 'fixture'})
            return {'inputHashes': {}, 'environment': {}, 'processes': [], 'security': None}
        with tempfile.TemporaryDirectory() as temporary, patch.object(subprocess, 'run', side_effect=process), patch.object(evaluator.original, 'evaluate_response', side_effect=evaluation):
            report = evaluator.evaluate_response('fixture', Path(temporary) / 'evaluation')
        self.assertEqual(len(set(homes)), 2)
        self.assertTrue(all(not home.exists() for home in homes))
        self.assertEqual(report['protocol'], evaluator.PROTOCOL)
        self.assertEqual(len(report['isolatedJvms']), 2)


if __name__ == '__main__': unittest.main()
