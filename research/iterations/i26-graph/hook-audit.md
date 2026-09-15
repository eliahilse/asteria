# Run-end hook audit: i26-graph

For every trajectory, the final evaluated submission: functional outcome, failing functional checks, and the new lines that read the live player name. The two null-name coupling tests are recordedSurvivalTimeIsTheRealElapsedTime and secondRunAlsoRecordedAndBoardSortedDescending; the driver player has no AI, so its team name is null. Classes (regular-expression judgments over the cited lines; check them against the source): fallback = a literal name replaces a null or blank name; skip_on_null = the hook returns or filters the player when the name is null; passthrough = the live name is handed on unchanged; none = no new line reads the name. Fate of a run without a name = hook class joined with the final artifact's rejectsNullName outcome: skipped at the hook, rejected at the store (passed through into a store that rejects null names), or recorded.

Test columns are authoritative; the mechanism columns are aids, and rows where the two disagree are listed for hand review.

| Condition | N | Functional | fallback | skip_on_null | passthrough | none | Skipped at hook | Rejected at store | Recorded | Not recorded | ... and fail a null-name test | Fail a null-name test | Only null-name tests fail | Discordant |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| generation_s__agentic__ast | 2 | 2 | 0 | 0 | 2 | 0 | 0 | 2 | 0 | 2 | 0 | 0 | 0 | 2 |
| generation_s__agentic__none | 2 | 2 | 0 | 0 | 2 | 0 | 0 | 2 | 0 | 2 | 0 | 0 | 0 | 2 |
| generation_s__agentic__static | 2 | 2 | 1 | 0 | 1 | 0 | 0 | 1 | 1 | 1 | 0 | 0 | 0 | 1 |
| generation_s__agentic__static-ast | 2 | 2 | 1 | 0 | 1 | 0 | 0 | 1 | 1 | 1 | 0 | 0 | 0 | 1 |
| generation_s__agentic__static-ast-guard | 2 | 1 | 0 | 0 | 2 | 0 | 0 | 2 | 0 | 2 | 0 | 0 | 0 | 2 |
| generation_s__agentic__static-guard | 2 | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |

## Rows for hand review

- generation_s__agentic__ast r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__ast r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__none r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__none r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-ast-guard r1: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static-ast-guard r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, not functional
- generation_s__agentic__static-ast r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional
- generation_s__agentic__static r2: passthrough, rejected at store, rejectsNullName pass, null-name tests failed 0, functional

## Trajectories

### generation_s__agentic__ast r1: passthrough, rejected at store, functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:71` `String name = best.getTeamName();`

### generation_s__agentic__ast r2: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:95` `String name = player.getTeamName();`

### generation_s__agentic__none r1: passthrough, rejected at store, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:105` `String name = selected.getTeamName();`

### generation_s__agentic__none r2: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:119` `String name = player.getTeamName();`

### generation_s__agentic__static-ast-guard r1: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:80` `String name = selected.getTeamName();`

### generation_s__agentic__static-ast-guard r2: passthrough, rejected at store, not functional

Final evaluated submission: 3; compilation: pass; rejectsNullName: pass; failing functional checks: recordsRealScore, recordsRealSurvivalTime, recordsRealPlayerName, recordRunEndAddsExactlyOneEntry.

- `ApoMarioLevel.java:1208` `String name = player.getTeamName();`

### generation_s__agentic__static-ast r1: fallback, recorded, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioMenu.java:573` `String name = names.get(i) == null ? "Unknown" : names.get(i);`
- `ApoMarioHighscore.java:88` `String name = selected.getTeamName();`

### generation_s__agentic__static-ast r2: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:73` `String name = selected.getTeamName();`

### generation_s__agentic__static-guard r1: fallback, recorded, functional

Final evaluated submission: 1; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioMenu.java:557` `String name = names.get(i) == null ? "Unknown" : names.get(i);`
- `ApoMarioHighscore.java:110` `String name = player.getTeamName();`

### generation_s__agentic__static-guard r2: fallback, recorded, functional

Final evaluated submission: 5; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioMenu.java:592` `if (name == null || name.length() == 0) name = "Unknown";`
- `ApoMarioHighscore.java:61` `String name = player.getTeamName();`

### generation_s__agentic__static r1: fallback, recorded, functional

Final evaluated submission: 4; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioLevel.java:1203` `String name = selected.getTeamName();`
- `ApoMarioHighscore.java:66` `String name = selected.getTeamName();`
- `ApoMarioHighscoreView.java:29` `String name = names.get(i) == null ? "Unknown" : names.get(i);`

### generation_s__agentic__static r2: passthrough, rejected at store, functional

Final evaluated submission: 2; compilation: pass; rejectsNullName: pass; failing functional checks: none.

- `ApoMarioHighscore.java:109` `String name = selected.getTeamName();`
