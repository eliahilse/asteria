# i01-actionable-context: results

Saved 2026-09-08T01:44:32.375723+00:00. Collection complete: True.

I01 used a shared JVM home directory. Persisted game state may have affected outcomes; isolated re-evaluation is pending. These are the original, unchanged observations.

N is independent trajectories; each permits up to three model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_sfb__none | 3 | 3 | 3 | 3 | 20/29 | 1 | 3 |
| generation_sfb__requirements | 3 | 1 | 2 | 3 | 12/29 | 1 | 7 |
| generation_sfb__boundaries | 3 | 2 | 2 | 2 | 6/19 | 11 | 5 |
| reuse_b__none | 3 | 2 | 2 | 3 | 14/19 | 11 | 5 |
| reuse_b__requirements | 3 | 1 | 3 | 3 | 18/29 | 1 | 6 |
| reuse_b__boundaries | 3 | 2 | 2 | 3 | 20/29 | 1 | 5 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
