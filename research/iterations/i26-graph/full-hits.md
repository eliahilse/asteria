# i26-graph: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from qualified-results.json.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| generation_s__agentic__none | 2 | 2 | 1 / 2 | 0 | 0 | **0** | 10 / 1 / 11 | 8.0 | 3 | 10 | 3 | 0 | 0 / 0 / 0 | 8.0; 2 |
| generation_s__agentic__static | 2 | 2 | 0 / 2 | 0 | 0 | **0** | 4 / 2 / 16 | 7.5 | 1 | 8 | 6 | 0 | 0 / 0 / 0 | 6.0; 2 |
| generation_s__agentic__ast | 2 | 2 | 1 / 2 | 0 | 0 | **0** | 9 / 2 / 11 | 8.5 | 2 | 11 | 4 | 4 | 0 / 0 / 0 | 8.5; 2 |
| generation_s__agentic__static-ast | 2 | 2 | 0 / 2 | 2 | 1 | **1** | 0 / 1 / 21 | 7.5 | 3 | 8 | 4 | 4 | 0 / 0 / 0 | 7.5; 2 |
| generation_s__agentic__static-guard | 2 | 2 | 1 / 2 | 0 | 0 | **0** | 3 / 2 / 17 | 12.5 | 3 | 10 | 6 | 0 | 19 / 8 / 6 | 12.5; 2 |
| generation_s__agentic__static-ast-guard | 2 | 2 | 0 / 1 | 2 | 0 | **0** | 2 / 2 / 18 | 12.0 | 3 | 8 | 7 | 8 | 17 / 8 / 6 | 10.5; 2 |
