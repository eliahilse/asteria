# Retention failures and policy interpretation

This is a post-collection source review of **both** Reuse S+B operational-guard
artifacts that fail `boundsRetainedEntries`: repetitions 2 and 4. It does not
change their outcomes, add code-generation samples or provide feedback to I07.
The [copy manifest](source-review/manifest.json) identifies exact sanitized-source
hashes and original archived paths.

The fixture inserts 120 valid records and requires at most 100 retained. The
five final artifacts in this condition use these limits:

| Repetition | Declared retained-record cap | Fixture outcome |
| --- | ---: | --- |
| 1 | 100 | Pass |
| 2 | 1,000 | Fail |
| 3 | 100 | Pass |
| 4 | 1,000 | Fail |
| 5 | 100 | Pass |

Repetition 2 declares `MAX_ENTRIES = 1000`, removes the lowest-ranked entry after
an insertion exceeds that limit, and rejects persisted counts above it during
load. Repetition 4 declares the same cap, rejects new insertions when already at
the cap, and rejects excessive persisted counts. Therefore the 120-record fixture
is consistent with these finite policies. Failing its 100-record contract is not
evidence that either implementation lacks a record-count limit.

The [repetition 2 source](source-review/r2-ApoMarioHighscore.java) contains the
declaration at line 23, insertion enforcement at line 46 and load enforcement at
line 120. The [repetition 4 source](source-review/r4-ApoMarioHighscore.java) has the
corresponding declaration at line 30 and guards at lines 54 and 174. Both also
check file size before opening the data stream: 1 MiB in repetition 2 and 256 KiB
in repetition 4. The existing oversized-line passes provide fixture-specific
behavioral evidence; these source observations alone do not establish general
resource safety or eliminate file-replacement races.

The [shared context](contexts/reuse-operations.txt), item C3, asks for explicit
record-count and file-size bounds without supplying a numerical cap. Thus the
count difference can reflect a model-chosen policy rather than failure to follow
the boundedness recommendation. The original test contract remains useful as a
fixed measure, provided it is described as policy conformance.

## An unmeasured persistence concern

Repetition 4's `storeRun` adds a record, calls `persistFile()`, ignores its boolean
result and returns true. Its name validator checks Java string length against 64
characters; the writer separately rejects UTF-8 encodings longer than 64 bytes.
Inspection therefore suggests that a multibyte name can be accepted into memory
while persistence fails, with success still reported to the caller. This path
has not been executed by a new validation test in this review. It is an explicit
candidate for follow-up, not an extra counted failure or a demonstrated exploit.

Item C5 asks for a complete persisted snapshot or retention of the previous file.
Even where that file-level property holds, the caller's success indication needs
its own contract and test. The original functional suite and ten issue contracts
do not cover every generated recommendation. Full marks on those checks cannot
establish complete implementation of the acquired security context.

## A lifecycle guard that suppresses later runs

The failed Reuse B operational trajectory, repetition 3, adds a
`highscoreRecorded` boolean to `ApoMarioLevel`. Its three occurrences are the field
declaration, an early-return condition and an assignment to true; there is no
reset to false. The [exact excerpts and occurrence list](source-review/run-lifecycle.json)
retain source and report hashes. After one recording on a level instance, this
guard suppresses further records on that instance. The autonomous report contains
the 54,321-point run but misses the other requested runs, consistent with that
state-lifetime error.

The shared operational context asks for one record per eligible run and a per-run
marker. The generated code instead gives its marker the level object's lifetime.
This is a concrete implementation defect in a suggested safeguard, not evidence
that duplicate protection itself should be removed. Three failed assertions here
depend on missing records; they do not demonstrate three independent defects.
This post-collection explanation does not isolate the causal effect of the
context or alter the frozen feedback or test counts.
