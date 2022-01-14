#! /bin/bash
docker build  --build-arg APP_PACKAGE="hdfs-ftp-server-2.0.tgz" -t linshen/hdfs-ftp-server:2.0 . && docker tag linshen/hdfs-ftp-server:2.0 linshen/hdfs-ftp-server:latest
