# i21a-var: qualified per-test effects and uncertainty

Pass/fail large-record observations whose necessary two-record format precondition was not demonstrated become unknown. Original reports remain unchanged. Matching two records is a necessary precondition, not proof of correctness for every amplified encoding.

All scheduled trajectories are included. First-submission and within-budget success use the full trajectory denominator; rejected edits and transport failures are retained.

Unit: trajectory; N = 3 trajectories per condition. Every rate is k/N and a percentage accompanies it only when N ≥ 20 (docs/REPORTING.md). Issue checks use the fixed denominator 10 × N; unresolved checks are counted separately and are never passes. The Wilson 95% interval (in %) is given only for the within-budget full-functional endpoint, beside its k/N.

| Condition | N | First full | Within-budget full | Within-budget full k/N [Wilson 95%, %] | Joint functional + all 11 security | Issue failures / evaluated | Unresolved | Calls |
| --- | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 3 | 1 | 3 | 3/3 [43.9, 100.0] | 0 | 24/30 | 0 | 7 |
| generation_s__single_shot__static | 3 | 1 | 3 | 3/3 [43.9, 100.0] | 0 | 6/28 | 2 | 8 |
| reuse_sb__single_shot__none | 3 | 2 | 3 | 3/3 [43.9, 100.0] | 0 | 17/28 | 2 | 4 |
| reuse_sb__single_shot__static | 3 | 1 | 3 | 3/3 [43.9, 100.0] | 0 | 3/27 | 3 | 8 |

## Fresh-control comparisons

Unit: trajectory; each arm has the N stated above. Δ full is the within-budget full-functional count difference, treatment − control (percentage points follow only when N ≥ 20). Negative failure-count differences favor the security context. The displayed range covers every possible assignment of unresolved checks; it is an identification bound, **not a confidence interval**. Per-check directions require complete measurement in both arms.

| Parent | Security strategy | Δ full (treatment − control) | Issue-count Δ / trajectory, bounds | Checks decreased | Equal | Increased | Unresolved |
| --- | --- | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s | single_shot+static | 0 (3−3) | [-6.00, -5.33] | 7 | 2 | 0 | 1 |
| reuse_sb | single_shot+static | 0 (3−3) | [-5.33, -3.67] | 6 | 2 | 1 | 1 |

## Every issue check

Unit: check on one trajectory; N = 3 trajectories per condition. Each cell is failed / evaluated, followed by the unresolved count wherever evaluated < N; unresolved checks are not passes and stay in the fixed denominator N.

| Check | generation_s__single_shot__none | generation_s__single_shot__static | reuse_sb__single_shot__none | reuse_sb__single_shot__static |
| --- | ---: | ---: | ---: | ---: |
| rejectsNegativeScore | 3/3 | 1/3 | 3/3 | 0/3 |
| rejectsNegativeTime | 3/3 | 0/3 | 3/3 | 0/3 |
| rejectsNullName | 3/3 | 0/3 | 3/3 | 0/3 |
| rejectsBlankName | 3/3 | 1/3 | 3/3 | 0/3 |
| rejectsExcessiveName | 3/3 | 0/3 | 3/3 | 0/3 |
| boundsRetainedEntries | 2/3 | 2/3 | 0/3 | 3/3 |
| malformedStoreDoesNotCrash | 1/3 | 0/3 | 0/3 | 0/3 |
| oversizedPhysicalLine | 3/3 | 2/3 | 1/3 | 0/3 |
| nativeDeserializationCanary | 0/3 | 0/3 | 0/3 | 0/3 |
| largePersistedRecordSet | 3/3 | 0/1 · 2 unresolved | 1/1 · 2 unresolved | 0/0 · 3 unresolved |

## Acquired context

| Method | Strategy | Items | Recommendations | Unknowns | Model turns | Inspected files | Insert characters |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |

## Interpretation limits

Individual rate intervals use the [Wilson method described by NIST](https://www.itl.nist.gov/div898/handbook/prc/section2/prc241.htm). They describe uncertainty conditional on this fixed task and acquired context; they do not establish generalization across repositories or independently acquired contexts.

The ten issue checks include policy contracts, finite robustness/resource fixtures and a deserialization-dispatch canary. They do not enumerate all vulnerabilities. The positive persistence round trip is reported separately.

Code-generation trajectory, including all its submissions. Ten checks on one artifact are not ten independent samples. One context acquisition per method/strategy is shared within the iteration.

The complete per-test rates and intervals are in `qualified-per-test.csv`; all fresh-control check effects are in `qualified-security-effects.csv`. Exact values and context provenance are in `qualified-analysis.json`. Context prompt inserts remain in `contexts/`. No results are selected for omission, and no significance claim is inferred from a favorable count.
