#!/bin/bash

LOG_DIR="./logs"
mkdir -p "$LOG_DIR"

PID_FILE="/tmp/disk_monitor.pid"

INTERVAL=60

current_date() {
    echo $(date '+%Y-%m-%d')
}

current_timestamp() {
    echo $(date '+%Y-%m-%d_%H-%M-%S')
}

monitor_disk() {
    local timestamp=$(current_timestamp)
    local date=$(current_date)
    local logfile="$LOG_DIR/disk_monitor_${timestamp}_${date}.csv"
    
    echo "timestamp,filesystem,used_space,available_space,used_inodes,available_inodes" > "$logfile"
    
    while true; do
        local now=$(date '+%Y-%m-%d %H:%M:%S')
        
        df_output=$(df -h | awk 'NR>1 {print $1","$5","$4}')
        
        inode_output=$(df -i | awk 'NR>1 {print $1","$5","$4}')

        while IFS= read -r line; do
            fs=$(echo $line | awk -F',' '{print $1}')
            used_space=$(echo $line | awk -F',' '{print $2}' | tr -d '%')
            available_space=$(echo $line | awk -F',' '{print $3}')
            
            inode_line=$(echo "$inode_output" | grep "^$fs")
            used_inodes=$(echo $inode_line | awk -F',' '{print $2}' | tr -d '%')
            available_inodes=$(echo $inode_line | awk -F',' '{print $3}')
            
            echo "$now,$fs,$used_space,$available_space,$used_inodes,$available_inodes" >> "$logfile"
        done <<< "$df_output"
        
        sleep $INTERVAL

        new_date=$(current_date)
        if [[ "$new_date" != "$date" ]]; then
            date=$new_date
            timestamp=$(current_timestamp)
            logfile="$LOG_DIR/disk_monitor_${timestamp}_${date}.csv"
            echo "timestamp,filesystem,used_space,available_space,used_inodes,available_inodes" > "$logfile"
        fi
    done
}


start_monitor() {
    if [[ -f "$PID_FILE" ]]; then
        echo "Монитор уже запущен. PID: $(cat $PID_FILE)"
        exit 1
    fi
    
    monitor_disk & echo $! > "$PID_FILE"
    echo "Мониторинг запущен. PID: $(cat $PID_FILE)"
}

stop_monitor() {
    if [[ -f "$PID_FILE" ]]; then
        kill $(cat "$PID_FILE") && rm -f "$PID_FILE"
        echo "Мониторинг остановлен."
    else
        echo "Мониторинг не запущен."
        exit 1
    fi
}

status_monitor() {
    if [[ -f "$PID_FILE" ]]; then
        echo "Мониторинг запущен. PID: $(cat $PID_FILE)"
    else
        echo "Мониторинг не запущен."
    fi
}

if [[ "$1" == "START" ]]; then
    start_monitor
elif [[ "$1" == "STOP" ]]; then
    stop_monitor
elif [[ "$1" == "STATUS" ]]; then
    status_monitor
else
    echo "Ошибка: Неверный параметр."
    echo "Использование: $0 {START|STOP|STATUS}"
    exit 1
fi

