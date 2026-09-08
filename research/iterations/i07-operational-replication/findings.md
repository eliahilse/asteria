# I07 replication findings

I07 completed all **80 trajectories**, with **74 full-functional results**, 78 final
compilations and 203 code submissions. All twelve security-context/control
comparisons have fewer total failed issue contracts under every assignment of
unresolved observations, as in I06. However, the operational strategy's resource
results weakened with fresh acquisition: the oversized-line fixture has **13
passes, six heap-exhaustion failures and one unmeasured result**, versus I06's
20 passes. There are **no qualified joint functional + all-security passes**.

This replication was declared before reviewing I06 outcomes. Both new operational
acquisitions used ten turns and produced eight items: Generation has seven
recommendations and one unknown; Reuse has three recommendations, two properties,
two change risks and one unknown. Repository snapshots, task text, generator code,
model requests, delivery budget and all comparison prompts match I06. Only the
operational inserts are newly acquired; requirements and boundaries remain I04
inserts. Every code conversation is fresh. The
[replication analysis](replication-analysis.md) verifies these distinctions and
recomputes both qualified summaries from raw evidence before comparing them.

## Every paper-context cell

Each condition has N=5. Issue counts are failed/evaluated out of 50 planned
contracts. The remaining observations are unresolved, not passes. Positive
persistence is reported separately from the ten issue contracts.

| Cell | Control full | Operations full | Requirements full | Boundaries full | Control issues | Operations issues | Requirements issues | Boundaries issues |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| Generation S | 5/5 | 4/5 | 5/5 | 4/5 | 38/48 | 18/46 | 10/49 | 25/47 |
| Generation S+F+B | 5/5 | 5/5 | 4/5 | 5/5 | 38/50 | 19/48 | 11/46 | 28/48 |
| Reuse B | 4/5 | 4/5 | 5/5 | 4/5 | 27/39 | 5/36 | 4/48 | 19/47 |
| Reuse S+B | 5/5 | 5/5 | 5/5 | 5/5 | 32/49 | 8/45 | 0/45 | 19/48 |

Operations reduce the total count by at least 16, 17, 8 and 19 checks across the
five trajectories in these four cells. These conservative missingness bounds are
not confidence intervals. Full-functional tradeoffs remain: operations and
boundaries lose one success against the Generation S control; requirements lose
one against Generation S+F+B but gain one in Reuse B. An overall lower count does
not establish improvement in every category or implementation.

## Resource outcomes across fresh acquisitions

| Cell | I06 operations: oversized pass / fail / unresolved | I07 operations: oversized pass / fail / unresolved | I07 control: oversized pass / fail / unresolved | I07 operations: qualified large-record pass / fail / unresolved |
| --- | --- | --- | --- | --- |
| Generation S | 5 / 0 / 0 | 3 / 2 / 0 | 0 / 5 / 0 | 1 / 0 / 4 |
| Generation S+F+B | 5 / 0 / 0 | 1 / 4 / 0 | 0 / 5 / 0 | 3 / 0 / 2 |
| Reuse B | 5 / 0 / 0 | 4 / 0 / 1 | 0 / 4 / 1 | 0 / 0 / 5 |
| Reuse S+B | 5 / 0 / 0 | 5 / 0 / 0 | 2 / 3 / 0 | 0 / 0 / 5 |

Both rounds show lower observed oversized-line failures in the operational arms,
but the effect size and coverage differ. I07's six failures are actual
`OutOfMemoryError` diagnostics in Generation artifacts. A structured guard
recommendation does not ensure a correct bounded parser in the generated code.
The [diagnostics](failure-diagnostics.json) retain exact messages, source hashes
and candidate guard/reader lines for inspection.

On the two-check resource category, Generation S operations have fewer affected
trajectories even under adverse missingness assumptions. Generation S+F+B ranges
from a 20-point reduction to no difference. Both Reuse operational categories
remain unresolved for all five trajectories because the large-record precondition
is unsupported or compilation failed. Their category bounds allow no benefit or
an increase. The [category table](qualified-categories.md) preserves this limit.

The audit examined all 78 final compiled artifacts and changed 16 legacy
large-record passes to unknown. Reuse S+B requirements illustrates the consequence:
five full-functional artifacts, zero observed failures across 45 measured issue
checks, but all five large-record outcomes unresolved. Thus it has zero joint
passes after qualification. I06's four joint passes have not been reproduced in
this new 80-trajectory round; original reports are retained separately.

## Repair cost and remaining functional failures

| Submission allowance within I07 | Full / 80 | Cumulative code calls | Rejected edits | Failed compilations |
| --- | ---: | ---: | ---: | ---: |
| 1 | 27 | 80 | 22 | 7 |
| 2 | 45 | 133 | 30 | 17 |
| 3 | 58 | 168 | 35 | 20 |
| 4 | 67 | 190 | 38 | 21 |
| 5 | 74 | 203 | 38 | 23 |

Operations achieve 2/20 full on the first submission and 18/20 within five, using
64 calls; controls achieve 11/20 and 19/20, using 38 calls. Sixteen trajectories
across all arms first succeed after submission three, with 35 additional calls.
The earlier slices were collected under an announced five-submission budget and
are not three-submission counterfactual experiments.

Two final artifacts do not compile. Reuse B operations repetition 1 duplicates
the level's `isHighscoreRecorded` and `setHighscoreRecorded` methods. Reuse B control
repetition 4 exposes a highscore accessor as `Object`, while the menu calls its
typed getters. All their unexecuted checks remain missing in the planned
denominators. The other four unsuccessful trajectories compile: Reuse B boundaries
repetition 2 misses later run records; Generation S operations repetition 4 and
Generation S+F+B requirements repetition 4 retain the 54,321-point run but miss
other required runs; Generation S boundaries repetition 5 has empty-list runtime
errors in invoked tests and no reachable live board in autonomous tests.

The [functional diagnostics](functional-diagnostics.csv) preserve all five failed
suite processes for the four compiling artifacts. Full compilation logs remain
in their archived reports. A declared literal screen found no sentinel matches
in any of the 165 delivered source submissions; that limited screen does not
establish absence of test-directed behavior.

## What this replication establishes

There is a repeated descriptive tendency toward fewer total issue contracts in
the tested combinations, alongside functional repair costs, policy-dependent
failures and incomplete resource coverage. The perfect I06 operational resource
result and its joint passing artifacts are not stable across this second
acquisition round. Both results belong in the presentation.

Each method has only two operational context acquisitions across I06/I07. Code
repetitions share inserts, checks within an artifact are correlated, and the
requirements/boundary acquisitions are held fixed. No significance or broad
repository-generalization claim follows from favorable counts. Provider model
identity matches Luna; effective settings remain unattested where reported.
[I08](../i08-evaluator-repeatability/README.md) will test evaluator repeatability
on fixed first-repetition artifacts after these outputs are double-saved.

Review the [qualified table](qualified-analysis.md), [XLSX](qualified-results.xlsx),
[per-test CSV](qualified-per-test.csv), [two-round CSV](replication-conditions.csv),
[effect comparisons](replication-effects.csv),
[quality/security figure](figures-qualified/quality-and-security.pdf) and
[per-check figure](figures-qualified/per-check-security-effects.pdf).
