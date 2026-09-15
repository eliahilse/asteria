# Where the functional cost of agent-acquired inserts comes from

Agent-acquired inserts (the I10 graph insert, the I11/I16/I16b compact insert,
the I10/I11 adaptive slices) lower failed issue checks further than the
researcher-written inserts of I09 but, unlike those, cost functional success.
This note locates that cost. Data: `hook-audit.md` and `hook-audit.csv` in each
round directory, produced by `research/hook_audit.py`.

## The conflict

The autonomous coupling tests end a run on the driver's human player. That
player has no AI, so `getTeamName()` returns null
(`corpus/java/ApoMario/_from_jar_ApoMario/apoMario/entity/ApoMarioPlayer.java:197`).
Two tests never set a name and require the run to be on the board afterwards:
`recordedSurvivalTimeIsTheRealElapsedTime` and
`secondRunAlsoRecordedAndBoardSortedDescending`
(`vamos-artifact/Tests/ApoMarioHighscoreAutonomousTest.java:86-112`). The
security contract asks the store to reject null names (`rejectsNullName`). An
artifact satisfies both only if the run-end hook substitutes a name before
calling the store. Every control artifact does that ("Player"); the
researcher-written requirements insert says "reject or safely normalize null,
blank, or overlong player names" and its artifacts do it too.

The agent-acquired inserts say something else at the hook. Generation
(data-flow angle, statement R2, failure behaviour): "If the level, player,
name, or elapsed-time source is unavailable or the mode is
replay/editor/simulation, skip recording and leave the existing highscore
unchanged." Reuse (C2): "obtain the selected human player's points/name and
elapsed time from the same level snapshot, and reject unavailable or
inconsistent values." Artifacts that follow this clause literally return from
the hook when the name is null, or pass the null name into a store that
rejects it. Either way the run is not recorded and exactly those two tests
fail.

## Counts

Trajectories whose final artifact fails functional checks and whose failing
checks are exactly the two null-name tests (of all completed trajectories in
the arm group):

| Arm group | Rounds | Trajectories | Only the null-name tests fail |
| --- | --- | ---: | ---: |
| No security context | I09, I10, I11, I16, I16b | 48 | 0 |
| Researcher-written inserts | I09 | 30 | 0 |
| Agent-acquired inserts and slices | I10, I11, I16, I16b | 46 | 12 |

Of the 19 agent-insert trajectories without a functional artifact, 12 fail
only these two tests; the other 7 also fail other checks (compile errors,
wrong player selection, missing menu wiring) and are outside this mechanism.
The 12 by round: I10 Generation S single-shot 3 of 3 non-functional, I10
Generation S agentic 2 of 4, I10 Reuse S+B single-shot 1 of 1, I11 adaptive 2
of 2, I16 Reuse S+B 2 of 2, I16b Generation S 2 of 2.

Hook mechanism among the 12 (regular-expression classes over the new lines
that read the live name, cited in each `hook-audit.md`): 10 return or filter
the player when the name is null, 2 pass the null name into a store that
rejects it. For example I16b Generation S static r4:

```java
if (p != null && p.getAi() == null && p.getTeamName() != null &&
        (selected == null || p.getPoints() > selected.getPoints())) {
```

and the functional artifact r3 of the same arm:

```java
String name = selected.getTeamName();
if (name == null || name.trim().length() == 0) name = "Player";
storeRun(selected.getPoints(), level.getPassedTime(), name);
```

## Reading

The functional cost is not a capability loss and not a security regression:
the artifacts implement the store contract and refuse a record the contract
calls invalid. It is a conflict between one insert clause and one fixture
property (a live player without a name) that the task contract never states.
Three consequences follow. First, the I09 result "no systematic functional
loss" holds for inserts that permit normalization; an insert that instructs
skipping at the hook trades functionality in 12 of 46 trajectories. Second,
the acquisition protocol should require failure behaviour that does not
contradict the functional contract of the change (record every completed
run), or the rendering should present requirements without failure clauses;
I17 tests the rendering variant. Third, the evaluator's coupling tests define
"real player name" for a player that has none; the contract could state the
expected fallback.

## Limits

Hook classes are regular-expression judgments; the test outcome columns are
authoritative and every cited line is printed. Rows where class and outcome
disagree are listed per round under "Rows for hand review"; their review is
recorded below when complete. One task, one repository pair, N = 3 to 5 per
arm, descriptive counts.
