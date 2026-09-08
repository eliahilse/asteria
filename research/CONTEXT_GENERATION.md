# Repository-to-context acquisition

The explorer's **Context generation** tab shows the task and the exact security
text inserted into code-generation prompts. Counts, evidence and complete model
exchanges remain in exported results and iteration archives.

## Current strategies

I03 uses `generate_security_context.py`, protocol
`repository-security-context-v3-explicit-unknowns`:

| Acquisition strategy | What Luna investigates | Prompt insert |
| --- | --- | --- |
| Overview | Relevant architecture, entry points, trust boundaries, persistence, dependencies, remote calls and cryptography. | A high-level security map with uncertainty, without detailed implementation guards. |
| Requirements | The feature's security obligations and the operations where safeguards belong. | Prospective validation, persistence, resource-use and failure-handling recommendations, with repository observations where available. |
| Trust boundaries | Sources, transformations, sensitive operations and guards across related files, including persisted bytes and new records. | Data-flow risks and proposed guards before relevant operations, with unresolved edges explicit. |

I06 introduces an exploratory **Operational guards** strategy through
`operational_context.py`. It reuses the frozen v3 search, read and evidence
machinery in a dedicated process and adds five explicit fields: operation,
untrusted input, invariant, enforcement point and failure behavior. These fields
are mandatory for proposed guards and may be unset for unknowns. Validation checks
representation and references, not whether a guard is correct. I06's protocol
is `repository-security-context-v4-operational-fields`; I03/I04/I05 remain unchanged.

The no-security control adds no insert. Strategy is the acquisition instruction;
content kind labels an item produced by the model. They are separate concepts and
are not independently manipulated here. Prompt length and added information also
vary, so this design does not isolate the effect of security wording alone.

## Start from repository and task

Each acquisition has a new identity and empty conversation history. Generation
sees ApoMario; reuse additionally sees the ApoIcarus donor. Both receive the
relevant Highscore task. They receive no S/F/B JSON, prior generated code, audit,
earlier context, evaluation feedback, test code or security fixture thresholds.
The researcher supplies a general security perspective as the instruction.

The immutable snapshot indexes supported source/configuration text plus embedded
Java source and manifests in JAR/ZIP archives. Compiled classes are not decompiled.
Omitted binaries and unsupported files remain listed. Symlinks, ignored files,
dotenv files, credential types and common generated directories are excluded.
Original-byte hashes, encodings and a snapshot fingerprint are recorded.

Literal search returns up to 80 matching lines with truncation disclosed. Read
accepts 1–5 ranges, at most 240 lines each and 30,000 characters total. Acquisition
executes no repository or model-supplied code.

Every returned excerpt has a stable evidence ID. The model selects IDs; the
harness resolves exact inspected source text and locations. This avoids the
quote/line-number reproduction errors that removed many I01 recommendations.
Inspection matching does not establish that source entails a security claim.

## Item representation

Each item has an ID, kind, basis, model-derived topic, statement, task relevance,
optional CWE annotations, inspected evidence IDs and a suggested verification.

| Kind | Meaning |
| --- | --- |
| `security_property` | A claimed observed property of inspected source. |
| `existing_risk` | A suspected risk in existing code, with inspected evidence. |
| `change_risk` | A risk associated with implementing the task. |
| `recommendation` | A prospective safeguard, not a claim that it already exists. |
| `unknown` | Information the acquisition could not establish. |

Basis is `observed`, `task` or `reasoned`. Observed properties and existing risks
require inspected references. Prospective recommendations can be uncited when
their basis is explicit. An uncited unknown stays unverified: a search miss is
not proof of absence. CWE labels and suggested checks are model annotations;
they are not validated findings or evidence that a test ran.

The insert contains the literal generated summary, typed statements, relevance,
source locations, suggested verification and limitations. Complete quoted excerpts
remain in the acquisition record. Each insert has a SHA-256 and is appended after
the unchanged task/paper attachments. The control omits it.

## Execution and preservation

The current budget is 16 model turns including the final answer, with requested
medium reasoning and 8,192 output tokens per turn on Luna. Every turn carries that
acquisition's retained conversation; acquisitions share no conversation history.

Requests are saved before submission. Responses, candidates and validation errors
are retained. Invalid output receives feedback within the fixed budget. A failed
acquisition cannot silently become an empty control. An interrupted request is
not automatically resubmitted. I02's failure and the fresh all-arm replacement
in I03 are documented in the iteration index.

The generic [adapter protocol](ADAPTER.md) is committed. Its implementation,
endpoint, credentials and provider transport traces stay gitignored. Calls use
`ASTERIA_ADAPTER_COMMAND`; returned model/request identities must match. Missing
provider attestation of settings remains `settings_unverified`.

Each experiment freezes acquisition records, snapshots, exact tasks, inserts and
producing source hashes before code collection. Completed evidence is archived,
committed and copied outside the repo. The static site publishes committed
results and inserts; it has no model-calling backend.

## Earlier protocols

`generate_context.py` remains the original exact-quote acquisition implementation.
The original matrix's overview/task/flow arms and I01's `iteration_contexts.py`
use it. Its CLI accepts `--repo`, `--task`, `--strategy` and optional `--execute`.
Do not edit frozen implementations to reinterpret earlier outputs; use producing
hashes and each iteration's plan when reproducing historical results.
