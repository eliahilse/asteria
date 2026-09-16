
## Amendment (2026-09-16, during collection)

The first artifact passing all 28 tests is the fourth submission of the
Generation S trajectory r1 (it fails two security checks, the points
overflow and the oversized line). Its evaluation report and complete
files are the evaluator reference of the Achievements task from now on
(`.local/calibration/achievements-integrated-reference/`, response sha256
starting 8c52cf22); every later Achievements round hashes that report.

## Amendment (2026-09-16, during collection): three trajectories restarted

`research/tasks.py` is one of this round's frozen sources and was edited
during collection (analysis helpers, no change to the Achievements
definition); three trajectories (Generation S r2, r3 and Reuse S+B r3)
refused to start with "Frozen input changed" and left no record. The
frozen version of the file was restored and the three trajectories are
collected under the same run ids after the running one finishes; the
three that ran before the edit are unaffected.
