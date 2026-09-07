#!/usr/bin/env python3
"""Extract structured findings from raw audit transcripts.

Both CLIs were told to wrap their answer in <<<JSON_BEGIN>>> ... <<<JSON_END>>>.
That marker convention is the primary anchor. Real-world wrinkles handled:
  - codex echoes the prompt (schema template) and often repeats its answer, so we
    take the LAST non-template block;
  - ox-alpha occasionally corrupts the OPENING marker with an injected code fence
    but leaves <<<JSON_END>>> intact, so we fall back to anchoring on JSON_END and
    brace-matching the object immediately before it;
  - codex may be cut off mid-answer by OpenAI's cyber content filter, leaving no
    complete answer (recorded as a refusal, not a parse failure).
"""
import re
import json
import pathlib

B = pathlib.Path(__file__).resolve().parents[1]
RUNS = B / "audit" / "runs"
OUT = B / "audit" / "findings"

REFUSAL_SIGN = "flagged for possible cybersecurity"


def clean(raw):
    raw = raw.strip()
    raw = re.sub(r"^```[a-zA-Z]*\s*", "", raw)
    raw = re.sub(r"\s*```$", "", raw)
    return raw.strip()


def is_template(obj):
    """True only for the schema template carried in the prompt echo.

    Uses unambiguous placeholder VALUES the model never reproduces in a real
    answer, not schema field names (which real answers legitimately contain).
    """
    if not isinstance(obj, dict):
        return False
    nf = obj.get("notable_files")
    if nf == ["path relative to source root"]:
        return True
    for f in obj.get("findings", []) or []:
        if isinstance(f, dict) and f.get("title") == "short specific title":
            return True
    return False


def try_load(raw):
    try:
        obj = json.loads(clean(raw), strict=False)
    except Exception:
        return None
    if isinstance(obj, dict) and "findings" in obj and not is_template(obj):
        return obj
    return None


def marker_blocks(text):
    """Last non-template marker-delimited object, tolerant of a mangled open marker."""
    best = None
    for raw in re.findall(r"<<<JSON_BEGIN>>>(.*?)<<<JSON_END>>>", text, re.S):
        obj = try_load(raw)
        if obj is not None:
            best = obj
    if best is not None:
        return best
    # Fallback: anchor on the (intact) closing marker, brace-match backwards.
    pos = text.rfind("JSON_END")
    if pos == -1:
        return None
    seg = text[:pos]
    end = seg.rfind("}")
    if end == -1:
        return None
    depth = 0
    in_str = False
    esc = False
    for i in range(end, -1, -1):
        ch = seg[i]
        if in_str:
            if esc:
                esc = False
            elif ch == "\\":
                esc = True  # note: reverse-scan approximation, good enough here
            elif ch == '"':
                in_str = False
            continue
        if ch == '"':
            in_str = True
        elif ch == "}":
            depth += 1
        elif ch == "{":
            depth -= 1
            if depth == 0:
                return try_load(seg[i:end + 1])
    return None


def extract(path):
    text = path.read_text(errors="replace")
    obj = marker_blocks(text)
    refused = obj is None and REFUSAL_SIGN in text
    return obj, refused


def main():
    summary = {}
    all_findings = []
    for fam_dir in sorted(RUNS.glob("*")):
        if not fam_dir.is_dir():
            continue
        fam = fam_dir.name
        summary[fam] = {"logs": 0, "parsed": 0, "refused": [], "failed": [], "findings": 0}
        for log in sorted(fam_dir.glob("*.log")):
            summary[fam]["logs"] += 1
            obj, refused = extract(log)
            if obj is None:
                (summary[fam]["refused"] if refused else summary[fam]["failed"]).append(log.stem)
                continue
            summary[fam]["parsed"] += 1
            variant = obj.get("variant") or log.stem
            for f in obj.get("findings", []) or []:
                if not isinstance(f, dict):
                    continue
                f["_family"] = fam
                f["_variant"] = variant
                f["_log"] = str(log.relative_to(B))
                all_findings.append(f)
                summary[fam]["findings"] += 1
            (OUT / f"{fam}__{log.stem}.json").write_text(json.dumps(obj, indent=2))

    (OUT / "all_findings.json").write_text(json.dumps(all_findings, indent=2))
    (OUT / "extract_summary.json").write_text(json.dumps(summary, indent=2))

    print(f"{'family':<8}{'logs':>6}{'parsed':>8}{'refused':>9}{'failed':>8}{'findings':>10}")
    print("-" * 60)
    for fam, s in summary.items():
        print(f"{fam:<8}{s['logs']:>6}{s['parsed']:>8}{len(s['refused']):>9}"
              f"{len(s['failed']):>8}{s['findings']:>10}")
        if s["refused"]:
            print(f"   refused (cyber filter): {', '.join(s['refused'])}")
        if s["failed"]:
            print(f"   parse-failed: {', '.join(s['failed'])}")
    print(f"\ntotal findings: {len(all_findings)}")


if __name__ == "__main__":
    main()
