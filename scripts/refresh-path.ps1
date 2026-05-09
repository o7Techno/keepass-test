# Reloads Machine + User PATH into this PowerShell session (e.g. after installing Maven).
# Use: . .\scripts\refresh-path.ps1   (leading dot = run in current scope so $env:Path updates)
$machine = [Environment]::GetEnvironmentVariable("Path", "Machine")
$user = [Environment]::GetEnvironmentVariable("Path", "User")
$env:Path = "$machine;$user"
Write-Host "PATH refreshed in this session. Try: mvn -v"
