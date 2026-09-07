# Intermediary meeting brief

## 60-second opening

The VaMoS paper measures whether LLM-generated and reused features compile and behave correctly, but explicitly leaves security to future work. I preserved its Highscore task, generation/reuse strategies, winning S/B/F contexts, and 16-test functional funnel, then added security as a third outcome and a matched security-context intervention. In the published artifact, exactly 6 of 80 Highscore attempts passed every functional test; source review found a weakness in all six. In the fresh 16-run pilot, all responses arrived, four compiled, one passed every functional test, and that one still had CWE-400. The security context produced one compiling implementation without the targeted patterns, but it did not integrate correctly. So the current result is not that security prompting solved the task: it is that avoiding a known insecure pattern and delivering a correct integrated feature remain separate objectives.

## The bridge from the paper

| Published design/result | Value |
|---|---:|
| Runs | 3 features × 2 strategies × 8 S/B/F contexts × 5 = 240 |
| Main metrics | compilation; mean test-pass proportion conditional on compilation |
| Overall compilation | 40% |
| Mean test-pass proportion among compiling outputs | 72% |
| Best compilation context, generation | S, 67% |
| Best compilation context, reuse | S+F+B, 53% |

This pilot uses Generation+S and Reuse+S+F+B because security evaluation first needs an executable artifact.

## Research questions

1. When small LLMs generate or reuse a Highscore feature, do fully functional outputs preserve, eliminate, or introduce weaknesses?
2. Does explicit security-findings context reduce those weaknesses without sacrificing compilation or functional behavior?
3. Exploratorily, do outcomes differ by model and strategy?

## Corrected results

| Evidence set | Attempts | Responses | Main compile | Passed all 16 functional tests | Secure + functional | Security result |
|---|---:|---:|---:|---:|---:|---|
| Published artifact, Highscore | 80 | — | — | 6 | 0 | Reuse full-pass: 3/3 CWE-502; generation full-pass: 3/3 CWE-400 |
| Fresh, no security context | 8 | 8 | 3 | 1 | 0 | 3/3 compiling outputs have CWE-400 |
| Fresh, security context | 8 | 8 | 1 | 0 | 0 | Sole compiling output is free of targeted findings but not fully functional |
| **Fresh total** | **16** | **16** | **4** | **1** | **0** | — |

“Passed all 16” means 7/7 unit, 4/4 invoked/coupling, and 5/5 autonomous/wiring tests.

## What is defensible today

- Perfect functional-test performance can coexist with a source-adjudicated weakness: this occurred in all six selected published outputs and the sole fully functional fresh output.
- Reuse did not blindly copy the donor's exact HTTP/client-trust flaws. The published full-pass reuse outputs omitted that online path but introduced a reachable Java `readObject()` sink over a locally editable score file. This is transformed attack surface, not demonstrated repair.
- All three published full-pass generation outputs and all three fresh compiling no-security outputs had uncapped local record loading (CWE-400).
- The sole compiling security-context output rejected invalid values and capped retained valid records. That is a preliminary, narrowly scoped suppression signal, but it failed feature integration; malformed-line scanning remains a residual test target, and secure-functional success stayed zero.

## What is not defensible yet

- Do not claim that the security context worked overall, harmed compilation causally, or that one model is better: each cell has only two runs.
- Do not call omission of the donor's online feature a security repair; synchronization was outside the target contract.
- Do not claim demonstrated RCE or DoS. The CWE-502 sink is reachable but gadget-dependent; CWE-400 is source-adjudicated and was not dynamically exploited.
- “Free of targeted findings” is not the same as universally secure.

## Proposed next experiment

Keep the paper's 16 functional tests unchanged and add a separate adversarial security suite for oversized/malformed score stores, invalid numeric values, and hostile names. Then expand the matched arms to five repetitions. The primary endpoint remains joint **secure functional success**, so an implementation must satisfy both suites rather than trading one for the other.

## Supervisor decisions to request

- Is secure functional success the right primary endpoint?
- Should omission caused by a changed feature contract be labeled “non-reproduction,” not “repair”?
- Should the next budget prioritize five repetitions of this intervention, a second feature, or the corrected-donor arm?

## Closing line

> The pilot suggests that avoiding a known insecure pattern is easier than producing a secure implementation that also integrates correctly; the next experiment must optimize both outcomes jointly.
