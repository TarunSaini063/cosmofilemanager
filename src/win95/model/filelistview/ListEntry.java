package win95.model.filelistview;

import javafx.scene.image.Image;
import win95.constants.Icons;
import win95.debug.LogsPrinter;
import win95.model.FileDetail;
import win95.model.filelistview.listViewelements.RowButtonShare;
import win95.model.filelistview.listViewelements.RowImageView;
import win95.model.filelistview.listViewelements.RowLabel;

import java.io.File;

public class ListEntry {
    private String name,url;

    private RowImageView rowImageView;
    private RowLabel rowNameLabel,rowCount;
    private RowButtonShare delete,open,share;
    private FileDetail fileDetail;



    public ListEntry(FileDetail fileDetail){
        this.fileDetail = fileDetail;
        this.name = fileDetail.getFileName();
        this.url = Icons.getFileIconPath(fileDetail.getFileExtension());

        rowNameLabel = new RowLabel(fileDetail);
        rowNameLabel.setMaxWidth(1000);
        rowNameLabel.setMinWidth(500);

        rowCount = new RowLabel(fileDetail);
        rowCount.setText("");
        rowCount.setMinWidth(30);

        Image image = new Image(new File(url).toURI().toString());
        this.rowImageView = new RowImageView(fileDetail);
        this.rowImageView.setImage(image);


        share = new RowButtonShare(fileDetail,"Share");

        share.setOnAction(event -> {
            /*
                replace with share module..
                not implemented yet...
             */
            LogsPrinter.printLogic("ListEntry",42,"Share logic not implemented yet");
        });
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RowImageView getRowImageView() {
        return rowImageView;
    }

    public void setRowImageView(RowImageView rowImageView) {
        this.rowImageView = rowImageView;
    }

    public RowLabel getRowNamelabel() {
        return rowNameLabel;
    }

    public void setRowNamelabel(RowLabel rowNamelabel) {
        this.rowNameLabel = rowNamelabel;
    }

    public RowLabel getRowCount() {
        return rowCount;
    }

    public void setRowCount(RowLabel rowCount) {
        this.rowCount = rowCount;
    }

    public RowButtonShare getDelete() {
        return delete;
    }

    public void setDelete(RowButtonShare delete) {
        this.delete = delete;
    }

    public RowButtonShare getOpen() {
        return open;
    }

    public void setOpen(RowButtonShare open) {
        this.open = open;
    }

    public RowButtonShare getShare() {
        return share;
    }

    public void setShare(RowButtonShare share) {
        this.share = share;
    }

    public FileDetail getFileDetail() {
        return fileDetail;
    }

    public void setFileDetail(FileDetail fileDetail) {
        this.fileDetail = fileDetail;
    }

    @Override
    public String toString() {
        return "ListEntry{" +
                "name='" + name + '\'' +
                ", imageView=" + "imageView" +
                ", delete=" + "delete" +
                ", open=" + "open" +
                '}';
    }


}
