package win95.utilities.filehandling.os.linux.mac;

public class MacCommand {
    public static String getOpenInTerminalCommand(String path) {
        return "open -a Terminal " + path;
    }

    public static String CreateNewFile(String path) {
        return "touch " + path;
    }

    public static String CreateNewFolder(String path) {
        return "mkdir " + path;
    }

    public static String DeleteNewFolder(String path) {
        return "rm -r " + path;
    }

    public static String DeleteNewFile(String path) {
        return "rm " + path;
    }
}
