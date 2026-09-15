# I19: compact insert from protocol v9 acquisitions (fail safe without losing the effect)

Declared on 2026-09-15 after I18, before any I19 code call. Two cells, two
arms, five fresh trajectories each, **20 trajectories**. Complete the schedule
regardless of direction; nothing is regenerated.

## Question

The hook audit traced the functional cost of agent-acquired inserts to a
failure clause that tells the run-end hook to skip a run whose player name is
unavailable; rendering the insert without failure lines removed the loss in
Generation but not in Reuse, where the clause sits in a statement body
(I17, I18). I19 fixes the clause at acquisition. Protocol v9 adds one ground
rule to the agent instructions (`research/security/agent/common.md`, rule 6):
rejection belongs at the boundary that receives untrusted data; at the
integration point a missing or invalid value is substituted or normalized,
never a reason to skip the primary effect. Does a compact insert acquired
under v9 keep the security effect without the null-name loss?

## Acquisitions

Two fresh Codex acquisitions (gpt-5.6-luna, reasoning medium, data-flow
angle, same task text as I10), one per method, protocol
`repository-security-context-v9-agent`:

| Method | Record | Items kept | Anchors matched | Compact insert |
| --- | --- | ---: | ---: | --- |
| Generation | agent-e73ba940ca4543ef9b4eb35f195e214e | 27 | 70 | 10 statements, 11,627 characters, 10 failure lines |
| Reuse | agent-ac1dca9e3c534ebf9217d68d26100af2 | 18 | 71 | 8 statements, 14,322 characters, 8 failure lines |

The failure lines now read, for example, "substitute score 0, normalized
fallback name "Player", and elapsed time 0 when a value is missing or invalid,
then continue" (Generation) and "substitute explicit defaults ... then perform
one bounded storeRun call" (Reuse). One Generation control places the fallback
"in storeRun rather than suppressing the run record", which, if implemented
literally, would fail `rejectsNullName`; it is kept as acquired. Graphs and
compact renderings (requirement and control statements, failure lines kept)
are in `contexts/`.

## Cell and arms

Generation S and Reuse S+B, single-shot delivery: fresh control (no security
context) and the v9 compact insert. Model, settings, five-submission budget,
evaluator and test contracts as in I07 and I16.

## Predictions, stated before collection

- Security: failed issue checks well below the fresh control in both cells,
  in the range of I16/I16b (2 and 3 of 50) to I18 (4 of 50).
- Functionality: no trajectory failing only the two null-name tests; hook
  audit reported with the counts. `rejectsNullName` may fail where the
  fallback is placed inside the store.

## Reporting

Counts of 5 per arm and of 50 issue checks per arm, first and within-budget
functional success, calls, identification bounds against the fresh control,
frozen amplification audit and qualifier after collection, hook audit.

## Limits

One acquisition per method shared by five trajectories; N = 5; a new
acquisition, so content differs from I16 beyond the rule; no significance
claims.
