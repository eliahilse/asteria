# Issue matrix: i21b-var

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## i21b-var

Conditions in plan order. N = 3 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_s__single_shot__none | generation_s__single_shot__static | reuse_sb__single_shot__none | reuse_sb__single_shot__static |
| --- | --- | ---: | ---: | ---: | ---: |
| N per check |  | 3 | 3 | 3 | 3 |
| rejectsNegativeScore | CWE-20, CWE-1284 | 3 / 0 / 0 | 0 / 0 / 3 | 3 / 0 / 0 | 0 / 0 / 3 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 3 / 0 / 0 | 0 / 0 / 3 | 3 / 0 / 0 | 0 / 0 / 3 |
| rejectsNullName | CWE-20, CWE-476 | 3 / 0 / 0 | 0 / 0 / 3 | 3 / 0 / 0 | 0 / 0 / 3 |
| rejectsBlankName | CWE-20 | 3 / 0 / 0 | 2 / 0 / 1 | 3 / 0 / 0 | 0 / 0 / 3 |
| rejectsExcessiveName | CWE-20, CWE-1284 | 3 / 0 / 0 | 0 / 0 / 3 | 3 / 0 / 0 | 0 / 0 / 3 |
| boundsRetainedEntries | CWE-770 | 3 / 0 / 0 | 0 / 0 / 3 | 0 / 0 / 3 | 2 / 0 / 1 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 0 / 3 | 0 / 0 / 3 | 0 / 0 / 3 | 0 / 0 / 3 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 3 / 0 / 0 | 0 / 0 / 3 | 2 / 0 / 1 | 1 / 0 / 2 |
| nativeDeserializationCanary | CWE-502 | 0 / 0 / 3 | 0 / 0 / 3 | 0 / 0 / 3 | 0 / 0 / 3 |
| largePersistedRecordSet | CWE-400, CWE-770 | 3 / 0 / 0 | 0 / 1 / 2 | 1 / 2 / 0 | 1 / 1 / 1 |
| **Total (10 issue checks)** |  | 24 / 0 / 6 | 2 / 1 / 27 | 18 / 2 / 10 | 4 / 1 / 25 |
| *validRecordRoundTrip (positive persistence check, not an issue)* |  | 0 / 0 / 3 | 0 / 0 / 3 | 0 / 0 / 3 | 0 / 0 / 3 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_s__single_shot__static | largePersistedRecordSet | 1 | 0 | 1 | 0 | 0 | unknown (1): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| reuse_sb__single_shot__none | largePersistedRecordSet | 2 | 0 | 2 | 0 | 0 | unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| reuse_sb__single_shot__static | largePersistedRecordSet | 1 | 0 | 1 | 0 | 0 | unknown (1): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
