# Research readout

The study asks whether security context acquired automatically from a repository
and feature task improves generated Highscore implementations while preserving
functionality. It is an **exploratory single-task study**. Later strategies were
developed from earlier findings; each subsequent schedule was frozen before its
code collection and completed with unfavorable outcomes retained.

Start with the [latest completed I07 findings](i07-operational-replication/findings.md),
[workbook](i07-operational-replication/qualified-results.xlsx) and
[two-round comparison](i07-operational-replication/replication-analysis.md).
I07 completes a fresh acquisition replication.
[I08](i08-evaluator-repeatability/findings.md) completes 64 repeat evaluations of
32 fixed artifacts, without new model samples or measurement transitions.

## What the context is

Luna starts each acquisition with an empty conversation and an immutable
repository snapshot plus the Highscore task. Generation has the ApoMario target;
Reuse also has the ApoIcarus donor and adaptation instructions. Search/read tools
return source excerpts with stable IDs. The final text is appended to the code
prompt after its unchanged task and paper-context attachments. No-security
controls receive no insert. Neither acquisition nor code generation receives
security-test results; code repair does receive functional feedback.

Three different classifications must remain separate:

| Classification | Values | Meaning |
| --- | --- | --- |
| Acquisition strategy | Overview, requirements, trust boundaries, operational guards | The instruction directing repository inspection and context production. |
| Generated item kind | Security property, existing risk, change risk, recommendation, unknown | What a generated statement claims; counts vary by acquisition. |
| Evaluation category | Input policy, retention policy, parser robustness, resource stress, deserialization dispatch | Groups of fixed executable contracts applied to generated code. |

Operational guards add operation, untrusted input, invariant, enforcement point
and failure behavior fields. Evidence-ID validation establishes that a cited
excerpt was inspected, not that the claim is true. An unknown is not proof of
absence. Topics such as dependency use, remote calls and cryptography can be
inspected when present; a generated CWE label is not a confirmed vulnerability.
See [acquisition methods](../CONTEXT_GENERATION.md), exact `contexts/` inserts and
the workbook's acquisition sheet for item counts and provenance.

## Comparable protocol rounds

These rounds share the corrected whole-game evaluator and separately qualified
large-record measurements. They differ in context samples, matrix scope or repair
budget, so their totals are not pooled as a treatment-effect estimate.

| Round | Trajectories | New context acquisitions | Submission cap | Code calls | First full | Within-cap full | Joint functional + all 11 security |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| [I03](i03-security-perspectives/findings.md) | 40 | 6 | 3 | 68 | 21 | 37 | 0 |
| [I04](i04-matrix-replication/findings.md) | 80 | 6 | 3 | 150 | 33 | 69 | 0 |
| [I05](i05-budget-sensitivity/findings.md) | 80 | 0 | 5 | 184 | 28 | 75 | 1 |
| [I06](i06-operational-guards/findings.md) | 80 | 2 | 5 | 190 | 24 | 76 | 4 |
| [I07](i07-operational-replication/findings.md) | 80 | 2 | 5 | 203 | 27 | 74 | 0 |

I03 covers Generation S+F+B and Reuse B; I04–I06 also cover Generation S and Reuse
S+B. These four cells were selected by the preserved original 2 × 8 matrix replay.
I04 acquires all three earlier strategies again. I05 holds those inserts fixed.
I06 adds operational guards in place of overview, retaining the fixed requirements
and boundary inserts. I07 repeats I06 with two fresh operational acquisitions.
The [iteration index](README.md) also retains the original single-response replay,
I01 delivery development and its same-code environment correction, and I02's
acquisition failure. None is silently folded into these corrected rounds.

A trajectory is one fresh code conversation with all its submissions. Checks and
repair attempts within it are not independent samples. Multiple trajectories
share one acquired insert per method/strategy; five code repetitions do not mean
five independent context acquisitions. Provider model identity is checked against
Luna; requested settings remain unverified when not attested by the provider.

## What the measurements support

Functionality requires all **7 unit + 4 invoked integration + 5 autonomous game
checks**. Compile success and individual suite pass rates are also retained, with
all planned trajectories in the denominator. Menu rendering is not covered by
these 16 checks. The model may repair functional failures up to the fixed cap;
first-submission results and complete repair curves show the cost of that repair.

The security suite has one positive persistence check and ten issue contracts:
five input policies, one retention policy, one malformed-input fixture, two
resource fixtures and one harmless deserialization canary. A failed policy is not
automatically a distinct vulnerability. In I06, two retention failures use a finite
1,000-entry cap while the test requires 100; the [source review](i06-operational-guards/source-review.md)
preserves that distinction and identifies an unmeasured persistence concern.

Qualified reports mark unsupported large-record outcomes unknown and keep the
original reports. Counts use failed/evaluated checks, with missing coverage
explicit. Difference bounds consider every assignment of unresolved outcomes;
they are not confidence intervals. Marginal functional Wilson intervals describe
uncertainty conditional on this task and insert, without a multiplicity-adjusted
significance claim. Category summaries count affected artifacts and avoid treating
five related input checks as five independent observations.

I06's clearest resource result is 20/20 oversized-line passes under operational
context versus 2/20 controls, with full functionality 19/20 versus 20/20. But all
five Reuse S+B operational large-record outcomes are unresolved, negative-score
policy failures persist, and retention policy regresses in that cell. The evidence
supports a specific fixture improvement, not uniform security improvement or a
claim that all passing artifacts are secure. I07 repeats lower total counts in
every combination, but operations achieve only 13 oversized-line passes, six
heap-exhaustion failures and one unmeasured result; full functionality is 18/20
versus 19/20 controls. There are no qualified joint passes in I07. The perfect
resource result did not fully replicate. I08 found no raw or qualified measurement
changes in its fixed selection. Its 819 fully measured artifact/check triples
were stable; 45 unresolved triples provide no evidence of a pass. This supports
local repeatability, not universal determinism or independent context replication.

## Recovery and review

The local site is at `http://127.0.0.1:5173/`; an iteration query opens its saved
table and exact inserts. A fresh checkout also loads committed completed results
without restoring private runtime files. Raw evidence can be restored with the
[archive commands](../results/README.md), without making model calls.

Both full archives and directly readable copies are outside the repository in
`../asteria-research-backups/`. For completed I06, open
`i06-complete-review/START-HERE.md`; its later source review is separately saved in
`i06-source-review/`. Every archive file is hashed, both copies are verified, and
small Conventional Commits preserve the producing code and observations. Private
adapter configuration and provider transport traces remain excluded.
