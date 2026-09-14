# Reporting standard: counts first

Every result table in this project shows counts before anything else. This page
states the standard, explains the example that motivated it, and maps every
explorer, workbook and Markdown table to it. The constants live in
`workbench/src/combination-stats.ts` (`PERCENT_MIN_N`, `Fraction`,
`percentAllowed`) and `research/report_matrix.py` (`PERCENT_MIN_N`, `rate`,
`count_delta`); every table builder imports them from there.

## The example that motivated it

A supervisor read a row of the old combination table as `80, 80, 80, 80` and
asked what it meant. The four cells were correct, and each was a different
fraction of a different denominator on the same five trajectories:

| Cell | What was counted | Old cell | Cell now |
| --- | --- | --- | --- |
| Compile | 4 of 5 trajectories compiled | `80.0` | `4/5` |
| Unit | 28 of 7 × 5 = 35 unit checks passed | `80.0` | `28/35` |
| Live | 20 of 5 × 5 = 25 autonomous checks passed | `80.0` | `20/25` |
| Full | 4 of 5 trajectories passed all 16 checks | `80.0` | `4/5` |

The percentages hid that the row rests on five trajectories, that the check
counts are correlated outcomes of those same five artifacts, and that "80 %"
of five is one trajectory away from "60 %" or "100 %". The counts say all of
that by themselves.

## The standard

1. **Every rate is `k/N`.** Numerator and denominator are shown. A percentage
   may accompany them only when N ≥ 20, and it never replaces them.
2. **Fixed denominators.** Per condition: N trajectories (or attempts). Per
   check: N. The ten issue checks: 10 × N. Unresolved outcomes (not run,
   unknown, compile error, infrastructure error) stay in the denominator and
   are shown as their own count. They are never dropped and never passes.
3. **No pooling of percentages.** Figures across conditions with different N
   are never averaged; a pooled figure is computed from pooled counts.
4. **Intervals.** A Wilson 95 % interval is given only for the primary endpoint
   (full functional) and only beside its `k/N`. Missingness bounds are
   identification bounds and are labelled as such; they are never called a
   confidence interval.
5. **Unit and N in every table.** Each table states its observation unit
   (trajectory, check, artifact) and N in its header, caption or cells.

**What N means for the percentage rule.** N is the number of independent
observations: trajectories or attempts. Checks measured on one artifact are
correlated, so `28/35` unit checks on five trajectories has N = 5, not 35, and
gets no percentage. In code this is the `units` field of a `Fraction`; a
percentage or `0.0%` number format is permitted only when
`percentAllowed(fraction)` is true.

**Deltas.** A treatment-minus-control difference is a count difference at equal
N, written `Δ (k−k)`, for example `−1 (4−5)`. Percentage points follow only when
N ≥ 20. Where arms have different N and both are below 20, the cell says so
instead of printing a percentage-point difference.

## Explorer, Experiment tab

Source: `workbench/src/experiment.tsx`, `combination-stats.ts`,
`experiment-export.ts`. Unit: trajectory (iterations) or attempt (original
study); the note above the table names it.

| Column | Cell | Denominator | Percentage |
| --- | --- | --- | --- |
| N | count | — | — |
| Compiled | `k/N` | N trajectories | tooltip only |
| Unit checks | `k/N` | 7 × N | tooltip only |
| Live checks | `k/N` | 5 × N | tooltip only |
| Full | `k/N` | N trajectories | tooltip only |
| Security issues | `failed · unresolved / 10 × N` with the identification-bound arrow | 10 × N | never |

The tooltip of every rate cell states `k/N = p %` and, where N < 20, that the
percentage is not printed because the counts are the result. An empty rate is
shown as `—`, never as `0`.

**Export XLSX (detailed).** The Combinations sheet carries `N` and, for each
stat, `<stat>_numerator`, `<stat>_denominator` and `<stat>_fraction` (a number
in 0–1; no percentage column). The remaining sheets are data sheets: Matrix
(counts beside `full_functional_rate`), Functional intervals (`passed`,
`trajectories`, `rate`, Wilson bounds, `scope`), Test rates (per-check counts
with Wilson bounds as data fields), Security comparisons (control and treatment
counts beside the percentage-point deltas), Attempts, Check observations. The
per-check intervals and percentage-point deltas in those sheets are analysis
fields kept for reproducibility; a table that quotes them must apply rules 1
and 4 (counts first, Wilson only for full functional).

**Export JSON** is the raw dataset: per-condition counts (`attempts`,
`compiled`, `fullFunctional`, per-check `pass` / `fail` / `executed` /
`attempts`) and per-run observations. Its only derived rate fields are the
per-check `passRate` and `allAttemptRate`, stored beside their counts.

## Report workbook (`experiment_results_report.xlsx`)

Source: `workbench/src/report-workbook.ts`, `report-data.ts`. Every rate is a
`k/N` text cell followed by a numeric `<label> (fraction)` column; the fraction
is unrounded and carries the `0.0%` number format only when the count rests on
at least 20 trajectories. Measured pass counts append `; u unresolved` to the
`k/N` text whenever unresolved checks exist. Each table states its unit in a
caption, header note or header. Sheet names and order are unchanged.

| Sheet / table | Unit | Cells |
| --- | --- | --- |
| Overview, Experiment Summary | trajectory (study) | `Overall compile` = compiled / N; `Overall test pass` = passed / evaluated checks on compiled runs, with unresolved; `Fully functional / all runs` = full / N; the `Fraction` column holds the number |
| Overview, Test Pass Rate by Test Type | check | Pass rate `k/N` + fraction, Unresolved count, per headline tier |
| Overview, Per-Task / Method / Security Summary | trajectory (Compile), check (Test Pass) | `k/N` + fraction pairs, Compile Fails, n |
| Compile Rate | trajectory | pivot by paper context: compiled / N per cell, Overall pooled from counts; caption below |
| Pass Rate | check | pivot: passed / evaluated on compiled runs, unresolved beside; caption below |
| Reuse vs Generation | check, then trajectory | the two pivots above, each with a caption |
| Compile Failure Analysis | trajectory | `# failed runs`, `Compile fail / all runs` `k/N` + fraction (lower is better) |
| Test Failure Analysis | check | `# fails`, `# tested`, `# compiled`, `# unresolved`, `Fail rate` `k/N` + fraction |
| Per-Test Breakdown | check | per test × paper context: passed / evaluated `k/N` + fraction; the unit statement is a note on the `k/N` header cells and every cell note gives passed, evaluated, failed and unresolved |
| Tier Breakdown | check | one pivot per headline tier (Unit, Autonomous), each with a caption |
| Context Effect, presence table | trajectory (compile), check (pass) | `n present`, `n absent`, `k/N` + fraction for each arm, deltas as fraction differences (not counts, because n present ≠ n absent) |
| Context Effect, ranked table | trajectory, check | `n`, Compile, Pass (measured), Pass (all runs = passed / 16 × n) as `k/N` + fraction |
| Delivery & Errors | trajectory | counts and means only |
| Raw Data | one run | P / T counts per tier and combined; `Combined (fraction)` is numeric without a percentage format (T ≤ 16) |
| Security Issues | issue check | N, failures, evaluated, unresolved, expected = 10 × N, equal-coverage delta, identification bounds |
| Security Checks | check | N, passed, failed, evaluated, unresolved, `Fail rate` `k/N` + fraction, unresolved reasons |
| Issue Matrix | check | failed / unresolved / passed per condition with an `N per check` row and a `Total (10 issue checks)` row summing to 10 × N; unresolved reasons table |
| Provenance | — | definitions, including this standard |

The workbook contains no interval columns. Wilson intervals for the full
functional endpoint are in the Markdown analysis and the detailed export.

## Python Markdown reports

| Report | Builder | Cells |
| --- | --- | --- |
| `results.md` per iteration | `research/iteration_results.py` | counts only: N, first-submission full, within-budget full, compiled, issue failures / evaluated, unresolved, model calls |
| `analysis.md`, `qualified-analysis.md` | `research/scientific_summary.py` (`render`) | condition table: N, counts, `Within-budget full k/N [Wilson 95%, %]` as `4/5 [37.6, 96.4]`; comparisons: `Δ full (treatment − control)` as `−1 (4−5)`, issue-count identification bounds; issue-check table: `failed/evaluated` with `· u unresolved` where evaluated < N; captions state unit and N |
| `results.md` of the original matrix | `research/report_matrix.py` (`rate`) | `k/N` cells, percentage only at n ≥ 20; `Wilson 95% for fully functional`; security differences as `Δ passes (k−k)` |
| `issue-matrix.md`, `issue-matrix.csv` | `research/issue_matrix.py` | failed / unresolved / passed against N per check and 10 × N |

`analysis.json`, `per-test.csv` and `security-effects.csv` are data files. They
keep per-check Wilson bounds and rate fields with their counts; a table built
from them follows the rules above. Regenerating an analysis changes only
`analysisSha256` (the hash of the builder) and, from this standard on, adds
the count-difference fields `withinBudgetFullCountDelta`,
`firstFullCountDelta`, `treatmentN` and `controlN` to each comparison.

## Checklist for a new table

- Unit named; N stated in the header, caption or every cell.
- Each rate written `k/N`; percentage only when the independent N ≥ 20.
- Unresolved counted and shown; denominators fixed by the plan, not by what
  happened to run.
- Pooled figures from pooled counts, never from averaged percentages.
- Wilson only for full functional, beside its `k/N`; bounds from missingness
  labelled as identification bounds.
- Deltas as count differences at equal N.
