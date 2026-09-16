# Issue matrix: i22-oneshot

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## i22-oneshot

Conditions in plan order. N = 5 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_s__single_shot__none | generation_s__single_shot__static | reuse_sb__single_shot__none | reuse_sb__single_shot__static |
| --- | --- | ---: | ---: | ---: | ---: |
| N per check |  | 5 | 5 | 5 | 5 |
| rejectsNegativeScore | CWE-20, CWE-1284 | 5 / 0 / 0 | 0 / 3 / 2 | 1 / 4 / 0 | 0 / 3 / 2 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 5 / 0 / 0 | 0 / 3 / 2 | 1 / 4 / 0 | 0 / 3 / 2 |
| rejectsNullName | CWE-20, CWE-476 | 5 / 0 / 0 | 0 / 3 / 2 | 1 / 4 / 0 | 0 / 3 / 2 |
| rejectsBlankName | CWE-20 | 5 / 0 / 0 | 0 / 3 / 2 | 1 / 4 / 0 | 0 / 3 / 2 |
| rejectsExcessiveName | CWE-20, CWE-1284 | 5 / 0 / 0 | 0 / 3 / 2 | 1 / 4 / 0 | 0 / 3 / 2 |
| boundsRetainedEntries | CWE-770 | 2 / 0 / 3 | 1 / 3 / 1 | 0 / 4 / 1 | 1 / 3 / 1 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 0 / 5 | 0 / 3 / 2 | 0 / 4 / 1 | 0 / 3 / 2 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 5 / 0 / 0 | 2 / 3 / 0 | 1 / 4 / 0 | 1 / 3 / 1 |
| nativeDeserializationCanary | CWE-502 | 0 / 0 / 5 | 0 / 3 / 2 | 0 / 4 / 1 | 0 / 3 / 2 |
| largePersistedRecordSet | CWE-400, CWE-770 | 5 / 0 / 0 | 0 / 5 / 0 | 0 / 5 / 0 | 1 / 3 / 1 |
| **Total (10 issue checks)** |  | 37 / 0 / 13 | 3 / 32 / 15 | 6 / 41 / 3 | 3 / 30 / 17 |
| *validRecordRoundTrip (positive persistence check, not an issue)* |  | 0 / 0 / 5 | 0 / 3 / 2 | 0 / 4 / 1 | 0 / 3 / 2 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_s__single_shot__static | rejectsNegativeScore | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsNegativeTime | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsNullName | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsBlankName | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsExcessiveName | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | boundsRetainedEntries | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | malformedStoreDoesNotCrash | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | oversizedPhysicalLine | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | nativeDeserializationCanary | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | largePersistedRecordSet | 5 | 3 | 2 | 0 | 0 | not_run (3): check not executed; unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | validRecordRoundTrip | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | rejectsNegativeScore | 4 | 3 | 0 | 1 | 0 | not_run (3): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | rejectsNegativeTime | 4 | 3 | 0 | 1 | 0 | not_run (3): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | rejectsNullName | 4 | 3 | 0 | 1 | 0 | not_run (3): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | rejectsBlankName | 4 | 3 | 0 | 1 | 0 | not_run (3): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | rejectsExcessiveName | 4 | 3 | 0 | 1 | 0 | not_run (3): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | boundsRetainedEntries | 4 | 3 | 0 | 1 | 0 | not_run (3): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | malformedStoreDoesNotCrash | 4 | 3 | 0 | 1 | 0 | not_run (3): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | oversizedPhysicalLine | 4 | 3 | 0 | 1 | 0 | not_run (3): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | nativeDeserializationCanary | 4 | 3 | 0 | 1 | 0 | not_run (3): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | largePersistedRecordSet | 5 | 3 | 1 | 1 | 0 | not_run (3): check not executed; unknown (1): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | validRecordRoundTrip | 4 | 3 | 0 | 1 | 0 | not_run (3): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | rejectsNegativeScore | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | rejectsNegativeTime | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | rejectsNullName | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | rejectsBlankName | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | rejectsExcessiveName | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | boundsRetainedEntries | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | malformedStoreDoesNotCrash | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | oversizedPhysicalLine | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | nativeDeserializationCanary | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | largePersistedRecordSet | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | validRecordRoundTrip | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
