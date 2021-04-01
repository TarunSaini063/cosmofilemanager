package win95.utilities.filehandling.os.linux.mac;

public class MacCommand {
    public static String getOpenInTerminalCommand(String path){
        System.out.println("open -a Terminal "+path);
        return "open -a Terminal "+path;
    }
}
