# A01 findings: Achievements bootstrap, agentic delivery without security context (N = 3 per cell)

Six trajectories, Generation S and Reuse S+B, agentic delivery (24 tool
turns, 5 submissions with compiler and test feedback, `gpt-5.6-luna` at
reasoning effort high), no security context. Three trajectories were
restarted after a frozen-source edit (README amendment); no
served-identity mismatch. Qualified counts (no amplification audit exists
for this task; qualified equals raw): functional of 3, issue checks failed
/ unresolved / passed of 30; see the [issue matrix](issue-matrix.md), the
[full hits](full-hits.md) and the [cost](cost.md).

| Cell | Functional | Issue checks f / u / p of 30 | Failed checks | Tool turns (median) | Submissions | Calls; input tokens (M) |
| --- | ---: | --- | --- | ---: | ---: | --- |
| Generation S | 3 | 6 / 0 / 24 | pointsOverflowSafe ×3, oversizedPhysicalLine ×3 | 9 | 14 | see cost.md |
| Reuse S+B | 2 | 6 / 0 / 24 | pointsOverflowSafe ×3, oversizedPhysicalLine ×3 | 11 | 13 | see cost.md |

## Against the predictions declared in the README

- Fewer than 6 of 6 functional: met, 5 of 6 (the Reuse S+B r2 artifact
  passes all 16 unit and 5 autonomous tests but none of the 7 invoked
  tests within five submissions). Highscore's agentic controls were 39 of
  40 functional; the harder task costs one artifact in six here and more
  submissions (13 and 14 of 15 used against 6 to 13 for Highscore).
- Compiled artifacts fail the negative-points, overflow and
  deserialization checks in most cases: partly met. Every artifact fails
  exactly two checks, the points overflow (a 19,999 + Integer.MAX_VALUE
  run total wraps and the goal stays locked) and the oversized line (a
  64 MiB line is read whole); none fails the negative-points,
  negative-time, unknown-id, oversized-id, duplicate, malformed-store or
  deserialization checks. The generator without context already validates
  ids and signs for this feature; what it never does is bound a read or
  guard an addition.

## What this round is for

The first artifact passing all 28 tests (Generation S r1, fourth
submission) is the evaluator's reference for the task. The two failed
checks are the ones a security document has to address for Achievements;
the input-policy checks that carried the Highscore result (5 of 5 failing
without context there) pass here without any context, so the second task
tests a different part of the documents: bounds on reads and arithmetic.

## Limits

N = 3 per cell, one arm; a bootstrap round; no significance claims.
