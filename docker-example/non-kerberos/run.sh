#! /bin/bash
WORKSPACE=$(pwd)
docker run -d --name hdfs-ftp-server \
  --restart=always \
  -e FTP_SERVER_PORT=12222 \
  --network=host \
  -v ${WORKSPACE}/conf:/opt/hdfs-ftp-server/conf \
  -v ${WORKSPACE}/hadoopConf:/opt/hdfs-ftp-server/conf/hadoop \
  linshen/hdfs-ftp-server
