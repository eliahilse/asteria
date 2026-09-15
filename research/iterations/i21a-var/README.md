# I21a: acquisition variance under protocol v10, second acquisition per method

Declared on 2026-09-15 after I20, before any I21a code call. Two cells, two
arms, three fresh trajectories each, **12 trajectories**. Complete the
schedule regardless of direction; nothing is regenerated. I21b is the same
design with a third acquisition per method; I20 holds the first.

## Question

Every round so far shares one acquisition across its trajectories, so the
insert's content and the protocol are confounded. Under one protocol (v10),
do independent acquisitions give the same direction and a similar size of
effect against their own fresh controls?

## Acquisitions

Fresh Codex acquisitions (gpt-5.6-luna, reasoning medium, data-flow angle,
same task text as I10), protocol `repository-security-context-v10-agent`,
collected after the I20 acquisitions and before this declaration:

| Method | Record | Items kept | Anchors matched | Compact insert |
| --- | --- | ---: | ---: | --- |
| Generation | agent-318ed121d6f1413a90b90d7822f34d9e | 20 | 84 | 8 statements, 13,448 characters |
| Reuse | agent-08dc5006d9534ce78a562d3503742ca7 | 14 | 73 | 6 statements, 11,807 characters |

No failure line instructs skipping the run record. Graphs and compact
renderings are in `contexts/`.

## Cell and arms

Generation S and Reuse S+B, single-shot delivery: fresh control (no security
context) and the compact insert of this acquisition. Model, settings,
five-submission budget, evaluator and test contracts as in I07 and I16.

## Predictions, stated before collection

- Direction: each insert arm below its fresh control on failed issue checks
  in both cells.
- Size: within the I20 range (Generation 39 → 11, Reuse 31 → 7 of 50),
  reported as counts of 30 here.
- Functionality: at least 2 of 3 functional per insert arm; no trajectory
  failing only the two null-name tests.

## Reporting

Counts of 3 per arm and of 30 issue checks per arm, first and within-budget
functional success, calls, identification bounds against the fresh control,
frozen amplification audit and qualifier after collection, hook audit. The
three acquisitions (I20, I21a, I21b) are compared side by side after I21b.

## Limits

N = 3 per arm gives direction only; one cell pair; no significance claims.
