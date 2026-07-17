# Backend multi-instance launcher
# Usage: powershell -ExecutionPolicy Bypass -File .\start-backend.ps1 [port|all]

param(
    [string]$Port = "2010"
)

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$ProjectRoot = Split-Path -Parent $ScriptDir

# Find JAR in target directory
$TargetDir = Get-ChildItem -Path $ProjectRoot -Recurse -Filter "property-management-system-*.jar" -File -ErrorAction SilentlyContinue | Where-Object { $_.DirectoryName -like "*target*" } | Select-Object -First 1

if (-not $TargetDir) {
    Write-Host "ERROR: JAR not found. Please build first: mvn clean package -DskipTests" -ForegroundColor Red
    exit 1
}

$JarPath = $TargetDir.FullName
$JvmOpts = "-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

function Start-Instance($port) {
    Write-Host "Starting backend on port $port..." -ForegroundColor Cyan
    Start-Process -FilePath "java" -ArgumentList "$JvmOpts -DSERVER_PORT=$port -jar `"$JarPath`"" -WindowStyle Minimized
}

if ($Port -eq "all") {
    Write-Host "Starting all backend instances..." -ForegroundColor Green
    Start-Instance 2010
    Start-Instance 2011
    Start-Instance 2012
    Write-Host "All instances started." -ForegroundColor Green
} else {
    Start-Instance $Port
}
