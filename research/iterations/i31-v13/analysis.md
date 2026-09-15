# i31-v13: per-test effects and uncertainty

All scheduled trajectories are included. First-submission and within-budget success use the full trajectory denominator; rejected edits and transport failures are retained.

Unit: trajectory; N = 5 trajectories per condition. Every rate is k/N and a percentage accompanies it only when N ≥ 20 (docs/REPORTING.md). Issue checks use the fixed denominator 10 × N; unresolved checks are counted separately and are never passes. The Wilson 95% interval (in %) is given only for the within-budget full-functional endpoint, beside its k/N.

| Condition | N | First full | Within-budget full | Within-budget full k/N [Wilson 95%, %] | Joint functional + all 11 security | Issue failures / evaluated | Unresolved | Calls |
| --- | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | 5 | 4 | 5 | 5/5 [56.6, 100.0] | 0 | 26/46 | 4 | 6 |
| generation_s__agentic__static | 5 | 2 | 5 | 5/5 [56.6, 100.0] | 0 | 3/45 | 5 | 10 |
| generation_s__agentic__static-guard | 5 | 1 | 4 | 4/5 [37.6, 96.4] | 1 | 2/46 | 4 | 16 |
| reuse_sb__agentic__none | 5 | 2 | 5 | 5/5 [56.6, 100.0] | 0 | 31/48 | 2 | 10 |
| reuse_sb__agentic__static | 5 | 1 | 4 | 4/5 [37.6, 96.4] | 0 | 2/47 | 3 | 15 |
| reuse_sb__agentic__static-guard | 5 | 3 | 4 | 4/5 [37.6, 96.4] | 0 | 4/46 | 4 | 12 |

## Fresh-control comparisons

Unit: trajectory; each arm has the N stated above. Δ full is the within-budget full-functional count difference, treatment − control (percentage points follow only when N ≥ 20). Negative failure-count differences favor the security context. The displayed range covers every possible assignment of unresolved checks; it is an identification bound, **not a confidence interval**. Per-check directions require complete measurement in both arms.

| Parent | Security strategy | Δ full (treatment − control) | Issue-count Δ / trajectory, bounds | Checks decreased | Equal | Increased | Unresolved |
| --- | --- | ---: | --- | ---: | ---: | ---: | ---: |
| generation_s | agentic+static | 0 (5−5) | [-5.40, -3.60] | 7 | 2 | 0 | 1 |
| generation_s | agentic+static-guard | −1 (4−5) | [-5.60, -4.00] | 7 | 2 | 0 | 1 |
| reuse_sb | agentic+static | −1 (4−5) | [-6.20, -5.20] | 6 | 3 | 0 | 1 |
| reuse_sb | agentic+static-guard | −1 (4−5) | [-5.80, -4.60] | 6 | 2 | 1 | 1 |

## Every issue check

Unit: check on one trajectory; N = 5 trajectories per condition. Each cell is failed / evaluated, followed by the unresolved count wherever evaluated < N; unresolved checks are not passes and stay in the fixed denominator N.

| Check | generation_s__agentic__none | generation_s__agentic__static | generation_s__agentic__static-guard | reuse_sb__agentic__none | reuse_sb__agentic__static | reuse_sb__agentic__static-guard |
| --- | ---: | ---: | ---: | ---: | ---: | ---: |
| rejectsNegativeScore | 5/5 | 0/5 | 0/5 | 5/5 | 0/5 | 0/5 |
| rejectsNegativeTime | 5/5 | 0/5 | 0/5 | 5/5 | 0/5 | 0/5 |
| rejectsNullName | 2/5 | 0/5 | 0/5 | 5/5 | 0/5 | 0/5 |
| rejectsBlankName | 2/5 | 0/5 | 0/5 | 5/5 | 0/5 | 0/5 |
| rejectsExcessiveName | 5/5 | 0/5 | 0/5 | 5/5 | 0/5 | 0/5 |
| boundsRetainedEntries | 3/5 | 0/5 | 0/5 | 4/5 | 0/5 | 0/5 |
| malformedStoreDoesNotCrash | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 |
| oversizedPhysicalLine | 4/5 | 3/5 | 2/5 | 2/5 | 2/5 | 3/5 |
| nativeDeserializationCanary | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 | 0/5 |
| largePersistedRecordSet | 0/1 · 4 unresolved | 0/0 · 5 unresolved | 0/1 · 4 unresolved | 0/3 · 2 unresolved | 0/2 · 3 unresolved | 1/1 · 4 unresolved |

## Acquired context

| Method | Strategy | Items | Recommendations | Unknowns | Model turns | Inspected files | Insert characters |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |

## Interpretation limits

Individual rate intervals use the [Wilson method described by NIST](https://www.itl.nist.gov/div898/handbook/prc/section2/prc241.htm). They describe uncertainty conditional on this fixed task and acquired context; they do not establish generalization across repositories or independently acquired contexts.

The ten issue checks include policy contracts, finite robustness/resource fixtures and a deserialization-dispatch canary. They do not enumerate all vulnerabilities. The positive persistence round trip is reported separately.

Code-generation trajectory, including all its submissions. Ten checks on one artifact are not ten independent samples. One context acquisition per method/strategy is shared within the iteration.

The complete per-test rates and intervals are in `per-test.csv`; all fresh-control check effects are in `security-effects.csv`. Exact values and context provenance are in `analysis.json`. Context prompt inserts remain in `contexts/`. No results are selected for omission, and no significance claim is inferred from a favorable count.
