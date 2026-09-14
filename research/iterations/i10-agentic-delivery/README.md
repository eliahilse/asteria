# I10: single-shot versus agentic delivery with a security-context sidecar

Declared on 2026-09-14 before any I10 acquisition or code call. Complete the
schedule regardless of direction; no arm, cell or trajectory is selected for
omission; failed acquisitions and failed trajectories are preserved and counted.

## Questions

1. Does letting the generator search and read the repository (agentic
   delivery) change functional success or failed issue checks against the
   single-shot protocol of I05–I09, at equal submission budget?
2. Does security context delivered adaptively, in proportion to the code the
   generator touches, reach the effect of the full static insert at lower
   injected volume?

## Context

One acquisition per method by the [security context agent](../../CONTEXT_AGENT.md)
under the data-flow angle, stored as a [context graph](../../context_graph.py).
The graph renders the static insert and yields adaptive slices. If the
acquisition for a method fails or inspects nothing, it is used as is and
reported; nothing is regenerated.

## Cells and arms

Generation S and Reuse S+B (the I09 cells), each with six arms and five fresh
trajectories: **60 trajectories**.

| Mode | Sidecar | Prompt and tools |
| --- | --- | --- |
| Single-shot | none | I07 control prompt bytes; `submit_feature_changes` only |
| Single-shot | static | control prompt + graph insert; `submit_feature_changes` only |
| Agentic | none | control prompt; `act` tool with search, read, submit |
| Agentic | static | control prompt + graph insert; `act` tool |
| Agentic | adaptive | control prompt; `act` tool; graph slices injected after reads and before submissions, each statement at most once |
| Single-shot | adaptive | not defined: single-shot has no reads; the arm is omitted, leaving five arms |

The schedule therefore has **50 trajectories** (two cells × five arms × five).
Submission cap five in every arm; agentic arms have a total tool-turn cap of
24. Model, settings, evaluator and test contracts are those of I07. Security
outcomes never enter feedback in any arm.

## Reporting

Counts with fixed denominators per [`docs/REPORTING.md`](../../../docs/REPORTING.md):
full functional within budget and on first submission (of 5), issue checks
failed / unresolved / passed (of 50), tool turns, reads, searches, submissions,
and for adaptive arms the number of injected statements and characters per
trajectory against the static insert's size. Apply the frozen large-record
qualification audit after collection.

## Limits

One acquisition per method is shared by 25 trajectories. Agentic arms change
both information access and interaction length; the design does not isolate
which of the two drives a difference. Two cells, N = 5 per arm, no significance
claims. The adaptive sidecar's slices depend on the code model's name-based
call resolution.

## Collection notes, 2026-09-14

Collection started after the two data-flow acquisitions were frozen (see
`acquisitions.md`; static inserts of 31,141 and 30,756 characters). In the
first adaptive trajectories the sidecar injected nothing after searches (no
file is touched by a search), nothing after reads in most cases, and then 16–18
of the 18–19 statements at the first submission, because the submitted edits
touch the three target files where almost every statement is anchored. At
file granularity the adaptive arm therefore delivers most of the static insert
after the first attempt. Symbol-level slicing (anchoring on the edited method
ranges instead of files) is the planned refinement for a later round; the
frozen I10 code is not changed during collection.
