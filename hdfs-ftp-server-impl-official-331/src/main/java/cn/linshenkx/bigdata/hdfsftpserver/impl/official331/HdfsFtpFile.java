package cn.linshenkx.bigdata.hdfsftpserver.impl.official331;

import org.apache.ftpserver.ftplet.FtpFile;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HdfsFtpFile implements FtpFile {

    private static final Logger log = LoggerFactory.getLogger(HdfsFtpFile.class);
    private final String path;
    private final HdfsFtpFileSystemView view;

    public HdfsFtpFile(String path, HdfsFtpFileSystemView view) {
        this.path = path;
        this.view = view;
    }


    @Override
    public boolean doesExist() {
        try {
            return view.dfs.exists(new Path(path));
        } catch (IllegalArgumentException | IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public String getAbsolutePath() {
        return path;
    }

    @Override
    public String getGroupName() {
        try {
            return view.dfs.getFileStatus(new Path(path)).getGroup();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public int getLinkCount() {
        return 0;
    }

    @Override
    public String getName() {

        try {
            return view.dfs.getFileStatus(new Path(path)).getPath().getName();
        } catch (IllegalArgumentException | IOException e) {
            log.error(e.getMessage());
        }
        return "undefined";
    }

    @Override
    public String getOwnerName() {
        try {
            return view.dfs.getFileStatus(new Path(path)).getOwner();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Object getPhysicalFile() {
        return null;
    }

    @Override
    public long getSize() {
        try {
            return view.dfs.getFileStatus(new Path(path)).getLen();
        } catch (IllegalArgumentException | IOException e) {
            log.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean isDirectory() {
        try {
            return view.dfs.getFileStatus(new Path(path)).isDirectory();
        } catch (IllegalArgumentException | IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean isFile() {
        try {
            FileStatus fileStatus = view.dfs.getFileStatus(new Path(path));
            return fileStatus.isFile();
        } catch (IllegalArgumentException | IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public long getLastModified() {
        try {
            return view.dfs.getFileStatus(new Path(path)).getModificationTime();
        } catch (IllegalArgumentException | IOException e) {
            log.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean setLastModified(long time) {
        return false;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public boolean isReadable() {
        try {
            return view.dfs.getFileStatus(new Path(path)).getPermission().getUserAction().implies(FsAction.READ);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean isRemovable() {
        try {
            return view.dfs.getFileStatus(new Path(path)).getPermission().getUserAction().implies(FsAction.WRITE);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean isWritable() {
        try {
            return view.dfs.getFileStatus(new Path(path)).getPermission().getUserAction().implies(FsAction.WRITE);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return true;
    }

    @Override
    public List<FtpFile> listFiles() {
        try {
            return Arrays.stream(view.dfs.listStatus(new Path(path))).map(v -> new HdfsFtpFile(v.getPath().toString(), view)).collect(Collectors.toList());
        } catch (IllegalArgumentException | IOException e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();

    }

    @Override
    public boolean mkdir() {
        try {
            view.dfs.mkdirs(new Path(path));
            return true;
        } catch (IllegalArgumentException | IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean move(FtpFile file) {
        try {
            return view.dfs.rename(new Path(path), new Path(file.getAbsolutePath()));
        } catch (IllegalArgumentException | IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete() {
        try {
            return view.dfs.delete(new Path(path), true);
        } catch (IllegalArgumentException | IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }


    @Override
    public InputStream createInputStream(long offset) throws IOException {
        try {
            return view.dfs.open(new Path(path));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public OutputStream createOutputStream(long offset) throws IOException {
        try {
            view.dfs.createNewFile(new Path(path));
            return view.dfs.create(new Path(path));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }


}
