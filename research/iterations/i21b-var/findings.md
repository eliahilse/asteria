# I21b findings: third v10 acquisition per method (N = 3)

Twelve single-shot trajectories, two cells × fresh control / compact insert
of the third v10 acquisition × three. Counts are of 3 (functional) and of 30
issue checks (failed / unresolved / passed); see the
[issue matrix](issue-matrix.md) and the [hook audit](hook-audit.md).

| Cell | Arm | First full | Within five | Calls | Issue checks failed / unresolved / passed of 30 | Functional and all 11 security checks |
| --- | --- | ---: | ---: | ---: | --- | ---: |
| Generation S | none | 3 | 3 | 3 | 24 / 0 / 6 | 0 |
| Generation S | v10 compact insert (acquisition 3) | 2 | 3 | 5 | 2 / 1 / 27 | 1 |
| Reuse S+B | none | 2 | 3 | 4 | 18 / 2 / 10 | 0 |
| Reuse S+B | v10 compact insert (acquisition 3) | 1 | 3 | 5 | 4 / 1 / 25 | 1 |

## Against the predictions declared in the README

Direction (met): Generation bound −7.3 to −7.0 per trajectory (seven checks
decrease, none increases); Reuse −5.3 to −4.3 (six decrease, one increases).

Size (met): 24 → 2 and 18 → 4 of 30, the I20 order or better.

Functionality (met): 3 of 3 functional in every arm; no trajectory fails only
the two null-name tests; every insert artifact substitutes a name at the
hook. Submissions 5 against 3 (Generation) and 5 against 4 (Reuse). One
artifact per cell passes every functional test and all eleven security
checks, the fifth and sixth such artifacts across all rounds.

## Detail

Generation: the two remaining failures are blank names (2 of 3); every
other issue check passes in 3 of 3, including the retention bound and the
oversized line that the first two v10 acquisitions left failing. Reuse: all
input-policy checks pass (15 of 15); the increase is again the retention
bound, 0 → 2 of 3 failed, the third time under a v10 Reuse insert; the other
failures are one oversized line and one large-record load.

## Limits

N = 3 per arm gives direction only; one acquisition per method in this
round; no significance claims.
