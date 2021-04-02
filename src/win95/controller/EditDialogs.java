package win95.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import win95.constants.CommonData;
import win95.model.quickaccess.TagDetail;
import win95.model.quickaccess.TaggedFiles;

import java.net.URL;
import java.util.ResourceBundle;

public class EditDialogs implements Initializable {
    @FXML
    private ColorPicker tagColorPicker;

    @FXML
    private AnchorPane dialogPane;

    @FXML
    private Circle tagColorCircle;

    @FXML
    private TextField tagName;

    @FXML
    private Button addTag;

    @FXML
    private Button cancel;

    @FXML
    private Label invalidColor;

    private TagDetail tagDetail;

    public void setTagDetail(TagDetail tagDetail){
        this.tagDetail = tagDetail;
    }

    public TagDetail getTagDetail(){
        return  tagDetail;
    }

    @FXML
    void addTag(ActionEvent event) {
        String color = String.format( "0X%02X%02X%02X",
                (int)( tagColorPicker.getValue().getRed() * 255 ),
                (int)( tagColorPicker.getValue().getGreen() * 255 ),
                (int)( tagColorPicker.getValue().getBlue() * 255 ) )+"FF";
        System.out.println("Updated tag new color = "+color);
        if(!TaggedFiles.editTag(tagDetail.getColor(),color,tagDetail.getName(),tagName.getText())){
            invalidColor.setVisible(true);
            return;
        }

        CommonData.instance.setTagPanel();
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

    @FXML
    void setCircleColor(ActionEvent event) {
        tagColorCircle.setFill(tagColorPicker.getValue());
        if (!tagColorCircle.isVisible()) {
            tagColorCircle.setVisible(true);
            /*
             *
             *
             * check for duplicate name or color and show message accordingly
             * invalidColor.setVisible(true);
             * not yet implemented...
             *
             */

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tagColorCircle.setVisible(false);
        invalidColor.setVisible(false);
    }

}
