# I16b findings: compact insert, Generation S re-collection (N = 5)

Ten single-shot trajectories: Generation S, fresh control (no security
context) against the compact agent-acquired insert (11 statements, 17,051
characters, identical bytes to the I16 arm that could not be measured).
Counts are of 5 (functional) and of 50 issue checks (failed / unresolved /
passed); see the [issue matrix](issue-matrix.md).

| Arm | First full | Within five | Calls | Issue checks failed / unresolved / passed of 50 |
| --- | ---: | ---: | ---: | --- |
| none | 3 | 5 | 7 | 37 / 2 / 11 |
| compact insert | 1 | 3 | 18 | 3 / 5 / 42 |

## Security

The compact insert reduces failed issue checks from 37 to 3 of 50; the
per-trajectory identification bound is −7.2 to −5.8 failed checks. Seven
checks decrease, two are equal (malformed store, deserialization canary: no
failures in either arm), none increases. The three remaining failures are blank
names (3 of 5 insert artifacts). Every control artifact fails all five input
checks and the oversized-line check; every insert artifact passes them.

The large-record check is unresolved for all five insert artifacts: two use a
persistence encoding the amplifier does not support, three fail the two-record
precondition of the legacy recipe. The probe's raw observation for the latter
three was that one million records were rejected or loaded within the limits,
but without the precondition no pass is recorded. In the control, three
artifacts fail with `OutOfMemoryError` and two are unresolved (encoding).

## Functionality and cost

Within-budget functional success falls from 5 to 3 of 5; first-submission
success from 3 to 1 of 5; submissions rise from 7 to 18. The two insert
trajectories without a functional artifact exhausted the five-submission budget.

## Comparison with earlier rounds

Same cell, single-shot delivery:

| Round | Insert | N | Within five | Failed / unresolved / passed of 10 N |
| --- | --- | ---: | ---: | --- |
| I09 | none | 5 | 5 | 38 / 0 / 12 |
| I09 | researcher-written requirements (S) | 5 | 5 | 12 / 2 / 36 |
| I11 | compact agent-acquired | 3 | 2 | 3 / 3 / 24 |
| I16b | none | 5 | 5 | 37 / 2 / 11 |
| I16b | compact agent-acquired | 5 | 3 | 3 / 5 / 42 |

The compact insert reproduces its I11 security profile at N = 5 and lowers
failed checks below the researcher-written requirements insert, at a cost in
functional success that the requirements insert did not show.

## Limits

One acquisition shared by five trajectories; one cell; N = 5; no significance
claims. The insert is 1.8 times the length of the I09 requirements insert.
