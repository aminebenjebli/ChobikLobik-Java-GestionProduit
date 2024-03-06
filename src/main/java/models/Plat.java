package models;

public class Plat {
    private int id_plat;
    private String nom;
    private int id_category; // This is the id of the category the plat belongs to.
    private int id_restaurant; // This is the id of the restaurant the plat belongs to.
    private String description;
    private float prix;
    private String image;
    private String categoryName; // Assuming you've added this field
    private String restaurantName;
    private Category category; // Add this line to include a reference to Category

    // Constructors, getters, and setters

    // Add getter and setter for category
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    // Default constructor
    public Plat() {
    }
    public Plat(int id_plat, String nom, int id_category, int id_restaurant, String description, float prix, String image) {
        this.id_plat = id_plat;
        this.nom = nom;
        this.id_category = id_category;
        this.id_restaurant = id_restaurant;
        this.description = description;
        this.prix = prix;
        this.image = image;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    // Constructor with all fields
    public Plat(int id_plat, int id_category, int id_restaurant, String nom, String image, String description, float prix) {
        this.id_plat = id_plat;
        this.id_category = id_category;
        this.id_restaurant = id_restaurant;
        this.nom = nom;
        this.image = image;
        this.description = description;
        this.prix = prix;
    }

    public Plat(int id_plat, String nom, String categoryName, String restaurantName, String description, float prix, String image) {
        this.id_plat = id_plat;
        this.nom = nom;
        this.categoryName = categoryName; // Set the category name
        this.restaurantName = restaurantName; // Set the restaurant name
        this.description = description;
        this.prix = prix;
        this.image = image;
    }
    // Getters and setters
    public int getIdPlat() {
        return id_plat;
    }

    public void setIdPlat(int id_plat) {
        this.id_plat = id_plat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId_category() {
        return this.id_category;
    }


    public void setIdCategory(int id_category) {
        this.id_category = id_category;
    }

    public int getIdRestaurant() {
        return id_restaurant;
    }

    public void setIdRestaurant(int id_restaurant) {
        this.id_restaurant = id_restaurant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    // Ensure you also have a getCategoryName method
    public void setPrix(double prix) {
        this.prix = (float) prix; // If you still want to store it as float internally
        // Or change the internal storage of prix to double as well.
    }


    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // ToString method for displaying object information
    @Override
    public String toString() {
        return "Plat{" +
                "id_plat=" + id_plat +
                ", nom='" + nom + '\'' +
                ", id_category=" + id_category +
                ", id_restaurant=" + id_restaurant +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", image='" + image + '\'' +
                '}';
    }
    public Plat(String nom, String description, float prix, String image, int id_category) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.image = image;
        this.id_category = id_category;
        // id_restaurant is not set here - you need to set it based on your application context
    }

}
