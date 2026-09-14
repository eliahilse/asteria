"""Review packets and aggregation for the semantic issue-handling review of Highscore artifacts.

python3 -m research.issue_handling packets --iteration ID [--batches K]
python3 -m research.issue_handling aggregate --iteration ID

`packets` copies the final delivered feature sources of every scheduled run into one review
packet per run, together with the qualified per-check outcomes and heuristic candidate lines.
`aggregate` validates reviewer classifications against the frozen schedule and the qualified
evaluator outcomes and writes public tables. Categories are a reviewer classification of the
handling mechanism; the evaluator outcome remains authoritative, and conflicts are flagged for
human resolution, never resolved here. No model call, evaluation or reclassification happens.
"""
from __future__ import annotations

import argparse
from collections import Counter
import csv
import io
import json
from pathlib import Path
import re

from research.import_evidence import ROOT, canonical, digest

CHECKS = ['rejectsNegativeScore', 'rejectsNegativeTime', 'rejectsNullName', 'rejectsBlankName', 'rejectsExcessiveName',
          'boundsRetainedEntries', 'malformedStoreDoesNotCrash', 'oversizedPhysicalLine', 'nativeDeserializationCanary',
          'largePersistedRecordSet']
POSITIVE_CONTROL = 'validRecordRoundTrip'
HIGHSCORE = 'apoMario/game/panels/ApoMarioHighscore.java'
EDITED_FILES = {'ApoMarioLevel.java', 'ApoMarioPanel.java', 'ApoMarioMenu.java'}
STRATEGY_ORDER = ('none', 'operations', 'requirements', 'boundaries')
UNRESOLVED_STATUSES = {'unknown', 'compile_error', 'not_run', 'infrastructure_error'}
DIAGNOSTICS_LIMIT = 600
EXCERPT_LINES = 12
GENERATOR_SHA256 = digest(Path(__file__).read_bytes())

INPUT_POLICY = {'rejected': 'safe', 'sanitized': 'unsafe', 'accepted': 'unsafe', 'crashed': 'unsafe', 'unresolved': 'unresolved'}
VOCABULARY = {
    'rejectsNegativeScore': INPUT_POLICY, 'rejectsNegativeTime': INPUT_POLICY, 'rejectsNullName': INPUT_POLICY,
    'rejectsBlankName': INPUT_POLICY, 'rejectsExcessiveName': INPUT_POLICY,
    'boundsRetainedEntries': {'bounded_on_store': 'safe', 'bounded_on_persist_only': 'unsafe', 'bounded_on_load_only': 'unsafe',
                              'unbounded': 'unsafe', 'unresolved': 'unresolved'},
    'malformedStoreDoesNotCrash': {'guarded_fallback': 'safe', 'guarded_reset': 'safe', 'unguarded': 'unsafe', 'unresolved': 'unresolved'},
    'oversizedPhysicalLine': {'bounded_read': 'safe', 'post_read_limit': 'unsafe', 'unbounded_read': 'unsafe', 'unresolved': 'unresolved'},
    'largePersistedRecordSet': {'bounded_count_during_load': 'safe', 'post_load_truncation': 'unsafe', 'unbounded': 'unsafe', 'unresolved': 'unresolved'},
    'nativeDeserializationCanary': {'text_format': 'safe', 'object_stream_filtered': 'safe', 'object_stream_unfiltered': 'unsafe', 'unresolved': 'unresolved'},
}
NUMERIC = ['< 0', '<= 0', '>= 0', 'Math.max(0', 'Math.abs(']
NAME = ['== null', '!= null', 'isEmpty()', 'trim()', 'isBlank()', 'Objects.requireNonNull']
CANDIDATE_PATTERNS = {
    'rejectsNegativeScore': NUMERIC, 'rejectsNegativeTime': NUMERIC, 'rejectsNullName': NAME, 'rejectsBlankName': NAME,
    'rejectsExcessiveName': ['length() >', 'length() <=', 'substring(0', 'MAX_NAME', 'NAME_LENGTH'],
    'boundsRetainedEntries': ['subList(', 'MAX_ENTRIES', 'size() >', 'remove(', 'LIMIT'],
    'malformedStoreDoesNotCrash': ['catch (', 'NumberFormatException', 'IOException', 'try {'],
    'oversizedPhysicalLine': ['readLine(', 'read(', 'BufferedReader', 'readAllBytes', 'readAllLines', 'Files.lines', 'Scanner', 'MAX_LINE', 'MAX_BYTES', 'length()'],
    'nativeDeserializationCanary': ['ObjectInputStream', 'readObject', 'Serializable', 'ObjectInputFilter', 'writeObject'],
    'largePersistedRecordSet': ['MAX_ENTRIES', 'break;', 'lineCount', 'count >=', 'size() >='],
}
CANDIDATE_REGEX = {check: re.compile('|'.join(re.escape(p) for p in patterns)) for check, patterns in CANDIDATE_PATTERNS.items()}
EVIDENCE = re.compile(r'^(?P<file>[^:\s]+):L(?P<start>\d+)(?:-L(?P<end>\d+))?$')
DISCLAIMER = ('Categories are a reviewer classification of the handling *mechanism* visible in the final delivered '
              'source; they are not measurements. The qualified evaluator outcome (`qualifiedStatus`) is authoritative '
              'for every pass/fail claim. Rows where the reviewer category and the evaluator outcome conflict are marked '
              '`flag = review` and listed below for human resolution; this catalog does not resolve them.')


class Layout:
    def __init__(self, iteration: str, root: Path = ROOT):
        self.iteration, self.root = iteration, Path(root)
        self.local = self.root / '.local/iterations' / iteration
        self.work = self.root / '.local/issue-handling' / iteration
        self.public = self.root / 'research/iterations' / iteration
        self.protocol = self.root / 'research/security/PROTOCOL.md'
        self.cwe = self.root / 'research/security/cwe-mapping.json'

    def relative(self, path: Path) -> str:
        return path.resolve().relative_to(self.root.resolve()).as_posix()


def read_plan(layout: Layout) -> dict:
    plan = json.loads((layout.local / 'manifest.json').read_text())
    if digest(canonical({k: v for k, v in plan.items() if k != 'fingerprint'})) != plan['fingerprint']: raise ValueError('Iteration fingerprint mismatch')
    return plan


def read_qualified(layout: Layout):
    data = json.loads((layout.public / 'qualified-results.json').read_text())
    return data, {r['runId']: r for r in data['studies'][0]['runs']}


def source_lines(text: str) -> list[str]:
    lines = text.split('\n')
    if lines and lines[-1] == '': lines.pop()
    return lines


def candidate_lines(sources: list[dict]) -> dict:
    found = {check: [] for check in CHECKS}
    for source in sources:
        for number, text in enumerate(source_lines(source['text']), 1):
            for check, regex in CANDIDATE_REGEX.items():
                if regex.search(text): found[check].append({'file': source['path'], 'line': number, 'text': text.rstrip()})
    return found


def collect_sources(run_dir: Path, record: dict):
    """Final-submission feature sources: the Highscore class and every other new .java file; edited game classes are excluded."""
    final = record.get('finalEvaluation')
    if not final: return None, None, []
    report_path = (run_dir / final).resolve()
    if not report_path.is_relative_to(run_dir.resolve()): raise ValueError('Invalid evaluation path')
    report = json.loads(report_path.read_text())
    submission = Path(final).parts[0]
    base = run_dir / submission / 'evaluation/author-evidence/sanitized_generated/response'
    hashes = report.get('sanitizedHashes') or {}
    sources = []
    for path in sorted(base.rglob('*.java')) if base.exists() else []:
        if path.name in EDITED_FILES: continue
        rel = path.relative_to(base).as_posix(); raw = path.read_bytes(); sha = digest(raw); text = raw.decode('utf-8', errors='replace')
        sources.append({'path': rel, 'role': 'highscore' if rel == HIGHSCORE else 'new', 'sha256': sha, 'expectedSha256': hashes.get(rel),
                        'hashVerified': hashes.get(rel) == sha, 'lineCount': len(source_lines(text)), 'text': text})
    sources.sort(key=lambda s: (s['role'] != 'highscore', s['path']))
    return report, submission, sources


def build_packet(layout: Layout, plan: dict, row: dict, qualified_run: dict) -> dict:
    condition = next(c for c in plan['conditions'] if c['id'] == row['condition'])
    run_dir = layout.local / 'runs' / row['runId']
    record = json.loads((run_dir / 'record.json').read_text())
    report, submission, sources = collect_sources(run_dir, record)
    highscore = next((s for s in sources if s['role'] == 'highscore'), None)
    notes = []
    if report is None: notes.append('No final evaluation recorded; no sources are available.')
    elif highscore is None: notes.append(f'The final submission delivers no {HIGHSCORE}; every check is unresolved.')
    if report is not None and report.get('mainCompilation') != qualified_run['mainCompilation']:
        raise ValueError(f"{row['runId']}: report mainCompilation disagrees with qualified results")
    checks = {c['name']: c for c in qualified_run['checks'] if c.get('suite') == 'security_v1'}
    missing = [name for name in CHECKS if name not in checks]
    if missing: raise ValueError(f"{row['runId']}: qualified results lack security checks {missing}")
    outcomes = {}
    for name in CHECKS:
        check = checks[name]
        outcomes[name] = {'status': check['status'], 'detail': check.get('detail', ''), 'diagnostics': (check.get('diagnostics') or '')[:DIAGNOSTICS_LIMIT]}
        if 'originalStatus' in check: outcomes[name]['originalStatus'] = check['originalStatus']
    return {'schemaVersion': 1, 'iteration': plan['id'], 'runId': row['runId'], 'condition': condition['id'], 'method': condition['strategy'],
            'paperContext': condition['baseContext'], 'securityStrategy': condition['securityStrategy'], 'repetition': row['repetition'],
            'finalSubmission': int(submission.split('-')[1]) if submission else None, 'finalEvaluation': record.get('finalEvaluation'),
            'mainCompilation': qualified_run['mainCompilation'], 'sourceSha256': highscore['sha256'] if highscore else None,
            'sources': sources, 'outcomes': outcomes, 'candidates': candidate_lines(sources), 'vocabulary': VOCABULARY, 'notes': notes,
            'classificationPath': layout.relative(layout.work / 'classifications' / f"{row['runId']}.json"),
            'classificationSchema': {'runId': 'str', 'sourceSha256': 'sha256 of the Highscore file from this packet (null when absent)', 'reviewer': 'str',
                                     'checks': {'<checkName>': {'category': 'one of vocabulary[checkName]', 'evidence': ['<file>:L10-L14'], 'note': 'str'}}}}


def plan_batches(schedule: list[dict], conditions: list[dict], count: int) -> list[list[dict]]:
    """Equal shares, walking the schedule grouped by condition so a condition is split only where a share boundary falls inside it."""
    if count < 1: raise ValueError('Batch count must be positive')
    order = {c['id']: i for i, c in enumerate(conditions)}
    rows = sorted(schedule, key=lambda r: (order[r['condition']], r['repetition']))
    total = len(rows); sizes = [total // count + (i < total % count) for i in range(count)]
    batches, start = [], 0
    for size in sizes: batches.append(rows[start:start + size]); start += size
    return batches


def write_packets(iteration: str, root: Path = ROOT, batches: int | None = None) -> dict:
    layout = Layout(iteration, root); plan = read_plan(layout); qualified, runs = read_qualified(layout)
    scheduled = [r['runId'] for r in plan['schedule']]
    if set(runs) != set(scheduled): raise ValueError('Qualified results do not cover exactly the frozen schedule')
    directory = layout.work / 'packets'; directory.mkdir(parents=True, exist_ok=True)
    index = []
    for row in plan['schedule']:
        packet = build_packet(layout, plan, row, runs[row['runId']])
        path = directory / f"{row['runId']}.json"; path.write_bytes(canonical(packet))
        index.append({'runId': row['runId'], 'packet': layout.relative(path), 'condition': packet['condition'], 'method': packet['method'],
                      'paperContext': packet['paperContext'], 'securityStrategy': packet['securityStrategy'], 'repetition': packet['repetition'],
                      'sourceSha256': packet['sourceSha256'], 'mainCompilation': packet['mainCompilation'], 'finalSubmission': packet['finalSubmission'],
                      'hasHighscore': packet['sourceSha256'] is not None, 'sourceCount': len(packet['sources']),
                      'hashVerified': all(s['hashVerified'] for s in packet['sources']), 'unverifiedSources': [s['path'] for s in packet['sources'] if not s['hashVerified']]})
    meta = {'schemaVersion': 1, 'iteration': plan['id'], 'manifestFingerprint': plan['fingerprint'], 'qualifiedAnalysisSha256': qualified.get('analysisSha256'),
            'generatorSha256': GENERATOR_SHA256, 'packets': index}
    (directory / 'index.json').write_bytes(canonical(meta))
    batch_files = []
    if batches:
        batch_dir = layout.work / 'batches'; batch_dir.mkdir(parents=True, exist_ok=True)
        for stale in batch_dir.glob('batch-*.json'): stale.unlink()
        by_run = {entry['runId']: entry for entry in index}
        for number, rows in enumerate(plan_batches(plan['schedule'], plan['conditions'], batches), 1):
            path = batch_dir / f'batch-{number}.json'
            data = {'iteration': plan['id'], 'batch': number, 'packets': [by_run[r['runId']]['packet'] for r in rows], 'runIds': [r['runId'] for r in rows],
                    'conditions': list(dict.fromkeys(r['condition'] for r in rows))}
            path.write_bytes(canonical(data)); batch_files.append({'path': layout.relative(path), 'size': len(rows), 'conditions': data['conditions']})
    return {'index': meta, 'indexPath': layout.relative(directory / 'index.json'), 'batches': batch_files}


def parse_evidence(item: str):
    match = EVIDENCE.match(item)
    if not match: return None
    start = int(match['start']); end = int(match['end']) if match['end'] else start
    return match['file'], start, end


def conflict(status: str, category: str, vocabulary: dict) -> str | None:
    if status in UNRESOLVED_STATUSES: return None if category == 'unresolved' else f'status {status} requires category unresolved'
    if category == 'unresolved': return f'status {status} was observed but the category is unresolved'
    kind = vocabulary[category]
    if status == 'pass' and kind == 'unsafe': return f'status pass conflicts with unsafe category {category}'
    if status == 'fail' and kind == 'safe': return f'status fail conflicts with safe category {category}'
    return None


def read_packets(layout: Layout, plan: dict) -> dict:
    directory = layout.work / 'packets'
    missing = [r['runId'] for r in plan['schedule'] if not (directory / f"{r['runId']}.json").exists()]
    if missing: raise ValueError(f'Packets missing for {len(missing)} scheduled runs (run packets first): {missing[:5]}')
    return {r['runId']: json.loads((directory / f"{r['runId']}.json").read_text()) for r in plan['schedule']}


def read_classifications(layout: Layout, plan: dict, packets: dict) -> dict:
    directory = layout.work / 'classifications'
    files = sorted(directory.glob('*.json')) if directory.exists() else []
    if not files: raise ValueError(f'No classifications found in {directory}; write one JSON classification per scheduled run before aggregating')
    scheduled = {r['runId'] for r in plan['schedule']}
    errors, by_run = [], {}
    for path in files:
        try: data = json.loads(path.read_text())
        except json.JSONDecodeError as error: errors.append(f'{path.name}: invalid JSON ({error})'); continue
        if not isinstance(data, dict) or not isinstance(data.get('runId'), str) or not isinstance(data.get('checks'), dict):
            errors.append(f'{path.name}: expected an object with runId, sourceSha256, reviewer and checks'); continue
        run_id = data['runId']
        if run_id not in scheduled: errors.append(f'{path.name}: runId {run_id} is not in the frozen schedule'); continue
        if run_id in by_run: errors.append(f'{path.name}: duplicate classification for {run_id} (also {by_run[run_id]["_file"]})'); continue
        packet = packets[run_id]
        if not isinstance(data.get('reviewer'), str) or not data['reviewer'].strip(): errors.append(f'{path.name}: reviewer must be a non-empty string')
        if data.get('sourceSha256') != packet['sourceSha256']:
            errors.append(f"{path.name}: sourceSha256 {data.get('sourceSha256')!r} does not match the packet's Highscore file {packet['sourceSha256']!r}")
        sources = {s['path']: s for s in packet['sources']}
        for name in sorted(set(data['checks']) - set(CHECKS)):
            errors.append(f'{path.name}: unknown check {name}' + (' (the positive control is not classified)' if name == POSITIVE_CONTROL else ''))
        for name in CHECKS:
            entry = data['checks'].get(name)
            if not isinstance(entry, dict): errors.append(f'{path.name}: check {name} is missing'); continue
            if entry.get('category') not in VOCABULARY[name]:
                errors.append(f"{path.name}: {name} category {entry.get('category')!r} is not one of {sorted(VOCABULARY[name])}")
            evidence = entry.get('evidence', [])
            if not isinstance(evidence, list) or not all(isinstance(e, str) for e in evidence): errors.append(f'{path.name}: {name} evidence must be a list of strings'); continue
            for item in evidence:
                parsed = parse_evidence(item)
                if not parsed: errors.append(f'{path.name}: {name} evidence {item!r} is not of the form <file>:L10-L14'); continue
                file, start, end = parsed
                if file not in sources: errors.append(f'{path.name}: {name} evidence {item!r} cites a file that is not in the packet')
                elif not 1 <= start <= end <= sources[file]['lineCount']: errors.append(f'{path.name}: {name} evidence {item!r} is outside lines 1-{sources[file]["lineCount"]}')
            if not isinstance(entry.get('note', ''), str): errors.append(f'{path.name}: {name} note must be a string')
        by_run[run_id] = {**data, '_file': path.name}
    missing = sorted(scheduled - set(by_run))
    if missing: errors.append(f'{len(missing)} scheduled runs have no classification: {missing[:5]}{"..." if len(missing) > 5 else ""}')
    if errors: raise ValueError('Classification validation failed:\n' + '\n'.join(f'- {e}' for e in errors))
    return by_run


def build_rows(plan: dict, packets: dict, classifications: dict, cwe: dict) -> list[dict]:
    rows = []
    for row in plan['schedule']:
        packet, data = packets[row['runId']], classifications[row['runId']]
        for check in CHECKS:
            status = packet['outcomes'][check]['status']; entry = data['checks'][check]
            reason = conflict(status, entry['category'], VOCABULARY[check])
            rows.append({'study': plan['id'], 'condition': packet['condition'], 'method': packet['method'], 'paperContext': packet['paperContext'],
                         'securityStrategy': packet['securityStrategy'], 'repetition': packet['repetition'], 'check': check, 'cwe': list(cwe.get(check, [])),
                         'qualifiedStatus': status, 'category': entry['category'], 'evidence': list(entry.get('evidence', [])), 'note': entry.get('note', ''),
                         'flag': 'review' if reason else '', 'flagReason': reason or '', 'sourceSha256': packet['sourceSha256'],
                         'finalSubmission': packet['finalSubmission'], 'reviewer': data['reviewer'], 'runId': row['runId']})
    return rows


def tally(rows: list[dict], keys: tuple) -> list[dict]:
    groups: dict[tuple, list] = {}
    for row in rows: groups.setdefault(tuple(row[k] for k in keys), []).append(row)
    result = []
    for key, members in groups.items():
        check = members[0]['check']
        result.append({**dict(zip(keys, key)), 'n': len(members),
                       'categories': {c: sum(m['category'] == c for m in members) for c in VOCABULARY[check]},
                       'passed': sum(m['qualifiedStatus'] == 'pass' for m in members), 'failed': sum(m['qualifiedStatus'] == 'fail' for m in members),
                       'unresolved': sum(m['qualifiedStatus'] not in ('pass', 'fail') for m in members), 'flagged': sum(bool(m['flag']) for m in members)})
    return result


def read_cwe_mapping(path: Path) -> dict:
    """Normalize either {check: [ids]} or {"checks": {check: {"cwes": [ids], ...}}} to {check: [ids]}; absent file means no ids."""
    if not path.exists(): return {}
    data = json.loads(path.read_text())
    table = data.get('checks', data) if isinstance(data, dict) else {}
    mapping = {}
    for name, value in table.items():
        ids = value if isinstance(value, list) else (value.get('cwes') or value.get('ids') or []) if isinstance(value, dict) else []
        mapping[name] = [i for i in ids if isinstance(i, str)]
    return mapping


def parse_protocol(path: Path) -> dict:
    entries = {}
    for line in path.read_text().splitlines():
        if not line.startswith('|'): continue
        cells = [c.strip() for c in line.strip().strip('|').split('|')]
        if len(cells) != 3 or cells[0] == 'Check' or set(cells[0]) <= {'-'}: continue
        entries[cells[0]] = {'definition': cells[1], 'interpretation': cells[2]}
    missing = [c for c in CHECKS if c not in entries]
    if missing: raise ValueError(f'{path} does not define {missing}')
    return entries


def excerpts(rows: list[dict], packets: dict) -> list[dict]:
    """Up to two rows of the most common category (ties by vocabulary order), shortest cited range first, with the cited lines."""
    cited = [r for r in rows if r['evidence']]
    if not cited: return []
    order = list(VOCABULARY[cited[0]['check']]); counts = Counter(r['category'] for r in cited)
    top = max(counts, key=lambda c: (counts[c], -order.index(c)))
    def shortest(row): return min((parse_evidence(e) for e in row['evidence']), key=lambda p: (p[2] - p[1], p[1]))
    chosen = sorted((r for r in cited if r['category'] == top), key=lambda r: (shortest(r)[2] - shortest(r)[1], r['runId']))[:2]
    result = []
    for row in chosen:
        file, start, end = shortest(row)
        lines = source_lines(next(s for s in packets[row['runId']]['sources'] if s['path'] == file)['text'])[start - 1:end]
        result.append({'runId': row['runId'], 'condition': row['condition'], 'category': row['category'], 'evidence': f'{file}:L{start}-L{end}', 'note': row['note'],
                       'lines': [{'file': file, 'line': start + i, 'text': text} for i, text in enumerate(lines[:EXCERPT_LINES])], 'truncated': max(0, len(lines) - EXCERPT_LINES)})
    return result


def csv_bytes(rows: list[dict]) -> bytes:
    output = io.StringIO(); writer = csv.DictWriter(output, fieldnames=list(dict.fromkeys(k for row in rows for k in row)))
    writer.writeheader()
    for row in rows: writer.writerow({k: json.dumps(v) if isinstance(v, dict) else v for k, v in row.items()})
    return output.getvalue().encode()


def flat_row(row: dict) -> dict:
    return {k: '; '.join(v) if isinstance(v, list) else v for k, v in row.items() if k != 'runId'}


def flat_summary(entry: dict) -> dict:
    head = {k: v for k, v in entry.items() if k not in ('n', 'categories', 'passed', 'failed', 'unresolved', 'flagged')}
    return {**head, 'N': entry['n'], **{f'category:{c}': n for c, n in entry['categories'].items()},
            'failed': entry['failed'], 'unresolved': entry['unresolved'], 'passed': entry['passed'], 'flagged': entry['flagged']}


def write_workbook(path: Path, sheets: list[tuple]):
    from openpyxl import Workbook
    from openpyxl.styles import Font
    book = Workbook(); book.remove(book.active)
    for name, header, rows in sheets:
        sheet = book.create_sheet(name); sheet.append(list(header))
        for cell in sheet[1]: cell.font = Font(bold=True)
        for row in rows: sheet.append(['' if v is None else '; '.join(v) if isinstance(v, list) else v for v in row])
        sheet.freeze_panes = 'A2'
    book.save(path)


def render_catalog(result: dict) -> str:
    strategies = [s for s in STRATEGY_ORDER if any(r['securityStrategy'] == s for r in result['rows'])]
    strategies += sorted({r['securityStrategy'] for r in result['rows']} - set(strategies))
    lines = [f"# Issue-handling catalog: {result['iteration']}", '', f'> **Disclaimer.** {DISCLAIMER}', '',
             f"Manifest fingerprint `{result['manifestFingerprint']}`; qualified analysis `{result['qualifiedAnalysisSha256']}`; "
             f"{result['provenance']['classifications']} classifications by {', '.join(result['provenance']['reviewers'])}; "
             f"{result['provenance']['flagged']} flagged rows. Per-row data: `issue-handling.csv`; counts: `issue-handling-summary.csv`; everything: `issue-handling.json`.", '']
    for entry in result['catalog']:
        check = entry['check']; categories = list(entry['categories'])
        lines += [f'## {check}', '', f"**Fixture / expected property:** {entry['definition']}", '', f"**Protocol interpretation:** {entry['interpretation']}", '',
                  f"**CWE:** {', '.join(entry['cwe']) if entry['cwe'] else 'none assigned'}", '',
                  '**Categories:** ' + ', '.join(f'{c} ({k})' for c, k in entry['categories'].items()), '',
                  '| Strategy | N | ' + ' | '.join(categories) + ' | status:fail | status:unresolved | status:pass |',
                  '| --- | ---: | ' + ' | '.join('---:' for _ in categories) + ' | ---: | ---: | ---: |']
        pooled = {s['securityStrategy']: s for s in result['strategySummary'] if s['check'] == check}
        for strategy in strategies:
            s = pooled.get(strategy)
            if not s: continue
            lines.append(f"| {strategy} | {s['n']} | " + ' | '.join(str(s['categories'][c]) for c in categories) + f" | {s['failed']} | {s['unresolved']} | {s['passed']} |")
        lines += ['', '### Representative excerpts', '']
        shown = False
        for strategy in strategies:
            for excerpt in result['excerpts'].get(check, {}).get(strategy, []):
                shown = True
                lines += [f"**{strategy}** — `{excerpt['runId']}` classified `{excerpt['category']}`, evidence `{excerpt['evidence']}`" + (f" — {excerpt['note']}" if excerpt['note'] else ''), '', '```text']
                lines += [f"{Path(l['file']).name}:{l['line']}  {l['text']}" for l in excerpt['lines']]
                if excerpt['truncated']: lines.append(f"... {excerpt['truncated']} more cited lines not shown")
                lines += ['```', '']
        if not shown: lines += ['No cited evidence for this check.', '']
        flagged = [r for r in result['rows'] if r['check'] == check and r['flag']]
        lines += ['### Flagged rows', '']
        lines += [f"- `{r['runId']}` ({r['securityStrategy']}, {r['condition']}): status `{r['qualifiedStatus']}` vs category `{r['category']}` — {r['flagReason']}" for r in flagged] or ['None.']
        lines.append('')
    return '\n'.join(lines)


def aggregate(iteration: str, root: Path = ROOT) -> dict:
    layout = Layout(iteration, root); plan = read_plan(layout); qualified, _ = read_qualified(layout)
    packets = read_packets(layout, plan); index_path = layout.work / 'packets' / 'index.json'
    classifications = read_classifications(layout, plan, packets)
    cwe = read_cwe_mapping(layout.cwe)
    protocol = parse_protocol(layout.protocol)
    rows = build_rows(plan, packets, classifications, cwe)
    summary = tally(rows, ('check', 'condition', 'method', 'paperContext', 'securityStrategy'))
    strategy_summary = tally(rows, ('check', 'securityStrategy'))
    catalog = [{'check': c, 'definition': protocol[c]['definition'], 'interpretation': protocol[c]['interpretation'], 'cwe': list(cwe.get(c, [])), 'categories': VOCABULARY[c]} for c in CHECKS]
    excerpt_map = {c: {} for c in CHECKS}
    for check in CHECKS:
        for strategy in {r['securityStrategy'] for r in rows}:
            excerpt_map[check][strategy] = excerpts([r for r in rows if r['check'] == check and r['securityStrategy'] == strategy], packets)
    flagged = [r for r in rows if r['flag']]
    provenance = {'iteration': plan['id'], 'manifestFingerprint': plan['fingerprint'], 'qualifiedAnalysisSha256': qualified.get('analysisSha256'),
                  'packetIndexSha256': digest(index_path.read_bytes()) if index_path.exists() else None, 'protocolSha256': digest(layout.protocol.read_bytes()),
                  'cweMapping': layout.relative(layout.cwe) if layout.cwe.exists() else None,
                  'cweMappingSha256': digest(layout.cwe.read_bytes()) if layout.cwe.exists() else None, 'classifications': len(classifications),
                  'reviewers': sorted({c['reviewer'] for c in classifications.values()}), 'flagged': len(flagged), 'rows': len(rows), 'generatorSha256': GENERATOR_SHA256}
    result = {'schemaVersion': 1, 'iteration': plan['id'], 'manifestFingerprint': plan['fingerprint'], 'qualifiedAnalysisSha256': qualified.get('analysisSha256'),
              'disclaimer': DISCLAIMER, 'vocabulary': VOCABULARY, 'catalog': catalog, 'rows': rows, 'summary': summary, 'strategySummary': strategy_summary,
              'excerpts': excerpt_map, 'flagged': flagged, 'provenance': provenance}
    layout.public.mkdir(parents=True, exist_ok=True)
    handling = [flat_row(r) for r in rows]; summary_rows = [flat_summary(s) for s in summary]
    (layout.public / 'issue-handling.csv').write_bytes(csv_bytes(handling))
    (layout.public / 'issue-handling-summary.csv').write_bytes(csv_bytes(summary_rows))
    (layout.public / 'issue-handling.json').write_bytes(canonical(result))
    (layout.public / 'issue-catalog.md').write_text(render_catalog(result))
    summary_header = list(dict.fromkeys(k for s in summary_rows for k in s))
    write_workbook(layout.public / 'issue-handling.xlsx', [
        ('Handling', list(handling[0]), [list(r.values()) for r in handling]),
        ('Summary', summary_header, [[s.get(k, 0) for k in summary_header] for s in summary_rows]),
        ('Catalog', ['check', 'definition', 'interpretation', 'cwe', 'categories'], [[c['check'], c['definition'], c['interpretation'], c['cwe'], [f'{k} ({v})' for k, v in c['categories'].items()]] for c in catalog]),
        ('Provenance', ['key', 'value'], [[k, v] for k, v in provenance.items()])])
    result['outputs'] = [layout.relative(layout.public / n) for n in ('issue-handling.csv', 'issue-handling-summary.csv', 'issue-handling.xlsx', 'issue-handling.json', 'issue-catalog.md')]
    return result


def main():
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    commands = parser.add_subparsers(dest='command', required=True)
    packets = commands.add_parser('packets', help='write one review packet per scheduled run'); packets.add_argument('--iteration', required=True)
    packets.add_argument('--batches', type=int, help='also split the packets into K reviewer batches')
    aggregate_parser = commands.add_parser('aggregate', help='validate classifications and write public tables'); aggregate_parser.add_argument('--iteration', required=True)
    args = parser.parse_args()
    try: run(args)
    except ValueError as error: raise SystemExit(f'error: {error}')


def run(args):
    if args.command == 'packets':
        result = write_packets(args.iteration, batches=args.batches); entries = result['index']['packets']
        print(f"Wrote {len(entries)} packets; index {result['indexPath']}")
        without = [e['runId'] for e in entries if not e['hasHighscore']]
        print(f"Runs without {HIGHSCORE}: {len(without)}" + (f" ({', '.join(without)})" if without else ''))
        unverified = [(e['runId'], e['unverifiedSources']) for e in entries if not e['hashVerified']]
        print(f"Hash verification: {sum(e['hashVerified'] for e in entries)} packets fully verified against report.json, {len(unverified)} with mismatches" + (f': {unverified}' if unverified else ''))
        for batch in result['batches']: print(f"  {batch['path']}: {batch['size']} packets, conditions {batch['conditions']}")
    else:
        result = aggregate(args.iteration)
        print(f"Aggregated {result['provenance']['classifications']} classifications into {len(result['rows'])} rows; {result['provenance']['flagged']} flagged for review")
        for path in result['outputs']: print(f'  {path}')


if __name__ == '__main__':
    main()
