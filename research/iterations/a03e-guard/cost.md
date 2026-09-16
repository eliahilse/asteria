# Time and tokens

Delivery arms: model calls of the generator (and of a judge where a sidecar recorded one), provider usage per call summed, wall time of the calls and of whole trajectories (including evaluation). Acquisitions: one Codex run each; usage as reported by Codex for the run.

## Delivery rounds

| Round | Arm | Trajectories | Calls | Input tokens | of which cached | Output tokens | Reasoning tokens | Call time (min) | Trajectory time (min) |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| a03e-guard | generation_s__agentic__static-guard | 5 | 100 | 8,217,291 | 0 | 154,855 | 57,141 | 23.5 | 29.4 |
| a03e-guard | reuse_sb__agentic__static-guard | 5 | 115 | 11,557,080 | 0 | 138,036 | 58,413 | 21.7 | 30.4 |

## Acquisitions (context agent)

| Record | Instructions | Angle | Method | Commands | Minutes | Input tokens | of which cached | Output tokens | Reasoning tokens | Items |
| --- | --- | --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
