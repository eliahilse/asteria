import json
import subprocess
import sys
import tempfile
import unittest
from pathlib import Path

from research.code_model import SINKS, build, callers, outline, resolve, sinks, symbol_text, validate_anchor
from research.import_evidence import ROOT, canonical, digest

try:
    import tree_sitter_java  # noqa: F401
    HAVE_TREE_SITTER = True
except ImportError:
    HAVE_TREE_SITTER = False

STORE = "com/example/io/LevelStore.java"
MAIN = "com/example/app/Main.java"
BROKEN = "com/example/app/Broken.java"
FIXTURE = {
    STORE: '''package com.example.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class LevelStore {
    private final File base;
    private int count = 0;

    public LevelStore(File base) {
        this.base = base;
    }

    public String read(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        List<String> all = Files.readAllLines(base.toPath());
        return line + all.size();
    }

    public String read(Path path, int limit) {
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
            return String.valueOf(in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public void run(String[] args, int... more) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                count = count + 1;
                helper(42);
            }
        };
        Runnable lambda = () -> helper(7);
        task.run();
        lambda.run();
        new Scanner(new File("x")).close();
    }

    private void helper(int value) {
        System.getProperty("user.home");
    }

    public static class Entry {
        public int score = 3;

        public int score() {
            return score * 2;
        }
    }
}
''',
    MAIN: '''package com.example.app;

import com.example.io.LevelStore;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        LevelStore store = new LevelStore(new File(args[0]));
        LevelStore.Entry entry = new LevelStore.Entry();
        store.run(args);
        entry.score();
    }
}
''',
    BROKEN: '''package com.example.app;

public class Broken {
    public void ok() { }
    public void oops( {
}
''',
}
STORE_ID = "com.example.io.LevelStore"
READ_READER = f"{STORE_ID}#read(BufferedReader)"
READ_PATH = f"{STORE_ID}#read(Path,int)"
RUN = f"{STORE_ID}#run(String[],int...)"
HELPER = f"{STORE_ID}#helper(int)"
MAIN_ID = "com.example.app.Main#main(String[])"


def line(path: str, needle: str) -> int:
    return next(i for i, text in enumerate(FIXTURE[path].splitlines(), 1) if needle in text)


def write_fixture(directory: Path) -> Path:
    for rel, text in FIXTURE.items():
        target = directory / rel
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_text(text)
    return directory


@unittest.skipUnless(HAVE_TREE_SITTER, 'tree-sitter not installed: pip install -r research/code-model-requirements.txt')
class SyntheticTreeTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.tmp = tempfile.TemporaryDirectory()
        cls.root = write_fixture(Path(cls.tmp.name))
        cls.model = build(cls.root)
        cls.symbols = {s["id"]: s for s in cls.model["symbols"]}

    @classmethod
    def tearDownClass(cls):
        cls.tmp.cleanup()

    def test_files_are_sorted_and_hashed(self):
        paths = [f["path"] for f in self.model["files"]]
        self.assertEqual(paths, sorted(FIXTURE))
        store = next(f for f in self.model["files"] if f["path"] == STORE)
        self.assertEqual(store["package"], "com.example.io")
        self.assertEqual(store["lines"], FIXTURE[STORE].count("\n"))
        self.assertEqual(store["sha256"], digest(FIXTURE[STORE].encode()))
        self.assertIn("java.nio.file.Files", store["imports"])
        self.assertEqual(store["parseErrors"], [])
        self.assertEqual(self.model["schemaVersion"], 1)
        self.assertEqual(self.model["sinks"], SINKS)
        self.assertEqual(len(self.model["fingerprint"]), 64)

    def test_symbol_ids_and_ranges(self):
        store = self.symbols[STORE_ID]
        self.assertEqual((store["kind"], store["start"], store["end"]), ("class", line(STORE, "public class LevelStore"), FIXTURE[STORE].count("\n")))
        self.assertIsNone(store["owner"])
        self.assertEqual(self.symbols[f"{STORE_ID}.base"]["type"], "File")
        self.assertEqual(self.symbols[f"{STORE_ID}.count"]["kind"], "field")
        ctor = self.symbols[f"{STORE_ID}#LevelStore(File)"]
        self.assertEqual((ctor["kind"], ctor["start"], ctor["end"]), ("constructor", line(STORE, "public LevelStore(File base)"), line(STORE, "public LevelStore(File base)") + 2))
        reader = self.symbols[READ_READER]
        self.assertEqual((reader["start"], reader["end"]), (line(STORE, "public String read(BufferedReader"), line(STORE, "return line + all.size();") + 1))
        self.assertEqual(reader["params"], [{"name": "reader", "type": "BufferedReader"}])
        self.assertEqual((reader["returns"], reader["throws"], reader["modifiers"]), ("String", ["IOException"], ["public"]))
        self.assertIn(READ_PATH, self.symbols)
        run = self.symbols[RUN]
        self.assertEqual((run["start"], run["end"]), (line(STORE, "public void run(String[] args"), line(STORE, 'new Scanner(new File("x")).close();') + 1))
        entry = self.symbols[f"{STORE_ID}.Entry"]
        self.assertEqual((entry["kind"], entry["owner"], entry["modifiers"]), ("class", STORE_ID, ["public", "static"]))
        self.assertEqual(self.symbols[f"{STORE_ID}.Entry.score"]["type"], "int")
        self.assertEqual(self.symbols[f"{STORE_ID}.Entry#score()"]["owner"], f"{STORE_ID}.Entry")
        self.assertEqual(self.symbols[MAIN_ID]["modifiers"], ["public", "static"])
        ordered = [(s["file"], s["start"]) for s in self.model["symbols"]]
        self.assertEqual(ordered, sorted(ordered))

    def test_calls_catches_literals_and_anonymous_attribution(self):
        self.assertEqual(self.symbols[READ_READER]["calls"], ["reader.readLine", "Files.readAllLines", "base.toPath", "all.size"])
        path = self.symbols[READ_PATH]
        self.assertEqual(path["catches"], ["IOException", "ClassNotFoundException"])
        self.assertIn("in.readObject", path["calls"])
        self.assertEqual(path["creates"], ["ObjectInputStream"])
        run = self.symbols[RUN]
        self.assertEqual(run["calls"], ["helper", "task.run", "lambda.run", "close"])
        self.assertEqual(run["creates"], ["Runnable", "Scanner", "File"])
        self.assertEqual([i for i in self.symbols if "#run(" in i], [RUN])
        self.assertEqual(self.symbols[HELPER]["literals"], {"stringCount": 1, "numericCount": 0})
        self.assertEqual(run["literals"], {"stringCount": 1, "numericCount": 3})

    def test_sink_tags(self):
        self.assertEqual(self.symbols[READ_READER]["sinkMatches"], {"file_read": ["BufferedReader", "Files.readAllLines"]})
        self.assertEqual(self.symbols[READ_PATH]["sinks"], ["deserialization"])
        self.assertEqual(self.symbols[READ_PATH]["sinkMatches"]["deserialization"], ["ObjectInputStream", "readObject"])
        self.assertEqual(self.symbols[RUN]["sinkMatches"], {"file_read": ["Scanner(File)"]})
        self.assertEqual(self.symbols[HELPER]["sinkMatches"], {"system_properties": ["System.getProperty", "user.home"]})
        self.assertEqual(self.symbols[f"{STORE_ID}.base"]["sinks"], [])
        self.assertEqual([s["id"] for s in sinks(self.model, "deserialization")], [READ_PATH])
        self.assertEqual({s["id"] for s in sinks(self.model)}, {READ_READER, READ_PATH, RUN, HELPER})
        with self.assertRaises(LookupError):
            sinks(self.model, "nope")

    def test_edges_resolve_by_name_and_arity(self):
        edges = {(e["from"], e["to"]) for e in self.model["edges"]}
        self.assertIn((RUN, HELPER), edges)
        self.assertIn((MAIN_ID, f"{STORE_ID}#LevelStore(File)"), edges)
        self.assertIn((MAIN_ID, RUN), edges)  # varargs: run(args) has arity 1
        self.assertIn((MAIN_ID, f"{STORE_ID}.Entry#score()"), edges)
        self.assertNotIn((RUN, RUN), edges)  # task.run() has arity 0 and does not match run(String[],int...)
        self.assertEqual(self.model["edges"], sorted(self.model["edges"], key=lambda e: (e["from"], e["to"])))

    def test_callers(self):
        self.assertEqual([s["id"] for s in callers(self.model, "readLine")], [READ_READER])
        self.assertEqual([s["id"] for s in callers(self.model, "reader.readLine")], [READ_READER])
        self.assertEqual([s["id"] for s in callers(self.model, "helper")], [RUN])
        self.assertEqual([s["id"] for s in callers(self.model, HELPER)], [RUN])
        self.assertEqual(callers(self.model, "nothingCallsThis"), [])

    def test_outline(self):
        text = outline(self.model)
        lines = text.splitlines()
        self.assertIn(STORE, lines)
        reader = self.symbols[READ_READER]
        self.assertIn(f"  class {STORE_ID} [L{self.symbols[STORE_ID]['start']}-L{self.symbols[STORE_ID]['end']}]", lines)
        self.assertIn(f"    method read(BufferedReader): String [L{reader['start']}-L{reader['end']}] sinks=file_read", lines)
        self.assertIn(f"    constructor LevelStore(File) [L{line(STORE, 'public LevelStore(File base)')}-L{line(STORE, 'public LevelStore(File base)') + 2}]", lines)
        self.assertIn(f"    field base: File [L{line(STORE, 'private final File base;')}-L{line(STORE, 'private final File base;')}]", lines)
        self.assertIn(f"    class {STORE_ID}.Entry [L{self.symbols[STORE_ID + '.Entry']['start']}-L{self.symbols[STORE_ID + '.Entry']['end']}]", lines)
        self.assertTrue(any(l.startswith("      method score(): int [L") for l in lines))
        self.assertTrue(any(l.startswith("  ! syntax error") or l.startswith("  ! missing") for l in lines))

    def test_resolve(self):
        row = line(STORE, "reader.readLine()")
        self.assertEqual(resolve(self.model, STORE, row, row), [READ_READER, STORE_ID])
        score = line(STORE, "return score * 2;")
        self.assertEqual(resolve(self.model, STORE, score, score), [f"{STORE_ID}.Entry#score()", f"{STORE_ID}.Entry", STORE_ID])
        self.assertEqual(resolve(self.model, STORE, 1, 1), [])
        with self.assertRaises(LookupError):
            resolve(self.model, "com/example/io/Missing.java", 1, 1)
        with self.assertRaises(ValueError):
            resolve(self.model, STORE, 5, 9999)

    def test_symbol_text(self):
        text = symbol_text(self.model, self.root, HELPER)
        self.assertTrue(text.startswith("    private void helper(int value) {"))
        self.assertIn('System.getProperty("user.home");', text)
        self.assertTrue(text.rstrip().endswith("}"))
        with self.assertRaises(KeyError):
            symbol_text(self.model, self.root, "nope")

    def test_validate_anchor(self):
        helper = self.symbols[HELPER]
        lines = FIXTURE[STORE].encode().split(b"\n")
        result = validate_anchor(self.model, self.root, {"symbol": HELPER, "file": None, "start_line": None, "end_line": None})
        self.assertEqual(result, {"ok": True, "reason": None, "file": STORE, "start": helper["start"], "end": helper["end"],
                                  "symbols": [HELPER, STORE_ID], "textSha256": digest(b"\n".join(lines[helper["start"] - 1:helper["end"]]))})
        inside = validate_anchor(self.model, self.root, {"symbol": HELPER, "file": STORE, "start_line": helper["start"] + 1, "end_line": helper["start"] + 1})
        self.assertTrue(inside["ok"])
        self.assertEqual(inside["textSha256"], digest(lines[helper["start"]]))
        by_lines = validate_anchor(self.model, self.root, {"symbol": None, "file": STORE, "start_line": helper["start"] + 1, "end_line": None})
        self.assertEqual((by_lines["ok"], by_lines["end"], by_lines["symbols"]), (True, helper["start"] + 1, [HELPER, STORE_ID]))
        wrong_file = validate_anchor(self.model, self.root, {"symbol": HELPER, "file": MAIN, "start_line": None, "end_line": None})
        self.assertFalse(wrong_file["ok"])
        self.assertIn("not " + MAIN, wrong_file["reason"])
        outside = validate_anchor(self.model, self.root, {"symbol": HELPER, "file": STORE, "start_line": 1, "end_line": helper["end"]})
        self.assertFalse(outside["ok"])
        self.assertIn("outside", outside["reason"])
        unknown = validate_anchor(self.model, self.root, {"symbol": "com.example.Nope#x()", "file": None, "start_line": None, "end_line": None})
        self.assertEqual((unknown["ok"], unknown["reason"]), (False, "unknown symbol: com.example.Nope#x()"))
        self.assertFalse(validate_anchor(self.model, self.root, {"symbol": None, "file": STORE, "start_line": None, "end_line": None})["ok"])
        self.assertFalse(validate_anchor(self.model, self.root, {"symbol": None, "file": "x/Y.java", "start_line": 1, "end_line": 1})["ok"])
        self.assertFalse(validate_anchor(self.model, self.root, {"symbol": None, "file": STORE, "start_line": 3, "end_line": 2})["ok"])
        self.assertFalse(validate_anchor(self.model, self.root, {"symbol": None, "file": None, "start_line": 1, "end_line": 1})["ok"])

    def test_validate_anchor_detects_modified_source(self):
        with tempfile.TemporaryDirectory() as directory:
            root = write_fixture(Path(directory))
            model = build(root)
            (root / STORE).write_text(FIXTURE[STORE].replace("user.home", "user.dir"))
            result = validate_anchor(model, root, {"symbol": HELPER, "file": None, "start_line": None, "end_line": None})
            self.assertFalse(result["ok"])
            self.assertIn("changed since the model was built", result["reason"])

    def test_deterministic_output(self):
        self.assertEqual(canonical(build(self.root)), canonical(self.model))

    def test_parse_error_is_recorded_not_fatal(self):
        broken = next(f for f in self.model["files"] if f["path"] == BROKEN)
        self.assertTrue(broken["parseErrors"])
        self.assertTrue(all(isinstance(e, str) for e in broken["parseErrors"]))
        self.assertIn("com.example.app.Broken", self.symbols)
        self.assertIn(MAIN_ID, self.symbols)

    def test_cli_build_and_query(self):
        with tempfile.TemporaryDirectory() as directory:
            root = write_fixture(Path(directory) / "src")
            out = Path(directory) / "model.json"
            summary = Path(directory) / "outline.md"
            proc = subprocess.run([sys.executable, "-m", "research.code_model", "build", "--root", str(root), "--output", str(out), "--outline", str(summary)],
                                  cwd=ROOT, capture_output=True, text=True)
            self.assertEqual(proc.returncode, 0, proc.stderr)
            self.assertEqual(json.loads(proc.stdout)["files"], 3)
            self.assertEqual(json.loads(out.read_text())["symbols"], self.model["symbols"])
            self.assertEqual(summary.read_text(), outline(self.model))
            query = subprocess.run([sys.executable, "-m", "research.code_model", "query", "--model", str(out), "resolve", "--file", STORE, "--start", "21", "--end", "21"],
                                   cwd=ROOT, capture_output=True, text=True)
            self.assertEqual(query.returncode, 0, query.stderr)
            self.assertEqual(json.loads(query.stdout)["symbols"], resolve(self.model, STORE, 21, 21))
            missing = subprocess.run([sys.executable, "-m", "research.code_model", "query", "--model", str(out), "symbol", "nope"],
                                     cwd=ROOT, capture_output=True, text=True)
            self.assertEqual((missing.returncode, json.loads(missing.stdout)), (1, {"error": "unknown symbol: nope"}))
            text = subprocess.run([sys.executable, "-m", "research.code_model", "query", "--model", str(out), "symbol", HELPER],
                                  cwd=ROOT, capture_output=True, text=True)
            self.assertIn('System.getProperty("user.home")', json.loads(text.stdout)["text"])


@unittest.skipUnless(HAVE_TREE_SITTER, 'tree-sitter not installed: pip install -r research/code-model-requirements.txt')
class ApoMarioTests(unittest.TestCase):
    ROOT = ROOT / "corpus/java/ApoMario/_from_jar_ApoMario"

    def test_real_tree_builds_with_plausible_counts(self):
        if not self.ROOT.is_dir():
            self.skipTest("ApoMario corpus not present")
        model = build(self.ROOT)
        self.assertGreater(len(model["files"]), 100)
        self.assertEqual([f["path"] for f in model["files"] if f["parseErrors"]], [])
        kinds = {}
        for symbol in model["symbols"]:
            kinds[symbol["kind"]] = kinds.get(symbol["kind"], 0) + 1
        self.assertGreater(len(model["symbols"]), 500)
        for kind in ("class", "method", "constructor", "field"):
            self.assertGreater(kinds.get(kind, 0), 10, kinds)
        self.assertGreater(len(model["edges"]), 100)
        symbols = {s["id"]: s for s in model["symbols"]}
        read_level = symbols["apoMario.level.ApoMarioLevelIO#readLevel(DataInputStream)"]
        self.assertEqual(read_level["sinkMatches"], {"file_read": ["DataInputStream"]})
        self.assertIn("data.readInt", read_level["calls"])
        self.assertTrue(any("ui_input" in s["sinks"] for s in model["symbols"]))
        lines = {f["path"]: f["lines"] for f in model["files"]}
        for symbol in model["symbols"]:
            self.assertTrue(1 <= symbol["start"] <= symbol["end"] <= lines[symbol["file"]], symbol["id"])
            if symbol["owner"]:
                owner = symbols[symbol["owner"]]
                self.assertTrue(owner["start"] <= symbol["start"] and symbol["end"] <= owner["end"], symbol["id"])
        ids = {e["from"] for e in model["edges"]} | {e["to"] for e in model["edges"]}
        self.assertTrue(ids <= set(symbols))
        self.assertIn("apoMario/level/ApoMarioLevelIO.java", outline(model))
        anchor = validate_anchor(model, self.ROOT, {"symbol": read_level["id"], "file": None, "start_line": None, "end_line": None})
        self.assertTrue(anchor["ok"], anchor)


if __name__ == "__main__":
    unittest.main()
