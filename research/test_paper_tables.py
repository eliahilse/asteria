"""The paper's tables are asserted against the saved analyses: the baseline matrix against research.matrix_comparison, the results table against research.paper_results_table."""
import json
from pathlib import Path
import unittest

from research.import_evidence import ROOT


def parse_table(source: str, label: str) -> tuple[list[str], list[tuple[str, list[str]]]]:
    block = source.split(f'\\label{{{label}}}', 1)[1].split('\\end{tabular}', 1)[0]
    rows = [line.strip().rstrip('\\').strip() for line in block.splitlines() if '&' in line]
    header = [cell.strip() for cell in rows[0].split('&')][1:]
    body = [(cells[0].strip(), [c.strip() for c in cells[1:]]) for cells in (row.split('&') for row in rows[1:])]
    return header, body


class MatrixTableTests(unittest.TestCase):
    def test_matrix_table_matches_prior_data_i23_and_i25(self):
        from research.matrix_comparison import rows as comparison_rows
        source = (ROOT / 'paper/sections/results.tex').read_text()
        header, body = parse_table(source, 'tab:matrix')
        strict = {(r['method'], r['context']): r for r in comparison_rows('i23-matrix')}
        lenient = {(r['method'], r['context']): r for r in comparison_rows('i25-lenient')}
        contexts = ['None', 'S', 'F', 'B', 'S+F', 'S+B', 'F+B', 'S+F+B']
        rows_by_label = {label: [int(v) for v in values] for label, values in body if label in contexts}
        def cell(method, context):
            s, l = strict[(method, context)], lenient[(method, context)]
            return [s['priorCompiled'], s['compiled'], s['functional'], l['compiled'], l['functional']]
        for context in contexts:
            self.assertEqual(rows_by_label[context], cell('Generation', context) + cell('Reuse', context), context)
        total = [int(v) for label, values in body if label == 'Total of 40' for v in values]
        self.assertEqual(total, [sum(cell(m, c)[k] for c in contexts) for m in ('Generation', 'Reuse') for k in range(5)])
        self.assertEqual(total[:5], [17, 18, 17, 25, 20]); self.assertEqual(total[5:], [13, 21, 19, 19, 18])


class ResultsTableTests(unittest.TestCase):
    def test_results_table_body_is_the_generated_one(self):
        from research.paper_results_table import body, rows
        source = (ROOT / 'paper/sections/results.tex').read_text()
        block = source.split('\\label{tab:results}', 1)[1].split('\\end{tabular}', 1)[0]
        printed = [line.strip() for line in block.splitlines() if line.strip().endswith('\\\\') and 'Security context' not in line]
        self.assertEqual(printed, [line.strip() for line in body().splitlines()])
        from research.paper_results_table import ACHIEVEMENTS_GROUPS
        if '\\label{tab:results-ach}' in source:
            block = source.split('\\label{tab:results-ach}', 1)[1].split('\\end{tabular}', 1)[0]
            printed = [line.strip() for line in block.splitlines() if line.strip().endswith('\\\\') and 'Security context' not in line]
            self.assertEqual(printed, [line.strip() for line in body(ACHIEVEMENTS_GROUPS).splitlines()])
        for group, items in rows():
            for r in items:
                self.assertEqual(r['n'], 10, r['label']); self.assertLessEqual(r['full'], r['compiled'], r['label'])
                for key in ('compiled', 'tests', 'checks', 'full'): self.assertTrue(r[key] is None or 0 <= r[key] <= 100, r['label'])


if __name__ == '__main__':
    unittest.main()
