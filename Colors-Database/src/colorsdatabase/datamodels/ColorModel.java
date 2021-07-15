package colorsdatabase.datamodels;

public class ColorModel {

    private String variable_name;
    private String hex_color;
    private int project_id;

    public ColorModel(String variable_name, String hex_color, int project_id) {
        this.variable_name = variable_name;
        this.hex_color = hex_color;
        this.project_id = project_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getVariable_name() {
        return variable_name;
    }

    public void setVariable_name(String variable_name) {
        this.variable_name = variable_name;
    }

    public String getHex_color() {
        return hex_color;
    }

    public void setHex_color(String hex_color) {
        this.hex_color = hex_color;
    }

    @Override
    public String toString() {
        return "Color{" + "project_id=" + project_id + ", variable_name=" + variable_name + ", hex_color=" + hex_color + '}';
    }

}
