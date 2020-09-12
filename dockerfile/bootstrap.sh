#! /bin/bash
${APP_HOME}/bin/hdfs-ftp-server.sh start
sleep 3
until find ${APP_HOME}/logs -mmin -1 | egrep -q '.*'; echo "`date`: Waiting for logs..." ; do sleep 2 ; done
tail -F ${APP_HOME}/logs/* &
echo "---------------------------------------------开始----------------------------------------------"
while(true)
do
    sleep 1000;
done
