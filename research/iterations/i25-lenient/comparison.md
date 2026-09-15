# i25-lenient: the prior study's Highscore matrix and our one-response rerun

Prior study: Gemini 3.1 Flash Lite, one response per prompt, its pipeline repairs imports and placement before compiling; unit tests are the seven `ApoMarioHighscoreTest` checks, counted over compiled runs only. Ours: Luna, one response, exact-edit delivery (a rejected delivery counts as not compiled), reasoning effort as declared in the round; unit / invoked / autonomous are the 7, 4 and 5 functional tests per trajectory, counted over all N; functional means all sixteen pass; issue checks are failed / unresolved / passed of 10N.

| Method | Context | Prior compiled | Prior unit tests (compiled runs) | Ours delivered | Ours compiled | Ours functional | Unit of 7N | Invoked of 4N | Autonomous of 5N | Issue checks f / u / p |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | --- |
| Generation | None | 1 of 5 | 7 of 7 | 2 of 5 | 1 of 5 | 0 of 5 | 7 of 35 | 0 of 20 | 5 of 25 | 7 / 41 / 2 of 50 |
| Generation | S | 3 of 5 | 21 of 21 | 5 of 5 | 5 of 5 | 3 of 5 | 35 of 35 | 20 of 20 | 15 of 25 | 32 / 5 / 13 of 50 |
| Generation | F | 3 of 5 | 21 of 21 | 4 of 5 | 4 of 5 | 3 of 5 | 28 of 35 | 15 of 20 | 19 of 25 | 28 / 14 / 8 of 50 |
| Generation | B | 2 of 5 | 14 of 14 | 2 of 5 | 2 of 5 | 2 of 5 | 14 of 35 | 8 of 20 | 10 of 25 | 12 / 32 / 6 of 50 |
| Generation | S+F | 2 of 5 | 14 of 14 | 3 of 5 | 3 of 5 | 3 of 5 | 21 of 35 | 12 of 20 | 15 of 25 | 18 / 23 / 9 of 50 |
| Generation | S+B | 2 of 5 | 14 of 14 | 4 of 5 | 4 of 5 | 3 of 5 | 28 of 35 | 15 of 20 | 20 of 25 | 27 / 14 / 9 of 50 |
| Generation | F+B | 4 of 5 | 28 of 28 | 4 of 5 | 4 of 5 | 4 of 5 | 28 of 35 | 16 of 20 | 20 of 25 | 29 / 13 / 8 of 50 |
| Generation | S+F+B | 0 of 5 | 0 of 0 | 3 of 5 | 2 of 5 | 2 of 5 | 14 of 35 | 8 of 20 | 10 of 25 | 8 / 32 / 10 of 50 |
| Reuse | None | 1 of 5 | 7 of 7 | 1 of 5 | 1 of 5 | 1 of 5 | 7 of 35 | 4 of 20 | 5 of 25 | 6 / 41 / 3 of 50 |
| Reuse | S | 3 of 5 | 19 of 21 | 3 of 5 | 2 of 5 | 2 of 5 | 14 of 35 | 8 of 20 | 10 of 25 | 13 / 32 / 5 of 50 |
| Reuse | F | 1 of 5 | 7 of 7 | 1 of 5 | 1 of 5 | 1 of 5 | 7 of 35 | 4 of 20 | 5 of 25 | 6 / 41 / 3 of 50 |
| Reuse | B | 1 of 5 | 7 of 7 | 4 of 5 | 3 of 5 | 3 of 5 | 21 of 35 | 12 of 20 | 15 of 25 | 19 / 23 / 8 of 50 |
| Reuse | S+F | 1 of 5 | 7 of 7 | 4 of 5 | 3 of 5 | 3 of 5 | 21 of 35 | 12 of 20 | 15 of 25 | 18 / 23 / 9 of 50 |
| Reuse | S+B | 4 of 5 | 21 of 21 | 2 of 5 | 2 of 5 | 2 of 5 | 14 of 35 | 8 of 20 | 10 of 25 | 13 / 32 / 5 of 50 |
| Reuse | F+B | 0 of 5 | 0 of 0 | 3 of 5 | 3 of 5 | 3 of 5 | 21 of 35 | 12 of 20 | 15 of 25 | 20 / 22 / 8 of 50 |
| Reuse | S+F+B | 2 of 5 | 14 of 14 | 4 of 5 | 4 of 5 | 3 of 5 | 28 of 35 | 16 of 20 | 15 of 25 | 24 / 14 / 12 of 50 |
| **Generation total** | | 17 of 40 | 119 of 119 | 27 of 40 | 25 of 40 | 20 of 40 | 175 of 280 | 94 of 160 | 114 of 200 | |
| **Reuse total** | | 13 of 40 | 82 of 84 | 22 of 40 | 19 of 40 | 18 of 40 | 133 of 280 | 76 of 160 | 90 of 200 | |
