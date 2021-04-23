package win95.utilities.filehandling;

import java.io.File;

public class FileValidity {

    public static boolean isValid(File file) {
        return file.exists();
    }

    public static boolean isValid(String path) {
        File file = new File(path);
        return file.exists();
    }
}
