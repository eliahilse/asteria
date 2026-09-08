# I08 evaluator repeatability findings

All **64 additional evaluations** completed on the 32 preselected source artifacts,
with **zero model calls, zero measurement transitions and zero source/class
mismatches**. Each artifact now has its original measurement and two repeats.
Original study outcomes remain unchanged and these repeats are not added to the
context-effect tables or code-generation denominators.

The selection is repetition 1 of every I06/I07 combination. All 32 source artifacts
were available. The original measurements contain 31 full-functional artifacts
and one compilation failure; both repeats preserve those states. Every repeated
input and sanitized-source hash matches its selected artifact. The 62 successful
repeat compilations match their original compiled-class maps; the other two
repeat the selected compilation failure and have no successful class map.

| Measurement | Selected artifacts | Complete pass/fail triples | Stable triples | Unresolved triples |
| --- | ---: | ---: | ---: | ---: |
| Each of the 16 functional checks | 32 | 31 | 31 | 1 |
| Each of the 10 security checks other than large-record amplification | 32 | 31 | 31 | 1 |
| Qualified large-record amplification | 32 | 13 | 13 | 19 |

The 13 fully measured large-record artifacts retain nine passes and four failures
in all three measurements. Eighteen artifacts retain unknown large-record results;
one retains a compilation error. Repeated unknowns do not validate the format
adapter or establish a security pass. Across the 27 checks, the data contains
819 fully measured stable triples and 45 unresolved triples. These are correlated
artifact/check observations, not 864 independent statistical samples.

The comparison checks both original raw statuses and separately qualified
statuses, in addition to compilation and full functionality. Source bytes,
sanitized files, compiled class bytes, report hashes and saved precondition
evidence are verified before summarizing. There were no execution errors or
replacement artifacts. The [frozen plan](plan.json), [all repeat observations](results.json),
[27-row stability table](stability.csv) and [transition export](transitions.csv)
retain the complete selection and outcome accounting. The transition export is
empty because no transitions occurred.

This supports **local repeatability for these selected artifacts and finite
fixtures**. It does not prove universal determinism, validity of unsupported
encodings, security beyond the tested contracts or replication on other hardware.
The fixed first-repetition selection contains no originally compiling functional
assertion failure, so it does not test repeatability of those failure paths. It
also does not include I07's six operational oversized-line failures, which occurred
in later repetitions; those remain separately documented in the
[source review](../i07-operational-replication/source-review.md).

The I06/I07 differences persist as reported: lower total issue counts across the
tested combinations, varying resource performance, functional repair costs and
missing coverage. Repeating unchanged artifacts does not make those context
acquisitions independent or remove the study's exploratory character.
