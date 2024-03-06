package models;

import java.util.Date;

public class Offre {
    private int id;
    private int id_resto;
    private int id_plat;
    private int pourcentage;
    private Date date_debut;
    private Date date_fin;
    private float new_price;
    private String platName;

    public Offre() {
    }

    public Offre(int id, double pourcentage, Date dateDebut, Date dateFin, int idPlat, double newPrice, String platName) {
        this.id = id;
        this.pourcentage = (int) pourcentage;
        this.date_debut = dateDebut;
        this.date_fin = dateFin;
        this.id_plat = idPlat;
        this.new_price = (float) newPrice;
        this.platName = platName;
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

    public int getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(int pourcentage) {
        if (pourcentage >= 0 && pourcentage <= 100) {
            this.pourcentage = pourcentage;
        } else {
            throw new IllegalArgumentException("Pourcentage must be between 0 and 100.");
        }
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

    public float getNew_price() {
        return new_price;
    }

    public void setNew_price(float new_price) {
        if (new_price >= 0) {
            this.new_price = new_price;
        } else {
            throw new IllegalArgumentException("New price must be positive.");
        }
    }

    public String getPlatName() {
        return platName;
    }

    public void setPlatName(String platName) {
        this.platName = platName;
    }

    public float getOriginalPrice() {
        return new_price / (1 - (pourcentage / 100.0f));
    }

    @Override
    public String toString() {
        return "Offre{" +
                "id=" + id +
                ", id_resto=" + id_resto +
                ", id_plat=" + id_plat +
                ", pourcentage=" + pourcentage +
                ", date_debut=" + date_debut +
                ", date_fin=" + date_fin +
                ", new_price=" + new_price +
                ", platName='" + platName + '\'' +
                '}';
    }
}
