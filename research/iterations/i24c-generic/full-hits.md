# i24c-generic: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from qualified-results.json.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| generation_none__single_shot__none | 5 | 4 | 3 / 3 | 0 | 0 | **0** | 24 / 15 / 16 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | 1.0; 4 |
| generation_none__single_shot__static | 5 | 3 | 2 / 2 | 2 | 0 | **0** | 1 / 25 / 29 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | 1; 3 |
| generation_s__single_shot__none | 5 | 3 | 3 / 3 | 0 | 0 | **0** | 22 / 24 / 9 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | 1; 3 |
| generation_s__single_shot__static | 5 | 5 | 4 / 4 | 0 | 0 | **0** | 6 / 15 / 34 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | 1.0; 4 |
| generation_sfb__single_shot__none | 5 | 3 | 3 / 3 | 0 | 0 | **0** | 22 / 24 / 9 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | 1; 3 |
| generation_sfb__single_shot__static | 5 | 3 | 2 / 2 | 2 | 0 | **0** | 1 / 25 / 29 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | 1; 3 |
| reuse_f__single_shot__none | 5 | 2 | 2 / 2 | 0 | 0 | **0** | 13 / 35 / 7 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | 1.0; 2 |
| reuse_f__single_shot__static | 5 | 3 | 1 / 1 | 2 | 0 | **0** | 1 / 34 / 20 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | 1.0; 2 |
| reuse_sfb__single_shot__none | 5 | 2 | 2 / 2 | 0 | 0 | **0** | 13 / 35 / 7 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | 1.0; 2 |
| reuse_sfb__single_shot__static | 5 | 2 | 2 / 2 | 2 | 1 | **1** | 1 / 33 / 21 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | 1.0; 2 |
