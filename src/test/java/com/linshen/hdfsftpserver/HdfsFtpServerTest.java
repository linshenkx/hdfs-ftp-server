package com.linshen.hdfsftpserver;

import org.apache.ftpserver.ftplet.FtpException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class HdfsFtpServerTest {

    @Test
    public void run() {
        try {
            HdfsFtpServer.run();
        } catch (IOException | FtpException e) {
            e.printStackTrace();
        }
    }

}
