package win95.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import win95.constants.CommonData;
import win95.constants.Dimensions;
import win95.constants.Fonts;
import win95.model.quickaccess.TaggedFiles;

import java.net.URL;
import java.util.ResourceBundle;

public class TagDialogs implements Initializable {
    @FXML
    private ColorPicker tagColorPicker;

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

    @FXML
    void addTag(ActionEvent event) {
        System.out.println(tagName.getText());
        String color = tagColorPicker.getValue().toString().toUpperCase();
        System.out.println("Adding tag = "+color);
        if(!TaggedFiles.addNewCreatedTag(color,tagName.getText())){
            invalidColor.setVisible(true);
            return;
        }

        Circle circle = new Circle(Dimensions.COLOR_RADIUS, tagColorPicker.getValue()){
            @Override
            public String toString() {
                return tagName.getText();
            }
        };
        circle.setOnMouseClicked(e->{
            CommonData.instance.showTaggedFileListView(color);
        });
        Label label = new Label(tagName.getText(), circle){
            @Override
            public String toString() {
                return tagName.getText();
            }
        };
        label.setOnMouseClicked(e->{
            CommonData.instance.showTaggedFileListView(color);
        });
        label.setPadding(new Insets(Dimensions.LEFT_PANEL_HBOX_PADDING));
        label.setFont(Fonts.LEFT_PANEL_HBOX_FONT);

        HBox tag = new HBox() {
            @Override
            public String toString() {
                return label.getText();
            }
        };
        tag.setOnMouseClicked(e->{
            CommonData.instance.showTaggedFileListView(color);
        });
        tag.setPadding(new Insets(Dimensions.LEFT_PANEL_HBOX_PADDING));

        tag.getChildren().add(label);

        CommonData.instance.appendTag(tag);
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void cancel(ActionEvent event) {
        System.out.println(tagName.getText());
        System.out.println(tagColorPicker.getValue());
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
