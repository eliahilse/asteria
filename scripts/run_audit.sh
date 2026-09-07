#!/usr/bin/env bash
# Fan out the per-variant security audit across independent model families.
#
# Families (see MODEL map below):
#   oxa    -> ox-alpha            (opencode)   family A
#   codex  -> gpt-5.6-sol max     (codex exec) family B
#   glm    -> GLM-5.3             (opencode)   open-weight baseline
#   kimi   -> Kimi-k3             (opencode)   open-weight
#   deepseek -> DeepSeek-v4-pro   (opencode)   open-weight
#   qwen   -> Qwen3.8-max         (opencode)   Alibaba, additional family
#
# Cross-family agreement is the cheap defense against single-model hallucination;
# an open-weight family (glm/kimi/deepseek) also satisfies the community
# guideline (arXiv:2508.15503, #6) to include an open LLM baseline. Raw
# transcripts are kept so extraction is auditable.
#
# usage: run_audit.sh <family> <parallelism> [variant ...]
set -uo pipefail

B="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
CORPUS="$B/corpus"
PROMPT_TMPL="$B/audit/prompts/variant_audit.md"

FAMILY="${1:?family required}"
PAR="${2:-6}"
shift 2 || true

# family -> opencode model slug + reasoning variant (bash 3.2-safe; codex special)
model_for() {
  case "$1" in
    oxa)      echo "opencode-go/ox-alpha-free|" ;;
    glm)      echo "opencode-go/glm-5.3|max" ;;
    kimi)     echo "opencode-go/kimi-k3|max" ;;
    # deepseek-v4-pro is geo-locked (China-only opt-in) via this provider — dropped
    qwen)     echo "opencode-go/qwen3.8-max|max" ;;
    minimax)  echo "opencode-go/minimax-m3|max" ;;
    *)        echo "|" ;;
  esac
}
export -f model_for

OUTDIR="$B/audit/runs/$FAMILY"
mkdir -p "$OUTDIR"

if [ "$#" -gt 0 ]; then
  JOBS=("$@")
else
  JOBS=()
  for d in "$CORPUS"/java/*/;    do JOBS+=("java/$(basename "$d")"); done
  for d in "$CORPUS"/android/*/; do JOBS+=("android/$(basename "$d")"); done
fi

run_one() {
  local job="$1" family="$2"
  local variant="${job#*/}"
  local path="$CORPUS/$job"
  local tag="${job//\//_}"
  local out="$OUTDIR/$tag.log"

  [ -d "$path" ] || { echo "SKIP  $job (missing)"; return; }
  if [ -s "$out" ] && grep -q 'JSON_END' "$out" 2>/dev/null; then
    echo "CACHED $family $job"; return
  fi

  local prompt
  prompt=$(sed -e "s|{{VARIANT}}|$variant|g" -e "s|{{PATH}}|$path|g" "$PROMPT_TMPL")

  local start=$SECONDS
  if [ "$family" = "codex" ]; then
    ( cd "$path" && codex exec -m gpt-5.6-sol \
        -c model_reasoning_effort=max -c model_service_tier=fast \
        --sandbox read-only --skip-git-repo-check "$prompt" \
        < /dev/null ) > "$out" 2>&1
  else
    local spec model variant
    spec=$(model_for "$family"); model="${spec%%|*}"; variant="${spec#*|}"
    [ -z "$model" ] && { echo "ERR unknown family $family"; return; }
    local vflag=()
    [ -n "$variant" ] && vflag=(--variant "$variant")
    opencode run -m "$model" "${vflag[@]}" --dir "$path" --auto "$prompt" \
      < /dev/null > "$out" 2>&1
  fi
  local dur=$(( SECONDS - start ))

  if grep -q 'JSON_END' "$out" 2>/dev/null; then
    echo "OK    $family $job (${dur}s)"
  else
    echo "NOJSON $family $job (${dur}s) -- see $out"
  fi
}
export -f run_one
export CORPUS OUTDIR PROMPT_TMPL B

printf '%s\n' "${JOBS[@]}" \
  | xargs -P "$PAR" -I{} bash -c 'run_one "$@"' _ {} "$FAMILY"

echo "=== $FAMILY done: $(ls "$OUTDIR"/*.log 2>/dev/null | wc -l | tr -d ' ') logs ==="
