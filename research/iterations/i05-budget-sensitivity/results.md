# i05-budget-sensitivity: results

Saved 2026-09-08T03:49:12.996721+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 5 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__none | 5 | 3 | 5 | 5 | 37/47 | 3 | 10 |
| generation_s__overview | 5 | 1 | 4 | 5 | 34/45 | 5 | 14 |
| generation_s__requirements | 5 | 1 | 5 | 5 | 8/49 | 1 | 10 |
| generation_s__boundaries | 5 | 1 | 4 | 5 | 29/48 | 2 | 13 |
| generation_sfb__none | 5 | 2 | 5 | 5 | 39/50 | 0 | 8 |
| generation_sfb__overview | 5 | 2 | 4 | 5 | 36/47 | 3 | 12 |
| generation_sfb__requirements | 5 | 2 | 5 | 5 | 10/50 | 0 | 13 |
| generation_sfb__boundaries | 5 | 4 | 5 | 5 | 30/47 | 3 | 6 |
| reuse_b__none | 5 | 1 | 5 | 5 | 33/49 | 1 | 13 |
| reuse_b__overview | 5 | 2 | 5 | 5 | 32/48 | 2 | 9 |
| reuse_b__requirements | 5 | 0 | 3 | 5 | 1/48 | 2 | 21 |
| reuse_b__boundaries | 5 | 1 | 5 | 5 | 26/47 | 3 | 14 |
| reuse_sb__none | 5 | 3 | 5 | 5 | 34/49 | 1 | 8 |
| reuse_sb__overview | 5 | 3 | 5 | 5 | 34/47 | 3 | 7 |
| reuse_sb__requirements | 5 | 1 | 5 | 5 | 0/49 | 1 | 13 |
| reuse_sb__boundaries | 5 | 1 | 5 | 5 | 26/49 | 1 | 13 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
