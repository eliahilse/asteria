# I12: gatekeeper sidecar (pilot)

Declared on 2026-09-15 after I11, before any I12 code call. One cell, two arms,
three fresh trajectories each, **6 trajectories**. Complete the schedule
regardless of direction; nothing is regenerated.

## Question

I10 and I11 delivered security context passively: statements were injected
when the generator touched anchored code, and volume, not timing, dominated.
I12 replaces injection with judgement. The sidecar sees each submission's edits
and the requirement and control statements anchored at the operations those
edits touch. When an edit touches no enforcement point it stays silent without
a model call. Otherwise it asks a judge (the same model, a separate one-turn
conversation, recorded) whether the submission clearly violates a listed
control at its enforcement point. Only a judged violation is acted on: the
submission is rejected before compilation and evaluation, with the violated
statements as the feedback; it counts as a submission. Everything else passes
untouched. Interventions, silent passes and judge transcripts are recorded.

## Cell and arms

Generation S. Fresh agentic control (no sidecar) and agentic with the gate
sidecar. Context: the frozen I11 Generation data-flow graph (acquisition
`agent-3b2861d7d0d041118bfbabf225ff37aa`), requirement and control statements
only. No upfront insert in either arm. Budgets (five submissions, 24 tool
turns), model, settings, evaluator and test contracts as in I10 and I11. The
judge sees the edits and the anchored statements only; it never sees test
outcomes, so security feedback from the evaluator still never reaches the
generator.

## Reporting

Counts with fixed denominators (of 3 per arm, 30 issue checks per arm), tool
turns, submissions, judge consultations, interventions, and for interventions
the statement ids and the reason. The judge system text hash and tool schema
are recorded in every record through the sidecar's `describe()`.

## Limits

Three trajectories per arm. The judge is the same model as the generator; a
wrong intervention costs a submission and is reported as such. One cell, one
graph.

## Collection notes, 2026-09-15

After the first two trajectories had started, the harness file
`research/agentic_delivery.py` was edited (an error-message wording change) and
committed. The frozen manifest hashes that file, so the next four trajectory
processes refused to start ("Frozen input changed") before any model call and
left no run directory. The file was restored to the frozen bytes, the unit test
was adapted to the frozen wording, and the four trajectories were then
collected; the two started trajectories were unaffected. No observation was
lost or repeated.
