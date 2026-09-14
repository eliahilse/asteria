# I16: compact agent-acquired insert, confirmation round

Declared on 2026-09-15 after I15, before any I16 code call. Two cells, two
arms, five fresh trajectories each, **20 trajectories**. Complete the schedule
regardless of direction; nothing is regenerated.

## Question

Across the pilots I10 to I15, the compact rendering of the agent-acquired
data-flow graph (requirement and control statements only, no assets,
boundaries, observations, risks or unknowns) delivered before the first
submission reached the security counts of the blocking sidecar policies while
keeping functional artifacts (I11: 2 of 3 functional, 3 failed checks of 30).
That observation rests on three trajectories in one cell. I16 tests it at the
scale of the main line, under the single-shot protocol of I05 to I09, in both
cells, against fresh controls.

## Cells and arms

Generation S and Reuse S+B. Arms: single-shot, no security context (fresh
control), and single-shot with the compact insert appended after the task. The
inserts are the compact renderings of the frozen I10 data-flow graphs
(acquisitions `agent-3b2861d7d0d041118bfbabf225ff37aa` for Generation and
`agent-2460818db48c4ece83361ab6c397ff8a` for Reuse); their byte hashes are in
the plan. No sidecar, no agentic tools, so requests are byte-comparable with
I07 and I09 apart from the insert. Model, settings, five-submission budget,
evaluator and test contracts as in I07.

## Reporting

Counts with fixed denominators (of 5 per arm, 50 issue checks per arm), first
and within-budget functional success, submissions and calls, identification
bounds against the fresh control, and the frozen amplification audit and
qualifier after collection. Comparison rows: I09 requirements (12 and 1 failed
of 50 with 5 of 5 functional) and I10 full static insert (5 and 4 failed of 50
with 2 and 4 of 5 functional).

## Limits

One acquisition per method shared by five trajectories; the compact insert
differs from I10's full insert in content and length together. Two cells,
N = 5 per arm, no significance claims.
