# Run-end hook audit: i32a-s1

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__single_shot__static | 5 | 2 | 0 | 0 | 3 | 2 | 0 | 3 | 0 | 3 | 0 | 0 | 0 | 3 |
| reuse_sb__single_shot__static | 5 | 3 | 2 | 0 | 2 | 1 | 0 | 2 | 2 | 2 | 0 | 0 | 0 | 2 |

## Rows for hand review

- generation_s__single_shot__static r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, not functional
- generation_s__single_shot__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__single_shot__static r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__single_shot__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__single_shot__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional

## Trajectories

### generation_s__single_shot__static r1: passthrough, rejected at store, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioLevel.java:1202` `String playerName = selected.getTeamName();`

### generation_s__single_shot__static r2: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:176` `String name = player == null ? null : player.getTeamName();`

### generation_s__single_shot__static r3: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:100` `String name = player == null ? null : player.getTeamName();`

### generation_s__single_shot__static r4: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### generation_s__single_shot__static r5: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__static r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:80` `report("Rejected highscore with a null player name");`
- `ApoMarioHighscore.java:189` `String name = selected.getTeamName();`

### reuse_sb__single_shot__static r2: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:95` `String name = selected.getTeamName();`

### reuse_sb__single_shot__static r3: none, none, not functional

Final evaluated submission: None; compilation: None; rejectsNullName: None; failing functional checks: none.

- no new line reads the live player name

### reuse_sb__single_shot__static r4: fallback, recorded, not functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioPanel.java:569` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:121` `String fileName = store.getFileName() == null ? "highscores" : store.getFileName().toString();`
- `ApoMarioHighscore.java:122` `temporary = (parent == null ? Paths.get(fileName + ".tmp") : parent.resolve(fileName + ".tmp"));`

### reuse_sb__single_shot__static r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:177` `String name = selected.getTeamName();`
