# I28 findings: the agentic arms at N = 5 in two cells (guard v2, advisory guard, AST autocontext)

Fifty trajectories, Generation S and Reuse S+B, five agentic arms (24
tool turns, 5 submissions with compiler and test feedback, reasoning
effort high), the S2 full document per method and the code graph of the
same acquisition workspace. Ten trajectories were stopped by a
served-identity mismatch before any evaluated submission and collected
again under the declared amendment (seven after the first pass, three
after the second; the stopped attempts are in `runs-superseded/`), so
every arm has five served trajectories. Qualified counts: functional of
5, issue checks failed / unresolved / passed of 50, input-policy clean
(all five rejection checks pass) of 5, full hits (all sixteen tests and all
eleven checks) of 5; see the [issue matrix](issue-matrix.md), the
[full hits](full-hits.md), the [cost](cost.md) and the
[hook audit](hook-audit.md).

| Cell | Arm | Functional | Input-policy clean | Issue checks f / u / p of 50 | Full hits | Tool turns (median) | Submissions | Guard consulted / cancelled (or advised) | Calls; input tokens (M) |
| --- | --- | ---: | ---: | --- | ---: | ---: | ---: | --- | --- |
| Generation S | none | 5 | 0 | 29 / 5 / 16 | 0 | 10 | 12 | | 46; 4.3 |
| Generation S | static | 5 | 3 | 6 / 5 / 39 | 0 | 8 | 12 | | 41; 4.3 |
| Generation S | static-ast | 5 | 3 | 5 / 5 / 40 | 0 | 8 | 14 | | 41; 4.3 |
| Generation S | static-ast-guard | 5 | 4 | 5 / 5 / 40 | 0 | 7 | 14 | 12 / 5 | 88; 5.0 |
| Generation S | static-ast-advise | 4 | 4 | 8 / 4 / 38 | 0 | 10 | 22 | 14 / 9 advised | 90; 5.7 |
| Reuse S+B | none | 5 | 0 | 28 / 4 / 18 | 0 | 8 | 11 | | 41; 6.0 |
| Reuse S+B | static | 5 | 5 | 6 / 3 / 41 | 0 | 9 | 14 | | 42; 6.5 |
| Reuse S+B | static-ast | 5 | 4 | 7 / 3 / 40 | 0 | 6 | 11 | | 36; 5.6 |
| Reuse S+B | static-ast-guard | 5 | 5 | 4 / 2 / 44 | 0 | 8 | 14 | 14 / 6 | 94; 9.0 |
| Reuse S+B | static-ast-advise | 5 | 4 | 6 / 1 / 43 | 0 | 11 | 20 | 11 / 6 advised | 77; 9.7 |

## Against the predictions declared in the README

- Full hits: 0 of 10 in every arm; not decidable as an ordering. No
  artifact passes all eleven checks: the retention bound fails in 19 of
  20 Reuse S2 artifacts and in 9 of 20 Generation ones, and the
  million-record check is unresolved in 32 of 50 artifacts.
- Failed checks of 100 per arm: none the most (57), every static arm
  below none in both cells (12, 12, 9, 14 against 57), met. static-ast
  below static in both cells: **not met** (5 against 6 in Generation, 7
  against 6 in Reuse).
- Functional at least 8 of 10 in every arm: met (49 of 50 trajectories;
  the one budget exhaustion is in the advisory arm). The cancelling guard
  costs the most calls (88 and 94 against 36 to 46) but not the most tool
  turns; the advisory arm costs the most submissions (22 and 20 against 11
  to 14), more than one extra per trajectory: **not met**.
- Guard v2: every acted verdict cites at most two statements and quotes
  the submission verbatim (11 of 11 cancellations); one cancellation per
  trajectory on average (5 in 5 and 6 in 5): met.

## What the arms do

The document does the work: 57 failed checks of 100 without it, 12 with
it, in both cells alike. The five input-policy checks go from 0 clean
artifacts of 10 to 8 (static), 7 (static-ast), 9 (guard) and 8
(advisory). The code graph changes nothing measurable: failed checks 5
against 6 and 7 against 6, tool turns 8 against 8 and 6 against 9, first
compiling submission at the same turn; the injections were shown (19 to 27
per arm) and in 21 of the 30 trajectories that received one the
generator later read a path an injection named.
The cancelling guard is the lowest arm in Reuse (4 failed, 5 of 5
input-policy clean) and tied lowest in Generation (5, 4 of 5 clean), at
about twice the calls and 1.2 to 1.4 times the input tokens; it cancelled
once per trajectory (11 of 26 consultations, the other positive verdicts
blocked by the rules: 9 cited only change-risk statements or none, 2
repeated a statement). The advisory guard delivered 15 verdicts on
executed submissions; the generators submitted again 20 and 22 times
against 12 to 14 elsewhere, and ended with 8 and 6 failed checks, no
better than the insert alone.

## What remains

The retention bound: in Reuse the S2 document never names a bound on the
number of records (the donor game keeps every record), and 21 of 25 S2
artifacts keep more than one hundred; in Generation 9 of 20 reject a valid
record when full and 4 keep more than one hundred, following the
document's "reject records beyond a fixed maximum". The negative score
(Generation 1 to 2 of 5 per S2 arm; Reuse 0) and the unbounded line
(Generation 0 to 4; Reuse 0 to 1). None of these is a judgement the guard
made: its cancellations concern run-end timing and the elapsed-time source
(C3, R3), as in I27.

## Limits

N = 5 per cell and arm, two cells, one acquisition per method, ten
re-collected trajectories; the judge's verdicts are counted, not
labelled; no significance claims.
