# I30 findings: the v12 document in agentic delivery, N = 5 in two cells

Thirty trajectories, Generation S and Reuse S+B, three agentic arms (24
tool turns, 5 submissions with compiler and test feedback, reasoning
effort high), a fresh data-flow document per method acquired under
protocol v12 (ground rule 7 as in v11, plus: the domain of a text names
presence, blankness after trimming and a length bound). No
served-identity mismatch. Qualified counts: functional of 5, issue checks
failed / unresolved / passed of 50, input-policy clean of 5, full hits
(all sixteen tests and all eleven security checks) of 5; see the
[issue matrix](issue-matrix.md), the [full hits](full-hits.md), the
[cost](cost.md) and the [hook audit](hook-audit.md).

| Cell | Arm | Functional | Input-policy clean | Issue checks f / u / p of 50 | Full hits | Tool turns (median) | Submissions | Guard consulted / positive / cancelled | Calls; input tokens (M) |
| --- | --- | ---: | ---: | --- | ---: | ---: | ---: | --- | --- |
| Generation S | none | 5 | 0 | 21 / 5 / 24 | 0 | 10 | 11 | | 47; 4.4 |
| Generation S | static | 5 | 5 | 3 / 1 / 46 | **2** | 6 | 10 | | 32; 3.2 |
| Generation S | static-guard | 5 | 5 | 5 / 1 / 44 | **1** | 11 | 12 | 7 / 4 / 2 | 78; 5.5 |
| Reuse S+B | none | 5 | 0 | 31 / 5 / 14 | 0 | 8 | 7 | | 38; 5.6 |
| Reuse S+B | static | 5 | 5 | 1 / 2 / 47 | **2** | 7 | 8 | | 36; 5.5 |
| Reuse S+B | static-guard | 5 | 5 | 0 / 1 / 49 | **4** | 7 | 8 | 10 / 8 / 5 | 71; 5.4 |

## Against the predictions declared in the README

- Blank name in at most 2 of 20 document artifacts, retention bound and
  negative score in none: met, all three in 0 of 20 (I29: blank name 20
  of 20).
- At least two full hits in the static or the static-guard arm, none in
  the none arm: met; the document arms have 4 and 5 full hits of 10, the
  controls 0. The best count of any earlier arm in this study was 2 of 5.
- Failed checks: none the most (21 and 31): met. static-guard at or
  below static: met in Reuse (0 against 1), **not met** in Generation (5
  against 3). Functional at least 4 of 5 in every arm: met (30 of 30).
- Guard: 7 cancellations in 10 trajectories, every one with a verbatim
  quote and at most two citations: met.

## What the document does

The document arms pass the five input-policy checks in every artifact
(10 of 10 clean per arm), the retention bound in 20 of 20, and the
parser, deserialization and round-trip checks in 20 of 20. What remains
is the resource pair: the oversized-line check fails in 3, 3, 1 and 0 of 5
(the documents bound records and names but say nothing about the length
of a line), and the million-record check is unresolved in 1, 1, 2 and 1
artifacts and fails in 2 of the Generation guard arm. Against I29 (v11),
the failures of the document arms go from 20 of 200 (all blank name) to 9
of 200 (7 oversized lines, 2 million-record), and full hits from 0 to 9
of 20.

The guard: 17 consultations, 12 positive raw verdicts, 5 blocked by the
rules, 7 cancellations, all verbatim and within two citations. Its arm
has the best cell of the study (Reuse: 0 failed, 4 full hits of 5) and a
worse Generation cell than the document alone (5 against 3 failed, 1
against 2 full hits); summed over both cells it fails 5 checks against 4
and reaches 5 full hits against 4, at 149 calls against 68. The
cancellations still concern the run-end integration and the elapsed-time
source; none names a line bound.

## Reading

Three revisions of one ground rule (versions 10, 11, 12) take the
document arms of these two cells from 12 failed checks of 100 with no
artifact passing everything to 4 with 4 of 10 passing everything, with
every artifact functional throughout. Each revision removed the failures
its predecessor's wording had left open: the retention bound and the
score sign (v11), the blank name (v12). The judge, at twice the calls, is
within one artifact of the document alone in either direction. A rule
about the length of a line would address 7 of the 9 remaining failures;
whether it should be a rule, or the document should be asked to bound
every read it introduces, is the next question.

## Limits

N = 5 per cell and arm, two cells, one acquisition per method; the
instruction revisions were made after examining failures on this same
task, so they show the rule works here, not that it transfers; no
significance claims.
