# i33a-s1: results

Saved 2026-09-15T16:19:57.610424+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__static | 5 | 2 | 5 | 5 | 4/49 | 1 | 12 |
| reuse_sb__agentic__static | 5 | 1 | 5 | 5 | 0/45 | 5 | 12 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
