这里例子把admin的密码改成admin123456，另外把anonymous给删了
env.sh和application.yaml都是默认的，不能删，否则启动会缺少文件
```shell
docker pull linshen/hdfs-ftp-server
docker rm -f hdfs-ftp-server331
WORKSPACE=$(pwd)
docker run -d --name hdfs-ftp-server331 \
  --restart=always \
  -e FTP_SERVER_PORT=12229 \
  -e HADOOP_CONF_DIR=/etc/hadoop/conf \
  -e KRB5_CONFIG=/etc/krb5.conf \
  -e USER_PRINCIPAL=admin01/adp@HADOOP.COM \
  -e USER_KEYTAB=/home/admin01.service.keytab \
  -e LOADER_JARFILE=hdfs-ftp-server-impl-official-331-shadow.jar \
  --network=host \
  -v /nfs/bigdata/hadoop/conf:/etc/hadoop/conf \
  -v /nfs/bigdata/keytab/admin01.service.keytab:/home/admin01.service.keytab \
  -v /nfs/bigdata/keytab/krb5.conf:/etc/krb5.conf \
  -v $WORKSPACE/conf/:/opt/hdfs-ftp-server/conf \
  linshen/hdfs-ftp-server
docker logs -f hdfs-ftp-server331

```
