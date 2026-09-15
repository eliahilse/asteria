# Run-end hook audit: i24a-high

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_none__single_shot__none | 5 | 1 | 3 | 0 | 1 | 1 | 0 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| generation_none__single_shot__static | 5 | 4 | 1 | 2 | 1 | 1 | 2 | 1 | 1 | 3 | 0 | 0 | 0 | 3 |
| generation_s__single_shot__none | 5 | 4 | 1 | 0 | 3 | 1 | 0 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| generation_s__single_shot__static | 5 | 1 | 0 | 0 | 1 | 4 | 0 | 1 | 0 | 1 | 0 | 0 | 0 | 1 |
| generation_sfb__single_shot__none | 5 | 3 | 1 | 0 | 2 | 2 | 0 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| generation_sfb__single_shot__static | 5 | 2 | 0 | 0 | 2 | 3 | 0 | 2 | 0 | 2 | 0 | 0 | 0 | 2 |
| reuse_f__single_shot__none | 5 | 0 | 1 | 0 | 0 | 4 | 0 | 0 | 1 | 0 | 0 | 1 | 0 | 1 |
| reuse_f__single_shot__static | 5 | 2 | 0 | 0 | 3 | 2 | 0 | 3 | 0 | 3 | 1 | 1 | 0 | 2 |
| reuse_sfb__single_shot__none | 5 | 2 | 2 | 0 | 1 | 2 | 0 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| reuse_sfb__single_shot__static | 5 | 1 | 1 | 0 | 0 | 4 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |

## Rows for hand review

- generation_none__single_shot__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_none__single_shot__static r4: skip_on_null, skipped at hook, rejectsNullName pass, null-name tests failed 0, functional
- generation_none__single_shot__static r5: skip_on_null, skipped at hook, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__single_shot__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_sfb__single_shot__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_sfb__single_shot__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_f__single_shot__none r5: fallback, recorded, rejectsNullName fail, null-name tests failed 2, not functional
- reuse_f__single_shot__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_f__single_shot__static r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional

## Trajectories

### generation_none__single_shot__none r1: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioHighscore.java:60` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:123` `String name = player.getTeamName();`

### generation_none__single_shot__none r2: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioLevel.java:1192` `String[] methods = new String[] { "getPlayerName", "getName", "getTeamName" };`
- `ApoMarioHighscore.java:51` `String name = playerName == null ? "Player" : playerName.trim();`

### generation_none__single_shot__none r3: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioPanel.java:568` `return this.getLevel() == null ? "Player" : this.getLevel().getPlayerName();`
- `ApoMarioHighscore.java:131` `if ("Player".equals(name) && best.getTeamName() != null`
- `ApoMarioHighscore.java:132` `&& best.getTeamName().trim().length() > 0) {`
- `ApoMarioHighscore.java:133` `name = best.getTeamName();`

### generation_none__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_none__single_shot__none r5: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:129` `String name = player.getTeamName();`

### generation_none__single_shot__static r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:179` `String name = player == null ? null : player.getTeamName();`

### generation_none__single_shot__static r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:186` `name = player.getTeamName();`
- `ApoMarioHighscore.java:193` `return isValidName(name) ? name : "Player";`

### generation_none__single_shot__static r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_none__single_shot__static r4: skip_on_null, skipped at hook, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:85` `if (player.getTeamName() != null && player.getTeamName().trim().length() > 0) {`
- `ApoMarioHighscore.java:86` `playerName = player.getTeamName();`

### generation_none__single_shot__static r5: skip_on_null, skipped at hook, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:170` `if (player.getTeamName() != null && player.getTeamName().length() > 0) {`
- `ApoMarioHighscore.java:171` `playerName = player.getTeamName();`

### generation_s__single_shot__none r1: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:164` `String name = selected.getTeamName();`

### generation_s__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:50` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:131` `String name = selected.getTeamName();`

### generation_s__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__none r4: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:116` `String name = best.getTeamName();`

### generation_s__single_shot__none r5: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:152` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:155` `name = selected.getAi().getTeamName();`

### generation_s__single_shot__static r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:75` `String name = player.getTeamName();`

### generation_sfb__single_shot__none r1: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:108` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:111` `name = best.getAi().getTeamName();`

### generation_sfb__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__none r3: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:151` `String name = best.getTeamName();`

### generation_sfb__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:54` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:143` `String name = selected.getTeamName();`

### generation_sfb__single_shot__static r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:93` `String name = player.getTeamName();`

### generation_sfb__single_shot__static r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__static r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__static r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__static r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:143` `String name = selected.getTeamName();`

### reuse_f__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__none r5: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:46` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:110` `String name = best.getTeamName();`

### reuse_f__single_shot__static r1: passthrough, rejected at store, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: recordsRealPlayerName, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:265` `String team = validateName(player.getAi().getTeamName());`

### reuse_f__single_shot__static r2: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:118` `String team = selected.getTeamName();`

### reuse_f__single_shot__static r3: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:119` `String name = player.getTeamName();`

### reuse_f__single_shot__static r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__static r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__none r1: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:115` `String name = best.getTeamName();`

### reuse_sfb__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__none r3: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: entriesPersistAcrossSessions, rankingPreservedAfterReload.

- `ApoMarioHighscore.java:75` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:162` `String name = selected.getTeamName();`

### reuse_sfb__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:42` `String name = playerName == null ? "Anonymous" : playerName.trim();`
- `ApoMarioHighscore.java:110` `String name = selected.getTeamName();`

### reuse_sfb__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__static r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__static r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__static r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__static r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:164` `name = player.getTeamName();`
- `ApoMarioHighscore.java:192` `return isValidName(name) ? name : "Player";`

### reuse_sfb__single_shot__static r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name
