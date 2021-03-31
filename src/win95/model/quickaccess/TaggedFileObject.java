package win95.model.quickaccess;

import win95.debug.LogsPrinter;
import win95.model.FileDetail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TaggedFileObject {
    ArrayList<String> paths;
    public TaggedFileObject(String color){
        paths = TaggedFiles.getList(color);
    }
    public ArrayList<FileDetail> getTaggedFileObject(){
        if(paths == null) return null;
        ArrayList<FileDetail> taggedFileObject = new ArrayList<>();
        for(String path : paths){
            try {
                FileDetail fileDetail = new FileDetail(new File(path));
                taggedFileObject.add(fileDetail);
            } catch (IOException e) {
                LogsPrinter.printError("TaggedFileObeject",20,
                        "IO exception in created tagged file : "+path);
            }
        }
        return taggedFileObject;
    }
}
