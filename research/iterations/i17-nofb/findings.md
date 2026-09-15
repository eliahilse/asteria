# I17 findings: compact insert without failure-behaviour lines (N = 5)

Ten single-shot trajectories on Generation S: fresh control against the I16b
compact insert rendered without its 11 "Failure behavior:" lines (same 11
statements, anchors and verification lines; 15,408 characters against
17,051). Counts are of 5 (functional) and of 50 issue checks (failed /
unresolved / passed); see the [issue matrix](issue-matrix.md) and the
[hook audit](hook-audit.md).

| Arm | First full | Within five | Calls | Issue checks failed / unresolved / passed of 50 | Functional and all 11 security checks |
| --- | ---: | ---: | ---: | --- | ---: |
| none | 4 | 5 | 6 | 36 / 4 / 10 | 0 |
| compact insert, no failure lines | 0 | 4 | 18 | 2 / 4 / 44 | 1 |

## Against the predictions declared in the README

Security: failed issue checks fall from 36 to 2 of 50 (identification bound
−7.6 to −6.0 per trajectory; seven checks decrease, none increases), the same
range as I16b with the failure lines (3 of 50). The two failures are one blank
name and one oversized line. The security effect lives in the statements, not
in the failure clauses.

Functionality: no trajectory fails only the two null-name coupling tests
(I16b: 2 of 5). All five insert artifacts substitute "Player" when the live
name is null, as the controls do. Functional success is 4 of 5 (I16b: 3 of 5).
The one non-functional trajectory (r2, budget exhausted) also substitutes a
name; it fails the three run-recording tests because its once-per-level guard
`highscoreRecorded` is reset in `init()` and not in `makeLevel`, so after the
driver's level reset no further run is recorded. That is a different defect,
seen once before under the I11 compact insert.

One artifact (r4) is the first in Generation S to pass every functional test
and all eleven security checks within budget; in I09 two Reuse artifacts under
the researcher-written requirements insert did so.

## Cost

No insert trajectory reaches full functionality on the first submission
(control: 4 of 5); submissions total 18 against 6. The insert still triples
the number of attempts even when it no longer removes the null-name runs. Four
of the five insert first submissions were rejected by the harness before
evaluation (two lacked a required integration edit, one edited a file it also
delivered as new, one old-text anchor did not match); the control had none in
this round, the I16 control had three ([delivery errors](delivery-errors.csv)).

## Limits

One acquisition shared by five trajectories; one cell; N = 5; a rendering
ablation of one insert, not a new acquisition; no significance claims. Whether
the failure clauses carry value elsewhere (they were written for the store and
parser controls too) is not measured.
