# I29: the v11 document (bounds as numbers, value domains) in agentic delivery, N = 5 in two cells

Declared on 2026-09-15 after I28, before any code-generation call of this
round. Two cells, Generation S and Reuse S+B; three agentic arms (24 tool
turns, 5 submissions with compiler and test feedback, reasoning effort
high); five trajectories per arm and cell, **30 trajectories**. The
security context is a fresh data-flow acquisition per method under
protocol v11 (`research/security/agent/common.md`, ground rule 7: a bound
is a concrete number kept by evicting or truncating what exceeds it,
never by rejecting a valid input; the domain of each validated value is
stated), rendered in full (`contexts/`). No code graph (I28: nothing
measurable) and no advisory guard (I28: submissions, not security).

| Arm | Prompt | Before a submission |
| --- | --- | --- |
| none | task | |
| static | task + v11 document | |
| static-guard | task + v11 document | guard v2 (submissions only, verbatim quote, at most two requirement or control citations, once per statement, cap 3) |

Served-identity mismatches are handled as in I28: a trajectory stopped
before any evaluated submission is moved to `runs-superseded/` and
collected again under the same run id, at most three passes.

## Predictions, stated before collection

- The retention-bound check (`boundsRetainedEntries`), failed by 28 of
  the 40 S2 artifacts of I28, fails in fewer than half of the 20 v11
  document artifacts, and the negative-score check in none of them.
- Full hits (all sixteen tests and all eleven security checks): at least
  one in the static or the static-guard arm; none in the none arm.
- Failed checks of 50 per cell and arm: none the most; static-guard at or
  below static; functional at least 4 of 5 in every arm.
- The guard cancels at most once per trajectory on average, every
  cancellation with a verbatim quote.

## Reporting

As I28, per cell and arm: functional of 5, input-policy clean of 5, issue
checks failed / unresolved / passed of 50, full hits of 5, tool turns,
submissions, guard consultations / cancellations, tokens and time of
generator and judge; predictions checked one by one; the v11 documents
compared with the v10 ones on the wording of the bound.
