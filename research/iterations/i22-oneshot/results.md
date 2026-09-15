# i22-oneshot: results

Saved 2026-09-15T05:26:22.177733+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 1 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 5 | 3 | 3 | 5 | 37/50 | 0 | 5 |
| generation_s__single_shot__static | 5 | 0 | 0 | 2 | 3/18 | 32 | 5 |
| reuse_sb__single_shot__none | 5 | 1 | 1 | 1 | 6/9 | 41 | 5 |
| reuse_sb__single_shot__static | 5 | 1 | 1 | 2 | 3/20 | 30 | 5 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
