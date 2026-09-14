# I15: rewind (pilot)

Declared on 2026-09-15 after I14, before any I15 code call. One cell, two
arms, three fresh trajectories each, **6 trajectories**. Complete the schedule
regardless of direction; nothing is regenerated.

## Question

I14 showed the two ways of acting on a correct verdict pull apart: coaching
after the first submission preserves functionality and barely changes security
(16 against 19 failed checks of 30), one rejection produces the most secure
artifacts and none that finish (3 failed of 30, 0 of 3 functional). Both leave
the offending attempt in the conversation. I15 tests **rewind**: on the first
positive verdict the harness discards the offending submission and the two
tool turns before it, restores the conversation, working files and touched
state from before those turns, and injects the verdict there, so the
generator re-approaches the change with the security context in place instead
of patching a committed design or arguing with a rejection. Later positive
verdicts are coached. Discarded turns stay in the record and count against the
turn budget; the rewound submission counts against the submission budget.

## Cell and arms

Generation S, agentic delivery: fresh control (no sidecar) and rewind. Context
graph: the frozen I11 Generation data-flow graph, requirement and control
statements only. Verdict rules of I13 (cited statements anchored at edited
operations, at least one verbatim quoted line). Budgets, model, settings,
evaluator and test contracts as in I10 to I14. The judge never sees test
outcomes; the generator sees the judge only through the injected verdict.

## Reporting

Counts of 3 per arm and of 30 issue checks per arm; tool turns, submissions,
judged submissions, positive verdicts, rewinds, coached submissions, characters
injected; per rewind the discarded turns.

## Limits

Three trajectories per arm, one cell, one graph, judge equal to the generator.
Rewind changes conversation length, content and the working state together.
