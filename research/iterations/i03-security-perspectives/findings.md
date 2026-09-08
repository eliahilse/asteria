# I03 findings for review

**Measurement update:** the table and counts below record the original evaluation.
Use the [qualified analysis](qualified-analysis.md), [qualified workbook](qualified-results.xlsx)
and [qualified figures](figures-qualified/quality-and-security.pdf) for current reporting.
The format audit makes three large-record outcomes unknown: two passes and one
failure. Requirements therefore have 12/92 failed/evaluated issue checks, with
eight unresolved; boundaries have 18/97, with three unresolved. Functional outcomes
are unchanged. Both strategies still reduce aggregate failures against both fresh
controls under every assignment of unresolved outcomes. The minimum reductions
are 28 and 23 for requirements, and 23 and 27 for boundaries, respectively.
The original memory-exhaustion observation remains evidence about those exact
bytes, even where valid-record amplification was not demonstrated.

Task-specific requirements and trust-boundary context reduced failed security
checks in both tested paper-context cells while preserving within-budget
functional success. The effect was strongest on explicit input policies. This is
an exploratory result for six fixed generated inserts; I04 uses fresh contexts
and responses to test whether the tendency repeats.

| Security strategy | Full functional within budget | First-submission full | Failed issue checks / evaluated | Unresolved | Code submissions |
| --- | ---: | ---: | ---: | ---: | ---: |
| None | 10/10 | 6/10 | 71/99 | 1 | 14 |
| Overview | 7/10 | 6/10 | 57/84 | 16 | 18 |
| Requirements | 10/10 | 6/10 | 13/94 | 6 | 15 |
| Trust boundaries | 10/10 | 3/10 | 18/98 | 2 | 21 |

This table adds the two cells for orientation. The primary comparisons remain
within Generation S+F+B and within Reuse B, against their own fresh controls.
Ten trajectories sharing two acquired contexts are not ten context acquisitions.

## What improved, and what remains

The requirements arm eliminated all five input-policy failures in all ten
trajectories: negative score, negative time, null name, blank name and the
excessive-name fixture. Each control cell failed all 25 corresponding checks.
Boundary guidance improved six fully measured issue checks per cell, with no
increase among the fully measured checks in this sample. The large-record check
still has incomplete coverage and is excluded from those directional counts.

Both targeted strategies have fewer failed checks even under the most adverse
assignment of unmeasured outcomes: requirements avoid at least 28 failures in
Generation and 24 in Reuse; boundaries avoid at least 24 and 27 respectively.
These finite-sample missingness bounds are not confidence intervals or a claim
about real-world vulnerability prevalence. The full per-check comparison and
marginal rate intervals are in [analysis.md](analysis.md).

The high-level overview does not support the same conclusion. Its functional
success fell to 3/5 in Generation and 4/5 in Reuse, while missing security
observations make its aggregate count reduction inconclusive. These failures
remain included and overview is retained in I04.

The first-submission result also differs from the within-budget endpoint.
Boundary context reached all functional checks after repairs, but succeeded on
only 3/10 first submissions versus 6/10 for controls and used 21 code submissions
versus 14. Requirements used 15. A claim of uniformly easier implementation would
be unsupported by these observations.

## Diagnosed failure mechanisms

The saved [diagnostics and source excerpts](failure-diagnostics.json) distinguish
actual heap exhaustion from the frozen test's policy thresholds. All frozen
pass/fail values remain unchanged.

Four Reuse requirements implementations explicitly use `MAX_ENTRIES = 1000`
(repetitions 1, 2, 4 and 5). They fail the declared retention check, which requires
at most 100 after 120 valid additions. This establishes a threshold mismatch.
The source still contains a finite retention cap, so this failure alone does not
establish unbounded storage. Repetition 5 also fails the amplified-store test on
the same 100-entry threshold.

The oversized-line failures provide a different kind of evidence. Generation
requirements repetitions 2 and 3 call `readLine()` before checking the returned
string's length. Repetition 4 reads lines without a pre-read byte-size guard.
All three produce `OutOfMemoryError` for the fixed oversized-line fixture.
Repetitions 1 and 5 check file size before reading and pass that fixture. These
are inspected code/outcome examples under the same acquired requirements insert;
they are not a controlled mutation experiment proving that the guard alone
caused the difference.

Reuse requirements repetition 4 calls `Files.readAllLines()` before checking the
resulting list size and exhausts the heap on both resource fixtures. A bound on
the final collection therefore does not establish a bound during decoding.
Across the requirements arm, seven of ten trajectories still exhibit heap
exhaustion on at least one tested fixture. Across boundaries, eight of ten do.
No trajectory passes all 16 functional and all 11 security checks jointly.

The deserialization canary passes in every compiling trajectory, including the
controls; this sample shows no improvement on that check. Its scope is test-only
callback dispatch, not a demonstrated production exploit chain. Existing corpus
findings remain separate evidence; this experiment measures the generated
Highscore feature under controlled caller-input and file-tampering fixtures.

## Review and reproduce

- [Full numeric workbook](results.xlsx), including every check and diagnostic.
- [Per-test rates](per-test.csv) and [fresh-control effects](security-effects.csv).
- [Quality/security figure](figures/quality-and-security.pdf) and [per-check effects](figures/per-check-security-effects.pdf), with SVG/PNG versions and hash provenance beside them.
- [Exact prompt inserts](contexts/) and [frozen protocol](README.md).
- Full raw inputs and intermediate outputs in `research/results/i03-complete/evidence.tar.gz`, also verified outside the repo.

The plotting environment is pinned in `research/plot-requirements.txt`. After
restoring the archive, `research.iteration_results`, `research.scientific_summary`
and `research.failure_diagnostics` reconstruct the tables without model calls.
`research.plot_iteration` renders the saved analysis. A clean-checkout restoration
check is recorded in `restore-proof.json`.
