# Backend multi-instance restarter
# Usage: powershell -ExecutionPolicy Bypass -File .\restart-backend.ps1 [port|all]

param(
    [string]$Port = "2010"
)

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition

Write-Host "Stopping backend..." -ForegroundColor Yellow
& "$ScriptDir\stop-backend.ps1" $Port

Start-Sleep -Seconds 2

Write-Host "Starting backend..." -ForegroundColor Cyan
& "$ScriptDir\start-backend.ps1" $Port

Write-Host "Restart complete." -ForegroundColor Green
