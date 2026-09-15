# Run-end hook audit: i24c-generic

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_none__single_shot__none | 5 | 3 | 1 | 0 | 3 | 1 | 0 | 1 | 3 | 1 | 0 | 0 | 0 | 1 |
| generation_none__single_shot__static | 5 | 2 | 0 | 0 | 3 | 2 | 0 | 3 | 0 | 3 | 0 | 0 | 0 | 3 |
| generation_s__single_shot__none | 5 | 3 | 0 | 0 | 3 | 2 | 0 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| generation_s__single_shot__static | 5 | 4 | 0 | 1 | 4 | 0 | 1 | 3 | 1 | 4 | 0 | 1 | 0 | 5 |
| generation_sfb__single_shot__none | 5 | 3 | 1 | 0 | 2 | 2 | 0 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| generation_sfb__single_shot__static | 5 | 2 | 0 | 0 | 3 | 2 | 0 | 3 | 0 | 3 | 0 | 0 | 0 | 3 |
| reuse_f__single_shot__none | 5 | 2 | 2 | 0 | 0 | 3 | 0 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| reuse_f__single_shot__static | 5 | 1 | 0 | 0 | 3 | 2 | 0 | 2 | 1 | 2 | 1 | 2 | 0 | 2 |
| reuse_sfb__single_shot__none | 5 | 2 | 0 | 0 | 2 | 3 | 0 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| reuse_sfb__single_shot__static | 5 | 2 | 0 | 0 | 2 | 3 | 0 | 2 | 0 | 2 | 0 | 0 | 0 | 2 |

## Rows for hand review

- generation_none__single_shot__none r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_none__single_shot__static r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_none__single_shot__static r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_none__single_shot__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, not functional
- generation_s__single_shot__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__single_shot__static r2: skip_on_null, skipped at hook, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__single_shot__static r3: passthrough, recorded, rejectsNullName compile_error, null-name tests failed 2, not functional
- generation_s__single_shot__static r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__single_shot__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_sfb__single_shot__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_sfb__single_shot__static r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, not functional
- generation_sfb__single_shot__static r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_f__single_shot__static r2: passthrough, recorded, rejectsNullName compile_error, null-name tests failed 2, not functional
- reuse_f__single_shot__static r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sfb__single_shot__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sfb__single_shot__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional

## Trajectories

### generation_none__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_none__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:66` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:88` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:90` `name = player.getAi().getTeamName();`

### generation_none__single_shot__none r3: passthrough, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioHighscore.java:105` `name = best.getTeamName();`

### generation_none__single_shot__none r4: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:199` `// getTeamName implementation.`
- `ApoMarioHighscore.java:200` `String[] accessors = { "getPlayerName", "getName", "getTeamName" };`

### generation_none__single_shot__none r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:144` `String name = player.getTeamName();`

### generation_none__single_shot__static r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_none__single_shot__static r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_none__single_shot__static r3: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:140` `playerName = player.getTeamName();`

### generation_none__single_shot__static r4: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:144` `playerName = player.getTeamName();`

### generation_none__single_shot__static r5: passthrough, rejected at store, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioLevel.java:1184` `String teamName = this.players.get(0).getTeamName();`
- `ApoMarioHighscore.java:121` `playerName = best.getTeamName();`

### generation_s__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__none r2: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:145` `String name = player.getTeamName();`

### generation_s__single_shot__none r3: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:117` `String name = player.getTeamName();`

### generation_s__single_shot__none r4: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:107` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:109` `name = best.getAi().getTeamName();`

### generation_s__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:145` `name = selected.getTeamName();`

### generation_s__single_shot__static r2: skip_on_null, skipped at hook, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:200` `if (selected.getTeamName() != null && selected.getTeamName().trim().length() > 0) {`
- `ApoMarioHighscore.java:201` `playerName = selected.getTeamName();`

### generation_s__single_shot__static r3: passthrough, recorded, not functional

Final evaluated submission: 1; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:154` `String name = best.getTeamName();`

### generation_s__single_shot__static r4: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:217` `String playerName = selected == null ? null : selected.getTeamName();`

### generation_s__single_shot__static r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:142` `playerName = selected.getTeamName();`

### generation_sfb__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__none r3: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:89` `String name = selected.getTeamName();`

### generation_sfb__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:70` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:150` `String name = selected.getTeamName();`

### generation_sfb__single_shot__none r5: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:152` `String name = selected.getTeamName();`

### generation_sfb__single_shot__static r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__static r2: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:161` `name = player.getTeamName();`

### generation_sfb__single_shot__static r3: passthrough, rejected at store, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: recordRunEndAddsExactlyOneEntry.

- `ApoMarioHighscore.java:221` `String name = player.getTeamName();`

### generation_sfb__single_shot__static r4: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:196` `playerName = best.getTeamName();`

### generation_sfb__single_shot__static r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:43` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:138` `String name = selected.getTeamName();`

### reuse_f__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:67` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:137` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:140` `name = player.getAi().getTeamName();`

### reuse_f__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__static r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__static r2: passthrough, recorded, not functional

Final evaluated submission: 1; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:153` `playerName = player.getTeamName();`

### reuse_f__single_shot__static r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__static r4: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:151` `String name = best.getTeamName();`

### reuse_f__single_shot__static r5: passthrough, rejected at store, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:105` `String playerName = player.getTeamName();`
- `ApoMarioHighscore.java:110` `playerName = player.getAi().getTeamName();`

### reuse_sfb__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__none r2: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:63` `String name = best.getTeamName();`

### reuse_sfb__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__none r4: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:102` `String name = best.getTeamName();`

### reuse_sfb__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__static r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:126` `playerName = selected.getTeamName();`

### reuse_sfb__single_shot__static r2: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:171` `playerName = selected.getTeamName();`

### reuse_sfb__single_shot__static r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__static r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__static r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name
