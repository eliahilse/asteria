# I26 findings: agentic delivery with the guard judge and AST autocontext (calibration, N = 2 per arm)

Twelve trajectories, Generation S, six agentic arms (24 tool turns, 5
submissions with compiler and test feedback, reasoning effort high), the
S2 full document as the static insert and as the guard's context, the code
graph of the same workspace as autocontext. Qualified counts: functional
of 2, issue checks failed / unresolved / passed of 20, full hits (all
sixteen tests and all eleven security checks) of 2; see the
[issue matrix](issue-matrix.md), the [full hits](full-hits.md), the
[cost](cost.md) and the [hook audit](hook-audit.md).

| Arm | Functional | Full hits | Issue checks f / u / p of 20 | Input-policy failures of 10 | Tool turns (median) | Submissions | Injections | Guard consulted / positive / cancelled | Generator calls, input tokens | Judge calls, input tokens |
| --- | ---: | ---: | --- | ---: | ---: | ---: | ---: | --- | --- | --- |
| none | 2 | 0 | 10 / 1 / 9 | 5 | 8 | 3 | 0 | | 16; 1.58 M | |
| static | 2 | 0 | 4 / 2 / 14 | 2 | 7.5 | 6 | 0 | | 15; 1.57 M | |
| ast | 2 | 0 | 9 / 2 / 9 | 5 | 8.5 | 4 | 4 | | 17; 1.54 M | |
| static-ast | 2 | **1** | 0 / 1 / 19 | 0 | 7.5 | 4 | 4 | | 15; 1.52 M | |
| static-guard | 2 | 0 | 3 / 2 / 15 | 2 | 12.5 | 6 | 0 | 19 / 8 / 6 | 24; 1.6 M | 39; 1.7 M |
| static-ast-guard | 1 | 0 | 2 / 2 / 16 | 0 | 12 | 7 | 8 | 17 / 8 / 6 | 24; 1.7 M | 39; 1.8 M |

Generator and judge tokens are read from `cost.md` (the judge's calls are
counted with the arm's calls there: 63 calls and 3.27 M and 3.49 M input
tokens for the two guard arms against 15 to 17 calls and 1.52 to 1.58 M
for the others; the split above attributes 24 generator calls per guard
arm from the records).

## Against the predictions declared in the README

- Full hits: none 0 of 2, met; static-ast-guard at least 1 of 2, **not
  met** (0; its r2 exhausted the five submissions after three
  cancellations). The one full hit of the round is static-ast r2, the
  first full hit of any agentic arm in this study.
- Security: every static arm below none on failed checks (4, 0, 3, 2
  against 10), met. Compiled guard artifacts pass the five input-policy
  checks: met in static-ast-guard (10 of 10), not in static-guard (both
  artifacts accept a negative score at the store).
- Functionality: at least 1 of 2 functional in every arm, met (11 of 12
  trajectories functional). The ast arms reach their first compiling
  submission in no more tool turns than their counterparts: static-ast
  7.5 against static 6 (median turn), ast 8.5 against none 8; not met.
- Guard: every cancellation cites S2 statements, at most 3 per trajectory
  (the cap was reached in all four guard trajectories), cost reported.

## What the judge did

Sixteen submissions were judged in the four guard trajectories; the
verdict was positive on 16 of 16 and 12 were cancelled (the cap). The
positive verdicts cite between 2 and 9 statements each (median 4), and in
3 of 16 fewer than half of the quoted lines occur verbatim in the
submission (one verdict quoted five lines, none verbatim). The reasons
recur within a trajectory: `recordRunEnd` called before `setAnalysis` has
finished its score changes (7 verdicts across three trajectories), the
capacity rule of `storeRun` (static-guard r2: an eleventh record accepted
and the lowest evicted, then every run rejected once the board is full,
then again eviction, three cancellations for opposite readings of C1's
"reject records beyond a fixed maximum"), and an invalid score replaced by
0 before the store (the first cancellation of static-guard r1, quoted
verbatim). The judge never intervened on the 24 searches and reads it was
consulted about. Each guard trajectory cost about twice the generator's
input tokens and 4 to 5 extra tool turns.

## What decides security here

Every arm is functional; the residual failures are three checks: the
store accepts a negative score (S2 asks for "finite ranges" and "reject
out-of-range scores" without naming the sign; the artifacts that pass use
`score >= 0`), the retention bound (S2's "reject records beyond a fixed
maximum" produces stores that reject a valid record when full, which the
check counts as a failure, or keep more than one hundred), and the
unbounded line read. static-ast has none of the three in either
trajectory; static fails the negative-score check in both, the retention
bound in one and the line read in one; static-guard fails the negative
score in both and the retention bound in one; static-ast-guard the
retention bound in one and the line read in one. With N = 2 the difference between
static-ast (0 failed) and static (4 failed) is two trajectories' worth of
choices, not a result; the code graph itself carries no security
statement, so any security effect of it runs through what the generator
reads and integrates.

## Limits

N = 2 per arm, one cell, one acquisition; the guard's verdict quality is
measured by citation and quotation counts, not by a labelled truth; no
significance claims. The guard rules for the next round are derived from
these verdicts and the design review in
`docs/reviews/2026-09-15-guard-gpt-6-astra.md`.
