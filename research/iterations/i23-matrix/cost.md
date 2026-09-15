# Time and tokens

Delivery arms: model calls of the generator (and of a judge where a sidecar recorded one), provider usage per call summed, wall time of the calls and of whole trajectories (including evaluation). Acquisitions: one Codex run each; usage as reported by Codex for the run.

## Delivery rounds

| Round | Arm | Trajectories | Calls | Input tokens | of which cached | Output tokens | Reasoning tokens | Call time (min) | Trajectory time (min) |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| i23-matrix | generation_b__single_shot__none | 5 | 5 | 417,160 | 0 | 74,286 | 18,061 | 8.7 | 8.7 |
| i23-matrix | generation_f__single_shot__none | 5 | 5 | 432,940 | 0 | 35,749 | 14,866 | 5.0 | 5.3 |
| i23-matrix | generation_fb__single_shot__none | 5 | 5 | 656,370 | 0 | 30,795 | 14,387 | 4.4 | 4.8 |
| i23-matrix | generation_none__single_shot__none | 5 | 5 | 193,730 | 0 | 41,142 | 16,091 | 5.9 | 6.4 |
| i23-matrix | generation_s__single_shot__none | 5 | 5 | 389,535 | 0 | 32,582 | 15,348 | 4.8 | 5.3 |
| i23-matrix | generation_sb__single_shot__none | 5 | 5 | 612,965 | 0 | 96,096 | 15,458 | 9.8 | 10.3 |
| i23-matrix | generation_sf__single_shot__none | 5 | 5 | 628,745 | 0 | 33,670 | 15,370 | 4.9 | 5.4 |
| i23-matrix | generation_sfb__single_shot__none | 5 | 5 | 852,175 | 0 | 51,748 | 15,097 | 6.3 | 6.8 |
| i23-matrix | reuse_b__single_shot__none | 5 | 5 | 432,045 | 0 | 71,684 | 19,322 | 8.5 | 8.9 |
| i23-matrix | reuse_f__single_shot__none | 5 | 5 | 447,825 | 0 | 34,987 | 14,568 | 4.9 | 5.5 |
| i23-matrix | reuse_fb__single_shot__none | 5 | 5 | 671,255 | 0 | 42,274 | 16,514 | 5.9 | 6.3 |
| i23-matrix | reuse_none__single_shot__none | 5 | 5 | 208,615 | 0 | 45,252 | 22,371 | 6.6 | 6.8 |
| i23-matrix | reuse_s__single_shot__none | 5 | 5 | 404,420 | 0 | 37,873 | 16,800 | 5.7 | 6.2 |
| i23-matrix | reuse_sb__single_shot__none | 5 | 5 | 627,850 | 0 | 36,758 | 15,533 | 5.2 | 5.7 |
| i23-matrix | reuse_sf__single_shot__none | 5 | 5 | 643,630 | 0 | 72,491 | 15,167 | 8.0 | 8.5 |
| i23-matrix | reuse_sfb__single_shot__none | 5 | 5 | 867,060 | 0 | 63,750 | 14,611 | 7.4 | 8.1 |

## Acquisitions (context agent)

| Record | Instructions | Angle | Method | Commands | Minutes | Input tokens | of which cached | Output tokens | Reasoning tokens | Items |
| --- | --- | --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
