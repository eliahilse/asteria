# I20: compact insert from protocol v10 acquisitions (the receiving operation rejects, the caller substitutes)

Declared on 2026-09-15 after I19, before any I20 code call. Two cells, two
arms, five fresh trajectories each, **20 trajectories**. Complete the schedule
regardless of direction; nothing is regenerated.

## Question

Under v9 the Generation agent placed the fail-safe substitution inside
`storeRun`, the artifacts normalized instead of rejecting, and the security
effect vanished (37 → 31 of 50) while Reuse, whose insert kept the store
rejecting, went 35 → 9 (I19). Protocol v10 rewrites ground rule 6: the
operation that receives untrusted data rejects and reports, never
substitutes or clamps; the caller at the integration point substitutes a
stated default before it calls that operation. Does a compact insert
acquired under v10 restore the security effect in Generation while keeping
the null-name loss at zero?

## Acquisitions

Two fresh Codex acquisitions (gpt-5.6-luna, reasoning medium, data-flow
angle, same task text as I10), one per method, protocol
`repository-security-context-v10-agent`:

| Method | Record | Items kept | Anchors matched | Compact insert |
| --- | --- | ---: | ---: | --- |
| Generation | agent-73bbc79a09ce40b9935b93a42e734488 | 25 | 77 | 10 statements, 15,330 characters |
| Reuse | agent-19d42897c05a486ca1c47b2dbaecd017 | 15 | 67 | 8 statements, 11,422 characters |

Both inserts now separate the two roles in their own words. Generation C1:
"At ApoMarioHighscore#storeRun, reject null/blank/oversized names,
out-of-range scores, invalid survival times ... Return false/report the
rejection and leave the validated board unchanged; recordRunEnd normalizes
missing values before this call." Reuse C1: "Reject and report the invalid
tuple without partial mutation; the caller must normalize missing/invalid
values before calling this operation." Graphs and compact renderings
(requirement and control statements, failure lines kept) are in `contexts/`.

## Cell and arms

Generation S and Reuse S+B, single-shot delivery: fresh control (no security
context) and the v10 compact insert. Model, settings, five-submission budget,
evaluator and test contracts as in I07 and I16.

## Predictions, stated before collection

- Security: failed issue checks in both cells in the range of I16/I16b/I18
  (2 to 4 of 50), in particular the five input-policy checks passing in
  Generation where v9 failed all of them.
- Functionality: no trajectory failing only the two null-name tests; within
  five submissions at least 4 of 5 functional per cell.

## Reporting

Counts of 5 per arm and of 50 issue checks per arm, first and within-budget
functional success, calls, identification bounds against the fresh control,
frozen amplification audit and qualifier after collection, hook audit.

## Limits

One acquisition per method shared by five trajectories; N = 5; a new
acquisition, so content differs from I19 beyond the rule; no significance
claims.
