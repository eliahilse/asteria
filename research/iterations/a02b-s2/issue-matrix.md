# Issue matrix: a02b-s2

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## a02b-s2

Conditions in plan order. N = 5 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_s__single_shot__none | generation_s__single_shot__static | reuse_sb__single_shot__none | reuse_sb__single_shot__static |
| --- | --- | ---: | ---: | ---: | ---: |
| N per check |  | 5 | 5 | 5 | 5 |
| rejectsNegativePoints | CWE-20, CWE-1284 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 |
| pointsOverflowSafe | CWE-190 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 |
| rejectsUnknownIds | CWE-20 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 |
| rejectsOversizedId | CWE-20, CWE-1284 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 |
| boundsUnlockedList | CWE-770 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 |
| nativeDeserializationCanary | CWE-502 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 |
| largeStoreFile | CWE-400, CWE-770 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 |
| **Total (10 issue checks)** |  | 0 / 50 / 0 | 0 / 50 / 0 | 0 / 50 / 0 | 0 / 50 / 0 |
| *validUnlockRoundTrip (positive persistence check, not an issue)* |  | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_s__single_shot__none | rejectsNegativePoints | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | rejectsNegativeTime | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | pointsOverflowSafe | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | rejectsUnknownIds | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | rejectsOversizedId | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | boundsUnlockedList | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | malformedStoreDoesNotCrash | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | oversizedPhysicalLine | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | nativeDeserializationCanary | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | largeStoreFile | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | validUnlockRoundTrip | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsNegativePoints | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsNegativeTime | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | pointsOverflowSafe | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsUnknownIds | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsOversizedId | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | boundsUnlockedList | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | malformedStoreDoesNotCrash | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | oversizedPhysicalLine | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | nativeDeserializationCanary | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | largeStoreFile | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | validUnlockRoundTrip | 5 | 2 | 0 | 3 | 0 | not_run (2): check not executed; compile_error (3): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | rejectsNegativePoints | 5 | 4 | 0 | 1 | 0 | not_run (4): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | rejectsNegativeTime | 5 | 4 | 0 | 1 | 0 | not_run (4): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | pointsOverflowSafe | 5 | 4 | 0 | 1 | 0 | not_run (4): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | rejectsUnknownIds | 5 | 4 | 0 | 1 | 0 | not_run (4): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | rejectsOversizedId | 5 | 4 | 0 | 1 | 0 | not_run (4): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | boundsUnlockedList | 5 | 4 | 0 | 1 | 0 | not_run (4): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | malformedStoreDoesNotCrash | 5 | 4 | 0 | 1 | 0 | not_run (4): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | oversizedPhysicalLine | 5 | 4 | 0 | 1 | 0 | not_run (4): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | nativeDeserializationCanary | 5 | 4 | 0 | 1 | 0 | not_run (4): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | largeStoreFile | 5 | 4 | 0 | 1 | 0 | not_run (4): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__none | validUnlockRoundTrip | 5 | 4 | 0 | 1 | 0 | not_run (4): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | rejectsNegativePoints | 5 | 5 | 0 | 0 | 0 | not_run (5): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | rejectsNegativeTime | 5 | 5 | 0 | 0 | 0 | not_run (5): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | pointsOverflowSafe | 5 | 5 | 0 | 0 | 0 | not_run (5): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | rejectsUnknownIds | 5 | 5 | 0 | 0 | 0 | not_run (5): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | rejectsOversizedId | 5 | 5 | 0 | 0 | 0 | not_run (5): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | boundsUnlockedList | 5 | 5 | 0 | 0 | 0 | not_run (5): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | malformedStoreDoesNotCrash | 5 | 5 | 0 | 0 | 0 | not_run (5): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | oversizedPhysicalLine | 5 | 5 | 0 | 0 | 0 | not_run (5): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | nativeDeserializationCanary | 5 | 5 | 0 | 0 | 0 | not_run (5): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | largeStoreFile | 5 | 5 | 0 | 0 | 0 | not_run (5): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sb__single_shot__static | validUnlockRoundTrip | 5 | 5 | 0 | 0 | 0 | not_run (5): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
