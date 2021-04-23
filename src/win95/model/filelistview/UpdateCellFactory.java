package win95.model.filelistview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import win95.constants.CommonData;
import win95.constants.Dimensions;
import win95.constants.FileType;
import win95.debug.LogsPrinter;
import win95.model.filelistview.listViewelements.RowGridPane;
import win95.model.filelistview.listViewelements.RowLabel;
import win95.utilities.filehandling.os.SystemCommands;

import java.io.IOException;

import static win95.utilities.filehandling.OpenFile.doubleClick;

public class UpdateCellFactory extends ListCell<ListEntry> {
    @Override
    protected void updateItem(ListEntry item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            int index = this.getIndex();

            RowGridPane grid = new RowGridPane(item.getFileDetail());
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(0, 10, 0, 10));

            RowLabel count = item.getRowCount();
            count.setText(index + 1 + "");
            count.setMinWidth(Dimensions.LISTVIEW_ROWCOUNT);

            grid.add(count, 0, 0, 1, 1);
            item.refresh();
            grid.add(item.getRowImageView(), 1, 0);
            grid.add(item.getRowNamelabel(), 2, 0, 3, 1);
            if (item.getFileDetail().getFileType() == FileType.FILE) {
                grid.add(item.getShare(), 5, 0);
            }
            grid.setAlignment(Pos.BASELINE_LEFT);
            setGraphic(grid);
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getStyleClass().add(".context-menu");
            this.setContextMenu(contextMenu);
            MenuItem menuItem1 = new MenuItem("Change tags");
            menuItem1.getStyleClass().add("menu-item");
            menuItem1.setOnAction(e -> {
                System.out.println("context click : " + item.toString());
                CommonData.instance.showAddTagToFileDialog(item);

            });
            if (item.getFileDetail().getFileType() == FileType.DIRECTORY) {
                MenuItem menuItem2 = new MenuItem("Open in Terminal");
                menuItem2.getStyleClass().add("menu-item");
                menuItem2.setOnAction(e -> {
                    System.out.println("context click : " + item.toString());
                    SystemCommands.OpenDirInTerminal(item.getFileDetail().getFilePath());

                });
                contextMenu.getItems().add(menuItem2);
            }
            MenuItem menuItem3 = new MenuItem("Delete");
            menuItem3.getStyleClass().add("menu-item");
            menuItem3.setOnAction(e -> {
                CommonData.instance.deleteView(item.getGridView());
                if (item.getFileDetail().getFileType() == FileType.DIRECTORY) {
                    SystemCommands.deleteFolder(item.getFileDetail().getFilePath());
                } else {
                    SystemCommands.deleteFile(item.getFileDetail().getFilePath());
                }
            });
            contextMenu.getItems().addAll(menuItem1, menuItem3);

            this.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (event.getClickCount() == 2) {
                        try {
                            doubleClick(item.getFileDetail());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (CommonData.instance != null) {
                            CommonData.instance.showPreview(item.getFileDetail());
                        } else {
                            LogsPrinter.printLogic("UpdateCellFactory", 47, "controller instance is null");
                        }
                    }
                }
            });
        }
    }
}
