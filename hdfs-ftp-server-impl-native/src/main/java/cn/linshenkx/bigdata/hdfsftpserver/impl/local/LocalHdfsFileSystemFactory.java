package cn.linshenkx.bigdata.hdfsftpserver.impl.local;

import cn.linshenkx.bigdata.hdfsftpserver.common.HadoopProp;
import cn.linshenkx.bigdata.hdfsftpserver.common.HdfsFileSystemFactory;
import org.apache.ftpserver.filesystem.nativefs.NativeFileSystemFactory;

public class LocalHdfsFileSystemFactory extends NativeFileSystemFactory implements HdfsFileSystemFactory {

    public LocalHdfsFileSystemFactory() {
        super();
        this.setCreateHome(true);
    }

    @Override
    public void init(HadoopProp prop) {

    }
}
