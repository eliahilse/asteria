# Results — Apo-Games security audit

_Auto-generated from the audit artifacts by `scripts/gen_report.py`. Do not hand-edit; regenerate._

## Coverage

- Corpus: **1686** `.java` files across the normalized variants.
- Raw findings from the two families: **109** (ox-alpha) + **80** (gpt-5.6-sol) = **189**, over **25** audited variant-runs.
- After grouping by (variant, file, category): **144** distinct issues, of which **41** are corroborated by both families.

## Category taxonomy

| Category | Variants affected | Corroborated | Single-source | Max clone spread |
|---|--:|--:|--:|--:|
| remote_code_loading | 18 | 13 | 8 | 17 |
| insecure_transport | 21 | 11 | 18 | 17 |
| unsafe_custom_parser | 19 | 9 | 17 | 17 |
| client_side_trust | 15 | 5 | 16 | 9 |
| resource_exhaustion | 21 | 1 | 27 | 17 |
| path_traversal | 3 | 1 | 2 | 2 |
| unsafe_native_call | 2 | 1 | 1 | 1 |
| weak_integrity_check | 9 | 0 | 9 | 17 |
| other | 3 | 0 | 3 | 1 |
| insecure_custom_parser_infrastructure | 1 | 0 | 1 | 17 |
| untrusted_deserialization | 1 | 0 | 1 | 1 |

_"Max clone spread" = the largest number of variants sharing a file (by basename) implicated in that category — an upper bound on how far one flaw reaches through reuse._

## Corroborated findings (both families, most severe first)

| Sev | Category | Variant | File | Clones | CWE | Title |
|---|---|---|---|--:|---|---|
| high | remote_code_loading | ApoRelax | ApoClassLoader.java | 17 | CWE-494 | ApoClassLoader loads and instantiates classes from arbitrary file/URL paths - pr |
| high | remote_code_loading | ApoIcejump | ApoIcejumpClassLoader.java | 1 | CWE-829 | Tampered icejump.properties auto-executes an arbitrary .class |
| high | remote_code_loading | ApoMario | ApoMarioClassLoader.java | 1 | CWE-829 | Startup property auto-instantiates arbitrary local bytecode |
| high | remote_code_loading | ApoSoccer | ApoSoccerClassLoader.java | 1 | CWE-829 | properties.txt can auto-execute an arbitrary AI class |
| high | unsafe_native_call | ApoSoccer | ApoSoccerAIJNI.java | 1 | CWE-114 | Desktop startup loads an unverified DLL from user.dir |
| high | remote_code_loading | TutorVolley | TutorVolleyClassLoader.java | 1 | CWE-829 | Working-directory class files execute before AI type checking |
| medium | remote_code_loading | ApoMarc | ApoClassLoader.java | 17 | CWE-494 | ApoClassLoader loads and instantiates classes from arbitrary local path or URL v |
| medium | insecure_transport | ApoMarc | ApoConstants.java | 17 | CWE-319 | Highscore submit/fetch over plain HTTP to apo-games.de with unauthenticated, ful |
| medium | insecure_transport | ApoNotSoSimple | ApoConstants.java | 17 | CWE-319 | Plain HTTP permits tampering with shared levels, uploads, and applet progress |
| medium | insecure_transport | ApoSimple | ApoConstants.java | 17 | CWE-319 | Highscores, shared levels, player names, and telemetry use plain HTTP |
| medium | remote_code_loading | ApoSimpleSudoku | ApoClassLoader.java | 17 | CWE-494 | Dead-code ApoClassLoader loads and instantiates classes from arbitrary file path |
| medium | insecure_transport | ApoSnake | ApoSnakeConstants.java | 2 | CWE-319 | Shared levels use unauthenticated cleartext HTTP |
| medium | unsafe_custom_parser | ApoSnake | ApoSnakePuzzleGame.java | 2 | CWE-20 | Malformed downloaded level strings crash the level decoder |
| medium | insecure_transport | ApoClock | ApoClockConstants.java | 1 | CWE-319 | Shared-level and leaderboard traffic uses unauthenticated HTTP |
| medium | unsafe_custom_parser | ApoClock | ApoClockPuzzleGame.java | 1 | CWE-20 | Downloaded level can set a zero clock interval and hang the update loop |
| medium | client_side_trust | ApoClock | ApoClockHighscoreLoad.java | 1 | CWE-602 | Online leaderboard accepts scores asserted entirely by the client |
| medium | insecure_transport | ApoDice | ApoDiceConstants.java | 1 | CWE-319 | Shared levels are downloaded and uploaded over cleartext HTTP |
| medium | unsafe_custom_parser | ApoDice | ApoDicePuzzleGame.java | 1 | CWE-20 | Downloaded level shorter than 64 characters crashes level loading |
| medium | insecure_transport | ApoMonoAndroid | ApoMonoConstants.java | 1 | CWE-319 | Shared levels are uploaded and downloaded over unauthenticated HTTP |
| medium | unsafe_custom_parser | ApoMonoAndroid | ApoMonoPuzzleGame.java | 1 | CWE-20 | Downloaded level strings reach fixed-position parsing without format validation |
| medium | insecure_transport | myTreasureAndroid | MyTreasureUserlevelsLoad.java | 1 | CWE-319 | Shared levels are downloaded and uploaded over plain HTTP |
| medium | unsafe_custom_parser | myTreasureAndroid | MyTreasureGame.java | 1 | CWE-20 | Malformed downloaded level crashes the unchecked level-string parser |
| medium | unsafe_custom_parser | ApoBot | ApoBotLevelIO.java | 1 | CWE-20 | Unbounded .bot fields drive allocations, rendering loops, and unchecked indices |
| medium | insecure_transport | ApoBot | ApoBotComponent.java | 1 | CWE-319 | Applet level packs are downloaded over unauthenticated HTTP |
| medium | resource_exhaustion | ApoCheating | ApoCheatingLoadSave.java | 1 | CWE-789 | Unchecked .cheat grid dimensions allow heap exhaustion |
| medium | unsafe_custom_parser | ApoSimple | ApoSimpleLevel.java | 1 | CWE-20 | Short downloaded level string terminates the main game loop when selected |
| medium | unsafe_custom_parser | ApoSlitherLink | ApoSlitherLinkLevel.java | 1 | CWE-20 | Malformed downloaded level becomes a negative-sized array |
| low | remote_code_loading | ApoBot | ApoClassLoader.java | 17 | CWE-470 | Unused helper can instantiate classes from caller-selected URLs |
| low | remote_code_loading | ApoCommando | ApoClassLoader.java | 17 | CWE-494 | Dormant helper loads and instantiates unverified classes |
| low | remote_code_loading | ApoNotSoSimple | ApoClassLoader.java | 17 | CWE-470 | Dormant URLClassLoader instantiates caller-selected code |
| low | remote_code_loading | ApoSimple | ApoClassLoader.java | 17 | CWE-829 | Dormant URLClassLoader can instantiate remote code but has no shipped call site |
| low | remote_code_loading | ApoSlitherLink | ApoClassLoader.java | 17 | CWE-470 | Dormant URL class instantiation helper is theoretical |
| low | insecure_transport | ApoSoccer | ApoImage.java | 17 | CWE-319 | Applet team artwork is fetched over plaintext HTTP |
| low | unsafe_custom_parser | ApoMarc | ApoHighscore.java | 9 | CWE-20 | Dormant high-score parser crashes or grows without bounds |
| low | path_traversal | ApoMario | ApoMarioPanel.java | 2 | CWE-22 | Replay-controlled level filename escapes the levels directory |
| low | client_side_trust | ApoDice | ApoDicePanel.java | 1 | CWE-602 | Editable SharedPreferences value unlocks every built-in level |
| low | client_side_trust | ApoMonoAndroid | ApoMonoSave.java | 1 | CWE-807 | Tampered solved-level preference directly controls progression gates |
| low | client_side_trust | myTreasureAndroid | MyTreasurePanel.java | 1 | CWE-807 | Locally editable skull count is trusted to unlock difficulty tiers |
| low | insecure_transport | ApoIcejump | ApoIcejumpNetwork.java | 1 | CWE-319 | Plain-HTTP public-IP lookup permits response spoofing |
| low | client_side_trust | ApoSimple | ApoSimpleProperties.java | 1 | CWE-602 | Editable properties file directly sets spendable coin balance |
| low | remote_code_loading | ApoSoccer | ApoSoccerClassLoaderURL.java | 1 | CWE-494 | Dormant applet loader defines classes from arbitrary URL bytes |

## Highest-severity single-source leads (need manual/PoC validation)

| Sev | Category | Variant | File | Family | Title |
|---|---|---|---|---|---|
| high | remote_code_loading | ApoPongBeat | ApoClassLoader.java | oxa | URLClassLoader loads and instantiates .class files from arbitrary loca |
| high | path_traversal | BitsEngineAndroid | BitsZip.java | oxa | Zip-Slip path traversal in BitsZip.extract(): ZipEntry names used unsa |
| medium | resource_exhaustion | ApoRelax | ApoHelp.java | codex | Unbounded HTTP line read can exhaust or hang the applet |
| medium | resource_exhaustion | ApoRelax | ApoImage.java | codex | Editor decodes full-size images before enforcing its size cap |
| medium | resource_exhaustion | ApoSimpleSudoku | ApoHelp.java | codex | Unbounded HTTP response line can hang or exhaust the game process |
| medium | insecure_transport | ApoSlitherLink | ApoConstants.java | codex | Shared-level downloads and uploads use cleartext HTTP |
| medium | insecure_transport | ApoStarz | ApoIO.java | codex | Applet highscore and level traffic uses unauthenticated HTTP |
| medium | insecure_transport | ApoIcarus | ApoConstants.java | oxa | All game-server traffic (highscore + achievements cookie) sent over pl |
| medium | unsafe_custom_parser | ApoNotSoSimple | ApoHighscore.java | codex | Partial five-line response nulls level state and crashes the game thre |
| medium | resource_exhaustion | ApoSlitherLink | ApoHighscore.java | codex | Unbounded HTTP response is retained until heap exhaustion |
| medium | client_side_trust | ApoIcarus | ApoHighscore.java | oxa | Highscore submission is unsigned and unauthenticated: leaderboard full |
| medium | weak_integrity_check | ApoNotSoSimple | ApoHighscore.java | oxa | Unauthenticated, unsigned level/solution submission to save_highscore. |
| medium | insecure_transport | ApoRelax | ApoHighscore.java | oxa | Cloned highscore client submits level blobs over plain HTTP to sibling |
| medium | insecure_transport | ApoSlitherLink | ApoHighscore.java | oxa | Highscore get/save exchanged over plain HTTP, spoofable by network att |
| medium | resource_exhaustion | ApoSnake | ApoSnakeUserlevelsLoad.java | codex | Unbounded shared-level response can exhaust memory and CPU |
| medium | resource_exhaustion | ApoMario | ApoMarioLevel.java | codex | Replay width drives a near-unbounded level allocation |
| medium | resource_exhaustion | ApoClock | ApoClockUserlevelsLoad.java | codex | Automatic level download consumes an unbounded response |
| medium | resource_exhaustion | ApoDice | ApoDiceUserlevelsLoad.java | codex | Unbounded level-feed response can exhaust application memory |
| medium | resource_exhaustion | ApoMonoAndroid | ApoMonoUserlevelsLoad.java | codex | Unbounded level-server response can exhaust heap and CPU |
| medium | resource_exhaustion | myTreasureAndroid | MyTreasureUserlevelsLoad.java | codex | Unbounded shared-level response can exhaust application memory |

## Deterministic sink census (context, not findings)

| Sink category | Total hits | Variants |
|---|--:|--:|
| file_io | 527 | 24 |
| network | 428 | 23 |
| weak_random | 385 | 23 |
| path_build | 284 | 22 |
| serializable | 140 | 20 |
| crypto | 83 | 13 |
| sysprops | 78 | 19 |
| classloader | 58 | 18 |
| reflection | 30 | 22 |
| applet | 22 | 17 |
| zip | 14 | 1 |
| native_code | 13 | 5 |
| deserialization | 8 | 1 |
| android_storage | 3 | 1 |
