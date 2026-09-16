# a02b-s2: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from qualified-results.json.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| generation_s__single_shot__none | 5 | 3 | 0 / 0 | 0 | 0 | **0** | 0 / 55 / 0 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | None; 0 |
| generation_s__single_shot__static | 5 | 3 | 0 / 0 | 0 | 0 | **0** | 0 / 55 / 0 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | None; 0 |
| reuse_sb__single_shot__none | 5 | 1 | 0 / 0 | 0 | 0 | **0** | 0 / 55 / 0 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | None; 0 |
| reuse_sb__single_shot__static | 5 | 0 | 0 / 0 | 0 | 0 | **0** | 0 / 55 / 0 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | None; 0 |
