#! /bin/bash
docker build -t linshen/hdfs-ftp-server:1.0 . && docker tag linshen/hdfs-ftp-server:1.0 linshen/hdfs-ftp-server:latest
