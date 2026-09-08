# Evaluate a model response

I03 uses `research.evaluate_integrated`, protocol
`highscore-response-v3-integrated-security`. It retains the original test
contracts while giving every generated-code JVM a fresh home/temp directory and
linking security probes against the exact successful whole-game compilation.
The compiled classes are saved with hashes before JUnit classes are added.
Compilation failure cannot produce security passes from a partial feature build.

```sh
python3 -m research.evaluate_integrated \
  --response path/to/complete-files.txt \
  --output .local/evaluations/a-new-directory
```

`evaluate_response` below remains the unchanged v1 evaluator used for the original
replay and I01. `evaluate_isolated` is the intermediate v2 wrapper used to measure
the home-directory effect separately. [I01 remeasurement](iterations/i01-home-isolated/README.md)
retains both sides of that same-code comparison. Do not combine evaluator versions
within a condition or overwrite earlier reports.

After receiving a completed adapter observation:

```sh
python3 -m research.evaluate_response \
  --observation .local/runs/luna-highscore-v1/ATTEMPT_ID.json \
  --output .local/evaluations/ATTEMPT_ID
```

The observation's request and model/settings are checked against the frozen
manifest before evaluation. For archived response calibration, use `--response
path/to/response.txt` instead. A raw-response calibration is not a new model
attempt and must not be added to a model-result denominator.

The output directory must be new. It contains the exact response, source hashes,
sanitized files and diffs, original author-suite output, per-process diagnostics,
16 named functional observations, 11 security observations and control results.
The runner observation stays unchanged. No model repair is performed. A response
with no extractable Java is an unsuccessful output, with all checks unevaluated.

For a separate development workflow that requests edits to existing game classes
and returns bounded functional feedback, see [feature delivery calibration](FEATURE_DELIVERY.md).
Those multi-submission trajectories are excluded from the single-response matrix.

The functional evaluator reuses the original author integration and the pilot's
documented package/import/environment repairs. It parses JUnit output separately:
unnamed tests can pass only after a consistent complete suite execution. Missing
counts, initialization failures or aborted processes cannot backfill passes.
The original author summaries are retained beside the stricter normalized checks.

The original import index selected the last file with a given simple class name
in filesystem traversal order. ApoMario contains two `ApoMarioAnalysis` classes;
Linux and macOS selected different packages, and the Linux selection broke the
reference output before test execution. The new evaluator excludes ambiguous
names from automatic import repair, preserving explicit imports. Candidate
packages are recorded in `ambiguousImports`. This is a declared harness correction
for future evaluations; archived observations and upstream scripts stay intact.

Functional JVMs use a 256 MiB heap and a private temporary directory; timeouts
remain those of the original suites. Security checks use the existing v1
protocol's 64 MiB heap, 15-second limits and freshly validated safe/weak controls.
The security unit compilation links saved Highscore classes to the original game
JAR, while the functional suites compile/integrate the complete generated feature.

## Environment calibration

A working JDK is required (`JAVA_HOME` or discoverable `javac`). The original
integration suites initialize AWT and require a graphical runtime. On Linux, run
under `xvfb-run -a`; on macOS, use a session with GUI access. A restricted sandbox
that blocks AWT can abort the JVM before a JUnit summary. Such checks remain
unknown, and cannot establish functional or joint success. These subprocesses
are resource-limited evaluation processes, not a general sandbox for hostile Java.

Before evaluating a new study environment, calibrate against the preserved
`gpt-5.4-mini_none__generation_s__r1` response:

```sh
python3 -m research.evaluate_response \
  --response experiments/vamos_security_pilot/results/raw_responses/gpt-5.4-mini_none__generation_s__r1.txt \
  --output .local/calibration/highscore-reference
```

Expected: 16/16 functional passes; security 5/11 passes, with the five record
validation failures and the persisted-record resource check failing. This was
reproduced on the local OpenJDK 26.0.2.1 macOS GUI environment on 2026-09-08.
It calibrates the wiring against known evidence; it is not independent validation
of the original test suite. Preserve and investigate any environment discrepancy
before interpreting new model outcomes.

The new observation report is local by default. Publishing it into the workbench
requires an explicit evidence import with a separate prospective cohort; it must
not overwrite the historical pilot or selected published subset.
