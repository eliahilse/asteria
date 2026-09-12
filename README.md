# Asteria — bachelor-thesis workspace

The [paper workspace](paper/README.md) contains the LaTeX manuscript, starting with
a short abstract and TL;DR. Build its PDF with `make -C paper`.

The [Highscore explorer](https://eliahilse.github.io/asteria/) compares code quality
and detected security-issue checks for each context combination. Its context tab
shows the exact generated prompt inserts. See the [iteration index](research/iterations/README.md),
[preserved evidence and restore instructions](research/results/README.md), and
[local setup](workbench/README.md).
The [research readout](research/iterations/REVIEW.md) connects the context definitions,
experimental units, per-check findings and interpretation limits.
New [security-context acquisitions](research/CONTEXT_GENERATION.md) use a minimal
task-only instruction, with repository tools and evidence recording. Completed
results below retain their original perspective-guided prompts.

Latest completed evidence: [I07 findings](research/iterations/i07-operational-replication/findings.md),
[two-round comparison](research/iterations/i07-operational-replication/replication-analysis.md)
and [XLSX](research/iterations/i07-operational-replication/qualified-results.xlsx).
The local table is at `http://127.0.0.1:5173/?iteration=i07-operational-replication`.
I07 reached 74/80 full-functional trajectories. Lower total issue counts recur,
but the earlier perfect operational resource result did not fully replicate.
[I08](research/iterations/i08-evaluator-repeatability/findings.md) repeated 32 fixed
artifacts twice: no measurement changes or source/class mismatches; unsupported
checks remain unresolved.

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
