# I06 findings for review

Operational context improved the oversized-line fixture in all four paper-context
cells: **20/20 passes versus 2/20 fresh controls**. It reached **19/20 full-functional
trajectories versus 20/20 controls**, with a failure in Reuse B. This is a useful
resource-specific result, with remaining policy failures and incomplete coverage
of the separate large-record fixture. The full experiment reached **76/80 full
functional**, using 190 code submissions; all 80 final artifacts compiled.

The new representation records the operation, untrusted input, invariant,
enforcement point and failure behavior for recommendations and change risks.
Two new Luna conversations acquired it from the repository and task, without
test or earlier-result access. They produced nine Generation items in ten turns
and eight Reuse items in eleven turns. Requirements and boundary inserts remain
the exact I04 inserts. This comparison changes acquisition instructions, schema,
content and insert length together; it cannot isolate the effect of individual
fields. The [exact inserts](contexts/) and [frozen plan](plan.json) are retained.

## Qualified per-cell outcomes

Each condition has five fresh trajectories and up to five code submissions.
Issue counts are failed/evaluated checks out of 50 planned; missing observations
remain unresolved. These are ten fixture and policy contracts, not counts of
unique vulnerabilities.

| Paper-context cell | Control full | Operations full | Requirements full | Boundaries full | Control issues | Operations issues | Requirements issues | Boundaries issues |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| Generation S | 5/5 | 5/5 | 4/5 | 5/5 | 35/48 | 13/49 | 9/47 | 22/46 |
| Generation S+F+B | 5/5 | 5/5 | 4/5 | 5/5 | 34/48 | 9/47 | 10/47 | 27/46 |
| Reuse B | 5/5 | 4/5 | 4/5 | 5/5 | 34/48 | 4/48 | 3/47 | 22/46 |
| Reuse S+B | 5/5 | 5/5 | 5/5 | 5/5 | 31/47 | 7/45 | 2/49 | 24/47 |

All twelve security-context/control comparisons have lower total failure counts
even under the most adverse assignment of unresolved checks. For operations the
minimum reductions are 21, 22, 28 and 19 checks across the five trajectories in
the four cells. This establishes the direction of these observed total-count
comparisons despite missingness; it is not a confidence interval or evidence of
uniform improvement in every issue category.

## Resource improvement and remaining regressions

| Cell | Oversized-line passes, control | Operations | Requirements | Boundaries | Qualified large-record passes, operations | Large-record unresolved, operations |
| --- | ---: | ---: | ---: | ---: | ---: | ---: |
| Generation S | 0/5 | 5/5 | 2/5 | 0/5 | 4/5 | 1/5 |
| Generation S+F+B | 0/5 | 5/5 | 1/5 | 0/5 | 2/5 | 3/5 |
| Reuse B | 1/5 | 5/5 | 3/5 | 5/5 | 3/5 | 2/5 |
| Reuse S+B | 1/5 | 5/5 | 3/5 | 5/5 | 0/5 | 5/5 |

The format audit changed 20 unsupported legacy large-record passes to unknown
across the complete experiment. In Reuse S+B operations, all five large-record
results are unresolved. Consequently the resource-category affected-rate bounds
there span a decrease or an increase relative to controls, despite all five
oversized-line passes. The other three operational cells have fewer affected
resource trajectories under every assignment of missing category observations.
See the [category analysis](qualified-categories.md) and [audit](amplification-audit.json).

Operations still fail negative-score rejection in all ten Generation artifacts
and all five Reuse S+B artifacts. Excessive-name rejection fails in 5/5 Generation
S and 4/5 Generation S+F+B artifacts. Reuse S+B retention failures **increase from
0/5 controls to 2/5 operations**. These policy outcomes are reported separately
from resource stress; adding their counts must not obscure the regression.
Malformed-store and deserialization-canary checks pass in every arm and therefore
provide no evidence that context improves those outcomes in this round.

Four artifacts pass all 16 functional and all 11 declared security checks after
qualification: Reuse B operations repetitions 1 and 4, and Reuse S+B requirements
repetitions 1 and 5. Passing these finite fixtures does not prove general security.
Other zero-failure artifacts with unresolved checks are not joint passes.

## Functional repair and unsuccessful trajectories

| Submission allowance within I06 | Full functional / 80 | Cumulative code calls | Rejected edits | Failed compilations |
| --- | ---: | ---: | ---: | ---: |
| 1 | 24 | 80 | 22 | 1 |
| 2 | 50 | 136 | 28 | 5 |
| 3 | 63 | 166 | 30 | 6 |
| 4 | 73 | 183 | 32 | 6 |
| 5 | 76 | 190 | 32 | 6 |

Operations reached 3/20 full on the first submission, compared with 10/20 controls,
and used 61 code calls versus 37 controls. Thirteen trajectories across all arms
first succeeded after submission three, using 24 additional calls. These slices
come from conversations told they had five attempts; they are not a controlled
comparison with an announced three-attempt budget.

The four final functional failures are autonomous gameplay assertions, not failed
compilation or a bootstrap exception. Generation S requirements repetition 2
records no completed runs. Reuse B operations repetition 3, Reuse B requirements
repetition 5 and Generation S+F+B requirements repetition 5 retain the later run
but fail the earlier-run and combined-board assertions. The
[functional diagnostics](functional-diagnostics.csv) preserve the exact process
output and report hashes; every failure stays in the denominator.

## Interpretation and replication

N=5 per combination and one operational acquisition per method limit inference.
Code trajectories share inserts; checks within an artifact are correlated.
Marginal Wilson intervals in the [analysis](qualified-analysis.md) are conditional
on this task and context, with no multiplicity-adjusted significance claim. Model
identity matches Luna; effective provider settings remain unattested. A declared
literal screen found no sentinel matches in all 158 delivered source submissions;
this narrow screen does not prove absence of test-directed behavior.

[I07](../i07-operational-replication/README.md) was declared before reviewing these
outcomes and will repeat all conditions with two fresh operational acquisitions.
[I08](../i08-evaluator-repeatability/README.md) will repeat evaluation of fixed
first-repetition artifacts from both rounds without adding model samples.
Neither stage selects artifacts or contexts because they performed well.

Review the [XLSX](qualified-results.xlsx), [per-test CSV](qualified-per-test.csv),
[security-effect CSV](qualified-security-effects.csv),
[submission curves](submission-curves.csv),
[quality/security figure](figures-qualified/quality-and-security.pdf) and
[per-check figure](figures-qualified/per-check-security-effects.pdf).
