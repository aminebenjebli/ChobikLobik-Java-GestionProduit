package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Avis;
import models.Gerant;
import models.Plat;
import services.AvisServices;
import services.PlatServices;

import java.sql.SQLException;

public class AvisController {

    @FXML
    private ComboBox<Gerant> comboGerant;
    @FXML
    private ComboBox<Plat> comboPlat;
    @FXML
    private TextArea txtDescription;
    @FXML
    private ComboBox<Integer> comboEtoile;
    @FXML
    private Button btnSubmit;

    private PlatServices platServices;
    private AvisServices avisServices;

    @FXML
    public void initialize() {
        platServices = new PlatServices();
        avisServices = new AvisServices();
        loadGerants();
        comboEtoile.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        comboGerant.setOnAction(event -> loadPlatsForGerant());
    }

    private void loadGerants() {
        try {
            comboGerant.setItems(FXCollections.observableArrayList(platServices.fetchGerants()));
        } catch (SQLException e) {
            // Handle errors here
        }
    }

    private void loadPlatsForGerant() {
        Gerant selectedGerant = comboGerant.getValue();
        if (selectedGerant != null) {
            try {
                comboPlat.setItems(FXCollections.observableArrayList(platServices.fetchPlatsByGerant(selectedGerant.getId())));
            } catch (SQLException e) {
                // Handle errors here
            }
        }
    }

    @FXML
    private void handleSubmitAction() {
        Plat selectedPlat = comboPlat.getValue();
        Integer rating = comboEtoile.getValue();
        String description = txtDescription.getText();

        if (selectedPlat == null || rating == null || description.isEmpty()) {
            // Handle validation error
            return;
        }

        Avis avis = new Avis();
        avis.setIdPlat(selectedPlat.getIdPlat());
        avis.setIdResto(selectedPlat.getIdRestaurant()); // Assuming you have this information in Plat
        avis.setIdClient(1); // Set to 1 as specified
        avis.setDescription(description);
        avis.setEtoile(rating);

        try {
            avisServices.ajouterAvis(avis);
            // Success notification
        } catch (SQLException e) {
            // Handle errors here
        }
    }
}
