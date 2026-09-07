# Highscore: controlled security reevaluation

Protocol: [`highscore-security-v1`](../research/security/PROTOCOL.md).
Machine-readable results: [`highscore-security-v1.json`](../research/results/highscore-security-v1.json).

The four historically compiling fresh-pilot outputs and six selected published
functional successes were evaluated without editing their saved Highscore
classes. Each of 11 checks ran in its own JVM and temporary directory, with a
64 MiB heap and a 15-second process budget. Both the corrected and intentionally
vulnerable controls produced all expected outcomes before evaluating the saved
outputs. This is a retrospective convenience sample, not a causal experiment.

| Fresh output | Security context | Checks passed | Checks failed | Unknown | New resource observation |
| --- | --- | ---: | ---: | ---: | --- |
| Generation r1 | No | 5 | 6 | 0 | Million-record load exhausted heap |
| Generation r2 | No | 4 | 7 | 0 | Large line and million-record load exhausted heap |
| Reuse r1 | No | 3 | 8 | 0 | Large line exhausted heap; million-record load exceeded process budget |
| Generation r1 | Yes | 10 | 1 | 0 | Large physical line exhausted heap despite a retained-entry cap |

All four fresh observations above used GPT-5.4 Mini at `none` reasoning. The
security-context output satisfies the six previously unexecuted API requirements
but fails the new physical-line resource check. Its historical source review
remains recorded as "free of the fixed targeted findings" with its original
residual-risk note. The new observation narrows that conclusion; neither record
is overwritten or silently reconciled into a universal "secure" label.

The three selected published reuse outputs executed the harmless serialization
canary callback before object-type rejection. This establishes deserialization
dispatch on the instrumented test classpath. It does not demonstrate an available
production gadget chain or remote code execution. Their million-record check is
`unknown` because that adapter does not support Java object serialization.

The three selected published generation outputs each failed resource/retention
checks; detailed per-check outcomes and diagnostics are in the machine record.
Every selected output failed at least one declared property. The twelve fresh
outputs that failed historical main compilation were not reevaluated and are not
counted as security-test failures.

## What this contributes

- 110 individual experimental check observations, plus 22 control observations.
- An executable counterexample to interpreting a retained-record cap as bounded
  input processing.
- Separate evidence for API requirements, finite resource fixtures and native
  deserialization dispatch.
- Original source hashes, environment, unsupported cases and diagnostics.

These check counts are not sample sizes for a statistical security-effect test.
The original attempt is the sampling unit. Tests overlap, requirements need a
declared threat model, and a passing fixture cannot establish universal safety.
The suite does not cover all network, path, cryptographic or dependency risks.
