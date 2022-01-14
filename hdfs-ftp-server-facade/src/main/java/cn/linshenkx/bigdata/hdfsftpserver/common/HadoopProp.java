package cn.linshenkx.bigdata.hdfsftpserver.common;

import lombok.Data;

@Data
public class HadoopProp {

    private String confDir;
    private String krb5Conf;
    private String userPrincipal;
    private String userKeytab;
    
}
