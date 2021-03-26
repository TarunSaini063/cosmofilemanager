package win95.model.filelistview;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class UpdateCellFactory extends ListCell<ListEntry> {
    @Override
    protected void updateItem(ListEntry item, boolean empty) {
        super.updateItem(item, empty);
        if(empty){
            setGraphic(null);
        }else{
            int index = this.getIndex();
            HBox listRow = new HBox(50);
            listRow.setAlignment(Pos.BASELINE_LEFT);
            listRow.getChildren().addAll(
                    new Label(index+1+""),
                    item.getImageView(),
                    item.getLabel(),
                    item.getOpen()
            );
            setGraphic(listRow);
        }
    }
}
