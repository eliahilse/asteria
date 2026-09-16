# i34-notes: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from qualified-results.json.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| generation_s__agentic__notes | 2 | 2 | 0 / 2 | 2 | 0 | **0** | 2 / 2 / 18 | 5.0 | 1 | 3 | 6 | 6 | 0 / 0 / 0 | 4.0; 2 |
| generation_s__agentic__static-notes | 2 | 2 | 0 / 2 | 2 | 0 | **0** | 2 / 2 / 18 | 12.0 | 4 | 11 | 9 | 11 | 0 / 0 / 0 | 9.5; 2 |
| reuse_sb__agentic__notes | 2 | 2 | 1 / 2 | 0 | 0 | **0** | 15 / 0 / 7 | 9.0 | 7 | 6 | 5 | 4 | 0 / 0 / 0 | 9.0; 2 |
| reuse_sb__agentic__static-notes | 2 | 2 | 2 / 2 | 2 | 1 | **1** | 2 / 0 / 20 | 6.5 | 6 | 5 | 2 | 3 | 0 / 0 / 0 | 6.5; 2 |
