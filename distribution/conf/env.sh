#!/usr/bin/env bash

## 注意：以下变量都优先从环境变量读取，
# 环境变量没有值才会根据下面命令配置
##

# app
#export APP_PROFILE_FILE="application.yaml"
#export LOADER_JARFILE="hdfs-ftp-server-impl-native-shadow.jar"

# jvm
#export APP_XMX="1g"
#export APP_XMS="1g"
#export APP_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UnlockDiagnosticVMOptions -XX:+G1SummarizeConcMark -XX:+ParallelRefProcEnabled -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -Xloggc:./logs/gc-%t.log -XX:+PrintAdaptiveSizePolicy -XX:G1HeapWastePercent=20 -XX:ConcGCThreads=8 -XX:ParallelGCThreads=13  -XX:InitiatingHeapOccupancyPercent=70  -XX:+PrintReferenceGC  -XX:-ResizePLAB "

# jdwp
# false/true ,default is false
#export ENABLE_REMOTE_DEBUG=false
#export REMOTE_DEBUG_PORT=5005

#export HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop
#export KRB5_CONFIG=/etc/krb5.conf
#export USER_PRINCIPAL=
#export USER_KEYTAB=

