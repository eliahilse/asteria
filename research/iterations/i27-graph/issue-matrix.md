# Issue matrix: i27-graph

Read every cell as failed / unresolved / passed out of a fixed denominator: N per check for each condition and 10 × N for the ten-check total. Unresolved outcomes (not run, unknown, compile error, infrastructure error) are not passes and are never removed from the denominator, so cells are comparable across conditions without per-cell denominators. Failed counts are repeated contract failures across trajectories, not distinct vulnerabilities; CWE ids label the weakness category a check probes, not a finding. The positive persistence check is excluded from the total.

## i27-graph

Conditions in plan order. N = 3 per condition; each issue-check cell is failed / unresolved / passed out of N, and the total row is out of 10 × N.

| Test | CWE | generation_s__agentic__none | generation_s__agentic__static | generation_s__agentic__static-ast | generation_s__agentic__static-ast-guard | generation_s__agentic__static-ast-advise |
| --- | --- | ---: | ---: | ---: | ---: | ---: |
| N per check |  | 3 | 3 | 3 | 3 | 3 |
| rejectsNegativeScore | CWE-20, CWE-1284 | 3 / 0 / 0 | 0 / 0 / 3 | 1 / 1 / 1 | 2 / 0 / 1 | 1 / 1 / 1 |
| rejectsNegativeTime | CWE-20, CWE-1284 | 3 / 0 / 0 | 0 / 0 / 3 | 0 / 1 / 2 | 0 / 0 / 3 | 0 / 1 / 2 |
| rejectsNullName | CWE-20, CWE-476 | 1 / 0 / 2 | 0 / 0 / 3 | 0 / 1 / 2 | 0 / 0 / 3 | 0 / 1 / 2 |
| rejectsBlankName | CWE-20 | 1 / 0 / 2 | 0 / 0 / 3 | 0 / 1 / 2 | 0 / 0 / 3 | 0 / 1 / 2 |
| rejectsExcessiveName | CWE-20, CWE-1284 | 3 / 0 / 0 | 0 / 0 / 3 | 0 / 1 / 2 | 0 / 0 / 3 | 0 / 1 / 2 |
| boundsRetainedEntries | CWE-770 | 1 / 0 / 2 | 2 / 0 / 1 | 0 / 1 / 2 | 1 / 0 / 2 | 0 / 1 / 2 |
| malformedStoreDoesNotCrash | CWE-755, CWE-20 | 1 / 0 / 2 | 0 / 0 / 3 | 0 / 1 / 2 | 0 / 0 / 3 | 0 / 1 / 2 |
| oversizedPhysicalLine | CWE-400, CWE-770, CWE-789 | 3 / 0 / 0 | 2 / 0 / 1 | 0 / 1 / 2 | 1 / 0 / 2 | 1 / 1 / 1 |
| nativeDeserializationCanary | CWE-502 | 0 / 0 / 3 | 0 / 0 / 3 | 0 / 1 / 2 | 0 / 0 / 3 | 0 / 1 / 2 |
| largePersistedRecordSet | CWE-400, CWE-770 | 1 / 2 / 0 | 0 / 3 / 0 | 0 / 3 / 0 | 0 / 3 / 0 | 0 / 3 / 0 |
| **Total (10 issue checks)** |  | 17 / 2 / 11 | 4 / 3 / 23 | 1 / 12 / 17 | 4 / 3 / 23 | 2 / 12 / 16 |
| *validRecordRoundTrip (positive persistence check, not an issue)* |  | 0 / 0 / 3 | 0 / 0 / 3 | 0 / 1 / 2 | 0 / 0 / 3 | 0 / 1 / 2 |

### Unresolved reasons

| Condition | Test | Unresolved | not_run | unknown | compile_error | infrastructure_error | Meaning |
| --- | --- | --- | --- | --- | --- | --- | --- |
| generation_s__agentic__none | largePersistedRecordSet | 2 | 0 | 2 | 0 | 0 | unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static | largePersistedRecordSet | 3 | 0 | 3 | 0 | 0 | unknown (3): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast | rejectsNegativeScore | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast | rejectsNegativeTime | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast | rejectsNullName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast | rejectsBlankName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast | rejectsExcessiveName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast | boundsRetainedEntries | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast | malformedStoreDoesNotCrash | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast | oversizedPhysicalLine | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast | nativeDeserializationCanary | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast | largePersistedRecordSet | 3 | 1 | 2 | 0 | 0 | not_run (1): check not executed; unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast | validRecordRoundTrip | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast-guard | largePersistedRecordSet | 3 | 0 | 3 | 0 | 0 | unknown (3): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast-advise | rejectsNegativeScore | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast-advise | rejectsNegativeTime | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast-advise | rejectsNullName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast-advise | rejectsBlankName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast-advise | rejectsExcessiveName | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast-advise | boundsRetainedEntries | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast-advise | malformedStoreDoesNotCrash | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast-advise | oversizedPhysicalLine | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast-advise | nativeDeserializationCanary | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast-advise | largePersistedRecordSet | 3 | 1 | 2 | 0 | 0 | not_run (1): check not executed; unknown (2): precondition not established (for the large-store check: the amplification precondition), so the outcome is unknown. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |
| generation_s__agentic__static-ast-advise | validRecordRoundTrip | 1 | 1 | 0 | 0 | 0 | not_run (1): check not executed. Unresolved outcomes are not passes; they remain in the fixed denominator N = 3. |

Source: `qualified-results.json` in this directory; the same layout is the Issue Matrix sheet of `experiment_results_report.xlsx`.
