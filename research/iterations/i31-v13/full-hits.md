# i31-v13: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from qualified-results.json.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| generation_s__agentic__none | 5 | 5 | 4 / 5 | 0 | 0 | **0** | 26 / 5 / 24 | 8 | 12 | 19 | 6 | 0 | 0 / 0 / 0 | 8; 5 |
| generation_s__agentic__static | 5 | 5 | 2 / 5 | 5 | 0 | **0** | 3 / 5 / 47 | 7 | 6 | 18 | 10 | 0 | 0 / 0 / 0 | 5; 5 |
| generation_s__agentic__static-guard | 5 | 5 | 1 / 4 | 5 | 1 | **1** | 2 / 4 / 49 | 9 | 13 | 16 | 16 | 0 | 13 / 6 / 6 | 9; 5 |
| reuse_sb__agentic__none | 5 | 5 | 2 / 5 | 0 | 0 | **0** | 31 / 5 / 19 | 7 | 5 | 18 | 10 | 0 | 0 / 0 / 0 | 6; 5 |
| reuse_sb__agentic__static | 5 | 5 | 1 / 4 | 5 | 0 | **0** | 2 / 3 / 50 | 9 | 14 | 15 | 15 | 0 | 0 / 0 / 0 | 8; 5 |
| reuse_sb__agentic__static-guard | 5 | 5 | 3 / 4 | 5 | 0 | **0** | 4 / 4 / 47 | 9 | 8 | 22 | 12 | 0 | 14 / 6 / 6 | 7; 5 |
