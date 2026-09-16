# A04 findings: Achievements, Reuse S+B repeated, no context against the v13 full document, N = 5 each

Ten trajectories in the Reuse S+B cell only, agentic delivery (24 tool
turns, 5 submissions with feedback, `gpt-5.6-luna` at reasoning effort
high), a fresh control against the same v13 full document (S2) as A03c.
Declared to test the functional drop under the document seen in A03
(control 5 of 5 functional, S2 2 of 5). See the
[issue matrix](issue-matrix.md), the [full hits](full-hits.md) and the
[cost](cost.md).

| Arm | Compiled | Functional | Full hits | Issue checks f / u / p of 50 | Failed checks | Tool turns (median) | Submissions |
| --- | ---: | ---: | ---: | --- | --- | ---: | ---: |
| no context | 3 | 3 | 0 | 6 / 20 / 24 | overflow 3, oversized line 3 | 8 | 24 |
| S2 full document | 4 | 1 | 0 | 5 / 10 / 35 | overflow 3, oversized line 2 | 11 | 25 |

The prediction in the README is met by neither branch: the control fell
to 3 of 5 functional (4 or 5 were predicted if the drop were the
document's), and the arms are two apart, not within one. The direction
repeats, however: the document arm is again below the control. Pooled
over A03 and A04 (N = 10 per arm in Reuse S+B), the control is functional
in 8 of 10 trajectories and the full document in 3 of 10; no Reuse
trajectory of either arm passes every test and check in either round
(0 of 20). Two control and one document artifact of this round fail the
security-suite compilation at the last submission, which no Reuse
trajectory of A03 did.

On the checks the document's effect is smaller here than in A03: among
compiled artifacts the control fails 6 of 30 checks and the document
5 of 40, and the document fails the overflow check in 3 of 4 compiled
artifacts (A03c: 2 of 5) although it names the overflow bound. Pooled
over both rounds, compiled control artifacts fail 17 of 80 checks and
compiled document artifacts 10 of 90; the overflow check fails 8 of 8
against 5 of 9 and the oversized line 8 of 8 against 5 of 9. All other
checks pass in every compiled artifact of both arms in both rounds.

Input tokens 7.0 M against 8.9 M for five trajectories (1.4 against 1.8 M
per artifact), 19 against 20 minutes of trajectory time.

The round changes no number of the paper's tables (it is not part of the
pooled groups); its reading is that Reuse S+B is the hard cell of the
second task for both arms and that the document's functional cost there
is consistent in direction across two draws at N = 5, not established.
N = 5 per arm; no significance claims.
