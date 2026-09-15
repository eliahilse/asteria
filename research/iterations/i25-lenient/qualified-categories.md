# i25-lenient: trajectories affected by security-check category

An affected trajectory has at least one observed failed check in the category. Unaffected requires every category check to pass. Otherwise unresolved. A known failure establishes affected status even when other category checks are unknown. Categories use the same N trajectories; five input checks do not become five independent samples. Bounds cover unmeasured category status, not confidence. These are fixture/policy outcomes, not counts of unique vulnerabilities.

| Condition | Category | Affected / N | Unaffected | Unresolved | Affected-rate Δ bounds, pp |
| --- | --- | ---: | ---: | ---: | ---: |
| generation_none__single_shot__none | Input policy | 1/5 | 0 | 4 | Control |
| generation_none__single_shot__none | Retention policy | 1/5 | 0 | 4 | Control |
| generation_none__single_shot__none | Parser robustness | 0/5 | 1 | 4 | Control |
| generation_none__single_shot__none | Resource stress | 1/5 | 0 | 4 | Control |
| generation_none__single_shot__none | Deserialization dispatch | 0/5 | 1 | 4 | Control |
| generation_s__single_shot__none | Input policy | 5/5 | 0 | 0 | Control |
| generation_s__single_shot__none | Retention policy | 5/5 | 0 | 0 | Control |
| generation_s__single_shot__none | Parser robustness | 0/5 | 5 | 0 | Control |
| generation_s__single_shot__none | Resource stress | 4/5 | 0 | 1 | Control |
| generation_s__single_shot__none | Deserialization dispatch | 0/5 | 5 | 0 | Control |
| generation_f__single_shot__none | Input policy | 4/5 | 0 | 1 | Control |
| generation_f__single_shot__none | Retention policy | 4/5 | 0 | 1 | Control |
| generation_f__single_shot__none | Parser robustness | 0/5 | 4 | 1 | Control |
| generation_f__single_shot__none | Resource stress | 4/5 | 0 | 1 | Control |
| generation_f__single_shot__none | Deserialization dispatch | 0/5 | 4 | 1 | Control |
| generation_b__single_shot__none | Input policy | 2/5 | 0 | 3 | Control |
| generation_b__single_shot__none | Retention policy | 2/5 | 0 | 3 | Control |
| generation_b__single_shot__none | Parser robustness | 0/5 | 2 | 3 | Control |
| generation_b__single_shot__none | Resource stress | 2/5 | 0 | 3 | Control |
| generation_b__single_shot__none | Deserialization dispatch | 0/5 | 2 | 3 | Control |
| generation_sf__single_shot__none | Input policy | 3/5 | 0 | 2 | Control |
| generation_sf__single_shot__none | Retention policy | 3/5 | 0 | 2 | Control |
| generation_sf__single_shot__none | Parser robustness | 0/5 | 3 | 2 | Control |
| generation_sf__single_shot__none | Resource stress | 3/5 | 0 | 2 | Control |
| generation_sf__single_shot__none | Deserialization dispatch | 0/5 | 3 | 2 | Control |
| generation_sb__single_shot__none | Input policy | 4/5 | 0 | 1 | Control |
| generation_sb__single_shot__none | Retention policy | 4/5 | 0 | 1 | Control |
| generation_sb__single_shot__none | Parser robustness | 0/5 | 4 | 1 | Control |
| generation_sb__single_shot__none | Resource stress | 3/5 | 0 | 2 | Control |
| generation_sb__single_shot__none | Deserialization dispatch | 0/5 | 4 | 1 | Control |
| generation_fb__single_shot__none | Input policy | 4/5 | 0 | 1 | Control |
| generation_fb__single_shot__none | Retention policy | 4/5 | 0 | 1 | Control |
| generation_fb__single_shot__none | Parser robustness | 0/5 | 4 | 1 | Control |
| generation_fb__single_shot__none | Resource stress | 4/5 | 0 | 1 | Control |
| generation_fb__single_shot__none | Deserialization dispatch | 0/5 | 4 | 1 | Control |
| generation_sfb__single_shot__none | Input policy | 2/5 | 0 | 3 | Control |
| generation_sfb__single_shot__none | Retention policy | 2/5 | 0 | 3 | Control |
| generation_sfb__single_shot__none | Parser robustness | 0/5 | 2 | 3 | Control |
| generation_sfb__single_shot__none | Resource stress | 2/5 | 0 | 3 | Control |
| generation_sfb__single_shot__none | Deserialization dispatch | 0/5 | 2 | 3 | Control |
| reuse_none__single_shot__none | Input policy | 1/5 | 0 | 4 | Control |
| reuse_none__single_shot__none | Retention policy | 1/5 | 0 | 4 | Control |
| reuse_none__single_shot__none | Parser robustness | 0/5 | 1 | 4 | Control |
| reuse_none__single_shot__none | Resource stress | 0/5 | 0 | 5 | Control |
| reuse_none__single_shot__none | Deserialization dispatch | 0/5 | 1 | 4 | Control |
| reuse_s__single_shot__none | Input policy | 2/5 | 0 | 3 | Control |
| reuse_s__single_shot__none | Retention policy | 2/5 | 0 | 3 | Control |
| reuse_s__single_shot__none | Parser robustness | 0/5 | 2 | 3 | Control |
| reuse_s__single_shot__none | Resource stress | 1/5 | 0 | 4 | Control |
| reuse_s__single_shot__none | Deserialization dispatch | 0/5 | 2 | 3 | Control |
| reuse_f__single_shot__none | Input policy | 1/5 | 0 | 4 | Control |
| reuse_f__single_shot__none | Retention policy | 0/5 | 1 | 4 | Control |
| reuse_f__single_shot__none | Parser robustness | 0/5 | 1 | 4 | Control |
| reuse_f__single_shot__none | Resource stress | 1/5 | 0 | 4 | Control |
| reuse_f__single_shot__none | Deserialization dispatch | 0/5 | 1 | 4 | Control |
| reuse_b__single_shot__none | Input policy | 3/5 | 0 | 2 | Control |
| reuse_b__single_shot__none | Retention policy | 3/5 | 0 | 2 | Control |
| reuse_b__single_shot__none | Parser robustness | 0/5 | 3 | 2 | Control |
| reuse_b__single_shot__none | Resource stress | 1/5 | 0 | 4 | Control |
| reuse_b__single_shot__none | Deserialization dispatch | 0/5 | 3 | 2 | Control |
| reuse_sf__single_shot__none | Input policy | 3/5 | 0 | 2 | Control |
| reuse_sf__single_shot__none | Retention policy | 2/5 | 1 | 2 | Control |
| reuse_sf__single_shot__none | Parser robustness | 0/5 | 3 | 2 | Control |
| reuse_sf__single_shot__none | Resource stress | 1/5 | 0 | 4 | Control |
| reuse_sf__single_shot__none | Deserialization dispatch | 0/5 | 3 | 2 | Control |
| reuse_sb__single_shot__none | Input policy | 2/5 | 0 | 3 | Control |
| reuse_sb__single_shot__none | Retention policy | 2/5 | 0 | 3 | Control |
| reuse_sb__single_shot__none | Parser robustness | 0/5 | 2 | 3 | Control |
| reuse_sb__single_shot__none | Resource stress | 1/5 | 0 | 4 | Control |
| reuse_sb__single_shot__none | Deserialization dispatch | 0/5 | 2 | 3 | Control |
| reuse_fb__single_shot__none | Input policy | 3/5 | 0 | 2 | Control |
| reuse_fb__single_shot__none | Retention policy | 2/5 | 1 | 2 | Control |
| reuse_fb__single_shot__none | Parser robustness | 0/5 | 3 | 2 | Control |
| reuse_fb__single_shot__none | Resource stress | 2/5 | 0 | 3 | Control |
| reuse_fb__single_shot__none | Deserialization dispatch | 0/5 | 3 | 2 | Control |
| reuse_sfb__single_shot__none | Input policy | 4/5 | 0 | 1 | Control |
| reuse_sfb__single_shot__none | Retention policy | 3/5 | 1 | 1 | Control |
| reuse_sfb__single_shot__none | Parser robustness | 0/5 | 4 | 1 | Control |
| reuse_sfb__single_shot__none | Resource stress | 3/5 | 0 | 2 | Control |
| reuse_sfb__single_shot__none | Deserialization dispatch | 0/5 | 4 | 1 | Control |

Input and retention policies are separate from parser robustness, resource stress and the deserialization-dispatch canary. This grouping is an exploratory analysis introduced after I04; original counts and complete per-check results remain available. No significance claim or category-weighted combined score is introduced.
