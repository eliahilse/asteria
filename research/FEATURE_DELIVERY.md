# Feature delivery calibration

The initial paper replay and security follow-up mostly delivered a standalone
`ApoMarioHighscore.java`. Across their 160 requests, 158 responses matched Luna
and 153 compiled. None passed all sixteen functional checks. All 153 compilable
responses failed the four autonomous checks that require actual run-end recording;
the fifth autonomous check, for phantom entries, passed. Only five accepted
responses failed compilation. Missing game wiring was the main failure mode.

`feature_delivery.py` tests a different delivery protocol before a new study:

- Supply the original Highscore task and the same three ApoMario source files
  used by Generation, starting with no added security context.
- Request complete new Java classes and exact replacements in existing classes
  through `submit_feature_changes`. Filenames are basenames. Reject ambiguous
  anchors and apply a submission transactionally. Retain unrelated source members.
- Require changes to the existing game classes; reconstruct complete files for
  the unchanged evaluator. Declare Java 8 compatibility and milliseconds for the
  numeric survival-time API; `mm:ss` is a presentation format.
- Allow at most three model submissions. Return compiler diagnostics and the
  sixteen functional outcomes as feedback. Security outcomes are retained locally
  but never fed back to the model. A provider/model identity mismatch stops the run.

The adapter remains provider-neutral. Configure the gitignored
`ASTERIA_ADAPTER_COMMAND` as described in [ADAPTER.md](ADAPTER.md), then run:

```sh
python3 -m research.feature_delivery           # describe only
python3 -m research.feature_delivery --execute
```

Each invocation creates a fresh `.local/delivery-calibration/delivery-…` folder
with the protocol source snapshot, requests, responses, exact edits, reconstructed
files, evaluator reports and hashes. An interrupted invocation is preserved;
another invocation starts a new calibration. It does not resume or replace it.
Effective settings remain unverified when the service does not attest them.

## Development observations, 2026-09-08

Two calibrations were run through the private async adapter with returned model
identity `gpt-5.6-luna`. Both requested medium reasoning and a 65,536-token output
limit; effective settings were not attested.

| Calibration | Submission | Delivery | Compile | Unit / 7 | Invoked / 4 | Autonomous / 5 |
| --- | ---: | --- | --- | ---: | ---: | ---: |
| Initial protocol | 1 | Rejected package-path filenames | — | — | — | — |
| Initial protocol | 2 | Accepted | Pass | 7 | 3 | 4 |
| Initial protocol | 3 | Accepted | Pass | 7 | 3 | 4 |
| Explicit basenames and time units | 1 | Accepted | Fail | — | — | — |
| Explicit basenames and time units | 2 | Accepted | Pass | 7 | 4 | 5 |

The initial run stored seconds where the tests expected milliseconds. After that
contract was made explicit, the first submission omitted `getHighscore()` from
`ApoMarioPanel`; compiler feedback led to the missing panel edits in submission
two. That output passed all sixteen functional checks. No human code repair or
test weakening was used. Its isolated security result was 4/11 passes, 7/11 fails.

The successful calibration's private ID is
`delivery-10a9e94cb2fa4b9d9aaadffe962bd575`; its protocol snapshot SHA-256 is
`b6bab39b91908838a620e3f59d7992d9f2682e042f3d930d675431d2c37a07eb`.
Subsequent runner changes improve error classification and provenance recording;
the saved snapshot identifies the version that produced this observation.

This establishes that the delivery workflow can produce code which passes the
existing integration checks. It is one development trajectory with test feedback,
not an estimate of single-response success. The sixteen checks do not exercise
menu navigation or rendering: inspection found a generated Highscore view and
getter without a menu call site, despite all sixteen checks passing. Full feature
completion therefore remains broader than this test suite's success criterion.

The old 160 observations remain unchanged and separate. A future matrix using
this delivery protocol needs a new frozen study version and fresh controls with
the same submission and feedback budget for every treatment. Compare first-submit
success and success within that budget separately, recording cumulative calls and
tokens. A pass after repair must not become a first-submit pass.
