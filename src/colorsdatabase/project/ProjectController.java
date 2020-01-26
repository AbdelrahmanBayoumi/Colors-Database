package colorsdatabase.project;

import colorsdatabase.datamodels.ColorBox;
import colorsdatabase.datamodels.ColorModel;
import com.jfoenix.controls.JFXColorPicker;
import colorsdatabase.utilities.Logger;
import static colorsdatabase.homepage.HomepageController.DefaultCenter_root;
import static colorsdatabase.homepage.HomepageController.stat;
import static colorsdatabase.homepage.HomepageController.con;
import static colorsdatabase.project.ProjectController.getProjectID;
import static colorsdatabase.homepage.HomepageController.result;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author Abdelrahman Bayoumi
 */
public class ProjectController implements Initializable {
    
    // ===== Helper Objects =====
    private static final String CLASS_NAME = ProjectController.class.getName();

    @FXML
    private FlowPane flowpane_instance;
    public static FlowPane flowpane_project;
    @FXML
    private Label projectName;
    public static Label projectName_pointer;
    @FXML
    private TextField variable_name;
    @FXML
    private JFXColorPicker color_picker;
    @FXML
    private TextField color_Textfield;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            projectName_pointer = projectName;
            flowpane_project = flowpane_instance;
            color_picker.setOnAction(colorListener());
            flowpane_instance.getChildren().clear();
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".initialize()");
        }
    }

    public static ObservableList<VBox> initColors(int projectid) {
        ObservableList<VBox> listOFVboxs = FXCollections.observableArrayList();
        try {
            ArrayList<String> resultList = new ArrayList<>();
            String sql = "SELECT variable_name, hex_color From color where project_id = " + projectid;
            result = con.prepareStatement(sql).executeQuery();
            while (result.next()) {
                resultList.add(result.getString(1));
                resultList.add(result.getString(2));
            }
            for (int i = 0; i < resultList.size(); i++) {
                ColorModel colorModel = new ColorModel(resultList.get(i),
                        resultList.get(++i), projectid);

                ColorBox colorBox = new ColorBox(colorModel);
                colorBox.getColor_item().setOnAction(copyToClipboard(colorBox.getVariable().getText(), colorBox.getColor_item().getText(), 1));
                colorBox.getVariable().setOnAction(copyToClipboard(colorBox.getVariable().getText(), colorBox.getColor_item().getText(), 0));
                colorBox.getIcon().setOnMouseClicked(deleteColor(colorBox, colorModel));
                listOFVboxs.add(colorBox);
            }
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".initColors()");
        }
        return listOFVboxs;
    }

    private EventHandler<ActionEvent> colorListener() {
        EventHandler<ActionEvent> ev = (ActionEvent event) -> {
            try {
                color_Textfield.setText(ColorBox.toRGBCode(color_picker.getValue()));
            } catch (Exception ex) {
                Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".colorListener(color_picker:" + color_picker + ")");
            }
        };
        return ev;
    }

    @FXML
    private void backAction(ActionEvent event) {
        try {
            colorsdatabase.homepage.HomepageController.BorderPane_Static.setCenter(DefaultCenter_root);
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".backAction()");
        }
    }

    @FXML
    private void add(ActionEvent event) {
        try {
            if (!variable_name.getText().trim().equals("")) {
                int projectid = getProjectID(projectName.getText());
                ColorModel colorModel = new ColorModel(variable_name.getText().trim(),
                        color_Textfield.getText().trim(), projectid);
                if (addColor(colorModel)) {
                    ColorBox colorBox = new ColorBox(colorModel);
                    colorBox.getColor_item().setOnAction(copyToClipboard(colorBox.getVariable().getText(), colorBox.getColor_item().getText(), 1));
                    colorBox.getVariable().setOnAction(copyToClipboard(colorBox.getVariable().getText(), colorBox.getColor_item().getText(), 0));
                    colorBox.getIcon().setOnMouseClicked(deleteColor(colorBox, colorModel));
                    flowpane_instance.getChildren().add(colorBox);
                    variable_name.setText("");
                }
            }
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".add()");
        }
    }

    public boolean addColor(ColorModel colorModel) {
        String sqlString = "INSERT INTO color (project_id, variable_name, hex_color) values (?,?,?)";
        try {
            PreparedStatement prepareStatement = con.prepareStatement(sqlString);
            prepareStatement.setInt(1, colorModel.getProject_id());
            prepareStatement.setString(2, colorModel.getVariable_name());
            prepareStatement.setString(3, colorModel.getHex_color());
            prepareStatement.execute();
            return true;
        } catch (SQLException ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".addColor(ColorModel:" + colorModel + ")");
            return false;
        }
    }

    public static int getProjectID(String project_name) {
        int x = -1;
        try {
            stat = con.prepareStatement(
                    "SELECT project_id FROM PROJECT WHERE project_name = '" + project_name + "'");
            result = stat.executeQuery();
            while (result.next()) {
                x = result.getInt(1);
            }
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".getProjectID(project_name:" + project_name + ")");
        }
        return x;
    }

    private static EventHandler<Event> deleteColor(VBox vbox, ColorModel colorModel) {
        EventHandler<Event> ev = (Event event) -> {
            try {
                String sqlString = "DELETE FROM color WHERE  project_id ="
                        + colorModel.getProject_id() + " AND  variable_name = '"
                        + colorModel.getVariable_name() + "'";
                PreparedStatement prepareStatement = con.prepareStatement(sqlString);
                prepareStatement.executeUpdate();
                flowpane_project.getChildren().remove(vbox);
            } catch (Exception ex) {
                Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".deleteColor(colorModel:" + colorModel + ")");
            }
        };
        return ev;
    }

    private static EventHandler<ActionEvent> copyToClipboard(String var_name, String color, int flag) {
        EventHandler<ActionEvent> ev = (ActionEvent event) -> {
            try {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                TrayNotification tray = new TrayNotification();
                tray.setAnimationType(AnimationType.POPUP);
                tray.setNotificationType(NotificationType.NOTICE);
                Image img = new Image("/colorsdatabase/resources/clipboard.png");
                tray.setImage(img);
                tray.showAndDismiss(Duration.millis(1000));

                if (flag == 0) {
                    // var
                    clipboard.setContents(new StringSelection(var_name), null);
                    tray.setTitle("Copied To Clipboard");
                    tray.setMessage("variable-name: " + var_name);
                } else {
                    // color
                    clipboard.setContents(new StringSelection(color), null);
                    tray.setTitle("Copied To Clipboard");
                    tray.setMessage("HEX COLOR: " + color);
                    tray.setRectangleFill(Paint.valueOf(color));
                }
            } catch (Exception ex) {
                Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".copyToClipboard(var_name:" + var_name + ", color:" + color + ", flag" + flag + ")");
            }
        };
        return ev;
    }

}
