# i31-v13: trajectories affected by security-check category

An affected trajectory has at least one observed failed check in the category. Unaffected requires every category check to pass. Otherwise unresolved. A known failure establishes affected status even when other category checks are unknown. Categories use the same N trajectories; five input checks do not become five independent samples. Bounds cover unmeasured category status, not confidence. These are fixture/policy outcomes, not counts of unique vulnerabilities.

| Condition | Category | Affected / N | Unaffected | Unresolved | Affected-rate Δ bounds, pp |
| --- | --- | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | Input policy | 5/5 | 0 | 0 | Control |
| generation_s__agentic__none | Retention policy | 3/5 | 2 | 0 | Control |
| generation_s__agentic__none | Parser robustness | 0/5 | 5 | 0 | Control |
| generation_s__agentic__none | Resource stress | 4/5 | 0 | 1 | Control |
| generation_s__agentic__none | Deserialization dispatch | 0/5 | 5 | 0 | Control |
| generation_s__agentic__static | Input policy | 0/5 | 5 | 0 | [-100, -100] |
| generation_s__agentic__static | Retention policy | 0/5 | 5 | 0 | [-60, -60] |
| generation_s__agentic__static | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__agentic__static | Resource stress | 3/5 | 0 | 2 | [-40, +20] |
| generation_s__agentic__static | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__agentic__static-guard | Input policy | 0/5 | 5 | 0 | [-100, -100] |
| generation_s__agentic__static-guard | Retention policy | 0/5 | 5 | 0 | [-60, -60] |
| generation_s__agentic__static-guard | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__agentic__static-guard | Resource stress | 2/5 | 1 | 2 | [-60, +0] |
| generation_s__agentic__static-guard | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__agentic__none | Input policy | 5/5 | 0 | 0 | Control |
| reuse_sb__agentic__none | Retention policy | 4/5 | 1 | 0 | Control |
| reuse_sb__agentic__none | Parser robustness | 0/5 | 5 | 0 | Control |
| reuse_sb__agentic__none | Resource stress | 2/5 | 0 | 3 | Control |
| reuse_sb__agentic__none | Deserialization dispatch | 0/5 | 5 | 0 | Control |
| reuse_sb__agentic__static | Input policy | 0/5 | 5 | 0 | [-100, -100] |
| reuse_sb__agentic__static | Retention policy | 0/5 | 5 | 0 | [-80, -80] |
| reuse_sb__agentic__static | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__agentic__static | Resource stress | 2/5 | 0 | 3 | [-60, +60] |
| reuse_sb__agentic__static | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__agentic__static-guard | Input policy | 0/5 | 5 | 0 | [-100, -100] |
| reuse_sb__agentic__static-guard | Retention policy | 0/5 | 5 | 0 | [-80, -80] |
| reuse_sb__agentic__static-guard | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__agentic__static-guard | Resource stress | 3/5 | 0 | 2 | [-40, +60] |
| reuse_sb__agentic__static-guard | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |

Input and retention policies are separate from parser robustness, resource stress and the deserialization-dispatch canary. This grouping is an exploratory analysis introduced after I04; original counts and complete per-check results remain available. No significance claim or category-weighted combined score is introduced.
