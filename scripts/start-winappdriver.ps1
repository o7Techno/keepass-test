# Starts WinAppDriver (must run before UI tests). Default listens on http://127.0.0.1:4723
$candidates = @(
    "${env:ProgramFiles(x86)}\Windows Application Driver\WinAppDriver.exe",
    "$env:ProgramFiles\Windows Application Driver\WinAppDriver.exe"
)
$exe = $candidates | Where-Object { Test-Path $_ } | Select-Object -First 1
if (-not $exe) {
    Write-Error "WinAppDriver.exe not found. Install from https://github.com/microsoft/WinAppDriver/releases and adjust paths in scripts/start-winappdriver.ps1"
    exit 1
}
Write-Host "Starting: $exe"
# Если тесты не видят окно — запусти PowerShell от имени администратора и снова этот скрипт.
Start-Process -FilePath $exe
