# i20-v10: results

Saved 2026-09-15T02:35:44.058933+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 5 | 1 | 5 | 5 | 39/49 | 1 | 10 |
| generation_s__single_shot__static | 5 | 2 | 5 | 5 | 11/45 | 5 | 10 |
| reuse_sb__single_shot__none | 5 | 0 | 5 | 5 | 31/50 | 0 | 13 |
| reuse_sb__single_shot__static | 5 | 1 | 5 | 5 | 7/50 | 0 | 11 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
