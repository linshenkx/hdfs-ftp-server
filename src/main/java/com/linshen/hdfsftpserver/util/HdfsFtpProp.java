package com.linshen.hdfsftpserver.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum HdfsFtpProp {
    /**
     * 实例
     */
    INSTANCE;

    public static final String SERVER_PROP_FILE = "SERVER_PROP_FILE";
    public static final String SERVER_PROP_FILE_DEFAULT = "classpath://conf/server.properties";
    public static final String FTP_USERS_PROP_FILE = "FTP_USERS_PROP_FILE";
    public static final String FTP_USERS_PROP_FILE_DEFAULT = "conf/users.properties";
    public static final String LOG_CONF_FILE = "LOG_CONF_FILE";
    public static final String LOG_CONF_FILE_DEFAULT = "classpath://conf/log4j.properties";

    public static final String FILE_SYSTEM = "ftp.server.filesystem";
    public static final String PORT = "ftp.server.port";
    public static final String PASSIVE_PORTS = "ftp.server.passive.ports";
    public static final String SSL_ENABLE = "ftp.server.ssl.enable";
    public static final String SSL_KEYSTORE_FILE = "ftp.server.ssl.keystore.file";
    public static final String SSL_KEYSTORE_PASSWORD = "ftp.server.ssl.keystore.password";
    public static final String SSL_PROTOCOL = "ftp.server.ssl.protocol";

    public static final String HADOOP_CONF_DIR = "hadoop.conf.dir";
    public static final String KRB5_CONFIG = "krb5.config";
    public static final String USER_PRINCIPAL = "user.principal";
    public static final String USER_KEYTAB_PATH = "user.keytab.path";


    private static final Properties PROPS;

    static {
        PROPS = new Properties();
        try (InputStream in = FileUtil.getInputStream(SERVER_PROP_FILE, HdfsFtpProp.SERVER_PROP_FILE_DEFAULT)) {
            PROPS.load(in);
        } catch (IOException e) {
            System.err.println("HdfsFtpProp init error:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 给定属性名查找并返回属性值
     * 查找顺序：环境变量->下划线大写形式的环境变量->配置文件
     *
     * @param prop
     * @return
     */
    public static String getProp(String prop) {
        String value = null;
        value = System.getProperty(prop);
        if (value != null) {
            return value;
        }
        value = System.getenv(prop);
        if (value != null) {
            return value;
        }
        String upperProp = prop.replace('.', '_').toUpperCase();
        value = System.getenv(upperProp);
        if (value != null) {
            return value;
        }
        value = PROPS.getProperty(prop);
        return value;
    }

}
