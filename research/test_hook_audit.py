import json
from pathlib import Path
import tempfile
import unittest

from research import hook_audit

ORIGINAL = {'ApoMarioLevel.java': 'class ApoMarioLevel {\n  void setAnalysis(boolean b) {\n    this.analysis = b;\n  }\n}\n'}
CHECKS = [{'name': 'saveAddsEntry', 'status': 'pass', 'suite': 'unit'},
          {'name': 'recordedSurvivalTimeIsTheRealElapsedTime', 'status': 'fail', 'suite': 'autonomous'},
          {'name': 'secondRunAlsoRecordedAndBoardSortedDescending', 'status': 'fail', 'suite': 'autonomous'}]


def write_run(root: Path, name: str, hook_lines: list[str], checks, functional: bool, rejects_null: str = 'pass'):
    run = root / '.local/iterations/fx/runs' / f'fx__{name}__r1'; (run / 'submission-2' / 'evaluation').mkdir(parents=True)
    level = ['class ApoMarioLevel {', '  void setAnalysis(boolean b) {', '    this.analysis = b;', '    recordRunEnd(this);', '  }', '  void recordRunEnd(ApoMarioLevel level) {', *hook_lines, '  }', '}']
    text = '```java filename=ApoMarioLevel.java\n' + '\n'.join(level) + '\n```\n\n```java filename=ApoMarioHighscore.java\nclass ApoMarioHighscore {}\n```\n'
    (run / 'submission-2' / 'complete-files.txt').write_text(text)
    (run / 'submission-2' / 'evaluation' / 'report.json').write_text(json.dumps({'security': {'checks': [{'name': 'rejectsNegativeScore', 'status': 'fail'}, {'name': 'rejectsNullName', 'status': rejects_null}]}}))
    record = {'runId': f'fx__{name}__r1', 'condition': name, 'repetition': 1, 'status': 'completed', 'functionalSuccess': functional, 'finalEvaluation': 'submission-2/evaluation/report.json',
              'submissions': [{'number': 1, 'status': 'invalid_changes'}, {'number': 2, 'status': 'evaluated', 'compilation': 'pass', 'feedback': {'functionalChecks': checks}}]}
    (run / 'record.json').write_text(json.dumps(record))


class HookAuditTests(unittest.TestCase):
    def test_classes_and_summary(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary)
            write_run(root, 'cell__skip', ['    ApoMarioPlayer p = level.getPlayers().get(0);', '    if (p == null || p.getTeamName() == null) return;', '    store.storeRun(p.getPoints(), level.getPassedTime(), p.getTeamName());',
                                           '    String line = in.readLine(); if (line == null) throw new IOException("truncated");'], CHECKS, False)  # a null test with a literal that is not a name fallback
            write_run(root, 'cell__fallback', ['    String name = p.getTeamName();', '    if (name == null || name.trim().length() == 0) name = "Player";', '    store.storeRun(p.getPoints(), level.getPassedTime(), name);'], [{**c, 'status': 'pass'} for c in CHECKS], True)
            write_run(root, 'cell__passthrough', ['    store.storeRun(p.getPoints(), level.getPassedTime(), p.getTeamName());'], CHECKS, False)
            write_run(root, 'cell__lenient', ['    store.storeRun(p.getPoints(), level.getPassedTime(), p.getTeamName());'], [{**c, 'status': 'pass'} for c in CHECKS], True, rejects_null='fail')
            write_run(root, 'cell__none', ['    store.storeRun(p.getPoints(), level.getPassedTime(), "Mario");'], CHECKS, False)
            rows = hook_audit.audit('fx', root, ORIGINAL)
            by = {r['condition']: r for r in rows}
            self.assertEqual(by['cell__skip']['class'], 'skip_on_null'); self.assertEqual(by['cell__skip']['nullNameTestsFailed'], 2); self.assertEqual(by['cell__skip']['finalSubmission'], 2)
            self.assertEqual([c['line'] for c in by['cell__skip']['cited']], [8, 9])  # only new lines; the untouched setAnalysis body is not cited
            self.assertEqual(by['cell__fallback']['class'], 'fallback'); self.assertEqual(by['cell__passthrough']['class'], 'passthrough'); self.assertEqual(by['cell__none']['class'], 'none')
            self.assertEqual({k: v['mechanism'] for k, v in by.items()}, {'cell__skip': 'skipped_at_hook', 'cell__fallback': 'recorded', 'cell__passthrough': 'rejected_at_store', 'cell__lenient': 'recorded', 'cell__none': 'none'})
            self.assertEqual((by['cell__passthrough']['rejectsNullName'], by['cell__lenient']['rejectsNullName']), ('pass', 'fail'))
            summary = {s['condition']: s for s in hook_audit.summarize(rows)}
            self.assertEqual((summary['cell__skip']['skip_on_null'], summary['cell__skip']['notRecorded'], summary['cell__skip']['notRecordedAndFail'], summary['cell__skip']['functional']), (1, 1, 1, 0))
            self.assertEqual((summary['cell__passthrough']['rejected_at_store'], summary['cell__passthrough']['notRecordedAndFail']), (1, 1))
            text = hook_audit.render(rows, 'fx')
            self.assertIn('| cell__skip | 1 | 0 | 0 | 1 | 0 | 0 | 1 | 0 | 0 | 1 | 1 | 1 | 1 | 0 |', text); self.assertIn('`ApoMarioLevel.java:8`', text); self.assertIn('skipped at hook, not functional', text)
            self.assertTrue(hook_audit.null_name_only(by['cell__skip'])); self.assertFalse(hook_audit.null_name_only(by['cell__fallback']))
            self.assertFalse(any(hook_audit.discordant(r) for r in rows)); self.assertIn('## Rows for hand review\n\n- none', text)
            by['cell__lenient']['nullNameTestsFailed'] = 2  # a recording hook that still fails the null-name tests must be flagged
            self.assertTrue(hook_audit.discordant(by['cell__lenient'])); self.assertIn('- cell__lenient r1: passthrough, recorded', hook_audit.render(rows, 'fx'))
            self.assertIn('fx__cell__fallback__r1,cell__fallback,1,completed,True,2,pass,fallback,pass,recorded,0,', hook_audit.csv_text(rows))

    def test_run_without_evaluated_submission(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary); run = root / '.local/iterations/fx/runs/fx__cell__x__r1'; run.mkdir(parents=True)
            (run / 'record.json').write_text(json.dumps({'runId': 'fx__cell__x__r1', 'condition': 'cell__x', 'repetition': 1, 'status': 'transport_failure', 'functionalSuccess': False, 'submissions': []}))
            row = hook_audit.audit('fx', root, ORIGINAL)[0]
            self.assertEqual((row['class'], row['finalSubmission'], row['failingChecks']), ('none', None, []))


if __name__ == '__main__':
    unittest.main()
