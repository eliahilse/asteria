# Security Context for LLM-Assisted Code — Apo-Games Feasibility Study

**Author:** Elia · **Status:** working doc for review before next meeting
**One-line result:** Apo-Games is a viable security testbed for the *reuse* track
of the project — real security flaws are cloned unchanged across the family — and
should be paired with an exposed-surface corpus (Prof. Wellig's) for the
*generation* track. First audit already yields 41 cross-model-corroborated
findings and two proven exploits.

> This is a **recon / feasibility** pass, not the full experiment. Its job is to
> answer "can this corpus carry the security angle, and what's actually in it?"
> The actual context-injection experiment is Section 8.

---

## 1. Why Apo-Games

Apo-Games is a family of **26 cloned Java/Android games** (SPLC 2018 case study,
Krüger et al.) — the same corpus the project's paper already uses. Two properties
make it the right first substrate for the *security-context* question:

- **Clone-and-own reuse is explicit.** The same files recur across variants,
  often byte-for-byte. This lets us *measure* how a single security flaw
  propagates through reuse — exactly the reuse scenario the project studies.
- **It composes with existing work** instead of starting a new baseline.

The trade-off: these are desktop/Android games, not networked services, so the
threat model is narrower than a web corpus (see Section 7).

Corpus after normalization: **20 Java (~232k LOC) + 6 Android (~72k LOC)**, 1686
`.java` files. (7 variants ship their source *inside* the distributed jar; the
pipeline unpacks those so every variant is audited equally.)

## 2. Threat model (what "security" means for a game)

No servers, so the attacker is not "someone hitting an endpoint." It is:

1. **Malicious / MITM server** — the games talk to `apo-games.de` over **plain
   HTTP** (highscore + level up/download): no TLS, no auth, no integrity.
2. **Malicious local file** — hand-rolled save / level / replay / cheat formats
   (`.cheat`, `.mar`, `.skunk`, `.defence`, `.rep`) parsed by hand.
3. **Hostile user-generated content** — levels downloaded from the shared level
   server are exactly these untrusted files, parsed by the same code.
4. **Local tampering to cheat** — "trust the client" scoring.

Severity below is judged against *this* model; we do not inflate findings by
pretending a web endpoint exists.

## 3. Method (how the findings were produced and validated)

The pipeline separates **orchestration** from **auditing**, and uses **two
independent auditor families** so no model checks only its own kind of output.

- **Orchestrator — Claude Fable 5.** Runs everything deterministic (corpus build,
  static sink census, fan-out, reconciliation) and does the human-in-the-loop
  work: reading the actual source to confirm or *reject* findings, and writing
  the executable proofs.
- **Auditor family A — ox-alpha.** Auditor family B — **gpt-5.6-sol** at max
  reasoning. Each audits every variant independently, never seeing the other's
  output, using one structured prompt that forces: a concrete threat model;
  entry-point → data-path → sink for every finding; an explicit *reachable vs
  theoretical* call; a *validation plan*; and honest "categories checked and
  absent."
- **Reconciliation.** A finding reported by **both** families is promoted to
  *corroborated*; single-family findings stay *leads*. Clone propagation is then
  measured deterministically (how many variants ship the same file; how many
  distinct versions exist).
- **Ground-truth + proof.** The top findings were verified by reading the source
  and reduced to **runnable PoCs**.

_(An open-weight baseline pass — GLM / Kimi / Qwen / MiniMax — is a further
enhancement in progress; it strengthens corroboration and satisfies guideline #6
below. Numbers here are the completed two-family results.)_

## 4. Results

**189 raw findings → 144 distinct issues → 41 corroborated by both families.**

| Category | Variants affected | Corroborated | Max clone spread |
|---|--:|--:|--:|
| Remote/local code loading | 18 | 13 | 17 |
| Insecure transport (plain HTTP) | 21 | 11 | 17 |
| Unsafe custom parser / unbounded alloc | 19 | 9 | 17 |
| Client-side trust (scores/progress) | 15 | 5 | 9 |
| Resource exhaustion | 21 | 1 | 17 |
| Path traversal / unsafe native call | 3 / 2 | 1 / 1 | 2 / 1 |

Categories actively looked for and **absent**: OS command exec, SQL, XXE/XML,
Android WebView/Intent injection — consistent with "offline game, no server."

### Headline findings

**F1 — Arbitrary code execution via the AI/bot class-loading feature · critical.**
The "load AI player" feature loads a `.class` from a caller-chosen path and
`newInstance()`s it (`ApoIcejumpClassLoader` + `ApoIcejumpPanel.loadPlayer`);
the cloned `ApoClassLoader` does the same over an arbitrary **URL**. Several
variants persist the AI class + path in a startup config file, so tampering a
plain `.properties` file executes attacker code **on next launch**, no user
action. The vulnerable `ApoClassLoader` is **byte-identical across ~11 variants**
— one flaw cloned across the family. **Proven** (PoC 1). CWE-470/494/829.

**F2 — Denial of service via unchecked allocation in the level parser · medium–high.**
`ApoCheatingLoadSave.readLevel` reads array dimensions straight from an untrusted
file and does `new int[y][x]` with no bounds check. A **14-byte** crafted
save/level file forces `OutOfMemoryError` / `NegativeArraySizeException`,
reachable via the downloaded-level path. **Proven** (PoC 2). CWE-789.

**F3 — Insecure transport + client-side trust · high (integrity).**
Highscores and shared levels are POSTed over plain HTTP with no signature or
nonce; the client simply asserts its own score. Eavesdrop/MITM/forge/replay all
apply, and downloaded levels then feed the F2 parser. Mechanism confirmed from
source (live PoC needs the now-defunct server). CWE-319/602/345.

## 5. Two methodology findings the group will care about

These are direct evidence for the project's core thesis — **security judgement
needs context, not API-pattern matching** — and for why executable proof, not
model consensus, is the bar:

- **A caught over-claim.** Both the static census and a model flagged
  `ObjectInputStream` in `ApoCheating` as *untrusted-deserialization RCE
  (CWE-502)*. Manual grep found **zero `readObject()` calls anywhere in the
  corpus** — the stream is only read with `readInt()`, so the gadget-chain RCE
  does not apply; the real defect is the DoS in F2. Only reading the code caught
  it.
- **A tooling finding.** gpt-5.6-sol was **refused by OpenAI's cybersecurity
  content filter on 6 of 26 variants** — exactly the security-relevant ones —
  while ox-alpha completed all 26. For research that audits code for weaknesses,
  a frontier model's safety filter silently reduces coverage precisely where it
  matters; model/tool choice is a methodological variable.

## 6. Fit with the shared references

- Yetiştiren et al., *Evaluating the Code Quality of AI-Assisted Code Generation
  Tools* (arXiv:2304.10778) — scores Copilot/CodeWhisperer/ChatGPT on **HumanEval
  single functions**: no existing system, no reuse, no cross-file context. That
  is exactly the gap this project fills.
- *Guidelines for Empirical Studies in SE involving LLMs* (arXiv:2508.15503,
  llm-guidelines.org) — this method already satisfies ~6 of the 8 guidelines
  (declare usage/role; report versions/config; document architecture; disclose
  prompts + logs; human validation; articulate limitations). **Gap:** an
  open-weight-model baseline (#6) — which the in-progress open-model pass closes.

## 7. Verdict & recommendation

- **Adopt Apo-Games for the reuse / propagation security experiments.** It is the
  only corpus on the table where a *real* flaw can be measured propagating across
  a family (F1 cloned in ~11 variants). Paper-worthy on its own, and it plugs
  straight into the existing reuse scenarios.
- **Do not rely on it for the generation security experiments** — no server
  surface means whole vulnerability classes can't appear. Pair with an
  exposed-surface corpus: **Prof. Wellig's repository** (bonus: likely outside
  frontier-LLM training data) and/or a public set.

## 8. Proposed next steps

1. Finalize the corroborated-findings set + the open-model baseline → a clean
   "validated security-issue set" for the corpus.
2. **The actual experiment:** inject reuse/security context and measure whether
   an LLM *propagates* or *fixes* the known byte-identical vulnerable component
   (F1) when generating a sibling variant. F1's clone spread is the seed.
3. Request Wellig's corpus; run the same pipeline to compare a game corpus vs an
   exposed-surface corpus for the generation track.
4. **Knowledge-graph bridge:** the clone-propagation result is a graph property —
   a security-annotated KG where the vulnerable node replicates across variant
   subgraphs visualizes flaw propagation through reuse (connects to Abdul's KG
   thread).

---

### Appendix — reproduce
```
scripts/build_corpus.sh              # normalize sources (loose + in-jar)
python3 scripts/sink_census.py       # deterministic sink census
scripts/run_audit.sh oxa 8           # family A
scripts/run_audit.sh codex 8         # family B
python3 scripts/extract_findings.py
python3 scripts/reconcile.py         # corroboration + clone spread
python3 scripts/gen_report.py        # -> RESULTS.md
poc/run_all.sh                       # -> 3/3 PoCs pass (needs a JDK)
```
Raw model transcripts kept under `audit/runs/` for auditability. Detailed tables
in `RESULTS.md`; full method in `METHODOLOGY.md`.
