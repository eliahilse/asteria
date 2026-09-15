# i30-v12: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from qualified-results.json.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| generation_s__agentic__none | 5 | 5 | 2 / 5 | 0 | 0 | **0** | 21 / 5 / 29 | 10 | 11 | 25 | 11 | 0 | 0 / 0 / 0 | 9; 5 |
| generation_s__agentic__static | 5 | 5 | 1 / 5 | 5 | 2 | **2** | 3 / 1 / 51 | 6 | 4 | 18 | 10 | 0 | 0 / 0 / 0 | 6; 5 |
| generation_s__agentic__static-guard | 5 | 5 | 1 / 5 | 5 | 1 | **1** | 5 / 1 / 49 | 11 | 7 | 30 | 12 | 0 | 7 / 2 / 2 | 11; 5 |
| reuse_sb__agentic__none | 5 | 5 | 3 / 5 | 0 | 0 | **0** | 31 / 5 / 19 | 8 | 10 | 21 | 7 | 0 | 0 / 0 / 0 | 8; 5 |
| reuse_sb__agentic__static | 5 | 5 | 3 / 5 | 5 | 2 | **2** | 1 / 2 / 52 | 7 | 8 | 20 | 8 | 0 | 0 / 0 / 0 | 7; 5 |
| reuse_sb__agentic__static-guard | 5 | 5 | 2 / 5 | 5 | 4 | **4** | 0 / 1 / 54 | 7 | 5 | 14 | 8 | 0 | 10 / 5 / 5 | 7; 5 |
