#!/usr/bin/env bash
set -euo pipefail
EXE=""
for cand in \
  "/c/Program Files (x86)/Windows Application Driver/WinAppDriver.exe" \
  "/c/Program Files/Windows Application Driver/WinAppDriver.exe"; do
  if [[ -f "$cand" ]]; then EXE="$cand"; break; fi
done
if [[ -z "${EXE}" ]]; then
  echo "WinAppDriver.exe not found (install from https://github.com/microsoft/WinAppDriver/releases)"
  exit 1
fi
exec "$EXE"
