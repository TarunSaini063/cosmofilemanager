package win95.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import win95.constants.*;
import win95.debug.LogsPrinter;
import win95.model.FileDetail;
import win95.model.filelistview.CellFactory;
import win95.model.filelistview.ListEntry;
import win95.model.filelistview.listViewelements.RowImageView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ListView<ListEntry> listView;

    @FXML
    private AnchorPane preview;

    final private ObservableList<ListEntry> observableList = FXCollections.observableArrayList();

    private LogicConstants.SortingType sortingType = LogicConstants.SortingType.BY_DEFAULT;

    Comparator<ListEntry> size_desc = (o2, o1) -> o1.getFileDetail().getSizeInByte().toLowerCase().compareTo(o2.getFileDetail().getSizeInByte().toLowerCase());

    Comparator<ListEntry> size_inc = (o2, o1) -> o2.getFileDetail().getSizeInByte().toLowerCase().compareTo(o1.getFileDetail().getSizeInByte().toLowerCase());

    Comparator<ListEntry> name_desc = (o2, o1) -> o1.getFileDetail().getFileName().toLowerCase().compareTo(o2.getFileDetail().getFileName().toLowerCase());

    Comparator<ListEntry> name_inc = (o2, o1) -> o2.getFileDetail().getFileName().toLowerCase().compareTo(o1.getFileDetail().getFileName().toLowerCase());

    Comparator<ListEntry> access_desc = (o2, o1) -> o1.getFileDetail().getLastAccessTime().toLowerCase().compareTo(o2.getFileDetail().getLastAccessTime().toLowerCase());

    Comparator<ListEntry> access_inc = (o2, o1) -> o2.getFileDetail().getLastAccessTime().toLowerCase().compareTo(o1.getFileDetail().getLastAccessTime().toLowerCase());

    void sortRows(LogicConstants.SortingType sortingType){
        if(this.sortingType != sortingType) {
            if (sortingType == LogicConstants.SortingType.BY_ACCESS_DESC){
                    FXCollections.sort(observableList,access_desc);
            }else if (sortingType == LogicConstants.SortingType.BY_ACCESS_INC){
                FXCollections.sort(observableList,access_inc);
            }else if (sortingType == LogicConstants.SortingType.BY_SIZE_DESC){
                FXCollections.sort(observableList,size_desc);
            }else if (sortingType == LogicConstants.SortingType.BY_SIZE_INC){
                FXCollections.sort(observableList,size_inc);
            }else if (sortingType == LogicConstants.SortingType.BY_NAME_DESC){
                FXCollections.sort(observableList,name_desc);
            }else if (sortingType == LogicConstants.SortingType.BY_NAME_INC){
                FXCollections.sort(observableList,name_inc);
            }
            this.sortingType = sortingType;
        }
    }

    @FXML
    private ImageView menu;

    ContextMenu menuPopup;

    @FXML
    void showMenu(MouseEvent event) {
        System.out.println("Menu button pressed");
        menu=(ImageView)event.getSource();
        menuPopup.show(menu,event.getScreenX(),event.getScreenY());
    }

    public void showPreview(FileDetail fileDetail){

        GridPane previewGridPane = new GridPane();
        previewGridPane.setHgap(2);
        previewGridPane.setVgap(10);
        previewGridPane.setAlignment(Pos.CENTER);

        RowImageView rowImageView = new RowImageView(fileDetail, LogicConstants.PREVIEW_MODE);
        Image image = new Image(new File(Icons.getFileIconPath(fileDetail.getFileExtension())).toURI().toString());
        rowImageView.setImage(image);
        GridPane.setHalignment(rowImageView, HPos.CENTER);
        previewGridPane.add(rowImageView,0,0,2,1);

        Label nameTag = new Label("Name : ");
        nameTag.setFont(Fonts.PREVIEW_FONT);
        previewGridPane.add(nameTag,0,1);
        Label name = new Label(fileDetail.getFileName());
        name.setMaxWidth(200);
        name.setWrapText(true);
        previewGridPane.add(name,1,1);

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
    public void updateListView(){
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
    }

    public void updateListView(FileDetail fileDetail){
        File parentPath = new File(fileDetail.getFilePath());
        File []files = parentPath.listFiles();
        if(files == null){
            LogsPrinter.printLogic("Controller",157,
                    "parent Path list files give null (invalid path/some other error");
            return;
        }
        observableList.clear();
        for(File file : files){
            try {
                FileDetail inFileDetail = new FileDetail(file);
                System.out.println(inFileDetail.toString());
                ListEntry listEntry = new ListEntry(inFileDetail);
                observableList.add(listEntry);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setItems(observableList);
        /*
        *
        * menu type will be set according to theme
        * by default set it according to light theme
        * themes not yet implemented...
        *
        */
        Image image = new Image(new File(Icons.LIGHT_MENU_DOT).toURI().toString());
        menu.setFitHeight(Dimensions.MENU_ICON);
        menu.setFitWidth(Dimensions.MENU_ICON);
        menu.setImage(image);

        updateListView();

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
        menuPopup = new ContextMenu();

        MenuItem sort_by_name=new MenuItem("sort by name");

        sort_by_name.setOnAction(event->{
            if(sortingType == LogicConstants.SortingType.BY_DEFAULT||
                sortingType == LogicConstants.SortingType.BY_NAME_DESC){
                sortRows(LogicConstants.SortingType.BY_NAME_INC);
            }else{
                sortRows(LogicConstants.SortingType.BY_NAME_DESC);
            }
        });


        MenuItem sort_by_size=new MenuItem("sort by size");

        sort_by_size.setOnAction(event->{
            if(sortingType == LogicConstants.SortingType.BY_DEFAULT||
                    sortingType == LogicConstants.SortingType.BY_SIZE_DESC){
                sortRows(LogicConstants.SortingType.BY_SIZE_INC);
            }else{
                sortRows(LogicConstants.SortingType.BY_SIZE_DESC);
            }
        });


        MenuItem sort_by_access=new MenuItem("sort by access time");

        sort_by_access.setOnAction(event->{
            if(sortingType == LogicConstants.SortingType.BY_DEFAULT||
                    sortingType == LogicConstants.SortingType.BY_ACCESS_DESC){
                sortRows(LogicConstants.SortingType.BY_ACCESS_INC);
            }else{
                sortRows(LogicConstants.SortingType.BY_ACCESS_DESC);
            }
        });

        menuPopup.getItems().addAll(sort_by_name,sort_by_size,sort_by_access);

        CommonData.instance = this;

    }


}
