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

**Experiment** is the default tab: one row per cohort × Generation/Reuse × S/F/B
combination × security strategy. All combinations are visible without selectors;
security strategies are marked in purple.

The table keeps N and the existing quality metrics: compilation, unit checks,
invoked integration, live integration, and full functional success. Percentages
use all attempts. A single **Security issues** column shows failed issue checks /
evaluated issue checks across the condition's attempts, followed by the change
from its matching fresh control. For example, `27/47 (↓5)` means 27 failed checks
out of 47 evaluated, five fewer than the control. These are repeated check
failures, not unique vulnerabilities. The valid-record round trip is excluded
because it is a positive functional check, not an issue detector.

Count differences require equal attempt counts, no pending attempts, and equal
evaluated counts for every issue check. Otherwise the cell shows `Δ —`; a missing
test must not look like an improvement. Hover for unresolved counts. Replay and
control rows have no treatment difference, and absent observations remain blank.

Local attempts are read automatically from `.local/experiments/<study-id>`.
XLSX starts with the compact **Combinations** sheet; security counts, evaluated and
unresolved checks, and count differences remain separate numeric fields for
analysis. Per-test outcomes, context-type counts and other details remain in the
supporting sheets. JSON retains the complete data. **Export report XLSX** creates
the supplied report layout with context columns, per-test sheets and security
coverage. Its test-pass percentages use evaluated checks on compiled runs;
the table percentages use all N. See [report definitions and reproduction](../research/REPORT_EXPORT.md).
The public build contains the committed completed results.
See [the two-stage protocol](../research/MATRIX_EXPERIMENT.md).

**Context generation** shows the exact generated security prompt inserts for the
active task and iteration, with a copy button. It contains no acquisition form,
metrics or trace dashboard. The harness acquires contexts automatically from the
repository and task; complete traces remain in the saved iteration archives.
See [the acquisition protocol](../research/CONTEXT_GENERATION.md).

The active iteration is read from `.local/iterations/active.json`. Its N counts
independent trajectories; the permitted submission budget is shown above the
table. XLSX retains first-submission success and total model calls separately.
To reopen the original study, use `?iteration=original`; saved iterations can be
opened with `?iteration=<id>`. The same parameter works on the context page.
[Saved snapshots and restore instructions](../research/results/README.md) remain
available in the repository. The static site reads only committed result snapshots
and prompt inserts; provider configuration stays private.

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
# Interpreting the security column

The cell contains failed/evaluated issue checks and a fresh-control difference.
When checks are unmeasured, the arrow range covers every possible assignment of
those outcomes in both arms. `3/46 (↓28–32)` means 28–32 fewer failed checks than
the matched control, allowing all missing outcomes. It is not a confidence
interval. Ranges spanning zero, unequal N and pending comparisons have no arrow.
The XLSX export retains each check, diagnostic, denominator, interval and count
bound separately; the table keeps one security column.
