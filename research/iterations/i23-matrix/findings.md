# I23 findings: the prior study's Highscore matrix, one response, reasoning effort high (N = 5 per cell)

Eighty trajectories, sixteen cells (Generation and Reuse × None, S, F, B,
S+F, S+B, F+B, S+F+B), no security context, one response and no feedback,
reasoning effort high. The side-by-side with the prior study's 80 Highscore
runs (Gemini 3.1 Flash Lite, one response) is in [comparison.md](comparison.md)
and [comparison.csv](comparison.csv); security counts per cell are in the
[issue matrix](issue-matrix.md).

| Method | Prior study compiled of 40 | Ours delivered of 40 | Ours compiled of 40 | Ours functional (16 tests) of 40 | Unit tests passed of 280 | Issue checks failed / unresolved / passed of 400 |
| --- | ---: | ---: | ---: | ---: | ---: | --- |
| Generation | 17 | 19 | 18 | 17 | 126 | 113 / 237 / 50 |
| Reuse | 13 | 26 | 21 | 19 | 147 | 131 / 208 / 61 |

## Against the predictions declared in the README

Compiled above the prior study in most cells: met in Reuse (21 against 13
of 40; above or equal in 6 of 8 cells) and not in Generation (18 against
17; above in 3 cells, equal in 2, below in 3). Reuse below Generation: not
met, Reuse compiles and passes more here (21 against 18 compiled, 19
against 17 functional). Functional between 1 and 4 of 5 in Generation
cells: met in 7 of 8 cells (Generation B has 0 of 5).

## What decides the outcome here

Thirty-five of the eighty responses were rejected by the exact-edit
delivery protocol before compilation ([delivery errors](delivery-errors.csv)):
25 listed a file as new that already existed, either a target file to be
edited (`ApoMarioLevel.java` 16 times, `ApoMarioPanel.java` 2) or the new
class itself listed twice (7), 8 omitted a required integration edit, and 2
used an edit anchor that does not occur once. Of the 45 responses that were
delivered, 39 compiled and 36 were fully functional. The prior study's
pipeline writes every returned file and repairs imports and placement
before compiling, so a response that replaces a whole file counts as
compiled there and as rejected here. Under that protocol the comparable
number for our generator is closer to the delivered-and-compiled rate than
to the compiled-of-all rate; a like-for-like comparison needs a lenient
delivery mode that accepts a full replacement of a target file.

Cells with the most functional artifacts on one response: Reuse F and Reuse
S+F+B (4 of 5), then Generation None, S, S+F+B and Reuse S, S+F, S+B (3 of
5). Generation B delivered 0 of 5 (all five replaced `ApoMarioLevel.java`
as a new file) and Reuse None compiled 0 of 5.

Security without context, on the compiled artifacts: the failed shares are
those of the earlier controls (for example Generation S 17 failed of the
27 resolved checks; Reuse F 26 of 37); unresolved counts are large because
rejected and non-compiling artifacts leave all ten checks unresolved.

## Limits

N = 5 per cell; one response; a different model, reasoning effort and
delivery protocol from the prior study; compile counts are compared, not
pooled; no significance claims.
