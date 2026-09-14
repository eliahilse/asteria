# Experiment report export

The Experiment tab has two Excel downloads:

- **Export XLSX**: the existing detailed data export, beginning with Combinations.
- **Export report XLSX**: the layout of the supplied `experiment_results_report.xlsx`,
  with counts-first rate cells, context columns, frozen headings and separate
  analysis sheets.

Both follow the reporting standard in [`docs/REPORTING.md`](../docs/REPORTING.md):
every rate is shown as `k/N`, a percentage accompanies the counts only when they
rest on at least 20 trajectories, unresolved outcomes stay visible, and every
table states its observation unit.

Both export the currently loaded observations. The report creates new workbook
content; it does not copy the example's Gemini results or depend on a file in the
user's Downloads directory.

The report retains the eleven original sheet names: Overview, Compile Rate,
Pass Rate, Reuse vs Generation, Compile Failure Analysis, Test Failure Analysis,
Per-Test Breakdown, Tier Breakdown, Context Effect, Delivery & Errors, and Raw
Data. Study and security-strategy columns distinguish cohorts and treatments.
Only Highscore is represented because that is the current experiment task.
Unobserved S/F/B combinations remain blank.

Security Issues, Security Checks, Issue Matrix and Provenance add the security
comparison and its measurement coverage. The positive persistence check is
included in the per-test sheet but excluded from the issue total. Qualification
metadata records which audit was used and how many original measurements were
reclassified.

**Issue Matrix** presents every issue check against its fixed denominator. Rows
are the ten issue checks in protocol order, a `Total (10 issue checks)` row and,
visually separated, the positive `validRecordRoundTrip` check, which is not an
issue. Columns are Study, Test, CWE and, for every condition in plan order,
`<condition> failed`, `<condition> unresolved` and `<condition> passed`; the
first data row states N per check, and header notes state N and 10 × N for the
total. Every check cell sums to N and every total cell to 10 × N, so the
familiar "18/46 versus 38/48" reading with shifting denominators is replaced by
"18 failed · 4 unresolved · 28 passed / 50". Unresolved outcomes are not passes.
A second table, `Unresolved reasons`, lists every (condition, check) cell with
unresolved observations and its reason: unknown means the qualification audit
did not establish the large-record precondition, compile error means the final
artifact failed the security-suite compilation, not run means the check was not
executed, and infrastructure error means a harness failure. The CWE column is
filled from `research/security/cwe-mapping.json` when the headless export finds
that file and stays blank otherwise (the browser download has no file access);
CWE ids label the weakness category a check probes, not a finding. The same
layout is written as Markdown and CSV by
`python3 -m research.issue_matrix --iteration <id>`.

The Overview table "Test Pass Rate by Test Type" and the Tier Breakdown sheet
report the unit and autonomous (wiring) tiers only. The four invoked-integration
(coupling) checks still count toward full functionality and remain in the
Per-Test Breakdown, Test Failure Analysis and Raw Data sheets; they are no longer
a separate headline tier, and the explorer's Combinations export has no
`Invoked %` column.

## Reading the numbers

Every rate in the report is a `k/N` text cell followed by a numeric
`<label> (fraction)` column. The fraction is stored unrounded and carries the
`0.0%` number format only when the count rests on at least 20 trajectories;
checks on one artifact are correlated and do not count as independent
observations, so a cell such as `78/80` evaluated checks on five trajectories
stays a plain number. Measured pass counts append `; u unresolved` whenever
unresolved checks exist. The Overview summary rows read, for example,
`Overall compile 78/80 0.975`.

Compilation counts use all recorded attempts or trajectories. The report's test
pass counts use **passed / evaluated checks on compiled runs**, with only pass and
fail considered evaluated. This conditional rate differs from the explorer's
quality cells, which use all N (7 × N unit checks, 5 × N live checks). The
Context Effect sheet also includes passed checks / (16 × N); its delta columns
are fraction differences, not counts, because the present and absent arms have
different n. Overall cells pool counts rather than averaging context
percentages. Per-test cell notes preserve numerator, denominator and unresolved
coverage, and the unit statement sits on that sheet's `k/N` header cells.

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
It contains 80 raw trajectory rows, 16 security-combination rows, 176 security
test-summary rows including the positive check, and a 15-sheet layout with the
Issue Matrix sheet (53 rows: the fixed-denominator matrix and 35 unresolved
reasons). The [I09 report](iterations/i09-generic-acquisition/experiment_results_report.xlsx)
is exported the same way from the [qualified I09 data](iterations/i09-generic-acquisition/qualified-results.json).
Both are counts-first exports; the original results and prior qualified
workbooks are preserved.

From `workbench/` with Node 24, export a saved dataset without a browser, local
server, model calls or Java evaluation:

```sh
node scripts/export-report.mjs \
  ../research/iterations/i07-operational-replication/qualified-results.json \
  /tmp/i07-experiment-results-report.xlsx
```

`node scripts/export-detailed.mjs INPUT.json OUTPUT.xlsx` writes the detailed
**Export XLSX** workbook the same way. Both commands refuse to overwrite an existing output. It uses the same workbook
builder as the browser download. Each saved example's byte hashes, source
hashes and expected row counts are recorded beside it in
`experiment_results_report.manifest.json`.
