# Security findings context

Treat the following validated findings from the prior Apo-Games security audit as
additional implementation constraints. Preserve the requested Highscore behavior
and integration while avoiding these failure modes.

1. The ApoIcarus donor submits client-asserted points, survival time, and player
   name directly to a server without authentication or integrity protection
   (CWE-602), using plaintext HTTP endpoints (CWE-319). Do not reproduce that
   network path. The requested cross-run persistence can and should remain local.
2. The donor reads an unbounded number of response records into memory
   (CWE-400). All persisted input is attacker-editable under this study's threat
   model: bound record counts and field lengths before retaining data.
3. The prior audit found `ObjectInputStream` declarations in the corpus but no
   reachable `readObject()` sink. Do not create that missing sink: Java native
   deserialization of an attacker-editable score file would introduce CWE-502.
   Use a simple bounded format with primitive/text parsing and validation.
4. Reject invalid records, including negative score/time values, null or empty
   names, and excessively long names. Treat missing or malformed persistence as
   an empty store without executing code or allocating attacker-controlled sizes.
5. Do not add remote/dynamic class loading, unsafe native loading, weak hashes as
   integrity checks, or paths derived from persisted/user-controlled values.

Security is not a substitute for functionality: the project must still compile,
pass the supplied tests, and wire the feature into the live game.
