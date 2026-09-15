# Run-end hook audit: i25-lenient

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_b__single_shot__none | 5 | 2 | 1 | 0 | 1 | 3 | 0 | 1 | 1 | 1 | 0 | 0 | 0 | 1 |
| generation_f__single_shot__none | 5 | 3 | 1 | 0 | 3 | 1 | 0 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| generation_fb__single_shot__none | 5 | 4 | 3 | 0 | 1 | 1 | 0 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| generation_none__single_shot__none | 5 | 0 | 2 | 0 | 0 | 3 | 0 | 0 | 2 | 0 | 0 | 1 | 0 | 1 |
| generation_s__single_shot__none | 5 | 3 | 4 | 0 | 1 | 0 | 0 | 1 | 4 | 1 | 1 | 2 | 0 | 1 |
| generation_sb__single_shot__none | 5 | 3 | 3 | 0 | 1 | 1 | 0 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| generation_sf__single_shot__none | 5 | 3 | 2 | 0 | 1 | 2 | 0 | 1 | 2 | 1 | 0 | 0 | 0 | 1 |
| generation_sfb__single_shot__none | 5 | 2 | 1 | 0 | 2 | 2 | 0 | 2 | 1 | 2 | 0 | 1 | 0 | 3 |
| reuse_b__single_shot__none | 5 | 3 | 1 | 0 | 3 | 1 | 0 | 0 | 4 | 0 | 0 | 1 | 0 | 1 |
| reuse_f__single_shot__none | 5 | 1 | 1 | 0 | 0 | 4 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| reuse_fb__single_shot__none | 5 | 3 | 2 | 0 | 1 | 2 | 0 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| reuse_none__single_shot__none | 5 | 1 | 0 | 0 | 1 | 4 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| reuse_s__single_shot__none | 5 | 2 | 1 | 0 | 2 | 2 | 0 | 0 | 3 | 0 | 0 | 1 | 0 | 1 |
| reuse_sb__single_shot__none | 5 | 2 | 1 | 0 | 1 | 3 | 0 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| reuse_sf__single_shot__none | 5 | 3 | 2 | 0 | 2 | 1 | 0 | 0 | 4 | 0 | 0 | 1 | 0 | 1 |
| reuse_sfb__single_shot__none | 5 | 3 | 2 | 0 | 2 | 1 | 0 | 1 | 3 | 1 | 0 | 1 | 0 | 2 |

## Rows for hand review

- generation_b__single_shot__none r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_none__single_shot__none r2: fallback, recorded, rejectsNullName compile_error, null-name tests failed 2, not functional
- generation_s__single_shot__none r1: fallback, recorded, rejectsNullName fail, null-name tests failed 2, not functional
- generation_sf__single_shot__none r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_sfb__single_shot__none r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_sfb__single_shot__none r2: fallback, recorded, rejectsNullName compile_error, null-name tests failed 2, not functional
- generation_sfb__single_shot__none r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_b__single_shot__none r5: passthrough, recorded, rejectsNullName compile_error, null-name tests failed 2, not functional
- reuse_s__single_shot__none r4: passthrough, recorded, rejectsNullName compile_error, null-name tests failed 2, not functional
- reuse_sf__single_shot__none r1: passthrough, recorded, rejectsNullName compile_error, null-name tests failed 2, not functional
- reuse_sfb__single_shot__none r3: fallback, recorded, rejectsNullName fail, null-name tests failed 2, not functional
- reuse_sfb__single_shot__none r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional

## Trajectories

### generation_b__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_b__single_shot__none r2: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:104` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:107` `name = player.getAi().getTeamName();`

### generation_b__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_b__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:56` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:131` `String name = bestPlayer.getTeamName();`
- `ApoMarioHighscore.java:133` `name = bestPlayer.getAi().getTeamName();`

### generation_b__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_f__single_shot__none r1: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:121` `String name = selected.getTeamName();`

### generation_f__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:59` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:121` `String name = player.getTeamName();`

### generation_f__single_shot__none r3: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:110` `String name = selected.getTeamName();`

### generation_f__single_shot__none r4: passthrough, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: recordsRealPlayerName, recordedNameIsTheRealPlayersName.

- `ApoMarioHighscore.java:137` `name = player.getAi().getTeamName();`

### generation_f__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_fb__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_fb__single_shot__none r2: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:129` `String name = player.getTeamName();`

### generation_fb__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:57` `String name = playerName == null ? "Unknown" : playerName.trim();`
- `ApoMarioHighscore.java:122` `String name = selected.getTeamName();`

### generation_fb__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:55` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:120` `String name = selected.getTeamName();`

### generation_fb__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:62` `String name = playerName == null ? "Unknown" : playerName.trim();`
- `ApoMarioHighscore.java:136` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:138` `name = player.getAi().getTeamName();`

### generation_none__single_shot__none r1: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioLevel.java:1191` `String name = this.players.get(0).getTeamName();`
- `ApoMarioHighscore.java:57` `String name = playerName == null ? "Player" : playerName.trim();`

### generation_none__single_shot__none r2: fallback, recorded, not functional

Final evaluated submission: 1; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:61` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:95` `String name = player.getTeamName();`

### generation_none__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_none__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_none__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__none r1: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:55` `String name = playerName == null ? "Unknown" : playerName.trim();`
- `ApoMarioHighscore.java:83` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:85` `name = best.getAi().getTeamName();`

### generation_s__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:55` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:118` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:120` `name = best.getAi().getTeamName();`

### generation_s__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:55` `String name = playerName == null ? "Unknown" : playerName.trim();`
- `ApoMarioHighscore.java:117` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:119` `name = selected.getAi().getTeamName();`

### generation_s__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:62` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:147` `String name = best.getTeamName();`

### generation_s__single_shot__none r5: passthrough, rejected at store, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:113` `String name = player.getTeamName();`

### generation_sb__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sb__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:53` `String name = playerName == null ? "Unknown" : playerName.trim();`
- `ApoMarioHighscore.java:136` `String name = player.getTeamName();`

### generation_sb__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:57` `String name = playerName == null ? "Unknown" : playerName.trim();`
- `ApoMarioHighscore.java:122` `String name = selected.getTeamName();`

### generation_sb__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:55` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:112` `String name = best.getTeamName();`

### generation_sb__single_shot__none r5: passthrough, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: recordRunEndAddsExactlyOneEntry.

- `ApoMarioHighscore.java:110` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:112` `name = player.getAi().getTeamName();`

### generation_sf__single_shot__none r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:120` `String name = normalizeName(best.getTeamName());`
- `ApoMarioHighscore.java:125` `name = normalizeName(best.getAi().getTeamName());`

### generation_sf__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:119` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:180` `return name.length() == 0 ? "Player" : name;`

### generation_sf__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:46` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:118` `String name = result.getTeamName();`
- `ApoMarioHighscore.java:120` `name = result.getAi().getTeamName();`

### generation_sf__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sf__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__none r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:127` `String name = bestPlayer.getTeamName();`

### generation_sfb__single_shot__none r2: fallback, recorded, not functional

Final evaluated submission: 1; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:40` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:94` `String name = result.getTeamName();`

### generation_sfb__single_shot__none r3: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:81` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:83` `name = best.getAi().getTeamName();`

### generation_sfb__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_b__single_shot__none r1: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:75` `this.storeRun(player.getPoints(), level.getPassedTime(), player.getTeamName());`

### reuse_b__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_b__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:48` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:108` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:110` `name = selected.getAi().getTeamName();`

### reuse_b__single_shot__none r4: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:92` `this.storeRun(best.getPoints(), level.getPassedTime(), best.getTeamName());`

### reuse_b__single_shot__none r5: passthrough, recorded, not functional

Final evaluated submission: 1; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:111` `String name = selected.getTeamName();`

### reuse_f__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:63` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:116` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:121` `name = player.getAi().getTeamName();`

### reuse_f__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_fb__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:56` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:118` `String name = selected.getTeamName();`

### reuse_fb__single_shot__none r2: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:100` `String name = selected.getTeamName();`

### reuse_fb__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_fb__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:43` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:106` `String name = player.getTeamName();`

### reuse_fb__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_none__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_none__single_shot__none r2: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:171` `name = player.getTeamName();`

### reuse_none__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_none__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_none__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_s__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:51` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:139` `String name = best.getTeamName();`

### reuse_s__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_s__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_s__single_shot__none r4: passthrough, recorded, not functional

Final evaluated submission: 1; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:261` `String name = best.getTeamName();`

### reuse_s__single_shot__none r5: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:149` `String name = selected.getTeamName();`

### reuse_sb__single_shot__none r1: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:92` `if (best != null) this.storeRun(best.getPoints(), level.getPassedTime(), best.getTeamName());`

### reuse_sb__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:49` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:96` `String name = selected.getTeamName();`

### reuse_sb__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sf__single_shot__none r1: passthrough, recorded, not functional

Final evaluated submission: 1; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioPanel.java:247` `String name = selected.getTeamName();`

### reuse_sf__single_shot__none r2: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:98` `String name = selected.getTeamName();`

### reuse_sf__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:58` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:139` `String name = best.getTeamName();`

### reuse_sf__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sf__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:122` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:151` `this.playersNames.add(name.length() == 0 ? "Player" : name);`

### reuse_sfb__single_shot__none r1: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:103` `String name = best.getTeamName();`

### reuse_sfb__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:50` `String name = playerName == null ? "Anonymous" : playerName.trim();`
- `ApoMarioHighscore.java:127` `String name = best.getTeamName();`

### reuse_sfb__single_shot__none r3: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:49` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:207` `String name = best.getTeamName();`

### reuse_sfb__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__none r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:135` `String name = selected.getTeamName();`
