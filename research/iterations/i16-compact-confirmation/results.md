# i16-compact-confirmation: results

Saved 2026-09-14T23:42:43.632875+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 5 | 3 | 5 | 5 | 36/48 | 2 | 8 |
| generation_s__single_shot__static | 5 | 0 | 0 | 0 | 0/0 | 50 | 5 |
| reuse_sb__single_shot__none | 5 | 4 | 5 | 5 | 29/48 | 2 | 7 |
| reuse_sb__single_shot__static | 5 | 0 | 3 | 5 | 2/50 | 0 | 17 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
