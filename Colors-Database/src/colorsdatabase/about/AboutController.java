package colorsdatabase.about;

import colorsdatabase.colorpicker.ColorPickerController;
import colorsdatabase.utilities.Logger;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Abdelrahman Bayoumi
 */
public class AboutController implements Initializable {

    // ===== Helper Objects =====
    private static final String CLASS_NAME = ColorPickerController.class.getName();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void okAction(ActionEvent event) {
        colorsdatabase.homepage.HomepageController.aboutDialog.close();
    }

    @FXML
    private void mailAction(MouseEvent event) {
        try {
            Desktop desktop;
            if (Desktop.isDesktopSupported()
                    && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
                URI mailto = new URI("mailto:abdelrahmanbayoumi1@gmail.com?");
                desktop.mail(mailto);
            } else {
                // TODO fallback to some Runtime.exec(..) voodoo?
                throw new RuntimeException("Desktop doesn't support mailto; mail is dead anyway;)");
            }
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".mailAction()");
        }
    }

    @FXML
    private void githubAction(MouseEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/AbdelrahmanBayoumi"));
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".githubAction()");
        }
    }
}
