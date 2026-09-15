# I29 findings: the v11 document in agentic delivery, N = 5 in two cells

Thirty trajectories, Generation S and Reuse S+B, three agentic arms (24
tool turns, 5 submissions with compiler and test feedback, reasoning
effort high), a fresh data-flow document per method acquired under
protocol v11 (ground rule 7: bounds are concrete numbers kept by eviction,
value domains stated). No served-identity mismatch; no re-collection.
Qualified counts: functional of 5, issue checks failed / unresolved /
passed of 50, full hits of 5; see the [issue matrix](issue-matrix.md),
the [full hits](full-hits.md), the [cost](cost.md) and the
[hook audit](hook-audit.md).

| Cell | Arm | Functional | Issue checks f / u / p of 50 | Full hits | Tool turns (median) | Submissions | Guard consulted / positive / cancelled | Calls; input tokens (M) |
| --- | --- | ---: | --- | ---: | ---: | ---: | --- | --- |
| Generation S | none | 5 | 25 / 5 / 20 | 0 | 8 | 13 | | 39; 3.6 |
| Generation S | static | 5 | 5 / 5 / 40 | 0 | 8 | 9 | | 41; 4.1 |
| Generation S | static-guard | 4 | 5 / 4 / 41 | 0 | 7 | 14 | 9 / 3 / 3 | 75; 4.7 |
| Reuse S+B | none | 4 | 24 / 13 / 13 | 0 | 6.5 | 11 | | 38; 5.6 |
| Reuse S+B | static | 5 | 5 / 0 / 45 | 0 | 6 | 9 | | 35; 5.7 |
| Reuse S+B | static-guard | 5 | 5 / 0 / 45 | 0 | 9 | 8 | 11 / 5 / 5 | 76; 7.8 |

## Against the predictions declared in the README

- Retention bound in fewer than half of the 20 document artifacts and the
  negative score in none: met, both in 0 of 20 (I28: 28 of 40 and 3 of 40).
- At least one full hit in a document arm: **not met**, 0 of 20, for one
  check: every one of the 20 document artifacts accepts a blank name. The
  v11 Generation document asks for "a non-null player name" and the Reuse
  one for a name "bounded" in length; neither says blank. The v10
  documents said "null/blank/oversized". One word.
- Failed checks: none the most (25 and 24), static-guard at or below
  static (5 and 5 against 5 and 5), functional at least 4 of 5 in every
  arm: met.
- Guard: 8 cancellations in 10 trajectories, all with a verbatim quote,
  at most one per trajectory: met.

## What the document does

With the v11 document the ten issue checks fail in exactly 5 of 50 per
arm and cell, all five the blank-name check, and the other nine checks
pass in every resolved outcome: 40 of 40 in Generation (the
million-record check unresolved in 5 and 4 artifacts) and 45 of 45 in
Reuse, where the million-record check is resolved and passed in all ten
document artifacts. Against the v10 document of I28 in the same cells
(failed 6 and 6 of 50 for the insert arms, with the retention bound in 1
and 5 artifacts and the negative score in 2 and 0), the v11 wording moves
the failures from three checks to one and makes the Reuse artifacts
resolve the million-record check by writing a bounded record format.
The guard adds nothing here: its arms fail the same 5 checks, at 75 and
76 calls against 41 and 35, and its cancellations concern the run-end
integration (timing, elapsed-time source), not the blank name.

## Reading

The residual failures of this study are decided by the wording of the
document, not by delivery mode, judge or graph. Two revisions of one
ground rule move the failed checks of the insert arms in these cells
from 12 of 100 (v10, I28) to 10 of 100 (v11), and the ten that remain
are one omitted word. Round I30 runs the same arms with a v12 document
whose rule names the domain of a text (non-null, not blank after
trimming, a length bound); its prediction is that the blank-name check
fails in at most 2 of 20 document artifacts and that full hits appear.

## Limits

N = 5 per cell and arm, two cells, one acquisition per method; no
significance claims.
