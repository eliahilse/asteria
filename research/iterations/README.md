# Research iteration index

Every completed stage retains its inputs, unsuccessful outputs, diagnostics and
per-test observations. Model settings are requested but not independently
attested by the provider. All returned model identities are checked against Luna.

| Stage | What it measures | Evidence |
| --- | --- | --- |
| Original replay and overlay | 80 single-response paper-matrix requests, followed by 80 fresh requests on four selected paper-context combinations. | [Original results](../results/2026-09-08-original-results/overview.md) |
| I01 | 18 trajectories with up to three source-edit submissions; requirements and boundary contexts against fresh controls. | [Plan](i01-actionable-context/README.md), [original results](i01-actionable-context/results.md), [assessment](i01-actionable-context/assessment.md) |
| I01 environment correction | Re-measure all 28 evaluated submissions with isolated JVM homes. No model calls or source changes. | [Paired comparison](i01-home-isolated/README.md) |
| I02 | Six acquisitions using stable evidence IDs: five completed, one exhausted its budget because of an overly strict unknown-item validator. No feature generation. | [Acquisition trial](i02-context-acquisition/README.md) |
| I03 | 40 fresh trajectories: two paper-context cells × four security perspectives × five repetitions; corrected evaluation and six fresh context acquisitions. | [Frozen plan](i03-security-perspectives/README.md) |

Do not pool single responses with multi-submission trajectories or count the
same saved code's remeasurement as a new model sample. Context acquisition itself
is a separate unit: multiple code trajectories using one insert do not establish
independent replication of that context-generation strategy.

The latest completed result is selected by `current.json`; the local running
experiment is selected by `.local/iterations/active.json`. An earlier local table
can be opened with `/?iteration=i01-actionable-context` or `/?iteration=original`.
The Context generation tab accepts the same iteration query parameter.

Each final result includes compact quality metrics, every functional/security
check's denominator and diagnostics, and the exact prompt inserts. The full raw
history is in the [verified archives](../results/README.md). Each archive has a
second verified copy in `../asteria-research-backups` outside the repository.

## Next replication, declared during I03 collection

After saving I03, repeat all four security arms with six newly acquired contexts
and fresh code. Include all four paper-context cells selected by the completed
original screening: Generation S, Generation S+F+B, Reuse B and Reuse S+B.
Use five trajectories per combination and the same three-submission budget,
giving 80 trajectories. Retain overview even if it performs poorly in I03.

The evaluator, source-edit protocol, model request settings and acquisition
protocol remain fixed. This both repeats I03's two cells and extends the observed
matrix; it is not a clean independent replication of a pre-existing scientific
claim. Report repeated cells separately from newly covered cells. No code or
context acquisition receives prior security-test feedback. Complete the schedule
regardless of whether it supports the security hypothesis.
