# Run-end hook audit: i34-notes

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__notes | 2 | 2 | 1 | 0 | 1 | 0 | 0 | 1 | 1 | 1 | 0 | 0 | 0 | 1 |
| generation_s__agentic__static-notes | 2 | 2 | 0 | 0 | 2 | 0 | 0 | 2 | 0 | 2 | 0 | 0 | 0 | 2 |
| reuse_sb__agentic__notes | 2 | 2 | 1 | 0 | 1 | 0 | 0 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| reuse_sb__agentic__static-notes | 2 | 2 | 0 | 0 | 2 | 0 | 0 | 2 | 0 | 2 | 0 | 0 | 0 | 2 |

## Rows for hand review

- generation_s__agentic__notes r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-notes r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-notes r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-notes r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static-notes r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional

## Trajectories

### generation_s__agentic__notes r1: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1187` `String name = player.getTeamName();`
- `ApoMarioLevel.java:1189` `name = player.getAi().getTeamName();`
- `ApoMarioLevel.java:1265` `String playerName = finishedPlayer == null ? "human" : finishedPlayer.getTeamName();`
- `ApoMarioHighscore.java:67` `String name = player == null ? null : player.getTeamName();`

### generation_s__agentic__notes r2: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:130` `playerName = selected.getTeamName();`

### generation_s__agentic__static-notes r1: passthrough, rejected at store, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:135` `String name = selected.getTeamName();`

### generation_s__agentic__static-notes r2: passthrough, rejected at store, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:78` `if (player != null && player.getTeamName() != null) {`
- `ApoMarioHighscore.java:79` `String teamName = player.getTeamName();`

### reuse_sb__agentic__notes r1: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:44` `String name = playerName == null ? "human" : playerName.trim();`
- `ApoMarioHighscore.java:96` `String name = chosen == null ? "human" : chosen.getTeamName();`

### reuse_sb__agentic__notes r2: passthrough, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: fail; failing functional checks: none.

- `ApoMarioHighscore.java:103` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:105` `name = selected.getAi().getTeamName();`

### reuse_sb__agentic__static-notes r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:93` `String name = selected.getTeamName();`

### reuse_sb__agentic__static-notes r2: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:134` `name = validName(selected.getTeamName());`
