# Agentic delivery with security-context sidecars

`research/agentic_delivery.py` compares two ways of delivering the Highscore
feature and three ways of supplying security context. **No real trajectory has
been executed with this protocol yet.** Everything below describes frozen
design and unit-tested harness behaviour, not observations.

Protocol id: `agentic-delivery-v1`. Evaluation: the unchanged
`highscore-response-v3-integrated-security` evaluator (`evaluate_integrated`),
with the same calibration check as `iteration_runner_v2.prepare`.

## Arms

A condition is `{cell}__{mode}__{sidecar}`, for example
`generation_s__agentic__adaptive`. Cells are baseline conditions of
`research/studies/highscore-paper-luna-v2` (`generation_s`, `generation_sfb`,
`reuse_b`, `reuse_sb`). Prompts are rebuilt exactly as in
`iteration_runner_v2.prepare`: the acquisition task text followed by the baseline
attachments split at `'\n\n--- BEGIN ATTACHED'`, CRLF normalised.

| Axis | Value | Meaning |
| --- | --- | --- |
| mode | `single_shot` | Existing protocol: one conversation, the unchanged `submit_feature_changes` tool (`feature_delivery.TOOL`), the I01 system text with the declared budget word (`up to five submissions`, derived as in `budget_sensitivity.py` and checked against the parent manifest's stored bytes), request ids `…-s{n}`, functional feedback only. Requests in the `none` and `static` arms are byte-comparable with I05–I09 apart from the run id. |
| mode | `agentic` | One forced tool `act` whose `action` selects `search`, `read` or `submit_feature_changes`. The system prompt is the single-shot system plus a tool/budget addendum (`systemAgentic` in the manifest). The first user message is the condition prompt followed by the frozen repository file index. Request ids are `…-t{n}`. |
| sidecar | `none` | Nothing appended, nothing injected. |
| sidecar | `static` | The insert file bytes for the cell are appended after `'\n\n'` to the prompt, exactly like earlier rounds. No injections during the trajectory. |
| sidecar | `adaptive` | Nothing upfront. After each successful search/read result and before the next submission attempt the harness asks the sidecar for statements relevant to what the model has touched; each statement id is injected at most once. |

Budgets: `maxTurns` model turns in total (default 24; search, read, invalid
actions and submissions all consume one) and `maxSubmissions` submissions
(default 5). A trajectory stops on full functional success, on the first budget
that runs out (`budgetLimit` = `turns` or `submissions`), on identity/settings
mismatch, incomplete response, adapter error, evaluation error or sidecar error.

## The `act` tool

Strict function schema, every field required, unused fields `null`:

```
action    : "search" | "read" | "submit_feature_changes"
query     : string | null            search: literal, case-insensitive, <= 200 characters
paths     : [string] | null          search: restrict to indexed paths
files     : [{path, start_line, end_line}] | null   read: 1–5 ranges, <= 240 lines each, 30000 characters per turn
new_files : [{filename, content}] | null            same item shape as feature_delivery.TOOL
edits     : [{filename, old_text, new_text}] | null same item shape as feature_delivery.TOOL
```

Search and read run through `context_repository.operate` on a snapshot of the
frozen repository input that the acquisitions used
(`security_followup.repository_input(method, parent_dir)`). Results are
returned in the next user message exactly like the acquisition loop: excerpts
with stable evidence ids (`E0001`, …), line-numbered text, `totalMatches`, and a
trailing `N tool turns and M submissions remain.` line. Operate errors are
returned as `{"error": …}` and still cost a turn.

`submit_feature_changes` follows `iteration_runner.trajectory` exactly:
`apply_changes` on the accumulated files, required edits to all three targets,
staged `complete-files.txt`, `evaluate_integrated.evaluate_response`, feedback
containing compilation, functional checks and compiler errors only (security
results are retained locally, never fed back). The single-shot feedback string is
unchanged; the agentic one appends `K tool turns remain.`.

## Sidecar contract

The harness accepts any object with two methods; it never imports a concrete
sidecar. A real one is expected from `research.context_graph`.

- `initial(condition) -> str | None`. For `static` conditions the frozen insert
  file is what the model sees. If a sidecar is supplied for a static run, the
  harness calls `initial` and records `staticInsert.sidecarConfirmed`; a
  differing non-`None` text stops the run as `sidecar_error` before any model
  call. Use `initial` (or any other source) to produce the insert files passed to
  `prepare(context_inserts=…)`.
- `update(touched, shown_ids) -> (str | None, list[str])` for `adaptive`
  conditions. `touched` is
  `{'files': set, 'symbols': set, 'queries': list, 'stage': 'after_read' | 'before_submit', 'condition': condition id, 'cell': parent cell, 'method': 'Generation' | 'Reuse'}`:
  `files` are snapshot paths that were read plus the snapshot paths matching any
  filename named in a submission (applied or rejected; new files stay as
  basenames), `symbols` is reserved and currently always empty, `queries` are all
  search strings so far. `shown_ids` are ids already injected. The returned text
  is injected verbatim; the ids identify its statements.

Harness rules for adaptive injection:

- Consultation happens only once something has been touched, so nothing is
  injected before the first turn.
- `after_read`: after every successful search/read; the text is appended to
  that tool-result user message after a blank line.
- `before_submit`: after every submission's feedback (the touched set may have
  grown through edits); the text becomes a separate user message before the
  model's next turn. In single-shot mode this is the only stage that fires.
- An injection is skipped and recorded with a `reason` when the text is empty
  (`nothing_relevant`), when no ids are returned (`missing_ids`) or when any id
  was already shown (`repeated_ids`). Statement ids are therefore never injected
  twice even with a sidecar that ignores `shown_ids`.
- Exceptions or malformed return values from the sidecar stop the run as
  `sidecar_error` with the message in `errorDetail`.

## Manifest and record layout

`prepare(identifier, cells, arms, repetitions, context_inserts, parent='i07-operational-replication', max_turns=24, max_submissions=5, directory=None)`
freezes `.local/iterations/{identifier}/manifest.json` (fingerprinted, `freeze`
refuses changed bytes) with `prompts/{sha}.txt` per condition and
`repository/{method}-index.txt` per method. `context_inserts` maps a cell id or
method name to an insert file. Manifest fields beyond the v2 layout: `maxTurns`,
`systemAgentic`, `tools` (both schemas), `arms`, `repositories` (snapshot
fingerprint, index hash, file counts), `parentIteration`, and per condition
`mode`, `sidecar`, `contextInsertFile/Sha256`, `repository`,
`snapshotFingerprint`, `repositoryIndexFile/Sha256`. `sourceHashes` include this
module, the reused runner/evaluator modules, the baseline prompts, the parent
manifest and frozen inputs, the insert files and the calibration input hashes.

`runs/{runId}/record.json` keeps the existing fields (`submissions` with
request/response/hashes, `functionalSuccess`, `settingsVerified`,
`finalEvaluation`, `sourceOrigins`) and adds:

| Field | Content |
| --- | --- |
| `mode`, `sidecar`, `maxTurns`, `maxSubmissions`, `snapshotFingerprint` | condition parameters |
| `turns[]` | every request/response with `requestSha256`, `responseSha256`, `action`, `status`, and `toolResult` for search/read |
| `submissions[].turn` | the turn that carried the submission |
| `sidecarEvents[]` | `turn` (completed turns at that moment), `stage`, `ids`, `injected`, `sha256`, `characters`, `touchedFiles`, or `reason` when nothing was injected |
| `filesRead[]`, `searches[]` | per read range (`path`, lines, `evidence_id`) and per search (`query`, `paths`, `totalMatches`, `returned`) |
| `touchedFiles`, `evidenceIndex`, `toolTurns`, `sidecarInjections`, `budgetLimit`, `staticInsert` | closing summaries |

## Commands

```sh
# Freeze a plan (no model call); one static insert per cell or method
python3 -m research.agentic_delivery --prepare i10-agentic-delivery \
  --cells generation_s,reuse_sb --arms single_shot:none,single_shot:static,agentic:none,agentic:adaptive \
  --repetitions 5 --insert generation_s=.local/iterations/i07-operational-replication/contexts/generation-requirements.txt \
  --insert reuse_sb=.local/iterations/i07-operational-replication/contexts/reuse-requirements.txt

# Describe
python3 -m research.agentic_delivery --manifest .local/iterations/i10-agentic-delivery/manifest.json

# Execute (subprocess per trajectory, lock file, logs, existing run directories are skipped)
python3 -m research.agentic_delivery --manifest … --execute --workers 3 --sidecar research.context_graph:sidecar

# One trajectory
python3 -m research.agentic_delivery --manifest … --run-id … --execute --sidecar research.context_graph:sidecar

# Counts per condition (k/N, no percentages)
python3 -m research.agentic_delivery --manifest … --summary
```

`--sidecar module:attribute` names an object with the two methods, a class, or a
zero-argument factory; it is required whenever the manifest contains adaptive
conditions. The adapter command comes from `ASTERIA_ADAPTER_COMMAND` as before.

`summary(manifest_dir)` reports per condition: planned, N, full success within
budget (k/N), first-submission success (k/N), tool turns, reads, searches,
submissions and sidecar injections (totals plus per-trajectory lists),
trajectories with at least one injection (k/N) and a status histogram.

## Status

Harness and tests only (`research/test_agentic_delivery.py`, fake adapter,
fake evaluator, fake sidecar, synthetic snapshot). No manifest has been frozen
under `.local/iterations` and no model request has been sent with this protocol.

## Line ranges and sidecar provenance (added after I10)

The `touched` view also carries `ranges`: for every read, the excerpt's line
range, and for every edit in a submission, the line range of its old text
located in the pre-edit source, keyed by snapshot path. `research/graph_sidecar.py`
uses them for symbol-level slicing (statements anchored at symbols overlapping
those ranges, no call hops by default, requirement and control statements
only); `research/i10_sidecar.py` remains the file-level sidecar used in I10.
Each record stores `sidecarSpec` (the `module:attribute` given on the command
line), `sidecarConfig` (the sidecar's `describe()`, including graph hashes) and
`touchedRanges`.

## Gate sidecar (added for I12)

Sidecar kind `gate`: no upfront insert and no passive injection. For every
submission that applies cleanly, the harness computes the line ranges of its
edits in the pre-edit source and calls `sidecar.judge(view)` with the changes,
ranges, touched files and a request id `<runId>-j<n>`. `research.graph_sidecar.GateSidecar`
selects the requirement and control statements anchored at symbols overlapping
those ranges; with none it answers without a model call (`consulted: false`).
Otherwise it asks a one-turn judge (same model and settings, strict
`judge_submission` tool: intervene, statement_ids, reason) whether the
submission clearly violates a listed control at its enforcement point. Only
`intervene: true` with cited statement ids rejects the submission before
compilation and evaluation, with the cited statements as feedback; the
submission counts against the budget. Every gate event records consultation,
verdict, ids, reason and the judge transcript with hashes. The judge sees the
edits and the statements only, never test outcomes.

## Rewind sidecar (added for I15)

Sidecar kind `rewind`: the judge runs as for `gate`. On the first positive
verdict the harness discards the offending submission and the `rewind_turns`
(default 2) tool turns before it, restores the conversation, working files and
touched state saved before the earliest discarded turn, appends the verdict
text there ("a previous attempt violated the controls below and was
discarded"), and continues. Discarded turns stay in the record with
`discarded: true`; the rewound submission counts against the submission
budget; `record.rewinds` lists each rewind with from/to turns. Later positive
verdicts are coached. `research.graph_sidecar:REWIND` serves I15.

## Guard sidecar (pre-tool-call judge)

Sidecar kinds `guard` and `guard_shadow` (agentic mode only; `normalize_arms`
rejects `single_shot:guard*`). No upfront insert and no passive injection. The
condition freezes the round's security context insert exactly like a static arm
(`--insert`, `contextInsertFile/Sha256`, `sourceHashes`) but the prompt stays the
`none` arm's; the insert is what the judge reads. No trajectory of this kind has
been executed yet.

**When it runs.** Before every tool call of the generator that would execute:
`search`, `read` and `submit_feature_changes` alike, after the `act` payload is
parsed. Malformed calls (operate errors, invalid or incomplete changes) take the
usual error path unjudged, so a consultation is never spent on a call that could
not execute. `apply_changes` and `operate` are pure, so this pre-check has no
side effect.

**What the judge sees** (`sidecar.judge(view)`, `GuardSidecar` in
`research/graph_sidecar.py`): the task text of the condition prompt before the
attachments (capped at `TASK_CHARS`), the frozen insert verbatim, the working
files by name with line counts, hashes and state (`unchanged`, `changed`,
`new`), the trajectory so far as one compact JSON line per completed turn
(action, arguments in short, functional outcome, earlier verdicts; the newest
entries kept within `HISTORY_CHARS`, the number omitted is stated), the pending
call with its arguments (edits and new files capped like the gate prompt), the
visible message text of the turn, and the guard state (interventions so far,
judge calls available). Under adapter protocol v1 no visible message or
reasoning text exists (the adapter returns the forced tool call's arguments
only); the prompt says so, and every event records `messageText: null` with
`messageTextNote`. Nothing is inferred from the arguments. Security check
results are never part of the view.

**Judge turns.** The judge answers through tools: `guard_act` (`search`, `read`
or `verdict`) on its first `judge_turns` calls (default 3), then `guard_verdict`
is forced. Its `read`/`search` run through `context_repository.operate` on the
frozen snapshot plus the generator's current working files addressed by bare
file name (`ApoMarioLevel.java`, a new file), with the generator's limits;
results are line-numbered, capped at `RESULT_CHARS`, and never touch the
generator's evidence index, `filesRead` or `touched`. Request ids are
`<runId>-g<turn>k<n>`; `prepare` reserves `g{maxTurns}k{GUARD_JUDGE_TURNS_LIMIT+1}`
in the request-id length check whenever a guard arm is present, and
`trajectory` refuses a sidecar whose `judgeTurns` would exceed the limit before
any model call. The verdict is `{intervene, reason, quoted, statement_ids, advice}`.

**What an intervention does.** A verdict acts only when `intervene` is true and
at least one cited statement id is present in the insert; the harness's
`interventions` count is passed in the view and the sidecar reports `capped`
once it reaches `max_interventions` (default 3), after which verdicts are
recorded but nothing is cancelled. `shadow=True` on the sidecar, or the kind
`guard_shadow`, records verdicts and never cancels (the harness also never
cancels for `guard_shadow`, whatever the sidecar returns). On an intervention
the pending call is not executed: no `operate`, no `apply_changes`, no
evaluation and no `submissions[]` entry. The generator receives, as the tool
result, `{"cancelled": <action>, "securityGuard": <text>}` followed by the usual
remaining-budget line, where `<text>` is
`--- TOOL CALL CANCELLED BY THE SECURITY GUARD ---`, the reason, the advice, the
quoted lines and the cited statements copied from the insert, `--- END ---`.
The turn counts against `maxTurns`; a cancelled submission does not count
against `maxSubmissions` (it never became a submission; `gate` rejections do).
The turn is recorded with `status: cancelled_by_guard`.

**What is recorded.** Each consultation is one `sidecarEvents[]` entry with
`stage: guard`, written before the judge runs (`status: started`), again by
the judge before and after each of its adapter calls (through the view's
`checkpoint`), and completed afterwards (`judged`, or `error` with
`error`/`errorCategory`, the requests sent so far kept): `turn`, `action`,
`requestId`, `requestIds`, `consulted`, `intervene`, `wouldIntervene`,
`verdictIntervene`, `verdictStatus` (`verdict`, `identity_mismatch`,
`settings_mismatch`, `incomplete_response`, `invalid_verdict`, `no_verdict`),
`capped`, `shadow`, `ids` (cited, `item:` form), `quoted`, `unquoted` (quotes
absent from a submission), `reason`, `adviceText`, `messageText`,
`messageTextNote`, `judgeTurns`, `judgeActions` (the judge's own reads and
searches with counts or errors), `historyEntries`, `historyOmitted`,
`touchedFiles`, `transcript` (one entry per judge request: `request`,
`requestSha256`, `response`, `responseSha256`, timestamps, `status`,
`settingsVerified`), `cancelled` (the cancelled action or `null`), `injected`,
`sha256`, `characters`. The insert's bytes are recorded as `record.guardInsert`
and the sidecar's `describe()` (judge turns, cap, shadow, graph, prompt and tool
hashes, budgets) as `sidecarConfig`; the closing count is
`record.guardInterventions`. `GuardSidecar` refuses an insert whose statement
blocks are not, line for line, rendered from the frozen graph of the method
(`sidecar_error` before any judge call), so the cited statements always come
from the insert the judge read. Verdict fields are type-checked
(`invalid_verdict` otherwise), never coerced.

`summary()` adds, for guard conditions only and as counts: `guardConsultations`,
`guardPositiveVerdicts`, `guardInterventions`, `guardCapped`, `guardErrors`,
`guardCancelledReads`, `guardCancelledSearches`, `guardCancelledSubmissions`,
`guardJudgeTurns`, `guardJudgeReads`, `guardJudgeSearches`,
`trajectoriesWithIntervention` (k/N), plus `readsExecuted` and
`searchesExecuted` (tool turns that returned a result; `reads` and `searches`
keep counting turns, cancelled and failed ones included). Other conditions'
summaries are unchanged.
`research.graph_sidecar:GUARD` and `GUARD_SHADOW` construct the judge over the
frozen I12 Generation graph; a round that adopts the guard should add its own
factory pointing at its contexts.
