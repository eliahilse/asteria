# A03d findings: Achievements, agentic delivery with the v13 generic document (S3, no repository), N = 5 per cell

Ten trajectories, Generation S and Reuse S+B, agentic delivery (24 tool
turns, 5 submissions with feedback, `gpt-5.6-luna` at reasoning effort
high), the v13 generic document written for the Achievements task without
any repository appended after the task; the control is A03a. See the
[issue matrix](issue-matrix.md), the [full hits](full-hits.md) and the
[cost](cost.md).

| Cell | Compiled | Functional | Full hits | Issue checks f / u / p of 50 | Failed checks | Tool turns (median) | Submissions |
| --- | ---: | ---: | ---: | --- | --- | ---: | ---: |
| Generation S | 4 | 3 (control 2) | 1 | 4 / 11 / 40 (control 9 / 10 / 31) | overflow 2, oversized line 2 | 10 | 16 |
| Reuse S+B | 3 | 2 (control 5) | 2 | 0 / 22 / 33 (control 11 / 0 / 39) | none | 11 | 26 |

Compiled artifacts with the generic document pass 94 percent of the
checks (control 78, high-level 96, full 95); 7 of 10 compile, 5 of 10 are
functional and 3 of 10 pass every test and every check (control 0, S1 3,
S2 5). The document written without seeing the repository carries the
security effect of the other two for this task as well; its Reuse cell
loses three functional artifacts against the control, as the full
document's did.

N = 5 per cell; no significance claims.
