"""Export a reviewable study snapshot with explicit test and attempt denominators."""
from __future__ import annotations

import argparse
import json
import math
from pathlib import Path

from research.import_evidence import ROOT, canonical, digest
from research.acquisition_diagnostics import diagnose
from research.run_experiment import timestamp
from research.study_results import index


def wilson(passes: int, attempts: int) -> tuple[float, float] | None:
    """Two-sided 95% Wilson score interval for a cell's Bernoulli response endpoint."""
    if not attempts: return None
    if not 0 <= passes <= attempts: raise ValueError('Invalid binomial counts')
    z, p = 1.959963984540054, passes / attempts
    d = 1 + z * z / attempts
    center = (p + z * z / (2 * attempts)) / d
    radius = z * math.sqrt(p * (1 - p) / attempts + z * z / (4 * attempts * attempts)) / d
    return max(0, center - radius), min(1, center + radius)


def rate(p: int, n: int) -> str:
    return f'{p}/{n} ({100 * p / n:.1f}%)' if n else '— (0 attempts)'


def interval(p: int, n: int) -> str:
    bounds = wilson(p, n)
    return f'{100 * bounds[0]:.1f}–{100 * bounds[1]:.1f}%' if bounds else '—'


def table(headers, rows):
    def cell(value): return str(value).replace('|', '\\|').replace('\n', ' ')
    return '\n'.join('| ' + ' | '.join(map(cell, row)) + ' |' for row in [headers, ['---'] * len(headers), *rows])


def comparisons(study):
    by_id = {c['id']: c for c in study['summary']['conditions']}
    for treatment in study['plan']['conditions']:
        if treatment['securityStrategy'] == 'none': continue
        control = next(c for c in study['plan']['conditions'] if c.get('parentCondition') == treatment['parentCondition'] and c['securityStrategy'] == 'none')
        a, b = by_id[control['id']], by_id[treatment['id']]
        controls = {t['id']: t for t in a['checks']}
        for t in b['checks']:
            c = controls[t['id']]
            yield {'parentCondition': treatment['parentCondition'], 'securityStrategy': treatment['securityStrategy'],
                   'control': control['id'], 'treatment': treatment['id'], 'test': t['id'], 'kind': t['kind'],
                   'controlCounts': c, 'treatmentCounts': t,
                   'allAttemptDeltaPp': 100 * (t['allAttemptRate'] - c['allAttemptRate']) if t['allAttemptRate'] is not None and c['allAttemptRate'] is not None else None,
                   'testedDeltaPp': 100 * (t['passRate'] - c['passRate']) if t['passRate'] is not None and c['passRate'] is not None else None}


def render(data: dict, created: str) -> str:
    lines = ['# Highscore context experiment', '', f'Results snapshot: {created}.', '',
             'Generation implements the feature from target ApoMario source. Reuse also receives the ApoIcarus donor implementation and reuse-prioritizing instructions.', '',
             'The paper matrix crosses these two methods with None, S, F, B, S+F, S+B, F+B and S+F+B. Each cell has five independent code requests. None still includes the task and target source.', '',
             'The follow-up evaluates the top two functional conditions per method against fresh no-security controls and three acquired security-context strategies. Security outcomes do not select conditions.', '',
             'Read the cell counts before interpreting percentages. Full functionality means all 16 functional checks pass on one response. Test/check totals are correlated outcomes, not additional independent replicates. Wilson intervals below apply only to the full-functionality endpoint within a cell, following the [NIST formula](https://www.itl.nist.gov/div898/handbook/prc/section2/prc241.htm). They assume independent draws with stable success probability in that cell; requested sampling settings remain unverified.', '']
    for study in data['studies']:
        plan, summary = study['plan'], study['summary']
        rows = {r['id']: r for r in summary['conditions']}
        n = sum(r['attempts'] for r in rows.values())
        lines += [f"## {plan['id']}", '', f"**{'Complete' if summary['complete'] else 'INCOMPLETE / provisional'}**: {n}/{len(plan['schedule'])} attempts recorded; {sum(r['pending'] for r in rows.values())} pending response or evaluation.", '',
                  f"Model: {plan['model']}; requested {plan['reasoning']} reasoning. {sum(r['unverifiedSettings'] for r in rows.values())} attempts lack an effective-settings attestation. No claim of verified sampling settings is made.", '',
                  f"Manifest SHA-256: `{plan['fingerprint']}`.", '']
        overview = []
        for c in plan['conditions']:
            r = rows[c['id']]
            suites = [rate(sum(t['pass'] for t in r['checks'] if t['suite'] == suite), count * r['attempts']) for suite, count in [('unit', 7), ('invoked', 4), ('autonomous', 5), ('security_v1', 11)]]
            overview.append([c['id'], rate(r['compiled'], r['attempts']), rate(r['fullFunctional'], r['attempts']), interval(r['fullFunctional'], r['attempts']), *suites])
        lines += [table(['Condition', 'Game compiled', 'Fully functional', '95% Wilson interval', 'Unit passes / 7n', 'Invoked passes / 4n', 'Autonomous passes / 5n', 'Security passes / 11n'], overview), '']
        if plan['phase'] == 'screening':
            lines += ['### Functional selection', '', 'Rule: ' + '; '.join(plan['selection']['order']) + '.', '']
            if summary['complete']:
                for method, ranking in summary['ranking'].items():
                    lines += [f"{method}: " + ' → '.join(ranking) + '.', '']
                lines += ['Selected: ' + ', '.join(summary['selected']) + '.', '']
                if not any(r['fullFunctional'] for r in rows.values()):
                    lines += ['No baseline response passed all functional checks. Selection therefore uses the predeclared per-test tie-breaker; selected conditions are not demonstrated working implementations.', '']
            else: lines += ['Selection is withheld until every scheduled attempt and evaluation is resolved.', '']
        if plan.get('acquisitions'):
            lines += ['### Frozen context acquisitions', '', table(['Method', 'Strategy', 'Acquisition', 'Items', 'Citations matched / total', 'Uncited items', 'Status'], [
                [a['method'], a['strategy'], a['id'], a['items'], f"{a['citationChecks']['matched']}/{a['citationChecks']['total']}", a['citationChecks']['uncitedItems'], a['status']] for a in plan['acquisitions']]), '',
                'Each context is acquired once and reused across that method’s selected paper contexts and code repetitions. Citation matching checks exact inspected source text; it does not prove the security claim.', '']
        if plan['phase'] == 'security_followup':
            lines += ['### Security differences from fresh controls', '', 'Treatment minus the matching follow-up control, in percentage points (pp). Responses are independent; matching repetition labels do not imply shared model seeds. These are descriptive differences without a multiplicity-adjusted significance claim.', '']
            all_comparisons = list(comparisons(study))
            for parent in plan['selected']:
                lines += [f'#### {parent}', '']
                for kind in ('functional', 'security'):
                    lines += [f'{kind.capitalize()} checks:', '', table(['Test', 'Security case', 'Control pass / tested', 'Treatment pass / tested', 'Control pass / attempts', 'Treatment pass / attempts', 'Δ tested pp', 'Δ all attempts pp'], [
                        [c['test'], c['securityStrategy'], rate(c['controlCounts']['pass'], c['controlCounts']['executed']), rate(c['treatmentCounts']['pass'], c['treatmentCounts']['executed']),
                         rate(c['controlCounts']['pass'], c['controlCounts']['attempts']), rate(c['treatmentCounts']['pass'], c['treatmentCounts']['attempts']),
                         '—' if c['testedDeltaPp'] is None else f"{c['testedDeltaPp']:+.1f}", '—' if c['allAttemptDeltaPp'] is None else f"{c['allAttemptDeltaPp']:+.1f}"]
                        for c in all_comparisons if c['parentCondition'] == parent and c['kind'] == kind]), '']
        lines += ['### Every test by condition', '', 'Counts preserve pass, fail and unresolved outcomes. Pass/tested conditions on execution; pass/attempt retains every recorded model attempt.', '']
        for c in plan['conditions']:
            lines += [f"#### {c['id']}", '', table(['Test', 'Pass', 'Fail', 'Not run', 'Unknown', 'Compile error', 'Environment error', 'Pass / tested', 'Pass / attempts'], [
                [t['id'], t['pass'], t['fail'], t['not_run'], t['unknown'], t['compile_error'], t['infrastructure_error'], rate(t['pass'], t['executed']), rate(t['pass'], t['attempts'])] for t in rows[c['id']]['checks']]), '']
        lines += ['### Protocol differences and limits', '', *['- ' + d for d in plan['deviations']], '']
    diagnostics = data.get('acquisitionDiagnostics', [])
    if diagnostics:
        lines += ['## Acquisition process measurements', '', table(['Method', 'Strategy', 'Files inspected / indexed', 'Lines shown / indexed', 'Turns', 'Tool-error turns', 'Citation-feedback turns', 'First candidate items', 'Final items'], [
            [a['method'], a['strategy'], f"{a['inspectedFiles']}/{a['sourceFiles']}", f"{a['inspectedLines']}/{a['sourceLines']}", a['turns'], a['toolErrorTurns'], a['citationFeedbackTurns'], a['firstCandidateItems'], a['finalItems']] for a in diagnostics]), '',
            'Candidate-size changes reveal that citation feedback can change the amount of context ultimately injected. These are observed outputs of this frozen acquisition protocol, not independent replications of each strategy. The JSON retains identifier changes for review; identifiers alone do not establish semantic claim loss.', '']
    lines += ['## Interpretation limits', '',
              'This exploratory sample does not establish a general model ranking or absence of vulnerabilities. Security checks cover explicit fixtures in an isolated feature harness; a pass there does not establish that the integrated game works. Additional information and token length accompany the security treatment. The six acquired contexts are not replicated, and the follow-up is conditional on selection and those specific contexts.', '',
              'Exact attempts, evaluator diagnostics and generated contexts remain local. The companion JSON contains hashes of the underlying records and reports; the explorer exposes individual evidence. The committed protocol is research/MATRIX_EXPERIMENT.md.', '']
    return '\n'.join(lines)


def write(output: Path):
    data = index()
    hashes = {}
    data['acquisitionDiagnostics'] = []
    for s in data['studies']:
        for a in s['plan'].get('acquisitions', []):
            path = ROOT / a['directory'] / 'record.json'
            expected = s['plan']['sourceHashes'][str(path.relative_to(ROOT))]
            if digest(path.read_bytes()) != expected: raise ValueError('Frozen acquisition record changed')
            data['acquisitionDiagnostics'].append({'method': a['method'], **diagnose(json.loads(path.read_text()))})
            hashes[str(path.relative_to(ROOT))] = expected
        base = ROOT / '.local/experiments' / s['plan']['id']
        for path in sorted([*base.glob('runs/*.json'), *base.glob('evaluations/*/report.json')]):
            hashes[str(path.relative_to(ROOT))] = digest(path.read_bytes())
    artifact = {'createdAt': timestamp(), 'data': data, 'evidenceHashes': hashes,
                'analysisSourceHashes': {str(p.relative_to(ROOT)): digest(p.read_bytes()) for p in (Path(__file__).resolve(), ROOT / 'research/acquisition_diagnostics.py', ROOT / 'research/study_results.py', ROOT / 'research/study_execution.py')}}
    artifact['fingerprint'] = digest(canonical(artifact))
    output.mkdir(parents=True, exist_ok=True)
    (output / 'results.json').write_bytes(canonical(artifact))
    (output / 'results.md').write_text(render(data, artifact['createdAt']))
    print(f"Wrote {output / 'results.md'}; snapshot {artifact['fingerprint']}")


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, default=ROOT / '.local/reports/highscore-matrix')
    write(parser.parse_args().output)
