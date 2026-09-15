# i24a-high: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from qualified-results.json.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| generation_none__single_shot__none | 5 | 4 | 1 / 1 | 0 | 0 | **0** | 26 / 4 / 14 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | 1; 3 |
| generation_none__single_shot__static | 5 | 4 | 4 / 4 | 4 | 1 | **1** | 0 / 3 / 41 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | None; 0 |
| generation_s__single_shot__none | 5 | 4 | 4 / 4 | 0 | 0 | **0** | 28 / 4 / 12 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | None; 0 |
| generation_s__single_shot__static | 5 | 1 | 1 / 1 | 1 | 0 | **0** | 0 / 1 / 10 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | None; 0 |
| generation_sfb__single_shot__none | 5 | 3 | 3 / 3 | 0 | 0 | **0** | 20 / 2 / 11 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | None; 0 |
| generation_sfb__single_shot__static | 5 | 2 | 2 / 2 | 2 | 0 | **0** | 1 / 2 / 19 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | None; 0 |
| reuse_f__single_shot__none | 5 | 1 | 0 / 0 | 0 | 0 | **0** | 8 / 0 / 3 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | 1; 1 |
| reuse_f__single_shot__static | 5 | 3 | 2 / 2 | 3 | 0 | **0** | 1 / 3 / 29 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | 1; 1 |
| reuse_sfb__single_shot__none | 5 | 3 | 2 / 2 | 0 | 0 | **0** | 20 / 3 / 10 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | 1; 1 |
| reuse_sfb__single_shot__static | 5 | 1 | 1 / 1 | 1 | 0 | **0** | 1 / 1 / 9 | 1 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | None; 0 |
