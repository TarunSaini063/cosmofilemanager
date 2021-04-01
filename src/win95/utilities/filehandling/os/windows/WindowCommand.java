package win95.utilities.filehandling.os.windows;

public class WindowCommand {
    public static String getOpenInTerminalCommand(String path){
        return "cmd.exe /c cd \""+path+"\" & start cmd.exe /k ";
    }

    public static String CreateNewFile(String path) {
        return "null >"+path;
    }

    public static String CreateNewFolder(String path) {
        return "mkdir "+path;
    }

    public static String DeleteNewFolder(String path) {
        return "RS /S "+path;
    }

    public static String DeleteNewFile(String path) {
        return "DEL /F /A "+path;
    }
}
