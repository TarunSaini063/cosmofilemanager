package win95.model.filelistview.listViewelements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import win95.constants.Dimensions;
import win95.constants.DirectoryMovement;
import win95.model.FileDetail;
import win95.utilities.filehandling.OpenFile;

import java.io.File;

public class RowImageView extends ImageView {
    private final FileDetail fileDetail;
    private  String imageURL;
    private Image image;

    public RowImageView( FileDetail fileDetail) {
        this.fileDetail = fileDetail;
        this.setFitHeight(Dimensions.LISTVIEW_ROWIMAGEVIEW);
        this.setFitWidth(Dimensions.LISTVIEW_ROWIMAGEVIEW);
        this.setOnMouseClicked(event -> {
            if ((event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2)) {
                OpenFile.open(fileDetail);
            }
        });
    }

    public RowImageView(FileDetail fileDetail, DirectoryMovement viewMode) {
        this.fileDetail = fileDetail;
        this.setFitHeight(Dimensions.PREVIEW_FILE_ICON);
        this.setFitWidth(Dimensions.PREVIEW_FILE_ICON);
    }

    public FileDetail getFileDetail() {
        return fileDetail;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setRowImage(String url){
        this.imageURL = url;
        image = new Image(new File(url).toURI().toString());
        super.setImage(image);
    }

    public void setRowImage(Image image){
        super.setImage(image);
    }

    @Override
    public String toString() {
        return  "{{" + fileDetail.getFileName() + "}}" + '\n' +
                "{{" + fileDetail.getFilePath() + "}}" + '\n';
    }
}