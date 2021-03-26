package win95.model.filelistview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

public class UpdateCellFactory extends ListCell<ListEntry> {
    @Override
    protected void updateItem(ListEntry item, boolean empty) {
        super.updateItem(item, empty);
        if(empty){
            setGraphic(null);
        }else{
            int index = this.getIndex();
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(0, 10, 0, 10));
            Label count = new Label(index+1+"");
            count.setMinWidth(30);
            grid.add(count,0,0,1,1);
            grid.add(item.getImageView(),1,0);
            grid.add(item.getLabel(),2,0,3,1);
            grid.add(item.getOpen(),5,0);
            grid.setAlignment(Pos.BASELINE_LEFT);
//            HBox listRow = new HBox(50);
//            listRow.setAlignment(Pos.BASELINE_LEFT);
//            listRow.getChildren().addAll(
//                    new Label(index+1+""),
//                    item.getImageView(),
//                    item.getLabel(),
//                    item.getOpen()
//            );
            setGraphic(grid);
        }
    }
}
