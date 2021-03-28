package win95.utilities.pathmanipulation;

import win95.model.FileDetail;

import java.util.ArrayList;

public class PathStack{
    private static final ArrayList<FileDetail> pathsPrevious = new ArrayList<>();
    private static final ArrayList<FileDetail> pathsNext = new ArrayList<>();

    public static FileDetail getNextDirectory(){
        if(pathsPrevious.isEmpty()) return null;
        FileDetail res = pathsPrevious.get(pathsPrevious.size()-1);
        pathsPrevious.remove(pathsPrevious.size()-1);
        return res;
    }

    public static FileDetail getPreviousDirectory(){
        if(pathsNext.isEmpty()) return null;
        FileDetail res = pathsNext.get(pathsNext.size()-1);
        pathsNext.remove(pathsNext.size()-1);
        return res;
    }

    public static void setNextDirectory(FileDetail fileDetail){
        pathsNext.add(fileDetail);
    }

    public static void setPreviousDirectory(FileDetail fileDetail){
        pathsPrevious.add(fileDetail);
    }
}
