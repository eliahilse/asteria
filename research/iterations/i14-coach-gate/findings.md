# I14 findings: coach and gate-once (pilot, N = 3)

Nine agentic trajectories on Generation S, three per arm. Counts are of 3
(functional) and of 30 issue checks (failed / unresolved / passed); see the
[issue matrix](issue-matrix.md).

| Arm | First full | Within five | Tool turns per trajectory | Judged | Positive verdicts | Rejections | Coached | Issue checks failed / unresolved / passed of 30 |
| --- | ---: | ---: | --- | ---: | ---: | ---: | ---: | --- |
| Agentic, none (fresh control) | 2 | 3 | 4, 8, 1 | 0 | 0 | 0 | 0 | 19 / 1 / 10 |
| Agentic, coach | 1 | 3 | 6, 5, 3 | 3 | 3 | 0 | 3 | 16 / 0 / 14 |
| Agentic, gate once | 0 | 0 | 7, 8, 7 | 7 | 6 | 3 | 3 | 3 / 3 / 24 |

## Coaching preserves functionality and barely moves security

All three coached trajectories reached full functionality. The judge was
positive on each first submission and its verdict (about 14,000 characters)
was attached to that submission's functional feedback. In two trajectories the
generator then needed two more submissions and ended functional; failed issue
checks fell from 19 to 16 of 30, with the same categories failing as in the
control (oversized line 3 of 3 in both, large-record 3 of 3). In the third
trajectory the first submission was already functional, so the trajectory
ended before the advice was ever sent. Advice delivered after a design is
committed changes little.

## One rejection yields the most secure and least functional artifacts

Gate once rejected exactly one submission per trajectory and coached the rest.
The evaluated artifacts fail only 3 issue checks of 30 (blank names, 3 of 3),
pass every other input, retention and resource check, and none reaches full
functionality within five submissions. In two trajectories the first
submission was rejected and the four following submissions were evaluated,
all functionally incomplete. The rejection turns the generator toward the
controls at the cost of the feature; five submissions are not enough to
finish both.

## Reading across I12 to I14

| Policy | Functional | Failed issue checks | Round |
| --- | --- | --- | --- |
| Reject every positive verdict | 0 of 3 | none evaluated | I12 |
| Never act (shadow) | 6 of 6 | 37 of 60 | I13 |
| Coach after the first verdict | 3 of 3 | 16 of 30 | I14 |
| Reject once, then coach | 0 of 3 | 3 of 30 | I14 |

The judge's signal is real and the two ways of acting on it pull in opposite
directions. Neither pilot policy dominates the static compact insert of I11
(2 of 3 functional, 3 failed of 30), which delivers the same statements before
the first design decision instead of after it.

## Limits

Three trajectories per arm, one cell, one graph, judge equal to the generator.
Rejection and coaching also add text to the conversation; content and timing
are not separated. Pilot counts give direction only.
