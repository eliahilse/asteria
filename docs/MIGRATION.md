# Migration from B

The in-progress bachelor-thesis workspace was copied from `B` into
[`eliahilse/asteria`](https://github.com/eliahilse/asteria) on 2026-09-07.
The original local workspace was retained. `B` itself had no Git history;
the destination's initial commit and license were retained.

The import includes the reports, audit prompts and transcripts, findings,
normalized corpus, proof-of-concept sources, scripts, and both experiment
directories, including their saved results. Python caches, macOS metadata,
and rebuildable `poc/*/build/` output were omitted.

## Upstream snapshots

These directories are included as ordinary files, with their existing notices
and documentation. Their nested Git metadata was omitted. Both source checkouts
matched the following revisions, apart from a local Python cache:

| Directory | Upstream | Revision |
| --- | --- | --- |
| `apogames/` | https://bitbucket.org/Jacob_Krueger/apogamessrc.git | `7b8c7973b59528447f93e803e9360de87e29e4d5` |
| `vamos-artifact/` | https://github.com/ieiris/llm-context-generation-reuse.git | `314df2ef5befacfb7f90aa3b0a514d1ea47662b7` |

Upstream authorship and licensing notices remain applicable to those files.

## Paths and reproduction

The top-level scripts now resolve the workspace relative to their own files.
The pilot's launch example uses the new checkout. Historical transcripts,
saved results, corpus paths, and meeting notes retain their original paths as
part of the research record. The external pilot backup mentioned in the meeting
notes was outside `B` and is not part of this import.

The pilot still depends on an external Atira checkout and its configured
environment; set `ATIRA_REPO` to that checkout when running it. No experiments
or model calls were rerun as part of the migration, and saved results were not
regenerated.
