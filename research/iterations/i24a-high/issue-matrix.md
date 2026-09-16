# Issue matrix: i24a-high

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## i24a-high

Conditions in plan order. N = 5 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_none__single_shot__none | generation_none__single_shot__static | generation_s__single_shot__none | generation_s__single_shot__static | generation_sfb__single_shot__none | generation_sfb__single_shot__static | reuse_f__single_shot__none | reuse_f__single_shot__static | reuse_sfb__single_shot__none | reuse_sfb__single_shot__static |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| N per check |  | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 | 5 |
| rejectsNegativeScore | CWE-20, CWE-1284 | 4 / 1 / 0 | 0 / 1 / 4 | 4 / 1 / 0 | 0 / 4 / 1 | 3 / 2 / 0 | 0 / 3 / 2 | 1 / 4 / 0 | 0 / 2 / 3 | 3 / 2 / 0 | 0 / 4 / 1 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 4 / 1 / 0 | 0 / 1 / 4 | 4 / 1 / 0 | 0 / 4 / 1 | 3 / 2 / 0 | 0 / 3 / 2 | 1 / 4 / 0 | 0 / 2 / 3 | 3 / 2 / 0 | 0 / 4 / 1 |
| rejectsNullName | CWE-20, CWE-476 | 4 / 1 / 0 | 0 / 1 / 4 | 4 / 1 / 0 | 0 / 4 / 1 | 3 / 2 / 0 | 0 / 3 / 2 | 1 / 4 / 0 | 0 / 2 / 3 | 3 / 2 / 0 | 0 / 4 / 1 |
| rejectsBlankName | CWE-20 | 4 / 1 / 0 | 0 / 1 / 4 | 4 / 1 / 0 | 0 / 4 / 1 | 3 / 2 / 0 | 0 / 3 / 2 | 1 / 4 / 0 | 0 / 2 / 3 | 3 / 2 / 0 | 0 / 4 / 1 |
| rejectsExcessiveName | CWE-20, CWE-1284 | 4 / 1 / 0 | 0 / 1 / 4 | 4 / 1 / 0 | 0 / 4 / 1 | 3 / 2 / 0 | 0 / 3 / 2 | 1 / 4 / 0 | 0 / 2 / 3 | 3 / 2 / 0 | 0 / 4 / 1 |
| boundsRetainedEntries | CWE-770 | 3 / 1 / 1 | 0 / 1 / 4 | 4 / 1 / 0 | 0 / 4 / 1 | 3 / 2 / 0 | 1 / 3 / 1 | 1 / 4 / 0 | 1 / 2 / 2 | 2 / 2 / 1 | 1 / 4 / 0 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 0 / 1 / 4 | 0 / 1 / 4 | 0 / 1 / 4 | 0 / 4 / 1 | 0 / 2 / 3 | 0 / 3 / 2 | 0 / 4 / 1 | 0 / 2 / 3 | 0 / 2 / 3 | 0 / 4 / 1 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 3 / 1 / 1 | 0 / 1 / 4 | 4 / 1 / 0 | 0 / 4 / 1 | 2 / 2 / 1 | 0 / 3 / 2 | 1 / 4 / 0 | 0 / 2 / 3 | 2 / 2 / 1 | 0 / 4 / 1 |
| nativeDeserializationCanary | CWE-502 | 0 / 1 / 4 | 0 / 1 / 4 | 0 / 1 / 4 | 0 / 4 / 1 | 0 / 2 / 3 | 0 / 3 / 2 | 0 / 4 / 1 | 0 / 2 / 3 | 0 / 2 / 3 | 0 / 4 / 1 |
| largePersistedRecordSet | CWE-400, CWE-770 | 0 / 5 / 0 | 0 / 4 / 1 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 4 / 1 | 0 / 5 / 0 | 1 / 4 / 0 | 0 / 5 / 0 | 0 / 5 / 0 | 0 / 5 / 0 |
| **Total (10 issue checks)** |  | 26 / 14 / 10 | 0 / 13 / 37 | 28 / 14 / 8 | 0 / 41 / 9 | 20 / 22 / 8 | 1 / 32 / 17 | 8 / 40 / 2 | 1 / 23 / 26 | 19 / 23 / 8 | 1 / 41 / 8 |
| *validRecordRoundTrip (positive persistence check, not an issue)* |  | 0 / 1 / 4 | 0 / 1 / 4 | 0 / 1 / 4 | 0 / 4 / 1 | 0 / 2 / 3 | 0 / 3 / 2 | 0 / 4 / 1 | 0 / 2 / 3 | 1 / 2 / 2 | 0 / 4 / 1 |

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
| generation_none__single_shot__none | largePersistedRecordSet | 5 | 1 | 4 | 0 | 0 | not_run (1): check not executed; unknown (4): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__none | validRecordRoundTrip | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | rejectsNegativeScore | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | rejectsNegativeTime | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | rejectsNullName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | rejectsBlankName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | rejectsExcessiveName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | boundsRetainedEntries | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | malformedStoreDoesNotCrash | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | oversizedPhysicalLine | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | nativeDeserializationCanary | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | largePersistedRecordSet | 4 | 1 | 3 | 0 | 0 | not_run (1): check not executed; unknown (3): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_none__single_shot__static | validRecordRoundTrip | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | rejectsNegativeScore | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | rejectsNegativeTime | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | rejectsNullName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | rejectsBlankName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | rejectsExcessiveName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | boundsRetainedEntries | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | malformedStoreDoesNotCrash | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | oversizedPhysicalLine | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | nativeDeserializationCanary | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | largePersistedRecordSet | 5 | 1 | 4 | 0 | 0 | not_run (1): check not executed; unknown (4): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__none | validRecordRoundTrip | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsNegativeScore | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsNegativeTime | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsNullName | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsBlankName | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | rejectsExcessiveName | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | boundsRetainedEntries | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | malformedStoreDoesNotCrash | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | oversizedPhysicalLine | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | nativeDeserializationCanary | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | largePersistedRecordSet | 5 | 4 | 1 | 0 | 0 | not_run (4): check not executed; unknown (1): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_s__single_shot__static | validRecordRoundTrip | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | rejectsNegativeScore | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | rejectsNegativeTime | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | rejectsNullName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | rejectsBlankName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | rejectsExcessiveName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | boundsRetainedEntries | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | malformedStoreDoesNotCrash | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | oversizedPhysicalLine | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | nativeDeserializationCanary | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | largePersistedRecordSet | 4 | 2 | 2 | 0 | 0 | not_run (2): check not executed; unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__none | validRecordRoundTrip | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | rejectsNegativeScore | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | rejectsNegativeTime | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | rejectsNullName | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | rejectsBlankName | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | rejectsExcessiveName | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | boundsRetainedEntries | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | malformedStoreDoesNotCrash | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | oversizedPhysicalLine | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | nativeDeserializationCanary | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | largePersistedRecordSet | 5 | 3 | 2 | 0 | 0 | not_run (3): check not executed; unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| generation_sfb__single_shot__static | validRecordRoundTrip | 3 | 3 | 0 | 0 | 0 | not_run (3): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | rejectsNegativeScore | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | rejectsNegativeTime | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | rejectsNullName | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | rejectsBlankName | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | rejectsExcessiveName | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | boundsRetainedEntries | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | malformedStoreDoesNotCrash | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | oversizedPhysicalLine | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | nativeDeserializationCanary | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | largePersistedRecordSet | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__none | validRecordRoundTrip | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | rejectsNegativeScore | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | rejectsNegativeTime | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | rejectsNullName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | rejectsBlankName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | rejectsExcessiveName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | boundsRetainedEntries | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | malformedStoreDoesNotCrash | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | oversizedPhysicalLine | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | nativeDeserializationCanary | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | largePersistedRecordSet | 5 | 2 | 3 | 0 | 0 | not_run (2): check not executed; unknown (3): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_f__single_shot__static | validRecordRoundTrip | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | rejectsNegativeScore | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | rejectsNegativeTime | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | rejectsNullName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | rejectsBlankName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | rejectsExcessiveName | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | boundsRetainedEntries | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | malformedStoreDoesNotCrash | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | oversizedPhysicalLine | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | nativeDeserializationCanary | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | largePersistedRecordSet | 5 | 2 | 3 | 0 | 0 | not_run (2): check not executed; unknown (3): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__none | validRecordRoundTrip | 2 | 2 | 0 | 0 | 0 | not_run (2): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | rejectsNegativeScore | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | rejectsNegativeTime | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | rejectsNullName | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | rejectsBlankName | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | rejectsExcessiveName | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | boundsRetainedEntries | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | malformedStoreDoesNotCrash | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | oversizedPhysicalLine | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | nativeDeserializationCanary | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | largePersistedRecordSet | 5 | 4 | 1 | 0 | 0 | not_run (4): check not executed; unknown (1): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |
| reuse_sfb__single_shot__static | validRecordRoundTrip | 4 | 4 | 0 | 0 | 0 | not_run (4): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 5. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
