# Run-end hook audit: i21a-var

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| generation_s__single_shot__static | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__single_shot__none | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__single_shot__static | 3 | 3 | 2 | 0 | 1 | 0 | 0 | 1 | 2 | 1 | 0 | 0 | 0 | 1 |

## Rows for hand review

- reuse_sb__single_shot__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional

## Trajectories

### generation_s__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:40` `String name = playerName == null ? "Unknown" : playerName.trim();`
- `ApoMarioHighscore.java:95` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:97` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:32` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:33` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:79` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:81` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:35` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:36` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:65` `String name = player.getTeamName();`

### generation_s__single_shot__static r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:105` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:106` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__static r2: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:59` `String name = p.getTeamName();`
- `ApoMarioHighscore.java:60` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__static r3: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1188` `String name = selected.getTeamName();`
- `ApoMarioLevel.java:1189` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:65` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:66` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:36` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:37` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:76` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:77` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:34` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:35` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:75` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:76` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:40` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:41` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:100` `if (chosen != null) storeRun(chosen.getPoints(), level.getPassedTime(), chosen.getTeamName());`

### reuse_sb__single_shot__static r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:73` `String name = player.getTeamName();`

### reuse_sb__single_shot__static r2: fallback, recorded, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:55` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:56` `if (name == null || name.trim().length() == 0) name = "Anonymous";`

### reuse_sb__single_shot__static r3: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:86` `String name = player == null ? "Player" : player.getTeamName();`
- `ApoMarioHighscore.java:87` `if (!validName(name)) name = "Player";`
