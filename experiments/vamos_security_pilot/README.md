# VaMoS Highscore security-context pilot

This experiment extends the VaMoS 2026 replication artifact with a fourth,
static security-findings context. It asks whether small LLMs reproduce known
Highscore weaknesses during generation/reuse, and whether the added findings
context suppresses them without breaking compilation or behavior.

The matched pilot uses the paper's compilation-winning S/B/F conditions:

- Generation + Structural (`p9`)
- Reuse + Structural+Functional+Behavioral (`p7`)
- each unchanged and with the security-findings context
- Gemini 3.5 Flash Lite and GPT-5.4 Mini, two repetitions per arm

This is a 16-run preliminary pilot, not a statistical reproduction of the
paper's 240-run experiment. Attachments are inlined because the Atira proxy has
no Gemini Files API. The paper runner's package/import sanitization is preserved
and the sanitized generated files and diffs are retained for security review.

Starting in the Asteria repository root, run from the Atira checkout so its
environment and Azure identity are available:

```sh
ASTERIA_ROOT="$PWD"
export ATIRA_REPO="$(cd ../Atira/atira && pwd)"
cd "$ATIRA_REPO"
mise exec -- fnox exec -- env PYTHONDONTWRITEBYTECODE=1 \
  .venv/bin/python "$ASTERIA_ROOT/experiments/vamos_security_pilot/run_pilot.py"
```

Adjust `ATIRA_REPO` if the Atira checkout is located elsewhere.

The runner is resumable. It persists Service Bus session IDs in
`results/run_state.json`, receives with PEEK_LOCK, writes each queue response
before completing it, then grades unit, invoked, and autonomous tests.

Primary security outcomes are manually adjudicated root-cause signatures in
only the generated/modified feature code. The automatic candidate scan is a
routing aid, not a vulnerability count.

Meeting-ready outputs:

- `results/MEETING_BRIEF.md` — concise narrative, tables, caveats, and next-step decisions
- `results/FRESH_RESULTS.md` — complete fresh 16-run result table
- `results/PUBLISHED_BASELINE.md` — corrected audit of the six published full-pass runs
- `results/manual_adjudication.json` — machine-readable evidence for the four compiling fresh runs
- `results/run_state.json` — prompts, session IDs, raw grading records, and security candidates
