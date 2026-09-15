# i31-v13: results

Saved 2026-09-15T11:59:11.690295+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | 5 | 4 | 5 | 5 | 26/46 | 4 | 6 |
| generation_s__agentic__static | 5 | 2 | 5 | 5 | 3/45 | 5 | 10 |
| generation_s__agentic__static-guard | 5 | 1 | 4 | 5 | 2/46 | 4 | 16 |
| reuse_sb__agentic__none | 5 | 2 | 5 | 5 | 31/48 | 2 | 10 |
| reuse_sb__agentic__static | 5 | 1 | 4 | 5 | 2/47 | 3 | 15 |
| reuse_sb__agentic__static-guard | 5 | 3 | 4 | 5 | 4/46 | 4 | 12 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
