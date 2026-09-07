#!/usr/bin/env bash
# Normalize Apo-Games into one .java-source tree per variant.
# Source comes from loose files and/or source bundled inside shipped jars.
set -uo pipefail
B="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SRC="$B/apogames"
OUT="$B/corpus"
rm -rf "$OUT/java" "$OUT/android"; mkdir -p "$OUT/java" "$OUT/android"

norm() { # $1=variant dir  $2=dest
  local vd="$1" dest="$2"; mkdir -p "$dest"
  # loose .java
  while IFS= read -r f; do
    rel="${f#$vd/}"; mkdir -p "$dest/$(dirname "$rel")"; cp "$f" "$dest/$rel" 2>/dev/null
  done < <(find "$vd" -name '*.java' -type f)
  # .java inside jars
  while IFS= read -r j; do
    tmp=$(mktemp -d)
    unzip -qq -o "$j" '*.java' -d "$tmp" 2>/dev/null
    if [ -n "$(find "$tmp" -name '*.java' -print -quit 2>/dev/null)" ]; then
      mkdir -p "$dest/_from_jar_$(basename "$j" .jar)"
      (cd "$tmp" && tar cf - .) | (cd "$dest/_from_jar_$(basename "$j" .jar)" && tar xf -) 2>/dev/null
    fi
    rm -rf "$tmp"
  done < <(find "$vd" -name '*.jar' -type f)
}

for d in "$SRC/Java"/*/; do v=$(basename "$d"); norm "$d" "$OUT/java/$v"; done
for d in "$SRC/Android"/*/; do [ -d "$d" ] || continue; v=$(basename "$d"); norm "$d" "$OUT/android/$v"; done

echo "variant,platform,java_files,loc"
for p in java android; do
  for d in "$OUT/$p"/*/; do
    v=$(basename "$d")
    n=$(find "$d" -name '*.java' -type f | wc -l | tr -d ' ')
    l=$(find "$d" -name '*.java' -type f -exec cat {} + 2>/dev/null | wc -l | tr -d ' ')
    echo "$v,$p,$n,$l"
  done
done
