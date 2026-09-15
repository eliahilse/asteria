# I22: true one-shot delivery (one response, no feedback) with the protocol v10 inserts

Declared on 2026-09-15 after I21b, before any I22 code call. Two cells, two
arms, five fresh trajectories each, **20 trajectories**. Complete the
schedule regardless of direction; nothing is regenerated.

## Why

Every round so far allowed up to five submissions with compiler and
functional-test feedback after each, which is closer to an assisted loop
than to the one-response protocol of the supervisors' study
(240 single-shot runs, one model response per prompt, no feedback;
<https://github.com/ieiris/llm-context-generation-reuse>). Under that
protocol the functional side is not saturated: across the single-shot
control arms of I16 to I21b, 39 of 70 trajectories were fully functional on
their first submission against 70 of 70 within five. I22 measures the
latest design under a one-response budget so that the baseline and the
cost of the insert are comparable to their protocol.

## Cell and arms

Generation S and Reuse S+B, single-shot delivery with **one submission and
one turn** (`--max-submissions 1 --max-turns 1`; the system text states the
budget as "one"): fresh control (no security context) and the protocol v10
compact inserts of I20, byte-identical
(`contexts/generation-dataflow-compact.txt`,
`contexts/reuse-dataflow-compact.txt`; records
agent-73bbc79a09ce40b9935b93a42e734488 and
agent-19d42897c05a486ca1c47b2dbaecd017). Model, settings, evaluator and
test contracts as in I07 and I20. Security checks run on whatever the one
response delivers; a non-compiling artifact leaves its ten checks
unresolved.

## Predictions, stated before collection

- Functional (first and only submission): control between 2 and 4 of 5 per
  cell, the I16 to I21b first-submission rate; insert arms at or below the
  control (I20 to I21b first-submission counts under the insert: 2, 1, 1,
  1, 2, 1 of 5 or 3).
- Security: the insert arms below their controls on failed checks, with
  more unresolved checks than in I20 where artifacts do not compile.

## Reporting

Counts of 5 per arm and of 50 issue checks per arm, compiled of 5,
functional of 5, identification bounds against the fresh control, frozen
amplification audit and qualifier after collection, hook audit.

## Limits

One acquisition per method shared by five trajectories; N = 5; one
response per trajectory; no significance claims.
