package win95.model.wirelessTransfer.wirelessfileslistview;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class WirelessCellFactory implements Callback<ListView<WirelessListEntry>, ListCell<WirelessListEntry>> {
    @Override
    public ListCell<WirelessListEntry> call(ListView<WirelessListEntry> param) {
        return new WirelessUpdateCellFactory();
    }
}
