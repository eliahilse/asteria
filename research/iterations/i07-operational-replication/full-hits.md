# i07-operational-replication: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from qualified-results.json.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| generation_s__none | 5 | 5 | 1 / 5 | 0 | 0 | **0** | 38 / 2 / 15 | 0 | 0 | 0 | 13 | 0 | 0 / 0 / 0 | 1; 5 |
| generation_s__operations | 5 | 5 | 1 / 4 | 0 | 0 | **0** | 18 / 4 / 33 | 0 | 0 | 0 | 13 | 0 | 0 / 0 / 0 | 2; 5 |
| generation_s__requirements | 5 | 5 | 1 / 5 | 0 | 0 | **0** | 10 / 1 / 44 | 0 | 0 | 0 | 15 | 0 | 0 / 0 / 0 | 1; 5 |
| generation_s__boundaries | 5 | 5 | 4 / 4 | 0 | 0 | **0** | 25 / 3 / 27 | 0 | 0 | 0 | 9 | 0 | 0 / 0 / 0 | 1; 5 |
| generation_sfb__none | 5 | 5 | 5 / 5 | 0 | 0 | **0** | 38 / 0 / 17 | 0 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | 1; 5 |
| generation_sfb__operations | 5 | 5 | 1 / 5 | 0 | 0 | **0** | 19 / 2 / 34 | 0 | 0 | 0 | 15 | 0 | 0 / 0 / 0 | 1; 5 |
| generation_sfb__requirements | 5 | 5 | 1 / 4 | 0 | 0 | **0** | 11 / 4 / 40 | 0 | 0 | 0 | 16 | 0 | 0 / 0 / 0 | 1; 5 |
| generation_sfb__boundaries | 5 | 5 | 5 / 5 | 0 | 0 | **0** | 28 / 2 / 25 | 0 | 0 | 0 | 5 | 0 | 0 / 0 / 0 | 1; 5 |
| reuse_b__none | 5 | 5 | 1 / 4 | 0 | 0 | **0** | 27 / 12 / 16 | 0 | 0 | 0 | 14 | 0 | 0 / 0 / 0 | 1.5; 4 |
| reuse_b__operations | 5 | 5 | 0 / 4 | 1 | 0 | **0** | 5 / 15 / 35 | 0 | 0 | 0 | 18 | 0 | 0 / 0 / 0 | 1; 5 |
| reuse_b__requirements | 5 | 5 | 0 / 5 | 5 | 0 | **0** | 4 / 2 / 49 | 0 | 0 | 0 | 18 | 0 | 0 / 0 / 0 | 1; 5 |
| reuse_b__boundaries | 5 | 5 | 1 / 4 | 0 | 0 | **0** | 19 / 3 / 33 | 0 | 0 | 0 | 14 | 0 | 0 / 0 / 0 | 2; 5 |
| reuse_sb__none | 5 | 5 | 4 / 5 | 0 | 0 | **0** | 32 / 1 / 22 | 0 | 0 | 0 | 6 | 0 | 0 / 0 / 0 | 1; 5 |
| reuse_sb__operations | 5 | 5 | 0 / 5 | 0 | 0 | **0** | 8 / 5 / 42 | 0 | 0 | 0 | 18 | 0 | 0 / 0 / 0 | 2; 5 |
| reuse_sb__requirements | 5 | 5 | 0 / 5 | 5 | 0 | **0** | 0 / 5 / 50 | 0 | 0 | 0 | 16 | 0 | 0 / 0 / 0 | 2; 5 |
| reuse_sb__boundaries | 5 | 5 | 2 / 5 | 0 | 0 | **0** | 19 / 2 / 34 | 0 | 0 | 0 | 8 | 0 | 0 / 0 / 0 | 1; 5 |
