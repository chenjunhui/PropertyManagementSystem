# Stop nginx
# Usage: powershell -ExecutionPolicy Bypass -File .\stop-nginx.ps1

$nginxProc = Get-Process -Name "nginx" -ErrorAction SilentlyContinue

if (-not $nginxProc) {
    Write-Host "nginx is not running" -ForegroundColor Yellow
    exit 0
}

Write-Host "Stopping nginx (PID: $($nginxProc.Id -join ', '))..." -ForegroundColor Cyan
$nginxProc | Stop-Process -Force
Start-Sleep -Seconds 1

$check = Get-Process -Name "nginx" -ErrorAction SilentlyContinue
if (-not $check) {
    Write-Host "nginx stopped" -ForegroundColor Green
} else {
    Write-Host "nginx may still be running" -ForegroundColor Yellow
}
