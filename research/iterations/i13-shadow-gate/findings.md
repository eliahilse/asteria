# I13 findings: shadow gate (pilot, N = 6)

Six agentic trajectories on Generation S with the gate in shadow mode: the
judge is consulted as in I12 but never rejects. All six reached full
functionality within five submissions (three on the first). Issue checks:
**37 failed / 0 unresolved / 23 passed of 60** ([issue matrix](issue-matrix.md)).
Dynamically the arm is an agentic control; its counts sit beside I12's control
(3 of 3 functional, 15 / 1 / 14 of 30).

## Calibration

The judge was consulted on 7 submissions; 6 of these were the first clean
submission of a trajectory and one was a repair that touched no enforcement
point, which the sidecar passed without a model call. On all 6 judged
submissions the verdict was "would intervene", citing 6 to 8 of the 11
statements with 4 to 42 verbatim quoted lines each
([calibration table](gate-calibration.md)). Every one of those 6 submissions
failed 4 to 7 issue checks, and in every case at least one failed check lay in
a category the cited statements named. With no silent verdict on an evaluated
submission, specificity cannot be assessed on this data; the base rate of a
first clean submission violating at least one control is 6 of 6.

## Reading

The I12 gate did not fail because the judge was wrong. First submissions do
violate the controls, and the judge points at the right categories. It failed
because rejection did not lead the generator to a compliant submission within
the budget: each rejection produced a new submission with new or persisting
violations, and the budget ran out with nothing evaluated.

## What follows

Two policies keep the judge's signal and drop the cost of rejection:

1. **Coach**: no rejection; the cited statements and quoted lines are attached
   to the functional feedback of the judged submission, so the generator sees
   targeted security context exactly when it violated it, and the artifact is
   still evaluated.
2. **Gate once**: at most one rejection per trajectory, then coach.

Both at N = 3 with a fresh agentic control.

## Limits

Six trajectories, one cell, one graph, judge equal to the generator. The
CWE-based link between statements and checks is coarse (several statements
share CWE-20).
