#!/usr/bin/env python3
"""Deterministic security-sink census over the normalized Apo-Games corpus.

Counts occurrences of security-relevant Java API sinks per category and per
variant. This is the static pre-pass: it bounds where an attack surface can
exist at all, independent of any LLM judgement, and is used both to ground the
LLM audit prompts and as corroborating evidence for the final taxonomy.
"""
import re
import json
import pathlib
import collections

CORPUS = pathlib.Path(__file__).resolve().parents[1] / "corpus"

SINKS = {
    "deserialization": r"ObjectInputStream|\breadObject\s*\(|readUnshared|ObjectOutputStream",
    "command_exec": r"Runtime\.getRuntime\(\)\s*\.\s*exec|ProcessBuilder",
    "reflection": r"Class\.forName|\.setAccessible\s*\(|\.newInstance\s*\(",
    "classloader": r"URLClassLoader|defineClass",
    "network": r"new\s+URL\s*\(|openConnection|new\s+Socket\s*\(|ServerSocket|DatagramSocket|HttpURLConnection|URLEncoder",
    "xml_parse": r"DocumentBuilderFactory|SAXParser|XMLReader|XMLInputFactory",
    "weak_random": r"new\s+Random\s*\(|Math\.random",
    "crypto": r"MessageDigest|Cipher\.|KeyGenerator|SecretKey|\bMD5\b|\bSHA1\b",
    "file_io": r"new\s+File\s*\(|FileOutputStream|FileInputStream|FileWriter|FileReader|RandomAccessFile",
    "path_build": r"getAbsolutePath|File\.separator|getProperty\s*\(\s*\"user\.",
    "zip": r"ZipFile|ZipInputStream|JarFile|getEntry|ZipEntry",
    "temp_file": r"createTempFile|java\.io\.tmpdir",
    "sql": r"DriverManager|executeQuery|createStatement",
    "serializable": r"implements\s+Serializable|serialVersionUID",
    "native_code": r"System\.loadLibrary|System\.load\s*\(",
    "applet": r"extends\s+Applet|extends\s+JApplet|AppletContext|getAppletContext",
    "sysprops": r"System\.getProperty|System\.setProperty",
    "exec_url_open": r"Desktop\.getDesktop|browse\s*\(|BrowserLauncher",
    "eval_script": r"ScriptEngine|javax\.script",
    "android_intent": r"new\s+Intent\s*\(|startActivity|sendBroadcast|registerReceiver",
    "android_webview": r"WebView|loadUrl|setJavaScriptEnabled|addJavascriptInterface",
    "android_storage": r"MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE|getExternalStorage|openFileOutput",
}

COMPILED = {k: re.compile(v) for k, v in SINKS.items()}


def main():
    per_cat = collections.Counter()
    per_cat_variants = collections.defaultdict(set)
    per_variant = collections.defaultdict(collections.Counter)
    evidence = collections.defaultdict(list)

    files = sorted(CORPUS.rglob("*.java"))
    for f in files:
        rel = f.relative_to(CORPUS)
        parts = rel.parts
        variant = f"{parts[0]}/{parts[1]}" if len(parts) > 1 else "unknown"
        try:
            text = f.read_text(errors="replace")
        except Exception:
            continue
        for cat, rx in COMPILED.items():
            found = rx.findall(text)
            if found:
                per_cat[cat] += len(found)
                per_cat_variants[cat].add(variant)
                per_variant[variant][cat] += len(found)
                if len(evidence[cat]) < 40:
                    for m in rx.finditer(text):
                        line_no = text.count("\n", 0, m.start()) + 1
                        line = text.splitlines()[line_no - 1].strip()[:180]
                        evidence[cat].append(
                            {"variant": variant, "file": str(rel), "line": line_no, "code": line}
                        )
                        break

    out = {
        "corpus_files": len(files),
        "categories": {
            cat: {
                "total_hits": per_cat[cat],
                "variants_hit": len(per_cat_variants[cat]),
                "variants": sorted(per_cat_variants[cat]),
            }
            for cat in SINKS
        },
        "per_variant": {v: dict(c) for v, c in sorted(per_variant.items())},
        "evidence": {k: v for k, v in evidence.items()},
    }
    outpath = CORPUS.parent / "audit" / "findings" / "sink_census.json"
    outpath.write_text(json.dumps(out, indent=2))

    print(f"corpus .java files: {len(files)}\n")
    print(f"{'category':<22}{'hits':>8}{'variants':>10}")
    print("-" * 40)
    for cat, _ in per_cat.most_common():
        print(f"{cat:<22}{per_cat[cat]:>8}{len(per_cat_variants[cat]):>10}")
    zero = [c for c in SINKS if per_cat[c] == 0]
    print("\nZERO-HIT categories:", ", ".join(zero) if zero else "(none)")
    print(f"\nwrote {outpath}")


if __name__ == "__main__":
    main()
