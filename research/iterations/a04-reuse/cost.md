# Time and tokens

Delivery arms: model calls of the generator (and of a judge where a sidecar recorded one), provider usage per call summed, wall time of the calls and of whole trajectories (including evaluation). Acquisitions: one Codex run each; usage as reported by Codex for the run.

## Delivery rounds

| Round | Arm | Trajectories | Calls | Input tokens | of which cached | Output tokens | Reasoning tokens | Call time (min) | Trajectory time (min) |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| a04-reuse | reuse_sb__agentic__none | 5 | 43 | 6,965,085 | 0 | 99,070 | 37,360 | 17.0 | 19.1 |
| a04-reuse | reuse_sb__agentic__static | 5 | 53 | 8,939,868 | 0 | 95,604 | 36,447 | 17.9 | 20.4 |

## Acquisitions (context agent)

| Record | Instructions | Angle | Method | Commands | Minutes | Input tokens | of which cached | Output tokens | Reasoning tokens | Items |
| --- | --- | --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
