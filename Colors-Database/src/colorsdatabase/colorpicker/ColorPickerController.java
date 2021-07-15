package colorsdatabase.colorpicker;

import colorsdatabase.datamodels.ColorBox;
import colorsdatabase.utilities.Logger;
import com.jfoenix.controls.JFXColorPicker;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
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
public class ColorPickerController implements Initializable {

    // ===== Helper Objects =====
    private static final String CLASS_NAME = ColorPickerController.class.getName();

    @FXML
    private JFXColorPicker color_picker;
    @FXML
    private TextField color_Textfield;
    @FXML
    private TextField red_Textfield;
    @FXML
    private TextField green_Textfield;
    @FXML
    private TextField blue_Textfield;
    @FXML
    private AnchorPane AP;
    public static AnchorPane AP_pointer;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        color_picker.setOnAction(colorListener(0));
        color_Textfield.setOnAction(colorListener(1));
        AP_pointer = AP;
    }

    private EventHandler<ActionEvent> colorListener(int flag) {
        EventHandler<ActionEvent> ev = (ActionEvent event) -> {
            try {
                if (flag == 0) {
                    color_Textfield.setText(ColorBox.toRGBCode(color_picker.getValue()));
                    int[] rgb = getRGB(ColorBox.toRGBCode(color_picker.getValue()));
                    red_Textfield.setText(rgb[0] + "");
                    green_Textfield.setText(rgb[1] + "");
                    blue_Textfield.setText(rgb[2] + "");
                    AP.setStyle("-fx-background-color:" + ColorBox.toRGBCode(color_picker.getValue()) + ";");
                } else {
                    int[] rgb = getRGB(color_Textfield.getText());
                    red_Textfield.setText(rgb[0] + "");
                    green_Textfield.setText(rgb[1] + "");
                    blue_Textfield.setText(rgb[2] + "");
                    AP.setStyle("-fx-background-color:" + color_Textfield.getText() + ";");
                }
            } catch (Exception ex) {
                Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".colorListener(color_picker:" + color_picker  + ")");
            }
        };
        return ev;
    }

    @FXML
    private void copyHex(ActionEvent event) {
        copyToClipboard(ColorBox.toRGBCode(Color.web(color_Textfield.getText())), 0);
    }

    @FXML
    private void copyRed(ActionEvent event) {
        copyToClipboard(red_Textfield.getText(), 1);
    }

    @FXML
    private void copyGreen(ActionEvent event) {
        copyToClipboard(green_Textfield.getText(), 1);
    }

    @FXML
    private void copyBlue(ActionEvent event) {
        copyToClipboard(blue_Textfield.getText(), 1);
    }

    private static void copyToClipboard(String color, int flag) {

        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            TrayNotification tray = new TrayNotification();
            tray.setAnimationType(AnimationType.POPUP);
            tray.setNotificationType(NotificationType.NOTICE);
            Image img = new Image("/colorsdatabase/resources/clipboard.png");
            tray.setImage(img);
            tray.showAndDismiss(Duration.millis(1000));

            if (flag == 0) {
                // Hex
                clipboard.setContents(new StringSelection(color), null);
                tray.setTitle("Copied To Clipboard");
                tray.setMessage("HEX COLOR: " + color);
                tray.setRectangleFill(Paint.valueOf(color));
            } else {
                clipboard.setContents(new StringSelection(color), null);
                tray.setTitle("Copied To Clipboard");
                tray.setMessage("COLOR: " + color);
            }

        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".copyToClipboard(color:" + color + ", flag" + flag + ")");
        }
    }

    public static int[] getRGB(String rgb) {
        rgb = rgb.replace("#", "");
        final int[] ret = new int[3];
        for (int i = 0; i < 3; i++) {
            ret[i] = Integer.parseInt(rgb.substring(i * 2, i * 2 + 2), 16);
        }
        return ret;
    }
}
