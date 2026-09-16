# A03c findings: Achievements, agentic delivery with the v13 full document (S2), N = 5 per cell

Ten trajectories, Generation S and Reuse S+B, agentic delivery (24 tool
turns, 5 submissions with feedback, `gpt-5.6-luna` at reasoning effort
high), the v13 full data-flow document written for the Achievements task
appended after the task; the control is A03a. See the
[issue matrix](issue-matrix.md), the [full hits](full-hits.md) and the
[cost](cost.md).

| Cell | Functional | Full hits | Issue checks f / u / p of 50 | Failed checks | Tool turns (median) | Submissions |
| --- | ---: | ---: | --- | --- | ---: | ---: |
| Generation S | 5 (control 2) | **5** | 0 / 0 / 50 (control 9 / 10 / 31) | none | 11 | 21 |
| Reuse S+B | 2 (control 5) | 0 | 5 / 0 / 45 (control 11 / 0 / 39) | overflow 2, oversized line 3 | 11 | 23 |

In Generation S every artifact passes all 28 tests and all eleven
security checks: five full hits of five, where the control had none and
failed the overflow and oversized-line checks in every artifact. In Reuse
S+B the document removes the negative-points failures and half of the
overflow and line failures (5 against 11) but three of five artifacts are
not functional within five submissions (control 5 of 5); the arm uses 23
submissions against the control's 22, so the loss is not for want of
attempts. Pooled, the document arm compiles 10 of 10 (control 9),
passes 96 percent of the tests per compiled artifact (95) and 95 percent
of the security checks (78), and has 5 full hits of 10 (0).

## Reading

The document that says how to bound arithmetic and reads (exact ids, at
most three records, byte bounds per record, non-negative values,
overflow) is followed to the letter in Generation; in Reuse, where the
model adapts ApoIcarus's implementation, the same document coincides
with three functional failures. Whether that is a cost of the document
or the draw is a question for the judge arm (A03e) and for more
trajectories.

N = 5 per cell; no significance claims.
