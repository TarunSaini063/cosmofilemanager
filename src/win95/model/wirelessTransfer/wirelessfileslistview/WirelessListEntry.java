package win95.model.wirelessTransfer.wirelessfileslistview;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class WirelessListEntry {
    Label name, count, percent;
    Button cancel;
    String nameStr;


    public WirelessListEntry(String name) {
        this.name = new Label(name);
        this.name.setMinWidth(200);
        nameStr = name;
        this.name.setMaxWidth(200);
        this.count = new Label();
        this.percent = new Label("0 %");
        Font PREVIEW_FONT = Font.font("Comic Sans MS", FontWeight.BOLD, 15);
        percent.setFont(PREVIEW_FONT);
        cancel = new Button("Cancel");
        cancel.setStyle("-fx-text-fill: white; -fx-background-color: red");
        cancel.setOnAction(e -> {
            System.out.println("pressed cancel");
        });
    }

    public String getNameStr() {
        return nameStr;
    }

    public Label getName() {
        return name;
    }

    public void setName(Label name) {
        this.name = name;
    }

    public Label getCount() {
        return count;
    }

    public void setCount(Label count) {
        this.count = count;
    }

    public Label getPercent() {
        return percent;
    }

    public void setPercent(Label percent) {
        this.percent = percent;
    }

    public Button getCancel() {
        return cancel;
    }
}
