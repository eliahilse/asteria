"""Equal trajectory denominators for distinct security-check categories."""
import argparse
from collections import defaultdict
from pathlib import Path

from research.import_evidence import ROOT, canonical, digest
from research.iteration_results import read_study
from research.qualification import apply_available
from research.scientific_summary import ISSUES, csv_bytes, wilson


def category_status(checks, names):
    statuses = [next((c['status'] for c in checks if c['suite'] == 'security_v1' and c['name'] == name), 'not_run') for name in names]
    if 'fail' in statuses: return 'affected'
    if all(status == 'pass' for status in statuses): return 'unaffected'
    return 'unresolved'


def summarize(study):
    if not study['summary']['complete']: raise ValueError('A complete fixed schedule is required')
    categories = defaultdict(list)
    for name, (category, _) in ISSUES.items(): categories[category].append(name)
    rows = []
    for condition in study['plan']['conditions']:
        runs = [r for r in study['runs'] if r['condition'] == condition['id']]; n = len(runs)
        for category, names in categories.items():
            statuses = [category_status(run['checks'], names) for run in runs]
            affected = statuses.count('affected'); unresolved = statuses.count('unresolved')
            interval = wilson(affected, n) if not unresolved else None
            rows.append({'condition': condition['id'], 'parent': condition['parentCondition'], 'strategy': condition['securityStrategy'],
                'category': category, 'checksPerArtifact': len(names), 'n': n, 'affected': affected,
                'unaffected': statuses.count('unaffected'), 'unresolved': unresolved,
                'affectedRateLowerBound': affected / n, 'affectedRateUpperBound': (affected + unresolved) / n,
                'wilson95LowWhenComplete': interval[0] if interval else None, 'wilson95HighWhenComplete': interval[1] if interval else None})
    for row in rows:
        control = next(c for c in rows if c['parent'] == row['parent'] and c['strategy'] == 'none' and c['category'] == row['category'])
        if row['strategy'] == 'none': continue
        row['deltaLowerBound'] = row['affectedRateLowerBound'] - control['affectedRateUpperBound']
        row['deltaUpperBound'] = row['affectedRateUpperBound'] - control['affectedRateLowerBound']
    return rows


def save(identifier):
    study, metadata = apply_available(read_study(ROOT / '.local/iterations' / identifier))
    rows = summarize(study)
    result = {'iteration': identifier, 'manifestFingerprint': study['plan']['fingerprint'], 'measurementQualification': metadata,
        'analysisSha256': digest(Path(__file__).read_bytes()), 'categories': rows,
        'meaning': 'An affected trajectory has at least one observed failed check in the category. Unaffected requires every category check to pass. Otherwise unresolved. A known failure establishes affected status even when other category checks are unknown. Categories use the same N trajectories; five input checks do not become five independent samples. Bounds cover unmeasured category status, not confidence. These are fixture/policy outcomes, not counts of unique vulnerabilities.'}
    directory = ROOT / 'research/iterations' / identifier
    (directory / 'qualified-category-analysis.json').write_bytes(canonical(result))
    (directory / 'qualified-categories.csv').write_bytes(csv_bytes(rows))
    lines = [f'# {identifier}: trajectories affected by security-check category', '', result['meaning'], '',
        '| Condition | Category | Affected / N | Unaffected | Unresolved | Affected-rate Δ bounds, pp |',
        '| --- | --- | ---: | ---: | ---: | ---: |']
    for r in rows:
        bounds = 'Control' if r['strategy'] == 'none' else f"[{100*r['deltaLowerBound']:+.0f}, {100*r['deltaUpperBound']:+.0f}]"
        lines.append(f"| {r['condition']} | {r['category']} | {r['affected']}/{r['n']} | {r['unaffected']} | {r['unresolved']} | {bounds} |")
    lines += ['', 'Input and retention policies are separate from parser robustness, resource stress and the deserialization-dispatch canary. This grouping is an exploratory analysis introduced after I04; original counts and complete per-check results remain available. No significance claim or category-weighted combined score is introduced.', '']
    (directory / 'qualified-categories.md').write_text('\n'.join(lines))
    print(f'{identifier}: {len(rows)} category rows; each category uses the trajectory denominator')
    return result


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration', required=True)
    save(parser.parse_args().iteration)
