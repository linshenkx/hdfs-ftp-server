```shell
docker pull linshen/hdfs-ftp-server
docker rm -f local-ftp-server
docker run -d --name local-ftp-server \
  --restart=always \
  -e FTP_SERVER_PORT=12225 \
  --network=host \
  linshen/hdfs-ftp-server
docker logs -f local-ftp-server

```
admin/admin123