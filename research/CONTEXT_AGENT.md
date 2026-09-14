# Security context agent

`context_agent.py` acquires security context with an off-the-shelf coding agent
(Codex CLI, `codex exec`) instead of the purpose-built search/read loop of
`generate_security_context.py`. The input is a repository workspace and the
feature task; the instruction is one of three angles. No check, fixture,
threshold, earlier context, generated code or test outcome is supplied.

## Process

1. **Workspace.** The tracked distribution of each game is copied read-only:
   Java sources embedded in a jar are unpacked to `<Game>/src/`, loose sources
   go to `<Game>/src/`, other text files keep their paths, binaries are listed
   as omitted. The file set is fingerprinted.
2. **Code model.** [`code_model.py`](CODE_MODEL.md) parses every Java file
   into symbols (types, methods, constructors, fields) with line ranges,
   invocations, call edges and sink tags, written as `code-model.json` and a
   compact `outline.md` in the workspace, together with `ONTOLOGY.md`,
   `schema.json` and the CWE Top 25 file.
3. **Prompt.** `security/agent/common.md` (task, workspace, ground rules,
   output contract) plus one angle file:
   - `dataflow`: sources, sinks, trust boundaries, STRIDE threats per boundary,
     weakness types, requirements and controls at the first receiving operation;
   - `requirements`: assets and properties, constraints per operation of the
     feature, controls with failure behaviour, existing violations;
   - `catalog`: applicability of every 2025 CWE Top 25 entry with evidence,
     then requirements and controls for applicable entries.
   The record freezes the prompt, the schema, the code model and the hashes
   of every generator file.
4. **Run.** `codex exec` with the chosen model and reasoning effort, read-only
   sandbox, the workspace as working directory, JSONL event output and the
   context schema as output schema. The transcript, stderr, exit code and
   final message are retained. A record is submitted once; nothing is
   regenerated.
5. **Validation.** The final document is checked against the schema; every
   anchor (symbol and/or file with line range) is resolved against the source
   by the harness, and the exact cited text is hashed. Observed statements
   without a resolvable anchor are dropped and listed; properties and existing
   risks require observed basis; related ids must exist.
6. **Insert.** Validated statements are rendered as the prompt insert
   (summary, assets, boundaries, typed items with enforcement points, failure
   behaviour, resolved locations, verification, limitations). Its SHA-256 is
   recorded; the insert is appended to the unchanged task in code generation.

## Commands

```sh
python3 -m research.context_agent prepare --method Generation --angle dataflow \
  --task 'Implement a Highscore feature in ApoMario.'          # no model call
python3 -m research.context_agent execute --id agent-<hex>      # one codex run
python3 -m research.context_agent prepare ... --execute         # both
```

`--model` and `--effort` are recorded in the record and passed to Codex; the
served model identity is recorded from the transcript when Codex reports it,
otherwise the record stays `settings_unverified`. Records live under
`.local/context-generation/agent-<hex>/` with `workspace/`, `prompt.md`,
`schema.json`, `transcript.jsonl`, `stderr.log`, `final.json` and `record.json`.

## Validation and revalidation

Anchor validation is versioned (`anchor-validation-v2`). When an agent names a
symbol the code model does not know, or a range outside the named symbol, but
the file range exists, the range is kept as verified evidence, the enclosing
symbols are resolved by the harness, and the correction is recorded on the
anchor (`symbolCorrected`) and counted in `citationChecks.symbolCorrected`.
`python3 -m research.context_agent revalidate --id agent-<hex>` recomputes the
validated output, checks and insert from the frozen raw output without a model
call and appends the previous validator, match count and insert hash to
`revalidations`.

## Status

Six acquisitions (three angles × two methods) ran on 2026-09-14 with
`codex exec` and `gpt-5.6-luna`: 2.7–4.4 minutes and 26–60 shell commands
each; 7–24 statements kept; anchors verified 34/34 to 79/79 after symbol
correction, except Reuse data flow 72/78 and Reuse requirements 47/50. The
JSONL events observed are `thread.started`, `turn.started`, `turn.completed`,
`item.started` and `item.completed` with `command_execution` and
`agent_message` items; Codex reports no served model identity, so records stay
`settings_unverified`. Summary: `python3 -m research.agent_acquisitions`.

## Schema v8

`context-schema.json` v8 renames the item kind `security_property` to
`observation` (an observed fact about the code that bears on a security
property; the term *security property* itself names an asset attribute) and adds
`entry_point` to trust boundaries, so the attack surface is the set of
boundaries with `entry_point: true`. Records acquired under v7 validate under
v8 through `revalidate`: legacy kinds are renamed (counted in
`citationChecks.legacyKindsRenamed`) and their boundaries carry
`entry_point: null`.
