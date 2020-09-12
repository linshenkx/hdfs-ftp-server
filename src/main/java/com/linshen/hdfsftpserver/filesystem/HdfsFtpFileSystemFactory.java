package com.linshen.hdfsftpserver.filesystem;

import org.apache.ftpserver.ftplet.FileSystemFactory;
import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.User;

public class HdfsFtpFileSystemFactory implements FileSystemFactory {

    @Override
    public FileSystemView createFileSystemView(User user) {
        return new HdfsFtpFileSystemView(user);
    }
}
