# Related datasets

We study whether an LLM coding agent, when implementing a feature inside an
existing project, *reuses* existing code, and how that reuse affects security —
i.e. the joint outcome of **functional correctness × code reuse × security
correctness**. No existing benchmark spans this intersection. SusVibes [1] and
SecureVibeBench [7] evaluate whether agents introduce vulnerabilities while
implementing features in real repositories, but do not measure whether an
existing implementation was reused; RepoExec [5] quantifies reuse via its
Dependency Invocation Rate yet has no security dimension; and AntMan [3],
VulCoCo [2], and VULTURE [4] establish that vulnerabilities propagate through
code reuse, but study human/historical copying rather than an agent's reuse
decisions. The remaining security benchmarks (A.S.E [8], BaxBench [10],
CWEval/CyberSecEval [11]) judge the security of generated code without a reuse
notion, and the vulnerable-clone tooling (V0Finder/MOVERY/VUDDY [9]) labels
propagation without an LLM. A controlled clone-and-own family such as Apo-Games
uniquely permits varying the reusable donor context — none, safe, vulnerable, or
mixed — for a fixed feature request, isolating whether an agent's reuse
propagates, avoids, or repairs a known flaw.

## References

1. **SusVibes** — *Is Vibe Coding Safe? Benchmarking Vulnerability of Agent-Generated Code in Real-World Tasks.* Zhao et al., CMU. ICML 2026. 200 tasks / 108 repos; functional + security dynamic tests. arXiv:2512.03262
2. **VulCoCo** — *A Simple Yet Effective Method for Detecting Vulnerable Code Clones.* Bui et al., SMU/GovTech. arXiv preprint, Jul 2025 (no venue); introduces the SyVC synthetic benchmark. arXiv:2507.16661
3. **AntMan** — *Recurring Vulnerability Detection: How Far Are We?* Cao et al., Fudan. ISSTA 2025. RV dataset of 4,569 recurring vulnerabilities. [pdf](https://chenbihuan.github.io/paper/issta25-cao-antman.pdf)
4. **VULTURE** — *Enhancing Security in Third-Party Library Reuse: Comprehensive Detection of 1-day Vulnerability through Code Patch Analysis.* Xu et al. NDSS 2025. arXiv:2411.19648
5. **RepoExec** — repo-level executable generation; introduces the Dependency Invocation Rate. NAACL 2025. arXiv:2406.11927
6. **FEA-Bench** — repo-level feature implementation from PRs. Microsoft. ACL 2025. 1,401 instances / 83 repos. arXiv:2503.06680
7. **SecureVibeBench** — *Benchmarking Secure Vibe Coding of AI Agents via Reconstructing Vulnerability-Introducing Scenarios.* Chen et al. ACL 2026 Main. 105 repo-level C/C++ tasks (ARVO/OSS-Fuzz). arXiv:2509.22097
8. **A.S.E** — *A Repository-Level Benchmark for Evaluating Security in AI-Generated Code.* Lian et al., Tencent. Findings of ACL 2026. Real repos with documented CVEs; containerized scoring. arXiv:2508.18106
9. **V0Finder** (USENIX Sec 2021), **MOVERY/V1SCAN** (USENIX 2022/2023), **VUDDY** (S&P 2017) — vulnerability origin/propagation and vulnerable-clone detection over reused C/C++.
10. **BaxBench** — *Can LLMs Generate Correct and Secure Backends?* Vero et al., ETH Zurich. ICML 2025. 392 tasks; functional tests + end-to-end exploits. arXiv:2502.11844
11. **CWEval** (arXiv:2501.08200), **CyberSecEval/PurpleLlama** (Meta) — one-shot generation-security baselines; no reuse dimension.
