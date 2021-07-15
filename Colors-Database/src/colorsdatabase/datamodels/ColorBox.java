package colorsdatabase.datamodels;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ColorBox extends VBox {

    private VBox box;
    private JFXButton variable;
    private JFXButton color_item;
    private FontAwesomeIconView icon;
    private HBox hbox;

    public ColorBox(ColorModel colorModel) {
        initBox(colorModel);
    }

    private void initBox(ColorModel colorModel) {
        box = this;

        icon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        icon.setSize("20");
        icon.getStyleClass().add("delete-icon");
        hbox = new HBox(icon);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPrefSize(160, 21);

        variable = new JFXButton(colorModel.getVariable_name());
        variable.setAlignment(Pos.CENTER);
        variable.getStyleClass().add("variable-name");
        variable.setPrefSize(160, 31);
        variable.setFocusTraversable(false);

        color_item = new JFXButton(colorModel.getHex_color());
        color_item.setPrefSize(160, 129);
        color_item.setAlignment(Pos.CENTER);
        color_item.setStyle("-fx-background-color: " + color_item.getText() + ";"
                + "-fx-text-fill: " + toRGBCode(Color.web(color_item.getText()).invert()) + ";"
                + "-fx-font-weight: BOLD;");
        color_item.setFocusTraversable(false);

        box.getChildren().addAll(hbox, variable, color_item);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);
        box.setPrefSize(200, 204);
        box.getStyleClass().add("color-box");
    }

    public VBox getBox() {
        return box;
    }

    public JFXButton getVariable() {
        return variable;
    }

    public JFXButton getColor_item() {
        return color_item;
    }

    public FontAwesomeIconView getIcon() {
        return icon;
    }

    public static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
