# I30: the v12 document (text domains named) in agentic delivery, N = 5 in two cells

Declared on 2026-09-15 after I29 was launched and its first artifacts showed one residual failure (a blank name accepted), before any code-generation call of this
round. Two cells, Generation S and Reuse S+B; three agentic arms (24 tool
turns, 5 submissions with compiler and test feedback, reasoning effort
high); five trajectories per arm and cell, **30 trajectories**. The
security context is a fresh data-flow acquisition per method under
protocol v12 (`research/security/agent/common.md`, ground rule 7 as in v11 plus: the domain of a text names presence, blankness after trimming and a length bound; a bound
is a concrete number kept by evicting or truncating what exceeds it,
never by rejecting a valid input; the domain of each validated value is
stated), rendered in full (`contexts/`). No code graph (I28: nothing
measurable) and no advisory guard (I28: submissions, not security).

| Arm | Prompt | Before a submission |
| --- | --- | --- |
| none | task | |
| static | task + v12 document | |
| static-guard | task + v12 document | guard v2 (submissions only, verbatim quote, at most two requirement or control citations, once per statement, cap 3) |

Served-identity mismatches are handled as in I28: a trajectory stopped
before any evaluated submission is moved to `runs-superseded/` and
collected again under the same run id, at most three passes.

## Predictions, stated before collection

- The blank-name check (`rejectsBlankName`), failed by the first four
  v11 artifacts of I29, fails in at most 2 of the 20 v12 document
  artifacts; the retention-bound and negative-score checks in none of
  them.
- Full hits (all sixteen tests and all eleven security checks): at least
  two in the static or the static-guard arm; none in the none arm.
- Failed checks of 50 per cell and arm: none the most; static-guard at or
  below static; functional at least 4 of 5 in every arm.
- The guard cancels at most once per trajectory on average, every
  cancellation with a verbatim quote.

## Reporting

As I28, per cell and arm: functional of 5, input-policy clean of 5, issue
checks failed / unresolved / passed of 50, full hits of 5, tool turns,
submissions, guard consultations / cancellations, tokens and time of
generator and judge; predictions checked one by one; the v12 documents
compared with the v10 ones on the wording of the bound.
