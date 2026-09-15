# Run-end hook audit: i30-v12

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | 5 | 5 | 0 | 0 | 5 | 0 | 0 | 5 | 0 | 5 | 0 | 0 | 0 | 5 |
| generation_s__agentic__static | 5 | 5 | 0 | 0 | 5 | 0 | 0 | 5 | 0 | 5 | 0 | 0 | 0 | 5 |
| generation_s__agentic__static-guard | 5 | 5 | 0 | 0 | 5 | 0 | 0 | 5 | 0 | 5 | 0 | 0 | 0 | 5 |
| reuse_sb__agentic__none | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__agentic__static | 5 | 5 | 1 | 0 | 4 | 0 | 0 | 4 | 1 | 4 | 0 | 0 | 0 | 4 |
| reuse_sb__agentic__static-guard | 5 | 5 | 1 | 0 | 4 | 0 | 0 | 4 | 1 | 4 | 0 | 0 | 0 | 4 |

## Rows for hand review

- generation_s__agentic__none r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__none r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__none r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__none r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__none r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-guard r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-guard r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-guard r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-guard r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-guard r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-guard r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-guard r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-guard r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-guard r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional

## Trajectories

### generation_s__agentic__none r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:117` `String name = selected.getTeamName();`

### generation_s__agentic__none r2: passthrough, rejected at store, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1189` `String playerName = player.getTeamName();`
- `ApoMarioHighscore.java:118` `String playerName = player.getTeamName();`

### generation_s__agentic__none r3: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:121` `String name = best.getTeamName();`

### generation_s__agentic__none r4: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:91` `String name = selected.getTeamName();`

### generation_s__agentic__none r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:79` `String name = best.getTeamName();`

### generation_s__agentic__static-guard r1: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:73` `String name = normalizeName(player.getTeamName());`

### generation_s__agentic__static-guard r2: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1239` `String runName = runPlayer.getTeamName();`
- `ApoMarioHighscore.java:78` `String name = validName(player.getTeamName());`

### generation_s__agentic__static-guard r3: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:76` `String name = player.getTeamName();`

### generation_s__agentic__static-guard r4: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:70` `String name = selected == null ? null : selected.getTeamName();`

### generation_s__agentic__static-guard r5: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:100` `String name = validName(player.getTeamName());`

### generation_s__agentic__static r1: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:69` `String name = validName(level.getPlayers().get(0).getTeamName());`

### generation_s__agentic__static r2: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:76` `String name = normalizeName(selected.getTeamName());`

### generation_s__agentic__static r3: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:130` `String name = normalizeName(player.getTeamName());`

### generation_s__agentic__static r4: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:69` `String name = selected.getTeamName();`

### generation_s__agentic__static r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:75` `String name = validName(player.getTeamName());`

### reuse_sb__agentic__none r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:48` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:77` `String name = selected.getTeamName();`

### reuse_sb__agentic__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:33` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:92` `storeRun(best.getPoints(), level.getPassedTime(), best.getTeamName());`

### reuse_sb__agentic__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:38` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:100` `String name = best.getTeamName();`

### reuse_sb__agentic__none r4: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:34` `String name = playerName == null ? "Unknown" : playerName.trim();`
- `ApoMarioHighscore.java:77` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:79` `name = best.getAi() == null ? "Human" : best.getAi().getTeamName();`

### reuse_sb__agentic__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:52` `String name = playerName == null ? "Unknown" : playerName.trim();`
- `ApoMarioHighscore.java:111` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:113` `name = selected.getAi().getTeamName();`

### reuse_sb__agentic__static-guard r1: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:97` `String candidate = player.getTeamName();`

### reuse_sb__agentic__static-guard r2: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:114` `String candidate = validName(selected.getTeamName());`

### reuse_sb__agentic__static-guard r3: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:132` `name = selected.getTeamName();`

### reuse_sb__agentic__static-guard r4: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:139` `String candidateName = selected.getTeamName();`

### reuse_sb__agentic__static-guard r5: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:116` `name = selected.getTeamName();`
- `ApoMarioHighscore.java:144` `return name == null ? "Player" : name;`

### reuse_sb__agentic__static r1: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:92` `String candidate = validName(selected.getTeamName());`

### reuse_sb__agentic__static r2: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:128` `String candidateName = selected.getTeamName();`

### reuse_sb__agentic__static r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:86` `String name = best == null ? "Player" : best.getTeamName();`
- `ApoMarioHighscore.java:87` `if (normaliseName(name) == null) name = "Player";`

### reuse_sb__agentic__static r4: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:106` `name = selected.getTeamName();`

### reuse_sb__agentic__static r5: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:65` `name = selected.getTeamName();`
