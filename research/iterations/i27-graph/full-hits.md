# i27-graph: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from qualified-results.json.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| generation_s__agentic__none | 3 | 3 | 2 / 3 | 0 | 0 | **0** | 17 / 2 / 14 | 7 | 7 | 11 | 4 | 0 | 0 / 0 / 0 | 7; 3 |
| generation_s__agentic__static | 3 | 3 | 2 / 3 | 3 | 0 | **0** | 4 / 3 / 26 | 4 | 2 | 9 | 5 | 0 | 0 / 0 / 0 | 4; 3 |
| generation_s__agentic__static-ast | 3 | 2 | 1 / 2 | 1 | 0 | **0** | 1 / 13 / 19 | 11 | 3 | 14 | 8 | 15 | 0 / 0 / 0 | 6.5; 2 |
| generation_s__agentic__static-ast-guard | 3 | 3 | 0 / 2 | 1 | 0 | **0** | 4 / 3 / 26 | 10 | 11 | 12 | 8 | 13 | 7 / 3 / 3 | 10; 3 |
| generation_s__agentic__static-ast-advise | 3 | 2 | 0 / 2 | 1 | 0 | **0** | 2 / 13 / 18 | 6 | 5 | 6 | 4 | 5 | 2 / 2 / 0 | 7.5; 2 |
