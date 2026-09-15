# Time and tokens

Delivery arms: model calls of the generator (and of a judge where a sidecar recorded one), provider usage per call summed, wall time of the calls and of whole trajectories (including evaluation). Acquisitions: one Codex run each; usage as reported by Codex for the run.

## Delivery rounds

| Round | Arm | Trajectories | Calls | Input tokens | of which cached | Output tokens | Reasoning tokens | Call time (min) | Trajectory time (min) |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| i31-v13 | generation_s__agentic__none | 5 | 37 | 3,430,260 | 0 | 39,015 | 17,487 | 8.1 | 8.7 |
| i31-v13 | generation_s__agentic__static-guard | 5 | 99 | 5,941,375 | 0 | 101,293 | 27,978 | 13.4 | 19.0 |
| i31-v13 | generation_s__agentic__static | 5 | 34 | 3,328,138 | 0 | 43,426 | 16,407 | 8.1 | 9.0 |
| i31-v13 | reuse_sb__agentic__none | 5 | 33 | 4,806,819 | 0 | 49,706 | 21,147 | 9.5 | 10.3 |
| i31-v13 | reuse_sb__agentic__static-guard | 5 | 95 | 8,068,773 | 0 | 100,804 | 35,462 | 14.0 | 20.5 |
| i31-v13 | reuse_sb__agentic__static | 5 | 44 | 6,954,371 | 0 | 75,895 | 22,891 | 12.5 | 13.4 |

## Acquisitions (context agent)

| Record | Instructions | Angle | Method | Commands | Minutes | Input tokens | of which cached | Output tokens | Reasoning tokens | Items |
| --- | --- | --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
