package colorsdatabase.datamodels;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class ProjectBox extends AnchorPane {

    private FontAwesomeIconView trash;
    private FontAwesomeIconView edit;
    private FontAwesomeIconView check;
    private AnchorPane Box;
    private Pane pane;
    private TextField textField;
    Project project;

    public ProjectBox(int id, String project_name) {
        project = new Project(id, project_name);
        initBox(project_name);
    }

    private AnchorPane initBox(String project_name) {
        Box = this;
        Box.getStyleClass().add("project-boxview");
        Box.setPrefSize(827, 66);

        trash = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        trash.setSize("25");
        trash.getStyleClass().add("delete-icon");
        AnchorPane.setTopAnchor(trash, 21.0);
        AnchorPane.setBottomAnchor(trash, 20.0);
        AnchorPane.setRightAnchor(trash, 15.0);

        edit = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
        edit.setSize("25");
        edit.getStyleClass().add("delete-icon");
        AnchorPane.setTopAnchor(edit, 21.0);
        AnchorPane.setBottomAnchor(edit, 20.0);
        AnchorPane.setRightAnchor(edit, 45.0);

        check = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
        check.setSize("25");
        check.getStyleClass().add("check-icon");
        AnchorPane.setTopAnchor(check, 21.0);
        AnchorPane.setBottomAnchor(check, 20.0);
        AnchorPane.setRightAnchor(check, 80.0);
        check.setVisible(false);

        textField = new TextField(project_name);
        textField.setPrefSize(507, 31);
        AnchorPane.setTopAnchor(textField, 18.0);
        AnchorPane.setBottomAnchor(textField, 18.0);
        AnchorPane.setLeftAnchor(textField, 40.0);
        textField.getStyleClass().add("trans-field");

        pane = new Pane();
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        Box.getChildren().addAll(pane, trash, edit, check, textField);

        return Box;
    }

    public FontAwesomeIconView getTrash() {
        return trash;
    }

    public FontAwesomeIconView getEdit() {
        return edit;
    }

    public FontAwesomeIconView getCheck() {
        return check;
    }

    public AnchorPane getBox() {
        return Box;
    }

    public Pane getPane() {
        return pane;
    }

    public TextField getTextField() {
        return textField;
    }

    public Project getProject() {
        return project;
    }

}
