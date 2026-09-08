# Why the operational resource result weakened

This post-collection review includes **all six** operational artifacts that failed
the I07 oversized-line fixture: Generation S repetitions 2 and 5, and Generation
S+F+B repetitions 2, 3, 4 and 5. Each observed failure is heap exhaustion. The
[manifest](source-review/manifest.json) retains the report hashes, original source
paths, exact source copies and load-method excerpts. No source, feedback or
outcome is changed, and this review adds no model sample or evaluation.

All six loaders call `BufferedReader.readLine()` without a preceding file-size
bound or a reader that limits the bytes/characters consumed for a line. They do
limit retained entries to 100 and normalize or limit names after reading. Those
later limits cannot bound the allocation of the first oversized physical line.
Four implementations read a complete header line before even entering their
record-count loop. The other two read the first record line in that loop.

| Artifact | First unbounded line read | Other bounds present |
| --- | ---: | --- |
| [Generation S r2](source-review/generation_s-r2-ApoMarioHighscore.java) | Line 93, header | 100 entries; name normalization capped at 48 characters |
| [Generation S+F+B r2](source-review/generation_sfb-r2-ApoMarioHighscore.java) | Line 133, header | 100 entries; name field checked after line splitting |
| [Generation S+F+B r3](source-review/generation_sfb-r3-ApoMarioHighscore.java) | Line 126, header | 100 records read; decoded name normalized afterward |
| [Generation S+F+B r4](source-review/generation_sfb-r4-ApoMarioHighscore.java) | Line 102, record | 100 entries; name normalized after line parsing |
| [Generation S r5](source-review/generation_s-r5-ApoMarioHighscore.java) | Line 86, header | 100 records; encoded field length checked afterward |
| [Generation S+F+B r5](source-review/generation_sfb-r5-ApoMarioHighscore.java) | Line 121, record | 100 records; name normalized after line splitting |

The [shared Generation insert](contexts/generation-operations.txt), HC-5,
explicitly proposes file-size, line and field bounds **before allocation**. The
source follows some nearby recommendations—record caps, name normalization,
structured records—while omitting the bound at the reading operation responsible
for the observed failure. A nonempty enforcement-point field is therefore not
evidence that generated code implements its stated safeguard.

This explains a concrete failure mechanism consistent with the recorded fixture
outcomes. It does not isolate why the model omitted the guard: context content,
length, placement, instruction interpretation and code sampling vary together.
It also does not convert retention or name-policy failures into demonstrated
resource vulnerabilities. The qualified counts and failed trajectories remain
exactly as originally reported.
