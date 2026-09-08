"""Development calibration for complete feature delivery through exact source edits.

This changes the original single-response protocol. Its attempts and feedback are
kept separately and never imported as paper-matrix observations.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path
import re
import uuid

from research.import_evidence import ROOT, canonical, digest
from research.model_adapter import command_from_env, invoke, AdapterFailure
from research.paper_matrix import source_attachment
from research.security_followup import acquisition_task
from research.run_experiment import timestamp, write_atomic
from research.evaluate_response import evaluate_response

MODEL = 'gpt-5.6-luna'
SETTINGS = {'reasoning_effort': 'medium', 'temperature': None, 'max_output_tokens': 65536}
TARGETS = ('ApoMarioLevel.java', 'ApoMarioMenu.java', 'ApoMarioPanel.java')


def obj(properties):
    return {'type': 'object', 'properties': properties, 'required': list(properties), 'additionalProperties': False}


STRING = {'type': 'string'}
TOOL = {'type': 'function', 'function': {'name': 'submit_feature_changes', 'strict': True,
        'description': 'Deliver complete new Java classes and exact replacements within existing Java classes. All changes are applied transactionally.',
        'parameters': obj({'new_files': {'type': 'array', 'items': obj({'filename': STRING, 'content': STRING})},
                           'edits': {'type': 'array', 'items': obj({'filename': STRING, 'old_text': STRING, 'new_text': STRING})}})}}
PROTOCOL = '''Implement and integrate the requested Highscore feature in the existing game.
Return changes by calling submit_feature_changes. Do not write tests or alter the test harness.
The target is Java 8. Every member called on existing code must exist with a compatible signature.
Every filename must be a Java basename such as ApoMarioLevel.java, never a package path.
Deliver complete new Java files through new_files. To change an existing class, use edits:
old_text must occur exactly once in that file and new_text replaces it. Source uses LF newlines.
Edits are applied in listed order; preserve every unrelated member of existing game classes.
If a delivery error is returned, none of that submission's edits or new files were applied.
Include actual edits to the supplied game classes to connect the feature to the live lifecycle
and menu. A standalone Highscore class does not complete this task. Recording must happen
when the live game ends a run, using its real score, player name and elapsed survival time.
Time units: storeRun's survivalTime and getSurvivalTimes() use milliseconds, matching
ApoMarioLevel.getPassedTime(). Preserve that value when recording and persisting a run;
convert milliseconds to mm:ss only when rendering the highscore board.
Do not assume that the evaluator will add missing integration for you.
The harness reconstructs COMPLETE modified files from your exact edits before compilation.
You may receive compilation and functional-test feedback for up to three submissions. Each
submission edits the current accumulated source. All submissions and feedback are retained.
No security context or security-test feedback is supplied in this delivery calibration.
'''


def apply_changes(files: dict[str, str], action: dict) -> dict[str, str]:
    if not isinstance(action, dict) or set(action) != {'new_files', 'edits'}: raise ValueError('Return new_files and edits')
    if not isinstance(action['new_files'], list) or not isinstance(action['edits'], list): raise ValueError('Changes must be arrays')
    if not 1 <= len(action['new_files']) + len(action['edits']) <= 60: raise ValueError('Deliver 1–60 changes')
    result = dict(files)
    def filename(value):
        if not isinstance(value, str) or not re.fullmatch(r'[A-Za-z_$][\w$]*\.java', value): raise ValueError('Use a Java basename, without a path')
        return value
    for item in action['new_files']:
        name = filename(item['filename'])
        if name in result: raise ValueError(f'{name} exists; edit it instead')
        if not isinstance(item['content'], str) or not item['content'].strip(): raise ValueError('New files must contain source')
        result[name] = item['content'].replace('\r\n', '\n')
    for edit in action['edits']:
        name = filename(edit['filename'])
        if name not in result: raise ValueError(f'{name} is not a supplied or newly created file')
        old, new = edit['old_text'], edit['new_text']
        if not isinstance(old, str) or not old or not isinstance(new, str): raise ValueError('Use a nonempty exact anchor and replacement text')
        old, new = old.replace('\r\n', '\n'), new.replace('\r\n', '\n')
        matches = result[name].count(old)
        if matches != 1: raise ValueError(f'{name}: old_text matches {matches} times; provide one unique exact anchor')
        result[name] = result[name].replace(old, new, 1)
    return result


def original_sources():
    files, origins = {}, {}
    for name in TARGETS:
        raw, origin = source_attachment('ApoMario.' + name.removesuffix('.java'))
        files[name] = raw.decode('utf-8', errors='replace').replace('\r\n', '\n')
        origins[name] = {**origin, 'sha256': digest(raw), 'normalizedSha256': digest(files[name].encode())}
    return files, origins


def run(output: Path, command: list[str], max_submissions=3):
    if not 1 <= max_submissions <= 3: raise ValueError('Use 1–3 calibration submissions')
    original, origins = original_sources()
    files = dict(original)
    run_id = 'delivery-' + uuid.uuid4().hex
    directory = output / run_id; directory.mkdir(parents=True, exist_ok=False)
    protocol_source = Path(__file__).read_bytes()
    (directory / 'protocol.py').write_bytes(protocol_source)
    prompt = PROTOCOL + '\nTASK\n' + acquisition_task('Generation')
    for name, source in files.items(): prompt += f'\n\n--- SOURCE: {name} ---\n{source}\n--- END SOURCE ---'
    messages = [{'role': 'user', 'content': prompt}]
    record = {'id': run_id, 'kind': 'delivery_calibration', 'status': 'started', 'startedAt': timestamp(), 'model': MODEL,
              'settings': SETTINGS, 'settingsVerified': None, 'maxSubmissions': max_submissions, 'sourceOrigins': origins,
              'protocolSha256': digest(protocol_source), 'submissions': [], 'functionalSuccess': False}
    write_atomic(directory / 'record.json', record)
    try:
        for number in range(1, max_submissions + 1):
            request = {'protocol_version': 1, 'request_id': f'{run_id}-s{number}', 'model': MODEL, 'settings': SETTINGS,
                       'messages': list(messages), 'tools': [TOOL], 'tool_choice': {'type': 'function', 'function': {'name': 'submit_feature_changes'}}, 'parallel_tool_calls': False}
            submission = {'number': number, 'status': 'started', 'startedAt': timestamp(), 'request': request, 'requestSha256': digest(canonical(request))}
            record['submissions'].append(submission); write_atomic(directory / 'record.json', record)
            response = invoke(command, request, 600)
            submission.update(response=response, receivedAt=timestamp(), responseSha256=digest(canonical(response)))
            if response['model'] != MODEL or response['request_id'] != request['request_id']:
                submission['status'] = record['status'] = 'identity_mismatch'; break
            if response['settings'] is not None and response['settings'] != SETTINGS:
                submission['status'] = record['status'] = 'settings_mismatch'; break
            record['settingsVerified'] = record['settingsVerified'] is not False and response['settings'] == SETTINGS
            if response['finish_reason'] != 'stop': submission['status'] = record['status'] = 'incomplete_response'; break
            messages.append({'role': 'assistant', 'content': response['output_text']})
            try:
                candidate = apply_changes(files, json.loads(response['output_text']))
                if not any(candidate[name] != original[name] for name in TARGETS): raise ValueError('No existing game class was changed; supply actual lifecycle/menu integration edits')
            except (ValueError, KeyError, TypeError) as error:
                submission['status'] = 'invalid_changes'; feedback = {'deliveryError': str(error)}
            else:
                files = candidate
                changed = {name: content for name, content in files.items() if content != original.get(name)}
                stage = directory / f'submission-{number}'; stage.mkdir()
                source_dir = stage / 'source'; source_dir.mkdir()
                for name, content in changed.items(): (source_dir / name).write_text(content)
                complete = '\n\n'.join(f'```java filename={name}\n{content}\n```' for name, content in changed.items())
                (stage / 'complete-files.txt').write_text(complete)
                submission.update(status='evaluating', deliveredFiles=list(changed), sourceHashes={name: digest(content.encode()) for name, content in changed.items()}, completeResponseSha256=digest(complete.encode()))
                write_atomic(directory / 'record.json', record)
                report = evaluate_response(complete, stage / 'evaluation', {'calibrationId': run_id, 'submission': number})
                submission.update(status='evaluated', evaluationStatus=report['status'], evaluationCanonicalSha256=digest(canonical(report)), compilation=report.get('mainCompilation'), functionalSuccess=report.get('functionalSuccess'))
                feedback = {'compilation': report.get('mainCompilation'), 'functionalSuccess': report.get('functionalSuccess'),
                            'functionalChecks': [c for c in report.get('checks', []) if c['suite'] in ('unit', 'invoked', 'autonomous')],
                            'compilerErrors': [(p.get('stderr') or '')[-18000:] for p in report.get('processes', []) if p.get('exitCode') and 'javac' in Path(p['command'][0]).name]}
                submission['feedback'] = feedback
                if report.get('functionalSuccess'):
                    record.update(status='completed', functionalSuccess=True); break
                if report['status'] != 'evaluated': record['status'] = 'evaluation_error'; break
            submission['feedback'] = feedback
            messages.append({'role': 'user', 'content': json.dumps(feedback) + f'\n{max_submissions - number} submissions remain. Correct the current source using exact edits.'})
            write_atomic(directory / 'record.json', record)
            print(f'{run_id} submission {number}: {submission["status"]}; functional={submission.get("functionalSuccess")}', flush=True)
        else: record['status'] = 'budget_exhausted'
    except AdapterFailure as error:
        record.update(status='adapter_error', errorCategory=error.category)
        submission.update(status='adapter_error', errorCategory=error.category)
    except BaseException:
        record['status'] = 'evaluation_error' if submission.get('status') == 'evaluating' else 'interrupted'
        submission['status'] = record['status']; raise
    finally:
        record['finishedAt'] = timestamp(); write_atomic(directory / 'record.json', record)
    print(f'{run_id}: {record["status"]}; functional={record["functionalSuccess"]}', flush=True)
    return record


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, default=ROOT / '.local/delivery-calibration')
    parser.add_argument('--execute', action='store_true')
    args = parser.parse_args()
    if args.execute: run(args.output, command_from_env())
    else: print('Calibration: up to three structured file-edit submissions with compilation/functional feedback. No matrix observations are changed. Add --execute to run.')
