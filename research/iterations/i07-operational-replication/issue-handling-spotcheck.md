# Mechanism review spot-check: i07-operational-replication

20 classified rows, two per issue check, drawn with seed 7 from `issue-handling.csv`. For each row, read the cited lines and fill the verdict: **agree** (the category describes the mechanism in the cited code), **disagree** (state the category you would assign), or **unsure**. The evaluator outcome is given for orientation only; the question is whether the mechanism label is right.

| # | Trajectory | Check | Evaluator | Category | Human verdict |
| ---: | --- | --- | --- | --- | --- |
| 1 | reuse_b__boundaries r2 | rejectsNegativeScore | fail | accepted |  |
| 2 | generation_s__requirements r5 | rejectsNegativeScore | fail | accepted |  |
| 3 | reuse_b__operations r3 | rejectsNegativeTime | pass | rejected |  |
| 4 | generation_s__none r2 | rejectsNegativeTime | fail | sanitized |  |
| 5 | generation_s__none r5 | rejectsNullName | fail | sanitized |  |
| 6 | reuse_sb__operations r1 | rejectsNullName | pass | rejected |  |
| 7 | generation_s__operations r3 | rejectsBlankName | fail | sanitized |  |
| 8 | reuse_b__none r2 | rejectsBlankName | fail | sanitized |  |
| 9 | reuse_sb__requirements r2 | rejectsExcessiveName | pass | rejected |  |
| 10 | generation_s__none r3 | rejectsExcessiveName | fail | accepted |  |
| 11 | reuse_sb__none r2 | boundsRetainedEntries | pass | bounded_on_store |  |
| 12 | generation_sfb__none r3 | boundsRetainedEntries | fail | unbounded |  |
| 13 | generation_s__boundaries r5 | malformedStoreDoesNotCrash | pass | guarded_fallback |  |
| 14 | generation_s__operations r2 | malformedStoreDoesNotCrash | pass | guarded_fallback |  |
| 15 | reuse_b__requirements r3 | oversizedPhysicalLine | fail | unbounded_read |  |
| 16 | reuse_b__requirements r1 | oversizedPhysicalLine | fail | unbounded_read |  |
| 17 | generation_s__none r4 | nativeDeserializationCanary | pass | text_format |  |
| 18 | generation_sfb__operations r1 | nativeDeserializationCanary | pass | text_format |  |
| 19 | generation_s__operations r1 | largePersistedRecordSet | pass | bounded_count_during_load |  |
| 20 | reuse_sb__none r3 | largePersistedRecordSet | fail | unbounded |  |

## Rows

### 1. reuse_b__boundaries r2 · rejectsNegativeScore

Evaluator: **fail** · Reviewer category: **accepted** · Reviewer: claude-fable-5.1/batch-6

Reviewer note: storeRun never examines score; a negative score is stored unchanged at L45 and true is returned (evaluator: fail).

Cited `apoMario/game/panels/ApoMarioHighscore.java:L43-L45`:

```java
43:         String name = cleanName(playerName);
44:         if (name == null || survivalTime < 0) return false;
45:         runs.add(new Run(score, survivalTime, name));
```

Human verdict: 

### 2. generation_s__requirements r5 · rejectsNegativeScore

Evaluator: **fail** · Reviewer category: **accepted** · Reviewer: claude-fable-5.1/batch-2

Reviewer note: storeRun validates only the cleaned name and survivalTime (L48); score is stored unchanged (L49).

Cited `apoMario/game/panels/ApoMarioHighscore.java:L46-L49`:

```java
46:     public synchronized boolean storeRun(int score, int survivalTime, String playerName) {
47:         String name = cleanName(playerName);
48:         if (name == null || survivalTime < 0) return false;
49:         runs.add(new Run(name, score, survivalTime));
```

Human verdict: 

### 3. reuse_b__operations r3 · rejectsNegativeTime

Evaluator: **pass** · Reviewer category: **rejected** · Reviewer: claude-fable-5.1/batch-5

Reviewer note: storeRun returns false when survivalTime < 0 (L57) before storing.

Cited `apoMario/game/panels/ApoMarioHighscore.java:L57`:

```java
57:         if (score < 0 || survivalTime < 0 || name == null) return false;
```

Human verdict: 

### 4. generation_s__none r2 · rejectsNegativeTime

Evaluator: **fail** · Reviewer category: **sanitized** · Reviewer: claude-fable-5.1/batch-1

Reviewer note: Negative survivalTime is clamped to 0 via Math.max and the record is still stored.

Cited `apoMario/game/panels/ApoMarioHighscore.java:L52`:

```java
52:         entries.add(new Entry(score, Math.max(0, survivalTime), name));
```

Human verdict: 

### 5. generation_s__none r5 · rejectsNullName

Evaluator: **fail** · Reviewer category: **sanitized** · Reviewer: claude-fable-5.1/batch-1

Reviewer note: A null playerName is replaced with "Unknown" (L53) and the record is stored.

Cited `apoMario/game/panels/ApoMarioHighscore.java:L53-L56`:

```java
53:         String name = playerName == null ? "Unknown" : playerName.trim();
54:         if (name.length() == 0) name = "Unknown";
55:         name = name.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ');
56:         entries.add(new Entry(name, score, Math.max(0, survivalTime)));
```

Human verdict: 

### 6. reuse_sb__operations r1 · rejectsNullName

Evaluator: **pass** · Reviewer category: **rejected** · Reviewer: claude-fable-5.1/batch-7

Reviewer note: cleanName(null) returns null (L64-L65) and storeRun returns false (L77).

Cited `apoMario/game/panels/ApoMarioHighscore.java:L64-L65`:

```java
64:     private String cleanName(String value) {
65:         if (value == null) return null;
```

Cited `apoMario/game/panels/ApoMarioHighscore.java:L77`:

```java
77:         if (name == null || survivalTime < 0 || entries.size() >= MAX_RECORDS) return false;
```

Human verdict: 

### 7. generation_s__operations r3 · rejectsBlankName

Evaluator: **fail** · Reviewer category: **sanitized** · Reviewer: claude-fable-5.1/batch-1

Reviewer note: normalize strips control characters and trims to "", and storeRun substitutes "Player" (L45) and stores the record.

Cited `apoMario/game/panels/ApoMarioHighscore.java:L44-L46`:

```java
44:         String name = normalize(playerName);
45:         if (name.length() == 0) name = "Player";
46:         runs.add(new Run(name, score, survivalTime));
```

Cited `apoMario/game/panels/ApoMarioHighscore.java:L134`:

```java
134:         return b.toString().trim();
```

Human verdict: 

### 8. reuse_b__none r2 · rejectsBlankName

Evaluator: **fail** · Reviewer category: **sanitized** · Reviewer: claude-fable-5.1/batch-5

Reviewer note: The name is trimmed and an empty result becomes "Player" (L33-L34); the record is stored.

Cited `apoMario/game/panels/ApoMarioHighscore.java:L33-L34`:

```java
33:         String name = playerName == null ? "Player" : playerName.trim();
34:         if (name.length() == 0) name = "Player";
```

Human verdict: 

### 9. reuse_sb__requirements r2 · rejectsExcessiveName

Evaluator: **pass** · Reviewer category: **rejected** · Reviewer: claude-fable-5.1/batch-8

Reviewer note: validName returns false when trimmed length > MAX_NAME=40 (L165); no truncation.

Cited `apoMario/game/panels/ApoMarioHighscore.java:L30`:

```java
30:     private static final int MAX_NAME = 40;
```

Cited `apoMario/game/panels/ApoMarioHighscore.java:L165`:

```java
165:         if (value.length() == 0 || value.length() > MAX_NAME) return false;
```

Human verdict: 

### 10. generation_s__none r3 · rejectsExcessiveName

Evaluator: **fail** · Reviewer category: **accepted** · Reviewer: claude-fable-5.1/batch-1

Reviewer note: No length limit or truncation on the name; an oversized name is stored as-is.

Cited `apoMario/game/panels/ApoMarioHighscore.java:L42-L49`:

```java
42:         String name = playerName == null ? "Unknown" : playerName.trim();
43:         if (name.length() == 0) {
44:             name = "Unknown";
45:         }
46:         if (survivalTime < 0) {
47:             survivalTime = 0;
48:         }
49:         entries.add(new Entry(score, survivalTime, name));
```

Human verdict: 

### 11. reuse_sb__none r2 · boundsRetainedEntries

Evaluator: **pass** · Reviewer category: **bounded_on_store** · Reviewer: claude-fable-5.1/batch-7

Reviewer note: After sorting, if size > MAX_ENTRIES=100 the last entry is removed (L40-L44). It is an if rather than a while, so it removes at most one entry per store, which holds the bound when starting from at most 100.

Cited `apoMario/game/panels/ApoMarioHighscore.java:L40-L44`:

```java
40:         if (scores.size() > MAX_ENTRIES) {
41:             scores.remove(scores.size() - 1);
42:             times.remove(times.size() - 1);
43:             names.remove(names.size() - 1);
44:         }
```

Human verdict: 

### 12. generation_sfb__none r3 · boundsRetainedEntries

Evaluator: **fail** · Reviewer category: **unbounded** · Reviewer: claude-fable-5.1/batch-3

Reviewer note: storeRun adds and sorts (L58-L59); sort() never trims and no maximum exists.

Cited `apoMario/game/panels/ApoMarioHighscore.java:L58-L59`:

```java
58:         entries.add(new Entry(name, score, Math.max(0, survivalTime)));
59:         sort();
```

Cited `apoMario/game/panels/ApoMarioHighscore.java:L48-L52`:

```java
48:     private void sort() {
49:         Collections.sort(entries, new Comparator<Entry>() {
50:             public int compare(Entry a, Entry b) { return b.score < a.score ? -1 : (b.score == a.score ? 0 : 1); }
51:         });
52:     }
```

Human verdict: 

### 13. generation_s__boundaries r5 · malformedStoreDoesNotCrash

Evaluator: **pass** · Reviewer category: **guarded_fallback** · Reviewer: claude-fable-5.1/batch-2

Reviewer note: load wraps everything in catch (Exception) (L73) and also catches Exception per record (L71); the header line is skipped by starting at index 1 (L70) without being validated. The store file is not rewritten.

Cited `apoMario/game/panels/ApoMarioHighscore.java:L67-L73`:

```java
67:         try {
68:             if (store == null || !Files.isRegularFile(store)) return;
69:             List<String> lines = Files.readAllLines(store, StandardCharsets.UTF_8);
70:             for (int i=1; i<lines.size() && entries.size()<MAX; i++) { String[] f=lines.get(i).split("\\t",-1); if(f.length!=3) continue;
71:                 try { int s=Integer.parseInt(f[0]), t=Integer.parseInt(f[1]); String n=clean(new String(java.util.Base64.getDecoder().decode(f[2]),StandardCharsets.UTF_8)); if(n.length()>0&&t>=0) entries.add(new Entry(s,t,n)); } catch(Exception ignored) { }
72:             } sort();
73:         } catch (Exception ignored) { }
```

Human verdict: 

### 14. generation_s__operations r2 · malformedStoreDoesNotCrash

Evaluator: **pass** · Reviewer category: **guarded_fallback** · Reviewer: claude-fable-5.1/batch-1

Reviewer note: A wrong header aborts load with an empty board (L93), lines without 3 fields are skipped (L96), per-record RuntimeException (Base64/parseInt) is swallowed (L97) and IOException clears the list (L101); the file is not rewritten on error.

Cited `apoMario/game/panels/ApoMarioHighscore.java:L90-L101`:

```java
90:         try {
91:             BufferedReader r = Files.newBufferedReader(store, StandardCharsets.UTF_8);
92:             try {
93:                 if (!"APO_MARIO_HIGHSCORE_1".equals(r.readLine())) return;
94:                 String line; int count = 0;
95:                 while (count++ < MAX_ENTRIES && (line = r.readLine()) != null) {
96:                     String[] p = line.split("\\t", -1); if (p.length != 3) continue;
97:                     try { String n = normalize(new String(Base64.getDecoder().decode(p[0]), StandardCharsets.UTF_8)); int s = Integer.parseInt(p[1]); int t = Integer.parseInt(p[2]); if (n.length() > 0 && t >= 0) runs.add(new Run(n, s, t)); } catch (RuntimeException e) { }
98:                 }
99:             } finally { r.close(); }
100:             sortAndTrim();
101:         } catch (IOException e) { runs.clear(); }
```

Human verdict: 

### 15. reuse_b__requirements r3 · oversizedPhysicalLine

Evaluator: **fail** · Reviewer category: **unbounded_read** · Reviewer: claude-fable-5.1/batch-6

Reviewer note: readLine is uncapped for both the header line (L95) and record lines (L97) with no file-size check, so an oversized line is fully buffered (evaluator: OOM). Only the parsed name field is length-checked afterwards (L79).

Cited `apoMario/game/panels/ApoMarioHighscore.java:L93-L97`:

```java
93:             BufferedReader in = Files.newBufferedReader(store, StandardCharsets.UTF_8);
94:             try {
95:                 if (!"APOMARIO-HIGHSCORE-1".equals(in.readLine())) return;
96:                 String line; int count = 0;
97:                 while (count++ < MAX_ENTRIES && (line = in.readLine()) != null) {
```

Human verdict: 

### 16. reuse_b__requirements r1 · oversizedPhysicalLine

Evaluator: **fail** · Reviewer category: **unbounded_read** · Reviewer: claude-fable-5.1/batch-6

Reviewer note: BufferedReader.readLine (L107) is called with no line-length or file-size cap, so an oversized line is fully buffered (evaluator: OOM). Only the parsed name field is length-checked afterwards (L93), which does not bound the read.

Cited `apoMario/game/panels/ApoMarioHighscore.java:L104-L107`:

```java
104:             BufferedReader in = Files.newBufferedReader(store.toAbsolutePath(), StandardCharsets.UTF_8);
105:             try {
106:                 String line; int count = 0;
107:                 while (count < MAX_ENTRIES && (line = in.readLine()) != null) {
```

Human verdict: 

### 17. generation_s__none r4 · nativeDeserializationCanary

Evaluator: **pass** · Reviewer category: **text_format** · Reviewer: claude-fable-5.1/batch-1

Reviewer note: Tab-separated text lines parsed with split/parseInt; no ObjectInputStream or readObject anywhere.

Cited `apoMario/game/panels/ApoMarioHighscore.java:L45-L46`:

```java
45:         try { BufferedReader r = Files.newBufferedReader(store, StandardCharsets.UTF_8); String line;
46:             while ((line = r.readLine()) != null) { String[] p = line.split("\\t", -1); if (p.length == 3) try { entries.add(new Entry(p[0], Integer.parseInt(p[1]), Integer.parseInt(p[2]))); } catch (NumberFormatException ignored) {} }
```

Human verdict: 

### 18. generation_sfb__operations r1 · nativeDeserializationCanary

Evaluator: **pass** · Reviewer category: **text_format** · Reviewer: claude-fable-5.1/batch-3

Reviewer note: Text lines with a Base64-encoded name and pipe-separated ints (L91-L95, L156-L161); no ObjectInputStream/readObject.

Cited `apoMario/game/panels/ApoMarioHighscore.java:L152-L161`:

```java
152:             List<String> lines = Files.readAllLines(store, StandardCharsets.UTF_8);
153:             if (lines.isEmpty() || !HEADER.equals(lines.get(0))) return;
154:             int limit = Math.min(lines.size(), MAX_ENTRIES + 1);
155:             for (int i = 1; i < limit; i++) {
156:                 String[] fields = lines.get(i).split("\\|", -1);
157:                 if (fields.length != 3) continue;
158:                 try {
159:                     String name = normalize(new String(Base64.getDecoder().decode(fields[0]), StandardCharsets.UTF_8));
160:                     int score = Integer.parseInt(fields[1]);
161:                     int time = Integer.parseInt(fields[2]);
```

Cited `apoMario/game/panels/ApoMarioHighscore.java:L91-L95`:

```java
91:             for (Run run : runs) {
92:                 lines.add(Base64.getEncoder().encodeToString(run.name.getBytes(StandardCharsets.UTF_8))
93:                         + "|" + run.score + "|" + run.time);
94:             }
95:             Files.write(temp, lines, StandardCharsets.UTF_8);
```

Human verdict: 

### 19. generation_s__operations r1 · largePersistedRecordSet

Evaluator: **pass** · Reviewer category: **bounded_count_during_load** · Reviewer: claude-fable-5.1/batch-1

Reviewer note: Load refuses files over 1 MiB (L125) and the read loop stops once entries.size() reaches MAX_ENTRIES = 1000 (L130); sortAndLimit afterwards keeps at most 1000.

Cited `apoMario/game/panels/ApoMarioHighscore.java:L125`:

```java
125:             if (Files.size(store) > 1024 * 1024) return;
```

Cited `apoMario/game/panels/ApoMarioHighscore.java:L130`:

```java
130:                 while (entries.size() < MAX_ENTRIES && (line = reader.readLine()) != null) {
```

Human verdict: 

### 20. reuse_sb__none r3 · largePersistedRecordSet

Evaluator: **fail** · Reviewer category: **unbounded** · Reviewer: claude-fable-5.1/batch-7

Reviewer note: load() appends every line with no count or size cap (L114-L121); trimming to 100 happens only in storeRun.

Cited `apoMario/game/panels/ApoMarioHighscore.java:L114-L121`:

```java
114:                 while ((line = reader.readLine()) != null) {
115:                     String[] p = line.split("\\t", 3);
116:                     if (p.length == 3) {
117:                         playersScores.add(Integer.valueOf(p[0]));
118:                         playersNames.add(p[2]);
119:                         survivalTimes.add(Integer.valueOf(p[1]));
120:                     }
121:                 }
```

Human verdict: 
