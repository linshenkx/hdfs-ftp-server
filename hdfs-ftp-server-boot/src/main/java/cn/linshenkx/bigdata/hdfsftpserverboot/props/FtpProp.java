package cn.linshenkx.bigdata.hdfsftpserverboot.props;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ftp.server")
public class FtpProp {

    @Value("${ftp.server.port}")
    private Integer port;
    @Value("${ftp.server.userPropFile}")
    private String userPropFile;
    @Value("${ftp.server.passive.ports}")
    private String passivePorts;
    @Value("${ftp.server.ssl.enable}")
    private Boolean sslEnable;
    @Value("${ftp.server.ssl.protocol}")
    private String sslProtocol;
    @Value("${ftp.server.ssl.keystore.file}")
    private String sslKeystoreFile;
    @Value("${ftp.server.ssl.keystore.password}")
    private String sslKeystorePassword;


}
