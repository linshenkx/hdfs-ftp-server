hdfs-ftp-server:一款基于hdfs的ftp服务器
# 项目说明
项目基于 https://github.com/iponweb/hdfs-over-ftp 和 https://github.com/garyfub/hdfsftpserver 进行改造升级。

github:https://github.com/linshenkx/hdfs-ftp-server

dockerhub:https://hub.docker.com/repository/docker/linshen/hdfs-ftp-server
## 特性
1. 使用java的spi机制内置支持多hadoop版本，无需编译可直接切换使用。目前预编译版本有：
   - cdh6.3.2(hadoop3.0.0)
   - hadoop3.3.1
   - 
2. 支持 hdfs 高可用连接及 kerberos 认证
3. 支持环境变量方式使用外部配置文件
4. 提供多种使用案例：jar包shell脚本启动、docker启动等。
## 项目结构
- dockerfile：用于构造docker镜像，命令示例参考：dockerfile/docker-build-tag.sh
- docker-example:docker使用示例，启动命令参考各子文件夹下的run.sh
    - kerberos:使用kerberos的hadoop版本（正式使用）
    - non-kerberos:不使用kerberos的hadoop版本
    - test:不使用hadoop的native版本，以项目根目录为ftp根目录，用于测试开发
## 配置说明
核心配置文件为：server.properties
server.properties中的所有配置可以使用环境变量进行配置，且环境变量的优先级比配置文件高
如环境变量中的 FTP_SERVER_PORT=12222 会覆盖配置文件中的 ftp.server.port=21121
此举旨在简化容器环境下的配置运行

这些额外文件的路径都在server.properties中指定
1. 需提供hadoop配置文件进行连接，如：core-site.xml、hdfs-site.xml
2. 如果使用kerberos认证，需提供用户Principal和keytab文件

## 使用说明
用ftp客户端软件连接时，可能需要把连接超时时间调大。
1. shell(推荐)
    - 执行：bin/hdfs-ftp-server.sh {start|stop}
2. docker(推荐)
    - 参考 docker-example

## 注意事项
目前以hdfs-kerberos方式登录时，权限由登录user（keytab+principal）权限决定，无法限制只读


## 开发
1. 编译打包
    - 打包：`gradle cleanAll createDirs prepareJars buildTarZip`（出错请使用最新版本的gradle）
    - 生成位置：build/distributions/hdfs-ftp-server-${VERSION}.tgz (zip)
    - 解压后获取hdfs-ftp-server文件夹