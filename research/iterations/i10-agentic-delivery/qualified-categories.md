# i10-agentic-delivery: trajectories affected by security-check category

An affected trajectory has at least one observed failed check in the category. Unaffected requires every category check to pass. Otherwise unresolved. A known failure establishes affected status even when other category checks are unknown. Categories use the same N trajectories; five input checks do not become five independent samples. Bounds cover unmeasured category status, not confidence. These are fixture/policy outcomes, not counts of unique vulnerabilities.

| Condition | Category | Affected / N | Unaffected | Unresolved | Affected-rate Δ bounds, pp |
| --- | --- | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | Input policy | 5/5 | 0 | 0 | Control |
| generation_s__single_shot__none | Retention policy | 3/5 | 2 | 0 | Control |
| generation_s__single_shot__none | Parser robustness | 0/5 | 5 | 0 | Control |
| generation_s__single_shot__none | Resource stress | 4/5 | 0 | 1 | Control |
| generation_s__single_shot__none | Deserialization dispatch | 0/5 | 5 | 0 | Control |
| generation_s__single_shot__static | Input policy | 4/5 | 1 | 0 | [-20, -20] |
| generation_s__single_shot__static | Retention policy | 1/5 | 4 | 0 | [-40, -40] |
| generation_s__single_shot__static | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__single_shot__static | Resource stress | 0/5 | 0 | 5 | [-100, +20] |
| generation_s__single_shot__static | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__agentic__none | Input policy | 5/5 | 0 | 0 | [+0, +0] |
| generation_s__agentic__none | Retention policy | 1/5 | 4 | 0 | [-40, -40] |
| generation_s__agentic__none | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__agentic__none | Resource stress | 5/5 | 0 | 0 | [+0, +20] |
| generation_s__agentic__none | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__agentic__static | Input policy | 3/5 | 2 | 0 | [-40, -40] |
| generation_s__agentic__static | Retention policy | 0/5 | 5 | 0 | [-60, -60] |
| generation_s__agentic__static | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__agentic__static | Resource stress | 0/5 | 0 | 5 | [-100, +20] |
| generation_s__agentic__static | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__agentic__adaptive | Input policy | 5/5 | 0 | 0 | [+0, +0] |
| generation_s__agentic__adaptive | Retention policy | 1/5 | 4 | 0 | [-40, -40] |
| generation_s__agentic__adaptive | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__agentic__adaptive | Resource stress | 1/5 | 0 | 4 | [-80, +20] |
| generation_s__agentic__adaptive | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__single_shot__none | Input policy | 5/5 | 0 | 0 | Control |
| reuse_sb__single_shot__none | Retention policy | 0/5 | 5 | 0 | Control |
| reuse_sb__single_shot__none | Parser robustness | 0/5 | 5 | 0 | Control |
| reuse_sb__single_shot__none | Resource stress | 5/5 | 0 | 0 | Control |
| reuse_sb__single_shot__none | Deserialization dispatch | 0/5 | 5 | 0 | Control |
| reuse_sb__single_shot__static | Input policy | 0/5 | 5 | 0 | [-100, -100] |
| reuse_sb__single_shot__static | Retention policy | 4/5 | 1 | 0 | [+80, +80] |
| reuse_sb__single_shot__static | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__single_shot__static | Resource stress | 0/5 | 1 | 4 | [-100, -20] |
| reuse_sb__single_shot__static | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__agentic__none | Input policy | 5/5 | 0 | 0 | [+0, +0] |
| reuse_sb__agentic__none | Retention policy | 2/5 | 3 | 0 | [+40, +40] |
| reuse_sb__agentic__none | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__agentic__none | Resource stress | 4/5 | 0 | 1 | [-20, +0] |
| reuse_sb__agentic__none | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__agentic__static | Input policy | 0/5 | 5 | 0 | [-100, -100] |
| reuse_sb__agentic__static | Retention policy | 5/5 | 0 | 0 | [+100, +100] |
| reuse_sb__agentic__static | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__agentic__static | Resource stress | 5/5 | 0 | 0 | [+0, +0] |
| reuse_sb__agentic__static | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__agentic__adaptive | Input policy | 3/5 | 1 | 1 | [-40, -20] |
| reuse_sb__agentic__adaptive | Retention policy | 1/5 | 3 | 1 | [+20, +40] |
| reuse_sb__agentic__adaptive | Parser robustness | 0/5 | 4 | 1 | [+0, +20] |
| reuse_sb__agentic__adaptive | Resource stress | 4/5 | 0 | 1 | [-20, +0] |
| reuse_sb__agentic__adaptive | Deserialization dispatch | 0/5 | 4 | 1 | [+0, +20] |

Input and retention policies are separate from parser robustness, resource stress and the deserialization-dispatch canary. This grouping is an exploratory analysis introduced after I04; original counts and complete per-check results remain available. No significance claim or category-weighted combined score is introduced.
