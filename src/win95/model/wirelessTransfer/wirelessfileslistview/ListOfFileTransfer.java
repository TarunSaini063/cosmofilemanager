package win95.model.wirelessTransfer.wirelessfileslistview;

import win95.model.FileDetail;
import win95.model.wirelessTransfer.FileMetaData;

import java.util.ArrayList;
import java.util.Collections;

public class ListOfFileTransfer {
    public static ArrayList<FileMetaData> arrayList = new ArrayList<>();

    public static void add(FileDetail fileDetail){
//        if(CommonData.transfer==null){
            arrayList.add(new FileMetaData(fileDetail.getFile()));
//        }else{
//            CommonData.transfer.appendListView(new FileMetaData(fileDetail.getFile()));
//        }
    }

    public static ArrayList<FileMetaData> fetch(){
        ArrayList<FileMetaData> fileMetaDataArrayList = new ArrayList<>(arrayList);
        Collections.reverse(fileMetaDataArrayList);
        return fileMetaDataArrayList;
    }
}
