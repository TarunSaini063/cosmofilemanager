package win95.model.wirelessTransfer;

import java.io.File;
import java.io.Serializable;

public class FileMetaData implements Serializable {
    String path;
    String name;
    long size;

    public FileMetaData(File file) {
        path = file.getAbsolutePath();
        name = file.getName();
        size = file.length();
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "FileMetaData{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
