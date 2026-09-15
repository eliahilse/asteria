# Run-end hook audit: i21b-var

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| generation_s__single_shot__static | 3 | 3 | 2 | 0 | 1 | 0 | 0 | 1 | 2 | 1 | 0 | 0 | 0 | 1 |
| reuse_sb__single_shot__none | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__single_shot__static | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |

## Rows for hand review

- generation_s__single_shot__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional

## Trajectories

### generation_s__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:82` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:84` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscoreView.java:34` `if (names.isEmpty()) g.drawString("No completed runs yet", x + 18, row);`

### generation_s__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:41` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:77` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:78` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:58` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:59` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:101` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:102` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__static r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:118` `String candidate = p.getTeamName();`
- `ApoMarioHighscore.java:119` `if (candidate == null && p.getAi() != null) candidate = p.getAi().getTeamName();`

### generation_s__single_shot__static r2: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:86` `name = player.getTeamName();`
- `ApoMarioHighscore.java:87` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:93` `if (!validName(name)) name = "Player";`

### generation_s__single_shot__static r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:77` `String name = p == null ? "Player" : p.getTeamName();`
- `ApoMarioHighscore.java:78` `if (name == null || name.trim().length() == 0) name = p != null && p.getAi() != null ? p.getAi().getTeamName() : "Player";`
- `ApoMarioHighscore.java:79` `if (name == null || name.trim().length() == 0 || !validName(name)) name = "Player";`

### reuse_sb__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:43` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:125` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:126` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:36` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:37` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:70` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:71` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:35` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:36` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:80` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:81` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__static r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:126` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:127` `if (name == null || name.trim().length() == 0) name = "human";`

### reuse_sb__single_shot__static r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:71` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:72` `if (name == null || name.trim().length() == 0) name = "human";`

### reuse_sb__single_shot__static r3: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:91` `ApoMarioPlayer p=level.getPlayers().get(0); String name=p.getTeamName(); if(name==null || name.trim().length()==0) name="human";`
