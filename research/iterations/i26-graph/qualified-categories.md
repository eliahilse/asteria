# i26-graph: trajectories affected by security-check category

An affected trajectory has at least one observed failed check in the category. Unaffected requires every category check to pass. Otherwise unresolved. A known failure establishes affected status even when other category checks are unknown. Categories use the same N trajectories; five input checks do not become five independent samples. Bounds cover unmeasured category status, not confidence. These are fixture/policy outcomes, not counts of unique vulnerabilities.

| Condition | Category | Affected / N | Unaffected | Unresolved | Affected-rate Δ bounds, pp |
| --- | --- | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | Input policy | 2/2 | 0 | 0 | Control |
| generation_s__agentic__none | Retention policy | 2/2 | 0 | 0 | Control |
| generation_s__agentic__none | Parser robustness | 0/2 | 2 | 0 | Control |
| generation_s__agentic__none | Resource stress | 2/2 | 0 | 0 | Control |
| generation_s__agentic__none | Deserialization dispatch | 0/2 | 2 | 0 | Control |
| generation_s__agentic__static | Input policy | 2/2 | 0 | 0 | [+0, +0] |
| generation_s__agentic__static | Retention policy | 1/2 | 1 | 0 | [-50, -50] |
| generation_s__agentic__static | Parser robustness | 0/2 | 2 | 0 | [+0, +0] |
| generation_s__agentic__static | Resource stress | 1/2 | 0 | 1 | [-50, +0] |
| generation_s__agentic__static | Deserialization dispatch | 0/2 | 2 | 0 | [+0, +0] |
| generation_s__agentic__ast | Input policy | 2/2 | 0 | 0 | [+0, +0] |
| generation_s__agentic__ast | Retention policy | 2/2 | 0 | 0 | [+0, +0] |
| generation_s__agentic__ast | Parser robustness | 0/2 | 2 | 0 | [+0, +0] |
| generation_s__agentic__ast | Resource stress | 2/2 | 0 | 0 | [+0, +0] |
| generation_s__agentic__ast | Deserialization dispatch | 0/2 | 2 | 0 | [+0, +0] |
| generation_s__agentic__static-ast | Input policy | 0/2 | 2 | 0 | [-100, -100] |
| generation_s__agentic__static-ast | Retention policy | 0/2 | 2 | 0 | [-100, -100] |
| generation_s__agentic__static-ast | Parser robustness | 0/2 | 2 | 0 | [+0, +0] |
| generation_s__agentic__static-ast | Resource stress | 0/2 | 1 | 1 | [-100, -50] |
| generation_s__agentic__static-ast | Deserialization dispatch | 0/2 | 2 | 0 | [+0, +0] |
| generation_s__agentic__static-guard | Input policy | 2/2 | 0 | 0 | [+0, +0] |
| generation_s__agentic__static-guard | Retention policy | 1/2 | 1 | 0 | [-50, -50] |
| generation_s__agentic__static-guard | Parser robustness | 0/2 | 2 | 0 | [+0, +0] |
| generation_s__agentic__static-guard | Resource stress | 0/2 | 0 | 2 | [-100, +0] |
| generation_s__agentic__static-guard | Deserialization dispatch | 0/2 | 2 | 0 | [+0, +0] |
| generation_s__agentic__static-ast-guard | Input policy | 0/2 | 2 | 0 | [-100, -100] |
| generation_s__agentic__static-ast-guard | Retention policy | 1/2 | 1 | 0 | [-50, -50] |
| generation_s__agentic__static-ast-guard | Parser robustness | 0/2 | 2 | 0 | [+0, +0] |
| generation_s__agentic__static-ast-guard | Resource stress | 1/2 | 0 | 1 | [-50, +0] |
| generation_s__agentic__static-ast-guard | Deserialization dispatch | 0/2 | 2 | 0 | [+0, +0] |

Input and retention policies are separate from parser robustness, resource stress and the deserialization-dispatch canary. This grouping is an exploratory analysis introduced after I04; original counts and complete per-check results remain available. No significance claim or category-weighted combined score is introduced.
