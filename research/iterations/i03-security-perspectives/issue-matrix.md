# Issue matrix: i03-security-perspectives

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## i03-security-perspectives

Conditions in plan order. N = 5 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_sfb__none | generation_sfb__overview | generation_sfb__requirements | generation_sfb__boundaries | reuse_b__none | reuse_b__overview | reuse_b__requirements | reuse_b__boundaries |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| N per check |  | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 |
| rejectsNegativeScore | CWE-20, CWE-1284 | 5 / 0 / 0 | 4 / 1 / 0 | 0 / 0 / 5 | 4 / 0 / 1 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 2 / 0 / 3 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 5 / 0 / 0 | 3 / 1 / 1 | 0 / 0 / 5 | 0 / 0 / 5 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 1 / 0 / 4 |
| rejectsNullName | CWE-20, CWE-476 | 5 / 0 / 0 | 3 / 1 / 1 | 0 / 0 / 5 | 0 / 0 / 5 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 |
| rejectsBlankName | CWE-20 | 5 / 0 / 0 | 4 / 1 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 |
| rejectsExcessiveName | CWE-20, CWE-1284 | 5 / 0 / 0 | 4 / 1 / 0 | 0 / 0 / 5 | 3 / 0 / 2 | 5 / 0 / 0 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 |
| boundsRetainedEntries | CWE-770 | 0 / 0 / 5 | 0 / 1 / 4 | 0 / 0 / 5 | 0 / 0 / 5 | 2 / 0 / 3 | 2 / 0 / 3 | 4 / 0 / 1 | 0 / 0 / 5 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 0 / 5 | 0 / 1 / 4 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 5 / 0 / 0 | 4 / 1 / 0 | 3 / 0 / 2 | 3 / 0 / 2 | 5 / 0 / 0 | 5 / 0 / 0 | 4 / 0 / 1 | 5 / 0 / 0 |
| nativeDeserializationCanary | CWE-502 | 0 / 0 / 5 | 0 / 1 / 4 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |
| largePersistedRecordSet | CWE-400, CWE-770 | 5 / 0 / 0 | 0 / 5 / 0 | 0 / 4 / 1 | 0 / 2 / 3 | 4 / 1 / 0 | 3 / 2 / 0 | 1 / 4 / 0 | 0 / 1 / 4 |
| **Total (10 issue checks)** |  | 35 / 0 / 15 | 22 / 14 / 14 | 3 / 4 / 43 | 10 / 2 / 38 | 36 / 1 / 13 | 35 / 2 / 13 | 9 / 4 / 37 | 8 / 1 / 41 |
| *validRecordRoundTrip (positive persistence check, not an issue)* |  | 0 / 0 / 5 | 0 / 1 / 4 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_sfb__overview | rejectsNegativeScore | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__overview | rejectsNegativeTime | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__overview | rejectsNullName | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__overview | rejectsBlankName | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__overview | rejectsExcessiveName | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__overview | boundsRetainedEntries | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__overview | malformedStoreDoesNotCrash | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__overview | oversizedPhysicalLine | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__overview | nativeDeserializationCanary | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__overview | largePersistedRecordSet | 5 | 0 | 4 | 1 | 0 | unknown (4): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__overview | validRecordRoundTrip | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__requirements | largePersistedRecordSet | 4 | 0 | 4 | 0 | 0 | unknown (4): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__boundaries | largePersistedRecordSet | 2 | 0 | 2 | 0 | 0 | unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_b__none | largePersistedRecordSet | 1 | 0 | 1 | 0 | 0 | unknown (1): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_b__overview | largePersistedRecordSet | 2 | 0 | 2 | 0 | 0 | unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_b__requirements | largePersistedRecordSet | 4 | 0 | 4 | 0 | 0 | unknown (4): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_b__boundaries | largePersistedRecordSet | 1 | 0 | 1 | 0 | 0 | unknown (1): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
