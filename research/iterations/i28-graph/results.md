# i28-graph: results

Saved 2026-09-15T10:13:45.090703+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | 5 | 2 | 5 | 5 | 29/45 | 5 | 12 |
| generation_s__agentic__static | 5 | 0 | 5 | 5 | 6/46 | 4 | 12 |
| generation_s__agentic__static-ast | 5 | 1 | 5 | 5 | 5/47 | 3 | 14 |
| generation_s__agentic__static-ast-guard | 5 | 1 | 5 | 5 | 5/46 | 4 | 14 |
| generation_s__agentic__static-ast-advise | 5 | 3 | 4 | 5 | 8/46 | 4 | 22 |
| reuse_sb__agentic__none | 5 | 0 | 5 | 5 | 28/49 | 1 | 11 |
| reuse_sb__agentic__static | 5 | 0 | 5 | 5 | 6/50 | 0 | 14 |
| reuse_sb__agentic__static-ast | 5 | 2 | 5 | 5 | 8/48 | 2 | 11 |
| reuse_sb__agentic__static-ast-guard | 5 | 1 | 5 | 5 | 4/50 | 0 | 14 |
| reuse_sb__agentic__static-ast-advise | 5 | 2 | 5 | 5 | 6/50 | 0 | 20 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
