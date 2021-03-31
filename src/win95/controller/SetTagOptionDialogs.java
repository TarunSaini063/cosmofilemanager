package win95.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import win95.constants.CommonData;
import win95.model.FileDetail;
import win95.model.quickaccess.TaggedFiles;
import win95.model.tagListView.TagCellFactory;
import win95.model.tagListView.TagListEntry;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class SetTagOptionDialogs implements Initializable {
    private  FileDetail fileDetail;
    final private ObservableList<TagListEntry> observableList = FXCollections.observableArrayList();

    @FXML
    private ListView<TagListEntry> tagListView;
    public void setFileDetails(FileDetail fileDetail){
        this.fileDetail = fileDetail;
    }

    public FileDetail getFileDetail() {
        return fileDetail;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        observableList.clear();
        tagListView.setItems(observableList);
        tagListView.setCellFactory(new TagCellFactory());
        CommonData.TAG_OPTION_INSTANCE = this;
        Map<String,String> taggedColorToNameMap = TaggedFiles.getTaggedColorToNameMap();
        for (Map.Entry<String, String> tag : taggedColorToNameMap.entrySet()){
            TagListEntry tagListEntry = new TagListEntry(tag.getKey(),tag.getValue());
            observableList.add(tagListEntry);
        }
    }
}
