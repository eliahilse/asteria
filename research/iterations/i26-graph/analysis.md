# i26-graph: per-test effects and uncertainty

All scheduled trajectories are included. First-submission and within-budget success use the full trajectory denominator; rejected edits and transport failures are retained.

Unit: trajectory; N = 2 trajectories per condition. Every rate is k/N and a percentage accompanies it only when N ≥ 20 (docs/REPORTING.md). Issue checks use the fixed denominator 10 × N; unresolved checks are counted separately and are never passes. The Wilson 95% interval (in %) is given only for the within-budget full-functional endpoint, beside its k/N.

| Condition | N | First full | Within-budget full | Within-budget full k/N [Wilson 95%, %] | Joint functional + all 11 security | Issue failures / evaluated | Unresolved | Calls |
| --- | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | 2 | 1 | 2 | 2/2 [34.2, 100.0] | 0 | 10/19 | 1 | 3 |
| generation_s__agentic__static | 2 | 0 | 2 | 2/2 [34.2, 100.0] | 0 | 4/19 | 1 | 6 |
| generation_s__agentic__ast | 2 | 1 | 2 | 2/2 [34.2, 100.0] | 0 | 9/18 | 2 | 4 |
| generation_s__agentic__static-ast | 2 | 0 | 2 | 2/2 [34.2, 100.0] | 1 | 0/19 | 1 | 4 |
| generation_s__agentic__static-guard | 2 | 1 | 2 | 2/2 [34.2, 100.0] | 0 | 3/20 | 0 | 6 |
| generation_s__agentic__static-ast-guard | 2 | 0 | 1 | 1/2 [9.5, 90.5] | 0 | 2/19 | 1 | 7 |

## Fresh-control comparisons

Unit: trajectory; each arm has the N stated above. Δ full is the within-budget full-functional count difference, treatment − control (percentage points follow only when N ≥ 20). Negative failure-count differences favor the security context. The displayed range covers every possible assignment of unresolved checks; it is an identification bound, **not a confidence interval**. Per-check directions require complete measurement in both arms.

| Parent | Security strategy | Δ full (treatment − control) | Issue-count Δ / trajectory, bounds | Checks decreased | Equal | Increased | Unresolved |
| --- | --- | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s | agentic+static | 0 (2−2) | [-3.50, -2.50] | 4 | 5 | 0 | 1 |
| generation_s | agentic+ast | 0 (2−2) | [-1.00, +0.50] | 0 | 9 | 0 | 1 |
| generation_s | agentic+static-ast | 0 (2−2) | [-5.50, -4.50] | 5 | 4 | 0 | 1 |
| generation_s | agentic+static-guard | 0 (2−2) | [-4.00, -3.50] | 4 | 5 | 0 | 1 |
| generation_s | agentic+static-ast-guard | −1 (1−2) | [-4.50, -3.50] | 5 | 4 | 0 | 1 |

## Every issue check

Unit: check on one trajectory; N = 2 trajectories per condition. Each cell is failed / evaluated, followed by the unresolved count wherever evaluated < N; unresolved checks are not passes and stay in the fixed denominator N.

| Check | generation_s__agentic__none | generation_s__agentic__static | generation_s__agentic__ast | generation_s__agentic__static-ast | generation_s__agentic__static-guard | generation_s__agentic__static-ast-guard |
| --- | ---: | ---: | ---: | ---: | ---: | ---: |
| rejectsNegativeScore | 2/2 | 2/2 | 2/2 | 0/2 | 2/2 | 0/2 |
| rejectsNegativeTime | 1/2 | 0/2 | 1/2 | 0/2 | 0/2 | 0/2 |
| rejectsNullName | 0/2 | 0/2 | 0/2 | 0/2 | 0/2 | 0/2 |
| rejectsBlankName | 0/2 | 0/2 | 0/2 | 0/2 | 0/2 | 0/2 |
| rejectsExcessiveName | 2/2 | 0/2 | 2/2 | 0/2 | 0/2 | 0/2 |
| boundsRetainedEntries | 2/2 | 1/2 | 2/2 | 0/2 | 1/2 | 1/2 |
| malformedStoreDoesNotCrash | 0/2 | 0/2 | 0/2 | 0/2 | 0/2 | 0/2 |
| oversizedPhysicalLine | 2/2 | 1/2 | 2/2 | 0/2 | 0/2 | 1/2 |
| nativeDeserializationCanary | 0/2 | 0/2 | 0/2 | 0/2 | 0/2 | 0/2 |
| largePersistedRecordSet | 1/1 · 1 unresolved | 0/1 · 1 unresolved | 0/0 · 2 unresolved | 0/1 · 1 unresolved | 0/2 | 0/1 · 1 unresolved |

## Acquired context

| Method | Strategy | Items | Recommendations | Unknowns | Model turns | Inspected files | Insert characters |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |

## Interpretation limits

Individual rate intervals use the [Wilson method described by NIST](https://www.itl.nist.gov/div898/handbook/prc/section2/prc241.htm). They describe uncertainty conditional on this fixed task and acquired context; they do not establish generalization across repositories or independently acquired contexts.

The ten issue checks include policy contracts, finite robustness/resource fixtures and a deserialization-dispatch canary. They do not enumerate all vulnerabilities. The positive persistence round trip is reported separately.

Code-generation trajectory, including all its submissions. Ten checks on one artifact are not ten independent samples. One context acquisition per method/strategy is shared within the iteration.

The complete per-test rates and intervals are in `per-test.csv`; all fresh-control check effects are in `security-effects.csv`. Exact values and context provenance are in `analysis.json`. Context prompt inserts remain in `contexts/`. No results are selected for omission, and no significance claim is inferred from a favorable count.
