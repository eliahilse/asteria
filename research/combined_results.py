"""Combine every round's saved qualified results into one multi-study dataset.

Reads `research/iterations/<round>/qualified-results.json` for every completed
round (natural round order), keeps each study with its measurement
qualification, and writes `research/iterations/all-rounds.json` in the same
shape the workbench and the XLSX exporters read (`{local, tests, studies}`).
`--write-public` also writes it as `workbench/public/data/matrix.json`, so the
static workbench shows every round; `--export` runs the two workbook exporters
into `research/iterations/all-rounds-report.xlsx` and `all-rounds-detailed.xlsx`.
No model calls.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path
import re
import subprocess

from research.import_evidence import ROOT, canonical, digest

ITERATIONS = ROOT / 'research/iterations'
OUTPUT = ITERATIONS / 'all-rounds.json'


def natural_key(name: str):
    match = re.match(r'i(\d+)([a-z]?)-', name)
    return (int(match.group(1)), match.group(2)) if match else (999, name)


def combine(root: Path = ROOT) -> dict:
    studies, tests = [], None
    for directory in sorted((p for p in (root / 'research/iterations').iterdir() if (p / 'qualified-results.json').exists()), key=lambda p: natural_key(p.name)):
        data = json.loads((directory / 'qualified-results.json').read_text())
        if tests is None: tests = data.get('tests')
        for study in data['studies']:
            study.setdefault('measurementQualification', data.get('measurementQualification'))
            study['plan'].setdefault('round', directory.name)
            studies.append(study)
    return {'local': False, 'tests': tests, 'studies': studies, 'rounds': [s['plan']['id'] for s in studies], 'analysisSha256': digest(Path(__file__).read_bytes())}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--write-public', action='store_true'); parser.add_argument('--export', action='store_true'); parser.add_argument('--output', type=Path, default=OUTPUT)
    args = parser.parse_args()
    data = combine(); payload = canonical(data); args.output.write_bytes(payload)
    trajectories = sum(len(s['runs']) for s in data['studies'])
    print(f"{len(data['studies'])} studies, {trajectories} trajectories -> {args.output}")
    if args.write_public:
        target = ROOT / 'workbench/public/data/matrix.json'; target.parent.mkdir(parents=True, exist_ok=True); target.write_bytes(payload); print(f'workbench matrix: {target}')
    if args.export:
        for script, name in (('export-report.mjs', 'all-rounds-report.xlsx'), ('export-detailed.mjs', 'all-rounds-detailed.xlsx')):
            out = ITERATIONS / name; out.unlink(missing_ok=True)
            subprocess.run(['node', f'scripts/{script}', str(args.output), str(out)], cwd=ROOT / 'workbench', check=True)


if __name__ == '__main__':
    main()
