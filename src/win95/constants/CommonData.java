package win95.constants;

import javafx.scene.Scene;
import win95.controller.Controller;
import win95.controller.SetTagOptionDialogs;
import win95.model.FileDetail;

public class CommonData {
    public static  FileType CURRENT_LIST_VIEW_ITEM = FileType.UNKNOWN;
    public static Controller instance = null;
    public static Scene CONTROLLER_SCENE = null;
    public static LogicConstants.OS OS = LogicConstants.OS.UNKNOWN;
    public static FileDetail CURRENT_DIRECTORY = null;
    public static SetTagOptionDialogs TAG_OPTION_INSTANCE = null;
    public static String  VIEW_MODE = "LISTVIEW";
}
