$candidates = @(
    "${env:ProgramFiles(x86)}\Windows Application Driver\WinAppDriver.exe",
    "$env:ProgramFiles\Windows Application Driver\WinAppDriver.exe"
)
$exe = $candidates | Where-Object { Test-Path $_ } | Select-Object -First 1
if (-not $exe) {
    Write-Error "WinAppDriver.exe not found."
    exit 1
}
Write-Host "Starting $exe"
Start-Process -FilePath $exe
