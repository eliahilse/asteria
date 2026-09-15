# i28-graph: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from qualified-results.json.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| generation_s__agentic__none | 5 | 5 | 2 / 5 | 0 | 0 | **0** | 29 / 5 / 21 | 10 | 10 | 24 | 12 | 0 | 0 / 0 / 0 | 8; 5 |
| generation_s__agentic__static | 5 | 5 | 0 / 5 | 3 | 0 | **0** | 6 / 5 / 44 | 8 | 9 | 20 | 12 | 0 | 0 / 0 / 0 | 8; 5 |
| generation_s__agentic__static-ast | 5 | 5 | 1 / 5 | 3 | 0 | **0** | 5 / 5 / 45 | 8 | 8 | 19 | 14 | 25 | 0 / 0 / 0 | 8; 5 |
| generation_s__agentic__static-ast-guard | 5 | 5 | 1 / 5 | 4 | 0 | **0** | 5 / 5 / 45 | 7 | 4 | 18 | 14 | 21 | 12 / 5 / 5 | 7; 5 |
| generation_s__agentic__static-ast-advise | 5 | 5 | 3 / 4 | 4 | 0 | **0** | 8 / 4 / 43 | 10 | 9 | 17 | 22 | 25 | 14 / 11 / 0 | 6; 5 |
| reuse_sb__agentic__none | 5 | 5 | 0 / 5 | 0 | 0 | **0** | 28 / 4 / 23 | 8 | 7 | 23 | 11 | 0 | 0 / 0 / 0 | 8; 5 |
| reuse_sb__agentic__static | 5 | 5 | 0 / 5 | 5 | 0 | **0** | 6 / 3 / 46 | 9 | 11 | 17 | 14 | 0 | 0 / 0 / 0 | 7; 5 |
| reuse_sb__agentic__static-ast | 5 | 5 | 2 / 5 | 4 | 0 | **0** | 7 / 3 / 45 | 6 | 7 | 18 | 11 | 19 | 0 / 0 / 0 | 5; 5 |
| reuse_sb__agentic__static-ast-guard | 5 | 5 | 1 / 5 | 5 | 0 | **0** | 4 / 2 / 49 | 8 | 13 | 20 | 14 | 25 | 14 / 6 / 6 | 8; 5 |
| reuse_sb__agentic__static-ast-advise | 5 | 5 | 2 / 5 | 4 | 0 | **0** | 6 / 1 / 48 | 11 | 14 | 25 | 20 | 27 | 11 / 7 / 0 | 9; 5 |
