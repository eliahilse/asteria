# Candidate public datasets & benchmarks

Scope: public corpora/benchmarks to complement Apo-Games (+ Prof. Wellig's
corpus). Ranked by the project's priority — **security × reuse intersection**
first (vulnerabilities that recur across reused/cloned code), then LLM-code
security oracles, then reuse-structure and repo-context benchmarks.

> **Bottom line for the meeting.** The *exact* dataset we'd want — the **same
> flaw labeled as it recurs across a deliberately clone-and-owned program
> family** — **does not exist publicly.** Two independent literature sweeps
> reached this conclusion. Apo-Games *is* that setup, so this is a **novelty
> argument**, not a gap. The practical path: use the propagation *tools* below to
> auto-label recurring vulns on Apo-Games, and the security *oracles* below to
> score whether an LLM reuses/generates the fix safely.

---

## Tier 1 — Security × reuse: vulnerability propagation across clones (most on-thesis)

| Dataset / tool | What it gives | Reuse/propagation | Lang · size | Access |
|---|---|---|---|---|
| **V0Finder** (USENIX Sec '21) | Traces a CVE's **origin + propagation** across cloned software; directed propagation graphs | **Yes — the closest match** | C/C++ · 5,671 CVEs over 80B LoC | [paper](https://www.usenix.org/system/files/sec21-woo.pdf) |
| **MOVERY / V1SCAN** (Woo lab, USENIX '22/'23) | Finds **modified** vulnerable code-clones in reused OSS; ships a reuse benchmark | **Yes** (62% reused *with* code changes) | C/C++ · ~150 CVE clones / 10 programs | [V1SCAN](https://www.usenix.org/system/files/usenixsecurity23-woo.pdf) |
| **VUDDY / ReDeBug** (S&P '17 / USENIX '12) | Function-granularity vulnerable-clone **detectors** + signature corpora; find unpatched clones | **Yes** — the tool to **auto-label recurrence on Apo-Games** | C/C++ (ReDeBug multi-lang) | [VUDDY](https://github.com/squizz617/vuddy) |
| **VMud / "Recurring Vulnerability Detection: How Far Are We?"** (ISSTA '25) | Recurring-vuln **evaluation** with multi-project ground truth | **Yes** (propagation-relevant) | C/C++ | ISSTA 2025 |

## Tier 2 — Is the reused/generated code secure? (LLM-code security oracles)

| Benchmark | What it tests | Lang · size | Access |
|---|---|---|---|
| **CyberSecEval / PurpleLlama** (Meta) | Whether an LLM **emits insecure code** + attack-compliance; static-analysis scored | ~8 langs · 100s/suite | [repo](https://github.com/meta-llama/PurpleLlama/tree/main/CybersecurityBenchmarks) |
| **CWEval** (2025) | Judges generated code on **functionality AND security oracles together** (fixes spec-vagueness of others) | ~5 langs · 100+ tasks | [paper](https://arxiv.org/pdf/2501.08200) |
| **SeCodePLT** (2024) | Security benchmark for **code agents** (44 CWE categories), some repo-level | Py/C/C++/Java · ~5.9k | [paper](https://arxiv.org/html/2410.11096) |
| **SecurityEval** / **LLMSecEval** | CWE-seeded generation prompts, CodeQL/Bandit scored (single-file) | Python / C+Py · ~120–150 | [SecurityEval](https://github.com/s2e-lab/SecurityEval) |

_Honorable mentions (verified): SALLM, CodeLMSec, SecureAgentBench (2025), SecVulEval (2025)._

## Tier 3 — Reuse structure & feature-into-existing-system (for the reuse/generation tracks)

| Dataset | Task shape | Reuse handle | Lang · size | Access |
|---|---|---|---|---|
| **FEA-Bench** (MS, ACL '25) | **Add a feature into an existing repo** (only clean match to this task) | New components + integrate existing code | Python · 1,401 / 83 repos | [github](https://github.com/microsoft/FEA-Bench) |
| **RepoExec** (NAACL '25) | Repo-level exec generation | **Dependency Invocation Rate** = a direct *reuse metric* | Python · ~350 | [arXiv](https://arxiv.org/abs/2406.11927) |
| **CoderEval** (ICSE '24) | Non-standalone function generation | **6 graded context-dependency levels** | Py+Java · 460 | [arXiv](https://arxiv.org/abs/2302.00288) |
| **DevEval / EvoCodeBench** | Repo-level function generation | Reference-**dependency annotations**; EvoCodeBench is leakage-resistant | Python · 1,825 / 275 | [DevEval](https://github.com/open-compass/DevEval) |
| **ArgoUML-SPL** | SPL feature-location benchmark | **Gold feature→code ground truth** (like Apo-Games) | Java · ~120 KLOC / 8 feats | [challenge](https://variability-challenges.github.io/2018/ArgoUMLSPL/) |
| **Divergent Fork Families** (EMSE '21) | Mined real cross-fork code propagation | **Labeled organic reuse** across siblings; vuln-propagation vehicle | Java/C#/JS · 9,401 families | [repo](https://github.com/johnxu21/emse2021) |
| **MuScalpel transplant set** (ISSTA '15) | **Transplant a feature donor→host** (oracle = tests) | Reuse *is* the task; tiny but perfect shape | C · 15 experiments | [site](http://crest.cs.ucl.ac.uk/autotransplantation/MuScalpel.html) |
| **PyMigBench-2.0 / MiG.4** | Library/API **migration** (same feature, new dependency) | Labeled source→target API mappings | Py / Py+Java · 335+ | [repo](https://github.com/ualberta-smr/PyMigBench) |

## Tier 4 — Repo-context & agentic SWE (mostly bug-fix; contrast/baseline only)

- **SWE-bench family** (Verified/Multimodal/Multilingual/Live/**Pro**) + **SWE-Lancer** — agentic GitHub-**issue resolution**; full-repo context but reuse incidental. Use as baseline; mine Pro/Lancer for the feature-flavored subset. [SWE-bench](https://github.com/swe-bench/SWE-bench)
- **CrossCodeEval / RepoBench / RepoEval / RepoCod** — cross-file **completion**; test "does the model use surrounding-repo context," not feature reuse.
- **Commit0 / DevBench** — feature/library generation but **from scratch**, not reuse of existing features.

### On "DeepSWE" specifically
DeepSWE is a **model, not a dataset** — a 32B coding agent RL-fine-tuned from
Qwen3-32B (open weights: HF `agentica-org/DeepSWE-Preview`). It was **trained on
~4,500 tasks from R2E-Gym** and **evaluated on SWE-bench Verified** (42.2%
Pass@1). So the reusable *data* behind it is **R2E-Gym / SWE-Gym** — issue-
resolution training environments, not reuse benchmarks. Worth citing as the
current open-agent reference point, not as a reuse corpus.

---

## Recommended concrete pipeline (what both sweeps converged on)

1. **Auto-label recurring vulns on Apo-Games** by running **VUDDY** (or **MOVERY**)
   over the 26 variants → turns our clone-propagation finding into labeled data.
2. **Score LLM reuse/fixes for security** with **CWEval** (functionality + security
   together) and/or **CyberSecEval**.
3. **Measure reuse directly** with **RepoExec's DIR** or **CoderEval's context
   levels** when we move to the generation track; **FEA-Bench** for the exact
   "add a feature to an existing system" task shape.
4. Position the project's own contribution as **the first clone-and-own *family*
   corpus with labeled recurring vulnerabilities** — the public gap.

_Not verified (flag before citing): exact SPDX licenses per repo; RepoZero and
SWE-ContextBench (surfaced in listings, primary sources unconfirmed)._
