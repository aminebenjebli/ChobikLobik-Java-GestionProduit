package models;

import java.util.Date;

public class Offre {
    private int id, id_resto, id_plat;
    private int percentage;
    private String image;
    private Date date_debut;
    private Date date_fin;

    public Offre(int id, int id_resto, int id_plat, int percentage, String image, Date date_debut, Date date_fin) {
        this.id = id;
        this.id_resto = id_resto;
        this.id_plat = id_plat;
        this.percentage = percentage;
        this.image = image;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
    }

    public Offre(int id_resto, int id_plat, int percentage, String image, Date date_debut, Date date_fin) {
        this.id_resto = id_resto;
        this.id_plat = id_plat;
        this.percentage = percentage;
        this.image = image;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
    }

    public Offre() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_resto() {
        return id_resto;
    }

    public void setId_resto(int id_resto) {
        this.id_resto = id_resto;
    }

    public int getId_plat() {
        return id_plat;
    }

    public void setId_plat(int id_plat) {
        this.id_plat = id_plat;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(Date date_debut) {
        this.date_debut = date_debut;
    }

    public Date getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
    }

    @Override
    public String toString() {
        return "Offre{" +
                "id=" + id +
                ", id_resto=" + id_resto +
                ", id_plat=" + id_plat +
                ", percentage=" + percentage +
                ", image='" + image + '\'' +
                ", date_debut=" + date_debut +
                ", date_fin=" + date_fin +
                '}';
    }
}
