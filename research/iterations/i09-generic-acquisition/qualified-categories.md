# i09-generic-acquisition: trajectories affected by security-check category

An affected trajectory has at least one observed failed check in the category. Unaffected requires every category check to pass. Otherwise unresolved. A known failure establishes affected status even when other category checks are unknown. Categories use the same N trajectories; five input checks do not become five independent samples. Bounds cover unmeasured category status, not confidence. These are fixture/policy outcomes, not counts of unique vulnerabilities.

| Condition | Category | Affected / N | Unaffected | Unresolved | Affected-rate Δ bounds, pp |
| --- | --- | ---: | ---: | ---: | ---: |
| generation_s__none | Input policy | 5/5 | 0 | 0 | Control |
| generation_s__none | Retention policy | 3/5 | 2 | 0 | Control |
| generation_s__none | Parser robustness | 0/5 | 5 | 0 | Control |
| generation_s__none | Resource stress | 5/5 | 0 | 0 | Control |
| generation_s__none | Deserialization dispatch | 0/5 | 5 | 0 | Control |
| generation_s__requirements | Input policy | 5/5 | 0 | 0 | [+0, +0] |
| generation_s__requirements | Retention policy | 0/5 | 5 | 0 | [-60, -60] |
| generation_s__requirements | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__requirements | Resource stress | 3/5 | 0 | 2 | [-40, +0] |
| generation_s__requirements | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__task_only | Input policy | 5/5 | 0 | 0 | [+0, +0] |
| generation_s__task_only | Retention policy | 0/5 | 5 | 0 | [-60, -60] |
| generation_s__task_only | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__task_only | Resource stress | 5/5 | 0 | 0 | [+0, +0] |
| generation_s__task_only | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__catalog | Input policy | 5/5 | 0 | 0 | [+0, +0] |
| generation_s__catalog | Retention policy | 0/5 | 5 | 0 | [-60, -60] |
| generation_s__catalog | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__catalog | Resource stress | 0/5 | 2 | 3 | [-100, -40] |
| generation_s__catalog | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__none | Input policy | 5/5 | 0 | 0 | Control |
| reuse_sb__none | Retention policy | 0/5 | 5 | 0 | Control |
| reuse_sb__none | Parser robustness | 0/5 | 5 | 0 | Control |
| reuse_sb__none | Resource stress | 3/5 | 0 | 2 | Control |
| reuse_sb__none | Deserialization dispatch | 0/5 | 5 | 0 | Control |
| reuse_sb__requirements | Input policy | 0/5 | 5 | 0 | [-100, -100] |
| reuse_sb__requirements | Retention policy | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__requirements | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__requirements | Resource stress | 1/5 | 2 | 2 | [-80, +0] |
| reuse_sb__requirements | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__task_only | Input policy | 4/5 | 1 | 0 | [-20, -20] |
| reuse_sb__task_only | Retention policy | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__task_only | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__task_only | Resource stress | 5/5 | 0 | 0 | [+0, +40] |
| reuse_sb__task_only | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__catalog | Input policy | 3/5 | 2 | 0 | [-40, -40] |
| reuse_sb__catalog | Retention policy | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__catalog | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__catalog | Resource stress | 5/5 | 0 | 0 | [+0, +40] |
| reuse_sb__catalog | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |

Input and retention policies are separate from parser robustness, resource stress and the deserialization-dispatch canary. This grouping is an exploratory analysis introduced after I04; original counts and complete per-check results remain available. No significance claim or category-weighted combined score is introduced.
