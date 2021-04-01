package win95.utilities.filehandling.os.windows;

public class WindowCommand {
    public static String getOpenInTerminalCommand(String path){
        return "cmd.exe /c cd \""+path+"\" & start cmd.exe /k ";
    }
}
