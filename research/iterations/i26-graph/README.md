# I26: agentic delivery with the guard judge and AST autocontext (calibration, N = 2 per arm)

Declared on 2026-09-15 after I25, before any model call of this round.
One cell, Generation S; six agentic arms (24 tool turns, 5 submissions,
compiler and test feedback after every submission, reasoning effort high);
two trajectories per arm, **12 trajectories**. The security context is the
S2 full data-flow document of I24b (`contexts/generation-dataflow-full.txt`,
25 statements, 37,613 characters), frozen with the graph it was rendered
from. Collected from the guard worktree (branch
`worktree-agent-a5b84f91685619538`) because the delivery modules of the
main checkout are frozen for I24c.

| Arm | Prompt | After every read | Before every tool call |
| --- | --- | --- | --- |
| none | task | | |
| static | task + S2 | | |
| ast | task | code graph of the code read | |
| static-ast | task + S2 | code graph | |
| static-guard | task + S2 | | guard judge (may cancel, cites S2 statements) |
| static-ast-guard | task + S2 | code graph | guard judge |

**AST autocontext** (`research/autocontext_sidecar.py`): the tree-sitter
code model of the repository snapshot, reduced to classes, constructors and
methods with line ranges and resolved call edges
(`contexts/generation-code-graph.json`, 1,525 symbols, 2,094 edges, from the
same acquisition workspace as the S2 graph). After each read the sidecar
appends, for every method or constructor overlapping the lines read and not
shown before, its callers and callees with the path and line range the
generator can read next; at most 6 symbols and 4,000 characters per
injection, no security statements, no model calls.

**Guard judge** (`research/graph_sidecar.py`, `GuardSidecar`): the same
model in a separate conversation judges every pending tool call before it
executes, with the task, S2, the trajectory so far, the pending call and up
to 3 repository reads of its own; a positive verdict that cites S2
statements cancels the call and hands the advice and the cited statements
back as the tool result; at most 3 cancellations per trajectory. Every
judge request and response is recorded.

Combined kinds (`static-ast-guard`) are one arm serving each hook; the
harness change is covered by `research/test_agentic_delivery.py` and
`research/test_autocontext_sidecar.py`.

## Predictions, stated before collection

- Full hits (functional on all sixteen tests **and** all eleven security
  checks pass): 0 of 2 in none; at least 1 of 2 in static-ast-guard. The
  best full-hit count of any arm so far is 2 of 5 (I06, I09); no agentic
  arm has one.
- Security: every static arm fails fewer checks than none; in the guard
  arms every compiled artifact passes the five input-policy checks.
- Functionality: at least 1 of 2 functional in every arm; the ast arms
  reach their first compiling submission in no more tool turns than their
  counterpart without ast.
- Guard: interventions cite statements present in S2; at most 3 per
  trajectory; cost (judge calls, tokens, time) reported per arm.

## Reporting

Per arm: delivered, compiled, functional (first submission and within
budget) of 2; issue checks failed / unresolved / passed of 20; full hits of
2; tool turns, reads, searches, submissions, injections, guard
consultations / positive verdicts / interventions; time and tokens of the
generator and of the judge. Predictions checked one by one. N = 2 is a
calibration; direction only, no claims.
