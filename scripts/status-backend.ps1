# Backend multi-instance status checker
# Usage: powershell -ExecutionPolicy Bypass -File .\status-backend.ps1

$ports = @(2010, 2011, 2012)
$anyRunning = $false

foreach ($port in $ports) {
    $conn = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue |
            Where-Object { $_.State -eq "Listen" }

    if ($conn) {
        $proc = Get-Process -Id $conn.OwningProcess -ErrorAction SilentlyContinue |
                Where-Object { $_.ProcessName -eq "java" }

        if ($proc) {
            $mem = [math]::Round($proc.WorkingSet64 / 1MB, 1)
            $uptime = (Get-Date) - $proc.StartTime
            $upStr = "{0}h {1}m" -f $uptime.Hours, $uptime.Minutes
            Write-Host "  port $port : RUNNING  PID=$($proc.Id)  MEM=${mem}MB  UPTIME=$upStr" -ForegroundColor Green
            $anyRunning = $true
            continue
        }
    }

    Write-Host "  port $port : STOPPED" -ForegroundColor Red
}

if ($anyRunning) {
    Write-Host ""
    Write-Host "Summary: some instances running." -ForegroundColor Cyan
} else {
    Write-Host ""
    Write-Host "Summary: all instances stopped." -ForegroundColor Yellow
}
