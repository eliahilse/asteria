# I34: security notes per symbol, surfaced for the code the agent reads (Highscore, calibration at N = 2 per cell)

Declared on 2026-09-16 before any model call. The security sidecars so far
were a document up front (kind `static`), a code graph of what was read
(kind `ast`) and a judge that vetoes submissions (kinds `guard`, `advise`).
This round adds a contributor: kind `notes` (`research/security_notes.py`).
Every requirement, control, observation and risk of the v13 data-flow
graph of I31 is tied to the classes, constructors and methods it is
anchored at or enforced at, keyed by the I28 code graph of the same
snapshot (frozen in `contexts/`: Generation 15 statements on 20 symbols in
8 files, Reuse 15 statements on 14 symbols in 6 files). After each read or
search, and before a submission for the edited code, the sidecar injects
the notes of the symbols that entered the viewport and the notes of their
callers and callees one call edge out, each once, with the path and lines
to read next. No model call at run time, nothing is cancelled.

Design: cells Generation S and Reuse S+B; arms `agentic:notes` (no
document up front) and `agentic:static-notes` (the v13 document plus the
notes); two trajectories per cell and arm (8 in all); the agentic protocol
of I31 (`gpt-5.6-luna`, reasoning effort high, 24 tool turns, 5
submissions with feedback); controls are the I31 `none` and `static`
arms. Sidecar factory `research.security_notes:I34`.

Predictions (a calibration round; counts, not rates): every trajectory
receives at least one notes injection, and the notes of
`ApoMarioHighscore#storeRun`/the reader are injected before the first
submission in at least 6 of 8 trajectories; the `notes` arm fails fewer
of the 10 issue checks per compiled artifact than the I31 control (5.7)
and lands within 2 of the I31 `static` arm (1.2); `static-notes` is
within 1 check of `static`. If the notes arm reaches the document's
check rate without a document up front, the next round scales both arms
to N = 5 and adds the Achievements task.
