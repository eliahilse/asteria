# i06-operational-guards: results

Saved 2026-09-08T04:32:40.548457+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__none | 5 | 3 | 5 | 5 | 35/48 | 2 | 8 |
| generation_s__operations | 5 | 0 | 5 | 5 | 13/49 | 1 | 14 |
| generation_s__requirements | 5 | 2 | 4 | 5 | 9/48 | 2 | 12 |
| generation_s__boundaries | 5 | 3 | 5 | 5 | 22/46 | 4 | 7 |
| generation_sfb__none | 5 | 2 | 5 | 5 | 34/48 | 2 | 9 |
| generation_sfb__operations | 5 | 2 | 5 | 5 | 9/47 | 3 | 13 |
| generation_sfb__requirements | 5 | 1 | 4 | 5 | 10/48 | 2 | 17 |
| generation_sfb__boundaries | 5 | 1 | 5 | 5 | 27/46 | 4 | 10 |
| reuse_b__none | 5 | 1 | 5 | 5 | 34/49 | 1 | 14 |
| reuse_b__operations | 5 | 0 | 4 | 5 | 4/50 | 0 | 17 |
| reuse_b__requirements | 5 | 0 | 4 | 5 | 3/48 | 2 | 14 |
| reuse_b__boundaries | 5 | 1 | 5 | 5 | 22/50 | 0 | 12 |
| reuse_sb__none | 5 | 4 | 5 | 5 | 31/48 | 2 | 6 |
| reuse_sb__operations | 5 | 1 | 5 | 5 | 7/50 | 0 | 17 |
| reuse_sb__requirements | 5 | 2 | 5 | 5 | 2/50 | 0 | 11 |
| reuse_sb__boundaries | 5 | 1 | 5 | 5 | 24/50 | 0 | 9 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
