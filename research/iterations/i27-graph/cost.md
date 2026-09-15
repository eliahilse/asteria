# Time and tokens

Delivery arms: model calls of the generator (and of a judge where a sidecar recorded one), provider usage per call summed, wall time of the calls and of whole trajectories (including evaluation). Acquisitions: one Codex run each; usage as reported by Codex for the run.

## Delivery rounds

| Round | Arm | Trajectories | Calls | Input tokens | of which cached | Output tokens | Reasoning tokens | Call time (min) | Trajectory time (min) |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| i27-graph | generation_s__agentic__none | 3 | 22 | 2,092,375 | 0 | 25,992 | 10,976 | 6.7 | 7.1 |
| i27-graph | generation_s__agentic__static-ast-advise | 3 | 24 | 1,743,634 | 0 | 29,687 | 8,798 | 6.0 | 7.2 |
| i27-graph | generation_s__agentic__static-ast-guard | 3 | 59 | 4,159,634 | 0 | 58,179 | 17,816 | 11.7 | 15.0 |
| i27-graph | generation_s__agentic__static-ast | 3 | 26 | 2,707,553 | 0 | 37,786 | 14,564 | 10.1 | 10.5 |
| i27-graph | generation_s__agentic__static | 3 | 16 | 1,674,950 | 0 | 22,356 | 7,877 | 5.4 | 5.9 |

## Acquisitions (context agent)

| Record | Instructions | Angle | Method | Commands | Minutes | Input tokens | of which cached | Output tokens | Reasoning tokens | Items |
| --- | --- | --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
