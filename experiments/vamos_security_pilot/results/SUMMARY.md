# VaMoS Highscore security-context pilot

Candidate security counts are reported only among compiling outputs. They require source adjudication and are not raw sink-hit vulnerability counts.

| Model | Strategy | S/B/F | Security context | n | Received | Compile | Unit pass | Full functional | Secure functional* | Candidate CWEs among compiled |
|---|---|---|---:|---:|---:|---:|---:|---:|---:|---|
| gemini-3.5-flash-lite | Generation | S | no | 2 | 2 | 0 | 0 | 0 | 0 | n/a |
| gemini-3.5-flash-lite | Generation | S | yes | 2 | 2 | 0 | 0 | 0 | 0 | n/a |
| gemini-3.5-flash-lite | Reuse | S+F+B | no | 2 | 2 | 0 | 0 | 0 | 0 | n/a |
| gemini-3.5-flash-lite | Reuse | S+F+B | yes | 2 | 2 | 0 | 0 | 0 | 0 | n/a |
| gpt-5.4-mini@none | Generation | S | no | 2 | 2 | 2 | 2 | 1 | 0 | CWE-400: 2/2 |
| gpt-5.4-mini@none | Generation | S | yes | 2 | 2 | 1 | 1 | 0 | 0 | none |
| gpt-5.4-mini@none | Reuse | S+F+B | no | 2 | 2 | 1 | 1 | 0 | 0 | CWE-400: 1/1 |
| gpt-5.4-mini@none | Reuse | S+F+B | yes | 2 | 2 | 0 | 0 | 0 | 0 | n/a |

\* Full functional = compile + all unit + invoked + autonomous tests. Secure functional is provisional until candidate findings are manually adjudicated.
