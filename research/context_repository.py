"""Read-only repository snapshots and bounded search/read operations for context acquisition."""
from __future__ import annotations

import os
import zipfile
from pathlib import Path

from research.import_evidence import canonical, digest

TEXT_SUFFIXES = {'.java', '.py', '.js', '.jsx', '.ts', '.tsx', '.json', '.xml', '.properties',
                 '.md', '.txt', '.yaml', '.yml', '.toml', '.gradle', '.kt', '.c', '.h', '.cpp',
                 '.cs', '.go', '.rs', '.sql', '.sh', '.html', '.css', '.mf'}
SKIP_DIRS = {'.git', '.local', '.venv', 'node_modules', '__pycache__', 'dist', 'build', 'target'}
MAX_FILE_BYTES = 2_000_000
MAX_SOURCE_BYTES = 20_000_000


def snapshot(repo: Path) -> dict:
    repo = repo.resolve(strict=True)
    if not repo.is_dir(): raise ValueError('Repository must be a directory')
    files, omitted, total = [], [], 0

    def add(path: str, raw: bytes):
        nonlocal total
        if len(raw) > MAX_FILE_BYTES: raise ValueError(f'Source exceeds file budget: {path}')
        total += len(raw)
        if total > MAX_SOURCE_BYTES: raise ValueError('Repository exceeds source byte budget')
        try: text, encoding = raw.decode('utf-8'), 'utf-8'
        except UnicodeDecodeError: text, encoding = raw.decode('latin-1'), 'latin-1'
        if '\x00' in text:
            omitted.append({'path': path, 'reason': 'binary content', 'sha256': digest(raw)})
            return
        files.append({'path': path, 'sha256': digest(raw), 'bytes': len(raw), 'encoding': encoding,
                      'lines': len(text.splitlines()), 'text': text})

    for directory, dirs, names in os.walk(repo, followlinks=False):
        dirs[:] = sorted(d for d in dirs if d not in SKIP_DIRS and not (Path(directory) / d).is_symlink())
        for name in sorted(names):
            path = Path(directory) / name
            relative = path.relative_to(repo).as_posix()
            if path.is_symlink() or name.startswith('.env') or path.suffix.lower() in {'.pem', '.key', '.p12', '.pfx'}:
                omitted.append({'path': relative, 'reason': 'local configuration or link'})
                continue
            if path.suffix.lower() in {'.jar', '.zip'}:
                with zipfile.ZipFile(path) as archive:
                    for entry in sorted(archive.infolist(), key=lambda e: e.filename):
                        member = Path(entry.filename)
                        if member.is_absolute() or '..' in member.parts: raise ValueError('Unsafe archive member')
                        # Embedded source is code; compiled classes and assets are not decompiled.
                        if member.suffix.lower() == '.java' or entry.filename.upper() == 'META-INF/MANIFEST.MF':
                            if entry.file_size > MAX_FILE_BYTES: raise ValueError('Archive source exceeds file budget')
                            add(relative + '!/' + entry.filename, archive.read(entry))
                omitted.append({'path': relative, 'reason': 'binary archive; embedded Java and manifest indexed',
                                'sha256': digest(path.read_bytes())})
            elif path.suffix.lower() in TEXT_SUFFIXES or name in {'Dockerfile', 'Makefile', 'LICENSE', 'README'}:
                if path.stat().st_size > MAX_FILE_BYTES: raise ValueError(f'Source exceeds file budget: {relative}')
                add(relative, path.read_bytes())
            else:
                omitted.append({'path': relative, 'reason': 'unsupported file type', 'sha256': digest(path.read_bytes())})
    files.sort(key=lambda f: f['path'])
    if not files: raise ValueError('No readable repository source found')
    manifest = [{k: v for k, v in f.items() if k != 'text'} for f in files]
    identity = {'files': manifest, 'omitted': sorted(omitted, key=lambda f: f['path'])}
    return {'fingerprint': digest(canonical(identity)), **identity, 'sources': files,
            'limits': {'fileBytes': MAX_FILE_BYTES, 'sourceBytes': MAX_SOURCE_BYTES},
            'exclusions': sorted(SKIP_DIRS)}


def operate(snap: dict, action: dict) -> dict:
    """Only snapshot bytes are accessible. Never execute repository or model-supplied code."""
    sources = {f['path']: f for f in snap['sources']}
    if action.get('action') == 'read':
        requests = action.get('files')
        if not isinstance(requests, list) or not 1 <= len(requests) <= 5: raise ValueError('Read 1–5 ranges per turn')
        excerpts, budget = [], 30_000
        for request in requests:
            source = sources.get(request.get('path'))
            if source is None: raise ValueError('Path is not in the source snapshot')
            start, end = request.get('start_line'), request.get('end_line')
            if type(start) is not int or type(end) is not int or not 1 <= start <= end <= source['lines'] or end - start >= 240:
                raise ValueError('Use valid 1-based ranges of at most 240 lines')
            text = '\n'.join(source['text'].splitlines()[start - 1:end])
            if len(text) > budget: raise ValueError('Read exceeds 30000 characters; request a smaller range')
            budget -= len(text)
            excerpts.append({'path': source['path'], 'start_line': start, 'end_line': end, 'text': text})
        return {'excerpts': excerpts}
    if action.get('action') == 'search':
        query, paths = action.get('query'), action.get('paths')
        if not isinstance(query, str) or not 1 <= len(query) <= 200: raise ValueError('Use a nonempty literal search of at most 200 characters')
        if paths is not None and (not isinstance(paths, list) or any(p not in sources for p in paths)):
            raise ValueError('Search paths must be snapshot paths')
        matches, total = [], 0
        for source in snap['sources']:
            if paths is not None and source['path'] not in paths: continue
            for line, text in enumerate(source['text'].splitlines(), 1):
                if query.casefold() in text.casefold():
                    total += 1
                    if len(matches) < 80:
                        matches.append({'path': source['path'], 'start_line': line, 'end_line': line,
                                        'text': text[:600], 'truncated': len(text) > 600})
        return {'excerpts': matches, 'totalMatches': total, 'truncated': total > len(matches)}
    raise ValueError('Unknown action; choose read, search, or finish')


def check_evidence(evidence: dict, snap: dict, inspected: list[dict]) -> dict:
    source = next((s for s in snap['sources'] if s['path'] == evidence.get('path')), None)
    start, end, quote = evidence.get('start_line'), evidence.get('end_line'), evidence.get('quote')
    valid_range = source is not None and type(start) is int and type(end) is int and 1 <= start <= end <= source['lines']
    matched = bool(valid_range and isinstance(quote, str) and quote.strip() and
                   quote in '\n'.join(source['text'].splitlines()[start - 1:end]))
    seen = bool(matched and any(e['path'] == source['path'] and e['start_line'] <= start and
                               e['end_line'] >= end and quote in e['text'] for e in inspected))
    return {**evidence, 'sourceMatch': matched, 'inspected': seen,
            'sourceSha256': source['sha256'] if source else None}
