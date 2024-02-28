package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Category;
import models.Offre;
import models.Plat;
import services.OffreServices;
import services.PlatServices;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class AddOffer {
    @FXML
    private Button consulterOff;

    @FXML
    private Text actionStatus;
    @FXML
    private TextField txtPercentage, txtNewPrice;
    @FXML
    private ComboBox<Plat> comboPlat;
    @FXML
    private Button btnAjouter;
    @FXML
    private DatePicker dpDateDebut, dpDateFin;
    @FXML
    private ComboBox<Integer> comboHourDebut, comboMinuteDebut, comboHourFin, comboMinuteFin;



    private OffreServices offreServices = new OffreServices();
    private PlatServices platServices = new PlatServices();

    @FXML
    public void initialize() {
        offreServices = new OffreServices();
        try {
            ObservableList<Plat> platList = FXCollections.observableArrayList(platServices.afficher());
            comboPlat.setItems(platList);
        } catch (SQLException e) {
            actionStatus.setText("Erreur lors du chargement des Plats: " + e.getMessage());
        }
        comboPlat.setCellFactory(lv -> new ListCell<Plat>() {
            @Override
            protected void updateItem(Plat plat, boolean empty) {
                super.updateItem(plat, empty);
                setText(empty ? null : plat.getNom());
            }
        });
        comboPlat.setButtonCell(new ListCell<Plat>() {
            @Override
            protected void updateItem(Plat plat, boolean empty) {
                super.updateItem(plat, empty);
                setText(empty ? null : plat.getNom());
            }
        });

    }

    @FXML
    void handleAjouter() {
        try {
            Plat selectedPlat = comboPlat.getValue();
            if (selectedPlat == null) {
                showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner un Plats s'il vous plait.");
                return;
            }
            String prixText = txtNewPrice.getText();
            // Validate input fields
            if (txtPercentage.getText().isEmpty() || txtNewPrice.getText().isEmpty() ||
                    dpDateDebut.getValue() == null || dpDateFin.getValue() == null ||
                    comboPlat.getSelectionModel().getSelectedItem() == null )
                     {
                // Show an error message or handle the error as you see fit
                showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez remplir tous les champs.");
                return;
            }

            //controle de saisie for price
            float prix = Float.parseFloat(prixText);
            if (prix <= 0) {
                showAlert(Alert.AlertType.WARNING, "Avertissement", "Le prix doit être supérieur à zéro.");
                return;
            }

            Offre newoffre = new Offre();
            newoffre.setPercentage(Integer.parseInt(txtPercentage.getText()));
            newoffre.setNew_price(Float.parseFloat(txtNewPrice.getText()));
            newoffre.setId_plat(comboPlat.getSelectionModel().getSelectedItem().getId_plat());

            offreServices.ajouter(newoffre,selectedPlat.getId_plat());
            // Set dates to the beginning of the day
            LocalDateTime dateDebut = dpDateDebut.getValue().atStartOfDay();
            LocalDateTime dateFin = dpDateFin.getValue().atStartOfDay();

            // Convert LocalDateTime to java.util.Date
            newoffre.setDate_debut(Date.from(dateDebut.atZone(ZoneId.systemDefault()).toInstant()));
            newoffre.setDate_fin(Date.from(dateFin.atZone(ZoneId.systemDefault()).toInstant()));


            // Call the service to add the offer


            showAlert(Alert.AlertType.INFORMATION, "Succès", "Plat ajouté avec succès.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format de nombre invalide pour le prix.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du plat: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur: " + e.getMessage());
        }

    }

    @FXML
    void consulterOffre(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConsulterOffre.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Vos Offres!");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading file ");
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