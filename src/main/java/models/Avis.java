package models;

public class Avis {
    private int idPlat;
    private int idResto;
    private int idClient;
    private String description;
    private int etoile;

    // Default constructor
    public Avis() {
    }

    // Parameterized constructor
    public Avis(int idPlat, int idResto, int idClient, String description, int etoile) {
        this.idPlat = idPlat;
        this.idResto = idResto;
        this.idClient = idClient;
        this.description = description;
        this.etoile = etoile;
    }

    // Getters and setters
    public int getIdPlat() {
        return idPlat;
    }

    public void setIdPlat(int idPlat) {
        this.idPlat = idPlat;
    }

    public int getIdResto() {
        return idResto;
    }

    public void setIdResto(int idResto) {
        this.idResto = idResto;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEtoile() {
        return etoile;
    }

    public void setEtoile(int etoile) {
        this.etoile = etoile;
    }
}
