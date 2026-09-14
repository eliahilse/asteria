# I13: shadow gate (pilot)

Declared on 2026-09-15 after I12, before any I13 code call. One cell, one arm,
six fresh trajectories. Complete the schedule regardless of direction; nothing
is regenerated.

## Question

In I12 the gatekeeper's judge intervened on every submission it saw, so no
artifact reached the evaluator. Before the judge may act again, its verdicts
have to be measured against the evaluator. I13 runs the gate in **shadow
mode**: on every clean submission that touches an enforcement point the judge
is consulted exactly as in I12, its verdict is recorded (intervene or not,
cited statements, quoted violating lines, reason), and nothing is rejected.
Each such submission is then compiled and evaluated as usual, so every verdict
is paired with that submission's own security check outcomes.

Two rules from the I12 findings are in force: a verdict counts as "would
intervene" only if it cites statements anchored at the edited operations and
quotes at least one verbatim line of the submission; statements outside the
touched enforcement points cannot be cited.

## Cell and arm

Generation S, agentic delivery, shadow gate; six trajectories. Context graph:
the frozen I11 Generation data-flow graph, requirement and control statements.
No upfront insert. Budgets, model, settings, evaluator and test contracts as in
I10 to I12. The judge never sees test outcomes; the generator never sees the
judge.

## Reporting

Per submission: would-intervene, cited statements, quoted lines, and the
submission's failed issue checks by category; per trajectory: functional
outcome and tool turns. The primary readout is the agreement between "would
intervene" and "at least one failed issue check whose CWE the cited statements
name", as counts (submissions), not rates. Functional and issue counts of the
arm are reported with fixed denominators (of 6 and of 60) as a second fresh
agentic reference.

## Limits

Six trajectories, one cell, one graph, one judge model equal to the generator.
Shadow mode changes nothing the generator sees, so the arm is dynamically an
agentic control; the calibration it yields is about this judge on these
submissions.
