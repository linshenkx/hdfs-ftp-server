package cn.linshenkx.bigdata.hdfsftpserverboot.props;

import cn.linshenkx.bigdata.hdfsftpserver.common.HadoopProp;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "my")
public class MyProp {

    private HadoopProp hadoop;

}
