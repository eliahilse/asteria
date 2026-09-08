# i04-matrix-replication: per-test effects and uncertainty

All scheduled trajectories are included. First-submission and within-budget success use the full trajectory denominator; rejected edits and transport failures are retained.

| Condition | N | First full | Within-budget full | Full rate, Wilson 95% | Joint functional + all 11 security | Issue failures / evaluated | Unresolved | Calls |
| --- | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s__none | 5 | 3 | 5 | 100.0% [56.6, 100.0] | 0 | 37/50 | 0 | 7 |
| generation_s__overview | 5 | 4 | 4 | 80.0% [37.6, 96.4] | 0 | 35/46 | 4 | 7 |
| generation_s__requirements | 5 | 1 | 4 | 80.0% [37.6, 96.4] | 0 | 8/48 | 2 | 11 |
| generation_s__boundaries | 5 | 4 | 5 | 100.0% [56.6, 100.0] | 0 | 30/49 | 1 | 7 |
| generation_sfb__none | 5 | 4 | 5 | 100.0% [56.6, 100.0] | 0 | 33/50 | 0 | 6 |
| generation_sfb__overview | 5 | 0 | 4 | 80.0% [37.6, 96.4] | 0 | 23/37 | 13 | 12 |
| generation_sfb__requirements | 5 | 1 | 5 | 100.0% [56.6, 100.0] | 0 | 9/50 | 0 | 11 |
| generation_sfb__boundaries | 5 | 2 | 5 | 100.0% [56.6, 100.0] | 0 | 25/49 | 1 | 9 |
| reuse_b__none | 5 | 1 | 4 | 80.0% [37.6, 96.4] | 0 | 32/48 | 2 | 11 |
| reuse_b__overview | 5 | 2 | 5 | 100.0% [56.6, 100.0] | 0 | 36/47 | 3 | 10 |
| reuse_b__requirements | 5 | 1 | 4 | 80.0% [37.6, 96.4] | 1 | 2/48 | 2 | 11 |
| reuse_b__boundaries | 5 | 2 | 4 | 80.0% [37.6, 96.4] | 0 | 23/49 | 1 | 9 |
| reuse_sb__none | 5 | 2 | 4 | 80.0% [37.6, 96.4] | 0 | 32/48 | 2 | 10 |
| reuse_sb__overview | 5 | 4 | 5 | 100.0% [56.6, 100.0] | 0 | 32/49 | 1 | 6 |
| reuse_sb__requirements | 5 | 2 | 2 | 40.0% [11.8, 76.9] | 0 | 3/30 | 20 | 11 |
| reuse_sb__boundaries | 5 | 0 | 4 | 80.0% [37.6, 96.4] | 0 | 16/39 | 11 | 12 |

## Fresh-control comparisons

Negative failure-count differences favor the security context. The displayed range covers every possible assignment of unresolved checks; it is an identification bound, **not a confidence interval**. Per-check directions require complete measurement in both arms.

| Parent | Security strategy | Full Δ, percentage points | Issue-count Δ / trajectory, bounds | Checks decreased | Equal | Increased | Unresolved |
| --- | --- | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s | overview | -20.0 | [-0.40, +0.40] | 0 | 8 | 1 | 1 |
| generation_s | requirements | -20.0 | [-5.80, -5.40] | 6 | 3 | 0 | 1 |
| generation_s | boundaries | +0.0 | [-1.40, -1.20] | 3 | 6 | 0 | 1 |
| generation_sfb | overview | -20.0 | [-2.00, +0.60] | 0 | 0 | 0 | 10 |
| generation_sfb | requirements | +0.0 | [-4.80, -4.80] | 7 | 3 | 0 | 0 |
| generation_sfb | boundaries | +0.0 | [-1.60, -1.40] | 2 | 7 | 0 | 1 |
| reuse_b | overview | +20.0 | [+0.40, +1.40] | 0 | 7 | 2 | 1 |
| reuse_b | requirements | +0.0 | [-6.40, -5.60] | 7 | 2 | 0 | 1 |
| reuse_b | boundaries | +0.0 | [-2.20, -1.60] | 5 | 4 | 0 | 1 |
| reuse_sb | overview | +20.0 | [-0.40, +0.20] | 1 | 7 | 1 | 1 |
| reuse_sb | requirements | -40.0 | [-6.20, -1.80] | 0 | 0 | 0 | 10 |
| reuse_sb | boundaries | +0.0 | [-3.60, -1.00] | 0 | 0 | 0 | 10 |

## Every issue check

Each cell is failed/evaluated. Denominators smaller than N indicate unresolved measurements.

| Check | generation_s__none | generation_s__overview | generation_s__requirements | generation_s__boundaries | generation_sfb__none | generation_sfb__overview | generation_sfb__requirements | generation_sfb__boundaries | reuse_b__none | reuse_b__overview | reuse_b__requirements | reuse_b__boundaries | reuse_sb__none | reuse_sb__overview | reuse_sb__requirements | reuse_sb__boundaries |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| rejectsNegativeScore | 5/5 | 5/5 | 5/5 | 5/5 | 5/5 | 4/4 | 5/5 | 5/5 | 5/5 | 5/5 | 0/5 | 4/5 | 5/5 | 5/5 | 0/3 | 4/4 |
| rejectsNegativeTime | 5/5 | 5/5 | 0/5 | 5/5 | 5/5 | 3/4 | 0/5 | 4/5 | 5/5 | 5/5 | 0/5 | 4/5 | 5/5 | 5/5 | 0/3 | 3/4 |
| rejectsNullName | 5/5 | 5/5 | 0/5 | 4/5 | 3/5 | 3/4 | 0/5 | 3/5 | 5/5 | 5/5 | 0/5 | 4/5 | 5/5 | 5/5 | 0/3 | 3/4 |
| rejectsBlankName | 5/5 | 5/5 | 0/5 | 4/5 | 3/5 | 3/4 | 0/5 | 3/5 | 5/5 | 5/5 | 0/5 | 5/5 | 5/5 | 5/5 | 0/3 | 3/4 |
| rejectsExcessiveName | 5/5 | 5/5 | 0/5 | 5/5 | 5/5 | 4/4 | 1/5 | 5/5 | 5/5 | 5/5 | 0/5 | 5/5 | 5/5 | 5/5 | 0/3 | 3/4 |
| boundsRetainedEntries | 2/5 | 4/5 | 0/5 | 0/5 | 2/5 | 1/4 | 0/5 | 0/5 | 3/5 | 4/5 | 0/5 | 1/5 | 1/5 | 4/5 | 0/3 | 0/4 |
| malformedStoreDoesNotCrash | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/4 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/3 | 0/4 |
| oversizedPhysicalLine | 5/5 | 5/5 | 3/5 | 5/5 | 5/5 | 4/4 | 3/5 | 5/5 | 3/5 | 5/5 | 2/5 | 0/5 | 4/5 | 2/5 | 3/3 | 0/4 |
| nativeDeserializationCanary | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/4 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/3 | 0/4 |
| largePersistedRecordSet | 5/5 | 1/1 | 0/3 | 2/4 | 5/5 | 1/1 | 0/5 | 0/4 | 1/3 | 2/2 | 0/3 | 0/4 | 2/3 | 1/4 | 0/3 | 0/3 |

## Acquired context

| Method | Strategy | Items | Recommendations | Unknowns | Model turns | Inspected files | Insert characters |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |
| Generation | overview | 10 | 2 | 3 | 10 | 5 | 8519 |
| Generation | requirements | 9 | 7 | 1 | 9 | 4 | 7174 |
| Generation | boundaries | 11 | 4 | 3 | 10 | 6 | 9635 |
| Reuse | overview | 10 | 1 | 2 | 10 | 44 | 10342 |
| Reuse | requirements | 12 | 5 | 2 | 9 | 7 | 9475 |
| Reuse | boundaries | 11 | 3 | 2 | 9 | 6 | 10445 |

## Interpretation limits

Individual rate intervals use the [Wilson method described by NIST](https://www.itl.nist.gov/div898/handbook/prc/section2/prc241.htm). They describe uncertainty conditional on this fixed task and acquired context; they do not establish generalization across repositories or independently acquired contexts.

The ten issue checks include policy contracts, finite robustness/resource fixtures and a deserialization-dispatch canary. They do not enumerate all vulnerabilities. The positive persistence round trip is reported separately.

Code-generation trajectory, including all its submissions. Ten checks on one artifact are not ten independent samples. One context acquisition per method/strategy is shared within the iteration.

The complete per-test rates and intervals are in `per-test.csv`; all fresh-control check effects are in `security-effects.csv`. Exact values and context provenance are in `analysis.json`. Context prompt inserts remain in `contexts/`. No results are selected for omission, and no significance claim is inferred from a favorable count.
