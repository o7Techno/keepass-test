#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
EXE=""
for cand in \
  "/c/Program Files (x86)/Windows Application Driver/WinAppDriver.exe" \
  "/c/Program Files/Windows Application Driver/WinAppDriver.exe"; do
  if [[ -f "$cand" ]]; then EXE="$cand"; break; fi
done
if [[ -z "${EXE}" ]]; then
  echo "WinAppDriver.exe not found"
  exit 1
fi
echo "WAD: $EXE"
"$EXE" &
WAD_PID=$!
trap 'kill "${WAD_PID}" 2>/dev/null || true' EXIT
sleep 2
(cd "$ROOT" && mvn -q test "$@")
