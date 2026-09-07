"""Current studies only. Private run/evaluation directories must be selected explicitly."""
import json
import zipfile
from pathlib import Path
from research.import_evidence import canonical, digest
from research.test_catalog import catalog

STATUSES = {'pass', 'fail', 'not_run', 'unknown', 'compile_error', 'infrastructure_error'}


def verify_fingerprint(value):
    if digest(canonical({k: v for k, v in value.items() if k != 'fingerprint'})) != value['fingerprint']:
        raise ValueError('Research fingerprint mismatch')


def build_study(i):
    extraction = i.read('research/results/security-context-v1.json')
    verify_fingerprint(extraction)
    for path, sha in extraction['inputHashes'].items():
        if i.artifact(path)['sha256'] != sha: raise ValueError('Context source changed')
    for source in extraction['sourceManifest'].values():
        path = (i.root / source['path']).resolve()
        if not path.is_relative_to(i.root.resolve()): raise ValueError('Source outside repository')
        if source['member']:
            with zipfile.ZipFile(path) as archive: content = archive.read(source['member'])
        else: content = path.read_bytes()
        if digest(content) != source['sha256']: raise ValueError('Parsed source changed')
    extraction['artifact'] = i.artifact('research/results/security-context-v1.json')
    plans = []
    for path in sorted((i.root / 'research/experiments').glob('*/manifest.json')):
        plan = i.read(path)
        verify_fingerprint(plan)
        if plan['contextFingerprint'] != extraction['fingerprint']: raise ValueError('Plan context mismatch')
        for source, sha in plan['sourceHashes'].items():
            if i.artifact(source)['sha256'] != sha: raise ValueError('Plan source changed')
        for condition in plan['conditions']:
            condition['prompt'] = i.artifact(path.parent / condition['promptFile'])
            if condition['prompt']['sha256'] != condition['promptSha256']: raise ValueError('Planned prompt changed')
        plan['artifact'] = i.artifact(path)
        plans.append(plan)
    tests = catalog(i)
    reports = {}
    if i.evaluations_dir:
        if not i.evaluations_dir.is_dir(): raise ValueError('Evaluation directory does not exist')
        for path in sorted(i.evaluations_dir.rglob('report.json')):
            report = json.loads(path.read_text())
            # Calibration files and old responses cannot become new model attempts.
            if report.get('inputKind') != 'model_observation': continue
            key = (report.get('manifestFingerprint'), report.get('runId'))
            if key in reports: raise ValueError('Duplicate evaluation for an attempt')
            reports[key] = (report, path)
    runs = []
    if i.runs_dir:
        if not i.runs_dir.is_dir(): raise ValueError('Run directory does not exist')
        for path in sorted(i.runs_dir.glob('*.json')):
            observation = i.read(path)
            plan = next((p for p in plans if p['fingerprint'] == observation.get('manifestFingerprint')), None)
            if not plan: raise ValueError('Run does not belong to a current study')
            row = next((r for r in plan['schedule'] if r['runId'] == observation.get('runId')), None)
            if not row or row['condition'] != observation.get('condition'): raise ValueError('Run is not in frozen schedule')
            if any(r['id'] == row['runId'] and r['planId'] == plan['id'] for r in runs): raise ValueError('Duplicate attempt')
            condition = next(c for c in plan['conditions'] if c['id'] == row['condition'])
            request = observation.get('request', {})
            settings = {'reasoning_effort': plan['reasoning'], 'temperature': plan['temperature'], 'max_output_tokens': plan['maxOutputTokens']}
            expected = {'protocol_version': 1, 'request_id': row['runId'], 'model': plan['model'], 'settings': settings,
                        'messages': [{'role': 'user', 'content': (i.root / condition['prompt']['path']).read_text()}]}
            if request != expected or observation.get('requestSha256') != digest(canonical(expected)):
                raise ValueError('Run request does not match frozen prompt/settings')
            response = observation.get('response') or {}
            if observation['status'] == 'completed' and any(response.get(k) != expected[k] for k in ('model', 'settings', 'request_id')):
                raise ValueError('Completed response has mismatched settings')
            evaluation, evaluation_path = reports.get((plan['fingerprint'], row['runId']), ({}, None))
            observed = {}
            evaluation_artifact = None
            if evaluation:
                if observation['status'] != 'completed': raise ValueError('Cannot evaluate an unverified response')
                if evaluation.get('inputFileSha256') != digest(path.read_bytes()) or evaluation.get('responseSha256') != digest(response.get('output_text', '').encode()):
                    raise ValueError('Evaluation is attached to different response bytes')
                for source, sha in evaluation['inputHashes'].items():
                    if i.artifact(source)['sha256'] != sha: raise ValueError('Evaluation source changed')
                security = evaluation.get('security') or {}
                controls = (security.get('controls') or {}).get('controls', [])
                if any(c['status'] in ('pass', 'fail') for c in security.get('checks', [])) and (not controls or not all(c['validated'] for c in controls)):
                    raise ValueError('Security results require validated controls')
                for check in evaluation.get('checks', []) + security.get('checks', []):
                    key = f"{check['suite']}.{check['name']}"
                    if key in observed or key not in {t['id'] for t in tests} or check['status'] not in STATUSES: raise ValueError('Invalid or duplicate test observation')
                    observed[key] = check
                evaluation_artifact = i.artifact(evaluation_path)
            normalized = [{**observed.get(t['id'], {'status': 'not_run', 'detail': 'No evaluation recorded.'}), 'id': t['id'], 'suite': t['suite'], 'name': t['name']} for t in tests]
            signature = digest(canonical({'inputs': evaluation.get('inputHashes'), 'environment': evaluation.get('environment')})) if evaluation else None
            runs.append({'id': row['runId'], 'planId': plan['id'], 'condition': condition['id'], 'stage': condition['stage'],
                         'strategy': condition['strategy'], 'repetition': row['repetition'], 'model': plan['model'], 'reasoning': plan['reasoning'],
                         'status': observation['status'], 'errorCategory': observation.get('errorCategory'),
                         'startedAt': observation.get('startedAt'), 'elapsedSeconds': observation.get('elapsedSeconds'),
                         'usage': response.get('usage', {}), 'costUsd': response.get('cost_usd'), 'finishReason': response.get('finish_reason'),
                         'compileStatus': evaluation.get('mainCompilation', 'not_run'), 'checks': normalized,
                         'evaluationSignature': signature, 'observation': i.artifact(path), 'evaluation': evaluation_artifact,
                         'prompt': condition['prompt'], 'factIds': condition['factIds'], 'contextTypes': condition['contextTypes']})
    data = {'schemaVersion': 2, 'title': 'Highscore experiments', 'runs': runs, 'tests': tests,
            'contextExtraction': extraction, 'experimentPlans': plans,
            'artifacts': sorted(i.artifacts.values(), key=lambda a: a['path'])}
    data['fingerprint'] = digest(canonical(data))
    return data
