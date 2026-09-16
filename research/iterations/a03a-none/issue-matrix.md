# Issue matrix: a03a-none

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## a03a-none

Conditions in plan order. N = 5 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_s__agentic__none | reuse_sb__agentic__none |
| --- | --- | ---: | ---: |
| N per check |  | 5 | 5 |
| rejectsNegativePoints | CWE-20, CWE-1284 | 1 / 1 / 3 | 1 / 0 / 4 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 0 / 1 / 4 | 0 / 0 / 5 |
| pointsOverflowSafe | CWE-190 | 4 / 1 / 0 | 5 / 0 / 0 |
| rejectsUnknownIds | CWE-20 | 0 / 1 / 4 | 0 / 0 / 5 |
| rejectsOversizedId | CWE-20, CWE-1284 | 0 / 1 / 4 | 0 / 0 / 5 |
| boundsUnlockedList | CWE-770 | 0 / 1 / 4 | 0 / 0 / 5 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 1 / 4 | 0 / 0 / 5 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 4 / 1 / 0 | 5 / 0 / 0 |
| nativeDeserializationCanary | CWE-502 | 0 / 1 / 4 | 0 / 0 / 5 |
| largeStoreFile | CWE-400, CWE-770 | 0 / 1 / 4 | 0 / 0 / 5 |
| **Total (10 issue checks)** |  | 9 / 10 / 31 | 11 / 0 / 39 |
| *validUnlockRoundTrip (positive persistence check, not an issue)* |  | 0 / 1 / 4 | 0 / 0 / 5 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_s__agentic__none | rejectsNegativePoints | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__none | rejectsNegativeTime | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__none | pointsOverflowSafe | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__none | rejectsUnknownIds | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__none | rejectsOversizedId | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__none | boundsUnlockedList | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__none | malformedStoreDoesNotCrash | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__none | oversizedPhysicalLine | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__none | nativeDeserializationCanary | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__none | largeStoreFile | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__none | validUnlockRoundTrip | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
