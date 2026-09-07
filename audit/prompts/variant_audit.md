You are performing a SECURITY AUDIT of one variant of the Apo-Games corpus, a
family of ~26 cloned Java/Android games used as an academic case study for
software product line research.

TARGET VARIANT: {{VARIANT}}
SOURCE ROOT:    {{PATH}}

Read the source yourself. Do not guess: every claim must cite a real file and
line number that you actually opened.

## What we are trying to learn

This audit feeds research on how much *security-relevant context* an LLM needs
in order to generate or reuse code safely. We therefore care about the
CATEGORIES and the REACHABILITY of weaknesses, not a raw bug count. A finding is
only useful to us if it is concrete enough that someone could try to reproduce
it.

## Ground rules (important)

1. NO GENERIC ADVICE. "Input should be validated" with no specific call site is
   worthless. Every finding must name the sink, the entry point, and the path
   between them.
2. DISTINGUISH REACHABLE FROM THEORETICAL. State plainly whether untrusted input
   (network response, file on disk the user can edit, level/save file,
   command-line arg) can actually reach the sink. If it cannot, say so and mark
   it theoretical.
3. THIS IS A DESKTOP/ANDROID GAME, NOT A SERVER. The relevant threat model is:
   a malicious server or network attacker (these games talk plain HTTP), a
   malicious/user-edited local file (saves, levels, replays), malicious
   user-generated content downloaded from the shared level server, and a local
   user tampering to cheat. Judge severity against THAT model. Do not inflate
   findings by pretending there is a web endpoint.
4. BE HONEST ABOUT ABSENCE. If a category genuinely does not appear in this
   variant, say so. A credible "not present" is as valuable as a finding.
5. NO DUPLICATES. Collapse the same root cause in the same file into one
   finding.

## Areas known to exist in this corpus (verify, do not assume)

- Custom class loaders that fetch bytes and call `defineClass` (possible remote
  or local code loading).
- Plain-HTTP calls to `apo-games.de` endpoints such as `save_highscore.php`,
  `save_level.php`, `get_level.php` (no TLS; score/level submission).
- Custom binary/text save, level, and replay formats parsed by hand
  (`.cheat`, `.mar`, `.skunk`, `.defence`, `.rep`).
- `java.io.ObjectInputStream` use in at least one variant.
- `MessageDigest` (often MD5) used around highscore/validation logic.
- Applet entry points, JNI `System.load`, and file paths built from
  `user.home` / `user.dir`.

## Output format

Think first, then emit ONE JSON object between the exact markers below and
nothing after it. No markdown fences inside the markers.

<<<JSON_BEGIN>>>
{
  "variant": "{{VARIANT}}",
  "files_reviewed": 0,
  "notable_files": ["path relative to source root"],
  "findings": [
    {
      "id": "{{VARIANT}}-01",
      "title": "short specific title",
      "category": "one of: remote_code_loading | insecure_transport | untrusted_deserialization | unsafe_custom_parser | path_traversal | weak_integrity_check | client_side_trust | information_disclosure | resource_exhaustion | unsafe_native_call | legacy_sandbox_assumption | credential_or_secret_exposure | other",
      "cwe": "CWE-XXX",
      "file": "path relative to source root",
      "line": 0,
      "code_excerpt": "the actual line(s), verbatim, max 300 chars",
      "entry_point": "where untrusted data originates",
      "sink": "the dangerous operation reached",
      "data_path": "how input reaches the sink; name the intermediate methods",
      "attack_scenario": "concrete: what an attacker does, step by step, and what they gain",
      "reachable": true,
      "preconditions": "what must hold for this to work",
      "severity": "critical | high | medium | low",
      "confidence": "high | medium | low",
      "validation_plan": "concrete steps to PROVE this is real: what to craft, what to run, what observable result confirms it",
      "shared_component": true
    }
  ],
  "categories_checked_and_absent": ["category names you actively looked for and did not find"],
  "notes": "anything about this variant that matters but is not a finding"
}
<<<JSON_END>>>

Set "shared_component" to true if the vulnerable file looks like cloned
infrastructure shared with sibling variants (e.g. ApoClassLoader.java,
ApoIO.java, ApoHelp.java), rather than logic unique to this game. This matters:
we are measuring how flaws propagate through clone-and-own reuse.

If you find nothing credible, return an empty "findings" array and explain why
in "notes". An honest empty result is acceptable and useful.
