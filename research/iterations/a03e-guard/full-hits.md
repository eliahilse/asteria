# a03e-guard: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from qualified-results.json.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| generation_s__agentic__static-guard | 5 | 5 | 0 / 1 | 2 | 1 | **0** | 1 / 33 / 21 | 11 | 12 | 24 | 24 | 0 | 15 / 5 / 5 | 15.5; 2 |
| reuse_sb__agentic__static-guard | 5 | 5 | 0 / 3 | 1 | 1 | **1** | 5 / 11 / 39 | 14 | 9 | 25 | 24 | 0 | 19 / 5 / 5 | 10.5; 4 |
