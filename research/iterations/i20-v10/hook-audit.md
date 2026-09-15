# Run-end hook audit: i20-v10

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| generation_s__single_shot__static | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__single_shot__none | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__single_shot__static | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |

## Rows for hand review

- none

## Trajectories

### generation_s__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:31` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:32` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:74` `String name = selected.getTeamName();`

### generation_s__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:35` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:36` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:67` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:68` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:72` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:73` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:84` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:86` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:39` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:40` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:76` `String name = best.getTeamName();`

### generation_s__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:42` `String name = playerName == null ? "Unknown" : playerName.trim();`
- `ApoMarioHighscore.java:80` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:81` `if ((name == null || name.trim().length() == 0) && selected.getAi() != null) name = selected.getAi().getTeamName();`
- `ApoMarioHighscore.java:82` `if (name == null || name.trim().length() == 0) name = "Human";`

### generation_s__single_shot__static r1: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:97` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:98` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__static r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:90` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:91` `if (name == null || name.trim().length() == 0) name = "Human";`

### generation_s__single_shot__static r3: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioMenu.java:542` `if (name == null || name.length() == 0) name = "Human";`
- `ApoMarioHighscore.java:49` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:50` `if (name == null || name.trim().length() == 0) name = "Human";`

### generation_s__single_shot__static r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:95` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:96` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__static r5: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:90` `String name = validName(player.getTeamName()) ? player.getTeamName().trim() : "Player";`

### reuse_sb__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioPanel.java:160` `String name = best.getTeamName();`
- `ApoMarioPanel.java:161` `if (name == null || name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:29` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:30` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:71` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:72` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:82` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:83` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:124` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:125` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:36` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:37` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:64` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:65` `if ((name == null || name.trim().length() == 0) && best.getAi() != null) name = best.getAi().getTeamName();`
- `ApoMarioHighscore.java:66` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:36` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:37` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:105` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:106` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:33` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:34` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:46` `String name = best.getTeamName(); if (name == null || name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:50` `try { Path parent=store.getParent(); if(parent!=null) Files.createDirectories(parent); BufferedWriter w=Files.newBufferedWriter(store, StandardCharsets.UTF_8); try { for(int i=0;i<names.size();i++){ w.write(scores.get(i)+"\t"+times.get(i)+"\t"+names.get(i).replace("\t"," ").replace("\n"," ")); w.newLine(); } } finally { w.close(); } } catch(IOException ignored) { }`

### reuse_sb__single_shot__static r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:123` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:124` `if (name == null || name.trim().length() == 0 || name.length() > MAX_NAME || !safeName(name)) name = "Player";`

### reuse_sb__single_shot__static r2: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:65` `String name = player == null ? "Player" : player.getTeamName();`
- `ApoMarioHighscore.java:66` `if (!validName(name)) name = "Player";`

### reuse_sb__single_shot__static r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:86` `String name = selected == null ? "Player" : selected.getTeamName();`
- `ApoMarioHighscore.java:87` `if (!validName(name)) name = "Player";`

### reuse_sb__single_shot__static r4: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:76` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:77` `if (name == null || name.trim().length() == 0 || name.length() > MAX_NAME_LENGTH || !safe(name.trim())) name = "Player";`

### reuse_sb__single_shot__static r5: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:66` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:67` `if (name == null || name.trim().length() == 0 || name.length() > MAX_NAME || !isSafe(name)) name = "Player";`
