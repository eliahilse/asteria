# Run-end hook audit: i09-generic-acquisition

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__catalog | 5 | 5 | 2 | 0 | 3 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| generation_s__none | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| generation_s__requirements | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| generation_s__task_only | 5 | 5 | 4 | 0 | 1 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__catalog | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__none | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__requirements | 5 | 5 | 4 | 0 | 1 | 0 | 0 | 1 | 4 | 1 | 0 | 0 | 0 | 1 |
| reuse_sb__task_only | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |

## Rows for hand review

- reuse_sb__requirements r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional

## Trajectories

### generation_s__catalog r1: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:77` `String name = chosen.getTeamName();`

### generation_s__catalog r2: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:52` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:54` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__catalog r3: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:55` `String name = chosen.getTeamName();`

### generation_s__catalog r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:79` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:80` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__catalog r5: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:90` `String name = selected.getTeamName();`

### generation_s__none r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioLevel.java:1182` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:39` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:125` `String name = player.getTeamName();`

### generation_s__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:41` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:95` `String name = best.getTeamName();`

### generation_s__none r3: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioLevel.java:1186` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1187` `if (name == null || name.trim().length() == 0) name = "Human";`
- `ApoMarioHighscore.java:43` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:51` `String name = playerName == null ? "Unknown" : playerName.trim();`

### generation_s__none r4: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioLevel.java:1184` `String name = selected.getTeamName();`
- `ApoMarioLevel.java:1185` `if (name == null || name.trim().length() == 0) name = "Human";`
- `ApoMarioHighscore.java:42` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:43` `if (name == null || name.trim().length() == 0) name = "Human";`
- `ApoMarioHighscore.java:48` `String name = playerName == null ? "Unknown" : playerName.trim();`

### generation_s__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:39` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:74` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:76` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__requirements r1: fallback, recorded, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:91` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:92` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__requirements r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:52` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:53` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__requirements r3: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:85` `String name = p.getTeamName();`
- `ApoMarioHighscore.java:87` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__requirements r4: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:72` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:73` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__requirements r5: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:91` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:92` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__task_only r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:75` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:76` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__task_only r2: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1182` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1184` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:41` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:43` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__task_only r3: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioPanel.java:229` `String name = player.getTeamName();`
- `ApoMarioPanel.java:231` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:46` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:48` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__task_only r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:51` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:53` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__task_only r5: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:68` `String name = player.getTeamName();`

### reuse_sb__catalog r1: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:103` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:104` `if (name == null || name.trim().length() == 0) name = "Human";`

### reuse_sb__catalog r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:44` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:45` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscoreStore.java:21` `if (name == null) name = "Player";`

### reuse_sb__catalog r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:89` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:90` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__catalog r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:76` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:77` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__catalog r5: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:41` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:42` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:31` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:85` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:86` `if (name == null || name.length() == 0) name = "Player";`

### reuse_sb__none r2: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioPanel.java:228` `String name = player.getTeamName();`
- `ApoMarioPanel.java:229` `if (name == null || name.trim().length() == 0) name = "Human";`
- `ApoMarioHighscore.java:52` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:60` `String name = playerName == null ? "Unknown" : playerName.trim();`

### reuse_sb__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:29` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:81` `if (chosen != null) storeRun(chosen.getPoints(), level.getPassedTime(), chosen.getTeamName());`

### reuse_sb__none r4: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioLevel.java:1184` `String name = best.getTeamName();`
- `ApoMarioLevel.java:1185` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:36` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:37` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:42` `String name = playerName == null ? "Player" : playerName.trim();`

### reuse_sb__none r5: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:32` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:69` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:70` `if ((name == null || name.trim().length() == 0) && chosen.getAi() != null) name = chosen.getAi().getTeamName();`

### reuse_sb__requirements r1: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioPanel.java:377` `String name = selected.getTeamName();`
- `ApoMarioPanel.java:378` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:33` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:34` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__requirements r2: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:109` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:110` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__requirements r3: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:89` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:90` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__requirements r4: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:81` `String name = selected.getTeamName();`

### reuse_sb__requirements r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:56` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:57` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__task_only r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:51` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:136` `String name = player.getTeamName();`

### reuse_sb__task_only r2: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:104` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:105` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__task_only r3: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:63` `String n = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:91` `String n = chosen.getTeamName(); if (n == null || n.trim().length() == 0) n = "Player";`

### reuse_sb__task_only r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:105` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:106` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__task_only r5: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1183` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1184` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:33` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:34` `if (name == null || name.trim().length() == 0) name = "Player";`
