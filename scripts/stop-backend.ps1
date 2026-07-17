# Backend multi-instance stopper
# Usage: powershell -ExecutionPolicy Bypass -File .\stop-backend.ps1 [port|all]

param(
    [string]$Port = "2010"
)

function Stop-Instance($port) {
    $procs = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue |
             Select-Object -ExpandProperty OwningProcess -Unique |
             ForEach-Object { Get-Process -Id $_ -ErrorAction SilentlyContinue } |
             Where-Object { $_.ProcessName -eq "java" }

    if ($procs) {
        $procs | Stop-Process -Force
        Write-Host "Stopped backend on port $port (PID: $($procs.Id -join ', '))" -ForegroundColor Green
    } else {
        Write-Host "No backend running on port $port" -ForegroundColor Yellow
    }
}

if ($Port -eq "all") {
    Write-Host "Stopping all backend instances..." -ForegroundColor Cyan
    Stop-Instance 2010
    Stop-Instance 2011
    Stop-Instance 2012
    Write-Host "All instances stopped." -ForegroundColor Green
} else {
    Stop-Instance $Port
}
