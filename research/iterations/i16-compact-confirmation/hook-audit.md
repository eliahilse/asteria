# Run-end hook audit: i16-compact-confirmation

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| generation_s__single_shot__static | 5 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__single_shot__none | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__single_shot__static | 5 | 3 | 3 | 2 | 0 | 0 | 2 | 0 | 3 | 2 | 2 | 2 | 2 | 0 |

## Rows for hand review

- none

## Trajectories

### generation_s__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:50` `String name = playerName == null ? "Unknown" : playerName.trim();`
- `ApoMarioHighscore.java:51` `if (name.length() == 0) name = "Unknown";`
- `ApoMarioHighscore.java:87` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:88` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:42` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:91` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:93` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:93` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:94` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioPanel.java:232` `String name = best.getTeamName();`
- `ApoMarioPanel.java:233` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:29` `String name = playerName == null ? "Unknown" : playerName.trim();`
- `ApoMarioHighscore.java:30` `if (name.length() == 0) name = "Unknown";`
- `ApoMarioHighscore.java:57` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:58` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:127` `if (names.isEmpty()) g.drawString("No runs recorded yet", x + 18, row);`

### generation_s__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:40` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:77` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:79` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__static r1: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r2: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:40` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:41` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:92` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:93` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:32` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:33` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:65` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:66` `if (name == null || name.trim().length() == 0) name = "Human";`

### reuse_sb__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:36` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:37` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:59` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:60` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:41` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:77` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:78` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:33` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:34` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:45` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:46` `if ((name == null || name.trim().length() == 0) && player.getAi() != null) name = player.getAi().getTeamName();`

### reuse_sb__single_shot__static r1: skip_on_null, skipped at hook, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:65` `if (selected == null || selected.getTeamName() == null) return;`
- `ApoMarioHighscore.java:67` `if (storeRun(selected.getPoints(), elapsed, selected.getTeamName())) persistAcrossRuns();`

### reuse_sb__single_shot__static r2: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:144` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:145` `if (name == null || name.trim().length() == 0) name = "Human";`

### reuse_sb__single_shot__static r3: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:45` `try{Path parent=store.toAbsolutePath().getParent();if(parent!=null)Files.createDirectories(parent);tmp=Files.createTempFile(parent,"apomario-highscore-",".tmp");DataOutputStream out=new DataOutputStream(Files.newOutputStream(tmp));out.writeInt(MAGIC);out.writeInt(VERSION);out.writeInt(rows.size());for(Row x:rows){out.writeUTF(x.name);out.writeInt(x.score);out.writeInt(x.time);}out.close();Files.move(tmp,store,StandardCopyOption.REPLACE_EXISTING);}catch(Exception e){if(tmp!=null)try{Files.deleteIfExists(tmp);}catch(IOException ignored){}}`
- `ApoMarioHighscore.java:47` `public synchronized void recordRunEnd(ApoMarioLevel level){if(level==null||level.getPlayers()==null||level.getPlayers().isEmpty())return;ApoMarioPlayer p=level.getPlayers().get(0);if(p==null)return;String n=p.getTeamName();if(n==null||n.trim().length()==0)n="Player";storeRun(p.getPoints(),level.getPassedTime(),n);}`

### reuse_sb__single_shot__static r4: skip_on_null, skipped at hook, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:94` `if (chosen == null || chosen.getTeamName() == null) return;`
- `ApoMarioHighscore.java:96` `if (storeRun(chosen.getPoints(), elapsed, chosen.getTeamName())) persistAcrossRuns();`

### reuse_sb__single_shot__static r5: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:65` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:66` `if (name == null || name.trim().length() == 0) name = "Player";`
