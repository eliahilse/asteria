# I31 findings: the v13 document in agentic delivery, N = 5 in two cells

Thirty trajectories, Generation S and Reuse S+B, three agentic arms (24
tool turns, 5 submissions with compiler and test feedback, reasoning
effort high), a fresh data-flow document per method acquired under
protocol v13 (ground rule 7 as in v12, plus: each unit read from untrusted
data is bounded in bytes or characters and the number stated). No
served-identity mismatch. Qualified counts as in I30; see the
[issue matrix](issue-matrix.md), the [full hits](full-hits.md), the
[cost](cost.md) and the [hook audit](hook-audit.md).

| Cell | Arm | Functional | Input-policy clean | Issue checks f / u / p of 50 | Full hits | Tool turns (median) | Submissions | Guard consulted / positive / cancelled | Calls; input tokens (M) |
| --- | --- | ---: | ---: | --- | ---: | ---: | ---: | --- | --- |
| Generation S | none | 5 | 0 | 26 / 5 / 19 | 0 | 8 | 6 | | 37; 3.4 |
| Generation S | static | 5 | 5 | 3 / 5 / 42 | 0 | 7 | 10 | | 34; 3.3 |
| Generation S | static-guard | 4 | 5 | 2 / 4 / 44 | 1 | 9 | 16 | 13 / 6 / 6 | 99; 5.9 |
| Reuse S+B | none | 5 | 0 | 31 / 5 / 14 | 0 | 7 | 10 | | 33; 4.8 |
| Reuse S+B | static | 4 | 5 | 2 / 3 / 45 | 0 | 9 | 15 | | 44; 7.0 |
| Reuse S+B | static-guard | 4 | 5 | 4 / 4 / 42 | 0 | 9 | 12 | 14 / 6 / 6 | 95; 8.1 |

## Against the predictions declared in the README

- Oversized line in at most 2 of 20 document artifacts: **not met**, 10
  of 20 (I30: 7 of 20). Blank name, retention bound and negative score in
  none: met.
- At least 6 full hits of 20 in the document arms: **not met**, 1 (I30:
  9). The million-record check is unresolved in 17 of the 20 document
  artifacts ("unsupported persistence encoding": the audit that
  establishes the precondition cannot write the artifacts' record
  formats) and fails in one; in I30 it was resolved in 16 of 20.
- Failed checks: none the most (26 and 31): met; static-guard within one
  of static per cell: met (2 against 3, 4 against 2); functional at least
  4 of 5: met (27 of 30).
- Guard: 12 cancellations in 10 trajectories, all with a verbatim quote
  and at most two citations; more than one per trajectory on average:
  **not met**.

## What the wording did

The v13 rule was written against the oversized-line check, and both
documents state a bound of 256 bytes per record. The artifacts apply it
after reading: they read a line with `readLine` and check its length,
so a 64 MiB line still fills the heap (7 of the 10 failures) or the
15-second budget (3). The rule says "bound each unit it reads … before it
is kept", which the artifacts satisfy literally; what the check needs is a
read that stops at the bound. At the same time the documents' bounded
record formats moved 17 of 20 artifacts to encodings the qualification
audit cannot generate a million records for, so the check that decides a
full hit is unresolved where I30's text formats had resolved it. The
v13 document arms fail 11 checks of 200 against 9 for v12; the difference
is inside the resource pair, everything else passes in every artifact of
both rounds.

## Reading

The third revision did not help: a rule can name a bound and still be
implemented as read-then-check, and a format chosen for bounded reading
can defeat the audit that establishes a precondition. The remaining
failures are not evidence against the document but against this
wording, and the audit's dependence on the record format is a limit of
the evaluation that I30 happened not to hit. The instruction line stops
here; the next question is transfer, not a fourth wording.

## Limits

N = 5 per cell and arm, two cells, one acquisition per method; the
million-record check depends on an audit that supports text formats only;
no significance claims.
