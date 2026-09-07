# Study data, version 2

The browser dataset contains current study definitions and explicitly imported
new attempts. Old pilot and selected published runs are no longer imported,
rendered or exported. Generation of the static data also removes stale result
payloads from its evidence directory.

## Test-level records

Each attempt has a frozen condition and one status slot for each of 27 declared
checks: 11 security checks and 16 functional checks. Test definitions include
input/scenario, expected outcome, security category, CWE review tags, coverage
limits and executable source. Descriptions and taxonomy links are human-authored
metadata, not additional test execution evidence.

- `pass` / `fail`: recorded outcome for the declared test.
- `not_run`: no outcome recorded.
- `unknown`: inconclusive execution, including unsupported persistence formats.
- `compile_error`: the relevant evaluation could not compile.
- `infrastructure_error`: the environment prevented evaluation.

The main table reports pass / (pass + fail), with unresolved counts beside it.
Opening a test shows all statuses separately, pass / all imported attempts and a
95% Wilson interval for the tested proportion. An empty denominator is null,
rendered as a dash, never 0%. Compilation failures do not invent individual test
failures. Different evaluator/environment signatures withhold rate differences.

Differences are unpaired percentage points. Repetition IDs are not shared model
seeds. Tests within one generation are not independent experimental replicates.
Different fractions of unresolved tests can bias a comparison of conditional pass
rates; inspect the all-attempt denominator and reasons before interpreting it.

## New run import

`ASTERIA_RUNS_DIR` and `ASTERIA_EVALUATIONS_DIR`, or corresponding CLI arguments,
select input directories explicitly. No private directory is discovered by
default. Each run must match the frozen schedule, exact prompt, request hash and
model settings. Evaluations must match the observation file and response bytes;
source hashes and security control outcomes are verified. Calibration reports
are excluded. Missing evaluation reports leave the check slots unevaluated.

The dataset fingerprint covers canonical JSON excluding the fingerprint itself.
Evidence artifacts have exact byte counts, source paths and SHA-256 hashes. XLSX
preserves individual statuses and both denominators; long evidence is chunked,
not truncated. Missing usage and billed cost remain unrecorded.

## Contexts and scenarios

C1 records security-relevant syntax, C2 local flow candidates, C3 existing audit
leads with original status, and C4 declared policy. Counts measure records, not
vulnerabilities. Type, scope, injection timing and evidence status are separate
fields. Source facts do not establish receiver binding, effective guards or
runtime reachability. See [extraction methods](../research/context/README.md).

The current study freezes both the original Highscore prompt scenarios and a
security-only ablation. Within each strategy, treatments compare against the
corresponding baseline. The original-prompt bridge and ablation differ in other
prompt content and must not be pooled. Dynamic reinjection is not part of this
version. No completed model attempts are implied by a planned schedule.
