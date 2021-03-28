package win95.model.filelistview.listViewelements;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import win95.model.FileDetail;

import static win95.utilities.filehandling.OpenFile.doubleClick;

public class RowGridPane extends GridPane {
    private final FileDetail fileDetail;

    public RowGridPane(FileDetail fileDetail) {
        this.fileDetail = fileDetail;
        this.setOnMouseClicked(event -> {
            if ((event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2)) {
                doubleClick(fileDetail);
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
