# i03-security-perspectives: results

Saved 2026-09-08T02:20:55.550506+00:00. Collection complete: True.

N is independent trajectories; each permits up to three model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_sfb__none | 5 | 4 | 5 | 5 | 35/50 | 0 | 6 |
| generation_sfb__overview | 5 | 2 | 3 | 4 | 22/36 | 14 | 11 |
| generation_sfb__requirements | 5 | 5 | 5 | 5 | 3/46 | 4 | 5 |
| generation_sfb__boundaries | 5 | 2 | 5 | 5 | 10/49 | 1 | 10 |
| reuse_b__none | 5 | 2 | 5 | 5 | 36/49 | 1 | 8 |
| reuse_b__overview | 5 | 4 | 4 | 5 | 35/48 | 2 | 7 |
| reuse_b__requirements | 5 | 1 | 5 | 5 | 10/48 | 2 | 10 |
| reuse_b__boundaries | 5 | 1 | 5 | 5 | 8/49 | 1 | 11 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
