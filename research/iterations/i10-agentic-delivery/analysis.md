# i10-agentic-delivery: per-test effects and uncertainty

All scheduled trajectories are included. First-submission and within-budget success use the full trajectory denominator; rejected edits and transport failures are retained.

Unit: trajectory; N = 5 trajectories per condition. Every rate is k/N and a percentage accompanies it only when N ≥ 20 (docs/REPORTING.md). Issue checks use the fixed denominator 10 × N; unresolved checks are counted separately and are never passes. The Wilson 95% interval (in %) is given only for the within-budget full-functional endpoint, beside its k/N.

| Condition | N | First full | Within-budget full | Within-budget full k/N [Wilson 95%, %] | Joint functional + all 11 security | Issue failures / evaluated | Unresolved | Calls |
| --- | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 5 | 3 | 5 | 5/5 [56.6, 100.0] | 0 | 33/47 | 3 | 7 |
| generation_s__single_shot__static | 5 | 0 | 2 | 2/5 [11.8, 76.9] | 0 | 5/48 | 2 | 23 |
| generation_s__agentic__none | 5 | 0 | 5 | 5/5 [56.6, 100.0] | 0 | 32/49 | 1 | 16 |
| generation_s__agentic__static | 5 | 0 | 1 | 1/5 [3.6, 62.4] | 1 | 3/48 | 2 | 24 |
| generation_s__agentic__adaptive | 5 | 0 | 3 | 3/5 [23.1, 88.2] | 0 | 8/50 | 0 | 21 |
| reuse_sb__single_shot__none | 5 | 4 | 5 | 5/5 [56.6, 100.0] | 0 | 35/50 | 0 | 6 |
| reuse_sb__single_shot__static | 5 | 2 | 4 | 4/5 [37.6, 96.4] | 1 | 4/50 | 0 | 13 |
| reuse_sb__agentic__none | 5 | 2 | 5 | 5/5 [56.6, 100.0] | 0 | 35/50 | 0 | 11 |
| reuse_sb__agentic__static | 5 | 1 | 4 | 4/5 [37.6, 96.4] | 0 | 10/47 | 3 | 17 |
| reuse_sb__agentic__adaptive | 5 | 0 | 4 | 4/5 [37.6, 96.4] | 0 | 23/40 | 10 | 19 |

## Fresh-control comparisons

Unit: trajectory; each arm has the N stated above. Δ full is the within-budget full-functional count difference, treatment − control (percentage points follow only when N ≥ 20). Negative failure-count differences favor the security context. The displayed range covers every possible assignment of unresolved checks; it is an identification bound, **not a confidence interval**. Per-check directions require complete measurement in both arms.

| Parent | Security strategy | Δ full (treatment − control) | Issue-count Δ / trajectory, bounds | Checks decreased | Equal | Increased | Unresolved |
| --- | --- | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s | single_shot+static | −3 (2−5) | [-6.20, -5.20] | 7 | 2 | 0 | 1 |
| generation_s | agentic+none | 0 (5−5) | [-0.80, +0.00] | 4 | 4 | 1 | 1 |
| generation_s | agentic+static | −4 (1−5) | [-6.60, -5.60] | 7 | 2 | 0 | 1 |
| generation_s | agentic+adaptive | −2 (3−5) | [-5.60, -5.00] | 7 | 2 | 0 | 1 |
| reuse_sb | single_shot+static | −1 (4−5) | [-6.20, -6.20] | 7 | 2 | 1 | 0 |
| reuse_sb | agentic+none | 0 (5−5) | [+0.00, +0.00] | 2 | 7 | 1 | 0 |
| reuse_sb | agentic+static | −1 (4−5) | [-5.00, -4.40] | 5 | 3 | 1 | 1 |
| reuse_sb | agentic+adaptive | −1 (4−5) | [-2.40, -0.40] | 0 | 0 | 0 | 10 |

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
| largePersistedRecordSet | 1/2 · 3 unresolved | 0/3 · 2 unresolved | 4/4 · 1 unresolved | 0/3 · 2 unresolved | 1/5 | 5/5 | 0/5 | 4/5 | 0/2 · 3 unresolved | 3/4 · 1 unresolved |

## Acquired context

| Method | Strategy | Items | Recommendations | Unknowns | Model turns | Inspected files | Insert characters |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |

## Interpretation limits

Individual rate intervals use the [Wilson method described by NIST](https://www.itl.nist.gov/div898/handbook/prc/section2/prc241.htm). They describe uncertainty conditional on this fixed task and acquired context; they do not establish generalization across repositories or independently acquired contexts.

The ten issue checks include policy contracts, finite robustness/resource fixtures and a deserialization-dispatch canary. They do not enumerate all vulnerabilities. The positive persistence round trip is reported separately.

Code-generation trajectory, including all its submissions. Ten checks on one artifact are not ten independent samples. One context acquisition per method/strategy is shared within the iteration.

The complete per-test rates and intervals are in `per-test.csv`; all fresh-control check effects are in `security-effects.csv`. Exact values and context provenance are in `analysis.json`. Context prompt inserts remain in `contexts/`. No results are selected for omission, and no significance claim is inferred from a favorable count.
