# a03d-s3: full hits per arm

A full hit is an artifact that passes all sixteen functional tests and all eleven security checks (the ten issue checks and the persistence round trip). Security clean: all eleven pass, functional or not. Input policy clean: the five rejection checks pass. Counts are of N; checks are failed / unresolved / passed of 11N. Outcomes from qualified-results.json.

| Arm | N | Compiled | Functional first / budget | Input policy clean | Security clean | Full hits | Checks f / u / p | Tool turns (median) | Reads | Searches | Submissions | Injections | Guard consulted / positive / cancelled | First compiling turn (median; artifacts) |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | ---: | ---: | ---: | --- | --- |
| generation_s__agentic__static | 5 | 5 | 0 / 3 | 2 | 1 | **1** | 4 / 11 / 40 | 10 | 14 | 16 | 25 | 0 | 0 / 0 / 0 | 10.5; 4 |
| reuse_sb__agentic__static | 5 | 5 | 0 / 2 | 3 | 3 | **2** | 0 / 22 / 33 | 11 | 7 | 26 | 23 | 0 | 0 / 0 / 0 | 10; 3 |
