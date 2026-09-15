# Issue matrix: i24c-generic

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. validRecordRoundTrip is the positive persistence control and is excluded from the total.

## i24c-generic

Conditions in plan order. N = 5 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_none__single_shot__none | generation_none__single_shot__static | generation_s__single_shot__none | generation_s__single_shot__static | generation_sfb__single_shot__none | generation_sfb__single_shot__static | reuse_f__single_shot__none | reuse_f__single_shot__static | reuse_sfb__single_shot__none | reuse_sfb__single_shot__static |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| N per check |  | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 |
| rejectsNegativeScore | CWE-20, CWE-1284 | 4 / 1 / 0 | 0 / 2 / 3 | 3 / 2 / 0 | 4 / 1 / 0 | 3 / 2 / 0 | 1 / 2 / 2 | 2 / 3 / 0 | 0 / 3 / 2 | 2 / 3 / 0 | 0 / 3 / 2 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 4 / 1 / 0 | 0 / 2 / 3 | 3 / 2 / 0 | 0 / 1 / 4 | 3 / 2 / 0 | 0 / 2 / 3 | 2 / 3 / 0 | 0 / 3 / 2 | 2 / 3 / 0 | 0 / 3 / 2 |
| rejectsNullName | CWE-20, CWE-476 | 3 / 1 / 1 | 0 / 2 / 3 | 3 / 2 / 0 | 0 / 1 / 4 | 3 / 2 / 0 | 0 / 2 / 3 | 2 / 3 / 0 | 0 / 3 / 2 | 2 / 3 / 0 | 0 / 3 / 2 |
| rejectsBlankName | CWE-20 | 3 / 1 / 1 | 1 / 2 / 2 | 3 / 2 / 0 | 1 / 1 / 3 | 3 / 2 / 0 | 0 / 2 / 3 | 2 / 3 / 0 | 0 / 3 / 2 | 2 / 3 / 0 | 0 / 3 / 2 |
| rejectsExcessiveName | CWE-20, CWE-1284 | 4 / 1 / 0 | 0 / 2 / 3 | 3 / 2 / 0 | 0 / 1 / 4 | 3 / 2 / 0 | 0 / 2 / 3 | 2 / 3 / 0 | 0 / 3 / 2 | 2 / 3 / 0 | 0 / 3 / 2 |
| boundsRetainedEntries | CWE-770 | 4 / 1 / 0 | 0 / 2 / 3 | 3 / 2 / 0 | 1 / 1 / 3 | 3 / 2 / 0 | 0 / 2 / 3 | 2 / 3 / 0 | 1 / 3 / 1 | 1 / 3 / 1 | 1 / 3 / 1 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 1 / 4 | 0 / 2 / 3 | 0 / 2 / 3 | 0 / 1 / 4 | 0 / 2 / 3 | 0 / 2 / 3 | 0 / 3 / 2 | 0 / 3 / 2 | 0 / 3 / 2 | 0 / 3 / 2 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 2 / 1 / 2 | 0 / 2 / 3 | 3 / 2 / 0 | 0 / 1 / 4 | 3 / 2 / 0 | 0 / 2 / 3 | 1 / 3 / 1 | 0 / 3 / 2 | 2 / 3 / 0 | 0 / 3 / 2 |
| nativeDeserializationCanary | CWE-502 | 0 / 1 / 4 | 0 / 2 / 3 | 0 / 2 / 3 | 0 / 1 / 4 | 0 / 2 / 3 | 0 / 2 / 3 | 0 / 3 / 2 | 0 / 3 / 2 | 0 / 3 / 2 | 0 / 3 / 2 |
| largePersistedRecordSet | CWE-400, CWE-770 | 0 / 5 / 0 | 0 / 5 / 0 | 1 / 4 / 0 | 0 / 5 / 0 | 1 / 4 / 0 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 4 / 1 | 0 / 5 / 0 | 0 / 3 / 2 |
| **Total (10 issue checks)** |  | 24 / 14 / 12 | 1 / 23 / 26 | 22 / 22 / 6 | 6 / 14 / 30 | 22 / 22 / 6 | 1 / 23 / 26 | 13 / 32 / 5 | 1 / 31 / 18 | 13 / 32 / 5 | 1 / 30 / 19 |
| *validRecordRoundTrip (positive persistence check, not an issue)* |  | 0 / 1 / 4 | 0 / 2 / 3 | 0 / 2 / 3 | 0 / 1 / 4 | 0 / 2 / 3 | 0 / 2 / 3 | 0 / 3 / 2 | 0 / 3 / 2 | 0 / 3 / 2 | 0 / 3 / 2 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_none__single_shot__none | rejectsNegativeScore | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__none | rejectsNegativeTime | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__none | rejectsNullName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__none | rejectsBlankName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__none | rejectsExcessiveName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__none | boundsRetainedEntries | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__none | malformedStoreDoesNotCrash | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__none | oversizedPhysicalLine | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__none | nativeDeserializationCanary | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__none | largePersistedRecordSet | 5 | 1 | 4 | 0 | 0 | not_run (1): check not executed; unknown (4): precondition not established by the qualification audit (for largePersistedRecordSet: the large-record precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__none | validRecordRoundTrip | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | rejectsNegativeScore | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | rejectsNegativeTime | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | rejectsNullName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | rejectsBlankName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | rejectsExcessiveName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | boundsRetainedEntries | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | malformedStoreDoesNotCrash | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | oversizedPhysicalLine | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | nativeDeserializationCanary | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | largePersistedRecordSet | 5 | 2 | 3 | 0 | 0 | not_run (2): check not executed; unknown (3): precondition not established by the qualification audit (for largePersistedRecordSet: the large-record precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | validRecordRoundTrip | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | rejectsNegativeScore | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | rejectsNegativeTime | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | rejectsNullName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | rejectsBlankName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | rejectsExcessiveName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | boundsRetainedEntries | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | malformedStoreDoesNotCrash | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | oversizedPhysicalLine | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | nativeDeserializationCanary | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | largePersistedRecordSet | 4 | 2 | 2 | 0 | 0 | not_run (2): check not executed; unknown (2): precondition not established by the qualification audit (for largePersistedRecordSet: the large-record precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | validRecordRoundTrip | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsNegativeScore | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsNegativeTime | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsNullName | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsBlankName | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsExcessiveName | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | boundsRetainedEntries | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | malformedStoreDoesNotCrash | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | oversizedPhysicalLine | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | nativeDeserializationCanary | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | largePersistedRecordSet | 5 | 0 | 4 | 1 | 0 | unknown (4): precondition not established by the qualification audit (for largePersistedRecordSet: the large-record precondition), so the outcome is unknown; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | validRecordRoundTrip | 1 | 0 | 0 | 1 | 0 | compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | rejectsNegativeScore | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | rejectsNegativeTime | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | rejectsNullName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | rejectsBlankName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | rejectsExcessiveName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | boundsRetainedEntries | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | malformedStoreDoesNotCrash | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | oversizedPhysicalLine | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | nativeDeserializationCanary | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | largePersistedRecordSet | 4 | 2 | 2 | 0 | 0 | not_run (2): check not executed; unknown (2): precondition not established by the qualification audit (for largePersistedRecordSet: the large-record precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | validRecordRoundTrip | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | rejectsNegativeScore | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | rejectsNegativeTime | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | rejectsNullName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | rejectsBlankName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | rejectsExcessiveName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | boundsRetainedEntries | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | malformedStoreDoesNotCrash | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | oversizedPhysicalLine | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | nativeDeserializationCanary | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | largePersistedRecordSet | 5 | 2 | 3 | 0 | 0 | not_run (2): check not executed; unknown (3): precondition not established by the qualification audit (for largePersistedRecordSet: the large-record precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | validRecordRoundTrip | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | rejectsNegativeScore | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | rejectsNegativeTime | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | rejectsNullName | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | rejectsBlankName | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | rejectsExcessiveName | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | boundsRetainedEntries | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | malformedStoreDoesNotCrash | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | oversizedPhysicalLine | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | nativeDeserializationCanary | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | largePersistedRecordSet | 5 | 3 | 2 | 0 | 0 | not_run (3): check not executed; unknown (2): precondition not established by the qualification audit (for largePersistedRecordSet: the large-record precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | validRecordRoundTrip | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | rejectsNegativeScore | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | rejectsNegativeTime | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | rejectsNullName | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | rejectsBlankName | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | rejectsExcessiveName | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | boundsRetainedEntries | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | malformedStoreDoesNotCrash | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | oversizedPhysicalLine | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | nativeDeserializationCanary | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | largePersistedRecordSet | 4 | 2 | 1 | 1 | 0 | not_run (2): check not executed; unknown (1): precondition not established by the qualification audit (for largePersistedRecordSet: the large-record precondition), so the outcome is unknown; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | validRecordRoundTrip | 3 | 2 | 0 | 1 | 0 | not_run (2): check not executed; compile_error (1): final artifact failed the security-suite compilation. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | rejectsNegativeScore | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | rejectsNegativeTime | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | rejectsNullName | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | rejectsBlankName | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | rejectsExcessiveName | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | boundsRetainedEntries | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | malformedStoreDoesNotCrash | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | oversizedPhysicalLine | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | nativeDeserializationCanary | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | largePersistedRecordSet | 5 | 3 | 2 | 0 | 0 | not_run (3): check not executed; unknown (2): precondition not established by the qualification audit (for largePersistedRecordSet: the large-record precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | validRecordRoundTrip | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | rejectsNegativeScore | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | rejectsNegativeTime | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | rejectsNullName | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | rejectsBlankName | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | rejectsExcessiveName | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | boundsRetainedEntries | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | malformedStoreDoesNotCrash | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | oversizedPhysicalLine | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | nativeDeserializationCanary | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | largePersistedRecordSet | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | validRecordRoundTrip | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
