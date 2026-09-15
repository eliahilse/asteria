# i32a-s1: per-test effects and uncertainty

All scheduled trajectories are included. First-submission and within-budget success use the full trajectory denominator; rejected edits and transport failures are retained.

Unit: trajectory; N = 5 trajectories per condition. Every rate is k/N and a percentage accompanies it only when N ≥ 20 (docs/REPORTING.md). Issue checks use the fixed denominator 10 × N; unresolved checks are counted separately and are never passes. The Wilson 95% interval (in %) is given only for the within-budget full-functional endpoint, beside its k/N.

| Condition | N | First full | Within-budget full | Within-budget full k/N [Wilson 95%, %] | Joint functional + all 11 security | Issue failures / evaluated | Unresolved | Calls |
| --- | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s__single_shot__static | 5 | 2 | 2 | 2/5 [11.8, 76.9] | 0 | 4/29 | 21 | 5 |
| reuse_sb__single_shot__static | 5 | 3 | 3 | 3/5 [23.1, 88.2] | 0 | 0/36 | 14 | 5 |

## Fresh-control comparisons

Unit: trajectory; each arm has the N stated above. Δ full is the within-budget full-functional count difference, treatment − control (percentage points follow only when N ≥ 20). Negative failure-count differences favor the security context. The displayed range covers every possible assignment of unresolved checks; it is an identification bound, **not a confidence interval**. Per-check directions require complete measurement in both arms.

| Parent | Security strategy | Δ full (treatment − control) | Issue-count Δ / trajectory, bounds | Checks decreased | Equal | Increased | Unresolved |
| --- | --- | ---: | --- | ---: | ---: | ---: | ---: |

## Every issue check

Unit: check on one trajectory; N = 5 trajectories per condition. Each cell is failed / evaluated, followed by the unresolved count wherever evaluated < N; unresolved checks are not passes and stay in the fixed denominator N.

| Check | generation_s__single_shot__static | reuse_sb__single_shot__static |
| --- | ---: | ---: |
| rejectsNegativeScore | 0/3 · 2 unresolved | 0/4 · 1 unresolved |
| rejectsNegativeTime | 0/3 · 2 unresolved | 0/4 · 1 unresolved |
| rejectsNullName | 0/3 · 2 unresolved | 0/4 · 1 unresolved |
| rejectsBlankName | 0/3 · 2 unresolved | 0/4 · 1 unresolved |
| rejectsExcessiveName | 0/3 · 2 unresolved | 0/4 · 1 unresolved |
| boundsRetainedEntries | 0/3 · 2 unresolved | 0/4 · 1 unresolved |
| malformedStoreDoesNotCrash | 0/3 · 2 unresolved | 0/4 · 1 unresolved |
| oversizedPhysicalLine | 3/3 · 2 unresolved | 0/4 · 1 unresolved |
| nativeDeserializationCanary | 0/3 · 2 unresolved | 0/4 · 1 unresolved |
| largePersistedRecordSet | 1/2 · 3 unresolved | 0/0 · 5 unresolved |

## Acquired context

| Method | Strategy | Items | Recommendations | Unknowns | Model turns | Inspected files | Insert characters |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |

## Interpretation limits

Individual rate intervals use the [Wilson method described by NIST](https://www.itl.nist.gov/div898/handbook/prc/section2/prc241.htm). They describe uncertainty conditional on this fixed task and acquired context; they do not establish generalization across repositories or independently acquired contexts.

The ten issue checks include policy contracts, finite robustness/resource fixtures and a deserialization-dispatch canary. They do not enumerate all vulnerabilities. The positive persistence round trip is reported separately.

Code-generation trajectory, including all its submissions. Ten checks on one artifact are not ten independent samples. One context acquisition per method/strategy is shared within the iteration.

The complete per-test rates and intervals are in `per-test.csv`; all fresh-control check effects are in `security-effects.csv`. Exact values and context provenance are in `analysis.json`. Context prompt inserts remain in `contexts/`. No results are selected for omission, and no significance claim is inferred from a favorable count.
