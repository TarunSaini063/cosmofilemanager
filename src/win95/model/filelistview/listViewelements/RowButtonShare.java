package win95.model.filelistview.listViewelements;

import javafx.scene.control.Button;
import win95.model.FileDetail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RowButtonShare extends Button implements ActionListener {
    private final FileDetail fileDetail;
    private final String name;
    private final String path;

    public RowButtonShare(FileDetail fileDetail, String text) {
        super(text);
        this.fileDetail = fileDetail;
        this.name = fileDetail.getFileName();
        this.path = fileDetail.getFilePath();
    }

    public FileDetail getFileDetail() {
        return fileDetail;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "{{" + name + "}}" + '\n' +
                "{{" + path + "}}" + '\n';
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*
            Start Sharing file...
            not implemented yet...
         */
    }
}
