# a03a-none: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from qualified-results.json.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| generation_s__agentic__none | 5 | 5 | 0 / 2 | 0 | 0 | **0** | 9 / 11 / 35 | 10 | 9 | 13 | 24 | 0 | 0 / 0 / 0 | 8.5; 4 |
| reuse_sb__agentic__none | 5 | 5 | 0 / 5 | 0 | 0 | **0** | 11 / 0 / 44 | 10 | 13 | 22 | 22 | 0 | 0 / 0 / 0 | 10; 5 |
