package cn.linshenkx.bigdata.hdfsftpserver.impl.official331;

import cn.linshenkx.bigdata.hdfsftpserver.common.HadoopProp;
import cn.linshenkx.bigdata.hdfsftpserver.common.HdfsFileSystemFactory;
import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.User;

public class Official331HdfsFileSystemFactory implements HdfsFileSystemFactory {

    private HadoopProp prop;

    @Override
    public FileSystemView createFileSystemView(User user) {
        return new HdfsFtpFileSystemView(user, prop);
    }

    @Override
    public void init(HadoopProp prop) {
        this.prop = prop;
    }
}
