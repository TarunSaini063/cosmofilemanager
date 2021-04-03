package win95.utilities.filehandling;

import win95.constants.CommonData;
import win95.constants.FileType;
import win95.controller.Controller;
import win95.debug.ExceptionPrinter;
import win95.debug.LogsPrinter;
import win95.model.FileDetail;
import win95.model.quickaccess.RecentFiles;

import java.awt.*;
import java.io.IOException;

public class OpenFile {
    /*
        if Desktop not work here use OS module to open file using system call
        not yet implemented...
     */

    public static void open(FileDetail fileDetail) {
        try {
            RecentFiles.addRecentQueue(fileDetail.getFilePath());
            Desktop.getDesktop().open(fileDetail.getFile());
        } catch (IOException e) {
            ExceptionPrinter.print("OpenFile",12,"failed to open file using Desktop class");
        }
    }
    public static void doubleClick(FileDetail fileDetail) throws IOException {
        if(fileDetail.getFileType()== FileType.FILE){
            open(fileDetail);
        }else{
            if(CommonData.instance != null) {
                Controller.BUTTON_PRESSED = "NEXT";
                    if (!CommonData.instance.updateView(fileDetail)) {
                        Controller.BUTTON_PRESSED = "NONE";
                    }
                }else {
                LogsPrinter.printLogic("UpdateCellFactory", 47,
                        "controller class instance is null\nfailed in updating listview");
            }
        }
    }
}
