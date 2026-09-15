# i29-v11: qualified per-test effects and uncertainty

Pass/fail large-record observations whose necessary two-record format precondition was not demonstrated become unknown. Original reports remain unchanged. Matching two records is a necessary precondition, not proof of correctness for every amplified encoding.

All scheduled trajectories are included. First-submission and within-budget success use the full trajectory denominator; rejected edits and transport failures are retained.

Unit: trajectory; N = 5 trajectories per condition. Every rate is k/N and a percentage accompanies it only when N ≥ 20 (docs/REPORTING.md). Issue checks use the fixed denominator 10 × N; unresolved checks are counted separately and are never passes. The Wilson 95% interval (in %) is given only for the within-budget full-functional endpoint, beside its k/N.

| Condition | N | First full | Within-budget full | Within-budget full k/N [Wilson 95%, %] | Joint functional + all 11 security | Issue failures / evaluated | Unresolved | Calls |
| --- | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | 5 | 1 | 5 | 5/5 [56.6, 100.0] | 0 | 25/45 | 5 | 13 |
| generation_s__agentic__static | 5 | 2 | 5 | 5/5 [56.6, 100.0] | 0 | 5/45 | 5 | 9 |
| generation_s__agentic__static-guard | 5 | 1 | 4 | 4/5 [37.6, 96.4] | 0 | 5/46 | 4 | 14 |
| reuse_sb__agentic__none | 5 | 2 | 4 | 4/5 [37.6, 96.4] | 0 | 24/37 | 13 | 11 |
| reuse_sb__agentic__static | 5 | 4 | 5 | 5/5 [56.6, 100.0] | 0 | 5/50 | 0 | 9 |
| reuse_sb__agentic__static-guard | 5 | 4 | 5 | 5/5 [56.6, 100.0] | 0 | 5/50 | 0 | 8 |

## Fresh-control comparisons

Unit: trajectory; each arm has the N stated above. Δ full is the within-budget full-functional count difference, treatment − control (percentage points follow only when N ≥ 20). Negative failure-count differences favor the security context. The displayed range covers every possible assignment of unresolved checks; it is an identification bound, **not a confidence interval**. Per-check directions require complete measurement in both arms.

| Parent | Security strategy | Δ full (treatment − control) | Issue-count Δ / trajectory, bounds | Checks decreased | Equal | Increased | Unresolved |
| --- | --- | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s | agentic+static | 0 (5−5) | [-5.00, -3.00] | 6 | 2 | 1 | 1 |
| generation_s | agentic+static-guard | −1 (4−5) | [-5.00, -3.20] | 6 | 2 | 1 | 1 |
| reuse_sb | agentic+static | +1 (5−4) | [-6.40, -3.80] | 0 | 0 | 0 | 10 |
| reuse_sb | agentic+static-guard | +1 (5−4) | [-6.40, -3.80] | 0 | 0 | 0 | 10 |

## Every issue check

Unit: check on one trajectory; N = 5 trajectories per condition. Each cell is failed / evaluated, followed by the unresolved count wherever evaluated < N; unresolved checks are not passes and stay in the fixed denominator N.

| Check | generation_s__agentic__none | generation_s__agentic__static | generation_s__agentic__static-guard | reuse_sb__agentic__none | reuse_sb__agentic__static | reuse_sb__agentic__static-guard |
| --- | ---: | ---: | ---: | ---: | ---: | ---: |
| rejectsNegativeScore | 5/5 | 0/5 | 0/5 | 4/4 · 1 unresolved | 0/5 | 0/5 |
| rejectsNegativeTime | 5/5 | 0/5 | 0/5 | 4/4 · 1 unresolved | 0/5 | 0/5 |
| rejectsNullName | 1/5 | 0/5 | 0/5 | 4/4 · 1 unresolved | 0/5 | 0/5 |
| rejectsBlankName | 1/5 | 5/5 | 5/5 | 4/4 · 1 unresolved | 5/5 | 5/5 |
| rejectsExcessiveName | 5/5 | 0/5 | 0/5 | 4/4 · 1 unresolved | 0/5 | 0/5 |
| boundsRetainedEntries | 3/5 | 0/5 | 0/5 | 2/4 · 1 unresolved | 0/5 | 0/5 |
| malformedStoreDoesNotCrash | 0/5 | 0/5 | 0/5 | 0/4 · 1 unresolved | 0/5 | 0/5 |
| oversizedPhysicalLine | 5/5 | 0/5 | 0/5 | 2/4 · 1 unresolved | 0/5 | 0/5 |
| nativeDeserializationCanary | 0/5 | 0/5 | 0/5 | 0/4 · 1 unresolved | 0/5 | 0/5 |
| largePersistedRecordSet | 0/0 · 5 unresolved | 0/0 · 5 unresolved | 0/1 · 4 unresolved | 0/1 · 4 unresolved | 0/5 | 0/5 |

## Acquired context

| Method | Strategy | Items | Recommendations | Unknowns | Model turns | Inspected files | Insert characters |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |

## Interpretation limits

Individual rate intervals use the [Wilson method described by NIST](https://www.itl.nist.gov/div898/handbook/prc/section2/prc241.htm). They describe uncertainty conditional on this fixed task and acquired context; they do not establish generalization across repositories or independently acquired contexts.

The ten issue checks include policy contracts, finite robustness/resource fixtures and a deserialization-dispatch canary. They do not enumerate all vulnerabilities. The positive persistence round trip is reported separately.

Code-generation trajectory, including all its submissions. Ten checks on one artifact are not ten independent samples. One context acquisition per method/strategy is shared within the iteration.

The complete per-test rates and intervals are in `qualified-per-test.csv`; all fresh-control check effects are in `qualified-security-effects.csv`. Exact values and context provenance are in `qualified-analysis.json`. Context prompt inserts remain in `contexts/`. No results are selected for omission, and no significance claim is inferred from a favorable count.
