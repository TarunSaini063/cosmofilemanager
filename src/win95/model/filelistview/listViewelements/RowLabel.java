package win95.model.filelistview.listViewelements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import win95.model.FileDetail;

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
    }

    public FileDetail getFileDetail() {
        return fileDetail;
    }
    public void refresh(){
        this.setMaxWidth(500);
        this.setMinWidth(500);
        this.setAlignment(Pos.BASELINE_CENTER);
    }


    @Override
    public String toString() {
        return  "{{" + fileDetail.getFileName() + "}}" + '\n' +
                "{{" + fileDetail.getFilePath() + "}}" + '\n';
    }
}
