"""The paper's result tables must equal the saved qualified analyses."""
import csv
import json
from pathlib import Path
import re
import unittest

from research.import_evidence import ROOT

CELLS = {'Generation S': 'generation_s', 'Generation S+F+B': 'generation_sfb', 'Reuse B': 'reuse_b', 'Reuse S+B': 'reuse_sb'}
ARMS = {'None': 'none', 'Operations': 'operations', 'Requirements': 'requirements', 'Boundaries': 'boundaries', 'Task only': 'task_only', 'Catalogue': 'catalog',
        'Single-shot, none': 'single_shot__none', 'Single-shot, static': 'single_shot__static', 'Agentic, none': 'agentic__none', 'Agentic, static': 'agentic__static', 'Agentic, adaptive': 'agentic__adaptive', 'Agentic, gate': 'agentic__gate', 'Agentic, gate (shadow)': 'agentic__gate', 'Agentic, coach': 'agentic__coach', 'Agentic, gate once': 'agentic__gate_once', 'Agentic, rewind': 'agentic__rewind'}
TABLES = {'tab:i07': 'i07-operational-replication', 'tab:i09': 'i09-generic-acquisition', 'tab:i10': 'i10-agentic-delivery', 'tab:i11': 'i11-symbol-sidecar', 'tab:i12': 'i12-gate-sidecar', 'tab:i13': 'i13-shadow-gate', 'tab:i14': 'i14-coach-gate', 'tab:i15': 'i15-rewind', 'tab:i16': 'i16-compact-confirmation', 'tab:i16b': 'i16b-gen-compact', 'tab:i17': 'i17-nofb'}


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
                    self.assertEqual(condition['n'] * 10, issues['plannedChecks'])

    def test_acquisition_table_matches_saved_summary(self):
        source = (ROOT / 'paper/sections/results.tex').read_text()
        rows = {(r['method'], r['angle']): r for r in csv.DictReader((ROOT / 'research/iterations/i10-agentic-delivery/acquisitions.csv').open())}
        angles = {'data flow': 'dataflow', 'requirements': 'requirements', 'catalogue': 'catalog'}
        _, body = parse_table(source, 'tab:acq')
        self.assertEqual(len(body), 6)
        for method, values in body:
            angle, commands, statements, anchors, corrected, characters = values
            row = rows[(method, angles[angle])]
            self.assertEqual(int(commands), int(row['commands']), (method, angle))
            self.assertEqual(statements, f"{row['items']}/{row['rawItems']}", (method, angle))
            self.assertEqual(anchors, f"{row['anchorsMatched']}/{row['anchorsTotal']}", (method, angle))
            self.assertEqual(int(corrected), int(row['anchorsCorrected']), (method, angle))
            self.assertEqual(int(characters), int(row['insertCharacters']), (method, angle))


if __name__ == '__main__':
    unittest.main()
