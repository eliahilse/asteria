import csv
import json
import tempfile
import unittest
from pathlib import Path

from research.issue_matrix import ISSUE_CHECKS, POSITIVE_CHECK, POSITIVE_LABEL, TOTAL_LABEL, csv_rows, cwe_mapping, matrix, render, write

STATUSES = ('pass', 'fail', 'not_run', 'unknown', 'compile_error', 'infrastructure_error')


def check(name: str, attempts: int, **outcomes) -> dict:
    counts = {s: 0 for s in STATUSES} | outcomes
    counts['pass'] = attempts - sum(counts[s] for s in STATUSES if s != 'pass')
    return {'id': f'security_v1.{name}', 'suite': 'security_v1', 'name': name, 'kind': 'security', **counts,
            'attempts': attempts, 'executed': counts['pass'] + counts['fail']}


def study(n: int = 2) -> dict:
    """Two conditions: a control with failures and unknowns, and a treatment where one artifact did not compile."""
    control = [check(name, n) for name in [POSITIVE_CHECK, *ISSUE_CHECKS]]
    for c in control:
        if c['name'] in ('rejectsNullName', 'oversizedPhysicalLine'): c.update(fail=n, executed=n, **{'pass': 0})
        if c['name'] == 'largePersistedRecordSet': c.update(unknown=1, fail=1, executed=1, **{'pass': 0})
    treatment = [check(name, n, compile_error=1) for name in [POSITIVE_CHECK, *ISSUE_CHECKS]]
    # A distractor functional check must not enter the security matrix.
    functional = [{'id': 'unit.saveAddsEntry', 'suite': 'unit', 'name': 'saveAddsEntry', 'kind': 'functional', 'pass': n, 'fail': 0, 'not_run': 0, 'unknown': 0, 'compile_error': 0, 'infrastructure_error': 0, 'attempts': n, 'executed': n}]
    return {'plan': {'id': 'synthetic', 'conditions': [{'id': 'reuse_b__none'}, {'id': 'reuse_b__operations'}]},
            'summary': {'conditions': [{'id': 'reuse_b__none', 'attempts': n, 'checks': control + functional},
                                       {'id': 'reuse_b__operations', 'attempts': n, 'checks': treatment + functional}]}}


class IssueMatrixTests(unittest.TestCase):
    def test_every_cell_uses_n_and_the_total_uses_ten_n(self):
        m = matrix(study(), {'rejectsNullName': ['CWE-20', 'CWE-476']})
        rows = {r['test']: r for r in m['rows']}
        self.assertEqual([r['test'] for r in m['rows']], ['N per check', *ISSUE_CHECKS, TOTAL_LABEL, POSITIVE_LABEL])
        self.assertEqual(rows['N per check']['cells'], {'reuse_b__none': (2, 2, 2), 'reuse_b__operations': (2, 2, 2)})
        self.assertEqual(rows['rejectsNullName']['cwe'], 'CWE-20, CWE-476')
        self.assertEqual(rows['rejectsNullName']['cells']['reuse_b__none'], (2, 0, 0))
        self.assertEqual(rows['largePersistedRecordSet']['cells']['reuse_b__none'], (1, 1, 0))
        # Unresolved checks are neither passes nor failures, and every cell sums to N.
        self.assertEqual(rows['rejectsNegativeScore']['cells']['reuse_b__operations'], (0, 1, 1))
        for r in m['rows']:
            if r['kind'] == 'issue':
                for c in m['conditions']: self.assertEqual(sum(r['cells'][c]), 2)
        self.assertEqual(rows[TOTAL_LABEL]['cells'], {'reuse_b__none': (5, 1, 14), 'reuse_b__operations': (0, 10, 10)})
        for c in m['conditions']: self.assertEqual(sum(rows[TOTAL_LABEL]['cells'][c]), 10 * 2)
        self.assertEqual(rows[POSITIVE_LABEL]['cells'], {'reuse_b__none': (0, 0, 2), 'reuse_b__operations': (0, 1, 1)})

    def test_unresolved_reasons_name_each_cell_and_are_not_passes(self):
        m = matrix(study())
        unresolved = {(u['condition'], u['test']): u for u in m['unresolved']}
        self.assertEqual(len(unresolved), 1 + 11)
        large = unresolved['reuse_b__none', 'largePersistedRecordSet']
        self.assertEqual((large['unresolved'], large['unknown'], large['compile_error']), (1, 1, 0))
        self.assertIn('amplification precondition', large['meaning'])
        self.assertIn('not passes', large['meaning'])
        compile_error = unresolved['reuse_b__operations', POSITIVE_CHECK]
        self.assertEqual(compile_error['compile_error'], 1)
        self.assertIn('security-suite compilation', compile_error['meaning'])

    def test_missing_observations_stay_blank(self):
        s = study()
        s['summary']['conditions'][1]['attempts'] = 0
        m = matrix(s)
        self.assertIsNone(m['rows'][1]['cells']['reuse_b__operations'])
        self.assertIsNone({r['test']: r for r in m['rows']}[TOTAL_LABEL]['cells']['reuse_b__operations'])
        self.assertEqual(csv_rows({'studies': [s]}, {})[2][6:9], ['', '', ''])

    def test_markdown_and_csv_share_the_fixed_denominator_layout(self):
        data = {'studies': [study()]}
        text = render(data, 'synthetic-iteration', {})
        self.assertIn('unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes'.lower(), text.lower())
        self.assertIn('N per check for each condition and 10 × N', text)
        self.assertIn('| Test | CWE | reuse_b__none | reuse_b__operations |', text)
        self.assertIn('| rejectsNullName |  | 2 / 0 / 0 | 0 / 1 / 1 |', text)
        self.assertIn('| rejectsNullName | CWE-20 |', render(data, 'x', {'rejectsNullName': ['CWE-20']}))
        self.assertIn(f'| **{TOTAL_LABEL}** |  | 5 / 1 / 14 | 0 / 10 / 10 |', text)
        self.assertIn(f'| *{POSITIVE_LABEL}* |', text)
        self.assertIn('| reuse_b__none | largePersistedRecordSet | 1 | 0 | 1 | 0 | 0 |', text)
        rows = csv_rows(data, {})
        self.assertEqual(rows[0], ['study', 'test', 'cwe', 'reuse_b__none failed', 'reuse_b__none unresolved', 'reuse_b__none passed',
                                   'reuse_b__operations failed', 'reuse_b__operations unresolved', 'reuse_b__operations passed'])
        self.assertEqual(rows[1], ['synthetic', 'N per check', '', 2, 2, 2, 2, 2, 2])
        self.assertEqual(rows[-2][:2], ['synthetic', TOTAL_LABEL])
        self.assertEqual(rows[-2][3:], [5, 1, 14, 0, 10, 10])
        self.assertEqual(rows[-1][1], POSITIVE_LABEL)

    def test_cwe_mapping_accepts_both_shapes_and_is_optional(self):
        with tempfile.TemporaryDirectory() as tmp:
            path = Path(tmp) / 'cwe-mapping.json'
            self.assertEqual(cwe_mapping(path), {})
            path.write_text(json.dumps({'rejectsNullName': ['CWE-20']}))
            self.assertEqual(cwe_mapping(path), {'rejectsNullName': ['CWE-20']})
            path.write_text(json.dumps({'meaning': 'x', 'checks': {'rejectsNullName': {'category': 'input_policy', 'cwes': ['CWE-20', 'CWE-476']}}}))
            self.assertEqual(cwe_mapping(path), {'rejectsNullName': ['CWE-20', 'CWE-476']})

    def test_write_places_both_files_beside_the_qualified_results(self):
        with tempfile.TemporaryDirectory() as tmp:
            directory = Path(tmp) / 'research/iterations/synthetic'
            directory.mkdir(parents=True)
            (directory / 'qualified-results.json').write_text(json.dumps({'studies': [study()]}))
            md, out = write('synthetic', Path(tmp))
            self.assertEqual((md.name, out.name), ('issue-matrix.md', 'issue-matrix.csv'))
            self.assertIn('# Issue matrix: synthetic', md.read_text())
            with out.open(newline='') as handle: rows = list(csv.reader(handle))
            self.assertEqual(len(rows), 1 + 1 + len(ISSUE_CHECKS) + 2)
            self.assertEqual(rows[2][:2], ['synthetic', 'rejectsNegativeScore'])


if __name__ == '__main__': unittest.main()
