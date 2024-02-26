package models;

import java.sql.Timestamp;

public class Livreur {
    private String vehiculeName;
    private String zoneLivraisonName;
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String adresse;
    private int idVehicule;
    private int idZoneLivraison;
    private int numTel;
    private Timestamp date;

    public Livreur(int id, String nom, String prenom, String email, String password, String adresse, int idVehicule, int idZoneLivraison, int numTel, Timestamp date) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.adresse = adresse;
        this.idVehicule = idVehicule;
        this.idZoneLivraison = idZoneLivraison;
        this.numTel = numTel;
        this.date = date;
    }
    public Livreur( String nom, String prenom, String email, String password, String adresse, int idVehicule, int idZoneLivraison, int numTel, Timestamp date) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.adresse = adresse;
        this.idVehicule = idVehicule;
        this.idZoneLivraison = idZoneLivraison;
        this.numTel = numTel;
        this.date = date;
    }
    public Livreur(String nom, String prenom, String email, String password, String adresse, int idVehicule, int idZoneLivraison, int numTel) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.adresse = adresse;
        this.idVehicule = idVehicule;
        this.idZoneLivraison = idZoneLivraison;
        this.numTel = numTel;
        // The 'date' field will be automatically set by the database, so it's not included here
    }

    public Livreur() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public int getIdZoneLivraison() {
        return idZoneLivraison;
    }

    public void setIdZoneLivraison(int idZoneLivraison) {
        this.idZoneLivraison = idZoneLivraison;
    }

    public int getNumTel() {
        return numTel;
    }

    public void setNumTel(int numTel) {
        this.numTel = numTel;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getVehiculeName() {
        return vehiculeName;
    }

    public void setVehiculeName(String vehiculeName) {
        this.vehiculeName = vehiculeName;
    }

    public String getZoneLivraisonName() {
        return zoneLivraisonName;
    }

    public void setZoneLivraisonName(String zoneLivraisonName) {
        this.zoneLivraisonName = zoneLivraisonName;
    }

}
