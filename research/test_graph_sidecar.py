import contextlib
import io
import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from research import agentic_delivery as agentic
from research import context_graph, graph_sidecar
from research.import_evidence import canonical, digest
from research.test_agentic_delivery import CORRECTION, Fixture, LEVEL_PATH, SCORE_PATH, act, report, valid_changes
from research.test_context_graph import DOCUMENT, MODEL, FILE, STORE, LOAD, MENU, anchor, item


class GraphSidecarTests(unittest.TestCase):
    def setUp(self):
        self.temporary = tempfile.TemporaryDirectory(); self.directory = Path(self.temporary.name)
        self.graph = context_graph.build(DOCUMENT, MODEL)
        (self.directory / 'generation-dataflow.graph.json').write_bytes(canonical(self.graph))

    def tearDown(self): self.temporary.cleanup()

    def test_compact_static_insert_and_description(self):
        sidecar = graph_sidecar.GraphSidecar(self.directory)
        text = sidecar.initial({'strategy': 'Generation'})
        self.assertIn('[R1; requirement', text); self.assertIn('[C1; control', text)
        self.assertNotIn('[X1; existing_risk', text); self.assertNotIn('Assets:', text); self.assertNotIn('[U1; unknown', text)
        self.assertEqual(text, context_graph.render(self.graph, kinds=graph_sidecar.COMPACT, header=False))
        described = sidecar.describe()
        self.assertEqual((described['granularity'], described['hops'], described['kinds'], described['header']), ('symbol', 0, ['requirement', 'control'], False))
        self.assertIn('Generation', described['graphs']); self.assertEqual(len(described['moduleSha256']), 64)

    def test_symbol_granularity_uses_edit_ranges_not_files(self):
        sidecar = graph_sidecar.GraphSidecar(self.directory)
        jar_path = 'ApoMario/ApoMario.jar!/apoMario/Store.java'
        # An edit inside store(int,String) (L10-L20) touches only R2; the whole-file touch would also bring X1/R1/C1 via load().
        text, ids = sidecar.update({'method': 'Generation', 'files': {jar_path}, 'ranges': {jar_path: [[12, 14]]}}, set())
        self.assertEqual(ids, ['item:R2']); self.assertIn('R2 statement', text)
        text, ids = sidecar.update({'method': 'Generation', 'files': {jar_path}, 'ranges': {jar_path: [[35, 36]]}}, {'item:R2'})
        self.assertEqual(set(ids), {'item:R1', 'item:C1'}); self.assertNotIn('item:X1', ids)  # compact kinds exclude the risk
        text, ids = sidecar.update({'method': 'Generation', 'files': {jar_path}, 'ranges': {}}, set())
        self.assertIsNone(text); self.assertEqual(ids, [])
        wide = graph_sidecar.GraphSidecar(self.directory, granularity='file', kinds=None, hops=1)
        text, ids = wide.update({'method': 'Generation', 'files': {jar_path}, 'ranges': {}}, set())
        self.assertEqual(set(ids), {'item:X1', 'item:R1', 'item:C1', 'item:R2'})

    def test_harness_reports_edit_and_read_ranges_and_sidecar_config(self):
        with tempfile.TemporaryDirectory() as temporary:
            fixture = Fixture(Path(temporary)); seen = []

            class Probe:
                def initial(self, condition): return None
                def describe(self): return {'module': 'probe', 'granularity': 'symbol'}
                def update(self, touched, shown_ids):
                    seen.append({'stage': touched['stage'], 'ranges': {k: list(v) for k, v in (touched.get('ranges') or {}).items()}, 'files': sorted(touched['files'])})
                    return None, []
            record, _ = fixture.run('agentic', 'adaptive', [act('read', files=[{'path': SCORE_PATH, 'start_line': 2, 'end_line': 3}]), act('submit_feature_changes', **valid_changes()), act('submit_feature_changes', **CORRECTION)], [report(False), report(True)], Probe())
            self.assertEqual(record['sidecarConfig'], {'module': 'probe', 'granularity': 'symbol'})
            after_read = next(s for s in seen if s['stage'] == 'after_read'); self.assertEqual(after_read['ranges'], {SCORE_PATH: [[2, 3]]})
            before_submit = next(s for s in seen if s['stage'] == 'before_submit')
            self.assertEqual(before_submit['ranges'].get(LEVEL_PATH), [[2, 2]]); self.assertIn(LEVEL_PATH, before_submit['files'])
            self.assertEqual(record['touchedRanges'].get(LEVEL_PATH), [[2, 2]]); self.assertEqual(record['touchedRanges'].get(SCORE_PATH), [[2, 3]])

    def test_gate_sidecar_rejects_only_judged_violations(self):
        level_file = 'ApoMario/src/apoMario/level/ApoMarioLevel.java'; symbol = 'apoMario.level.ApoMarioLevel'
        doc = {'angle': 'dataflow', 'summary': 'S', 'limitations': [], 'assets': [], 'boundaries': [],
               'items': [item('R1', 'requirement', 'task', [], enforcement_point=anchor(symbol, 1, 3, level_file)),
                         item('C1', 'control', 'reasoned', [], ['R1'], enforcement_point=anchor(symbol, 1, 3, level_file), failure_behavior='reject')]}
        model = {'symbols': [{'id': symbol, 'file': level_file, 'start': 1, 'end': 3, 'sinks': []}], 'edges': []}
        (self.directory / 'generation-dataflow.graph.json').write_bytes(canonical(context_graph.build(doc, model)))
        sidecar = graph_sidecar.GateSidecar(self.directory, command=['fixture-judge'])
        self.assertIsNone(sidecar.initial({'strategy': 'Generation'})); self.assertEqual(sidecar.update({'method': 'Generation'}, set()), (None, []))
        self.assertEqual(sidecar.describe()['mode'], 'gate')
        verdicts = iter([{'intervene': True, 'statement_ids': ['C1'], 'quoted_lines': ['int preserved; int added;'], 'reason': 'stores without validation'}, {'intervene': False, 'statement_ids': [], 'quoted_lines': [], 'reason': 'fine'}])
        judge_requests = []

        def fake_invoke(command, request, timeout):
            judge_requests.append(request)
            return {'protocol_version': 1, 'request_id': request['request_id'], 'model': request['model'], 'settings': None, 'finish_reason': 'stop', 'output_text': json.dumps(next(verdicts)), 'usage': {}, 'cost_usd': None}
        with tempfile.TemporaryDirectory() as temporary, patch.object(graph_sidecar, 'invoke', side_effect=fake_invoke):
            fixture = Fixture(Path(temporary))
            for c in fixture.conditions:
                if c['sidecar'] == 'adaptive': c['sidecar'] = 'gate'; c['id'] = c['id'].replace('adaptive', 'gate')
            for row in fixture.plan['schedule']: row['condition'] = row['condition'].replace('adaptive', 'gate'); row['runId'] = row['runId'].replace('adaptive', 'gate')
            record, requests = fixture.run('agentic', 'gate', [act('submit_feature_changes', **valid_changes()), act('submit_feature_changes', **valid_changes())], [report(True)], sidecar)
        self.assertEqual([s['status'] for s in record['submissions']], ['rejected_by_sidecar', 'evaluated']); self.assertTrue(record['functionalSuccess'])
        gate_events = [e for e in record['sidecarEvents'] if e['stage'] == 'gate']
        self.assertEqual([(e['consulted'], e['intervene'], e['ids']) for e in gate_events], [(True, True, ['item:C1']), (True, False, ['item:R1', 'item:C1'])])
        self.assertIn('SUBMISSION REJECTED BY THE SECURITY SIDECAR', record['submissions'][0]['feedback']['securityContext'])
        self.assertIn('stores without validation', record['submissions'][0]['feedback']['deliveryError'])
        self.assertEqual(judge_requests[0]['request_id'], record['runId'] + '-j1'); self.assertEqual(judge_requests[0]['tools'][0]['function']['name'], 'judge_submission')
        self.assertIn('--- edit ApoMarioLevel.java ---', judge_requests[0]['messages'][1]['content']); self.assertNotIn('functionalChecks', judge_requests[0]['messages'][1]['content'])
        self.assertEqual(gate_events[0]['transcript']['requestSha256'], digest(canonical(judge_requests[0])))
        # the rejected submission is counted, the agent saw the rejection feedback, and no evaluation happened for it
        self.assertEqual(requests[1]['messages'][-1]['content'].count('rejected by the security sidecar'), 1)
        self.assertNotIn('evaluationFile', record['submissions'][0])
        self.assertEqual(record['sidecarConfig']['mode'], 'gate'); self.assertEqual(gate_events[0]['quoted'], ['int preserved; int added;']); self.assertTrue(gate_events[0]['wouldIntervene'])
        # a verdict without a verbatim quote does not intervene; shadow mode records the would-be verdict and never rejects
        unquoted = iter([{'intervene': True, 'statement_ids': ['C1'], 'quoted_lines': ['not in the submission'], 'reason': 'x'}])
        shadow = graph_sidecar.GateSidecar(self.directory, command=['fixture-judge'], shadow=True); quoted = iter([{'intervene': True, 'statement_ids': ['C1'], 'quoted_lines': ['int preserved; int added;'], 'reason': 'y'}])
        for sidecar_case, script, expect_would in ((graph_sidecar.GateSidecar(self.directory, command=['fixture-judge']), unquoted, False), (shadow, quoted, True)):
            def fake(command, request, timeout, script=script):
                return {'protocol_version': 1, 'request_id': request['request_id'], 'model': request['model'], 'settings': None, 'finish_reason': 'stop', 'output_text': json.dumps(next(script)), 'usage': {}, 'cost_usd': None}
            with tempfile.TemporaryDirectory() as temporary, patch.object(graph_sidecar, 'invoke', side_effect=fake):
                fixture = Fixture(Path(temporary))
                for c in fixture.conditions:
                    if c['sidecar'] == 'adaptive': c['sidecar'] = 'gate'; c['id'] = c['id'].replace('adaptive', 'gate')
                for row in fixture.plan['schedule']: row['condition'] = row['condition'].replace('adaptive', 'gate'); row['runId'] = row['runId'].replace('adaptive', 'gate')
                record2, _ = fixture.run('agentic', 'gate', [act('submit_feature_changes', **valid_changes())], [report(True)], sidecar_case)
            event = [e for e in record2['sidecarEvents'] if e['stage'] == 'gate'][0]
            self.assertEqual((event['intervene'], event['wouldIntervene'], event['verdictIntervene']), (False, expect_would, True)); self.assertEqual(record2['submissions'][0]['status'], 'evaluated')
        self.assertEqual(shadow.describe()['mode'], 'shadow-gate')

    def test_coach_and_gate_once_policies(self):
        level_file = 'ApoMario/src/apoMario/level/ApoMarioLevel.java'; symbol = 'apoMario.level.ApoMarioLevel'
        doc = {'angle': 'dataflow', 'summary': 'S', 'limitations': [], 'assets': [], 'boundaries': [],
               'items': [item('R1', 'requirement', 'task', [], enforcement_point=anchor(symbol, 1, 3, level_file)),
                         item('C1', 'control', 'reasoned', [], ['R1'], enforcement_point=anchor(symbol, 1, 3, level_file), failure_behavior='reject')]}
        model = {'symbols': [{'id': symbol, 'file': level_file, 'start': 1, 'end': 3, 'sinks': []}], 'edges': []}
        (self.directory / 'generation-dataflow.graph.json').write_bytes(canonical(context_graph.build(doc, model)))
        positive = {'intervene': True, 'statement_ids': ['C1'], 'quoted_lines': ['int preserved; int added;'], 'reason': 'violation'}

        def run_kind(kind, sidecar, actions, reports):
            script = iter([positive] * len(actions))
            def fake(command, request, timeout):
                return {'protocol_version': 1, 'request_id': request['request_id'], 'model': request['model'], 'settings': None, 'finish_reason': 'stop', 'output_text': json.dumps(next(script)), 'usage': {}, 'cost_usd': None}
            with tempfile.TemporaryDirectory() as temporary, patch.object(graph_sidecar, 'invoke', side_effect=fake):
                fixture = Fixture(Path(temporary))
                for c in fixture.conditions:
                    if c['sidecar'] == 'adaptive': c['sidecar'] = kind; c['id'] = c['id'].replace('adaptive', kind)
                for row in fixture.plan['schedule']: row['condition'] = row['condition'].replace('adaptive', kind); row['runId'] = row['runId'].replace('adaptive', kind)
                return fixture.run('agentic', kind, actions, reports, sidecar)
        record, requests = run_kind('coach', graph_sidecar.GateSidecar(self.directory, command=['j'], policy='auto'), [act('submit_feature_changes', **valid_changes()), act('submit_feature_changes', **CORRECTION)], [report(False), report(True)])
        self.assertEqual([s['status'] for s in record['submissions']], ['evaluated', 'evaluated'])
        first = [e for e in record['sidecarEvents'] if e['stage'] == 'gate'][0]
        self.assertEqual((first['wouldIntervene'], first['intervene'], first['advice'], first['injected']), (True, False, True, True))
        self.assertIn('SECURITY CONTEXT FOR THIS SUBMISSION', requests[1]['messages'][-1]['content']); self.assertIn('"functionalSuccess": false', requests[1]['messages'][-1]['content'])
        self.assertEqual(record['sidecarConfig']['policy'], 'auto')
        record, requests = run_kind('gate_once', graph_sidecar.GateSidecar(self.directory, command=['j'], policy='auto'), [act('submit_feature_changes', **valid_changes()), act('submit_feature_changes', **valid_changes()), act('submit_feature_changes', **CORRECTION)], [report(False), report(True)])
        self.assertEqual([s['status'] for s in record['submissions']], ['rejected_by_sidecar', 'evaluated', 'evaluated'])
        events = [e for e in record['sidecarEvents'] if e['stage'] == 'gate']
        self.assertEqual([(e['intervene'], e['advice'], e['consulted']) for e in events], [(True, False, True), (False, True, True), (False, False, False)])  # the correction touches no enforcement point
        self.assertIn('SUBMISSION REJECTED', requests[1]['messages'][-1]['content']); self.assertIn('SECURITY CONTEXT FOR THIS SUBMISSION', requests[2]['messages'][-1]['content'])
        self.assertNotIn('SECURITY CONTEXT FOR THIS SUBMISSION', requests[2]['messages'][-1]['content'].split('SECURITY CONTEXT FOR THIS SUBMISSION', 1)[1])


if __name__ == '__main__':
    unittest.main()
