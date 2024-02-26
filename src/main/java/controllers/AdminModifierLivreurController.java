package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import models.Livreur;
import services.LivreurService;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import java.util.List;

public class AdminModifierLivreurController {

    @FXML
    private ComboBox<String> comboAdresse;
    @FXML
    private ComboBox<String> comboVehicule;
    @FXML
    private ComboBox<String> comboZone;
    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private TextField txtEmail;
    @FXML
    private Button updateButton;

    private LivreurService livreurService = new LivreurService();
    private Livreur currentLivreur;

    public void initialize() {
        try {
            List<String> vehiculeNames = livreurService.getAllVehiculeNames();
            List<String> zoneNames = livreurService.getAllZoneLivraisonNames();
            List<String> cityNames = livreurService.getCities();
            comboVehicule.getItems().addAll(vehiculeNames);
            comboZone.getItems().addAll(zoneNames);
            comboAdresse.getItems().addAll(cityNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setLivreur(Livreur livreur) {
        this.currentLivreur = livreur;
        txtNom.setText(livreur.getNom());
        txtPrenom.setText(livreur.getPrenom());
        txtEmail.setText(livreur.getEmail());
        // Additional logic to select the city in the combo box based on the livreur's city
        // This is left as an exercise, as it requires knowledge of how the city is stored in the Livreur model
        comboVehicule.getSelectionModel().select(livreur.getVehiculeName());
        comboZone.getSelectionModel().select(livreur.getZoneLivraisonName());
    }

    @FXML
    private void handleUpdateAction() {
        if (txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Nom and Prenom cannot be empty.");
            return;
        }

        currentLivreur.setNom(txtNom.getText());
        currentLivreur.setPrenom(txtPrenom.getText());
        currentLivreur.setEmail(txtEmail.getText());
        String cityName = comboAdresse.getValue();

        String selectedVehicule = comboVehicule.getValue();
        String selectedZone = comboZone.getValue();

        if (selectedVehicule == null || selectedZone == null || cityName == null || cityName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please select Vehicule, Zone, and City.");
            return;
        }

        try {
            int idVehicule = livreurService.getVehiculeIdByName(selectedVehicule);
            int idZoneLivraison = livreurService.getZoneIdByName(selectedZone);
            currentLivreur.setIdVehicule(idVehicule);
            currentLivreur.setIdZoneLivraison(idZoneLivraison);
            livreurService.updateLivreur(currentLivreur, cityName);
            showAlert(Alert.AlertType.INFORMATION, "Update Successful", "Livreur updated successfully.");
            updateButton.getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update livreur.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
