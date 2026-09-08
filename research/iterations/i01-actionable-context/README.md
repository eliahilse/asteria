# I01: actionable repository-derived security context

Frozen before code collection on 2026-09-08. This is an exploratory development
iteration, informed by the original replay's missing integration and limited
security gains. It is not pooled with the original single-response study.

| Factor | Levels |
| --- | --- |
| Paper combination | Generation S+F+B; Reuse B |
| Security context | None; prospective requirements; trust-boundary review |
| Repetitions | 3 independent trajectories per combination, randomized in blocks |
| Delivery budget | At most 3 Luna submissions per trajectory; 18 trajectories, at most 54 code requests |
| Feedback | Delivery validation, compiler errors and the original 16 functional checks only |
| Model | Requested and returned identity checked as gpt-5.6-luna; medium reasoning, effective settings may be unattested |

The two paper combinations were selected using the prior functional ranking.
Every security arm has a fresh control with the same delivery and feedback budget.
All scheduled outcomes, including errors and regressions, remain in the analysis.

The task, target/donor files and selected paper context are retained. The old prose
output instructions are replaced by exact source edits and reconstructed complete
files. Time units and required integration files are explicit in every arm.
Changes to Level, Panel and Menu are required before evaluation; this is a delivery
criterion, not a claim that menu rendering has been tested.

The four contexts are fresh Luna acquisitions from repository snapshots and task.
They receive no old responses, reports, test code or fixture thresholds. Their
acquisition emphasis was designed after reviewing prior failure categories, so
the iteration is development evidence. A later replication must use fresh code
responses and newly acquired contexts. The first output of each acquisition is
retained, including sparse output and declared uncertainty.

Report first-submission and within-budget functionality separately. Report every
security check with coverage, plus failed-check counts; unsupported encodings and
missing evaluations stay unresolved. Do not interpret fewer evaluated checks as
fewer security issues. A favorable trajectory is not a representative sample by
itself. Complete this fixed schedule before choosing the next setup.

Exact plan: [plan.json](plan.json). Prompt inserts: [contexts](contexts).
Durable runtime evidence: `.local/iterations/i01-actionable-context`, archived
under [research/results](../../results/README.md) and copied to the sibling backup
folder before the next iteration begins.
