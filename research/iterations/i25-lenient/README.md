# I25: the prior study's Highscore matrix under lenient delivery (one response, reasoning effort high)

Declared on 2026-09-15 after I23 and I24, before any code call of this
round. The same sixteen cells and protocol as I23 (one response, no
feedback, no security context, reasoning effort high, five trajectories per
cell, **80 trajectories**), with one change: delivery is **lenient**. A
returned file that names an existing file replaces it whole instead of
being rejected; edit anchors still have to match exactly, and the three
target files still have to differ from the originals. This is the closest
our harness comes to the prior study's pipeline, which writes every returned
file before compiling. I23 rejected 35 of 80 responses, 25 of them for
exactly this reason. Complete the schedule regardless of direction; nothing
is regenerated. Collected from a separate worktree because the delivery
modules are frozen for the rounds collecting in the main checkout.

## Predictions, stated before collection

- Delivered: above I23's 45 of 80; compiled and functional above I23 (18
  and 17 of 40 in Generation, 21 and 19 in Reuse) in most cells.
- The prior study's compile counts (17 and 13 of 40) below ours in both
  methods.

## Reporting

As I23: per cell delivered, compiled, functional of 5, unit / invoked /
autonomous tests passed, issue checks failed / unresolved / passed of 50;
the comparison table beside the prior study and beside I23.

## Limits

N = 5 per cell; one response; the lenient rule changes only the delivery
step; no significance claims.
