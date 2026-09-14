# Declared checks in CWE terms

The Highscore security protocol declares eleven executable properties
([PROTOCOL.md](PROTOCOL.md)). To describe them with recognized vocabulary rather
than project-specific labels, [`cwe-mapping.json`](cwe-mapping.json) assigns each
check the closest categories from MITRE's Common Weakness Enumeration. A CWE label
here names the category of weakness a check probes. A failed check is a failed
requirement or robustness contract on one feature, not a confirmed vulnerability.

| Check | Evaluation category | CWE | Why |
| --- | --- | --- | --- |
| validRecordRoundTrip | Positive control | — | Persistence works; not an issue check. |
| rejectsNegativeScore | Input policy | CWE-20, CWE-1284 | Range requirement on an input quantity. |
| rejectsNegativeTime | Input policy | CWE-20, CWE-1284 | Range requirement on an input quantity. |
| rejectsNullName | Input policy | CWE-20, CWE-476 | Missing value; an unchecked null is a later null-dereference exposure. |
| rejectsBlankName | Input policy | CWE-20 | Content requirement on a string. |
| rejectsExcessiveName | Input policy | CWE-20, CWE-1284 | Length requirement on a string. |
| boundsRetainedEntries | Retention policy | CWE-770 | Unlimited retained records. |
| malformedStoreDoesNotCrash | Parser robustness | CWE-755, CWE-20 | One malformed-store fixture; the exception must not escape. |
| oversizedPhysicalLine | Resource stress | CWE-400, CWE-770, CWE-789 | Unbounded line read; observed heap exhaustion is evidence for this fixture only. |
| nativeDeserializationCanary | Deserialization dispatch | CWE-502 | Native object deserialization of the store. |
| largePersistedRecordSet | Resource stress | CWE-400, CWE-770 | Unbounded record loading; precondition-qualified. |

Category level: input policy → CWE-20; retention policy → CWE-770; parser
robustness → CWE-755; resource stress → CWE-400; deserialization dispatch → CWE-502.

## Catalog used for the generic acquisition arm

[`cwe-top25-2025.json`](cwe-top25-2025.json) holds the 2025 CWE Top 25 Most
Dangerous Software Weaknesses (published 2025-12-15) with identifiers, names and
MITRE descriptions retrieved from the CWE API on 2026-09-14. The catalog
acquisition ([`catalog_context.py`](../catalog_context.py)) inserts this complete
list after the task-only sentence. It is the published list, not a selection made
to match the checks above: CWE-20, CWE-476, CWE-502 and CWE-770 appear in it;
CWE-400, CWE-755, CWE-789 and CWE-1284 do not.

## Strategy names in standard terms

| Project label | Closest standard description |
| --- | --- |
| Requirements | Security requirements elicitation for the change |
| Trust boundaries | Data-flow threat modeling across trust boundaries |
| Operational guards | Security controls specified at enforcement points |
| Task only | No security perspective; task statement only |
| CWE catalog | Weakness-catalog vocabulary (CWE Top 25) without a perspective |

The project labels remain in the data; the descriptions are for presentation.
