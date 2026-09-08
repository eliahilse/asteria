# Repository-to-context acquisition

Open **Context generation** in the explorer to read and copy the exact security
prompt inserts for the active task. Context acquisition runs through the research
harness; inputs, raw outputs and traces are saved in each iteration's archive.

| Strategy | What Luna investigates |
| --- | --- |
| Repository overview | Security-relevant architecture, trust boundaries, persistence, remote calls, cryptography and dependencies, related to the task. |
| Task-focused review | The feature implementation and relevant callers, callees, storage and integration points. |
| Data-flow review | Input paths, parsing, guards and sensitive operations across the relevant source files. |

These are different acquisition instructions, not predefined facts or context
types. All three use the same model, tools and default budget. The task is supplied
in every condition. No prior generation, audit, C1–C4 record or evaluator result is
loaded into the model context. Existing source within the selected game is input;
files elsewhere in the thesis workspace are not.

## Input and execution

The default targets are `apogames/Java/ApoMario` and `apogames/Java/ApoIcarus`.
Custom local targets can be configured through `ASTERIA_CONTEXT_REPOS`, a JSON
object mapping display names to source directories. The CLI accepts any explicit
source directory:

```sh
python3 -m research.generate_context \
  --repo apogames/Java/ApoMario \
  --task 'Implement persistent Highscore storage and integrate submission and display.' \
  --strategy task                          # snapshot only
# Add --execute to submit through ASTERIA_ADAPTER_COMMAND.
```

The generic [adapter protocol](ADAPTER.md) is committed. Keep the implementation,
endpoint, credentials and transport diagnostics in gitignored `.local/` and
`.env.local`. Vite reads `ASTERIA_` variables from the root `.env.local` on startup;
the CLI uses its process environment. Restart Vite after changing adapter settings.
The static GitHub Pages deployment has no generation backend or private outputs.

Snapshots include supported text source/configuration files and embedded Java
source and manifests in JAR/ZIP archives. Binary assets and unsupported formats
are listed as omitted; compiled classes are not decompiled. Symlinks, credential
file extensions, dotenv files, ignored files and common generated directories are
excluded. Git ignore rules apply when the target is in a Git worktree. Budgets
reject oversized source input rather than silently truncating it. Every source's
original-byte hash and encoding are recorded; the snapshot has a content hash.

Each attempt has a new identity, empty conversation history, a fixed snapshot,
the exact task, strategy instruction and generator source hashes. The default is
`gpt-5.6-luna`, requested medium effort, 8192 output tokens per call and 12 calls
including the final answer. Calls are fresh protocol submissions carrying that
attempt's full history; there is no conversation shared between attempts.

Luna can request literal search or bounded file ranges. Search returns up to 80
matching lines and reports truncation. Read accepts up to five ranges, 240 lines
per range and 30000 characters total. Excerpts carry explicit line numbers.
The runner supplies a single structured action tool and executes only reads of
snapshot bytes. It does not execute model-supplied or repository code.

## Outputs and checks

Each output item has a category derived by the model, a statement, task relevance,
optional CWE tags, exact source citations and a proposed check. Items distinguish:

- `security_property`: an observed property of inspected source;
- `existing_risk`: a suspected issue in existing code;
- `change_risk`: a risk relevant to implementing the task;
- `unknown`: information that could not be established.

These labels describe model claims. They are not validated vulnerabilities.
CWE tags are model annotations. Citation checking confirms that quoted bytes
match the claimed line range and that the model inspected that excerpt. It does
not establish that the cited source supports the security interpretation.

When citations fail, Luna receives source-matching feedback within the same turn
budget. Earlier candidates and feedback remain in the trace. Remaining citation
errors at the final turn are retained and labeled `citation_issues`. Uncited items
are counted separately. Missing repository or server code stays an unknown.

The explorer shows the task and the exact inserted text, without acquisition
metrics or trace panels. Full output items, citations, token usage and model
requests remain in the archived generation records.

Attempt and per-call records are written before submission. Timeouts and transport
errors are preserved; interrupted attempts are never resubmitted automatically.
The provider's reported model must match Luna. If effective settings are not
reported, the output is retained with `settings_unverified`; requested settings
are not presented as independently verified. Usage and cost are never estimated.

The main experiment does not yet inject these generated outputs. This phase is
for inspecting acquisition behavior and defining the subsequent study. Changing
the acquisition protocol changes its recorded generator hashes; outputs from
different protocol versions must not be pooled as one experimental condition.
