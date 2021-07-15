package colorsdatabase.homepage;

import colorsdatabase.datamodels.Project;
import colorsdatabase.datamodels.ProjectBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import static colorsdatabase.homepage.HomepageController.con;
import static colorsdatabase.homepage.HomepageController.getProjectsNumber;
import static colorsdatabase.project.ProjectController.getProjectID;
import static colorsdatabase.homepage.HomepageController.num_projects_pointer;
import static colorsdatabase.homepage.HomepageController.result;
import colorsdatabase.utilities.Logger;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import colorsdatabase.project.ProjectController;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;

/**
 * FXML Controller class
 *
 * @author Abdelrahman Bayoumi
 */
public class DefaultCenterController implements Initializable {

    // ===== Helper Objects =====
    private static final String CLASS_NAME = DefaultCenterController.class.getName();

    private static TextField edit_TextField;
    private static Project edit_Project;
    private static FontAwesomeIconView edit_check;
    private static FontAwesomeIconView edit_icon;

    // ===== GUI Objects =====
    public static Stage Project_stage;
    public static Parent Project_root;

    @FXML
    private FlowPane flowpane_instance;
    public static FlowPane flowpane_pointer;
    @FXML
    private TextField projectNameField;
    @FXML
    private ScrollPane sp;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            connectToDataBase();
            createTables();

            Project_root = FXMLLoader.load(getClass().getResource("/colorsdatabase/project/Project.fxml"));

            flowpane_pointer = flowpane_instance;
            flowpane_instance.getChildren().clear();
            flowpane_instance.getChildren().addAll(initProjects());
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".initialize()");
        }
    }

    private ObservableList<AnchorPane> initProjects() {
        ObservableList<AnchorPane> listOFBoxs = FXCollections.observableArrayList();
        try {
            ArrayList<String> projectArrayList = new ArrayList<>();
            String sql = "SELECT project_name From project";
            result = con.prepareStatement(sql).executeQuery();
            while (result.next()) {
                projectArrayList.add(result.getString(1));
            }
            String project_name;
            for (int i = 0; i < projectArrayList.size(); i++) {
                project_name = projectArrayList.get(i);

                ProjectBox projectBox = new ProjectBox(getProjectID(project_name), project_name);

                projectBox.getPane().setOnMouseClicked(goToProject(projectBox.getTextField()));
                projectBox.getTextField().setOnMouseClicked(goToProject(projectBox.getTextField()));

                projectBox.getEdit().setOnMouseClicked(editProject(projectBox.getTextField(),
                        projectBox.getCheck(), projectBox.getEdit(), projectBox.getProject()));

                projectBox.getCheck().setOnMouseClicked(checkAction());

                projectBox.getTrash().setOnMouseClicked(deleteProject(projectBox.getBox(),
                        projectBox.getProject().getProject_id()));
                listOFBoxs.add(projectBox);
            }
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".initProjects()");
        }
        return listOFBoxs;
    }

    public static EventHandler<Event> goToProject(TextField textField) {
        EventHandler<Event> ev = (Event event) -> {
            try {
                DoCheck();

                colorsdatabase.homepage.HomepageController.BorderPane_Static.setCenter(Project_root);
                colorsdatabase.project.ProjectController.projectName_pointer.setText(textField.getText());
                colorsdatabase.project.ProjectController.flowpane_project.getChildren().clear();
                colorsdatabase.project.ProjectController.flowpane_project.getChildren().addAll(
                        ProjectController.initColors(ProjectController.getProjectID(textField.getText())));
            } catch (Exception ex) {
                Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".goToProject(textField:" + textField + ")");
            }
        };
        return ev;
    }

    private EventHandler<Event> editProject(TextField textField, FontAwesomeIconView check, FontAwesomeIconView edit, Project project) {
        EventHandler<Event> ev = (Event event) -> {
            try {
                edit_TextField = textField;
                edit_Project = project;
                edit_check = check;
                edit_icon = edit;

                if (!check.isVisible()) {
                    edit_Project.setProject_name(textField.getText());
                    edit.setIcon(FontAwesomeIcon.CLOSE);
                    edit.setStyle("-fx-fill:red;");
                    textField.setEditable(true);
                    textField.setOnMouseClicked(null);
                    textField.setStyle("-fx-cursor: text;"
                            + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);"
                            + "-fx-background-color: white;");
                    check.setVisible(true);
                } else {
                    edit.setStyle("-fx-fill:gray;");
                    textField.setEditable(false);
                    textField.setText(edit_Project.getProject_name());
                    textField.setOnMouseClicked(goToProject(textField));
                    textField.setStyle("-fx-cursor: hand;"
                            + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.0), 10, 0, 0, 0);"
                            + " -fx-background-color: transparent;");
                    check.setVisible(false);
                    edit.setIcon(FontAwesomeIcon.EDIT);
                }
            } catch (Exception ex) {
                Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".editProject(project:" + project + ", textfield:" + textField + ")");
            }
        };
        return ev;
    }

    private EventHandler<Event> checkAction() {
        EventHandler<Event> ev = (Event event) -> {
            DoCheck();
        };
        return ev;
    }

    public static void DoCheck() {
        try {
            if (edit_TextField == null || edit_check == null || edit_Project == null) {
                return;
            }
            edit_TextField.setEditable(false);
            edit_TextField.setOnMouseClicked(goToProject(edit_TextField));
            edit_TextField.setStyle("-fx-cursor: hand;"
                    + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.0), 10, 0, 0, 0);"
                    + " -fx-background-color: transparent;");
            edit_check.setVisible(false);
            edit_icon.setIcon(FontAwesomeIcon.EDIT);
            edit_icon.setStyle("-fx-fill:gray;");

            if (edit_TextField.getText().equals("") || edit_TextField.getText().contains("'")) {
                edit_TextField.setText(edit_Project.getProject_name());
            } else {
                edit_Project = new Project(edit_Project.getProject_id(), edit_TextField.getText());
                String sqlString = "Update project set project_name = '" + edit_Project.getProject_name()
                        + "' where project_id =" + edit_Project.getProject_id();
                PreparedStatement prepareStatement = con.prepareStatement(sqlString);
                prepareStatement.executeUpdate();
            }
            edit_TextField = null;
            edit_check = null;
            edit_Project = null;
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".DoCheck(edit_Project: " + edit_Project + ", edit_TextField: " + edit_TextField + ")");
        }
    }

    private EventHandler<Event> deleteProject(AnchorPane anchorPane, int project_id) {
        EventHandler<Event> ev = (Event event) -> {
            try {
                DoCheck();

                flowpane_pointer.getChildren().remove(anchorPane);
                String sqlString = "DELETE FROM project WHERE  project_id =" + project_id;
                PreparedStatement prepareStatement = con.prepareStatement(sqlString);
                prepareStatement.executeUpdate();
                num_projects_pointer.setText(getProjectsNumber());
            } catch (Exception ex) {
                Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".deleteProject(project_id: " + project_id + ")");
            }
        };
        return ev;
    }

    @FXML
    public void add(Event e) {
        if (projectNameField.getText().trim().equals("")) {
            return;
        }
        DoCheck();
        addProject(projectNameField.getText());
        projectNameField.setText("");
        flowpane_instance.getChildren().clear();
        flowpane_instance.getChildren().addAll(initProjects());
        num_projects_pointer.setText(getProjectsNumber());
    }

    public void addProject(String project_name) {
        String sqlString = "INSERT INTO project (project_name) values (?)";
        try {
            PreparedStatement prepareStatement = con.prepareStatement(sqlString);
            prepareStatement.setString(1, project_name);
            prepareStatement.execute();
        } catch (SQLException ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".addProject(project_name: " + project_name + ")");
        }
    }

    // ========== Database ==========
    public static void connectToDataBase() {
        try {
            if (con == null) {
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:database.db");
                con.prepareStatement(" PRAGMA foreign_keys = ON").execute();
            }
        } catch (Exception ex) {
            con = null;
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".connectToDataBase()");
        }
    }

    private void createTables() {
        String project = "CREATE TABLE if not EXISTS project ( project_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " project_name VARCHAR(255) UNIQUE NOT NULL)";
        String color = "CREATE TABLE if not EXISTS color ( project_id INTEGER references project(project_id) ON DELETE CASCADE ,"
                + " variable_name VARCHAR(255) PRIMARY KEY , hex_color VARCHAR(255) NOT NULL)";
        createTable(color);
        createTable(project);
    }

    private boolean createTable(String sql) {
        try {
            return con.prepareStatement(sql).execute();
        } catch (Exception ex) {
            Logger.log(Logger.Level.ERROR, "Exception[" + ex + "] in " + CLASS_NAME + ".createTable()");
            return false;
        }
    }

    @FXML
    private void scrollHandler(ScrollEvent event) {
        double deltaY = event.getDeltaY() * 4;
        double width = sp.getContent().getBoundsInLocal().getWidth();
        double vvalue = sp.getVvalue();
        sp.setVvalue(vvalue + -deltaY / width);
    }

}
