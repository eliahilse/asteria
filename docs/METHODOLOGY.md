# Methodology — security-issue mining on Apo-Games

This documents *how* the security findings were produced and validated, so the
process can be judged, repeated, and criticised independently of the findings
themselves. It is written to be the "methods" half of the working doc.

## System architecture — three models, two roles

The pipeline deliberately separates **orchestration** from **auditing**, and uses
**two independent auditor families** so no model ever checks only its own kind of
output.

```
                    ┌────────────────────────────────────────────┐
                    │  ORCHESTRATOR  —  Claude Fable 5            │
                    │  builds corpus, runs the deterministic      │
                    │  census, fans out the audits, reconciles,   │
                    │  ground-truths + writes the PoCs            │
                    └───────────────┬────────────────────────────┘
                                    │ same prompt, per variant, independently
              ┌─────────────────────┴─────────────────────┐
              ▼                                             ▼
   ┌──────────────────────┐                    ┌──────────────────────────┐
   │  AUDITOR FAMILY A     │                    │  AUDITOR FAMILY B         │
   │  ox-alpha             │                    │  gpt-5.6-sol (codex)      │
   │  via `opencode run`   │                    │  via `codex exec`, max    │
   │  26/26 variants        │                    │  reasoning, read-only     │
   └──────────┬───────────┘                    └───────────┬──────────────┘
              └──────────────────┬──────────────────────────┘
                                 ▼
                 reconcile: agree → corroborated, disagree → lead
                                 ▼
                 orchestrator manually verifies + proves (PoCs)
```

- **Orchestrator — Claude Fable 5.** Runs everything deterministic (corpus build,
  sink census, fan-out, reconciliation, report generation) and does the human-in-
  the-loop work the auditors can't be trusted with: reading the actual source to
  confirm or *reject* a finding, and writing the executable PoCs.
- **Auditor family A — ox-alpha** (`opencode run -m opencode-go/ox-alpha-free`).
  Completed all 26 variants.
- **Auditor family B — gpt-5.6-sol** (`codex exec -m gpt-5.6-sol
  -c model_reasoning_effort=max -c model_service_tier=fast --sandbox read-only`).
  Max reasoning effort. Completed 20/26 (6 refused by a content filter, see §3).

_Effort-parity note:_ family B ran at **max** reasoning; family A ran at
ox-alpha's **default** effort (the audit did not pass `--variant max`). Re-running
family A at max is a ~30-min job and is the one loose end if you want to state
"both at max" in the talk. Conclusions are unlikely to move — the disagreements
were about severity/reachability, not existence.

## 0. Why this corpus

Apo-Games is a family of ~26 cloned Java/Android games (SPLC 2018 case study,
Krüger et al.). Two properties make it the right first substrate for the
security-context question:

- **Clone-and-own reuse is explicit.** The same file recurs, often byte-for-byte,
  across many variants. That lets us measure how a single flaw *propagates*
  through reuse — which is exactly the reuse scenario the project studies.
- **It is already the paper's corpus.** Findings here compose with the existing
  generation/reuse experiments instead of starting a new baseline.

The trade-off (see feasibility verdict) is that these are desktop/Android games,
not networked services, so the threat model is narrower than a web corpus.

## 1. Corpus normalization

Several variants ship their source *inside* the distributed `.jar` rather than
as loose files (7 of 20 Java variants had zero loose `.java`). `build_corpus.sh`
unpacks `*.java` from every jar and merges it with loose sources into one tree
per variant, so every variant is auditable on equal footing.

- Result: **20 Java variants (~232k LOC) + 6 Android (~72k LOC)**, 1686 `.java`
  files after normalization.

## 2. Static sink census (deterministic, no LLM)

`sink_census.py` counts security-relevant API sinks per category and per variant
with fixed regexes (deserialization, custom class loading, plain-HTTP I/O, weak
crypto, native calls, unbounded allocation, etc.).

Purpose: it **bounds where an attack surface can exist at all**, independent of
any model judgement, and it grounds the LLM prompts (the prompt tells the model
which sink families are known to be present). It is corroborating evidence, not
a finding source — a regex hit is not a vulnerability.

## 3. Dual-family LLM audit (the finding source)

Every variant is audited independently by **two unrelated model families**:

- **ox-alpha** via `opencode run`
- **gpt-5.6-sol** via `codex exec` (read-only sandbox)

Neither model sees the other's output. Each is given the same structured prompt
(`audit/prompts/variant_audit.md`) that forces:

- a concrete threat model (malicious server / MITM over the games' plain HTTP;
  malicious or user-edited local save/level/replay files; hostile
  user-generated content downloaded from the shared level server; local cheating),
- every finding to name **entry point → data path → sink**, not generic advice,
- an explicit **reachable vs theoretical** judgement,
- an explicit **validation plan** (what to craft, run, and observe to prove it),
- and honest **"categories checked and absent"** so a credible negative is recorded.

Two families are used deliberately: the researchers' own concern was
"LLMs judging LLMs." Cross-family agreement is the cheap defense against
single-model hallucination. It is necessary but **not sufficient** (see §5).

### Coverage actually obtained (be honest about it)

- **ox-alpha:** completed all 26 variants; 25 parsed cleanly, 1 (`ApoStarz`)
  emitted malformed JSON (unescaped quotes inside a `code_excerpt`) and was
  dropped for that family.
- **gpt-5.6-sol (codex):** 20 of 26 completed; **6 were refused outright by
  OpenAI's cybersecurity content filter** (`"This content was flagged for
  possible cybersecurity risk … Trusted Access for Cyber program"`): `ApoIcarus`,
  `ApoImp`, `ApoPongBeat`, `ApoSkunkman`, `ApoSnake`, `BitsEngineAndroid`.

This is itself a **finding about tooling**, and one the project should record: a
frontier model's safety filter refuses a *defensive* security-audit task on
exactly the security-relevant variants, while the other family completed them.
For research that audits code for weaknesses, model/tool choice is not neutral —
a filtered model silently reduces coverage precisely where coverage matters most.
Net effect on this run: no variant is fully lost — 24 variants have both
families (so corroboration is possible), `ApoStarz` is codex-only, and the 6
refused variants are ox-alpha-only.

## 4. Reconciliation & clone-propagation (deterministic)

`reconcile.py` groups findings by (variant, file, category):

- reported by **both** families → **CORROBORATED**
- reported by **one** family → **SINGLE_SOURCE** (a lead, needs manual proof)

For each finding it also measures clone spread: how many variants ship a file of
the same basename, and how many *distinct* content hashes exist among those
copies. A high spread with one hash = one flaw cloned unchanged across the family.

## 5. Manual ground-truth + executable PoCs (the real validation)

Cross-family agreement filters hallucination but two models can be wrong the
same way, so the top findings were verified by reading the actual source and,
where possible, **demonstrated with a runnable PoC** distilled from the cited
corpus code (`poc/`, `poc/run_all.sh`):

- **PoC 1 — arbitrary code execution** via the AI/bot class-loading feature
  (`ApoIcejumpClassLoader` + `ApoIcejumpPanel.loadPlayer`, cloned as
  `ApoClassLoader`): a downloaded opponent `.class` runs attacker code at
  `newInstance()`. Demonstrated: marker file written by the malicious class.
- **PoC 2 — denial of service** via unchecked allocation in the hand-rolled
  level parser (`ApoCheatingLoadSave.readLevel`): a 14-byte crafted save file
  forces `OutOfMemoryError` / `NegativeArraySizeException`.

### The over-claim we caught

Both the census and at least one model flag `ObjectInputStream` in `ApoCheating`
as **untrusted deserialization (CWE-502 RCE)**. Manual grep shows **zero
`readObject()` call sites anywhere in the corpus** — the stream is only ever read
with `readInt()`. So the classic gadget-chain RCE does **not** apply; the real
defect is unbounded allocation (DoS), which is what PoC 2 actually proves. This
is recorded as a finding *about model behaviour*: presence of a dangerous *type*
was pattern-matched into a dangerous *sink* that isn't reached. It is a concrete
example of why executable validation, not model consensus, is the bar.

Similarly, `MessageDigest`/MD5 appears in the Android `BitsEngine` but is used as
a **bitmap cache key**, not a security check — same API, non-security context.
Severity must be judged from the surrounding context, not the API name.

## 6. What this pipeline is and is not

- **Is:** a reproducible way to surface, cross-check, rank, and *prove* a
  categorical set of security weaknesses, with clone-propagation quantified.
- **Is not:** a guarantee of completeness. It finds categories present in this
  corpus; absence of a category here is about this corpus, not about LLM-written
  code in general. Single-source findings are leads until an execution proves
  them.

## Reproduce

```
scripts/build_corpus.sh          # normalize sources (loose + in-jar)
python3 scripts/sink_census.py   # deterministic sink census
scripts/run_audit.sh oxa   8     # family A audit, all variants
scripts/run_audit.sh codex 8     # family B audit, all variants
python3 scripts/extract_findings.py
python3 scripts/reconcile.py     # corroboration + clone spread + taxonomy
poc/run_all.sh                   # execute the validation PoCs (needs a JDK)
```
