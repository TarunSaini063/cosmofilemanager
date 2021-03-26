package win95.model.filelistview;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class CellFactory implements Callback<ListView<ListEntry>, ListCell<ListEntry>> {
    @Override
    public ListCell<ListEntry> call(ListView<ListEntry> param) {
        return new UpdateCellFactory();
    }
}
