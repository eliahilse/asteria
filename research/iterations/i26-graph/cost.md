# Time and tokens

Delivery arms: model calls of the generator (and of a judge where a sidecar recorded one), provider usage per call summed, wall time of the calls and of whole trajectories (including evaluation). Acquisitions: one Codex run each; usage as reported by Codex for the run.

## Delivery rounds

| Round | Arm | Trajectories | Calls | Input tokens | of which cached | Output tokens | Reasoning tokens | Call time (min) | Trajectory time (min) |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| i26-graph | generation_s__agentic__ast | 2 | 17 | 1,540,083 | 0 | 22,133 | 8,514 | 5.5 | 5.8 |
| i26-graph | generation_s__agentic__none | 2 | 16 | 1,583,149 | 0 | 21,075 | 7,677 | 5.1 | 5.4 |
| i26-graph | generation_s__agentic__static-ast-guard | 2 | 63 | 3,492,462 | 0 | 86,360 | 19,108 | 13.5 | 19.3 |
| i26-graph | generation_s__agentic__static-ast | 2 | 15 | 1,518,172 | 0 | 22,589 | 6,764 | 5.2 | 5.5 |
| i26-graph | generation_s__agentic__static-guard | 2 | 63 | 3,273,361 | 0 | 68,744 | 19,242 | 10.9 | 15.9 |
| i26-graph | generation_s__agentic__static | 2 | 15 | 1,567,205 | 112,241 | 21,746 | 7,315 | 10.2 | 10.8 |

## Acquisitions (context agent)

| Record | Instructions | Angle | Method | Commands | Minutes | Input tokens | of which cached | Output tokens | Reasoning tokens | Items |
| --- | --- | --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
