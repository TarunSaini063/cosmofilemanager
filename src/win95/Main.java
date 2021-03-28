package win95;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import win95.constants.CommonData;
import win95.constants.LogicConstants;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("./view/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
//        primaryStage.setFullScreen(true);
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

        launch(args);
    }
}
