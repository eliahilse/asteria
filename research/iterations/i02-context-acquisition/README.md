# I02: context acquisition trial

Six fresh repository/task-only acquisitions were attempted using stable evidence IDs. Five finished; Generation / boundaries exhausted its 16-turn budget. No feature-generation trajectories were collected under this setup. Every request, candidate and error is retained.

| Method | Strategy | Status | Turns | Final items |
| --- | --- | --- | ---: | ---: |
| Generation | overview | settings_unverified | 11 | 8 |
| Generation | requirements | settings_unverified | 12 | 10 |
| Generation | boundaries | budget_exhausted | 16 | 0 |
| Reuse | overview | settings_unverified | 10 | 11 |
| Reuse | requirements | settings_unverified | 12 | 8 |
| Reuse | boundaries | settings_unverified | 11 | 8 |

The failed acquisition repeatedly returned an explicitly unknown name source after searches found no matches. The validator incorrectly required a citation for every observed-basis item, including an unknown. The corrected rule allows uncited unknowns, labels them unverified, and still rejects uncited observed properties and existing-risk claims. Errors now identify the offending item. No model claim is promoted to a proven fact.

I03 acquires all six contexts afresh with the corrected protocol. Successful I02 outputs are preserved but are not selectively reused in I03. This is acquisition/setup development, not a security-effect result.

Full evidence: `research/results/i02-context-acquisition/evidence.tar.gz`. The separate verified copy is in the sibling `asteria-research-backups/i02-context-acquisition` folder.
