ANGLE: ASSETS AND SECURITY REQUIREMENTS
Derive the security requirements of the change from its assets and operations.
1. Locate the code the change will touch or imitate and the public operations the task specifies.
2. List the assets the change creates or uses (records, files, displayed state, identities) and the property each must keep, with anchors to where the asset lives or is produced.
3. For each operation of the feature (create, validate, store, load, display, integrate with the game loop), state the constraints that protect the assets: valid input, bounded size and count, safe persistence and decoding, failure handling, identity of values recorded. Each constraint is one `requirement` with its enforcement point.
4. For each requirement, propose one `control` with failure behavior, citing existing repository conventions where they exist (observed) and marking the rest as prospective.
5. Add `existing_risk` items where the code the change builds on already violates a requirement, with anchors.
6. Record verification per requirement and every unknown.
Prefer few precise requirements over many general ones.
