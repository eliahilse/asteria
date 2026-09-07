"""Build a deterministic, offline research dataset from preserved evidence.

Run from the repository root: python3 -m research.import_evidence
This imports observations; it never executes models or rewrites source results.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import re
import os
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
PILOT = Path("experiments/vamos_security_pilot/results")
MEETING = Path("experiments/meeting_iteration_2")
TEST_NAMES = {
    "unit": ["emptyBoardInitially", "saveAddsEntry", "parallelListsAligned", "boardSortedDescendingByPoints", "entriesPersistAcrossSessions", "emptyPersistRobust", "rankingPreservedAfterReload"],
    "invoked": ["recordsRealScore", "recordsRealSurvivalTime", "recordsRealPlayerName", "recordRunEndAddsExactlyOneEntry"],
    "autonomous": ["runEndRecordsScoreWithoutHelp", "recordedNameIsTheRealPlayersName", "noPhantomEntriesWithoutRunEnd", "recordedSurvivalTimeIsTheRealElapsedTime", "secondRunAlsoRecordedAndBoardSortedDescending"],
    "security": ["rejectsNegativeScore", "rejectsNegativeTime", "rejectsNullName", "rejectsBlankName", "rejectsExcessiveName", "boundsRetainedEntries"],
}


def digest(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


def canonical(value: object) -> bytes:
    return (json.dumps(value, ensure_ascii=False, sort_keys=True, indent=2) + "\n").encode()


class Importer:
    def __init__(self, root: Path = ROOT, runs_dir: Path | None = None, evaluations_dir: Path | None = None):
        self.root = root
        self.runs_dir = runs_dir
        self.evaluations_dir = evaluations_dir
        self.artifacts: dict[str, dict] = {}
        self.payloads: dict[str, bytes] = {}

    def artifact(self, path: Path | str) -> dict:
        path = Path(path)
        full = (self.root / path).resolve()
        if not full.is_relative_to(self.root.resolve()):
            raise ValueError(f"Artifact outside repository: {path}")
        data = full.read_bytes()
        sha = digest(data)
        rel = str(full.relative_to(self.root.resolve()))
        asset = f"data/evidence/{sha}.txt"
        record = {"path": rel, "sha256": sha, "bytes": len(data), "href": asset}
        self.artifacts[rel] = record
        self.payloads[sha] = data
        return record

    def read(self, path: Path | str):
        self.artifact(path)
        return json.loads((self.root / path).read_text())

    def build(self) -> dict:
        from research.study_data import build_study
        return build_study(self)

    def write(self, output: Path) -> dict:
        dataset = self.build()
        (output / "evidence").mkdir(parents=True, exist_ok=True)
        for sha, content in self.payloads.items():
            (output / "evidence" / f"{sha}.txt").write_bytes(content)
        # Remove obsolete generated payloads so deleted runs cannot remain downloadable.
        for old in (output / "evidence").glob("*.txt"):
            if re.fullmatch(r"[0-9a-f]{64}", old.stem) and old.stem not in self.payloads:
                old.unlink()
        (output / "study.json").write_bytes(canonical(dataset))
        return dataset


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--output", type=Path, default=ROOT / "workbench/public/data")
    parser.add_argument('--runs-dir', type=Path, default=os.environ.get('ASTERIA_RUNS_DIR'))
    parser.add_argument('--evaluations-dir', type=Path, default=os.environ.get('ASTERIA_EVALUATIONS_DIR'))
    args = parser.parse_args()
    resolve = lambda path: (ROOT / path).resolve() if path else None
    dataset = Importer(runs_dir=resolve(args.runs_dir), evaluations_dir=resolve(args.evaluations_dir)).write(args.output)
    print(f"Imported {len(dataset['runs'])} runs, {len(dataset['artifacts'])} artifacts; SHA-256 {dataset['fingerprint']}")


if __name__ == "__main__":
    main()
