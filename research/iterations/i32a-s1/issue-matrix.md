# Issue matrix: i32a-s1

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## i32a-s1

Conditions in plan order. N = 5 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_s__single_shot__static | reuse_sb__single_shot__static |
| --- | --- | ---: | ---: |
| N per check |  | 5 | 5 |
| rejectsNegativeScore | CWE-20, CWE-1284 | 0 / 2 / 3 | 0 / 1 / 4 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 0 / 2 / 3 | 0 / 1 / 4 |
| rejectsNullName | CWE-20, CWE-476 | 0 / 2 / 3 | 0 / 1 / 4 |
| rejectsBlankName | CWE-20 | 0 / 2 / 3 | 0 / 1 / 4 |
| rejectsExcessiveName | CWE-20, CWE-1284 | 0 / 2 / 3 | 0 / 1 / 4 |
| boundsRetainedEntries | CWE-770 | 0 / 2 / 3 | 0 / 1 / 4 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 2 / 3 | 0 / 1 / 4 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 3 / 2 / 0 | 0 / 1 / 4 |
| nativeDeserializationCanary | CWE-502 | 0 / 2 / 3 | 0 / 1 / 4 |
| largePersistedRecordSet | CWE-400, CWE-770 | 1 / 3 / 1 | 0 / 5 / 0 |
| **Total (10 issue checks)** |  | 4 / 21 / 25 | 0 / 14 / 36 |
| *validRecordRoundTrip (positive persistence check, not an issue)* |  | 0 / 2 / 3 | 0 / 1 / 4 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_s__single_shot__static | rejectsNegativeScore | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsNegativeTime | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsNullName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsBlankName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsExcessiveName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | boundsRetainedEntries | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | malformedStoreDoesNotCrash | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | oversizedPhysicalLine | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | nativeDeserializationCanary | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | largePersistedRecordSet | 3 | 2 | 1 | 0 | 0 | not_run (2): check not executed; unknown (1): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | validRecordRoundTrip | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | rejectsNegativeScore | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | rejectsNegativeTime | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | rejectsNullName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | rejectsBlankName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | rejectsExcessiveName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | boundsRetainedEntries | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | malformedStoreDoesNotCrash | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | oversizedPhysicalLine | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | nativeDeserializationCanary | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | largePersistedRecordSet | 5 | 1 | 4 | 0 | 0 | not_run (1): check not executed; unknown (4): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | validRecordRoundTrip | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
