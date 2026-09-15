# Time and tokens

Delivery arms: model calls of the generator (and of a judge where a sidecar recorded one), provider usage per call summed, wall time of the calls and of whole trajectories (including evaluation). Acquisitions: one Codex run each; usage as reported by Codex for the run.

## Delivery rounds

| Round | Arm | Trajectories | Calls | Input tokens | of which cached | Output tokens | Reasoning tokens | Call time (min) | Trajectory time (min) |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| i29-v11 | generation_s__agentic__none | 5 | 39 | 3,624,915 | 0 | 57,758 | 21,955 | 10.2 | 11.0 |
| i29-v11 | generation_s__agentic__static-guard | 5 | 75 | 4,700,836 | 0 | 80,856 | 23,565 | 11.9 | 15.7 |
| i29-v11 | generation_s__agentic__static | 5 | 41 | 4,075,738 | 0 | 49,068 | 17,399 | 9.4 | 10.2 |
| i29-v11 | reuse_sb__agentic__none | 5 | 38 | 5,615,641 | 0 | 54,682 | 18,979 | 10.3 | 10.9 |
| i29-v11 | reuse_sb__agentic__static-guard | 5 | 76 | 7,812,807 | 0 | 84,929 | 29,702 | 13.1 | 16.9 |
| i29-v11 | reuse_sb__agentic__static | 5 | 35 | 5,679,865 | 0 | 52,221 | 20,994 | 9.5 | 10.2 |

## Acquisitions (context agent)

| Record | Instructions | Angle | Method | Commands | Minutes | Input tokens | of which cached | Output tokens | Reasoning tokens | Items |
| --- | --- | --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
