package win95.model.filelistview.listViewelements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import win95.constants.Dimensions;
import win95.constants.LogicConstants;
import win95.model.FileDetail;

import java.io.File;

public class RowImageView extends ImageView {
    private final FileDetail fileDetail;
    private  String imageURL;
    private Image image;

    public RowImageView(FileDetail fileDetail) {
        this.fileDetail = fileDetail;
        this.setFitHeight(Dimensions.LISTVIEW_ROWIMAGEVIEW);
        this.setFitWidth(Dimensions.LISTVIEW_ROWIMAGEVIEW);
    }

    public RowImageView(FileDetail fileDetail,LogicConstants previewMode) {
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
