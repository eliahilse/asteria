# Issue-handling catalog: i07-operational-replication

> **Disclaimer.** Categories are a reviewer classification of the handling *mechanism* visible in the final delivered source; they are not measurements. The qualified evaluator outcome (`qualifiedStatus`) is authoritative for every pass/fail claim. Rows where the reviewer category and the evaluator outcome conflict are marked `flag = review` and listed below for human resolution; this catalog does not resolve them.

Manifest fingerprint `8829219e6a4356eb6f95e2d0330756c4e241584d417f7563940053cdf9157821`; qualified analysis `54328cd6ead96f59f5efe9ce1794cb9bd2d0338b79641c6f6b52112c86ebe604`; 80 classifications by claude-fable-5.1/batch-1, claude-fable-5.1/batch-2, claude-fable-5.1/batch-3, claude-fable-5.1/batch-4, claude-fable-5.1/batch-5, claude-fable-5.1/batch-6, claude-fable-5.1/batch-7, claude-fable-5.1/batch-8; 2 flagged rows. Per-row data: `issue-handling.csv`; counts: `issue-handling-summary.csv`; everything: `issue-handling.json`.

## rejectsNegativeScore

**Fixture / expected property:** `storeRun(-1, 1, "Player")` returns false, retains no record

**Protocol interpretation:** Explicit input requirement

**CWE:** CWE-20, CWE-1284

**Categories:** rejected (safe), sanitized (unsafe), accepted (unsafe), crashed (unsafe), unresolved (unresolved)

| Strategy | N | rejected | sanitized | accepted | crashed | unresolved | status:fail | status:unresolved | status:pass |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| none | 20 | 0 | 0 | 19 | 0 | 1 | 19 | 1 | 0 |
| operations | 20 | 3 | 0 | 16 | 0 | 1 | 16 | 1 | 3 |
| requirements | 20 | 10 | 0 | 10 | 0 | 0 | 10 | 0 | 10 |
| boundaries | 20 | 1 | 2 | 17 | 0 | 0 | 19 | 0 | 1 |

### Representative excerpts

**none** — `i07-operational-replication__generation_sfb__none__r1` classified `accepted`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L44-L44` — storeRun stores score unchanged (L44); no sign check and no clamping.

```text
ApoMarioHighscore.java:44          entries.add(new Entry(name, score, Math.max(0, survivalTime)));
```

**none** — `i07-operational-replication__generation_sfb__none__r2` classified `accepted`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L43-L43` — score is stored unchanged (L43); no check or clamp.

```text
ApoMarioHighscore.java:43          runs.add(new Run(score, survivalTime, name));
```

**operations** — `i07-operational-replication__generation_sfb__operations__r1` classified `accepted`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L58-L58` — The guard at L51 only rejects negative survivalTime and null name; score is stored unchanged (L58).

```text
ApoMarioHighscore.java:58          runs.add(new Run(name, score, survivalTime));
```

**operations** — `i07-operational-replication__generation_sfb__operations__r3` classified `accepted`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L47-L47` — Guard at L47 only covers negative time and null name; score is stored unchanged (L50).

```text
ApoMarioHighscore.java:47          if (survivalTime < 0 || playerName == null) return false;
```

**requirements** — `i07-operational-replication__reuse_b__requirements__r1` classified `rejected`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L39-L39` — storeRun returns false when score < 0 (L39) before any record is added; no clamping.

```text
ApoMarioHighscore.java:39          if (score < 0 || survivalTime < 0 || !validName(playerName)) return false;
```

**requirements** — `i07-operational-replication__reuse_b__requirements__r2` classified `rejected`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L35-L35` — storeRun returns false when score < 0 (L35) before storing; no clamping.

```text
ApoMarioHighscore.java:35          if (score < 0 || survivalTime < 0 || !validName(playerName)) return false;
```

**boundaries** — `i07-operational-replication__reuse_b__boundaries__r3` classified `accepted`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L44-L44` — storeRun never examines score; a negative score is stored unchanged (L44) and true is returned.

```text
ApoMarioHighscore.java:44          entries.add(new Entry(score, Math.max(0, survivalTime), name));
```

**boundaries** — `i07-operational-replication__generation_sfb__boundaries__r1` classified `accepted`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L42-L43` — The only score guard is score < Integer.MIN_VALUE (L42), which is always false; a negative score is stored unchanged (L43). Consistent with the evaluator's fail.

```text
ApoMarioHighscore.java:42          if (name.length() == 0 || score < Integer.MIN_VALUE || survivalTime < 0) return false;
ApoMarioHighscore.java:43          records.add(new Record(name, score, survivalTime));
```

### Flagged rows

None.

## rejectsNegativeTime

**Fixture / expected property:** Negative time rejected

**Protocol interpretation:** Explicit input requirement

**CWE:** CWE-20, CWE-1284

**Categories:** rejected (safe), sanitized (unsafe), accepted (unsafe), crashed (unsafe), unresolved (unresolved)

| Strategy | N | rejected | sanitized | accepted | crashed | unresolved | status:fail | status:unresolved | status:pass |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| none | 20 | 0 | 19 | 0 | 0 | 1 | 19 | 1 | 0 |
| operations | 20 | 19 | 0 | 0 | 0 | 1 | 0 | 1 | 19 |
| requirements | 20 | 20 | 0 | 0 | 0 | 0 | 0 | 0 | 20 |
| boundaries | 20 | 6 | 14 | 0 | 0 | 0 | 14 | 0 | 6 |

### Representative excerpts

**none** — `i07-operational-replication__generation_s__none__r1` classified `sanitized`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L58-L58` — Negative survivalTime is clamped with Math.max(0, survivalTime) and the record is still stored; storeRun returns true.

```text
ApoMarioHighscore.java:58          runs.add(new Run(name, score, Math.max(0, survivalTime)));
```

**none** — `i07-operational-replication__generation_s__none__r2` classified `sanitized`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L52-L52` — Negative survivalTime is clamped to 0 via Math.max and the record is still stored.

```text
ApoMarioHighscore.java:52          entries.add(new Entry(score, Math.max(0, survivalTime), name));
```

**operations** — `i07-operational-replication__generation_s__operations__r1` classified `rejected`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L51-L51` — storeRun returns false when survivalTime < 0 before anything is added; no clamping.

```text
ApoMarioHighscore.java:51          if (name == null || survivalTime < 0) return false;
```

**operations** — `i07-operational-replication__generation_s__operations__r2` classified `rejected`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L40-L40` — storeRun returns false when survivalTime < 0 before storing; no clamping.

```text
ApoMarioHighscore.java:40          if (survivalTime < 0 || playerName == null) return false;
```

**requirements** — `i07-operational-replication__generation_s__requirements__r1` classified `rejected`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L54-L54` — storeRun returns false when survivalTime < 0 (L54); no clamping.

```text
ApoMarioHighscore.java:54          if (name.length() == 0 || name.length() > MAX_NAME || survivalTime < 0) return false;
```

**requirements** — `i07-operational-replication__generation_s__requirements__r2` classified `rejected`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L40-L40` — storeRun returns false when survivalTime < 0 (L40).

```text
ApoMarioHighscore.java:40          if (name == null || survivalTime < 0) return false;
```

**boundaries** — `i07-operational-replication__generation_s__boundaries__r2` classified `sanitized`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L56-L56` — storeRun clamps a negative survivalTime to 0 (L56) and stores the record (L57).

```text
ApoMarioHighscore.java:56          if (survivalTime < 0) survivalTime = 0;
```

**boundaries** — `i07-operational-replication__generation_s__boundaries__r3` classified `sanitized`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L53-L53` — storeRun clamps a negative survivalTime to 0 (L53) and stores the record (L54).

```text
ApoMarioHighscore.java:53          if (survivalTime < 0) survivalTime = 0;
```

### Flagged rows

None.

## rejectsNullName

**Fixture / expected property:** Null name rejected

**Protocol interpretation:** Explicit input requirement

**CWE:** CWE-20, CWE-476

**Categories:** rejected (safe), sanitized (unsafe), accepted (unsafe), crashed (unsafe), unresolved (unresolved)

| Strategy | N | rejected | sanitized | accepted | crashed | unresolved | status:fail | status:unresolved | status:pass |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| none | 20 | 0 | 19 | 0 | 0 | 1 | 19 | 1 | 0 |
| operations | 20 | 15 | 4 | 0 | 0 | 1 | 4 | 1 | 15 |
| requirements | 20 | 20 | 0 | 0 | 0 | 0 | 0 | 0 | 20 |
| boundaries | 20 | 5 | 15 | 0 | 0 | 0 | 15 | 0 | 5 |

### Representative excerpts

**none** — `i07-operational-replication__generation_sfb__none__r1` classified `sanitized`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L42-L42` — null playerName is substituted with "Unknown" (L42) and the record is stored.

```text
ApoMarioHighscore.java:42          String name = playerName == null ? "Unknown" : playerName.trim();
```

**none** — `i07-operational-replication__generation_sfb__none__r2` classified `sanitized`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L40-L40` — null playerName becomes "Unknown" (L40) and the record is stored.

```text
ApoMarioHighscore.java:40          String name = playerName == null ? "Unknown" : playerName.trim();
```

**operations** — `i07-operational-replication__generation_s__operations__r2` classified `rejected`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L40-L40` — storeRun returns false when playerName == null before storing.

```text
ApoMarioHighscore.java:40          if (survivalTime < 0 || playerName == null) return false;
```

**operations** — `i07-operational-replication__generation_sfb__operations__r3` classified `rejected`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L47-L47` — storeRun returns false when playerName == null (L47) before storing.

```text
ApoMarioHighscore.java:47          if (survivalTime < 0 || playerName == null) return false;
```

**requirements** — `i07-operational-replication__generation_s__requirements__r1` classified `rejected`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L52-L52` — storeRun returns false when playerName == null (L52) before any trim is attempted.

```text
ApoMarioHighscore.java:52          if (playerName == null) return false;
```

**requirements** — `i07-operational-replication__generation_s__requirements__r2` classified `rejected`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L40-L40` — cleanName returns null for a null input (L99) and storeRun returns false on a null cleaned name (L40).

```text
ApoMarioHighscore.java:40          if (name == null || survivalTime < 0) return false;
```

**boundaries** — `i07-operational-replication__generation_s__boundaries__r2` classified `sanitized`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L45-L45` — cleanName substitutes "Player" for a null name (L45) and the record is stored (L57).

```text
ApoMarioHighscore.java:45          if (name == null) return "Player";
```

**boundaries** — `i07-operational-replication__generation_s__boundaries__r3` classified `sanitized`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L42-L42` — cleanName substitutes "Player" for a null name (L42) and the record is stored (L54).

```text
ApoMarioHighscore.java:42          if (name == null) return "Player";
```

### Flagged rows

None.

## rejectsBlankName

**Fixture / expected property:** Whitespace-only name rejected

**Protocol interpretation:** Explicit input requirement

**CWE:** CWE-20

**Categories:** rejected (safe), sanitized (unsafe), accepted (unsafe), crashed (unsafe), unresolved (unresolved)

| Strategy | N | rejected | sanitized | accepted | crashed | unresolved | status:fail | status:unresolved | status:pass |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| none | 20 | 0 | 19 | 0 | 0 | 1 | 19 | 1 | 0 |
| operations | 20 | 12 | 7 | 0 | 0 | 1 | 7 | 1 | 12 |
| requirements | 20 | 20 | 0 | 0 | 0 | 0 | 0 | 0 | 20 |
| boundaries | 20 | 6 | 14 | 0 | 0 | 0 | 14 | 0 | 6 |

### Representative excerpts

**none** — `i07-operational-replication__reuse_b__none__r3` classified `sanitized`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L31-L31` — A blank playerName (trim().length()==0) becomes "Player" (L31); the record is stored.

```text
ApoMarioHighscore.java:31          names.add(playerName == null || playerName.trim().length() == 0 ? "Player" : playerName.trim());
```

**none** — `i07-operational-replication__generation_sfb__none__r1` classified `sanitized`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L42-L43` — Name is trimmed and an empty result is replaced by "Unknown" (L42-L43); the record is stored.

```text
ApoMarioHighscore.java:42          String name = playerName == null ? "Unknown" : playerName.trim();
ApoMarioHighscore.java:43          if (name.length() == 0) name = "Unknown";
```

**operations** — `i07-operational-replication__reuse_sb__operations__r1` classified `rejected`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L77-L77` — cleanName trims and maps an empty result to null (L71-L72); storeRun returns false (L77).

```text
ApoMarioHighscore.java:77          if (name == null || survivalTime < 0 || entries.size() >= MAX_RECORDS) return false;
```

**operations** — `i07-operational-replication__reuse_sb__operations__r4` classified `rejected`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L41-L41` — normalize trims and returns null for an empty result (L84-L85); storeRun returns false (L41).

```text
ApoMarioHighscore.java:41          if (name == null || survivalTime < 0) return false;
```

**requirements** — `i07-operational-replication__generation_s__requirements__r2` classified `rejected`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L40-L40` — cleanName trims and returns null when the result is empty (L100-L101), so storeRun returns false (L40).

```text
ApoMarioHighscore.java:40          if (name == null || survivalTime < 0) return false;
```

**requirements** — `i07-operational-replication__generation_s__requirements__r3` classified `rejected`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L48-L48` — cleanName trims and returns null when the result is empty (L105-L106), so storeRun returns false (L48).

```text
ApoMarioHighscore.java:48          if (name == null || survivalTime < 0) return false;
```

**boundaries** — `i07-operational-replication__generation_s__boundaries__r2` classified `sanitized`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L57-L57` — cleanName trims and substitutes "Player" for an empty result (L51-L52); the record is stored (L57).

```text
ApoMarioHighscore.java:57          runs.add(new Run(score, survivalTime, cleanName(playerName)));
```

**boundaries** — `i07-operational-replication__generation_s__boundaries__r3` classified `sanitized`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L54-L54` — cleanName trims and substitutes "Player" for an empty result (L48-L49); the record is stored (L54).

```text
ApoMarioHighscore.java:54          runs.add(new Run(score, survivalTime, cleanName(playerName)));
```

### Flagged rows

None.

## rejectsExcessiveName

**Fixture / expected property:** 1,024-character name rejected

**Protocol interpretation:** Explicit input requirement

**CWE:** CWE-20, CWE-1284

**Categories:** rejected (safe), sanitized (unsafe), accepted (unsafe), crashed (unsafe), unresolved (unresolved)

| Strategy | N | rejected | sanitized | accepted | crashed | unresolved | status:fail | status:unresolved | status:pass |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| none | 20 | 0 | 4 | 15 | 0 | 1 | 19 | 1 | 0 |
| operations | 20 | 6 | 13 | 0 | 0 | 1 | 13 | 1 | 6 |
| requirements | 20 | 16 | 4 | 0 | 0 | 0 | 4 | 0 | 16 |
| boundaries | 20 | 2 | 18 | 0 | 0 | 0 | 18 | 0 | 2 |

### Representative excerpts

**none** — `i07-operational-replication__reuse_b__none__r3` classified `accepted`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L31-L31` — The name is only trimmed (L31); there is no length cap and the full-length name is stored.

```text
ApoMarioHighscore.java:31          names.add(playerName == null || playerName.trim().length() == 0 ? "Player" : playerName.trim());
```

**none** — `i07-operational-replication__reuse_sb__none__r1` classified `accepted`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L38-L38` — Name is stored after only tab/newline replacement (L38); there is no length limit.

```text
ApoMarioHighscore.java:38          names.add(name.replace('\t', ' ').replace('\n', ' '));
```

**operations** — `i07-operational-replication__generation_s__operations__r1` classified `sanitized`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L52-L52` — normalize truncates the name to MAX_NAME_LENGTH = 64 characters (L156) and the record is stored; over-long names are never rejected.

```text
ApoMarioHighscore.java:52          entries.add(new Entry(name, score, survivalTime));
```

**operations** — `i07-operational-replication__generation_s__operations__r2` classified `sanitized`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L43-L43` — normalize truncates the name to MAX_NAME = 48 characters (L104) and the record is then stored; over-long names are never rejected.

```text
ApoMarioHighscore.java:43          runs.add(new Run(name, score, survivalTime));
```

**requirements** — `i07-operational-replication__generation_s__requirements__r1` classified `rejected`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L54-L54` — storeRun returns false when the trimmed name is longer than MAX_NAME=64 (L54); no truncation.

```text
ApoMarioHighscore.java:54          if (name.length() == 0 || name.length() > MAX_NAME || survivalTime < 0) return false;
```

**requirements** — `i07-operational-replication__generation_s__requirements__r3` classified `rejected`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L48-L48` — cleanName returns null when the trimmed length exceeds MAX_NAME=64 (L106), so storeRun returns false (L48); control characters are replaced by '?' (L110) but there is no truncation.

```text
ApoMarioHighscore.java:48          if (name == null || survivalTime < 0) return false;
```

**boundaries** — `i07-operational-replication__generation_s__boundaries__r1` classified `sanitized`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L50-L50` — normalize copies at most MAX_NAME_LENGTH=40 non-control characters (L151), silently truncating; the truncated record is stored (L50).

```text
ApoMarioHighscore.java:50          records.add(new Record(score, survivalTime, name));
```

**boundaries** — `i07-operational-replication__generation_s__boundaries__r2` classified `sanitized`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L57-L57` — cleanName copies at most MAX_NAME=40 non-control characters (L47), silently truncating; the truncated record is stored (L57).

```text
ApoMarioHighscore.java:57          runs.add(new Run(score, survivalTime, cleanName(playerName)));
```

### Flagged rows

None.

## boundsRetainedEntries

**Fixture / expected property:** After 120 accepted valid records, at most 100 retained

**Protocol interpretation:** Retention requirement; not proof of bounded loading

**CWE:** CWE-770

**Categories:** bounded_on_store (safe), bounded_on_persist_only (unsafe), bounded_on_load_only (unsafe), unbounded (unsafe), unresolved (unresolved)

| Strategy | N | bounded_on_store | bounded_on_persist_only | bounded_on_load_only | unbounded | unresolved | status:fail | status:unresolved | status:pass |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| none | 20 | 10 | 0 | 0 | 9 | 1 | 9 | 1 | 10 |
| operations | 20 | 15 | 0 | 0 | 4 | 1 | 4 | 1 | 15 |
| requirements | 20 | 20 | 0 | 0 | 0 | 0 | 0 | 0 | 20 |
| boundaries | 20 | 20 | 0 | 0 | 0 | 0 | 0 | 0 | 20 |

### Representative excerpts

**none** — `i07-operational-replication__generation_sfb__none__r1` classified `bounded_on_store`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L45-L45` — storeRun calls sortAndTrim (L45) which drops entries beyond MAX_ENTRIES=20 (L115).

```text
ApoMarioHighscore.java:45          sortAndTrim();
```

**none** — `i07-operational-replication__generation_sfb__none__r4` classified `bounded_on_store`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L46-L46` — storeRun calls sortAndTrim (L46) which removes entries beyond MAX_ENTRIES=20 (L110).

```text
ApoMarioHighscore.java:46          sortAndTrim();
```

**operations** — `i07-operational-replication__generation_sfb__operations__r1` classified `bounded_on_store`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L59-L59` — storeRun calls sortAndLimit (L59) which removes entries beyond MAX_ENTRIES=100 (L189).

```text
ApoMarioHighscore.java:59          sortAndLimit();
```

**operations** — `i07-operational-replication__generation_sfb__operations__r2` classified `bounded_on_store`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L69-L69` — storeRun calls sortEntries (L69) which removes entries beyond MAX_ENTRIES=100 (L63).

```text
ApoMarioHighscore.java:69          sortEntries();
```

**requirements** — `i07-operational-replication__reuse_b__requirements__r1` classified `bounded_on_store`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L41-L41` — storeRun calls sortAndTrim (L41), which removes trailing entries until size <= MAX_ENTRIES=100 (L99).

```text
ApoMarioHighscore.java:41          sortAndTrim();
```

**requirements** — `i07-operational-replication__reuse_b__requirements__r2` classified `bounded_on_store`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L36-L36` — storeRun calls sortAndTrim (L36), which rebuilds the parallel lists keeping only the first MAX_ENTRIES=100 sorted indices (L48).

```text
ApoMarioHighscore.java:36          names.add(playerName.trim()); scores.add(Integer.valueOf(score)); times.add(Integer.valueOf(survivalTime)); sortAndTrim(); persistAcrossRuns(); return true;
```

**boundaries** — `i07-operational-replication__generation_s__boundaries__r5` classified `bounded_on_store`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L37-L37` — storeRun adds the entry and calls sort(), which removes tail entries until size <= MAX=100 (L75).

```text
ApoMarioHighscore.java:37          entries.add(new Entry(score, survivalTime, name)); sort(); persistAcrossRuns(); return true;
```

**boundaries** — `i07-operational-replication__generation_sfb__boundaries__r1` classified `bounded_on_store`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L136-L136` — sortAndTrim after each add (L44) removes records beyond MAX_RECORDS=100 (L136).

```text
ApoMarioHighscore.java:136          while (records.size() > MAX_RECORDS) records.remove(records.size() - 1);
```

### Flagged rows

None.

## malformedStoreDoesNotCrash

**Fixture / expected property:** Fixed malformed byte sequence does not escape the constructor as an exception

**Protocol interpretation:** One malformed-input fixture

**CWE:** CWE-755, CWE-20

**Categories:** guarded_fallback (safe), guarded_reset (safe), unguarded (unsafe), unresolved (unresolved)

| Strategy | N | guarded_fallback | guarded_reset | unguarded | unresolved | status:fail | status:unresolved | status:pass |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| none | 20 | 19 | 0 | 0 | 1 | 0 | 1 | 19 |
| operations | 20 | 19 | 0 | 0 | 1 | 0 | 1 | 19 |
| requirements | 20 | 20 | 0 | 0 | 0 | 0 | 0 | 20 |
| boundaries | 20 | 20 | 0 | 0 | 0 | 0 | 0 | 20 |

### Representative excerpts

**none** — `i07-operational-replication__generation_sfb__none__r1` classified `guarded_fallback`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L108-L108` — Lines with the wrong field count are skipped (L101) and NumberFormatException is caught per record (L102-L103); IOException is swallowed (L108). Board keeps the parseable subset.

```text
ApoMarioHighscore.java:108          } catch (IOException ignored) { }
```

**none** — `i07-operational-replication__generation_sfb__none__r4` classified `guarded_fallback`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L103-L103` — Lines without 3 fields are skipped (L96), NumberFormatException caught per record (L97-L98), IOException swallowed (L103); parseable subset kept.

```text
ApoMarioHighscore.java:103          } catch (IOException ignored) { }
```

**operations** — `i07-operational-replication__generation_sfb__operations__r2` classified `guarded_fallback`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L148-L148` — Missing header returns with an empty board (L133); malformed lines are skipped (L137) and NumberFormatException caught per record (L142); IOException swallowed (L148).

```text
ApoMarioHighscore.java:148          } catch (IOException ignored) { }
```

**operations** — `i07-operational-replication__generation_sfb__operations__r5` classified `guarded_fallback`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L135-L135` — Wrong field count skipped (L123); NumberFormatException caught per record (L129); negative time / empty name records dropped (L128); IOException swallowed (L135).

```text
ApoMarioHighscore.java:135          } catch (IOException ignored) { }
```

**requirements** — `i07-operational-replication__reuse_sb__requirements__r3` classified `guarded_fallback`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L94-L94` — load catches Exception (covers NumberFormatException from parseInt at L80/L87-L88 and IOException) and clears all three lists (L94); a missing header line returns early (L78). The file is not rewritten.

```text
ApoMarioHighscore.java:94          } catch (Exception ex) { names.clear(); scores.clear(); survivalTimes.clear(); }
```

**requirements** — `i07-operational-replication__reuse_sb__requirements__r5` classified `guarded_fallback`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L72-L72` — The whole load body is wrapped in try { ... } catch(Exception) which clears all three lists (L72); NumberFormatException from parseInt and IOException are both absorbed. The file is not rewritten.

```text
ApoMarioHighscore.java:72          try { if (Files.size(store)>131072) return; BufferedReader in=Files.newBufferedReader(store,StandardCharsets.UTF_8); try { String first=in.readLine(); int count=Integer.parseInt(first==null?"0":first); if(count<0||count>MAX_ENTRIES)return; for(int i=0;i<count;i++){String s=in.readLine(),t=in.readLine(),n=in.readLine(); if(s==null||t==null||n==null)break; int score=Integer.parseInt(s),time=Integer.parseInt(t); String name=validName(n); if(score>=0&&time>=0&&name!=null){scores.add(score);survivalTimes.add(time);names.add(name);}} } finally {in.close();} sortAndTrim(); } catch(Exception ex){names.clear();scores.clear();survivalTimes.clear();}
```

**boundaries** — `i07-operational-replication__reuse_sb__boundaries__r1` classified `guarded_fallback`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L123-L123` — load reads into a separate list and catches Exception (L137), covering NumberFormatException and Base64 IllegalArgumentException; any anomaly (bad header L123, wrong field count L128, negative time L130, bad name L132) returns before the list is applied at L136, so the board stays empty. The file is not rewritten.

```text
ApoMarioHighscore.java:123                  if (!"APO_MARIO_HIGHSCORE_1".equals(in.readLine())) return;
```

**boundaries** — `i07-operational-replication__reuse_sb__boundaries__r2` classified `guarded_fallback`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L130-L130` — load parses into a separate list and catches IOException (L146-L148; EOFException on truncation, and an explicit IOException for a bad name length at L135); bad magic/version/count returns early (L130). Only IOException is caught, but no RuntimeException is reachable in the parse path since the array length is validated first. The file is not reset.

```text
ApoMarioHighscore.java:130                  if (magic != MAGIC || version != VERSION || count < 0 || count > MAX_RECORDS) { in.close(); return; }
```

### Flagged rows

None.

## oversizedPhysicalLine

**Fixture / expected property:** 64 MiB ASCII line, streamed to disk; constructor completes within heap/time limits

**Protocol interpretation:** Finite resource robustness test; observed OOM is direct evidence for this fixture

**CWE:** CWE-400, CWE-770, CWE-789

**Categories:** bounded_read (safe), post_read_limit (unsafe), unbounded_read (unsafe), unresolved (unresolved)

| Strategy | N | bounded_read | post_read_limit | unbounded_read | unresolved | status:fail | status:unresolved | status:pass |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| none | 20 | 2 | 0 | 17 | 1 | 17 | 1 | 2 |
| operations | 20 | 13 | 5 | 1 | 1 | 6 | 1 | 13 |
| requirements | 20 | 9 | 8 | 3 | 0 | 11 | 0 | 9 |
| boundaries | 20 | 10 | 10 | 0 | 0 | 10 | 0 | 10 |

### Representative excerpts

**none** — `i07-operational-replication__generation_sfb__none__r2` classified `unbounded_read`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L92-L92` — Files.readAllLines (L92) reads the entire file into memory with no size cap and no length check afterwards.

```text
ApoMarioHighscore.java:92              for (String line : Files.readAllLines(store, UTF8)) {
```

**none** — `i07-operational-replication__generation_sfb__none__r3` classified `unbounded_read`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L40-L40` — Files.readAllLines (L40) reads the whole file with no size cap; no length check afterwards.

```text
ApoMarioHighscore.java:40              for (String line : Files.readAllLines(store, Charset.forName("UTF-8"))) {
```

**operations** — `i07-operational-replication__generation_s__operations__r4` classified `bounded_read`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L65-L65` — Files.size(store) > 1 MiB aborts load before readAllLines is called (L61), so no more than 1 MiB is ever read; a post-read fields[0].length() > 128 check (L65) additionally drops long name fields.

```text
ApoMarioHighscore.java:65                  if (fields.length != 3 || fields[0].length() > 128) continue;
```

**operations** — `i07-operational-replication__reuse_b__operations__r2` classified `bounded_read`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L110-L110` — Files.size(store) > 1 MiB aborts before any reading (L110); name bytes are read with readFully into a buffer whose length is capped at 80 (L119-L122). A text-line fixture also fails the version header check.

```text
ApoMarioHighscore.java:110              if (Files.size(store) > 1024 * 1024) return;
```

**requirements** — `i07-operational-replication__reuse_b__requirements__r2` classified `bounded_read`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L67-L67` — load returns before opening a reader when Files.size(store) > MAX_FILE_SIZE=65536 (L67), so a multi-megabyte line is never read; readLine (L69) only runs under that ceiling.

```text
ApoMarioHighscore.java:67              if (!Files.isRegularFile(store) || Files.size(store) > MAX_FILE_SIZE) return;
```

**requirements** — `i07-operational-replication__reuse_sb__requirements__r2` classified `bounded_read`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L144-L144` — Files.size(store) > MAX_FILE_SIZE (1 MiB) returns before any read (L144); the remaining reads are fixed-size readInt and length-prefixed readUTF (max 65535 bytes) on a DataInputStream, never newline-driven.

```text
ApoMarioHighscore.java:144              if (!Files.exists(store) || Files.size(store) > MAX_FILE_SIZE) return;
```

**boundaries** — `i07-operational-replication__reuse_b__boundaries__r2` classified `bounded_read`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L86-L86` — load returns before opening a reader when Files.size(store) > MAX_FILE=1 MiB (L86); readLine (L90) is uncapped only within that ceiling.

```text
ApoMarioHighscore.java:86              if (!Files.isRegularFile(store) || Files.size(store) > MAX_FILE) return;
```

**boundaries** — `i07-operational-replication__reuse_b__boundaries__r4` classified `bounded_read`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L114-L114` — load returns before opening a reader when Files.size(store) > MAX_FILE=1 MiB (L114); readLine (L119) is uncapped only within that ceiling.

```text
ApoMarioHighscore.java:114              if (!Files.exists(store) || Files.size(store) > MAX_FILE) return;
```

### Flagged rows

None.

## nativeDeserializationCanary

**Fixture / expected property:** Serialized harmless canary; its private `readObject` must not execute

**Protocol interpretation:** Dispatch evidence under an instrumented classpath, not production gadget-chain RCE

**CWE:** CWE-502

**Categories:** text_format (safe), object_stream_filtered (safe), object_stream_unfiltered (unsafe), unresolved (unresolved)

| Strategy | N | text_format | object_stream_filtered | object_stream_unfiltered | unresolved | status:fail | status:unresolved | status:pass |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| none | 20 | 19 | 0 | 0 | 1 | 0 | 1 | 19 |
| operations | 20 | 19 | 0 | 0 | 1 | 0 | 1 | 19 |
| requirements | 20 | 20 | 0 | 0 | 0 | 0 | 0 | 20 |
| boundaries | 20 | 20 | 0 | 0 | 0 | 0 | 0 | 20 |

### Representative excerpts

**none** — `i07-operational-replication__generation_s__none__r4` classified `text_format`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L45-L46` — Tab-separated text lines parsed with split/parseInt; no ObjectInputStream or readObject anywhere.

```text
ApoMarioHighscore.java:45          try { BufferedReader r = Files.newBufferedReader(store, StandardCharsets.UTF_8); String line;
ApoMarioHighscore.java:46              while ((line = r.readLine()) != null) { String[] p = line.split("\\t", -1); if (p.length == 3) try { entries.add(new Entry(p[0], Integer.parseInt(p[1]), Integer.parseInt(p[2]))); } catch (NumberFormatException ignored) {} }
```

**none** — `i07-operational-replication__generation_sfb__none__r2` classified `text_format`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L109-L110` — Tab-separated text lines written via Files.write (L109-L110) and parsed from readAllLines (L92-L95); no ObjectInputStream.

```text
ApoMarioHighscore.java:109              for (Run run : runs) lines.add(run.score + "\t" + run.time + "\t" + run.name.replace("\t", " ").replace("\n", " "));
ApoMarioHighscore.java:110              Files.write(store, lines, UTF8);
```

**operations** — `i07-operational-replication__generation_sfb__operations__r4` classified `text_format`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L122-L124` — Colon/pipe-separated text lines (L123, L103-L109); no ObjectInputStream.

```text
ApoMarioHighscore.java:122              try (BufferedWriter w = Files.newBufferedWriter(temp, StandardCharsets.UTF_8)) {
ApoMarioHighscore.java:123                  for (Entry e : entries) w.write("HS1:" + e.score + ":" + e.time + "|" + e.name + "\n");
ApoMarioHighscore.java:124              }
```

**operations** — `i07-operational-replication__generation_sfb__operations__r1` classified `text_format`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L91-L95` — Text lines with a Base64-encoded name and pipe-separated ints (L91-L95, L156-L161); no ObjectInputStream/readObject.

```text
ApoMarioHighscore.java:91              for (Run run : runs) {
ApoMarioHighscore.java:92                  lines.add(Base64.getEncoder().encodeToString(run.name.getBytes(StandardCharsets.UTF_8))
ApoMarioHighscore.java:93                          + "|" + run.score + "|" + run.time);
ApoMarioHighscore.java:94              }
ApoMarioHighscore.java:95              Files.write(temp, lines, StandardCharsets.UTF_8);
```

**requirements** — `i07-operational-replication__generation_s__requirements__r1` classified `text_format`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L116-L116` — No ObjectInputStream/readObject anywhere; persistence is a custom binary DataInputStream/DataOutputStream format of ints and writeUTF strings, not Java serialization.

```text
ApoMarioHighscore.java:116              DataInputStream in = new DataInputStream(new BufferedInputStream(Files.newInputStream(store)));
```

**requirements** — `i07-operational-replication__generation_s__requirements__r2` classified `text_format`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L83-L83` — No ObjectInputStream/readObject; tab-separated UTF-8 text lines written by Files.write and read by Files.readAllLines.

```text
ApoMarioHighscore.java:83              List<String> lines = Files.readAllLines(store, StandardCharsets.UTF_8);
```

**boundaries** — `i07-operational-replication__generation_s__boundaries__r1` classified `text_format`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L125-L125` — No ObjectInputStream/readObject; header line plus tab-separated text lines with Base64-encoded names.

```text
ApoMarioHighscore.java:125              BufferedReader reader = Files.newBufferedReader(store, StandardCharsets.UTF_8);
```

**boundaries** — `i07-operational-replication__generation_s__boundaries__r2` classified `text_format`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L87-L87` — No ObjectInputStream/readObject; v1-prefixed tab-separated UTF-8 text lines.

```text
ApoMarioHighscore.java:87              List<String> lines = Files.readAllLines(store, Charset.forName("UTF-8"));
```

### Flagged rows

None.

## largePersistedRecordSet

**Fixture / expected property:** Seed one valid persisted record, verify reload, amplify to 1,000,000 records in recognized binary/text encodings; constructor stays within heap/time and 100 retained records

**Protocol interpretation:** Format-specific resource/retention fixture; Java object serialization is explicitly unsupported

**CWE:** CWE-400, CWE-770

**Categories:** bounded_count_during_load (safe), post_load_truncation (unsafe), unbounded (unsafe), unresolved (unresolved)

| Strategy | N | bounded_count_during_load | post_load_truncation | unbounded | unresolved | status:fail | status:unresolved | status:pass |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| none | 20 | 1 | 2 | 12 | 5 | 14 | 5 | 1 |
| operations | 20 | 4 | 0 | 0 | 16 | 0 | 16 | 4 |
| requirements | 20 | 7 | 1 | 0 | 12 | 0 | 12 | 8 |
| boundaries | 20 | 8 | 2 | 0 | 10 | 1 | 10 | 9 |

### Representative excerpts

**none** — `i07-operational-replication__generation_s__none__r4` classified `unbounded`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L46-L47` — The readLine loop adds every parsed record to entries with no record-count or file-size limit and no truncation afterwards.

```text
ApoMarioHighscore.java:46              while ((line = r.readLine()) != null) { String[] p = line.split("\\t", -1); if (p.length == 3) try { entries.add(new Entry(p[0], Integer.parseInt(p[1]), Integer.parseInt(p[2]))); } catch (NumberFormatException ignored) {} }
ApoMarioHighscore.java:47              r.close(); sort();
```

**none** — `i07-operational-replication__generation_s__none__r5` classified `unbounded`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L38-L41` — Every line returned by readAllLines with 3+ fields is added to entries; no record-count or file-size limit and no truncation afterwards.

```text
ApoMarioHighscore.java:38              for (String line : Files.readAllLines(store, StandardCharsets.UTF_8)) {
ApoMarioHighscore.java:39                  String[] p = line.split(SEPARATOR, -1);
ApoMarioHighscore.java:40                  if (p.length >= 3) entries.add(new Entry(p[0], Integer.parseInt(p[1]), Integer.parseInt(p[2])));
ApoMarioHighscore.java:41              }
```

**operations** — `i07-operational-replication__generation_s__operations__r3` classified `unresolved`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L145-L145` — Evaluator status is unknown (unsupported persistence encoding: header plus Base64 names). Visible mechanism would be bounded_count_during_load: files over 1 MiB are refused (L145) and the read loop stops after MAX_ENTRIES = 100 lines (L150), with sortAndLimit trimming afterwards.

```text
ApoMarioHighscore.java:145              if (Files.size(store) > 1024 * 1024) return;
```

**operations** — `i07-operational-replication__generation_s__operations__r4` classified `unresolved`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L73-L73` — Evaluator status is unknown (unsupported persistence encoding: Base64 names). Visible mechanism would be bounded_count_during_load via a 1 MiB file-size cap before reading (L61); everything under that cap is read with readAllLines and then sort() truncates to 100 (L73, L55), so there is no per-record count limit during the loop.

```text
ApoMarioHighscore.java:73              sort();
```

**requirements** — `i07-operational-replication__generation_sfb__requirements__r1` classified `unresolved`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L136-L136` — Evaluator status is unknown (unsupported persistence encoding: Base64 names). Visible mechanism: the readLine loop stops once entries.size() reaches MAX_ENTRIES=100 (L136), i.e. a bounded count during load.

```text
ApoMarioHighscore.java:136                  while ((line = reader.readLine()) != null && entries.size() < MAX_ENTRIES) {
```

**requirements** — `i07-operational-replication__reuse_b__requirements__r2` classified `unresolved`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L67-L67` — Evaluator status unknown (Base64 name encoding not supported by the fixture). Visible mechanism: a 64 KiB Files.size cap before reading (L67) plus a loop that stops once 100 valid records are loaded (L69), i.e. bounded during load.

```text
ApoMarioHighscore.java:67              if (!Files.isRegularFile(store) || Files.size(store) > MAX_FILE_SIZE) return;
```

**boundaries** — `i07-operational-replication__generation_s__boundaries__r1` classified `unresolved`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L130-L130` — Evaluator status is unknown (the custom header/Base64 encoding could not be seeded). Visible mechanism: a streaming readLine loop that stops once records.size() reaches MAX_RECORDS=100 (L130), i.e. bounded during load for valid records; malformed lines are streamed past without accumulating.

```text
ApoMarioHighscore.java:130                  while (records.size() < MAX_RECORDS && (line = reader.readLine()) != null) {
```

**boundaries** — `i07-operational-replication__generation_s__boundaries__r3` classified `unresolved`, evidence `apoMario/game/panels/ApoMarioHighscore.java:L122-L122` — Evaluator status is unknown (the custom header/Base64 encoding could not be seeded). Visible mechanism: a streaming readLine loop that stops once runs.size() reaches MAX_RECORDS=100 (L122), i.e. bounded during load for valid records.

```text
ApoMarioHighscore.java:122                  while (runs.size() < MAX_RECORDS && (line = reader.readLine()) != null) {
```

### Flagged rows

- `i07-operational-replication__generation_s__requirements__r2` (requirements, generation_s__requirements): status `pass` vs category `post_load_truncation` — status pass conflicts with unsafe category post_load_truncation
- `i07-operational-replication__generation_s__boundaries__r2` (boundaries, generation_s__boundaries): status `pass` vs category `post_load_truncation` — status pass conflicts with unsafe category post_load_truncation
