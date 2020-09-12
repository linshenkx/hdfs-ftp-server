#! /bin/bash

DIR=`dirname $0`

cd $DIR/..
DIR=`pwd`

source $DIR/conf/env.sh

LOGDIR=$DIR/logs
mkdir -p $LOGDIR

PIDFILE=$DIR/hdfs-ftp-server.pid
OUTFILE=$LOGDIR/hdfs-over-ftp.out
JARFILE=$DIR/lib/hdfs-ftp-server.jar

if [ -z $SERVER_PROP_FILE ];then
  export SERVER_PROP_FILE="$DIR/conf/server.properties"
fi

if [ -z $LOG_CONF_FILE ];then
  export LOG_CONF_FILE="$DIR/conf/log4j.properties"
fi

if [ -z $FTP_USERS_PROP_FILE ];then
  export FTP_USERS_PROP_FILE="$DIR/conf/users.properties"
fi

start(){
        echo "start hdfs-ftp-server ..."

        if [ -e $PIDFILE ]; then
                pid=`cat $PIDFILE`
                find=`ps -ef | grep $pid | grep -v grep | awk '{ print $2 }'`
                if [ -n "$find" ]; then
                        echo "hdfs-ftp-server has already been start."
                        exit
                fi
        fi

        nohup $JAVA_HOME/bin/java  -Xmx${APP_XMX} -Xms${APP_XMS} $APP_OPTS  -jar $JARFILE </dev/null 2>&1 >> $OUTFILE &
        echo $! > $PIDFILE
        if [ -e $PIDFILE ]; then
        pid=`cat $PIDFILE`
        find=`ps -ef | grep $pid | grep -v grep | awk '{ print $2 }'`
        if [ -n "$find" ]; then
            echo "hdfs-ftp-server is stared"
            exit
        fi
    fi

    echo "start hdfs-ftp-server fail"
}

stop() {
        echo "stop hdfs-ftp-server..."

        if [ -e $PIDFILE ]; then
                pid=`cat $PIDFILE`
                find=`ps -ef | grep $pid | grep -v grep | awk '{ print $2 }'`
                if [ -n "$find" ]; then
                        kill -9 $find

                        while [ -n "`ps -ef | grep $pid | grep -v grep | awk '{ print $2 }'`" ]; do
                                sleep 1;
                        done
                        echo "ok"
                        exit
                fi
        fi
        echo "no hdfs-ftp-server found"
}

case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
        *)
        echo "Usage: hdfs-ftp-server.sh {start|stop}"
esac