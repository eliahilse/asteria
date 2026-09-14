# I10 findings: single-shot versus agentic delivery with a security-context sidecar

I10 completed all **50 trajectories** (two cells × five arms × five), **38 full
functional** within five submissions, no adapter or evaluation errors. The
context in every sidecar arm is the agent-acquired data-flow graph of the
method (31,141 and 30,756 characters as a static insert), the largest insert
used in any round. Every issue check below is failed / unresolved / passed out
of 5 per check and 50 per arm ([issue matrix](issue-matrix.md)); functional
counts are out of 5.

| Cell | Arm | First full | Within five | Tool turns per trajectory | Issue checks failed / unresolved / passed of 50 |
| --- | --- | ---: | ---: | --- | --- |
| Generation S | single-shot, none | 3 | 5 | 2, 1, 1, 2, 1 | 33 / 4 / 13 |
| Generation S | single-shot, static | 0 | 2 | 5, 5, 5, 4, 4 | 5 / 5 / 40 |
| Generation S | agentic, none | 0 | 5 | 5, 9, 10, 9, 7 | 32 / 1 / 17 |
| Generation S | agentic, static | 0 | 1 | 5, 13, 10, 5, 5 | 3 / 5 / 42 |
| Generation S | agentic, adaptive | 0 | 3 | 5, 7, 14, 10, 9 | 8 / 4 / 38 |
| Reuse S+B | single-shot, none | 4 | 5 | 2, 1, 1, 1, 1 | 35 / 0 / 15 |
| Reuse S+B | single-shot, static | 2 | 4 | 1, 5, 2, 4, 1 | 4 / 4 / 42 |
| Reuse S+B | agentic, none | 2 | 5 | 5, 4, 9, 8, 10 | 35 / 1 / 14 |
| Reuse S+B | agentic, static | 1 | 4 | 12, 6, 12, 8, 5 | 10 / 4 / 36 |
| Reuse S+B | agentic, adaptive | 0 | 4 | 8, 6, 7, 9, 8 | 23 / 11 / 16 |

## Repository access alone changes nothing in the counts

Agentic delivery without a sidecar reaches the same functional count as
single-shot (5 of 5 in both cells) with the same failed issue checks within one
(32 versus 33; 35 versus 35), at three to five times the tool turns and with
fewer first-submission successes (0 versus 3; 2 versus 4). The generator uses
search and read before its first submission; what it reads does not change what
it guards.

## The static graph insert is the strongest context so far, and the most costly

With the static insert, failed issue checks drop from 33 to 5 (single-shot) and
3 (agentic) in Generation S, and from 35 to 4 and 10 in Reuse S+B; every
input-policy check is passed by all five Generation single-shot artifacts, and
the oversized-line fixture is passed by all five in three of the four static
arms. The per-trajectory identification bounds are −6.4 to −4.6, −6.8 to −5.0,
−6.2 to −5.4 and −5.0 to −4.2 failed checks. The requirements insert of I09
reached 12 and 1 failures in the same cells with 7,174 and 9,475 characters.

Functionality pays for it in Generation S: 2 of 5 (single-shot) and 1 of 5
(agentic) reach full functionality, against 5 of 5 without the insert, and no
static-insert trajectory succeeds on the first submission. Reuse S+B loses one
trajectory in each static arm. The unresolved checks in the static arms are all
large-record outcomes whose precondition the audit could not establish (five,
five, four and four); no static-arm artifact failed to compile.

## The file-level adaptive sidecar delivers the static insert late

The adaptive sidecar injected in every adaptive trajectory: one to three times
in Generation S (91 statement ids, 123,847 characters over five trajectories)
and once in Reuse S+B (80 ids, 111,065 characters). The first injection came at
turn 2 to 4, after searches and reads that touch no file and at the first
submission, whose edits touch the three target files where almost every
statement is anchored. Each injection therefore carried 16 to 19 of the 18 to
19 statements: the static insert, delivered after the first attempt.

The outcome follows that timing. Generation S adaptive has 8 failed checks and
3 of 5 full functional, between the static (3 and 5; 1 and 2 of 5) and none
(32; 5 of 5) arms. Reuse S+B adaptive has 23 failed and 11 unresolved: one
artifact does not compile (10 unresolved), and the first submissions of three
others, made before any injection, already fixed the acceptance behaviour that
later submissions did not revisit (3 of 5 accept negative scores, times and
names). Reads are rare in this cell (six over five trajectories), so the
sidecar had nothing to react to before the first submission.

## What follows for the next round

1. Slicing at file granularity does not select; the next sidecar anchors on the
   edited method ranges (from each edit's old text) and the read ranges, and
   injects only statements anchored at those symbols.
2. The static insert should be shortened to its requirement and control
   statements with enforcement points; the data-flow narrative, assets and
   boundaries are reference material the generator does not need in the prompt.
3. Both are pilots at small N first.

## Limits

Two cells, five trajectories per arm, one acquisition per method shared by 25
trajectories. Mode and sidecar change information access, prompt length and
interaction length together. Tool-turn counts include rejected submissions.
Joint functional-plus-all-security artifacts: none. No significance claims.
Review the [qualified analysis](qualified-analysis.md), [delivery summary](delivery-summary.json),
[category table](qualified-categories.md), [workbook](experiment_results_report.xlsx)
and [figures](figures-qualified/quality-and-security.pdf).
