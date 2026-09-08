# I03: security perspectives under corrected evaluation

Collection is complete. Read the [findings](findings.md), [per-test analysis](analysis.md),
or [workbook](results.xlsx). The following is the protocol frozen before collection.

This exploratory experiment freezes 40 fresh Luna trajectories: Generation S+F+B
and Reuse B, each with no security context, a repository security overview,
concrete security requirements, or trust-boundary guidance; five trajectories per
combination. Each trajectory has at most three submissions. The complete schedule
is randomized within repetition blocks and runs regardless of its outcomes.

All six context acquisitions start from the task and a fresh repository snapshot.
They receive no previous generated code, audits, test code, fixture thresholds or
evaluation results. Context generation uses inspected evidence IDs and explicitly
distinguishes observations, prospective recommendations and unknowns. The exact
task and final prompt inserts are frozen in `plan.json` and `contexts/`. Every
acquisition turn and candidate is retained in the archived runtime evidence.
I02 acquisition outputs are not reused.

The code-generation delivery protocol is identical to I01: transactional source
edits must integrate the live lifecycle and menu. Subsequent submissions receive
only delivery, compiler and the original functional-test feedback. Security-test
feedback is withheld. All submissions, including rejected edit batches and
provider failures, remain observations; the budget is never reset.

Evaluation uses a fresh home/temp directory for each Java process. Security probes
use the exact compiled game, including modified classes, before test classes are
added. The original 16 functional checks and 11 security checks are unchanged.
Reference calibration reproduces 16/16 functional and 5/11 security passes; the
previously unlinked view artifact now permits all 11 security checks to execute.
The manifest freezes the evaluator, test, library and reference input hashes.

Report first-submission and within-budget functional success separately. Issue
counts exclude the positive valid-record round trip and include their evaluated
and unresolved denominators. A noncompiling or unmeasured case cannot count as
secure. Comparisons use fresh controls from this same schedule. Saved per-test
results and trajectory-level observations support examination of heterogeneous
effects; checks within one artifact are not independent samples.

This remains exploratory development informed by I01 and I02. One acquisition
per method/perspective is reused within I03, so five code trajectories do not
constitute five independent context acquisitions. A later replication must use
fresh context acquisitions and fresh code. Returned model identity is checked;
the provider does not attest effective reasoning/temperature settings. The tests
cover lifecycle recording but do not establish correct menu rendering or prove
the absence of all vulnerabilities.
