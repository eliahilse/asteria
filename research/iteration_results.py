"""Read durable iteration trajectories into the compact explorer and saved reports."""
from __future__ import annotations

import argparse
import json
from pathlib import Path
import re

from research.import_evidence import ROOT, canonical, digest
from research.run_experiment import timestamp
from research.study_results import TESTS, STATUSES, index as original_index


def normalized_feedback(messages):
    normalized = []
    for position, message in enumerate(messages):
        if position > 1 and message['role'] == 'user':
            feedback, separator, suffix = message['content'].rpartition('\n')
            # JSON key order can change when a saved record is canonicalized;
            # compare its semantic content while preserving the exact suffix.
            normalized.append({**message, 'content': canonical(json.loads(feedback)).decode() + separator + suffix})
        else: normalized.append(message)
    return normalized


def read_study(directory: Path):
    plan = json.loads((directory / 'manifest.json').read_text())
    if digest(canonical({k: v for k, v in plan.items() if k != 'fingerprint'})) != plan['fingerprint']: raise ValueError('Iteration fingerprint mismatch')
    runs, reports, records = [], {}, {}
    for row in plan['schedule']:
        path = directory / 'runs' / row['runId'] / 'record.json'
        if not path.exists(): continue
        record = json.loads(path.read_text()); records[row['runId']] = record
        if record['manifestFingerprint'] != plan['fingerprint']: raise ValueError('Trajectory identity mismatch')
        prompt = (directory / next(c for c in plan['conditions'] if c['id'] == row['condition'])['promptFile']).read_bytes().decode()
        expected_messages = [{'role': 'system', 'content': plan['system']}, {'role': 'user', 'content': prompt}]
        for submission in record['submissions']:
            if digest(canonical(submission['request'])) != submission['requestSha256']: raise ValueError('Request hash mismatch')
            if submission.get('response') and digest(canonical(submission['response'])) != submission['responseSha256']: raise ValueError('Response hash mismatch')
            request = submission['request']
            if normalized_feedback(request['messages']) != normalized_feedback(expected_messages) or request['settings'] != plan['settings'] or request['model'] != plan['model'] or request['request_id'] != f"{row['runId']}-s{submission['number']}":
                raise ValueError('Request differs from frozen task, model, settings or retained feedback')
            if submission.get('response'): expected_messages.append({'role': 'assistant', 'content': submission['response']['output_text']})
            if submission.get('feedback'):
                expected_messages.append({'role': 'user', 'content': json.dumps(submission['feedback']) + f"\n{plan['maxSubmissions'] - submission['number']} submissions remain. Correct the current source using exact edits."})
        report = {}
        if record.get('finalEvaluation'):
            report_path = (path.parent / record['finalEvaluation']).resolve()
            if not report_path.is_relative_to(path.parent.resolve()): raise ValueError('Invalid evaluation path')
            report = json.loads(report_path.read_text())
            submission = next(s for s in record['submissions'] if s.get('evaluationFile') == record['finalEvaluation'])
            if digest(canonical(report)) != submission['evaluationCanonicalSha256'] or report['responseSha256'] != submission['completeResponseSha256']: raise ValueError('Evaluation lineage mismatch')
            if plan.get('evaluationProtocol') and report['protocol'] != plan['evaluationProtocol']: raise ValueError('Unexpected evaluator protocol')
            if any(name in plan['sourceHashes'] and sha != plan['sourceHashes'][name] for name, sha in report['inputHashes'].items()): raise ValueError('Evaluation used different frozen inputs')
            if submission['response']['model'] != plan['model']: raise ValueError('Nonmatching model evaluation')
            controls = ((report.get('security') or {}).get('controls') or {}).get('controls', [])
            if any(c['status'] in ('pass', 'fail') for c in (report.get('security') or {}).get('checks', [])) and not (controls and all(c['validated'] for c in controls)):
                raise ValueError('Security controls were not validated')
            reports[row['runId']] = report
        responses = [s['response'] for s in record['submissions'] if s.get('response')]
        usage = {key: sum(r['usage'][key] for r in responses) if responses and all(isinstance((r.get('usage') or {}).get(key), int) for r in responses) else None
                 for key in ('input_tokens', 'output_tokens', 'reasoning_tokens', 'cached_input_tokens')}
        first = record['submissions'][0] if record['submissions'] else {}
        runs.append({**row, 'status': record['status'], 'errorCategory': record.get('errorCategory'), 'evaluationStatus': report.get('status'),
                     'mainCompilation': report.get('mainCompilation'), 'functionalSuccess': record.get('functionalSuccess'),
                     'finishReason': responses[-1]['finish_reason'] if responses else None, 'usage': usage,
                     'submissions': len(record['submissions']), 'firstFunctionalSuccess': first.get('functionalSuccess') is True,
                     'firstCompilation': first.get('compilation'), 'checks': report.get('checks', []) + (report.get('security') or {}).get('checks', [])})
    conditions = []
    for condition in plan['conditions']:
        selected = [r for r in runs if r['condition'] == condition['id']]
        checks = []
        for test in TESTS:
            counts = {status: 0 for status in STATUSES}
            for run in selected:
                check = next((c for c in run['checks'] if c['suite'] == test['suite'] and c['name'] == test['name']), None)
                counts[check['status'] if check else 'not_run'] += 1
            n = counts['pass'] + counts['fail']
            checks.append({**test, **counts, 'attempts': len(selected), 'executed': n, 'passRate': counts['pass'] / n if n else None,
                           'allAttemptRate': counts['pass'] / len(selected) if selected else None})
        conditions.append({'id': condition['id'], 'attempts': len(selected), 'planned': condition['repetitions'],
            'pending': sum(r['status'] == 'started' for r in selected), 'compiled': sum(r['mainCompilation'] == 'pass' for r in selected),
            'fullFunctional': sum(r['functionalSuccess'] is True for r in selected), 'firstFullFunctional': sum(r['firstFunctionalSuccess'] for r in selected),
            'modelSubmissions': sum(r['submissions'] for r in selected),
            'functionalChecksPassed': sum(c['pass'] for c in checks if c['kind'] == 'functional'),
            'securityChecksPassed': sum(c['pass'] for c in checks if c['kind'] == 'security'),
            'securityChecksExecuted': sum(c['executed'] for c in checks if c['kind'] == 'security'),
            'unverifiedSettings': sum(records[r['runId']]['settingsVerified'] is False for r in selected),
            'transportErrors': sum(r['status'] in ('adapter_error', 'identity_mismatch', 'settings_mismatch', 'interrupted', 'incomplete_response') for r in selected), 'checks': checks})
    acquisitions = []
    for item in plan['acquisitions']:
        record = json.loads((ROOT / '.local/context-generation' / item['id'] / 'record.json').read_text())
        acquisitions.append({**item, 'snapshotFingerprint': record['snapshotFingerprint'], 'taskSha256': digest(record['task'].encode()),
            'status': record['status'], 'settingsVerified': record['settingsVerified'], 'items': len(record['output']['items']), 'citationChecks': record['citationChecks']})
    display_plan = {**plan, 'label': plan['id'].split('-')[0].upper(), 'observationUnit': 'trajectory', 'reasoning': plan['settings']['reasoning_effort'],
                    'axes': {'method': ['Generation', 'Reuse'], 'paperContext': list(dict.fromkeys(c['baseContext'] for c in plan['conditions'])), 'securityContext': list(dict.fromkeys(c['securityStrategy'] for c in plan['conditions']))},
                    'acquisitions': acquisitions, 'deviations': list(plan['analysis'].values())}
    note = ROOT / 'research/iterations' / plan['id'] / 'assessment.json'
    if note.exists(): display_plan['evaluationNote'] = json.loads(note.read_text())['message']
    return {'plan': display_plan, 'summary': {'complete': all(c['attempts'] == c['planned'] and c['pending'] == 0 for c in conditions),
            'conditions': conditions, 'ranking': {}, 'selected': []}, 'runs': runs}


def index(public=False, iteration=None):
    pointer = ROOT / ('research/iterations/current.json' if public else '.local/iterations/active.json')
    if iteration is None and pointer.exists(): iteration = json.loads(pointer.read_text())['id']
    if not iteration or iteration == 'original': return original_index(public=public)
    if not re.fullmatch(r'[a-z0-9][a-z0-9_-]+', iteration): raise ValueError('Invalid iteration')
    if public:
        path = ROOT / 'research/iterations' / iteration / 'results.json'
        if not path.exists(): return original_index(public=True)
        data = json.loads(path.read_text()); data['local'] = False; return data
    return {'local': True, 'tests': TESTS, 'studies': [read_study(ROOT / '.local/iterations' / iteration)], 'analysisSha256': digest(Path(__file__).read_bytes())}


def save(identifier: str):
    data = index(iteration=identifier)
    directory = ROOT / 'research/iterations' / identifier
    directory.mkdir(parents=True, exist_ok=True)
    (directory / 'results.json').write_bytes(canonical(data))
    (ROOT / 'research/iterations/current.json').write_bytes(canonical({'id': identifier}))
    study = data['studies'][0]
    lines = [f'# {identifier}: results', '', f'Saved {timestamp()}. Collection complete: {study["summary"]["complete"]}.', '',
             f'N counts code-generation trajectories sharing the acquired context within each arm; each permits up to {study["plan"]["maxSubmissions"]} model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.', '',
             '| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |',
             '| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |']
    if study['plan'].get('evaluationNote'): lines[4:4] = [study['plan']['evaluationNote'], '']
    for row in study['summary']['conditions']:
        security = [c for c in row['checks'] if c['suite'] == 'security_v1' and c['name'] != 'validRecordRoundTrip']
        fail, evaluated = sum(c['fail'] for c in security), sum(c['executed'] for c in security)
        lines += [f"| {row['id']} | {row['attempts']} | {row['firstFullFunctional']} | {row['fullFunctional']} | {row['compiled']} | {fail}/{evaluated} | {10 * row['attempts'] - evaluated} | {row['modelSubmissions']} |"]
    lines += ['', 'All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.', '']
    (directory / 'results.md').write_text('\n'.join(lines))
    print('\n'.join(lines))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--iteration')
    parser.add_argument('--save', action='store_true')
    parser.add_argument('--write-public', action='store_true')
    args = parser.parse_args()
    if args.save:
        if not args.iteration: parser.error('--iteration is required to save')
        save(args.iteration)
    elif args.write_public:
        directory = ROOT / 'workbench/public/data'; directory.mkdir(parents=True, exist_ok=True)
        (directory / 'original-matrix.json').write_bytes(canonical(original_index(public=True)))
        (directory / 'matrix.json').write_bytes(canonical(index(public=True)))
    else: print(json.dumps(index(iteration=args.iteration)))
