import json
import os
from pathlib import Path
import stat
import tempfile
from types import SimpleNamespace
import unittest
from unittest.mock import patch
import zipfile

from research import context_agent
from research.import_evidence import digest

SOURCE = 'package apoMario;\n\npublic class Score {\n  public boolean store(int points) {\n    return points >= 0;\n  }\n}\n'
SYMBOL = 'apoMario.Score#store(int)'
FILE = 'ApoMario/src/apoMario/Score.java'


def fake_code_model():
    def build(root):
        files = [{'path': p.relative_to(root).as_posix(), 'lines': len(p.read_text().splitlines())} for p in sorted(root.rglob('*.java'))]
        return {'files': files, 'symbols': [{'id': SYMBOL, 'kind': 'method', 'file': FILE, 'start': 4, 'end': 6}]}

    def outline(model):
        return '\n'.join(f"{s['id']} [L{s['start']}-L{s['end']}]" for s in model['symbols'])

    def validate_anchor(model, root, anchor):
        symbol = next((s for s in model['symbols'] if s['id'] == anchor.get('symbol')), None)
        if anchor.get('symbol') and not symbol: return {'ok': False, 'reason': 'unknown symbol'}
        file = anchor.get('file') or (symbol and symbol['file'])
        if not file or not (root / file).exists(): return {'ok': False, 'reason': 'unknown file'}
        lines = (root / file).read_text().splitlines()
        start, end = anchor.get('start_line') or (symbol and symbol['start']), anchor.get('end_line') or (symbol and symbol['end'])
        if not (1 <= start <= end <= len(lines)): return {'ok': False, 'reason': 'range outside file'}
        if symbol and not (symbol['start'] <= start and end <= symbol['end']): return {'ok': False, 'reason': 'range outside symbol'}
        return {'ok': True, 'reason': None, 'file': file, 'start': start, 'end': end, 'symbols': [symbol['id']] if symbol else [], 'textSha256': digest('\n'.join(lines[start - 1:end]).encode())}
    return SimpleNamespace(build=build, outline=outline, validate_anchor=validate_anchor)


def document(extra_items=()):
    anchor = {'symbol': SYMBOL, 'file': FILE, 'start_line': 4, 'end_line': 6}
    item = lambda id, kind, basis, anchors, **kw: {'id': id, 'kind': kind, 'basis': basis, 'statement': f'{id} statement', 'task_relevance': 'relevant', 'threat': None,
                                                   'cwe': [], 'capec': [], 'asvs': [], 'cert': [], 'enforcement_point': None, 'failure_behavior': None,
                                                   'anchors': anchors, 'verification': None, 'related': [], **kw}
    return {'angle': 'dataflow', 'summary': 'Scores are stored by Score.store.', 'limitations': ['Only one class inspected.'],
            'assets': [{'name': 'score records', 'property': 'integrity', 'anchors': [anchor]}],
            'boundaries': [{'name': 'store input', 'untrusted_input': 'points', 'source': 'caller', 'sink': 'store', 'anchors': [anchor]}],
            'items': [item('P1', 'security_property', 'observed', [anchor]),
                      item('R1', 'requirement', 'task', [], threat='tampering', cwe=['CWE-20'], enforcement_point=anchor, failure_behavior='return false', verification='call with -1'),
                      item('X1', 'existing_risk', 'observed', [{'symbol': None, 'file': 'ApoMario/src/missing.java', 'start_line': 1, 'end_line': 2}]),
                      item('P2', 'security_property', 'observed', [{'symbol': 'apoMario.Wrong#store(int)', 'file': FILE, 'start_line': 4, 'end_line': 6}]),
                      *extra_items]}


class ContextAgentTests(unittest.TestCase):
    def setUp(self):
        self.temporary = tempfile.TemporaryDirectory(); self.root = Path(self.temporary.name)
        game = self.root / 'game'; (game / 'levels').mkdir(parents=True)
        with zipfile.ZipFile(game / 'ApoMario.jar', 'w') as archive:
            archive.writestr('apoMario/Score.java', SOURCE); archive.writestr('apoMario/Score.class', b'\xca\xfe'); archive.writestr('img/x.png', b'png')
        (game / 'levels/one.mar').write_bytes(b'\x00\x00\x00d'); (game / 'mario.properties').write_text('a=b\n'); (game / 'logo.png').write_bytes(b'png')
        files = [(game / 'ApoMario.jar', 'ApoMario.jar'), (game / 'levels/one.mar', 'levels/one.mar'), (game / 'mario.properties', 'mario.properties'), (game / 'logo.png', 'logo.png')]
        self.patches = [patch.object(context_agent, 'tracked_files', side_effect=lambda game: files), patch.object(context_agent, 'code_model', fake_code_model())]
        for p in self.patches: p.start()

    def tearDown(self):
        for p in self.patches: p.stop()
        self.temporary.cleanup()

    def fake_codex(self, name, exit_code=0, body=None, emit_final=True):
        path = self.root / name; script = '''#!/bin/sh
out=""; while [ $# -gt 0 ]; do if [ "$1" = "-o" ]; then out="$2"; shift; fi; shift; done
cat > "$(dirname "$out")/prompt-seen.txt"
echo '{"type":"thread.started","thread_id":"t1"}'
echo '{"type":"item.completed","item":{"type":"command_execution","command":"rg store","exit_code":0}}'
echo '{"type":"item.completed","item":{"type":"agent_message","text":"done"}}'
echo '{"type":"turn.completed","usage":{"input_tokens":10,"output_tokens":5}}'
'''
        if emit_final: script += f"cat > \"$out\" <<'JSON'\n{body}\nJSON\n"
        script += f'exit {exit_code}\n'
        path.write_text(script); path.chmod(path.stat().st_mode | stat.S_IEXEC); return str(path)

    def test_prepare_builds_workspace_prompt_and_record(self):
        record, directory = context_agent.prepare('Generation', 'dataflow', 'Implement a Highscore feature in ApoMario.', self.root / 'out')
        workspace = directory / 'workspace'
        self.assertEqual((workspace / FILE).read_text(), SOURCE)
        self.assertTrue((workspace / 'ApoMario/mario.properties').exists()); self.assertTrue((workspace / 'ApoMario/levels/one.mar').exists())
        self.assertFalse(list(workspace.rglob('*.class'))); self.assertFalse(list(workspace.rglob('*.png')))
        self.assertEqual({o['file'] for o in record['workspace']['omitted']}, {'ApoMario/ApoMario.jar', 'ApoMario/logo.png'})
        for name in ('code-model.json', 'outline.md', 'schema.json', 'ONTOLOGY.md', 'cwe-top25-2025.json'): self.assertTrue((workspace / name).exists())
        prompt = (directory / 'prompt.md').read_text()
        self.assertIn('TASK\nImplement a Highscore feature in ApoMario.', prompt); self.assertIn('ANGLE: DATA FLOW', prompt); self.assertIn('"angle": "dataflow"', prompt)
        self.assertIn('at most 60 shell commands', prompt); self.assertNotIn('{{', prompt); self.assertNotIn('ApoIcarus', prompt)
        self.assertEqual(record['strategy'], 'agent_dataflow'); self.assertEqual(record['status'], 'prepared')
        self.assertEqual(record['initialPromptSha256'], digest(prompt.encode()))
        self.assertIn('research/security/agent/dataflow.md', record['generatorHashes']); self.assertNotIn('research/security/cwe-top25-2025.json', record['generatorHashes'])
        reuse = context_agent.build_prompt('t', 'Reuse', 'catalog'); self.assertIn('ApoIcarus is the donor', reuse); self.assertIn('ANGLE: WEAKNESS CATALOG', reuse)
        self.assertIn('research/security/cwe-top25-2025.json', context_agent.generator_hashes('catalog'))

    def test_execute_validates_anchors_and_renders_insert(self):
        record, directory = context_agent.prepare('Generation', 'dataflow', 'Implement a Highscore feature.', self.root / 'out')
        codex = self.fake_codex('codex-ok', body=json.dumps(document()))
        result = context_agent.execute(record, directory, codex)
        self.assertEqual(result['status'], 'settings_unverified'); self.assertEqual(result['exitCode'], 0); self.assertEqual(result['commandsExecuted'], 1)
        self.assertEqual((directory / 'prompt-seen.txt').read_text(), record['initialPrompt'])
        self.assertEqual([i['id'] for i in result['output']['items']], ['P1', 'R1', 'P2'])
        self.assertEqual(result['citationChecks']['droppedItems'][0]['id'], 'X1'); self.assertEqual(result['citationChecks']['matched'], 5); self.assertEqual(result['citationChecks']['total'], 6)
        corrected = result['output']['items'][2]['anchors'][0]; self.assertEqual(corrected['symbolCorrected']['symbolGiven'], 'apoMario.Wrong#store(int)'); self.assertEqual(corrected['symbols'], [])
        self.assertEqual(result['citationChecks']['symbolCorrected'], 1); self.assertEqual(result['citationChecks']['validator'], 'anchor-validation-v2')
        again = context_agent.revalidate(json.loads((directory / 'record.json').read_text()), directory); self.assertEqual(again['revalidations'][0]['matched'], 5); self.assertEqual(again['promptInsertSha256'], result['promptInsertSha256'])
        self.assertEqual(result['output']['items'][1]['enforcement_point']['symbols'], [SYMBOL])
        insert = result['promptInsert']
        self.assertIn('angle: dataflow', insert); self.assertIn(f'{FILE}:4-6 ({SYMBOL})', insert); self.assertIn('[R1; requirement; task; tampering; CWE-20]', insert)
        self.assertIn('Enforcement point: ', insert); self.assertEqual(result['promptInsertSha256'], digest(insert.encode()))
        saved = json.loads((directory / 'record.json').read_text()); self.assertEqual(saved['status'], 'settings_unverified')
        self.assertTrue((directory / 'transcript.jsonl').exists())
        with self.assertRaises(FileExistsError): context_agent.execute(json.loads((directory / 'record.json').read_text()) | {'status': 'prepared'}, directory, codex)

    def test_failures_are_recorded_not_hidden(self):
        record, directory = context_agent.prepare('Generation', 'requirements', 'Task.', self.root / 'out')
        result = context_agent.execute(record, directory, self.fake_codex('codex-fail', exit_code=2, emit_final=False))
        self.assertEqual(result['status'], 'failed'); self.assertIn('exited with 2', result['failure']); self.assertIsNone(result['output'])
        record2, directory2 = context_agent.prepare('Generation', 'requirements', 'Task.', self.root / 'out')
        bad = document(); bad['items'][0]['kind'] = 'nonsense'
        result2 = context_agent.execute(record2, directory2, self.fake_codex('codex-bad', body=json.dumps(bad)))
        self.assertEqual(result2['status'], 'invalid_output'); self.assertIn('Schema violations', result2['failure'])

    def test_changed_prompt_is_rejected_before_running(self):
        record, directory = context_agent.prepare('Generation', 'catalog', 'Task.', self.root / 'out')
        (directory / 'prompt.md').write_text(record['initialPrompt'] + '\nextra')
        with self.assertRaisesRegex(ValueError, 'prompt changed'): context_agent.execute(record, directory, self.fake_codex('codex-unused'))

    def test_schema_checker_subset(self):
        schema = json.loads(context_agent.SCHEMA.read_text())
        self.assertEqual(context_agent.check_schema(document(), schema), [])
        broken = document(); broken['items'][0]['threat'] = 'weird'; broken['extra'] = 1; del broken['items'][1]['cwe']
        errors = context_agent.check_schema(broken, schema)
        self.assertTrue(any('threat' in e for e in errors)); self.assertTrue(any('unexpected field' in e for e in errors)); self.assertTrue(any('cwe: missing' in e for e in errors))


if __name__ == '__main__':
    unittest.main()
