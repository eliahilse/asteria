# Local model adapter

The committed runner knows the experiment protocol, not your provider. Configure
an executable command in `ASTERIA_ADAPTER_COMMAND`, a JSON argument array. It runs
without a shell and inherits your environment. Keep your implementation in
`.local/` and secrets in your environment manager or `.env`; both locations are
gitignored. The runner does not automatically execute/source dotenv files.

```sh
export ASTERIA_ADAPTER_COMMAND='["python3", ".local/model_adapter.py"]'
python3 -m research.run_experiment                         # validate only
python3 -m research.run_experiment --execute --limit 1     # one new attempt
python3 -m research.run_experiment --execute --stage bridge --limit 20
python3 -m research.run_experiment --execute --stage ablation --limit 120
```

Alternatively, an HTTP service can implement the same JSON protocol:

```sh
export ASTERIA_ADAPTER_COMMAND='["python3", "-m", "research.adapters.http_json"]'
export ASTERIA_MODEL_ENDPOINT='https://your-service.example/generate'
# Configure ASTERIA_MODEL_TOKEN privately if the service uses a bearer token.
```

The HTTP adapter forwards JSON unchanged, does not retry or follow redirects,
and uses a 540-second timeout (`ASTERIA_HTTP_TIMEOUT` overrides it). For an
existing provider with a different schema, write a private adapter translating
the request and response. No changes to the committed experiment code are needed.

## Protocol v1

Each process receives one JSON object on stdin and must print one JSON object on
stdout. Put diagnostics in a private log. A nonzero exit is recorded generically;
provider stderr, URLs, command arguments and environment variables are not saved.

Request (the actual prompt is the exact frozen file, with no adapter-added text):

```json
{
  "protocol_version": 1,
  "request_id": "luna_medium__bridge_generation_s__r1",
  "model": "gpt-5.6-luna",
  "messages": [{"role": "user", "content": "<exact frozen prompt>"}],
  "settings": {"reasoning_effort": "medium", "temperature": null, "max_output_tokens": 65536}
}
```

Response:

```json
{
  "protocol_version": 1,
  "request_id": "luna_medium__bridge_generation_s__r1",
  "model": "gpt-5.6-luna",
  "settings": {"reasoning_effort": "medium", "temperature": null, "max_output_tokens": 65536},
  "output_text": "<unmodified generated response text>",
  "finish_reason": "stop",
  "usage": {"input_tokens": 12000, "output_tokens": 4000, "reasoning_tokens": null, "cached_input_tokens": null},
  "cost_usd": null
}
```

Context acquisition also sends `"response_format": {"type": "json_object"}`.
Adapters used for this workflow must forward the requested structured response
format. Context requests contain the complete multi-turn message history; each
invocation still submits exactly one new model request. An adapter must not add
context from other generations. If effective settings are not reported by the
service, return `null` settings; context outputs retain that uncertainty.

These are schema examples, not model observations. `null` temperature means omit
the provider parameter. Return the served model and effective settings honestly;
use `null` settings when unknown. Unknown/different settings or request IDs are
recorded as `settings_unverified`, preserving the response. The runner cannot
independently prove a provider's self-reported settings. Missing usage and cost
remain null; token pricing is never substituted for billed cost. Do not substitute
models, add system messages, modify prompts, or silently retry within the adapter.
Preserve a provider response privately if translation requires an audit trail.

## Observation lifecycle

Each request is hashed and durably recorded as `started` before submission.
Completion saves response, elapsed wall time, runner hashes and manifest identity.
`completed` means model transport completed with matching reported settings; it
does **not** mean the generated feature compiles, works or passes security checks.
`evaluation` stays null until a separate evaluator produces observations.

Default output is `.local/runs/luna-highscore-v1/`, never included automatically
in the public workbench. Inspect observations before explicitly publishing them.
One OS-level lock prevents concurrent submissions to the same output directory.
Keep one canonical output directory for each study: a second directory cannot
discover submissions made in the first.

Resume skips **every** existing attempt record, including failures and interrupted
`started` records. A local timeout cannot establish cancellation at the remote
service. Reconcile uncertain requests with the provider using `request_id` before
any explicit rerun; preserve the original attempt and record a new study/attempt
identity and reason. There is deliberately no automatic retry command.

Evaluate a received observation with the [response evaluation command](EVALUATION.md).
It verifies the frozen request, runs the original functional suites and the
controlled security protocol, and writes a separate local report.

The manifest freezes 20 bridge and 120 ablation attempts. These exploratory
sample sizes are not a power calculation. Run the bridge first and inspect
integration/tooling before the ablation. Any protocol change after observing
results needs a new version and an explicit deviation record.
