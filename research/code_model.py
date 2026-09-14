"""Deterministic, AST-based code model of a Java source tree.

Run from the repository root:
    python3 -m research.code_model build --root DIR --output code-model.json [--outline outline.md]
    python3 -m research.code_model query --model code-model.json [--root DIR] symbol ID
    python3 -m research.code_model query --model code-model.json callers NAME
    python3 -m research.code_model query --model code-model.json sinks [CATEGORY]
    python3 -m research.code_model query --model code-model.json file PATH
    python3 -m research.code_model query --model code-model.json resolve --file PATH --start N --end M

The model gives every type, method, constructor and field a stable id and an exact
1-based inclusive line range, so that statements produced by an agent can be anchored to
real code elements and checked by the harness (see validate_anchor). Parsing uses
tree-sitter with the Java grammar; nothing is executed. See research/CODE_MODEL.md.
"""
from __future__ import annotations

import argparse
import json
import platform
import sys
from importlib import metadata
from pathlib import Path

from research.import_evidence import digest, canonical

SCHEMA_VERSION = 1
TOOL = "research.code_model"

# Category -> API patterns. Simple names match declared/used type names, receiver names and
# invoked method names; dotted names match invocation chains (Runtime.getRuntime().exec()),
# declared type + method name, static imports and string literals; `T(Arg)` matches a
# `new T(...)` in a symbol that also mentions the Arg type.
SINKS: dict[str, list[str]] = {
    "file_read": ["FileInputStream", "FileReader", "BufferedReader", "RandomAccessFile", "Files.readAllLines",
                  "Files.readAllBytes", "Files.lines", "Files.newBufferedReader", "Scanner(File)", "DataInputStream"],
    "file_write": ["FileOutputStream", "FileWriter", "PrintWriter(File)", "Files.write", "Files.newBufferedWriter",
                   "RandomAccessFile"],
    "deserialization": ["ObjectInputStream", "readObject", "XMLDecoder"],
    "serialization": ["ObjectOutputStream", "writeObject"],
    "network": ["URL", "URLConnection", "HttpURLConnection", "Socket", "ServerSocket", "DatagramSocket"],
    "process_exec": ["Runtime.exec", "ProcessBuilder"],
    "class_loading": ["ClassLoader", "defineClass", "Class.forName", "URLClassLoader"],
    "reflection": ["Method.invoke", "getDeclaredMethod", "setAccessible"],
    "native": ["System.load", "System.loadLibrary"],
    "system_properties": ["System.getProperty", "System.getenv", "user.home", "user.dir"],
    "crypto": ["MessageDigest", "Cipher", "SecureRandom", "Random"],
    "threads": ["Thread", "ExecutorService"],
    "ui_input": ["KeyEvent", "MouseEvent", "JFileChooser", "JOptionPane"],
}

TYPE_KINDS = {
    "class_declaration": "class",
    "interface_declaration": "interface",
    "enum_declaration": "enum",
    "annotation_type_declaration": "annotation",
    "record_declaration": "record",
}
FIELD_NODES = {"field_declaration", "constant_declaration"}
METHOD_NODES = {"method_declaration", "annotation_type_element_declaration"}
CONSTRUCTOR_NODES = {"constructor_declaration", "compact_constructor_declaration"}
MEMBER_NODES = FIELD_NODES | METHOD_NODES | CONSTRUCTOR_NODES | set(TYPE_KINDS)
NUMERIC_LITERALS = {"decimal_integer_literal", "hex_integer_literal", "octal_integer_literal", "binary_integer_literal",
                    "decimal_floating_point_literal", "hex_floating_point_literal"}
STRING_LITERALS = {"string_literal", "text_block"}
ANNOTATION_NODES = {"annotation", "marker_annotation"}


# ----------------------------------------------------------------------------- parsing

def _parser():
    try:
        import tree_sitter_java
        from tree_sitter import Language, Parser
    except ImportError as exc:  # pragma: no cover - depends on the environment
        raise ImportError("tree-sitter is required: pip install --user -r research/code-model-requirements.txt") from exc
    return Parser(Language(tree_sitter_java.language()))


def _versions() -> dict:
    versions = {"python": platform.python_version()}
    for name in ("tree-sitter", "tree-sitter-java"):
        try:
            versions[name] = metadata.version(name)
        except metadata.PackageNotFoundError:  # pragma: no cover
            versions[name] = None
    return versions


def _text(node) -> str:
    return node.text.decode("utf-8", "replace")


def _compact(node) -> str:
    """Node text with all whitespace removed (types as written, e.g. Map<String,Integer>)."""
    return "".join(_text(node).split())


def _named(node):
    return [child for child in node.named_children if not child.is_extra]


def _lines_of(node) -> tuple[int, int]:
    start = node.start_point.row + 1
    end = node.end_point.row + 1
    if node.end_point.column == 0 and end > start:
        end -= 1  # the node ended exactly at a line break
    return start, end


def _simple_type(node) -> str | None:
    """Simple name of a type node (last segment of scoped names, generic arguments dropped)."""
    if node is None:
        return None
    if node.type == "type_identifier":
        return _text(node)
    if node.type == "scoped_type_identifier":
        parts = [c for c in node.children if c.type == "type_identifier"]
        return _text(parts[-1]) if parts else _compact(node)
    if node.type == "generic_type":
        for child in node.named_children:
            if child.type in ("type_identifier", "scoped_type_identifier"):
                return _simple_type(child)
    if node.type == "array_type":
        return _simple_type(node.child_by_field_name("element"))
    return _compact(node)


def _chain(node) -> list[str] | None:
    """Flatten a receiver expression into name segments; None when it is not a name chain."""
    if node is None:
        return []
    kind = node.type
    if kind == "identifier":
        return [_text(node)]
    if kind in ("this", "super"):
        return [kind]
    if kind == "field_access":
        head = _chain(node.child_by_field_name("object"))
        return None if head is None else head + [_text(node.child_by_field_name("field"))]
    if kind == "method_invocation":
        head = _chain(node.child_by_field_name("object"))
        return None if head is None else head + [_text(node.child_by_field_name("name"))]
    if kind == "object_creation_expression":
        simple = _simple_type(node.child_by_field_name("type"))
        return [simple] if simple else None
    if kind == "parenthesized_expression":
        inner = _named(node)
        return _chain(inner[0]) if len(inner) == 1 else None
    if kind == "cast_expression":
        return _chain(node.child_by_field_name("value"))
    if kind == "class_literal":
        inner = _named(node)
        return [_compact(inner[0]), "class"] if inner else None
    return None


class _Tokens:
    """Everything collected from a symbol's subtree that the model reports or matches on."""

    def __init__(self):
        self.calls: list[dict] = []      # ordered occurrences: name, simple, arity, chain, receiver
        self.creates: list[dict] = []    # ordered occurrences: type, arity
        self.types: set[str] = set()
        self.names: set[str] = set()     # identifiers used as receivers/qualifiers (System, Files, KeyEvent)
        self.strings: list[str] = []
        self.catches: list[str] = []
        self.string_count = 0
        self.numeric_count = 0

    def add_type(self, node):
        simple = _simple_type(node)
        if simple:
            self.types.add(simple)

    def scan(self, node, skip_members: bool = False):
        """Pre-order walk. skip_members leaves out direct member declarations (used for type bodies)."""
        stack = [node]
        while stack:
            current = stack.pop()
            if current is node and skip_members:
                stack.extend(c for c in reversed(current.children) if c.type not in MEMBER_NODES)
                continue
            kind = current.type
            if kind in ("scoped_type_identifier", "type_identifier"):
                self.add_type(current)
                continue
            if kind in STRING_LITERALS:
                self.string_count += 1
                raw = _text(current)
                self.strings.append(raw.strip('"'))
                continue
            if kind in NUMERIC_LITERALS:
                self.numeric_count += 1
                continue
            if kind == "method_invocation":
                self._invocation(current)
            elif kind == "explicit_constructor_invocation":
                self._explicit_constructor(current)
            elif kind == "method_reference":
                self._reference(current)
            elif kind == "object_creation_expression":
                type_node = current.child_by_field_name("type")
                args = current.child_by_field_name("arguments")
                simple = _simple_type(type_node)
                if simple:
                    self.creates.append({"type": simple, "arity": len(_named(args)) if args is not None else 0})
            elif kind == "field_access":
                obj = current.child_by_field_name("object")
                if obj is not None and obj.type == "identifier":
                    self.names.add(_text(obj))
            elif kind == "catch_type":
                for alternative in _named(current):
                    text = _compact(alternative)
                    if text not in self.catches:
                        self.catches.append(text)
            stack.extend(reversed(current.children))

    def _record_call(self, name: str, simple: str, arity: int | None, chain, receiver: dict | None):
        self.calls.append({"name": name, "simple": simple, "arity": arity, "chain": chain, "receiver": receiver})

    def _invocation(self, node):
        obj = node.child_by_field_name("object")
        name = _text(node.child_by_field_name("name"))
        args = node.child_by_field_name("arguments")
        arity = len(_named(args)) if args is not None else 0
        chain = _chain(obj)
        written = name
        receiver = None
        if obj is not None:
            if obj.type == "identifier":
                written = f"{_text(obj)}.{name}"
                receiver = {"kind": "identifier", "name": _text(obj)}
                self.names.add(_text(obj))
            elif obj.type == "field_access" and chain is not None:
                written = ".".join(chain) + "." + name
                receiver = {"kind": "field_access", "name": ".".join(chain)}
            elif obj.type == "super":
                written = f"super.{name}"
                receiver = {"kind": "super", "name": "super"}
            elif obj.type == "this":
                receiver = {"kind": "this", "name": "this"}
            elif obj.type == "object_creation_expression":
                receiver = {"kind": "new", "name": _simple_type(obj.child_by_field_name("type"))}
            else:
                receiver = {"kind": obj.type, "name": None}
        self._record_call(written, name, arity, (chain + [name]) if chain is not None else [name], receiver)

    def _explicit_constructor(self, node):
        keyword = next((c.type for c in node.children if c.type in ("this", "super")), "this")
        args = node.child_by_field_name("arguments")
        arity = len(_named(args)) if args is not None else 0
        self._record_call(keyword, keyword, arity, [keyword], {"kind": "constructor", "name": keyword})

    def _reference(self, node):
        children = [c for c in node.children if c.type != "::"]
        if len(children) < 2:
            return
        qualifier, member = children[0], children[-1]
        chain = _chain(qualifier)
        if qualifier.type in ("identifier",):
            self.names.add(_text(qualifier))
        qualifier_text = ".".join(chain) if chain else _compact(qualifier)
        name = _text(member)
        self._record_call(f"{qualifier_text}::{name}", name, None, (chain or [qualifier_text]) + [name],
                          {"kind": "reference", "name": qualifier_text})


# ----------------------------------------------------------------------------- sink matching

def _match_sinks(tokens: _Tokens, declared: set[str], imports: list[str]) -> tuple[list[str], dict]:
    types = tokens.types | declared
    simple_calls = {c["simple"] for c in tokens.calls}
    chains = [c["chain"] for c in tokens.calls if c["chain"]]
    created = {c["type"] for c in tokens.creates}
    matches: dict[str, list[str]] = {}
    for category, patterns in SINKS.items():
        for pattern in patterns:
            if _pattern_hits(pattern, types, tokens.names, simple_calls, chains, created, tokens.strings, imports):
                matches.setdefault(category, []).append(pattern)
    return sorted(matches), {k: sorted(v) for k, v in sorted(matches.items())}


def _pattern_hits(pattern, types, names, simple_calls, chains, created, strings, imports) -> bool:
    if pattern.endswith(")"):
        owner, arg = pattern[:-1].split("(", 1)
        return owner in created and arg in types
    if "." in pattern:
        owner, member = pattern.rsplit(".", 1)
        if any(owner in chain[:-1] and chain[-1] == member for chain in chains):
            return True
        if member in simple_calls and (owner in types or any(
                imp.endswith(f".{owner}.{member}") or imp.endswith(f".{owner}.*") for imp in imports)):
            return True
        return any(pattern in literal for literal in strings)
    return pattern in types or pattern in names or pattern in simple_calls


# ----------------------------------------------------------------------------- extraction

class _FileExtractor:
    def __init__(self, path: str, tree, imports: list[str], package: str | None):
        self.path = path
        self.tree = tree
        self.imports = imports
        self.package = package
        self.symbols: list[dict] = []
        self.occurrences: dict[tuple[str, int], _Tokens] = {}
        self.sequence = 0

    def run(self):
        for node in self.tree.root_node.children:
            if node.type in TYPE_KINDS:
                self.type_declaration(node, self.package or "", None)

    def emit(self, record: dict, tokens: _Tokens, declared: set[str]) -> dict:
        record["sinks"], record["sinkMatches"] = _match_sinks(tokens, declared, self.imports)
        record["calls"] = list(dict.fromkeys(c["name"] for c in tokens.calls))
        record["creates"] = list(dict.fromkeys(c["type"] for c in tokens.creates))
        record["_seq"] = self.sequence
        self.sequence += 1
        self.symbols.append(record)
        self.occurrences[(self.path, record["_seq"])] = tokens
        return record

    @staticmethod
    def modifiers(node) -> tuple[list[str], list[str]]:
        mods, annotations = [], []
        block = next((c for c in node.children if c.type == "modifiers"), None)
        if block is not None:
            for child in block.children:
                if child.type in ANNOTATION_NODES:
                    name = child.child_by_field_name("name")
                    annotations.append(_compact(name) if name is not None else _compact(child))
                elif child.is_named or child.type.isalpha():
                    mods.append(_text(child))
        return mods, annotations

    def type_declaration(self, node, prefix: str, owner: str | None):
        name = _text(node.child_by_field_name("name"))
        type_id = f"{prefix}.{name}" if prefix else name
        start, end = _lines_of(node)
        mods, annotations = self.modifiers(node)
        extends, implements = [], []
        for child in node.children:
            if child.type == "superclass":
                extends += [_compact(c) for c in _named(child)]
            elif child.type == "extends_interfaces":
                extends += [_compact(c) for lst in _named(child) for c in _named(lst)]
            elif child.type == "super_interfaces":
                implements += [_compact(c) for lst in _named(child) for c in _named(lst)]
        body = node.child_by_field_name("body")
        tokens = _Tokens()
        declared = set()
        for child in node.children:
            if child.type in ("superclass", "extends_interfaces", "super_interfaces"):
                tokens.scan(child)
                declared |= tokens.types
        if body is not None:
            tokens.scan(body, skip_members=True)
        record = {"id": type_id, "kind": TYPE_KINDS[node.type], "file": self.path, "start": start, "end": end,
                  "owner": owner, "modifiers": mods, "annotations": annotations, "extends": extends,
                  "implements": implements}
        self.emit(record, tokens, declared)
        if body is None:
            return
        members = list(body.children)
        if node.type == "enum_declaration":
            members = [m for m in body.children if m.type != "enum_body_declarations"] + \
                      [m for d in body.children if d.type == "enum_body_declarations" for m in d.children]
        record_params = node.child_by_field_name("parameters") if node.type == "record_declaration" else None
        for member in members:
            if member.type in TYPE_KINDS:
                self.type_declaration(member, type_id, type_id)
            elif member.type in FIELD_NODES:
                self.field_declaration(member, type_id)
            elif member.type in METHOD_NODES:
                self.method_declaration(member, type_id, name, constructor=False)
            elif member.type in CONSTRUCTOR_NODES:
                self.method_declaration(member, type_id, name, constructor=True, record_params=record_params)

    def field_declaration(self, node, type_id: str):
        base_type = node.child_by_field_name("type")
        mods, annotations = self.modifiers(node)
        start, end = _lines_of(node)
        for declarator in node.children_by_field_name("declarator"):
            name = _text(declarator.child_by_field_name("name"))
            dims = declarator.child_by_field_name("dimensions")
            field_type = _compact(base_type) + (_compact(dims) if dims is not None else "")
            tokens = _Tokens()
            tokens.scan(base_type)
            declared = set(tokens.types)
            value = declarator.child_by_field_name("value")
            if value is not None:
                tokens.scan(value)
            record = {"id": f"{type_id}.{name}", "kind": "field", "file": self.path, "start": start, "end": end,
                      "owner": type_id, "modifiers": mods, "annotations": annotations, "type": field_type}
            self.emit(record, tokens, declared)

    def method_declaration(self, node, type_id: str, type_name: str, constructor: bool, record_params=None):
        name = type_name if constructor else _text(node.child_by_field_name("name"))
        params_node = node.child_by_field_name("parameters")
        if params_node is None and record_params is not None:
            params_node = record_params
        params, declared, tokens = [], set(), _Tokens()
        if params_node is not None:
            for param in _named(params_node):
                if param.type not in ("formal_parameter", "spread_parameter"):
                    continue
                type_node = param.child_by_field_name("type")
                if type_node is None:  # spread_parameter has no field names
                    type_node = next((c for c in param.named_children if c.type not in ("modifiers", "variable_declarator")), None)
                dims = param.child_by_field_name("dimensions")
                type_text = _compact(type_node) if type_node is not None else "?"
                if dims is not None:
                    type_text += _compact(dims)
                if param.type == "spread_parameter":
                    type_text += "..."
                    name_node = next((c.child_by_field_name("name") for c in param.named_children if c.type == "variable_declarator"), None)
                else:
                    name_node = param.child_by_field_name("name")
                params.append({"name": _text(name_node) if name_node is not None else "?", "type": type_text})
                if type_node is not None:
                    tokens.scan(type_node)
        returns = None
        if not constructor:
            type_node = node.child_by_field_name("type")
            returns = _compact(type_node) if type_node is not None else "void"
            if type_node is not None:
                tokens.scan(type_node)
        throws = []
        for child in node.children:
            if child.type == "throws":
                throws += [_compact(c) for c in _named(child)]
                tokens.scan(child)
        declared |= set(tokens.types)
        body = node.child_by_field_name("body")
        if body is not None:
            tokens.scan(body)
        else:  # annotation elements carry a default value instead of a body
            value = node.child_by_field_name("value")
            if value is not None:
                tokens.scan(value)
        mods, annotations = self.modifiers(node)
        start, end = _lines_of(node)
        signature = ",".join(p["type"] for p in params)
        record = {"id": f"{type_id}#{name}({signature})", "kind": "constructor" if constructor else "method",
                  "file": self.path, "start": start, "end": end, "owner": type_id, "modifiers": mods,
                  "annotations": annotations, "returns": returns, "params": params, "throws": throws,
                  "catches": list(tokens.catches),
                  "literals": {"stringCount": tokens.string_count, "numericCount": tokens.numeric_count}}
        self.emit(record, tokens, declared)


def _parse_errors(root_node, limit: int = 20) -> list[str]:
    errors = []
    stack = [root_node]
    while stack and len(errors) < limit:
        node = stack.pop()
        if node.is_missing:
            row, col = node.start_point
            errors.append(f"missing {node.type!r} at L{row + 1}:C{col + 1}")
            continue
        if node.type == "ERROR":
            row, col = node.start_point
            snippet = _text(node)[:40].replace("\n", " ")
            errors.append(f"syntax error at L{row + 1}:C{col + 1}: {snippet!r}")
        stack.extend(reversed(node.children))
    return errors


def _decode(data: bytes) -> tuple[bytes, str]:
    try:
        data.decode("utf-8")
        return data, "utf-8"
    except UnicodeDecodeError:
        return data.decode("latin-1").encode("utf-8"), "latin-1"


def _line_count(data: bytes) -> int:
    return data.count(b"\n") + (1 if data and not data.endswith(b"\n") else 0)


def _extract_file(parser, rel: str, data: bytes) -> tuple[dict, list[dict], dict[tuple[str, int], _Tokens]]:
    record = {"path": rel, "sha256": digest(data), "lines": _line_count(data), "package": None, "imports": [],
              "parseErrors": [], "encoding": "utf-8"}
    try:
        source, record["encoding"] = _decode(data)
        tree = parser.parse(source)
        root = tree.root_node
        for node in root.children:
            if node.type == "package_declaration":
                names = [c for c in _named(node) if c.type in ("identifier", "scoped_identifier")]
                record["package"] = _compact(names[0]) if names else None
            elif node.type == "import_declaration":
                parts = [_compact(c) for c in node.children if c.type in ("identifier", "scoped_identifier", "asterisk")]
                record["imports"].append(".".join(parts))
        if root.has_error:
            record["parseErrors"] = _parse_errors(root)
        extractor = _FileExtractor(rel, tree, record["imports"], record["package"])
        extractor.run()
        return record, extractor.symbols, extractor.occurrences
    except Exception as exc:  # unparsable file: keep the record, drop the symbols
        record["parseErrors"] = [f"{type(exc).__name__}: {exc}"]
        return record, [], {}


# ----------------------------------------------------------------------------- edges

def _edges(symbols: list[dict], occurrences: dict[str, _Tokens]) -> list[dict]:
    by_id = {s["id"]: s for s in symbols}
    methods: dict[str, list[dict]] = {}
    constructors: dict[str, list[dict]] = {}
    types_by_simple: dict[str, list[str]] = {}
    for s in symbols:
        if s["kind"] == "method":
            methods.setdefault(s["id"].split("#", 1)[1].split("(", 1)[0], []).append(s)
        elif s["kind"] == "constructor":
            constructors.setdefault(s["owner"].rsplit(".", 1)[-1], []).append(s)
        elif s["kind"] not in ("field",):
            types_by_simple.setdefault(s["id"].rsplit(".", 1)[-1], []).append(s["id"])

    def arity_ok(candidate: dict, arity: int | None) -> bool:
        if arity is None:
            return True
        params = candidate["params"]
        if params and params[-1]["type"].endswith("..."):
            return arity >= len(params) - 1
        return len(params) == arity

    def enclosing(symbol: dict) -> list[str]:
        chain, current = [], symbol["owner"] if symbol["kind"] in ("method", "constructor", "field") else symbol["id"]
        while current is not None:
            chain.append(current)
            current = by_id[current]["owner"] if current in by_id else None
        return chain

    def within(candidates: list[dict], type_id: str) -> list[dict]:
        return [c for c in candidates if c["owner"] == type_id]

    def superclass_id(type_id: str) -> str | None:
        ext = by_id.get(type_id, {}).get("extends") or []
        if not ext:
            return None
        simple = ext[0].split("<", 1)[0].rsplit(".", 1)[-1]
        ids = types_by_simple.get(simple, [])
        return ids[0] if len(ids) == 1 else None

    edges: set[tuple[str, str]] = set()
    for symbol in symbols:
        tokens = occurrences.get(symbol["id"])
        if tokens is None:
            continue
        scope = enclosing(symbol)
        own = scope[0] if scope else None
        for call in tokens.calls:
            receiver = call["receiver"]
            kind = receiver["kind"] if receiver else None
            if kind == "constructor":
                target = own if call["simple"] == "this" else superclass_id(own) if own else None
                candidates = [c for c in constructors.get(by_id[target]["id"].rsplit(".", 1)[-1], []) if c["owner"] == target] if target in by_id else []
            else:
                candidates = methods.get(call["simple"], [])
            candidates = [c for c in candidates if arity_ok(c, call["arity"])]
            if kind in ("identifier", "field_access", "new", "reference"):
                name = receiver["name"] or ""
                simple = name.rsplit(".", 1)[-1]
                if kind == "new" or (simple[:1].isupper() and simple in types_by_simple):
                    type_ids = types_by_simple.get(simple, [])
                    candidates = [c for c in candidates if c["owner"] in type_ids] if type_ids else []
                elif name in ("this",) or name.startswith("this."):
                    candidates = candidates if name != "this" else (within(candidates, own) or candidates)
            elif kind == "super":
                parent = superclass_id(own) if own else None
                candidates = within(candidates, parent) if parent else []
            elif kind in (None, "this"):
                for type_id in scope:
                    local = within(candidates, type_id)
                    if local:
                        candidates = local
                        break
            if len(candidates) == 1:
                edges.add((symbol["id"], candidates[0]["id"]))
        for creation in tokens.creates:
            candidates = [c for c in constructors.get(creation["type"], []) if arity_ok(c, creation["arity"])]
            if len(candidates) == 1:
                edges.add((symbol["id"], candidates[0]["id"]))
    return [{"from": a, "to": b} for a, b in sorted(edges)]


# ----------------------------------------------------------------------------- build

def build(root: Path) -> dict:
    """Parse every *.java under root and return the model (a plain dict, canonical-JSON ready)."""
    root = Path(root)
    parser = _parser()
    files, symbols, occurrences = [], [], {}
    paths = sorted(((p.relative_to(root).as_posix(), p) for p in root.rglob("*.java") if p.is_file()), key=lambda x: x[0])
    for rel, path in paths:
        try:
            data = path.read_bytes()
        except OSError as exc:
            files.append({"path": rel, "sha256": None, "lines": 0, "package": None, "imports": [],
                          "parseErrors": [f"{type(exc).__name__}: {exc}"], "encoding": None})
            continue
        record, found, tokens = _extract_file(parser, rel, data)
        files.append(record)
        symbols.extend(found)
        occurrences.update(tokens)
    symbols.sort(key=lambda s: (s["file"], s["start"], s["_seq"]))
    seen: dict[str, int] = {}
    by_id: dict[str, _Tokens] = {}
    for symbol in symbols:
        base = symbol["id"]
        seen[base] = seen.get(base, 0) + 1
        if seen[base] > 1:  # duplicate declarations (only possible in broken sources) stay addressable
            symbol["id"] = f"{base}~{seen[base]}"
        by_id[symbol["id"]] = occurrences[(symbol["file"], symbol.pop("_seq"))]
    occurrences = by_id
    fingerprint = digest("\n".join(f"{f['path']}\t{f['sha256']}" for f in sorted(files, key=lambda f: f["path"])).encode())
    return {
        "schemaVersion": SCHEMA_VERSION,
        "generator": {"tool": TOOL, "versions": _versions(), "sha256": digest(Path(__file__).read_bytes())},
        "root": str(root),
        "fingerprint": fingerprint,
        "files": files,
        "symbols": symbols,
        "edges": _edges(symbols, occurrences),
        "sinks": SINKS,
    }


# ----------------------------------------------------------------------------- outline

def _signature(symbol: dict) -> str:
    return ",".join(p["type"] for p in symbol["params"])


def outline(model: dict) -> str:
    by_id = {s["id"]: s for s in model["symbols"]}
    by_file: dict[str, list[dict]] = {}
    for symbol in model["symbols"]:
        by_file.setdefault(symbol["file"], []).append(symbol)
    lines = []
    for record in model["files"]:
        lines.append(record["path"])
        for error in record["parseErrors"]:
            lines.append(f"  ! {error}")
        for symbol in by_file.get(record["path"], []):
            depth, owner = 1, symbol["owner"]
            while owner is not None:
                depth += 1
                owner = by_id[owner]["owner"] if owner in by_id else None
            span = f"[L{symbol['start']}-L{symbol['end']}]"
            kind = symbol["kind"]
            if kind in ("method", "constructor"):
                name = symbol["id"].split("#", 1)[1].split("(", 1)[0]
                text = f"{kind} {name}({_signature(symbol)})"
                if kind == "method":
                    text += f": {symbol['returns']}"
            elif kind == "field":
                text = f"field {symbol['id'].rsplit('.', 1)[-1]}: {symbol['type']}"
            else:
                text = f"{kind} {symbol['id']}"
            text += f" {span}"
            if kind not in ("method", "constructor", "field"):
                if symbol["extends"]:
                    text += " extends " + ", ".join(symbol["extends"])
                if symbol["implements"]:
                    text += " implements " + ", ".join(symbol["implements"])
            if symbol["sinks"]:
                text += " sinks=" + ",".join(symbol["sinks"])
            lines.append("  " * depth + text)
    return "\n".join(lines) + "\n"


# ----------------------------------------------------------------------------- queries

def _file_record(model: dict, file: str) -> dict | None:
    return next((f for f in model["files"] if f["path"] == file), None)


def resolve(model: dict, file: str, start: int, end: int) -> list[str]:
    """Ids of the symbols enclosing the line range, innermost first. Raises LookupError/ValueError."""
    record = _file_record(model, file)
    if record is None:
        raise LookupError(f"unknown file: {file}")
    if not (isinstance(start, int) and isinstance(end, int)) or start < 1 or end < start or end > record["lines"]:
        raise ValueError(f"line range L{start}-L{end} is outside {file} (1-{record['lines']})")
    hits = [s for s in model["symbols"] if s["file"] == file and s["start"] <= start and end <= s["end"]]
    hits.sort(key=lambda s: (s["end"] - s["start"], -s["start"], s["id"]))
    return [s["id"] for s in hits]


def _read_lines(root: Path, file: str) -> tuple[bytes, list[bytes]]:
    data = (Path(root) / file).read_bytes()
    return data, data.split(b"\n")


def _decode_text(data: bytes) -> str:
    try:
        return data.decode("utf-8")
    except UnicodeDecodeError:
        return data.decode("latin-1")


def symbol_text(model: dict, root: Path, symbol_id: str) -> str:
    symbol = next((s for s in model["symbols"] if s["id"] == symbol_id), None)
    if symbol is None:
        raise KeyError(symbol_id)
    _, lines = _read_lines(root, symbol["file"])
    return _decode_text(b"\n".join(lines[symbol["start"] - 1:symbol["end"]]))


def validate_anchor(model: dict, root: Path, anchor: dict) -> dict:
    """Check an agent-supplied anchor {symbol, file, start_line, end_line} against the model and the tree."""
    result = {"ok": False, "reason": None, "file": None, "start": None, "end": None, "symbols": [], "textSha256": None}
    symbol_id = anchor.get("symbol")
    file = anchor.get("file")
    start, end = anchor.get("start_line"), anchor.get("end_line")
    if start is not None and end is None:
        end = start
    symbol = None
    if symbol_id is not None:
        symbol = next((s for s in model["symbols"] if s["id"] == symbol_id), None)
        if symbol is None:
            result["reason"] = f"unknown symbol: {symbol_id}"
            return result
        if file is not None and file != symbol["file"]:
            result.update(file=file, start=start, end=end, reason=f"symbol {symbol_id} is in {symbol['file']}, not {file}")
            return result
        file = symbol["file"]
        if start is None:
            start, end = symbol["start"], symbol["end"]
    if file is None:
        result["reason"] = "anchor names neither a symbol nor a file"
        return result
    result.update(file=file, start=start, end=end)
    record = _file_record(model, file)
    if record is None:
        result["reason"] = f"unknown file: {file}"
        return result
    if start is None:
        result["reason"] = "a line range is required when no symbol is named"
        return result
    if not isinstance(start, int) or not isinstance(end, int) or start < 1 or end < start or end > record["lines"]:
        result["reason"] = f"line range L{start}-L{end} is outside {file} (1-{record['lines']})"
        return result
    if symbol is not None and not (symbol["start"] <= start and end <= symbol["end"]):
        result["reason"] = f"L{start}-L{end} lies outside {symbol_id} (L{symbol['start']}-L{symbol['end']})"
        return result
    try:
        data, lines = _read_lines(root, file)
    except OSError as exc:
        result["reason"] = f"cannot read {file}: {exc}"
        return result
    if digest(data) != record["sha256"]:
        result["reason"] = f"{file} changed since the model was built"
        return result
    result["symbols"] = resolve(model, file, start, end)
    result["textSha256"] = digest(b"\n".join(lines[start - 1:end]))
    result["ok"] = True
    return result


def _simple_call(name: str) -> str:
    return name.rsplit("::", 1)[-1].rsplit(".", 1)[-1]


def callers(model: dict, name: str) -> list[dict]:
    """Symbols whose calls include name (simple name, qualified suffix, or a symbol id via edges)."""
    if "#" in name:
        targets = {e["from"] for e in model["edges"] if e["to"] == name}
        return [s for s in model["symbols"] if s["id"] in targets]
    hits = []
    for symbol in model["symbols"]:
        for call in symbol.get("calls", []):
            if call == name or call.endswith("." + name) or call.endswith("::" + name) or _simple_call(call) == name:
                hits.append(symbol)
                break
    return hits


def sinks(model: dict, category: str | None = None) -> list[dict]:
    if category is not None and category not in model["sinks"]:
        raise LookupError(f"unknown sink category: {category} (known: {', '.join(model['sinks'])})")
    return [s for s in model["symbols"] if s["sinks"] and (category is None or category in s["sinks"])]


# ----------------------------------------------------------------------------- CLI

def _emit(value, status: int = 0) -> int:
    sys.stdout.write(json.dumps(value, ensure_ascii=False, sort_keys=True, indent=2) + "\n")
    return status


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(prog="python3 -m research.code_model", description=__doc__,
                                     formatter_class=argparse.RawDescriptionHelpFormatter)
    commands = parser.add_subparsers(dest="command", required=True)
    build_cmd = commands.add_parser("build", help="parse a Java tree and write the model")
    build_cmd.add_argument("--root", type=Path, required=True)
    build_cmd.add_argument("--output", type=Path, required=True)
    build_cmd.add_argument("--outline", type=Path)
    query_cmd = commands.add_parser("query", help="query a built model (JSON on stdout)")
    query_cmd.add_argument("--model", type=Path, required=True)
    query_cmd.add_argument("--root", type=Path, help="source root for symbol text (defaults to the model's root)")
    queries = query_cmd.add_subparsers(dest="query", required=True)
    queries.add_parser("symbol").add_argument("id")
    queries.add_parser("callers").add_argument("name")
    queries.add_parser("sinks").add_argument("category", nargs="?")
    queries.add_parser("file").add_argument("path")
    resolve_cmd = queries.add_parser("resolve")
    resolve_cmd.add_argument("--file", required=True)
    resolve_cmd.add_argument("--start", type=int, required=True)
    resolve_cmd.add_argument("--end", type=int, required=True)
    args = parser.parse_args(argv)

    if args.command == "build":
        model = build(args.root)
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_bytes(canonical(model))
        if args.outline:
            args.outline.parent.mkdir(parents=True, exist_ok=True)
            args.outline.write_text(outline(model))
        kinds: dict[str, int] = {}
        for s in model["symbols"]:
            kinds[s["kind"]] = kinds.get(s["kind"], 0) + 1
        return _emit({"output": str(args.output), "files": len(model["files"]),
                      "parseErrors": sum(1 for f in model["files"] if f["parseErrors"]),
                      "symbols": kinds, "edges": len(model["edges"]), "fingerprint": model["fingerprint"]})

    model = json.loads(args.model.read_text())
    root = args.root or Path(model["root"])
    try:
        if args.query == "symbol":
            symbol = next((s for s in model["symbols"] if s["id"] == args.id), None)
            if symbol is None:
                return _emit({"error": f"unknown symbol: {args.id}"}, 1)
            try:
                text = symbol_text(model, root, args.id)
            except OSError as exc:
                return _emit({"symbol": symbol, "text": None, "error": f"cannot read source: {exc}"}, 1)
            return _emit({"symbol": symbol, "text": text})
        if args.query == "callers":
            return _emit(callers(model, args.name))
        if args.query == "sinks":
            return _emit(sinks(model, args.category))
        if args.query == "file":
            if _file_record(model, args.path) is None:
                return _emit({"error": f"unknown file: {args.path}"}, 1)
            return _emit([s for s in model["symbols"] if s["file"] == args.path])
        if args.query == "resolve":
            return _emit({"file": args.file, "start": args.start, "end": args.end,
                          "symbols": resolve(model, args.file, args.start, args.end)})
    except (LookupError, ValueError) as exc:
        return _emit({"error": str(exc)}, 1)
    return 2


if __name__ == "__main__":
    sys.exit(main())
