# Time and tokens

Delivery arms: model calls of the generator (and of a judge where a sidecar recorded one), provider usage per call summed, wall time of the calls and of whole trajectories (including evaluation). Acquisitions: one Codex run each; usage as reported by Codex for the run.

## Delivery rounds

| Round | Arm | Trajectories | Calls | Input tokens | of which cached | Output tokens | Reasoning tokens | Call time (min) | Trajectory time (min) |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| i24b-full | generation_none__single_shot__none | 5 | 5 | 193,730 | 0 | 38,973 | 19,436 | 7.3 | 8.0 |
| i24b-full | generation_none__single_shot__static | 5 | 5 | 239,270 | 0 | 60,449 | 18,875 | 10.0 | 11.5 |
| i24b-full | generation_s__single_shot__none | 5 | 5 | 389,535 | 0 | 50,028 | 15,387 | 8.0 | 8.2 |
| i24b-full | generation_s__single_shot__static | 5 | 5 | 435,075 | 0 | 41,923 | 16,405 | 7.8 | 9.4 |
| i24b-full | generation_sfb__single_shot__none | 5 | 5 | 852,175 | 0 | 38,871 | 16,301 | 7.1 | 7.8 |
| i24b-full | generation_sfb__single_shot__static | 5 | 5 | 897,715 | 0 | 34,894 | 15,697 | 7.2 | 7.3 |
| i24b-full | reuse_f__single_shot__none | 5 | 5 | 447,825 | 0 | 41,710 | 14,946 | 7.1 | 7.6 |
| i24b-full | reuse_f__single_shot__static | 5 | 5 | 479,350 | 0 | 44,899 | 18,209 | 8.1 | 8.5 |
| i24b-full | reuse_sfb__single_shot__none | 5 | 5 | 867,060 | 0 | 33,023 | 12,578 | 6.2 | 7.1 |
| i24b-full | reuse_sfb__single_shot__static | 5 | 5 | 898,585 | 0 | 43,047 | 17,941 | 8.8 | 9.9 |

## Acquisitions (context agent)

| Record | Instructions | Angle | Method | Commands | Minutes | Input tokens | of which cached | Output tokens | Reasoning tokens | Items |
| --- | --- | --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
