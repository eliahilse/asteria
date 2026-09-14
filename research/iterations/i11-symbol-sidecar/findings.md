# I11 findings: compact insert and symbol-level sidecar (pilot, N = 3)

Nine trajectories on Generation S, three per arm. Counts are of 3
(functional) and of 30 issue checks (failed / unresolved / passed); see the
[issue matrix](issue-matrix.md). Three trajectories per arm give direction, not
an estimate.

| Arm | First full | Within five | Tool turns per trajectory | Issue checks failed / unresolved / passed of 30 |
| --- | ---: | ---: | --- | --- |
| Single-shot, none (fresh control) | 2 | 3 | 1, 3, 1 | 23 / 1 / 6 |
| Single-shot, compact static insert (17,051 characters) | 0 | 2 | 4, 5, 2 | 3 / 3 / 24 |
| Agentic, symbol-level adaptive sidecar | 0 | 1 | 7, 7, 12 | 1 / 2 / 27 |

## Compact insert

The requirement-and-control rendering of the same graph (11 statements,
17,051 characters instead of 31,141) keeps the security effect of I10's full
insert: 3 failed checks of 30 against 23 in the control, with every
input-policy check except blank names passed by all three artifacts. Two of
three trajectories reach full functionality against I10's 2 of 5 with the full
insert; none succeeds on the first submission.

## Symbol-level sidecar

Slicing on read and edit ranges changed timing, not volume: one trajectory
received 8 statements after reading the level's run-end region and 1 more after
a second read (13,555 characters), and completed in three submissions; the
other two received 9 of the 11 statements before a submission (13,489
characters each) and exhausted the budget. The edits of a Highscore
implementation span the methods where the statements are anchored, so a
symbol-level slice of a compact graph is nearly the compact insert. Failed
checks are the fewest of any arm so far (1 of 30, one blank-name failure), and
functional success the lowest (1 of 3).

## What follows

Injection volume is not the lever; timing and selectivity are. The next pilot
replaces passive injection with a gatekeeper: the sidecar judges each
submission that touches an enforcement point against the anchored controls,
intervenes only on a judged violation by rejecting the submission with the
relevant statements, and stays silent otherwise. Interventions are counted.

## Limits

One cell, three trajectories per arm, one shared graph. The compact insert and
the symbol sidecar change content and timing together relative to I10. No
significance claims.
