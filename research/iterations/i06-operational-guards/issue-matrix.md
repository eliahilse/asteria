# Issue matrix: i06-operational-guards

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## i06-operational-guards

Conditions in plan order. N = 5 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_s__none | generation_s__operations | generation_s__requirements | generation_s__boundaries | generation_sfb__none | generation_sfb__operations | generation_sfb__requirements | generation_sfb__boundaries | reuse_b__none | reuse_b__operations | reuse_b__requirements | reuse_b__boundaries | reuse_sb__none | reuse_sb__operations | reuse_sb__requirements | reuse_sb__boundaries |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| N per check |  | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 |
| rejectsNegativeScore | CWE-20, CWE-1284 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 3 / 0 / 2 | 0 / 0 / 5 | 5 / 0 / 0 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 5 / 0 / 0 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 3 / 0 / 2 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 4 / 0 / 1 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 4 / 0 / 1 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 4 / 0 / 1 |
| rejectsNullName | CWE-20, CWE-476 | 4 / 0 / 1 | 1 / 0 / 4 | 0 / 0 / 5 | 2 / 0 / 3 | 4 / 0 / 1 | 0 / 0 / 5 | 0 / 0 / 5 | 4 / 0 / 1 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 3 / 0 / 2 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 5 / 0 / 0 |
| rejectsBlankName | CWE-20 | 4 / 0 / 1 | 2 / 0 / 3 | 0 / 0 / 5 | 2 / 0 / 3 | 4 / 0 / 1 | 0 / 0 / 5 | 1 / 0 / 4 | 4 / 0 / 1 | 5 / 0 / 0 | 0 / 0 / 5 | 1 / 0 / 4 | 4 / 0 / 1 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 5 / 0 / 0 |
| rejectsExcessiveName | CWE-20, CWE-1284 | 5 / 0 / 0 | 5 / 0 / 0 | 1 / 0 / 4 | 5 / 0 / 0 | 5 / 0 / 0 | 4 / 0 / 1 | 0 / 0 / 5 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 5 / 0 / 0 |
| boundsRetainedEntries | CWE-770 | 4 / 0 / 1 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 3 / 0 / 2 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 2 / 0 / 3 | 1 / 0 / 4 | 0 / 0 / 5 | 1 / 0 / 4 | 0 / 0 / 5 | 2 / 0 / 3 | 0 / 0 / 5 | 0 / 0 / 5 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 5 / 0 / 0 | 0 / 0 / 5 | 3 / 0 / 2 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 4 / 0 / 1 | 5 / 0 / 0 | 4 / 0 / 1 | 0 / 0 / 5 | 2 / 0 / 3 | 0 / 0 / 5 | 4 / 0 / 1 | 0 / 0 / 5 | 2 / 0 / 3 | 0 / 0 / 5 |
| nativeDeserializationCanary | CWE-502 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |
| largePersistedRecordSet | CWE-400, CWE-770 | 3 / 2 / 0 | 0 / 1 / 4 | 0 / 3 / 2 | 0 / 4 / 1 | 3 / 2 / 0 | 0 / 3 / 2 | 0 / 3 / 2 | 0 / 4 / 1 | 3 / 2 / 0 | 0 / 2 / 3 | 0 / 3 / 2 | 0 / 4 / 1 | 2 / 3 / 0 | 0 / 5 / 0 | 0 / 1 / 4 | 0 / 3 / 2 |
| **Total (10 issue checks)** |  | 35 / 2 / 13 | 13 / 1 / 36 | 9 / 3 / 38 | 22 / 4 / 24 | 34 / 2 / 14 | 9 / 3 / 38 | 10 / 3 / 37 | 27 / 4 / 19 | 34 / 2 / 14 | 4 / 2 / 44 | 3 / 3 / 44 | 22 / 4 / 24 | 31 / 3 / 16 | 7 / 5 / 38 | 2 / 1 / 47 | 24 / 3 / 23 |
| *validRecordRoundTrip (positive persistence check, not an issue)* |  | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_s__none | largePersistedRecordSet | 2 | 0 | 2 | 0 | 0 | unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__operations | largePersistedRecordSet | 1 | 0 | 1 | 0 | 0 | unknown (1): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__requirements | largePersistedRecordSet | 3 | 0 | 3 | 0 | 0 | unknown (3): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__boundaries | largePersistedRecordSet | 4 | 0 | 4 | 0 | 0 | unknown (4): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__none | largePersistedRecordSet | 2 | 0 | 2 | 0 | 0 | unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__operations | largePersistedRecordSet | 3 | 0 | 3 | 0 | 0 | unknown (3): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__requirements | largePersistedRecordSet | 3 | 0 | 3 | 0 | 0 | unknown (3): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__boundaries | largePersistedRecordSet | 4 | 0 | 4 | 0 | 0 | unknown (4): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_b__none | largePersistedRecordSet | 2 | 0 | 2 | 0 | 0 | unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_b__operations | largePersistedRecordSet | 2 | 0 | 2 | 0 | 0 | unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_b__requirements | largePersistedRecordSet | 3 | 0 | 3 | 0 | 0 | unknown (3): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_b__boundaries | largePersistedRecordSet | 4 | 0 | 4 | 0 | 0 | unknown (4): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__none | largePersistedRecordSet | 3 | 0 | 3 | 0 | 0 | unknown (3): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__operations | largePersistedRecordSet | 5 | 0 | 5 | 0 | 0 | unknown (5): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__requirements | largePersistedRecordSet | 1 | 0 | 1 | 0 | 0 | unknown (1): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__boundaries | largePersistedRecordSet | 3 | 0 | 3 | 0 | 0 | unknown (3): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
