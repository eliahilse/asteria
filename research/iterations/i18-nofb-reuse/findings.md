# I18 findings: compact insert without failure-behaviour lines, Reuse S+B (N = 5)

Ten single-shot trajectories on Reuse S+B: fresh control against the I16
Reuse compact insert rendered without its 8 "Failure behavior:" lines (same 8
statements, anchors and verification lines; 11,475 characters against
12,672). Counts are of 5 (functional) and of 50 issue checks (failed /
unresolved / passed); see the [issue matrix](issue-matrix.md) and the
[hook audit](hook-audit.md).

| Arm | First full | Within five | Calls | Issue checks failed / unresolved / passed of 50 |
| --- | ---: | ---: | ---: | --- |
| none | 2 | 5 | 11 | 27 / 4 / 19 |
| compact insert, no failure lines | 1 | 4 | 12 | 4 / 5 / 41 |

## Against the predictions declared in the README

Security: failed issue checks fall from 27 to 4 of 50 (identification bound
−5.4 to −3.6 per trajectory). Five checks decrease, three are equal, and one
increases: the oversized-line check fails in 4 of 5 insert artifacts against 2
of 5 controls (I16, with the failure lines: 2 of 5 insert, 3 of 5 control).
All four are `OutOfMemoryError` on the 64 MiB line. The dropped R1 failure
line read "Reject invalid records and excessive sizes"; whether its absence
explains the two extra failures cannot be told apart from N = 5 variation.
The prediction "in the range of I16 (2 of 50)" is missed by two checks, both
on this one test.

Functionality: 1 of 5 insert trajectories fails only the two null-name tests
(I16: 2 of 5; prediction: fewer than 2). Its hook passes the live name
unchanged into a store that rejects null (`ApoMarioHighscore.java:81`,
[hook audit](hook-audit.md)). In Reuse the instruction to reject an
unavailable name is part of the C2 statement itself ("obtain the selected
human player's points/name ... and reject unavailable or inconsistent
values"), not only of its failure line, so the rendering ablation cannot
remove it here. The other four insert artifacts substitute a name, as all five
controls do. Functional success is 4 of 5 (I16: 3 of 5).

## Cost

Submissions are 12 against 11; first-submission success 1 against 2. Unlike
Generation (I17: 18 against 6), the Reuse insert does not multiply attempts.

## Reading with I17

Dropping the failure lines removes the null-name skips where the clause lived
in a failure line (Generation: 2 of 5 → 0 of 5) and halves them where the
clause is in the statement body (Reuse: 2 of 5 → 1 of 5). The security effect
stays large in both cells (36 → 2 and 27 → 4 of 50) but one resource check
worsens in Reuse. The clause, not the rendering, is the object to fix: the
acquisition protocol should forbid failure behaviour and control wording that
drops a completed run.

## Limits

One acquisition shared by five trajectories; one cell; N = 5; a rendering
ablation of one insert, not a new acquisition; no significance claims.
