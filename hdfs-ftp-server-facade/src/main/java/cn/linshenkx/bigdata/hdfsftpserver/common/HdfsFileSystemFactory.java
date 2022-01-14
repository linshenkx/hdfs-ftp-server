package cn.linshenkx.bigdata.hdfsftpserver.common;

import org.apache.ftpserver.ftplet.FileSystemFactory;

public interface HdfsFileSystemFactory extends FileSystemFactory {
    void init(HadoopProp prop);
}
