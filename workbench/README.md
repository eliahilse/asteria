# Highscore explorer

A plain interface for individual test outcomes, security contexts and prompt
comparisons. The default dataset contains the current study and no old runs.

```sh
cd workbench
npm ci
npm run dev -- --port 5173
```

Open http://localhost:5173. Changes update live. No model connection is needed to
inspect the test definitions, extracted contexts or frozen prompts.

**Experiment** is the default tab. It shows the original Generation/Reuse ×
S/F/B matrix with security cases as additional columns. Local attempts are read
automatically from `.local/experiments/<study-id>`; no import environment variables
are needed for this matrix. Open a cell for all 27 test contracts, separate outcome
counts, raw diagnostics and exact submitted prompts. JSON/XLSX exports retain
condition, attempt and test-level denominators. The public build contains plans
only. See [the two-stage protocol](../research/MATRIX_EXPERIMENT.md).

**Context generation** creates fresh Luna security context from a selected game
repository and a feature task. Its local backend reads the gitignored adapter
configuration and saves inputs, outputs and traces in `.local/context-generation`.
See [the acquisition protocol](../research/CONTEXT_GENERATION.md). Generation has
its own JSON exports; experiment exports do not include private generations.

## Earlier single-axis study views

The normal navigation contains Experiment and Context generation. Earlier Results,
Runs, Contexts and Conditions views remain accessible through explicit `?view=results`,
`?view=runs`, `?view=contexts` and `?view=conditions` links for reference; they do not
show the current paper-matrix observations.

The [adapter](../research/ADAPTER.md) writes run records and the
[evaluator](../research/EVALUATION.md) writes separate reports. Select those
folders explicitly when starting the explorer (paths resolve from the repo root):

```sh
ASTERIA_RUNS_DIR=.local/runs/luna-highscore-v1 \
ASTERIA_EVALUATIONS_DIR=.local/evaluations \
npm run dev -- --port 5173
```

Both directories and generated browser data are gitignored. A normal build or
CI deployment imports no private run folders. Calibration outputs cannot become
study runs. Old generated result payloads are removed when rebuilding the data.

## Read the results

Results compare each test between a baseline and security treatment of the same
strategy and scenario. Pass, fail and unresolved counts are separate. Open a test
for its fixture, expected behavior, limits, individual diagnostics and denominator
breakdown. A missing result never becomes a pass, fail or zero-percent rate.

Runs expose response and evaluation records. Contexts show typed source records,
source locations and extraction limits. Conditions show context counts, exact
prompts and their differences from the corresponding baseline.

XLSX contains test comparisons, definitions, runs, individual check observations,
conditions, context records, schedule and exact evidence text. JSON includes the
study and current selection. Long text is split without truncation; every embedded
artifact is checked against its SHA-256. Missing rates/costs remain blank.

```sh
npm run build
npm test
PLAYWRIGHT_CHANNEL=chrome npm run test:e2e
```

CI verifies data, controls, browser behavior and full functional calibration before
publishing the static site. See [data semantics](../docs/EVIDENCE_MODEL.md).
