package win95.model.filelistview.listViewelements;

import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import win95.model.FileDetail;
import win95.utilities.filehandling.OpenFile;

public class RowLabel extends Label {
    /*
     *
     *
     * set set theme from user previous data
     *
     *
     */

    private final FileDetail fileDetail;

    public RowLabel(FileDetail fileDetail) {
        super(fileDetail.getFileName());
        this.fileDetail = fileDetail;
        this.setOnMouseClicked(event -> {
            if ((event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2)) {
                OpenFile.open(fileDetail);
            }
        });
    }

    public FileDetail getFileDetail() {
        return fileDetail;
    }


    @Override
    public String toString() {
        return  "{{" + fileDetail.getFileName() + "}}" + '\n' +
                "{{" + fileDetail.getFilePath() + "}}" + '\n';
    }
}
