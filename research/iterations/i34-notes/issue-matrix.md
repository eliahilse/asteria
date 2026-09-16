# Issue matrix: i34-notes

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## i34-notes

Conditions in plan order. N = 2 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_s__agentic__notes | generation_s__agentic__static-notes | reuse_sb__agentic__notes | reuse_sb__agentic__static-notes |
| --- | --- | ---: | ---: | ---: | ---: |
| N per check |  | 2 | 2 | 2 | 2 |
| rejectsNegativeScore | CWE-20, CWE-1284 | 0 / 0 / 2 | 0 / 0 / 2 | 2 / 0 / 0 | 0 / 0 / 2 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 0 / 0 / 2 | 0 / 0 / 2 | 2 / 0 / 0 | 0 / 0 / 2 |
| rejectsNullName | CWE-20, CWE-476 | 0 / 0 / 2 | 0 / 0 / 2 | 2 / 0 / 0 | 0 / 0 / 2 |
| rejectsBlankName | CWE-20 | 0 / 0 / 2 | 0 / 0 / 2 | 2 / 0 / 0 | 0 / 0 / 2 |
| rejectsExcessiveName | CWE-20, CWE-1284 | 0 / 0 / 2 | 0 / 0 / 2 | 2 / 0 / 0 | 0 / 0 / 2 |
| boundsRetainedEntries | CWE-770 | 0 / 0 / 2 | 0 / 0 / 2 | 1 / 0 / 1 | 0 / 0 / 2 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 0 / 2 | 0 / 0 / 2 | 0 / 0 / 2 | 0 / 0 / 2 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 2 / 0 / 0 | 2 / 0 / 0 | 2 / 0 / 0 | 1 / 0 / 1 |
| nativeDeserializationCanary | CWE-502 | 0 / 0 / 2 | 0 / 0 / 2 | 0 / 0 / 2 | 0 / 0 / 2 |
| largePersistedRecordSet | CWE-400, CWE-770 | 0 / 2 / 0 | 0 / 2 / 0 | 2 / 0 / 0 | 1 / 0 / 1 |
| **Total (10 issue checks)** |  | 2 / 2 / 16 | 2 / 2 / 16 | 15 / 0 / 5 | 2 / 0 / 18 |
| *validRecordRoundTrip (positive persistence check, not an issue)* |  | 0 / 0 / 2 | 0 / 0 / 2 | 0 / 0 / 2 | 0 / 0 / 2 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_s__agentic__notes | largePersistedRecordSet | 2 | 0 | 2 | 0 | 0 | unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 2. |
| generation_s__agentic__static-notes | largePersistedRecordSet | 2 | 0 | 2 | 0 | 0 | unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 2. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
