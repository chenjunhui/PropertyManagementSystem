# Start nginx with project config
# Usage: powershell -ExecutionPolicy Bypass -File .\start-nginx.ps1

param(
    [string]$NginxDir = "C:\nginx\nginx-1.26.2"
)

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$ProjectRoot = Split-Path -Parent $ScriptDir
$ProjectConf = Join-Path $ProjectRoot "03-deploy\nginx\nginx-win.conf"

# Check nginx exists
$NginxExe = Join-Path $NginxDir "nginx.exe"
if (-not (Test-Path $NginxExe)) {
    Write-Host "ERROR: nginx.exe not found at $NginxExe" -ForegroundColor Red
    exit 1
}

# Check if already running
$running = Get-NetTCPConnection -LocalPort 80 -ErrorAction SilentlyContinue |
           Where-Object { $_.State -eq "Listen" }

if ($running) {
    Write-Host "Port 80 already in use, checking nginx process..." -ForegroundColor Yellow
    $nginxProc = Get-Process -Name "nginx" -ErrorAction SilentlyContinue
    if ($nginxProc) {
        Write-Host "nginx is already running (PID: $($nginxProc.Id -join ', '))" -ForegroundColor Green
        exit 0
    }
    Write-Host "Port 80 occupied by other process, cannot start nginx" -ForegroundColor Red
    exit 1
}

# Use project config
if (Test-Path $ProjectConf) {
    Write-Host "Starting nginx with project config..." -ForegroundColor Cyan
    Write-Host "  Config: $ProjectConf" -ForegroundColor Gray
    Push-Location $NginxDir
    Start-Process -FilePath $NginxExe -ArgumentList "-c `"$ProjectConf`"" -WindowStyle Hidden
    Pop-Location
} else {
    Write-Host "Project config not found, starting with default..." -ForegroundColor Yellow
    Push-Location $NginxDir
    Start-Process -FilePath $NginxExe -WindowStyle Hidden
    Pop-Location
}

Start-Sleep -Seconds 1

# Verify
$check = Get-NetTCPConnection -LocalPort 80 -ErrorAction SilentlyContinue |
         Where-Object { $_.State -eq "Listen" }

if ($check) {
    Write-Host "nginx started successfully on port 80" -ForegroundColor Green
    Write-Host ""
    Write-Host "Access URLs:" -ForegroundColor Cyan
    Write-Host "  Admin:      http://localhost/admin/" -ForegroundColor White
    Write-Host "  Client:     http://localhost/client/" -ForegroundColor White
    Write-Host "  Monitor:    http://localhost/monitor/" -ForegroundColor White
    Write-Host "  API:        http://localhost/api/" -ForegroundColor White
    Write-Host "  API Docs:   http://localhost/swagger-ui/index.html" -ForegroundColor White
} else {
    Write-Host "nginx failed to start, check logs in $NginxDir\logs\" -ForegroundColor Red
}
