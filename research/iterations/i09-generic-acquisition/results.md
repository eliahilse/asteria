# i09-generic-acquisition: results

Saved 2026-09-14T18:54:49.425469+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__none | 5 | 2 | 5 | 5 | 38/50 | 0 | 10 |
| generation_s__requirements | 5 | 1 | 5 | 5 | 12/50 | 0 | 13 |
| generation_s__task_only | 5 | 2 | 5 | 5 | 15/47 | 3 | 9 |
| generation_s__catalog | 5 | 4 | 5 | 5 | 25/47 | 3 | 6 |
| reuse_sb__none | 5 | 2 | 5 | 5 | 31/50 | 0 | 10 |
| reuse_sb__requirements | 5 | 1 | 5 | 5 | 1/50 | 0 | 11 |
| reuse_sb__task_only | 5 | 1 | 5 | 5 | 16/49 | 1 | 12 |
| reuse_sb__catalog | 5 | 3 | 5 | 5 | 13/48 | 2 | 9 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
