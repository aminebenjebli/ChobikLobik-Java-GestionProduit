package models;

import java.time.LocalDate;

public class Reservation {
    private int id;
    private int idClient;
    private int idRestaurant;
    private int idTable;
    private LocalDate dateReservation;
    private String clientName;
    private String clientSurname;
    private String status;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientSurname() {
        return clientSurname;
    }

    public void setClientSurname(String clientSurname) {
        this.clientSurname = clientSurname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Constructor
    public Reservation(int id, int idClient, int idRestaurant, int idTable, LocalDate dateReservation) {
        this.id = id;
        this.idClient = idClient;
        this.idRestaurant = idRestaurant;
        this.idTable = idTable;
        this.dateReservation = dateReservation;
    }
    public Reservation(int id, int idClient, int idRestaurant, int idTable, LocalDate dateReservation, String clientName, String clientSurname, String status) {
        this.id = id;
        this.idClient = idClient;
        this.idRestaurant = idRestaurant;
        this.idTable = idTable;
        this.dateReservation = dateReservation;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(int idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }
}
