# I04 findings for review

Trust-boundary context reduced failed issue checks in all four paper-context
cells and matched each control's full-functional count. Requirements produced
larger security-count reductions, with functional regressions in Generation S
and Reuse S+B. Overview did not produce a consistent reduction. These are
descriptive results on one game and feature, with five code trajectories per
combination and one acquired insert per method/strategy in this round.

The table uses **qualified measurements**: unsupported large-record observations
are unknown. Each trajectory permits three submissions with functional feedback;
every scheduled trajectory, rejected edit and intermediate evaluation is retained.

| Paper-context cell | Control full | Boundaries full | Control issue failures / evaluated | Boundaries issue failures / evaluated | Boundary minus control, count bounds |
| --- | ---: | ---: | ---: | ---: | ---: |
| Generation S | 5/5 | 5/5 | 37/50 | 30/49 | [−7, −6] |
| Generation S+F+B | 5/5 | 5/5 | 33/50 | 25/49 | [−8, −7] |
| Reuse B | 4/5 | 4/5 | 32/46 | 23/47 | [−13, −6] |
| Reuse S+B | 4/5 | 4/5 | 32/48 | 16/38 | [−18, −4] |

The ranges assign every unresolved check either outcome; they are **not confidence
intervals**. Ten issue checks on one artifact are correlated measurements, not ten
independent samples. Equal functional counts at N=5 do not establish equivalence:
the marginal Wilson 95% interval for 5/5 is 56.6–100%, and for 4/5 is 37.6–96.4%.

## Requirements: stronger count reductions, mixed delivery

| Paper-context cell | Requirements full | Control full | Requirements issue failures / evaluated | Unresolved issue checks | First-submission full | Code submissions |
| --- | ---: | ---: | ---: | ---: | ---: | ---: |
| Generation S | 4/5 | 5/5 | 8/47 | 3 | 1/5 | 11 |
| Generation S+F+B | 5/5 | 5/5 | 9/48 | 2 | 1/5 | 11 |
| Reuse B | 4/5 | 4/5 | 2/47 | 3 | 1/5 | 11 |
| Reuse S+B | 2/5 | 4/5 | 3/30 | 20 | 2/5 | 11 |

Reuse S+B requirements repetition 1 rejected two edits before a compilation
failure; repetition 4 rejected all three edits because the requested old text
did not occur. Repetition 3 compiled on every submission but repeatedly failed
three autonomous lifecycle checks: the second run was not recorded. These are
distinct delivery and integration failures. Their missing security observations
remain in the bounds and never become passes.

Requirements' impact also varied by check and method. All eight compiled Reuse
requirements artifacts passed the five input-policy checks. Generation requirements
still failed negative-score rejection in every trajectory, despite passing most
other input policies. The [Generation insert](contexts/generation-requirements.txt)
asks for score bounds but specifically requires nonnegative survival time; it
does not explicitly require rejecting every negative score. This is a plausible
explanation for the difference, not a causal attribution: the insert contains
multiple recommendations, its source observations differ from I03, and model
outputs vary within the same arm.

Resource safety remains incomplete. Requirements fail the oversized-line fixture
in 3/5 Generation S, 3/5 Generation S+F+B, 2/5 Reuse B and 3/3 evaluated Reuse S+B
artifacts. Boundary guidance passes that fixture in every compiled Reuse artifact
but fails it in every Generation artifact. This method-dependent behavior is
hidden by a single total. The [per-test CSV](qualified-per-test.csv),
[effect CSV](qualified-security-effects.csv) and
[failure diagnostics](failure-diagnostics.json) retain the individual observations.

## What replicated

Generation S+F+B and Reuse B repeat I03 with newly acquired inserts and fresh code;
Generation S and Reuse S+B extend the selected matrix. Requirements and boundaries
reduce aggregate failure counts in both repeated cells in both rounds after
measurement qualification. Effect magnitude is unstable: Generation boundary
failures move from 10/48 in I03 to 25/49 in I04, versus controls 35/50 and 33/50.
This supports studying the contents of acquired contexts, not treating a strategy
name as a fixed treatment with a universal effect.

No trajectory in I03 or I04 jointly passed all 16 functional and all 11 declared
security checks. The deserialization canary and malformed-store checks pass across
all compiling I04 artifacts, including controls; these tests therefore provide
no observed discriminatory benefit for security context in this round.

## Measurement audit and limits

The original large-record adapter repeated an entire text seed file. For formats
with a record-count header, repeating that file can still declare only one record.
A calibrated two-record precondition probe identifies unsupported amplification.
Twelve I04 legacy passes become unknown; [original reports](results.json) and
[audit evidence](amplification-audit.json) remain preserved. A matched two-record
probe is necessary evidence for the adapter, not proof for all possible encodings
or arbitrarily large inputs. The original contracts, evaluation feedback and
generated sources were not changed during this audit.

The ten issue checks combine input-policy contracts, a fixed retention threshold,
finite robustness/resource fixtures and a harmless deserialization-dispatch canary.
Counts are not unique vulnerabilities, CVEs or a full security assessment. There
is no held-out repository, independent replication lab, multiplicity-adjusted
significance claim or verified provider attestation of effective sampling settings.
The requested and returned model identity is Luna; all unattested settings stay
explicit in provenance.

## Review artifacts and next fixed experiment

- [Complete qualified analysis](qualified-analysis.md): every condition, check and denominator.
- [Qualified XLSX](qualified-results.xlsx): numeric table, test rates, observations and provenance.
- [Quality and security figure](figures-qualified/quality-and-security.pdf).
- [Per-check effect figure](figures-qualified/per-check-security-effects.pdf).
- [Exact generated inserts](contexts/): six contexts reused within this round.
- [I05 frozen rationale](../i05-budget-sensitivity/README.md): fresh five-submission
  trajectories in every arm, holding I04's context inserts fixed to study repair budget.
