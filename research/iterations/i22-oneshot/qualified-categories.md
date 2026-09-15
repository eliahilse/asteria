# i22-oneshot: trajectories affected by security-check category

An affected trajectory has at least one observed failed check in the category. Unaffected requires every category check to pass. Otherwise unresolved. A known failure establishes affected status even when other category checks are unknown. Categories use the same N trajectories; five input checks do not become five independent samples. Bounds cover unmeasured category status, not confidence. These are fixture/policy outcomes, not counts of unique vulnerabilities.

| Condition | Category | Affected / N | Unaffected | Unresolved | Affected-rate Δ bounds, pp |
| --- | --- | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | Input policy | 5/5 | 0 | 0 | Control |
| generation_s__single_shot__none | Retention policy | 2/5 | 3 | 0 | Control |
| generation_s__single_shot__none | Parser robustness | 0/5 | 5 | 0 | Control |
| generation_s__single_shot__none | Resource stress | 5/5 | 0 | 0 | Control |
| generation_s__single_shot__none | Deserialization dispatch | 0/5 | 5 | 0 | Control |
| generation_s__single_shot__static | Input policy | 0/5 | 2 | 3 | [-100, -40] |
| generation_s__single_shot__static | Retention policy | 1/5 | 1 | 3 | [-20, +40] |
| generation_s__single_shot__static | Parser robustness | 0/5 | 2 | 3 | [+0, +60] |
| generation_s__single_shot__static | Resource stress | 2/5 | 0 | 3 | [-60, +0] |
| generation_s__single_shot__static | Deserialization dispatch | 0/5 | 2 | 3 | [+0, +60] |
| reuse_sb__single_shot__none | Input policy | 1/5 | 0 | 4 | Control |
| reuse_sb__single_shot__none | Retention policy | 0/5 | 1 | 4 | Control |
| reuse_sb__single_shot__none | Parser robustness | 0/5 | 1 | 4 | Control |
| reuse_sb__single_shot__none | Resource stress | 1/5 | 0 | 4 | Control |
| reuse_sb__single_shot__none | Deserialization dispatch | 0/5 | 1 | 4 | Control |
| reuse_sb__single_shot__static | Input policy | 0/5 | 2 | 3 | [-100, +40] |
| reuse_sb__single_shot__static | Retention policy | 1/5 | 1 | 3 | [-60, +80] |
| reuse_sb__single_shot__static | Parser robustness | 0/5 | 2 | 3 | [-80, +60] |
| reuse_sb__single_shot__static | Resource stress | 1/5 | 1 | 3 | [-80, +60] |
| reuse_sb__single_shot__static | Deserialization dispatch | 0/5 | 2 | 3 | [-80, +60] |

Input and retention policies are separate from parser robustness, resource stress and the deserialization-dispatch canary. This grouping is an exploratory analysis introduced after I04; original counts and complete per-check results remain available. No significance claim or category-weighted combined score is introduced.
