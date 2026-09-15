# Time and tokens

Delivery arms: model calls of the generator (and of a judge where a sidecar recorded one), provider usage per call summed, wall time of the calls and of whole trajectories (including evaluation). Acquisitions: one Codex run each; usage as reported by Codex for the run.

## Delivery rounds

| Round | Arm | Trajectories | Calls | Input tokens | of which cached | Output tokens | Reasoning tokens | Call time (min) | Trajectory time (min) |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| i24c-generic | generation_none__single_shot__none | 5 | 5 | 193,730 | 0 | 44,187 | 22,318 | 9.4 | 10.2 |
| i24c-generic | generation_none__single_shot__static | 5 | 5 | 201,535 | 0 | 43,066 | 18,863 | 8.9 | 9.5 |
| i24c-generic | generation_s__single_shot__none | 5 | 5 | 389,535 | 0 | 46,849 | 17,162 | 8.8 | 9.3 |
| i24c-generic | generation_s__single_shot__static | 5 | 5 | 397,340 | 0 | 44,982 | 18,533 | 9.1 | 9.8 |
| i24c-generic | generation_sfb__single_shot__none | 5 | 5 | 852,175 | 0 | 36,576 | 15,122 | 7.4 | 7.9 |
| i24c-generic | generation_sfb__single_shot__static | 5 | 5 | 859,980 | 171,866 | 63,062 | 20,783 | 16.4 | 16.9 |
| i24c-generic | reuse_f__single_shot__none | 5 | 5 | 447,825 | 0 | 47,016 | 18,165 | 9.1 | 9.4 |
| i24c-generic | reuse_f__single_shot__static | 5 | 5 | 453,960 | 0 | 44,201 | 20,099 | 9.2 | 9.5 |
| i24c-generic | reuse_sfb__single_shot__none | 5 | 5 | 867,060 | 0 | 27,643 | 10,423 | 9.4 | 9.7 |
| i24c-generic | reuse_sfb__single_shot__static | 5 | 5 | 873,195 | 0 | 43,972 | 18,766 | 8.9 | 9.2 |

## Acquisitions (context agent)

| Record | Instructions | Angle | Method | Commands | Minutes | Input tokens | of which cached | Output tokens | Reasoning tokens | Items |
| --- | --- | --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
