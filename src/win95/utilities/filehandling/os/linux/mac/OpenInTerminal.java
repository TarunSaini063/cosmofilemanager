package win95.utilities.filehandling.os.linux.mac;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OpenInTerminal {
    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
    public static void OpenDirInTerminal(String path){
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("open -a Terminal "+path);
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
