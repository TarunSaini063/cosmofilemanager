package win95.model.wirelessTransfer.wirelessfileslistview;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class WirelessUpdateCellFactory extends ListCell<WirelessListEntry> {
    @Override
    protected void updateItem(WirelessListEntry item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            int index = this.getIndex();

            Label count = item.getCount();
            count.setText((1 + index) + ". ");
            Label name = item.getName();

            Label percent = item.getPercent();

            Button cancel = item.getCancel();

            HBox entry = new HBox();
            entry.setSpacing(5);
            entry.getChildren().addAll(count, name, percent, cancel);

            setGraphic(entry);
        }
    }
}
