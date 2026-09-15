# Acquisition variance under protocol v10 (I20, I21a, I21b)

Every earlier round shared one acquisition across its trajectories, so the
protocol and the content of one document were confounded. Under protocol
v10, three independent Codex acquisitions per method (data-flow angle, same
task text, same model and settings) were each run against their own fresh
control in both cells: I20 (N = 5 per arm), I21a and I21b (N = 3 per arm).
Counts are failed / unresolved / passed issue checks of 10N; bounds are
identification bounds on failed checks per trajectory, insert minus control.

| Round | N | Cell | Control | Insert | Bound | Functional (insert vs control) | Submissions |
| --- | ---: | --- | --- | --- | --- | --- | --- |
| I20 | 5 | Generation S | 39 / 1 / 10 | 11 / 5 / 34 | −5.8 to −4.6 | 5 vs 5 | 10 vs 10 |
| I21a | 3 | Generation S | 24 / 0 / 6 | 6 / 2 / 22 | −6.0 to −5.3 | 3 vs 3 | 8 vs 7 |
| I21b | 3 | Generation S | 24 / 0 / 6 | 2 / 1 / 27 | −7.3 to −7.0 | 3 vs 3 | 5 vs 3 |
| I20 | 5 | Reuse S+B | 31 / 1 / 18 | 7 / 2 / 41 | −5.0 to −4.4 | 5 vs 5 | 11 vs 13 |
| I21a | 3 | Reuse S+B | 17 / 2 / 11 | 3 / 3 / 24 | −5.3 to −3.7 | 3 vs 3 | 8 vs 4 |
| I21b | 3 | Reuse S+B | 18 / 2 / 10 | 4 / 1 / 25 | −5.3 to −4.3 | 3 vs 3 | 5 vs 4 |

## What is stable across acquisitions

- Direction: the insert arm lies below its control under every assignment
  of unresolved outcomes in all six comparisons; no bound crosses zero.
- Size: 3.7 to 7.3 fewer failed checks per trajectory, against controls
  that fail 5.7 to 8.0 of 10 per trajectory.
- Functionality: 22 of 22 insert trajectories functional within five
  submissions, the same as their 22 controls; no trajectory fails only the
  two null-name tests (hook audits of the three rounds).
- Input policy: every one of the five input checks passes in all Reuse
  insert artifacts (55 of 55); in Generation 49 of 55 (negative scores and
  blank names account for the six).
- Attempts: submissions are within two of the control total in five of six
  comparisons.

## What varies

- The remaining failures are resource checks, and which ones depends on the
  acquisition: Generation oversized line 5 of 5, 2 of 3, 0 of 3; Generation
  retention bound 4 of 5, 2 of 3, 0 of 3.
- The Reuse retention bound worsens under every v10 insert (3 of 5, 3 of 3,
  2 of 3 failed; 0 in the six control arms). None of the three v10 Reuse
  inserts states a record-count limit; the v8 Reuse insert of I16 did
  ("parse a strict format with a maximum record count ..."), and its arm
  failed that check 0 of 5. Whether the v10 wording draws the agent's
  attention away from bounding, or the three acquisitions simply missed it,
  cannot be told apart here.
- Joint artifacts (every functional test and all eleven security checks):
  1, 0 and 2 across the three rounds; six exist across all rounds.

## Reading

The v10 result is a property of the protocol, not of one lucky document:
three acquisitions per method agree in direction, order of size and
functional cost. The insert's content still decides which resource checks
survive, and the Reuse retention regression is a candidate for the next
protocol wording (a bound on retained records as a required statement when
the change persists a growing collection).

## Limits

N = 3 in two of the three rounds; one task, one repository pair; the three
acquisitions share model, settings and task text and differ only by
sampling; descriptive counts.
