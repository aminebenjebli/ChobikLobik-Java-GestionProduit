package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import models.Client;
import services.Clientservices;
import utils.SessionManager;
import java.sql.SQLException;

public class UserProfileController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> addressComboBox;
    @FXML private TextField numTelField;
    @FXML private Button goBackButton;

    private Clientservices clientService = new Clientservices();
    private Client currentUser;

    public void initialize() {
        loadCurrentUserProfile();
        loadCitiesIntoComboBox();
    }

    private void loadCurrentUserProfile() {
        currentUser = SessionManager.getCurrentClient();
        if (currentUser != null) {
            populateFields();
        } else {
            showAlert("Login Required", "No user is currently logged in.");
        }
    }

    private void populateFields() {
        nomField.setText(currentUser.getNom());
        prenomField.setText(currentUser.getPrenom());
        emailField.setText(currentUser.getEmail());
        addressComboBox.setValue(currentUser.getAdresse());
        numTelField.setText(String.valueOf(currentUser.getNumTel()));
    }

    private void loadCitiesIntoComboBox() {
        try {
            addressComboBox.getItems().setAll(clientService.getCityNames());
        } catch (SQLException e) {
            showAlert("Error", "Failed to load city names.");
        }
    }

    @FXML
    private void handleUpdateProfileAction(ActionEvent event) {
        boolean isUpdated = false;

        String nom = nomField.getText();
        if (!nom.equals(currentUser.getNom())) {
            currentUser.setNom(nom);
            isUpdated = true;
        }

        String prenom = prenomField.getText();
        if (!prenom.equals(currentUser.getPrenom())) {
            currentUser.setPrenom(prenom);
            isUpdated = true;
        }

        String email = emailField.getText();
        if (!email.equals(currentUser.getEmail())) {
            currentUser.setEmail(email);
            isUpdated = true;
        }

        String password = passwordField.getText();
        if (!password.isEmpty()) { // Assuming password field is empty if not changed. Adjust logic as needed.
            currentUser.setPassword(password); // Remember to hash the password if necessary
            isUpdated = true;
        }

        String adresse = addressComboBox.getValue();
        if (adresse != null && !adresse.equals(currentUser.getAdresse())) {
            currentUser.setAdresse(adresse);
            isUpdated = true;
        }

        try {
            String numTelStr = numTelField.getText();
            int numTel = numTelStr.isEmpty() ? currentUser.getNumTel() : Integer.parseInt(numTelStr);
            if (numTel != currentUser.getNumTel()) {
                currentUser.setNumTel(numTel);
                isUpdated = true;
            }

            if (isUpdated) {
                clientService.modifier(currentUser);
                showAlert("Success", "Profile updated successfully.");
            } else {
                showAlert("Information", "No changes detected.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid phone number format.");
        } catch (SQLException e) {
            showAlert("Error", "Failed to update profile.");
        }
    }


    @FXML
    private void handleDeleteAccountAction(ActionEvent event) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Account Deletion");
        confirmAlert.setContentText("Are you sure you want to delete your account?");

        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);

        confirmAlert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        confirmAlert.showAndWait().ifPresent(type -> {
            if (type == buttonTypeYes) {
                try {
                    clientService.supprimer(currentUser.getId());
                    SessionManager.clearSession(); // Clear the session
                    showAlert("Account Deleted", "Your account has been successfully deleted.");
                    redirectToLogin();
                } catch (SQLException e) {
                    showAlert("Error", "Failed to delete account.");
                }
            } else if (type == buttonTypeNo) {
                redirectToClientHomePage();
            }
        });
    }

    private void redirectToClientHomePage() {
        try {
            Stage stage = (Stage) goBackButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/ClientHomePage.fxml")); // Ensure this path is correct
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to redirect to Client Homepage.");
        }
    }

    @FXML private void handleGoBackAction(ActionEvent event) {
        try {
            Stage stage = (Stage) goBackButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/ClientHomePage.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void redirectToLogin() {
        try {
            Stage stage = (Stage) goBackButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/Showroom.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to redirect to Showroom.");
        }
    }
}
