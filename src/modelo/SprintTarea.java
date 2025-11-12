package modelo;

public class SprintTarea {
    private int id;
    private int idSprint;
    private int idTarea;

    public SprintTarea() {}

    public SprintTarea(int idSprint, int idTarea) {
        this.idSprint = idSprint;
        this.idTarea = idTarea;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdSprint() { return idSprint; }
    public void setIdSprint(int idSprint) { this.idSprint = idSprint; }
    public int getIdTarea() { return idTarea; }
    public void setIdTarea(int idTarea) { this.idTarea = idTarea; }
}
