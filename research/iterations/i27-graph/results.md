# i27-graph: results

Saved 2026-09-15T09:37:55.388257+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | 3 | 2 | 3 | 3 | 17/28 | 2 | 4 |
| generation_s__agentic__static | 3 | 2 | 3 | 3 | 4/27 | 3 | 5 |
| generation_s__agentic__static-ast | 3 | 1 | 2 | 2 | 1/19 | 11 | 8 |
| generation_s__agentic__static-ast-guard | 3 | 0 | 2 | 3 | 4/29 | 1 | 8 |
| generation_s__agentic__static-ast-advise | 3 | 0 | 2 | 2 | 2/19 | 11 | 4 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
