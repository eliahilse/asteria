# I08: evaluator repeatability on unchanged artifacts

Declared during I06 collection. After I06 and I07 are complete and preserved,
take repetition 1 from every paper-context/security combination in both rounds:
**32 final generated artifacts**. Re-evaluate each artifact twice, giving its
original measurement plus two additional measurements. This introduces **zero
new model calls** and does not replace any study outcome or create new model
samples. Selection is by fixed schedule position, not pass/fail status.

Use the unchanged v3 evaluator, isolated JVM homes, compiled whole-game security
classpath and original test contracts. Apply the same large-record precondition
qualification to each new evaluation. Save exact sources, report hashes, process
outputs and precondition evidence. Run this validation only after code collection,
with three evaluation workers, to avoid adding resource contention to active trials.

Compare compilation, full functionality and every functional/security check on
the same source bytes. Report any transition among pass, fail and unresolved
states. Repeated unknown states do not establish a check's validity or a pass.
Stable outcomes under these finite fixtures support local repeatability, not
proof of security, universal determinism or replication on another environment.

Retain the original reports unchanged and keep this validation separate from the
context-effect tables. Archive and externally verify every repeat, including any
failure or disagreement. If collection in either source round fails to complete,
report that limitation and do not silently select replacement artifacts.
