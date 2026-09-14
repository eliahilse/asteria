# i10-agentic-delivery: qualified per-test effects and uncertainty

Pass/fail large-record observations whose necessary two-record format precondition was not demonstrated become unknown. Original reports remain unchanged. Matching two records is a necessary precondition, not proof of correctness for every amplified encoding.

All scheduled trajectories are included. First-submission and within-budget success use the full trajectory denominator; rejected edits and transport failures are retained.

Unit: trajectory; N = 5 trajectories per condition. Every rate is k/N and a percentage accompanies it only when N ≥ 20 (docs/REPORTING.md). Issue checks use the fixed denominator 10 × N; unresolved checks are counted separately and are never passes. The Wilson 95% interval (in %) is given only for the within-budget full-functional endpoint, beside its k/N.

| Condition | N | First full | Within-budget full | Within-budget full k/N [Wilson 95%, %] | Joint functional + all 11 security | Issue failures / evaluated | Unresolved | Calls |
| --- | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 5 | 3 | 5 | 5/5 [56.6, 100.0] | 0 | 33/46 | 4 | 7 |
| generation_s__single_shot__static | 5 | 0 | 2 | 2/5 [11.8, 76.9] | 0 | 5/45 | 5 | 23 |
| generation_s__agentic__none | 5 | 0 | 5 | 5/5 [56.6, 100.0] | 0 | 32/49 | 1 | 16 |
| generation_s__agentic__static | 5 | 0 | 1 | 1/5 [3.6, 62.4] | 0 | 3/45 | 5 | 24 |
| generation_s__agentic__adaptive | 5 | 0 | 3 | 3/5 [23.1, 88.2] | 0 | 8/46 | 4 | 21 |
| reuse_sb__single_shot__none | 5 | 4 | 5 | 5/5 [56.6, 100.0] | 0 | 35/50 | 0 | 6 |
| reuse_sb__single_shot__static | 5 | 2 | 4 | 4/5 [37.6, 96.4] | 0 | 4/46 | 4 | 13 |
| reuse_sb__agentic__none | 5 | 2 | 5 | 5/5 [56.6, 100.0] | 0 | 35/49 | 1 | 11 |
| reuse_sb__agentic__static | 5 | 1 | 4 | 4/5 [37.6, 96.4] | 0 | 10/46 | 4 | 17 |
| reuse_sb__agentic__adaptive | 5 | 0 | 4 | 4/5 [37.6, 96.4] | 0 | 23/39 | 11 | 19 |

## Fresh-control comparisons

Unit: trajectory; each arm has the N stated above. Δ full is the within-budget full-functional count difference, treatment − control (percentage points follow only when N ≥ 20). Negative failure-count differences favor the security context. The displayed range covers every possible assignment of unresolved checks; it is an identification bound, **not a confidence interval**. Per-check directions require complete measurement in both arms.

| Parent | Security strategy | Δ full (treatment − control) | Issue-count Δ / trajectory, bounds | Checks decreased | Equal | Increased | Unresolved |
| --- | --- | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s | single_shot+static | −3 (2−5) | [-6.40, -4.60] | 7 | 2 | 0 | 1 |
| generation_s | agentic+none | 0 (5−5) | [-1.00, +0.00] | 4 | 4 | 1 | 1 |
| generation_s | agentic+static | −4 (1−5) | [-6.80, -5.00] | 7 | 2 | 0 | 1 |
| generation_s | agentic+adaptive | −2 (3−5) | [-5.80, -4.20] | 7 | 2 | 0 | 1 |
| reuse_sb | single_shot+static | −1 (4−5) | [-6.20, -5.40] | 6 | 2 | 1 | 1 |
| reuse_sb | agentic+none | 0 (5−5) | [+0.00, +0.20] | 1 | 7 | 1 | 1 |
| reuse_sb | agentic+static | −1 (4−5) | [-5.00, -4.20] | 5 | 3 | 1 | 1 |
| reuse_sb | agentic+adaptive | −1 (4−5) | [-2.40, -0.20] | 0 | 0 | 0 | 10 |

## Every issue check

Unit: check on one trajectory; N = 5 trajectories per condition. Each cell is failed / evaluated, followed by the unresolved count wherever evaluated < N; unresolved checks are not passes and stay in the fixed denominator N.

| Check | generation_s__single_shot__none | generation_s__single_shot__static | generation_s__agentic__none | generation_s__agentic__static | generation_s__agentic__adaptive | reuse_sb__single_shot__none | reuse_sb__single_shot__static | reuse_sb__agentic__none | reuse_sb__agentic__static | reuse_sb__agentic__adaptive |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| rejectsNegativeScore | 5/5 | 0/5 | 5/5 | 0/5 | 0/5 | 5/5 | 0/5 | 5/5 | 0/5 | 3/4 · 1 unresolved |
| rejectsNegativeTime | 5/5 | 0/5 | 4/5 | 0/5 | 0/5 | 5/5 | 0/5 | 5/5 | 0/5 | 3/4 · 1 unresolved |
| rejectsNullName | 5/5 | 0/5 | 4/5 | 0/5 | 0/5 | 5/5 | 0/5 | 5/5 | 0/5 | 3/4 · 1 unresolved |
| rejectsBlankName | 5/5 | 4/5 | 4/5 | 3/5 | 4/5 | 5/5 | 0/5 | 5/5 | 0/5 | 3/4 · 1 unresolved |
| rejectsExcessiveName | 5/5 | 0/5 | 5/5 | 0/5 | 1/5 | 5/5 | 0/5 | 5/5 | 0/5 | 3/4 · 1 unresolved |
| boundsRetainedEntries | 3/5 | 1/5 | 1/5 | 0/5 | 1/5 | 0/5 | 4/5 | 2/5 | 5/5 | 1/4 · 1 unresolved |
| malformedStoreDoesNotCrash | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/4 · 1 unresolved |
| oversizedPhysicalLine | 4/5 | 0/5 | 5/5 | 0/5 | 1/5 | 5/5 | 0/5 | 4/5 | 5/5 | 4/4 · 1 unresolved |
| nativeDeserializationCanary | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/4 · 1 unresolved |
| largePersistedRecordSet | 1/1 · 4 unresolved | 0/0 · 5 unresolved | 4/4 · 1 unresolved | 0/0 · 5 unresolved | 1/1 · 4 unresolved | 5/5 | 0/1 · 4 unresolved | 4/4 · 1 unresolved | 0/1 · 4 unresolved | 3/3 · 2 unresolved |

## Acquired context

| Method | Strategy | Items | Recommendations | Unknowns | Model turns | Inspected files | Insert characters |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |

## Interpretation limits

Individual rate intervals use the [Wilson method described by NIST](https://www.itl.nist.gov/div898/handbook/prc/section2/prc241.htm). They describe uncertainty conditional on this fixed task and acquired context; they do not establish generalization across repositories or independently acquired contexts.

The ten issue checks include policy contracts, finite robustness/resource fixtures and a deserialization-dispatch canary. They do not enumerate all vulnerabilities. The positive persistence round trip is reported separately.

Code-generation trajectory, including all its submissions. Ten checks on one artifact are not ten independent samples. One context acquisition per method/strategy is shared within the iteration.

The complete per-test rates and intervals are in `qualified-per-test.csv`; all fresh-control check effects are in `qualified-security-effects.csv`. Exact values and context provenance are in `qualified-analysis.json`. Context prompt inserts remain in `contexts/`. No results are selected for omission, and no significance claim is inferred from a favorable count.
