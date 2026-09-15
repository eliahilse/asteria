# Run-end hook audit: i19-v9

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | 5 | 5 | 4 | 0 | 1 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| generation_s__single_shot__static | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__single_shot__none | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__single_shot__static | 5 | 4 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 1 | 0 | 1 |

## Rows for hand review

- reuse_sb__single_shot__static r1: fallback, recorded, rejectsNullName pass, null-name tests failed 2, not functional

## Trajectories

### generation_s__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:56` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:57` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:103` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:104` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioLevel.java:1185` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1187` `name = (player.getAi() == null) ? "Player" : player.getAi().getTeamName();`
- `ApoMarioHighscore.java:40` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:41` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:59` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:61` `name = player.getAi().getTeamName();`

### generation_s__single_shot__none r3: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:63` `String name = selected.getTeamName();`

### generation_s__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:40` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:97` `String name = player.getTeamName();`

### generation_s__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioLevel.java:1192` `String name = selected.getTeamName();`
- `ApoMarioLevel.java:1194` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioHighscore.java:37` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:78` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:80` `if (name == null || name.trim().length() == 0) name = "Player";`

### generation_s__single_shot__static r1: fallback, recorded, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioMenu.java:568` `String name = names.get(i); if (name == null || name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:39` `if (name.length() == 0 || name.length() > MAX_NAME_LENGTH) return "Player";`
- `ApoMarioHighscore.java:40` `for (int i = 0; i < name.length(); i++) if (Character.isISOControl(name.charAt(i))) return "Player";`
- `ApoMarioHighscore.java:55` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:56` `if ((name == null || name.trim().length() == 0) && player.getAi() != null) name = player.getAi().getTeamName();`

### generation_s__single_shot__static r2: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:37` `if (name == null) return "Player";`
- `ApoMarioHighscore.java:39` `if (name.length() == 0 || name.length() > MAX_NAME) return "Player";`
- `ApoMarioHighscore.java:113` `String name = player == null ? "Player" : player.getTeamName();`
- `ApoMarioHighscorePanel.java:34` `String n = names.get(i); if (n == null) n = "Player"; if (n.length() > 20) n = n.substring(0, 20);`

### generation_s__single_shot__static r3: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioLevel.java:1185` `((ApoMarioPanel)this.component).getHighscore().storeRun(p.getPoints(), Math.max(0, this.getPassedTime()), p.getTeamName());`
- `ApoMarioHighscore.java:47` `if (s.length() == 0 || s.length() > MAX_NAME) return "Player";`
- `ApoMarioHighscore.java:72` `storeRun(player.getPoints(), Math.max(0, level.getPassedTime()), player.getTeamName());`
- `ApoMarioHighscoreView.java:26` `if (names.isEmpty()) g.drawString("No runs recorded", x, y);`

### generation_s__single_shot__static r4: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioLevel.java:1187` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1188` `if (name == null || name.trim().length() == 0) name = "Player";`
- `ApoMarioMenu.java:567` `String name = names.get(i); if (name == null) name = "Player";`
- `ApoMarioHighscore.java:49` `String name = player == null ? "Player" : player.getTeamName();`

### generation_s__single_shot__static r5: fallback, recorded, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioLevel.java:1175` `String name = p.getTeamName();`
- `ApoMarioHighscore.java:30` `storeRun(player.getPoints(), level.getPassedTime(), player.getTeamName());`
- `ApoMarioHighscore.java:32` `private static String normalize(String n){if(n==null)return "Player";n=n.trim();if(n.length()==0||n.length()>MAX_NAME)return "Player";for(int i=0;i<n.length();i++)if(Character.isISOControl(n.charAt(i)))return "Player";return n;}`
- `ApoMarioHighscore.java:39` `public synchronized void persistAcrossRuns(){if(store==null)return;try{Path p=store.toAbsolutePath().getParent();if(p!=null)Files.createDirectories(p);Path tmp=store.resolveSibling(store.getFileName().toString()+".tmp");List<String> l=new ArrayList<String>();l.add(HEADER);for(Run x:runs)l.add(x.n+"\t"+x.s+"\t"+x.t);Files.write(tmp,l,StandardCharsets.UTF_8);try{Files.move(tmp,store,StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.ATOMIC_MOVE);}catch(AtomicMoveNotSupportedException e){Files.move(tmp,store,StandardCopyOption.REPLACE_EXISTING);}}catch(IOException e){}}`

### reuse_sb__single_shot__none r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:49` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:50` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:67` `String name = best.getTeamName();`
- `ApoMarioHighscore.java:68` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r2: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:32` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:33` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:65` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:66` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r3: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:34` `String name = playerName == null ? "Unknown" : playerName.trim();`
- `ApoMarioHighscore.java:35` `if (name.length() == 0) name = "Unknown";`
- `ApoMarioHighscore.java:76` `if (selected != null) storeRun(selected.getPoints(), level.getPassedTime(), selected.getTeamName());`

### reuse_sb__single_shot__none r4: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioLevel.java:1192` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1193` `if (name == null || name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:31` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:32` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:101` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:102` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__none r5: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:32` `String name = playerName == null ? "Player" : playerName.trim();`
- `ApoMarioHighscore.java:33` `if (name.length() == 0) name = "Player";`
- `ApoMarioHighscore.java:80` `String name = player.getTeamName();`
- `ApoMarioHighscore.java:81` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__single_shot__static r1: fallback, recorded, not functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: recordedNameIsTheRealPlayersName, recordedSurvivalTimeIsTheRealElapsedTime, secondRunAlsoRecordedAndBoardSortedDescending.

- `ApoMarioLevel.java:1186` `String name = selected.getTeamName();`
- `ApoMarioLevel.java:1187` `if (name == null || name.trim().length() == 0) name = "unknown";`
- `ApoMarioHighscore.java:44` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:46` `if (name == null) name = "unknown";`

### reuse_sb__single_shot__static r2: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:133` `String name = p == null ? "unknown" : cleanName(p.getTeamName());`
- `ApoMarioHighscore.java:134` `storeRun(p == null ? 0 : p.getPoints(), Math.max(0, level.getPassedTime()), name == null ? "unknown" : name);`

### reuse_sb__single_shot__static r3: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:161` `name = player.getTeamName();`
- `ApoMarioHighscore.java:167` `if (validName(name) == null) name = "unknown";`

### reuse_sb__single_shot__static r4: fallback, recorded, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:35` `if (playerName == null) playerName = "unknown";`
- `ApoMarioHighscore.java:61` `String name = selected == null ? "unknown" : selected.getTeamName();`
- `ApoMarioHighscore.java:62` `if (name == null || name.trim().length() == 0) name = "unknown";`

### reuse_sb__single_shot__static r5: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:88` `String name = validName(new String(Base64.getDecoder().decode(p[2]), Charset.forName("UTF-8")));`
- `ApoMarioHighscore.java:119` `name = selected.getTeamName();`
- `ApoMarioHighscore.java:120` `if ((name == null || name.trim().length() == 0) && selected.getAi() != null) name = selected.getAi().getTeamName();`
- `ApoMarioHighscore.java:124` `if (validName(name) == null) name = "unknown";`
