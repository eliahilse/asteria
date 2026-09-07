# Asteria — bachelor-thesis workspace

The live [research workbench](https://eliahilse.github.io/asteria/) provides run-level results,
individual functional/security checks, source evidence, exact prompt comparisons,
typed security contexts and XLSX export. Start with the
[meeting brief](docs/MEETING_BRIEF.md) for the current findings and their limits.
See [local development instructions](workbench/README.md) to run it yourself.

```sh
cd workbench
npm ci
npm run dev
```

The next Luna study has 16 frozen conditions and 140 planned attempts. Connect
your provider through the [private adapter protocol](research/ADAPTER.md); local
adapters, credentials and observations stay gitignored. Validate the plan without
making model calls with `python3 -m research.run_experiment` from the root.

Migrated from the local `B` workspace. The reports, experiments, audit evidence,
corpus, and upstream source snapshots are preserved here. See
[`docs/MIGRATION.md`](docs/MIGRATION.md) for provenance and migration details.

## Apo-Games security context study

Working area for the security track of the bachelor-thesis collaboration
(context-for-LLMs in software engineering). This pass answers: **can the
Apo-Games corpus carry the security angle of the project, and what security
issues does it actually contain?**

## Earlier corpus audit
- **`docs/REPORT.md`** — feasibility verdict, threat model, validated findings,
  recommendation for the meeting. Read this first.
- **`docs/RESULTS.md`** — auto-generated category taxonomy + corroborated
  findings + clone-propagation tables.
- **`docs/METHODOLOGY.md`** — how findings were produced and validated (the
  two-family audit + executable-PoC method).

## Proof
- **`poc/`** — runnable proofs-of-concept distilled from cited corpus code.
  `poc/run_all.sh` compiles and runs them (needs a JDK; tested on OpenJDK 26).

## Data & pipeline
- `apogames/` — upstream clone (Bitbucket `Jacob_Krueger/apogamessrc`).
- `corpus/` — normalized one-tree-per-variant source (loose + unpacked-from-jar).
- `scripts/` — `build_corpus.sh`, `sink_census.py`, `run_audit.sh`,
  `extract_findings.py`, `reconcile.py`, `gen_report.py`.
- `audit/prompts/` — the audit prompt. `audit/runs/{oxa,codex}/` — raw model
  transcripts (kept for auditability). `audit/findings/` — extracted JSON.

## Reproduce
```
scripts/build_corpus.sh
python3 scripts/sink_census.py
scripts/run_audit.sh oxa 8 && scripts/run_audit.sh codex 8
python3 scripts/extract_findings.py && python3 scripts/reconcile.py
python3 scripts/gen_report.py
poc/run_all.sh
```
