# Vocabulary for repository security context

Use these terms exactly. Every statement you produce is one of the item kinds
below and must be anchored to code elements where its basis is observed.

| Term | Meaning |
| --- | --- |
| Security property | Attribute of an asset to preserve: confidentiality, integrity, availability, authenticity, non_repudiation, accountability, resistance (ISO/IEC 25010). |
| Asset | Data or functionality whose properties matter for the change. |
| Threat | Potential violation of a property. Classify with STRIDE: spoofing, tampering, repudiation, information_disclosure, denial_of_service, elevation_of_privilege. |
| Trust boundary | Point where data crosses between components of different trust; name the untrusted input, its source and the sink it reaches. Mark `entry_point: true` when untrusted data enters the system there; the entry points form the attack surface. |
| Weakness | Type of flaw that can lead to a violation (CWE identifier). A weakness is not a proven vulnerability. |
| Security requirement | Constraint on an operation of the feature that protects an asset against a threat. |
| Control | Safeguard that satisfies a requirement, stated with its enforcement point (the operation where it must hold) and its failure behavior. |
| Verification | How a requirement can be checked. |
| Unknown | Something inspection could not establish. A search miss is not proof of absence. |

Item kinds: `observation` (observed fact about the code that bears on a security property), `existing_risk`
(suspected weakness in existing code), `change_risk` (risk created by the
change), `requirement`, `control`, `verification`, `unknown`.

Basis: `observed` (from inspected code; anchors required), `task` (from the
feature description), `reasoned` (deduction or proposal).

Anchors: `{"symbol": "pkg.Type#method(ParamType)", "file": "Game/src/pkg/Type.java", "start_line": n, "end_line": m}`.
Symbols come from `code-model.json` / `outline.md`; files and lines from the
workspace. The harness verifies every anchor against the source; an anchor that
does not resolve invalidates the statement. Never invent line numbers.

Catalog identifiers: CWE (`CWE-20`), CAPEC (`CAPEC-153`), ASVS 5.0 requirement
ids (`V2.1.1`), CERT Java rules (`IDS00-J`). Cite only identifiers you are sure of.
