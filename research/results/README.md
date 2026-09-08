# Saved research iterations

Every snapshot contains exact prompts, responses, generated code, test outcomes,
context acquisitions, source snapshots, and provenance available at that point.
`manifest.json` hashes every file and the compressed archive. The same verified
archive is copied outside the repository to `../asteria-research-backups/<id>/`.
Provider connection settings, credentials, and raw service traces stay excluded.

| Snapshot | Contents | Summary |
| --- | --- | --- |
| [2026-09-08-original-results](2026-09-08-original-results/manifest.json) | All existing research evidence before overnight iterations; 1,471 files | [Original experiment findings](2026-09-08-original-results/overview.md) |

Verify the repository and external copies:

```sh
python3 -m research.archive_results verify --id 2026-09-08-original-results
```

Restore into the current checkout, without overwriting different files:

```sh
python3 -m research.archive_results restore --id 2026-09-08-original-results
cd workbench
npm run dev -- --port 5173
```

Use `--destination /path/to/empty/folder` to inspect a snapshot separately if
current files differ. The snapshot's `repositoryCommit` identifies the checkout
used at capture; archived Python tools are also available under
`.local/archive-tooling/<id>/`. Old evaluator versions must remain available when
reading frozen observations. Restoring a snapshot never submits model requests.

Each new iteration uses its own directory. Previous evidence is archived and
committed before starting another setup; no outcome is overwritten or dropped
because it is unfavorable. Development iterations and subsequent fresh replication
must be reported separately.
