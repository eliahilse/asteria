# Run-end hook audit: i29-v11

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | 5 | 5 | 1 | 0 | 4 | 0 | 0 | 4 | 1 | 4 | 0 | 0 | 0 | 4 |
| generation_s__agentic__static | 5 | 5 | 1 | 1 | 3 | 0 | 1 | 3 | 1 | 4 | 0 | 0 | 0 | 4 |
| generation_s__agentic__static-guard | 5 | 4 | 3 | 0 | 2 | 0 | 0 | 2 | 3 | 2 | 0 | 1 | 1 | 3 |
| reuse_sb__agentic__none | 5 | 4 | 2 | 0 | 2 | 1 | 0 | 0 | 4 | 0 | 0 | 1 | 0 | 0 |
| reuse_sb__agentic__static | 5 | 5 | 2 | 0 | 3 | 0 | 0 | 3 | 2 | 3 | 0 | 0 | 0 | 3 |
| reuse_sb__agentic__static-guard | 5 | 5 | 4 | 0 | 1 | 0 | 0 | 1 | 4 | 1 | 0 | 0 | 0 | 1 |

## Rows for hand review

- generation_s__agentic__none r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__none r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__none r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__none r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-guard r2: fallback, recorded, rejectsNullName pass, null-name tests failed 2, not functional
- generation_s__agentic__static-guard r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-guard r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r4: skip_on_null, skipped at hook, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-guard r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional

## Trajectories

### generation_s__agentic__none r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:126` `String name = player.getTeamName();`

### generation_s__agentic__none r2: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:97` `String name = selected.getTeamName();`

### generation_s__agentic__none r3: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioLevel.java:1191` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:44` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:169` `String name = best.getTeamName();`

### generation_s__agentic__none r4: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:105` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:107` `name = player.getAi().getTeamName();`

### generation_s__agentic__none r5: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:163` `String name = player.getTeamName();`

### generation_s__agentic__static-guard r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:136` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:144` `if (playerName.length() > MAX_NAME_LENGTH) playerName = "Player";`
- `ApoMarioHighscore.java:179` `if (name == null || name.length() == 0) return "Player";`

### generation_s__agentic__static-guard r2: fallback, recorded, not functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioMenu.java:590` `if (name == null) name = "Player";`
- `ApoMarioHighscore.java:97` `name = selected.getTeamName();`

### generation_s__agentic__static-guard r3: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:133` `name = selected.getTeamName();`

### generation_s__agentic__static-guard r4: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:148` `playerName = selected.getTeamName();`

### generation_s__agentic__static-guard r5: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1202` `if (selected.getTeamName() != null && selected.getTeamName().length() > 0) name = selected.getTeamName();`
- `ApoMarioLevel.java:1210` `if (name.length() > 32 || name.indexOf('\t') >= 0 || name.indexOf('\r') >= 0 || name.indexOf('\n') >= 0) name = "Player";`
- `ApoMarioHighscore.java:100` `if (selected.getTeamName() != null && selected.getTeamName().length() > 0) {`
- `ApoMarioHighscore.java:101` `playerName = selected.getTeamName();`
- `ApoMarioHighscoreView.java:57` `if (name == null || name.length() == 0) return "Player";`

### generation_s__agentic__static r1: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:156` `String candidate = selected.getTeamName();`

### generation_s__agentic__static r2: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:112` `String candidate = selected.getTeamName();`

### generation_s__agentic__static r3: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:175` `name = selected.getTeamName();`
- `ApoMarioHighscore.java:221` `String name = run.playerName == null ? "Player" : run.playerName;`

### generation_s__agentic__static r4: skip_on_null, skipped at hook, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:162` `if (selected.getTeamName() != null && selected.getTeamName().length() > 0`
- `ApoMarioHighscore.java:163` `&& selected.getTeamName().length() <= MAX_NAME_LENGTH) {`
- `ApoMarioHighscore.java:164` `name = selected.getTeamName();`

### generation_s__agentic__static r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:86` `String candidate = selected.getTeamName();`

### reuse_sb__agentic__none r1: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:98` `String name = selected.getTeamName();`

### reuse_sb__agentic__none r2: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:32` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:92` `String name = best.getTeamName();`

### reuse_sb__agentic__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:38` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:118` `String name = best.getTeamName();`

### reuse_sb__agentic__none r4: passthrough, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:74` `String name = selected.getTeamName();`

### reuse_sb__agentic__none r5: none, none, not functional

Final evaluated submission: 5; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- no new line reads the live player name

### reuse_sb__agentic__static-guard r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:98` `String name = player == null ? "Player" : player.getTeamName();`
- `ApoMarioHighscorePanel.java:69` `g.drawString(names.get(i) == null ? "Player" : names.get(i), 165, y);`

### reuse_sb__agentic__static-guard r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:74` `name = selected.getTeamName();`
- `ApoMarioHighscorePanel.java:71` `String name = names.get(i) == null ? "Player" : names.get(i);`

### reuse_sb__agentic__static-guard r3: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:93` `String candidate = selected.getTeamName();`

### reuse_sb__agentic__static-guard r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:198` `String name = selected == null ? null : selected.getTeamName();`
- `ApoMarioHighscore.java:199` `if (!isValidName(name)) name = "Player";`

### reuse_sb__agentic__static-guard r5: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1202` `String name = selected == null ? "Player" : selected.getTeamName();`
- `ApoMarioHighscore.java:46` `if (selected.getTeamName() != null && selected.getTeamName().trim().length() > 0) {`
- `ApoMarioHighscore.java:47` `playerName = selected.getTeamName().trim();`

### reuse_sb__agentic__static r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:166` `String name = selected.getTeamName();`

### reuse_sb__agentic__static r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:117` `name = player.getTeamName();`
- `ApoMarioHighscoreView.java:87` `String name = names.get(i) == null ? "Player" : names.get(i);`

### reuse_sb__agentic__static r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:126` `name = player.getTeamName();`
- `ApoMarioHighscore.java:296` `String name = names.get(i) == null ? "Player" : names.get(i);`

### reuse_sb__agentic__static r4: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:90` `String name = selected.getTeamName();`

### reuse_sb__agentic__static r5: passthrough, rejected at store, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:114` `String name = player == null ? DEFAULT_NAME : player.getTeamName();`
