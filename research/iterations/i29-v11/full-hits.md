# i29-v11: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from qualified-results.json.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| generation_s__agentic__none | 5 | 5 | 1 / 5 | 0 | 0 | **0** | 25 / 5 / 25 | 8 | 8 | 18 | 13 | 0 | 0 / 0 / 0 | 8; 5 |
| generation_s__agentic__static | 5 | 5 | 2 / 5 | 0 | 0 | **0** | 5 / 5 / 45 | 8 | 11 | 21 | 9 | 0 | 0 / 0 / 0 | 8; 5 |
| generation_s__agentic__static-guard | 5 | 5 | 1 / 4 | 0 | 0 | **0** | 5 / 4 / 46 | 7 | 9 | 16 | 14 | 0 | 9 / 3 / 3 | 7; 5 |
| reuse_sb__agentic__none | 5 | 5 | 2 / 4 | 0 | 0 | **0** | 24 / 14 / 17 | 7 | 8 | 19 | 11 | 0 | 0 / 0 / 0 | 6.5; 4 |
| reuse_sb__agentic__static | 5 | 5 | 4 / 5 | 0 | 0 | **0** | 5 / 0 / 50 | 6 | 7 | 19 | 9 | 0 | 0 / 0 / 0 | 6; 5 |
| reuse_sb__agentic__static-guard | 5 | 5 | 4 / 5 | 0 | 0 | **0** | 5 / 0 / 50 | 9 | 13 | 21 | 8 | 0 | 11 / 5 / 5 | 9; 5 |
