# i30-v12: results

Saved 2026-09-15T11:19:43.197991+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | 5 | 2 | 5 | 5 | 21/45 | 5 | 11 |
| generation_s__agentic__static | 5 | 1 | 5 | 5 | 3/49 | 1 | 10 |
| generation_s__agentic__static-guard | 5 | 1 | 5 | 5 | 5/49 | 1 | 12 |
| reuse_sb__agentic__none | 5 | 3 | 5 | 5 | 31/48 | 2 | 7 |
| reuse_sb__agentic__static | 5 | 3 | 5 | 5 | 1/50 | 0 | 8 |
| reuse_sb__agentic__static-guard | 5 | 2 | 5 | 5 | 0/50 | 0 | 8 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
