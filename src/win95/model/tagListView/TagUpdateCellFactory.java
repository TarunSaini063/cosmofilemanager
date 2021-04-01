package win95.model.tagListView;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import win95.constants.CommonData;
import win95.constants.Dimensions;
import win95.model.quickaccess.TaggedFiles;
import win95.utilities.pathmanipulation.PathHandling;

import java.util.ArrayList;

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
            Button button = new Button();

            tagRowGridPane.add(count,0,0);
            tagRowGridPane.add(tag,2,0);
            String filePath = new PathHandling(CommonData.TAG_OPTION_INSTANCE.getFileDetail().getFilePath()).getFixedPath();
            String tagColor = item.getColor()+"FF";
            ArrayList<String> tagfiles = TaggedFiles.getList(tagColor);
            if(tagfiles.contains(filePath)){
                button.setText("Remove");
                button.setOnMouseClicked(e->{
                    tagfiles.remove(filePath);
                    Node source = (Node) e.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                });
            }else{
                button.setText("Add");

                button.setOnMouseClicked(e->{
                    for(String path : tagfiles) System.out.println(path);
                    TaggedFiles.addThisFileToTag(item.getColorCircle().getFill().toString().toUpperCase(),
                            CommonData.TAG_OPTION_INSTANCE.getFileDetail().getFilePath(),
                            item.getName());
                    Node source = (Node) e.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                });
            }
            tagRowGridPane.add(button,3,0);
            setGraphic(tagRowGridPane);
        }
    }
}
