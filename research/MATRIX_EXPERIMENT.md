# Highscore: paper matrix and security follow-up

This study replays the Highscore rows of the authors’ replication package in
`vamos-artifact/Pipeline/Prompts.csv`. The unit of observation is one independently
requested code response. The task is to integrate ApoMario Highscore; ApoIcarus
provides the donor implementation in the reuse arm.

**Generation** receives the task, target source and chosen target context.
**Reuse** additionally receives two donor classes and stronger instructions to
adapt existing code. Thus method changes both the instructions and available
source, as in the artifact. Neither arm starts from an empty codebase.

| Paper context | Contents |
| --- | --- |
| None | Task and source attachments only |
| S | Structural JSON: classes, fields, signatures and assets |
| F | Functional JSON: roles and documented capabilities |
| B | Behavioral JSON: calls and execution flow |
| S+F, S+B, F+B, S+F+B | Corresponding original JSON combinations |

## Stage 1: screening

Two methods × eight context combinations × five repetitions = **80 attempts**.
The frozen protocol is `studies/highscore-paper-luna-v2/manifest.json`. Its source,
attachment and complete prompt hashes make the submitted input reviewable. The
schedule randomizes dispatch within repetition blocks. Concurrent completion
order can differ from dispatch order; block numbers are not shared model seeds.

We request `gpt-5.6-luna`, medium reasoning and at most 65,536 output tokens. The
original study used a different model and sampling configuration. We preserve the
original task and output-format suffix, including their conflicting formatting
instructions. Attachments are inlined in their original order instead of being
uploaded through the original provider’s Files API. This is a model replay with
explicit transport differences, not an exact numerical reproduction of the paper.

The primary functional endpoint is all 16 author checks passing, divided by all
attempts. Select the top two conditions **within each method** by:

1. Full functional successes / all attempts.
2. Functional checks passed / (16 × all attempts).
3. Successful game compilations / all attempts.
4. Ascending original CSV prompt ID to resolve remaining ties.

Selection waits for the complete screening. The eleven additional security checks
do not determine selection. Missing, failed and interrupted responses remain in
the attempt denominator; they are never silently retried or discarded.

## Stage 2: security overlay

The conceptual matrix is two methods × eight paper contexts × four security
cases. Screening chooses four method/context combinations for the first follow-up:
**four selected combinations × four security cases × five repetitions = 80 new
code attempts**. Unselected cells remain explicitly untested.

The security cases are no additional context, repository overview, task-focused
review and data-flow review. Each method/strategy gets one fresh acquisition from
repository source and the original Highscore task. Generation acquisition sees
ApoMario; reuse acquisition sees ApoMario and ApoIcarus. It receives no screening
responses, evaluation fixtures, previous contexts or S/F/B JSON. The acquisition
protocol and bounded repository tools are documented in `CONTEXT_GENERATION.md`.

Freeze each acquisition before follow-up code generation, then reuse it across
that method’s selected S/F/B combinations and five repetitions. Append one clearly
delimited JSON security-context block after the original complete prompt. Keep the
control prompt byte-identical to the baseline input. Collect **fresh control
responses** so the comparison does not reuse outcomes that won selection.

Report every generated item, its kind, evidence, citation verification and stated
uncertainty. Keep the first acquisition outcome, including citation problems or
failure; do not regenerate until a more attractive context appears. A failed
acquisition blocks its treatment; it must not silently become an empty control.

Compare treatments with their matching **follow-up controls**, test by test, with
both pass/tested and pass/all-attempt rates. Repetitions are independent calls,
not deterministic seed pairs. Context itself is not replicated in this first
follow-up: conclusions are conditional on the six acquired contexts. Additional
information and prompt length change alongside security content, so this design
does not isolate the effect of security wording alone.

## Evaluation and evidence

Each response is stored before evaluation. A separate evaluator process uses the
original author integration procedure, documented package/import normalization,
and a fresh game workspace. The sixteen functional checks comprise seven unit,
four invoked-integration and five autonomous-integration checks. Eleven security
checks run separately with positive/negative controls and bounded resources.
See `EVALUATION.md` for contracts and limitations.

The explorer shows each test’s pass, fail, not-run, unknown, compile-error and
environment-error counts, alongside attempt-level compiler/test diagnostics and
exact requests/responses. A security pass in the isolated feature harness does
not establish that the whole game works or that no vulnerability exists.

All-attempt rates retain failures to obtain/evaluate usable code. Tested rates
condition on a test actually yielding pass or fail; both denominators are shown.
The 27 checks are correlated outcomes within a response, not 27 independent
replicates. Five responses per cell make this an exploratory study. Avoid broad
model rankings or claims of statistical confirmation from this sample.

The adapter records the served model. If the service does not attest effective
settings, the response remains `settings_unverified`; requested settings are not
presented as confirmed. Model/request mismatches cannot enter evaluation.

Four attempts under v1 are excluded transport calibration: Python’s text reader
normalized CRLF before submission, violating the frozen prompt hashes. V2 reads
exact UTF-8 bytes and starts all 80 attempts afresh. The original v1 records remain
local and are excluded from the active matrix.

## Local operation

The model adapter command and all responses remain gitignored. Configure
`ASTERIA_ADAPTER_COMMAND` as described in `ADAPTER.md`, then validate or execute:

```sh
python3 -m research.study_execution --manifest research/studies/highscore-paper-luna-v2/manifest.json
python3 -m research.study_execution --manifest research/studies/highscore-paper-luna-v2/manifest.json --execute --workers 4
cd workbench
npm run dev
```

The default **Experiment** tab reads local records without initiating model calls.
Its JSON/XLSX exports include condition and test-level counts and observations.
The static deployment contains only public plans; private requests and responses
are never included in its assets.

To export a reviewable local results report and evidence-hash snapshot:

```sh
python3 -m research.report_matrix
```

The report marks incomplete collection, preserves every test denominator, and
includes per-cell 95% Wilson intervals for full functional success using the
[NIST formula](https://www.itl.nist.gov/div898/handbook/prc/section2/prc241.htm).
Intervals assume independent draws with stable success probability within a cell;
there is no multiplicity-adjusted confirmatory claim. At 0/5 successes the interval
still extends to approximately 43.4%. Generated reports stay in `.local/reports`.
