# A05: security notes per symbol on the second task (Achievements, N = 5 per cell)

Declared on 2026-09-16 after the I34 calibration, before any model call
of this round. The `notes` sidecar (`research/security_notes.py`) ties
every requirement, control, observation and risk of the v13 Achievements
data-flow graphs (A02 contexts, S2) to the classes, constructors and
methods they are anchored at or enforced at, keyed by the I28 code graph
of the same repository snapshot (frozen in `contexts/`: Generation 13
statements on 26 symbols, Reuse 14 statements on 21 symbols). After each
read or search, and before a submission for the edited code, the notes of
the symbols in view and of their callers and callees one call edge out
are injected once, with the path and lines to read next; no model call at
run time, nothing is cancelled.

Design: cells Generation S and Reuse S+B; arms `agentic:notes` (no
document up front) and `agentic:static-notes` (the v13 full document plus
the notes); five trajectories per cell and arm (20 in all); the agentic
protocol of A03 (`gpt-5.6-luna`, reasoning effort high, 24 tool turns, 5
submissions with feedback). Controls are A03a (`none`: compiled 9 of 10,
functional 7 of 10, checks 78 %, all-pass 0) and A03c (`static`, the same
document up front: 10 of 10, 7 of 10, 95 %, all-pass 5 of 10, with
Reuse S+B functional 2 of 5 against the control's 5 of 5; A04 repeated
that cell at 3 of 5 against 1 of 5). Sidecar factory
`research.security_notes:A05`.

Predictions: the `notes` arm keeps the control's functional count in
Reuse S+B (at least 4 of 5) while passing more checks than the control
(at least 90 % of the ten checks per compiled artifact); its all-pass
count is at least 3 of 10; `static-notes` is within one all-pass of
`static`. If the notes arm reaches the document's checks without the
Reuse functional cost, the notes replace the document as the paper's
recommended delivery.
