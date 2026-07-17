#!/bin/bash
# 后端实例健康检查 + 自动重启
# 配合 cron 使用: */5 * * * * /path/to/health-check.sh

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
BASE_DIR="$(dirname "$SCRIPT_DIR")"
LOG_DIR="$BASE_DIR/logs"
JAR_PATH="$BASE_DIR/01-项目源码/backend/target/property-management-system-1.0.0.jar"
JVM_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

mkdir -p "$LOG_DIR"

PORTS="2010 2011 2012"

for PORT in $PORTS; do
    NAME="backend-$PORT"
    PIDFILE="$LOG_DIR/$NAME.pid"

    # Check if process is running
    RUNNING=false
    if [ -f "$PIDFILE" ]; then
        PID=$(cat "$PIDFILE")
        if kill -0 "$PID" 2>/dev/null; then
            RUNNING=true
        fi
    fi

    if [ "$RUNNING" = false ]; then
        echo "[$(date)] $NAME is down. Restarting on port $PORT..."
        nohup java $JVM_OPTS -DSERVER_PORT=$PORT -jar "$JAR_PATH" \
            > "$LOG_DIR/$NAME.log" 2>&1 &
        echo $! > "$PIDFILE"
        echo "[$(date)] $NAME restarted (PID: $(cat "$PIDFILE"))"
    fi
done
