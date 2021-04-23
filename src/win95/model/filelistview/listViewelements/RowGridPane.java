package win95.model.filelistview.listViewelements;

import javafx.scene.layout.GridPane;
import win95.model.FileDetail;

public class RowGridPane extends GridPane {
    private final FileDetail fileDetail;

    public RowGridPane(FileDetail fileDetail) {
        this.fileDetail = fileDetail;
    }

    public FileDetail getFileDetail() {
        return fileDetail;
    }


    @Override
    public String toString() {
        return "{{" + fileDetail.getFileName() + "}}" + '\n' +
                "{{" + fileDetail.getFilePath() + "}}" + '\n';
    }
}
