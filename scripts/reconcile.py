#!/usr/bin/env python3
"""N-family reconciliation of the Apo-Games security audit.

Each variant is audited independently by up to 7 model families across 4
providers:
  Anthropic-adjacent orchestration aside, the AUDITORS are —
    ox-alpha, gpt-5.6-sol (max)         [families A, B: the original pair]
    GLM-5.3, Kimi-k3, DeepSeek-v4-pro,  [open-weight baseline families]
    MiniMax-m3, Qwen3.8-max (max)       [+ Alibaba]

A finding is grouped by (variant, file, category). Its AGREEMENT is the number
of distinct families that independently reported it; the DENOMINATOR is how many
families actually produced a parsed audit for that variant (families differ in
coverage — some were refused or errored on some variants). Tiers:

  CONSENSUS      agreement >= 4 families
  CORROBORATED   agreement 2-3 families
  SINGLE_SOURCE  agreement 1 family (a lead, needs manual/PoC validation)

Cross-family agreement across unrelated providers is the cheap, strong defense
against single-model hallucination, and including open-weight families satisfies
the community guideline (arXiv:2508.15503 #6) to use an open LLM baseline.

Clone propagation is measured deterministically (as before): how many variants
ship a file of the same basename, and how many distinct content hashes exist.
"""
import json
import pathlib
import hashlib
import collections

B = pathlib.Path(__file__).resolve().parents[1]
CORPUS = B / "corpus"
FIND = B / "audit" / "findings"

SEV_RANK = {"critical": 0, "high": 1, "medium": 2, "low": 3}
FAMILY_ORDER = ["oxa", "codex", "glm", "kimi", "deepseek", "minimax", "qwen"]


def build_clone_index():
    idx = collections.defaultdict(dict)
    for f in CORPUS.rglob("*.java"):
        parts = f.relative_to(CORPUS).parts
        if len(parts) < 2:
            continue
        variant = f"{parts[0]}/{parts[1]}"
        try:
            idx[f.name].setdefault(variant, hashlib.md5(f.read_bytes()).hexdigest())
        except Exception:
            pass
    return idx


def tag_of(finding):
    """Unique variant key: the log stem (platform_variant), not the bare name."""
    log = finding.get("_log", "")
    return pathlib.PurePath(log).stem or (finding.get("_variant") or "unknown")


def families_per_variant():
    """From the per-log extracted JSONs, which families produced an audit for each tag."""
    fpv = collections.defaultdict(set)
    for jf in FIND.glob("*__*.json"):
        fam, tag = jf.stem.split("__", 1)
        fpv[tag].add(fam)
    return fpv


def main():
    findings = json.loads((FIND / "all_findings.json").read_text())
    clones = build_clone_index()
    fpv = families_per_variant()

    groups = collections.defaultdict(list)
    for f in findings:
        base = pathlib.PurePath(str(f.get("file") or "")).name
        key = (tag_of(f), base, (f.get("category") or "other").strip())
        groups[key].append(f)

    reconciled = []
    for (tag, base, cat), items in groups.items():
        fams = sorted({i["_family"] for i in items}, key=lambda x: FAMILY_ORDER.index(x)
                      if x in FAMILY_ORDER else 99)
        agree = len(fams)
        denom = len(fpv.get(tag, set())) or agree
        best = sorted(items, key=lambda x: SEV_RANK.get(str(x.get("severity", "low")).lower(), 9))[0]
        copies = clones.get(base, {})
        tier = "CONSENSUS" if agree >= 4 else "CORROBORATED" if agree >= 2 else "SINGLE_SOURCE"
        reconciled.append({
            "variant": tag,
            "file": base,
            "category": cat,
            "tier": tier,
            "agreement": agree,
            "audited_by": denom,
            "families": fams,
            "severity": best.get("severity"),
            "cwe": best.get("cwe"),
            "title": best.get("title"),
            "reachable": best.get("reachable"),
            "attack_scenario": best.get("attack_scenario"),
            "validation_plan": best.get("validation_plan"),
            "line": best.get("line"),
            "clone_variants": len(copies),
            "clone_distinct_versions": len(set(copies.values())),
            "n_reports": len(items),
        })

    reconciled.sort(key=lambda r: (-r["agreement"], SEV_RANK.get(str(r["severity"]).lower(), 9),
                                   -r["clone_variants"]))
    (FIND / "reconciled.json").write_text(json.dumps(reconciled, indent=2))

    # ---- category taxonomy ----
    cat_stats = collections.defaultdict(lambda: {
        "variants": set(), "consensus": 0, "corroborated": 0, "single": 0, "max_clone": 0})
    for r in reconciled:
        s = cat_stats[r["category"]]
        s["variants"].add(r["variant"])
        s["consensus" if r["tier"] == "CONSENSUS" else
          "corroborated" if r["tier"] == "CORROBORATED" else "single"] += 1
        s["max_clone"] = max(s["max_clone"], r["clone_variants"])
    tax = [{"category": c, "variants_affected": len(s["variants"]),
            "consensus": s["consensus"], "corroborated": s["corroborated"],
            "single_source": s["single"], "max_clone_spread": s["max_clone"]}
           for c, s in sorted(cat_stats.items(),
                              key=lambda kv: (-(kv[1]["consensus"] + kv[1]["corroborated"]),
                                              -len(kv[1]["variants"])))]
    (FIND / "taxonomy.json").write_text(json.dumps(tax, indent=2))

    # ---- per-family reliability (precision proxy) ----
    fam_stats = {}
    for fam in FAMILY_ORDER:
        raw = [f for f in findings if f["_family"] == fam]
        if not raw and fam not in {x for s in fpv.values() for x in s}:
            continue
        # a family's finding is "backed" if its group has agreement >= 2
        backed = 0
        for (tag, base, cat), items in groups.items():
            fset = {i["_family"] for i in items}
            if fam in fset and len(fset) >= 2:
                backed += 1
        fam_stats[fam] = {
            "variants_audited": sum(1 for t, s in fpv.items() if fam in s),
            "raw_findings": len(raw),
            "distinct_groups_touched": sum(1 for _, its in groups.items()
                                           if fam in {i["_family"] for i in its}),
            "groups_backed_by_others": backed,
        }
    (FIND / "family_stats.json").write_text(json.dumps(fam_stats, indent=2))

    con = sum(1 for r in reconciled if r["tier"] == "CONSENSUS")
    cor = sum(1 for r in reconciled if r["tier"] == "CORROBORATED")
    print(f"raw findings: {len(findings)}   groups: {len(reconciled)}   "
          f"consensus(>=4): {con}   corroborated(2-3): {cor}\n")
    print(f"{'family':<10}{'variants':>9}{'raw':>6}{'groups':>8}{'backed':>8}")
    print("-" * 42)
    for fam, s in fam_stats.items():
        print(f"{fam:<10}{s['variants_audited']:>9}{s['raw_findings']:>6}"
              f"{s['distinct_groups_touched']:>8}{s['groups_backed_by_others']:>8}")
    print(f"\n{'category':<26}{'vars':>5}{'cons':>6}{'corr':>6}{'single':>7}{'clones':>7}")
    print("-" * 60)
    for t in tax:
        print(f"{t['category']:<26}{t['variants_affected']:>5}{t['consensus']:>6}"
              f"{t['corroborated']:>6}{t['single_source']:>7}{t['max_clone_spread']:>7}")
    print("\n--- highest-agreement findings ---")
    for r in reconciled[:22]:
        print(f"  [{r['agreement']}/{r['audited_by']}] {str(r['severity']):7} "
              f"{r['category']:<22} {r['variant']}/{r['file']} clones={r['clone_variants']}")


if __name__ == "__main__":
    main()
