package win95.constants;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import win95.debug.LogsPrinter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserPreference {
    public static Themes THEME = Themes.LIGHT;
    public static String VIEW_MODE;
    public static void fetchUserPreferences(){
        JSONParser jsonParser = new JSONParser();

        File USER_HOME = new File(System.getProperty("user.home"));
        String homeFolderPath = USER_HOME.getAbsolutePath() + "/.CosmoFileManager";
        File homeFolder = new File(homeFolderPath);

        if (homeFolder.exists() && homeFolder.isDirectory()) {

            String userPreferencesPath = homeFolder + "/UserPreferences.json";
            File userPreferencesFile = new File(userPreferencesPath);

            if (userPreferencesFile.exists() && userPreferencesFile.isFile()) {
                try (FileReader reader = new FileReader(userPreferencesFile)) {

                    Object userPreferences = jsonParser.parse(reader);
                    setUserPreferences((JSONObject) userPreferences);

                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    if (userPreferencesFile.createNewFile()) {
                        LogsPrinter.printLogic("userPreferencesFile", 42,
                                "Successfully crated userPreferences.json");
                    } else {
                        LogsPrinter.printLogic("userPreferencesFile", 42,
                                "return false in crating userPreferences.json");
                    }
                } catch (IOException e) {
                    LogsPrinter.printError("userPreferencesFile", 42,
                            "IOException in crating userPreferences.json");
                }
            }
        } else {
            if (homeFolder.mkdir()) {
                String userPreferencesPath = homeFolder + "/UserPreferences.json";
                File userPreferencesPathFile = new File(userPreferencesPath);
                LogsPrinter.printLogic("UserPreferences", 52,
                        "Successfully crated home folder");
                try {
                    if (userPreferencesPathFile.createNewFile()) {
                        LogsPrinter.printLogic("UserPreferences", 52,
                                "Successfully crated UserPreferences.json");
                    } else {
                        LogsPrinter.printLogic("UserPreferences", 52,
                                "return false in crating UserPreferences.json");
                    }
                } catch (IOException e) {
                    LogsPrinter.printError("UserPreferences", 52,
                            "IOException in crating UserPreferences.json");
                }
            } else {
                LogsPrinter.printError("UserPreferences", 52,
                        "some error occur in crating home folder");
            }
        }

    }
    public static void saveUserPreferences(){
        String userPreferencesPath = new File(System.getProperty("user.home")).getAbsolutePath() +
                "/.CosmoFileManager/UserPreferences.json";

        JSONObject userPreferences = new JSONObject();
        userPreferences.put("theme",THEME.toString());
        userPreferences.put("viewmode",CommonData.VIEW_MODE);

        try (FileWriter file = new FileWriter(userPreferencesPath)) {
            file.write(userPreferences.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void setUserPreferences(JSONObject jsonObject) {
        String theme = (String) jsonObject.get("theme");
        String view_mode = (String) jsonObject.get("viewmode");
        if(theme.equals("LIGHT")) THEME = Themes.LIGHT;
        else if(theme.equals("DARK")) THEME = Themes.DARK;
        else THEME = Themes.FADE;
        VIEW_MODE = view_mode;

    }

    public static Themes getTHEME() {
        return THEME;
    }
    public static String getViewMode() {
        return VIEW_MODE;
    }
    public static void setTHEME(Themes THEME) {
        UserPreference.THEME = THEME;
    }
    public static void setViewMode(String viewMode) {
        VIEW_MODE = viewMode;
    }
}
