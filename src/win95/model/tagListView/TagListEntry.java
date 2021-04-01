package win95.model.tagListView;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import win95.constants.Dimensions;
import win95.constants.Fonts;

public class TagListEntry {
    private Circle colorCircle;
    String color ;

    private Label tagName;
    private Label tagCount;
    private String tagNameString;


    public TagListEntry(String color, String tagName){
        tagCount = new Label();
        tagNameString = tagName;
        tagCount.setMinWidth(10);
        if(color.length()>8)
            color = color.substring(0,color.length()-2);
        this.color = color;
        colorCircle = new Circle(Dimensions.COLOR_RADIUS, Color.web(color)) {
            @Override
            public String toString() {
                return tagName;
            }
        };
        this.tagName = new Label(tagName, colorCircle) {
            @Override
            public String toString() {
                return tagName;
            }
        };
        this.tagName.setPadding(new Insets(Dimensions.LEFT_PANEL_HBOX_PADDING));
        this.tagName.setFont(Fonts.LEFT_PANEL_HBOX_FONT);
        this.tagName.setMinWidth(200);
    }
    public Circle getColorCircle() {
        return colorCircle;
    }

    public Label getTagNameLabel() {
        return this.tagName;
    }
    public String getColor() {
        return color;
    }

    public Label getTagCount() {
        return tagCount;
    }

    public String getName(){
        return tagNameString;
    }
    @Override
    public String toString() {
        return "Color : " + colorCircle.getFill()+'\n'+
                "Name : " + tagName.getText()+'\n';
    }
}
