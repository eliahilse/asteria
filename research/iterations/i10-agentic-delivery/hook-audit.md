# Run-end hook audit: i10-agentic-delivery

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__adaptive | 5 | 3 | 4 | 0 | 1 | 0 | 0 | 1 | 4 | 1 | 1 | 2 | 0 | 1 |
| generation_s__agentic__none | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| generation_s__agentic__static | 5 | 1 | 2 | 2 | 1 | 0 | 2 | 1 | 2 | 3 | 3 | 3 | 2 | 0 |
| generation_s__single_shot__none | 5 | 5 | 4 | 0 | 1 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| generation_s__single_shot__static | 5 | 2 | 2 | 3 | 0 | 0 | 3 | 0 | 2 | 3 | 3 | 3 | 3 | 0 |
| reuse_sb__agentic__adaptive | 5 | 4 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 1 | 0 | 1 |
| reuse_sb__agentic__none | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__agentic__static | 5 | 4 | 4 | 0 | 1 | 0 | 0 | 1 | 4 | 1 | 1 | 1 | 0 | 0 |
| reuse_sb__single_shot__none | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__single_shot__static | 5 | 4 | 4 | 0 | 1 | 0 | 0 | 1 | 4 | 1 | 1 | 1 | 1 | 0 |

## Rows for hand review

- generation_s__agentic__adaptive r5: fallback, recorded, rejectsNullName pass, null-name tests failed 2, not functional
- reuse_sb__agentic__adaptive r1: fallback, recorded, rejectsNullName compile_error, null-name tests failed 2, not functional

## Trajectories

### generation_s__agentic__adaptive r1: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:67` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:68` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__agentic__adaptive r2: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:53` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:54` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__agentic__adaptive r3: passthrough, rejected at store, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioLevel.java:1183` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:61` `String name = player.getTeamName();`

### generation_s__agentic__adaptive r4: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1182` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1183` `if (name == null || name.length() == 0) name = "Player 1";`
- `ApoMarioHighscore.java:45` `if (player == null || player.getTeamName() == null || player.getTeamName().length() == 0) return;`
- `ApoMarioHighscore.java:46` `storeRun(player.getPoints(), level.getPassedTime(), player.getTeamName());`

### generation_s__agentic__adaptive r5: fallback, recorded, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioLevel.java:1186` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1187` `if (name == null || name.length() == 0) name = "Player" + (i + 1);`
- `ApoMarioPanel.java:546` `String name = p.getTeamName(); if (name == null || name.length() == 0) name = "Player" + (i + 1);`
- `ApoMarioHighscore.java:81` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:82` `if (name == null || name.length() == 0) name = "Player" + (i + 1);`

### generation_s__agentic__none r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:42` `if (playerName == null || playerName.trim().length() == 0) playerName = "Player";`
- `ApoMarioHighscore.java:70` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:71` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__agentic__none r2: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1180` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1181` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:55` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:56` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__agentic__none r3: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:31` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:32` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:64` `storeRun(selected.getPoints(), level.getPassedTime(), selected.getTeamName());`
- `ApoMarioHighscore.java:131` `private Entry(int score, int time, String name) { this.score = score; this.time = time; this.name = name == null ? "Player" : name; }`

### generation_s__agentic__none r4: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:34` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:35` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:64` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:65` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__agentic__none r5: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:68` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:69` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:110` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:111` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__agentic__static r1: skip_on_null, skipped at hook, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioLevel.java:1193` `if (player == null || player.getTeamName() == null || !(this.component instanceof ApoMarioPanel)) return;`
- `ApoMarioLevel.java:1195` `if (board != null && board.storeRun(player.getPoints(), this.getPassedTime(), player.getTeamName())) this.runRecorded = true;`
- `ApoMarioHighscore.java:98` `storeRun(selected.getPoints(), level.getPassedTime(), selected.getTeamName());`

### generation_s__agentic__static r2: fallback, recorded, not functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioLevel.java:1252` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1253` `if (name == null || name.length() == 0) name = "Player";`
- `ApoMarioMenu.java:528` `String name = names.get(i) == null ? "Player" : names.get(i);`

### generation_s__agentic__static r3: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1184` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1185` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:119` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:120` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__agentic__static r4: skip_on_null, skipped at hook, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:46` `if (player == null || player.getAi() != null || player.getTeamName() == null) return;`
- `ApoMarioHighscore.java:50` `if (storeRun(player.getPoints(), elapsed, player.getTeamName())) persistAcrossRuns();`

### generation_s__agentic__static r5: passthrough, rejected at store, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedNameIsTheRealPlayersName, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:65` `storeRun(player.getPoints(), level.getPassedTime(), player.getTeamName());`

### generation_s__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:42` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:43` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:54` `String name = player.getTeamName();`

### generation_s__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:63` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:64` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:117` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:118` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r3: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:62` `String name = player.getTeamName();`

### generation_s__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:40` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:67` `String name = player.getTeamName();`

### generation_s__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:61` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:62` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:105` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:107` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__static r1: skip_on_null, skipped at hook, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioLevel.java:1182` `if (player == null || player.getAi() != null || player.getTeamName() == null) return;`
- `ApoMarioLevel.java:1184` `panel.getHighscore().storeRun(player.getPoints(), this.getPassedTime(), player.getTeamName());`
- `ApoMarioHighscore.java:49` `if (first != null && first.getTeamName() != null) selected = first;`
- `ApoMarioHighscore.java:53` `if (player != null && player.getAi() == null && player.getTeamName() != null) {`
- `ApoMarioHighscore.java:66` `storeRun(selected.getPoints(), elapsed, selected.getTeamName());`

### generation_s__single_shot__static r2: skip_on_null, skipped at hook, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioLevel.java:1182` `if (player == null || player.getTeamName() == null) return;`
- `ApoMarioLevel.java:1183` `panel.getHighscore().storeRun(player.getPoints(), this.getPassedTime(), player.getTeamName());`
- `ApoMarioHighscore.java:62` `String name = player.getTeamName();`

### generation_s__single_shot__static r3: skip_on_null, skipped at hook, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioLevel.java:1187` `if (player == null || player.getTeamName() == null) return;`
- `ApoMarioHighscore.java:44` `if (player != null && player.getTeamName() != null`
- `ApoMarioHighscore.java:52` `storeRun(selected.getPoints(), elapsed, selected.getTeamName());`

### generation_s__single_shot__static r4: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:139` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:140` `if (name == null || name.length() == 0) name = "Player";`

### generation_s__single_shot__static r5: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:98` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:99` `if (name == null || name.length() == 0) name = "Player";`

### reuse_sb__agentic__adaptive r1: fallback, recorded, not functional

Final evaluated submission: 3; compilation: fail; rejectsNullName: compile_error; failing functional checks: emptyBoardInitially, saveAddsEntry, parallelListsAligned, boardSortedDescendingByPoints, entriesPersistAcrossSessions, emptyPersistRobust, rankingPreservedAfterReload, recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry, runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:28` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:29` `if (name.length() == 0 || name.length() > MAX_NAME || hasControl(name)) name = "Player";`
- `ApoMarioHighscore.java:62` `if (elapsed >= 0) storeRun(p.getPoints(), elapsed, p.getTeamName());`

### reuse_sb__agentic__adaptive r2: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:34` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:35` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:74` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:75` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:99` `if (names.isEmpty()) g.drawString("No runs recorded", 70, 85);`

### reuse_sb__agentic__adaptive r3: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:59` `String name = p.getTeamName(); if (name == null || name.trim().length() == 0) name = "Human";`

### reuse_sb__agentic__adaptive r4: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:31` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:32` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:57` `String name = chosen.getTeamName();`

### reuse_sb__agentic__adaptive r5: fallback, recorded, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:31` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:32` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:69` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:70` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__agentic__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:35` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:36` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:70` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:71` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__agentic__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:43` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:44` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:119` `String name = chosen.getTeamName();`

### reuse_sb__agentic__none r3: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:36` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:37` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:85` `storeRun(player.getPoints(), level.getPassedTime(), player.getTeamName());`

### reuse_sb__agentic__none r4: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:30` `if (playerName == null || playerName.trim().length() == 0) playerName = "Player";`
- `ApoMarioHighscore.java:78` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:79` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__agentic__none r5: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:26` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:27` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:46` `storeRun(p.getPoints(), level.getPassedTime(), p.getTeamName());`

### reuse_sb__agentic__static r1: passthrough, rejected at store, not functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: recordedNameIsTheRealPlayersName, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:41` `try { storeRun(player.getPoints(), level.getPassedTime(), player.getTeamName()); } catch (RuntimeException ignored) { }`

### reuse_sb__agentic__static r2: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:69` `String name = p.getTeamName();`
- `ApoMarioHighscore.java:70` `if (name == null || name.trim().length() == 0) name = "Human";`

### reuse_sb__agentic__static r3: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:58` `String name=p.getTeamName(); if(name==null || name.trim().length()==0) name="Human";`

### reuse_sb__agentic__static r4: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:51` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:52` `if (name == null || name.trim().length() == 0) name = "Human";`

### reuse_sb__agentic__static r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:92` `String n=p.getTeamName(); if (n==null || n.trim().length()==0) n="Player";`

### reuse_sb__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:37` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:38` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:72` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:73` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:32` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:33` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:58` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:59` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:31` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:32` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:55` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:56` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:31` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:32` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:81` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:82` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:34` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:35` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:57` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:58` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__static r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:66` `storeRun(selected.getPoints(), elapsed, selected.getTeamName() == null ? "Human" : selected.getTeamName());`

### reuse_sb__single_shot__static r2: passthrough, rejected at store, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:60` `if (elapsed >= 0 && storeRun(p.getPoints(), elapsed, p.getTeamName())) persistAcrossRuns();`

### reuse_sb__single_shot__static r3: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:69` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:70` `if (name == null || name.trim().length() == 0) name = "Human";`

### reuse_sb__single_shot__static r4: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:51` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:52` `if (name == null || name.trim().length() == 0) name = "Human";`

### reuse_sb__single_shot__static r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:52` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:53` `if (name == null || name.trim().length() == 0) name = "Human";`
