"""Transparent per-test effects and uncertainty for a completed frozen iteration.

No pooling of checks as independent samples, no outcome selection, no p-value
search. Missing security observations remain explicit in conservative bounds.
"""
from __future__ import annotations

import argparse
import csv
import io
import json
import math
from pathlib import Path

from research.import_evidence import ROOT, canonical, digest
from research.iteration_results import read_study

WILSON_SOURCE = 'https://www.itl.nist.gov/div898/handbook/prc/section2/prc241.htm'
ISSUES = {
    'rejectsNegativeScore': ('Input policy', 'Negative score rejected and not retained.'),
    'rejectsNegativeTime': ('Input policy', 'Negative time rejected and not retained.'),
    'rejectsNullName': ('Input policy', 'Null name rejected and not retained.'),
    'rejectsBlankName': ('Input policy', 'Whitespace-only name rejected and not retained.'),
    'rejectsExcessiveName': ('Input policy', 'The declared 1024-character name fixture is rejected and not retained.'),
    'boundsRetainedEntries': ('Retention policy', 'At most 100 entries retained after 120 valid additions.'),
    'malformedStoreDoesNotCrash': ('Parser robustness', 'The declared malformed store does not crash loading.'),
    'oversizedPhysicalLine': ('Resource stress', 'The declared 64 MiB physical line loads or is rejected within the 64 MiB heap and process budget.'),
    'nativeDeserializationCanary': ('Deserialization dispatch', 'A test-only readObject hook does not execute before record validation; failure does not establish a production RCE chain.'),
    'largePersistedRecordSet': ('Resource stress', 'Supported seed formats are amplified to one million records; loading retains at most 100 within the declared heap/time limits. Unsupported encodings are unknown.'),
}


def wilson(successes: int, total: int):
    if not 0 <= successes <= total: raise ValueError('Invalid binomial counts')
    if total == 0: return None
    z = 1.959963984540054; proportion = successes / total
    denominator = 1 + z * z / total
    midpoint = (proportion + z * z / (2 * total)) / denominator
    radius = z * math.sqrt(proportion * (1 - proportion) / total + z * z / (4 * total * total)) / denominator
    return [max(0., midpoint - radius), min(1., midpoint + radius)]


def issue_totals(condition):
    checks = [c for c in condition['checks'] if c['suite'] == 'security_v1' and c['name'] in ISSUES]
    failures, evaluated = sum(c['fail'] for c in checks), sum(c['executed'] for c in checks)
    return {'failed': failures, 'evaluated': evaluated, 'unresolved': len(ISSUES) * condition['attempts'] - evaluated,
            'plannedChecks': len(ISSUES) * condition['attempts']}


def summarize(study):
    if not study['summary']['complete']: raise ValueError('Complete the frozen schedule before reporting inferential intervals')
    by_id = {c['id']: c for c in study['summary']['conditions']}
    conditions, comparisons, per_test = [], [], []
    for condition in study['plan']['conditions']:
        row = by_id[condition['id']]; n = row['attempts']
        full = row['fullFunctional']; first = row['firstFullFunctional']
        issues = issue_totals(row)
        runs = [r for r in study['runs'] if r['condition'] == condition['id']]
        all_security_pass = sum(r['functionalSuccess'] is True and
            all(any(c['suite'] == 'security_v1' and c['name'] == name and c['status'] == 'pass' for c in r['checks']) for name in [*ISSUES, 'validRecordRoundTrip']) for r in runs)
        conditions.append({'condition': row['id'], 'method': condition['strategy'], 'paperContext': condition['baseContext'], 'securityStrategy': condition['securityStrategy'],
            'n': n, 'compiled': row['compiled'], 'firstFull': first, 'withinBudgetFull': full,
            'firstFullRate': first / n, 'firstFullWilson95': wilson(first, n),
            'withinBudgetFullRate': full / n, 'withinBudgetFullWilson95': wilson(full, n),
            'functionalAndAllDeclaredSecurityPass': all_security_pass, 'issues': issues,
            'observedFailuresPerTrajectory': issues['failed'] / n,
            'failureCountIdentificationBoundsPerTrajectory': [issues['failed'] / n, (issues['failed'] + issues['unresolved']) / n],
            'modelSubmissions': row['modelSubmissions'], 'unverifiedSettings': row['unverifiedSettings'], 'transportErrors': row['transportErrors']})
        for check in row['checks']:
            per_test.append({'condition': row['id'], 'suite': check['suite'], 'test': check['name'], 'n': n,
                'passed': check['pass'], 'failed': check['fail'], 'evaluated': check['executed'], 'unresolved': n - check['executed'],
                'passRateAmongEvaluated': check['pass'] / check['executed'] if check['executed'] else None,
                'passWilson95AmongEvaluated': wilson(check['pass'], check['executed']), 'passRateAllTrajectories': check['pass'] / n})
        if condition['securityStrategy'] == 'none': continue
        control = next(c for c in study['plan']['conditions'] if c['parentCondition'] == condition['parentCondition'] and c['securityStrategy'] == 'none')
        base = by_id[control['id']]; bn = base['attempts']; base_issues = issue_totals(base)
        tests = []
        for name in ISSUES:
            target = next(c for c in row['checks'] if c['suite'] == 'security_v1' and c['name'] == name)
            source = next(c for c in base['checks'] if c['suite'] == 'security_v1' and c['name'] == name)
            complete = target['executed'] == n and source['executed'] == bn
            delta = target['fail'] / n - source['fail'] / bn if complete else None
            tests.append({'test': name, 'category': ISSUES[name][0], 'controlFailed': source['fail'], 'controlEvaluated': source['executed'],
                'controlUnresolved': bn - source['executed'], 'treatmentFailed': target['fail'], 'treatmentEvaluated': target['executed'],
                'treatmentUnresolved': n - target['executed'], 'failureRateDelta': delta,
                'direction': 'unresolved' if delta is None else ('decreased' if delta < 0 else 'increased' if delta > 0 else 'equal')})
        lower = issues['failed'] / n - (base_issues['failed'] + base_issues['unresolved']) / bn
        upper = (issues['failed'] + issues['unresolved']) / n - base_issues['failed'] / bn
        comparisons.append({'parent': condition['parentCondition'], 'control': control['id'], 'treatment': condition['id'], 'securityStrategy': condition['securityStrategy'],
            'functionalRateDelta': full / n - base['fullFunctional'] / bn,
            'firstFunctionalRateDelta': first / n - base['firstFullFunctional'] / bn,
            'failureCountDeltaIdentificationBoundsPerTrajectory': [lower, upper],
            'descriptiveDirectionRegardlessOfMissing': 'decreased' if upper < 0 else 'increased' if lower > 0 else 'unresolved_or_equal',
            'perCheckDirections': {d: sum(c['direction'] == d for c in tests) for d in ('decreased', 'equal', 'increased', 'unresolved')}, 'tests': tests})
    acquisitions = []
    for acquisition in study['plan']['acquisitions']:
        record = json.loads((ROOT / '.local/context-generation' / acquisition['id'] / 'record.json').read_text())
        acquisitions.append({**acquisition, 'modelTurns': len(record['turns']), 'inspectedFiles': record['inspectedFiles'], 'inspectedLines': record['inspectedLines'],
            'promptInsertCharacters': len(record['promptInsert']), 'promptInsertSha256': record['promptInsertSha256'],
            'topics': [i['topic'] for i in record['output']['items']], 'basisCounts': {b: sum(i.get('basis') == b for i in record['output']['items']) for b in ('observed', 'task', 'reasoned')}})
    return {'schemaVersion': 1, 'iteration': study['plan']['id'], 'manifestFingerprint': study['plan']['fingerprint'],
        'analysisSha256': digest(Path(__file__).read_bytes()), 'conditions': conditions, 'comparisons': comparisons, 'perTest': per_test,
        'acquisitions': acquisitions, 'issueDefinitions': ISSUES,
        'methods': {'intervals': 'Wilson 95% intervals for individual binomial proportions, conditional on the fixed context and task. Descriptive marginal intervals, without multiplicity adjustment; no confirmatory significance claim.',
            'source': WILSON_SOURCE,
            'missingness': 'Failure-count identification bounds assign every unresolved check first to pass then to fail. They are not confidence intervals or estimates of real-world vulnerability prevalence.',
            'perCheckDirection': 'A directional per-check comparison is reported only if every trajectory in both arms has an observed pass/fail for that check.',
            'unit': 'Code-generation trajectory, including all its submissions. Ten checks on one artifact are not ten independent samples. One context acquisition per method/strategy is shared within the iteration.',
            'scope': 'The ten issue checks include policy contracts, finite robustness/resource fixtures and a deserialization-dispatch canary. They do not enumerate all vulnerabilities. The positive persistence round trip is reported separately.'}}


def csv_bytes(rows):
    output = io.StringIO(); writer = csv.DictWriter(output, fieldnames=list(dict.fromkeys(k for row in rows for k in row)))
    writer.writeheader()
    for row in rows: writer.writerow({k: json.dumps(v) if isinstance(v, (list, dict)) else v for k, v in row.items()})
    return output.getvalue().encode()


def save(identifier):
    directory = ROOT / 'research/iterations' / identifier
    result = summarize(read_study(ROOT / '.local/iterations' / identifier)); directory.mkdir(parents=True, exist_ok=True)
    (directory / 'analysis.json').write_bytes(canonical(result))
    (directory / 'per-test.csv').write_bytes(csv_bytes(result['perTest']))
    (directory / 'security-effects.csv').write_bytes(csv_bytes([{'parent': c['parent'], 'strategy': c['securityStrategy'], **t} for c in result['comparisons'] for t in c['tests']]))
    lines = [f'# {identifier}: per-test effects and uncertainty', '',
        'All scheduled trajectories are included. First-submission and within-budget success use the full trajectory denominator; rejected edits and transport failures are retained.', '',
        '| Condition | N | First full | Within-budget full | Full rate, Wilson 95% | Joint functional + all 11 security | Issue failures / evaluated | Unresolved | Calls |',
        '| --- | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: |']
    for c in result['conditions']:
        lo, hi = c['withinBudgetFullWilson95']; issues = c['issues']
        lines.append(f"| {c['condition']} | {c['n']} | {c['firstFull']} | {c['withinBudgetFull']} | {100*c['withinBudgetFullRate']:.1f}% [{100*lo:.1f}, {100*hi:.1f}] | {c['functionalAndAllDeclaredSecurityPass']} | {issues['failed']}/{issues['evaluated']} | {issues['unresolved']} | {c['modelSubmissions']} |")
    lines += ['', '## Fresh-control comparisons', '',
        'Negative failure-count differences favor the security context. The displayed range covers every possible assignment of unresolved checks; it is an identification bound, **not a confidence interval**. Per-check directions require complete measurement in both arms.', '',
        '| Parent | Security strategy | Full Δ, percentage points | Issue-count Δ / trajectory, bounds | Checks decreased | Equal | Increased | Unresolved |',
        '| --- | --- | ---: | --- | ---: | ---: | ---: | ---: |']
    for c in result['comparisons']:
        lo, hi = c['failureCountDeltaIdentificationBoundsPerTrajectory']; d = c['perCheckDirections']
        lines.append(f"| {c['parent']} | {c['securityStrategy']} | {100*c['functionalRateDelta']:+.1f} | [{lo:+.2f}, {hi:+.2f}] | {d['decreased']} | {d['equal']} | {d['increased']} | {d['unresolved']} |")
    lines += ['', '## Every issue check', '', 'Each cell is failed/evaluated. Denominators smaller than N indicate unresolved measurements.', '',
        '| Check | ' + ' | '.join(c['condition'] for c in result['conditions']) + ' |', '| --- | ' + ' | '.join('---:' for _ in result['conditions']) + ' |']
    for name in ISSUES:
        cells = [next(t for t in result['perTest'] if t['condition'] == c['condition'] and t['suite'] == 'security_v1' and t['test'] == name) for c in result['conditions']]
        lines.append('| ' + name + ' | ' + ' | '.join(f"{t['failed']}/{t['evaluated']}" for t in cells) + ' |')
    lines += ['', '## Acquired context', '', '| Method | Strategy | Items | Recommendations | Unknowns | Model turns | Inspected files | Insert characters |', '| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |']
    for a in result['acquisitions']:
        kinds = a['citationChecks']['itemsByKind']
        lines.append(f"| {a['method']} | {a['strategy']} | {a['items']} | {kinds.get('recommendation', 0)} | {kinds.get('unknown', 0)} | {a['modelTurns']} | {a['inspectedFiles']} | {a['promptInsertCharacters']} |")
    lines += ['', '## Interpretation limits', '',
        f"Individual rate intervals use the [Wilson method described by NIST]({WILSON_SOURCE}). They describe uncertainty conditional on this fixed task and acquired context; they do not establish generalization across repositories or independently acquired contexts.", '',
        result['methods']['scope'], '', result['methods']['unit'], '',
        'The complete per-test rates and intervals are in `per-test.csv`; all fresh-control check effects are in `security-effects.csv`. Exact values and context provenance are in `analysis.json`. Context prompt inserts remain in `contexts/`. No results are selected for omission, and no significance claim is inferred from a favorable count.', '']
    (directory / 'analysis.md').write_text('\n'.join(lines))
    print(f"Saved {len(result['conditions'])} conditions, {len(result['perTest'])} test-rate rows and {len(result['comparisons']) * len(ISSUES)} issue comparisons")
    return result


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration', required=True)
    args = parser.parse_args(); save(args.iteration)
