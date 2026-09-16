# Issue matrix: i31-v13

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## i31-v13

Conditions in plan order. N = 5 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_s__agentic__none | generation_s__agentic__static | generation_s__agentic__static-guard | reuse_sb__agentic__none | reuse_sb__agentic__static | reuse_sb__agentic__static-guard |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |
| N per check |  | 5 | 5 | 5 | 5 | 5 | 5 |
| rejectsNegativeScore | CWE-20, CWE-1284 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 |
| rejectsNullName | CWE-20, CWE-476 | 2 / 0 / 3 | 0 / 0 / 5 | 0 / 0 / 5 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 |
| rejectsBlankName | CWE-20 | 2 / 0 / 3 | 0 / 0 / 5 | 0 / 0 / 5 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 |
| rejectsExcessiveName | CWE-20, CWE-1284 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 | 5 / 0 / 0 | 0 / 0 / 5 | 0 / 0 / 5 |
| boundsRetainedEntries | CWE-770 | 3 / 0 / 2 | 0 / 0 / 5 | 0 / 0 / 5 | 4 / 0 / 1 | 0 / 0 / 5 | 0 / 0 / 5 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 4 / 0 / 1 | 3 / 0 / 2 | 2 / 0 / 3 | 2 / 0 / 3 | 2 / 0 / 3 | 3 / 0 / 2 |
| nativeDeserializationCanary | CWE-502 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |
| largePersistedRecordSet | CWE-400, CWE-770 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 4 / 1 | 0 / 5 / 0 | 0 / 3 / 2 | 1 / 4 / 0 |
| **Total (10 issue checks)** |  | 26 / 5 / 19 | 3 / 5 / 42 | 2 / 4 / 44 | 31 / 5 / 14 | 2 / 3 / 45 | 4 / 4 / 42 |
| *validRecordRoundTrip (positive persistence check, not an issue)* |  | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 | 0 / 0 / 5 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_s__agentic__none | largePersistedRecordSet | 5 | 0 | 5 | 0 | 0 | unknown (5): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__static | largePersistedRecordSet | 5 | 0 | 5 | 0 | 0 | unknown (5): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__static-guard | largePersistedRecordSet | 4 | 0 | 4 | 0 | 0 | unknown (4): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | largePersistedRecordSet | 5 | 0 | 5 | 0 | 0 | unknown (5): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static | largePersistedRecordSet | 3 | 0 | 3 | 0 | 0 | unknown (3): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static-guard | largePersistedRecordSet | 4 | 0 | 4 | 0 | 0 | unknown (4): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
