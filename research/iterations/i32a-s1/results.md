# i32a-s1: results

Saved 2026-09-15T16:08:13.792656+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 1 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__static | 5 | 2 | 2 | 3 | 4/29 | 21 | 5 |
| reuse_sb__single_shot__static | 5 | 3 | 3 | 4 | 0/36 | 14 | 5 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
