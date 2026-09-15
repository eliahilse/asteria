# Time and tokens

Delivery arms: model calls of the generator (and of a judge where a sidecar recorded one), provider usage per call summed, wall time of the calls and of whole trajectories (including evaluation). Acquisitions: one Codex run each; usage as reported by Codex for the run.

## Delivery rounds

| Round | Arm | Trajectories | Calls | Input tokens | of which cached | Output tokens | Reasoning tokens | Call time (min) | Trajectory time (min) |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| i30-v12 | generation_s__agentic__none | 5 | 47 | 4,414,134 | 0 | 43,788 | 19,257 | 9.5 | 10.4 |
| i30-v12 | generation_s__agentic__static-guard | 5 | 78 | 5,496,040 | 0 | 86,745 | 33,257 | 13.1 | 16.9 |
| i30-v12 | generation_s__agentic__static | 5 | 32 | 3,188,898 | 0 | 49,121 | 15,551 | 8.2 | 8.9 |
| i30-v12 | reuse_sb__agentic__none | 5 | 38 | 5,615,684 | 0 | 44,964 | 17,677 | 8.8 | 9.4 |
| i30-v12 | reuse_sb__agentic__static-guard | 5 | 71 | 5,403,159 | 0 | 92,791 | 28,451 | 11.8 | 16.0 |
| i30-v12 | reuse_sb__agentic__static | 5 | 36 | 5,540,427 | 0 | 46,777 | 14,479 | 8.2 | 8.9 |

## Acquisitions (context agent)

| Record | Instructions | Angle | Method | Commands | Minutes | Input tokens | of which cached | Output tokens | Reasoning tokens | Items |
| --- | --- | --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
