package win95.model.filelistview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseButton;
import win95.constants.CommonData;
import win95.constants.Dimensions;
import win95.debug.LogsPrinter;
import win95.model.filelistview.listViewelements.RowGridPane;
import win95.model.filelistview.listViewelements.RowLabel;

import static win95.utilities.filehandling.OpenFile.doubleClick;

public class UpdateCellFactory extends ListCell<ListEntry> {
    @Override
    protected void updateItem(ListEntry item, boolean empty) {
        super.updateItem(item, empty);
        if(empty){
            setGraphic(null);
        }else{
            int index = this.getIndex();

            RowGridPane grid = new RowGridPane(item.getFileDetail());
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(0, 10, 0, 10));

            RowLabel count = item.getRowCount();
            count.setText(index+1+"");
            count.setMinWidth(Dimensions.LISTVIEW_ROWCOUNT);

            grid.add(count,0,0,1,1);
            grid.add(item.getRowImageView(),1,0);
            grid.add(item.getRowNamelabel(),2,0,3,1);
            grid.add(item.getShare(),5,0);

            grid.setAlignment(Pos.BASELINE_LEFT);
//            grid.setGridLinesVisible(true);
            setGraphic(grid);
            this.setOnMouseClicked(event->{
                if (event.getButton() == MouseButton.PRIMARY){
                    if(event.getClickCount() == 2){
                        doubleClick(item.getFileDetail());
                    }
                    else{
                            if(CommonData.instance!=null){
                                LogsPrinter.printLogic("UpdateCellFactory",47,"calling controller instance");
                                CommonData.instance.showPreview(item.getFileDetail());
                            }else{
                                LogsPrinter.printLogic("UpdateCellFactory",47,"controller instance is null");
                            }
                        /*
                        * set right anchor pane to show preview details
                        */
                    }
                }
            });
        }
    }
}
