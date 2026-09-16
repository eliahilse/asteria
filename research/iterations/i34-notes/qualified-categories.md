# i34-notes: trajectories affected by security-check category

An affected trajectory has at least one observed failed check in the category. Unaffected requires every category check to pass. Otherwise unresolved. A known failure establishes affected status even when other category checks are unknown. Categories use the same N trajectories; five input checks do not become five independent samples. Bounds cover unmeasured category status, not confidence. These are fixture/policy outcomes, not counts of unique vulnerabilities.

| Condition | Category | Affected / N | Unaffected | Unresolved | Affected-rate Δ bounds, pp |
| --- | --- | ---: | ---: | ---: | ---: |
| generation_s__agentic__notes | Input policy | 0/2 | 2 | 0 | no control arm in this round |
| generation_s__agentic__notes | Retention policy | 0/2 | 2 | 0 | no control arm in this round |
| generation_s__agentic__notes | Parser robustness | 0/2 | 2 | 0 | no control arm in this round |
| generation_s__agentic__notes | Resource stress | 2/2 | 0 | 0 | no control arm in this round |
| generation_s__agentic__notes | Deserialization dispatch | 0/2 | 2 | 0 | no control arm in this round |
| generation_s__agentic__static-notes | Input policy | 0/2 | 2 | 0 | no control arm in this round |
| generation_s__agentic__static-notes | Retention policy | 0/2 | 2 | 0 | no control arm in this round |
| generation_s__agentic__static-notes | Parser robustness | 0/2 | 2 | 0 | no control arm in this round |
| generation_s__agentic__static-notes | Resource stress | 2/2 | 0 | 0 | no control arm in this round |
| generation_s__agentic__static-notes | Deserialization dispatch | 0/2 | 2 | 0 | no control arm in this round |
| reuse_sb__agentic__notes | Input policy | 2/2 | 0 | 0 | no control arm in this round |
| reuse_sb__agentic__notes | Retention policy | 1/2 | 1 | 0 | no control arm in this round |
| reuse_sb__agentic__notes | Parser robustness | 0/2 | 2 | 0 | no control arm in this round |
| reuse_sb__agentic__notes | Resource stress | 2/2 | 0 | 0 | no control arm in this round |
| reuse_sb__agentic__notes | Deserialization dispatch | 0/2 | 2 | 0 | no control arm in this round |
| reuse_sb__agentic__static-notes | Input policy | 0/2 | 2 | 0 | no control arm in this round |
| reuse_sb__agentic__static-notes | Retention policy | 0/2 | 2 | 0 | no control arm in this round |
| reuse_sb__agentic__static-notes | Parser robustness | 0/2 | 2 | 0 | no control arm in this round |
| reuse_sb__agentic__static-notes | Resource stress | 1/2 | 1 | 0 | no control arm in this round |
| reuse_sb__agentic__static-notes | Deserialization dispatch | 0/2 | 2 | 0 | no control arm in this round |

Input and retention policies are separate from parser robustness, resource stress and the deserialization-dispatch canary. This grouping is an exploratory analysis introduced after I04; original counts and complete per-check results remain available. No significance claim or category-weighted combined score is introduced.
