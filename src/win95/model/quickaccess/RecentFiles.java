package win95.model.quickaccess;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import win95.debug.LogsPrinter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import static win95.constants.Dimensions.RECENT_QUEUE_CAPACITY;

public class RecentFiles {
    private final static Deque<String> recentQueue = new ArrayDeque<>();

    public static void fetchRecent() {
        JSONParser jsonParser = new JSONParser();
        File USER_HOME = new File(System.getProperty("user.home"));
        String homeFolderPath = USER_HOME.getAbsolutePath() + "/.CosmoFileManager";
        File homeFolder = new File(homeFolderPath);
        if (homeFolder.exists() && homeFolder.isDirectory()) {
            String recentFilePath = homeFolder + "/recent.json";
            File recentFile = new File(recentFilePath);
            if (recentFile.exists() && recentFile.isFile()) {
                try (FileReader reader = new FileReader(recentFilePath)) {
                    Object recentFileListObject = jsonParser.parse(reader);

                    JSONArray recentFileList = (JSONArray) recentFileListObject;
                    System.out.println(recentFileList);

                    for(Object jsonObject : recentFileList){
                        addRecentQueue((JSONObject) jsonObject);
                    }

                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    if (recentFile.createNewFile()) {
                        LogsPrinter.printLogic("RecentFiles", 42,
                                "Successfully crated recent.json");
                    } else {
                        LogsPrinter.printLogic("RecentFiles", 42,
                                "return false in crating recent.json");
                    }
                } catch (IOException e) {
                    LogsPrinter.printError("RecentFiles", 42,
                            "IOException in crating recent.json");
                }
            }
        } else {
            if (homeFolder.mkdir()) {
                String recentFilePath = homeFolder + "/recent.json";
                File recentFile = new File(recentFilePath);
                LogsPrinter.printLogic("RecentFiles", 52,
                        "Successfully crated home folder");
                try {
                    if (recentFile.createNewFile()) {
                        LogsPrinter.printLogic("RecentFiles", 52,
                                "Successfully crated recent.json");
                    } else {
                        LogsPrinter.printLogic("RecentFiles", 52,
                                "return false in crating recent.json");
                    }
                } catch (IOException e) {
                    LogsPrinter.printError("RecentFiles", 52,
                            "IOException in crating recent.json");
                }
            } else {
                LogsPrinter.printError("RecentFiles", 52,
                        "some error occur in crating home folder");
            }
        }
    }

    public static void addRecentQueue(String path) {
        if (recentQueue.size() == RECENT_QUEUE_CAPACITY) {
            recentQueue.removeLast();
        }
        recentQueue.addFirst(path);
    }

    public static void addRecentQueue(JSONObject jsonObject) {
        if (recentQueue.size() == RECENT_QUEUE_CAPACITY) {
            recentQueue.removeLast();
        }
        String path = (String) jsonObject.get("path");
        addRecentQueue(path);
        System.out.println(RecentFiles.printQueue());
    }

    public static void saveRecent(){
        String recentFilePath = new File(System.getProperty("user.home")).getAbsolutePath() +
                                "/.CosmoFileManager/recent.json";

        JSONArray recentFileArray = new JSONArray();
        for(String path : recentQueue){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("path",path);
            recentFileArray.add(jsonObject);
        }

        try (FileWriter file = new FileWriter(recentFilePath)) {
            file.write(recentFileArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Deque<String> getRecentQueue(){
        return recentQueue;
    }
    public static String printQueue() {
        StringBuilder res = new StringBuilder();
        for(String path : recentQueue){
            res.append(path).append('\n');
        }
        return res.toString();
    }
}
