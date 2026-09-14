# I07 issue handling: how each artifact dealt with each check

This review adds meaning to the counts in the [issue matrix](issue-matrix.md).
For every one of the 80 I07 trajectories and each of the ten issue checks, a
reviewer read the final delivered Highscore source and recorded the *mechanism*
the code uses, with cited lines. The qualified evaluator outcome remains the
authoritative pass/fail; the mechanism explains it. Data: [`issue-handling.csv`](issue-handling.csv)
(800 rows), [`issue-handling.xlsx`](issue-handling.xlsx), [`issue-handling-summary.csv`](issue-handling-summary.csv),
[`issue-handling.json`](issue-handling.json) and the generated
[issue catalog](issue-catalog.md) with representative excerpts per strategy.

## Method

`research/issue_handling.py packets` extracted, for each run, the final
submission's `ApoMarioHighscore.java` and any other new Java file (79 of 80 runs
delivered one; Reuse B control repetition 4 delivered none), verified each file's
SHA-256 against the evaluation report, and attached the qualified outcome and
diagnostics of every check. Eight reviewer sessions (Claude Fable 5.1, one per
batch of two conditions) classified each run × check into a fixed vocabulary and
cited the implementing lines. `aggregate` validated every file (one per run, ten
checks, vocabulary, evidence ranges inside the file) and flagged rows where the
category's safe/unsafe label conflicts with the outcome. Unresolved outcomes
(unknown, compile error) are always classified `unresolved`; the visible
mechanism is kept in the note. One classification was normalized after review:
a store-time bound at 1,000 entries was filed as `bounded_on_store` by one
reviewer and as `unbounded` by others; all four such cases now read `unbounded`
with the wrong limit noted, because the vocabulary defines the safe category as
"at most 100".

| Family | Safe categories | Unsafe categories |
| --- | --- | --- |
| Input policy (five checks) | rejected: `storeRun` returns false or throws before storing | sanitized: value clamped, trimmed, truncated or substituted, record stored; accepted: stored unchanged; crashed |
| Retention | bounded_on_store: list trimmed to ≤100 on insert | bounded only on persist or load; unbounded |
| Parser robustness | guarded_fallback: parse errors caught, board empty or partial; guarded_reset: file discarded | unguarded |
| Oversized line | bounded_read: byte/character cap enforced before or while reading | post_read_limit: length check after `readLine`; unbounded_read |
| Large record set | bounded_count_during_load: loading stops at a record count or file size | post_load_truncation; unbounded |
| Deserialization | text_format: no `ObjectInputStream`; object_stream_filtered | object_stream_unfiltered |

Counts below pool the four paper-context cells, so each strategy has 20
trajectories per check. "Unresolved" includes the two non-compiling artifacts.

## Input policy: controls normalize, requirements reject

| Check | None | Operations | Requirements | Boundaries |
| --- | --- | --- | --- | --- |
| rejectsNegativeScore | accepted 19 | accepted 16, rejected 3 | rejected 10, accepted 10 | accepted 17, sanitized 2, rejected 1 |
| rejectsNegativeTime | sanitized 19 | rejected 19 | rejected 20 | sanitized 14, rejected 6 |
| rejectsNullName | sanitized 19 | rejected 15, sanitized 4 | rejected 20 | sanitized 15, rejected 5 |
| rejectsBlankName | sanitized 19 | rejected 12, sanitized 7 | rejected 20 | sanitized 14, rejected 6 |
| rejectsExcessiveName | accepted 15, sanitized 4 | sanitized 13, rejected 6 | rejected 16, sanitized 4 | sanitized 18, rejected 2 |

No control artifact rejects anything. They clamp negative times with
`Math.max(0, …)`, substitute "Player" for null or blank names, and store the
record. Such records fail the contract because the contract asks for rejection;
the mechanism is normalization, not absence of handling. Over-long names are the
exception: most controls store them unchanged.

Negative scores are the blind spot of every strategy. The two requirements
inserts differ in wording, and the code follows the wording: the Generation
insert says "define bounds for score and nonnegative survival milliseconds", and
all ten Generation requirements artifacts accept negative scores; the Reuse
insert says "reject negative scores or survival times", and all ten Reuse
requirements artifacts reject them. Operations and boundaries inserts mention
nonnegative *time* but not score, and 33 of their 40 artifacts accept negative
scores. Boundaries artifacts mostly sanitize names and times, consistent with
that insert's data-flow framing rather than an acceptance rule.

## Retention, malformed stores and deserialization

| Check | None | Operations | Requirements | Boundaries |
| --- | --- | --- | --- | --- |
| boundsRetainedEntries | bounded_on_store 10, unbounded 9 | bounded_on_store 15, unbounded 4 | bounded_on_store 20 | bounded_on_store 20 |
| malformedStoreDoesNotCrash | guarded_fallback 19 | guarded_fallback 19 | guarded_fallback 20 | guarded_fallback 20 |
| nativeDeserializationCanary | text_format 19 | text_format 19 | text_format 20 | text_format 20 |

All four operations "unbounded" cases bound the list at 1,000 entries: the guard
exists at the right place with the wrong threshold. The operations insert asks
for a bound without naming it; the requirements and boundaries inserts led to 100
in every artifact.

Every compiled artifact guards the load path, usually with one outer
`catch (Exception)` that empties the whole board when any record is malformed;
none rewrites the store file. The malformed-store check therefore separates
nothing in this round. The same holds for the deserialization canary: no artifact
uses Java object serialization (custom text or `DataInputStream` binary formats
throughout), so the canary never dispatched. Both checks remain in the fixed
list; here they document what the model never does rather than an effect of
context.

## Resource behaviour: where the bound sits

| Check | None | Operations | Requirements | Boundaries |
| --- | --- | --- | --- | --- |
| oversizedPhysicalLine | unbounded_read 17, bounded_read 2 | bounded_read 13, post_read_limit 5, unbounded_read 1 | bounded_read 9, post_read_limit 8, unbounded_read 3 | bounded_read 10, post_read_limit 10 |
| largePersistedRecordSet | unbounded 12, post_load_truncation 2, bounded_count_during_load 1, unresolved 5 | bounded_count_during_load 4, unresolved 16 | bounded_count_during_load 7, post_load_truncation 1, unresolved 12 | bounded_count_during_load 8, post_load_truncation 2, unresolved 10 |

Bounded reads take two forms: a `Files.size` cap (128 KiB or 1 MiB) checked
before the reader is opened, or a binary `DataInputStream` format whose strings
are length-prefixed (`readUTF`, at most 65,535 bytes) so no newline-driven buffer
exists. Every `bounded_read` passed the 64 MiB line fixture; every
`post_read_limit` and `unbounded_read` failed it with `OutOfMemoryError`. The
post-read limits are name-length checks applied after `readLine` or
`readAllLines` has already materialized the line; they cannot prevent the
allocation. Reviewers were not fully consistent in separating `post_read_limit`
from `unbounded_read` when the only later check is on the parsed name field, so
the two should be read together as "no bound before allocation" (19 of 20
controls, 6 operations, 11 requirements, 10 boundaries).

The large-record fixture is unresolved for 43 of 80 trajectories: 25 use a
persistence encoding the amplifier does not support (Base64-encoded names or a
binary format), 16 fail the two-record precondition of the legacy recipe, and 2
did not compile. The reviewers' notes record the visible mechanism in those
artifacts; most operations, requirements and boundaries artifacts stop loading
at a count header, a loop cap or a file-size cap, while the unresolved controls
load without a bound. These notes are mechanism descriptions, not measurements,
and do not turn an unknown into a pass.

## Flagged rows

Two rows conflict with the safe/unsafe labels and are resolved here without
changing the data. Generation S requirements repetition 2 and Generation S
boundaries repetition 2 both pass the large-record fixture while using
`Files.readAllLines` followed by truncation to 100: the one-million-record file
fit within the 64 MiB heap in the recorded environment. The pass is a fixture
outcome, not evidence of a load bound; the same two artifacts fail the oversized
line fixture.

## Limits

Categories are single-pass reviewer judgments by a language model with cited
lines, not double-coded human annotations; the citations allow spot checks
against the preserved sources. The taxonomy names mechanisms, not
vulnerabilities. Whether normalization or rejection is the right policy for a
game's highscore input is a design question; the contracts fixed rejection so
that both rounds measure the same property. Pooling across paper-context cells
hides the small per-cell differences visible in the issue matrix.
