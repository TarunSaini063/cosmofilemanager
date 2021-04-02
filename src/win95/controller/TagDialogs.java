package win95.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
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
    private AnchorPane dialogPane;

    @FXML
    void addTag(ActionEvent event) {
        System.out.println(tagName.getText());
        String color = String.format( "0X%02X%02X%02X",
                (int)( tagColorPicker.getValue().getRed() * 255 ),
                (int)( tagColorPicker.getValue().getGreen() * 255 ),
                (int)( tagColorPicker.getValue().getBlue() * 255 ) )+"FF";

        if(!TaggedFiles.addNewCreatedTag(color,tagName.getText())){
            invalidColor.setVisible(true);
            return;
        }
        ContextMenu contextMenu = new ContextMenu();
        MenuItem delete = new MenuItem("delete");
        delete.setOnAction(e->{
            TaggedFiles.deleteTag(color);
            CommonData.instance.setTagPanel();
        });
        MenuItem modify = new MenuItem("Modify");
        modify.setOnAction(e->{
        });
        contextMenu.getItems().addAll(delete,modify);

        Circle circle = new Circle(Dimensions.COLOR_RADIUS, tagColorPicker.getValue()){
            @Override
            public String toString() {
                return color;
            }
        };
        circle.setOnMouseClicked(e->{
            if(e.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(circle, e.getScreenX(), e.getScreenY());
            }else{
                CommonData.instance.showTaggedFileListView(color);
            }
        });
        Label label = new Label(tagName.getText(), circle){
            @Override
            public String toString() {
                return color;
            }
        };
        label.setOnMouseClicked(e->{
            if(e.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(circle, e.getScreenX(), e.getScreenY());
            }else{
                CommonData.instance.showTaggedFileListView(color);
            }
        });
        label.setPadding(new Insets(Dimensions.LEFT_PANEL_HBOX_PADDING));
        label.setFont(Fonts.LEFT_PANEL_HBOX_FONT);

        HBox tag = new HBox() {
            @Override
            public String toString() {
                return color;
            }
        };
        tag.setOnMouseClicked(e->{
            if(e.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(circle, e.getScreenX(), e.getScreenY());
            }else{
                CommonData.instance.showTaggedFileListView(color);
            }
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
