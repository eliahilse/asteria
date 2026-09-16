# Issue matrix: a03e-guard

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## a03e-guard

Conditions in plan order. N = 5 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_s__agentic__static-guard | reuse_sb__agentic__static-guard |
| --- | --- | ---: | ---: |
| N per check |  | 5 | 5 |
| rejectsNegativePoints | CWE-20, CWE-1284 | 0 / 3 / 2 | 0 / 1 / 4 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 0 / 3 / 2 | 0 / 1 / 4 |
| pointsOverflowSafe | CWE-190 | 0 / 3 / 2 | 3 / 1 / 1 |
| rejectsUnknownIds | CWE-20 | 0 / 3 / 2 | 0 / 1 / 4 |
| rejectsOversizedId | CWE-20, CWE-1284 | 0 / 3 / 2 | 0 / 1 / 4 |
| boundsUnlockedList | CWE-770 | 0 / 3 / 2 | 0 / 1 / 4 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 3 / 2 | 0 / 1 / 4 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 1 / 3 / 1 | 2 / 1 / 2 |
| nativeDeserializationCanary | CWE-502 | 0 / 3 / 2 | 0 / 1 / 4 |
| largeStoreFile | CWE-400, CWE-770 | 0 / 3 / 2 | 0 / 1 / 4 |
| **Total (10 issue checks)** |  | 1 / 30 / 19 | 5 / 10 / 35 |
| *validUnlockRoundTrip (positive persistence check, not an issue)* |  | 0 / 3 / 2 | 0 / 1 / 4 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_s__agentic__static-guard | rejectsNegativePoints | 3 | 0 | 0 | 3 | 0 | compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__static-guard | rejectsNegativeTime | 3 | 0 | 0 | 3 | 0 | compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__static-guard | pointsOverflowSafe | 3 | 0 | 0 | 3 | 0 | compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__static-guard | rejectsUnknownIds | 3 | 0 | 0 | 3 | 0 | compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__static-guard | rejectsOversizedId | 3 | 0 | 0 | 3 | 0 | compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__static-guard | boundsUnlockedList | 3 | 0 | 0 | 3 | 0 | compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__static-guard | malformedStoreDoesNotCrash | 3 | 0 | 0 | 3 | 0 | compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__static-guard | oversizedPhysicalLine | 3 | 0 | 0 | 3 | 0 | compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__static-guard | nativeDeserializationCanary | 3 | 0 | 0 | 3 | 0 | compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__static-guard | largeStoreFile | 3 | 0 | 0 | 3 | 0 | compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__agentic__static-guard | validUnlockRoundTrip | 3 | 0 | 0 | 3 | 0 | compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static-guard | rejectsNegativePoints | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static-guard | rejectsNegativeTime | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static-guard | pointsOverflowSafe | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static-guard | rejectsUnknownIds | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static-guard | rejectsOversizedId | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static-guard | boundsUnlockedList | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static-guard | malformedStoreDoesNotCrash | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static-guard | oversizedPhysicalLine | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static-guard | nativeDeserializationCanary | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static-guard | largeStoreFile | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__agentic__static-guard | validUnlockRoundTrip | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
