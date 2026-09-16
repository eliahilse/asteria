# Time and tokens

Delivery arms: model calls of the generator (and of a judge where a sidecar recorded one), provider usage per call summed, wall time of the calls and of whole trajectories (including evaluation). Acquisitions: one Codex run each; usage as reported by Codex for the run.

## Delivery rounds

| Round | Arm | Trajectories | Calls | Input tokens | of which cached | Output tokens | Reasoning tokens | Call time (min) | Trajectory time (min) |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| i34-notes | generation_s__agentic__notes | 2 | 10 | 953,235 | 0 | 27,206 | 6,864 | 4.2 | 4.8 |
| i34-notes | generation_s__agentic__static-notes | 2 | 24 | 2,590,112 | 0 | 20,575 | 7,720 | 7.3 | 8.1 |
| i34-notes | reuse_sb__agentic__notes | 2 | 18 | 2,674,627 | 0 | 19,184 | 6,822 | 5.1 | 5.8 |
| i34-notes | reuse_sb__agentic__static-notes | 2 | 13 | 1,977,649 | 0 | 15,077 | 5,728 | 4.3 | 5.0 |

## Acquisitions (context agent)

| Record | Instructions | Angle | Method | Commands | Minutes | Input tokens | of which cached | Output tokens | Reasoning tokens | Items |
| --- | --- | --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
