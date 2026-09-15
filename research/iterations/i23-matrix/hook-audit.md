# Run-end hook audit: i23-matrix

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_b__single_shot__none | 5 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| generation_f__single_shot__none | 5 | 2 | 1 | 0 | 1 | 3 | 0 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| generation_fb__single_shot__none | 5 | 2 | 0 | 0 | 2 | 3 | 0 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| generation_none__single_shot__none | 5 | 3 | 2 | 0 | 1 | 2 | 0 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| generation_s__single_shot__none | 5 | 3 | 1 | 0 | 2 | 2 | 0 | 2 | 1 | 2 | 0 | 0 | 0 | 2 |
| generation_sb__single_shot__none | 5 | 2 | 2 | 0 | 0 | 3 | 0 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| generation_sf__single_shot__none | 5 | 2 | 1 | 0 | 2 | 2 | 0 | 0 | 3 | 0 | 0 | 1 | 0 | 0 |
| generation_sfb__single_shot__none | 5 | 3 | 0 | 0 | 3 | 2 | 0 | 2 | 1 | 2 | 0 | 0 | 0 | 2 |
| reuse_b__single_shot__none | 5 | 2 | 2 | 0 | 0 | 3 | 0 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| reuse_f__single_shot__none | 5 | 4 | 2 | 0 | 2 | 1 | 0 | 1 | 3 | 1 | 0 | 0 | 0 | 1 |
| reuse_fb__single_shot__none | 5 | 1 | 2 | 0 | 2 | 1 | 0 | 1 | 3 | 1 | 1 | 3 | 0 | 2 |
| reuse_none__single_shot__none | 5 | 0 | 2 | 1 | 0 | 2 | 1 | 0 | 2 | 1 | 1 | 3 | 0 | 2 |
| reuse_s__single_shot__none | 5 | 3 | 1 | 0 | 2 | 2 | 0 | 1 | 2 | 1 | 0 | 0 | 0 | 1 |
| reuse_sb__single_shot__none | 5 | 3 | 1 | 0 | 2 | 2 | 0 | 1 | 2 | 1 | 0 | 0 | 0 | 1 |
| reuse_sf__single_shot__none | 5 | 3 | 1 | 0 | 2 | 2 | 0 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| reuse_sfb__single_shot__none | 5 | 3 | 3 | 0 | 1 | 1 | 0 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |

## Rows for hand review

- generation_s__single_shot__none r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__single_shot__none r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_sfb__single_shot__none r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_sfb__single_shot__none r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_f__single_shot__none r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_fb__single_shot__none r1: fallback, recorded, rejectsNullName compile_error, null-name tests failed 2, not functional
- reuse_fb__single_shot__none r4: passthrough, recorded, rejectsNullName compile_error, null-name tests failed 2, not functional
- reuse_none__single_shot__none r4: fallback, recorded, rejectsNullName compile_error, null-name tests failed 2, not functional
- reuse_none__single_shot__none r5: fallback, recorded, rejectsNullName compile_error, null-name tests failed 2, not functional
- reuse_s__single_shot__none r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__single_shot__none r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional

## Trajectories

### generation_b__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_b__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_b__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_b__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_b__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_f__single_shot__none r1: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:140` `String name = player.getTeamName();`

### generation_f__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:104` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:187` `String name = playerName == null ? "Unknown" : playerName.trim();`

### generation_f__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_f__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_f__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_fb__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_fb__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_fb__single_shot__none r3: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:85` `String name = selected.getTeamName();`

### generation_fb__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_fb__single_shot__none r5: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:74` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:76` `name = player.getAi().getTeamName();`

### generation_none__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:58` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:127` `String name = result.getTeamName();`
- `ApoMarioHighscore.java:129` `name = result.getAi().getTeamName();`

### generation_none__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_none__single_shot__none r3: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:194` `name = player.getTeamName();`

### generation_none__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_none__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:56` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:140` `String name = player.getTeamName();`

### generation_s__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:50` `String name = playerName == null ? "Anonymous" : playerName.trim();`
- `ApoMarioHighscore.java:126` `String name = selected.getTeamName();`

### generation_s__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__none r4: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:114` `String name = selected.getTeamName();`

### generation_s__single_shot__none r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:125` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:127` `name = best.getAi().getTeamName();`

### generation_sb__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:53` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:138` `String name = player.getTeamName();`

### generation_sb__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sb__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sb__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sb__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:45` `String name = playerName == null ? "Unknown" : playerName.trim();`
- `ApoMarioHighscore.java:126` `String name = selected.getTeamName();`

### generation_sf__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sf__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:55` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:137` `String name = selected.getTeamName();`

### generation_sf__single_shot__none r3: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:178` `String name = player.getTeamName();`

### generation_sf__single_shot__none r4: passthrough, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: recordRunEndAddsExactlyOneEntry.

- `ApoMarioHighscore.java:145` `String name = player.getTeamName();`

### generation_sf__single_shot__none r5: none, none, not functional

Final evaluated submission: 1; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- no new line reads the live player name

### generation_sfb__single_shot__none r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:130` `String name = selected.getTeamName();`

### generation_sfb__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__none r3: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:202` `String name = result.getTeamName();`

### generation_sfb__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__none r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:103` `String name = best.getTeamName();`

### reuse_b__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_b__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:49` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:94` `this.storeRun(player.getPoints(), level.getPassedTime(), player.getTeamName());`

### reuse_b__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:50` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:130` `this.playersNames.add(name == null || name.length() == 0 ? "Player" : name);`
- `ApoMarioHighscore.java:193` `String name = best.getTeamName();`

### reuse_b__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_b__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:62` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:128` `String name = selected.getTeamName();`

### reuse_f__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:45` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:110` `String name = best.getTeamName();`

### reuse_f__single_shot__none r4: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:179` `String name = player.getTeamName();`

### reuse_f__single_shot__none r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:124` `String name = player.getTeamName();`

### reuse_fb__single_shot__none r1: fallback, recorded, not functional

Final evaluated submission: 1; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:160` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:161` `if (name == null || name.trim().length() == 0 || "Human".equalsIgnoreCase(name)) {`

### reuse_fb__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:53` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:226` `String name = best.getTeamName();`

### reuse_fb__single_shot__none r3: passthrough, rejected at store, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:105` `String name = best.getTeamName();`

### reuse_fb__single_shot__none r4: passthrough, recorded, not functional

Final evaluated submission: 1; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:107` `String name = best.getTeamName();`

### reuse_fb__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_none__single_shot__none r1: skip_on_null, skipped at hook, not functional

Final evaluated submission: 1; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioLevel.java:1203` `if (best != null && best.getTeamName() != null && best.getTeamName().trim().length() > 0) {`
- `ApoMarioLevel.java:1204` `return best.getTeamName().trim();`

### reuse_none__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_none__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_none__single_shot__none r4: fallback, recorded, not functional

Final evaluated submission: 1; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:71` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:108` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:110` `name = player.getAi().getTeamName();`

### reuse_none__single_shot__none r5: fallback, recorded, not functional

Final evaluated submission: 1; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:58` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:79` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:81` `if (player.getAi() != null && player.getAi().getTeamName() != null) {`
- `ApoMarioHighscore.java:82` `name = player.getAi().getTeamName();`

### reuse_s__single_shot__none r1: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:129` `String name = selected.getTeamName();`

### reuse_s__single_shot__none r2: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:115` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:117` `name = selected.getAi().getTeamName();`

### reuse_s__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:56` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:116` `String name = selected.getTeamName();`

### reuse_s__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_s__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__none r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:120` `String name = best.getTeamName();`

### reuse_sb__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__none r4: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:114` `String name = best.getTeamName();`

### reuse_sb__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:37` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:107` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:182` `this.name = name == null ? "Player" : name;`

### reuse_sf__single_shot__none r1: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:78` `String name = selected.getTeamName();`

### reuse_sf__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sf__single_shot__none r3: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:113` `String name = best.getTeamName();`

### reuse_sf__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:57` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:141` `String name = best.getTeamName();`

### reuse_sf__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:62` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:95` `String name = best.getTeamName();`

### reuse_sfb__single_shot__none r2: passthrough, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioPanel.java:258` `String name = selected.getTeamName();`

### reuse_sfb__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:104` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:183` `this.playersNames.add(name.length() == 0 ? "Player" : name);`

### reuse_sfb__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:56` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:122` `String name = selected.getTeamName();`
