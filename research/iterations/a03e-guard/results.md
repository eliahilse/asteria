# a03e-guard: results

Saved 2026-09-16T11:40:33.953961+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__static-guard | 5 | 0 | 1 | 2 | 1/20 | 30 | 24 |
| reuse_sb__agentic__static-guard | 5 | 0 | 3 | 4 | 5/40 | 10 | 24 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
