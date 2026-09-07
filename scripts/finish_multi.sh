#!/usr/bin/env bash
# Wait for all audit drivers to drain, then run the full downstream pipeline.
# Self-contained so it survives waiter churn: one job does wait + process + print.
set -uo pipefail
cd "$(dirname "${BASH_SOURCE[0]}")/.." || exit 1

# 1. wait for driver processes to exit (xargs blocks until all runs done)
while [ "$(pgrep -f 'scripts/run_audit.sh' | wc -l | tr -d ' ')" != "0" ]; do
  sleep 30
done
echo "=== drivers drained; runs complete ==="
for fam in oxa codex glm kimi qwen minimax; do
  d=$(grep -l JSON_END audit/runs/$fam/*.log 2>/dev/null | wc -l | tr -d ' ')
  t=$(ls audit/runs/$fam/*.log 2>/dev/null | wc -l | tr -d ' ')
  echo "  $fam: $d/$t have JSON"
done

# 2. downstream pipeline
echo "=== EXTRACT ==="
python3 scripts/extract_findings.py
echo "=== RECONCILE ==="
python3 scripts/reconcile.py
echo "=== GEN REPORT ==="
python3 scripts/gen_report.py
echo "=== FINISH_MULTI_DONE ==="
