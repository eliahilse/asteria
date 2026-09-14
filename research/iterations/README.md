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
| I03 | 40 fresh trajectories: two paper-context cells × four security perspectives × five repetitions; corrected evaluation and six fresh context acquisitions. | [Findings](i03-security-perspectives/findings.md), [per-test analysis](i03-security-perspectives/analysis.md), [workbook](i03-security-perspectives/results.xlsx) |
| I04 | 80 fresh trajectories across all four originally selected paper-context cells, with the same four security arms and six new acquisitions. | [Findings](i04-matrix-replication/findings.md), [qualified per-test analysis](i04-matrix-replication/qualified-analysis.md), [workbook](i04-matrix-replication/qualified-results.xlsx) |
| I03/I04 measurement qualification | Audit exact compiled artifacts for valid-record amplification; retain original outcomes, mark unsupported observations unknown separately. | [I03 qualified analysis](i03-security-perspectives/qualified-analysis.md), [I04 qualified analysis](i04-matrix-replication/qualified-analysis.md) |
| I05 | 80 fresh trajectories with up to five submissions, holding I04 context inserts fixed; 75 full-functional results, with tradeoffs retained. | [Findings](i05-budget-sensitivity/findings.md), [qualified analysis](i05-budget-sensitivity/qualified-analysis.md), [workbook](i05-budget-sensitivity/qualified-results.xlsx) |
| I06 | 80 fresh trajectories, 76 full functional; operational context improves the oversized-line fixture, with policy regressions and coverage limits retained. | [Findings](i06-operational-guards/findings.md), [qualified analysis](i06-operational-guards/qualified-analysis.md), [workbook](i06-operational-guards/qualified-results.xlsx) |
| I07 | 80 fresh trajectories, 74 full functional; total-count decreases recur, but perfect operational resource results do not fully replicate. | [Findings](i07-operational-replication/findings.md), [two-round comparison](i07-operational-replication/replication-analysis.md), [workbook](i07-operational-replication/qualified-results.xlsx) |
| I08 | 64 repeat evaluations on 32 fixed artifacts; no measurement transitions or source/class mismatches; unresolved checks remain distinct. Zero new model calls. | [Findings](i08-evaluator-repeatability/findings.md), [per-check stability](i08-evaluator-repeatability/stability.csv), [all observations](i08-evaluator-repeatability/results.json) |
| I09 | 40 fresh trajectories on Generation S and Reuse S+B × none / requirements / task-only / CWE-catalog inserts, all full functional; every generic arm has fewer failed checks than its control, requirements remains strongest, one catalog acquisition inspected nothing. | [Findings](i09-generic-acquisition/findings.md), [issue matrix](i09-generic-acquisition/issue-matrix.md), [workbook](i09-generic-acquisition/experiment_results_report.xlsx) |
| I10 | 50 trajectories on Generation S and Reuse S+B × single-shot / agentic × none / static / adaptive sidecar with the agent-acquired data-flow graph; static graph insert cuts failed checks to 3–10 of 50 but costs functionality in Generation; file-level adaptive slicing delivers the insert one attempt late. | [Findings](i10-agentic-delivery/findings.md), [issue matrix](i10-agentic-delivery/issue-matrix.md), [workbook](i10-agentic-delivery/experiment_results_report.xlsx) |
| I11 | Pilot, 9 trajectories on Generation S: fresh control, compact requirement-and-control insert (17k characters), symbol-level adaptive sidecar; the compact insert keeps the security effect (3 failed of 30 vs 23) at lower functional cost than the full insert; symbol slicing changes timing, not volume. | [Findings](i11-symbol-sidecar/findings.md), [issue matrix](i11-symbol-sidecar/issue-matrix.md) |
| I12 | Pilot, 6 trajectories on Generation S, agentic delivery: fresh control vs a gatekeeper sidecar whose judge rejects submissions on judged control violations; the judge intervened on all 11 consulted submissions, no gate trajectory reached evaluation. | [Findings](i12-gate-sidecar/findings.md), [issue matrix](i12-gate-sidecar/issue-matrix.md) |
| I13 | Pilot, 6 agentic trajectories on Generation S with the gate in shadow mode: 6/6 functional, 37 failed of 60; the judge would have intervened on all 6 judged first submissions and each of them failed checks in the cited categories. | [Findings](i13-shadow-gate/findings.md), [calibration](i13-shadow-gate/gate-calibration.md) |
| I14 | Pilot, 9 agentic trajectories on Generation S: fresh control, coach (verdict attached to functional feedback) and gate-once (one rejection, then coach); coaching keeps 3/3 functional with 16 failed of 30, gate-once reaches 3 failed of 30 with 0/3 functional. | [Findings](i14-coach-gate/findings.md), [issue matrix](i14-coach-gate/issue-matrix.md) |
| I15 | Pilot, 6 agentic trajectories on Generation S: fresh control vs rewind (on the first positive verdict, discard the submission and two tool turns, restore state, inject the verdict); rewind gives 3 failed of 30 and 0/3 functional, with 7 of 12 post-rewind submissions invalid. | [Findings](i15-rewind/findings.md), [issue matrix](i15-rewind/issue-matrix.md) |
| I16 | Confirmation, 20 single-shot trajectories: fresh control vs compact agent-acquired insert on both cells. Reuse S+B: 29 → 2 failed of 50, functional 5 → 3 of 5. Generation S static arm not measured: all five requests rejected by the provider for a 66-character request id (limit 64); retained as transport failures, re-collected as I16b. | [Findings](i16-compact-confirmation/findings.md), [issue matrix](i16-compact-confirmation/issue-matrix.md) |

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
