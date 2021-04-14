package win95;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import win95.constants.CommonData;
import win95.constants.LogicConstants;
import win95.constants.Themes;
import win95.constants.UserPreference;
import win95.model.quickaccess.RecentFiles;
import win95.model.quickaccess.TaggedFiles;
import win95.model.wirelessTransfer.terminate.PublicThreads;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        UserPreference.fetchUserPreferences();
        TaggedFiles.fetchTagged();
        RecentFiles.fetchRecent();
        Parent root = FXMLLoader.load(getClass().getResource("./view/sample.fxml"));
        primaryStage.setTitle("BIT-COSMOS");
        Scene scene = new Scene(root);
        if(UserPreference.THEME == Themes.DARK) {
            scene.getStylesheets().add(getClass().getResource("./view/css/DarkStyle.css").toExternalForm());
        }else if(UserPreference.THEME == Themes.LIGHT){
            scene.getStylesheets().add(getClass().getResource("./view/css/LightStyle.css").toExternalForm());
        }else{
            /*  set FADE theme not Theme implemented */
            scene.getStylesheets().add(getClass().getResource("./view/css/LightStyle.css").toExternalForm());
        }
        primaryStage.setOnHidden(e -> Platform.exit());
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {

        /*
         *
         * Read data from user folder created/updated at last execution
         * not yet implement....
         * create separate module
         *
         */

        if (CommonData.OS == LogicConstants.OS.UNKNOWN) {

            String OS = System.getProperty("os.name", "generic").toLowerCase();

            if ((OS.contains("mac")) || (OS.contains("darwin"))) {
                CommonData.OS = LogicConstants.OS.MAC;
            } else if (OS.contains("win")) {
                CommonData.OS = LogicConstants.OS.WINDOW;
            } else if (OS.contains("nux")) {
                CommonData.OS = LogicConstants.OS.LINUX;
            }
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("In shutdown hook");
            RecentFiles.saveRecent();
            TaggedFiles.saveTagged();
            UserPreference.saveUserPreferences();
            PublicThreads.clearThreads();
            System.exit(0);
        }, "Shutdown-thread"));
        launch(args);
    }
}
