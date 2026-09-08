# i07-operational-replication: results

Saved 2026-09-08T05:18:13.660036+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__none | 5 | 1 | 5 | 5 | 38/48 | 2 | 13 |
| generation_s__operations | 5 | 1 | 4 | 5 | 18/46 | 4 | 13 |
| generation_s__requirements | 5 | 1 | 5 | 5 | 10/50 | 0 | 15 |
| generation_s__boundaries | 5 | 4 | 4 | 5 | 25/47 | 3 | 9 |
| generation_sfb__none | 5 | 5 | 5 | 5 | 38/50 | 0 | 5 |
| generation_sfb__operations | 5 | 1 | 5 | 5 | 19/48 | 2 | 15 |
| generation_sfb__requirements | 5 | 1 | 4 | 5 | 11/46 | 4 | 16 |
| generation_sfb__boundaries | 5 | 5 | 5 | 5 | 28/48 | 2 | 5 |
| reuse_b__none | 5 | 1 | 4 | 4 | 27/39 | 11 | 14 |
| reuse_b__operations | 5 | 0 | 4 | 4 | 5/40 | 10 | 18 |
| reuse_b__requirements | 5 | 0 | 5 | 5 | 4/48 | 2 | 18 |
| reuse_b__boundaries | 5 | 1 | 4 | 5 | 19/47 | 3 | 14 |
| reuse_sb__none | 5 | 4 | 5 | 5 | 32/50 | 0 | 6 |
| reuse_sb__operations | 5 | 0 | 5 | 5 | 8/50 | 0 | 18 |
| reuse_sb__requirements | 5 | 0 | 5 | 5 | 0/50 | 0 | 16 |
| reuse_sb__boundaries | 5 | 2 | 5 | 5 | 19/48 | 2 | 8 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
