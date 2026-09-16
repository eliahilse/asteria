# A03b findings: Achievements, agentic delivery with the v13 high-level document (S1), N = 5 per cell

Ten trajectories, Generation S and Reuse S+B, agentic delivery (24 tool
turns, 5 submissions with feedback, `gpt-5.6-luna` at reasoning effort
high), the v13 high-level document written for the Achievements task
(general rules, no code citation) appended after the task; the control is
A03a. See the [issue matrix](issue-matrix.md), the [full hits](full-hits.md)
and the [cost](cost.md).

| Cell | Compiled | Functional | Full hits | Issue checks f / u / p of 50 | Failed checks | Tool turns (median) | Submissions |
| --- | ---: | ---: | ---: | --- | --- | ---: | ---: |
| Generation S | 4 | 3 (control 2) | 2 | 1 / 11 / 43 (control 9 / 10 / 31) | oversized line 1 | 10 | 14 |
| Reuse S+B | 3 | 3 (control 5) | 1 | 2 / 22 / 31 (control 11 / 0 / 39) | oversized line 2 | 10 | 17 |

Compiled artifacts with the high-level document pass 96 percent of the
checks (control 78): the overflow and negative-points failures of the
control are gone and only the oversized-line check still fails, in three
of seven compiled artifacts. Three trajectories do not reach a compiling
submission (control one), and functional artifacts are 6 of 10 against
the control's 7 and the full document's 7; full hits 3 of 10 (control 0,
full document 5). The document without code citations carries most of
the security effect of the anchored one at the same cost (1.4 M input
tokens, 3.4 minutes per trajectory).

N = 5 per cell; no significance claims.
