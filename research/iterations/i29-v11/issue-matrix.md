# Issue matrix: i29-v11

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. validRecordRoundTrip is the positive persistence control and is excluded from the total.

## i29-v11

Conditions in plan order. N = 5 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_s__agentic__none | generation_s__agentic__static | generation_s__agentic__static-guard | reuse_sb__agentic__none | reuse_sb__agentic__static | reuse_sb__agentic__static-guard |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |
| N per check |  | 5 | 5 | 5 | 5 | 5 | 5 |
| rejectsNegativeScore | CWE-20, CWE-1284 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 4 / 1 / 0 | 0 / 0 / 5 | 0 / 0 / 5 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 4 / 1 / 0 | 0 / 0 / 5 | 0 / 0 / 5 |
| rejectsNullName | CWE-20, CWE-476 | 1 / 0 / 4 | 0 / 0 / 5 | 0 / 0 / 5 | 4 / 1 / 0 | 0 / 0 / 5 | 0 / 0 / 5 |
| rejectsBlankName | CWE-20 | 1 / 0 / 4 | 5 / 0 / 0 | 5 / 0 / 0 | 4 / 1 / 0 | 5 / 0 / 0 | 5 / 0 / 0 |
| rejectsExcessiveName | CWE-20, CWE-1284 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 4 / 1 / 0 | 0 / 0 / 5 | 0 / 0 / 5 |
| boundsRetainedEntries | CWE-770 | 3 / 0 / 2 | 0 / 0 / 5 | 0 / 0 / 5 | 2 / 1 / 2 | 0 / 0 / 5 | 0 / 0 / 5 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 1 / 4 | 0 / 0 / 5 | 0 / 0 / 5 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 2 / 1 / 2 | 0 / 0 / 5 | 0 / 0 / 5 |
| nativeDeserializationCanary | CWE-502 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 1 / 4 | 0 / 0 / 5 | 0 / 0 / 5 |
| largePersistedRecordSet | CWE-400, CWE-770 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 4 / 1 | 0 / 4 / 1 | 0 / 0 / 5 | 0 / 0 / 5 |
| **Total (10 issue checks)** |  | 25 / 5 / 20 | 5 / 5 / 40 | 5 / 4 / 41 | 24 / 13 / 13 | 5 / 0 / 45 | 5 / 0 / 45 |
| *validRecordRoundTrip (positive persistence check, not an issue)* |  | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 1 / 4 | 0 / 0 / 5 | 0 / 0 / 5 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_s__agentic__none | largePersistedRecordSet | 5 | 0 | 5 | 0 | 0 | unknown (5): precondition not established by the qualification audit (for largePersistedRecordSet: the large-record precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__static | largePersistedRecordSet | 5 | 0 | 5 | 0 | 0 | unknown (5): precondition not established by the qualification audit (for largePersistedRecordSet: the large-record precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__static-guard | largePersistedRecordSet | 4 | 0 | 4 | 0 | 0 | unknown (4): precondition not established by the qualification audit (for largePersistedRecordSet: the large-record precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | rejectsNegativeScore | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | rejectsNegativeTime | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | rejectsNullName | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | rejectsBlankName | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | rejectsExcessiveName | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | boundsRetainedEntries | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | malformedStoreDoesNotCrash | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | oversizedPhysicalLine | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | nativeDeserializationCanary | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | largePersistedRecordSet | 4 | 0 | 3 | 1 | 0 | unknown (3): precondition not established by the qualification audit (for largePersistedRecordSet: the large-record precondition), so the outcome is unknown; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | validRecordRoundTrip | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
