# I16 findings: compact agent-acquired insert, confirmation (N = 5)

Twenty single-shot trajectories, two cells × fresh control / compact insert ×
five. Counts are of 5 (functional) and of 50 issue checks (failed / unresolved
/ passed); see the [issue matrix](issue-matrix.md).

| Cell | Arm | First full | Within five | Calls | Issue checks failed / unresolved / passed of 50 |
| --- | --- | ---: | ---: | ---: | --- |
| Generation S | none | 3 | 5 | 8 | 36 / 2 / 12 |
| Generation S | compact insert | 0 | 0 | 5 | 0 / 50 / 0 (all five transport failures, see below) |
| Reuse S+B | none | 4 | 5 | 7 | 29 / 4 / 17 |
| Reuse S+B | compact insert | 0 | 3 | 17 | 2 / 4 / 44 |

## Reuse S+B

The compact insert (8 statements, 12,672 characters) reduces failed issue
checks from 29 to 2 of 50; the per-trajectory identification bound is −6.2 to
−4.6 failed checks. Six checks decrease, three are equal, none increases. The
two remaining failures are blank names. Functional success falls from 5 to 3
of 5, with no first-submission success (control: 4 of 5) and 17 calls against
7. In I09 the same cell had 1 failed check of 50 with 5 of 5 functional under
the researcher-written requirements insert (9,475 characters).

The two non-functional insert trajectories fail exactly the two coupling tests
that end a run on a player without a name; both final hooks return when
`getTeamName()` is null ([hook audit](hook-audit.md)), following the insert's
C2 clause "reject unavailable or inconsistent values". The functional insert
artifacts and all controls substitute "Player". See
[functional-cost-null-name.md](../functional-cost-null-name.md).

## Generation S: not measured

All five trajectories of the compact-insert arm failed before their first
model response. The private adapter forwards the request identifier as the
provider's `user` field, which the provider caps at 64 characters; the run
identifiers of this arm are 66 characters long (the round name, the cell and
the arm), and the provider answered every request with a 400 error. The other
three arms have shorter identifiers and were unaffected. The five failed
trajectories are retained as transport failures in the denominators; their
50 issue checks are unresolved and no comparison is possible. The freeze step
now rejects any plan whose request identifiers can exceed the limit, and the
cell is collected again under a shorter round name with its own fresh control
(I16b).

## Limits

One acquisition per method; the compact insert differs from the full I10
insert in content and length. N = 5, two cells, no significance claims.
