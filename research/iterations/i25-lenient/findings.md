# I25 findings: the prior study's matrix under lenient delivery, one response, reasoning effort high (N = 5 per cell)

Eighty trajectories, the sixteen cells of I23 (Generation and Reuse × None,
S, F, B, S+F, S+B, F+B, S+F+B), no security context, one response and no
feedback, reasoning effort high, and one change against I23: a returned
file that names an existing file replaces it whole instead of being
rejected. The side-by-side with the prior study and the per-cell counts are
in [comparison.md](comparison.md) and [comparison.csv](comparison.csv);
security counts per cell in the [issue matrix](issue-matrix.md); the
rejected responses in [delivery-errors.csv](delivery-errors.csv); time and
tokens per arm in [cost.md](cost.md).

| Method | Prior study compiled of 40 | I23 delivered / compiled / functional of 40 (strict) | I25 delivered / compiled / functional of 40 (lenient) | I25 unit tests passed of 280 | I25 issue checks failed / unresolved / passed of 400 |
| --- | ---: | ---: | ---: | ---: | --- |
| Generation | 17 | 19 / 18 / 17 | 27 / 25 / 20 | 175 | 161 / 174 / 65 |
| Reuse | 13 | 26 / 21 / 19 | 22 / 19 / 18 | 133 | 119 / 228 / 53 |

## Against the predictions declared in the README

Delivered above I23's 45 of 80: met, 49 of 80. Compiled and functional
above I23 in most cells: not met as stated; compiled is above I23 in 8
cells, equal in 3 and below in 5 (functional the same 8 / 3 / 5); the
totals are above in Generation (25 against 18 compiled, 20 against 17
functional) and below in Reuse (19 against 21, 18 against 19). The prior
study's compile counts below ours in both methods: met, 17 against 25 and
13 against 19.

## What the lenient rule changed, and what it did not

The rule removed the rejection class that dominated I23 (25 of 35
rejections were a target file returned whole); 27 responses are still
rejected, for reasons the rule does not touch:

- 11 left a required integration file (`ApoMarioMenu.java`,
  `ApoMarioPanel.java`) untouched; the feature is implemented in
  `ApoMarioLevel.java` only and never reaches the menu.
- 8 used an edit anchor that does not occur in the file, with no whole-file
  replacement involved.
- 6 returned a target file whole **and** edits to the same file anchored on
  the original text; the replacement is applied first, so the anchors no
  longer match. This class exists only under the lenient rule. The prior
  study's protocol has no edits, so it has no analogue; whether these six
  would compile if the edits were dropped is not known.
- 2 returned an empty new file.

Four Reuse trajectories (B r2, F r4, F+B r3, S r3) were served by
`gpt-5.6-terra` instead of the requested model, were stopped before
delivery as the protocol requires, and count as not delivered; those four
cells have four responses each.

Of the responses that were delivered, 44 of 49 compiled and 38 were fully
functional (I23: 39 of 45 and 36). The like-for-like number against the
prior study's "compiled" is therefore in the range 44 to 55 of 80 depending
on how its pipeline would treat the eleven responses without an
integration edit; our count of 44 is the conservative end.

## Cell counts move between identical reruns

I23 and I25 differ only in the delivery rule, and the rule does not touch
Reuse F or Generation None, yet Reuse F compiled 4 of 5 in I23 and 1 of 5
here, Generation None 3 and 1, Generation B 0 and 2, Generation S 3 and 5.
At N = 5 a cell's count moves by up to three between reruns. The cells
chosen after I23 for the one-response security rounds (Generation None, S,
S+F+B; Reuse F, S+F+B) were the most functional cells of I23; this round
shows the choice is a choice, not a property of the cells, and every
security round carries its own fresh control for that reason.

Security without context, on the compiled artifacts: the failed shares
match the earlier controls (Generation S 32 failed of 45 resolved checks;
Reuse S+F+B 24 of 36); unresolved counts are large because rejected and
non-compiling artifacts leave all ten checks unresolved.

## Cost

Eighty model calls, 8.49 M input tokens (the sixteen prompts differ only
in base context), 753 k output and 253 k reasoning tokens, 125.5 minutes of
call time in total, 2.7 minutes of trajectory wall time on average at
eight in parallel (I23 at four in parallel: 801 k output, 260 k reasoning,
102.1 call minutes).

## Limits

N = 5 per cell; one response; a different model, reasoning effort and
delivery protocol from the prior study; four trajectories lost to a served
identity mismatch; compile counts are compared, not pooled; no
significance claims.
