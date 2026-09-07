#!/usr/bin/env python3
"""Resumable VaMoS Highscore security-context pilot via the Atira dev proxy.

The runner preserves the paper's prompt and S/B/F attachments, inlining the
attachments because the Atira OpenAI-compatible proxy has no Gemini Files API.
It submits through Atira's asynchronous endpoint, persists each Service Bus
session id, receives with PEEK_LOCK, writes the queue response before completing
the message, then reuses the authors' compile/unit-test integration code.
"""

from __future__ import annotations

import argparse
import asyncio
import difflib
import hashlib
import importlib.util
import json
import os
import re
import shutil
import subprocess
import sys
import tempfile
import time
import traceback
import types
import urllib.request
import zipfile
from collections import defaultdict
from pathlib import Path
from typing import Any


HERE = Path(__file__).resolve().parent
B_ROOT = HERE.parents[1]
ARTIFACT = B_ROOT / "vamos-artifact"
ATIRA_REPO = Path(
    os.environ.get("ATIRA_REPO", "/Users/eliahilse/Documents/GitHub/atira/atira")
)
OUTPUT = HERE / "results"
LIB = HERE / "lib"
STATE_FILE = OUTPUT / "run_state.json"
SECURITY_CONTEXT_FILE = HERE / "security_context.md"

JUNIT_URL = "https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar"
HAMCREST_URL = (
    "https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/"
    "hamcrest-core-1.3.jar"
)

# Compilation winners from Table 3. Semantic winners are not used to select the
# pilot because security can only be evaluated on code that compiles. The paper's
# semantic Generation/S+B winner is based on only two compiling Highscore runs.
CONDITIONS = {
    "reuse_sfb": {
        "prompt_id": 7,
        "strategy": "Reuse",
        "context": "S+F+B",
        "security_context": False,
    },
    "reuse_sfb_security": {
        "prompt_id": 7,
        "strategy": "Reuse",
        "context": "S+F+B",
        "security_context": True,
    },
    "generation_s": {
        "prompt_id": 9,
        "strategy": "Generation",
        "context": "S",
        "security_context": False,
    },
    "generation_s_security": {
        "prompt_id": 9,
        "strategy": "Generation",
        "context": "S",
        "security_context": True,
    },
}

DEFAULT_MODELS = ["gemini-3.5-flash-lite", "gpt-5.4-mini@none"]


class Java8SubprocessProxy:
    """Run the legacy Apo-Games sources against the JDK 8 API surface."""

    TimeoutExpired = subprocess.TimeoutExpired

    @staticmethod
    def run(command, *args, **kwargs):
        if command and Path(command[0]).name == "javac" and "--release" not in command:
            command = [command[0], "--release", "8", *command[1:]]
        return subprocess.run(command, *args, **kwargs)


def atomic_write_text(path: Path, text: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    tmp = path.with_name(path.name + ".tmp")
    tmp.write_text(text, encoding="utf-8")
    os.replace(tmp, path)


def atomic_write_json(path: Path, value: Any) -> None:
    atomic_write_text(path, json.dumps(value, indent=2, ensure_ascii=False) + "\n")


def load_state() -> dict[str, Any]:
    if STATE_FILE.exists():
        return json.loads(STATE_FILE.read_text(encoding="utf-8"))
    return {
        "schema_version": 1,
        "artifact_commit": "314df2ef5befacfb7f90aa3b0a514d1ea47662b7",
        "created_at": time.strftime("%Y-%m-%dT%H:%M:%S%z"),
        "runs": {},
    }


def slug(value: str) -> str:
    return re.sub(r"[^a-zA-Z0-9._-]+", "_", value).strip("_")


def import_module(name: str, path: Path):
    spec = importlib.util.spec_from_file_location(name, path)
    if spec is None or spec.loader is None:
        raise RuntimeError(f"Cannot import {path}")
    module = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(module)
    return module


def import_module_with_replacements(
    name: str, path: Path, replacements: dict[str, str]
):
    """Load an artifact script after filling its intentionally blank constants."""
    source = path.read_text(encoding="utf-8")
    for old, new in replacements.items():
        if old not in source:
            raise RuntimeError(f"Expected configuration line not found in {path}: {old}")
        source = source.replace(old, new, 1)
    module = types.ModuleType(name)
    module.__file__ = str(path)
    exec(compile(source, str(path), "exec"), module.__dict__)
    return module


def find_mario_root() -> Path:
    """Stage the original source+class jar for legacy integration grading."""
    root = HERE / "staged/ApoMario"
    if (root / "apoMario").is_dir() and (root / "test/TestSolve.class").is_file():
        return root

    source_jar = B_ROOT / "apogames/Java/ApoMario/ApoMario.jar"
    if not source_jar.is_file():
        raise FileNotFoundError(f"ApoMario artifact not found: {source_jar}")
    root.parent.mkdir(parents=True, exist_ok=True)
    temporary = Path(tempfile.mkdtemp(prefix="ApoMario-stage-", dir=root.parent))
    try:
        with zipfile.ZipFile(source_jar) as archive:
            for member in archive.infolist():
                destination = (temporary / member.filename).resolve()
                if temporary.resolve() not in destination.parents and destination != temporary.resolve():
                    raise RuntimeError(f"Unsafe jar member path: {member.filename}")
                archive.extract(member, temporary)
        distribution = B_ROOT / "apogames/Java/ApoMario"
        for name in ("levels", "replay"):
            source = distribution / name
            if source.is_dir():
                shutil.copytree(source, temporary / name, dirs_exist_ok=True)
        properties = distribution / "mario.properties"
        if properties.is_file():
            shutil.copy2(properties, temporary / properties.name)
        if root.exists():
            raise RuntimeError(f"Incomplete staging directory already exists: {root}")
        os.replace(temporary, root)
    except Exception:
        shutil.rmtree(temporary, ignore_errors=True)
        raise
    return root


def configure_author_harness():
    gr = import_module("vamos_generation_runner", ARTIFACT / "Pipeline/run.py")
    gr.PROJECT_BASE = str(B_ROOT)
    gr.FEATURES_CSV = str(ARTIFACT / "Pipeline/Features.csv")
    gr.PROMPTS_CSV = str(ARTIFACT / "Pipeline/Prompts.csv")
    gr.CONTEXT_JSONS_DIR = str(ARTIFACT / "Contexts")
    gr.OUTPUT_DIR = str(OUTPUT / "author_compat")
    gr.LIB_DIR = str(LIB)
    gr.JUNIT_JAR = str(LIB / "junit-4.13.2.jar")
    gr.HAMCREST_JAR = str(LIB / "hamcrest-core-1.3.jar")
    gr.EXTRA_CLASSPATH = ""
    gr.GAME_PROJECTS = {
        "ApoBot": str(B_ROOT / "apogames/Java/ApoBot"),
        "ApoMario": str(find_mario_root()),
        "ApoIcarus": str(B_ROOT / "apogames/Java/ApoIcarus"),
        "ApoSimple": str(B_ROOT / "apogames/Java/ApoSimple"),
    }
    gr.TASK_CONFIG = json.loads(
        (ARTIFACT / "Pipeline/task_config.json").read_text(encoding="utf-8")
    )
    # The public jar contains two additional test-only AI classes solely as
    # bytecode. The paper config strips the other three such constructor paths,
    # but omits these two. Main grading deliberately compiles source only, so
    # remove the remaining test-only branches as the same environment repair.
    gr.TASK_CONFIG["Highscore"]["broken_import_patterns"].extend(
        ["new TestSolve()", "new IcarusAI()"]
    )
    gr.subprocess = Java8SubprocessProxy
    gr.load_data(client=None)
    gr.build_import_index()
    return gr


def ensure_dependencies() -> None:
    LIB.mkdir(parents=True, exist_ok=True)
    for path, url in (
        (LIB / "junit-4.13.2.jar", JUNIT_URL),
        (LIB / "hamcrest-core-1.3.jar", HAMCREST_URL),
    ):
        if not path.exists():
            print(f"Downloading {path.name}", flush=True)
            urllib.request.urlretrieve(url, path)

    jdk_bin = Path("/opt/homebrew/opt/openjdk@26/bin")
    if jdk_bin.is_dir():
        os.environ["PATH"] = str(jdk_bin) + os.pathsep + os.environ.get("PATH", "")


def attachment_text(gr, prompt_row: dict[str, Any]) -> tuple[str, list[dict[str, Any]]]:
    sections: list[str] = [prompt_row["prompt_text"]]
    manifest: list[dict[str, Any]] = []
    seen: set[Path] = set()

    def add(path: Path, kind: str) -> None:
        resolved = path.resolve()
        if resolved in seen:
            return
        if not resolved.exists():
            raise FileNotFoundError(f"Missing {kind} attachment: {resolved}")
        seen.add(resolved)
        content = resolved.read_text(encoding="utf-8", errors="replace")
        digest = hashlib.sha256(content.encode("utf-8")).hexdigest()
        manifest.append(
            {
                "kind": kind,
                "name": resolved.name,
                "bytes": len(content.encode("utf-8")),
                "sha256": digest,
            }
        )
        sections.extend(
            [
                f"\n\n--- BEGIN ATTACHED {kind.upper()}: {resolved.name} ---",
                content,
                f"--- END ATTACHED {kind.upper()}: {resolved.name} ---",
            ]
        )

    for abbrev, game in prompt_row["context_games"].items():
        type_name = gr.CONTEXT_TYPE_MAP.get(abbrev, abbrev)
        add(ARTIFACT / "Contexts" / f"{game}_{type_name}.json", "context")

    refs = gr._split_refs(prompt_row["reuse_code_refs"]) + gr._split_refs(
        prompt_row["source_code_refs"]
    )
    reuse_refs = set(gr._split_refs(prompt_row["reuse_code_refs"]))
    for ref in refs:
        game_name, class_name = ref.split(".")[0], ref.split(".")[-1]
        source = gr.find_java_file(gr.GAME_PROJECTS[game_name], class_name)
        if not source:
            raise FileNotFoundError(f"Cannot resolve source attachment {ref}")
        add(Path(source), "reuse source" if ref in reuse_refs else "target source")

    return "\n".join(sections), manifest


def build_prompt(gr, condition_name: str) -> tuple[str, dict[str, Any], list[dict[str, Any]]]:
    condition = CONDITIONS[condition_name]
    prompt_row = next(p for p in gr.prompts if p["id"] == condition["prompt_id"])
    text, attachments = attachment_text(gr, prompt_row)

    if condition["security_context"]:
        security_context = SECURITY_CONTEXT_FILE.read_text(encoding="utf-8")
        text += "\n\n--- BEGIN SECURITY FINDINGS CONTEXT ---\n"
        text += security_context
        text += "\n--- END SECURITY FINDINGS CONTEXT ---"

    # Preserve the paper runner's conflicting output suffix for fidelity.
    text += "\n\n--- OUTPUT FORMAT ---\n"
    text += (
        "First, think step-by-step through the class architecture, dependencies, "
        "and potential logic edge cases. Write down your brief internal reasoning.\n"
        "Then, output the final Java source code. For each class, wrap it cleanly "
        "in this block:\n"
        "```java filename=ClassName.java\n"
        "// your code here\n"
        "```"
    )
    return text, prompt_row, attachments


def make_jobs(models: list[str], runs: int, conditions: list[str]) -> list[dict[str, Any]]:
    jobs = []
    for model in models:
        for condition in conditions:
            for repetition in range(1, runs + 1):
                run_id = f"{slug(model)}__{condition}__r{repetition}"
                jobs.append(
                    {
                        "run_id": run_id,
                        "model_spec": model,
                        "condition": condition,
                        "repetition": repetition,
                        **CONDITIONS[condition],
                    }
                )
    return jobs


def load_atira():
    if not (ATIRA_REPO / "app").is_dir():
        raise FileNotFoundError(f"Atira checkout not found: {ATIRA_REPO}")
    os.chdir(ATIRA_REPO)
    sys.path.insert(0, str(ATIRA_REPO))
    from app.core import context as app_context
    from app.core.config import get_config
    from app.services.llm.client.atira_llm_client import AtiraLLMClient
    from app.services.llm.dependencies import get_llm_blob_store
    from app.services.llm.model_spec import resolve_model_spec
    from app.services.queue.azure_service_bus import AzureServiceBusService
    from app.services.queue.payload import resolve_payload

    return {
        "context": app_context,
        "get_config": get_config,
        "client_cls": AtiraLLMClient,
        "get_blob_store": get_llm_blob_store,
        "resolve_model_spec": resolve_model_spec,
        "queue_cls": AzureServiceBusService,
        "resolve_payload": resolve_payload,
    }


async def submit_and_receive(
    *,
    atira: dict[str, Any],
    client: Any,
    queue: Any,
    blob_store: Any,
    gr: Any,
    job: dict[str, Any],
    state: dict[str, Any],
    state_lock: asyncio.Lock,
    semaphore: asyncio.Semaphore,
    request_timeout: int,
    queue_timeout: int,
) -> None:
    run_id = job["run_id"]
    queue_message_path = OUTPUT / "queue_messages" / f"{run_id}.json"
    api_response_path = OUTPUT / "api_responses" / f"{run_id}.json"
    raw_response_path = OUTPUT / "raw_responses" / f"{run_id}.txt"

    async with semaphore:
        entry = state["runs"].setdefault(run_id, dict(job))
        if entry.get("status") in {"received", "graded", "complete"}:
            return

        try:
            prompt, prompt_row, attachments = build_prompt(gr, job["condition"])
            prompt_hash = hashlib.sha256(prompt.encode("utf-8")).hexdigest()

            if not entry.get("session_id"):
                bare_model, effort, temperature = atira["resolve_model_spec"](
                    job["model_spec"], None, 0.7
                )
                # Some OpenAI reasoning routes reject temperature. The paper-like
                # Gemini model keeps 0.7; GPT-mini uses its deterministic none tier.
                if bare_model.startswith("gpt-"):
                    temperature = None
                payload: dict[str, Any] = {
                    "model": bare_model,
                    "messages": [{"role": "user", "content": prompt}],
                    "max_tokens": 65536,
                    "timeout": request_timeout,
                }
                if effort is not None:
                    payload["reasoning_effort"] = effort
                if temperature is not None:
                    payload["temperature"] = temperature

                async with state_lock:
                    entry.update(
                        {
                            "status": "submitting",
                            "bare_model": bare_model,
                            "reasoning_effort": effort,
                            "temperature": temperature,
                            "prompt_sha256": prompt_hash,
                            "prompt_chars": len(prompt),
                            "attachments": attachments,
                            "submitted_at": time.strftime("%Y-%m-%dT%H:%M:%S%z"),
                        }
                    )
                    atomic_write_json(STATE_FILE, state)

                session_id = await client._post_async(
                    "/v1/async/chat/completions", payload
                )
                async with state_lock:
                    entry["session_id"] = session_id
                    entry["status"] = "submitted"
                    atomic_write_json(STATE_FILE, state)
                print(f"SUBMITTED {run_id} session={session_id}", flush=True)
            else:
                session_id = entry["session_id"]
                print(f"RESUMING  {run_id} session={session_id}", flush=True)

            if not queue_message_path.exists():
                async with queue.get_receiver(
                    atira["get_config"]().LLM_ASYNC_RESPONSE_QUEUE_NAME,
                    session_id=session_id,
                    max_wait_time=queue_timeout,
                ) as receiver:
                    messages = await receiver.receive_messages(
                        max_message_count=1, max_wait_time=queue_timeout
                    )
                    if not messages:
                        raise TimeoutError(
                            f"No response for session {session_id} within {queue_timeout}s"
                        )
                    message = messages[0]
                    atomic_write_text(queue_message_path, str(message))
                    await receiver.complete_message(message)

            async with state_lock:
                entry["status"] = "queue_response_persisted"
                atomic_write_json(STATE_FILE, state)

            queue_body = queue_message_path.read_text(encoding="utf-8")
            resolved = await atira["resolve_payload"](queue_body, blob_store)
            client._check_queue_error(resolved)
            if isinstance(resolved, bytes):
                resolved = resolved.decode("utf-8")
            data = json.loads(resolved)
            atomic_write_json(api_response_path, data)
            content = data["choices"][0]["message"].get("content")
            if not content:
                raise RuntimeError("LLM response has no text content")
            atomic_write_text(raw_response_path, content)

            choice = data["choices"][0]
            async with state_lock:
                entry.update(
                    {
                        "status": "received",
                        "received_at": time.strftime("%Y-%m-%dT%H:%M:%S%z"),
                        "served_model": data.get("model"),
                        "finish_reason": choice.get("finish_reason"),
                        "usage": data.get("usage"),
                        "response_chars": len(content),
                    }
                )
                entry.pop("last_error", None)
                atomic_write_json(STATE_FILE, state)
            print(f"RECEIVED  {run_id} chars={len(content)}", flush=True)
        except Exception as exc:
            async with state_lock:
                entry["last_error"] = f"{type(exc).__name__}: {exc}"
                entry["last_error_at"] = time.strftime("%Y-%m-%dT%H:%M:%S%z")
                # A submitted session remains resumable; only a pre-session failure
                # is marked failed and eligible for an explicit retry.
                if not entry.get("session_id"):
                    entry["status"] = "failed"
                atomic_write_json(STATE_FILE, state)
            print(f"ERROR     {run_id}: {type(exc).__name__}: {exc}", flush=True)


def sanitize_and_persist(gr, files: dict[str, str], run_id: str) -> dict[str, str]:
    target_game = "ApoMario"
    config = gr.TASK_CONFIG["Highscore"]
    project_dir = gr.GAME_PROJECTS[target_game]
    original_index = gr.import_index_per_game.get(target_game, {})
    game_index = dict(original_index)
    placements: dict[str, tuple[str, str, bool]] = {}

    try:
        gr.import_index_per_game[target_game] = game_index
        for filename, code in files.items():
            cls = filename[:-5]
            dest, package, overwrote = gr.resolve_generated_placement(
                cls,
                code,
                project_dir,
                config["generated_package"],
                config["generated_dir"],
            )
            placements[filename] = (dest, package, overwrote)
            game_index[cls] = f"{package}.{cls}" if package else cls

        persisted: dict[str, str] = {}
        diff_dir = OUTPUT / "diffs" / run_id
        generated_dir = OUTPUT / "sanitized_generated" / run_id
        for filename, code in files.items():
            dest, package, overwrote = placements[filename]
            code = gr.autofix_imports(code, target_game)
            code = gr.inject_missing_imports(code, target_game)
            code = gr.fix_reserved_keyword_methods(code)
            code = gr.fix_generated_package(code, package)
            for pattern in config.get("broken_import_patterns", []):
                code = "\n".join(
                    line for line in code.splitlines() if pattern not in line
                )
            if not gr.code_braces_balanced(code):
                continue

            relative = os.path.relpath(dest, project_dir)
            persisted[relative] = code
            atomic_write_text(generated_dir / relative, code)

            original = ""
            if overwrote and Path(dest).exists():
                original = Path(dest).read_text(encoding="utf-8", errors="replace")
            diff = "".join(
                difflib.unified_diff(
                    original.splitlines(keepends=True),
                    code.splitlines(keepends=True),
                    fromfile=f"a/{relative}",
                    tofile=f"b/{relative}",
                )
            )
            atomic_write_text(diff_dir / f"{filename}.diff", diff)
        return persisted
    finally:
        gr.import_index_per_game[target_game] = original_index


def feature_security_candidates(files: dict[str, str]) -> list[dict[str, Any]]:
    # Primary evaluation is adjudicated later. These deterministic signatures are
    # candidates with direct evidence, never raw sink-hit vulnerability counts.
    joined = "\n".join(files.values())
    evidence_lines = []
    for relpath, code in files.items():
        # Highscore feature classes are new. For modified target classes, restrict
        # this quick scan to inserted feature names and network/deserialization sinks.
        for lineno, line in enumerate(code.splitlines(), 1):
            if re.search(
                r"http://|HIGHSCORE_(?:GET|SAVE)PHP|URLConnection|openConnection|"
                r"ObjectInputStream|readObject\s*\(|URLClassLoader|defineClass|"
                r"System\.load(?:Library)?\s*\(|MessageDigest\.getInstance",
                line,
            ):
                evidence_lines.append((relpath, lineno, line.strip()))

    candidates: list[dict[str, Any]] = []

    def add(signature: str, category: str, cwe: str, severity: str, patterns: list[str]):
        matches = [
            {"file": path, "line": line, "code": code[:300]}
            for path, line, code in evidence_lines
            if any(re.search(pattern, code) for pattern in patterns)
        ]
        if matches:
            candidates.append(
                {
                    "signature": signature,
                    "category": category,
                    "cwe": cwe,
                    "severity": severity,
                    "evidence": matches,
                }
            )

    network = bool(re.search(r"URLConnection|openConnection|HttpClient|new\s+URL\s*\(", joined))
    if network and re.search(r"http://|HIGHSCORE_(?:GET|SAVE)PHP", joined):
        add(
            "highscore_plain_http",
            "insecure_transport",
            "CWE-319",
            "medium",
            [r"http://", r"HIGHSCORE_(?:GET|SAVE)PHP", r"URLConnection", r"openConnection"],
        )

    sends_fields = all(re.search(term, joined, re.I) for term in (r"points|score", r"time", r"name"))
    auth = re.search(r"Authorization|Bearer|Hmac|Mac\.getInstance|signature|authToken", joined, re.I)
    if network and sends_fields and not auth:
        add(
            "client_asserted_highscore_without_auth",
            "client_side_trust",
            "CWE-602",
            "medium",
            [r"URLConnection", r"openConnection"],
        )

    if re.search(r"ObjectInputStream", joined) and re.search(r"readObject\s*\(", joined):
        add(
            "native_deserialization_of_editable_store",
            "untrusted_deserialization",
            "CWE-502",
            "medium",
            [r"ObjectInputStream", r"readObject\s*\("],
        )

    loop_evidence = []
    for relpath, code in files.items():
        lines = code.splitlines()
        for index, line in enumerate(lines):
            header = "\n".join(lines[index : index + 3])
            if not re.search(r"while\s*\(.*(?:readLine|hasNextLine)", header, re.S):
                continue
            # A fixed cap in the loop condition bounds the number of retained
            # records even when EOF remains part of the condition.
            if re.search(
                r"(?:count|size|entries?|records?)\s*[<]=?\s*(?:MAX_[A-Z_]+|\d+)",
                header,
                re.I,
            ):
                continue
            # A loop that breaks once a fixed entry cap is reached is bounded even
            # though its condition is syntactically EOF-controlled.
            window = "\n".join(lines[index : index + 12])
            if re.search(
                r"MAX_(?:ENTRIES|RECORDS|SCORES)|(?:size|count)\s*\(\s*\)\s*>=\s*\d+",
                window,
            ) and "break" in window:
                continue
            loop_evidence.append(
                {"file": relpath, "line": index + 1, "code": line.strip()[:300]}
            )

        # Binary formats often put an attacker-controlled record count before a
        # counted loop. Treat it like the EOF-controlled case unless the count is
        # explicitly capped or rejected against a fixed maximum before the loop.
        for match in re.finditer(
            r"(?:int|long)\s+(?P<var>[A-Za-z_$][\w$]*)\s*=\s*"
            r"(?:[A-Za-z_$][\w$]*\.)?read(?:Unsigned)?(?:Int|Long|Short)\s*\(\s*\)\s*;",
            code,
        ):
            variable = match.group("var")
            tail = code[match.end() : match.end() + 1800]
            loop = re.search(
                rf"for\s*\([^;]*;[^;]*(?:<|<=)\s*{re.escape(variable)}\b",
                tail,
            )
            if not loop:
                continue
            before_loop = tail[: loop.start()]
            capped = bool(
                re.search(
                    rf"{re.escape(variable)}\s*=\s*Math\.min\s*\([^,]+,\s*(?:MAX_[A-Z_]+|\d+)",
                    before_loop,
                    re.I,
                )
                or re.search(
                    rf"if\s*\([^)]*{re.escape(variable)}\s*>\s*=?\s*(?:MAX_[A-Z_]+|\d+)[^)]*\)"
                    rf"[\s\S]{{0,180}}(?:return|throw|break)",
                    before_loop,
                    re.I,
                )
            )
            loop_header = tail[loop.start() : loop.end() + 160]
            if capped or re.search(r"&&[^;]*(?:MAX_[A-Z_]+|\d+)", loop_header):
                continue
            line_number = code.count("\n", 0, match.start()) + 1
            loop_evidence.append(
                {
                    "file": relpath,
                    "line": line_number,
                    "code": match.group(0).strip()[:300],
                }
            )

        # readAllLines materializes an attacker-controlled file before any later
        # trimming, so a post-read list limit does not bound peak allocation.
        for match in re.finditer(r"Files\.readAllLines\s*\(", code):
            line_number = code.count("\n", 0, match.start()) + 1
            loop_evidence.append(
                {
                    "file": relpath,
                    "line": line_number,
                    "code": code.splitlines()[line_number - 1].strip()[:300],
                }
            )
    if loop_evidence:
        candidates.append(
            {
                "signature": "unbounded_record_loading_candidate",
                "category": "resource_exhaustion",
                "cwe": "CWE-400",
                "severity": "low",
                "evidence": loop_evidence,
            }
        )

    add(
        "dynamic_code_loading_candidate",
        "remote_code_loading",
        "CWE-470/494/829",
        "high",
        [r"URLClassLoader", r"defineClass"],
    )
    add(
        "unsafe_native_loading_candidate",
        "unsafe_native_call",
        "CWE-114",
        "high",
        [r"System\.load(?:Library)?\s*\("],
    )
    return candidates


def highscore_feature_files(files: dict[str, str]) -> dict[str, str]:
    """Keep the newly generated feature classes for primary security scanning."""
    return {
        path: code
        for path, code in files.items()
        if Path(path).name.startswith("ApoMarioHighscore")
    }


def refresh_security_analysis(
    state: dict[str, Any], jobs: list[dict[str, Any]]
) -> None:
    """Recompute candidates without rerunning the GUI-dependent Java tests."""
    for job in jobs:
        entry = state["runs"].get(job["run_id"])
        if not entry or entry.get("status") not in {"graded", "complete"}:
            continue
        generated_dir = OUTPUT / "sanitized_generated" / job["run_id"]
        files: dict[str, str] = {}
        if generated_dir.is_dir():
            for path in generated_dir.rglob("*.java"):
                files[str(path.relative_to(generated_dir))] = path.read_text(
                    encoding="utf-8", errors="replace"
                )
        entry["security_candidates"] = feature_security_candidates(
            highscore_feature_files(files)
        )
        entry["secure_functional_success_candidate"] = bool(
            entry.get("full_functional_success")
            and not entry["security_candidates"]
        )
    atomic_write_json(STATE_FILE, state)


def configure_supplementary(gr):
    # The published files have names that disagree with their public Java class
    # declarations. Materialize correctly named local copies so javac and JUnit
    # execute the intended suites rather than reporting false compile failures.
    supplementary_dir = HERE / "supplementary_tests"
    atomic_write_text(
        supplementary_dir / "ApoMarioHighscoreWiringTest.java",
        (ARTIFACT / "Tests/ApoMarioHighscoreAutonomousTest.java").read_text(
            encoding="utf-8"
        ),
    )
    atomic_write_text(
        supplementary_dir / "ApoMarioHighscoreCouplingTest.java",
        (ARTIFACT / "Tests/ApoMarioHighscoreInvokedTest.java").read_text(
            encoding="utf-8"
        ),
    )
    atomic_write_text(
        supplementary_dir / "IntegrationDriver.java",
        (ARTIFACT / "Tests/IntegrationDriver.java").read_text(encoding="utf-8"),
    )

    auto = import_module_with_replacements(
        "vamos_autonomous",
        ARTIFACT / "Pipeline/autonomous_runner.py",
        {
            'BASE        = ""': f"BASE        = {str(B_ROOT)!r}",
            'RESULTS_DIR = ""': f"RESULTS_DIR = {str(OUTPUT)!r}",
            'TESTS_DIR   = ""': f"TESTS_DIR   = {str(supplementary_dir)!r}",
            'GEN = ""': f"GEN = {str(ARTIFACT / 'Pipeline/run.py')!r}",
            'LIB        = ""': f"LIB        = {str(LIB)!r}",
        },
    )
    auto.gr = gr
    auto.subprocess = Java8SubprocessProxy
    auto.TESTS_DIR = str(supplementary_dir)
    auto.LIB = str(LIB)
    auto.SEP = os.pathsep
    auto.TASKS["Highscore"] = {
        "game_root": str(find_mario_root()),
        "game_name": "ApoMario",
        "driver": str(supplementary_dir / "IntegrationDriver.java"),
        "test": "ApoMarioHighscoreWiringTest",
        "pkg_dir": "apoMario/game/panels",
        "gen_pkg": "apoMario.game.panels",
        "test_pkg": "apoMario.game.panels",
        "subs": ("apoMario", "org", "test", "images", "levels", "META-INF"),
    }

    invoked = import_module_with_replacements(
        "vamos_invoked",
        ARTIFACT / "Pipeline/invoked_runner.py",
        {
            'GAME_ROOT   = ""': f"GAME_ROOT   = {str(find_mario_root())!r}",
            'GEN_ROOT    = ""': f"GEN_ROOT    = {str(ARTIFACT / 'Pipeline')!r}",
            'RESULTS_DIR = ""': f"RESULTS_DIR = {str(OUTPUT)!r}",
            'INTEG       = ""': f"INTEG       = {str(supplementary_dir)!r}",
            'DRIVER      = ""': f"DRIVER      = {str(supplementary_dir / 'IntegrationDriver.java')!r}",
            'INVOKED_DIR = ""': f"INVOKED_DIR = {str(supplementary_dir)!r}",
            'LIB         = ""': f"LIB         = {str(LIB)!r}",
            'GEN         = ""': f"GEN         = {str(ARTIFACT / 'Pipeline/run.py')!r}",
        },
    )
    invoked.gr = gr
    invoked.subprocess = Java8SubprocessProxy
    invoked.GAME_ROOT = str(find_mario_root())
    invoked.DRIVER = str(supplementary_dir / "IntegrationDriver.java")
    invoked.INVOKED_DIR = str(supplementary_dir)
    invoked.INVOKED["Highscore"] = "ApoMarioHighscoreCouplingTest"
    invoked.LIB = str(LIB)
    invoked.SEP = os.pathsep
    return auto, invoked


def grade_received(gr, state: dict[str, Any], jobs: list[dict[str, Any]]) -> None:
    auto, invoked = configure_supplementary(gr)
    for job in jobs:
        run_id = job["run_id"]
        entry = state["runs"].get(run_id, {})
        if entry.get("status") in {"graded", "complete"}:
            continue
        raw_path = OUTPUT / "raw_responses" / f"{run_id}.txt"
        if entry.get("status") != "received" or not raw_path.exists():
            continue

        print(f"GRADING   {run_id}", flush=True)
        try:
            response_text = raw_path.read_text(encoding="utf-8")
            extracted = gr.extract_java_files(response_text)
            entry["generated_files"] = sorted(extracted)
            if not extracted:
                entry["grading"] = {
                    "compilation_success": False,
                    "compilation_errors": "No Java files extracted",
                    "tests_run": 0,
                    "tests_passed": 0,
                }
                entry["security_candidates"] = []
                entry["status"] = "graded"
                atomic_write_json(STATE_FILE, state)
                continue

            sanitized = sanitize_and_persist(gr, extracted, run_id)
            entry["sanitized_files"] = sorted(sanitized)
            entry["security_candidates"] = feature_security_candidates(
                highscore_feature_files(sanitized)
            )
            main_result = gr.integrate_compile_test("Highscore", extracted, run_id=run_id)
            entry["grading"] = main_result

            if main_result.get("compilation_success"):
                entry["invoked"] = invoked.run_invoked(extracted, "Highscore", run_id)
                entry["autonomous"] = auto.run_autonomous(extracted, "Highscore", run_id)
            else:
                entry["invoked"] = {"outcome": "NOT_RUN", "detail": "main compile failed"}
                entry["autonomous"] = {"outcome": "NOT_RUN", "detail": "main compile failed"}

            unit_pass = (
                main_result.get("tests_run", 0) > 0
                and main_result.get("tests_passed") == main_result.get("tests_run")
            )
            invoked_pass = entry["invoked"].get("outcome") == "PASS"
            autonomous_pass = entry["autonomous"].get("outcome") == "PASS"
            entry["full_functional_success"] = bool(
                main_result.get("compilation_success")
                and unit_pass
                and invoked_pass
                and autonomous_pass
            )
            entry["secure_functional_success_candidate"] = bool(
                entry["full_functional_success"] and not entry["security_candidates"]
            )
            entry["status"] = "graded"
            entry["graded_at"] = time.strftime("%Y-%m-%dT%H:%M:%S%z")
        except Exception as exc:
            entry["grading_error"] = f"{type(exc).__name__}: {exc}"
            entry["grading_traceback"] = traceback.format_exc()[-4000:]
        atomic_write_json(STATE_FILE, state)


def write_summary(state: dict[str, Any], jobs: list[dict[str, Any]]) -> None:
    grouped: dict[tuple[str, str], list[dict[str, Any]]] = defaultdict(list)
    for job in jobs:
        entry = state["runs"].get(job["run_id"])
        if entry:
            grouped[(job["model_spec"], job["condition"])].append(entry)

    rows = []
    for (model, condition), entries in sorted(grouped.items()):
        n = len(entries)
        received = sum(e.get("status") in {"received", "graded", "complete"} for e in entries)
        compiled = sum(bool(e.get("grading", {}).get("compilation_success")) for e in entries)
        unit_pass = sum(
            e.get("grading", {}).get("tests_run", 0) > 0
            and e.get("grading", {}).get("tests_passed") == e.get("grading", {}).get("tests_run")
            for e in entries
        )
        full_pass = sum(bool(e.get("full_functional_success")) for e in entries)
        secure_pass = sum(bool(e.get("secure_functional_success_candidate")) for e in entries)
        cwe_counts: dict[str, int] = defaultdict(int)
        compiled_entries = [
            entry
            for entry in entries
            if entry.get("grading", {}).get("compilation_success")
        ]
        for entry in compiled_entries:
            for candidate in entry.get("security_candidates", []):
                cwe_counts[candidate["cwe"]] += 1
        rows.append(
            {
                "model": model,
                "condition": condition,
                "strategy": CONDITIONS[condition]["strategy"],
                "context": CONDITIONS[condition]["context"],
                "security_context": CONDITIONS[condition]["security_context"],
                "n": n,
                "received": received,
                "compiled": compiled,
                "unit_pass": unit_pass,
                "full_functional_success": full_pass,
                "secure_functional_success_candidate": secure_pass,
                "security_candidate_compiled_runs_by_cwe": dict(sorted(cwe_counts.items())),
            }
        )
    atomic_write_json(OUTPUT / "summary.json", rows)

    lines = [
        "# VaMoS Highscore security-context pilot",
        "",
        "Candidate security counts are reported only among compiling outputs. They require source adjudication and are not raw sink-hit vulnerability counts.",
        "",
        "| Model | Strategy | S/B/F | Security context | n | Received | Compile | Unit pass | Full functional | Secure functional* | Candidate CWEs among compiled |",
        "|---|---|---|---:|---:|---:|---:|---:|---:|---:|---|",
    ]
    for row in rows:
        cwes = ", ".join(
            f"{key}: {value}/{row['compiled']}"
            for key, value in row["security_candidate_compiled_runs_by_cwe"].items()
        ) or ("none" if row["compiled"] else "n/a")
        lines.append(
            f"| {row['model']} | {row['strategy']} | {row['context']} | "
            f"{'yes' if row['security_context'] else 'no'} | {row['n']} | "
            f"{row['received']} | {row['compiled']} | {row['unit_pass']} | "
            f"{row['full_functional_success']} | {row['secure_functional_success_candidate']} | {cwes} |"
        )
    lines.extend(
        [
            "",
            "\\* Full functional = compile + all unit + invoked + autonomous tests. Secure functional is provisional until candidate findings are manually adjudicated.",
        ]
    )
    atomic_write_text(OUTPUT / "SUMMARY.md", "\n".join(lines) + "\n")


async def async_main(args: argparse.Namespace) -> None:
    ensure_dependencies()
    gr = configure_author_harness()
    models = [m.strip() for m in args.models.split(",") if m.strip()]
    condition_names = [c.strip() for c in args.conditions.split(",") if c.strip()]
    unknown = [c for c in condition_names if c not in CONDITIONS]
    if unknown:
        raise SystemExit(f"Unknown conditions: {', '.join(unknown)}")
    jobs = make_jobs(models, args.runs, condition_names)
    state = load_state()
    for job in jobs:
        state["runs"].setdefault(job["run_id"], dict(job, status="planned"))
    atomic_write_json(STATE_FILE, state)

    if not args.grade_only:
        atira = load_atira()
        config = atira["get_config"]()
        queue = atira["queue_cls"](config.LLM_SERVICE_BUS_HOSTNAME)
        blob_store = atira["get_blob_store"]()
        client = atira["client_cls"](queue_service=queue, blob_store=blob_store)
        token = atira["context"].org_id.set("atira-dev")
        try:
            state_lock = asyncio.Lock()
            semaphore = asyncio.Semaphore(args.concurrency)
            await asyncio.gather(
                *(
                    submit_and_receive(
                        atira=atira,
                        client=client,
                        queue=queue,
                        blob_store=blob_store,
                        gr=gr,
                        job=job,
                        state=state,
                        state_lock=state_lock,
                        semaphore=semaphore,
                        request_timeout=args.request_timeout,
                        queue_timeout=args.queue_timeout,
                    )
                    for job in jobs
                )
            )
        finally:
            atira["context"].org_id.reset(token)
            await queue.close()

    grade_received(gr, state, jobs)
    refresh_security_analysis(state, jobs)
    write_summary(state, jobs)
    print(f"State:   {STATE_FILE}")
    print(f"Summary: {OUTPUT / 'SUMMARY.md'}")


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser()
    parser.add_argument("--models", default=",".join(DEFAULT_MODELS))
    parser.add_argument("--runs", type=int, default=2)
    parser.add_argument("--conditions", default=",".join(CONDITIONS))
    parser.add_argument("--concurrency", type=int, default=4)
    parser.add_argument("--request-timeout", type=int, default=900)
    parser.add_argument("--queue-timeout", type=int, default=1920)
    parser.add_argument("--grade-only", action="store_true")
    return parser.parse_args()


if __name__ == "__main__":
    asyncio.run(async_main(parse_args()))
