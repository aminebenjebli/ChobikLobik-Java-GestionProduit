package models;

import java.time.LocalDate;

public class Subscription {
    private int id;
    private int idResto;
    private String status;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int abonnementTypeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdResto() {
        return idResto;
    }

    public void setIdResto(int idResto) {
        this.idResto = idResto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public int getAbonnementTypeId() {
        return abonnementTypeId;
    }

    public void setAbonnementTypeId(int abonnementTypeId) {
        this.abonnementTypeId = abonnementTypeId;
    }
}
