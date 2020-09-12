package com.linshen.hdfsftpserver;

import com.linshen.hdfsftpserver.filesystem.HdfsFtpFileSystemFactory;
import com.linshen.hdfsftpserver.filesystem.HdfsFtpPlet;
import com.linshen.hdfsftpserver.util.FileUtil;
import com.linshen.hdfsftpserver.util.HdfsFtpProp;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.filesystem.nativefs.NativeFileSystemFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class HdfsFtpServer {


    private static final Logger log = LoggerFactory.getLogger(HdfsFtpServer.class);

    static {
        try (InputStream logFileInputStream = FileUtil.getInputStream(HdfsFtpProp.LOG_CONF_FILE, HdfsFtpProp.LOG_CONF_FILE_DEFAULT)) {
            PropertyConfigurator.configure(logFileInputStream);
        } catch (IOException e) {
            System.err.println("Log init error:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        log.info("HdfsFtpServer :run");
        log.info("env:");
        System.getenv().entrySet().forEach(entry -> {
            log.info(entry.getKey() + ":" + entry.getValue());
        });
        try {
            HdfsFtpServer.run();
        } catch (FtpException | IOException e) {
            log.info("Error: server run failed.");
        }
    }


    public static void run() throws IOException, FtpException {
        //核心：ftp服务器的工厂类
        FtpServerFactory serverFactory = new FtpServerFactory();

        //配置监听相关，配置项参考：https://mina.apache.org/ftpserver-project/configuration_listeners.html
        ListenerFactory listenerFactory = new ListenerFactory();
        DataConnectionConfigurationFactory dataConnectionConfFactory = new DataConnectionConfigurationFactory();

        //0. 配置使用自定义文件系统
        //NativeFileSystemFactory是用于测试的本地ftp文件系统，ftp根目录为项目目录
        if ("native".equalsIgnoreCase(HdfsFtpProp.getProp(HdfsFtpProp.FILE_SYSTEM))) {
            NativeFileSystemFactory fsf = new NativeFileSystemFactory();
            fsf.setCreateHome(true);
            serverFactory.setFileSystem(fsf);
        } else {
            serverFactory.setFileSystem(new HdfsFtpFileSystemFactory());
        }


        //1、设置服务端口
        int port = Integer.parseInt(HdfsFtpProp.getProp(HdfsFtpProp.PORT));
        log.info("server port:" + port);
        listenerFactory.setPort(port);
        //2、设置被动模式数据上传的接口范围,云服务器需要开放对应区间的端口给客户端
        dataConnectionConfFactory.setPassivePorts(HdfsFtpProp.getProp(HdfsFtpProp.PASSIVE_PORTS));
        listenerFactory.setDataConnectionConfiguration(dataConnectionConfFactory.createDataConnectionConfiguration());
        //3、增加SSL安全配置
        if (Boolean.parseBoolean(HdfsFtpProp.getProp(HdfsFtpProp.SSL_ENABLE))) {
            SslConfigurationFactory ssl = new SslConfigurationFactory();
            ssl.setKeystoreFile(new File(HdfsFtpProp.getProp(HdfsFtpProp.SSL_KEYSTORE_FILE)));
            ssl.setKeystorePassword(HdfsFtpProp.getProp(HdfsFtpProp.SSL_KEYSTORE_PASSWORD));
            ssl.setSslProtocol(HdfsFtpProp.getProp(HdfsFtpProp.SSL_PROTOCOL));
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
        userManagerFactory.setFile(FileUtil.getFileByEnvOrClassPath(HdfsFtpProp.FTP_USERS_PROP_FILE, HdfsFtpProp.FTP_USERS_PROP_FILE_DEFAULT));
        //密码以明文的方式
        userManagerFactory.setPasswordEncryptor(new ClearTextPasswordEncryptor());
        serverFactory.setUserManager(userManagerFactory.createUserManager());

        FtpServer server = serverFactory.createServer();
        server.start();
        log.info("Apache Ftp server is started!");

    }

}
