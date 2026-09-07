# Evidence model, version 1

The unit of observation is one attempt, one named test execution, or one finding.
Attempts belong to a cohort and a model × strategy × context condition. Repetition
numbers identify observations; they do not imply paired random seeds.

`python3 -m research.import_evidence` builds the browser dataset from preserved
source files. No source evidence is modified. Its SHA-256 fingerprint is computed
over canonical JSON excluding the fingerprint itself. Every evidence artifact
retains a repository path, byte count and content hash. Exact historical prompt
hashes and character counts are checked against the submitted run state.

## Observation semantics

- `pass` / `fail`: an observed test outcome.
- `not_run`: no execution result exists.
- `compile_error`: a test suite could not compile.
- `infrastructure_error`: the environment prevented evaluation.
- Empty tests on the selected published cohort: individual observations were not
  imported; aggregate reported suite results are retained separately.
- `source_adjudicated`: a historical source-review conclusion. It does not imply
  that an exploit was executed.
- Scanner candidates remain separate from adjudicated findings.
- `no_targeted_findings` means the fixed historical review found no targeted
  patterns. It does not mean the implementation is universally secure.
- Missing cost, reasoning settings or token observations are null/absent, never 0.

The six selected published successes are separated from the complete 16-attempt
fresh pilot. The interface defaults to the latter and never pools the cohorts.
Compilation and functional-success rates use all attempts in the selected cell.
Security results must state the assessed denominator and the missing count.
Per-test rates describe test coverage; tests within one implementation are not
independent experimental replicates.

The original missing-Java observations remain historical records. Any rerun is
stored in `research/results/` with a protocol, environment and evidence, and is
displayed separately. Queue elapsed seconds include orchestration and collection
delay and must not be described as model inference latency.

## Context provenance

The five numbered historical constraints are imported as `legacy_mixed` facts.
They are not retrospectively relabeled as automatically generated C1–C4 contexts.
Automatic extraction, proposed conditions and executed conditions are distinct.

Future observations must freeze source and test revisions, extraction rules,
threat model, exact prompt, context fact IDs, model settings and raw outputs.
Evaluation reports should include both vulnerable and corrected controls and
explain untested properties. Requirements must not be inferred from test results
and fed back into a supposedly independent generation experiment.
