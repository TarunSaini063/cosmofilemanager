package win95.model.tagListView;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class TagCellFactory implements Callback<ListView<TagListEntry>, ListCell<TagListEntry>> {
    @Override
    public ListCell<TagListEntry> call(ListView<TagListEntry> param) {
        return new TagUpdateCellFactory();
    }
}
