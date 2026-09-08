# i05-budget-sensitivity: trajectories affected by security-check category

An affected trajectory has at least one observed failed check in the category. Unaffected requires every category check to pass. Otherwise unresolved. A known failure establishes affected status even when other category checks are unknown. Categories use the same N trajectories; five input checks do not become five independent samples. Bounds cover unmeasured category status, not confidence. These are fixture/policy outcomes, not counts of unique vulnerabilities.

| Condition | Category | Affected / N | Unaffected | Unresolved | Affected-rate Δ bounds, pp |
| --- | --- | ---: | ---: | ---: | ---: |
| generation_s__none | Input policy | 5/5 | 0 | 0 | Control |
| generation_s__none | Retention policy | 5/5 | 0 | 0 | Control |
| generation_s__none | Parser robustness | 0/5 | 5 | 0 | Control |
| generation_s__none | Resource stress | 5/5 | 0 | 0 | Control |
| generation_s__none | Deserialization dispatch | 0/5 | 5 | 0 | Control |
| generation_s__overview | Input policy | 5/5 | 0 | 0 | [+0, +0] |
| generation_s__overview | Retention policy | 4/5 | 1 | 0 | [-20, -20] |
| generation_s__overview | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__overview | Resource stress | 5/5 | 0 | 0 | [+0, +0] |
| generation_s__overview | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__requirements | Input policy | 4/5 | 1 | 0 | [-20, -20] |
| generation_s__requirements | Retention policy | 0/5 | 5 | 0 | [-100, -100] |
| generation_s__requirements | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__requirements | Resource stress | 2/5 | 2 | 1 | [-60, -40] |
| generation_s__requirements | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__boundaries | Input policy | 5/5 | 0 | 0 | [+0, +0] |
| generation_s__boundaries | Retention policy | 0/5 | 5 | 0 | [-100, -100] |
| generation_s__boundaries | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| generation_s__boundaries | Resource stress | 5/5 | 0 | 0 | [+0, +0] |
| generation_s__boundaries | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| generation_sfb__none | Input policy | 5/5 | 0 | 0 | Control |
| generation_sfb__none | Retention policy | 4/5 | 1 | 0 | Control |
| generation_sfb__none | Parser robustness | 0/5 | 5 | 0 | Control |
| generation_sfb__none | Resource stress | 5/5 | 0 | 0 | Control |
| generation_sfb__none | Deserialization dispatch | 0/5 | 5 | 0 | Control |
| generation_sfb__overview | Input policy | 5/5 | 0 | 0 | [+0, +0] |
| generation_sfb__overview | Retention policy | 4/5 | 1 | 0 | [+0, +0] |
| generation_sfb__overview | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| generation_sfb__overview | Resource stress | 5/5 | 0 | 0 | [+0, +0] |
| generation_sfb__overview | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| generation_sfb__requirements | Input policy | 5/5 | 0 | 0 | [+0, +0] |
| generation_sfb__requirements | Retention policy | 0/5 | 5 | 0 | [-80, -80] |
| generation_sfb__requirements | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| generation_sfb__requirements | Resource stress | 4/5 | 0 | 1 | [-20, +0] |
| generation_sfb__requirements | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| generation_sfb__boundaries | Input policy | 5/5 | 0 | 0 | [+0, +0] |
| generation_sfb__boundaries | Retention policy | 0/5 | 5 | 0 | [-80, -80] |
| generation_sfb__boundaries | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| generation_sfb__boundaries | Resource stress | 5/5 | 0 | 0 | [+0, +0] |
| generation_sfb__boundaries | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| reuse_b__none | Input policy | 5/5 | 0 | 0 | Control |
| reuse_b__none | Retention policy | 1/5 | 4 | 0 | Control |
| reuse_b__none | Parser robustness | 0/5 | 5 | 0 | Control |
| reuse_b__none | Resource stress | 4/5 | 0 | 1 | Control |
| reuse_b__none | Deserialization dispatch | 0/5 | 5 | 0 | Control |
| reuse_b__overview | Input policy | 5/5 | 0 | 0 | [+0, +0] |
| reuse_b__overview | Retention policy | 2/5 | 3 | 0 | [+20, +20] |
| reuse_b__overview | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| reuse_b__overview | Resource stress | 4/5 | 0 | 1 | [-20, +20] |
| reuse_b__overview | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| reuse_b__requirements | Input policy | 0/5 | 5 | 0 | [-100, -100] |
| reuse_b__requirements | Retention policy | 0/5 | 5 | 0 | [-20, -20] |
| reuse_b__requirements | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| reuse_b__requirements | Resource stress | 1/5 | 1 | 3 | [-80, +0] |
| reuse_b__requirements | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| reuse_b__boundaries | Input policy | 5/5 | 0 | 0 | [+0, +0] |
| reuse_b__boundaries | Retention policy | 1/5 | 4 | 0 | [+0, +0] |
| reuse_b__boundaries | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| reuse_b__boundaries | Resource stress | 0/5 | 2 | 3 | [-100, -20] |
| reuse_b__boundaries | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__none | Input policy | 5/5 | 0 | 0 | Control |
| reuse_sb__none | Retention policy | 2/5 | 3 | 0 | Control |
| reuse_sb__none | Parser robustness | 0/5 | 5 | 0 | Control |
| reuse_sb__none | Resource stress | 4/5 | 0 | 1 | Control |
| reuse_sb__none | Deserialization dispatch | 0/5 | 5 | 0 | Control |
| reuse_sb__overview | Input policy | 5/5 | 0 | 0 | [+0, +0] |
| reuse_sb__overview | Retention policy | 2/5 | 3 | 0 | [+0, +0] |
| reuse_sb__overview | Parser robustness | 1/5 | 4 | 0 | [+20, +20] |
| reuse_sb__overview | Resource stress | 5/5 | 0 | 0 | [+0, +20] |
| reuse_sb__overview | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__requirements | Input policy | 0/5 | 5 | 0 | [-100, -100] |
| reuse_sb__requirements | Retention policy | 0/5 | 5 | 0 | [-40, -40] |
| reuse_sb__requirements | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__requirements | Resource stress | 0/5 | 1 | 4 | [-100, +0] |
| reuse_sb__requirements | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__boundaries | Input policy | 5/5 | 0 | 0 | [+0, +0] |
| reuse_sb__boundaries | Retention policy | 2/5 | 3 | 0 | [+0, +0] |
| reuse_sb__boundaries | Parser robustness | 0/5 | 5 | 0 | [+0, +0] |
| reuse_sb__boundaries | Resource stress | 0/5 | 2 | 3 | [-100, -20] |
| reuse_sb__boundaries | Deserialization dispatch | 0/5 | 5 | 0 | [+0, +0] |

Input and retention policies are separate from parser robustness, resource stress and the deserialization-dispatch canary. This grouping is an exploratory analysis introduced after I04; original counts and complete per-check results remain available. No significance claim or category-weighted combined score is introduced.
