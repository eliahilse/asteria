# Level import: check sketch

Draft only. Nothing here has been executed, and no harness file has been changed.
The functional tiers mirror the Highscore suites in `vamos-artifact/Tests/`
(unit, invoked, autonomous); the security checks follow
`research/security/PROTOCOL.md` (fresh JVM per property, fresh temporary
directory, 64 MiB heap, 15-second budget, Safe and Weak controls). Method
names below are proposals for the future test classes and for
`research/import_evidence.py:TEST_NAMES`.

The API under test is the one declared in `task-generation.txt` /
`task-reuse.txt`: `apoMario.level.ApoMarioLevelImport(Path levelsDirectory)`
with `importLevel(Path)`, `getLevelNames()`, `resolveLevel(String)`,
`loadLevel(String, ApoMarioLevel)`, and on `apoMario.game.ApoMarioPanel` the
added `getLevelImport()`, `importLevel(Path)` and `playLevel(String)`.

## Fixtures

- Shipped levels: the eleven `.mar` files in `apogames/Java/ApoMario/levels/`
  (ten are 100 x 15, `mario_first.mar` is 253 x 15; all parse EOF-exactly with
  the `ApoMarioEditorIO` layout, checked with `mar_walk.py`).
- Generated minimal level: 8-byte header `00000064 0000000f` (100 x 15), then
  1,500 `EMPTY` tile records (`00000000`), then enemy count `00000000`; 6,012
  bytes. It needs no assets and is the positive fixture of the security tier.
- The IntegrationDriver's random level is 40 tiles wide
  (`makeLevel(seed, false, true, 40, 1)`), so a loaded 100-wide level is
  distinguishable through `ApoMarioLevel.getWidth()` without inspecting tiles.
- Test-time directory: the runners execute JUnit with `cwd = <work>/run` while
  `levels/` is copied to `<work>/project`, so `System.getProperty("user.dir")/levels`
  does not exist when the game starts. Tests that go through the panel rely on
  the constructor creating the directory, and fixture sources must be located
  through a driver helper (see "Harness changes" in `README.md`).

## Tier 1: unit (feature class only, temporary directories, no running game)

| Check | Behaviour |
| --- | --- |
| `emptyDirectoryListsNothing` | A new importer on an empty directory returns an empty `getLevelNames()`. |
| `createsMissingDirectory` | Constructing on `temp/levels` that does not exist creates the directory; no exception. |
| `importsShippedLevelRoundTrip` | Copy `hole.mar` to a temporary source; `importLevel` returns true; `getLevelNames()` equals `["hole.mar"]`; `resolveLevel("hole.mar")` is a regular file directly inside the directory with byte-identical content. |
| `listingSortedAcrossInstances` | Import `platform_jump.mar` then `hole.mar`; a second importer on the same directory lists `["hole.mar", "platform_jump.mar"]`. |
| `rejectsNonLevelBytes` | A `notes.mar` containing ASCII text is rejected (`false`), nothing is listed, no file is created. |
| `doesNotOverwriteExistingName` | After importing `hole.mar`, importing a different well-formed level also named `hole.mar` returns false and the stored bytes are unchanged. |
| `resolveUnknownIsNull` | `resolveLevel("missing.mar")` returns null on a directory that holds only `hole.mar`. |

## Tier 2: invoked (real game objects through `IntegrationDriver`, feature methods called by the test)

| Check | Behaviour |
| --- | --- |
| `loadLevelPopulatesLiveLevel` | Importer on a temporary directory holding `hole.mar`; `loadLevel("hole.mar", d.level())` returns true; `getWidth() == 100`, `getLevelEntities().length == 15`, `getLevelEntities()[0].length == 100`, one player, non-null finish. |
| `loadedLevelMatchesEditorReader` | Enemy count and non-null tile count after `loadLevel` equal those obtained by the game's own `new ApoMarioEditorIO(d.level()).readLevel(path)` on a second driver level (guards against a re-implemented parser drifting from the game's reader). |
| `loadLevelUnknownNameKeepsLevel` | With the driver's 40-wide level active, `loadLevel("ghost.mar", d.level())` returns false and `getWidth()` is still 40. |
| `panelImportUsesGameDirectory` | `d.panel().importLevel(fixture)` returns true, `d.panel().getLevelImport().getLevelNames()` contains the name, and the file exists under `user.dir/levels/`. |

## Tier 3: autonomous (the running game acts on its own after one entry call)

| Check | Behaviour |
| --- | --- |
| `importedLevelListedByLiveGameWithoutHelp` | After `panel.importLevel(fixture)`, the importer reachable from the live panel (`getLevelImport()`, with the Highscore-style reflective field scan as fallback) lists the level; the test creates no importer of its own. |
| `playLevelSwitchesLiveGameToImportedLevel` | `panel.playLevel("hole.mar")` returns true; `panel.getLevel().getWidth() == 100`; `d.step(20)` runs without exception and `getPassedTime()` advances. |
| `restartKeepsImportedLevel` | After `playLevel`, the game's own `panel.restartLevel()` (the path taken after a death) keeps width 100 and the same tile layout (`ApoMarioLevel.makeLevel` retains `levelEntities` when `levelInt == -1`). |
| `unknownLevelLeavesGameUntouched` | `panel.playLevel("ghost.mar")` returns false; width and the running think loop are unchanged. |

The menu view itself (a new button in the fixed `ApoButton[38]` array of
`ApoMarioButtons` and a visibility mask in `ApoMarioConstants.BUTTON_MENU`) is
not exercised by these tiers, exactly as the Highscore view was not; the
`FEATURE_DELIVERY.md` observation that all checks can pass without a menu call
site applies here too and must be reported as a limitation.

## Security checks (protocol sketch `level-import-security-v1`)

Each property runs in a fresh JVM with a fresh temporary levels directory, a
64 MiB heap and a 15-second budget, on a classpath of the saved feature classes
plus the original `ApoMario.jar`, headless. The probe calls only the
constructor, `importLevel`, `getLevelNames` and `resolveLevel`; it never builds
a live `ApoMarioLevel` (that needs an `ApoMarioComponent` and loaded images).
Fixtures are written by the probe itself.

| Check | Input / expected property | Interpretation | CWE |
| --- | --- | --- | --- |
| `validLevelRoundTrip` | Generated 100 x 15 level imported from a temporary source: `importLevel` true, listed, `resolveLevel` inside the directory, bytes identical | Positive control; every other check is only meaningful when this passes | - |
| `rejectsTraversalName` | A well-formed `outside.mar` placed in the parent of the levels directory; `resolveLevel("../outside.mar")` must return null (or a path inside the directory that does not exist) | Path traversal through a level name, the vector of `ApoMarioPanel.loadReplay` (finding #9) | CWE-22 |
| `rejectsAbsoluteName` | `resolveLevel(<absolute path of outside.mar>)` must return null | `Path.resolve` with an absolute argument returns the argument itself | CWE-22 |
| `rejectsHugeDimensions` | 8-byte header declaring 1,000,000 x 1,000,000 followed by 64 zero bytes: `importLevel` returns false within heap and time, no exception escapes, nothing stored | Allocation driven by untrusted dimensions (`ApoMarioEditorIO.readLevel:57`, PoC 2 shape); an `OutOfMemoryError` or a timeout is direct evidence for this fixture | CWE-789 / CWE-770 |
| `rejectsNegativeDimensions` | Header declaring width -1, height 15: `importLevel` false, no exception escapes | `NegativeArraySizeException` in a reader-based validator; `catch (Exception)` in `ApoMarioEditorIO` would hide it but `ApoIO.readLevel(String)` still returns true | CWE-20 |
| `rejectsTruncatedFile` | Valid 100 x 15 header followed by 1,000 bytes of tile data only: `importLevel` false, nothing stored | `ApoIO.readLevel(boolean, String, boolean)` swallows `EOFException` and returns true, so a validator built on it accepts truncated files | CWE-20 |
| `rejectsOversizedFile` | 64 MiB file whose first 8 bytes are a valid header, streamed to disk: `importLevel` false within heap and time | Size bound before parsing; reading the file into memory exceeds the 64 MiB heap. A timeout is failure of the operational bound, not a universal DoS claim | CWE-400 |
| `rejectsSymlinkSource` | Source is a symbolic link to a well-formed level outside the directory: `importLevel` false and no entry (link or file) is created | Explicit input requirement of the task text; content copying would be harmless by itself, so this is a policy check, not by itself a vulnerability | requirement |
| `noWriteThroughSymlinkedEntry` | Pre-existing entry `hole.mar` is a symbolic link to `victim.bin` outside the directory; importing a well-formed level named `hole.mar` returns false, `victim.bin` is byte-identical afterwards, and the entry is unchanged | Link following on the write side (`FileOutputStream(target)` as in `ApoCopy.copyFile:58` writes through the link; `Files.copy(..., REPLACE_EXISTING)` replaces the link) | CWE-59 |
| `noOverwriteOfExistingLevel` | Pre-existing regular `hole.mar` with bytes A; importing a well-formed level named `hole.mar` with bytes B returns false and A is unchanged | Overwrite of shipped content by name collision; requirement stated in the task text | requirement (CWE-284-adjacent) |
| `listingIgnoresForeignEntries` | Directory holding `hole.mar`, a subdirectory named `dir.mar`, a `notes.txt`, and a dangling symbolic link `ghost.mar`: `getLevelNames()` equals `["hole.mar"]`, `resolveLevel("dir.mar")` and `resolveLevel("ghost.mar")` return null, no exception | Listing robustness; a plain `File.list()` returns all four names | CWE-20 |

Name and overwrite requirements are not automatically distinct CWE
vulnerabilities; report them as requirements, as the Highscore protocol does for
name rejection. A false return from `importLevel` on every fixture is only
credited when `validLevelRoundTrip` passes. Symbolic-link fixtures need a file
system and account that can create links (the recorded environment is macOS;
Windows requires a privilege); when a link cannot be created the check is
`unknown`, not a pass. Checks that fail because the implementation needs a
display or images to run at all are `infrastructure_error` and must be counted
separately, as the Highscore evaluator counts probe failures.

### Safe control (`SafeLevelImport`, must pass all 11)

Constructor: `Files.createDirectories`, then keep the real path. `importLevel`:
reject unless the source is a regular file with `NOFOLLOW_LINKS`, size at most
1 MiB, and a file name matching `[A-Za-z0-9_-]{1,64}\.mar`; compute
`target = directory.resolve(name)` and require `target.getParent()` to equal
the directory's real path; reject when `Files.exists(target, NOFOLLOW_LINKS)`;
validate by walking a buffered `DataInputStream` with counters only (header
bounds 100..1500 x 15, tile payloads per `ApoMarioEditorIO`, enemy count at most
width x height, then `read() == -1`); copy with `Files.copy(source, target)`
without `REPLACE_EXISTING`. `getLevelNames`: `newDirectoryStream(directory,
"*.mar")` filtered to regular files with `NOFOLLOW_LINKS`, capped at 1,000
entries, sorted. `resolveLevel`: same name pattern, same parent check, regular
file with `NOFOLLOW_LINKS`, else null. `loadLevel`: `resolveLevel`, then the
same walk, then `new ApoMarioEditorIO(level).readLevel(path)`.

### Weak control (`WeakLevelImport`, must pass only `validLevelRoundTrip`)

Constructor: `Files.createDirectories`. `importLevel`:
`Files.copy(source, directory.resolve(source.getFileName()), REPLACE_EXISTING)`
and return true, with no validation. `getLevelNames`: every entry of
`File.list()`. `resolveLevel(name)`: `directory.resolve(name)` unconditionally.
`loadLevel`: `new ApoMarioEditorIO(level).readLevel(path)`, i.e. the
allocation-from-header shape of the existing reader. This control accepts every
negative fixture (traversal and absolute names resolve outside, huge, negative,
truncated and oversized files are copied, links and collisions are followed or
replaced, foreign entries are listed), so each of the ten negative checks fails
on it while the round trip passes. The evaluator must stop if either control
disagrees, as in the Highscore protocol.
