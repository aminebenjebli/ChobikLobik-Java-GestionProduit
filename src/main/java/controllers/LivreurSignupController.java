package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Livreur;
import services.LivreurService;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;


public class LivreurSignupController {

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ComboBox<String> cmbAdresse;
    @FXML
    private ComboBox<String> cmbVehicule;
    @FXML
    private ComboBox<String> cmbZoneLivraison;
    @FXML
    private TextField txtNumTel;
    @FXML
    private Label lblStatus;

    private LivreurService livreurService = new LivreurService();

    @FXML
    public void initialize() {
        try {
            populateComboBoxes();
        } catch (SQLException e) {
            e.printStackTrace();
            lblStatus.setText("Failed to load data: " + e.getMessage());
        }
    }

    private void populateComboBoxes() throws SQLException {
        cmbAdresse.getItems().clear();
        cmbAdresse.getItems().addAll(livreurService.getCities());
        cmbVehicule.getItems().clear();
        cmbVehicule.getItems().addAll(livreurService.getVehiculeTypes());
        cmbZoneLivraison.getItems().clear();
        cmbZoneLivraison.getItems().addAll(livreurService.getDeliveryZones());
    }


    @FXML
    private void handleSignup() {
        try {
            int idVehicule = livreurService.findVehiculeIdByType(cmbVehicule.getValue());
            int idZoneLivraison = livreurService.findZoneLivraisonIdByName(cmbZoneLivraison.getValue());
            Livreur newLivreur = new Livreur(
                    txtNom.getText(),
                    txtPrenom.getText(),
                    txtEmail.getText(),
                    txtPassword.getText(),
                    cmbAdresse.getValue(),
                    idVehicule,
                    idZoneLivraison,
                    Integer.parseInt(txtNumTel.getText())
            );

            livreurService.ajouterLivreur(newLivreur);
            SessionManager.setCurrentLivreur(newLivreur);

            lblStatus.setText("Signup successful.");
            redirectToLogin();

        } catch (SQLException e) {
            lblStatus.setText("Signup failed: " + e.getMessage());
        } catch (NumberFormatException e) {
            lblStatus.setText("Invalid phone number format.");
        } catch (Exception e) {
            lblStatus.setText("Failed to redirect: " + e.getMessage());
        }
    }
    private void redirectToLogin() {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Scene loginScene = new Scene(loginPage);
            Stage currentStage = (Stage) lblStatus.getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            lblStatus.setText("Failed to redirect: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            lblStatus.setText("Failed to redirect: " + e.getMessage());
        }
    }

    public void handleBackAction(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/Showroom.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
