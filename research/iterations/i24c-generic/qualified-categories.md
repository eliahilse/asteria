# i24c-generic: trajectories affected by security-check category

An affected trajectory has at least one observed failed check in the category. Unaffected requires every category check to pass. Otherwise unresolved. A known failure establishes affected status even when other category checks are unknown. Categories use the same N trajectories; five input checks do not become five independent samples. Bounds cover unmeasured category status, not confidence. These are fixture/policy outcomes, not counts of unique vulnerabilities.

| Condition | Category | Affected / N | Unaffected | Unresolved | Affected-rate Δ bounds, pp |
| --- | --- | ---: | ---: | ---: | ---: |
| generation_none__single_shot__none | Input policy | 4/5 | 0 | 1 | Control |
| generation_none__single_shot__none | Retention policy | 4/5 | 0 | 1 | Control |
| generation_none__single_shot__none | Parser robustness | 0/5 | 4 | 1 | Control |
| generation_none__single_shot__none | Resource stress | 2/5 | 0 | 3 | Control |
| generation_none__single_shot__none | Deserialization dispatch | 0/5 | 4 | 1 | Control |
| generation_none__single_shot__static | Input policy | 1/5 | 2 | 2 | [-80, -20] |
| generation_none__single_shot__static | Retention policy | 0/5 | 3 | 2 | [-100, -40] |
| generation_none__single_shot__static | Parser robustness | 0/5 | 3 | 2 | [-20, +40] |
| generation_none__single_shot__static | Resource stress | 0/5 | 0 | 5 | [-100, +60] |
| generation_none__single_shot__static | Deserialization dispatch | 0/5 | 3 | 2 | [-20, +40] |
| generation_s__single_shot__none | Input policy | 3/5 | 0 | 2 | Control |
| generation_s__single_shot__none | Retention policy | 3/5 | 0 | 2 | Control |
| generation_s__single_shot__none | Parser robustness | 0/5 | 3 | 2 | Control |
| generation_s__single_shot__none | Resource stress | 3/5 | 0 | 2 | Control |
| generation_s__single_shot__none | Deserialization dispatch | 0/5 | 3 | 2 | Control |
| generation_s__single_shot__static | Input policy | 4/5 | 0 | 1 | [-20, +40] |
| generation_s__single_shot__static | Retention policy | 1/5 | 3 | 1 | [-80, -20] |
| generation_s__single_shot__static | Parser robustness | 0/5 | 4 | 1 | [-40, +20] |
| generation_s__single_shot__static | Resource stress | 0/5 | 0 | 5 | [-100, +40] |
| generation_s__single_shot__static | Deserialization dispatch | 0/5 | 4 | 1 | [-40, +20] |
| generation_sfb__single_shot__none | Input policy | 3/5 | 0 | 2 | Control |
| generation_sfb__single_shot__none | Retention policy | 3/5 | 0 | 2 | Control |
| generation_sfb__single_shot__none | Parser robustness | 0/5 | 3 | 2 | Control |
| generation_sfb__single_shot__none | Resource stress | 3/5 | 0 | 2 | Control |
| generation_sfb__single_shot__none | Deserialization dispatch | 0/5 | 3 | 2 | Control |
| generation_sfb__single_shot__static | Input policy | 1/5 | 2 | 2 | [-80, +0] |
| generation_sfb__single_shot__static | Retention policy | 0/5 | 3 | 2 | [-100, -20] |
| generation_sfb__single_shot__static | Parser robustness | 0/5 | 3 | 2 | [-40, +40] |
| generation_sfb__single_shot__static | Resource stress | 0/5 | 0 | 5 | [-100, +40] |
| generation_sfb__single_shot__static | Deserialization dispatch | 0/5 | 3 | 2 | [-40, +40] |
| reuse_f__single_shot__none | Input policy | 2/5 | 0 | 3 | Control |
| reuse_f__single_shot__none | Retention policy | 2/5 | 0 | 3 | Control |
| reuse_f__single_shot__none | Parser robustness | 0/5 | 2 | 3 | Control |
| reuse_f__single_shot__none | Resource stress | 1/5 | 0 | 4 | Control |
| reuse_f__single_shot__none | Deserialization dispatch | 0/5 | 2 | 3 | Control |
| reuse_f__single_shot__static | Input policy | 0/5 | 2 | 3 | [-100, +20] |
| reuse_f__single_shot__static | Retention policy | 1/5 | 1 | 3 | [-80, +40] |
| reuse_f__single_shot__static | Parser robustness | 0/5 | 2 | 3 | [-60, +60] |
| reuse_f__single_shot__static | Resource stress | 0/5 | 1 | 4 | [-100, +60] |
| reuse_f__single_shot__static | Deserialization dispatch | 0/5 | 2 | 3 | [-60, +60] |
| reuse_sfb__single_shot__none | Input policy | 2/5 | 0 | 3 | Control |
| reuse_sfb__single_shot__none | Retention policy | 1/5 | 1 | 3 | Control |
| reuse_sfb__single_shot__none | Parser robustness | 0/5 | 2 | 3 | Control |
| reuse_sfb__single_shot__none | Resource stress | 2/5 | 0 | 3 | Control |
| reuse_sfb__single_shot__none | Deserialization dispatch | 0/5 | 2 | 3 | Control |
| reuse_sfb__single_shot__static | Input policy | 0/5 | 2 | 3 | [-100, +20] |
| reuse_sfb__single_shot__static | Retention policy | 1/5 | 1 | 3 | [-60, +60] |
| reuse_sfb__single_shot__static | Parser robustness | 0/5 | 2 | 3 | [-60, +60] |
| reuse_sfb__single_shot__static | Resource stress | 0/5 | 2 | 3 | [-100, +20] |
| reuse_sfb__single_shot__static | Deserialization dispatch | 0/5 | 2 | 3 | [-60, +60] |

Input and retention policies are separate from parser robustness, resource stress and the deserialization-dispatch canary. This grouping is an exploratory analysis introduced after I04; original counts and complete per-check results remain available. No significance claim or category-weighted combined score is introduced.
