#!/bin/bash
# 物业管理系统 - 后端多实例启动脚本 (Linux/Mac)
# 用法: ./start-backend.sh [实例编号|all]
# 示例: ./start-backend.sh 1   (启动实例1，端口2010)
#        ./start-backend.sh all (启动所有3个实例)

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
BASE_DIR="$(dirname "$SCRIPT_DIR")"
JAR_PATH="$BASE_DIR/01-项目源码/backend/target/property-management-system-1.0.0.jar"
JVM_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Djava.security.egd=file:/dev/./urandom"

start_instance() {
    local port=$1
    local name="backend-$port"
    echo "Starting $name on port $port..."
    nohup java $JVM_OPTS -DSERVER_PORT=$port -jar "$JAR_PATH" \
        > "$BASE_DIR/logs/$name.log" 2>&1 &
    echo $! > "$BASE_DIR/logs/$name.pid"
    echo "  $name started (PID: $(cat "$BASE_DIR/logs/$name.pid"))"
}

if [ "$1" = "all" ]; then
    mkdir -p "$BASE_DIR/logs"
    echo "Starting all backend instances..."
    start_instance 2010
    start_instance 2011
    start_instance 2012
    echo "All instances started. Logs: $BASE_DIR/logs/"
elif [ -n "$1" ]; then
    mkdir -p "$BASE_DIR/logs"
    start_instance "$1"
else
    echo "Starting backend on port 2010..."
    java $JVM_OPTS -DSERVER_PORT=2010 -jar "$JAR_PATH"
fi
