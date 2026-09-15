# Run-end hook audit: i18-nofb-reuse

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| reuse_sb__single_shot__none | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__single_shot__static | 5 | 4 | 4 | 0 | 1 | 0 | 0 | 1 | 4 | 1 | 1 | 1 | 1 | 0 |

## Rows for hand review

- none

## Trajectories

### reuse_sb__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:37` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:38` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:80` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:81` `if ((name == null || name.trim().length() == 0) && selected.getAi() != null) name = selected.getAi().getTeamName();`

### reuse_sb__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:34` `String name = playerName == null ? "Unknown" : playerName.trim();`
- `ApoMarioHighscore.java:35` `if (name.length() == 0) name = "Unknown";`
- `ApoMarioHighscore.java:57` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:58` `if (name == null || name.trim().length() == 0) name = "Human";`

### reuse_sb__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:30` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:31` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:108` `String name = best.getTeamName();`

### reuse_sb__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:32` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:33` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:77` `String name = selected.getTeamName();`

### reuse_sb__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:37` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:38` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:77` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:78` `if ((name == null || name.trim().length() == 0) && player.getAi() != null) name = player.getAi().getTeamName();`

### reuse_sb__single_shot__static r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:34` `public synchronized void persistAcrossRuns(){if(store==null)return;Path tmp=null;try{Path p=store.toAbsolutePath().getParent();if(p!=null)Files.createDirectories(p);tmp=store.resolveSibling(store.getFileName()+".tmp");BufferedWriter w=Files.newBufferedWriter(tmp,StandardCharsets.UTF_8);w.write(MAGIC);w.newLine();w.write(String.valueOf(runs.size()));w.newLine();for(Run r:runs){w.write(r.s+"\t"+r.t+"\t"+r.n);w.newLine();}w.close();try{Files.move(tmp,store,StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.ATOMIC_MOVE);}catch(AtomicMoveNotSupportedException e){Files.move(tmp,store,StandardCopyOption.REPLACE_EXISTING);}}catch(IOException e){if(tmp!=null)try{Files.deleteIfExists(tmp);}catch(IOException ignored){}}}`
- `ApoMarioHighscore.java:35` `public synchronized void recordRunEnd(ApoMarioLevel level){if(level==null||level.isBReplay()||level.getPlayers()==null)return;for(ApoMarioPlayer p:level.getPlayers())if(p!=null&&p.getAi()==null&&p.isBVisible()){String n=clean(p.getTeamName());if(n==null)n="Player";if(storeRun(p.getPoints(),level.getPassedTime(),n))persistAcrossRuns();return;}}`

### reuse_sb__single_shot__static r2: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:85` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:86` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__static r3: passthrough, rejected at store, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioHighscore.java:81` `String name = selected.getTeamName();`

### reuse_sb__single_shot__static r4: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:38` `if(store==null)return; Path tmp=store.resolveSibling(store.getFileName().toString()+".tmp");`
- `ApoMarioHighscore.java:56` `if(chosen==null)return; String n=name(chosen.getTeamName()); if(n==null)n="Player";`

### reuse_sb__single_shot__static r5: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:75` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:76` `if (name == null || name.trim().length() == 0) name = "Player";`
