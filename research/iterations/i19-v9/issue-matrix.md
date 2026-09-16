# Issue matrix: i19-v9

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## i19-v9

Conditions in plan order. N = 5 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_s__single_shot__none | generation_s__single_shot__static | reuse_sb__single_shot__none | reuse_sb__single_shot__static |
| --- | --- | ---: | ---: | ---: | ---: |
| N per check |  | 5 | 5 | 5 | 5 |
| rejectsNegativeScore | CWE-20, CWE-1284 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 |
| rejectsNullName | CWE-20, CWE-476 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 1 / 0 / 4 |
| rejectsBlankName | CWE-20 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 |
| rejectsExcessiveName | CWE-20, CWE-1284 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 1 / 0 / 4 |
| boundsRetainedEntries | CWE-770 | 4 / 0 / 1 | 0 / 0 / 5 | 1 / 0 / 4 | 0 / 0 / 5 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 2 / 0 / 3 |
| nativeDeserializationCanary | CWE-502 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |
| largePersistedRecordSet | CWE-400, CWE-770 | 3 / 2 / 0 | 1 / 2 / 2 | 4 / 1 / 0 | 0 / 4 / 1 |
| **Total (10 issue checks)** |  | 37 / 2 / 11 | 31 / 2 / 17 | 35 / 1 / 14 | 9 / 4 / 37 |
| *validRecordRoundTrip (positive persistence check, not an issue)* |  | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_s__single_shot__none | largePersistedRecordSet | 2 | 0 | 2 | 0 | 0 | unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | largePersistedRecordSet | 2 | 0 | 2 | 0 | 0 | unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | largePersistedRecordSet | 1 | 0 | 1 | 0 | 0 | unknown (1): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | largePersistedRecordSet | 4 | 0 | 4 | 0 | 0 | unknown (4): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
