# Run-end hook audit: i32b-s2

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 5 | 2 | 1 | 0 | 1 | 3 | 0 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| generation_s__single_shot__static | 5 | 3 | 0 | 0 | 3 | 2 | 0 | 3 | 0 | 3 | 0 | 0 | 0 | 3 |
| reuse_sb__single_shot__none | 5 | 2 | 0 | 0 | 2 | 3 | 0 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__single_shot__static | 5 | 2 | 0 | 0 | 2 | 3 | 0 | 2 | 0 | 2 | 0 | 0 | 0 | 2 |

## Rows for hand review

- generation_s__single_shot__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__single_shot__static r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__single_shot__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__single_shot__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__single_shot__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional

## Trajectories

### generation_s__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:51` `String name = playerName == null ? "Anonymous" : playerName.trim();`
- `ApoMarioHighscore.java:127` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:129` `name = player.getAi().getTeamName();`

### generation_s__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__none r3: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:143` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:146` `name = player.getAi().getTeamName();`

### generation_s__single_shot__none r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:160` `String name = validName(best.getTeamName());`

### generation_s__single_shot__static r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r4: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:141` `String name = player == null ? null : player.getTeamName();`
- `ApoMarioHighscore.java:143` `name = player.getAi().getTeamName();`

### generation_s__single_shot__static r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:163` `name = selected.getTeamName();`

### reuse_sb__single_shot__none r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__none r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__none r3: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:214` `this.storeRun(best.getPoints(), level.getPassedTime(), best.getTeamName());`

### reuse_sb__single_shot__none r4: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:182` `String name = player.getTeamName();`

### reuse_sb__single_shot__none r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__static r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:107` `String name = selected.getTeamName();`

### reuse_sb__single_shot__static r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__static r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__static r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__static r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:132` `name = selected.getTeamName();`
