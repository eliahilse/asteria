#!/usr/bin/env bash
# Run the open-model audit families through ONE throttled queue.
#
# Why not per-family fan-out: launching 4 families x 5 = 20 concurrent opencode
# requests stalled the opencode-go provider, and opencode has no client-side
# timeout, so stuck requests hung forever and deadlocked the batch. Here:
#   - total concurrency is capped at 6 (one shared queue across all families),
#   - every run is hard-capped with gtimeout, so a stalled request dies and is
#     retried instead of hanging,
#   - completed runs (JSON_END present) are cached and skipped on retry.
set -uo pipefail

B="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
CORPUS="$B/corpus"
PROMPT_TMPL="$B/audit/prompts/variant_audit.md"
TIMEOUT="${RUN_TIMEOUT:-900}"
PAR="${1:-6}"
PASSES="${2:-2}"
FAMILIES=(glm kimi qwen minimax)

model_for() {
  case "$1" in
    glm)     echo "opencode-go/glm-5.3" ;;
    kimi)    echo "opencode-go/kimi-k3" ;;
    qwen)    echo "opencode-go/qwen3.8-max" ;;
    minimax) echo "opencode-go/minimax-m3" ;;
  esac
}
export -f model_for

run_pair() {
  local pair="$1"
  local family="${pair%%|*}" job="${pair#*|}"
  local variant="${job#*/}" path="$CORPUS/$job"
  local tag="${job//\//_}" out="$B/audit/runs/$family/$tag.log"
  mkdir -p "$B/audit/runs/$family"
  [ -d "$path" ] || { echo "SKIP  $family $job (missing)"; return; }
  if [ -s "$out" ] && grep -q 'JSON_END' "$out" 2>/dev/null; then
    echo "CACHED $family $job"; return
  fi
  local model; model=$(model_for "$family")
  local prompt; prompt=$(sed -e "s|{{VARIANT}}|$variant|g" -e "s|{{PATH}}|$path|g" "$PROMPT_TMPL")
  local start=$SECONDS
  gtimeout --signal=KILL "$TIMEOUT" \
    opencode run -m "$model" --variant max --dir "$path" --auto "$prompt" \
    < /dev/null > "$out" 2>&1
  local rc=$? dur=$(( SECONDS - start ))
  if grep -q 'JSON_END' "$out" 2>/dev/null; then
    echo "OK    $family $job (${dur}s)"
  elif [ "$rc" -eq 137 ] || [ "$rc" -eq 124 ]; then
    echo "TIMEOUT $family $job (${dur}s)"
  else
    echo "NOJSON $family $job (${dur}s rc=$rc)"
  fi
}
export -f run_pair
export B CORPUS PROMPT_TMPL TIMEOUT

build_jobs() {
  for fam in "${FAMILIES[@]}"; do
    for d in "$CORPUS"/java/*/;    do echo "$fam|java/$(basename "$d")"; done
    for d in "$CORPUS"/android/*/; do echo "$fam|android/$(basename "$d")"; done
  done
}

for pass in $(seq 1 "$PASSES"); do
  echo "===== PASS $pass/$PASSES (par=$PAR timeout=${TIMEOUT}s) ====="
  build_jobs | xargs -P "$PAR" -I{} bash -c 'run_pair "$@"' _ {}
  # status after this pass
  for fam in "${FAMILIES[@]}"; do
    d=$(grep -l JSON_END "$B/audit/runs/$fam"/*.log 2>/dev/null | wc -l | tr -d ' ')
    echo "  after pass $pass -- $fam: $d/26"
  done
done
echo "===== RUN_OPEN_ALL_DONE ====="
