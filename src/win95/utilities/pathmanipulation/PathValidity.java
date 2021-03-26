package win95.utilities.pathmanipulation;

import win95.constants.FileType;

import java.io.File;

public class PathValidity {
    private final String  path;
    private  FileType type;
    PathValidity(String path){
        this.path = path;
    }
    boolean isValid(){
        File file = new File(path);
        if(file.exists()) {
            if(file.isFile()) {
                type = FileType.FILE;
            }else{
                type = FileType.DIRECTORY;
            }
            return true;
        }else{
            type = FileType.UNKNOWN;
            return false;
        }
    }

    FileType fileType(){
        return type;
    }
}
