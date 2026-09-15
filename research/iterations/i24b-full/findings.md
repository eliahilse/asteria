# I24b findings: one-shot, strategy S2 (full data-flow document, anchored), N = 5

Fifty trajectories, five cells × fresh control / S2 insert × five, one
response and no feedback, strict delivery, reasoning effort high. The
insert is the full rendering of a fresh v10 data-flow acquisition per
method (Generation 37,609 characters, 25 statements; Reuse in
`contexts/`). Counts are of 5 (delivered, compiled, functional) and of 50
issue checks (failed / unresolved / passed); see the
[issue matrix](issue-matrix.md), the [delivery errors](delivery-errors.csv),
the [full hits](full-hits.md), the [hook audit](hook-audit.md) and the
[cost](cost.md).

| Cell | Arm | Delivered | Compiled | Functional | Issue checks failed / unresolved / passed of 50 | Bound per artifact |
| --- | --- | ---: | ---: | ---: | --- | --- |
| Generation None | none | 3 | 3 | 2 | 18 / 23 / 9 | |
| Generation None | S2 | 4 | 4 | 4 | 5 / 14 / 31 | −7.2 to +0.2 |
| Generation S | none | 1 | 1 | 1 | 7 / 41 / 2 | |
| Generation S | S2 | 5 | 5 | 3 | 5 / 5 / 40 | −8.6 to +0.6 |
| Generation S+F+B | none | 2 | 2 | 2 | 14 / 32 / 4 | |
| Generation S+F+B | S2 | 1 | 1 | 1 | 1 / 40 / 9 | −9.0 to +5.4 |
| Reuse F | none | 2 | 2 | 0 | 13 / 32 / 5 | |
| Reuse F | S2 | 2 | 2 | 2 | 2 / 31 / 17 | −8.6 to +4.0 |
| Reuse S+F+B | none | 4 | 4 | 4 | 25 / 12 / 13 | |
| Reuse S+F+B | S2 | 3 | 3 | 2 | 4 / 21 / 25 | −6.6 to 0.0 |

Totals of 25 per arm: delivered 12 (none) against 15 (S2); compiled 12
against 15; functional 9 against 12; issue checks failed 77 / unresolved
140 / passed 33 (none) against 17 / 111 / 122 (S2). Full hits (all sixteen
tests and all eleven security checks): 0 in every arm.

## Against the predictions declared in the README

Security, insert below control on failed checks in every cell where at
least two artifacts of each arm compile: met in the three such cells
(Generation None 5 against 18, Reuse F 2 against 13, Reuse S+F+B 4 against
25) and in the same direction in the other two (Generation S 5 against 7
with 5 against 1 compiled; S+F+B 1 against 14). No identification bound
excludes zero; Reuse S+F+B touches it (−6.6 to 0.0). Delivery and
compilation at or below the controls: **not met**. S2 delivered 15 against
12: above in Generation None (4 against 3) and Generation S (5 against 1),
equal in Reuse F, below in Generation S+F+B (1 against 2) and Reuse S+F+B
(3 against 4). The delivery cost seen for the inserts of I22 and I24a
(11 against 15 delivered) did not appear here.

## What remains after the insert

Of the 17 checks the S2 artifacts fail, 12 are the retention bound
(`boundsRetainedEntries`: 4, 3, 1, 1, 3 of the compiled artifacts per
cell). S2 asks for "a fixed maximum" of records without naming the
contract's one hundred, and the artifacts keep more. The five input-policy
checks fail 2 times in 75 resolved S2 outcomes (both `rejectsNegativeScore` in
Generation S) against 56 of 60 in the controls. The oversized-line
check fails 3 times in S2 artifacts (Generation None, Reuse F, Reuse
S+F+B) and 11 times in controls. The million-record check is unresolved
in 21 of 25 S2 outcomes and in 23 of 25 control outcomes (precondition), so
no arm reaches a full hit.

Rejections: 23 responses, 13 in the controls (8 returned a target file as
new, 3 omitted an integration edit, 2 anchors not found) and 10 in the S2
arms (4 anchors not found, 3 target files as new, 2 integration edits
missing, 1 path instead of a basename).

## Reading

The anchored full document keeps the security effect of the compact
inserts of the feedback rounds under one response: the compiled S2
artifacts fail 17 checks in 250 against 77 for the controls, and pass the
five input-policy checks in 73 of 75 resolved outcomes. Unlike S1 (I24a),
S2 did not reduce delivery in this round; at N = 5 per cell that
difference between the two strategies (11 and 15 delivered of 25 for the
inserts) is within what I23 and I25 showed one cell can move between
reruns. The retention bound is the one check the document leaves
unresolved by wording, and the million-record precondition is the reason
no one-response artifact is a full hit.

## Limits

One acquisition per method shared by five trajectories; N = 5; one
response; strict delivery; no significance claims.
