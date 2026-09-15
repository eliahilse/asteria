# I31: the v13 document (each unit read from untrusted data bounded) in agentic delivery, N = 5 in two cells

Declared on 2026-09-15 after I30, whose document artifacts failed only the oversized-line check (7 of 20) and the million-record check, before any code-generation call of this
round. Two cells, Generation S and Reuse S+B; three agentic arms (24 tool
turns, 5 submissions with compiler and test feedback, reasoning effort
high); five trajectories per arm and cell, **30 trajectories**. The
security context is a fresh data-flow acquisition per method under
protocol v13 (`research/security/agent/common.md`, ground rule 7 as in v12 plus: where the change introduces a read of untrusted data, each unit it reads, a line, a record, a field, is bounded in bytes or characters and the number stated; a bound
is a concrete number kept by evicting or truncating what exceeds it,
never by rejecting a valid input; the domain of each validated value is
stated), rendered in full (`contexts/`). No code graph (I28: nothing
measurable) and no advisory guard (I28: submissions, not security).

| Arm | Prompt | Before a submission |
| --- | --- | --- |
| none | task | |
| static | task + v13 document | |
| static-guard | task + v13 document | guard v2 (submissions only, verbatim quote, at most two requirement or control citations, once per statement, cap 3) |

Served-identity mismatches are handled as in I28: a trajectory stopped
before any evaluated submission is moved to `runs-superseded/` and
collected again under the same run id, at most three passes.

## Predictions, stated before collection

- The oversized-line check (`oversizedPhysicalLine`), failed by 7 of the
  20 v12 document artifacts of I30, fails in at most 2 of the 20 v13
  document artifacts; the blank-name, retention-bound and negative-score
  checks in none.
- Full hits (all sixteen tests and all eleven security checks): at least
  6 of 20 in the two document arms together (I30: 9); none in the none
  arm.
- Failed checks of 50 per cell and arm: none the most; static-guard
  within one failed check of static per cell; functional at least 4 of 5
  in every arm.
- The guard cancels at most once per trajectory on average, every
  cancellation with a verbatim quote.

## Reporting

As I28, per cell and arm: functional of 5, input-policy clean of 5, issue
checks failed / unresolved / passed of 50, full hits of 5, tool turns,
submissions, guard consultations / cancellations, tokens and time of
generator and judge; predictions checked one by one; the v13 documents
compared with the v10 ones on the wording of the bound.
