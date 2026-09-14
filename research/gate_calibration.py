"""Compare shadow-gate verdicts with the evaluator's outcomes for the same submission.

For every gate consultation recorded in a trajectory, the submission it judged
was compiled and evaluated (shadow mode never rejects). The judge's verdict
("would intervene", cited statements, quoted lines) is joined with that
submission's own security check outcomes. Checks are linked to statements
through CWE identifiers: a cited statement predicts the failure of the checks
whose CWE it names (research/security/cwe-mapping.json). Counts only.
"""
from __future__ import annotations

import argparse
import csv
import io
import json
from pathlib import Path

from research.import_evidence import ROOT, canonical

ISSUE_CHECKS = ['rejectsNegativeScore', 'rejectsNegativeTime', 'rejectsNullName', 'rejectsBlankName', 'rejectsExcessiveName', 'boundsRetainedEntries',
                'malformedStoreDoesNotCrash', 'oversizedPhysicalLine', 'nativeDeserializationCanary', 'largePersistedRecordSet']


def check_cwes(mapping: dict) -> dict[str, set[str]]:
    return {name: set(entry.get('cwes', [])) for name, entry in mapping['checks'].items() if name in ISSUE_CHECKS}


def statement_cwes(graph: dict) -> dict[str, set[str]]:
    return {node['id']: set(node.get('cwe', [])) for node in graph['nodes'] if node['id'].startswith('item:')}


def join(run_dir: Path, graph: dict, mapping: dict) -> list[dict]:
    """One row per judged submission of one trajectory, with its evaluated security outcomes."""
    record = json.loads((run_dir / 'record.json').read_text())
    submissions = {s['number']: s for s in record['submissions']}
    cwes_of_statement, cwes_of_check = statement_cwes(graph), check_cwes(mapping)
    rows = []
    for event in record.get('sidecarEvents', []):
        if event.get('stage') != 'gate' or not event.get('consulted'): continue
        submission = submissions.get(event['submission'])
        report = None
        if submission and submission.get('evaluationFile'):
            report = json.loads((run_dir / submission['evaluationFile']).read_text())
        checks = {c['name']: c['status'] for c in ((report or {}).get('security') or {}).get('checks', [])}
        failed = sorted(name for name, status in checks.items() if name in ISSUE_CHECKS and status == 'fail')
        evaluated = sorted(name for name, status in checks.items() if name in ISSUE_CHECKS and status in ('pass', 'fail'))
        cited = list(event.get('citedIds') or [])
        cited_cwes = set().union(*(cwes_of_statement.get(i, set()) for i in cited)) if cited else set()
        predicted = sorted(name for name in evaluated if cwes_of_check.get(name, set()) & cited_cwes)
        rows.append({'runId': record['runId'], 'condition': record['condition'], 'submission': event['submission'], 'submissionStatus': submission['status'] if submission else None,
                     'compiled': (report or {}).get('mainCompilation'), 'functional': (report or {}).get('functionalSuccess'),
                     'wouldIntervene': bool(event.get('wouldIntervene')), 'verdictIntervene': bool(event.get('verdictIntervene')), 'citedIds': cited, 'quotedLines': len(event.get('quoted') or []),
                     'unquotedLines': event.get('unquoted'), 'citedCwes': sorted(cited_cwes), 'evaluatedChecks': len(evaluated), 'failedChecks': failed,
                     'predictedChecks': predicted, 'predictedFailed': sorted(set(predicted) & set(failed)), 'reason': event.get('reason')})
    return rows


def summarize(rows: list[dict]) -> dict:
    evaluated = [r for r in rows if r['evaluatedChecks']]
    would = [r for r in evaluated if r['wouldIntervene']]; silent = [r for r in evaluated if not r['wouldIntervene']]
    return {'judgedSubmissions': len(rows), 'evaluatedJudgedSubmissions': len(evaluated), 'notEvaluated': len(rows) - len(evaluated),
            'wouldIntervene': len(would), 'wouldIntervene_withAnyFailedCheck': sum(1 for r in would if r['failedChecks']),
            'wouldIntervene_withPredictedFailedCheck': sum(1 for r in would if r['predictedFailed']),
            'silent': len(silent), 'silent_withAnyFailedCheck': sum(1 for r in silent if r['failedChecks']),
            'verdictInterveneWithoutQuote': sum(1 for r in rows if r['verdictIntervene'] and not r['wouldIntervene']),
            'failedChecksPerJudgedSubmission': [len(r['failedChecks']) for r in evaluated]}


def render(rows: list[dict], summary: dict, identifier: str) -> str:
    lines = [f'# Shadow-gate calibration: {identifier}', '',
             'Each row is one judged submission that was then compiled and evaluated. "Predicted" checks are the issue checks whose CWE a cited statement names. Counts, not rates.', '',
             '| Trajectory | Submission | Would intervene | Cited statements | Quoted lines | Failed checks of this submission | Predicted checks | Predicted and failed |', '| --- | ---: | --- | --- | ---: | --- | --- | --- |']
    for r in rows:
        lines.append(f"| {r['runId'].rsplit('__', 2)[-2]} r{r['runId'].rsplit('r', 1)[-1]} | {r['submission']} | {'yes' if r['wouldIntervene'] else 'no'} | {', '.join(i.split(':', 1)[1] for i in r['citedIds']) or '—'} | {r['quotedLines']} | "
                     f"{', '.join(r['failedChecks']) or ('none' if r['evaluatedChecks'] else 'not evaluated')} | {', '.join(r['predictedChecks']) or '—'} | {', '.join(r['predictedFailed']) or '—'} |")
    s = summary
    lines += ['', '## Counts', '',
              f"Judged submissions: {s['judgedSubmissions']}; evaluated: {s['evaluatedJudgedSubmissions']}; not evaluated (invalid edits): {s['notEvaluated']}.",
              f"Would intervene: {s['wouldIntervene']} of {s['evaluatedJudgedSubmissions']} evaluated; of these, {s['wouldIntervene_withAnyFailedCheck']} had at least one failed issue check and {s['wouldIntervene_withPredictedFailedCheck']} had a failed check among those the cited statements name.",
              f"Silent: {s['silent']}; of these, {s['silent_withAnyFailedCheck']} had at least one failed issue check.",
              f"Verdicts that said intervene but quoted no verbatim line (not acted on): {s['verdictInterveneWithoutQuote']}.", '']
    return '\n'.join(lines)


def main():
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration', required=True)
    parser.add_argument('--graph', type=Path); args = parser.parse_args()
    directory = ROOT / '.local/iterations' / args.iteration; public = ROOT / 'research/iterations' / args.iteration
    graph_path = args.graph or next(public.glob('contexts/*.graph.json'))
    graph = json.loads(graph_path.read_text()); mapping = json.loads((ROOT / 'research/security/cwe-mapping.json').read_text())
    rows = []
    for run_dir in sorted(p for p in (directory / 'runs').iterdir() if p.is_dir()): rows += join(run_dir, graph, mapping)
    summary = summarize(rows)
    (public / 'gate-calibration.json').write_bytes(canonical({'rows': rows, 'summary': summary, 'graph': graph_path.name}))
    buffer = io.StringIO(); writer = csv.writer(buffer); writer.writerow(['runId', 'submission', 'submissionStatus', 'compiled', 'functional', 'wouldIntervene', 'verdictIntervene', 'citedIds', 'quotedLines', 'citedCwes', 'evaluatedChecks', 'failedChecks', 'predictedChecks', 'predictedFailed'])
    for r in rows: writer.writerow([r['runId'], r['submission'], r['submissionStatus'], r['compiled'], r['functional'], r['wouldIntervene'], r['verdictIntervene'], ';'.join(r['citedIds']), r['quotedLines'], ';'.join(r['citedCwes']), r['evaluatedChecks'], ';'.join(r['failedChecks']), ';'.join(r['predictedChecks']), ';'.join(r['predictedFailed'])])
    (public / 'gate-calibration.csv').write_text(buffer.getvalue())
    text = render(rows, summary, args.iteration); (public / 'gate-calibration.md').write_text(text); print(text)


if __name__ == '__main__':
    main()
