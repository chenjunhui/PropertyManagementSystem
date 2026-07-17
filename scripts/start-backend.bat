@echo off
setlocal

set SCRIPT_DIR=%~dp0
set PROJECT_ROOT=%SCRIPT_DIR%..

REM Find JAR file in target directory
for /r "%PROJECT_ROOT%" %%f in (property-management-system-*.jar) do (
    echo %%f | findstr /i "target" >nul && (
        set JAR_PATH=%%f
        goto :found
    )
)
echo ERROR: JAR not found. Please build first: mvn clean package -DskipTests
exit /b 1

:found
set JVM_OPTS=-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200

if "%1"=="all" (
    echo Starting all backend instances...
    start "Backend-1" cmd /c "set SERVER_PORT=2010 && java %JVM_OPTS% -jar "%JAR_PATH%""
    echo   Instance 1: port 2010
    start "Backend-2" cmd /c "set SERVER_PORT=2011 && java %JVM_OPTS% -jar "%JAR_PATH%""
    echo   Instance 2: port 2011
    start "Backend-3" cmd /c "set SERVER_PORT=2012 && java %JVM_OPTS% -jar "%JAR_PATH%""
    echo   Instance 3: port 2012
    echo All instances started.
    goto :eof
)

set PORT=%1
if "%PORT%"=="" set PORT=2010

echo Starting backend on port %PORT%...
java %JVM_OPTS% -DSERVER_PORT=%PORT% -jar "%JAR_PATH%"
