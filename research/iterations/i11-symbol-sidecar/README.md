# I11: compact insert and symbol-level sidecar (pilot)

Declared on 2026-09-15 after I10, before any I11 code call. A small pilot:
one cell, three arms, three fresh trajectories each, **9 trajectories**.
Complete the schedule regardless of direction; nothing is regenerated.

## Question

I10 showed that the full data-flow graph insert (31,141 characters) gives the
fewest failed checks of any round and, in Generation S, the fewest functional
trajectories, and that file-level adaptive slicing delivers almost the whole
insert at the first submission. Two changes are tested here:

1. A **compact static insert**: the same Generation data-flow graph rendered as
   requirement and control statements only, without assets, boundaries,
   properties, risks, verification and unknowns.
2. A **symbol-level adaptive sidecar**: slices anchored on the line ranges the
   generator read or edited (the old text of each edit located in the pre-edit
   source), no call-edge hops, requirement and control statements only, each
   statement at most once.

## Cell and arms

Generation S only. Fresh single-shot control (none), single-shot with the
compact static insert, agentic with the symbol-level adaptive sidecar. The
context graph is the frozen I10 Generation data-flow graph (same acquisition,
`agent-3b2861d7d0d041118bfbabf225ff37aa`); no new acquisition. Delivery
protocol, budgets (five submissions, 24 tool turns), evaluator and test
contracts are those of I10. Security outcomes never enter feedback.

## Reporting

Counts with fixed denominators (of 3 per arm, 30 issue checks per arm), tool
turns, and for the adaptive arm the statements and characters injected per
trajectory against the compact insert's size. Sidecar configuration
(`describe()`) is recorded in every record.

## Limits

Three trajectories per arm establish feasibility and direction only. The
compact insert and the symbol sidecar change content and timing together
relative to I10. One cell.
