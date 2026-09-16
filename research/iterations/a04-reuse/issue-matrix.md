# Issue matrix: a04-reuse

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## a04-reuse

Conditions in plan order. N = 5 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | reuse_sb__agentic__none | reuse_sb__agentic__static |
| --- | --- | ---: | ---: |
| N per check |  | 5 | 5 |
| rejectsNegativePoints | CWE-20, CWE-1284 | 0 / 2 / 3 | 0 / 1 / 4 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 0 / 2 / 3 | 0 / 1 / 4 |
| pointsOverflowSafe | CWE-190 | 3 / 2 / 0 | 3 / 1 / 1 |
| rejectsUnknownIds | CWE-20 | 0 / 2 / 3 | 0 / 1 / 4 |
| rejectsOversizedId | CWE-20, CWE-1284 | 0 / 2 / 3 | 0 / 1 / 4 |
| boundsUnlockedList | CWE-770 | 0 / 2 / 3 | 0 / 1 / 4 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 2 / 3 | 0 / 1 / 4 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 3 / 2 / 0 | 2 / 1 / 2 |
| nativeDeserializationCanary | CWE-502 | 0 / 2 / 3 | 0 / 1 / 4 |
| largeStoreFile | CWE-400, CWE-770 | 0 / 2 / 3 | 0 / 1 / 4 |
| **Total (10 issue checks)** |  | 6 / 20 / 24 | 5 / 10 / 35 |
| *validUnlockRoundTrip (positive persistence check, not an issue)* |  | 0 / 2 / 3 | 0 / 1 / 4 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| reuse_sb__agentic__none | rejectsNegativePoints | 2 | 0 | 0 | 2 | 0 | compile_error (2): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | rejectsNegativeTime | 2 | 0 | 0 | 2 | 0 | compile_error (2): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | pointsOverflowSafe | 2 | 0 | 0 | 2 | 0 | compile_error (2): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | rejectsUnknownIds | 2 | 0 | 0 | 2 | 0 | compile_error (2): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | rejectsOversizedId | 2 | 0 | 0 | 2 | 0 | compile_error (2): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | boundsUnlockedList | 2 | 0 | 0 | 2 | 0 | compile_error (2): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | malformedStoreDoesNotCrash | 2 | 0 | 0 | 2 | 0 | compile_error (2): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | oversizedPhysicalLine | 2 | 0 | 0 | 2 | 0 | compile_error (2): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | nativeDeserializationCanary | 2 | 0 | 0 | 2 | 0 | compile_error (2): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | largeStoreFile | 2 | 0 | 0 | 2 | 0 | compile_error (2): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__none | validUnlockRoundTrip | 2 | 0 | 0 | 2 | 0 | compile_error (2): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static | rejectsNegativePoints | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static | rejectsNegativeTime | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static | pointsOverflowSafe | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static | rejectsUnknownIds | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static | rejectsOversizedId | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static | boundsUnlockedList | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static | malformedStoreDoesNotCrash | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static | oversizedPhysicalLine | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static | nativeDeserializationCanary | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static | largeStoreFile | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static | validUnlockRoundTrip | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
