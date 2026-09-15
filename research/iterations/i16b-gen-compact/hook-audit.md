# Run-end hook audit: i16b-gen-compact

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| generation_s__single_shot__static | 5 | 3 | 3 | 2 | 0 | 0 | 2 | 0 | 3 | 2 | 2 | 2 | 2 | 0 |

## Rows for hand review

- none

## Trajectories

### generation_s__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:40` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:107` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:109` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioLevel.java:1188` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1189` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:38` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:39` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:102` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:103` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:38` `if (playerName == null || playerName.trim().length() == 0) playerName = "Player";`
- `ApoMarioHighscore.java:84` `String name = player.getTeamName();`

### generation_s__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:40` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:41` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:76` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:78` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:42` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:43` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:75` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:77` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__static r1: skip_on_null, skipped at hook, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioLevel.java:1187` `if (human == null || human.getTeamName() == null || human.getTeamName().length() == 0) return;`
- `ApoMarioHighscore.java:130` `if (human == null || human.getTeamName() == null || human.getTeamName().length() == 0) return;`
- `ApoMarioHighscore.java:133` `String name = human.getTeamName();`

### generation_s__single_shot__static r2: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:113` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:114` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__static r3: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1188` `String name = selected.getTeamName();`
- `ApoMarioLevel.java:1189` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:64` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:65` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__static r4: skip_on_null, skipped at hook, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:96` `if (p != null && p.getAi() == null && p.getTeamName() != null &&`
- `ApoMarioHighscore.java:104` `storeRun(selected.getPoints(), elapsed, selected.getTeamName());`

### generation_s__single_shot__static r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:65` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:66` `if (name == null || name.trim().length() == 0) name = "Player";`
