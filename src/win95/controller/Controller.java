package win95.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import win95.constants.*;
import win95.model.FileDetail;
import win95.model.filelistview.CellFactory;
import win95.model.filelistview.ListEntry;
import win95.model.filelistview.listViewelements.RowImageView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ListView<ListEntry> listView;

    @FXML
    private AnchorPane preview;

    final private ObservableList<ListEntry> observableList = FXCollections.observableArrayList();

    public void showPreview(FileDetail fileDetail){

        GridPane previewGridPane = new GridPane();
        previewGridPane.setHgap(2);
        previewGridPane.setVgap(10);
        previewGridPane.setAlignment(Pos.CENTER);

        RowImageView rowImageView = new RowImageView(fileDetail, LogicConstants.PREVIEW_MODE);
        Image image = new Image(new File(FileIcons.getFileIconPath(fileDetail.getFileExtension())).toURI().toString());
        rowImageView.setImage(image);
        GridPane.setHalignment(rowImageView, HPos.CENTER);
        previewGridPane.add(rowImageView,0,0,2,1);

        Label nameTag = new Label("Name : ");
        nameTag.setFont(Fonts.PREVIEW_FONT);
        previewGridPane.add(nameTag,0,1);
        previewGridPane.add(new Label(fileDetail.getFileName()),1,1);

        Label sizeTag = new Label("Size : ");
        sizeTag.setFont(Fonts.PREVIEW_FONT);
        previewGridPane.add(sizeTag,0,2);
        previewGridPane.add(new Label(fileDetail.getOptimizedSize()),1,2);


        Label creationTime = new Label(fileDetail.getCreationTime());
        Label creationTimeTag = new Label("Creation Time : ");
        creationTimeTag.setFont(Fonts.PREVIEW_FONT);
        previewGridPane.add(creationTimeTag,0,3);
        previewGridPane.add(creationTime,1,3);

        Label lastAccessTime = new Label(fileDetail.getLastAccessTime());
        Label lastAccessTimeTag = new Label("Last Access Time : ");
        lastAccessTimeTag.setFont(Fonts.PREVIEW_FONT);
        previewGridPane.add(lastAccessTimeTag,0,4);
        previewGridPane.add(lastAccessTime,1,4);

        Label lastModifiedTime = new Label(fileDetail.getLastModifiedTime());
        Label lastModifiedTimeTag = new Label("Last Modified Time : ");
        lastModifiedTimeTag.setFont(Fonts.PREVIEW_FONT);
        previewGridPane.add(lastModifiedTimeTag,0,5);
        previewGridPane.add(lastModifiedTime,1,5);

//        previewGridPane.setGridLinesVisible(true);

        AnchorPane.setTopAnchor(previewGridPane, 5d);
        AnchorPane.setLeftAnchor(previewGridPane, 5d);
        AnchorPane.setRightAnchor(previewGridPane, 5d);
        AnchorPane.setBottomAnchor(previewGridPane, 5d);
        preview.getChildren().clear();
        System.out.println(previewGridPane.toString());
        preview.getChildren().add(previewGridPane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setItems(observableList);
        File USER_HOME = new File(System.getProperty("user.home"));
        File []files = USER_HOME.listFiles();
        assert files != null;
        for(File file : files){
            try {
                FileDetail fileDetail = new FileDetail(file);
                System.out.println(fileDetail.toString());
                ListEntry listEntry = new ListEntry(fileDetail);
                observableList.add(listEntry);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        listView.setCellFactory(new CellFactory());

       /*
        listView.setOnMouseClicked(event -> {
            if ((event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) &&
               (event.getTarget() instanceof LabeledText ||
                event.getTarget() instanceof RowLabel ||
                event.getTarget() instanceof RowImageView ||
                event.getTarget() instanceof RowButtonShare ||
                event.getTarget() instanceof RowGridPane)) {
                if(event.getTarget() instanceof LabeledText){
                    LogsPrinter.printLogic("Controller",58,event.getTarget().toString());
                }else {
                        Add login to show detail in right pane using file detail pointer on single click
                        not yet implemented..
                    LogsPrinter.printLogic("Controller", 58, event.getTarget().toString());
                   if(event.getTarget() instanceof RowLabel) {
                       OpenFile.open(((RowLabel) event.getTarget()).getFileDetail());
                   }else if(event.getTarget() instanceof RowImageView) {
                       OpenFile.open(((RowImageView) event.getTarget()).getFileDetail());
                   }else if(event.getTarget() instanceof RowButtonShare) {
                       OpenFile.open(((RowButtonShare) event.getTarget()).getFileDetail());
                   }else if(event.getTarget() instanceof RowGridPane) {
                       OpenFile.open(((RowGridPane) event.getTarget()).getFileDetail());
                   }
                }
            }
        });
        */
        ControllerInstances.instance = this;
    }


}
