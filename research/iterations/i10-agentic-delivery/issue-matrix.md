# Issue matrix: i10-agentic-delivery

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## i10-agentic-delivery

Conditions in plan order. N = 5 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_s__single_shot__none | generation_s__single_shot__static | generation_s__agentic__none | generation_s__agentic__static | generation_s__agentic__adaptive | reuse_sb__single_shot__none | reuse_sb__single_shot__static | reuse_sb__agentic__none | reuse_sb__agentic__static | reuse_sb__agentic__adaptive |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| N per check |  | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 |
| rejectsNegativeScore | CWE-20, CWE-1284 | 5 / 0 / 0 | 0 / 0 / 5 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 5 / 0 / 0 | 0 / 0 / 5 | 5 / 0 / 0 | 0 / 0 / 5 | 3 / 1 / 1 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 5 / 0 / 0 | 0 / 0 / 5 | 4 / 0 / 1 | 0 / 0 / 5 | 0 / 0 / 5 | 5 / 0 / 0 | 0 / 0 / 5 | 5 / 0 / 0 | 0 / 0 / 5 | 3 / 1 / 1 |
| rejectsNullName | CWE-20, CWE-476 | 5 / 0 / 0 | 0 / 0 / 5 | 4 / 0 / 1 | 0 / 0 / 5 | 0 / 0 / 5 | 5 / 0 / 0 | 0 / 0 / 5 | 5 / 0 / 0 | 0 / 0 / 5 | 3 / 1 / 1 |
| rejectsBlankName | CWE-20 | 5 / 0 / 0 | 4 / 0 / 1 | 4 / 0 / 1 | 3 / 0 / 2 | 4 / 0 / 1 | 5 / 0 / 0 | 0 / 0 / 5 | 5 / 0 / 0 | 0 / 0 / 5 | 3 / 1 / 1 |
| rejectsExcessiveName | CWE-20, CWE-1284 | 5 / 0 / 0 | 0 / 0 / 5 | 5 / 0 / 0 | 0 / 0 / 5 | 1 / 0 / 4 | 5 / 0 / 0 | 0 / 0 / 5 | 5 / 0 / 0 | 0 / 0 / 5 | 3 / 1 / 1 |
| boundsRetainedEntries | CWE-770 | 3 / 0 / 2 | 1 / 0 / 4 | 1 / 0 / 4 | 0 / 0 / 5 | 1 / 0 / 4 | 0 / 0 / 5 | 4 / 0 / 1 | 2 / 0 / 3 | 5 / 0 / 0 | 1 / 1 / 3 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 1 / 4 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 4 / 0 / 1 | 0 / 0 / 5 | 5 / 0 / 0 | 0 / 0 / 5 | 1 / 0 / 4 | 5 / 0 / 0 | 0 / 0 / 5 | 4 / 0 / 1 | 5 / 0 / 0 | 4 / 1 / 0 |
| nativeDeserializationCanary | CWE-502 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 1 / 4 |
| largePersistedRecordSet | CWE-400, CWE-770 | 1 / 4 / 0 | 0 / 5 / 0 | 4 / 1 / 0 | 0 / 5 / 0 | 1 / 4 / 0 | 5 / 0 / 0 | 0 / 4 / 1 | 4 / 1 / 0 | 0 / 4 / 1 | 3 / 2 / 0 |
| **Total (10 issue checks)** |  | 33 / 4 / 13 | 5 / 5 / 40 | 32 / 1 / 17 | 3 / 5 / 42 | 8 / 4 / 38 | 35 / 0 / 15 | 4 / 4 / 42 | 35 / 1 / 14 | 10 / 4 / 36 | 23 / 11 / 16 |
| *validRecordRoundTrip (positive persistence check, not an issue)* |  | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 1 / 4 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_s__single_shot__none | largePersistedRecordSet | 4 | 0 | 4 | 0 | 0 | unknown (4): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | largePersistedRecordSet | 5 | 0 | 5 | 0 | 0 | unknown (5): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__none | largePersistedRecordSet | 1 | 0 | 1 | 0 | 0 | unknown (1): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__static | largePersistedRecordSet | 5 | 0 | 5 | 0 | 0 | unknown (5): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__adaptive | largePersistedRecordSet | 4 | 0 | 4 | 0 | 0 | unknown (4): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | largePersistedRecordSet | 4 | 0 | 4 | 0 | 0 | unknown (4): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | largePersistedRecordSet | 1 | 0 | 1 | 0 | 0 | unknown (1): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static | largePersistedRecordSet | 4 | 0 | 4 | 0 | 0 | unknown (4): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__adaptive | rejectsNegativeScore | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__adaptive | rejectsNegativeTime | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__adaptive | rejectsNullName | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__adaptive | rejectsBlankName | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__adaptive | rejectsExcessiveName | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__adaptive | boundsRetainedEntries | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__adaptive | malformedStoreDoesNotCrash | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__adaptive | oversizedPhysicalLine | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__adaptive | nativeDeserializationCanary | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__adaptive | largePersistedRecordSet | 2 | 0 | 1 | 1 | 0 | unknown (1): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__adaptive | validRecordRoundTrip | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
