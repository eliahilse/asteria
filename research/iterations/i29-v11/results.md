# i29-v11: results

Saved 2026-09-15T10:53:54.733526+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | 5 | 1 | 5 | 5 | 25/45 | 5 | 13 |
| generation_s__agentic__static | 5 | 2 | 5 | 5 | 5/48 | 2 | 9 |
| generation_s__agentic__static-guard | 5 | 1 | 4 | 5 | 5/47 | 3 | 14 |
| reuse_sb__agentic__none | 5 | 2 | 4 | 4 | 24/38 | 12 | 11 |
| reuse_sb__agentic__static | 5 | 4 | 5 | 5 | 5/50 | 0 | 9 |
| reuse_sb__agentic__static-guard | 5 | 4 | 5 | 5 | 5/50 | 0 | 8 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
