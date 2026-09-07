# Apo-Games — categorized security findings (representative set)

12 findings across 7 categories. **PROVEN** = runnable PoC; **corroborated** =
independently reported by both auditor families; **lead** = single-family, needs
validation. "clones ×N" = the same vulnerable file ships in N variants.

The point isn't that a game is high-value — it's that **every one of these
categories is the kind of flaw that gets copy-pasted into the next game.** They
are exactly the classes you'd *deliberately seed* to build the clone-and-own
vuln corpus that doesn't exist publicly.

## A. Remote code execution (code loading)
1. **Arbitrary class loaded & instantiated from an attacker-chosen path/URL** — `ApoClassLoader.getMyClass` builds a `URLClassLoader` over an arbitrary URL, `loadClass` → `newInstance()`. **clones ×11 (byte-identical).** `critical` · **PROVEN (PoC 1)** · CWE-470/494
2. **AI/bot class auto-executed on startup from a tampered config file** — the AI path is persisted in `icejump.properties` / `properties.txt`; editing it runs attacker code on next launch (ApoIcejump, ApoMario, ApoSoccer, TutorVolley). `high` · corroborated · CWE-829

## B. Insecure network transport
3. **Highscores & shared levels sent over plain HTTP, no TLS/auth** — `save_highscore.php` / `save_level.php` on `apo-games.de` (`ApoConstants`). **clones ×17.** `medium–high` · corroborated · CWE-319
4. **Public-IP lookup over plain HTTP** (`whatismyip`), response spoofable — `ApoIcejumpNetwork`. `low` · corroborated · CWE-319

## C. Client-side trust / tamperable state
5. **Leaderboard accepts scores asserted entirely by the client** — no server-side validation or signing (`ApoClockHighscoreLoad`). `medium` · corroborated · CWE-602
6. **Editable local prefs/properties unlock content or set balances** — SharedPreferences/properties directly gate levels, coin balance, skull tiers (ApoDice, ApoMono, myTreasure, ApoSimple). `low–medium` · corroborated · CWE-602/807

## D. Unsafe custom parsers / memory safety
7. **Untrusted level/save dimensions → unbounded allocation → OOM / negative-array** — `ApoCheatingLoadSave.readLevel` does `new int[y][x]` from file bytes; a 14-byte file crashes the JVM. `medium–high` · **PROVEN (PoC 2)** · CWE-789
8. **Malformed downloaded level crashes the hand-rolled parser** — unchecked length/format in level decoders (ApoSnake, ApoClock, ApoDice, ApoSimple, ApoSlitherLink). `medium` · corroborated · CWE-20

## E. Path traversal
9. **Replay-controlled level filename escapes the levels directory** (`../`) — `ApoMarioPanel`. `low` · corroborated · CWE-22
10. **Zip-Slip in archive extraction** — `ZipEntry` name used unsanitized in `BitsZip.extract` (Android BitsEngine). `high` · lead (codex was filter-refused on this variant) · CWE-22

## F. Weak cryptographic integrity
11. **MD5 used for score/validation integrity** — fast, unsalted, trivially forgeable (`ApoHelp.getMD5`). Note: same MD5 API elsewhere is a *bitmap cache key* (not security) — context decides. `low–medium` · corroborated · CWE-328

## G. Unsafe native code loading
12. **JNI loads an unverified DLL from `user.dir`** (DLL planting) — `ApoSoccerAIJNI`; also `System.loadLibrary` with a runtime-built name. `high` · corroborated · CWE-114

---

**Coverage:** 7 categories, 12 findings, drawn from 41 cross-model-corroborated
issues (of 144 distinct). Categories checked and **absent** (honest breadth
control): OS command injection, SQL, XXE/XML, WebView/Intent injection —
consistent with an offline game.

**Reuse angle (the pitch):** #1, #3, and #11 are literally the *same file* cloned
across 11–17 variants — one flaw, propagated by copy-paste. That is the project's
thesis made concrete, and the menu of vulnerability classes to seed when
constructing a purpose-built clone-and-own security corpus.
