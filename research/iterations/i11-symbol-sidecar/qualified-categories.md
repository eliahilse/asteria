# i11-symbol-sidecar: trajectories affected by security-check category

An affected trajectory has at least one observed failed check in the category. Unaffected requires every category check to pass. Otherwise unresolved. A known failure establishes affected status even when other category checks are unknown. Categories use the same N trajectories; five input checks do not become five independent samples. Bounds cover unmeasured category status, not confidence. These are fixture/policy outcomes, not counts of unique vulnerabilities.

| Condition | Category | Affected / N | Unaffected | Unresolved | Affected-rate Δ bounds, pp |
| --- | --- | ---: | ---: | ---: | ---: |
| generation_s__single_shot__none | Input policy | 3/3 | 0 | 0 | Control |
| generation_s__single_shot__none | Retention policy | 3/3 | 0 | 0 | Control |
| generation_s__single_shot__none | Parser robustness | 0/3 | 3 | 0 | Control |
| generation_s__single_shot__none | Resource stress | 3/3 | 0 | 0 | Control |
| generation_s__single_shot__none | Deserialization dispatch | 0/3 | 3 | 0 | Control |
| generation_s__single_shot__static | Input policy | 2/3 | 1 | 0 | [-33, -33] |
| generation_s__single_shot__static | Retention policy | 0/3 | 3 | 0 | [-100, -100] |
| generation_s__single_shot__static | Parser robustness | 0/3 | 3 | 0 | [+0, +0] |
| generation_s__single_shot__static | Resource stress | 1/3 | 0 | 2 | [-67, +0] |
| generation_s__single_shot__static | Deserialization dispatch | 0/3 | 3 | 0 | [+0, +0] |
| generation_s__agentic__adaptive | Input policy | 1/3 | 2 | 0 | [-67, -67] |
| generation_s__agentic__adaptive | Retention policy | 0/3 | 3 | 0 | [-100, -100] |
| generation_s__agentic__adaptive | Parser robustness | 0/3 | 3 | 0 | [+0, +0] |
| generation_s__agentic__adaptive | Resource stress | 0/3 | 1 | 2 | [-100, -33] |
| generation_s__agentic__adaptive | Deserialization dispatch | 0/3 | 3 | 0 | [+0, +0] |

Input and retention policies are separate from parser robustness, resource stress and the deserialization-dispatch canary. This grouping is an exploratory analysis introduced after I04; original counts and complete per-check results remain available. No significance claim or category-weighted combined score is introduced.
