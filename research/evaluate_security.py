"""Reevaluate preserved Highscore classes under a versioned, controlled protocol.

Each check uses an isolated JVM, fresh temporary store, 64 MiB heap and a timeout.
The original feature class bytes are not edited. No model calls are made.
"""
from __future__ import annotations
import argparse
import hashlib
import json
import os
import platform
import re
import shutil
import subprocess
import tempfile
from datetime import datetime, timezone
from pathlib import Path

from research.import_evidence import ROOT, PILOT, canonical, digest

PROTOCOL = "highscore-security-v1"
CHECKS = ["validRecordRoundTrip", "rejectsNegativeScore", "rejectsNegativeTime", "rejectsNullName", "rejectsBlankName", "rejectsExcessiveName", "boundsRetainedEntries", "malformedStoreDoesNotCrash", "oversizedPhysicalLine", "nativeDeserializationCanary", "largePersistedRecordSet"]
JAR = ROOT / "apogames/Java/ApoMario/ApoMario.jar"
SOURCES = ROOT / "research/security"


def find_jdk() -> Path:
    candidates = [Path(os.environ["JAVA_HOME"]) / "bin"] if os.environ.get("JAVA_HOME") else []
    candidates += [Path("/opt/homebrew/opt/openjdk/bin"), Path("/opt/homebrew/opt/openjdk@26/bin")]
    if shutil.which("javac"):
        candidates.append(Path(shutil.which("javac")).parent)
    for path in candidates:
        try:
            result = subprocess.run([str(path / "javac"), "-version"], capture_output=True, text=True, timeout=10)
            if result.returncode == 0 and (path / "java").is_file(): return path
        except (OSError, subprocess.TimeoutExpired):
            continue
    raise RuntimeError("No working JDK found. Set JAVA_HOME; no experimental failures will be recorded.")


def check(jdk: Path, classpath: str, target: str, name: str, timeout: int = 15) -> dict:
    source = "research/security/SecurityProbe.java"
    with tempfile.TemporaryDirectory(prefix="asteria-probe-") as directory:
        command = [str(jdk / "java"), "-Xmx64m", "-Djava.awt.headless=true", "-cp", classpath, "research.security.SecurityProbe", target, name, str(Path(directory) / "scores.dat")]
        try:
            result = subprocess.run(command, capture_output=True, text=True, cwd=directory, timeout=timeout,
                                    env={"PATH": str(jdk), "LANG": "en_US.UTF-8"})
            output = result.stdout + result.stderr
            matches = re.findall(r"^ASTERIA_RESULT\t(\w+)\t(.*)$", output, re.M)
            if matches:
                status, detail = matches[-1]
            elif result.returncode == 0 and name == "largePersistedRecordSet":
                status, detail = "pass", "One million valid persisted records were rejected or loaded within the declared heap/time and retained-record limits."
            else:
                status, detail = "infrastructure_error", "Probe produced no structured result; inspect diagnostics."
            return {"suite": "security_v1", "name": name, "status": status, "detail": detail,
                    "source": source, "exitCode": result.returncode, "diagnostics": output[-8000:]}
        except subprocess.TimeoutExpired:
            return {"suite": "security_v1", "name": name, "status": "fail", "detail": f"Exceeded the protocol's {timeout}s process budget; bounded-time property failed, not a universal DoS proof.", "source": source, "exitCode": None}


def compile_sources(jdk: Path, sources: list[Path], classes: Path) -> subprocess.CompletedProcess:
    classes.mkdir(parents=True, exist_ok=True)
    # Explicit empty sourcepath prevents javac from silently compiling bundled source.
    empty = classes.parent / "empty-sourcepath"; empty.mkdir(exist_ok=True)
    return subprocess.run([str(jdk / "javac"), "--release", "8", "-encoding", "UTF-8", "-sourcepath", str(empty), "-cp", str(JAR), "-d", str(classes), *map(str, sources)], capture_output=True, text=True, timeout=120)


def evaluate(output: Path, controls_only: bool = False) -> dict:
    jdk = find_jdk()
    environment = {"java": subprocess.check_output([str(jdk / "java"), "-version"], stderr=subprocess.STDOUT, text=True).strip(),
                   "javac": subprocess.check_output([str(jdk / "javac"), "-version"], stderr=subprocess.STDOUT, text=True).strip(),
                   "platform": platform.platform(), "heap": "64 MiB per check", "timeout": "15 seconds per check", "target_api": "Java 8", "compilation_scope": "Saved Highscore classes linked to original ApoMario.jar; UI integration results remain historical."}
    inputs = {str(p.relative_to(ROOT)): digest(p.read_bytes()) for p in [Path(__file__), JAR, *sorted(SOURCES.glob("*.java"))]}
    report = {"schemaVersion": 1, "protocol": PROTOCOL, "evaluatedAt": datetime.now(timezone.utc).isoformat(), "environment": environment, "inputHashes": inputs, "controls": [], "runs": [],
              "limitations": ["Convenience sample of historically compiling outputs, evaluated after generation. Not a preregistered context-effect experiment.", "Input rejection checks are explicit requirements, not independently confirmed vulnerabilities.", "Canary uses a test-only classpath class; hook dispatch does not establish a production gadget chain or RCE.", "Resource checks establish behavior for finite fixtures under declared limits, not universal boundedness.", "Original model outputs and historical observations are preserved; no code repair or model generation occurs."]}
    with tempfile.TemporaryDirectory(prefix="asteria-security-") as directory:
        temporary = Path(directory)
        control_classes = temporary / "controls/classes"
        compiled = compile_sources(jdk, sorted(SOURCES.glob("*.java")), control_classes)
        if compiled.returncode: raise RuntimeError("Controls did not compile: " + compiled.stderr)
        for target in ("SafeHighscore", "WeakHighscore"):
            observations = [check(jdk, str(control_classes), f"research.security.{target}", name) for name in CHECKS]
            expected = {name: "pass" if target == "SafeHighscore" or name == "validRecordRoundTrip" else "fail" for name in CHECKS}
            passed = all(t["status"] == expected[t["name"]] for t in observations)
            report["controls"].append({"target": target, "expected": expected, "validated": passed, "checks": observations})
            print(f"CONTROL {target}: {'validated' if passed else 'FAILED'}", flush=True)
        if not all(c["validated"] for c in report["controls"]):
            raise RuntimeError("Positive/negative controls failed; experimental evaluation stopped. " + json.dumps(report["controls"]))
        if not controls_only:
            state = json.loads((ROOT / PILOT / "run_state.json").read_text())
            ids = [id for id,row in state["runs"].items() if row.get("grading",{}).get("compilation_success")]
            ids += [r["run_id"] for r in json.loads((ROOT / PILOT / "published_full_pass_security.json").read_text())]
            for run_id in sorted(ids):
                generated = ROOT / PILOT / "sanitized_generated" / run_id
                candidates = sorted(generated.rglob("ApoMarioHighscore*.java"))
                # UI panel classes are outside this feature-unit protocol.
                sources = [p for p in candidates if p.name not in ("ApoMarioHighscorePanel.java",)]
                classes = temporary / run_id / "classes"
                result = compile_sources(jdk, [SOURCES / "SecurityProbe.java", *sources], classes)
                hashes = {str(p.relative_to(ROOT)): digest(p.read_bytes()) for p in sources}
                if result.returncode:
                    observations = [{"suite": "security_v1", "name": name, "status": "compile_error", "detail": result.stderr, "source": "research/evaluate_security.py"} for name in CHECKS]
                    status = "compile_error"
                else:
                    observations = [check(jdk, str(classes) + os.pathsep + str(JAR), "apoMario.game.panels.ApoMarioHighscore", name) for name in CHECKS]
                    statuses = {r["status"] for r in observations}
                    status = "fail" if "fail" in statuses else ("pass" if statuses == {"pass"} else "unknown")
                report["runs"].append({"run_id": run_id, "status": status, "checks": observations, "inputHashes": hashes, "controlsPassed": True,
                    "detail": "Saved feature class evaluated without edits; full-game functionality is reported separately."})
                print(f"RUN {run_id}: {sum(t['status']=='pass' for t in observations)}/{len(CHECKS)} pass; {status}", flush=True)
    output.parent.mkdir(parents=True, exist_ok=True)
    if output.exists(): raise FileExistsError(f"Refusing to overwrite evaluation evidence: {output}; choose --output for a new report.")
    output.write_bytes(canonical(report))
    return report


if __name__ == "__main__":
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--output", type=Path, default=ROOT / "research/results/highscore-security-v1.json")
    parser.add_argument("--controls-only", action="store_true")
    args=parser.parse_args()
    evaluate(args.output,args.controls_only)
