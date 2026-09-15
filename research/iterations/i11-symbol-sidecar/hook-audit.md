# Run-end hook audit: i11-symbol-sidecar

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__adaptive | 3 | 1 | 1 | 1 | 1 | 0 | 1 | 1 | 1 | 2 | 2 | 2 | 2 | 0 |
| generation_s__single_shot__none | 3 | 3 | 2 | 0 | 1 | 0 | 0 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| generation_s__single_shot__static | 3 | 2 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 0 | 0 | 1 | 0 | 1 |

## Rows for hand review

- generation_s__single_shot__static r2: fallback, recorded, rejectsNullName pass, null-name tests failed 2, not functional

## Trajectories

### generation_s__agentic__adaptive r1: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1184` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1185` `if (name == null || name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:56` `String name = selected.getTeamName();`

### generation_s__agentic__adaptive r2: passthrough, rejected at store, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:83` `String name = chosen.getTeamName();`

### generation_s__agentic__adaptive r3: skip_on_null, skipped at hook, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:45` `if (selected == null || selected.getTeamName() == null) return;`
- `ApoMarioHighscore.java:48` `storeRun(selected.getPoints(), elapsed, selected.getTeamName());`

### generation_s__single_shot__none r1: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:138` `String name = best.getTeamName();`

### generation_s__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioLevel.java:1182` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1183` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioPanel.java:544` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:38` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:69` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:70` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:35` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:70` `String name = selected.getTeamName();`

### generation_s__single_shot__static r1: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1187` `String name = selected.getTeamName();`
- `ApoMarioLevel.java:1188` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:89` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:90` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__static r2: fallback, recorded, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedNameIsTheRealPlayersName, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:64` `String name=selected.getTeamName();`
- `ApoMarioHighscore.java:65` `storeRun(selected.getPoints(), level.getPassedTime(), name == null || name.trim().length()==0 ? "Player" : name);`

### generation_s__single_shot__static r3: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1186` `String name = selected.getTeamName();`
- `ApoMarioLevel.java:1187` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:69` `String name = selected.getTeamName();`
