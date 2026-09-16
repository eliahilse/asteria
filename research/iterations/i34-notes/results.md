# i34-notes: results

Saved 2026-09-16T13:37:12.412984+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__notes | 2 | 0 | 2 | 2 | 2/18 | 2 | 6 |
| generation_s__agentic__static-notes | 2 | 0 | 2 | 2 | 2/18 | 2 | 9 |
| reuse_sb__agentic__notes | 2 | 1 | 2 | 2 | 15/20 | 0 | 5 |
| reuse_sb__agentic__static-notes | 2 | 2 | 2 | 2 | 2/20 | 0 | 2 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
