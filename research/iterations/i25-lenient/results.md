# i25-lenient: results

Saved 2026-09-15T06:46:34.569320+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 1 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_none__single_shot__none | 5 | 0 | 0 | 1 | 7/9 | 41 | 5 |
| generation_s__single_shot__none | 5 | 3 | 3 | 5 | 32/46 | 4 | 5 |
| generation_f__single_shot__none | 5 | 3 | 3 | 4 | 28/36 | 14 | 5 |
| generation_b__single_shot__none | 5 | 2 | 2 | 2 | 12/18 | 32 | 5 |
| generation_sf__single_shot__none | 5 | 3 | 3 | 3 | 18/27 | 23 | 5 |
| generation_sb__single_shot__none | 5 | 3 | 3 | 4 | 27/37 | 13 | 5 |
| generation_fb__single_shot__none | 5 | 4 | 4 | 4 | 29/37 | 13 | 5 |
| generation_sfb__single_shot__none | 5 | 2 | 2 | 2 | 8/18 | 32 | 5 |
| reuse_none__single_shot__none | 5 | 1 | 1 | 1 | 6/10 | 40 | 5 |
| reuse_s__single_shot__none | 5 | 2 | 2 | 2 | 13/19 | 31 | 5 |
| reuse_f__single_shot__none | 5 | 1 | 1 | 1 | 6/9 | 41 | 5 |
| reuse_b__single_shot__none | 5 | 3 | 3 | 3 | 19/29 | 21 | 5 |
| reuse_sf__single_shot__none | 5 | 3 | 3 | 3 | 18/29 | 21 | 5 |
| reuse_sb__single_shot__none | 5 | 2 | 2 | 2 | 13/19 | 31 | 5 |
| reuse_fb__single_shot__none | 5 | 3 | 3 | 3 | 20/29 | 21 | 5 |
| reuse_sfb__single_shot__none | 5 | 3 | 3 | 4 | 24/37 | 13 | 5 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
