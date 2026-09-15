# Run-end hook audit: i28-graph

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | 5 | 5 | 2 | 0 | 3 | 0 | 0 | 2 | 3 | 2 | 0 | 0 | 0 | 2 |
| generation_s__agentic__static | 5 | 5 | 0 | 0 | 5 | 0 | 0 | 5 | 0 | 5 | 0 | 0 | 0 | 5 |
| generation_s__agentic__static-ast | 5 | 5 | 0 | 0 | 5 | 0 | 0 | 5 | 0 | 5 | 0 | 0 | 0 | 5 |
| generation_s__agentic__static-ast-advise | 5 | 4 | 2 | 0 | 3 | 0 | 0 | 3 | 2 | 3 | 0 | 0 | 0 | 3 |
| generation_s__agentic__static-ast-guard | 5 | 5 | 1 | 0 | 4 | 0 | 0 | 4 | 1 | 4 | 0 | 0 | 0 | 4 |
| reuse_sb__agentic__none | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__agentic__static | 5 | 5 | 0 | 0 | 5 | 0 | 0 | 5 | 0 | 5 | 0 | 0 | 0 | 5 |
| reuse_sb__agentic__static-ast | 5 | 5 | 2 | 1 | 2 | 0 | 1 | 2 | 2 | 3 | 0 | 0 | 0 | 3 |
| reuse_sb__agentic__static-ast-advise | 5 | 5 | 1 | 1 | 3 | 0 | 1 | 3 | 1 | 4 | 0 | 0 | 0 | 4 |
| reuse_sb__agentic__static-ast-guard | 5 | 5 | 3 | 0 | 2 | 0 | 0 | 2 | 3 | 2 | 0 | 0 | 0 | 2 |

## Rows for hand review

- generation_s__agentic__none r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__none r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-ast-advise r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-ast-advise r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-ast-advise r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, not functional
- generation_s__agentic__static-ast-guard r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-ast-guard r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-ast-guard r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-ast-guard r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-ast r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-ast r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-ast r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-ast r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-ast r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-ast-advise r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-ast-advise r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-ast-advise r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-ast-advise r5: skip_on_null, skipped at hook, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-ast-guard r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-ast-guard r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-ast r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-ast r4: skip_on_null, skipped at hook, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-ast r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional

## Trajectories

### generation_s__agentic__none r1: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:108` `String name = best.getTeamName();`

### generation_s__agentic__none r2: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:75` `String name = player.getTeamName();`

### generation_s__agentic__none r3: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioLevel.java:1189` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:41` `String name = playerName == null ? "Unknown" : playerName.trim();`
- `ApoMarioHighscore.java:61` `String name = selected.getTeamName();`

### generation_s__agentic__none r4: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:91` `String name = selected.getTeamName();`

### generation_s__agentic__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:37` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:110` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:112` `name = selected.getAi().getTeamName();`

### generation_s__agentic__static-ast-advise r1: passthrough, rejected at store, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:53` `String name = player.getTeamName();`

### generation_s__agentic__static-ast-advise r2: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1197` `String name = player.getTeamName();`
- `ApoMarioMenu.java:554` `String name = names.get(i) == null ? "Unknown" : names.get(i);`
- `ApoMarioHighscore.java:78` `String name = player.getTeamName();`

### generation_s__agentic__static-ast-advise r3: passthrough, rejected at store, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1192` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:90` `String name = selected.getTeamName();`

### generation_s__agentic__static-ast-advise r4: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioMenu.java:589` `String name = names.get(i) == null || names.get(i).length() == 0 ? "Unknown" : names.get(i);`
- `ApoMarioHighscore.java:77` `String name = player.getTeamName();`

### generation_s__agentic__static-ast-advise r5: passthrough, rejected at store, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioHighscore.java:69` `String name = player.getTeamName();`

### generation_s__agentic__static-ast-guard r1: passthrough, rejected at store, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1202` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:215` `String name = player.getTeamName();`

### generation_s__agentic__static-ast-guard r2: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:82` `String playerName = player.getTeamName();`

### generation_s__agentic__static-ast-guard r3: fallback, recorded, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1200` `String playerName = selected.getTeamName();`
- `ApoMarioMenu.java:591` `String name = names.get(i) == null ? "Unknown" : names.get(i);`
- `ApoMarioHighscore.java:90` `String playerName = selected.getTeamName();`

### generation_s__agentic__static-ast-guard r4: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:49` `String name = player.getTeamName();`

### generation_s__agentic__static-ast-guard r5: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:68` `String name = player.getTeamName();`

### generation_s__agentic__static-ast r1: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:98` `String name = player.getTeamName();`

### generation_s__agentic__static-ast r2: passthrough, rejected at store, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1195` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:81` `String name = player.getTeamName();`

### generation_s__agentic__static-ast r3: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:111` `String name = selected.getTeamName();`

### generation_s__agentic__static-ast r4: passthrough, rejected at store, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1192` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:73` `String name = player.getTeamName();`

### generation_s__agentic__static-ast r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:62` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:64` `name = player.getAi().getTeamName();`

### generation_s__agentic__static r1: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1190` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:81` `String name = player.getTeamName();`

### generation_s__agentic__static r2: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:64` `String name = player.getTeamName();`

### generation_s__agentic__static r3: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1190` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:104` `String name = selected.getTeamName();`

### generation_s__agentic__static r4: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:103` `String playerName = player.getTeamName();`

### generation_s__agentic__static r5: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:130` `String name = player.getTeamName();`

### reuse_sb__agentic__none r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:57` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:182` `String name = best.getTeamName();`

### reuse_sb__agentic__none r2: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:44` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:73` `storeRun(player.getPoints(), level.getPassedTime(), player.getTeamName());`

### reuse_sb__agentic__none r3: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:43` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:116` `String name = best.getTeamName();`

### reuse_sb__agentic__none r4: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:47` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:48` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:70` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:71` `if ((name == null || name.trim().length() == 0) && selected.getAi() != null) name = selected.getAi().getTeamName();`

### reuse_sb__agentic__none r5: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:33` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:34` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:94` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:167` `this.name = name == null || name.length() == 0 ? "Player" : name;`

### reuse_sb__agentic__static-ast-advise r1: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:110` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:212` `String prefix = absoluteStore.getFileName() == null ? "apomario-highscore" : absoluteStore.getFileName().toString();`

### reuse_sb__agentic__static-ast-advise r2: passthrough, rejected at store, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:143` `String name = selected.getTeamName();`

### reuse_sb__agentic__static-ast-advise r3: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:78` `name = selected.getTeamName();`

### reuse_sb__agentic__static-ast-advise r4: passthrough, rejected at store, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:101` `name = player.getTeamName();`
- `ApoMarioHighscore.java:103` `name = player.getAi().getTeamName();`

### reuse_sb__agentic__static-ast-advise r5: skip_on_null, skipped at hook, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:104` `if (selected.getTeamName() != null && selected.getTeamName().trim().length() > 0) {`
- `ApoMarioHighscore.java:105` `name = selected.getTeamName().trim();`

### reuse_sb__agentic__static-ast-guard r1: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:216` `String name = selected == null ? null : selected.getTeamName();`
- `ApoMarioHighscore.java:217` `if (validName(name) == null) name = "Player";`

### reuse_sb__agentic__static-ast-guard r2: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:92` `String name = validName(selected.getTeamName());`
- `ApoMarioHighscore.java:93` `if (name == null) name = "Player";`

### reuse_sb__agentic__static-ast-guard r3: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:147` `String name = selected == null ? null : selected.getTeamName();`

### reuse_sb__agentic__static-ast-guard r4: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:142` `name = selected.getTeamName();`
- `ApoMarioHighscore.java:147` `if (validName(name) == null) name = "Player";`

### reuse_sb__agentic__static-ast-guard r5: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:91` `String name = validName(player.getTeamName());`

### reuse_sb__agentic__static-ast r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:86` `String name = validName(player.getTeamName()) ? player.getTeamName().trim() : "Player";`

### reuse_sb__agentic__static-ast r2: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:111` `String name = selected.getTeamName();`

### reuse_sb__agentic__static-ast r3: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:106` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:107` `if (!validName(name)) name = "Player";`

### reuse_sb__agentic__static-ast r4: skip_on_null, skipped at hook, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:127` `if (selected.getTeamName() != null && validName(selected.getTeamName())) {`
- `ApoMarioHighscore.java:128` `name = selected.getTeamName().trim();`

### reuse_sb__agentic__static-ast r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:131` `String name = selected.getTeamName();`

### reuse_sb__agentic__static r1: passthrough, rejected at store, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1199` `String name = result.getTeamName();`
- `ApoMarioHighscore.java:80` `String name = validName(player.getTeamName());`

### reuse_sb__agentic__static r2: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:159` `name = player.getTeamName();`

### reuse_sb__agentic__static r3: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:88` `String name = selected.getTeamName();`

### reuse_sb__agentic__static r4: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:69` `String name = chosen == null ? null : chosen.getTeamName();`

### reuse_sb__agentic__static r5: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:130` `String playerName = selected.getTeamName();`
