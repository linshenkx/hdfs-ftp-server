#!/usr/bin/env bash

# SERVER_PROP_FILE
# default: $DIR/conf/server.properties
#export SERVER_PROP_FILE=

# LOG_CONF_FILE
# default: $DIR/conf/log4j.properties
#export LOG_CONF_FILE=

# FTP_USERS_PROP_FILE
# default: $DIR/conf/users.properties
#export FTP_USERS_PROP_FILE=

export APP_XMX="${APP_XMX:-1g}"
export APP_XMS="${APP_XMS:-1g}"
if [ -z $APP_OPTS ];then
  export APP_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UnlockDiagnosticVMOptions -XX:+G1SummarizeConcMark -XX:+ParallelRefProcEnabled -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -Xloggc:./logs/gc-%t.log -XX:+PrintAdaptiveSizePolicy -XX:G1HeapWastePercent=20 -XX:ConcGCThreads=8 -XX:ParallelGCThreads=13  -XX:InitiatingHeapOccupancyPercent=70  -XX:+PrintReferenceGC  -XX:-ResizePLAB "
fi
