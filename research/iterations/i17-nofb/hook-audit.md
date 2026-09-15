# Run-end hook audit: i17-nofb

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| generation_s__single_shot__static | 5 | 4 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 1 | 0 | 1 |

## Rows for hand review

- generation_s__single_shot__static r2: fallback, recorded, rejectsNullName pass, null-name tests failed 2, not functional

## Trajectories

### generation_s__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioLevel.java:1189` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:39` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:73` `String name = player.getTeamName();`
- `ApoMarioHighscorePanel.java:36` `if (names.isEmpty()) g.drawString("No runs recorded", x + 20, row);`

### generation_s__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:44` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:70` `String name = selected.getTeamName();`

### generation_s__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:45` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:78` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:80` `name = player.getAi() == null ? "Player" : player.getAi().getAuthor();`

### generation_s__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:40` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:41` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:98` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:99` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:39` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:101` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:102` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__static r1: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:154` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:155` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__static r2: fallback, recorded, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedNameIsTheRealPlayersName, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:47` `String name=selected.getTeamName();`
- `ApoMarioHighscore.java:49` `if (name == null || name.trim().length()==0) name="Player";`

### generation_s__single_shot__static r3: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:92` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:93` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__static r4: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1184` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1185` `if (name == null || name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:143` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:144` `if (name == null || name.length() == 0) name = "Player";`

### generation_s__single_shot__static r5: fallback, recorded, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:83` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:84` `if (name == null || name.trim().length() == 0) name = "Player";`
