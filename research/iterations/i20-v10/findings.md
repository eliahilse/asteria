# I20 findings: compact inserts from protocol v10 acquisitions (N = 5)

Twenty single-shot trajectories, two cells × fresh control / v10 compact
insert × five. Counts are of 5 (functional) and of 50 issue checks (failed /
unresolved / passed); see the [issue matrix](issue-matrix.md) and the
[hook audit](hook-audit.md).

| Cell | Arm | First full | Within five | Calls | Issue checks failed / unresolved / passed of 50 | Functional and all 11 security checks |
| --- | --- | ---: | ---: | ---: | --- | ---: |
| Generation S | none | 1 | 5 | 10 | 39 / 1 / 10 | 0 |
| Generation S | v10 compact insert | 2 | 5 | 10 | 11 / 5 / 34 | 0 |
| Reuse S+B | none | 0 | 5 | 13 | 31 / 1 / 18 | 0 |
| Reuse S+B | v10 compact insert | 1 | 5 | 11 | 7 / 2 / 41 | 1 |

## Against the predictions declared in the README

Functionality (met): 5 of 5 functional in both insert arms, the same as their
controls; no trajectory fails only the two null-name tests; every insert
artifact substitutes a name at the hook ([hook audit](hook-audit.md)).
Submissions are 10 against 10 (Generation) and 11 against 13 (Reuse): the
first agent-acquired insert that costs no attempts.

Security (direction met, range missed): Generation 39 → 11 failed of 50
(bound −5.8 to −4.6; six checks decrease, none increases), Reuse 31 → 7
(bound −5.0 to −4.4; six decrease, one increases). The predicted range was 2
to 4 of 50. In Generation the five input-policy checks pass in 23 of 25
(v9: 0 of 25; the two failures are negative scores); the remaining failures
are resource checks (oversized line 5 of 5, retention bound 4 of 5). In Reuse
every input-policy check passes (25 of 25); the increase is the retention
bound, 0 → 3 of 5 failed, and the other failures are two oversized lines and
two large-record loads.

## Reading with I19

The v10 wording, "the receiving operation rejects and never substitutes or
clamps; the caller substitutes before calling it", separates the two roles
that v9 let the agent merge. Generation C1 under v10: "At
ApoMarioHighscore#storeRun, reject null/blank/oversized names, out-of-range
scores, invalid survival times ... Return false ...; recordRunEnd normalizes
missing values before this call." The artifacts do exactly that. Against the
v7/v8 compact inserts (I16/I16b: 2 and 3 failed of 50, 3 of 5 functional,
submissions 17 to 18) the v10 inserts trade a few more failed checks, mostly
the resource checks whose statements are shorter in the new acquisitions,
for full functionality at no attempt cost.

One Reuse artifact passes every functional test and all eleven security
checks, the fourth such artifact across all rounds (two in I09 under the
researcher-written requirements insert, one in I17).

## Limits

One acquisition per method shared by five trajectories; N = 5; a new
acquisition per protocol version, so content differs beyond the rule; the
retention increase in Reuse (3 of 5) is within N = 5 variation and unexplained;
no significance claims.
