package win95.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import win95.constants.*;
import win95.debug.LogsPrinter;
import win95.model.FileDetail;
import win95.model.filegridview.GridEntry;
import win95.model.filelistview.CellFactory;
import win95.model.filelistview.ListEntry;
import win95.model.filelistview.listViewelements.RowImageView;
import win95.model.quickaccess.RecentFiles;
import win95.model.quickaccess.TagDetail;
import win95.model.quickaccess.TaggedFileObject;
import win95.model.quickaccess.TaggedFiles;
import win95.utilities.pathmanipulation.PathStack;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static javafx.application.Application.setUserAgentStylesheet;
import static win95.constants.CommonData.CURRENT_DIRECTORY;
import static win95.constants.Dimensions.LEFT_PANEL_ICON;
import static win95.utilities.filehandling.OpenFile.doubleClick;

public class Controller implements Initializable {

    @FXML
    private ListView<ListEntry> listView;
    @FXML
    private FlowPane gridView;
    @FXML
    private ScrollPane gridViewScrollPane;
    @FXML
    private VBox favouritePanel;

    @FXML
    private VBox tagPanel;

    @FXML
    private AnchorPane preview;

    @FXML
    private ImageView menu;

    @FXML
    private AnchorPane tagPane;

    @FXML
    private AnchorPane middle;

    @FXML
    private AnchorPane middleTop;

    @FXML
    private Button left;

    @FXML
    private Button right;

    public static String BUTTON_PRESSED = "NONE";

    ContextMenu menuPopup;

    final private ObservableList<ListEntry> observableList = FXCollections.observableArrayList();
    final private ArrayList<GridEntry> gridObjectList = new ArrayList<>();
    private LogicConstants.SortingType sortingType = LogicConstants.SortingType.BY_DEFAULT;

    Comparator<ListEntry> size_desc = (o2, o1) -> o1.getFileDetail().getSizeInByte().toLowerCase().compareTo(o2.getFileDetail().getSizeInByte().toLowerCase());

    Comparator<ListEntry> size_inc = (o2, o1) -> o2.getFileDetail().getSizeInByte().toLowerCase().compareTo(o1.getFileDetail().getSizeInByte().toLowerCase());

    Comparator<ListEntry> name_desc = (o2, o1) -> o1.getFileDetail().getFileName().toLowerCase().compareTo(o2.getFileDetail().getFileName().toLowerCase());

    Comparator<ListEntry> name_inc = (o2, o1) -> o2.getFileDetail().getFileName().toLowerCase().compareTo(o1.getFileDetail().getFileName().toLowerCase());

    Comparator<ListEntry> access_desc = (o2, o1) -> o1.getFileDetail().getLastAccessTime().toLowerCase().compareTo(o2.getFileDetail().getLastAccessTime().toLowerCase());

    Comparator<ListEntry> access_inc = (o2, o1) -> o2.getFileDetail().getLastAccessTime().toLowerCase().compareTo(o1.getFileDetail().getLastAccessTime().toLowerCase());

    void sortRows(LogicConstants.SortingType sortingType) {
        if (this.sortingType != sortingType) {
            if (sortingType == LogicConstants.SortingType.BY_ACCESS_DESC) {
                FXCollections.sort(observableList, access_desc);
            } else if (sortingType == LogicConstants.SortingType.BY_ACCESS_INC) {
                FXCollections.sort(observableList, access_inc);
            } else if (sortingType == LogicConstants.SortingType.BY_SIZE_DESC) {
                FXCollections.sort(observableList, size_desc);
            } else if (sortingType == LogicConstants.SortingType.BY_SIZE_INC) {
                FXCollections.sort(observableList, size_inc);
            } else if (sortingType == LogicConstants.SortingType.BY_NAME_DESC) {
                FXCollections.sort(observableList, name_desc);
            } else if (sortingType == LogicConstants.SortingType.BY_NAME_INC) {
                FXCollections.sort(observableList, name_inc);
            }
            this.sortingType = sortingType;
        }
    }

    HBox getLeftPaneHBox(String text, String iconPath) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(Dimensions.LEFT_PANEL_HBOX_PADDING));

        Image image = new Image(new File(iconPath).toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(LEFT_PANEL_ICON);
        imageView.setFitHeight(LEFT_PANEL_ICON);

        Label label = new Label(text, imageView);
        label.setPadding(new Insets(Dimensions.LEFT_PANEL_HBOX_PADDING));
        label.setFont(Fonts.LEFT_PANEL_HBOX_FONT);

        hbox.getChildren().add(label);
        hbox.setOnMouseClicked(event -> {
            File USER_HOME = new File(System.getProperty("user.home"));
            if (text.equals("Recent")) {
                CommonData.CURRENT_LIST_VIEW_ITEM = FileType.UNKNOWN;
                Set<String> recentQueue = RecentFiles.getRecentQueue();
                LinkedList<String> list = new LinkedList<>(recentQueue);
                Iterator<String> itr = list.descendingIterator();
                observableList.clear();
                gridView.getChildren().clear();
                gridObjectList.clear();
                preview.getChildren().clear();
                while(itr.hasNext()) {
                    String path = itr.next();
                    try {
                        FileDetail inFileDetail = new FileDetail(new File(path));
                        ListEntry listEntry = new ListEntry(inFileDetail);
                        observableList.add(listEntry);
                        GridEntry gridEntry = new GridEntry(listEntry);
                        gridObjectList.add(gridEntry);
                    } catch (IOException e) {
                        LogsPrinter.printError("Controller.java", 131,
                                "Error in creating recent file object");
                    }
                }
            } else if (text.equals("Desktop") || text.equals("Downloads") || text.equals("Documents")) {
                try {
                    FileDetail next = new FileDetail(new File(USER_HOME.getAbsolutePath() + "/" + text));
                    System.out.println(next.getFilePath());
                    doubleClick(next);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (text.equals("Home")) {
                try {
                    doubleClick(new FileDetail(USER_HOME));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        setHoverEffect(hbox);
        return hbox;
    }

    HBox getLeftTagPaneHBox(javafx.scene.paint.Color colorWord, String text,TagDetail tagDetail) {

        String color = String.format( "0X%02X%02X%02X",
                (int)( colorWord.getRed() * 255 ),
                (int)( colorWord.getGreen() * 255 ),
                (int)( colorWord.getBlue() * 255 ) )+"FF";
        System.out.println(text+" "+color);
        HBox hbox = new HBox() {
            @Override
            public String toString() {
                return color;
            }
        };
        Circle circle = new Circle(Dimensions.COLOR_RADIUS, colorWord) {
            @Override
            public String toString() {
                return color;
            }
        };
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getStyleClass().add("context-menu");
        MenuItem delete = new MenuItem("delete");
        delete.getStyleClass().add("menu-item");
        delete.setOnAction(e->{
            TaggedFiles.deleteTag(color);
            setTagPanel();
        });
        MenuItem modify = new MenuItem("Modify");
        modify.getStyleClass().add("menu-item");
        modify.setOnAction(e1->{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/EditDialogs.fxml"));
            Parent parent = null;
            try {
                parent = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            EditDialogs dialogs = fxmlLoader.<EditDialogs>getController();
            dialogs.setTagDetail(tagDetail);
            Scene scene = new Scene(parent, 464, 234);
            if(UserPreference.getTHEME() == Themes.LIGHT){
                scene.getStylesheets().add(getClass().getResource("../view/css/LightStyle.css").toExternalForm());
            }else if(UserPreference.getTHEME() == Themes.DARK){
                scene.getStylesheets().add(getClass().getResource("../view/css/DarkStyle.css").toExternalForm());
            }
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        });
        contextMenu.getItems().addAll(delete,modify);

        circle.setOnMouseClicked(e->{

            if(e.getButton() == MouseButton.SECONDARY) {
                System.out.println("second button pressed");
                contextMenu.show(circle, e.getScreenX(), e.getScreenY());
            }else{
                showTaggedFileListView(color);
            }
        });

        Label label = new Label(text, circle) {
            @Override
            public String toString() {
                return color;
            }
        };
        label.setContextMenu(contextMenu);
        label.setOnMouseClicked(e->{
            if(e.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(hbox, e.getScreenX(), e.getScreenY());
            }else{
                showTaggedFileListView(color);
            }
        });
        label.setPadding(new Insets(Dimensions.LEFT_PANEL_HBOX_PADDING));
        label.setFont(Fonts.LEFT_PANEL_HBOX_FONT);

        hbox.setOnMouseClicked(e->{
            if(e.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(hbox, e.getScreenX(), e.getScreenY());
            }else{
                showTaggedFileListView(color);
            }
        });
        hbox.setPadding(new Insets(Dimensions.LEFT_PANEL_HBOX_PADDING));
        hbox.getChildren().add(label);
        hbox.getStyleClass().add("hbox");
        setHoverEffect(hbox);

        return hbox;
    }

    public void showTaggedFileListView(String color) {
        CommonData.CURRENT_LIST_VIEW_ITEM = FileType.UNKNOWN;
        System.out.println("click on : "+color.toUpperCase());
        TaggedFileObject taggedFileObject = new TaggedFileObject(color.toUpperCase());
        ArrayList<FileDetail> fileDetails = taggedFileObject.getTaggedFileObject();
        observableList.clear();
        System.out.println("file fetched from taggedFileObject \n"+fileDetails.toString());
        for(FileDetail fileDetail : fileDetails){
            observableList.add(new ListEntry(fileDetail));
        }
    }

    void setFavouritePanel() {
        HBox top = new HBox();
        Label favourite = new Label("Favourite");
        favourite.setFont(Fonts.TOP_FONT);

        HBox recent = getLeftPaneHBox("Recent", Icons.RECENT_LIGHT);
        recent.getStyleClass().add("hbox");
        HBox desktop = getLeftPaneHBox("Desktop", Icons.DESKTOP_LIGHT);
        desktop.getStyleClass().add("hbox");
        HBox download = getLeftPaneHBox("Downloads", Icons.DOWNLOAD_LIGHT);
        download.getStyleClass().add("hbox");
        HBox document = getLeftPaneHBox("Documents", Icons.DOCUMENT_LIGHT);
        document.getStyleClass().add("hbox");
        HBox home = getLeftPaneHBox("Home", Icons.HOME_LIGHT);
        home.getStyleClass().add("hbox");
        favouritePanel.getChildren().addAll(favourite, recent, desktop, download, document, home);


    }

    void setDefaultTags() {
        TaggedFiles.setUserTag();
        for (Map.Entry<String, TagDetail> color : TaggedFiles.taggedFile.entrySet()){
            TagDetail tagDetail = color.getValue();
            String name = tagDetail.getName();
            String tagColor = tagDetail.getColor();
            tagColor = tagColor.substring(0,tagColor.length()-2);
            HBox hbox = getLeftTagPaneHBox(javafx.scene.paint.Color.web(tagColor), name,tagDetail);
            appendTag(hbox);
        }
    }

    public void appendTag(HBox tag) {

        setHoverEffect(tag);
        tagPanel.getChildren().add(tag);
    }

    public void setTagPanel() {
        HBox top = new HBox() {
            @Override
            public String toString() {
                return "Tags";
            }
        };
        top.setSpacing(200);

        Label Tags = new Label("Tags");
        Tags.setFont(Fonts.TOP_FONT);

        Image addTagImage = new Image(new File(Icons.ADDTAG_LIGHT).toURI().toString());
        ImageView addTagImageView = new ImageView(addTagImage);
        addTagImageView.setFitWidth(LEFT_PANEL_ICON);
        addTagImageView.setFitHeight(LEFT_PANEL_ICON);
        addTagImageView.setOnMouseClicked(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/Dialogs.fxml"));
            Parent parent = null;
            try {
                parent = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            TagDialogs dialogs = fxmlLoader.<TagDialogs>getController();
            Scene scene = new Scene(parent, 464, 234);
            Stage stage = new Stage();
            if(UserPreference.getTHEME() == Themes.LIGHT){
                scene.getStylesheets().add(getClass().getResource("../view/css/LightStyle.css").toExternalForm());
            }else if(UserPreference.getTHEME() == Themes.DARK){
                scene.getStylesheets().add(getClass().getResource("../view/css/DarkStyle.css").toExternalForm());
            }
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        });
        Tooltip.install(addTagImageView, new Tooltip("Add new Tag"));

        top.getChildren().addAll(Tags, addTagImageView);

        tagPanel.getChildren().clear();
        tagPanel.getChildren().add(top);

        setDefaultTags();
    }
    @FXML
    void showMenu(MouseEvent event) {
        System.out.println("Menu button pressed");
        menu = (ImageView) event.getSource();
        menuPopup.show(menu, event.getScreenX(), event.getScreenY());
    }

    @FXML
    void nextDirectory(ActionEvent event) throws IOException {
        System.out.println("Next directory button pressed");
        BUTTON_PRESSED = "NEXT";
        FileDetail nextDirectory = PathStack.getNextDirectory();
        if (nextDirectory == null) {
            BUTTON_PRESSED = "NONE";
            System.out.println("PathStack.getNextDirectory() return null ");
            return;
        }
        if(CommonData.VIEW_MODE.equals("LISTVIEW")) {
            if (updateListView(nextDirectory)) {
                preview.getChildren().clear();
            } else {
                BUTTON_PRESSED = "NONE";
            }
        }else{
            if (updateGridView(nextDirectory)) {
                preview.getChildren().clear();
            } else {
                BUTTON_PRESSED = "NONE";
            }
        }
    }

    @FXML
    void previousDirectory(ActionEvent event) throws IOException {
        System.out.println("Previous directory button pressed");
        BUTTON_PRESSED = "PREVIOUS";
        FileDetail previousDirectory = PathStack.getPreviousDirectory();
        if (previousDirectory == null) {
            BUTTON_PRESSED = "NONE";
            System.out.println("PathStack.getPreviousDirectory() return null ");
            return;
        }
        if(CommonData.VIEW_MODE.equals("LISTVIEW")) {
            if (updateListView(previousDirectory)) {
                preview.getChildren().clear();
            } else {
                BUTTON_PRESSED = "NONE";
            }
        }else{
            if (updateGridView(previousDirectory)) {
                preview.getChildren().clear();
            } else {
                BUTTON_PRESSED = "NONE";
            }
        }
    }

    public void showPreview(FileDetail fileDetail) {

        GridPane previewGridPane = new GridPane();
        previewGridPane.setHgap(2);
        previewGridPane.setVgap(10);
        previewGridPane.setAlignment(Pos.CENTER);

        RowImageView rowImageView = new RowImageView(fileDetail, LogicConstants.PREVIEW_MODE);
        Image image = new Image(new File(Icons.getFileIconPath(fileDetail.getFileExtension())).toURI().toString());
        rowImageView.setImage(image);
        GridPane.setHalignment(rowImageView, HPos.CENTER);
        previewGridPane.add(rowImageView, 0, 0, 2, 1);

        Label nameTag = new Label("Name : ");
        nameTag.setFont(Fonts.PREVIEW_FONT);
        previewGridPane.add(nameTag, 0, 1);
        Label name = new Label(fileDetail.getFileName());
        name.setMaxWidth(200);
        name.setWrapText(true);
        previewGridPane.add(name, 1, 1);

        Label sizeTag = new Label("Size : ");
        sizeTag.setFont(Fonts.PREVIEW_FONT);
        previewGridPane.add(sizeTag, 0, 2);
        previewGridPane.add(new Label(fileDetail.getOptimizedSize()), 1, 2);


        Label creationTime = new Label(fileDetail.getCreationTime());
        Label creationTimeTag = new Label("Creation Time : ");
        creationTimeTag.setFont(Fonts.PREVIEW_FONT);
        previewGridPane.add(creationTimeTag, 0, 3);
        previewGridPane.add(creationTime, 1, 3);

        Label lastAccessTime = new Label(fileDetail.getLastAccessTime());
        Label lastAccessTimeTag = new Label("Last Access Time : ");
        lastAccessTimeTag.setFont(Fonts.PREVIEW_FONT);
        previewGridPane.add(lastAccessTimeTag, 0, 4);
        previewGridPane.add(lastAccessTime, 1, 4);

        Label lastModifiedTime = new Label(fileDetail.getLastModifiedTime());
        Label lastModifiedTimeTag = new Label("Last Modified Time : ");
        lastModifiedTimeTag.setFont(Fonts.PREVIEW_FONT);
        previewGridPane.add(lastModifiedTimeTag, 0, 5);
        previewGridPane.add(lastModifiedTime, 1, 5);

        AnchorPane.setTopAnchor(previewGridPane, 5d);
        AnchorPane.setLeftAnchor(previewGridPane, 5d);
        AnchorPane.setRightAnchor(previewGridPane, 5d);
        AnchorPane.setBottomAnchor(previewGridPane, 5d);
        preview.getChildren().clear();
        System.out.println(previewGridPane.toString());
        preview.getChildren().add(previewGridPane);
    }

    public void updateListView() throws IOException {
        File USER_HOME = new File(System.getProperty("user.home"));
        CommonData.CURRENT_LIST_VIEW_ITEM = FileType.DIRECTORY;
        CURRENT_DIRECTORY = new FileDetail(USER_HOME);
        File[] files = USER_HOME.listFiles();
        gridView.getChildren().clear();
        observableList.clear();
        assert files != null;
        for (File file : files) {
            try {
                FileDetail fileDetail = new FileDetail(file);
                ListEntry listEntry = new ListEntry(fileDetail);
                observableList.add(listEntry);
                GridEntry  gridEntry = new GridEntry(listEntry);
                gridObjectList.add(gridEntry);
                gridView.getChildren().add(gridEntry.getFileGridBlock());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void updateGridView() throws IOException {
        File USER_HOME = new File(System.getProperty("user.home"));
        CommonData.CURRENT_LIST_VIEW_ITEM = FileType.DIRECTORY;
        CURRENT_DIRECTORY = new FileDetail(USER_HOME);
        File[] files = USER_HOME.listFiles();
        observableList.clear();
        gridObjectList.clear();
        gridView.getChildren().clear();
        assert files != null;
        for (File file : files) {
            try {
                FileDetail fileDetail = new FileDetail(file);
                ListEntry listEntry = new ListEntry(fileDetail);
                observableList.add(listEntry);
                GridEntry gridEntry = new GridEntry(listEntry);
                gridEntry.refresh();
                gridObjectList.add(gridEntry);
                gridView.getChildren().add(gridEntry.getFileGridBlock());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gridView.setVisible(true);
    }

    public boolean updateListView(FileDetail fileDetail) throws IOException {
        if (fileDetail == null) return false;
        File parentPath = fileDetail.getFile();

        File[] files = parentPath.listFiles();
        if (files == null) {
            LogsPrinter.printLogic("Controller", 157,
                    "parent Path list files give null (invalid path/some other error");
            return false;
        }
        if (BUTTON_PRESSED.equals("NEXT")) {
            PathStack.setPreviousDirectory(CURRENT_DIRECTORY);
            BUTTON_PRESSED = "NONE";
            CURRENT_DIRECTORY = fileDetail;
        } else if (BUTTON_PRESSED.equals("PREVIOUS")) {
            PathStack.setNextDirectory(CURRENT_DIRECTORY);
            BUTTON_PRESSED = "NONE";
            CURRENT_DIRECTORY = fileDetail;
        }
        observableList.clear();
        gridView.getChildren().clear();
        gridObjectList.clear();
        for (File file : files) {
            try {
                FileDetail inFileDetail = new FileDetail(file);
                ListEntry listEntry = new ListEntry(inFileDetail);
                listEntry.refresh();
                gridObjectList.add(new GridEntry(listEntry));
                observableList.add(listEntry);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        CommonData.CURRENT_LIST_VIEW_ITEM = FileType.DIRECTORY;
        return true;
    }

    public boolean updateGridView(FileDetail fileDetail) throws IOException {
        if (fileDetail == null) return false;
        File parentPath = fileDetail.getFile();

        File[] files = parentPath.listFiles();
        if (files == null) {
            LogsPrinter.printLogic("Controller", 157,
                    "parent Path list files give null (invalid path/some other error");
            return false;
        }
        if (BUTTON_PRESSED.equals("NEXT")) {
            PathStack.setPreviousDirectory(CURRENT_DIRECTORY);
            BUTTON_PRESSED = "NONE";
            CURRENT_DIRECTORY = fileDetail;
        } else if (BUTTON_PRESSED.equals("PREVIOUS")) {
            PathStack.setNextDirectory(CURRENT_DIRECTORY);
            BUTTON_PRESSED = "NONE";
            CURRENT_DIRECTORY = fileDetail;
        }
        observableList.clear();
        gridView.getChildren().clear();
        gridObjectList.clear();
        for (File file : files) {
            try {
                FileDetail inFileDetail = new FileDetail(file);
                ListEntry listEntry = new ListEntry(inFileDetail);
                GridEntry gridEntry = new GridEntry(listEntry);
                gridEntry.refresh();
                gridObjectList.add(gridEntry);
                observableList.add(listEntry);
                gridView.getChildren().add(gridEntry.getFileGridBlock());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CommonData.CURRENT_LIST_VIEW_ITEM = FileType.DIRECTORY;
        listView.setVisible(false);
        gridView.setVisible(true);
        return true;
    }

    void setHoverEffect(Node node) {
//        node.styleProperty().bind(
//                Bindings
//                        .when(node.hoverProperty())
//                        .then(
//                                new SimpleStringProperty(HOVERED_BUTTON_STYLE)
//                        )
//                        .otherwise(
//                                new SimpleStringProperty(STANDARD_BUTTON_STYLE)
//                        )
//        );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gridView.setVgap(10);
        gridViewScrollPane.setFitToWidth(true);
        listView.setItems(observableList);

        if(CommonData.VIEW_MODE.equals("LISTVIEW")){
            gridView.setVisible(false);
            listView.setVisible(true);
            try {
                updateListView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            listView.setVisible(false);
            gridView.setVisible(true);
            try {
                updateGridView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /*
         *
         * menu type will be set according to theme
         * by default set it according to light theme
         * themes not yet implemented...
         *
         */
//        tagPane.setStyle("-fx-background-color: " + Color.LEFT_PANE_COLOR);
//        middleTop.setStyle("-fx-background-color: " + Color.MIDDLE_PANE_COLOR);
//        preview.setStyle("-fx-background-color: " + Color.PREVIEW_PANE_COLOR);

        Image leftImageLight = new Image(new File(Icons.LIGHT_LEFT_ARROW).toURI().toString());
        ImageView leftImageLightImageView = new ImageView(leftImageLight);
        leftImageLightImageView.setFitWidth(Dimensions.DIRECTORY_MOVEMENT_ICON);
        leftImageLightImageView.setFitHeight(Dimensions.DIRECTORY_MOVEMENT_ICON);
        left.setGraphic(leftImageLightImageView);

        Image rightImageLight = new Image(new File(Icons.LIGHT_RIGHT_ARROW).toURI().toString());
        ImageView rightImageLightImageView = new ImageView(rightImageLight);
        rightImageLightImageView.setFitWidth(Dimensions.DIRECTORY_MOVEMENT_ICON);
        rightImageLightImageView.setFitHeight(Dimensions.DIRECTORY_MOVEMENT_ICON);
        right.setGraphic(rightImageLightImageView);

        Image image = new Image(new File(Icons.LIGHT_MENU_DOT).toURI().toString());
        menu.setFitHeight(Dimensions.MENU_ICON);
        menu.setFitWidth(Dimensions.MENU_ICON);
        menu.setImage(image);


        setFavouritePanel();
        setTagPanel();

        listView.setCellFactory(new CellFactory());

        menuPopup = new ContextMenu();
        menuPopup.getStyleClass().add("context-menu");

        MenuItem sort_by_name = new MenuItem("sort by name");
        sort_by_name.getStyleClass().add("menu-item");
        MenuItem createNewFile = new MenuItem("New File");
        createNewFile.getStyleClass().add("menu-item");
        createNewFile.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/CreateFile.fxml"));
            Parent parent = null;
            try {
                parent = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            CreateFile createFile = fxmlLoader.<CreateFile>getController();
            createFile.setFileDetail(CURRENT_DIRECTORY);
            createFile.setFileType(FileType.FILE);
            Scene scene1 = new Scene(parent, 419, 159);
            if(UserPreference.getTHEME() == Themes.LIGHT){
                scene1.getStylesheets().add(getClass().getResource("../view/css/LightStyle.css").toExternalForm());
            }else if(UserPreference.getTHEME() == Themes.DARK){
                scene1.getStylesheets().add(getClass().getResource("../view/css/DarkStyle.css").toExternalForm());
            }
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene1);
            stage.showAndWait();
        });

        MenuItem createNewFolder = new MenuItem("New Folder");
        createNewFolder.getStyleClass().add("menu-item");
        createNewFolder.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/CreateFile.fxml"));
            Parent parent = null;
            try {
                parent = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            CreateFile createFile = fxmlLoader.<CreateFile>getController();
            createFile.setFileDetail(CURRENT_DIRECTORY);
            createFile.setFileType(FileType.DIRECTORY);
            Scene scene1 = new Scene(parent, 419, 159);
            if(UserPreference.getTHEME() == Themes.LIGHT){
                scene1.getStylesheets().add(getClass().getResource("../view/css/LightStyle.css").toExternalForm());
            }else if(UserPreference.getTHEME() == Themes.DARK){
                scene1.getStylesheets().add(getClass().getResource("../view/css/DarkStyle.css").toExternalForm());
            }
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene1);
            stage.showAndWait();
        });

        sort_by_name.setOnAction(event -> {
            if (sortingType == LogicConstants.SortingType.BY_DEFAULT ||
                    sortingType == LogicConstants.SortingType.BY_NAME_DESC) {
                sortRows(LogicConstants.SortingType.BY_NAME_INC);
            } else {
                sortRows(LogicConstants.SortingType.BY_NAME_DESC);
            }
        });


        MenuItem sort_by_size = new MenuItem("sort by size");
        sort_by_size.getStyleClass().add("menu-item");
        sort_by_size.setOnAction(event -> {
            if (sortingType == LogicConstants.SortingType.BY_DEFAULT ||
                    sortingType == LogicConstants.SortingType.BY_SIZE_DESC) {
                sortRows(LogicConstants.SortingType.BY_SIZE_INC);
            } else {
                sortRows(LogicConstants.SortingType.BY_SIZE_DESC);
            }
        });


        MenuItem sort_by_access = new MenuItem("sort by access time");
        sort_by_access.getStyleClass().add("menu-item");
        sort_by_access.setOnAction(event -> {
            if (sortingType == LogicConstants.SortingType.BY_DEFAULT ||
                    sortingType == LogicConstants.SortingType.BY_ACCESS_DESC) {
                sortRows(LogicConstants.SortingType.BY_ACCESS_INC);
            } else {
                sortRows(LogicConstants.SortingType.BY_ACCESS_DESC);
            }
        });

        MenuItem theme = new MenuItem("Change Theme");
        theme.setOnAction(e->{
            Scene scene = listView.getScene();
            scene.getStylesheets().clear();
            setUserAgentStylesheet(null);
            System.out.println(scene);
            if(UserPreference.getTHEME() == Themes.DARK){
                scene.getStylesheets().add(this.getClass().getResource("../view/css/LightStyle.css").toExternalForm());
                UserPreference.setTHEME(Themes.LIGHT);
            }else if(UserPreference.getTHEME() == Themes.LIGHT){
                scene.getStylesheets().add(this.getClass().getResource("../view/css/DarkStyle.css").toExternalForm());
                UserPreference.setTHEME(Themes.DARK);
            }
        });

        MenuItem toggleFileView = new MenuItem("Toggle view mode");
        toggleFileView.setOnAction(event -> {
            toggleViewMode();
        });
        menuPopup.getItems().addAll(sort_by_name, sort_by_size, sort_by_access,theme,toggleFileView);
        if(CommonData.CURRENT_LIST_VIEW_ITEM == FileType.DIRECTORY){
            menuPopup.getItems().addAll(createNewFile,createNewFolder);
        }
        CommonData.instance = this;

    }

    private void toggleViewMode() {
        if(CommonData.VIEW_MODE.equals("LISTVIEW")){
            CommonData.VIEW_MODE = "GRIDVIEW";
            if(observableList.isEmpty()){
                try {
                    updateGridView();
                    gridView.setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                listView.setVisible(false);
                gridView.getChildren().clear();
                gridObjectList.clear();
                for(ListEntry fileListEntry : observableList){
                    GridEntry gridEntry = new GridEntry(fileListEntry);
                    gridEntry.refresh();
                     gridObjectList.add(gridEntry);
                     gridView.getChildren().add(gridEntry.getFileGridBlock());
                }
                observableList.clear();
                gridView.setVisible(true);
            }
        }else{
            CommonData.VIEW_MODE = "LISTVIEW";
            gridView.setVisible(false);
            observableList.clear();
            for(GridEntry gridEntry : gridObjectList){
                gridEntry.getListEntry().refresh();
                observableList.add(gridEntry.getListEntry());
            }
            gridView.getChildren().clear();
            gridObjectList.clear();
            listView.setVisible(true);
        }
    }

    public void showAddTagToFileDialog(ListEntry item) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/SetTagOptionDialogs.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        SetTagOptionDialogs setTagOptionDialogs = fxmlLoader.<SetTagOptionDialogs>getController();
        assert parent != null;
        setTagOptionDialogs.setFileDetails(item.getFileDetail());
        Scene scene = new Scene(parent, 500, 600);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        if(UserPreference.getTHEME() == Themes.LIGHT){
            scene.getStylesheets().add(this.getClass().getResource("../view/css/LightStyle.css").toExternalForm());
        }else if(UserPreference.getTHEME() == Themes.DARK){
            scene.getStylesheets().add(this.getClass().getResource("../view/css/DarkStyle.css").toExternalForm());
        }
        stage.setScene(scene);
        stage.setResizable(false);
//        stage.setMaxHeight(600);
//        stage.setMaxWidth(500);
        stage.showAndWait();
    }

    public void deleteFromListView(ListEntry listEntry) {
        listView.getItems().remove(listEntry);
    }

    public void deleteFromGridView(GridEntry gridEntry) {
        gridView.getChildren().remove(gridEntry.getFileGridBlock());
        gridObjectList.remove(gridEntry);
    }

    public void appendInCurrentListView(ListEntry listEntry){
        listView.getItems().add(listEntry);
    }

    public void appendInCurrentGridView(GridEntry gridEntry){
        gridView.getChildren().add(gridEntry.getFileGridBlock());
        gridObjectList.add(gridEntry);
    }

    public void addThemeOptionMenu(){

    }

    private void changeTheme() {
        if(UserPreference.getTHEME() == Themes.DARK){
            listView.getScene().getStylesheets().add(getClass().getResource("./view/css/LightStyle.css").toExternalForm());
        }else if(UserPreference.getTHEME() == Themes.LIGHT){
            listView.getScene().getStylesheets().add(getClass().getResource("./view/css/DarkStyle.css").toExternalForm());
        }
    }
}
