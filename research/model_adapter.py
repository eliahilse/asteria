"""Provider-neutral, one-request/one-response JSON subprocess protocol."""
from __future__ import annotations

import json
import math
import os
import signal
import subprocess
from dataclasses import dataclass


@dataclass
class AdapterFailure(Exception):
    category: str
    # Deliberately omit stderr, environment, and provider exception text.
    # They may contain credentials. Providers can retain private diagnostics.


def command_from_env() -> list[str]:
    try:
        command = json.loads(os.environ['ASTERIA_ADAPTER_COMMAND'])
        if not isinstance(command, list) or not command or not all(isinstance(x, str) and x for x in command):
            raise ValueError()
        return command
    except (KeyError, ValueError):
        raise ValueError('Set ASTERIA_ADAPTER_COMMAND to a JSON argv array; see research/ADAPTER.md.') from None


def validate_response(value: object) -> dict:
    if not isinstance(value, dict) or value.get('protocol_version') != 1:
        raise AdapterFailure('invalid_protocol')
    for key in ('model', 'output_text', 'finish_reason'):
        if not isinstance(value.get(key), str) or not value[key].strip():
            raise AdapterFailure('invalid_response')
    if value.get('request_id') is not None and not isinstance(value['request_id'], str):
        raise AdapterFailure('invalid_request_id')
    usage = value.get('usage', {})
    if not isinstance(usage, dict): raise AdapterFailure('invalid_usage')
    allowed_usage = ('input_tokens', 'output_tokens', 'reasoning_tokens', 'cached_input_tokens')
    for key in allowed_usage:
        v = usage.get(key)
        if v is not None and (type(v) is not int or v < 0): raise AdapterFailure('invalid_usage')
    cost = value.get('cost_usd')
    if cost is not None and (type(cost) not in (int, float) or not math.isfinite(cost) or cost < 0):
        raise AdapterFailure('invalid_cost')
    # Keep only protocol fields; arbitrary headers and debug payloads stay private.
    return {key: value.get(key) for key in ('protocol_version', 'request_id', 'model', 'output_text', 'finish_reason', 'settings')} | {
        'usage': {key: usage.get(key) for key in allowed_usage}, 'cost_usd': cost}


def invoke(command: list[str], request: dict, timeout: float) -> dict:
    try:
        process = subprocess.Popen(command, stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                                   stderr=subprocess.DEVNULL, start_new_session=True)
    except OSError:
        raise AdapterFailure('adapter_start_failed') from None
    try:
        stdout, _ = process.communicate(json.dumps(request, allow_nan=False).encode(), timeout=timeout)
    except subprocess.TimeoutExpired:
        os.killpg(process.pid, signal.SIGKILL)
        process.communicate()
        # Killing the local adapter does not establish cancellation at the provider.
        raise AdapterFailure('transport_outcome_unknown') from None
    except BaseException:
        os.killpg(process.pid, signal.SIGKILL)
        process.communicate()
        raise
    if process.returncode:
        raise AdapterFailure('adapter_exit_outcome_unknown')
    try:
        value = json.loads(stdout)
    except (ValueError, UnicodeDecodeError):
        raise AdapterFailure('invalid_json_outcome_unknown') from None
    return validate_response(value)
