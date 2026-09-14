# Level import: a second feature task for ApoMario (prepared, not run)

Status: preparation only. Nothing in this directory has been executed against a
model, no harness file outside this directory was changed, and no results
exist. The files here are a rationale, two task texts in the Highscore task
structure, a check sketch, and a read-only format walker.

| File | Content |
| --- | --- |
| `README.md` | Rationale, threat model, current level pipeline, donor choice, alternatives, harness changes, open questions |
| `task-generation.txt` | Task text for the Generation method (structure of `research/security_followup.py:acquisition_task`) |
| `task-reuse.txt` | Task text for the Reuse method with ApoSkunkman as donor |
| `CHECKS.md` | Functional tiers (unit / invoked / autonomous) and a security protocol sketch with fixtures and Safe/Weak controls |
| `mar_walk.py` | Walks `.mar` files with the `ApoMarioEditorIO` layout; used to verify the format facts below |

## 1. Why level import

The Highscore study exercises one code path: a locally persisted record store
that the game reads at start-up (record validation, retention bounds, malformed
stores, native deserialization). Going "vertical" means a second feature on the
same target and under the same threat model, but on code paths the Highscore
checks cannot reach. Level import does that:

- Same threat model. `docs/REPORT.md` section 2 lists a malicious local file
  (vector 2), hostile user-generated content (vector 3) and local tampering
  (vector 4). An imported level file is all three at once, and the shared level
  server that once distributed such files is the reason vector 3 exists.
- Different code paths. Import touches path handling (a user-chosen source, a
  level name that later selects a file), a hand-rolled binary parser whose
  header drives a two-dimensional allocation, directory listing, and writes
  into a directory that ships with the game, including overwrite and
  symbolic-link handling. None of these are covered by the eleven Highscore
  properties.
- The corpus audit already contains the flaw shapes this feature would either
  repeat or avoid (`docs/FINDINGS_LIST.md`): #7 (`ApoCheatingLoadSave.readLevel`
  allocates from file bytes, PoC 2), #8 (malformed downloaded levels crash
  hand-rolled parsers) and #9 (the replay-controlled level filename escapes the
  levels directory in `ApoMarioPanel`). The same shapes exist in ApoMario's own
  level reader (section 3), so both methods start from code that already has
  the weakness.
- It stays testable offline. Unlike level download, no server is needed; the
  fixtures are files.

### Threat model to CWE mapping

| Vector (REPORT section 2) | What the feature does with it | CWE |
| --- | --- | --- |
| Level name from a replay or menu selection used to locate a file | `resolveLevel(name)`; today `ApoMarioPanel.loadReplay` concatenates the replay's line onto `user.dir/levels/` | CWE-22 |
| Untrusted file structure (header, tile stream, enemy list) | validation before the game's reader runs | CWE-20 |
| Width and height read from the file drive `new ApoMarioEntity[height][width]` | reject out-of-range dimensions without allocating | CWE-789 / CWE-770 |
| Arbitrary file size handed to a copy or a full read | size bound before parsing, streamed walk | CWE-400 |
| Import target already exists, or is a symbolic link | never overwrite; never follow a link on the write side | CWE-59 and an explicit overwrite requirement |

The current corpus findings are cited as leads for what to check, not as claims
about what a model will do; the study's question remains whether acquired
security context changes the outcome on these fixtures.

## 2. What the ApoMario level pipeline looks like today

Sources: `corpus/java/ApoMario/_from_jar_ApoMario/` (identical `.java` members
are inside `apogames/Java/ApoMario/ApoMario.jar`, which is what the acquisition
and paper harness attach). Line numbers refer to the corpus copies.

Entry points and file dialogs

- `apoMario/ApoMarioComponent.java:101-116` (`init`): three `JFileChooser`s,
  guarded by `!ApoConstants.B_APPLET`; the editor chooser uses
  `ApoFileFilter("mar")` and starts in `user.dir/levels/`. `IntegrationDriver`
  sets `B_APPLET = true`, so no chooser exists in tests.
- `apoMario/game/ApoMarioPanel.java:251-260` `loadEditorLevel()`: open dialog,
  then `editorIO.readLevel(path)` on whatever path was chosen, returns true;
  `ApoMarioMenu.excecuteFunction` (`FUNCTION_EDITORLOAD`, lines 378 and 457)
  then calls `setGame(false)`. So loading an arbitrary `.mar` from disk already
  exists; it is dialog-only and copies nothing.
- `ApoMarioPanel.java:264-272` `saveEditorLevel()`: save dialog, appends `.mar`
  if missing, `editorIO.writeLevel(path)` to any path.
- `ApoMarioPanel.java:274-282` `loadReplay()`: loads a `.rep`; when the replay
  names a level, reads `System.getProperty("user.dir") + File.separator +
  "levels" + File.separator + replay.getLevelString()` (line 279). The name
  comes from `ApoMarioReplay.loadLevel(BufferedReader)` line 310
  (`this.levelString = reader.readLine()` when the random seed is -1). This is
  finding #9.
- No level list exists in the menu. `ApoMarioMenu` offers "start" (random level
  via `ApoMarioPanel.newLevel`), "editorLoad" (the dialog), replay load, editor,
  options, simulation, credits. Buttons are a fixed `ApoButton[38]` array in
  `apoMario/game/ApoMarioButtons.java` with visibility masks in
  `ApoMarioConstants.BUTTON_MENU` etc.; a new menu entry means extending both.

The `.mar` reader actually in use

- `apoMario/game/panels/ApoMarioEditorIO.java` (extends `org.apogames.ApoIO`).
  `readLevel(String)` (line 45) keeps the file name as `levelString`.
  `readLevel(DataInputStream)` (lines 51-133): `width = readInt()`,
  `height = readInt()` (53-54), then
  `level.setLevelEntities(new ApoMarioEntity[height][width])` (57), the
  allocation from untrusted dimensions. Tiles are read row by row from
  `height - 1` down to 0; per tile an `int` type and a type-specific payload.
  Then `makeGroundCorrect`, an enemy count, enemy records, `setPlayerAndFinish`,
  `setLevelInt(-1)`, `setLevelString(width, levelString)`. The whole body is in
  `catch (Exception ex)` (line 129) that prints and continues:
  `NegativeArraySizeException` is swallowed, `OutOfMemoryError` is an `Error`
  and escapes.
- `org/apogames/ApoIO.java:210-235` `readLevel(boolean, String, boolean)`:
  `FileInputStream` on the given name, calls the abstract reader, swallows
  `EOFException` (227) and returns true (234) whatever the reader did; only an
  `IOException` returns false. A truncated file therefore "loads".
- `apoMario/level/ApoMarioLevel.java`: `makeLevel` (275-352) fixes
  `height = 15` (336) and allocates (337); `makeEmptyLevel(width, height,
  difficulty)` allocates at 250; `restart(false)` (1124) calls
  `makeLevel(levelInt, true, false, -1, -1)`, and when `levelInt == -1` (a
  loaded level) the branch at 286 keeps `levelEntities` and re-initialises
  entities and players, so restarts after a death keep a loaded level.
  `setLevelString(width, name)` (1146) records the width and tells the replay
  the level's file name; `newLevel()` clears it. `getHeight()` is not updated by
  the editor reader, so a file with a height other than 15 leaves
  `levelEntities.length` and `getHeight()` inconsistent; the task text pins
  height to 15 for that reason. The constructor needs
  `component.getImages()`, so no `ApoMarioLevel` exists without an
  `ApoMarioComponent`.
- `apoMario/level/ApoMarioLevelIO.java` is a second reader with a four-int
  header (width, height, difficulty, time) that calls `makeEmptyLevel` and thus
  allocates at `ApoMarioLevel:250`. It is referenced nowhere else in the jar
  sources, and its layout does not match the shipped files. Both readers are
  present in the paper contexts (108 classes each), so a model can pick the
  wrong one; the task text names `ApoMarioEditorIO` explicitly.
- `org/apogames/help/ApoCopy.java` (`copyDirectory`, `copyFile`, lines 57-58)
  copies with `FileInputStream`/`FileOutputStream`, follows links and
  overwrites; it is an obvious reuse target for "copy into the levels
  directory".

The `.mar` format, verified

`mar_walk.py` walks a file with exactly the reads of
`ApoMarioEditorIO.readLevel(DataInputStream)`. On the eleven shipped files in
`apogames/Java/ApoMario/levels/`:

- Header: two big-endian `int`s, width then height. No magic number, version,
  length or checksum. Ten files are 100 x 15, `mario_first.mar` is 253 x 15.
- Tile record: `int` type code from `ApoMarioConstants` (observed
  0, 1, 2, 3, 4, 5, 7, 9), with payloads `WALL`: `boolean bTube`, and if true
  `int, int, boolean`; `QUESTIONMARKBOX`: `int`; `NO_GROUND_WALL`: `boolean`;
  `DESTRUCTIBLEBOX`: `boolean, int`; `CANNON`: `int, int`; others none.
- Enemy list: `int` count, then per enemy `int` type; type 1 (Gumba):
  `int x, int y, 4 booleans`; type 2 (Koopa): `int x, int y, 3 booleans`; type 0
  (what `writeLevel` emits for flowers) has no payload. Observed counts 0-57.
- Every shipped file ends exactly after the last enemy record (leftover 0
  bytes); sizes 6,131-16,213 bytes.

Because there is no header magic, "is this a level" can only be decided by the
structural walk, which is precisely where the CWE-20/789 checks apply.

## 3. Donor for the Reuse method

No game in `corpus/java/` implements "copy an external level file into the
levels directory and list that directory in the menu". The four games with a
"userlevels" list (ApoSimple, ApoSnake, ApoImp, ApoNotSoSimple) acquire levels
from `apo-games.de` over HTTP; the others load a chosen file directly. The
donor therefore supplies the nearest structural building blocks; listing,
validation and copying will be new code under both methods, and the reuse
ceiling is lower than for Highscore, where ApoIcarus had a complete feature.

Chosen donor: ApoSkunkman

- `corpus/java/ApoSkunkman/_from_jar_ApoSkunkman/apoSkunkman/game/ApoSkunkmanPanel.java:597-610`
  `loadLevel(String s, boolean bEditor)`: uses the given path when `s` is not
  null and only opens the editor chooser otherwise, then `editorIO.readLevel(path)`,
  `editor.makeEditorLevel()`, `editor.setEditorLevel()`, `makeBackground(...)`.
  A dialog-free load entry with a path argument is exactly what ApoMario lacks
  and what the invoked and autonomous tiers need.
- `ApoSkunkmanPanel.java:578-589` `saveLevel(String s)`: path or save dialog,
  appends the filter extension (`.skunk`), `editorIO.writeLevel(path)`.
- `apoSkunkman/game/ApoSkunkmanModelGame.java:165-175` `exitGame()`: writes
  `user.dir/levels/lastLevel.skunk` on quit, and
  `ApoSkunkmanPanel.loadStartProperties()` (102-112) reloads that file at
  start-up when the persisted `LEVEL_TYPE` says so. The game already writes
  into and reads from its own levels directory by a fixed name.
- `apoSkunkman/game/ApoSkunkmanEditorIO.java` (103 lines): an `ApoIO` subclass
  like `ApoMarioEditorIO`, reading into a pre-sized level array (`readInt`
  time, `readByte` coordinates and types); it does not allocate from the file,
  but it does not validate either.
- `apoSkunkman/ApoSkunkmanGameComponent.java:87-92`: the same
  `fileChooserEditor` rooted at `user.dir/levels/` as `ApoMarioComponent`.
- Distribution: `apogames/Java/ApoSkunkman/` is tracked with `ApoSkunkman.jar`
  (the `.java` members `ApoSkunkmanPanel.java`, `ApoSkunkmanEditorIO.java`,
  `ApoSkunkmanModelGame.java` are inside), `levels/` with six `.skunk` files,
  `replays/` and `skunkman.properties`, so
  `security_followup.repository_input` can copy it unchanged.
- Same author and family as ApoMario (`ApoIO`, `ApoFileFilter`, AI class
  loading, replay IO), which is the structural analogy the Reuse instructions
  ask the model to exploit.

Runner-up: ApoStarz

- `corpus/java/ApoStarz/.../apoStarz/game/ApoStarzGame.java:533-551`
  `levelLoad(String level)`: path or chooser, then `ApoStarzIO.readLevel(s)`
  (`apoStarz/game/ApoStarzIO.java:85-116`, returns a boolean, reads a text file
  of level strings, `addLevel` de-duplicates, `getLevel(int)`, `writeLevel`),
  and the loaded set replaces the playable set via `setLevelString`. Fixed
  set names live in `ApoStarzConstants.LEVELS` under `levels/`. Tracked as
  loose source with `levels/` (eight files). Closer to "a chosen file becomes
  the playable content, with a validating boolean reader", but text-based, no
  directory write, and a puzzle game whose level model differs from ApoMario.

Also considered: the "userlevels" games (`ApoSimpleUserlevelsLoad.load()`
lines 111-140 fetch `get_level.php` into memory, with a dedicated menu button
and list; server defunct); ApoBot, ApoSlitherLink, ApoIcejump and ApoDefence
(chooser then `loadLevel(s)` with fixed level lists); ApoRelax (loads an image
as a level); and ApoCheating, whose `ApoCheatingGamePanel` chooser feeds the
proven-vulnerable `ApoCheatingLoadSave.readLevel` (`new int[y][x]` at line
373, PoC 2). ApoCheating would make a propagation variant (does the model copy
the unchecked allocation from the donor?), which is a different question from
the context-effect question of the Highscore study and is not proposed here.

## 4. Alternatives considered

Achievements. The VAMOS artifact already contains this task for ApoMario
(`Features.csv` row, 16 `Prompts.csv` rows with ApoIcarus's
`ApoJumpStateAchievements` as donor, `ApoMarioAchievements{Test,
InvokedTest, AutonomousTest}.java`, entries in `task_config.json` and
`autonomous_runner.TASKS`). It is the cheapest second task to run because the
runners already know it. It was not preferred because its security surface is
the Highscore surface again: a locally persisted store (`achievements.dat`)
read at start-up, so the eleven properties would be re-labelled rather than
extended, and the code paths (record validation, retention, malformed store)
are the ones already measured. It remains a reasonable replication task if the
goal is breadth of features rather than breadth of code paths.

Level download from the shared server. ApoSimple, ApoSnake and ApoImp fetch
user levels from `apo-games.de` (`get_level.php`, plain HTTP) into an in-memory
list shown in a dedicated menu, which is the user-facing analogue of import and
would add the network vector (REPORT vector 1: no TLS, no integrity). It was
not preferred because the server is defunct, so an autonomous tier would need a
local stub server and network isolation that the harness does not have; the
corpus verdict (`docs/WORKING_DOC.md` section 7) already advises against using
this corpus for network-facing classes; and the parsing and path handling that
matter for security are the same as in local import, which is testable offline.

## 5. Proposed API

Declared in full in `task-generation.txt` and `task-reuse.txt`; summary:

- `apoMario.level.ApoMarioLevelImport(java.nio.file.Path levelsDirectory)`,
  created when missing; the live game passes `user.dir/levels`.
- `boolean importLevel(java.nio.file.Path source)`: validate, then copy under
  the source's own file name; false and nothing stored on malformed, oversized
  (> 1 MiB), symbolic-link, non-`.mar` name or existing name.
- `java.util.List<String> getLevelNames()`: sorted file names of regular
  `.mar` files, shipped and imported alike. (The requested
  `getImportedLevelNames()` was renamed: the directory holds shipped and
  imported levels together and the game has no bookkeeping to tell them apart;
  the unit tier starts from an empty directory, so the distinction is not
  needed for tests.)
- `java.nio.file.Path resolveLevel(String name)`: the path inside the
  directory, or null for unknown, traversing, absolute or non-regular names.
  This is the replacement for the concatenation at `ApoMarioPanel.java:279`.
- `boolean loadLevel(String name, apoMario.level.ApoMarioLevel level)`: load
  through `ApoMarioEditorIO`, false without throwing on failure. (The
  requested `ApoMarioLevel loadImported(String)` cannot exist: an
  `ApoMarioLevel` needs the component's image container.)
- On `apoMario.game.ApoMarioPanel`: `getLevelImport()`, `importLevel(Path)`,
  `playLevel(String)`, the last being the dialog-free counterpart of
  `loadEditorLevel()` followed by `setGame(false)`. The panel getter follows the
  `getHighscore()` lesson in `research/FEATURE_DELIVERY.md`.

The task text also defines "well-formed" concretely (bounds 100..1500 x 15,
tile and enemy payloads as read by `ApoMarioEditorIO`, exact end of file) and
requires validation without dimension-proportional allocation and without a
running game, so that the security probe can run the class in isolation, as
the Highscore probe does.

## 6. Harness changes that would be needed

Nothing below has been done. Paths are the files that currently hard-code the
Highscore task.

VAMOS pipeline (`vamos-artifact/Pipeline/`)

- `Features.csv`: a `LevelImport` row (description, detailed description as in
  the task texts, effort, reuse priority, "appears in" ApoSkunkman / ApoStarz
  as partial analogues, unit test skeleton from `CHECKS.md`).
- `Prompts.csv`: 16 rows, `Task = LevelImport`, `Method` Generation (8 context
  combinations, `Source = -`) and Reuse (8, `Source = ApoSkunkman`),
  `Target = ApoMario`. Proposed `Source Code` attachments:
  `ApoMario.ApoMarioPanel; ApoMario.ApoMarioMenu; ApoMario.ApoMarioEditorIO;
  ApoMario.ApoMarioComponent; ApoMario.ApoMarioLevel` (five, like
  Achievements). Proposed `Reuse Files`: `ApoSkunkman.ApoSkunkmanPanel;
  ApoSkunkman.ApoSkunkmanEditorIO; ApoSkunkman.ApoSkunkmanModelGame`.
- `task_config.json`: a `LevelImport` entry (`target_game` ApoMario,
  `generated_package` `apoMario.level`, `generated_dir` `apoMario/level`,
  `test_class` `ApoMarioLevelImportTest`, same `broken_import_patterns`).
- `invoked_runner.py`: `INVOKED` map entry, and the hard-coded
  `apoMario/game/panels` destination and `apoMario.game.panels.<test>` class
  name (lines 43-58, 80) need to become per-task like `autonomous_runner.TASKS`
  already is (`pkg_dir`, `gen_pkg`, `test_pkg`).
- `autonomous_runner.py`: `TASKS["LevelImport"]` with `pkg_dir apoMario/level`.
- `Tests/`: `ApoMarioLevelImportTest.java`, `ApoMarioLevelImportInvokedTest.java`,
  `ApoMarioLevelImportAutonomousTest.java` per `CHECKS.md`;
  `IntegrationDriver.java` gains a helper that locates the shipped `.mar`
  fixtures (the JVM runs with `cwd = <work>/run`, `levels/` is copied to
  `<work>/project`), for example through a system property set by the runners.
- Paper contexts: `vamos-artifact/Contexts/ApoMario_{Structural,Functional,
  Behavioral}.json` already cover all 108 classes including `ApoMarioEditorIO`,
  `ApoMarioLevelIO`, `ApoMarioLevel`, `ApoMarioPanel`, `ApoMarioMenu`,
  `ApoMarioComponent`, `ApoIO`, `ApoMarioReplay`, `ApoMarioButtons` and
  `ApoFileFilter`; no new S/F/B generation is needed for the target, and the
  paper design attaches no donor context.

Research harness (`research/`)

- `security_followup.py`: `acquisition_task` selects `Task == 'Highscore'` and
  hard-codes the ApoIcarus donor sentence; `repository_input` copies
  `['ApoMario'] + ['ApoIcarus']`. Both need a task/donor parameter.
- `paper_matrix.py`: `row['Task'] != 'Highscore'`, `STUDY_ID`, the "16
  conditions" assertion, and `source_attachment`, which reads jar members only
  for `ApoMario`; ApoSkunkman is jar-only, so the jar branch must be generalised
  to any `apogames/Java/<Game>/<Game>.jar`.
- `prepare_experiment.py:36`, `evaluate_response.py` (the `'Highscore'`
  literals at 145-151, the suite class names, the `ApoMarioHighscore*.java`
  glob and target class), `evaluate_security.py` (`PROTOCOL`, `CHECKS`,
  `SOURCES`, target class), `import_evidence.py:TEST_NAMES`,
  `functional_diagnostics.py:SUITES`, `amplification_audit.py`,
  `repeatability.py`, `failure_diagnostics.py`.
- `security/`: a `SecurityProbeLevelImport.java` (or a task switch in
  `SecurityProbe.java`), `SafeLevelImport.java`, `WeakLevelImport.java` as
  sketched in `CHECKS.md`.
- `context/policy.json` is `scope: Highscore` with Highscore-specific C4
  requirements; a `level-import-local-policy-v1` with path, size, dimension,
  link and overwrite rules is needed for the C4 arm, and the C1-C3 projection
  (currently file-name based) needs a Level-import projection over
  `ApoMarioEditorIO`, `ApoIO`, `ApoMarioPanel`, `ApoMarioComponent`,
  `ApoMarioReplay`, `ApoCopy`, `ApoFileFilter`.
- A new frozen study id and manifest under `research/studies/`, and workbench
  labels (`workbench/src/report-data.ts`, `main.tsx`, `context-generation.tsx`,
  `report-workbook.ts` reference Highscore names).

## 7. Open questions

1. Header: confirmed, two big-endian ints (width, height) with no magic,
   version, length or checksum; the shipped files follow `ApoMarioEditorIO`,
   not `ApoMarioLevelIO`. Whether the task should mention that
   `ApoMarioLevelIO` is unused, or leave the ambiguity as part of the task, is
   a design choice.
2. Height is pinned to 15 in the task text because `ApoMarioLevel.getHeight()`
   is not updated by the editor reader. If the study designers want the
   width/height bounds to come from the model instead, the security fixtures
   for dimensions still work, but the functional round trip needs a shipped
   level (all 15 high) rather than a generated one.
3. Package: the task places the class in `apoMario.level`; the invoked runner
   currently hard-codes `apoMario.game.panels`. Either parameterise the runner
   or move the class to `apoMario.game.panels`.
4. Levels directory at test time: `user.dir/levels` does not exist when JUnit
   runs; the task text makes the constructor create it. If the study wants the
   game to use the copied `<work>/project/levels`, the runners must set
   `user.dir` or pass the directory explicitly.
5. Menu integration is not automatically verifiable (as for Highscore); the
   "selectable in the menu" clause will be inspection-only unless a
   `getLevelNames`-backed menu getter is added to the contract.
6. Reuse ceiling: the donor provides load/save-by-path and the levels-directory
   write; listing, validation and copying are new under both methods. Reuse
   instruction 2 ("introduce new code only when no attached member can be
   reused") will be satisfied less often than in the Highscore task; record
   this as a property of the task, not as a model failure.
7. Probe environment: if a model validates by building a live level, the
   isolated probe cannot construct one (no component, headless). The task text
   forbids that dependency; the evaluator still needs an explicit
   `infrastructure_error` classification for such failures, separate from
   `fail`.
8. Symbolic-link fixtures depend on the file system and account; record the
   environment with every comparison as the Highscore protocol does, and treat
   link creation failure as `unknown`.
9. Whether to keep the dialog-based `loadEditorLevel()` alongside `playLevel`
   is left to the model; both can coexist, and tests never open the dialog.

## 8. Anything that makes level import unsuitable?

Nothing found. Two caveats: part of the feature already exists (loading an
arbitrary `.mar` through the dialog), so the new work is validation, copying,
listing, dialog-free selection and menu wiring rather than a whole feature; and
the format has no self-identification, so a naive "validate by loading through
the existing reader" implementation inherits the allocation-from-header and
swallowed-EOF behaviour of `ApoMarioEditorIO`/`ApoIO`. The second point is not
a problem for the study; it is the effect the security fixtures are designed to
observe.
