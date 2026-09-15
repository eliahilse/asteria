# I21a findings: second v10 acquisition per method (N = 3)

Twelve single-shot trajectories, two cells × fresh control / compact insert
of the second v10 acquisition × three. Counts are of 3 (functional) and of 30
issue checks (failed / unresolved / passed); see the
[issue matrix](issue-matrix.md) and the [hook audit](hook-audit.md).

| Cell | Arm | First full | Within five | Calls | Issue checks failed / unresolved / passed of 30 |
| --- | --- | ---: | ---: | ---: | --- |
| Generation S | none | 1 | 3 | 7 | 24 / 0 / 6 |
| Generation S | v10 compact insert (acquisition 2) | 1 | 3 | 8 | 6 / 2 / 22 |
| Reuse S+B | none | 2 | 3 | 4 | 17 / 2 / 11 |
| Reuse S+B | v10 compact insert (acquisition 2) | 1 | 3 | 8 | 3 / 3 / 24 |

## Against the predictions declared in the README

Direction (met): both insert arms lie below their fresh controls under every
assignment of unresolved outcomes; Generation bound −6.0 to −5.3 per
trajectory (seven checks decrease, none increases), Reuse −5.3 to −3.7 (six
decrease, one increases).

Size (met): scaled to 30 checks, I20 gave Generation 39 → 11 and Reuse 31 → 7
of 50; this acquisition gives 24 → 6 and 17 → 3 of 30, the same order.

Functionality (met): 3 of 3 functional in every arm; no trajectory fails only
the two null-name tests; every insert artifact substitutes a name at the
hook. Submissions 8 against 7 (Generation) and 8 against 4 (Reuse).

## Detail

Generation: all five input-policy checks pass in 13 of 15 (one negative
score, one blank name accepted); remaining failures are the retention bound
(2 of 3) and the oversized line (2 of 3). Reuse: every input-policy check
passes (15 of 15) and the oversized line passes in 3 of 3; the one increase
is again the retention bound, 0 → 3 of 3 failed, as in I20 (0 → 3 of 5).
Twice in a row the v10 Reuse insert artifacts keep more than 100 entries
while their controls do not; the three-acquisition comparison after I21b
looks at whether the Reuse inserts state a retention limit at all.

## Limits

N = 3 per arm gives direction only; one acquisition per method in this
round; no significance claims.
