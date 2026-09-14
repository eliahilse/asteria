# Code model

`research/code_model.py` builds a deterministic, AST-based model of a Java source tree so
that security-context statements produced by an LLM agent can be anchored to real code
elements (symbols with a file and an exact line range) and checked by the harness. It
parses with tree-sitter and the Java grammar; it never compiles or executes anything.

## Install

```
pip install --user -r research/code-model-requirements.txt
```

The requirements pin `tree-sitter==0.25.2` and `tree-sitter-java==0.23.5`. tree-sitter
0.26.0 segfaults on several corpus files (a crash in the Python binding, not a grammar
issue) and must not be used.

## CLI

```
python3 -m research.code_model build --root DIR --output code-model.json [--outline outline.md]
python3 -m research.code_model query --model M [--root DIR] symbol ID      # record + source text
python3 -m research.code_model query --model M callers NAME                # symbols whose calls include NAME
python3 -m research.code_model query --model M sinks [CATEGORY]            # symbols tagged with sink categories
python3 -m research.code_model query --model M file PATH                   # symbols in a file
python3 -m research.code_model query --model M resolve --file PATH --start N --end M
```

`build` parses every `*.java` under `DIR` (any root; paths are recorded relative to it
with forward slashes) and writes canonical JSON (sorted keys, symbols ordered by path, start
line, declaration order). Files that cannot be parsed are recorded with their error instead
of aborting the build. Every query prints JSON to stdout; failures print `{"error": ...}`
and exit 1. `callers NAME` matches a simple name (`readLine`), a qualified suffix
(`reader.readLine`, `level.setTime`) or, when NAME is a symbol id, the resolved `edges`.
`symbol` reads the source text from `--root`, defaulting to the root stored in the model.

## Outline

`--outline` writes one line per file, type and member, indented by nesting, for an agent to
read:

```
apoMario/level/ApoMarioLevelIO.java
  class apoMario.level.ApoMarioLevelIO [L22-L200] extends ApoIO
    field level: ApoMarioLevel [L24-L24]
    constructor ApoMarioLevelIO(ApoMarioLevel) [L26-L28]
    method readLevel(DataInputStream): boolean [L30-L109] sinks=file_read
    method writeLevel(DataOutputStream): boolean [L111-L196]
```

Parse errors appear under the file as `  ! missing ')' at L5:C22` or
`  ! syntax error at L5:C21: '...'`; the symbols tree-sitter could still recover follow.

## JSON model (`schemaVersion` 1)

- `generator`: `{tool, versions: {python, tree-sitter, tree-sitter-java}, sha256}` where
  `sha256` is the digest of `research/code_model.py`.
- `root`: the root as given; `fingerprint`: sha256 over the sorted `path\tsha256` lines of
  all files.
- `files`: `[{path, sha256, lines, package, imports, parseErrors, encoding}]`. `lines` counts
  `\n`-terminated lines plus a trailing partial line. `imports` are qualified names as written
  (`java.io.*`, static imports as `java.nio.file.Files.readAllLines`). `encoding` is
  `utf-8`, or `latin-1` when the bytes were not valid UTF-8 (the corpus contains such files;
  line numbers are unaffected).
- `symbols`: records with a stable `id`, `kind`, `file`, `start`, `end` (1-based, inclusive;
  the declaration node's extent, which includes leading annotations such as `@Override`),
  `owner` (enclosing type id or null), `modifiers`, `annotations`, `sinks`, `sinkMatches`,
  `calls`, `creates`:
  - types (`class|interface|enum|annotation|record`): plus `extends: []`, `implements: []`.
    `calls`/`sinks` of a type come only from what no member owns: `extends`/`implements`,
    initializer blocks and enum-constant bodies.
  - `method|constructor`: plus `returns` (null for constructors), `params: [{name, type}]`,
    `throws`, `catches` (exception types in `catch` clauses, first appearance order),
    `literals: {stringCount, numericCount}`.
  - `field`: plus `type` (one record per declarator; `int a, b;` yields two).
  - `calls`: invoked method names as written, deduplicated in order of first appearance:
    qualified when the receiver is an identifier or field access (`reader.readLine`,
    `Files.readAllLines`, `this.level.setTime`), the bare name when the receiver is `this`, a
    call result or an expression; `super.foo` for super calls; `this`/`super` for explicit
    constructor calls; `Foo::bar` for method references. Invocations inside anonymous classes,
    lambdas and local classes are attributed to the enclosing method; no symbols are created
    for them.
  - `creates`: types instantiated with `new`, deduplicated.
- `edges`: `[{from, to}]`, resolved calls (see below), sorted.
- `sinks`: the catalog used (category → patterns).

## Id scheme

- type: `pkg.Outer`, nested `pkg.Outer.Inner` (default package: just the name).
- method/constructor: `pkg.Type#name(ParamType1,ParamType2)` with parameter types as written
  (simple or qualified names, generics without spaces, arrays as `String[]`, varargs as
  `int...`, C-style `String args[]` normalised to `String[]`). Constructors use the type's
  simple name: `pkg.Type#Type(int)`.
- field: `pkg.Type.name`.
- Duplicate declarations (only possible in broken sources) get `~2`, `~3` suffixes in
  declaration order so every symbol stays addressable.

## Sink catalog and matching

| category | patterns |
|---|---|
| file_read | FileInputStream, FileReader, BufferedReader, RandomAccessFile, Files.readAllLines, Files.readAllBytes, Files.lines, Files.newBufferedReader, Scanner(File), DataInputStream |
| file_write | FileOutputStream, FileWriter, PrintWriter(File), Files.write, Files.newBufferedWriter, RandomAccessFile |
| deserialization | ObjectInputStream, readObject, XMLDecoder |
| serialization | ObjectOutputStream, writeObject |
| network | URL, URLConnection, HttpURLConnection, Socket, ServerSocket, DatagramSocket |
| process_exec | Runtime.exec, ProcessBuilder |
| class_loading | ClassLoader, defineClass, Class.forName, URLClassLoader |
| reflection | Method.invoke, getDeclaredMethod, setAccessible |
| native | System.load, System.loadLibrary |
| system_properties | System.getProperty, System.getenv, user.home, user.dir |
| crypto | MessageDigest, Cipher, SecureRandom, Random |
| threads | Thread, ExecutorService |
| ui_input | KeyEvent, MouseEvent, JFileChooser, JOptionPane |

Matching works on AST tokens of the symbol (declared types, types used in the body,
receiver identifiers, invoked names, string literals), never on comments:

- a simple name (`FileInputStream`, `Thread`, `readObject`) matches a declared or used type
  name, a receiver identifier (`Thread.sleep`, `KeyEvent.VK_UP`) or an invoked method name;
- a dotted name (`Runtime.exec`, `System.getProperty`) matches an invocation chain whose
  segments contain the owner before the final member (`Runtime.getRuntime().exec()`), a
  declared type plus an invoked name (`Method m; m.invoke()`), a static import of the member
  (`import static java.nio.file.Files.readAllLines`), or a string literal containing the
  pattern (`"user.home"`);
- `T(Arg)` matches `new T(...)` in a symbol that also mentions the `Arg` type.

A symbol is tagged with every category that has at least one match; `sinkMatches` records
the matching patterns per category. Tags are evidence of API use inside that symbol only: a
method calling `rand.nextInt()` on a `Random` field is not tagged, the field is.

## Edges

An invocation becomes an edge when name-based resolution leaves exactly one candidate:
methods with the same simple name, filtered by argument count (varargs aware), then by
receiver: a receiver naming a type in the model (`Foo.bar()`, `new Foo().bar()`) restricts to
that type; `this`, or no receiver, prefers the caller's own type and then its enclosing
types; `super.` and `super(...)` use the declared superclass when it is in the model;
`this(...)` and `new T(...)` resolve to constructors by arity. Anything still ambiguous
(overloads with equal arity, the same name in several unrelated classes) stays only in
`calls`.

## Library API

```python
from research.code_model import build, outline, resolve, symbol_text, validate_anchor, callers, sinks

model = build(root)                         # dict, canonical-JSON ready
outline(model)                              # str
resolve(model, file, start, end)            # enclosing symbol ids, innermost first; LookupError/ValueError
symbol_text(model, root, symbol_id)         # exact source lines of the symbol
validate_anchor(model, root, {"symbol": ..., "file": ..., "start_line": ..., "end_line": ...})
```

`validate_anchor` returns `{"ok", "reason", "file", "start", "end", "symbols", "textSha256"}`.
An anchor is valid when it names an existing symbol (the range defaults to the symbol's),
and/or an existing file with a line range inside it (`end_line` defaults to `start_line`);
when both are given the range must lie inside the symbol and the file must match. The source
file must still hash to the value recorded at build time. `textSha256` is the sha256 of the
exact bytes of the cited lines joined with `\n`, so a citation can be re-verified later.

## Determinism

Files are visited in sorted path order, symbols are sorted by (path, start, declaration
order), sets are emitted sorted, and the output is written with `canonical()` from
`research.import_evidence`. Building the same tree twice yields identical bytes; the
`fingerprint` changes only when a file's bytes change.

## Limits

- Name-based only: no type inference, no symbol table. Overloads with the same arity are not
  distinguished (`readLevel(String)` vs `readLevel(DataInputStream)` called with a local
  variable), and calls through variables are matched by method name alone.
- No inheritance-aware resolution: an unqualified call to an inherited method is only resolved
  when the name is unique in the model; overriding methods are never linked to the overridden
  one; dynamic dispatch is not modelled.
- Sink tags are lexical evidence of API use in a symbol, not data-flow; a field of a sink type
  used elsewhere is tagged on the field, not on its users.
- Anonymous classes, lambdas, local classes and initializer blocks do not get symbols; their
  content is attributed to the enclosing method or type.
- Line ranges are tree-sitter node extents (annotations included, Javadoc excluded); columns
  are not recorded.
- tree-sitter is error-tolerant: a file with syntax errors keeps the symbols that could be
  parsed and lists the errors in `parseErrors`.
