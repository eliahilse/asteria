# Run-end hook audit: i22-oneshot

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 5 | 3 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 1 | 0 | 1 |
| generation_s__single_shot__static | 5 | 0 | 2 | 0 | 0 | 3 | 0 | 0 | 2 | 0 | 0 | 1 | 0 | 1 |
| reuse_sb__single_shot__none | 5 | 1 | 2 | 0 | 0 | 3 | 0 | 0 | 2 | 0 | 0 | 1 | 0 | 1 |
| reuse_sb__single_shot__static | 5 | 1 | 2 | 1 | 0 | 2 | 1 | 0 | 2 | 1 | 0 | 1 | 0 | 2 |

## Rows for hand review

- generation_s__single_shot__none r3: fallback, recorded, rejectsNullName fail, null-name tests failed 2, not functional
- generation_s__single_shot__static r5: fallback, recorded, rejectsNullName pass, null-name tests failed 2, not functional
- reuse_sb__single_shot__none r3: fallback, recorded, rejectsNullName compile_error, null-name tests failed 2, not functional
- reuse_sb__single_shot__static r3: skip_on_null, skipped at hook, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__single_shot__static r5: fallback, recorded, rejectsNullName compile_error, null-name tests failed 2, not functional

## Trajectories

### generation_s__single_shot__none r1: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioPanel.java:241` `String name = player.getTeamName();`
- `ApoMarioPanel.java:242` `if (name == null || name.trim().length() == 0) name = "Human";`
- `ApoMarioHighscore.java:36` `String name = playerName == null ? "Unknown" : playerName.trim();`

### generation_s__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:103` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:104` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r3: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:31` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:73` `String name = player.getTeamName();`

### generation_s__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:30` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:31` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:56` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:57` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:65` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:67` `name = player.getAi() == null ? "Player" : player.getAi().getTeamName();`

### generation_s__single_shot__static r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r3: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioLevel.java:1185` `String name = selected.getTeamName();`
- `ApoMarioLevel.java:1186` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioMenu.java:541` `String name = names.get(i) == null ? "Player" : names.get(i);`

### generation_s__single_shot__static r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r5: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioLevel.java:1185` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1186` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioMenu.java:567` `String name = n.get(i) == null ? "Player" : n.get(i);`

### reuse_sb__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__none r3: fallback, recorded, not functional

Final evaluated submission: 1; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:32` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:33` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:102` `if (best != null) storeRun(best.getPoints(), level.getPassedTime(), best.getTeamName());`

### reuse_sb__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:43` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:44` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:63` `storeRun(player.getPoints(), level.getPassedTime(), player.getTeamName());`

### reuse_sb__single_shot__static r1: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioPanel.java:227` `String name = p.getTeamName();`
- `ApoMarioPanel.java:228` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__static r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__static r3: skip_on_null, skipped at hook, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:81` `if (player.getTeamName() != null && player.getTeamName().trim().length() > 0`
- `ApoMarioHighscore.java:82` `&& player.getTeamName().length() <= MAX_NAME && isSafe(player.getTeamName())) name = player.getTeamName();`

### reuse_sb__single_shot__static r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__static r5: fallback, recorded, not functional

Final evaluated submission: 1; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:85` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:86` `if (name == null || name.trim().length() == 0 || name.length() > MAX_NAME) name = "Player";`
