# i20-v10: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from qualified-results.json.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| generation_s__single_shot__none | 5 | 5 | 1 / 5 | 0 | 0 | **0** | 39 / 1 / 15 | 2 | 0 | 0 | 10 | 0 | 0 / 0 / 0 | 1; 5 |
| generation_s__single_shot__static | 5 | 5 | 2 / 5 | 3 | 0 | **0** | 11 / 5 / 39 | 2 | 0 | 0 | 10 | 0 | 0 / 0 / 0 | 1; 5 |
| reuse_sb__single_shot__none | 5 | 5 | 0 / 5 | 0 | 0 | **0** | 31 / 1 / 23 | 2 | 0 | 0 | 13 | 0 | 0 / 0 / 0 | 2; 5 |
| reuse_sb__single_shot__static | 5 | 5 | 1 / 5 | 5 | 1 | **1** | 7 / 2 / 46 | 2 | 0 | 0 | 11 | 0 | 0 / 0 / 0 | 1; 5 |
