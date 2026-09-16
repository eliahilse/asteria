# A03e findings: Achievements, agentic delivery with the v13 full document and the guard judge, N = 5 per cell

Ten trajectories, Generation S and Reuse S+B, agentic delivery (24 tool
turns, 5 submissions with feedback, `gpt-5.6-luna` at reasoning effort
high), the v13 full document appended after the task and the guard judge
v2 before every submission (a verbatim quote, at most two requirement or
control citations, once per statement, cap of three cancellations); the
control is A03a, the document alone A03c. See the
[issue matrix](issue-matrix.md), the [full hits](full-hits.md) and the
[cost](cost.md).

| Cell | Compiled | Functional | Full hits | Issue checks f / u / p of 50 | Guard consulted / cancelled | Tool turns (median) | Submissions |
| --- | ---: | ---: | ---: | --- | --- | ---: | ---: |
| Generation S | 2 | 1 (document alone 5) | 0 (5) | 1 / 33 / 21 | 15 / 5 | 11 | 24 |
| Reuse S+B | 4 | 3 (document alone 2) | 1 (0) | 5 / 11 / 39 | 19 / 5 | 14 | 25 |

The judge was consulted on 34 submissions, returned a positive verdict on
20, and cancelled 10 (one per trajectory; the other positive verdicts were
blocked by the rules: non-normative citations or a statement already
cancelled for), 9 of the 10 with a verbatim quote. Pooled, the judged arm
compiles 6 of 10 (document alone 10), is functional in 4 of 10 (7), and
reaches 1 full hit (5); its compiled artifacts pass 90 percent of the
checks (95), at 2.0 M input tokens and 4.5 minutes per trajectory (1.5 M
and 3.4). Four trajectories never reach a compiling submission within
the budget after a cancellation. On the harder task the cancelling judge
costs functionality and does not add security beyond the document; in
Reuse S+B it coincides with one more functional artifact than the
document alone, within the noise of five.

N = 5 per cell; no significance claims.
