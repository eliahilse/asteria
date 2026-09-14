# I15 findings: rewind (pilot, N = 3)

Six agentic trajectories on Generation S, three per arm. Counts are of 3
(functional) and of 30 issue checks (failed / unresolved / passed); see the
[issue matrix](issue-matrix.md).

| Arm | First full | Within five | Tool turns per trajectory | Rewinds | Positive verdicts | Coached | Issue checks failed / unresolved / passed of 30 |
| --- | ---: | ---: | --- | ---: | ---: | ---: | --- |
| Agentic, none (fresh control) | 0 | 2 | 4, 10, 6 | 0 | 0 | 0 | 14 / 11 / 5 |
| Agentic, rewind | 0 | 0 | 10, 11, 14 | 3 | 8 | 5 | 3 / 3 / 24 |

The control's 11 unresolved checks come from one trajectory whose final
artifact did not compile (10) and one unknown large-record outcome.

## Rewinding behaves like rejecting

Each rewind trajectory rewound once, at its first judged submission,
discarding that submission and the two tool turns before it and re-entering
with the verdict in place. What followed resembles gate-once (I14): the
evaluated artifacts fail 3 of 30 checks (blank names 2, retention 1) and pass
every other input, resource and retention check; none reaches full
functionality within five submissions. Rewinding added a cost of its own: 7 of
the 12 post-rewind submissions were rejected by the harness as invalid edits,
against 1 of 8 in the control, because the generator lost the conversation
turns in which it had read the code it was editing and its old-text anchors no
longer matched.

## Reading across the sidecar pilots

| Policy on a positive verdict | Round | Functional | Failed issue checks |
| --- | --- | --- | --- |
| Reject every time | I12 | 0 of 3 | none evaluated |
| Record only | I13 | 6 of 6 | 37 of 60 |
| Coach after the fact | I14 | 3 of 3 | 16 of 30 |
| Reject once, then coach | I14 | 0 of 3 | 3 of 30 |
| Rewind two turns once, then coach | I15 | 0 of 3 | 3 of 30 |
| Compact insert before the first attempt (no judge) | I11 | 2 of 3 | 3 of 30 |

Every policy that removes or blocks the first submission produces the same
security profile, 3 failed checks of 30, and no functional artifact within
the budget. The policy that leaves the first submission alone keeps
functionality and gains little. The compact insert delivered before the first
attempt reaches the same security counts as the blocking policies while
keeping 2 of 3 functional. Within these pilots, when the context arrives
matters more than how forcefully it is applied.

## Limits

Three trajectories per arm, one cell, one graph, judge equal to the generator.
Rewind changes conversation length, working state and content together.
Pilot counts give direction only.
