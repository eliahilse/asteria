# Exact assembled pilot prompts

These are the exact user-message payloads used for all 16 completed calls. Repetitions and models within a condition received identical prompts. Each SHA-256 hash is verified against `vamos_security_pilot/results/run_state.json`.

| File | Strategy | S/B/F | Security context | Characters | SHA-256 |
|---|---|---|---:|---:|---|
| `reuse_sfb.txt` | Reuse | S+F+B | no | 677708 | `82b72918e583f1bb21b40feb87e1d1115210f452393999101f48efd68cb878d9` |
| `reuse_sfb_security.txt` | Reuse | S+F+B | yes | 679432 | `d13a141c375741764eb1a8e8ec44652126ceae7f8fe80d576b0d8def977de1bc` |
| `generation_s.txt` | Generation | S | no | 288561 | `2269a017bff239b221c8211336f68e3a416ca99458cc74390a599ceb87880749` |
| `generation_s_security.txt` | Generation | S | yes | 290285 | `5a59ab252991407162fea4e1e8a0546a89f589523ed277afe7ee9e92d2b5ce31` |

Each text file contains the original VaMoS task prompt, the inlined S/B/F JSON and Java attachments for that condition, the optional security-findings block, and the exact output-format suffix.
