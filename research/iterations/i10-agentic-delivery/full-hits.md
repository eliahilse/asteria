# i10-agentic-delivery: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| generation_s__single_shot__none | 5 | 5 | 3 / 5 | 0 | 0 | **0** | 33 / 3 / 19 | 1 | 0 | 0 | 7 | 0 | 0 / 0 / 0 | 1; 1 |
| generation_s__single_shot__static | 5 | 5 | 0 / 2 | 1 | 0 | **0** | 5 / 2 / 48 | 5 | 0 | 0 | 23 | 0 | 0 / 0 / 0 | 2; 5 |
| generation_s__agentic__none | 5 | 5 | 0 / 5 | 0 | 0 | **0** | 32 / 1 / 22 | 9 | 3 | 21 | 16 | 0 | 0 / 0 / 0 | 7.0; 4 |
| generation_s__agentic__static | 5 | 5 | 0 / 1 | 2 | 2 | **1** | 3 / 2 / 50 | 5 | 2 | 12 | 24 | 0 | 0 / 0 / 0 | 3; 5 |
| generation_s__agentic__adaptive | 5 | 5 | 0 / 3 | 0 | 0 | **0** | 8 / 0 / 47 | 9 | 9 | 15 | 21 | 10 | 0 / 0 / 0 | 6; 5 |
| reuse_sb__single_shot__none | 5 | 5 | 4 / 5 | 0 | 0 | **0** | 35 / 0 / 20 | 1 | 0 | 0 | 6 | 0 | 0 / 0 / 0 | 1; 1 |
| reuse_sb__single_shot__static | 5 | 5 | 2 / 4 | 5 | 1 | **1** | 4 / 0 / 51 | 2 | 0 | 0 | 13 | 0 | 0 / 0 / 0 | 1; 3 |
| reuse_sb__agentic__none | 5 | 5 | 2 / 5 | 0 | 0 | **0** | 35 / 0 / 20 | 8 | 5 | 20 | 11 | 0 | 0 / 0 / 0 | 8.0; 2 |
| reuse_sb__agentic__static | 5 | 5 | 1 / 4 | 5 | 0 | **0** | 10 / 3 / 42 | 8 | 6 | 20 | 17 | 0 | 0 / 0 / 0 | 6.5; 4 |
| reuse_sb__agentic__adaptive | 5 | 5 | 0 / 4 | 1 | 0 | **0** | 23 / 11 / 21 | 8 | 3 | 16 | 19 | 5 | 0 / 0 / 0 | 3.5; 4 |
