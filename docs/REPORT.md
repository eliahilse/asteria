# Apo-Games as a security testbed — feasibility & first findings

**For:** the bachelor-thesis collaboration (context-for-LLMs in SE; security track)
**Prepared by:** Elia · draft for the next meeting
**Scope of this pass:** recon of the Apo-Games corpus, a first categorical
security audit, and a judgement on whether this corpus can carry the security
angle of the project. Not an exhaustive bug hunt.

> One-line answer: **Yes — usable, and uniquely good for the *reuse* half of the
> project, because real security flaws here are cloned unchanged across the
> family. But it is a desktop/Android-game corpus with no server surface, so it
> should be paired with an exposed-endpoint corpus (Prof. Wellig's projects or a
> public dataset) for the *generation* half.**

---

## 1. What the corpus actually is

- **26 variants**: 20 Java desktop games + 6 Android projects (incl. the shared
  `BitsEngine`), ~232k + ~72k LOC, 1686 `.java` files after normalization.
- It is a **clone-and-own family**: the same files recur across variants, often
  byte-for-byte. This is the property that matters for us — it is the SPLC 2018
  case study and the same corpus the project's paper already uses.
- Practical snag handled up front: 7 of 20 Java variants ship their source
  *inside* the distributed jar, not as loose files. The pipeline unpacks those
  so every variant is audited on equal footing.

## 2. Threat model (what "security" means for a game)

These are not servers, so the relevant attacker is not "someone hitting an
endpoint." It is:

1. **Malicious / MITM server** — the games talk to `apo-games.de` over **plain
   HTTP** (highscore + level up/download). No TLS, no auth, no integrity.
2. **Malicious local file** — hand-rolled save / level / replay / "cheat"
   formats parsed by hand (`.cheat`, `.mar`, `.skunk`, `.defence`, `.rep`).
3. **Hostile user-generated content** — levels downloaded from the shared level
   server are exactly these untrusted files, parsed by the same code.
4. **Local tampering to cheat** — client-side "trust the client" scoring.

Severity in this report is judged against *this* model — we do not inflate
findings by pretending a web endpoint exists.

## 3. Validated findings (proven, not just asserted)

Two findings were reduced to **runnable proofs** (`poc/run_all.sh`, passes 3/3 on
OpenJDK 26). Each PoC is distilled from cited corpus code.

### F1 — Arbitrary code execution via the AI/bot class-loading feature · **critical**
- **Where:** `ApoIcejumpClassLoader.loadClass → defineClass`, invoked by
  `ApoIcejumpPanel.loadPlayer() : 647` as `new ApoIcejumpClassLoader(path, classname).getAI()`.
  The cloned `org/apogames/help/ApoClassLoader.java` does the same with a
  `URLClassLoader` over an **arbitrary URL** (remote when running as an applet).
- **Mechanism:** the "load AI player" feature takes a path + class name, loads
  that `.class`, and `newInstance()`s it. The class file is attacker-supplied
  (you download an opponent bot). Instantiation runs attacker code with the game
  process's full privileges.
- **Worse than "user loads a bot": it auto-executes on startup.** The audit
  surfaced (corroborated) that several variants **persist the AI class + path in
  a startup config file** and reload it on launch — `ApoIcejump`
  (`icejump.properties`), `ApoMario`, `ApoSoccer` (`properties.txt`),
  `TutorVolley`. So tampering a plain config file → code execution at next launch,
  no user action.
- **Reuse angle:** `ApoClassLoader.java` is **byte-identical across ~11
  variants** (17 copies of the file, 2 distinct versions). One flaw, cloned
  unchanged — the clearest possible illustration of security debt propagating
  through reuse. `remote_code_loading` is the single most-corroborated category:
  **13 corroborated findings across 18 variants.**
- **Proof:** PoC 1 — the malicious "AI" writes a marker file on load.
- **CWE-470 / CWE-494 / CWE-829.**
- **Related, same class:** `ApoSoccerAIJNI` loads an **unverified DLL from
  `user.dir`** (CWE-114, DLL planting, corroborated); `BitsEngine`'s `BitsZip.extract`
  is a **Zip-Slip** path traversal (CWE-22, strong single-source lead — codex was
  filter-refused on that variant so it is uncorroborated).

### F2 — Denial of service via unchecked allocation in the level/save parser · **medium–high**
- **Where:** `ApoCheatingLoadSave.readLevel() : 371–378` reads `y`, `x` from the
  file and does `new int[y][x]` with no bounds check; the same shape recurs in
  the other hand-rolled parsers.
- **Mechanism:** a **14-byte** crafted save/level file declares huge or negative
  dimensions → `OutOfMemoryError` / `NegativeArraySizeException`. Reachable via
  the downloaded-level path (vector 3).
- **Proof:** PoC 2 — tiny file crashes a 64 MB JVM.
- **CWE-789.**

### F3 — Insecure transport + client-side trust for scores/levels · **high (integrity)**
- **Where:** `ApoClockHighscoreLoad.save()` and the `*Load` siblings POST
  `points`/`name`/level data to `http://…/save_highscore.php` etc. over plain
  HTTP; the client simply asserts its own score. Endpoints enumerated in the
  corpus (`save_level.php`, `get_level.php`, `save_highscore.php`).
- **Mechanism:** no TLS (eavesdrop/MITM), no signature or nonce (forge or replay
  any score), no server-side validation implied by the client. Downloaded levels
  are then fed to the F2 parser.
- **Status:** mechanism confirmed by reading the code; not turned into a live PoC
  because it needs the (now-defunct) server. Documented as reachable-by-design.
- **CWE-319 / CWE-602 / CWE-345.**

## 4. Category taxonomy & clone propagation

**189 raw findings** from the two families → **144 distinct issues** →
**41 corroborated by both families.** Full tables in **`docs/RESULTS.md`**
(auto-generated). Corroborated counts by category:

| Category | Variants | Corroborated | Max clone spread |
|---|--:|--:|--:|
| remote_code_loading | 18 | 13 | 17 |
| insecure_transport | 21 | 11 | 17 |
| unsafe_custom_parser | 19 | 9 | 17 |
| client_side_trust | 15 | 5 | 9 |
| resource_exhaustion | 21 | 1 | 17 |
| path_traversal / unsafe_native_call | 3 / 2 | 1 / 1 | 2 / 1 |

Categories actively looked for and **absent**: OS command exec, SQL, XXE/XML,
Android WebView/Intent injection — consistent with "offline game, no server."
The lone `untrusted_deserialization` entry is single-source and is the **debunked
over-claim** from §5 (no `readObject()` exists), not a real finding.

## 5. The methodology, and why it is trustworthy

See **`docs/METHODOLOGY.md`** for the full description. In short: a deterministic
static sink census grounds the prompts; every variant is then audited
**independently by two unrelated model families** (ox-alpha and gpt-5.6-sol);
findings reported by both are promoted, single-source ones stay leads;
clone-propagation is measured deterministically; and the top findings are
**hand-verified and executed as PoCs**.

**A tooling finding worth recording.** gpt-5.6-sol (codex) was **refused by
OpenAI's cybersecurity content filter on 6 of 26 variants** — exactly the
security-relevant ones — while ox-alpha completed all 26. For research that
audits code for weaknesses, a frontier model's safety filter silently reduces
coverage precisely where it matters; model/tool choice is a methodological
variable, not a detail. (Net effect here: no variant fully lost — 24 have both
families, `ApoStarz` is codex-only, the 6 refused are ox-alpha-only.)

**Honesty check we want in the paper.** Both the census and a model flagged
`ObjectInputStream` in `ApoCheating` as *untrusted-deserialization RCE
(CWE-502)*. Manual grep found **zero `readObject()` calls in the entire
corpus** — the stream is only read with `readInt()`, so the gadget-chain RCE does
not apply; the real defect is the DoS in F2. Likewise, MD5 in the Android
`BitsEngine` is a **bitmap cache key**, not a security control. Same APIs,
different context → different (or no) severity. This is direct evidence for the
project's core thesis — that **security judgement needs *context*, not just
API-pattern matching** — and it is why the bar here is executable proof, not
model consensus.

## 6. Verdict & recommendation for the meeting

- **Adopt Apo-Games for the *reuse / propagation* security experiments.** It is
  the only corpus on the table where we can *measure* a real flaw being cloned
  across a family (F1: identical loader in ~11 variants). That is a paper-worthy
  result on its own and it plugs straight into the existing reuse scenarios.
- **Do not rely on it for the *generation* security experiments.** No server /
  endpoint surface means whole vulnerability classes (injection, authz, web)
  cannot appear. Pair it with an exposed-surface corpus — **Prof. Wellig's
  repository** (bonus: likely outside frontier-LLM training data) and/or a public
  set — for those.
- **Next concrete steps I'd propose:**
  1. finalize the corroborated-findings table + one more PoC (F3 against a local
     stub server) → a clean "validated security-issue set" for the corpus;
  2. use F1's clone spread as the seed experiment for "does injected reuse
     context make the LLM propagate or *fix* a known-vulnerable cloned component?";
  3. request Wellig's corpus and run the same pipeline to compare a game corpus
     vs an exposed-surface corpus for the generation track.

---

### How to reproduce
```
scripts/build_corpus.sh
python3 scripts/sink_census.py
scripts/run_audit.sh oxa 8 && scripts/run_audit.sh codex 8
python3 scripts/extract_findings.py && python3 scripts/reconcile.py
python3 scripts/gen_report.py       # -> docs/RESULTS.md
poc/run_all.sh                      # -> 3/3 PoCs pass (needs a JDK)
```
Raw model transcripts are kept under `audit/runs/{oxa,codex}/` for audit.
