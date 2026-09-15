# Run-end hook audit: i31-v13

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | 5 | 5 | 2 | 0 | 3 | 0 | 0 | 3 | 2 | 3 | 0 | 0 | 0 | 3 |
| generation_s__agentic__static | 5 | 5 | 0 | 0 | 5 | 0 | 0 | 5 | 0 | 5 | 0 | 0 | 0 | 5 |
| generation_s__agentic__static-guard | 5 | 4 | 0 | 2 | 3 | 0 | 2 | 3 | 0 | 5 | 0 | 0 | 0 | 5 |
| reuse_sb__agentic__none | 5 | 5 | 3 | 0 | 2 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__agentic__static | 5 | 4 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 1 | 0 | 1 |
| reuse_sb__agentic__static-guard | 5 | 4 | 1 | 2 | 2 | 0 | 2 | 2 | 1 | 4 | 0 | 0 | 0 | 4 |

## Rows for hand review

- generation_s__agentic__none r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__none r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__none r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-guard r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-guard r2: skip_on_null, skipped at hook, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-guard r3: skip_on_null, skipped at hook, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-guard r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-guard r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, not functional
- generation_s__agentic__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-guard r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-guard r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-guard r4: skip_on_null, skipped at hook, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-guard r5: skip_on_null, skipped at hook, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static r5: fallback, recorded, rejectsNullName pass, null-name tests failed 2, not functional

## Trajectories

### generation_s__agentic__none r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:180` `String name = selected.getTeamName();`

### generation_s__agentic__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:44` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:129` `String name = selected.getTeamName();`

### generation_s__agentic__none r3: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:98` `String name = selected.getTeamName();`

### generation_s__agentic__none r4: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:97` `String name = selected.getTeamName();`

### generation_s__agentic__none r5: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:33` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:80` `storeRun(selected.getPoints(), level.getPassedTime(), selected.getTeamName());`

### generation_s__agentic__static-guard r1: passthrough, rejected at store, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:202` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:191` `String name = player.getTeamName();`

### generation_s__agentic__static-guard r2: skip_on_null, skipped at hook, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:87` `if (selected.getTeamName() != null && isValidName(selected.getTeamName())) {`
- `ApoMarioHighscore.java:88` `name = selected.getTeamName().trim();`

### generation_s__agentic__static-guard r3: skip_on_null, skipped at hook, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:63` `if (selected.getTeamName() != null && selected.getTeamName().trim().length() > 0`
- `ApoMarioHighscore.java:64` `&& selected.getTeamName().length() <= MAX_NAME_LENGTH) {`
- `ApoMarioHighscore.java:65` `name = selected.getTeamName().trim();`

### generation_s__agentic__static-guard r4: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:97` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:99` `name = selected.getAi().getTeamName();`

### generation_s__agentic__static-guard r5: passthrough, rejected at store, not functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: recordsRealPlayerName, recordedNameIsTheRealPlayersName.

- `ApoMarioHighscore.java:89` `if (selected != null && selected.getAi() != null && selected.getAi().getTeamName() != null) {`
- `ApoMarioHighscore.java:90` `String candidate = selected.getAi().getTeamName().trim();`

### generation_s__agentic__static r1: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:88` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:90` `name = selected.getAi().getTeamName();`

### generation_s__agentic__static r2: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:109` `if (player != null && player.getTeamName() != null`
- `ApoMarioHighscore.java:110` `&& player.getTeamName().trim().length() > 0) {`
- `ApoMarioHighscore.java:111` `name = player.getTeamName().trim();`

### generation_s__agentic__static r3: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:76` `String name = selected.getTeamName();`

### generation_s__agentic__static r4: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:97` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:99` `name = player.getAi().getTeamName();`

### generation_s__agentic__static r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:64` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:66` `name = player.getAi().getTeamName();`

### reuse_sb__agentic__none r1: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:45` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:97` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:99` `name = player.getAi().getTeamName();`

### reuse_sb__agentic__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:48` `String name = playerName == null ? "Human" : playerName.trim();`
- `ApoMarioHighscore.java:112` `String name = selected.getTeamName();`

### reuse_sb__agentic__none r3: passthrough, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:92` `String name = selected.getTeamName();`

### reuse_sb__agentic__none r4: passthrough, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:62` `String name = best.getTeamName();`

### reuse_sb__agentic__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:32` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:69` `String name = player.getTeamName();`

### reuse_sb__agentic__static-guard r1: fallback, recorded, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioLevel.java:1282` `this.highscoreName = selected == null ? null : selected.getTeamName();`
- `ApoMarioHighscore.java:81` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:126` `String fileName = this.store.getFileName() == null ? "highscore" : this.store.getFileName().toString();`

### reuse_sb__agentic__static-guard r2: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:130` `String name = selected.getTeamName();`

### reuse_sb__agentic__static-guard r3: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:99` `String playerName = player.getTeamName();`

### reuse_sb__agentic__static-guard r4: skip_on_null, skipped at hook, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1239` `String highscoreName = highscorePlayer.getTeamName();`
- `ApoMarioHighscore.java:88` `if (selected.getTeamName() != null && selected.getTeamName().trim().length() > 0) {`
- `ApoMarioHighscore.java:89` `name = selected.getTeamName().trim();`

### reuse_sb__agentic__static-guard r5: skip_on_null, skipped at hook, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:114` `if (player.getTeamName() != null && player.getTeamName().trim().length() > 0) {`
- `ApoMarioHighscore.java:115` `name = player.getTeamName().trim();`

### reuse_sb__agentic__static r1: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:83` `String prefix = absolute.getFileName() == null ? "highscore" : absolute.getFileName().toString();`
- `ApoMarioHighscore.java:124` `String name = validName(selected.getTeamName());`
- `ApoMarioHighscore.java:125` `if (name == null) name = "human";`

### reuse_sb__agentic__static r2: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:100` `name = selected.getTeamName();`
- `ApoMarioHighscore.java:110` `if (name == null) name = "human";`

### reuse_sb__agentic__static r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:132` `String name = selected == null ? "human" : selected.getTeamName();`

### reuse_sb__agentic__static r4: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:76` `String name = selected == null ? "human" : selected.getTeamName();`

### reuse_sb__agentic__static r5: fallback, recorded, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: runEndRecordsScoreWithoutHelp, recordedNameIsTheRealPlayersName, noPhantomEntriesWithoutRunEnd, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:106` `if (selected.getTeamName() != null`
- `ApoMarioHighscore.java:107` `&& selected.getTeamName().trim().length() > 0) {`
- `ApoMarioHighscore.java:108` `name = selected.getTeamName().trim();`
- `ApoMarioHighscore.java:136` `String fileName = this.store.getFileName() == null ? "highscores" : this.store.getFileName().toString();`
