"""Build a deterministic, offline research dataset from preserved evidence.

Run from the repository root: python3 -m research.import_evidence
This imports observations; it never executes models or rewrites source results.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import re
from datetime import datetime
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
    def __init__(self, root: Path = ROOT):
        self.root = root
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

    def tests(self, row: dict, historical_security: dict | None) -> list[dict]:
        tests = []
        for suite in ("unit", "invoked", "autonomous"):
            result = row.get("grading" if suite == "unit" else suite, {})
            for name in TEST_NAMES[suite]:
                value = result.get("test_results", {}).get(name)
                status = {"PASS": "pass", "FAIL": "fail"}.get(value, "not_run")
                detail = result.get("test_failure_messages", {}).get(name, "")
                if value is None:
                    if suite == "unit" and row.get("grading", {}).get("compilation_success") is False:
                        detail = "Main compilation failed."
                    else:
                        detail = result.get("detail", "No individual test result recorded.")
                    if result.get("outcome") == "compile-fail":
                        status = "compile_error"
                tests.append({"suite": suite, "name": name, "status": status, "detail": detail, "source": str(PILOT / "run_state.json")})
        for name in TEST_NAMES["security"]:
            error = (historical_security or {}).get("compile_error", "")
            status = "infrastructure_error" if "Unable to locate a Java Runtime" in error else "not_run"
            tests.append({"suite": "security", "name": name, "status": status,
                          "detail": error or "No historical execution recorded.",
                          "source": str(MEETING / "results/adversarial_security_tests.json")})
        return tests

    def build(self) -> dict:
        state = self.read(PILOT / "run_state.json")
        reviews = self.read(PILOT / "manual_adjudication.json")
        published = self.read(PILOT / "published_full_pass_security.json")
        historical = {r["run_id"]: r for r in self.read(MEETING / "results/adversarial_security_tests.json")}
        security_artifact = self.artifact("experiments/vamos_security_pilot/security_context.md")
        security_text = (self.root / security_artifact["path"]).read_text()
        self.artifact(MEETING / "security_tests/ApoMarioHighscoreSecurityTest.java")
        facts = []
        for index, match in enumerate(re.finditer(r"(?ms)^\d+\. (.*?)(?=^\d+\. |\nSecurity is not|\Z)", security_text), 1):
            facts.append({"id": f"legacy-{index}", "type": "legacy_mixed", "text": re.sub(r"\s+", " ", match.group(1)).strip(),
                          "cwes": sorted(set(re.findall(r"CWE-\d+", match.group(1)))), "source": security_artifact,
                          "status": "curated_constraint", "scope": "Highscore", "extraction": "Numbered blocks from the historical Markdown intervention; no automatic source analysis."})
        runs = []
        for run_id, row in sorted(state["runs"].items()):
            prompt = self.artifact(MEETING / "prompts" / f"{row['condition']}.txt")
            if prompt["sha256"] != row["prompt_sha256"]:
                raise ValueError(f"Prompt hash mismatch: {run_id}")
            if len((self.root / prompt["path"]).read_text()) != row["prompt_chars"]:
                raise ValueError(f"Prompt character count mismatch: {run_id}")
            review = reviews["runs"].get(run_id)
            findings = []
            for finding in (review or {}).get("validated_findings", []):
                evidence = self.artifact(PILOT / finding["evidence_file"])
                findings.append({**finding, "status": "source_adjudicated", "evidence": evidence, "lines": finding["evidence_lines"], "dynamicProof": False})
            code = [self.artifact(PILOT / "sanitized_generated" / run_id / path) for path in row.get("sanitized_files", [])]
            raw_dir = self.root / PILOT / "raw_responses"
            raw = self.artifact(PILOT / "raw_responses" / f"{run_id}.txt") if (raw_dir / f"{run_id}.txt").exists() else None
            elapsed = (datetime.fromisoformat(row["received_at"]) - datetime.fromisoformat(row["submitted_at"])).total_seconds()
            runs.append({
                "id": run_id, "cohort": "fresh_pilot", "feature": "Highscore", "model": row["bare_model"],
                "servedModel": row.get("served_model"), "reasoning": row.get("reasoning_effort"), "temperature": row.get("temperature"),
                "strategy": row["strategy"], "condition": row["condition"], "baseContext": row["context"],
                "securityContext": row["security_context"], "contextTypes": ["legacy_mixed"] if row["security_context"] else [],
                "factIds": [f["id"] for f in facts] if row["security_context"] else [], "repetition": row["repetition"],
                "prompt": prompt, "promptChars": row["prompt_chars"], "attachments": row["attachments"],
                "usage": row["usage"], "elapsedSeconds": elapsed, "costUsd": None,
                "submittedAt": row["submitted_at"], "finishReason": row.get("finish_reason"),
                "compileStatus": "pass" if row["grading"]["compilation_success"] else "fail",
                "compileDetail": row["grading"].get("compilation_errors", ""),
                "functionalSuccess": row["full_functional_success"],
                "tests": self.tests(row, historical.get(run_id)),
                "assessment": "not_reviewed" if review is None else ("no_targeted_findings" if review["targeted_findings_free"] else "findings_present"),
                "reviewNotes": (review or {}).get("review_notes", []), "findings": findings,
                "candidates": row.get("security_candidates", []), "code": code, "rawResponse": raw,
                "source": str(PILOT / "run_state.json"), "securityEvaluation": None,
            })
        for row in published:
            findings = []
            for finding in row["validated_new_findings"]:
                e = finding["evidence"]
                findings.append({"cwe": finding["cwe"], "signature": finding["signature"], "severity": finding["severity"],
                    "reason": finding["note"], "status": "source_adjudicated", "dynamicProof": False,
                    "evidence": self.artifact(PILOT / "sanitized_generated" / row["run_id"] / e[0]["file"]), "lines": [x["line"] for x in e]})
            runs.append({"id": row["run_id"], "cohort": "published_selected", "feature": "Highscore", "model": row["model"],
                "servedModel": None, "reasoning": None, "temperature": None, "strategy": row["strategy"],
                "condition": f"published_p{row['prompt_id']}", "baseContext": row["context"], "securityContext": False,
                "contextTypes": [], "factIds": [], "repetition": row["run"], "prompt": None, "promptChars": None,
                "attachments": [], "usage": {}, "elapsedSeconds": None, "costUsd": None, "submittedAt": None, "finishReason": None,
                "compileStatus": "pass", "compileDetail": "", "functionalSuccess": row["full_functional_success"], "tests": [],
                "reportedSuites": {s: row[f"{s}_tests"] for s in ("unit", "invoked", "autonomous")},
                "assessment": "findings_present", "reviewNotes": ["Selected six fully functional outputs from 80 attempts. Individual test observations are not imported; archived summary reports 16/16."],
                "findings": findings, "candidates": [], "code": [self.artifact(PILOT / "sanitized_generated" / row["run_id"] / p) for p in row["sanitized_feature_files"]],
                "rawResponse": None, "source": str(PILOT / "published_full_pass_security.json"), "securityEvaluation": None})
        # New evaluations live separately from preserved historical observations.
        evaluation_path = Path("research/results/highscore-security-v1.json")
        if (self.root / evaluation_path).exists():
            evaluation = self.read(evaluation_path)
            for run in runs:
                observed = next((r for r in evaluation["runs"] if r["run_id"] == run["id"]), None)
                if observed:
                    run["securityEvaluation"] = {**observed, "source": str(evaluation_path), "protocol": evaluation["protocol"], "environment": evaluation["environment"]}
        for suite, filename in (("unit", "ApoMarioHighscoreTest.java"), ("invoked", "ApoMarioHighscoreInvokedTest.java"), ("autonomous", "ApoMarioHighscoreAutonomousTest.java")):
            self.artifact(Path("vamos-artifact/Tests") / filename)
        dataset = {
            "schemaVersion": 1, "title": "Highscore · security context study", "artifactCommit": state["artifact_commit"],
            "cohorts": [{"id": "fresh_pilot", "label": "Fresh pilot", "attempts": 16, "selection": "All 16 attempts; two repetitions per model × strategy × intervention cell."},
                        {"id": "published_selected", "label": "Published · selected", "attempts": 80, "selection": "Only the six fully functional outputs are included. Not representative of all 80 attempts; never pool with the fresh pilot."}],
            "threatModel": reviews["threat_model"], "facts": facts, "runs": runs,
            "limitations": ["Exploratory sample: two repetitions per fresh-pilot cell; no causal or general security claims.",
                "Source adjudication and executable security checks are separate evidence layers.",
                "The historical six-check security suite did not execute: Java runtime unavailable.",
                "No historical billed costs were recorded. Queue elapsed time is not model latency.",
                "New context definitions and Luna experiments are planned, not observed results."],
            "artifacts": sorted(self.artifacts.values(), key=lambda a: a["path"]),
        }
        dataset["fingerprint"] = digest(canonical(dataset))
        return dataset

    def write(self, output: Path) -> dict:
        dataset = self.build()
        (output / "evidence").mkdir(parents=True, exist_ok=True)
        for sha, content in self.payloads.items():
            (output / "evidence" / f"{sha}.txt").write_bytes(content)
        (output / "study.json").write_bytes(canonical(dataset))
        return dataset


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--output", type=Path, default=ROOT / "workbench/public/data")
    args = parser.parse_args()
    dataset = Importer().write(args.output)
    print(f"Imported {len(dataset['runs'])} runs, {len(dataset['artifacts'])} artifacts; SHA-256 {dataset['fingerprint']}")


if __name__ == "__main__":
    main()
