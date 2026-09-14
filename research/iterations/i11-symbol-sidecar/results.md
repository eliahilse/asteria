# i11-symbol-sidecar: results

Saved 2026-09-14T22:37:26.698925+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 3 | 2 | 3 | 3 | 23/29 | 1 | 5 |
| generation_s__single_shot__static | 3 | 0 | 2 | 3 | 3/29 | 1 | 11 |
| generation_s__agentic__adaptive | 3 | 0 | 1 | 3 | 1/30 | 0 | 13 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
