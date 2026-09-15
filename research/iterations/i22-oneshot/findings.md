# I22 findings: true one-shot delivery, protocol v10 inserts (N = 5)

Twenty trajectories with one model response each and no feedback, two cells
× fresh control / v10 compact insert (the I20 inserts, byte-identical) ×
five. Counts are of 5 (compiled, functional) and of 50 issue checks (failed
/ unresolved / passed); see the [issue matrix](issue-matrix.md), the
[delivery errors](delivery-errors.csv) and the [hook audit](hook-audit.md).

| Cell | Arm | Delivered | Compiled | Functional | Functional checks passed of 80 | Issue checks failed / unresolved / passed of 50 |
| --- | --- | ---: | ---: | ---: | ---: | --- |
| Generation S | none | 5 | 5 | 3 | 23 | 37 / 0 / 13 |
| Generation S | v10 compact insert | 2 | 2 | 0 | 19 | 3 / 32 / 15 |
| Reuse S+B | none | 2 | 1 | 1 | 0 | 6 / 41 / 3 |
| Reuse S+B | v10 compact insert | 3 | 2 | 1 | 12 | 3 / 30 / 17 |

"Delivered" counts responses the harness accepted as a valid submission;
the others were rejected before compilation.

## Against the predictions declared in the README

Functional (control prediction met, insert prediction met): controls 3 of 5
(Generation) and 1 of 5 (Reuse) functional on their single response; insert
arms 0 of 5 and 1 of 5. Security (direction met where measurable, bounds
wide): Generation 37 → 3 failed of 50 with 32 unresolved under the insert,
identification bound −6.8 to −0.4 per artifact; Reuse 6 → 3 with 41 and 30
unresolved, bound −8.8 to +5.4, no direction. The unresolved checks come
from artifacts that did not compile or were never delivered (ten checks
each).

## What one response without feedback shows

Eight of the twenty responses were rejected by the delivery protocol before
compilation, in every arm except the Generation control: four listed
`ApoMarioHighscore.java` twice in `new_files` (the harness rejects a second
file of the same name), three used an `old_text` anchor that does not occur
once in the target file, and one omitted the required edit to the menu.
In the five-submission rounds the same slips occur on first submissions
(I17: 4 of 5 insert first submissions) and are repaired on the next
submission; with one response they are final. The insert arms slip more
often than the controls (5 of 10 against 3 of 10), and the Reuse control
slips in 3 of 5.

Among the artifacts that compiled, the security effect is as in I20: the two
compiled Generation insert artifacts fail 3 of their 20 checks (the controls
37 of 50), the two compiled Reuse insert artifacts fail 3 of 20 (the one
compiled control fails 6 of 10). No compiled artifact loses the nameless
run.

## Reading

Under a one-response protocol with exact-edit delivery, the insert costs
delivery and compilation before it can pay in security: Generation 5 → 2
compiled, 3 → 0 functional. This is the trade the five-submission rounds
hide behind their 5 of 5 functional counts. It is also not the same
protocol as the prior study's: that pipeline extracts code blocks, places
files, repairs imports and packages, and then compiles, so its compile
rates tolerate slips this harness rejects. A comparable one-response
measurement needs either the same repair step or full-file delivery instead
of exact edits.

## Limits

One acquisition per method shared by five trajectories; N = 5; eight of
twenty responses unevaluated for security beyond "unresolved"; no
significance claims.
