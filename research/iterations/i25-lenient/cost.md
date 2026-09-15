# Time and tokens

Delivery arms: model calls of the generator (and of a judge where a sidecar recorded one), provider usage per call summed, wall time of the calls and of whole trajectories (including evaluation). Acquisitions: one Codex run each; usage as reported by Codex for the run.

## Delivery rounds

| Round | Arm | Trajectories | Calls | Input tokens | of which cached | Output tokens | Reasoning tokens | Call time (min) | Trajectory time (min) |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| i25-lenient | generation_b__single_shot__none | 5 | 5 | 417,160 | 0 | 42,101 | 19,491 | 7.1 | 7.6 |
| i25-lenient | generation_f__single_shot__none | 5 | 5 | 432,940 | 0 | 33,360 | 14,213 | 5.7 | 6.4 |
| i25-lenient | generation_fb__single_shot__none | 5 | 5 | 656,370 | 0 | 94,133 | 15,207 | 11.5 | 12.6 |
| i25-lenient | generation_none__single_shot__none | 5 | 5 | 193,730 | 0 | 58,733 | 17,528 | 8.3 | 8.5 |
| i25-lenient | generation_s__single_shot__none | 5 | 5 | 389,535 | 0 | 69,254 | 16,052 | 9.3 | 10.2 |
| i25-lenient | generation_sb__single_shot__none | 5 | 5 | 612,965 | 0 | 34,830 | 15,068 | 5.9 | 6.7 |
| i25-lenient | generation_sf__single_shot__none | 5 | 5 | 628,745 | 0 | 40,560 | 14,686 | 6.4 | 6.9 |
| i25-lenient | generation_sfb__single_shot__none | 5 | 5 | 852,175 | 0 | 70,661 | 15,333 | 9.3 | 9.8 |
| i25-lenient | reuse_b__single_shot__none | 5 | 5 | 432,045 | 0 | 37,204 | 17,868 | 7.7 | 8.4 |
| i25-lenient | reuse_f__single_shot__none | 5 | 5 | 447,825 | 0 | 29,309 | 12,201 | 13.4 | 13.5 |
| i25-lenient | reuse_fb__single_shot__none | 5 | 5 | 671,255 | 0 | 28,581 | 11,634 | 6.1 | 6.9 |
| i25-lenient | reuse_none__single_shot__none | 5 | 5 | 208,615 | 0 | 40,481 | 20,302 | 7.0 | 7.2 |
| i25-lenient | reuse_s__single_shot__none | 5 | 5 | 404,420 | 0 | 46,028 | 13,752 | 7.4 | 7.8 |
| i25-lenient | reuse_sb__single_shot__none | 5 | 5 | 627,850 | 0 | 55,565 | 17,547 | 8.1 | 8.5 |
| i25-lenient | reuse_sf__single_shot__none | 5 | 5 | 643,630 | 0 | 36,353 | 16,298 | 6.2 | 6.8 |
| i25-lenient | reuse_sfb__single_shot__none | 5 | 5 | 867,060 | 0 | 35,978 | 15,854 | 6.1 | 7.1 |

## Acquisitions (context agent)

| Record | Instructions | Angle | Method | Commands | Minutes | Input tokens | of which cached | Output tokens | Reasoning tokens | Items |
| --- | --- | --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
