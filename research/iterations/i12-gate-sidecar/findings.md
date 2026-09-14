# I12 findings: gatekeeper sidecar (pilot, N = 3)

Six trajectories on Generation S, three per arm, agentic delivery in both.
Counts are of 3 (functional) and of 30 issue checks (failed / unresolved /
passed); see the [issue matrix](issue-matrix.md).

| Arm | First full | Within five | Tool turns per trajectory | Judge consultations | Interventions | Issue checks failed / unresolved / passed of 30 |
| --- | ---: | ---: | --- | ---: | ---: | --- |
| Agentic, none (fresh control) | 3 | 3 | 6, 3, 3 | 0 | 0 | 15 / 1 / 14 |
| Agentic, gate | 0 | 0 | 10, 7, 10 | 11 | 11 | 0 / 30 / 0 |

## The judge intervened on every submission it saw

The gate consulted the judge 11 times and intervened 11 times. Of the 15
submissions in the gate arm, 11 were rejected by the sidecar and 4 were
rejected by the harness as invalid edits; none reached compilation or
evaluation, so all 30 issue checks of the arm are unresolved and no gate
trajectory is functional. The control arm reached full functionality in all
three trajectories, each on the first evaluated submission.

The judge cited 1 to 8 of the 11 anchored statements per intervention. Its
reasons include violations of listed controls at the edited operation (a
loader that checks the file size and then reads all bytes; recording without
validating score, name and time), but also concerns outside the listed
statements (no guard against duplicate finalization; the time source not
verified as available). The instruction "intervene only when the submission
clearly violates a listed control" did not produce a rare intervention.

## What follows

A gate that never lets a submission through measures nothing about security;
it converts every trajectory into a budget exhaustion. Two changes are needed
before the mechanism is worth a larger run, both cheap to test:

1. **Shadow mode.** The judge runs and records its verdict, but never rejects.
   Verdicts can then be compared with the evaluator's outcomes per artifact,
   giving the judge's precision and recall against the declared checks before
   it is allowed to act.
2. **Evidence-bound verdicts.** The judge must quote the edited lines that
   violate the cited control and may cite only statements whose enforcement
   point the edit touches; a verdict without a quoted line does not intervene.
   Interventions are capped at one per trajectory, followed by a warning-only
   mode.

## Limits

Three trajectories per arm; one cell; one graph; the judge is the same model as
the generator. Rejection feedback contained the cited statements, so the gate
arm also received more security context than the control; with no evaluated
artifact this cannot be separated from the rejections themselves.
