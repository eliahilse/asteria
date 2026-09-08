# Saved research iterations

Every snapshot contains exact prompts, responses, generated code, test outcomes,
context acquisitions, source snapshots, and provenance available at that point.
`manifest.json` hashes every file and the compressed archive. The same verified
archive is copied outside the repository to `../asteria-research-backups/<id>/`.
Provider connection settings, credentials, and raw service traces stay excluded.

| Snapshot | Contents | Summary |
| --- | --- | --- |
| [2026-09-08-original-results](2026-09-08-original-results/manifest.json) | All existing research evidence before overnight iterations; 1,471 files | [Original experiment findings](2026-09-08-original-results/overview.md) |
| [i01-prepared](i01-prepared/manifest.json) | Frozen I01 plan and four fresh acquisitions before code collection; 226 files | [Protocol](../iterations/i01-actionable-context/README.md) |
| [i01-complete](i01-complete/manifest.json) | All 18 trajectories, including rejected submissions and intermediate evaluations; 628 files | [Results](../iterations/i01-actionable-context/results.md), [setup assessment](../iterations/i01-actionable-context/assessment.md) |
| [i01-home-isolated](i01-home-isolated/manifest.json) | All 28 saved submissions re-evaluated with separate JVM homes; 1,031 files | [Paired comparison](../iterations/i01-home-isolated/README.md) |
| [i02-context-acquisition](i02-context-acquisition/manifest.json) | Six acquisitions, including the budget-exhausted context and all earlier candidates; 1,493 files | [Acquisition trial](../iterations/i02-context-acquisition/README.md) |
| [i03-prepared](i03-prepared/manifest.json) | Frozen 40-trajectory schedule, six fresh contexts and calibrated v3 evaluator; 1,654 files | [Protocol](../iterations/i03-security-perspectives/README.md) |
| [i03-complete](i03-complete/manifest.json) | All 40 trajectories and 68 code submissions, plus readable reports and exports; 9,094 files | [Findings](../iterations/i03-security-perspectives/findings.md), [per-test analysis](../iterations/i03-security-perspectives/analysis.md) |
| [i04-prepared](i04-prepared/manifest.json) | Frozen 80-trajectory replication and six new acquisitions; 9,279 files | [Protocol](../iterations/i04-matrix-replication/README.md) |

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

I03's archive was restored in a clean detached checkout and reconstructed the
saved 40-trajectory table, 216 per-test rates and 60 issue comparisons exactly.
See [restore proof](../iterations/i03-security-perspectives/restore-proof.json).
The outside `i03-complete/review/` folder also provides directly readable CSV,
XLSX, PDF, source excerpts and a per-file SHA-256 manifest.

Each new iteration uses its own directory. Previous evidence is archived and
committed before starting another setup; no outcome is overwritten or dropped
because it is unfavorable. Development iterations and subsequent fresh replication
must be reported separately.
