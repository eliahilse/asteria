# i23-matrix: results

Saved 2026-09-15T06:07:02.691745+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 1 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_none__single_shot__none | 5 | 3 | 3 | 3 | 19/28 | 22 | 5 |
| generation_s__single_shot__none | 5 | 3 | 3 | 3 | 17/27 | 23 | 5 |
| generation_f__single_shot__none | 5 | 2 | 2 | 2 | 13/19 | 31 | 5 |
| generation_b__single_shot__none | 5 | 0 | 0 | 0 | 0/0 | 50 | 5 |
| generation_sf__single_shot__none | 5 | 2 | 2 | 3 | 20/28 | 22 | 5 |
| generation_sb__single_shot__none | 5 | 2 | 2 | 2 | 14/18 | 32 | 5 |
| generation_fb__single_shot__none | 5 | 2 | 2 | 2 | 15/19 | 31 | 5 |
| generation_sfb__single_shot__none | 5 | 3 | 3 | 3 | 15/28 | 22 | 5 |
| reuse_none__single_shot__none | 5 | 0 | 0 | 0 | 0/0 | 50 | 5 |
| reuse_s__single_shot__none | 5 | 3 | 3 | 3 | 18/27 | 23 | 5 |
| reuse_f__single_shot__none | 5 | 4 | 4 | 4 | 26/37 | 13 | 5 |
| reuse_b__single_shot__none | 5 | 2 | 2 | 2 | 12/20 | 30 | 5 |
| reuse_sf__single_shot__none | 5 | 3 | 3 | 3 | 20/27 | 23 | 5 |
| reuse_sb__single_shot__none | 5 | 3 | 3 | 3 | 17/28 | 22 | 5 |
| reuse_fb__single_shot__none | 5 | 1 | 1 | 2 | 12/19 | 31 | 5 |
| reuse_sfb__single_shot__none | 5 | 3 | 3 | 4 | 26/39 | 11 | 5 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
