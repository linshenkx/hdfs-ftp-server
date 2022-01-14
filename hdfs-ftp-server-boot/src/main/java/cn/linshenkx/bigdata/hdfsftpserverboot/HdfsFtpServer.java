package cn.linshenkx.bigdata.hdfsftpserverboot;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import cn.linshenkx.bigdata.hdfsftpserver.common.HdfsFileSystemFactory;
import cn.linshenkx.bigdata.hdfsftpserverboot.props.FtpProp;
import cn.linshenkx.bigdata.hdfsftpserverboot.props.MyProp;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;


@Component
public class HdfsFtpServer {

    private static final Logger log = LoggerFactory.getLogger(HdfsFtpServer.class);

    @Resource
    private FtpProp ftpProp;
    @Resource
    private MyProp myProp;

    @PostConstruct
    public void run() throws IOException, FtpException {
        log.info("myProp conf:\n{}", JSONUtil.toJsonStr(myProp));
        log.info("ftpProp conf:\n{}", JSONUtil.toJsonStr(ftpProp));
        //核心：ftp服务器的工厂类
        FtpServerFactory serverFactory = new FtpServerFactory();

        //配置监听相关，配置项参考：https://mina.apache.org/ftpserver-project/configuration_listeners.html
        ListenerFactory listenerFactory = new ListenerFactory();
        DataConnectionConfigurationFactory dataConnectionConfFactory = new DataConnectionConfigurationFactory();

        //0. 配置使用自定义文件系统
        ServiceLoader<HdfsFileSystemFactory> fileSystemFactoryServiceLoader = ServiceLoader.load(HdfsFileSystemFactory.class);
        HdfsFileSystemFactory targetFileSystemFactory = null;
        for (HdfsFileSystemFactory systemFactory : fileSystemFactoryServiceLoader) {
            targetFileSystemFactory = systemFactory;
        }
        if (targetFileSystemFactory == null) {
            throw new RuntimeException("未找到FileSystemFactory");
        }
        targetFileSystemFactory.init(myProp.getHadoop());
        serverFactory.setFileSystem(targetFileSystemFactory);

        //1、设置服务端口
        int port = ftpProp.getPort();
        log.info("server port:{}", port);
        listenerFactory.setPort(port);
        //2、设置被动模式数据上传的接口范围,云服务器需要开放对应区间的端口给客户端
        dataConnectionConfFactory.setPassivePorts(ftpProp.getPassivePorts());
        listenerFactory.setDataConnectionConfiguration(dataConnectionConfFactory.createDataConnectionConfiguration());
        //3、增加SSL安全配置
        if (ftpProp.getSslEnable()) {
            SslConfigurationFactory ssl = new SslConfigurationFactory();
            ssl.setKeystoreFile(new File(ftpProp.getSslKeystoreFile()));
            ssl.setKeystorePassword(ftpProp.getSslKeystorePassword());
            ssl.setSslProtocol(ftpProp.getSslProtocol());
            // set the SSL configuration for the listener
            listenerFactory.setSslConfiguration(ssl.createSslConfiguration());
            listenerFactory.setImplicitSsl(true);
        }


        //4、替换默认的监听器: https://mina.apache.org/ftpserver-project/ftplet.html
        serverFactory.addListener("default", listenerFactory.createListener());

        //5、配置自定义用户事件
        Map<String, Ftplet> ftpLets = new HashMap<>();
        ftpLets.put("ftpService", new HdfsFtpPlet());
        serverFactory.setFtplets(ftpLets);

        //6、读取用户的配置信息
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(FileUtil.file(ftpProp.getUserPropFile()));
        //密码以明文的方式
        userManagerFactory.setPasswordEncryptor(new ClearTextPasswordEncryptor());
        serverFactory.setUserManager(userManagerFactory.createUserManager());

        FtpServer server = serverFactory.createServer();
        server.start();
        log.info("Apache Ftp server is started!");

    }

}
