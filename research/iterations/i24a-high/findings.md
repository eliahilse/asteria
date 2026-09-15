# I24a findings: one-shot, strategy S1 (high-level guidance, no anchors), N = 5

Fifty trajectories, five cells × fresh control / S1 insert × five, one
response and no feedback, strict delivery, reasoning effort high. Counts are
of 5 (delivered, compiled, functional) and of 50 issue checks (failed /
unresolved / passed); see the [issue matrix](issue-matrix.md), the
[delivery errors](delivery-errors.csv) and the [hook audit](hook-audit.md).

| Cell | Arm | Delivered | Compiled | Functional | Issue checks failed / unresolved / passed of 50 | Bound per artifact |
| --- | --- | ---: | ---: | ---: | --- | --- |
| Generation None | none | 4 | 4 | 1 | 26 / 14 / 10 | |
| Generation None | S1 | 4 | 4 | 4 | 0 / 13 / 37 | −8.0 to −2.6 |
| Generation S | none | 4 | 4 | 4 | 28 / 14 / 8 | |
| Generation S | S1 | 1 | 1 | 1 | 0 / 41 / 9 | −8.4 to +2.6 |
| Generation S+F+B | none | 3 | 3 | 3 | 20 / 22 / 8 | |
| Generation S+F+B | S1 | 2 | 2 | 2 | 1 / 32 / 17 | −8.2 to +2.6 |
| Reuse F | none | 1 | 1 | 0 | 8 / 40 / 2 | |
| Reuse F | S1 | 3 | 3 | 2 | 1 / 23 / 26 | −9.4 to +3.2 |
| Reuse S+F+B | none | 3 | 3 | 2 | 19 / 23 / 8 | |
| Reuse S+F+B | S1 | 1 | 1 | 1 | 1 / 41 / 8 | −8.2 to +4.6 |

## Against the predictions declared in the README

Security direction (met where measurable): in every cell the resolved
checks of the insert arm fail far less often than the control's (0, 0, 1,
1, 1 failed of 37, 9, 18, 27, 9 resolved, against 26, 28, 20, 8, 19 of 36,
36, 28, 10, 27). The identification bound excludes zero only in Generation
None (−8.0 to −2.6), where both arms delivered 4 of 5; elsewhere the
rejected and non-compiling artifacts leave too many checks unresolved for
a direction. Delivery at or below the control (prediction met in 3 of 5
cells): Generation S 4 → 1 and S+F+B 3 → 2 delivered, Reuse S+F+B 3 → 1;
Generation None equal at 4; Reuse F above, 1 → 3.

## Reading

The high-level insert, about 7,600 characters with no code citation, moves
the security checks of the compiled artifacts as strongly as the anchored
inserts did in the feedback rounds: all five input-policy checks pass in
every one of the 11 compiled insert artifacts (55 of 55 resolved checks)
and fail in every one of the 15 compiled controls (75 of 75). The cost
under one response is again delivery: the insert arms deliver 11 of 25
responses against 15 of 25 for the controls, and one compiled artifact
passes all functional tests and all eleven security checks (Generation
None). Whether S1 keeps this effect at the feedback
rounds' functional level is the agentic round's question.

## Limits

One acquisition per method shared by five trajectories; N = 5; one
response; strict delivery; no significance claims.
