"""Agentic security-context acquisition through an off-the-shelf coding agent.

A workspace is built from the tracked game distributions (jar sources unpacked),
a code model is derived from it, and an agent harness (Codex CLI) inspects the
workspace read-only under one of three angle instructions. The final document is
validated against the context schema, every anchor is resolved against the
source by the harness, and the validated statements are rendered as the prompt
insert. Records keep the prompt, transcript, output, insert and all hashes.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path
import shutil
import subprocess
import uuid
import zipfile

from research import code_model
from research.import_evidence import ROOT, canonical, digest
from research.run_experiment import timestamp, write_atomic

PROTOCOL = 'repository-security-context-v7-agent'
ANGLES = ('dataflow', 'requirements', 'catalog')
ASSETS = ROOT / 'research/security/agent'
SCHEMA = ROOT / 'research/security/context-schema.json'
CATALOG = ROOT / 'research/security/cwe-top25-2025.json'
GAMES = {'Generation': ['ApoMario'], 'Reuse': ['ApoMario', 'ApoIcarus']}
BINARY = {'.jar', '.class', '.png', '.jpg', '.jpeg', '.gif', '.bmp', '.wav', '.mid', '.mp3', '.ogg', '.ico', '.ttf'}
DEFAULT_MODEL = 'gpt-5.6-luna'
DEFAULT_EFFORT = 'medium'
OUTPUT = ROOT / '.local/context-generation'
STRIDE = ('spoofing', 'tampering', 'repudiation', 'information_disclosure', 'denial_of_service', 'elevation_of_privilege')


def tracked_files(game: str) -> list[tuple[Path, str]]:
    """Tracked files of one game distribution as (absolute path, path relative to the game directory)."""
    base = ROOT / 'apogames/Java' / game
    raw = subprocess.check_output(['git', 'ls-files', '-z', '--', f'apogames/Java/{game}'], cwd=ROOT).decode()
    return [(ROOT / name, (ROOT / name).relative_to(base).as_posix()) for name in raw.split('\0') if name]


def build_workspace(method: str, directory: Path) -> dict:
    if method not in GAMES: raise ValueError('Method must be Generation or Reuse')
    workspace = directory / 'workspace'; workspace.mkdir(parents=True, exist_ok=False)
    files, omitted = {}, []
    for game in GAMES[method]:
        for path, relative in tracked_files(game):
            if path.is_symlink(): raise ValueError('Source links are not permitted')
            if path.suffix == '.jar':
                with zipfile.ZipFile(path) as archive:
                    for member in sorted(archive.namelist()):
                        if not member.endswith('.java') or member.endswith('/'): continue
                        raw = archive.read(member); target = workspace / game / 'src' / member
                        target.parent.mkdir(parents=True, exist_ok=True); target.write_bytes(raw)
                        files[f'{game}/src/{member}'] = digest(raw)
                omitted.append({'file': f'{game}/{relative}', 'reason': 'archive; embedded Java sources unpacked to src/'})
            elif path.suffix.lower() in BINARY:
                omitted.append({'file': f'{game}/{relative}', 'reason': 'binary'})
            else:
                raw = path.read_bytes()
                target = workspace / game / ('src/' + relative if path.suffix == '.java' else relative)
                target.parent.mkdir(parents=True, exist_ok=True); target.write_bytes(raw)
                files[target.relative_to(workspace).as_posix()] = digest(raw)
    model = code_model.build(workspace)
    (workspace / 'code-model.json').write_bytes(canonical(model))
    (workspace / 'outline.md').write_text(code_model.outline(model))
    shutil.copy(SCHEMA, workspace / 'schema.json'); shutil.copy(ASSETS / 'ONTOLOGY.md', workspace / 'ONTOLOGY.md')
    shutil.copy(CATALOG, workspace / 'cwe-top25-2025.json')
    return {'games': GAMES[method], 'files': files, 'omitted': omitted, 'fingerprint': digest(canonical(files)),
            'codeModelSha256': digest(canonical(model)), 'symbols': len(model['symbols']), 'sourceFiles': len(model['files'])}


def build_prompt(task: str, method: str, angle: str) -> str:
    if angle not in ANGLES: raise ValueError('Unknown angle')
    common = (ASSETS / 'common.md').read_text(); body = (ASSETS / f'{angle}.md').read_text().strip()
    donor = ' ApoIcarus is the donor whose existing implementation should be reused where possible.' if method == 'Reuse' else ''
    return (common.replace('{{TASK}}', task.strip()).replace('{{GAMES}}', ', '.join(GAMES[method]))
            .replace('{{DONOR_NOTE}}', donor).replace('{{ANGLE}}', body).replace('{{ANGLE_ID}}', angle))


def generator_hashes(angle: str) -> dict:
    paths = [Path(__file__), ROOT / 'research/code_model.py', SCHEMA, ASSETS / 'common.md', ASSETS / f'{angle}.md', ASSETS / 'ONTOLOGY.md']
    if angle == 'catalog': paths.append(CATALOG)
    return {str(p.relative_to(ROOT)): digest(p.read_bytes()) for p in paths}


def prepare(method: str, angle: str, task: str, output: Path = OUTPUT, model: str = DEFAULT_MODEL, effort: str = DEFAULT_EFFORT, max_commands: int = 60):
    if not task.strip(): raise ValueError('A task is required')
    identifier = 'agent-' + uuid.uuid4().hex; directory = output / identifier; directory.mkdir(parents=True, exist_ok=False)
    workspace = build_workspace(method, directory)
    prompt = build_prompt(task, method, angle).replace('{{MAX_COMMANDS}}', str(max_commands))
    (directory / 'prompt.md').write_text(prompt); shutil.copy(SCHEMA, directory / 'schema.json')
    record = {'schemaVersion': 7, 'protocol': PROTOCOL, 'id': identifier, 'status': 'prepared', 'startedAt': timestamp(),
              'harness': 'codex-exec', 'method': method, 'repository': '+'.join(GAMES[method]), 'angle': angle, 'strategy': f'agent_{angle}',
              'task': task.strip(), 'model': model, 'settings': {'reasoning_effort': effort, 'sandbox': 'read-only', 'max_commands': max_commands},
              'workspace': workspace, 'snapshotFingerprint': workspace['fingerprint'], 'schemaSha256': digest(SCHEMA.read_bytes()),
              'initialPrompt': prompt, 'initialPromptSha256': digest(prompt.encode()), 'generatorHashes': generator_hashes(angle),
              'sourceFiles': workspace['sourceFiles'], 'turns': [], 'output': None, 'citationChecks': None, 'settingsVerified': None}
    write_atomic(directory / 'record.json', record)
    return record, directory


def check_schema(instance, schema, definitions=None, path='$') -> list[str]:
    """Minimal JSON Schema subset: type, enum, required, properties, additionalProperties, items, anyOf, $ref."""
    definitions = definitions or schema.get('definitions', {}); errors = []
    if '$ref' in schema:
        return check_schema(instance, definitions[schema['$ref'].split('/')[-1]], definitions, path)
    if 'anyOf' in schema:
        if not any(not check_schema(instance, option, definitions, path) for option in schema['anyOf']): errors.append(f'{path}: matches no alternative')
        return errors
    types = schema.get('type'); types = [types] if isinstance(types, str) else types
    kinds = {'object': dict, 'array': list, 'string': str, 'integer': int, 'null': type(None), 'boolean': bool, 'number': (int, float)}
    if types and not any(isinstance(instance, kinds[t]) and not (t == 'integer' and isinstance(instance, bool)) for t in types):
        return [f'{path}: expected {"/".join(types)}']
    if 'enum' in schema and instance not in schema['enum']: errors.append(f'{path}: not one of {schema["enum"]}')
    if isinstance(instance, dict):
        for key in schema.get('required', []):
            if key not in instance: errors.append(f'{path}.{key}: missing')
        for key, value in instance.items():
            if key in schema.get('properties', {}): errors += check_schema(value, schema['properties'][key], definitions, f'{path}.{key}')
            elif schema.get('additionalProperties') is False: errors.append(f'{path}.{key}: unexpected field')
    if isinstance(instance, list) and 'items' in schema:
        for index, value in enumerate(instance): errors += check_schema(value, schema['items'], definitions, f'{path}[{index}]')
    return errors


def validate_output(document: dict, workspace: Path, model: dict) -> tuple[dict, dict]:
    """Schema check, anchor resolution and evidence rules. Returns (validated document, citation checks)."""
    schema = json.loads(SCHEMA.read_text()); errors = check_schema(document, schema)
    if errors: raise ValueError('Schema violations: ' + '; '.join(errors[:12]))
    total = matched = 0; rejected = []

    def resolve(anchors):
        nonlocal total, matched; resolved = []
        for anchor in anchors:
            total += 1; result = code_model.validate_anchor(model, workspace, anchor)
            if result['ok']: matched += 1; resolved.append({**anchor, 'resolvedFile': result['file'], 'resolvedStart': result['start'], 'resolvedEnd': result['end'], 'symbols': result['symbols'], 'textSha256': result['textSha256']})
            else: rejected.append({'anchor': anchor, 'reason': result['reason']})
        return resolved

    validated = {'angle': document['angle'], 'summary': document['summary'], 'limitations': list(document['limitations']), 'assets': [], 'boundaries': [], 'items': []}
    for asset in document['assets']: validated['assets'].append({**asset, 'anchors': resolve(asset['anchors'])})
    for boundary in document['boundaries']: validated['boundaries'].append({**boundary, 'anchors': resolve(boundary['anchors'])})
    ids = [item['id'] for item in document['items']]
    if len(ids) != len(set(ids)): raise ValueError('Duplicate item ids')
    dropped = []
    for item in document['items']:
        anchors = resolve(item['anchors'])
        enforcement = None
        if item['enforcement_point'] is not None:
            resolved = resolve([item['enforcement_point']]); enforcement = resolved[0] if resolved else None
        if item['basis'] == 'observed' and not anchors:
            dropped.append({'id': item['id'], 'kind': item['kind'], 'reason': 'observed basis without a resolvable anchor'}); continue
        if item['kind'] in ('security_property', 'existing_risk') and item['basis'] != 'observed':
            dropped.append({'id': item['id'], 'kind': item['kind'], 'reason': 'properties and existing risks require observed basis'}); continue
        validated['items'].append({**item, 'anchors': anchors, 'enforcement_point': enforcement, 'related': [r for r in item['related'] if r in ids]})
    kinds = {}
    for item in validated['items']: kinds[item['kind']] = kinds.get(item['kind'], 0) + 1
    checks = {'total': total, 'matched': matched, 'rejectedAnchors': rejected, 'droppedItems': dropped, 'itemsByKind': kinds,
              'uncitedItems': sum(1 for item in validated['items'] if not item['anchors']),
              'limitation': 'Anchors resolve to inspected source ranges; claim entailment, completeness and exploitability are not mechanically validated.'}
    return validated, checks


def location(anchor: dict) -> str:
    symbol = f" ({anchor['symbol']})" if anchor.get('symbol') else ''
    return f"{anchor['resolvedFile']}:{anchor['resolvedStart']}-{anchor['resolvedEnd']}{symbol}"


def prompt_insert(validated: dict) -> str:
    lines = [f"--- BEGIN REPOSITORY-DERIVED SECURITY CONTEXT (angle: {validated['angle']}) ---", validated['summary']]
    if validated['assets']:
        lines += ['', 'Assets:'] + [f"- {a['name']} [{a['property']}]" + (' @ ' + '; '.join(location(x) for x in a['anchors']) if a['anchors'] else '') for a in validated['assets']]
    if validated['boundaries']:
        lines += ['', 'Trust boundaries:'] + [f"- {b['name']}: {b['untrusted_input']} from {b['source']} to {b['sink']}" + (' @ ' + '; '.join(location(x) for x in b['anchors']) if b['anchors'] else '') for b in validated['boundaries']]
    for item in validated['items']:
        tags = [item['id'], item['kind'], item['basis']] + ([item['threat']] if item['threat'] else []) + item['cwe'] + item['capec'] + item['asvs'] + item['cert']
        lines += ['', f"[{'; '.join(tags)}] {item['statement']}", 'Task relevance: ' + item['task_relevance']]
        if item['enforcement_point']: lines.append('Enforcement point: ' + location(item['enforcement_point']))
        if item['failure_behavior']: lines.append('Failure behavior: ' + item['failure_behavior'])
        for anchor in item['anchors']: lines.append('Inspected source: ' + location(anchor))
        if not item['anchors']:
            lines.append('Uncited unknown; absence was not established.' if item['kind'] == 'unknown' else 'Basis: prospective task requirement or reasoning; not an implemented safeguard.')
        if item['verification']: lines.append('Suggested verification: ' + item['verification'])
        if item['related']: lines.append('Related: ' + ', '.join(item['related']))
    lines += ['', 'Uncertainty and limits:', *['- ' + value for value in validated['limitations']], '--- END REPOSITORY-DERIVED SECURITY CONTEXT ---', '']
    return '\n'.join(lines)


def summarize_events(events: list[dict]) -> list[dict]:
    turns = []
    for event in events:
        item = event.get('item') or {}
        kind = item.get('type') or event.get('type')
        summary = {'type': event.get('type'), 'item': item.get('type')}
        if item.get('type') == 'command_execution': summary['command'] = (item.get('command') or '')[:300]; summary['exitCode'] = item.get('exit_code')
        if item.get('type') == 'agent_message': summary['characters'] = len(item.get('text') or '')
        if event.get('type') == 'turn.completed': summary['usage'] = event.get('usage')
        if event.get('type') == 'thread.started': summary['threadId'] = event.get('thread_id')
        turns.append(summary)
    return turns


def execute(record: dict, directory: Path, codex: str = 'codex', timeout: int = 3600) -> dict:
    if record['protocol'] != PROTOCOL or record['status'] != 'prepared': raise ValueError('Expected a prepared agent acquisition')
    prompt = (directory / 'prompt.md').read_text()
    if digest(prompt.encode()) != record['initialPromptSha256'] or prompt != record['initialPrompt']: raise ValueError('Frozen acquisition prompt changed')
    for path, sha in record['generatorHashes'].items():
        if digest((ROOT / path).read_bytes()) != sha: raise ValueError(f'Frozen acquisition generator changed: {path}')
    if digest((directory / 'schema.json').read_bytes()) != record['schemaSha256']: raise ValueError('Frozen schema changed')
    workspace = directory / 'workspace'; model = json.loads((workspace / 'code-model.json').read_text())
    if digest(canonical(model)) != record['workspace']['codeModelSha256']: raise ValueError('Frozen code model changed')
    with (directory / 'submitted').open('x') as marker: marker.write(timestamp())
    final = directory / 'final.json'
    command = [codex, 'exec', '-m', record['model'], '-c', f"model_reasoning_effort={record['settings']['reasoning_effort']}", '--sandbox', 'read-only',
               '--skip-git-repo-check', '-C', str(workspace), '--json', '--output-schema', str(directory / 'schema.json'), '-o', str(final), '-']
    record.update(status='running', command=command, submittedAt=timestamp()); write_atomic(directory / 'record.json', record)
    try:
        process = subprocess.run(command, input=prompt, capture_output=True, text=True, timeout=timeout, cwd=workspace)
        stdout, stderr, code = process.stdout, process.stderr, process.returncode
    except subprocess.TimeoutExpired as error:
        partial = error.stdout or ''
        stdout, stderr, code = (partial.decode() if isinstance(partial, bytes) else partial), 'timeout', None
    (directory / 'transcript.jsonl').write_text(stdout); (directory / 'stderr.log').write_text(stderr or '')
    events = []
    for line in stdout.splitlines():
        line = line.strip()
        if not line: continue
        try: events.append(json.loads(line))
        except ValueError: events.append({'type': 'unparsed', 'text': line[:500]})
    record.update(turns=summarize_events(events), exitCode=code, finishedAt=timestamp(), transcriptSha256=digest(stdout.encode()),
                  commandsExecuted=sum(1 for e in events if (e.get('item') or {}).get('type') == 'command_execution'))
    reported = {e.get('model') for e in events if e.get('model')} | {(e.get('item') or {}).get('model') for e in events if (e.get('item') or {}).get('model')}
    record['modelReported'] = sorted(m for m in reported if m)
    document = None
    if final.exists():
        try: document = json.loads(final.read_text())
        except ValueError: record['failure'] = 'Final message is not JSON'
    elif code == 0:
        messages = [(e.get('item') or {}).get('text') for e in events if (e.get('item') or {}).get('type') == 'agent_message']
        for text in reversed([m for m in messages if m]):
            try: document = json.loads(text); break
            except ValueError: continue
    if code != 0: record['failure'] = f'Agent harness exited with {code}' if code is not None else 'Agent harness timed out'
    if document is not None and code == 0:
        try:
            validated, checks = validate_output(document, workspace, model)
            record.update(rawOutput=document, output=validated, citationChecks=checks, promptInsert=prompt_insert(validated))
            record['promptInsertSha256'] = digest(record['promptInsert'].encode())
            if record['modelReported'] and record['modelReported'] != [record['model']]: record['status'] = 'identity_mismatch'
            else: record['status'] = 'settings_unverified' if not record['modelReported'] else 'completed'
        except ValueError as error:
            record.update(rawOutput=document, failure=str(error), status='invalid_output')
    elif 'failure' not in record: record['failure'] = 'No final document'
    if record['status'] in ('running',): record['status'] = 'failed'
    write_atomic(directory / 'record.json', record)
    return record


def main():
    parser = argparse.ArgumentParser(description=__doc__); sub = parser.add_subparsers(dest='action', required=True)
    p = sub.add_parser('prepare'); p.add_argument('--method', choices=list(GAMES), required=True); p.add_argument('--angle', choices=ANGLES, required=True)
    p.add_argument('--task', required=True); p.add_argument('--model', default=DEFAULT_MODEL); p.add_argument('--effort', default=DEFAULT_EFFORT)
    p.add_argument('--output', type=Path, default=OUTPUT); p.add_argument('--execute', action='store_true'); p.add_argument('--codex', default='codex')
    e = sub.add_parser('execute'); e.add_argument('--id', required=True); e.add_argument('--output', type=Path, default=OUTPUT); e.add_argument('--codex', default='codex'); e.add_argument('--timeout', type=int, default=3600)
    args = parser.parse_args()
    if args.action == 'prepare':
        record, directory = prepare(args.method, args.angle, args.task, args.output, args.model, args.effort)
        print(json.dumps({'id': record['id'], 'workspaceFiles': len(record['workspace']['files']), 'symbols': record['workspace']['symbols']}), flush=True)
        if args.execute: record = execute(record, directory, args.codex); print(json.dumps({'id': record['id'], 'status': record['status'], 'items': len((record.get('output') or {}).get('items', []))}))
    else:
        directory = args.output / args.id; record = json.loads((directory / 'record.json').read_text())
        record = execute(record, directory, args.codex, args.timeout)
        print(json.dumps({'id': record['id'], 'status': record['status'], 'items': len((record.get('output') or {}).get('items', [])), 'failure': record.get('failure')}))


if __name__ == '__main__':
    main()
