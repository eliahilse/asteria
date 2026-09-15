# i26-graph: results

Saved 2026-09-15T09:21:29.623376+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | 2 | 1 | 2 | 2 | 10/19 | 1 | 3 |
| generation_s__agentic__static | 2 | 0 | 2 | 2 | 4/19 | 1 | 6 |
| generation_s__agentic__ast | 2 | 1 | 2 | 2 | 9/18 | 2 | 4 |
| generation_s__agentic__static-ast | 2 | 0 | 2 | 2 | 0/19 | 1 | 4 |
| generation_s__agentic__static-guard | 2 | 1 | 2 | 2 | 3/20 | 0 | 6 |
| generation_s__agentic__static-ast-guard | 2 | 0 | 1 | 2 | 2/19 | 1 | 7 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
