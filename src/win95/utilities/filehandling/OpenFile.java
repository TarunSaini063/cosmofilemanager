package win95.utilities.filehandling;

import win95.debug.ExceptionPrinter;
import win95.model.FileDetail;

import java.awt.*;
import java.io.IOException;

public class OpenFile {
    /*
        if Desktop not work here use OS module to open file using system call
        not yet implemented...
     */

    public static void open(FileDetail fileDetail) {
        try {
            Desktop.getDesktop().open(fileDetail.getFile());
        } catch (IOException e) {
            ExceptionPrinter.print("openfile",12,"failed to open file using Desktop class");
        }
    }
}
