# Time and tokens

Delivery arms: model calls of the generator (and of a judge where a sidecar recorded one), provider usage per call summed, wall time of the calls and of whole trajectories (including evaluation). Acquisitions: one Codex run each; usage as reported by Codex for the run.

## Delivery rounds

| Round | Arm | Trajectories | Calls | Input tokens | of which cached | Output tokens | Reasoning tokens | Call time (min) | Trajectory time (min) |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| a02b-s2 | generation_s__single_shot__none | 5 | 5 | 450,935 | 0 | 48,136 | 22,701 | 7.6 | 7.9 |
| a02b-s2 | generation_s__single_shot__static | 5 | 5 | 486,205 | 0 | 51,937 | 25,517 | 8.6 | 8.9 |
| a02b-s2 | reuse_sb__single_shot__none | 5 | 5 | 682,600 | 0 | 57,938 | 25,667 | 8.9 | 9.0 |
| a02b-s2 | reuse_sb__single_shot__static | 5 | 5 | 716,405 | 0 | 67,236 | 28,961 | 10.2 | 10.2 |

## Acquisitions (context agent)

| Record | Instructions | Angle | Method | Commands | Minutes | Input tokens | of which cached | Output tokens | Reasoning tokens | Items |
| --- | --- | --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
