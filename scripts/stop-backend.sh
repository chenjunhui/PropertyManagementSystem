#!/bin/bash
# 停止所有后端实例
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
BASE_DIR="$(dirname "$SCRIPT_DIR")"
LOG_DIR="$BASE_DIR/logs"

if [ ! -d "$LOG_DIR" ]; then
    echo "No running instances found."
    exit 0
fi

for pidfile in "$LOG_DIR"/*.pid; do
    [ -f "$pidfile" ] || continue
    pid=$(cat "$pidfile")
    name=$(basename "$pidfile" .pid)
    if kill -0 "$pid" 2>/dev/null; then
        echo "Stopping $name (PID: $pid)..."
        kill "$pid"
        sleep 2
        if kill -0 "$pid" 2>/dev/null; then
            echo "  Force killing $name..."
            kill -9 "$pid"
        fi
        echo "  $name stopped."
    else
        echo "$name already stopped."
    fi
    rm -f "$pidfile"
done
echo "All instances stopped."
