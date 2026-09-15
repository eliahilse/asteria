# i18-nofb-reuse: results

Saved 2026-09-15T00:55:40.324315+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| reuse_sb__single_shot__none | 5 | 2 | 5 | 5 | 27/48 | 2 | 11 |
| reuse_sb__single_shot__static | 5 | 1 | 4 | 5 | 4/50 | 0 | 12 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
