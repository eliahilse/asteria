# i10-agentic-delivery: results

Saved 2026-09-14T22:23:40.766319+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 5 | 3 | 5 | 5 | 33/47 | 3 | 7 |
| generation_s__single_shot__static | 5 | 0 | 2 | 5 | 5/48 | 2 | 23 |
| generation_s__agentic__none | 5 | 0 | 5 | 5 | 32/49 | 1 | 16 |
| generation_s__agentic__static | 5 | 0 | 1 | 5 | 3/48 | 2 | 24 |
| generation_s__agentic__adaptive | 5 | 0 | 3 | 5 | 8/50 | 0 | 21 |
| reuse_sb__single_shot__none | 5 | 4 | 5 | 5 | 35/50 | 0 | 6 |
| reuse_sb__single_shot__static | 5 | 2 | 4 | 5 | 4/50 | 0 | 13 |
| reuse_sb__agentic__none | 5 | 2 | 5 | 5 | 35/50 | 0 | 11 |
| reuse_sb__agentic__static | 5 | 1 | 4 | 5 | 10/47 | 3 | 17 |
| reuse_sb__agentic__adaptive | 5 | 0 | 4 | 4 | 23/40 | 10 | 19 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
