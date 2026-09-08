# Experiment report export

The Experiment tab has two Excel downloads:

- **Export XLSX**: the existing detailed data export, beginning with Combinations.
- **Export report XLSX**: the layout of the supplied `experiment_results_report.xlsx`,
  with percentages, context columns, frozen headings and separate analysis sheets.

Both export the currently loaded observations. The report creates new workbook
content; it does not copy the example's Gemini results or depend on a file in the
user's Downloads directory.

The report retains the eleven original sheet names: Overview, Compile Rate,
Pass Rate, Reuse vs Generation, Compile Failure Analysis, Test Failure Analysis,
Per-Test Breakdown, Tier Breakdown, Context Effect, Delivery & Errors, and Raw
Data. Study and security-strategy columns distinguish cohorts and treatments.
Only Highscore is represented because that is the current experiment task.
Unobserved S/F/B combinations remain blank.

Security Issues, Security Checks and Provenance add the security comparison and
its measurement coverage. The positive persistence check is included in the
per-test sheet but excluded from the issue total. Qualification metadata records
which audit was used and how many original measurements were reclassified.

## Reading the numbers

Compilation rates use all recorded attempts or trajectories. The report's test
pass rates use **passed / evaluated checks on compiled runs**, with only pass and
fail considered evaluated. This conditional rate differs from the explorer's
quality percentages, which use all N. The Context Effect sheet also includes
passed checks / (16 × N). Overall rates pool counts rather than averaging context
percentages. Per-test cell notes preserve numerator, denominator and unresolved
coverage; stored percentage values are unrounded numeric fractions.

Unknown and unexecuted checks cannot establish a pass. Security count differences
use matching fresh controls; bounds allow every assignment of unresolved checks
and are not confidence intervals. The context-presence and method summaries are
descriptive: selected paper contexts may be unbalanced, and multiple code
repetitions share one acquired insert.

The compact dataset does not contain delivered-file counts, compiler diagnostic
counts or a compiler-error taxonomy. Those template fields remain blank, and
known compilation failures are labeled Unclassified. Counts of actual failed
compilations are available. Transport errors are not relabeled as compiler errors.
Most-common-failure entries come from observed test diagnostics; this grouping is
not an independent root-cause analysis. Overlong diagnostic excerpts are marked,
with the full text retained in the detailed export.

## Saved example and reproduction

The [I07 report](iterations/i07-operational-replication/experiment_results_report.xlsx)
uses the unchanged [qualified I07 data](iterations/i07-operational-replication/qualified-results.json).
It contains 80 raw trajectory rows, 16 security-combination rows and 176 security
test-summary rows, including the positive check. The original results and prior
qualified workbook are preserved.

From `workbench/` with Node 24, export a saved dataset without a browser, local
server, model calls or Java evaluation:

```sh
node scripts/export-report.mjs \
  ../research/iterations/i07-operational-replication/qualified-results.json \
  /tmp/i07-experiment-results-report.xlsx
```

The command refuses to overwrite an existing output. It uses the same workbook
builder as the browser download. The saved example's byte hashes and counts are
recorded beside it in `experiment_results_report.manifest.json`.
