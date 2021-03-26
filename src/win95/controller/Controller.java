package win95.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import win95.model.filelistview.CellFactory;
import win95.model.filelistview.ListEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ListView<ListEntry> listView;

    final private ObservableList<ListEntry> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setItems(observableList);
        ArrayList<String> arrayList = new ArrayList<>();
        String command = "ls /Users/tarun/Desktop/Coding/Codechef";
        BufferedReader bufferedReader=null;
        try {
            Process process = Runtime.getRuntime().exec(command);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output;
            while((output = bufferedReader.readLine())!=null){
                System.out.println(output);
                arrayList.add(output);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bufferedReader!=null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for(String item : arrayList){
            ListEntry listEntry = new ListEntry(item);
            observableList.add(listEntry);
        }
        listView.setCellFactory(new CellFactory());
    }

}
