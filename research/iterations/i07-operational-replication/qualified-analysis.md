# i07-operational-replication: qualified per-test effects and uncertainty

Pass/fail large-record observations whose necessary two-record format precondition was not demonstrated become unknown. Original reports remain unchanged. Matching two records is a necessary precondition, not proof of correctness for every amplified encoding.

All scheduled trajectories are included. First-submission and within-budget success use the full trajectory denominator; rejected edits and transport failures are retained.

Unit: trajectory; N = 5 trajectories per condition. Every rate is k/N and a percentage accompanies it only when N ≥ 20 (docs/REPORTING.md). Issue checks use the fixed denominator 10 × N; unresolved checks are counted separately and are never passes. The Wilson 95% interval (in %) is given only for the within-budget full-functional endpoint, beside its k/N.

| Condition | N | First full | Within-budget full | Within-budget full k/N [Wilson 95%, %] | Joint functional + all 11 security | Issue failures / evaluated | Unresolved | Calls |
| --- | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s__none | 5 | 1 | 5 | 5/5 [56.6, 100.0] | 0 | 38/48 | 2 | 13 |
| generation_s__operations | 5 | 1 | 4 | 4/5 [37.6, 96.4] | 0 | 18/46 | 4 | 13 |
| generation_s__requirements | 5 | 1 | 5 | 5/5 [56.6, 100.0] | 0 | 10/49 | 1 | 15 |
| generation_s__boundaries | 5 | 4 | 4 | 4/5 [37.6, 96.4] | 0 | 25/47 | 3 | 9 |
| generation_sfb__none | 5 | 5 | 5 | 5/5 [56.6, 100.0] | 0 | 38/50 | 0 | 5 |
| generation_sfb__operations | 5 | 1 | 5 | 5/5 [56.6, 100.0] | 0 | 19/48 | 2 | 15 |
| generation_sfb__requirements | 5 | 1 | 4 | 4/5 [37.6, 96.4] | 0 | 11/46 | 4 | 16 |
| generation_sfb__boundaries | 5 | 5 | 5 | 5/5 [56.6, 100.0] | 0 | 28/48 | 2 | 5 |
| reuse_b__none | 5 | 1 | 4 | 4/5 [37.6, 96.4] | 0 | 27/39 | 11 | 14 |
| reuse_b__operations | 5 | 0 | 4 | 4/5 [37.6, 96.4] | 0 | 5/36 | 14 | 18 |
| reuse_b__requirements | 5 | 0 | 5 | 5/5 [56.6, 100.0] | 0 | 4/48 | 2 | 18 |
| reuse_b__boundaries | 5 | 1 | 4 | 4/5 [37.6, 96.4] | 0 | 19/47 | 3 | 14 |
| reuse_sb__none | 5 | 4 | 5 | 5/5 [56.6, 100.0] | 0 | 32/49 | 1 | 6 |
| reuse_sb__operations | 5 | 0 | 5 | 5/5 [56.6, 100.0] | 0 | 8/45 | 5 | 18 |
| reuse_sb__requirements | 5 | 0 | 5 | 5/5 [56.6, 100.0] | 0 | 0/45 | 5 | 16 |
| reuse_sb__boundaries | 5 | 2 | 5 | 5/5 [56.6, 100.0] | 0 | 19/48 | 2 | 8 |

## Fresh-control comparisons

Unit: trajectory; each arm has the N stated above. Δ full is the within-budget full-functional count difference, treatment − control (percentage points follow only when N ≥ 20). Negative failure-count differences favor the security context. The displayed range covers every possible assignment of unresolved checks; it is an identification bound, **not a confidence interval**. Per-check directions require complete measurement in both arms.

| Parent | Security strategy | Δ full (treatment − control) | Issue-count Δ / trajectory, bounds | Checks decreased | Equal | Increased | Unresolved |
| --- | --- | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s | operations | −1 (4−5) | [-4.40, -3.20] | 6 | 3 | 0 | 1 |
| generation_s | requirements | 0 (5−5) | [-6.00, -5.40] | 6 | 3 | 0 | 1 |
| generation_s | boundaries | −1 (4−5) | [-3.00, -2.00] | 4 | 5 | 0 | 1 |
| generation_sfb | operations | 0 (5−5) | [-3.80, -3.40] | 5 | 4 | 0 | 1 |
| generation_sfb | requirements | −1 (4−5) | [-5.40, -4.60] | 5 | 4 | 0 | 1 |
| generation_sfb | boundaries | 0 (5−5) | [-2.00, -1.60] | 4 | 5 | 0 | 1 |
| reuse_b | operations | 0 (4−4) | [-6.60, -1.60] | 0 | 0 | 0 | 10 |
| reuse_b | requirements | +1 (5−4) | [-6.80, -4.20] | 0 | 0 | 0 | 10 |
| reuse_b | boundaries | 0 (4−4) | [-3.80, -1.00] | 0 | 0 | 0 | 10 |
| reuse_sb | operations | 0 (5−5) | [-5.00, -3.80] | 5 | 3 | 1 | 1 |
| reuse_sb | requirements | 0 (5−5) | [-6.60, -5.40] | 7 | 2 | 0 | 1 |
| reuse_sb | boundaries | 0 (5−5) | [-2.80, -2.20] | 6 | 3 | 0 | 1 |

## Every issue check

Unit: check on one trajectory; N = 5 trajectories per condition. Each cell is failed / evaluated, followed by the unresolved count wherever evaluated < N; unresolved checks are not passes and stay in the fixed denominator N.

| Check | generation_s__none | generation_s__operations | generation_s__requirements | generation_s__boundaries | generation_sfb__none | generation_sfb__operations | generation_sfb__requirements | generation_sfb__boundaries | reuse_b__none | reuse_b__operations | reuse_b__requirements | reuse_b__boundaries | reuse_sb__none | reuse_sb__operations | reuse_sb__requirements | reuse_sb__boundaries |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| rejectsNegativeScore | 5/5 | 4/5 | 5/5 | 5/5 | 5/5 | 5/5 | 5/5 | 5/5 | 4/4 · 1 unresolved | 2/4 · 1 unresolved | 0/5 | 4/5 | 5/5 | 5/5 | 0/5 | 5/5 |
| rejectsNegativeTime | 5/5 | 0/5 | 0/5 | 3/5 | 5/5 | 0/5 | 0/5 | 4/5 | 4/4 · 1 unresolved | 0/4 · 1 unresolved | 0/5 | 3/5 | 5/5 | 0/5 | 0/5 | 4/5 |
| rejectsNullName | 5/5 | 3/5 | 0/5 | 4/5 | 5/5 | 1/5 | 0/5 | 4/5 | 4/4 · 1 unresolved | 0/4 · 1 unresolved | 0/5 | 4/5 | 5/5 | 0/5 | 0/5 | 3/5 |
| rejectsBlankName | 5/5 | 3/5 | 0/5 | 3/5 | 5/5 | 4/5 | 0/5 | 4/5 | 4/4 · 1 unresolved | 0/4 · 1 unresolved | 0/5 | 4/5 | 5/5 | 0/5 | 0/5 | 3/5 |
| rejectsExcessiveName | 5/5 | 5/5 | 3/5 | 5/5 | 5/5 | 5/5 | 1/5 | 5/5 | 4/4 · 1 unresolved | 2/4 · 1 unresolved | 0/5 | 4/5 | 5/5 | 1/5 | 0/5 | 4/5 |
| boundsRetainedEntries | 5/5 | 1/5 | 0/5 | 0/5 | 3/5 | 0/5 | 0/5 | 0/5 | 0/4 · 1 unresolved | 1/4 · 1 unresolved | 0/5 | 0/5 | 1/5 | 2/5 | 0/5 | 0/5 |
| malformedStoreDoesNotCrash | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/4 · 1 unresolved | 0/4 · 1 unresolved | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 |
| oversizedPhysicalLine | 5/5 | 2/5 | 2/5 | 5/5 | 5/5 | 4/5 | 5/5 | 5/5 | 4/4 · 1 unresolved | 0/4 · 1 unresolved | 4/5 | 0/5 | 3/5 | 0/5 | 0/5 | 0/5 |
| nativeDeserializationCanary | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/4 · 1 unresolved | 0/4 · 1 unresolved | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 |
| largePersistedRecordSet | 3/3 · 2 unresolved | 0/1 · 4 unresolved | 0/4 · 1 unresolved | 0/2 · 3 unresolved | 5/5 | 0/3 · 2 unresolved | 0/1 · 4 unresolved | 1/3 · 2 unresolved | 3/3 · 2 unresolved | 0/0 · 5 unresolved | 0/3 · 2 unresolved | 0/2 · 3 unresolved | 3/4 · 1 unresolved | 0/0 · 5 unresolved | 0/0 · 5 unresolved | 0/3 · 2 unresolved |

## Acquired context

| Method | Strategy | Items | Recommendations | Unknowns | Model turns | Inspected files | Insert characters |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |
| Generation | requirements | 9 | 7 | 1 | 9 | 4 | 7174 |
| Generation | boundaries | 11 | 4 | 3 | 10 | 6 | 9635 |
| Reuse | requirements | 12 | 5 | 2 | 9 | 7 | 9475 |
| Reuse | boundaries | 11 | 3 | 2 | 9 | 6 | 10445 |
| Generation | operations | 8 | 7 | 1 | 10 | 6 | 11122 |
| Reuse | operations | 8 | 3 | 1 | 10 | 8 | 10815 |

## Interpretation limits

Individual rate intervals use the [Wilson method described by NIST](https://www.itl.nist.gov/div898/handbook/prc/section2/prc241.htm). They describe uncertainty conditional on this fixed task and acquired context; they do not establish generalization across repositories or independently acquired contexts.

The ten issue checks include policy contracts, finite robustness/resource fixtures and a deserialization-dispatch canary. They do not enumerate all vulnerabilities. The positive persistence round trip is reported separately.

Code-generation trajectory, including all its submissions. Ten checks on one artifact are not ten independent samples. One context acquisition per method/strategy is shared within the iteration.

The complete per-test rates and intervals are in `qualified-per-test.csv`; all fresh-control check effects are in `qualified-security-effects.csv`. Exact values and context provenance are in `qualified-analysis.json`. Context prompt inserts remain in `contexts/`. No results are selected for omission, and no significance claim is inferred from a favorable count.
