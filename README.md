# Asteria — bachelor-thesis workspace

The [Highscore explorer](https://eliahilse.github.io/asteria/) compares code quality
and detected security-issue checks for each context combination. Its context tab
shows the exact generated prompt inserts. See the [iteration index](research/iterations/README.md),
[preserved evidence and restore instructions](research/results/README.md), and
[local setup](workbench/README.md).

```sh
cd workbench
npm ci
npm run dev
```

The original **2 methods × 8 context combinations** replay and its security
follow-up are preserved. Subsequent experiments use complete feature edits,
bounded functional feedback and a corrected evaluator. Read each iteration's
frozen plan before comparing its results with the original
[single-response protocol](research/MATRIX_EXPERIMENT.md).
Connect your provider through the [private adapter protocol](research/ADAPTER.md);
local adapters, credentials and provider traces stay gitignored. Research
observations are committed in verified archives and copied to the sibling
`asteria-research-backups` folder between iterations. The local Experiment tab
reads current observations as they are evaluated.

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
