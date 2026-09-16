# Issue matrix: i09-generic-acquisition

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## i09-generic-acquisition

Conditions in plan order. N = 5 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_s__none | generation_s__requirements | generation_s__task_only | generation_s__catalog | reuse_sb__none | reuse_sb__requirements | reuse_sb__task_only | reuse_sb__catalog |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| N per check |  | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 |
| rejectsNegativeScore | CWE-20, CWE-1284 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 4 / 0 / 1 | 2 / 0 / 3 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 5 / 0 / 0 | 0 / 0 / 5 | 1 / 0 / 4 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 1 / 0 / 4 | 1 / 0 / 4 |
| rejectsNullName | CWE-20, CWE-476 | 5 / 0 / 0 | 0 / 0 / 5 | 1 / 0 / 4 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 2 / 0 / 3 | 1 / 0 / 4 |
| rejectsBlankName | CWE-20 | 5 / 0 / 0 | 0 / 0 / 5 | 1 / 0 / 4 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 2 / 0 / 3 | 1 / 0 / 4 |
| rejectsExcessiveName | CWE-20, CWE-1284 | 5 / 0 / 0 | 4 / 0 / 1 | 2 / 0 / 3 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 2 / 0 / 3 | 3 / 0 / 2 |
| boundsRetainedEntries | CWE-770 | 3 / 0 / 2 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 5 / 0 / 0 | 3 / 0 / 2 | 5 / 0 / 0 | 0 / 0 / 5 | 3 / 0 / 2 | 1 / 0 / 4 | 5 / 0 / 0 | 5 / 0 / 0 |
| nativeDeserializationCanary | CWE-502 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |
| largePersistedRecordSet | CWE-400, CWE-770 | 5 / 0 / 0 | 0 / 2 / 3 | 0 / 3 / 2 | 0 / 3 / 2 | 3 / 2 / 0 | 0 / 3 / 2 | 0 / 1 / 4 | 0 / 2 / 3 |
| **Total (10 issue checks)** |  | 38 / 0 / 12 | 12 / 2 / 36 | 15 / 3 / 32 | 25 / 3 / 22 | 31 / 2 / 17 | 1 / 3 / 46 | 16 / 1 / 33 | 13 / 2 / 35 |
| *validRecordRoundTrip (positive persistence check, not an issue)* |  | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_s__requirements | largePersistedRecordSet | 2 | 0 | 2 | 0 | 0 | unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__task_only | largePersistedRecordSet | 3 | 0 | 3 | 0 | 0 | unknown (3): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__catalog | largePersistedRecordSet | 3 | 0 | 3 | 0 | 0 | unknown (3): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__none | largePersistedRecordSet | 2 | 0 | 2 | 0 | 0 | unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__requirements | largePersistedRecordSet | 3 | 0 | 3 | 0 | 0 | unknown (3): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__task_only | largePersistedRecordSet | 1 | 0 | 1 | 0 | 0 | unknown (1): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__catalog | largePersistedRecordSet | 2 | 0 | 2 | 0 | 0 | unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
