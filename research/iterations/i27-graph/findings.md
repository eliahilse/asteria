# I27 findings: guard v2, advisory guard and AST autocontext (calibration, N = 3 per arm)

Fifteen trajectories, Generation S, five agentic arms (24 tool turns, 5
submissions with feedback, reasoning effort high), the S2 full document
and the code graph of I26. Three trajectories were served by a different
model and stopped at their first call (static-ast r3, static-ast-guard
r2, static-ast-advise r3), so those arms have two served trajectories.
Qualified counts: functional of 3, issue checks failed / unresolved /
passed of 30 (the eleven unresolved checks of a stopped trajectory stay
in the denominator), full hits of 3; see the [issue matrix](issue-matrix.md),
the [full hits](full-hits.md) and the [cost](cost.md).

| Arm | Served | Functional | Full hits | Issue checks f / u / p of 30 | Tool turns (median) | Submissions | Injections | Guard consulted / positive / acted | Calls; input tokens (M) |
| --- | ---: | ---: | ---: | --- | ---: | ---: | ---: | --- | --- |
| none | 3 | 3 | 0 | 17 / 2 / 11 | 7 | 7 | 0 | | 22; 2.09 |
| static | 3 | 3 | 0 | 4 / 3 / 23 | 4 | 2 | 0 | | 16; 1.67 |
| static-ast | 2 | 2 | 0 | 1 / 12 / 17 | 11 | 3 | 15 | | 26; 2.71 |
| static-ast-guard | 2 | 2 | 0 | 4 / 3 / 23 | 10 | 11 | 13 | 7 / 5 / 3 | 59; 4.16 |
| static-ast-advise | 2 | 2 | 0 | 2 / 12 / 16 | 7.5 | 5 | 5 | 2 / 2 / 0 | 24; 1.74 |

## Against the predictions declared in the README

- Full hits: 0 in every arm; the million-record check is unresolved in
  every served artifact (precondition not established), so no artifact
  can be a full hit here. Prediction not decidable; the ordering on failed
  checks holds for static-ast (1) against static (4) and none (17), and
  not for the guard arms (4 and 2) against static-ast.
- Guard v2 cancels at most once per trajectory on average: met, 3
  cancellations in 3 trajectories; every acted verdict cites two
  requirement or control statements and quotes lines that occur verbatim
  in the submission (3 of 3, 3 of 3, 5 of 5). The advisory arm reached
  its first compiling submission at turn 7.5 (median) against 10: met.
- Functional at least 2 of 3 in every arm: met (12 of 12 served
  trajectories).
- Residual failures less frequent in the guard arms than in static-ast:
  not met (negative score 2 against 1, retention 1 against 0, line read 1
  against 0 in the cancelling arm).

## What the rules did to the judge

The judge still found a violation in every submission it was shown (8 of 8
raw verdicts positive, as 16 of 16 in I26). The structural rules turned
three of the eight into no action: one cited only change-risk statements
(none normative), two cited a statement the guard had already cancelled
for in that trajectory. The three cancellations concern run-end timing and
the source of the elapsed time (C3, R3: "the available evidence does not
establish that levelStartTime is set", a reason the v2 prompt tells the
judge not to act on) and, once, a name truncated before the store (R1).
None concerns the store's score validation, which is where the residual
failures are. The advisory arm's two positive verdicts never reached the
generator: both came on a functional submission, and this round's harness
ended the trajectory there (changed for I28). Cost: the cancelling arm
used 59 calls and 4.16 M input tokens for two served trajectories against
26 and 2.71 M for static-ast.

## Reading

With N = 2 to 3 per arm, one cell and three lost trajectories, this round
calibrates and does not rank. Two things are stable across I26 and I27:
every S2 arm fails far fewer checks than none (4, 1, 4, 2 against 17
here; 4, 0, 3, 2 against 10 in I26), and the judge, even constrained to
one verbatim-quoted cancellation per statement, does not target the
checks the artifacts fail. The confirmation round I28 (N = 5, two cells,
50 trajectories) decides between static-ast, the cancelling guard and the
advisory guard with the corrected stopping rule.

## Limits

N = 3 declared, 2 served in three arms; one cell; one acquisition; no
significance claims.
