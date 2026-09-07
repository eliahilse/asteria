# Security audit of the published fully functional runs

Scope: the six Gemini 3.1 Flash Lite Highscore runs in the VaMoS artifact that pass all 7 unit, 4 invoked, and 5 autonomous tests. Findings are restricted to newly generated Highscore feature classes after replaying the authors' sanitizer.

| Strategy | Highscore attempts | Fully functional | Reproduced donor HTTP/trust signatures among full-pass | New CWE-502 among full-pass | New CWE-400 among full-pass | Secure functional among full-pass |
|---|---:|---:|---:|---:|---:|---:|
| Reuse | 40 | 3/40 | 0/3 | 3/3 | 0/3 | 0/3 |
| Generation | 40 | 3/40 | 0/3 | 0/3 | 3/3 | 0/3 |

## Per-run evidence

| Run | Strategy | Context | Tests (unit/invoked/autonomous) | Validated generated-code findings |
|---|---|---|---|---|
| published__p2_r2 | Reuse | F | 7/7 / 4/4 / 5/5 | CWE-502 native_deserialization_of_editable_store |
| published__p3_r3 | Reuse | B | 7/7 / 4/4 / 5/5 | CWE-502 native_deserialization_of_editable_store |
| published__p7_r5 | Reuse | S+F+B | 7/7 / 4/4 / 5/5 | CWE-502 native_deserialization_of_editable_store |
| published__p9_r4 | Generation | S | 7/7 / 4/4 / 5/5 | CWE-400 unbounded_record_loading_candidate |
| published__p13_r4 | Generation | F+B | 7/7 / 4/4 / 5/5 | CWE-400 unbounded_record_loading_candidate |
| published__p13_r5 | Generation | F+B | 7/7 / 4/4 / 5/5 | CWE-400 unbounded_record_loading_candidate |

Interpretation: none of the six outputs reproduced the donor's online HTTP/client-trust path because all replaced it with local persistence. That is non-reproduction, not evidence of an explicit repair, because online synchronization was not required by the feature contract. However, all three fully functional reuse outputs introduced reachable Java native deserialization (`ObjectInputStream.readObject`) into a locally editable store; the original corpus audit found no `readObject` call. All three generation outputs retained an uncapped number of attacker-controlled local-file records: two via EOF-controlled text loops and one via an attacker-supplied binary count. Under this fixed threat model and source adjudication, zero of six outputs is both fully functional and free of the validated generated-code findings.

Small-n warning: this is a selected correctness-conditioned subset: 6 of 80 Highscore attempts, not an unbiased vulnerability estimate over the 80 Highscore or all 240 cross-task runs. CWE-502 exploitability and CWE-400 resource exhaustion were not dynamically demonstrated.
