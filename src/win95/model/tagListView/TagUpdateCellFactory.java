package win95.model.tagListView;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import win95.constants.CommonData;
import win95.constants.Dimensions;
import win95.model.quickaccess.TaggedFiles;

public class TagUpdateCellFactory extends ListCell<TagListEntry> {
    @Override
    protected void updateItem(TagListEntry item, boolean empty) {
        super.updateItem(item, empty);
        if(empty){
            setGraphic(null);
        }else {
            int index = this.getIndex();
            GridPane tagRowGridPane = new GridPane();
            tagRowGridPane.setHgap(10);
            tagRowGridPane.setVgap(10);

            Label count = item.getTagCount();
            count.setText(index+1+"");
            count.setMinWidth(Dimensions.LISTVIEW_ROWCOUNT);

            Label tag = item.getTagNameLabel();

            tagRowGridPane.add(count,0,0);
            tagRowGridPane.add(tag,2,0);

            setGraphic(tagRowGridPane);
            this.setOnMouseClicked(e->{
                if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                    TaggedFiles.addTaggedQueue(item.getColorCircle().getFill().toString().toUpperCase(),
                            CommonData.TAG_OPTION_INSTANCE.getFileDetail().getFilePath(),
                            item.getName());
                    Node source = (Node) e.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                }
            });
        }
    }
}
