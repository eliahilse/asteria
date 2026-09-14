# i14-coach-gate: results

Saved 2026-09-14T23:18:26.979121+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | 3 | 2 | 3 | 3 | 19/29 | 1 | 4 |
| generation_s__agentic__coach | 3 | 1 | 3 | 3 | 16/30 | 0 | 7 |
| generation_s__agentic__gate_once | 3 | 0 | 0 | 3 | 3/30 | 0 | 15 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
