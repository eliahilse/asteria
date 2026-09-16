# A02b findings: Achievements under one response, no context and the v13 full document (S2), N = 5 per cell

Twenty trajectories, Generation S and Reuse S+B, one response, no
feedback, strict delivery, `gpt-5.6-luna` at reasoning effort high; the
control arm is shared with A02a (S1) and A02c (S3). Counts of 5 per cell
and arm; see the [issue matrix](issue-matrix.md), the [full hits](full-hits.md)
and the [cost](cost.md).

| Cell | Arm | Delivered | Compiled | Functional | Issue checks f / u / p of 50 |
| --- | --- | ---: | ---: | ---: | --- |
| Generation S | none | 3 | 0 | 0 | 0 / 50 / 0 |
| Generation S | S2 | 3 | 0 | 0 | 0 / 50 / 0 |
| Reuse S+B | none | 1 | 0 | 0 | 0 / 50 / 0 |
| Reuse S+B | S2 | 0 | 0 | 0 | 0 / 50 / 0 |

No artifact of the twenty compiles. Thirteen responses are rejected at
delivery (six return an existing file as new, four use an edit anchor
that does not occur, three omit a required integration edit); the seven
that are delivered fail to compile as a whole game, so no functional test
runs and every security check is unresolved. The prior study compiled 33
percent of its Generation and 23 percent of its Reuse Achievements runs
with a pipeline that repairs imports and placement before compiling.

## Against the prediction

Fewer artifacts compile than for Highscore (50 to 70 percent with a
document): met, at zero. The security prediction is not decidable; no
check resolves. The document changes nothing here because nothing
compiles: under one response the second task is a compilation problem
before it is a security one.

## Limits

N = 5 per cell and arm; one response; strict delivery; no significance
claims.
