# i21b-var: results

Saved 2026-09-15T02:50:35.245846+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 3 | 3 | 3 | 3 | 24/30 | 0 | 3 |
| generation_s__single_shot__static | 3 | 2 | 3 | 3 | 2/29 | 1 | 5 |
| reuse_sb__single_shot__none | 3 | 2 | 3 | 3 | 18/29 | 1 | 4 |
| reuse_sb__single_shot__static | 3 | 1 | 3 | 3 | 4/30 | 0 | 5 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
