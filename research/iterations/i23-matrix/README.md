# I23: the prior study's Highscore matrix rerun with Luna, one response, reasoning effort high

Declared on 2026-09-15 after I22, before any I23 code call. Sixteen cells
(Generation and Reuse × the eight base-context combinations None, S, F, B,
S+F, S+B, F+B, S+F+B), one arm each (no security context), five fresh
trajectories per cell, **80 trajectories**. Complete the schedule regardless
of direction; nothing is regenerated.

## Why

The prior study (<https://github.com/ieiris/llm-context-generation-reuse>)
ran this matrix with Gemini 3.1 Flash Lite, one response per prompt, and
reported compilation and test pass rates. Our security rounds used four of
the sixteen cells. This round gives the whole matrix under our generator
and protocol, so the choice of cells and the baseline the security rounds
build on can be shown against their numbers.

## Protocol

Single-shot delivery with one submission and one turn (no feedback), the
same prompts as the paper matrix (task, attached base-context files), exact
edit delivery as in every round. Generator settings for this round:
reasoning effort **high** (all earlier rounds used medium; the setting is
declared per round from this round on and recorded in the manifest),
temperature at the provider default, 65,536 output tokens. Delivery
rejections (duplicate new file, non-matching anchor, missing menu edit) are
counted as not compiled, as in I22.

## Predictions, stated before collection

- Compiled: above the prior study's Highscore rates (Generation 20 to 80
  percent per cell, Reuse 0 to 80 percent) in most cells, given I22's
  Generation control (5 of 5 compiled at medium effort); Reuse below
  Generation.
- Functional (all sixteen tests) on the single response: between 1 and 4
  of 5 in Generation cells, lower in Reuse.
- Security checks are run and reported for every artifact but no direction
  is predicted; all arms are controls.

## Reporting

Counts of 5 per cell: delivered, compiled, functional; functional checks
passed of 80; issue checks failed / unresolved / passed of 50; the
sixteen-cell table beside the prior study's compile and pass rates.

## Limits

N = 5 per cell; one response; a different model, protocol and delivery
format from the prior study, so rates are compared, not pooled; no
significance claims.
