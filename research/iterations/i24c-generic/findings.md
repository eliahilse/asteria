# I24c findings: one-shot, strategy S3 (generic, no repository), N = 5

Fifty trajectories, five cells × fresh control / S3 insert × five, one
response and no feedback, strict delivery, reasoning effort high. The
insert is written by the acquisition agent from the language, the kind of
program and the task sentence alone, with no repository to inspect (one
acquisition per method, `contexts/`). Counts are of 5 (delivered, compiled,
functional) and of 50 issue checks (failed / unresolved / passed); see the
[issue matrix](issue-matrix.md), the [delivery errors](delivery-errors.csv),
the [full hits](full-hits.md), the [hook audit](hook-audit.md) and the
[cost](cost.md).

| Cell | Arm | Delivered | Compiled | Functional | Issue checks failed / unresolved / passed of 50 | Bound per artifact |
| --- | --- | ---: | ---: | ---: | --- | --- |
| Generation None | none | 4 | 4 | 3 | 24 / 14 / 12 | |
| Generation None | S3 | 3 | 3 | 2 | 1 / 23 / 26 | −7.4 to 0.0 |
| Generation S | none | 3 | 3 | 3 | 22 / 22 / 6 | |
| Generation S | S3 | 5 | 5 | 4 | 6 / 14 / 30 | −7.6 to −0.4 |
| Generation S+F+B | none | 3 | 3 | 3 | 22 / 22 / 6 | |
| Generation S+F+B | S3 | 3 | 3 | 2 | 1 / 23 / 26 | −8.6 to +0.4 |
| Reuse F | none | 2 | 2 | 2 | 13 / 32 / 5 | |
| Reuse F | S3 | 3 | 3 | 1 | 1 / 31 / 18 | −8.8 to +3.8 |
| Reuse S+F+B | none | 2 | 2 | 2 | 13 / 32 / 5 | |
| Reuse S+F+B | S3 | 2 | 2 | 2 | 1 / 30 / 19 | −8.8 to +3.6 |

Totals of 25 per arm: delivered 14 (none) against 16 (S3); compiled 14
against 16; functional 13 against 11; issue checks failed 94 / unresolved
122 / passed 34 (none) against 10 / 121 / 119 (S3). Three trajectories were
served by a different model and stopped before delivery (Generation None
S3 r2, Reuse S+F+B none r1 and r5); rejections: 9 in the controls (5
returned a target file as new, 4 anchors not found) and 8 in the S3 arms
(4 target files as new, 2 anchors, 2 integration edits missing). Full
hits: 0 in every arm.

## Against the predictions declared in the README

Security, insert below control on failed checks in every cell where at
least two artifacts of each arm compile: met in all five cells (1 against
24, 6 against 22, 1 against 22, 1 against 13, 1 against 13); the
identification bound excludes zero in Generation S (−7.6 to −0.4) and
touches it in Generation None. Delivery and compilation at or below the
controls: **not met** (16 against 14 compiled; above in Generation S and
Reuse F, below in Generation None, equal elsewhere). The size ordering
S2 > S1 > S3 was expected but not assumed: on failed checks of 250 the
inserts order S1 3, S3 10, S2 17, and on the five input-policy checks S1 0
of 55 resolved, S2 2 of 75, S3 7 of 70 (controls 75 of 75, 56 of 60, 68 of
70). The document written without the repository is not the weakest.

## What remains after the insert

The ten S3 failures: seven input-policy failures (four artifacts in
Generation S and one in Generation S+F+B accept a negative score; one in
Generation S and one in Generation None accept a blank name), three
retention-bound failures; no oversized-line failure (controls 11) and no
million-record failure (controls 2, S3 0 of 3 resolved). The controls fail
the input-policy checks in 68 of 70 resolved outcomes.

## Reading

Three strategies, three rounds, one direction: every insert arm fails far
fewer checks than its fresh control in every cell with two compiled
artifacts per arm (15 of 15 such cells across I24a to I24c), and the
input-policy checks go from 199 failures in 205 resolved control outcomes
to 9 in 200. The strategies differ in what they leave: S1 and S3 leave
input-policy and retention failures in single artifacts, S2 leaves the
retention bound in 12 artifacts by its wording. Delivery under one
response is not consistently lower with an insert: 11 against 15 (S1), 15
against 12 (S2), 16 against 14 (S3).

## Limits

One acquisition per method shared by five trajectories; N = 5; one
response; strict delivery; three trajectories lost to a served identity
mismatch; no significance claims.
