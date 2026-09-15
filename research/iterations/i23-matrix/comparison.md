# i23-matrix: the prior study's Highscore matrix and our one-response rerun

Prior study: Gemini 3.1 Flash Lite, one response per prompt, its pipeline repairs imports and placement before compiling; unit tests are the seven `ApoMarioHighscoreTest` checks, counted over compiled runs only. Ours: Luna, one response, exact-edit delivery (a rejected delivery counts as not compiled), reasoning effort as declared in the round; unit / invoked / autonomous are the 7, 4 and 5 functional tests per trajectory, counted over all N; functional means all sixteen pass; issue checks are failed / unresolved / passed of 10N.

| Method | Context | Prior compiled | Prior unit tests (compiled runs) | Ours delivered | Ours compiled | Ours functional | Unit of 7N | Invoked of 4N | Autonomous of 5N | Issue checks f / u / p |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | --- |
| Generation | None | 1 of 5 | 7 of 7 | 3 of 5 | 3 of 5 | 3 of 5 | 21 of 35 | 12 of 20 | 15 of 25 | 19 / 23 / 8 of 50 |
| Generation | S | 3 of 5 | 21 of 21 | 3 of 5 | 3 of 5 | 3 of 5 | 21 of 35 | 12 of 20 | 15 of 25 | 17 / 23 / 10 of 50 |
| Generation | F | 3 of 5 | 21 of 21 | 2 of 5 | 2 of 5 | 2 of 5 | 14 of 35 | 8 of 20 | 10 of 25 | 13 / 32 / 5 of 50 |
| Generation | B | 2 of 5 | 14 of 14 | 0 of 5 | 0 of 5 | 0 of 5 | 0 of 35 | 0 of 20 | 0 of 25 | 0 / 50 / 0 of 50 |
| Generation | S+F | 2 of 5 | 14 of 14 | 4 of 5 | 3 of 5 | 2 of 5 | 21 of 35 | 11 of 20 | 15 of 25 | 20 / 23 / 7 of 50 |
| Generation | S+B | 2 of 5 | 14 of 14 | 2 of 5 | 2 of 5 | 2 of 5 | 14 of 35 | 8 of 20 | 10 of 25 | 14 / 32 / 4 of 50 |
| Generation | F+B | 4 of 5 | 28 of 28 | 2 of 5 | 2 of 5 | 2 of 5 | 14 of 35 | 8 of 20 | 10 of 25 | 15 / 31 / 4 of 50 |
| Generation | S+F+B | 0 of 5 | 0 of 0 | 3 of 5 | 3 of 5 | 3 of 5 | 21 of 35 | 12 of 20 | 15 of 25 | 15 / 23 / 12 of 50 |
| Reuse | None | 1 of 5 | 7 of 7 | 3 of 5 | 0 of 5 | 0 of 5 | 0 of 35 | 0 of 20 | 0 of 25 | 0 / 50 / 0 of 50 |
| Reuse | S | 3 of 5 | 19 of 21 | 3 of 5 | 3 of 5 | 3 of 5 | 21 of 35 | 12 of 20 | 15 of 25 | 18 / 23 / 9 of 50 |
| Reuse | F | 1 of 5 | 7 of 7 | 4 of 5 | 4 of 5 | 4 of 5 | 28 of 35 | 16 of 20 | 20 of 25 | 26 / 13 / 11 of 50 |
| Reuse | B | 1 of 5 | 7 of 7 | 2 of 5 | 2 of 5 | 2 of 5 | 14 of 35 | 8 of 20 | 10 of 25 | 12 / 32 / 6 of 50 |
| Reuse | S+F | 1 of 5 | 7 of 7 | 3 of 5 | 3 of 5 | 3 of 5 | 21 of 35 | 12 of 20 | 15 of 25 | 20 / 23 / 7 of 50 |
| Reuse | S+B | 4 of 5 | 21 of 21 | 3 of 5 | 3 of 5 | 3 of 5 | 21 of 35 | 12 of 20 | 15 of 25 | 17 / 23 / 10 of 50 |
| Reuse | F+B | 0 of 5 | 0 of 0 | 4 of 5 | 2 of 5 | 1 of 5 | 14 of 35 | 8 of 20 | 5 of 25 | 12 / 31 / 7 of 50 |
| Reuse | S+F+B | 2 of 5 | 14 of 14 | 4 of 5 | 4 of 5 | 3 of 5 | 28 of 35 | 12 of 20 | 20 of 25 | 26 / 13 / 11 of 50 |
| **Generation total** | | 17 of 40 | 119 of 119 | 19 of 40 | 18 of 40 | 17 of 40 | 126 of 280 | 71 of 160 | 90 of 200 | |
| **Reuse total** | | 13 of 40 | 82 of 84 | 26 of 40 | 21 of 40 | 19 of 40 | 147 of 280 | 80 of 160 | 100 of 200 | |
