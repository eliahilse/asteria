"""Double-save finished trajectories during collection; never copy active outputs."""
import argparse
import json
from pathlib import Path
import re
import shutil

from research.archive_results import BACKUPS, SECRET_PATTERNS
from research.import_evidence import ROOT, canonical, digest
from research.iteration_results import index
from research.run_experiment import write_atomic


def save(identifier):
    data = index(iteration=identifier); study = data['studies'][0]
    runtime = ROOT / '.local/iterations' / identifier
    outside = BACKUPS / (identifier + '-live'); outside.mkdir(parents=True, exist_ok=True)
    files, completed = {}, 0
    finished_ids = {r['runId'] for r in study['runs'] if r['status'] != 'started'}
    for row in study['plan']['schedule']:
        if row['runId'] not in finished_ids: continue
        path = runtime / 'runs' / row['runId'] / 'record.json'
        if not path.exists(): continue
        record = json.loads(path.read_text())
        if record['status'] == 'started' or not record.get('finishedAt'): continue
        record_hash = digest(path.read_bytes())
        for source in sorted(path.parent.rglob('*')):
            if source.is_symlink(): raise ValueError('Checkpoint links are not supported')
            if not source.is_file(): continue
            relative = source.relative_to(runtime); destination = outside / relative
            raw = source.read_bytes()
            if any(re.search(pattern, raw) for pattern in SECRET_PATTERNS): raise ValueError('Potential private configuration in completed evidence')
            expected = digest(raw)
            if not destination.exists():
                destination.parent.mkdir(parents=True, exist_ok=True); shutil.copy2(source, destination)
            if digest(destination.read_bytes()) != expected: raise ValueError('Completed backup differs; never overwrite sealed evidence')
            files[str(relative)] = {'sha256': expected, 'bytes': len(raw)}
        if digest(path.read_bytes()) != record_hash: raise ValueError('Trajectory changed during checkpoint')
        completed += 1
    # Initial prompts and the frozen manifest are also copied exactly. Full
    # dependency restoration uses the prepared snapshot until final archiving.
    for source in [runtime / 'manifest.json', *sorted((runtime / 'prompts').glob('*.txt'))]:
        relative = source.relative_to(runtime); destination = outside / relative
        destination.parent.mkdir(parents=True, exist_ok=True)
        if not destination.exists(): shutil.copy2(source, destination)
        if destination.read_bytes() != source.read_bytes(): raise ValueError('Frozen plan backup differs')
        files[str(relative)] = {'sha256': digest(source.read_bytes()), 'bytes': source.stat().st_size}
    manifest = {'iteration': identifier, 'completedTrajectories': completed, 'sourcePlanFingerprint': study['plan']['fingerprint'],
        'purpose': 'Additional live backup of sealed trajectories. Use the prepared full snapshot for frozen dependencies; this is not a standalone full archive.', 'files': files}
    write_atomic(outside / f'checkpoint-{completed:03d}.json', manifest)
    public = ROOT / 'research/iterations' / identifier / 'checkpoints'; public.mkdir(parents=True, exist_ok=True)
    target = public / f'{completed:03d}-finished.json'
    if target.exists(): raise ValueError('That public checkpoint already exists')
    write_atomic(target, data)
    print(f'{identifier}: {completed} finished trajectories double-saved; {len(files)} copied files verified; public progress retains pending statuses')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration', required=True)
    save(parser.parse_args().iteration)
