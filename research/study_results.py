"""Study-level denominators, test-level evidence and predeclared screening selection."""
from __future__ import annotations

import argparse
import json
from pathlib import Path

from research.import_evidence import ROOT, TEST_NAMES, canonical, digest
from research.evaluate_security import CHECKS
from research.study_execution import evaluable

TESTS = [{'id': f'{suite}.{name}', 'suite': suite, 'name': name, 'kind': 'security' if suite == 'security_v1' else 'functional'}
         for suite, names in {**{k: TEST_NAMES[k] for k in ('unit', 'invoked', 'autonomous')}, 'security_v1': CHECKS}.items() for name in names]
STATUSES = ('pass', 'fail', 'not_run', 'unknown', 'compile_error', 'infrastructure_error')


def manifests():
    return sorted((ROOT / 'research/studies').glob('*/manifest.json')) + sorted((ROOT / '.local/studies').glob('*/manifest.json'))


def summarize(plan: dict, observations: list[dict], reports: dict[str, dict]) -> dict:
    rows = []
    for condition in plan['conditions']:
        runs = [r for r in observations if r['condition'] == condition['id']]
        checks = []
        for test in TESTS:
            counts = {s: 0 for s in STATUSES}
            for run in runs:
                report = reports.get(run['runId'], {})
                observed = report.get('checks', []) + (report.get('security') or {}).get('checks', [])
                found = next((c for c in observed if f"{c['suite']}.{c['name']}" == test['id']), None)
                counts[found['status'] if found else 'not_run'] += 1
            n = counts['pass'] + counts['fail']
            checks.append({**test, **counts, 'attempts': len(runs), 'executed': n,
                           'passRate': counts['pass'] / n if n else None, 'allAttemptRate': counts['pass'] / len(runs) if runs else None})
        pending = sum(r['status'] == 'started' or (evaluable(r, plan) and reports.get(r['runId'], {}).get('status') not in ('evaluated', 'evaluation_error')) for r in runs)
        compiled = sum(reports.get(r['runId'], {}).get('mainCompilation') == 'pass' for r in runs)
        full = sum(reports.get(r['runId'], {}).get('functionalSuccess') is True for r in runs)
        rows.append({'id': condition['id'], 'attempts': len(runs), 'planned': condition['repetitions'], 'pending': pending,
                     'compiled': compiled, 'fullFunctional': full, 'functionalChecksPassed': sum(c['pass'] for c in checks if c['kind'] == 'functional'),
                     'securityChecksPassed': sum(c['pass'] for c in checks if c['kind'] == 'security'),
                     'securityChecksExecuted': sum(c['executed'] for c in checks if c['kind'] == 'security'),
                     'unverifiedSettings': sum(r['status'] == 'settings_unverified' for r in runs),
                     'transportErrors': sum(r['status'] in ('adapter_error', 'identity_mismatch', 'interrupted') for r in runs),
                     'checks': checks})
    complete = all(r['attempts'] == r['planned'] and r['pending'] == 0 for r in rows)
    ranking, selected = {}, []
    if complete and plan.get('phase') == 'screening':
        by_id = {r['id']: r for r in rows}
        for method in ('Generation', 'Reuse'):
            conditions = [c for c in plan['conditions'] if c['strategy'] == method]
            conditions.sort(key=lambda c: (-by_id[c['id']]['fullFunctional'] / by_id[c['id']]['attempts'],
                                           -by_id[c['id']]['functionalChecksPassed'] / (16 * by_id[c['id']]['attempts']),
                                           -by_id[c['id']]['compiled'] / by_id[c['id']]['attempts'], c['paperPromptId']))
            ranking[method] = [c['id'] for c in conditions]
            selected.extend(ranking[method][:plan['selection']['perMethod']])
    return {'complete': complete, 'conditions': rows, 'ranking': ranking, 'selected': selected}


def study(path: Path, *, public=False) -> dict:
    plan = json.loads(path.read_text())
    if digest(canonical({k: v for k, v in plan.items() if k != 'fingerprint'})) != plan['fingerprint']: raise ValueError('Manifest fingerprint mismatch')
    directory = ROOT / '.local/experiments' / plan['id']
    observations, reports = [], {}
    if not public:
        conditions = {c['id']: c for c in plan['conditions']}
        signatures, source_hashes = set(), {}
        for row in plan['schedule']:
            p = directory / 'runs' / f"{row['runId']}.json"
            if not p.exists(): continue
            observation = json.loads(p.read_text())
            if observation['manifestFingerprint'] != plan['fingerprint'] or observation['condition'] != row['condition']: raise ValueError('Observation identity mismatch')
            request = observation.get('request', {})
            messages = request.get('messages', [])
            condition = conditions[row['condition']]
            if len(messages) != 1 or messages[0].get('role') != 'user' or digest(messages[0].get('content', '').encode()) != condition['promptSha256']:
                raise ValueError('Observation prompt differs from the frozen condition')
            expected = {'protocol_version': 1, 'request_id': row['runId'], 'model': plan['model'], 'messages': messages,
                        'settings': {'reasoning_effort': plan['reasoning'], 'temperature': plan['temperature'], 'max_output_tokens': plan['maxOutputTokens']}}
            if request != expected or observation.get('requestSha256') != digest(canonical(expected)): raise ValueError('Observation request mismatch')
            observations.append(observation)
            r = directory / 'evaluations' / row['runId'] / 'report.json'
            if r.exists():
                report = json.loads(r.read_text())
                if report.get('inputFileSha256') != digest(p.read_bytes()) or report.get('responseSha256') != digest(observation.get('response', {}).get('output_text', '').encode()):
                    raise ValueError('Evaluation response lineage mismatch')
                if not evaluable(observation, plan) or report.get('manifestFingerprint') != plan['fingerprint'] or report.get('runId') != row['runId']:
                    raise ValueError('Evaluation study identity mismatch')
                for name, sha in report['inputHashes'].items():
                    source = (ROOT / name).resolve()
                    if not source.is_relative_to(ROOT): raise ValueError('Evaluator source outside repository')
                    if name not in source_hashes: source_hashes[name] = digest(source.read_bytes())
                    if source_hashes[name] != sha: raise ValueError('Evaluator source changed; preserve versions and reevaluate explicitly')
                seen = set()
                for check in report.get('checks', []) + (report.get('security') or {}).get('checks', []):
                    key = f"{check['suite']}.{check['name']}"
                    if key in seen or key not in {t['id'] for t in TESTS} or check['status'] not in STATUSES: raise ValueError('Invalid test observation')
                    seen.add(key)
                security = report.get('security') or {}
                controls = (security.get('controls') or {}).get('controls', [])
                if any(c['status'] in ('pass', 'fail') for c in security.get('checks', [])) and (not controls or not all(c['validated'] for c in controls)):
                    raise ValueError('Security observations require validated controls')
                signatures.add(digest(canonical({'inputs': report['inputHashes'], 'environment': report['environment']})))
                reports[row['runId']] = report
        if len(signatures) > 1: raise ValueError('Mixed evaluator versions or environments; do not pool these observations')
    compact = [{k: r.get(k) for k in ('runId', 'condition', 'repetition', 'status', 'startedAt', 'elapsedSeconds', 'errorCategory')} |
               {'usage': (r.get('response') or {}).get('usage'), 'finishReason': (r.get('response') or {}).get('finish_reason'),
                'evaluationStatus': reports.get(r['runId'], {}).get('status'), 'mainCompilation': reports.get(r['runId'], {}).get('mainCompilation'),
                'functionalSuccess': reports.get(r['runId'], {}).get('functionalSuccess'),
                'checks': reports.get(r['runId'], {}).get('checks', []) + (reports.get(r['runId'], {}).get('security') or {}).get('checks', [])}
               for r in observations]
    return {'plan': plan, 'summary': summarize(plan, observations, reports), 'runs': compact}


def index(public=False):
    return {'local': not public, 'tests': TESTS, 'studies': [study(p, public=public) for p in manifests() if not public or '.local' not in p.parts]}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--public', action='store_true')
    parser.add_argument('--write-public', action='store_true')
    args = parser.parse_args()
    data = index(public=args.public or args.write_public)
    if args.write_public:
        directory = ROOT / 'workbench/public/data'
        directory.mkdir(parents=True, exist_ok=True)
        (directory / 'matrix.json').write_bytes(canonical(data))
        prompts = directory / 'matrix-prompts'; prompts.mkdir(exist_ok=True)
        for p in manifests():
            if '.local' in p.parts: continue
            plan = json.loads(p.read_text())
            for c in plan['conditions']:
                (prompts / f"{c['promptSha256']}.txt").write_bytes((p.parent / c['promptFile']).read_bytes())
    else: print(json.dumps(data))
