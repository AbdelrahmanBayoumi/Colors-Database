package colorsdatabase.homepage;

import colorsdatabase.colorpicker.ColorPickerController;
import com.jfoenix.controls.JFXDialog;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import colorsdatabase.utilities.Logger;
import com.jfoenix.controls.JFXButton;

/**
 * FXML Controller class
 *
 * @author Abdelrahman Bayoumi
 */
public class HomepageController implements Initializable {

    // ===== Helper Objects =====
    private static final String CLASS_NAME = HomepageController.class.getName();

    // ===== Database =====
    public static Connection con = null;
    public static ResultSet result = null;
    public static PreparedStatement stat = null;

    // ===== GUI Objects =====
    public static Parent DefaultCenter_root;
    public static Parent ColorPicker_root;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private BorderPane BorderPane_instance;
    public static BorderPane BorderPane_Static;

    @FXML
    private Label num_projects;
    public static Label num_projects_pointer;

    @FXML
    StackPane AP;
    public static JFXDialog aboutDialog;
    @FXML
    private JFXButton Projects_BTN;
    @FXML
    private JFXButton ColorPicker_BTN;
    private JFXButton currentBtn;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            currentBtn = Projects_BTN;
            // BorderPane
            DefaultCenter_root = FXMLLoader.load(getClass().getResource("/colorsdatabase/homepage/DefaultCenter.fxml"));
            ColorPicker_root = FXMLLoader.load(getClass().getResource("/colorsdatabase/colorpicker/ColorPicker.fxml"));
            BorderPane_instance.setCenter(DefaultCenter_root);
            BorderPane_Static = BorderPane_instance;
            //number of projects
            num_projects_pointer = num_projects;
            num_projects.setText(getProjectsNumber());
            // about 
            AnchorPane aboutPane = FXMLLoader.load(getClass().getResource("/colorsdatabase/about/About.fxml"));
            aboutDialog = new JFXDialog(AP, aboutPane, JFXDialog.DialogTransition.TOP);
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".initialize()");
        }

    }

    @FXML
    public void RootMousePressed(Event event) {
        MouseEvent e = (MouseEvent) event;
        xOffset = e.getSceneX();
        yOffset = e.getSceneY();
    }

    @FXML
    public void RootMouseDragged(Event event) {
        if (isMaximized(event)) {
            return;
        }
        MouseEvent e = (MouseEvent) event;
        ((Stage) (((Node) (event.getSource())).getScene().getWindow())).setX(e.getScreenX() - xOffset);
        ((Stage) (((Node) (event.getSource())).getScene().getWindow())).setY(e.getScreenY() - yOffset);
    }

    @FXML
    public void closeWindow(Event event) {
        // Log ExitTime
        Logger.log(Logger.Level.INFO, "ColorsDatabase closed");
        System.exit(0);
    }

    @FXML
    public void MinWindow(Event event) {
        Stage s = ((Stage) (((Node) (event.getSource())).getScene().getWindow()));
        s.setIconified(true);
    }

    @FXML
    public void MaxWindow(Event event) {
        Stage s = ((Stage) (((Node) (event.getSource())).getScene().getWindow()));
        int w = 900;
        int h = 700;
        if (isMaximized(event)) {
            s.setWidth(w);
            s.setHeight(h);
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            s.setX(bounds.getWidth() / 2 - (w / 2));
            s.setY(bounds.getHeight() / 2 - (h / 2));
        } else {
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            s.setWidth(bounds.getWidth());
            s.setHeight(bounds.getHeight());
            s.setX(0);
            s.setY(0);
        }
    }

    public boolean isMaximized(Event event) {
        Stage s = ((Stage) (((Node) (event.getSource())).getScene().getWindow()));
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        return s.getWidth() == bounds.getWidth() && s.getHeight() == bounds.getHeight();
    }

    @FXML
    private void aboutAction(Event event) {
        if (aboutDialog.isVisible()) {
            return;
        }
        aboutDialog.show();
    }

    /**
     *
     * @return number of projects in database
     */
    public static String getProjectsNumber() {
        String s = "";
        try {
            stat = con.prepareStatement("SELECT COUNT(project_id) FROM project");
            result = stat.executeQuery();
            s += result.getInt(1);
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".getProjectsNumber()");
        }
        return s;
    }

    @FXML
    private void goToProjectsAction(ActionEvent event) {
        try {
            DefaultCenterController.DoCheck();
            toggleBtn(Projects_BTN);
            colorsdatabase.homepage.HomepageController.BorderPane_Static.setCenter(DefaultCenter_root);
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".goToProjectsAction()");
        }
    }

    @FXML
    private void goToColorPickerAction(ActionEvent event) {
        try {
            DefaultCenterController.DoCheck();
            toggleBtn(ColorPicker_BTN);
            ColorPickerController.AP_pointer.setStyle("-fx-background-color: #f4f4f4 ;");
            colorsdatabase.homepage.HomepageController.BorderPane_Static.setCenter(ColorPicker_root);
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".goToColorPickerAction()");
        }
    }

    private void setSelected(JFXButton b) {
        b.setStyle("-fx-background-radius: 20 20 0 0;"
                + "    -fx-text-fill: black;"
                + "    -fx-font-size: 17;"
                + "    -fx-font-weight: BOLD;"
                + "    -fx-background-color: #F4F4F4;"
                + "    -fx-cursor: hand;"
                + "    -fx-alignment: CENTER;"
                + "    -fx-pref-height: 31;"
                + "    -fx-pref-width: 420;");
        b.getGraphic().setStyle("-fx-fill: black;");
    }

    private void delSelected(JFXButton b) {
        b.setStyle("-fx-background-radius: 20 20 0 0;"
                + "    -fx-text-fill: white;"
                + "    -fx-font-size: 17;"
                + "    -fx-font-weight: BOLD;"
                + "    -fx-background-color: #005E7C;"
                + "    -fx-cursor: hand;"
                + "    -fx-alignment: CENTER;"
                + "    -fx-pref-height: 31;"
                + "    -fx-pref-width: 420;");
        b.getGraphic().setStyle("-fx-fill: white;");
    }

    public void toggleBtn(JFXButton btn) {
        delSelected(currentBtn);
        currentBtn = btn;
        setSelected(currentBtn);
    }

}
