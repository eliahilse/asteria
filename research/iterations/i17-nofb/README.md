# I17: compact insert without failure-behaviour lines (rendering ablation)

Declared on 2026-09-15 after the hook audit
([functional-cost-null-name.md](../functional-cost-null-name.md)), before any
I17 code call. One cell, two arms, five fresh trajectories each, **10
trajectories**. Complete the schedule regardless of direction; nothing is
regenerated.

## Question

The functional cost of the agent-acquired compact insert is traced to one
clause of statement R2 ("if the ... name ... is unavailable ... skip
recording"), rendered as a "Failure behavior:" line. Does the same insert
without its failure-behaviour lines keep the security effect (I16b: 37 → 3
failed of 50) while removing the functional cost (I16b: 5 → 3 functional of
5, both losses fail only the two null-name coupling tests)?

## Cell and arms

Generation S, single-shot delivery: fresh control (no security context) and
the I16b compact insert rendered with `failure_behavior=False`
(`research/context_graph.render`; same graph, same 11 statements, same
anchors and verification lines; the 11 "Failure behavior:" lines are dropped;
15,408 characters against 17,051). Model, settings, five-submission budget,
evaluator and test contracts as in I07 and I16b.

## Predictions, stated before collection

- Security: failed issue checks well below the fresh control, in the range of
  I16b (3 of 50) if the effect lives in the statements rather than the failure
  clauses.
- Functionality: fewer than 2 of 5 trajectories failing only the null-name
  tests; the hook audit (`research/hook_audit.py`) is run on the round and
  reported with the counts.

## Reporting

Counts of 5 per arm and of 50 issue checks per arm, first and within-budget
functional success, calls, identification bounds against the fresh control,
frozen amplification audit and qualifier after collection, hook audit.

## Limits

One acquisition shared by five trajectories; one cell; N = 5; a rendering
ablation, not a new acquisition; no significance claims.
