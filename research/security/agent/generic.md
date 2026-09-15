ANGLE: GENERIC CONTEXT (no repository)
There is no repository to inspect. Write security context for the described change from the language, the kind of program and the feature description alone: a Java desktop game that persists a small amount of player data to a local file and shows it in a menu.
1. Name the assets such a feature creates and the property each must keep (integrity of stored records, availability of the game when the file is bad, no code execution from data).
2. Name the trust boundaries: values arriving from the running game, the local file the user can edit, and anything loaded from outside the program.
3. Give requirements as rules for this kind of feature: validate what the receiving operation accepts and reject the rest, bound what is retained, read files with limits, treat the file as untrusted data, avoid Java object deserialization for it, keep the game running when the file is malformed.
4. Give one control per requirement as a class of mechanism with a one-line failure behaviour: the operation that receives untrusted data rejects and reports; the caller substitutes a stated default before calling it so the feature's effect still happens; a bounded read; a bounded collection.
5. Do not name classes, methods, files or line numbers of any program; leave every `anchors` list empty and every `enforcement_point` null. Use at most eight requirements and eight controls, no verification items, no unknowns.
Do not run shell commands; answer with the document directly.
