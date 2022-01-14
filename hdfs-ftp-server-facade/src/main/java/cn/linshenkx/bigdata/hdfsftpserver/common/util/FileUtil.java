package cn.linshenkx.bigdata.hdfsftpserver.common.util;


import cn.hutool.core.io.IoUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

public enum FileUtil {
    /**
     * 实例
     */
    INSTANCE;

    /**
     * 根据给定环境变量获取文件输入流，如果找不到则使用默认路径
     * 默认路径需添加classpath://或file://前缀限定，不加则使用classpath
     *
     * @param env
     * @param defaultPath
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream getInputStream(String env, String defaultPath) throws FileNotFoundException {
        String filePath = System.getenv(env);
        boolean useDefault = false;
        InputStream inputStream = null;
        if (StringUtils.isBlank(filePath)) {
            useDefault = true;
        } else {
            File logFile = new File(filePath);
            if (!logFile.exists()) {
                useDefault = true;
            } else {
                inputStream = new FileInputStream(logFile);
            }
        }
        if (useDefault) {
            if (defaultPath.startsWith("file://")) {
                inputStream = new FileInputStream(new File(defaultPath.substring("file://".length())));
            } else {
                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(defaultPath.substring("classpath://".length()));
            }
        }
        return inputStream;
    }

    public static File getFileByEnvOrClassPath(String env, String classPath) throws IOException {
        if (!StringUtils.isBlank(System.getenv(env))) {
            File file = new File(System.getenv(env));
            if (file.exists()) {
                return file;
            }
        }
        //注意：配置文件位于resources目录下，如果项目使用内置容器打成jar包发布，FTPServer无法直接直接读取Jar包中的配置文件。
        //解决办法：将文件复制到指定目录(本文指定到根目录)下然后FTPServer才能读取到。
        String tempPath = System.getProperty("java.io.tmpdir") + System.currentTimeMillis();
        File tempConfig = new File(tempPath);
        IoUtil.copy(Thread.currentThread().getContextClassLoader().getResourceAsStream(classPath), new FileOutputStream(tempConfig));
        return tempConfig;
    }

}
