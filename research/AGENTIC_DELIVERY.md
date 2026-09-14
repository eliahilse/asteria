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
  `{'files': set, 'symbols': set, 'queries': list, 'stage': 'after_read' | 'before_submit'}`:
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
