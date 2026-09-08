# Highscore: completed matrix replay and security follow-up

The two stages contain **160 code-generation attempts**: 80 in the original paper
matrix and 80 fresh follow-up attempts. Six additional acquisitions generated the
security contexts from repository source and the original task. All code attempts
and evaluations have reached a terminal outcome.

## What generation and reuse mean

Generation receives the Highscore task, three ApoMario source classes and the
selected S/F/B context. Reuse additionally receives ApoIcarus’s two Highscore
classes and instructions prioritizing adaptation. S describes structure, F
functionality and B behavior. “None” removes the JSON context; it still includes
the task and source.

The first stage crosses both methods with all eight S/F/B combinations, five
requests per cell. This replays the artifact’s matrix with requested
`gpt-5.6-luna` and medium reasoning; it is not an exact reproduction of the
original model, sampling setup or attachment transport.

## Screening and selection

No baseline attempt passed all 16 functional checks. The predeclared functional
check tie-breaker therefore selected the following conditions. Each denominator
of 80 is 16 correlated checks × five attempted responses, not 80 independent trials.

| Method | Selected paper context | Functional checks passed | Game compiled | Fully functional |
| --- | --- | --- | --- | --- |
| Generation | S+F+B | 58/80 | 5/5 | 0/5 |
| Generation | S | 56/80 | 5/5 | 0/5 |
| Reuse | B | 60/80 | 5/5 | 0/5 |
| Reuse | S+B | 60/80 | 5/5 | 0/5 |

Remaining ties were resolved by ascending original prompt ID. Security outcomes
were not used for selection. Each selected condition then received five fresh
responses for each of four cases: no security, overview, task-focused and data-flow
context. The controls use byte-identical baseline prompts. Treatment prompts append
one frozen, delimited context block.

## Main functional result

**0/160 requested attempts produced a fully functional integrated feature.**
The baseline compiled in 76/80 attempts; the follow-up compiled in 77/80. Every
compilable output passed the seven isolated unit tests, but every one failed the
four autonomous checks requiring the running game to record real runs.

Each stage contains one service-reported model substitution, excluded from code
evaluation but retained in the all-attempt denominator. The other 158 responses
reported the requested model; effective sampling settings were not attested.
Five of those responses failed compilation. These distinctions remain explicit
in every condition’s evidence.

All 79 model-matched baseline responses delivered only an
`ApoMarioHighscore.java` code block. In the follow-up, 76 responses did the same;
three also supplied a new `ApoMarioHighscorePanel.java`. None supplied a modified
original game-class block. The raw outputs and live-game assertions agree on the
missing integration.
This is evidence about these submitted requests and this service configuration,
not a general conclusion about the model’s capabilities.

## Security changes are specific and mixed

The following examples compare each treatment with its matching **fresh follow-up
control**. Every row below had five executed outcomes in both arms. The complete
report includes every test, all unresolved outcomes and both denominator choices.

| Selected condition | Security case | Check | Control pass | Treatment pass | Difference |
| --- | --- | --- | --- | --- | --- |
| Generation S+F+B | Task-focused | Bound oversized physical-line processing | 0/5 | 3/5 | +60 pp |
| Generation S+F+B | Data-flow | Bound oversized physical-line processing | 0/5 | 2/5 | +40 pp |
| Reuse B | Overview | Bound retained entries | 3/5 | 1/5 | −40 pp |
| Reuse B | Task-focused | Bound retained entries | 3/5 | 0/5 | −60 pp |
| Reuse S+B | Overview | Bound oversized physical-line processing | 1/5 | 3/5 | +40 pp |
| Reuse S+B | Overview | Bound retained entries | 3/5 | 1/5 | −40 pp |

These exploratory differences do not identify a general winning strategy. A
single response changes a cell rate by 20 percentage points. The persisted-record
resource probe often remained unknown because its fixture could not establish a
valid persisted representation; unknown outcomes must not be described as safe.
In Generation S, some all-attempt improvements also reflect the control’s one
compilation failure. The explorer shows pass/tested beside pass/all-attempt rates
so execution coverage remains distinguishable from check behavior.

## The acquisition process also needs attention

| Method | Strategy | First candidate items | Final items | Citation-feedback turns | Lines shown / indexed |
| --- | --- | --- | --- | --- | --- |
| Generation | Overview | 6 | 5 | 1 | 660/23,347 |
| Generation | Task-focused | 7 | 7 | 2 | 643/23,347 |
| Generation | Data-flow | 8 | 6 | 1 | 902/23,347 |
| Reuse | Overview | 7 | 1 | 2 | 449/31,899 |
| Reuse | Task-focused | 9 | 1 | 2 | 589/31,899 |
| Reuse | Data-flow | 4 | 4 | 1 | 19/31,899 |

The final reuse overview contains a single integration unknown; the final reuse
task-focused context contains a single donor-ordering property. During citation
feedback the model sometimes returned only a narrow correction. The first
candidates, feedback and final outputs are all retained. This does not isolate
acquisition strategy from the behavior of the revision protocol.

There was one acquisition per method/strategy, reused across the selected paper
contexts and code repetitions. Therefore the code results are conditional on
these six contexts; they do not estimate variation between repeated acquisitions.
Exact quote matching does not validate a security claim or a model-supplied CWE tag.

## What this supports next

The next protocol should preserve the complete context during citation repair,
and check complete integration-file delivery before another full matrix. Preserve
this replay as its own study. Changes to acquisition, prompts or model configuration
need a new frozen version, followed by fresh controls and repeated acquisitions.

Five code responses per cell support descriptive exploration. Per-cell Wilson
intervals in the full report show the uncertainty; no multiplicity-adjusted
significance claim is made. Additional repository information and prompt length
also change with the security treatment.

## Evidence

- [Complete condition and per-test report](results.md)
- [Results and evidence-hash snapshot](results.json)
- [Explorer spreadsheet export](highscore-matrix.xlsx)
- Protocol: `research/MATRIX_EXPERIMENT.md`
- Local explorer: http://localhost:5173

Snapshot SHA-256: `471387f00aececb37f4f35e91627f37222ae6e48b93a03650acbcaf019b287bb`.
