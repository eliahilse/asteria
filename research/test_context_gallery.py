import json
from pathlib import Path
import tempfile
import unittest
from unittest import mock

from research import context_gallery

TEXT = '[R1; requirement; task; tampering; CWE-20] Reject invalid names at storeRun.\nTask relevance: names are persisted.\nFailure behavior: return false and leave the board unchanged.\nInspected source: A.java:1-2 (A#storeRun)\n'


class ContextGalleryTests(unittest.TestCase):
    def test_agent_round_inserts_and_counts(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary); it = root / 'research/iterations/i99-fx'; (it / 'contexts').mkdir(parents=True)
            (it / 'contexts/generation-dataflow-compact.txt').write_text(TEXT); (it / 'contexts/generation-dataflow.record-id.txt').write_text('agent-none\n')
            plan = {'id': 'i99-fx', 'conditions': [
                {'id': 'generation_s__single_shot__none', 'strategy': 'Generation', 'repository': 'Generation', 'parentCondition': 'generation_s', 'sidecar': 'none', 'mode': 'single_shot'},
                {'id': 'generation_s__single_shot__static', 'strategy': 'Generation', 'repository': 'Generation', 'parentCondition': 'generation_s', 'sidecar': 'static', 'mode': 'single_shot',
                 'contextInsertFile': 'research/iterations/i99-fx/contexts/generation-dataflow-compact.txt'}]}
            (it / 'plan.json').write_text(json.dumps(plan))
            (it / 'qualified-analysis.json').write_text(json.dumps({'conditions': [
                {'condition': 'generation_s__single_shot__none', 'n': 3, 'withinBudgetFull': 3, 'issues': {'failed': 20, 'unresolved': 1, 'evaluated': 29, 'plannedChecks': 30}},
                {'condition': 'generation_s__single_shot__static', 'n': 3, 'withinBudgetFull': 2, 'issues': {'failed': 4, 'unresolved': 2, 'evaluated': 28, 'plannedChecks': 30}}]}))
            (it / 'README.md').write_text('# I99: fixture round\n')
            with mock.patch.object(context_gallery, 'ROOT', root), mock.patch.object(context_gallery, 'ITERATIONS', root / 'research/iterations'), mock.patch.object(context_gallery, 'RECORDS', root / '.local/context-generation'):
                rounds = context_gallery.gather()
                self.assertEqual([r['id'] for r in rounds], ['i99-fx'])
                insert = rounds[0]['inserts'][0]
                self.assertEqual(insert['source'], 'agent-acquired'); self.assertEqual(insert['record'], {'id': 'agent-none'})
                self.assertEqual([a['id'] for a in insert['arms']], ['generation_s__single_shot__static']); self.assertEqual(insert['arms'][0]['passed'], 24)
                self.assertEqual([c['id'] for c in insert['controls']], ['generation_s__single_shot__none'])
                page = context_gallery.render(rounds, 'rule text')
                self.assertIn('class="stmt">[R1; requirement', page); self.assertIn('class="fail">Failure behavior: return false', page)
                self.assertIn('functional 2 of 3; failed / unresolved / passed 4 / 2 / 24 of 30', page); self.assertIn('rule text', page)
                self.assertEqual(context_gallery.stats(TEXT), {'characters': len(TEXT), 'statements': 1, 'failureLines': 1})

    def test_natural_order(self):
        self.assertEqual(sorted(['i16b-x', 'i9-x', 'i16-x', 'i21a-x'], key=context_gallery.natural_key), ['i9-x', 'i16-x', 'i16b-x', 'i21a-x'])


if __name__ == '__main__':
    unittest.main()
