# i24a-high: results

Saved 2026-09-15T06:39:14.435782+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 1 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_none__single_shot__none | 5 | 1 | 1 | 4 | 26/37 | 13 | 5 |
| generation_none__single_shot__static | 5 | 4 | 4 | 4 | 0/37 | 13 | 5 |
| generation_s__single_shot__none | 5 | 4 | 4 | 4 | 28/36 | 14 | 5 |
| generation_s__single_shot__static | 5 | 1 | 1 | 1 | 0/9 | 41 | 5 |
| generation_sfb__single_shot__none | 5 | 3 | 3 | 3 | 20/28 | 22 | 5 |
| generation_sfb__single_shot__static | 5 | 2 | 2 | 2 | 1/18 | 32 | 5 |
| reuse_f__single_shot__none | 5 | 0 | 0 | 1 | 8/10 | 40 | 5 |
| reuse_f__single_shot__static | 5 | 2 | 2 | 3 | 1/28 | 22 | 5 |
| reuse_sfb__single_shot__none | 5 | 2 | 2 | 3 | 20/28 | 22 | 5 |
| reuse_sfb__single_shot__static | 5 | 1 | 1 | 1 | 1/9 | 41 | 5 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
