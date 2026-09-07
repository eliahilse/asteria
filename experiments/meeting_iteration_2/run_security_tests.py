#!/usr/bin/env python3
"""Run separate adversarial tests on the four compiling fresh pilot outputs."""

from __future__ import annotations

import json
import re
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path


HERE = Path(__file__).resolve().parent
B_ROOT = HERE.parents[1]
PILOT = B_ROOT / "experiments/vamos_security_pilot"
PILOT_RESULTS = PILOT / "results"
TEST_SOURCE = HERE / "security_tests/ApoMarioHighscoreSecurityTest.java"
OUTPUT = HERE / "results"
JUNIT = PILOT / "lib/junit-4.13.2.jar"
HAMCREST = PILOT / "lib/hamcrest-core-1.3.jar"


def import_pilot():
    sys.path.insert(0, str(PILOT))
    import run_pilot  # type: ignore

    return run_pilot


def parse_junit(output: str) -> tuple[int, int, dict[str, str]]:
    ok = re.search(r"OK\s*\((\d+)\s+tests?\)", output)
    if ok:
        return int(ok.group(1)), 0, {}
    total = re.search(r"Tests run:\s*(\d+)", output)
    failures = re.search(r"Failures:\s*(\d+)", output)
    messages: dict[str, str] = {}
    current = None
    for line in output.splitlines():
        match = re.match(r"\d+\)\s+(\w+)\(", line)
        if match:
            current = match.group(1)
            messages[current] = ""
        elif current and not messages[current] and line.strip() and not line.lstrip().startswith("at "):
            messages[current] = line.strip()[:400]
    ran = int(total.group(1)) if total else 0
    failed = int(failures.group(1)) if failures else len(messages)
    return ran - failed, failed, messages


def run_one(pilot, run_id: str) -> dict:
    staged = pilot.find_mario_root()
    generated = PILOT_RESULTS / "sanitized_generated" / run_id
    if not generated.is_dir():
        raise FileNotFoundError(generated)

    with tempfile.TemporaryDirectory(prefix=f"security-tests-{run_id}-") as temporary:
        temporary_path = Path(temporary)
        project = temporary_path / "project"
        classes = temporary_path / "classes"
        shutil.copytree(
            staged,
            project,
            ignore=shutil.ignore_patterns("*.class", ".git", ".idea", "*.iml", "Test.java"),
        )
        for source in generated.rglob("*.java"):
            destination = project / source.relative_to(generated)
            destination.parent.mkdir(parents=True, exist_ok=True)
            shutil.copy2(source, destination)

        pilot_source = project / "apoMario/game/panels/ApoMarioHighscoreSecurityTest.java"
        pilot_source.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(TEST_SOURCE, pilot_source)
        pilot.configure_author_harness().convert_to_utf8(str(project))
        classes.mkdir()
        classpath = f"{JUNIT}:{HAMCREST}"
        production = [
            str(path)
            for path in project.rglob("*.java")
            if path.name != TEST_SOURCE.name
        ]
        compile_production = subprocess.run(
            [
                "javac", "--release", "8", "-cp", classpath, "-d", str(classes),
                "-encoding", "UTF-8", "-nowarn", *production,
            ],
            capture_output=True,
            text=True,
            timeout=120,
        )
        if compile_production.returncode:
            return {
                "run_id": run_id,
                "compiled": False,
                "compile_error": compile_production.stderr[-4000:],
            }
        compile_test = subprocess.run(
            [
                "javac", "--release", "8", "-cp", f"{classes}:{classpath}",
                "-d", str(classes), "-sourcepath", str(project),
                "-encoding", "UTF-8", "-nowarn", str(pilot_source),
            ],
            capture_output=True,
            text=True,
            timeout=120,
        )
        if compile_test.returncode:
            return {
                "run_id": run_id,
                "compiled": False,
                "compile_error": compile_test.stderr[-4000:],
            }
        execute = subprocess.run(
            [
                "java", "-cp", f"{classes}:{classpath}",
                "org.junit.runner.JUnitCore",
                "apoMario.game.panels.ApoMarioHighscoreSecurityTest",
            ],
            capture_output=True,
            text=True,
            timeout=60,
        )
        output = execute.stdout + execute.stderr
        passed, failed, messages = parse_junit(output)
        return {
            "run_id": run_id,
            "compiled": True,
            "tests_run": passed + failed,
            "tests_passed": passed,
            "tests_failed": failed,
            "failure_messages": messages,
            "test_output": output,
        }


def main() -> None:
    pilot = import_pilot()
    state = json.loads((PILOT_RESULTS / "run_state.json").read_text(encoding="utf-8"))
    run_ids = sorted(
        run_id
        for run_id, row in state["runs"].items()
        if row.get("grading", {}).get("compilation_success")
    )
    assert len(run_ids) == 4, f"Expected four compiling pilot runs, found {len(run_ids)}"
    rows = [run_one(pilot, run_id) for run_id in run_ids]
    OUTPUT.mkdir(parents=True, exist_ok=True)
    (OUTPUT / "adversarial_security_tests.json").write_text(
        json.dumps(rows, indent=2) + "\n", encoding="utf-8"
    )
    for row in rows:
        print(
            row["run_id"],
            f"{row.get('tests_passed', 0)}/{row.get('tests_run', 0)}",
            "PASS" if row.get("tests_failed") == 0 and row.get("tests_run") else "FAIL",
        )


if __name__ == "__main__":
    main()
