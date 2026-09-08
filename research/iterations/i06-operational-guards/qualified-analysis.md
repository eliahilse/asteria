# i06-operational-guards: qualified per-test effects and uncertainty

Pass/fail large-record observations whose necessary two-record format precondition was not demonstrated become unknown. Original reports remain unchanged. Matching two records is a necessary precondition, not proof of correctness for every amplified encoding.

All scheduled trajectories are included. First-submission and within-budget success use the full trajectory denominator; rejected edits and transport failures are retained.

| Condition | N | First full | Within-budget full | Full rate, Wilson 95% | Joint functional + all 11 security | Issue failures / evaluated | Unresolved | Calls |
| --- | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s__none | 5 | 3 | 5 | 100.0% [56.6, 100.0] | 0 | 35/48 | 2 | 8 |
| generation_s__operations | 5 | 0 | 5 | 100.0% [56.6, 100.0] | 0 | 13/49 | 1 | 14 |
| generation_s__requirements | 5 | 2 | 4 | 80.0% [37.6, 96.4] | 0 | 9/47 | 3 | 12 |
| generation_s__boundaries | 5 | 3 | 5 | 100.0% [56.6, 100.0] | 0 | 22/46 | 4 | 7 |
| generation_sfb__none | 5 | 2 | 5 | 100.0% [56.6, 100.0] | 0 | 34/48 | 2 | 9 |
| generation_sfb__operations | 5 | 2 | 5 | 100.0% [56.6, 100.0] | 0 | 9/47 | 3 | 13 |
| generation_sfb__requirements | 5 | 1 | 4 | 80.0% [37.6, 96.4] | 0 | 10/47 | 3 | 17 |
| generation_sfb__boundaries | 5 | 1 | 5 | 100.0% [56.6, 100.0] | 0 | 27/46 | 4 | 10 |
| reuse_b__none | 5 | 1 | 5 | 100.0% [56.6, 100.0] | 0 | 34/48 | 2 | 14 |
| reuse_b__operations | 5 | 0 | 4 | 80.0% [37.6, 96.4] | 2 | 4/48 | 2 | 17 |
| reuse_b__requirements | 5 | 0 | 4 | 80.0% [37.6, 96.4] | 0 | 3/47 | 3 | 14 |
| reuse_b__boundaries | 5 | 1 | 5 | 100.0% [56.6, 100.0] | 0 | 22/46 | 4 | 12 |
| reuse_sb__none | 5 | 4 | 5 | 100.0% [56.6, 100.0] | 0 | 31/47 | 3 | 6 |
| reuse_sb__operations | 5 | 1 | 5 | 100.0% [56.6, 100.0] | 0 | 7/45 | 5 | 17 |
| reuse_sb__requirements | 5 | 2 | 5 | 100.0% [56.6, 100.0] | 2 | 2/49 | 1 | 11 |
| reuse_sb__boundaries | 5 | 1 | 5 | 100.0% [56.6, 100.0] | 0 | 24/47 | 3 | 9 |

## Fresh-control comparisons

Negative failure-count differences favor the security context. The displayed range covers every possible assignment of unresolved checks; it is an identification bound, **not a confidence interval**. Per-check directions require complete measurement in both arms.

| Parent | Security strategy | Full Δ, percentage points | Issue-count Δ / trajectory, bounds | Checks decreased | Equal | Increased | Unresolved |
| --- | --- | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s | operations | +0.0 | [-4.80, -4.20] | 5 | 4 | 0 | 1 |
| generation_s | requirements | -20.0 | [-5.60, -4.60] | 6 | 3 | 0 | 1 |
| generation_s | boundaries | +0.0 | [-3.00, -1.80] | 4 | 5 | 0 | 1 |
| generation_sfb | operations | +0.0 | [-5.40, -4.40] | 6 | 3 | 0 | 1 |
| generation_sfb | requirements | -20.0 | [-5.20, -4.20] | 6 | 3 | 0 | 1 |
| generation_sfb | boundaries | +0.0 | [-1.80, -0.60] | 2 | 7 | 0 | 1 |
| reuse_b | operations | -20.0 | [-6.40, -5.60] | 7 | 2 | 0 | 1 |
| reuse_b | requirements | -20.0 | [-6.60, -5.60] | 7 | 2 | 0 | 1 |
| reuse_b | boundaries | +0.0 | [-2.80, -1.60] | 5 | 4 | 0 | 1 |
| reuse_sb | operations | +0.0 | [-5.40, -3.80] | 5 | 3 | 1 | 1 |
| reuse_sb | requirements | +0.0 | [-6.40, -5.60] | 6 | 3 | 0 | 1 |
| reuse_sb | boundaries | +0.0 | [-2.00, -0.80] | 2 | 7 | 0 | 1 |

## Every issue check

Each cell is failed/evaluated. Denominators smaller than N indicate unresolved measurements.

| Check | generation_s__none | generation_s__operations | generation_s__requirements | generation_s__boundaries | generation_sfb__none | generation_sfb__operations | generation_sfb__requirements | generation_sfb__boundaries | reuse_b__none | reuse_b__operations | reuse_b__requirements | reuse_b__boundaries | reuse_sb__none | reuse_sb__operations | reuse_sb__requirements | reuse_sb__boundaries |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| rejectsNegativeScore | 5/5 | 5/5 | 5/5 | 5/5 | 5/5 | 5/5 | 5/5 | 5/5 | 5/5 | 3/5 | 0/5 | 5/5 | 5/5 | 5/5 | 0/5 | 5/5 |
| rejectsNegativeTime | 5/5 | 0/5 | 0/5 | 3/5 | 5/5 | 0/5 | 0/5 | 4/5 | 5/5 | 0/5 | 0/5 | 4/5 | 5/5 | 0/5 | 0/5 | 4/5 |
| rejectsNullName | 4/5 | 1/5 | 0/5 | 2/5 | 4/5 | 0/5 | 0/5 | 4/5 | 5/5 | 0/5 | 0/5 | 3/5 | 5/5 | 0/5 | 0/5 | 5/5 |
| rejectsBlankName | 4/5 | 2/5 | 0/5 | 2/5 | 4/5 | 0/5 | 1/5 | 4/5 | 5/5 | 0/5 | 1/5 | 4/5 | 5/5 | 0/5 | 0/5 | 5/5 |
| rejectsExcessiveName | 5/5 | 5/5 | 1/5 | 5/5 | 5/5 | 4/5 | 0/5 | 5/5 | 5/5 | 0/5 | 0/5 | 5/5 | 5/5 | 0/5 | 0/5 | 5/5 |
| boundsRetainedEntries | 4/5 | 0/5 | 0/5 | 0/5 | 3/5 | 0/5 | 0/5 | 0/5 | 2/5 | 1/5 | 0/5 | 1/5 | 0/5 | 2/5 | 0/5 | 0/5 |
| malformedStoreDoesNotCrash | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 |
| oversizedPhysicalLine | 5/5 | 0/5 | 3/5 | 5/5 | 5/5 | 0/5 | 4/5 | 5/5 | 4/5 | 0/5 | 2/5 | 0/5 | 4/5 | 0/5 | 2/5 | 0/5 |
| nativeDeserializationCanary | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 |
| largePersistedRecordSet | 3/3 | 0/4 | 0/2 | 0/1 | 3/3 | 0/2 | 0/2 | 0/1 | 3/3 | 0/3 | 0/2 | 0/1 | 2/2 | 0/0 | 0/4 | 0/2 |

## Acquired context

| Method | Strategy | Items | Recommendations | Unknowns | Model turns | Inspected files | Insert characters |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |
| Generation | requirements | 9 | 7 | 1 | 9 | 4 | 7174 |
| Generation | boundaries | 11 | 4 | 3 | 10 | 6 | 9635 |
| Reuse | requirements | 12 | 5 | 2 | 9 | 7 | 9475 |
| Reuse | boundaries | 11 | 3 | 2 | 9 | 6 | 10445 |
| Generation | operations | 9 | 4 | 1 | 10 | 5 | 12037 |
| Reuse | operations | 8 | 5 | 0 | 11 | 51 | 11949 |

## Interpretation limits

Individual rate intervals use the [Wilson method described by NIST](https://www.itl.nist.gov/div898/handbook/prc/section2/prc241.htm). They describe uncertainty conditional on this fixed task and acquired context; they do not establish generalization across repositories or independently acquired contexts.

The ten issue checks include policy contracts, finite robustness/resource fixtures and a deserialization-dispatch canary. They do not enumerate all vulnerabilities. The positive persistence round trip is reported separately.

Code-generation trajectory, including all its submissions. Ten checks on one artifact are not ten independent samples. One context acquisition per method/strategy is shared within the iteration.

The complete per-test rates and intervals are in `qualified-per-test.csv`; all fresh-control check effects are in `qualified-security-effects.csv`. Exact values and context provenance are in `qualified-analysis.json`. Context prompt inserts remain in `contexts/`. No results are selected for omission, and no significance claim is inferred from a favorable count.
