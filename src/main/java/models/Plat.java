package models;

public class Plat {
    private int id_plat , id, id_restaurant;
    private String nom , image ;
    private String  description ;
    private float prix ;
    private Category category; // Add this line to include a reference to Category

    // Constructors, getters, and setters

    // Add getter and setter for category
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public Plat() {
    }
    public int getIdPlat() {
        return id_plat;
    }
    public int getIdRestaurant() {
        return id_restaurant;
    }
    public Plat(int id_plat, int id, int id_restaurant, String nom, String image, String description, float prix) {
        this.id_plat = id_plat;
        this.id = id;
        this.id_restaurant = id_restaurant;
        this.nom = nom;
        this.image = image;
        this.description = description;
        this.prix = prix;
    }

    public Plat(int id, int id_restaurant, String nom, String image, String description, float prix) {
        this.id = id;
        this.id_restaurant = id_restaurant;
        this.nom = nom;
        this.image = image;
        this.description = description;
        this.prix = prix;
    }

    public int getId_plat() {
        return id_plat;
    }

    public void setId_plat(int id_plat) {
        this.id_plat = id_plat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_restaurant() {
        return id_restaurant;
    }

    public void setId_restaurant(int id_restaurant) {
        this.id_restaurant = id_restaurant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return this.nom; // Assuming 'nom' is the name of the dish
    }
}
