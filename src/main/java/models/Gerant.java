package models;

import javafx.beans.property.*;
import java.sql.Timestamp;

public class Gerant {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty username = new SimpleStringProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private StringProperty document = new SimpleStringProperty();
    private StringProperty image = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private StringProperty adresse = new SimpleStringProperty();
    private IntegerProperty cityId = new SimpleIntegerProperty(); // Reference to City
    private ObjectProperty<Timestamp> date = new SimpleObjectProperty<>();

    public Gerant(String username, String name, String description, String document, String image, String email, String password, String adresse, int cityId) {
        this.username.set(username);
        this.name.set(name);
        this.description.set(description);
        this.document.set(document);
        this.image.set(image);
        this.email.set(email);
        this.password.set(password);
        this.adresse.set(adresse);
        this.cityId.set(cityId); // Set city ID
    }
    public Gerant(String name, String description, String image) {
        this.name.set(name);
        this.description.set(description);
        this.image.set(image);
    }
    public Gerant(int id, String name, String description, String image) {
        this.id.set(id);
        this.name.set(name);
        this.description.set(description);
        this.image.set(image);
    }


    public Gerant() {

    }

    // Getters and setters for cityId
    public final void setCityId(int value) {
        cityId.set(value);
    }

    public final int getCityId() {
        return cityId.get();
    }

    public IntegerProperty cityIdProperty() {
        return cityId;
    }
    // Adresse Property


    public Gerant(String username, String name, String description, String document, String image, String email, String password, String adresse) {
        this.username.set(username);
        this.name.set(name);
        this.description.set(description);
        this.document.set(document);
        this.image.set(image);
        this.email.set(email);
        this.password.set(password);
        this.adresse.set(adresse);
    }
    public String getAdresse() { return adresse.get(); }
    public void setAdresse(String adresse) { this.adresse.set(adresse); }


    public Gerant(int id, String username, String name, String description, String document, String image, String email, String password, Timestamp date) {
        this.id.set(id);
        this.username.set(username);
        this.name.set(name);
        this.description.set(description);
        this.document.set(document);
        this.image.set(image);
        this.email.set(email);
        this.password.set(password);
        this.date.set(date);
    }

    public Gerant(String username, String name, String description, String document, String image, String email, String password, Timestamp date) {
        this.username.set(username);
        this.name.set(name);
        this.description.set(description);
        this.document.set(document);
        this.image.set(image);
        this.email.set(email);
        this.password.set(password);
        this.date.set(date);
    }

    public Gerant(int id, String username, String name, String description, String document,
                  String image, String email, String password, String adresse, Timestamp date) {
        this.id.set(id);
        this.username.set(username);
        this.name.set(name);
        this.description.set(description);
        this.document.set(document);
        this.image.set(image);
        this.email.set(email);
        this.password.set(password);
        this.adresse.set(adresse); // Set the address
        this.date.set(date);
    }

    // ID Property
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    // Username Property
    public String getUsername() { return username.get(); }
    public void setUsername(String username) { this.username.set(username); }
    public StringProperty usernameProperty() { return username; }

    // Name Property
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }

    // Description Property
    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    public StringProperty descriptionProperty() { return description; }

    // Document Property
    public String getDocument() { return document.get(); }
    public void setDocument(String document) { this.document.set(document); }
    public StringProperty documentProperty() { return document; }

    // Image Property
    public String getImage() { return image.get(); }
    public void setImage(String image) { this.image.set(image); }
    public StringProperty imageProperty() { return image; }

    // Email Property
    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }


    // Password Property
    public String getPassword() { return password.get(); }
    public void setPassword(String password) { this.password.set(password); }
    public StringProperty passwordProperty() { return password; }

    // Date Property
    public Timestamp getDate() { return date.get(); }
    public void setDate(Timestamp date) { this.date.set(date); }
    public ObjectProperty<Timestamp> dateProperty() { return date; }

}
