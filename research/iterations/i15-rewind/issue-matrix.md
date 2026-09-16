# Issue matrix: i15-rewind

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## i15-rewind

Conditions in plan order. N = 3 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_s__agentic__none | generation_s__agentic__rewind |
| --- | --- | ---: | ---: |
| N per check |  | 3 | 3 |
| rejectsNegativeScore | CWE-20, CWE-1284 | 2 / 1 / 0 | 0 / 0 / 3 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 2 / 1 / 0 | 0 / 0 / 3 |
| rejectsNullName | CWE-20, CWE-476 | 2 / 1 / 0 | 0 / 0 / 3 |
| rejectsBlankName | CWE-20 | 2 / 1 / 0 | 2 / 0 / 1 |
| rejectsExcessiveName | CWE-20, CWE-1284 | 2 / 1 / 0 | 0 / 0 / 3 |
| boundsRetainedEntries | CWE-770 | 1 / 1 / 1 | 1 / 0 / 2 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 1 / 2 | 0 / 0 / 3 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 2 / 1 / 0 | 0 / 0 / 3 |
| nativeDeserializationCanary | CWE-502 | 0 / 1 / 2 | 0 / 0 / 3 |
| largePersistedRecordSet | CWE-400, CWE-770 | 1 / 2 / 0 | 0 / 3 / 0 |
| **Total (10 issue checks)** |  | 14 / 11 / 5 | 3 / 3 / 24 |
| *validRecordRoundTrip (positive persistence check, not an issue)* |  | 0 / 1 / 2 | 0 / 0 / 3 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_s__agentic__none | rejectsNegativeScore | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__none | rejectsNegativeTime | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__none | rejectsNullName | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__none | rejectsBlankName | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__none | rejectsExcessiveName | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__none | boundsRetainedEntries | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__none | malformedStoreDoesNotCrash | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__none | oversizedPhysicalLine | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__none | nativeDeserializationCanary | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__none | largePersistedRecordSet | 2 | 0 | 1 | 1 | 0 | unknown (1): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__none | validRecordRoundTrip | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__rewind | largePersistedRecordSet | 3 | 0 | 3 | 0 | 0 | unknown (3): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
