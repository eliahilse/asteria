# I19 findings: compact inserts from protocol v9 acquisitions (N = 5)

Twenty single-shot trajectories, two cells × fresh control / v9 compact insert
× five. Counts are of 5 (functional) and of 50 issue checks (failed /
unresolved / passed); see the [issue matrix](issue-matrix.md) and the
[hook audit](hook-audit.md).

| Cell | Arm | First full | Within five | Calls | Issue checks failed / unresolved / passed of 50 |
| --- | --- | ---: | ---: | ---: | --- |
| Generation S | none | 3 | 5 | 7 | 37 / 2 / 11 |
| Generation S | v9 compact insert | 0 | 5 | 20 | 31 / 2 / 17 |
| Reuse S+B | none | 2 | 5 | 10 | 35 / 1 / 14 |
| Reuse S+B | v9 compact insert | 1 | 4 | 17 | 9 / 4 / 37 |

## Against the predictions declared in the README

Functionality (met): no trajectory in either cell fails only the two
null-name tests (I16: 2 of 5 per cell). Every v9 insert artifact substitutes
a name at the hook. Generation is 5 of 5 functional; Reuse 4 of 5, the loss
(r1) failing the real-name test as well because of a once-per-level guard
that is never reset after the driver's level reset, the defect seen in I11,
I17 and I18.

Security (met in Reuse, missed in Generation). Reuse: 35 → 9 failed of 50
(bound −5.4 to −4.4; six checks decrease, none increases); the remaining
failures are negative scores (5 of 5), one null name, one over-long name and
two oversized lines. Generation: 37 → 31 of 50 (bound −1.6 to −0.8); only
the retention bound improves (4 → 0 of 5); all five input-policy checks and
the oversized-line check fail in 5 of 5 insert artifacts, as in the control.

## Why Generation lost the effect

The v9 rule says rejection belongs at the boundary and substitution at the
integration point. The Generation acquisition placed the substitution at the
store: its R2 failure line reads "use the stated fallback "Player" at
storeRun so the primary record effect occurs" and its C2 says "Enforce name
normalization at the name input and storeRun boundaries ... replace
null/empty/invalid values". All five artifacts implement `storeRun` that way:
they clamp negative scores and times to 0 and normalize names instead of
returning false (`ApoMarioHighscore.java`, `storeRun` bodies in the
[hook audit](hook-audit.md) rows), which is what the control artifacts do
without any insert. The Reuse acquisition kept the two apart: "Reject an
invalid tuple at storeRun, or normalize it to explicit bounded defaults at
the run-end integration point", and its artifacts reject at the store and
substitute at the hook.

The security effect of the earlier inserts was carried by the word "reject"
at the store. A fail-safe rule that lets the agent put the substitution into
the boundary operation removes that effect. The rule must bind substitution
to the caller of the boundary operation and keep the boundary itself
rejecting; that is the next protocol change (v10).

## Cost

Submissions 20 against 7 (Generation) and 17 against 10 (Reuse);
first-submission success 0 and 1 of 5 against 3 and 2.

## Limits

One acquisition per method shared by five trajectories; N = 5; a new
acquisition, so content differs from I16 beyond the rule; no significance
claims.
