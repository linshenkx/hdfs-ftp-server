一款基于hdfs的ftp服务器，通过java的spi机制内置支持多hadoop版本，自带依赖，无需编译即可使用。
## 项目说明
项目基于 https://github.com/iponweb/hdfs-over-ftp 和 https://github.com/garyfub/hdfsftpserver 进行改造升级。
主工程使用springboot实现，hadoop客户端以spi插件形式隔离，启动时通过环境变量指定jar包自动加载对应库。

github:https://github.com/linshenkx/hdfs-ftp-server
dockerhub:https://hub.docker.com/repository/docker/linshen/hdfs-ftp-server
## 支持版本
| 名称             | jar包 | 说明                                    |
|----------------|------|---------------------------------------|
| official3.3.1  |  hdfs-ftp-server-impl-official-331-shadow.jar   | hadoop官方3.3.1版本（目前最新版本）               |
| cdh6.3.2-3.0.0 |  hdfs-ftp-server-impl-cdh632-300-shadow.jar    | cdh发行版6.3.2（最后的免费版本），对应hadoop版本为3.0.0 |
| native         |  hdfs-ftp-server-impl-native-shadow.jar    | 本地ftp服务器，以项目根目录为ftp根目录，用于测试开发      |

## 特性
1. 自带依赖，仅需提供连接配置文件，无需环境jar包
2. 内置多种环境，无需自行编译
3. 支持hdfs高可用连接及kerberos认证
4. 使用环境变量支持容器化使用

## 项目结构
- dockerfile：用于构造docker镜像，命令示例参考：dockerfile/docker-build-tag.sh
- docker-example:docker使用示例，启动命令参考各子文件夹下的run.sh
    - kerberos:使用kerberos的hadoop版本（正式使用）
    - non-kerberos:不使用kerberos的hadoop版本
    - test:不使用hadoop的native版本

## 配置说明
注意：所有配置基本都可以通过环境变量定制，方便容器化使用
配置的优先级由高到低为：application.xml、env.sh、环境变量
容器化使用建议配置环境变量，普通使用建议修改env.sh
application.xml则是最终配置
### 1. env.sh    
环境变量配置文件，被注释掉的是默认值
主要为服务器运行配置，如加载application配置文件名、加载hadoop插件包、运行内存、是否开启远程debug等
另外也可配置keberos认证（这个属于业务了，也可在application.xml里面配置）

主要修改配置项为LOADER_JARFILE

### 2. application.xml
主要为ftp服务器配置

### 3. users.properties
ftp服务器用户配置，默认为admin/admin123(完整权限)，anonymous(读权限)
注意：anonymous建议删掉，避免匿名访问
另外，这里的用户主要用于登录访问，权限控制应以hdfs自身的为主

****************************************
## 使用说明
1. shell(推荐)
    - 执行：bin/hdfs-ftp-server.sh {start|stop}
2. docker(推荐)
    - 参考 docker-example

## 注意事项
目前以hdfs-kerberos方式登录时，权限由登录user（keytab+principal）权限决定，无法限制只读

## 开发
使用模块化开发，适配其他hadoop版本时，复制原有工程，修改依赖性即可
1. 复制原有工程模块，如hdfs-ftp-server-impl-cdh632-300
2. 修改名称，注意前缀为`hdfs-ftp-server-impl-`
3. 修改settings.gradle，增加新添的工程模块
4. 修改工程的包名、类名、代码等
5. 修改service文件cn.linshenkx.bigdata.hdfsftpserver.common.HdfsFileSystemFactory的内容为新添工程的类名
6. 编译打包
    - 打包命令：`gradle cleanAll createDirs prepareJars buildTarZip`（出错请使用最新版本的gradle）
    - 生成位置：build/distributions/hdfs-ftp-server-${VERSION}.tgz (zip)
    - 解压后获取hdfs-ftp-server文件夹
