package colorsdatabase.utilities;

import colorsdatabase.main.Main;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Utility {

    //========= Helper Objects =========
    private static final String CLASS_NAME = Main.class.getName();
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("[dd-MM-yyyy] [hh:mm:ss a]");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    public static String formatDateTimeString(Long time) {
        return DATE_TIME_FORMAT.format(new Date(time));
    }

    public static String formatDateTimeString(Date date) {
        return DATE_TIME_FORMAT.format(date);
    }

    public static String getDateString(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static String getProgramPath() throws IOException {
        String currentdir = System.getProperty("user.dir");
        currentdir = currentdir.replace("\\", "/");
        return currentdir;
    }

    public static boolean createDirectory(String DirectoryName) {
        try {
            String d = getProgramPath() + "/" + DirectoryName + "/";
            File dir = new File(d);//The name of the directory to create                                                                                      
            //Creates the directory                   
            return dir.mkdir();
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".createDirectory()");
            return false;
        }
    }

    public static void setStageIcon(Stage stage, String img_path) {
        stage.getIcons().clear();
        stage.getIcons().add(new Image(img_path));
    }

    public static void setSceneStyleSheet(Scene scene, String sheet_path) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(sheet_path);
    }
}
