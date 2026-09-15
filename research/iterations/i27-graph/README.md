# I27: guard judge v2 (submissions only, verbatim quotes, advisory variant) with AST autocontext (N = 3 per arm)

Declared on 2026-09-15 after I26 and its design review
([docs/reviews/2026-09-15-guard-gpt-6-astra.md](../../../docs/reviews/2026-09-15-guard-gpt-6-astra.md)),
before any model call of this round. One cell, Generation S; five agentic
arms (24 tool turns, 5 submissions, compiler and test feedback, reasoning
effort high); three trajectories per arm, **15 trajectories**. Security
context and code graph are those of I26 (`../i26-graph/contexts`, S2 full
document and the code graph of the same acquisition workspace).

| Arm | Prompt | After reads and searches | Before a submission |
| --- | --- | --- | --- |
| none | task | | |
| static | task + S2 | | |
| static-ast | task + S2 | code graph | |
| static-ast-guard | task + S2 | code graph | guard v2: may cancel |
| static-ast-advise | task + S2 | code graph | guard v2: never cancels, its verdict rides on the submission's feedback |

## What changed against I26

- **Guard v2** (`research/graph_sidecar.py`, `GuardSidecar`): consulted on
  submissions only (I26: 24 consultations on searches and reads, none
  intervened, 14 to 30 percent of the generator's input tokens); a positive
  verdict acts only if it cites at most two requirement or control
  statements and at least one quoted line occurs verbatim in the submitted
  text (I26: the second intervention quoted five lines, none verbatim, and
  asked for wiring it had not seen); the system prompt (`GUARD_SYSTEM_V2`)
  judges the pending call only, names the substitution-before-store case as
  a violation and incompleteness as never one, and treats the statements'
  suggested verifications (negative scores, oversized names) as the cases
  the controls must reject. Cap of 3 cancellations unchanged.
- **Advisory variant** (`advise` kind): the same judge, but the submission
  executes and a positive verdict is appended to the compiler and test
  feedback with its cited statements; the harness records it on the guard
  event (`advised`).
- **AST autocontext** now also covers the excerpts returned by searches
  (`searchRanges`); in I26 the generator searched more than it read, and the
  first graph injection came only before the first submission.

## Predictions, stated before collection

- Full hits (all sixteen tests and all eleven security checks, qualified):
  static-ast-guard or static-ast-advise at or above static-ast; static-ast
  above static; none 0 of 3.
- Guard v2 cancels at most once per trajectory on average and every acted
  verdict quotes a submitted line verbatim; the advisory arm reaches its
  first compiling submission in no more tool turns than the cancelling arm.
- Functional: at least 2 of 3 in every arm (I26: 12 of 12).
- The residual failures of I26 (negative score accepted at the store, the
  retention bound, the unbounded line) each occur less often in the guard
  arms than in static-ast.

## Reporting

As I26: per arm, delivered, compiled, functional of 3; issue checks failed /
unresolved / passed of 30; full hits of 3; tool turns, reads, searches,
submissions, injections, guard consultations / positive / acted verdicts
(cancelled or advised); tokens and time of generator and judge. N = 3 is a
calibration; direction only.
