"""The paper's result tables must equal the saved qualified analyses."""
import json
from pathlib import Path
import re
import unittest

from research.import_evidence import ROOT

CELLS = {'Generation S': 'generation_s', 'Generation S+F+B': 'generation_sfb', 'Reuse B': 'reuse_b', 'Reuse S+B': 'reuse_sb'}
ARMS = {'None': 'none', 'Operations': 'operations', 'Requirements': 'requirements', 'Boundaries': 'boundaries', 'Task only': 'task_only', 'Catalogue': 'catalog'}
TABLES = {'tab:i07': 'i07-operational-replication', 'tab:i09': 'i09-generic-acquisition'}


def parse_table(source: str, label: str) -> tuple[list[str], list[tuple[str, list[str]]]]:
    block = source.split(f'\\label{{{label}}}', 1)[1].split('\\end{tabular}', 1)[0]
    rows = [line.strip().rstrip('\\').strip() for line in block.splitlines() if '&' in line]
    header = [cell.strip() for cell in rows[0].split('&')][1:]
    body = [(cells[0].strip(), [c.strip() for c in cells[1:]]) for cells in (row.split('&') for row in rows[1:])]
    return header, body


class PaperTableTests(unittest.TestCase):
    def test_result_tables_match_qualified_analysis(self):
        source = (ROOT / 'paper/sections/results.tex').read_text()
        for label, iteration in TABLES.items():
            analysis = json.loads((ROOT / 'research/iterations' / iteration / 'qualified-analysis.json').read_text())
            conditions = {c['condition']: c for c in analysis['conditions']}
            header, body = parse_table(source, label)
            self.assertTrue(body, label)
            for cell, values in body:
                for arm, value in zip(header, values):
                    condition = conditions[f'{CELLS[cell]}__{ARMS[arm]}']
                    match = re.fullmatch(r'(\d+); (\d+)/(\d+)/(\d+)', value)
                    self.assertIsNotNone(match, f'{label} {cell} {arm}: {value!r}')
                    full, failed, unresolved, passed = map(int, match.groups())
                    issues = condition['issues']
                    expected = (condition['withinBudgetFull'], issues['failed'], issues['unresolved'], issues['evaluated'] - issues['failed'])
                    self.assertEqual((full, failed, unresolved, passed), expected, f'{label} {cell} {arm}')
                    self.assertEqual(failed + unresolved + passed, issues['plannedChecks'], f'{label} {cell} {arm}: denominator')
                    self.assertEqual(condition['n'], 5)


if __name__ == '__main__':
    unittest.main()
