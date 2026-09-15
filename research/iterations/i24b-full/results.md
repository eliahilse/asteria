# i24b-full: results

Saved 2026-09-15T09:05:12.397946+00:00. Collection complete: True.

N counts code-generation trajectories sharing the acquired context within each arm; each permits up to 1 model submissions. Security issues count failed checks, excluding the positive valid-record round trip. Unresolved checks are not passes.

| Condition | N | First-submit full | Within-budget full | Compiled | Issue failures / evaluated | Unresolved | Model calls |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_none__single_shot__none | 5 | 2 | 2 | 3 | 18/28 | 22 | 5 |
| generation_none__single_shot__static | 5 | 4 | 4 | 4 | 5/39 | 11 | 5 |
| generation_s__single_shot__none | 5 | 1 | 1 | 1 | 7/9 | 41 | 5 |
| generation_s__single_shot__static | 5 | 3 | 3 | 5 | 5/47 | 3 | 5 |
| generation_sfb__single_shot__none | 5 | 2 | 2 | 2 | 14/18 | 32 | 5 |
| generation_sfb__single_shot__static | 5 | 1 | 1 | 1 | 1/10 | 40 | 5 |
| reuse_f__single_shot__none | 5 | 0 | 0 | 2 | 13/18 | 32 | 5 |
| reuse_f__single_shot__static | 5 | 2 | 2 | 2 | 2/20 | 30 | 5 |
| reuse_sfb__single_shot__none | 5 | 4 | 4 | 4 | 25/38 | 12 | 5 |
| reuse_sfb__single_shot__static | 5 | 2 | 2 | 3 | 4/30 | 20 | 5 |

All per-test and trajectory observations are retained in results.json; exact requests, responses and intermediate evaluations are retained in the archived runtime directory. This is exploratory development, not a confirmatory significance claim.
