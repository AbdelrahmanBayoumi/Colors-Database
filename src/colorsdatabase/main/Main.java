package colorsdatabase.main;

import colorsdatabase.utilities.Logger;
import static colorsdatabase.utilities.Utility.setSceneStyleSheet;
import static colorsdatabase.utilities.Utility.setStageIcon;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Main Class for the program
 *
 * @author Abdelrahman Bayoumi
 */
public class Main extends Application {

    private static final String CLASS_NAME = Main.class.getName();

    @Override
    public void start(Stage stage) {
        try {
            // load Homepage
            Parent root = FXMLLoader.load(getClass().getResource("/colorsdatabase/homepage/Homepage.fxml"));
            Scene scene = new Scene(root);
            setSceneStyleSheet(scene, "/colorsdatabase/resources/style.css");
            stage.setScene(scene);
            stage.setTitle("Colors Database");
            setStageIcon(stage, "/colorsdatabase/resources/palette.png");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (Exception e) {
            Logger.log(Logger.Level.ERROR, "Exception[" + e + "] in " + CLASS_NAME + ".start(Stage:" + stage + ")");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // init logger
            Logger.init();
            // Log StartTime
            Logger.log(Logger.Level.INFO, "ColorsDatabase launched");
            // JavaFX
            launch(args);
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".main()");
        }

    }
}
