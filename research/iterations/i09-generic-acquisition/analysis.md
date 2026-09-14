# i09-generic-acquisition: per-test effects and uncertainty

All scheduled trajectories are included. First-submission and within-budget success use the full trajectory denominator; rejected edits and transport failures are retained.

Unit: trajectory; N = 5 trajectories per condition. Every rate is k/N and a percentage accompanies it only when N ≥ 20 (docs/REPORTING.md). Issue checks use the fixed denominator 10 × N; unresolved checks are counted separately and are never passes. The Wilson 95% interval (in %) is given only for the within-budget full-functional endpoint, beside its k/N.

| Condition | N | First full | Within-budget full | Within-budget full k/N [Wilson 95%, %] | Joint functional + all 11 security | Issue failures / evaluated | Unresolved | Calls |
| --- | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s__none | 5 | 2 | 5 | 5/5 [56.6, 100.0] | 0 | 38/50 | 0 | 10 |
| generation_s__requirements | 5 | 1 | 5 | 5/5 [56.6, 100.0] | 0 | 12/50 | 0 | 13 |
| generation_s__task_only | 5 | 2 | 5 | 5/5 [56.6, 100.0] | 0 | 15/47 | 3 | 9 |
| generation_s__catalog | 5 | 4 | 5 | 5/5 [56.6, 100.0] | 0 | 25/47 | 3 | 6 |
| reuse_sb__none | 5 | 2 | 5 | 5/5 [56.6, 100.0] | 0 | 31/50 | 0 | 10 |
| reuse_sb__requirements | 5 | 1 | 5 | 5/5 [56.6, 100.0] | 4 | 1/50 | 0 | 11 |
| reuse_sb__task_only | 5 | 1 | 5 | 5/5 [56.6, 100.0] | 0 | 16/49 | 1 | 12 |
| reuse_sb__catalog | 5 | 3 | 5 | 5/5 [56.6, 100.0] | 0 | 13/48 | 2 | 9 |

## Fresh-control comparisons

Unit: trajectory; each arm has the N stated above. Δ full is the within-budget full-functional count difference, treatment − control (percentage points follow only when N ≥ 20). Negative failure-count differences favor the security context. The displayed range covers every possible assignment of unresolved checks; it is an identification bound, **not a confidence interval**. Per-check directions require complete measurement in both arms.

| Parent | Security strategy | Δ full (treatment − control) | Issue-count Δ / trajectory, bounds | Checks decreased | Equal | Increased | Unresolved |
| --- | --- | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s | requirements | 0 (5−5) | [-5.20, -5.20] | 7 | 3 | 0 | 0 |
| generation_s | task_only | 0 (5−5) | [-4.60, -4.00] | 5 | 4 | 0 | 1 |
| generation_s | catalog | 0 (5−5) | [-2.60, -2.00] | 2 | 7 | 0 | 1 |
| reuse_sb | requirements | 0 (5−5) | [-6.00, -6.00] | 7 | 3 | 0 | 0 |
| reuse_sb | task_only | 0 (5−5) | [-3.00, -2.80] | 5 | 3 | 1 | 1 |
| reuse_sb | catalog | 0 (5−5) | [-3.60, -3.20] | 5 | 3 | 1 | 1 |

## Every issue check

Unit: check on one trajectory; N = 5 trajectories per condition. Each cell is failed / evaluated, followed by the unresolved count wherever evaluated < N; unresolved checks are not passes and stay in the fixed denominator N.

| Check | generation_s__none | generation_s__requirements | generation_s__task_only | generation_s__catalog | reuse_sb__none | reuse_sb__requirements | reuse_sb__task_only | reuse_sb__catalog |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| rejectsNegativeScore | 5/5 | 5/5 | 5/5 | 5/5 | 5/5 | 0/5 | 4/5 | 2/5 |
| rejectsNegativeTime | 5/5 | 0/5 | 1/5 | 5/5 | 5/5 | 0/5 | 1/5 | 1/5 |
| rejectsNullName | 5/5 | 0/5 | 1/5 | 5/5 | 5/5 | 0/5 | 2/5 | 1/5 |
| rejectsBlankName | 5/5 | 0/5 | 1/5 | 5/5 | 5/5 | 0/5 | 2/5 | 1/5 |
| rejectsExcessiveName | 5/5 | 4/5 | 2/5 | 5/5 | 5/5 | 0/5 | 2/5 | 3/5 |
| boundsRetainedEntries | 3/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 |
| malformedStoreDoesNotCrash | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 |
| oversizedPhysicalLine | 5/5 | 3/5 | 5/5 | 0/5 | 3/5 | 1/5 | 5/5 | 5/5 |
| nativeDeserializationCanary | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 |
| largePersistedRecordSet | 5/5 | 0/5 | 0/2 · 3 unresolved | 0/2 · 3 unresolved | 3/5 | 0/5 | 0/4 · 1 unresolved | 0/3 · 2 unresolved |

## Acquired context

| Method | Strategy | Items | Recommendations | Unknowns | Model turns | Inspected files | Insert characters |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |
| Generation | requirements | 9 | 7 | 1 | 9 | 4 | 7174 |
| Reuse | requirements | 12 | 5 | 2 | 9 | 7 | 9475 |
| Generation | task_only | 10 | 3 | 0 | 13 | 24 | 9487 |
| Generation | catalog | 7 | 7 | 0 | 6 | 0 | 5871 |
| Reuse | task_only | 7 | 3 | 0 | 7 | 7 | 7812 |
| Reuse | catalog | 7 | 1 | 0 | 9 | 22 | 8801 |

## Interpretation limits

Individual rate intervals use the [Wilson method described by NIST](https://www.itl.nist.gov/div898/handbook/prc/section2/prc241.htm). They describe uncertainty conditional on this fixed task and acquired context; they do not establish generalization across repositories or independently acquired contexts.

The ten issue checks include policy contracts, finite robustness/resource fixtures and a deserialization-dispatch canary. They do not enumerate all vulnerabilities. The positive persistence round trip is reported separately.

Code-generation trajectory, including all its submissions. Ten checks on one artifact are not ten independent samples. One context acquisition per method/strategy is shared within the iteration.

The complete per-test rates and intervals are in `per-test.csv`; all fresh-control check effects are in `security-effects.csv`. Exact values and context provenance are in `analysis.json`. Context prompt inserts remain in `contexts/`. No results are selected for omission, and no significance claim is inferred from a favorable count.
