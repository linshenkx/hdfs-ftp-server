package cn.linshenkx.bigdata.hdfsftpserver.common.util;

public enum HdfsFtpProp {
    /**
     * 实例
     */
    INSTANCE;


    /**
     * 给定属性名查找并返回属性值
     * 查找顺序：环境变量->下划线大写形式的环境变量
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
        return null;
    }

}
