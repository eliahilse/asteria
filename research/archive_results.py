"""Immutable research snapshots, verified in Git and in a separate sibling folder."""
from __future__ import annotations

import argparse
import gzip
import io
import json
from pathlib import Path
import re
import shutil
import subprocess
import tarfile

from research.import_evidence import ROOT, canonical, digest
from research.run_experiment import timestamp

ARCHIVES = ROOT / 'research/results'
BACKUPS = ROOT.parent / 'asteria-research-backups'
SCOPES = ('experiments', 'calibration', 'context-generation', 'delivery-calibration', 'reports', 'studies', 'iterations')
SECRET_PATTERNS = [rb'-----BEGIN (?:RSA |EC |OPENSSH )?PRIVATE KEY-----',
                   rb'(?i)(?:SharedAccessKey|AccountKey|api[_-]?key|access[_-]?token|client[_-]?secret)\s*[=:]\s*["\x27]?[A-Za-z0-9+/_.-]{24,}',
                   rb'\b(?:ghp_|github_pat_)[A-Za-z0-9_]{25,}']


def verify(directory: Path) -> dict:
    manifest = json.loads((directory / 'manifest.json').read_text())
    archive = directory / 'evidence.tar.gz'
    if digest(archive.read_bytes()) != manifest['archiveSha256']: raise ValueError('Archive hash mismatch')
    seen = set()
    with tarfile.open(archive, 'r:gz') as bundle:
        for entry in bundle:
            if not entry.isfile() or entry.name not in manifest['files'] or entry.name in seen: raise ValueError('Unexpected archive member')
            if digest(bundle.extractfile(entry).read()) != manifest['files'][entry.name]['sha256']: raise ValueError('Member hash mismatch')
            seen.add(entry.name)
    if seen != set(manifest['files']): raise ValueError('Archive is incomplete')
    return manifest


def snapshot(identifier: str, scopes=SCOPES) -> dict:
    if not re.fullmatch(r'[a-z0-9][a-z0-9_-]+', identifier): raise ValueError('Use a lowercase snapshot ID')
    if not scopes or any(scope not in SCOPES for scope in scopes): raise ValueError('Unknown evidence scope')
    directory = ARCHIVES / identifier
    directory.mkdir(parents=True, exist_ok=False)
    files = {}
    for scope in scopes:
        for p in sorted((ROOT / '.local' / scope).rglob('*')):
            if any(part in ('.git', '__pycache__') for part in p.parts): continue
            if p.is_symlink(): raise ValueError('Evidence links are not archived')
            if p.is_file(): files[str(p.relative_to(ROOT))] = p
    # Save the producing tools alongside artifacts, without overwriting current tools on restore.
    for p in sorted((ROOT / 'research').glob('*.py')):
        files[f'.local/archive-tooling/{identifier}/{p.name}'] = p
    manifest = {'id': identifier, 'createdAt': timestamp(), 'repositoryCommit': subprocess.check_output(['git', 'rev-parse', 'HEAD'], cwd=ROOT, text=True).strip(),
                'scopes': list(scopes), 'files': {}, 'exclusions': ['provider-traces', 'adapter/configuration', '.git', '__pycache__']}
    with (directory / 'evidence.tar.gz').open('xb') as output, gzip.GzipFile(filename='', mode='wb', fileobj=output, mtime=0) as compressed, tarfile.open(fileobj=compressed, mode='w') as bundle:
        for name, p in sorted(files.items()):
            raw = p.read_bytes()
            if p.name.startswith('.env') or p.suffix in ('.pem', '.key', '.p12', '.pfx') or any(re.search(pattern, raw) for pattern in SECRET_PATTERNS):
                raise ValueError(f'Potential private configuration in evidence: {name}')
            info = tarfile.TarInfo(name); info.size = len(raw); info.mode = 0o600
            bundle.addfile(info, io.BytesIO(raw))
            manifest['files'][name] = {'sha256': digest(raw), 'bytes': len(raw)}
    manifest['archiveSha256'] = digest((directory / 'evidence.tar.gz').read_bytes())
    (directory / 'manifest.json').write_bytes(canonical(manifest))
    verify(directory)
    external = BACKUPS / identifier
    external.parent.mkdir(parents=True, exist_ok=True)
    shutil.copytree(directory, external)
    verify(external)
    if (external / 'manifest.json').read_bytes() != (directory / 'manifest.json').read_bytes(): raise ValueError('Backup manifests differ')
    print(f"{identifier}: {len(files)} files; {sum(f['bytes'] for f in manifest['files'].values())} bytes; both archive copies verified", flush=True)
    return manifest


def restore(directory: Path, destination: Path):
    manifest = verify(directory)
    destination = destination.resolve()
    with tarfile.open(directory / 'evidence.tar.gz', 'r:gz') as bundle:
        # Validate all destinations before writing any file. Never overwrite different evidence.
        for entry in bundle:
            target = (destination / entry.name).resolve()
            if not target.is_relative_to(destination) or not entry.name.startswith('.local/'): raise ValueError('Unsafe destination')
            if target.exists() and (not target.is_file() or digest(target.read_bytes()) != manifest['files'][entry.name]['sha256']):
                raise ValueError(f'Different file already exists: {entry.name}; restore to a new directory')
        for entry in bundle.getmembers():
            target = destination / entry.name
            if not target.exists():
                target.parent.mkdir(parents=True, exist_ok=True)
                with target.open('xb') as handle: handle.write(bundle.extractfile(entry).read())
    print(f'Restored {len(manifest["files"])} verified files to {destination}', flush=True)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('action', choices=['snapshot', 'verify', 'restore'])
    parser.add_argument('--id', required=True)
    parser.add_argument('--scope', action='append', choices=SCOPES)
    parser.add_argument('--destination', type=Path, default=ROOT)
    args = parser.parse_args()
    if not re.fullmatch(r'[a-z0-9][a-z0-9_-]+', args.id): parser.error('Invalid snapshot ID')
    if args.action == 'snapshot': snapshot(args.id, args.scope or SCOPES)
    elif args.action == 'restore': restore(ARCHIVES / args.id, args.destination)
    else:
        manifest = verify(ARCHIVES / args.id)
        verify(BACKUPS / args.id)
        print(f"Verified both copies: {len(manifest['files'])} files")
