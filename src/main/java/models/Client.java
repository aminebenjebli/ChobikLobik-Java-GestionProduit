package models;
import java.util.Date;

public class Client {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String username;
    private String adresse;
    private int numTel;
    private Date date;

    public Client(int id, String nom, String prenom, String email, String password, String username, String adresse, int numTel, Date date) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.username = username;
        this.adresse = adresse;
        this.numTel = numTel;
        this.date = date;
    }

    public Client(String nom, String prenom, String email, String password, String username, String adresse, int numTel,  Date date) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.username = username;
        this.adresse = adresse;
        this.numTel = numTel;
        this.date = date;

    }

    public Client() {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getNumTel() {
        return numTel;
    }

    public void setNumTel(int numTel) {
        this.numTel = numTel;
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
