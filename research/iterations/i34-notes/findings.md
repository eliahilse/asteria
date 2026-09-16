# I34 findings: security notes per symbol, Highscore calibration (N = 2 per cell)

Eight trajectories, Generation S and Reuse S+B, agentic delivery (24 tool
turns, 5 submissions with feedback, `gpt-5.6-luna` at reasoning effort
high), the `notes` sidecar alone and with the v13 document up front
(`static-notes`); controls are the I31 `none` and `static` arms (N = 5 per
cell). See the [issue matrix](issue-matrix.md), the [full hits](full-hits.md)
and the [cost](cost.md).

| Cell | Arm | Functional | Full hits | Issue checks f / u / p of 20 | Failed checks | Injections | Tool turns (median) | Input tokens (M) |
| --- | --- | ---: | ---: | --- | --- | ---: | ---: | ---: |
| Generation S | notes | 2 | 0 | 2 / 2 / 16 | oversized line 2 | 6 | 5 | 1.0 |
| Generation S | static-notes | 2 | 0 | 2 / 2 / 16 | oversized line 2 | 11 | 12 | 2.6 |
| Reuse S+B | notes | 2 | 0 | 15 / 0 / 5 | every input-policy check 2, retention 2, oversized line 2, … | 4 | 9 | 2.7 |
| Reuse S+B | static-notes | 2 | **1** | 2 / 0 / 18 | oversized line 1, million-record 1 | 3 | 6.5 | 2.0 |

For comparison, I31 per cell of 50 checks: Generation none 26 / 5 / 19,
static 3 / 5 / 42; Reuse none 31 / 5 / 14, static 2 / 3 / 45; full hits
1 of 30.

The sidecar works as designed: every trajectory but one received
injections (median 3.5 per trajectory, 1.0 to 2.6 M input tokens per
artifact), the blocks carry the statements tied to the classes and
methods just read with their enforcement points, and no trajectory lost
functionality (8 of 8 functional, against 18 of 20 in I31).

The effect differs by cell. In Generation, the notes alone match the
document: both arms fail only the oversized-line check, as the I31
`static` arm did, while the I31 control fails half the checks. In Reuse,
the notes alone do not carry the input policy: the statements that
demand validation at `storeRun` and bounded reads are anchored at the
ApoIcarus store class the agent is meant to reuse
(`org.apogames.help.ApoHighscore#load/save`), and the two notes-only
trajectories read other code first (one injection at turn 4, one at turn
1 and 5), so 15 of 20 checks fail, more than the I31 control. With the
document up front the notes add the one full hit of the round (Reuse S+B,
r1: every test and every check), where the I31 `static` arm had none in
five.

Reading: notes reach the generator only where it reads noted code. New
code (the highscore store in Generation, the copied store in Reuse) has no
symbol in the snapshot, so statements about it surface only through the
existing classes they are anchored at. The next version should resolve
the identifiers named in a pending submission (the planned
`ApoMarioHighscore#storeRun`, the callees of new code) to their
statements, so the notes reach the code as it is written.

N = 2 per cell; a calibration round, no rate claims.
