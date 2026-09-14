# I09 findings: generic acquisition instructions

I09 completed all **40 trajectories** on Generation S and Reuse S+B, with
**40 full-functional results** within five submissions, 16 on the first
submission, and 80 code calls. Four new acquisitions were made from the
repository and task alone: a one-sentence **task-only** instruction and the same
sentence followed by the **2025 CWE Top 25** catalog. Fresh no-security controls
and the fixed I04 **requirements** insert (unchanged I07 prompt bytes) were run
alongside. Every issue check below uses the fixed denominator of the
[issue matrix](issue-matrix.md): failed / unresolved / passed out of 5 per check
and 50 per condition.

| Cell | Arm | First full | Within five | Issue checks failed / unresolved / passed of 50 | Joint functional + all 11 |
| --- | --- | ---: | ---: | --- | ---: |
| Generation S | None | 2/5 | 5/5 | 38 / 0 / 12 | 0 |
| Generation S | Requirements | 1/5 | 5/5 | 12 / 2 / 36 | 0 |
| Generation S | Task only | 2/5 | 5/5 | 15 / 3 / 32 | 0 |
| Generation S | CWE catalog | 4/5 | 5/5 | 25 / 3 / 22 | 0 |
| Reuse S+B | None | 2/5 | 5/5 | 31 / 2 / 17 | 0 |
| Reuse S+B | Requirements | 1/5 | 5/5 | 1 / 3 / 46 | 2 |
| Reuse S+B | Task only | 1/5 | 5/5 | 16 / 1 / 33 | 0 |
| Reuse S+B | CWE catalog | 3/5 | 5/5 | 13 / 2 / 35 | 0 |

All six context arms have fewer failed issue checks than their fresh control
under every assignment of unresolved outcomes ([qualified analysis](qualified-analysis.md)):
the per-trajectory bounds are −5.2 to −4.8 (requirements), −4.6 to −4.0
(task only) and −2.6 to −2.0 (catalog) in Generation S, and −6.4 to −5.4,
−3.4 to −2.8 and −4.0 to −3.2 in Reuse S+B. These are identification bounds,
not confidence intervals. The requirements arm remains the strongest in both
cells and produced the round's only two joint functional-plus-all-security
artifacts. Unresolved outcomes are all large-record checks whose precondition
the qualification audit did not establish (16 of 400).

## What the generic instructions changed

A single sentence with no security perspective still yields inserts associated
with 23 and 15 fewer failed checks than the controls over five trajectories.
The [inserts](contexts/) show why: both task-only acquisitions inspected the
repository (24 and 7 files) and derived validation, bounded-storage and
bounded-reading recommendations for the Highscore store on their own. Neither
insert names a check, fixture or threshold.

The two catalog acquisitions differ sharply. The Reuse catalog acquisition
inspected 22 files and cited 36 excerpts; its arm has 18 fewer failures than its
control. The Generation catalog acquisition finished after six turns without any
inspected excerpt: its literal searches used regular-expression syntax and its
read paths omitted the `ApoMario.jar!/` prefix, so every tool call returned
nothing, and it emitted seven uncited, task-derived recommendations. The
protocol retains it unchanged. Its arm still has 13 fewer failures than the
control, all in the retention and resource checks, while every input-policy
check fails exactly as in the control. That split is the clearest single
observation of the round: catalog vocabulary without repository evidence moved
bounds and reads, not input acceptance rules.

| Check | Gen. none | Gen. req. | Gen. task | Gen. catalog | Reuse none | Reuse req. | Reuse task | Reuse catalog |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| rejectsNegativeScore | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 4 / 0 / 1 | 2 / 0 / 3 |
| rejectsNegativeTime | 5 / 0 / 0 | 0 / 0 / 5 | 1 / 0 / 4 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 1 / 0 / 4 | 1 / 0 / 4 |
| rejectsNullName | 5 / 0 / 0 | 0 / 0 / 5 | 1 / 0 / 4 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 2 / 0 / 3 | 1 / 0 / 4 |
| rejectsBlankName | 5 / 0 / 0 | 0 / 0 / 5 | 1 / 0 / 4 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 2 / 0 / 3 | 1 / 0 / 4 |
| rejectsExcessiveName | 5 / 0 / 0 | 4 / 0 / 1 | 2 / 0 / 3 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 2 / 0 / 3 | 3 / 0 / 2 |
| boundsRetainedEntries | 3 / 0 / 2 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |
| malformedStoreDoesNotCrash | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |
| oversizedPhysicalLine | 5 / 0 / 0 | 3 / 0 / 2 | 5 / 0 / 0 | 0 / 0 / 5 | 3 / 0 / 2 | 1 / 0 / 4 | 5 / 0 / 0 | 5 / 0 / 0 |
| nativeDeserializationCanary | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |
| largePersistedRecordSet | 5 / 0 / 0 | 0 / 2 / 3 | 0 / 3 / 2 | 0 / 3 / 2 | 3 / 2 / 0 | 0 / 3 / 2 | 0 / 1 / 4 | 0 / 2 / 3 |

Negative scores stay the blind spot seen in I07: only the Reuse requirements
insert, which says "reject negative scores", leads to rejection in every
artifact. The oversized-line fixture is the one check where generic arms are
worse than their control: both Reuse generic arms fail it in all five
artifacts against three control failures, and the Generation task-only arm
fails all five. The Generation catalog arm passes all five, the only arm with a
perfect result on this check. Malformed-store and deserialization checks pass
everywhere, as in I07.

## Cost and repair

Catalog inserts are the shortest (5,871 and 8,801 characters) and their arms
have the most first-submission successes (4/5 and 3/5); requirements arms have
the fewest (1/5 each) and use the most calls. Every trajectory reached full
functionality within the budget, against 74 of 80 in I07; the two cells chosen
here were I07's best functional cells, so this is not evidence of easier repair
in general.

## Collection

The three-worker runner was stopped by the host for low memory after 26
completed trajectories. The interrupted attempts and the failed re-collection
attempts (a local trace-directory collision, not a provider failure) are
preserved and listed in [`interruptions.json`](interruptions.json); none is
counted. The remaining 14 trajectories were collected one at a time with fresh
conversations under reused request identifiers. See the
[protocol page](README.md) for the full note.

## Limits

One acquisition per method and strategy is shared by five trajectories; the
Generation catalog result in particular rests on a single acquisition that did
not inspect the repository, so the observed split between resource and
input-policy outcomes is one instance, not a property of catalog prompting. Two
cells, five trajectories each, no significance claims. Instruction, generated
content and insert length vary together. Security outcomes never entered code
generation. Provider model identity matched; effective settings remain
unattested. Review the [qualified table](qualified-analysis.md),
[issue matrix](issue-matrix.md), [report workbook](experiment_results_report.xlsx),
[detailed workbook](qualified-results.xlsx),
[category table](qualified-categories.md) and
[figures](figures-qualified/quality-and-security.pdf).
