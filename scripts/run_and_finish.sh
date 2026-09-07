#!/usr/bin/env bash
# One job: throttled open-model audit (with timeouts + retry) then full pipeline.
set -uo pipefail
export PATH="/opt/homebrew/bin:$PATH"   # gtimeout
cd "$(dirname "${BASH_SOURCE[0]}")/.." || exit 1

echo "### open-model audit starting $(date '+%H:%M:%S')"
./scripts/run_open_all.sh 6 2

echo "### processing $(date '+%H:%M:%S')"
python3 scripts/extract_findings.py
python3 scripts/reconcile.py
python3 scripts/gen_report.py
echo "### RUN_AND_FINISH_DONE $(date '+%H:%M:%S')"
