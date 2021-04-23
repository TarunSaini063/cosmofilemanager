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

    public static void executeCommand(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (process != null)
                printResults(process);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void OpenDirInTerminal(String path) {
        path = new PathHandling(path).getFixedPath();

        if (CommonData.OS == LogicConstants.OS.MAC) {
            executeCommand(MacCommand.getOpenInTerminalCommand(path));
        } else {
            executeCommand(WindowCommand.getOpenInTerminalCommand(path));
        }

    }

    public static void createFile(String path) {
        path = new PathHandling(path).getFixedPath();

        if (CommonData.OS == LogicConstants.OS.MAC) {
            executeCommand(MacCommand.CreateNewFile(path));
        } else {
            executeCommand(WindowCommand.CreateNewFile(path));
        }
    }

    public static void createFolder(String path) {
        path = new PathHandling(path).getFixedPath();

        if (CommonData.OS == LogicConstants.OS.MAC) {
            executeCommand(MacCommand.CreateNewFolder(path));
        } else {
            executeCommand(WindowCommand.CreateNewFolder(path));
        }
    }

    public static void deleteFolder(String path) {
        path = new PathHandling(path).getFixedPath();

        if (CommonData.OS == LogicConstants.OS.MAC) {
            executeCommand(MacCommand.DeleteNewFolder(path));
        } else {
            executeCommand(WindowCommand.DeleteNewFolder(path));
        }
    }

    public static void deleteFile(String path) {
        path = new PathHandling(path).getFixedPath();

        if (CommonData.OS == LogicConstants.OS.MAC) {
            executeCommand(MacCommand.DeleteNewFile(path));
        } else {
            executeCommand(WindowCommand.DeleteNewFile(path));
        }
    }
}
