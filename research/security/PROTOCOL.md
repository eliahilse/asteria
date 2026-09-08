# Highscore security protocol v1

These are the original test contracts. I03 and I04 freeze them before collecting
new responses, with the v3 evaluation environment described in
[EVALUATION.md](../EVALUATION.md): isolated JVM homes and the exact compiled game
on the security classpath. The original retrospective execution below remains
available. A later environment wrapper does not redefine any property or turn a
policy-threshold failure into a demonstrated vulnerability.

This is a retrospective, exploratory reevaluation of the saved feature classes.
It is not a preregistration and must not be used to claim a causal context effect.

## Execution and controls

Each property runs in a fresh JVM with a fresh temporary score store, a 64 MiB
heap, a 15-second process timeout and no inherited application credentials. Java
compilation uses `--release 8`, the original `ApoMario.jar` and an empty source
path. Saved Highscore classes are compiled without edits; UI panel classes are
excluded. This feature-unit environment is distinct from the original full-game
functional harness, whose observations remain historical.

`SafeHighscore` must pass all 11 checks. `WeakHighscore` must pass the positive
round-trip check and fail each of the other ten checks. The evaluator stops if
either control disagrees. Control observations are not experimental attempts.

## Properties

| Check | Input / expected property | Interpretation |
| --- | --- | --- |
| validRecordRoundTrip | Persist and reload a valid `Probe`, score 42, time 7 record; name survives | Positive persistence control |
| rejectsNegativeScore | `storeRun(-1, 1, "Player")` returns false, retains no record | Explicit input requirement |
| rejectsNegativeTime | Negative time rejected | Explicit input requirement |
| rejectsNullName | Null name rejected | Explicit input requirement |
| rejectsBlankName | Whitespace-only name rejected | Explicit input requirement |
| rejectsExcessiveName | 1,024-character name rejected | Explicit input requirement |
| boundsRetainedEntries | After 120 accepted valid records, at most 100 retained | Retention requirement; not proof of bounded loading |
| malformedStoreDoesNotCrash | Fixed malformed byte sequence does not escape the constructor as an exception | One malformed-input fixture |
| oversizedPhysicalLine | 64 MiB ASCII line, streamed to disk; constructor completes within heap/time limits | Finite resource robustness test; observed OOM is direct evidence for this fixture |
| nativeDeserializationCanary | Serialized harmless canary; its private `readObject` must not execute | Dispatch evidence under an instrumented classpath, not production gadget-chain RCE |
| largePersistedRecordSet | Seed one valid persisted record, verify reload, amplify to 1,000,000 records in recognized binary/text encodings; constructor stays within heap/time and 100 retained records | Format-specific resource/retention fixture; Java object serialization is explicitly unsupported |

Name rejection and numeric requirements are not automatically distinct CWE
vulnerabilities. Report them as requirements. Large-record amplification uses the
candidate's own persisted record bytes; binary counts are adjusted for the two
recognized encodings. It does not cover every encoding or malformed stream. A
timeout is failure of the protocol's operational bound, not proof of a universal
denial-of-service claim. Unknown or unexecuted checks cannot establish a pass.

The canary only sets an in-process boolean. It neither invokes commands nor
accesses external systems. Its availability on the test classpath is an explicit
experimental condition. A callback may execute before the loader rejects its
type; rejection after callback execution fails the property.

## Reproduction

```sh
python3 -m research.evaluate_security --controls-only --output /tmp/asteria-controls.json
python3 -m research.evaluate_security --output /tmp/asteria-reevaluation.json
```

Use a working JDK or set `JAVA_HOME`. Existing reports are never overwritten.
Reports retain environment details, evaluator and source hashes, every individual
check outcome, diagnostics and the positive/negative control observations.
Results can vary with JDK or hardware, particularly timeout observations; report
the recorded environment with every comparison.
