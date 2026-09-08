"""Prefer live evidence when present, otherwise the committed iteration snapshot."""
import json
from pathlib import Path
import re


def resolve(root: Path, public=False, iteration=None):
    if iteration is None:
        pointers = [root / 'research/iterations/current.json'] if public else [root / '.local/iterations/active.json', root / 'research/iterations/current.json']
        for pointer in pointers:
            if pointer.exists():
                iteration = json.loads(pointer.read_text())['id']; break
    if iteration is not None and not re.fullmatch(r'[a-z0-9][a-z0-9_-]+', iteration): raise ValueError('Invalid iteration')
    if iteration and iteration != 'original' and not public and not (root / '.local/iterations' / iteration / 'manifest.json').exists():
        public = True
    return public, iteration
