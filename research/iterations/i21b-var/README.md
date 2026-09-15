# I21b: acquisition variance under protocol v10, third acquisition per method

Declared on 2026-09-15 after I20, before any I21b code call. Two cells, two
arms, three fresh trajectories each, **12 trajectories**. Complete the
schedule regardless of direction; nothing is regenerated. Same design and
predictions as I21a with the third acquisition per method; I20 holds the
first, I21a the second.

## Acquisitions

Fresh Codex acquisitions (gpt-5.6-luna, reasoning medium, data-flow angle,
same task text as I10), protocol `repository-security-context-v10-agent`,
collected after the I20 acquisitions and before this declaration:

| Method | Record | Items kept | Anchors matched | Compact insert |
| --- | --- | ---: | ---: | --- |
| Generation | agent-ce94112ab1ca438ca54b7a60bc77de33 | 25 | 84 | 6 statements, 10,498 characters |
| Reuse | agent-6d5c6ea5258742f99b2b3a1146d181bb | 13 | 50 | 6 statements, 11,475 characters |

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
frozen amplification audit and qualifier after collection, hook audit; then
the three-acquisition comparison.

## Limits

N = 3 per arm gives direction only; one cell pair; no significance claims.
