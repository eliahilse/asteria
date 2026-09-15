# I24C GENERIC: one-shot security context, strategy S3: generic context without any repository (language and kind of program only)

Declared on 2026-09-15 after I23, before any code call of this round. Five
cells (Generation None, Generation S, Generation S+F+B, Reuse F, Reuse
S+F+B: the cells with the most functional artifacts on one response in I23,
plus the Generation cell without base context), two arms (fresh control
without security context, static insert), five trajectories each, **50
trajectories**. Complete the schedule regardless of direction; nothing is
regenerated. Rounds I24a, I24b and I24c share this design and differ only
in the insert; each has its own fresh controls.

## Protocol

One response, no feedback (`--max-submissions 1 --max-turns 1`), exact-edit
delivery as in I22 and I23 (a rejected delivery counts as not compiled),
reasoning effort high, evaluator and tests as in I07.

## Insert

S3: generic context without any repository (language and kind of program only). Rendered in full (assets, trust boundaries, then every statement):
Generation 12 statements, 7,613 characters (`contexts/generation-generic-full.txt`);
Reuse 11 statements, 6,231 characters (`contexts/reuse-generic-full.txt`).
Acquisition records are named in `contexts/*.record-id.txt`.

## Predictions, stated before collection

- Security: the insert arm below its control on failed checks in every cell
  where at least two artifacts of each arm compile; the size ordering
  S2 > S1 > S3 is expected but not assumed.
- Delivery and compilation: the insert arms at or below the controls, as in
  I22 (the insert costs delivery under one response).

## Reporting

Counts of 5 per arm: delivered, compiled, functional; issue checks failed /
unresolved / passed of 50; identification bounds against the fresh control;
hook audit.

## Limits

One acquisition per method shared by five trajectories; one response; N = 5;
no significance claims.
