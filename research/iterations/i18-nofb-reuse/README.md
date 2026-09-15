# I18: compact insert without failure-behaviour lines, Reuse S+B

Declared on 2026-09-15 after I17, before any I18 code call. One cell, two
arms, five fresh trajectories each, **10 trajectories**. Complete the schedule
regardless of direction; nothing is regenerated.

## Question

I17 showed on Generation S that the compact insert without its
failure-behaviour lines keeps the security effect (36 → 2 failed of 50) and
removes the null-name skips (0 of 5 against 2 of 5 in I16b). In I16 the Reuse
S+B compact insert also lost 2 of 5 trajectories to the same mechanism, there
through control C2 ("reject unavailable or inconsistent values"). Does the
rendering change transfer to the Reuse cell?

## Cell and arms

Reuse S+B, single-shot delivery: fresh control (no security context) and the
I16 Reuse compact insert (8 statements) rendered with `failure_behavior=False`
(`research/context_graph.render`; same graph, anchors and verification lines;
the "Failure behavior:" lines are dropped). Model, settings, five-submission
budget, evaluator and test contracts as in I07 and I16.

## Predictions, stated before collection

- Security: failed issue checks in the range of I16 (2 of 50) against a fresh
  control near 29 to 31 of 50.
- Functionality: fewer than 2 of 5 trajectories failing only the null-name
  tests; hook audit reported with the counts.

## Reporting

Counts of 5 per arm and of 50 issue checks per arm, first and within-budget
functional success, calls, identification bounds against the fresh control,
frozen amplification audit and qualifier after collection, hook audit.

## Limits

One acquisition shared by five trajectories; one cell; N = 5; a rendering
ablation, not a new acquisition; no significance claims.
