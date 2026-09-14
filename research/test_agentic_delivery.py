import contextlib
import io
import json
from pathlib import Path
import sys
import tempfile
import types
import unittest
from unittest.mock import patch

from research import agentic_delivery as agentic
from research import iteration_runner as delivery
from research.context_repository import snapshot
from research.feature_delivery import MODEL, SETTINGS, TARGETS, TOOL
from research.import_evidence import canonical, digest

PACKAGES = {'ApoMarioLevel.java': 'apoMario/level', 'ApoMarioMenu.java': 'apoMario/game/panels', 'ApoMarioPanel.java': 'apoMario/game'}
SOURCES = {name: f'class {name[:-5]} {{\n  int preserved;\n}}\n' for name in TARGETS}
ORIGINS = {name: {'path': 'fixture', 'member': None, 'sha256': digest(text.encode()), 'normalizedSha256': digest(text.encode())} for name, text in SOURCES.items()}
LEVEL_PATH, SCORE_PATH = 'ApoMario/src/apoMario/level/ApoMarioLevel.java', 'ApoMario/src/apoMario/game/panels/Score.java'
SCORE = 'class Score {\n  String name;\n  int points;\n}\n'
PROMPT = 'Fixture task\n\n--- BEGIN ATTACHED TARGET SOURCE: ApoMarioLevel.java ---\n' + SOURCES['ApoMarioLevel.java'] + '--- END ATTACHED TARGET SOURCE: ApoMarioLevel.java ---\n'
INSERT = '--- BEGIN REPOSITORY-DERIVED SECURITY CONTEXT ---\nSTATIC INSERT\n--- END REPOSITORY-DERIVED SECURITY CONTEXT ---\n'
NULLS = {'query': None, 'paths': None, 'files': None, 'new_files': None, 'edits': None}
CORRECTION = {'new_files': [], 'edits': [{'filename': 'Scores.java', 'old_text': 'int value;', 'new_text': 'int corrected;'}]}


def synthetic_repository(root: Path) -> Path:
    repo = root / 'repo'
    for name, text in SOURCES.items():
        target = repo / 'ApoMario/src' / PACKAGES[name] / name; target.parent.mkdir(parents=True, exist_ok=True); target.write_text(text)
    (repo / SCORE_PATH).write_text(SCORE)
    return repo


def valid_changes():
    return {'new_files': [{'filename': 'Scores.java', 'content': 'class Scores { int value; }'}],
            'edits': [{'filename': name, 'old_text': 'int preserved;', 'new_text': 'int preserved; int added;'} for name in TARGETS]}


def act(action, **fields): return {'action': action, **NULLS, **fields}


def report(success):
    return {'status': 'evaluated', 'mainCompilation': 'pass', 'functionalSuccess': success, 'processes': [],
            'checks': [{'suite': 'invoked', 'name': 'functional-fixture', 'status': 'pass' if success else 'fail'},
                       {'suite': 'security', 'name': 'secret', 'status': 'fail', 'detail': 'SECURITY_FEEDBACK_MUST_STAY_PRIVATE'}]}


class FakeSidecar:
    def __init__(self, honor_shown=True, insert=INSERT):
        self.calls, self.honor, self.insert = [], honor_shown, insert

    def initial(self, condition): return self.insert

    def update(self, touched, shown_ids):
        self.calls.append((touched, set(shown_ids)))
        ids = []
        if touched['queries']: ids.append('ctx-query')
        if any(p.endswith('Score.java') for p in touched['files']): ids.append('ctx-score')
        if any(p.endswith('ApoMarioLevel.java') for p in touched['files']): ids.append('ctx-level')
        if self.honor: ids = [i for i in ids if i not in shown_ids]
        return (f'SECURITY CONTEXT {" ".join(ids)}', ids) if ids else (None, [])


class Fixture:
    """A manifest directory with all six arms of one cell, a scripted adapter and fake evaluation."""

    def __init__(self, root: Path, *, max_turns=24, max_submissions=5, repetitions=1):
        self.root, self.repo = root, synthetic_repository(root)
        self.snap = snapshot(self.repo)
        index = agentic.repository_index(self.snap)
        (root / 'repository').mkdir(); (root / 'repository/generation-index.txt').write_text(index); (root / 'prompts').mkdir()
        self.conditions = []
        for arm in agentic.DEFAULT_ARMS:
            text = PROMPT + ('\n\n' + INSERT if arm['sidecar'] == 'static' else '')
            sha = digest(text.encode()); (root / 'prompts' / f'{sha}.txt').write_text(text)
            self.conditions.append({'id': f"generation_s__{arm['mode']}__{arm['sidecar']}", 'parentCondition': 'generation_s', 'strategy': 'Generation', 'repository': 'Generation', **arm,
                'promptFile': f'prompts/{sha}.txt', 'promptSha256': sha, 'snapshotFingerprint': self.snap['fingerprint'], 'repositoryIndexFile': 'repository/generation-index.txt',
                'repositoryIndexSha256': digest(index.encode()), 'contextInsertSha256': digest(INSERT.encode()) if arm['sidecar'] == 'static' else None})
        self.plan = {'id': 'fixture', 'protocol': agentic.PROTOCOL, 'fingerprint': 'fixture', 'model': MODEL, 'settings': SETTINGS, 'system': agentic.delivery_system(max_submissions),
                     'systemAgentic': agentic.agentic_system(max_turns, max_submissions), 'maxTurns': max_turns, 'maxSubmissions': max_submissions,
                     'parentIteration': {'id': 'fixture-parent'}, 'conditions': self.conditions,
                     'schedule': [{'runId': f"fixture__{c['id']}__r{r}", 'condition': c['id'], 'repetition': r} for r in range(1, repetitions + 1) for c in self.conditions]}
        (root / 'manifest.json').write_text(json.dumps(self.plan))

    def run(self, mode, sidecar_kind, actions, reports=(), sidecar=None, repetition=1):
        run_id = f'fixture__generation_s__{mode}__{sidecar_kind}__r{repetition}'
        actions, requests = iter(actions), []

        def invoke(command, request, timeout):
            requests.append(request); action = next(actions)
            if callable(action): return action(request)
            return {'protocol_version': 1, 'request_id': request['request_id'], 'model': request['model'], 'settings': None, 'finish_reason': 'stop',
                    'output_text': json.dumps(action), 'usage': {}, 'cost_usd': None}
        with patch.object(agentic, 'validate', return_value=self.plan), patch.object(agentic, 'repository_snapshot', return_value=self.snap), \
             patch.object(agentic, 'original_sources', return_value=(dict(SOURCES), ORIGINS)), patch('research.agentic_delivery.invoke', side_effect=invoke), \
             patch.object(agentic, 'evaluate_response', side_effect=list(reports)), contextlib.redirect_stdout(io.StringIO()):
            returned = agentic.trajectory(self.root / 'manifest.json', run_id, sidecar, ['fixture-adapter'])
        record = json.loads((self.root / 'runs' / run_id / 'record.json').read_text())
        assert record == json.loads(json.dumps(returned))
        return record, requests


class AgenticDeliveryTests(unittest.TestCase):
    def test_act_tool_is_strict_and_reuses_the_delivery_change_shapes(self):
        parameters = agentic.ACT['function']['parameters']
        self.assertEqual(agentic.ACT['function']['name'], 'act'); self.assertTrue(agentic.ACT['function']['strict'])
        self.assertEqual(parameters['required'], ['action', 'query', 'paths', 'files', 'new_files', 'edits']); self.assertFalse(parameters['additionalProperties'])
        self.assertEqual(parameters['properties']['action']['enum'], ['search', 'read', 'submit_feature_changes'])
        for key in ('new_files', 'edits'):
            self.assertEqual(parameters['properties'][key]['type'], ['array', 'null'])
            self.assertEqual(parameters['properties'][key]['items'], TOOL['function']['parameters']['properties'][key]['items'])
        self.assertEqual(TOOL['function']['name'], 'submit_feature_changes')

    def test_single_shot_requests_match_the_reference_runner_and_never_offer_repository_actions(self):
        with tempfile.TemporaryDirectory() as temporary:
            fixture = Fixture(Path(temporary))
            record, requests = fixture.run('single_shot', 'none', [valid_changes(), CORRECTION], [report(False), report(True)])
            run_id = 'fixture__generation_s__single_shot__none__r1'
            expected = {'protocol_version': 1, 'request_id': run_id + '-s1', 'model': MODEL, 'settings': SETTINGS,
                        'messages': [{'role': 'system', 'content': fixture.plan['system']}, {'role': 'user', 'content': PROMPT}], 'tools': [TOOL],
                        'tool_choice': {'type': 'function', 'function': {'name': 'submit_feature_changes'}}, 'parallel_tool_calls': False}
            self.assertEqual(requests[0], expected)
            self.assertEqual([r['tools'] for r in requests], [[TOOL], [TOOL]])
            self.assertEqual([r['request_id'] for r in requests], [run_id + '-s1', run_id + '-s2'])
            self.assertNotIn('REPOSITORY FILE INDEX', requests[0]['messages'][1]['content'])
            feedback = {'compilation': 'pass', 'functionalSuccess': False, 'functionalChecks': [{'suite': 'invoked', 'name': 'functional-fixture', 'status': 'fail'}], 'compilerErrors': []}
            self.assertEqual(requests[1]['messages'][-1]['content'], json.dumps(feedback) + '\n4 submissions remain. Correct the current source using exact edits.')
            self.assertEqual(record['submissions'][0]['feedback'], feedback)
            self.assertIn('functional-fixture', json.dumps(feedback)); self.assertNotIn('SECURITY_FEEDBACK_MUST_STAY_PRIVATE', json.dumps(requests))
            self.assertEqual((record['status'], record['mode'], record['sidecar'], record['functionalSuccess']), ('completed', 'single_shot', 'none', True))
            self.assertEqual([s['functionalSuccess'] for s in record['submissions']], [False, True])
            self.assertEqual([s['turn'] for s in record['submissions']], [1, 2])
            self.assertEqual(len(record['turns']), 2)
            for turn, submission in zip(record['turns'], record['submissions']):
                self.assertEqual(turn['requestSha256'], digest(canonical(turn['request']))); self.assertEqual(submission['request'], turn['request'])
                self.assertEqual(submission['responseSha256'], digest(canonical(submission['response']))); self.assertEqual(turn['action'], 'submit_feature_changes')
            self.assertEqual((record['filesRead'], record['searches'], record['sidecarEvents'], record['settingsVerified']), ([], [], [], False))
            self.assertEqual(record['touchedFiles'], sorted([LEVEL_PATH, 'ApoMario/src/apoMario/game/ApoMarioPanel.java', 'ApoMario/src/apoMario/game/panels/ApoMarioMenu.java', 'Scores.java']))
            self.assertTrue((fixture.root / 'runs' / run_id / 'submission-1/complete-files.txt').exists())
            self.assertEqual(record['finalEvaluation'], 'submission-2/evaluation/report.json')

    def test_agentic_search_read_submit_with_adaptive_injections_after_reads_and_before_submissions(self):
        with tempfile.TemporaryDirectory() as temporary:
            fixture, sidecar = Fixture(Path(temporary)), FakeSidecar()
            actions = [act('search', query='String name'), act('read', files=[{'path': SCORE_PATH, 'start_line': 1, 'end_line': 3}]),
                       act('submit_feature_changes', **valid_changes()), act('submit_feature_changes', **CORRECTION)]
            record, requests = fixture.run('agentic', 'adaptive', actions, [report(False), report(True)], sidecar)
            run_id = 'fixture__generation_s__agentic__adaptive__r1'
            self.assertEqual([r['request_id'] for r in requests], [f'{run_id}-t{n}' for n in range(1, 5)])
            self.assertTrue(all(r['tools'] == [agentic.ACT] and r['tool_choice']['function']['name'] == 'act' for r in requests))
            first = requests[0]['messages']
            self.assertEqual(first[0]['content'], fixture.plan['systemAgentic']); self.assertIn('call act exactly once per turn', first[0]['content'])
            self.assertTrue(first[1]['content'].startswith(PROMPT)); self.assertIn('REPOSITORY FILE INDEX', first[1]['content']); self.assertIn(SCORE_PATH, first[1]['content'])
            self.assertNotIn('SECURITY CONTEXT', first[1]['content']); self.assertEqual(len(first), 2)
            search = requests[1]['messages'][-1]['content']
            self.assertIn('"evidence_id": "E0001"', search); self.assertIn('"totalMatches": 1', search); self.assertIn('"2:   String name;"', search)
            self.assertIn('\n23 tool turns and 5 submissions remain.', search); self.assertTrue(search.endswith('\n\nSECURITY CONTEXT ctx-query'))
            read = requests[2]['messages'][-1]['content']
            self.assertIn('"evidence_id": "E0002"', read); self.assertIn('1: class Score {\\n2:   String name;\\n3:   int points;', read)
            self.assertTrue(read.endswith('\n\nSECURITY CONTEXT ctx-score'))
            self.assertTrue(requests[3]['messages'][-2]['content'].endswith('\n4 submissions remain. Correct the current source using exact edits. 21 tool turns remain.'))
            self.assertEqual(requests[3]['messages'][-1], {'role': 'user', 'content': 'SECURITY CONTEXT ctx-level'})
            events = record['sidecarEvents']
            self.assertEqual([(e['turn'], e['stage'], e['ids'], e['injected']) for e in events], [(1, 'after_read', ['ctx-query'], True), (2, 'after_read', ['ctx-score'], True), (3, 'before_submit', ['ctx-level'], True)])
            self.assertTrue(all(e['sha256'] == digest(('SECURITY CONTEXT ' + e['ids'][0]).encode()) and e['characters'] > 0 for e in events))
            injected = [i for e in events for i in e['ids']]; self.assertEqual(len(injected), len(set(injected)))
            self.assertEqual(sidecar.calls[-1][0]['stage'], 'before_submit'); self.assertEqual(sidecar.calls[-1][1], {'ctx-query', 'ctx-score'})
            self.assertEqual(sidecar.calls[1][0]['files'], {SCORE_PATH}); self.assertEqual(sidecar.calls[0][0]['queries'], ['String name'])
            self.assertEqual(record['filesRead'], [{'turn': 2, 'path': SCORE_PATH, 'start_line': 1, 'end_line': 3, 'evidence_id': 'E0002'}])
            self.assertEqual(record['searches'], [{'turn': 1, 'query': 'String name', 'paths': None, 'totalMatches': 1, 'returned': 1}])
            self.assertEqual([t['action'] for t in record['turns']], ['search', 'read', 'submit_feature_changes', 'submit_feature_changes'])
            self.assertEqual([s['turn'] for s in record['submissions']], [3, 4]); self.assertEqual(record['submissions'][1]['request'], record['turns'][3]['request'])
            self.assertEqual((record['status'], record['functionalSuccess'], record['sidecarInjections'], record['toolTurns']), ('completed', True, 3, 4))
            self.assertIn(LEVEL_PATH, record['touchedFiles']); self.assertIn(SCORE_PATH, record['touchedFiles']); self.assertIn('Scores.java', record['touchedFiles'])
            self.assertEqual(set(record['evidenceIndex']), {'E0001', 'E0002'})

    def test_repeated_statement_ids_are_never_injected_twice(self):
        with tempfile.TemporaryDirectory() as temporary:
            fixture, sidecar = Fixture(Path(temporary)), FakeSidecar(honor_shown=False)
            actions = [act('read', files=[{'path': SCORE_PATH, 'start_line': 1, 'end_line': 3}]), act('read', files=[{'path': SCORE_PATH, 'start_line': 2, 'end_line': 3}]),
                       act('submit_feature_changes', **valid_changes())]
            record, requests = fixture.run('agentic', 'adaptive', actions, [report(True)], sidecar)
            self.assertEqual([(e['stage'], e['ids'], e['injected'], e.get('reason')) for e in record['sidecarEvents']],
                             [('after_read', ['ctx-score'], True, None), ('after_read', ['ctx-score'], False, 'repeated_ids')])
            self.assertTrue(requests[1]['messages'][-1]['content'].endswith('SECURITY CONTEXT ctx-score'))
            self.assertNotIn('SECURITY CONTEXT', requests[2]['messages'][-1]['content'])
            self.assertEqual(record['sidecarInjections'], 1); self.assertEqual(record['status'], 'completed')

    def test_static_condition_has_the_insert_in_the_first_message_and_no_injections(self):
        with tempfile.TemporaryDirectory() as temporary:
            fixture, sidecar = Fixture(Path(temporary)), FakeSidecar()
            record, requests = fixture.run('single_shot', 'static', [valid_changes()], [report(True)], sidecar)
            self.assertTrue(requests[0]['messages'][1]['content'].endswith('\n\n' + INSERT)); self.assertEqual(len(requests[0]['messages']), 2)
            self.assertEqual((record['sidecarEvents'], sidecar.calls, record['staticInsert']['sidecarConfirmed'], record['status']), ([], [], True, 'completed'))
            record, requests = fixture.run('agentic', 'static', [act('submit_feature_changes', **valid_changes())], [report(True)], sidecar)
            self.assertIn('\n\n' + INSERT + '\n\nREPOSITORY FILE INDEX\n', requests[0]['messages'][1]['content']); self.assertEqual(record['sidecarEvents'], [])
        with tempfile.TemporaryDirectory() as temporary:
            fixture = Fixture(Path(temporary))
            record, requests = fixture.run('single_shot', 'static', [valid_changes()], [report(True)], FakeSidecar(insert='a different insert'))
            self.assertEqual((record['status'], requests, record['turns']), ('sidecar_error', [], [])); self.assertIn('differs', record['errorDetail'])
            with self.assertRaisesRegex(ValueError, 'sidecar object'): fixture.run('agentic', 'adaptive', [], [], None)

    def test_turn_budget_stops_the_loop(self):
        with tempfile.TemporaryDirectory() as temporary:
            fixture = Fixture(Path(temporary), max_turns=3)
            record, requests = fixture.run('agentic', 'none', [act('search', query='preserved')] * 3, [])
            self.assertEqual((record['status'], record['budgetLimit'], len(record['turns']), record['submissions'], len(record['searches'])), ('budget_exhausted', 'turns', 3, [], 3))
            self.assertTrue(requests[2]['messages'][-1]['content'].endswith('\n1 tool turns and 5 submissions remain.'))
            self.assertEqual(len(requests), 3); self.assertEqual(record['toolTurns'], 3)

    def test_submission_budget_stops_the_loop_in_both_modes(self):
        with tempfile.TemporaryDirectory() as temporary:
            fixture = Fixture(Path(temporary), max_submissions=2)
            record, requests = fixture.run('single_shot', 'none', [valid_changes(), CORRECTION], [report(False), report(False)])
            self.assertEqual((record['status'], record['budgetLimit'], len(record['submissions']), record['functionalSuccess'], len(requests)), ('budget_exhausted', 'submissions', 2, False, 2))
            self.assertEqual(record['finalEvaluation'], 'submission-2/evaluation/report.json')
            actions = [act('read', files=[{'path': SCORE_PATH, 'start_line': 1, 'end_line': 2}]), act('submit_feature_changes', **valid_changes()), act('submit_feature_changes', **CORRECTION)]
            record, requests = fixture.run('agentic', 'none', actions, [report(False), report(False)])
            self.assertEqual((record['status'], record['budgetLimit'], len(record['turns']), len(record['submissions']), len(requests)), ('budget_exhausted', 'submissions', 3, 2, 3))

    def test_identity_mismatch_stops_the_trajectory(self):
        with tempfile.TemporaryDirectory() as temporary:
            fixture = Fixture(Path(temporary))
            other = lambda request: {'protocol_version': 1, 'request_id': request['request_id'], 'model': 'other-model', 'settings': None, 'finish_reason': 'stop', 'output_text': '{}'}
            record, requests = fixture.run('single_shot', 'none', [other, valid_changes()], [report(True)])
            self.assertEqual((record['status'], len(requests), len(record['turns']), record['turns'][0]['status'], record['submissions'][0]['status']), ('identity_mismatch', 1, 1, 'identity_mismatch', 'identity_mismatch'))
            record, requests = fixture.run('agentic', 'none', [other], [])
            self.assertEqual((record['status'], len(requests), record['submissions']), ('identity_mismatch', 1, []))

    def test_invalid_actions_cost_a_turn_and_return_delivery_errors(self):
        with tempfile.TemporaryDirectory() as temporary:
            fixture = Fixture(Path(temporary))
            actions = [{'action': 'fly', **NULLS}, act('search'), act('read', files=[{'path': 'missing.java', 'start_line': 1, 'end_line': 2}]),
                       act('submit_feature_changes', new_files=[], edits=[]),
                       act('submit_feature_changes', new_files=[{'filename': 'Scores.java', 'content': 'class Scores {}'}], edits=[]),
                       act('submit_feature_changes', **valid_changes())]
            record, requests = fixture.run('agentic', 'none', actions, [report(True)])
            self.assertEqual([t['action'] for t in record['turns']], ['invalid', 'search', 'read', 'submit_feature_changes', 'submit_feature_changes', 'submit_feature_changes'])
            self.assertIn('Choose action', record['turns'][0]['toolResult']['error']); self.assertIn('literal search', record['turns'][1]['toolResult']['error'])
            self.assertIn('not in the source snapshot', record['turns'][2]['toolResult']['error'])
            self.assertEqual([s['status'] for s in record['submissions']], ['invalid_changes', 'invalid_changes', 'evaluated'])
            self.assertIn('Deliver 1', record['submissions'][0]['feedback']['deliveryError']); self.assertIn('Missing required game integration edits', record['submissions'][1]['feedback']['deliveryError'])
            self.assertEqual((record['status'], record['searches'], record['filesRead'], len(requests)), ('completed', [], [], 6))
            self.assertEqual(requests[3]['messages'][-1]['content'], json.dumps(record['turns'][2]['toolResult'], ensure_ascii=False) + '\n21 tool turns and 5 submissions remain.')

    def test_summary_reports_counts_per_condition(self):
        with tempfile.TemporaryDirectory() as temporary:
            fixture = Fixture(Path(temporary), repetitions=2)
            fixture.run('single_shot', 'none', [valid_changes()], [report(True)])
            fixture.run('single_shot', 'none', [valid_changes(), CORRECTION], [report(False), report(True)], repetition=2)
            fixture.run('agentic', 'adaptive', [act('search', query='name'), act('submit_feature_changes', **valid_changes())], [report(True)], FakeSidecar())
            result = agentic.summary(fixture.root)
            self.assertEqual((result['planned'], result['records'], result['maxTurns'], result['maxSubmissions']), (12, 3, 24, 5))
            single = result['conditions']['generation_s__single_shot__none']
            self.assertEqual((single['planned'], single['N'], single['fullWithinBudget'], single['firstSubmissionFull']), (2, 2, {'count': 2, 'of': 2}, {'count': 1, 'of': 2}))
            self.assertEqual((single['toolTurns'], single['submissions']['total'], single['reads']['total'], single['sidecarInjections']['total']), ({'total': 3, 'perTrajectory': [1, 2]}, 3, 0, 0))
            adaptive = result['conditions']['generation_s__agentic__adaptive']
            self.assertEqual((adaptive['N'], adaptive['searches'], adaptive['sidecarInjections'], adaptive['trajectoriesWithInjection'], adaptive['statuses']),
                             (1, {'total': 1, 'perTrajectory': [1]}, {'total': 1, 'perTrajectory': [1]}, {'count': 1, 'of': 1}, {'completed': 1}))
            self.assertEqual(result['conditions']['generation_s__agentic__none']['fullWithinBudget'], {'count': 0, 'of': 0})
            self.assertNotIn('%', json.dumps(result))

    def test_prepare_freezes_every_cell_and_arm_with_shared_prompt_bytes(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary); repo = synthetic_repository(root)
            parent = root / 'parent'; parent.mkdir()
            plan = {'id': 'fixture-parent', 'maxSubmissions': 5}; plan['fingerprint'] = digest(canonical(plan))
            (parent / 'manifest.json').write_bytes(canonical(plan)); (parent / 'generation-input.json').write_bytes(canonical({'sources': {}, 'games': ['ApoMario']}))
            calibration = root / 'calibration.json'; calibration.write_bytes(canonical({'protocol': agentic.EVALUATION_PROTOCOL, 'functionalSuccess': True, 'inputHashes': {}}))
            insert = root / 'generation-static.txt'; insert.write_text(INSERT)
            directory = root / 'iteration'
            with patch.object(agentic, 'repository_input', return_value=repo), patch.object(agentic, 'CALIBRATION', calibration):
                with self.assertRaisesRegex(ValueError, 'No static context insert'):
                    agentic.prepare('fixture-agentic', ['generation_s'], agentic.DEFAULT_ARMS, 2, {}, parent, directory=directory)
                with self.assertRaisesRegex(ValueError, 'Invalid arm'):
                    agentic.prepare('fixture-agentic', ['generation_s'], ['agentic:catalog'], 2, {}, parent, directory=directory)
                first = agentic.prepare('fixture-agentic', ['generation_s'], agentic.DEFAULT_ARMS, 2, {'generation_s': insert}, parent, directory=directory)
                second = agentic.prepare('fixture-agentic', ['generation_s'], agentic.DEFAULT_ARMS, 2, {'generation_s': insert}, parent, directory=directory)
            self.assertEqual(first['fingerprint'], second['fingerprint']); self.assertEqual(first['protocol'], 'agentic-delivery-v1')
            self.assertEqual([c['id'] for c in first['conditions']], [f"generation_s__{a['mode']}__{a['sidecar']}" for a in agentic.DEFAULT_ARMS])
            by_id = {c['id']: c for c in first['conditions']}
            for sidecar in ('none', 'static', 'adaptive'):
                self.assertEqual(by_id[f'generation_s__single_shot__{sidecar}']['promptSha256'], by_id[f'generation_s__agentic__{sidecar}']['promptSha256'])
            self.assertEqual(by_id['generation_s__single_shot__none']['promptSha256'], by_id['generation_s__agentic__adaptive']['promptSha256'])
            static, control = by_id['generation_s__single_shot__static'], by_id['generation_s__single_shot__none']
            self.assertEqual((directory / static['promptFile']).read_bytes(), (directory / control['promptFile']).read_bytes() + b'\n\n' + INSERT.encode())
            self.assertEqual(static['contextInsertSha256'], digest(INSERT.encode())); self.assertIsNone(control['contextInsertSha256'])
            self.assertTrue((directory / control['promptFile']).read_text().startswith('Target repository: ApoMario.'))
            self.assertIn('\n\n--- BEGIN ATTACHED', (directory / control['promptFile']).read_text())
            self.assertEqual(control['repositoryIndexFile'], 'repository/generation-index.txt'); self.assertEqual(control['snapshotFingerprint'], snapshot(repo)['fingerprint'])
            self.assertEqual((directory / control['repositoryIndexFile']).read_text(), agentic.repository_index(snapshot(repo)))
            self.assertEqual(len(first['schedule']), 12); self.assertEqual({r['condition'] for r in first['schedule']}, set(by_id))
            self.assertEqual((first['maxTurns'], first['maxSubmissions'], first['parentIteration']['id']), (24, 5, 'fixture-parent'))
            self.assertIn('research/agentic_delivery.py', first['sourceHashes']); self.assertIn(str(insert.resolve()), first['sourceHashes'])
            self.assertEqual(first['system'], delivery.SYSTEM.replace('up to three submissions', 'up to five submissions'))
            self.assertTrue(first['systemAgentic'].startswith(first['system'])); self.assertIn('24 tool turns', first['systemAgentic'])
            with self.assertRaisesRegex(ValueError, 'differs from the parent'): agentic.delivery_system(5, {'maxSubmissions': 5, 'system': 'something else'})
            self.assertEqual(agentic.delivery_system(3, {'maxSubmissions': 5, 'system': 'something else'}), delivery.SYSTEM)
            self.assertEqual(delivery.validate(directory / 'manifest.json')['fingerprint'], first['fingerprint'])
            self.assertFalse((directory / 'runs').exists())

    def test_prepare_rejects_request_ids_longer_than_the_provider_limit(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary); repo = synthetic_repository(root)
            parent = root / 'parent'; parent.mkdir()
            plan = {'id': 'fixture-parent', 'maxSubmissions': 5}; plan['fingerprint'] = digest(canonical(plan))
            (parent / 'manifest.json').write_bytes(canonical(plan)); (parent / 'generation-input.json').write_bytes(canonical({'sources': {}, 'games': ['ApoMario']}))
            calibration = root / 'calibration.json'; calibration.write_bytes(canonical({'protocol': agentic.EVALUATION_PROTOCOL, 'functionalSuccess': True, 'inputHashes': {}}))
            insert = root / 'generation-static.txt'; insert.write_text(INSERT)
            with patch.object(agentic, 'repository_input', return_value=repo), patch.object(agentic, 'CALIBRATION', calibration):
                with self.assertRaisesRegex(ValueError, 'Request identifiers would reach 66'):
                    agentic.prepare('i16-compact-confirmation', ['generation_s'], ['single_shot:static'], 5, {'generation_s': insert}, parent, directory=root / 'long')
                short = agentic.prepare('i16b-gen-compact', ['generation_s'], ['single_shot:static'], 5, {'generation_s': insert}, parent, directory=root / 'short')
                self.assertTrue(all(len(f"{r['runId']}-t{short['maxTurns']}") <= agentic.REQUEST_ID_LIMIT for r in short['schedule']))

    def test_load_sidecar_accepts_objects_classes_and_factories(self):
        module = types.ModuleType('fixture_sidecar_module')
        module.instance, module.klass, module.factory, module.broken = FakeSidecar(), FakeSidecar, (lambda: FakeSidecar()), object()
        with patch.dict(sys.modules, {'fixture_sidecar_module': module}):
            self.assertIs(agentic.load_sidecar('fixture_sidecar_module:instance'), module.instance)
            self.assertIsInstance(agentic.load_sidecar('fixture_sidecar_module:klass'), FakeSidecar)
            self.assertIsInstance(agentic.load_sidecar('fixture_sidecar_module:factory'), FakeSidecar)
            with self.assertRaisesRegex(ValueError, 'initial'): agentic.load_sidecar('fixture_sidecar_module:broken')
        with self.assertRaisesRegex(ValueError, 'module:attribute'): agentic.load_sidecar('nocolon')
        self.assertIsNone(agentic.load_sidecar(None))


if __name__ == '__main__': unittest.main()
