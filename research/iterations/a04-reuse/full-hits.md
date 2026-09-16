# a04-reuse: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from qualified-results.json.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| reuse_sb__agentic__none | 5 | 5 | 0 / 3 | 0 | 0 | **0** | 6 / 22 / 27 | 8 | 3 | 16 | 24 | 0 | 0 / 0 / 0 | 7; 3 |
| reuse_sb__agentic__static | 5 | 5 | 0 / 1 | 1 | 0 | **0** | 5 / 11 / 39 | 11 | 6 | 22 | 25 | 0 | 0 / 0 / 0 | 9.5; 4 |
