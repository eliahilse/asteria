"""The paper's result tables must equal the saved qualified analyses and audits."""
import csv
import json
from pathlib import Path
import re
import unittest

from research.import_evidence import ROOT

CELLS = {'Generation S': 'generation_s', 'Generation S+F+B': 'generation_sfb', 'Reuse B': 'reuse_b', 'Reuse S+B': 'reuse_sb'}
ARMS = {'None': 'none', 'Operations': 'operations', 'Requirements': 'requirements', 'Boundaries': 'boundaries', 'Task only': 'task_only', 'Catalogue': 'catalog'}
TABLES = {'tab:i07': 'i07-operational-replication', 'tab:i09': 'i09-generic-acquisition'}
ROUNDS = {'I10': 'i10-agentic-delivery', 'I16': 'i16-compact-confirmation', 'I16b': 'i16b-gen-compact', 'I17': 'i17-nofb', 'I18': 'i18-nofb-reuse', 'I19': 'i19-v9', 'I20': 'i20-v10', 'I21a': 'i21a-var', 'I21b': 'i21b-var', 'I22': 'i22-oneshot'}
MODES = {'single-shot': 'single_shot', 'agentic': 'agentic', 'one response': 'single_shot'}
HOOK_ROUNDS = ('i09-generic-acquisition', 'i10-agentic-delivery', 'i11-symbol-sidecar', 'i16-compact-confirmation', 'i16b-gen-compact')
NULL_NAME_TESTS = {'recordedSurvivalTimeIsTheRealElapsedTime', 'secondRunAlsoRecordedAndBoardSortedDescending'}
CELL_VALUE = re.compile(r'(\d+); (\d+)/(\d+)/(\d+)')


def parse_table(source: str, label: str) -> tuple[list[str], list[tuple[str, list[str]]]]:
    block = source.split(f'\\label{{{label}}}', 1)[1].split('\\end{tabular}', 1)[0]
    rows = [line.strip().rstrip('\\').strip() for line in block.splitlines() if '&' in line]
    header = [cell.strip() for cell in rows[0].split('&')][1:]
    body = [(cells[0].strip(), [c.strip() for c in cells[1:]]) for cells in (row.split('&') for row in rows[1:])]
    return header, body


def analysis(iteration: str) -> dict:
    data = json.loads((ROOT / 'research/iterations' / iteration / 'qualified-analysis.json').read_text())
    return {c['condition']: c for c in data['conditions']}


class PaperTableTests(unittest.TestCase):
    def check_cell(self, condition: dict, value: str, where: str):
        match = CELL_VALUE.fullmatch(value)
        self.assertIsNotNone(match, f'{where}: {value!r}')
        full, failed, unresolved, passed = map(int, match.groups())
        issues = condition['issues']
        self.assertEqual((full, failed, unresolved, passed), (condition['withinBudgetFull'], issues['failed'], issues['unresolved'], issues['evaluated'] - issues['failed']), where)
        self.assertEqual(failed + unresolved + passed, issues['plannedChecks'], f'{where}: denominator')
        self.assertEqual(condition['n'] * 10, issues['plannedChecks'], where)

    def test_cell_by_arm_tables_match_qualified_analysis(self):
        source = (ROOT / 'paper/sections/results.tex').read_text()
        for label, iteration in TABLES.items():
            conditions = analysis(iteration); header, body = parse_table(source, label)
            self.assertTrue(body, label)
            for cell, values in body:
                for arm, value in zip(header, values):
                    self.check_cell(conditions[f'{CELLS[cell]}__{ARMS[arm]}'], value, f'{label} {cell} {arm}')

    def test_agent_table_matches_qualified_analyses(self):
        source = (ROOT / 'paper/sections/results.tex').read_text()
        header, body = parse_table(source, 'tab:agent')
        self.assertEqual(header, ['Cell', 'Mode', 'Insert', 'None', 'Insert'])
        self.assertEqual([r[0] for r in body], ['I10', 'I10', 'I10', 'I10', 'I16', 'I16b', 'I17', 'I18', 'I19', 'I19', 'I20', 'I20', 'I21a', 'I21a', 'I21b', 'I21b', 'I22', 'I22'])
        for round_name, (cell, mode, _insert, none, static) in body:
            conditions = analysis(ROUNDS[round_name]); prefix = f'{CELLS[cell]}__{MODES[mode]}'
            self.check_cell(conditions[f'{prefix}__none'], none, f'tab:agent {round_name} {cell} {mode} none')
            self.check_cell(conditions[f'{prefix}__static'], static, f'tab:agent {round_name} {cell} {mode} static')

    def test_hook_table_matches_audits(self):
        source = (ROOT / 'paper/sections/results.tex').read_text()
        _, body = parse_table(source, 'tab:hook')
        counts = {'No security context': [0, 0], 'Researcher-written inserts': [0, 0], 'Agent-acquired context': [0, 0]}
        for iteration in HOOK_ROUNDS:
            for row in csv.DictReader((ROOT / 'research/iterations' / iteration / 'hook-audit.csv').open()):
                if row['status'] not in ('completed', 'budget_exhausted'): continue
                arm = row['condition'].split('__')[-1]
                group = 'No security context' if arm == 'none' else ('Researcher-written inserts' if iteration == 'i09-generic-acquisition' else 'Agent-acquired context')
                failing = set(row['failingChecks'].split())
                counts[group][0] += 1; counts[group][1] += bool(failing) and failing <= NULL_NAME_TESTS
        self.assertEqual({g: (int(v[1]), int(v[2])) for g, v in body}, {g: tuple(c) for g, c in counts.items()})

    def test_acquisition_sentence_matches_saved_summary(self):
        source = (ROOT / 'paper/sections/results.tex').read_text()
        rows = list(csv.DictReader((ROOT / 'research/iterations/i10-agentic-delivery/acquisitions.csv').open()))
        self.assertEqual(len(rows), 6)
        commands = sorted(int(r['commands']) for r in rows)
        kept, produced = sum(int(r['items']) for r in rows), sum(int(r['rawItems']) for r in rows)
        matched, total = sum(int(r['anchorsMatched']) for r in rows), sum(int(r['anchorsTotal']) for r in rows)
        text = ' '.join(source.split())
        self.assertIn(f'using {commands[0]} to {commands[-1]} commands', text)
        self.assertIn(f'kept {kept} of the {produced} statements produced and verified {matched} of the {total} code locations', text)


if __name__ == '__main__':
    unittest.main()
