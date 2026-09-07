# Validation PoCs

Each PoC reproduces a **specific, cited** weakness found in the Apo-Games
corpus. The point is methodological: a security finding only counts once it is
*demonstrated*, not merely asserted by a model. Each PoC is distilled from real
corpus code (file:line noted in the source) and reduced to the minimum that
executes the vulnerable mechanism — no game assets, no network required.

Run all: `./run_all.sh` (needs a JDK on PATH; tested with OpenJDK 26).

| PoC | Finding | Distilled from | CWE |
|-----|---------|----------------|-----|
| poc1_code_loading | Arbitrary code execution via the AI/bot class-loading feature | `ApoIcejumpClassLoader.java` + `ApoIcejumpPanel.loadPlayer()`; same pattern as cloned `ApoClassLoader.java` | CWE-470 / CWE-494 |
| poc2_level_alloc | Denial of service via attacker-controlled allocation sizes in the hand-rolled level parser | `ApoCheatingLoadSave.readLevel()` | CWE-789 / CWE-502-adjacent |

## What each PoC proves — and what it does NOT

- **poc1** proves that a class loaded from an attacker-chosen path executes
  attacker code at `newInstance()` time, with the full privileges of the game
  process. This is the real, reachable mechanism behind the "download an
  opponent bot" feature.
- **poc2** proves that length fields read from an untrusted level/save file
  drive unchecked array allocation, so a tiny crafted file forces the JVM into
  `OutOfMemoryError` or `NegativeArraySizeException`. It does **not** claim a
  Java-serialization gadget-chain RCE: the corpus constructs `ObjectInputStream`
  but never calls `readObject()`, so the classic CWE-502 RCE does not apply
  here. That distinction is itself a finding about model over-claiming.
