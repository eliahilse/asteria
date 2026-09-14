# I16b: compact insert, Generation S re-collection

Declared on 2026-09-15 after I16, before any I16b code call. One cell, two
arms, five fresh trajectories each, **10 trajectories**. Complete the schedule
regardless of direction; nothing is regenerated.

## Why a second round

In I16 the Generation S compact-insert arm could not be measured: its run
identifiers were 66 characters long and the provider rejects identifiers above
64 in the field the adapter uses, so every request failed before a model
response. Those five trajectories stay in I16 as transport failures. I16b
collects the Generation S cell again under a shorter round name, with its own
fresh single-shot control, so the cell is compared within one round as
everywhere else. The Reuse S+B result of I16 stands.

## Cell and arms

Generation S, single-shot delivery: fresh control (no security context) and
the compact insert (the I16 Generation compact rendering, identical bytes,
11 statements, 17,051 characters). Model, settings, five-submission budget,
evaluator and test contracts as in I07. The freeze step now refuses plans
whose request identifiers could exceed the provider limit.

## Reporting

Counts of 5 per arm and of 50 issue checks per arm, first and within-budget
functional success, calls, identification bounds against the fresh control,
frozen amplification audit and qualifier after collection.

## Limits

One acquisition shared by five trajectories; one cell; N = 5; no significance
claims.
