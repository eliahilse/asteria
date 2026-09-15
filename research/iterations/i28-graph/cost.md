# Time and tokens

Delivery arms: model calls of the generator (and of a judge where a sidecar recorded one), provider usage per call summed, wall time of the calls and of whole trajectories (including evaluation). Acquisitions: one Codex run each; usage as reported by Codex for the run.

## Delivery rounds

| Round | Arm | Trajectories | Calls | Input tokens | of which cached | Output tokens | Reasoning tokens | Call time (min) | Trajectory time (min) |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| i28-graph | generation_s__agentic__none | 5 | 46 | 4,333,094 | 0 | 48,305 | 19,805 | 10.6 | 11.7 |
| i28-graph | generation_s__agentic__static-ast-advise | 5 | 90 | 5,674,895 | 0 | 66,920 | 20,049 | 10.9 | 16.5 |
| i28-graph | generation_s__agentic__static-ast-guard | 5 | 88 | 5,042,448 | 0 | 98,171 | 28,764 | 14.0 | 19.7 |
| i28-graph | generation_s__agentic__static-ast | 5 | 41 | 4,331,622 | 0 | 64,912 | 22,395 | 12.1 | 13.0 |
| i28-graph | generation_s__agentic__static | 5 | 41 | 4,285,743 | 0 | 49,166 | 15,074 | 9.5 | 10.4 |
| i28-graph | reuse_sb__agentic__none | 5 | 41 | 6,027,380 | 143,383 | 55,509 | 18,978 | 10.8 | 11.5 |
| i28-graph | reuse_sb__agentic__static-ast-advise | 5 | 77 | 9,656,840 | 0 | 71,822 | 31,141 | 14.2 | 17.9 |
| i28-graph | reuse_sb__agentic__static-ast-guard | 5 | 94 | 8,994,452 | 0 | 98,195 | 32,374 | 15.6 | 20.9 |
| i28-graph | reuse_sb__agentic__static-ast | 5 | 36 | 5,565,211 | 299,440 | 53,018 | 19,615 | 10.1 | 11.0 |
| i28-graph | reuse_sb__agentic__static | 5 | 42 | 6,519,219 | 149,688 | 71,470 | 21,594 | 12.7 | 13.7 |

## Acquisitions (context agent)

| Record | Instructions | Angle | Method | Commands | Minutes | Input tokens | of which cached | Output tokens | Reasoning tokens | Items |
| --- | --- | --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
