# Automatic security context, version 1

Run `python3 -m research.extract_context`. The extractor uses javac's syntax-tree
API over the original ApoMario source JAR and the loose ApoIcarus Java sources.
Source bytes, encodings, parser coverage and extraction-rule hashes are retained.
Comments and arbitrary string examples are not treated as executed API calls.

The four outputs are independently selectable:

- **C1:** observed security-relevant syntax, imports and endpoint literals. API
  names and explicit algorithm arguments are retained; purpose, dependency
  versions, receiver binding and runtime reachability remain unresolved.
- **C2:** direct method-local input-to-operation candidates through variables and
  arguments, including reads flowing to retained collections or array dimensions.
  The pass is source-order, not alias-aware, interprocedural or path-sensitive.
  Guards are not proven absent. These records must never be described as verified
  taint paths or vulnerabilities.
- **C3:** matching historical audit records, with their original status. A
  single-source finding remains a lead, and server validation cannot be deduced
  from the client alone. Automatic record retrieval is distinct from validation.
- **C4:** versioned policy requirements, derived from declared study assumptions.
  Code alone cannot supply the intended threat model. The policy was developed
  using historical evidence; future study outcomes must be kept separate.

Every fact carries a stable content-derived ID, type, category, scope, evidence
status, CWE review tags, source location and snippet or rule provenance. CWE
tags are taxonomy labels, not counts of confirmed vulnerabilities.

`feature` currently selects Highscore-named source files plus feature policy;
`repository` includes both complete game source trees. This scope selector is
deliberately documented as a filename projection, not a resolved dependency
slice. No new generated code or evaluation outcome enters this extraction.

Run `python3 -m research.prepare_experiment` to freeze 16 prompt conditions and
140 planned attempts: 20 historical-prompt bridge attempts and 120 ablation
attempts (six security selections × two strategies × ten repetitions). Planned
attempts are not results. The bridge retains the exact historical payloads.
The ablation uses the original no-context task and required source attachments,
fixes the contradictory output suffix consistently across all arms, and provides
the same threat-model disclosure to the zero-fact control. No automatic retry or
model call occurs during preparation.
