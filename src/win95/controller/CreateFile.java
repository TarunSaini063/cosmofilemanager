package win95.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import win95.constants.CommonData;
import win95.constants.FileType;
import win95.debug.LogsPrinter;
import win95.model.FileDetail;
import win95.model.filelistview.ListEntry;
import win95.utilities.filehandling.os.SystemCommands;

import java.io.File;
import java.io.IOException;

public class CreateFile {
    @FXML
    private TextField name;

    private FileType fileType;
    private FileDetail fileDetail;
    @FXML
    private Button add;

    @FXML
    private Button cancel;

    @FXML
    void add(ActionEvent event) {
        String path = fileDetail.getFilePath();
        if(fileType == FileType.DIRECTORY) {
            SystemCommands.createFolder(path + "/" + name.getText());
        }else{
            SystemCommands.createFile(path + "/" + name.getText());
        }
        try {
            CommonData.instance.appendInCurrentListView(new ListEntry(new FileDetail(new File(path+"/"+name.getText()))));
        } catch (IOException e) {
            LogsPrinter.printError("CreateFile",41,
                    "Error in appending newly created file to listView");
            e.printStackTrace();
        }
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void cancel(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public FileDetail getFileDetail() {
        return fileDetail;
    }

    public void setFileDetail(FileDetail fileDetail) {
        this.fileDetail = fileDetail;
    }
}
