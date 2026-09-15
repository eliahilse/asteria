# I28: confirmation of the agentic arms at N = 5 in two cells (guard v2, advisory guard, AST autocontext)

Declared on 2026-09-15 after I26 and I27, before any model call of this
round. Two cells, Generation S and Reuse S+B (the cells of the feedback
rounds I10 to I21b); five agentic arms (24 tool turns, 5 submissions,
compiler and test feedback, reasoning effort high); five trajectories per
arm and cell, **50 trajectories**. Security context per method is the S2
full document of the I24b acquisitions; the code graph is the compact
tree-sitter model of the same acquisition workspaces (`contexts/`, both
methods). Collected from the main checkout with
`research.autocontext_sidecar:I28`.

| Arm | Prompt | After reads and searches | Before a submission |
| --- | --- | --- | --- |
| none | task | | |
| static | task + S2 | | |
| static-ast | task + S2 | code graph | |
| static-ast-guard | task + S2 | code graph | guard v2: may cancel (cap 3) |
| static-ast-advise | task + S2 | code graph | guard v2 advisory: never cancels; a functional submission with a positive verdict continues with the advice on its feedback |

Guard v2 as in I27 (submissions only, at most two requirement or control
citations, a verbatim quote, once per statement, prompt v2). New against
I27: the advisory arm no longer ends at a functional submission the judge
objects to (I27 collected the advisory arm with the old stopping rule, so
its verdicts never reached the generator on a functional submission).

## Predictions, stated before collection

- Full hits (all sixteen tests and all eleven security checks, qualified)
  of 10 per arm: static-ast above static and none; the two guard arms at or
  above static-ast; none 0 of 10. The best arm of any earlier round is 2
  of 5 (feedback rounds I06, I09).
- Issue checks failed of 100 per arm: none the most; every static arm
  below none in both cells; static-ast below static in both cells.
- Functional of 10: at least 8 in every arm; the cancelling guard costs
  the most tool turns and tokens; the advisory arm no more than one extra
  submission per trajectory on average.
- Guard v2: every acted verdict quotes a submitted line verbatim and cites
  at most two statements; at most one cancellation per trajectory on
  average.

## Reporting

Per arm and cell: delivered, compiled, functional of 5; issue checks
failed / unresolved / passed of 50; full hits of 5; tool turns, reads,
searches, submissions, injections, guard consultations / positive / acted
verdicts; tokens and time of generator and judge. Predictions checked one
by one; identification bounds per cell; no significance claims.

## Amendment (2026-09-15, 09:45, during collection)

In the first wave of five parallel trajectories the provider served
`gpt-5.6-terra` for one response each, and seven of the first nine
trajectories stopped with a served-identity mismatch before any submission
was evaluated. Earlier rounds kept such trajectories as lost (I24c 3 of
50, I25 4 of 80, I27 3 of 15). For this round a stopped trajectory is a
transport failure, not an outcome: after each pass, trajectories whose
status is `identity_mismatch` or `adapter_error` are moved to
`runs-superseded/` with their provider traces and collected again under
the same run id, at most three passes in total. Nothing evaluated is ever
replaced; the superseded attempts stay on disk and are counted in the
findings.
