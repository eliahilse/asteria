# Run-end hook audit: i24b-full

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_none__single_shot__none | 5 | 2 | 2 | 0 | 1 | 2 | 0 | 1 | 2 | 1 | 0 | 0 | 0 | 1 |
| generation_none__single_shot__static | 5 | 4 | 1 | 0 | 3 | 1 | 0 | 3 | 1 | 3 | 0 | 0 | 0 | 3 |
| generation_s__single_shot__none | 5 | 1 | 1 | 0 | 0 | 4 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| generation_s__single_shot__static | 5 | 3 | 1 | 0 | 4 | 0 | 0 | 4 | 1 | 4 | 0 | 0 | 0 | 4 |
| generation_sfb__single_shot__none | 5 | 2 | 1 | 0 | 1 | 3 | 0 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| generation_sfb__single_shot__static | 5 | 1 | 0 | 0 | 1 | 4 | 0 | 1 | 0 | 1 | 0 | 0 | 0 | 1 |
| reuse_f__single_shot__none | 5 | 0 | 2 | 0 | 0 | 3 | 0 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| reuse_f__single_shot__static | 5 | 2 | 0 | 0 | 2 | 3 | 0 | 2 | 0 | 2 | 0 | 0 | 0 | 2 |
| reuse_sfb__single_shot__none | 5 | 4 | 1 | 0 | 3 | 1 | 0 | 1 | 3 | 1 | 0 | 0 | 0 | 1 |
| reuse_sfb__single_shot__static | 5 | 2 | 1 | 0 | 2 | 2 | 0 | 2 | 1 | 2 | 1 | 1 | 1 | 1 |

## Rows for hand review

- generation_none__single_shot__none r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_none__single_shot__static r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_none__single_shot__static r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_none__single_shot__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__single_shot__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__single_shot__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, not functional
- generation_s__single_shot__static r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, not functional
- generation_s__single_shot__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_sfb__single_shot__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_f__single_shot__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_f__single_shot__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sfb__single_shot__none r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sfb__single_shot__static r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional

## Trajectories

### generation_none__single_shot__none r1: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioLevel.java:1183` `String name = this.players.get(0).getTeamName();`
- `ApoMarioHighscore.java:165` `name = name.substring(0, Math.max(1, name.length() - 1)) + "...";`

### generation_none__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:58` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:129` `String name = level.getPlayers().get(0).getTeamName();`

### generation_none__single_shot__none r3: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:177` `String name = player.getTeamName();`

### generation_none__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_none__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_none__single_shot__static r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_none__single_shot__static r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:177` `String name = player == null ? "Human" : player.getTeamName();`

### generation_none__single_shot__static r3: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:94` `String name = player.getTeamName();`

### generation_none__single_shot__static r4: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:178` `String name = selected.getTeamName();`

### generation_none__single_shot__static r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:170` `String name = player.getTeamName();`

### generation_s__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:59` `String name = playerName == null ? "Anonymous" : playerName.trim();`
- `ApoMarioHighscore.java:140` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:142` `name = selected.getAi().getTeamName();`

### generation_s__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:146` `String playerName = selected.getTeamName();`
- `ApoMarioHighscore.java:148` `playerName = selected.getAi().getTeamName();`

### generation_s__single_shot__static r2: passthrough, rejected at store, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioLevel.java:1216` `String name = selected.getTeamName();`

### generation_s__single_shot__static r3: passthrough, rejected at store, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioLevel.java:1200` `String playerName = player.getTeamName();`

### generation_s__single_shot__static r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:107` `String fileName = absoluteStore.getFileName() == null ? "highscore" : absoluteStore.getFileName().toString();`
- `ApoMarioHighscore.java:164` `String name = selected.getTeamName();`

### generation_s__single_shot__static r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:119` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:121` `if (player.getAi() != null && player.getAi().getTeamName() != null`
- `ApoMarioHighscore.java:122` `&& player.getAi().getTeamName().trim().length() > 0) {`
- `ApoMarioHighscore.java:123` `name = player.getAi().getTeamName();`

### generation_sfb__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:57` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:129` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:131` `name = best.getAi().getTeamName();`

### generation_sfb__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__none r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__none r4: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:130` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:133` `name = selected.getAi().getTeamName();`

### generation_sfb__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__static r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__static r2: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:108` `String name = selected.getTeamName();`

### generation_sfb__single_shot__static r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__static r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_sfb__single_shot__static r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__none r2: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: recordsRealPlayerName, recordedNameIsTheRealPlayersName.

- `ApoMarioHighscore.java:43` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:72` `if (player.getAi().getTeamName() != null && player.getAi().getTeamName().trim().length() > 0) {`
- `ApoMarioHighscore.java:73` `name = player.getAi().getTeamName().trim();`

### reuse_f__single_shot__none r3: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: recordsRealPlayerName, recordedNameIsTheRealPlayersName.

- `ApoMarioHighscore.java:42` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:115` `if (selected.getAi().getTeamName() != null`
- `ApoMarioHighscore.java:116` `&& selected.getAi().getTeamName().trim().length() > 0) {`
- `ApoMarioHighscore.java:117` `name = selected.getAi().getTeamName();`
- `ApoMarioHighscore.java:217` `this.playersNames.add(position, name == null || name.length() == 0 ? "Player" : name);`

### reuse_f__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__static r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__static r2: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:117` `name = selected.getTeamName();`

### reuse_f__single_shot__static r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__static r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_f__single_shot__static r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:176` `String name = player.getTeamName();`

### reuse_sfb__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:54` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:102` `String name = result.getTeamName();`

### reuse_sfb__single_shot__none r3: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:101` `String name = best.getTeamName();`

### reuse_sfb__single_shot__none r4: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:96` `String name = best.getTeamName();`

### reuse_sfb__single_shot__none r5: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:94` `String name = best.getTeamName();`

### reuse_sfb__single_shot__static r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:184` `String name = selected == null ? "Player" : selected.getTeamName();`

### reuse_sfb__single_shot__static r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__static r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sfb__single_shot__static r4: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:154` `String name = selected.getTeamName();`

### reuse_sfb__single_shot__static r5: passthrough, rejected at store, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:114` `String name = selected == null ? null : selected.getTeamName();`
