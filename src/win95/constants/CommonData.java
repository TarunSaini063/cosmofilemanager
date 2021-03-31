package win95.constants;

import win95.controller.Controller;
import win95.controller.SetTagOptionDialogs;
import win95.model.FileDetail;

public class CommonData {
    public static Controller instance = null;
    public static LogicConstants.OS OS = LogicConstants.OS.UNKNOWN;
    public static FileDetail CURRENT_DIRECTORY = null;
    public static SetTagOptionDialogs TAG_OPTION_INSTANCE = null;
}
