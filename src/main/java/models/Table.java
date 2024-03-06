package models;

public class Table {
    private int id;
    private int nombre_p;
    private String status;
    private int id_resto; // Assuming this is the ID of the logged-in restaurant

    // Constructor
    public Table(int id, int nombre_p, String status, int id_resto) {
        this.id = id;
        this.nombre_p = nombre_p;
        this.status = status;
        this.id_resto = id_resto;
    }


    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNombre_p() {
        return nombre_p;
    }

    public void setNombre_p(int nombre_p) {
        this.nombre_p = nombre_p;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId_resto() {
        return id_resto;
    }

    public void setId_resto(int id_resto) {
        this.id_resto = id_resto;
    }
    @Override
    public String toString() {
        return "Table Number " + this.id + " : " + this.nombre_p;
    }

}
