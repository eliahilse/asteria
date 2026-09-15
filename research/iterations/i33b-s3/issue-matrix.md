# Issue matrix: i33b-s3

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. validRecordRoundTrip is the positive persistence control and is excluded from the total.

## i33b-s3

Conditions in plan order. N = 5 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_s__agentic__static | reuse_sb__agentic__static |
| --- | --- | ---: | ---: |
| N per check |  | 5 | 5 |
| rejectsNegativeScore | CWE-20, CWE-1284 | 0 / 0 / 5 | 0 / 0 / 5 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 0 / 0 / 5 | 0 / 0 / 5 |
| rejectsNullName | CWE-20, CWE-476 | 0 / 0 / 5 | 0 / 0 / 5 |
| rejectsBlankName | CWE-20 | 0 / 0 / 5 | 0 / 0 / 5 |
| rejectsExcessiveName | CWE-20, CWE-1284 | 0 / 0 / 5 | 0 / 0 / 5 |
| boundsRetainedEntries | CWE-770 | 0 / 0 / 5 | 0 / 0 / 5 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 0 / 5 | 0 / 0 / 5 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 0 / 0 / 5 | 4 / 0 / 1 |
| nativeDeserializationCanary | CWE-502 | 0 / 0 / 5 | 0 / 0 / 5 |
| largePersistedRecordSet | CWE-400, CWE-770 | 0 / 5 / 0 | 1 / 3 / 1 |
| **Total (10 issue checks)** |  | 0 / 5 / 45 | 5 / 3 / 42 |
| *validRecordRoundTrip (positive persistence check, not an issue)* |  | 0 / 0 / 5 | 0 / 0 / 5 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_s__agentic__static | largePersistedRecordSet | 5 | 0 | 5 | 0 | 0 | unknown (5): precondition not established by the qualification audit (for largePersistedRecordSet: the large-record precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static | largePersistedRecordSet | 3 | 0 | 3 | 0 | 0 | unknown (3): precondition not established by the qualification audit (for largePersistedRecordSet: the large-record precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
