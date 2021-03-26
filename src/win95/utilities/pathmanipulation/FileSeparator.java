package win95.utilities.pathmanipulation;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

public class FileSeparator {
    public static FileSystem fileSystem;
     static {
         fileSystem = FileSystems.getDefault();
    }
    public static String fileSeparator(){
         return fileSystem.getSeparator();
    }
}
