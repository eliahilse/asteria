# A03a findings: Achievements, agentic delivery without security context (control), N = 5 per cell

Ten trajectories, Generation S and Reuse S+B, agentic delivery (24 tool
turns, 5 submissions with compiler and test feedback, `gpt-5.6-luna` at
reasoning effort high), no security context; the control arm of A03b to
A03e. Qualified counts (no amplification audit exists for this task): see
the [issue matrix](issue-matrix.md), the [full hits](full-hits.md) and the
[cost](cost.md).

| Cell | Functional | Issue checks f / u / p of 50 | Most failed checks | Tool turns (median) | Submissions |
| --- | ---: | --- | --- | ---: | ---: |
| Generation S | 2 | 9 / 10 / 31 | overflow 4, oversized line 4, negative points 1 (one artifact does not compile: 10 unresolved) | 10 | 13 |
| Reuse S+B | 5 | 11 / 0 / 39 | overflow 5, oversized line 5, negative points 1 | 10 | 22 |

With repository tools and feedback the harder task compiles in 9 of 10
trajectories and is functional in 7 of 10 (Generation S 2 of 5, Reuse S+B
5 of 5); under one response (A02) nothing compiled. Compiled control
artifacts pass 78 percent of the ten security checks: every one fails the
points overflow and the oversized line, two fail the negative-points
check, and all pass the id, duplicate, malformed-store, deserialization
and million-line checks. This is the profile a security document has to
change for Achievements: bounds on arithmetic and on reads, not input
policy on ids. Cost: 1.4 M input tokens and 3.6 minutes per trajectory,
about 1.5 times the Highscore control.

N = 5 per cell, one cell pair; no significance claims.
