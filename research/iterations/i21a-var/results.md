# i21a-var: results

Saved 2026-09-15T02:45:34.858312+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 3 | 1 | 3 | 3 | 24/30 | 0 | 7 |
| generation_s__single_shot__static | 3 | 1 | 3 | 3 | 7/30 | 0 | 8 |
| reuse_sb__single_shot__none | 3 | 2 | 3 | 3 | 17/30 | 0 | 4 |
| reuse_sb__single_shot__static | 3 | 1 | 3 | 3 | 3/30 | 0 | 8 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
