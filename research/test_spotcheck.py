import csv
import io
import json
from pathlib import Path
import tempfile
import unittest

from research import spotcheck


class SpotcheckTests(unittest.TestCase):
    def test_stratified_sample_and_inlined_citations(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary); iteration = 'fx'
            public = root / 'research/iterations' / iteration; public.mkdir(parents=True)
            packets = root / '.local/issue-handling' / iteration / 'packets'; packets.mkdir(parents=True)
            source = 'class A {\n  int a;\n  int b;\n  int c;\n}\n'
            rows = []
            for rep in (1, 2, 3):
                (packets / f'fx__cell__none__r{rep}.json').write_text(json.dumps({'sources': [{'path': 'A.java', 'text': source}]}))
                for check in spotcheck.CHECKS:
                    rows.append({'condition': 'cell__none', 'repetition': str(rep), 'check': check, 'qualifiedStatus': 'fail', 'category': 'accepted' if rep < 3 else 'unresolved',
                                 'evidence': 'A.java:L2-L3; A.java:L9', 'note': 'n', 'reviewer': 'r', 'sourceSha256': 'x'})
            buffer = io.StringIO(); writer = csv.DictWriter(buffer, fieldnames=list(rows[0].keys())); writer.writeheader(); writer.writerows(rows)
            (public / 'issue-handling.csv').write_text(buffer.getvalue())
            entries = spotcheck.build(iteration, 2, 7, root)
            self.assertEqual(len(entries), 20); self.assertEqual({e['check'] for e in entries}, set(spotcheck.CHECKS))
            self.assertTrue(all(e['category'] != 'unresolved' for e in entries))
            self.assertEqual(entries, spotcheck.build(iteration, 2, 7, root))  # deterministic
            first = entries[0]['citations']
            self.assertEqual(first[0]['text'], '2:   int a;\n3:   int b;'); self.assertEqual(first[1]['text'], '')  # L9 is beyond the file: empty excerpt, reference kept
            text = spotcheck.render(entries, iteration, 7)
            self.assertIn('| 1 | cell__none r', text); self.assertIn('```java\n2:   int a;\n3:   int b;\n```', text); self.assertIn('Human verdict: ', text)


if __name__ == '__main__':
    unittest.main()
