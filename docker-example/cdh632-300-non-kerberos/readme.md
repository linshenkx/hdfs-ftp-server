这里例子把admin的密码改成admin123456，另外把anonymous给删了
env.sh和application.yaml都是默认的，不能删，否则启动会缺少文件
```shell
docker pull linshen/hdfs-ftp-server
docker rm -f hdfs-ftp-server
WORKSPACE=$(pwd)
docker run -d --name hdfs-ftp-server \
  --restart=always \
  -e FTP_SERVER_PORT=12222 \
  -e HADOOP_CONF_DIR=/etc/hadoop/conf \
  -e LOADER_JARFILE=hdfs-ftp-server-impl-cdh632-300-shadow.jar \
  --network=host \
  -v /etc/hadoop/conf:/etc/hadoop/conf \
  -v $WORKSPACE/conf/:/opt/hdfs-ftp-server/conf \
  linshen/hdfs-ftp-server
docker logs -f hdfs-ftp-server

```
