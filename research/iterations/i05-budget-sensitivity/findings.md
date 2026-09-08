# I05 findings for review

The complete five-submission experiment reached **75/80 full-functional trajectories**,
with all 80 final artifacts compiling. Requirements and trust-boundary contexts
reduced failed issue-check totals in every paper-context cell after qualification.
Functional tradeoffs remain: requirements reached only 3/5 in Reuse B versus 5/5
controls, and boundaries reached 4/5 in Generation S versus 5/5 controls.

All task, attachment and security-insert bytes were reused from I04. The budget
announcement and remaining-submission feedback changed from three to five. Code
conversations were fresh. This holds context acquisition fixed, but collection
time and response sampling can also affect cross-round differences.

## Per-cell results

All counts below use separately qualified measurements. The ten issue checks
exclude the positive persistence round trip. An evaluated check is an observed
pass or fail; the remaining planned checks are unresolved.

| Paper-context cell | Control full | Requirements full | Boundaries full | Control issues | Requirements issues | Boundaries issues |
| --- | ---: | ---: | ---: | ---: | ---: | ---: |
| Generation S | 5/5 | 5/5 | 4/5 | 37/47 | 8/48 | 29/48 |
| Generation S+F+B | 5/5 | 5/5 | 5/5 | 39/50 | 10/49 | 30/47 |
| Reuse B | 5/5 | 3/5 | 5/5 | 33/48 | 1/46 | 26/47 |
| Reuse S+B | 5/5 | 5/5 | 5/5 | 34/48 | 0/46 | 26/47 |

The minimum count reductions under the most adverse assignment of unresolved
checks are 27, 28, 28 and 30 for requirements, and 6, 6, 4 and 5 for boundaries,
respectively. These bounds are not confidence intervals. The overview arm remains
in the [complete table](qualified-analysis.md); its small count differences do not
establish a consistent decrease when unresolved observations are considered.

## What the additional submissions did

| Submission allowance within I05 | Full functional / 80 | Cumulative code calls | Rejected edits | Failed compilations |
| --- | ---: | ---: | ---: | ---: |
| 1 | 28 | 80 | 24 | 5 |
| 2 | 51 | 132 | 35 | 10 |
| 3 | 65 | 161 | 38 | 12 |
| 4 | 72 | 176 | 38 | 12 |
| 5 | 75 | 184 | 39 | 12 |

Ten trajectories first succeeded after the third submission, using 23 additional
calls across all trajectories that continued. I04 reached 69/80 within its
announced three-submission budget; I05 reached 65/80 at submission three and 75/80
at submission five. The first three I05 submissions are descriptive slices of
conversations that were told they had five attempts, not a controlled three-attempt
counterfactual. The [per-condition curves](submission-curves.csv) retain that
distinction and all intermediate report hashes are verified in
[submission-analysis.json](submission-analysis.json).

The five unsuccessful trajectories have distinct diagnostics. Three repeatedly
missed run-recording behavior: Reuse B requirements repetitions 2 and 5, and
Generation S boundaries repetition 5. Generation S overview repetition 1 failed
the subsequent-run recording checks. Generation S+F+B overview repetition 3 threw
a `NullPointerException` during the autonomous suite's bootstrap because it called
`getHighscore().resetRun(...)` before initialization; JUnit ran zero tests in that
suite, so those five observations remain unknown. All these trajectories stay in
the full-functional denominator.

## A complete passing artifact, with incomplete arm-level coverage

Reuse S+B requirements repetition 2 is the first artifact across I03–I05 to pass
all 16 functional and all 11 declared security checks, including the matched
large-record precondition. Its highscore class checks stored byte size before
reading, caps the record loop and retained entries, and bounds player names.
The exact source, compilation, test outputs and precondition bytes are preserved.
Passing these fixtures is not a proof of general security.

The other four trajectories in that arm have no observed issue-check failures
but lack a qualified large-record result. Thus the arm has **0/46 failures, four
unresolved checks and only 1/5 demonstrated joint passes**. Reporting five secure
implementations would be unsupported. The precondition audit made 13 legacy passes
unknown across I05; [original reports](results.json) and
[audit evidence](amplification-audit.json) remain available.

The resource outcomes are still method dependent. Requirements fail the oversized
physical-line fixture in 2/5 Generation S and 4/5 Generation S+F+B artifacts,
compared with 1/5 Reuse B and 0/5 Reuse S+B. Negative-score rejection still fails in
every Generation requirements artifact. The [category analysis](qualified-categories.md)
counts affected trajectories separately from the total number of failed checks.

## Scope and next experiment

I05 contains no new security-context acquisitions and therefore adds no independent
replication of context generation. N=5 code trajectories per combination remains
small: a 5/5 marginal Wilson 95% interval is 56.6–100%. Checks within one artifact
are correlated, and there is no multiplicity-adjusted significance claim or
generalization across repositories. Returned model identity matches Luna; effective
provider settings remain explicitly unverified where unattested.

[I06](../i06-operational-guards/README.md) was declared during this collection. It
adds explicit operation/input/invariant/enforcement/failure fields to newly acquired
repository/task context, compares against the fixed requirements and boundary
inserts, and emphasizes resource outcomes alongside functional success. No I05
code or test results are supplied to that acquisition or its code conversations.

Review the [qualified analysis](qualified-analysis.md), [XLSX](qualified-results.xlsx),
[per-test CSV](qualified-per-test.csv), [security-effect CSV](qualified-security-effects.csv),
[quality/security figure](figures-qualified/quality-and-security.pdf) and
[per-check figure](figures-qualified/per-check-security-effects.pdf).
