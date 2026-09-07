#!/usr/bin/env python3
"""Export the four exact prompt payloads used by the completed pilot."""

from __future__ import annotations

import hashlib
import json
import sys
from pathlib import Path


HERE = Path(__file__).resolve().parent
PILOT = HERE.parent / "vamos_security_pilot"
OUT = HERE / "prompts"


def main() -> None:
    sys.path.insert(0, str(PILOT))
    import run_pilot as pilot  # type: ignore

    gr = pilot.configure_author_harness()
    state = json.loads(pilot.STATE_FILE.read_text(encoding="utf-8"))
    OUT.mkdir(parents=True, exist_ok=True)
    manifest = []

    for condition in pilot.CONDITIONS:
        prompt, prompt_row, attachments = pilot.build_prompt(gr, condition)
        digest = hashlib.sha256(prompt.encode("utf-8")).hexdigest()
        expected = {
            row["prompt_sha256"]
            for row in state["runs"].values()
            if row.get("condition") == condition
        }
        assert expected == {digest}, (
            f"Prompt hash mismatch for {condition}: built {digest}, persisted {expected}"
        )
        path = OUT / f"{condition}.txt"
        path.write_text(prompt, encoding="utf-8")
        manifest.append(
            {
                "condition": condition,
                "strategy": pilot.CONDITIONS[condition]["strategy"],
                "context": pilot.CONDITIONS[condition]["context"],
                "security_context": pilot.CONDITIONS[condition]["security_context"],
                "paper_prompt_id": prompt_row["id"],
                "characters": len(prompt),
                "bytes": len(prompt.encode("utf-8")),
                "sha256": digest,
                "attachments": attachments,
                "file": path.name,
            }
        )

    (OUT / "manifest.json").write_text(
        json.dumps(manifest, indent=2, ensure_ascii=False) + "\n",
        encoding="utf-8",
    )
    lines = [
        "# Exact assembled pilot prompts",
        "",
        "These are the exact user-message payloads used for all 16 completed calls. Repetitions and models within a condition received identical prompts. Each SHA-256 hash is verified against `vamos_security_pilot/results/run_state.json`.",
        "",
        "| File | Strategy | S/B/F | Security context | Characters | SHA-256 |",
        "|---|---|---|---:|---:|---|",
    ]
    for row in manifest:
        lines.append(
            f"| `{row['file']}` | {row['strategy']} | {row['context']} | "
            f"{'yes' if row['security_context'] else 'no'} | {row['characters']} | "
            f"`{row['sha256']}` |"
        )
    lines.extend(
        [
            "",
            "Each text file contains the original VaMoS task prompt, the inlined S/B/F JSON and Java attachments for that condition, the optional security-findings block, and the exact output-format suffix.",
        ]
    )
    (OUT / "README.md").write_text("\n".join(lines) + "\n", encoding="utf-8")
    for row in manifest:
        print(row["file"], row["characters"], row["sha256"])


if __name__ == "__main__":
    main()
