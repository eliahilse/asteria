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

class StrategyTableTests(unittest.TestCase):
    ROUNDS = {'S1 high-level (I24a)': 'i24a-high', 'S2 full document (I24b)': 'i24b-full', 'S3 generic (I24c)': 'i24c-generic'}

    def test_strategy_totals_match_qualified_analyses(self):
        source = (ROOT / 'paper/sections/results.tex').read_text()
        header, body = parse_table(source, 'tab:strategies')
        self.assertEqual(header, ['Arm', 'Compiled', 'Functional', 'Checks f / u / p'])
        for label, (arm, compiled, functional, checks) in body:
            conditions = [c for c in analysis(self.ROUNDS[label]).values() if c['condition'].endswith('__' + {'control': 'none', 'insert': 'static'}[arm])]
            self.assertEqual(len(conditions), 5, label)
            failed, unresolved, passed = map(int, checks.split('/'))
            self.assertEqual((int(compiled), int(functional), failed, unresolved, passed),
                             (sum(c['compiled'] for c in conditions), sum(c['withinBudgetFull'] for c in conditions), sum(c['issues']['failed'] for c in conditions),
                              sum(c['issues']['unresolved'] for c in conditions), sum(c['issues']['evaluated'] - c['issues']['failed'] for c in conditions)), f'{label} {arm}')
            self.assertEqual(failed + unresolved + passed, 250, label)


class GraphTableTests(unittest.TestCase):
    ARMS = {'none': 'none', 'insert': 'static', 'graph': 'ast', 'insert + graph': 'static-ast', 'insert + guard': 'static-guard', 'insert + graph + guard': 'static-ast-guard'}

    def test_graph_table_matches_qualified_analysis_and_full_hits(self):
        import csv
        source = (ROOT / 'paper/sections/results.tex').read_text()
        header, body = parse_table(source, 'tab:graph')
        self.assertEqual(header, ['Functional', 'Full hits', 'Checks f / u / p', 'Turns', 'Guard', 'Calls', 'Input (M tokens)'])
        conditions = analysis('i26-graph')
        hits = {r['arm']: r for r in csv.DictReader((ROOT / 'research/iterations/i26-graph/full-hits.csv').open())}
        cost = {r['arm']: r for r in csv.DictReader((ROOT / 'research/iterations/i26-graph/cost.csv').open())}
        for label, (functional, full, checks, turns, guard, calls, tokens) in body:
            arm = f'generation_s__agentic__{self.ARMS[label]}'; c = conditions[arm]; h = hits[arm]; k = cost[arm]
            failed, unresolved, passed = map(int, checks.split('/'))
            self.assertEqual((int(functional), int(full), failed, unresolved, passed), (c['withinBudgetFull'], int(h['fullHits']), c['issues']['failed'], c['issues']['unresolved'], c['issues']['evaluated'] - c['issues']['failed']), label)
            self.assertEqual(float(turns), float(h['toolTurnsMedian']), label)
            self.assertEqual(guard, f"{h['guardConsultations']} / {h['guardInterventions']}" if self.ARMS[label].endswith('guard') else '--', label)
            self.assertEqual((int(calls), tokens), (int(k['calls']), f"{int(k['inputTokens'])/1e6:.2f}"), label)


class CostTableTests(unittest.TestCase):
    ROUNDS = {'S1 high-level': 'i24a-high', 'S2 full document': 'i24b-full', 'S3 generic': 'i24c-generic'}

    def test_cost_table_matches_cost_summaries(self):
        import csv, glob
        source = (ROOT / 'paper/sections/results.tex').read_text()
        header, body = parse_table(source, 'tab:cost')
        self.assertEqual(header, ['What', 'Calls or commands', 'Input (k)', 'Output (k)', 'Reasoning (k)', 'Minutes'])
        acquisitions = {r['record']: r for r in csv.DictReader((ROOT / 'research/iterations/cost-acquisitions.csv').open())}
        arms = list(csv.DictReader((ROOT / 'research/iterations/cost-summary.csv').open()))
        for label, (what, calls, inp, out, reasoning, minutes) in body:
            rnd = self.ROUNDS[label]
            if what.endswith('acquisition'):
                method = what.split()[0].lower(); path = [f for f in glob.glob(str(ROOT / f'research/iterations/{rnd}/contexts/{method}-*record-id.txt')) if 'code-graph' not in f][0]
                a = acquisitions[Path(path).read_text().strip()]
                self.assertEqual((calls, inp, out, reasoning, minutes), (a['commands'], f"{int(a['inputTokens'])/1e3:.0f}", f"{int(a['outputTokens'])/1e3:.1f}", f"{int(a['reasoningTokens'])/1e3:.1f}", f"{float(a['minutes']):.1f}"), f'{label} {what}')
            else:
                arm = 'none' if what.startswith('control') else 'static'
                sub = [r for r in arms if r['round'] == rnd and r['arm'].endswith('__' + arm)]
                self.assertEqual(len(sub), 5, f'{label} {what}')
                total = {k: sum(float(r[k]) for r in sub) for k in ('calls', 'inputTokens', 'outputTokens', 'reasoningTokens', 'callSeconds')}
                self.assertEqual((int(calls), inp, out, reasoning, minutes), (int(total['calls']), f"{total['inputTokens']/1e3:.0f}", f"{total['outputTokens']/1e3:.1f}", f"{total['reasoningTokens']/1e3:.1f}", f"{total['callSeconds']/60:.1f}"), f'{label} {what}')


class I28TableTests(unittest.TestCase):
    CELLS = {'Generation S': 'generation_s', 'Reuse S+B': 'reuse_sb'}
    ARMS = {'none': 'none', 'insert': 'static', 'insert + graph': 'static-ast', '+ guard': 'static-ast-guard', '+ advisory': 'static-ast-advise'}

    def test_i28_table_matches_qualified_analysis_full_hits_and_cost(self):
        import csv
        source = (ROOT / 'paper/sections/results.tex').read_text()
        header, body = parse_table(source, 'tab:i28')
        self.assertEqual(header, ['Arm', 'Functional', 'Policy clean', 'Checks f / u / p', 'Turns', 'Submissions', 'Guard', 'Calls', 'Input (M)'])
        conditions = analysis('i28-graph')
        hits = {r['arm']: r for r in csv.DictReader((ROOT / 'research/iterations/i28-graph/full-hits.csv').open())}
        cost = {r['arm']: r for r in csv.DictReader((ROOT / 'research/iterations/i28-graph/cost.csv').open())}
        self.assertEqual(len(body), 10)
        for cell, (label, functional, clean, checks, turns, submissions, guard, calls, tokens) in body:
            kind = self.ARMS[label]; arm = f'{self.CELLS[cell]}__agentic__{kind}'; c = conditions[arm]; h = hits[arm]; k = cost[arm]
            failed, unresolved, passed = map(int, checks.split('/'))
            self.assertEqual((int(functional), int(clean), failed, unresolved, passed), (c['withinBudgetFull'], int(h['inputPolicyClean']), c['issues']['failed'], c['issues']['unresolved'], c['issues']['evaluated'] - c['issues']['failed']), arm)
            self.assertEqual(failed + unresolved + passed, 50, arm)
            self.assertEqual((float(turns), int(submissions)), (float(h['toolTurnsMedian']), int(h['submissions'])), arm)
            expected = f"{h['guardConsultations']} / {h['guardInterventions']}" if kind.endswith('guard') else (f"{h['guardConsultations']} / adv" if kind.endswith('advise') else '--')
            self.assertEqual(guard, expected, arm)
            self.assertEqual((int(calls), tokens), (int(k['calls']), f"{int(k['inputTokens'])/1e6:.1f}"), arm)


class I29TableTests(unittest.TestCase):
    CELLS = {'Generation S': 'generation_s', 'Reuse S+B': 'reuse_sb'}
    ARMS = {'none': 'none', 'v11 document': 'static', 'v11 document + guard': 'static-guard'}

    def test_i29_table_matches_qualified_analysis_full_hits_and_cost(self):
        import csv
        source = (ROOT / 'paper/sections/results.tex').read_text()
        header, body = parse_table(source, 'tab:i29')
        self.assertEqual(header, ['Arm', 'Functional', 'Full hits', 'Checks f / u / p', 'Turns', 'Guard', 'Calls', 'Input (M)'])
        conditions = analysis('i29-v11')
        hits = {r['arm']: r for r in csv.DictReader((ROOT / 'research/iterations/i29-v11/full-hits.csv').open())}
        cost = {r['arm']: r for r in csv.DictReader((ROOT / 'research/iterations/i29-v11/cost.csv').open())}
        self.assertEqual(len(body), 6)
        for cell, (label, functional, full, checks, turns, guard, calls, tokens) in body:
            kind = self.ARMS[label]; arm = f'{self.CELLS[cell]}__agentic__{kind}'; c = conditions[arm]; h = hits[arm]; k = cost[arm]
            failed, unresolved, passed = map(int, checks.split('/'))
            self.assertEqual((int(functional), int(full), failed, unresolved, passed), (c['withinBudgetFull'], int(h['fullHits']), c['issues']['failed'], c['issues']['unresolved'], c['issues']['evaluated'] - c['issues']['failed']), arm)
            self.assertEqual(failed + unresolved + passed, 50, arm)
            self.assertEqual(float(turns), float(h['toolTurnsMedian']), arm)
            self.assertEqual(guard, f"{h['guardConsultations']} / {h['guardInterventions']}" if kind.endswith('guard') else '--', arm)
            self.assertEqual((int(calls), tokens), (int(k['calls']), f"{int(k['inputTokens'])/1e6:.1f}"), arm)


if __name__ == '__main__':
    unittest.main()
