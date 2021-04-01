package win95.utilities.filehandling.os;

import win95.constants.CommonData;
import win95.constants.LogicConstants;
import win95.utilities.filehandling.os.linux.mac.MacCommand;
import win95.utilities.filehandling.os.windows.WindowCommand;
import win95.utilities.pathmanipulation.PathHandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemCommands {
    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
    public static void OpenDirInTerminal(String path){
        path = new PathHandling(path).getFixedPath();
        Process process = null;
        try {
            if(CommonData.OS == LogicConstants.OS.MAC) {
                process = Runtime.getRuntime().exec(MacCommand.getOpenInTerminalCommand(path));
            }else {
                process = Runtime.getRuntime().exec(WindowCommand.getOpenInTerminalCommand(path));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if(process!=null)
                printResults(process);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
