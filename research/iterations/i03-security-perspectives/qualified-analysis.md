# i03-security-perspectives: qualified per-test effects and uncertainty

Pass/fail large-record observations whose necessary two-record format precondition was not demonstrated become unknown. Original reports remain unchanged. Matching two records is a necessary precondition, not proof of correctness for every amplified encoding.

All scheduled trajectories are included. First-submission and within-budget success use the full trajectory denominator; rejected edits and transport failures are retained.

| Condition | N | First full | Within-budget full | Full rate, Wilson 95% | Joint functional + all 11 security | Issue failures / evaluated | Unresolved | Calls |
| --- | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: |
| generation_sfb__none | 5 | 4 | 5 | 100.0% [56.6, 100.0] | 0 | 35/50 | 0 | 6 |
| generation_sfb__overview | 5 | 2 | 3 | 60.0% [23.1, 88.2] | 0 | 22/36 | 14 | 11 |
| generation_sfb__requirements | 5 | 5 | 5 | 100.0% [56.6, 100.0] | 0 | 3/46 | 4 | 5 |
| generation_sfb__boundaries | 5 | 2 | 5 | 100.0% [56.6, 100.0] | 0 | 10/48 | 2 | 10 |
| reuse_b__none | 5 | 2 | 5 | 100.0% [56.6, 100.0] | 0 | 36/49 | 1 | 8 |
| reuse_b__overview | 5 | 4 | 4 | 80.0% [37.6, 96.4] | 0 | 35/48 | 2 | 7 |
| reuse_b__requirements | 5 | 1 | 5 | 100.0% [56.6, 100.0] | 0 | 9/46 | 4 | 10 |
| reuse_b__boundaries | 5 | 1 | 5 | 100.0% [56.6, 100.0] | 0 | 8/49 | 1 | 11 |

## Fresh-control comparisons

Negative failure-count differences favor the security context. The displayed range covers every possible assignment of unresolved checks; it is an identification bound, **not a confidence interval**. Per-check directions require complete measurement in both arms.

| Parent | Security strategy | Full Δ, percentage points | Issue-count Δ / trajectory, bounds | Checks decreased | Equal | Increased | Unresolved |
| --- | --- | ---: | --- | ---: | ---: | ---: | ---: |
| generation_sfb | overview | -40.0 | [-2.60, +0.20] | 0 | 0 | 0 | 10 |
| generation_sfb | requirements | +0.0 | [-6.40, -5.60] | 6 | 3 | 0 | 1 |
| generation_sfb | boundaries | +0.0 | [-5.00, -4.60] | 6 | 3 | 0 | 1 |
| reuse_b | overview | -20.0 | [-0.40, +0.20] | 0 | 9 | 0 | 1 |
| reuse_b | requirements | +0.0 | [-5.60, -4.60] | 6 | 2 | 1 | 1 |
| reuse_b | boundaries | +0.0 | [-5.80, -5.40] | 6 | 3 | 0 | 1 |

## Every issue check

Each cell is failed/evaluated. Denominators smaller than N indicate unresolved measurements.

| Check | generation_sfb__none | generation_sfb__overview | generation_sfb__requirements | generation_sfb__boundaries | reuse_b__none | reuse_b__overview | reuse_b__requirements | reuse_b__boundaries |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| rejectsNegativeScore | 5/5 | 4/4 | 0/5 | 4/5 | 5/5 | 5/5 | 0/5 | 2/5 |
| rejectsNegativeTime | 5/5 | 3/4 | 0/5 | 0/5 | 5/5 | 5/5 | 0/5 | 1/5 |
| rejectsNullName | 5/5 | 3/4 | 0/5 | 0/5 | 5/5 | 5/5 | 0/5 | 0/5 |
| rejectsBlankName | 5/5 | 4/4 | 0/5 | 0/5 | 5/5 | 5/5 | 0/5 | 0/5 |
| rejectsExcessiveName | 5/5 | 4/4 | 0/5 | 3/5 | 5/5 | 5/5 | 0/5 | 0/5 |
| boundsRetainedEntries | 0/5 | 0/4 | 0/5 | 0/5 | 2/5 | 2/5 | 4/5 | 0/5 |
| malformedStoreDoesNotCrash | 0/5 | 0/4 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 |
| oversizedPhysicalLine | 5/5 | 4/4 | 3/5 | 3/5 | 5/5 | 5/5 | 4/5 | 5/5 |
| nativeDeserializationCanary | 0/5 | 0/4 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 |
| largePersistedRecordSet | 5/5 | 0/0 | 0/1 | 0/3 | 4/4 | 3/3 | 1/1 | 0/4 |

## Acquired context

| Method | Strategy | Items | Recommendations | Unknowns | Model turns | Inspected files | Insert characters |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |
| Generation | overview | 8 | 1 | 1 | 10 | 24 | 7398 |
| Generation | requirements | 10 | 6 | 2 | 8 | 5 | 8243 |
| Generation | boundaries | 7 | 1 | 1 | 10 | 5 | 6912 |
| Reuse | overview | 10 | 2 | 2 | 11 | 8 | 9487 |
| Reuse | requirements | 10 | 6 | 2 | 10 | 46 | 9301 |
| Reuse | boundaries | 10 | 2 | 1 | 9 | 8 | 8418 |

## Interpretation limits

Individual rate intervals use the [Wilson method described by NIST](https://www.itl.nist.gov/div898/handbook/prc/section2/prc241.htm). They describe uncertainty conditional on this fixed task and acquired context; they do not establish generalization across repositories or independently acquired contexts.

The ten issue checks include policy contracts, finite robustness/resource fixtures and a deserialization-dispatch canary. They do not enumerate all vulnerabilities. The positive persistence round trip is reported separately.

Code-generation trajectory, including all its submissions. Ten checks on one artifact are not ten independent samples. One context acquisition per method/strategy is shared within the iteration.

The complete per-test rates and intervals are in `qualified-per-test.csv`; all fresh-control check effects are in `qualified-security-effects.csv`. Exact values and context provenance are in `qualified-analysis.json`. Context prompt inserts remain in `contexts/`. No results are selected for omission, and no significance claim is inferred from a favorable count.
