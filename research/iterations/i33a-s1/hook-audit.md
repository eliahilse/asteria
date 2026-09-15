# Run-end hook audit: i33a-s1

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__static | 5 | 5 | 0 | 1 | 4 | 0 | 1 | 4 | 0 | 5 | 0 | 0 | 0 | 5 |
| reuse_sb__agentic__static | 5 | 5 | 2 | 0 | 3 | 0 | 0 | 3 | 2 | 3 | 0 | 0 | 0 | 3 |

## Rows for hand review

- generation_s__agentic__static r1: skip_on_null, skipped at hook, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r4: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static r3: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- reuse_sb__agentic__static r5: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional

## Trajectories

### generation_s__agentic__static r1: skip_on_null, skipped at hook, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:117` `if (selected.getTeamName() != null && selected.getTeamName().trim().length() > 0) {`
- `ApoMarioHighscore.java:118` `name = selected.getTeamName().trim();`

### generation_s__agentic__static r2: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:113` `String name = player.getTeamName();`

### generation_s__agentic__static r3: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:149` `name = selected.getTeamName();`

### generation_s__agentic__static r4: passthrough, rejected at store, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:127` `String name = player.getTeamName();`

### generation_s__agentic__static r5: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:164` `String name = player.getTeamName();`

### reuse_sb__agentic__static r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:167` `String name = cleanName(best.getTeamName());`
- `ApoMarioHighscore.java:168` `if (name == null) name = "Player " + (index + 1);`

### reuse_sb__agentic__static r2: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:164` `String name = result.getTeamName();`
- `ApoMarioHighscore.java:166` `name = result.getAi().getTeamName();`

### reuse_sb__agentic__static r3: passthrough, rejected at store, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1199` `String name = chosen.getTeamName();`
- `ApoMarioLevel.java:1201` `name = chosen.getAi().getTeamName();`
- `ApoMarioHighscore.java:204` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:206` `name = chosen.getAi().getTeamName();`

### reuse_sb__agentic__static r4: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:162` `String name = chosen.getTeamName();`
- `ApoMarioHighscore.java:163` `if (name == null || name.trim().length() == 0) name = "Player";`

### reuse_sb__agentic__static r5: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:213` `String name = player.getTeamName();`
