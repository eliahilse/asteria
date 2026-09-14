# I14: coach and gate-once (pilot)

Declared on 2026-09-15 after I13, before any I14 code call. One cell, three
arms, three fresh trajectories each, **9 trajectories**. Complete the schedule
regardless of direction; nothing is regenerated.

## Question

I12 showed that rejecting every judged violation prevents any artifact from
being evaluated; I13 showed that the judge's verdicts point at real failing
categories on every first submission. I14 keeps the judge and changes what
happens with a positive verdict:

- **Coach**: nothing is rejected. The verdict (reason, quoted violating lines,
  cited requirement and control statements) is appended to the functional
  feedback of the judged submission, which is compiled and evaluated as usual.
  The generator therefore receives targeted security context exactly at the
  submission that violated it, and every submission still produces an
  artifact.
- **Gate once**: the first positive verdict rejects the submission with the
  same text; later positive verdicts are coached.

## Cell and arms

Generation S, agentic delivery: fresh control (no sidecar), coach, gate once.
Context graph: the frozen I11 Generation data-flow graph, requirement and
control statements only. No upfront insert. Verdicts count only with cited
statements anchored at the edited operations and at least one verbatim quoted
line (I13 rules). Budgets, model, settings, evaluator and test contracts as in
I10 to I13. The judge never sees test outcomes; the generator never sees the
judge except through the attached verdict.

## Reporting

Counts of 3 per arm and of 30 issue checks per arm; tool turns and submissions;
per arm the number of judged submissions, positive verdicts, rejections
(gate once only) and coached submissions, with the characters attached. The
sidecar policy, cap and graph hash are recorded in every record.

## Limits

Three trajectories per arm, one cell, one graph, judge equal to the generator.
Coaching adds text to the conversation, so it changes prompt length as well as
information; the design does not separate the two.
