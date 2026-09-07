#!/usr/bin/env python3
"""Reconstruct and security-audit the six fully functional published runs."""

from __future__ import annotations

import json
from collections import Counter, defaultdict
from pathlib import Path

import run_pilot as pilot


PUBLISHED = pilot.ARTIFACT / "Results"
OUT_JSON = pilot.OUTPUT / "published_full_pass_security.json"
OUT_MD = pilot.OUTPUT / "PUBLISHED_BASELINE.md"


def keyed(rows):
    return {(row["prompt_id"], row["run"]): row for row in rows}


def highscore_feature_files(files: dict[str, str]) -> dict[str, str]:
    return {
        path: code
        for path, code in files.items()
        if Path(path).name.startswith("ApoMarioHighscore")
    }


def manually_adjudicate(candidates: list[dict]) -> list[dict]:
    """Adjudicate only signatures with a direct entry point and sink in source."""
    accepted = []
    for candidate in candidates:
        signature = candidate["signature"]
        if signature == "native_deserialization_of_editable_store":
            accepted.append(
                {
                    **candidate,
                    "status": "validated",
                    "entry_point": "At game initialization, production wiring opens a CWD-relative, locally editable score file",
                    "sink": "ObjectInputStream.readObject()",
                    "reachability": "constructor executes load when the store exists",
                    "note": "Reachable untrusted Java-native-deserialization sink with potentially high impact. Code execution depends on a usable classpath gadget and was not demonstrated; fixed ordinal severity is medium. The original corpus audit found no readObject call.",
                }
            )
        elif signature == "unbounded_record_loading_candidate":
            accepted.append(
                {
                    **candidate,
                    "status": "validated",
                    "entry_point": "At game initialization, production wiring opens a CWD-relative, locally editable score file",
                    "sink": "EOF- or attacker-count-controlled loop retains an uncapped number of parsed records",
                    "reachability": "constructor executes load when the store exists",
                    "note": "Memory use grows with attacker-controlled file contents; source-adjudicated CWE-400 candidate, fixed low severity for this local-file threat model. Resource exhaustion was not dynamically demonstrated.",
                }
            )
    return accepted


def main() -> None:
    pilot.ensure_dependencies()
    gr = pilot.configure_author_harness()
    experiment = keyed(json.loads((PUBLISHED / "experiment_results.json").read_text()))
    invoked = keyed(json.loads((PUBLISHED / "invoked_results.json").read_text()))
    autonomous = keyed(json.loads((PUBLISHED / "autonomous_results.json").read_text()))

    highscore_attempts = {
        key: row for key, row in experiment.items() if row.get("task") == "Highscore"
    }
    full_pass_keys = sorted(
        key
        for key, main_row in highscore_attempts.items()
        if main_row["compilation_success"]
        and main_row["tests_run"] > 0
        and main_row["tests_passed"] == main_row["tests_run"]
        and invoked[key].get("coupling_pass")
        and autonomous[key].get("wiring_pass")
    )
    assert len(highscore_attempts) == 80, "Published Highscore attempt count drifted"
    assert len(full_pass_keys) == 6, "Published Highscore full-pass count drifted"

    rows = []
    for prompt_id, repetition in full_pass_keys:
        run_id = f"published__p{prompt_id}_r{repetition}"
        raw_path = PUBLISHED / "raw_responses" / f"p{prompt_id}_r{repetition}.txt"
        extracted = gr.extract_java_files(raw_path.read_text(encoding="utf-8"))
        sanitized = pilot.sanitize_and_persist(gr, extracted, run_id)
        feature_files = highscore_feature_files(sanitized)
        candidates = pilot.feature_security_candidates(feature_files)
        findings = manually_adjudicate(candidates)

        main_row = experiment[(prompt_id, repetition)]
        invoked_row = invoked[(prompt_id, repetition)]
        autonomous_row = autonomous[(prompt_id, repetition)]
        full_pass = bool(
            main_row["compilation_success"]
            and main_row["tests_run"] > 0
            and main_row["tests_passed"] == main_row["tests_run"]
            and invoked_row.get("coupling_pass")
            and autonomous_row.get("wiring_pass")
        )
        assert full_pass, f"Published full-pass set drifted for {run_id}"

        known = [
            candidate
            for candidate in candidates
            if candidate["signature"]
            in {
                "highscore_plain_http",
                "client_asserted_highscore_without_auth",
            }
        ]
        rows.append(
            {
                "run_id": run_id,
                "prompt_id": prompt_id,
                "run": repetition,
                "model": "gemini-3.1-flash-lite",
                "strategy": main_row["method"],
                "context": main_row["context_label"],
                "unit_tests": f"{main_row['tests_passed']}/{main_row['tests_run']}",
                "invoked_tests": f"{invoked_row['tests_passed']}/{invoked_row['tests_passed'] + invoked_row['tests_failed']}",
                "autonomous_tests": f"{autonomous_row['tests_passed']}/{autonomous_row['tests_passed'] + autonomous_row['tests_failed']}",
                "full_functional_success": full_pass,
                "known_donor_signatures": known,
                "validated_new_findings": findings,
                "secure_functional_success": full_pass and not findings,
                "sanitized_feature_files": sorted(feature_files),
            }
        )

    pilot.atomic_write_json(OUT_JSON, rows)

    by_strategy = defaultdict(list)
    for row in rows:
        by_strategy[row["strategy"]].append(row)
    attempts_by_strategy = Counter(
        row["method"] for row in highscore_attempts.values()
    )
    lines = [
        "# Security audit of the published fully functional runs",
        "",
        "Scope: the six Gemini 3.1 Flash Lite Highscore runs in the VaMoS artifact that pass all 7 unit, 4 invoked, and 5 autonomous tests. Findings are restricted to newly generated Highscore feature classes after replaying the authors' sanitizer.",
        "",
        "| Strategy | Highscore attempts | Fully functional | Reproduced donor HTTP/trust signatures among full-pass | New CWE-502 among full-pass | New CWE-400 among full-pass | Secure functional among full-pass |",
        "|---|---:|---:|---:|---:|---:|---:|",
    ]
    for strategy in ("Reuse", "Generation"):
        group = by_strategy[strategy]
        counts = Counter(
            finding["cwe"]
            for row in group
            for finding in row["validated_new_findings"]
        )
        lines.append(
            f"| {strategy} | {attempts_by_strategy[strategy]} | "
            f"{len(group)}/{attempts_by_strategy[strategy]} | "
            f"{sum(bool(row['known_donor_signatures']) for row in group)}/{len(group)} | "
            f"{counts['CWE-502']}/{len(group)} | {counts['CWE-400']}/{len(group)} | "
            f"{sum(row['secure_functional_success'] for row in group)}/{len(group)} |"
        )

    lines.extend(
        [
            "",
            "## Per-run evidence",
            "",
            "| Run | Strategy | Context | Tests (unit/invoked/autonomous) | Validated generated-code findings |",
            "|---|---|---|---|---|",
        ]
    )
    for row in rows:
        findings = ", ".join(
            f"{finding['cwe']} {finding['signature']}"
            for finding in row["validated_new_findings"]
        ) or "none"
        lines.append(
            f"| {row['run_id']} | {row['strategy']} | {row['context']} | "
            f"{row['unit_tests']} / {row['invoked_tests']} / {row['autonomous_tests']} | {findings} |"
        )

    lines.extend(
        [
            "",
            "Interpretation: none of the six outputs reproduced the donor's online HTTP/client-trust path because all replaced it with local persistence. That is non-reproduction, not evidence of an explicit repair, because online synchronization was not required by the feature contract. However, all three fully functional reuse outputs introduced reachable Java native deserialization (`ObjectInputStream.readObject`) into a locally editable store; the original corpus audit found no `readObject` call. All three generation outputs retained an uncapped number of attacker-controlled local-file records: two via EOF-controlled text loops and one via an attacker-supplied binary count. Under this fixed threat model and source adjudication, zero of six outputs is both fully functional and free of the validated generated-code findings.",
            "",
            "Small-n warning: this is a selected correctness-conditioned subset: 6 of 80 Highscore attempts, not an unbiased vulnerability estimate over the 80 Highscore or all 240 cross-task runs. CWE-502 exploitability and CWE-400 resource exhaustion were not dynamically demonstrated.",
        ]
    )
    pilot.atomic_write_text(OUT_MD, "\n".join(lines) + "\n")
    print(OUT_MD)
    print(OUT_JSON)


if __name__ == "__main__":
    main()
