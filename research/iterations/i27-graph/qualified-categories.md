# i27-graph: trajectories affected by security-check category

An affected trajectory has at least one observed failed check in the category. Unaffected requires every category check to pass. Otherwise unresolved. A known failure establishes affected status even when other category checks are unknown. Categories use the same N trajectories; five input checks do not become five independent samples. Bounds cover unmeasured category status, not confidence. These are fixture/policy outcomes, not counts of unique vulnerabilities.

| Condition | Category | Affected / N | Unaffected | Unresolved | Affected-rate Δ bounds, pp |
| --- | --- | ---: | ---: | ---: | ---: |
| generation_s__agentic__none | Input policy | 3/3 | 0 | 0 | Control |
| generation_s__agentic__none | Retention policy | 1/3 | 2 | 0 | Control |
| generation_s__agentic__none | Parser robustness | 1/3 | 2 | 0 | Control |
| generation_s__agentic__none | Resource stress | 3/3 | 0 | 0 | Control |
| generation_s__agentic__none | Deserialization dispatch | 0/3 | 3 | 0 | Control |
| generation_s__agentic__static | Input policy | 0/3 | 3 | 0 | [-100, -100] |
| generation_s__agentic__static | Retention policy | 2/3 | 1 | 0 | [+33, +33] |
| generation_s__agentic__static | Parser robustness | 0/3 | 3 | 0 | [-33, -33] |
| generation_s__agentic__static | Resource stress | 2/3 | 0 | 1 | [-33, +0] |
| generation_s__agentic__static | Deserialization dispatch | 0/3 | 3 | 0 | [+0, +0] |
| generation_s__agentic__static-ast | Input policy | 1/3 | 1 | 1 | [-67, -33] |
| generation_s__agentic__static-ast | Retention policy | 0/3 | 2 | 1 | [-33, +0] |
| generation_s__agentic__static-ast | Parser robustness | 0/3 | 2 | 1 | [-33, +0] |
| generation_s__agentic__static-ast | Resource stress | 0/3 | 0 | 3 | [-100, +0] |
| generation_s__agentic__static-ast | Deserialization dispatch | 0/3 | 2 | 1 | [+0, +33] |
| generation_s__agentic__static-ast-guard | Input policy | 2/3 | 1 | 0 | [-33, -33] |
| generation_s__agentic__static-ast-guard | Retention policy | 1/3 | 2 | 0 | [+0, +0] |
| generation_s__agentic__static-ast-guard | Parser robustness | 0/3 | 3 | 0 | [-33, -33] |
| generation_s__agentic__static-ast-guard | Resource stress | 1/3 | 0 | 2 | [-67, +0] |
| generation_s__agentic__static-ast-guard | Deserialization dispatch | 0/3 | 3 | 0 | [+0, +0] |
| generation_s__agentic__static-ast-advise | Input policy | 1/3 | 1 | 1 | [-67, -33] |
| generation_s__agentic__static-ast-advise | Retention policy | 0/3 | 2 | 1 | [-33, +0] |
| generation_s__agentic__static-ast-advise | Parser robustness | 0/3 | 2 | 1 | [-33, +0] |
| generation_s__agentic__static-ast-advise | Resource stress | 1/3 | 0 | 2 | [-67, +0] |
| generation_s__agentic__static-ast-advise | Deserialization dispatch | 0/3 | 2 | 1 | [+0, +33] |

Input and retention policies are separate from parser robustness, resource stress and the deserialization-dispatch canary. This grouping is an exploratory analysis introduced after I04; original counts and complete per-check results remain available. No significance claim or category-weighted combined score is introduced.
