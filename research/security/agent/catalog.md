ANGLE: WEAKNESS CATALOG
Walk a published catalog and decide, with repository evidence, which entries apply to the change.
1. Locate the code the change will touch or imitate.
2. Read `cwe-top25-2025.json` (2025 CWE Top 25: identifier, name, description). For every entry decide: applicable to this change in this repository, not applicable, or undecidable. Applicability needs a concrete place in the code or in the specified operations where the weakness could arise; cite it.
3. For each applicable entry, add a `change_risk` (or `existing_risk` when the code already exhibits it) with the CWE identifier, the threat it realises, and anchors; then a `requirement` and a `control` at the enforcement point, with failure behavior.
4. Add entries outside the Top 25 only when the code makes them concrete (for example resource exhaustion while reading a file), and cite the CWE identifier you use.
5. Record verification per requirement, and list undecidable entries as `unknown` items.
Do not force irrelevant entries; a short, applicable list is the goal.
